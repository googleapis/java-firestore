/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
  private final SettableApiFuture<WriteResult> operationFuture = SettableApiFuture.create();
  private final DocumentReference documentReference;
  private final BulkWriter.OperationType operationType;
  private final ApiFunction<BulkWriterOperation, Void> scheduleWriteCallback;
  private final Executor successExecutor;
  private final BulkWriter.WriteResultCallback successListener;
  private final Executor errorExecutor;
  private final BulkWriter.WriteErrorCallback errorListener;

  private int failedAttempts = 0;

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
    return operationFuture;
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

    final SettableApiFuture<Void> callbackFuture = SettableApiFuture.create();

    ApiFutures.addCallback(
        invokeUserErrorCallback(bulkWriterException),
        new ApiFutureCallback<Boolean>() {
          @Override
          public void onFailure(Throwable throwable) {
            operationFuture.setException(throwable);
            callbackFuture.set(null);
          }

          @Override
          public void onSuccess(Boolean shouldRetry) {
            if (shouldRetry) {
              scheduleWriteCallback.apply(BulkWriterOperation.this);
            } else {
              operationFuture.setException(bulkWriterException);
            }
            callbackFuture.set(null);
          }
        },
        MoreExecutors.directExecutor());

    return callbackFuture;
  }

  /** Callback invoked when the operation succeeds. */
  public ApiFuture<Void> onSuccess(final WriteResult result) {
    final SettableApiFuture<Void> callbackFuture = SettableApiFuture.create();
    ApiFutures.addCallback(
        invokeUserSuccessCallback(documentReference, result),
        new ApiFutureCallback<Void>() {
          @Override
          public void onFailure(Throwable throwable) {
            operationFuture.setException(throwable);
            callbackFuture.set(null);
          }

          @Override
          public void onSuccess(Void aVoid) {
            operationFuture.set(result);
            callbackFuture.set(null);
          }
        },
        MoreExecutors.directExecutor());
    return callbackFuture;
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
