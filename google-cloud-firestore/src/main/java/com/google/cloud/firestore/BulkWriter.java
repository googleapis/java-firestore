/*
 * Copyright 2020 Google LLC
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

import com.google.api.core.ApiAsyncFunction;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.core.BetaApi;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.rpc.StatusCode.Code;
import com.google.cloud.firestore.v1.FirestoreSettings;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** A Firestore BulkWriter that can be used to perform a large number of writes in parallel. */
@BetaApi
public final class BulkWriter implements AutoCloseable {
  /**
   * A callback set by `addWriteResultListener()` to be run every time an operation successfully
   * completes.
   */
  public interface WriteResultCallback {
    /**
     * @param documentReference The document the operation was performed on.
     * @param result The result of the operation.
     */
    void onResult(DocumentReference documentReference, WriteResult result);
  };

  /**
   * A callback set by `addWriteErrorListener()` to be run every time an operation fails and
   * determines if an operation should be retried.
   */
  public interface WriteErrorCallback {
    /**
     * @param error The error object from the failed BulkWriter operation attempt.
     * @return Whether to retry the operation or not.
     */
    boolean onError(BulkWriterException error);
  }

  private interface BulkWriterOperationCallback {
    ApiFuture<WriteResult> apply(BulkCommitBatch batch);
  }

  enum OperationType {
    CREATE,
    SET,
    UPDATE,
    DELETE
  }
  /** The maximum number of writes that can be in a single batch. */
  public static final int MAX_BATCH_SIZE = 20;

  /**
   * The maximum number of retries that will be attempted with backoff before stopping all retry
   * attempts.
   */
  public static final int MAX_RETRY_ATTEMPTS = 10;

  /**
   * The starting maximum number of operations per second as allowed by the 500/50/5 rule.
   *
   * @see <a href=https://cloud.google.com/datastore/docs/best-practices#ramping_up_traffic>Ramping
   *     up traffic</a>
   */
  static final int DEFAULT_STARTING_MAXIMUM_OPS_PER_SECOND = 500;

  /**
   * The rate by which to increase the capacity as specified by the 500/50/5 rule.
   *
   * @see <a href=https://cloud.google.com/datastore/docs/best-practices#ramping_up_traffic>Ramping
   *     up traffic</a>
   */
  private static final double RATE_LIMITER_MULTIPLIER = 1.5;

  /**
   * How often the operations per second capacity should increase in milliseconds as specified by
   * the 500/50/5 rule.
   *
   * @see <a href=https://cloud.google.com/datastore/docs/best-practices#ramping_up_traffic>Ramping
   *     up traffic</a>
   */
  private static final int RATE_LIMITER_MULTIPLIER_MILLIS = 5 * 60 * 1000;

  private static final WriteResultCallback DEFAULT_SUCCESS_LISTENER =
      new WriteResultCallback() {
        public void onResult(DocumentReference documentReference, WriteResult result) {}
      };

  private static final WriteErrorCallback DEFAULT_ERROR_LISTENER =
      new WriteErrorCallback() {
        public boolean onError(BulkWriterException error) {
          if (error.getFailedAttempts() == MAX_RETRY_ATTEMPTS) {
            return false;
          }
          Set<Code> codes = FirestoreSettings.newBuilder().batchWriteSettings().getRetryableCodes();
          for (Code code : codes) {
            if (code.equals(Code.valueOf(error.getStatus().getCode().name()))) {
              return true;
            }
          }
          return false;
        }
      };

  private static final Logger logger = Logger.getLogger(BulkWriter.class.getName());

  /** The maximum number of writes that can be in a single batch. */
  private int maxBatchSize = MAX_BATCH_SIZE;

  /** A queue of batches to be written. */
  private final List<BulkCommitBatch> batchQueue = new ArrayList<>();

  /** A queue of batches containing operations that need to be retried. */
  private final List<BulkCommitBatch> retryBatchQueue = new ArrayList<>();

  /**
   * A set of futures that represent pending BulkWriter operations. Each future is completed when
   * the BulkWriter operation resolves with either a WriteResult or failure after all applicable
   * retries are performed. This set of futures is used by flush() to track when enqueued writes are
   * completed.
   */
  private final Set<ApiFuture<Void>> pendingOperations = new HashSet<>();

  /**
   * A list of futures that represent sent batches. Each future is completed when the batch's
   * response is received. This includes batches from both the batchQueue and retryBatchQueue.
   */
  private final Set<ApiFuture<Void>> pendingBatches = new HashSet<>();

  /** Whether this BulkWriter instance is closed. Once closed, it cannot be opened again. */
  private boolean closed = false;

  /** Rate limiter used to throttle requests as per the 500/50/5 rule. */
  private final RateLimiter rateLimiter;

  private WriteResultCallback successListener = DEFAULT_SUCCESS_LISTENER;

  private WriteErrorCallback errorListener = DEFAULT_ERROR_LISTENER;

  private Executor successExecutor;
  private Executor errorExecutor;

  /**
   * Used to track when writes are enqueued. The user handler executors cannot be changed after a
   * write has been enqueued.
   */
  private boolean writesEnqueued = false;

  private final FirestoreImpl firestore;

  // Executor used to run all BulkWriter operations. BulkWriter uses its own executor since we
  // don't want to block a gax/grpc executor while running user error and success callbacks.
  private final ScheduledExecutorService bulkWriterExecutor;

  BulkWriter(FirestoreImpl firestore, BulkWriterOptions options) {
    this.firestore = firestore;
    this.bulkWriterExecutor =
        options.getExecutor() != null
            ? options.getExecutor()
            : Executors.newSingleThreadScheduledExecutor();
    this.successExecutor = MoreExecutors.directExecutor();
    this.errorExecutor = MoreExecutors.directExecutor();

    if (!options.getThrottlingEnabled()) {
      this.rateLimiter =
          new RateLimiter(
              Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    } else {
      double startingRate = DEFAULT_STARTING_MAXIMUM_OPS_PER_SECOND;
      double maxRate = Double.POSITIVE_INFINITY;

      if (options.getInitialOpsPerSecond() != null) {
        startingRate = options.getInitialOpsPerSecond();
      }

      if (options.getMaxOpsPerSecond() != null) {
        maxRate = options.getMaxOpsPerSecond();
      }

      // The initial validation step ensures that the maxOpsPerSecond is greater than
      // initialOpsPerSecond. If this inequality is true, that means initialOpsPerSecond was not
      // set and maxOpsPerSecond is less than the default starting rate.
      if (maxRate < startingRate) {
        startingRate = maxRate;
      }

      // Ensure that the batch size is not larger than the number of allowed
      // operations per second.
      if (startingRate < maxBatchSize) {
        this.maxBatchSize = (int) startingRate;
      }

      this.rateLimiter =
          new RateLimiter(
              (int) startingRate,
              RATE_LIMITER_MULTIPLIER,
              RATE_LIMITER_MULTIPLIER_MILLIS,
              (int) maxRate);
    }
  }

  /**
   * Create a document with the provided data. This single operation will fail if a document exists
   * at its location.
   *
   * @param documentReference A reference to the document to be created.
   * @param fields A map of the fields and values for the document.
   * @return An ApiFuture containing the result of the write. Contains a {@link BulkWriterException}
   *     if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> create(
      @Nonnull final DocumentReference documentReference,
      @Nonnull final Map<String, Object> fields) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.CREATE,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.create(documentReference, fields);
          }
        });
  }

  /**
   * Create a document with the provided data. This single operation will fail if a document exists
   * at its location.
   *
   * @param documentReference A reference to the document to be created.
   * @param pojo The POJO that will be used to populate the document contents.
   * @return An ApiFuture containing the result of the write. Contains a {@link BulkWriterException}
   *     if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> create(
      @Nonnull final DocumentReference documentReference, @Nonnull final Object pojo) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.CREATE,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.create(documentReference, pojo);
          }
        });
  }

  /**
   * Delete a document from the database.
   *
   * @param documentReference The DocumentReference to delete.
   * @return An ApiFuture containing a sentinel value (Timestamp(0)) for the delete operation.
   *     Contains a {@link BulkWriterException} if the delete fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> delete(@Nonnull final DocumentReference documentReference) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.DELETE,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.delete(documentReference);
          }
        });
  }

  /**
   * Delete a document from the database.
   *
   * @param documentReference The DocumentReference to delete.
   * @param precondition Precondition to enforce for this delete.
   * @return An ApiFuture containing a sentinel value (Timestamp(0)) for the delete operation.
   *     Contains a {@link BulkWriterException} if the delete fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> delete(
      @Nonnull final DocumentReference documentReference,
      @Nonnull final Precondition precondition) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.DELETE,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.delete(documentReference, precondition);
          }
        });
  }

  /**
   * Write to the document referred to by the provided DocumentReference. If the document does not
   * exist yet, it will be created.
   *
   * @param documentReference A reference to the document to be set.
   * @param fields A map of the fields and values for the document.
   * @return An ApiFuture containing the result of the write. Contains a {@link BulkWriterException}
   *     if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> set(
      @Nonnull final DocumentReference documentReference,
      @Nonnull final Map<String, Object> fields) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.SET,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.set(documentReference, fields);
          }
        });
  }

  /**
   * Write to the document referred to by the provided DocumentReference. If the document does not
   * exist yet, it will be created. If you pass a {@link SetOptions}, the provided data can be
   * merged into an existing document.
   *
   * @param documentReference A reference to the document to be set.
   * @param fields A map of the fields and values for the document.
   * @param options An object to configure the set behavior.
   * @return An ApiFuture containing the result of the write. Contains a {@link BulkWriterException}
   *     if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> set(
      @Nonnull final DocumentReference documentReference,
      @Nonnull final Map<String, Object> fields,
      @Nonnull final SetOptions options) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.SET,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.set(documentReference, fields, options);
          }
        });
  }

  /**
   * Write to the document referred to by the provided DocumentReference. If the document does not
   * exist yet, it will be created. If you pass a {@link SetOptions}, the provided data can be
   * merged into an existing document.
   *
   * @param documentReference A reference to the document to be set.
   * @param pojo The POJO that will be used to populate the document contents.
   * @param options An object to configure the set behavior.
   * @return An ApiFuture containing the result of the write. Contains a {@link BulkWriterException}
   *     if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> set(
      @Nonnull final DocumentReference documentReference,
      @Nonnull final Object pojo,
      @Nonnull final SetOptions options) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.SET,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.set(documentReference, pojo, options);
          }
        });
  }

  /**
   * Write to the document referred to by the provided DocumentReference. If the document does not
   * exist yet, it will be created.
   *
   * @param documentReference A reference to the document to be set.
   * @param pojo The POJO that will be used to populate the document contents.
   * @return An ApiFuture containing the result of the write. Contains a {@link BulkWriterException}
   *     if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> set(
      @Nonnull final DocumentReference documentReference, @Nonnull final Object pojo) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.SET,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.set(documentReference, pojo);
          }
        });
  }

  /**
   * Update fields of the document referred to by the provided {@link DocumentReference}. If the
   * document doesn't yet exist, the update will fail.
   *
   * <p>The update() method accepts either an object with field paths encoded as keys and field
   * values encoded as values, or a variable number of arguments that alternate between field paths
   * and field values. Nested fields can be updated by providing dot-separated field path strings or
   * by providing FieldPath objects.
   *
   * @param documentReference A reference to the document to be updated.
   * @param fields A map of the fields and values for the document.
   * @return An ApiFuture containing the result of the write. Contains a {@link BulkWriterException}
   *     if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> update(
      @Nonnull final DocumentReference documentReference,
      @Nonnull final Map<String, Object> fields) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.UPDATE,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.update(documentReference, fields);
          }
        });
  }

  /**
   * Update fields of the document referred to by the provided {@link DocumentReference}. If the
   * document doesn't yet exist, the update will fail.
   *
   * <p>The update() method accepts either an object with field paths encoded as keys and field
   * values encoded as values, or a variable number of arguments that alternate between field paths
   * and field values. Nested fields can be updated by providing dot-separated field path strings or
   * by providing FieldPath objects.
   *
   * @param documentReference A reference to the document to be updated.
   * @param fields A map of the fields and values for the document.
   * @param precondition Precondition to enforce on this update.
   * @return An ApiFuture containing the result of the write. Contains a {@link BulkWriterException}
   *     if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> update(
      @Nonnull final DocumentReference documentReference,
      @Nonnull final Map<String, Object> fields,
      @Nonnull final Precondition precondition) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.UPDATE,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.update(documentReference, fields, precondition);
          }
        });
  }

  /**
   * Update fields of the document referred to by the provided {@link DocumentReference}. If the
   * document doesn't yet exist, the update will fail.
   *
   * <p>The update() method accepts either an object with field paths encoded as keys and field
   * values encoded as values, or a variable number of arguments that alternate between field paths
   * and field values. Nested fields can be updated by providing dot-separated field path strings or
   * by providing FieldPath objects.
   *
   * @param documentReference A reference to the document to be updated.
   * @param field The first field to set.
   * @param value The first value to set.
   * @param moreFieldsAndValues String and Object pairs with more fields to be set.
   * @return An ApiFuture containing the result of the write. Contains a {@link BulkWriterException}
   *     if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> update(
      @Nonnull final DocumentReference documentReference,
      @Nonnull final String field,
      @Nullable final Object value,
      final Object... moreFieldsAndValues) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.UPDATE,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.update(documentReference, field, value, moreFieldsAndValues);
          }
        });
  }

  /**
   * Update fields of the document referred to by the provided {@link DocumentReference}. If the
   * document doesn't yet exist, the update will fail.
   *
   * <p>The update() method accepts either an object with field paths encoded as keys and field
   * values encoded as values, or a variable number of arguments that alternate between field paths
   * and field values. Nested fields can be updated by providing dot-separated field path strings or
   * by providing FieldPath objects.
   *
   * @param documentReference A reference to the document to be updated.
   * @param fieldPath The first field to set.
   * @param value The first value to set.
   * @param moreFieldsAndValues String and Object pairs with more fields to be set.
   * @return An ApiFuture containing the result of the write. Contains a {@link BulkWriterException}
   *     if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> update(
      @Nonnull final DocumentReference documentReference,
      @Nonnull final FieldPath fieldPath,
      @Nullable final Object value,
      final Object... moreFieldsAndValues) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.UPDATE,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.update(documentReference, fieldPath, value, moreFieldsAndValues);
          }
        });
  }

  /**
   * Update fields of the document referred to by the provided {@link DocumentReference}. If the
   * document doesn't yet exist, the update will fail.
   *
   * <p>The update() method accepts either an object with field paths encoded as keys and field
   * values encoded as values, or a variable number of arguments that alternate between field paths
   * and field values. Nested fields can be updated by providing dot-separated field path strings or
   * by providing FieldPath objects.
   *
   * @param documentReference A reference to the document to be updated.
   * @param field The first field to set.
   * @param value The first value to set.
   * @param moreFieldsAndValues String and Object pairs with more fields to be set.
   * @return An ApiFuture containing the result of the write. Contains a {@link BulkWriterException}
   *     if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> update(
      @Nonnull final DocumentReference documentReference,
      @Nonnull final Precondition precondition,
      @Nonnull final String field,
      @Nullable final Object value,
      final Object... moreFieldsAndValues) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.UPDATE,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.update(documentReference, precondition, field, value, moreFieldsAndValues);
          }
        });
  }

  /**
   * Update fields of the document referred to by the provided {@link DocumentReference}. If the
   * document doesn't yet exist, the update will fail.
   *
   * <p>The update() method accepts either an object with field paths encoded as keys and field
   * values encoded as values, or a variable number of arguments that alternate between field paths
   * and field values. Nested fields can be updated by providing dot-separated field path strings or
   * by providing FieldPath objects.
   *
   * @param documentReference A reference to the document to be updated.
   * @param precondition Precondition to enforce on this update.
   * @param fieldPath The first field to set.
   * @param value The first value to set.
   * @param moreFieldsAndValues String and Object pairs with more fields to be set.
   * @return An ApiFuture containing the result of the write. Contains a {@link BulkWriterException}
   *     if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> update(
      @Nonnull final DocumentReference documentReference,
      @Nonnull final Precondition precondition,
      @Nonnull final FieldPath fieldPath,
      @Nullable final Object value,
      final Object... moreFieldsAndValues) {
    verifyNotClosed();
    return executeWrite(
        documentReference,
        OperationType.UPDATE,
        new BulkWriterOperationCallback() {
          public ApiFuture<WriteResult> apply(BulkCommitBatch batch) {
            return batch.update(
                documentReference, precondition, fieldPath, value, moreFieldsAndValues);
          }
        });
  }

  /**
   * Schedules the provided write operation and runs the user success callback when the write result
   * is obtained.
   */
  private ApiFuture<WriteResult> executeWrite(
      final DocumentReference documentReference,
      final OperationType operationType,
      final BulkWriterOperationCallback operationCallback) {
    final SettableApiFuture<Void> operationCompletedFuture = SettableApiFuture.create();
    writesEnqueued = true;

    ApiFuture<WriteResult> writeResultApiFuture =
        ApiFutures.transformAsync(
            enqueueWrite(documentReference, operationType, operationCallback),
            new ApiAsyncFunction<WriteResult, WriteResult>() {
              public ApiFuture<WriteResult> apply(final WriteResult result)
                  throws ExecutionException, InterruptedException {

                invokeUserSuccessCallback(documentReference, result).get();
                return ApiFutures.immediateFuture(result);
              }
            },
            // This should always run on the `bulkWriterExecutor`. However, we use
            // `directExecutor()`
            // here to ensure that the transform code runs immediately after without yielding. Using
            // `bulkWriterExecutor` instead means that the transform is scheduled after other work
            // already on the executor, which causes BulkWriter to deadlock.
            MoreExecutors.directExecutor());

    writeResultApiFuture.addListener(
        new Runnable() {
          public void run() {
            pendingOperations.remove(operationCompletedFuture);
            operationCompletedFuture.set(null);
          }
        },
        // This should always run on the `bulkWriterExecutor`. However, we use `directExecutor()`
        // here to ensure that the transform code runs immediately after without yielding. Using
        // `bulkWriterExecutor` instead means that the transform is scheduled after other work
        // already on the executor, which causes BulkWriter to deadlock.
        MoreExecutors.directExecutor());

    pendingOperations.add(operationCompletedFuture);
    return writeResultApiFuture;
  }

  /** Used to schedule the write enqueue logic onto the bulkWriterExecutor. */
  private ApiFuture<WriteResult> enqueueWrite(
      final DocumentReference documentReference,
      final OperationType operationType,
      final BulkWriterOperationCallback operationCallback) {
    return ApiFutures.transformAsync(
        ApiFutures.immediateFuture(null),
        new ApiAsyncFunction<Object, WriteResult>() {
          public ApiFuture<WriteResult> apply(Object o) throws Exception {
            return enqueueWriteHelper(documentReference, operationType, operationCallback, 0);
          }
        },
        bulkWriterExecutor);
  }

  /**
   * Adds the write to the appropriate batch queue and performs the bulkCommit. This helper method
   * also catches failures and applies the user error callback and retrying if necessary.
   */
  private ApiFuture<WriteResult> enqueueWriteHelper(
      final DocumentReference documentReference,
      final OperationType operationType,
      final BulkWriterOperationCallback operationCallback,
      final int failedAttempts) {
    List<BulkCommitBatch> operationBatchQueue = failedAttempts > 0 ? retryBatchQueue : batchQueue;
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference, operationBatchQueue);

    // Send ready batches if this is the first attempt. Subsequent retry batches are scheduled
    // after the initial batch returns.
    if (failedAttempts == 0) {
      sendReadyBatches(operationBatchQueue);
    }

    return ApiFutures.catchingAsync(
        operationCallback.apply(bulkCommitBatch),
        FirestoreException.class,
        new ApiAsyncFunction<FirestoreException, WriteResult>() {
          public ApiFuture<WriteResult> apply(FirestoreException exception)
              throws BulkWriterException, ExecutionException, InterruptedException {
            BulkWriterException bulkWriterException =
                new BulkWriterException(
                    exception.getStatus(),
                    exception.getMessage(),
                    documentReference,
                    operationType,
                    failedAttempts);

            boolean shouldRetry = invokeUserErrorCallback(bulkWriterException).get();
            logger.log(
                Level.INFO,
                String.format(
                    "Ran error callback on document: %s, error code: %d, shouldRetry: %b",
                    documentReference.getPath(),
                    exception.getStatus().getCode().value(),
                    shouldRetry));
            if (!shouldRetry) {
              throw bulkWriterException;
            } else {
              return enqueueWriteHelper(
                  documentReference, operationType, operationCallback, failedAttempts + 1);
            }
          }
        },
        // This code should always be on the bulkWriterExecutor. However, we use `directExecutor()`
        // here to ensure that the transform code runs immediately after without yielding. Using
        // `bulkWriterExecutor` instead means that the transform is scheduled after other work
        // already on the executor, which causes BulkWriter to deadlock..
        MoreExecutors.directExecutor());
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

  /**
   * Commits all writes that have been enqueued up to this point in parallel.
   *
   * <p>Returns an ApiFuture that completes when all currently queued operations have been
   * committed. The ApiFuture will never return an error since the results for each individual
   * operation are conveyed via their individual ApiFutures.
   *
   * <p>The ApiFuture completes immediately if there are no pending writes. Otherwise, the ApiFuture
   * waits for all previously issued writes, but it does not wait for writes that were added after
   * the method is called. If you want to wait for additional writes, call `flush()` again.
   *
   * @return An ApiFuture that completes when all enqueued writes up to this point have been
   *     committed.
   */
  @Nonnull
  public ApiFuture<Void> flush() {
    verifyNotClosed();
    final ArrayList<ApiFuture<Void>> pendingOperationsAtFlush =
        Lists.newArrayList(pendingOperations);
    return ApiFutures.transformAsync(
        ApiFutures.immediateFuture(null),
        new ApiAsyncFunction<Object, Void>() {
          public ApiFuture<Void> apply(Object o) throws Exception {
            return performFlush(pendingOperationsAtFlush);
          }
        },
        bulkWriterExecutor);
  }

  private ApiFuture<Void> performFlush(final List<ApiFuture<Void>> pendingOperations) {
    for (BulkCommitBatch batch : batchQueue) {
      batch.markReadyToSend();
    }

    // Send all scheduled operations on the BatchQueue first.
    sendReadyBatches(batchQueue);
    final SettableApiFuture<Void> batchQueueFlushComplete = SettableApiFuture.create();
    final SettableApiFuture<Void> flushComplete = SettableApiFuture.create();
    ApiFutures.successfulAsList(pendingBatches)
        .addListener(
            new Runnable() {
              public void run() {
                batchQueueFlushComplete.set(null);
              }
            },
            MoreExecutors.directExecutor());

    // Afterwards, send all accumulated retry operations. Wait until the retryBatchQueue is cleared.
    // This way, operations scheduled after flush() will not be sent until the retries are done.
    batchQueueFlushComplete.addListener(
        new Runnable() {
          public void run() {
            for (BulkCommitBatch batch : retryBatchQueue) {
              batch.markReadyToSend();
            }
            sendReadyBatches(retryBatchQueue);
          }
        },
        MoreExecutors.directExecutor());

    ApiFutures.successfulAsList(pendingOperations)
        .addListener(
            new Runnable() {
              public void run() {
                flushComplete.set(null);
              }
            },
            MoreExecutors.directExecutor());

    return flushComplete;
  }

  /**
   * Commits all enqueued writes and marks the BulkWriter instance as closed.
   *
   * <p>After calling `close()`, calling any method will return an error. Any retries scheduled with
   * `addWriteErrorListener()` will be run before `close()` completes.
   *
   * <p>This method completes when there are no more pending writes. Calling this method will send
   * all requests.
   */
  public void close() throws InterruptedException, ExecutionException {
    ApiFuture<Void> flushFuture = flush();
    closed = true;
    flushFuture.get();
  }

  private void verifyNotClosed() {
    if (this.closed) {
      throw new IllegalStateException("BulkWriter has already been closed.");
    }
  }

  /**
   * Attaches a listener that is run every time a BulkWriter operation successfully completes. The
   * listener will be run before `close()` completes.
   *
   * <p>For example, see the sample code: <code>
   *   BulkWriter bulkWriter = firestore.bulkWriter();
   *   bulkWriter.addWriteResultListener(
   *         (DocumentReference documentReference, WriteResult result) -> {
   *             System.out.println(
   *                 "Successfully executed write on document: "
   *                     + documentReference
   *                     + " at: "
   *                     + result.getUpdateTime());
   *           }
   *         );
   * </code>
   *
   * @param writeResultCallback A callback to be called every time a BulkWriter operation
   *     successfully completes.
   */
  public void addWriteResultListener(WriteResultCallback writeResultCallback) {
    successListener = writeResultCallback;
  }

  /**
   * Attaches a listener that is run every time a BulkWriter operation successfully completes.
   *
   * <p>The executor cannot be changed once writes have been enqueued onto the BulkWriter.
   *
   * <p>For example, see the sample code: <code>
   *   BulkWriter bulkWriter = firestore.bulkWriter();
   *   bulkWriter.addWriteResultListener(
   *         (DocumentReference documentReference, WriteResult result) -> {
   *             System.out.println(
   *                 "Successfully executed write on document: "
   *                     + documentReference
   *                     + " at: "
   *                     + result.getUpdateTime());
   *           }
   *         );
   * </code>
   *
   * @param executor The executor to run the provided callback on.
   * @param writeResultCallback A callback to be called every time a BulkWriter operation
   *     successfully completes.
   */
  public void addWriteResultListener(
      @Nonnull Executor executor, WriteResultCallback writeResultCallback) {
    if (writesEnqueued) {
      throw new IllegalStateException(
          "The executor cannot be changed once writes have been enqueued.");
    }
    successListener = writeResultCallback;
    successExecutor = executor;
  }

  /**
   * Attaches an error handler listener that is run every time a BulkWriter operation fails.
   *
   * <p>BulkWriter has a default error handler that retries UNAVAILABLE and ABORTED errors up to a
   * maximum of 10 failed attempts. When an error handler is specified, the default error handler
   * will be overwritten.
   *
   * <p>For example, see the sample code: <code>
   *   BulkWriter bulkWriter = firestore.bulkWriter();
   *   bulkWriter.addWriteErrorListener(
   *         (BulkWriterException error) -> {
   *           if (error.getStatus() == Status.UNAVAILABLE
   *             && error.getFailedAttempts() < MAX_RETRY_ATTEMPTS) {
   *             return true;
   *           } else {
   *             System.out.println("Failed write at document: " + error.getDocumentReference());
   *             return false;
   *           }
   *         }
   *       );
   * </code>
   *
   * @param onError A callback to be called every time a BulkWriter operation fails. Returning
   *     `true` will retry the operation. Returning `false` will stop the retry loop.
   */
  public void addWriteErrorListener(WriteErrorCallback onError) {
    errorListener = onError;
  }

  /**
   * Attaches an error handler listener that is run every time a BulkWriter operation fails.
   *
   * <p>The executor cannot be changed once writes have been enqueued onto the BulkWriter.
   *
   * <p>BulkWriter has a default error handler that retries UNAVAILABLE and ABORTED errors up to a
   * maximum of 10 failed attempts. When an error handler is specified, the default error handler
   * will be overwritten.
   *
   * <p>For example, see the sample code: <code>
   *   BulkWriter bulkWriter = firestore.bulkWriter();
   *   bulkWriter.addWriteErrorListener(
   *         (BulkWriterException error) -> {
   *           if (error.getStatus() == Status.UNAVAILABLE
   *             && error.getFailedAttempts() < MAX_RETRY_ATTEMPTS) {
   *             return true;
   *           } else {
   *             System.out.println("Failed write at document: " + error.getDocumentReference());
   *             return false;
   *           }
   *         }
   *       );
   * </code>
   *
   * @param executor The executor to run the provided callback on.
   * @param onError A callback to be called every time a BulkWriter operation fails. Returning
   *     `true` will retry the operation. Returning `false` will stop the retry loop.
   */
  public void addWriteErrorListener(@Nonnull Executor executor, WriteErrorCallback onError) {
    if (writesEnqueued) {
      throw new IllegalStateException(
          "The executor cannot be changed once writes have been enqueued.");
    }
    errorListener = onError;
    errorExecutor = executor;
  }

  /**
   * Return the first eligible batch that can hold a write to the provided reference, or creates one
   * if no eligible batches are found.
   */
  private BulkCommitBatch getEligibleBatch(
      DocumentReference documentReference, List<BulkCommitBatch> batchQueue) {
    if (batchQueue.size() > 0) {
      BulkCommitBatch lastBatch = batchQueue.get(batchQueue.size() - 1);
      if (lastBatch.isOpen() && !lastBatch.has(documentReference)) {
        return lastBatch;
      }
    }
    return createNewBatch(batchQueue);
  }

  /**
   * Creates a new batch and adds it to the BatchQueue. If there is already a batch enqueued, sends
   * the batch after a new one is created.
   */
  private BulkCommitBatch createNewBatch(List<BulkCommitBatch> batchQueue) {
    BulkCommitBatch newBatch = new BulkCommitBatch(firestore, maxBatchSize);

    if (batchQueue.size() > 0) {
      batchQueue.get(batchQueue.size() - 1).markReadyToSend();
    }
    batchQueue.add(newBatch);
    return newBatch;
  }

  /**
   * Attempts to send batches starting from the front of the provided batch queue until a batch
   * cannot be sent.
   *
   * <p>After a batch is complete, try sending batches again.
   */
  private void sendReadyBatches(final List<BulkCommitBatch> batchQueue) {
    int index = 0;
    while (index < batchQueue.size() && batchQueue.get(index).isReadyToSend()) {
      final BulkCommitBatch batch = batchQueue.get(index);

      // Future that completes when the current batch or its scheduling attempts completes.
      final SettableApiFuture<Void> batchCompletedFuture = SettableApiFuture.create();
      pendingBatches.add(batchCompletedFuture);

      // Send the batch if it is under the rate limit, or schedule another attempt after the
      // appropriate timeout.
      long delayMs = rateLimiter.getNextRequestDelayMs(batch.getPendingOperationCount());
      Preconditions.checkState(delayMs != -1, "Batch size should be under capacity");
      if (delayMs == 0) {
        sendBatch(batch, batchQueue, batchCompletedFuture);
      } else {
        bulkWriterExecutor.schedule(
            new Runnable() {
              @Override
              public void run() {
                sendReadyBatches(batchQueue);
                pendingBatches.remove(batchCompletedFuture);
                batchCompletedFuture.set(null);
              }
            },
            delayMs,
            TimeUnit.MILLISECONDS);
        break;
      }

      ++index;
    }
  }

  /**
   * Sends the provided batch and processes the results. After the batch is committed, sends the
   * next group of ready batches.
   */
  private void sendBatch(
      final BulkCommitBatch batch,
      final List<BulkCommitBatch> batchQueue,
      final SettableApiFuture<Void> batchCompletedFuture) {
    boolean success = rateLimiter.tryMakeRequest(batch.getPendingOperationCount());
    Preconditions.checkState(success, "Batch should be under rate limit to be sent.");

    ApiFuture<Void> commitFuture = bulkCommit(batch);
    commitFuture.addListener(
        new Runnable() {
          public void run() {
            boolean removed = batchQueue.remove(batch);
            Preconditions.checkState(
                removed, "The batch should be in the BatchQueue." + batchQueue.size());

            if (batchQueue.equals(retryBatchQueue)) {
              for (BulkCommitBatch batch : retryBatchQueue) {
                batch.markReadyToSend();
              }
            }

            pendingBatches.remove(batchCompletedFuture);
            batchCompletedFuture.set(null);

            sendReadyBatches(batchQueue);
          }
        },
        MoreExecutors.directExecutor());
  }

  private ApiFuture<Void> bulkCommit(final BulkCommitBatch batch) {
    return ApiFutures.transformAsync(
        ApiFutures.catchingAsync(
            batch.bulkCommit(),
            Exception.class,
            new ApiAsyncFunction<Exception, List<BatchWriteResult>>() {
              public ApiFuture<List<BatchWriteResult>> apply(Exception exception) {
                List<BatchWriteResult> results = new ArrayList<>();
                // If the BatchWrite RPC fails, map the exception to each individual result.
                for (int i = 0; i < batch.getPendingOperationCount(); ++i) {
                  results.add(new BatchWriteResult(null, exception));
                }
                return ApiFutures.immediateFuture(results);
              }
            },
            MoreExecutors.directExecutor()),
        new ApiAsyncFunction<List<BatchWriteResult>, Void>() {
          public ApiFuture<Void> apply(List<BatchWriteResult> results) throws Exception {
            batch.processResults(results);
            return ApiFutures.immediateFuture(null);
          }
        },
        MoreExecutors.directExecutor());
  }

  @VisibleForTesting
  void setMaxBatchSize(int size) {
    maxBatchSize = size;
  }

  @VisibleForTesting
  RateLimiter getRateLimiter() {
    return rateLimiter;
  }
}
