package com.google.cloud.firestore;

import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.core.SettableApiFuture;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Executor;

/**
 * Represents a single write for BulkWriter, encapsulating operation dispatch and error handling.
 */
class BulkWriterOperation {
  private final SettableApiFuture<WriteResult> future = SettableApiFuture.create();
  private final DocumentReference documentReference;
  private final BulkWriter.OperationType operationType;
  private final ApiFunction<BulkWriterOperation, Void> scheduleWriteCallback;
  private final Executor successExecutor;
  private final BulkWriter.WriteResultCallback successListener;
  private final Executor errorExecutor;
  private final BulkWriter.WriteErrorCallback errorListener;

  private int failedAttempts;

  /**
   * @param documentReference The document reference being written to.
   * @param operationType The type of operation that created this write.
   * @param scheduleWriteCallback The callback used to schedule a new write.
   * @param successExecutor The executor to run the user-provided success handler on.
   * @param successListener The user-provided success handler.
   * @param errorExecutor The executor to run the user-provided error handler on.
   * @param errorListener The user-provided error handler.
   */
  BulkWriterOperation(
      DocumentReference documentReference,
      BulkWriter.OperationType operationType,
      ApiFunction<BulkWriterOperation, Void> scheduleWriteCallback,
      Executor successExecutor,
      BulkWriter.WriteResultCallback successListener,
      Executor errorExecutor,
      BulkWriter.WriteErrorCallback errorListener) {
    this.documentReference = documentReference;
    this.operationType = operationType;
    this.scheduleWriteCallback = scheduleWriteCallback;
    this.successExecutor = successExecutor;
    this.successListener = successListener;
    this.errorExecutor = errorExecutor;
    this.errorListener = errorListener;
  }

  /**
   * Returns an ApiFuture that resolves when the operation completes (either successfully or failed
   * after all retry attempts are exhausted).
   */
  public ApiFuture<WriteResult> getFuture() {
    return future;
  }

  public DocumentReference getDocumentReference() {
    return documentReference;
  }

  /** Callback invoked when an operation attempt fails. */
  public ApiFuture<Void> onException(FirestoreException exception) {
    ++failedAttempts;

    final BulkWriterException bulkWriterException =
        new BulkWriterException(
            exception.getStatus(),
            exception.getMessage(),
            documentReference,
            operationType,
            failedAttempts);

    final SettableApiFuture<Void> future = SettableApiFuture.create();

    ApiFutures.addCallback(
        invokeUserErrorCallback(bulkWriterException),
        new ApiFutureCallback<Boolean>() {
          @Override
          public void onFailure(Throwable throwable) {
            BulkWriterOperation.this.future.setException(throwable);
            future.set(null);
          }

          @Override
          public void onSuccess(Boolean shouldRetry) {
            if (shouldRetry) {
              scheduleWriteCallback.apply(BulkWriterOperation.this);
            } else {
              BulkWriterOperation.this.future.setException(bulkWriterException);
            }
            future.set(null);
          }
        },
        MoreExecutors.directExecutor());

    return future;
  }

  /** Callback invoked when the operation succeeds. */
  public ApiFuture<Void> onSuccess(final WriteResult result) {
    ApiFuture<Void> future = invokeUserSuccessCallback(documentReference, result);
    ApiFutures.addCallback(
        future,
        new ApiFutureCallback<Void>() {
          @Override
          public void onFailure(Throwable throwable) {
            BulkWriterOperation.this.future.setException(throwable);
          }

          @Override
          public void onSuccess(Void aVoid) {
            BulkWriterOperation.this.future.set(result);
          }
        },
        MoreExecutors.directExecutor());
    return future;
  }

  /** Invokes the user error callback on the user callback executor and returns the result. */
  private SettableApiFuture<Boolean> invokeUserErrorCallback(final BulkWriterException error) {
    final SettableApiFuture<Boolean> callbackResult = SettableApiFuture.create();
    errorExecutor.execute(
        new Runnable() {
          @Override
          public void run() {
            try {
              boolean shouldRetry = errorListener.onError(error);
              callbackResult.set(shouldRetry);
            } catch (Exception e) {
              callbackResult.setException(e);
            }
          }
        });
    return callbackResult;
  }

  /** Invokes the user success callback on the user callback executor. */
  private ApiFuture<Void> invokeUserSuccessCallback(
      final DocumentReference documentReference, final WriteResult result) {
    final SettableApiFuture<Void> callbackResult = SettableApiFuture.create();
    successExecutor.execute(
        new Runnable() {
          @Override
          public void run() {
            try {
              successListener.onResult(documentReference, result);
              callbackResult.set(null);
            } catch (Exception e) {
              callbackResult.setException(e);
            }
          }
        });
    return callbackResult;
  }
}
