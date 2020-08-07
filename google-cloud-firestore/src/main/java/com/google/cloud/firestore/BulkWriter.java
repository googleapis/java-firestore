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

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.core.SettableApiFuture;
import com.google.cloud.firestore.UpdateBuilder.BatchState;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** Used to represent a batch on the BatchQueue. */
class BulkCommitBatch extends UpdateBuilder<ApiFuture<WriteResult>> {

  BulkCommitBatch(FirestoreImpl firestore, int maxBatchSize) {
    super(firestore, maxBatchSize);
  }

  ApiFuture<WriteResult> wrapResult(ApiFuture<WriteResult> result) {
    return result;
  }

  @Override
  boolean allowDuplicateDocs() {
    return false;
  }
}

public class BulkWriter {
  /** The maximum number of writes that can be in a single batch. */
  public static final int MAX_BATCH_SIZE = 500;

  /**
   * The starting maximum number of operations per second as allowed by the 500/50/5 rule.
   *
   * @see <a href=https://cloud.google.com/datastore/docs/best-practices#ramping_up_traffic>Ramping
   *     up traffic</a>
   */
  private static final int STARTING_MAXIMUM_OPS_PER_SECOND = 500;

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

  private static final Logger logger = Logger.getLogger(BulkWriter.class.getName());

  /** The maximum number of writes that can be in a single batch. */
  private int maxBatchSize = MAX_BATCH_SIZE;

  /** A queue of batches to be written. */
  private final List<BulkCommitBatch> batchQueue = new ArrayList<>();

  /** Whether this BulkWriter instance is closed. Once closed, it cannot be opened again. */
  private boolean closed = false;

  /** Rate limiter used to throttle requests as per the 500/50/5 rule. */
  private final RateLimiter rateLimiter;

  private final FirestoreImpl firestore;

  BulkWriter(FirestoreImpl firestore, boolean enableThrottling) {
    this.firestore = firestore;
    if (enableThrottling) {
      rateLimiter =
          new RateLimiter(
              STARTING_MAXIMUM_OPS_PER_SECOND,
              RATE_LIMITER_MULTIPLIER,
              RATE_LIMITER_MULTIPLIER_MILLIS);
    } else {
      rateLimiter = new RateLimiter(Integer.MAX_VALUE, Double.MAX_VALUE, Integer.MAX_VALUE);
    }
  }

  /**
   * Create a document with the provided data. This single operation will fail if a document exists
   * at its location.
   *
   * @param documentReference A reference to the document to be created.
   * @param fields A map of the fields and values for the document.
   * @return An ApiFuture containing the result of the write. Contains an error if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> create(
      @Nonnull DocumentReference documentReference, @Nonnull Map<String, Object> fields) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future = bulkCommitBatch.create(documentReference, fields);
    sendReadyBatches();
    return future;
  }

  /**
   * Create a document with the provided data. This single operation will fail if a document exists
   * at its location.
   *
   * @param documentReference A reference to the document to be created.
   * @param pojo The POJO that will be used to populate the document contents.
   * @return An ApiFuture containing the result of the write. Contains an error if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> create(
      @Nonnull DocumentReference documentReference, @Nonnull Object pojo) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future = bulkCommitBatch.create(documentReference, pojo);
    sendReadyBatches();
    return future;
  }

  /**
   * Delete a document from the database.
   *
   * @param documentReference The DocumentReference to delete.
   * @return An ApiFuture containing the result of the delete. Contains an error if the delete
   *     fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> delete(@Nonnull DocumentReference documentReference) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future = bulkCommitBatch.delete(documentReference);
    sendReadyBatches();
    return future;
  }

  /**
   * Delete a document from the database.
   *
   * @param documentReference The DocumentReference to delete.
   * @param precondition Precondition to enforce for this delete.
   * @return An ApiFuture containing a sentinel value (Timestamp(0)) for the delete operation.
   *     Contains an error if the delete fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> delete(
      @Nonnull DocumentReference documentReference, @Nonnull Precondition precondition) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future = bulkCommitBatch.delete(documentReference, precondition);
    sendReadyBatches();
    return future;
  }

  /**
   * Write to the document referred to by the provided DocumentReference. If the document does not
   * exist yet, it will be created.
   *
   * @param documentReference A reference to the document to be set.
   * @param fields A map of the fields and values for the document.
   * @return An ApiFuture containing a sentinel value (Timestamp(0)) for the delete operation.
   *     Contains an error if the delete fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> set(
      @Nonnull DocumentReference documentReference, @Nonnull Map<String, Object> fields) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future = bulkCommitBatch.set(documentReference, fields);
    sendReadyBatches();
    return future;
  }

  /**
   * Write to the document referred to by the provided DocumentReference. If the document does not
   * exist yet, it will be created. If you pass a {@link SetOptions}, the provided data can be
   * merged into an existing document.
   *
   * @param documentReference A reference to the document to be set.
   * @param fields A map of the fields and values for the document.
   * @param options An object to configure the set behavior.
   * @return An ApiFuture containing the result of the write. Contains an error if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> set(
      @Nonnull DocumentReference documentReference,
      @Nonnull Map<String, Object> fields,
      @Nonnull SetOptions options) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future = bulkCommitBatch.set(documentReference, fields, options);
    sendReadyBatches();
    return future;
  }

  /**
   * Write to the document referred to by the provided DocumentReference. If the document does not
   * exist yet, it will be created. If you pass a {@link SetOptions}, the provided data can be
   * merged into an existing document.
   *
   * @param documentReference A reference to the document to be set.
   * @param pojo The POJO that will be used to populate the document contents.
   * @param options An object to configure the set behavior.
   * @return An ApiFuture containing the result of the write. Contains an error if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> set(
      @Nonnull DocumentReference documentReference, Object pojo, @Nonnull SetOptions options) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future = bulkCommitBatch.set(documentReference, pojo, options);
    sendReadyBatches();
    return future;
  }

  /**
   * Write to the document referred to by the provided DocumentReference. If the document does not
   * exist yet, it will be created.
   *
   * @param documentReference A reference to the document to be set.
   * @param pojo The POJO that will be used to populate the document contents.
   * @return An ApiFuture containing the result of the write. Contains an error if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> set(@Nonnull DocumentReference documentReference, Object pojo) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future = bulkCommitBatch.set(documentReference, pojo);
    sendReadyBatches();
    return future;
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
   * @return An ApiFuture containing the result of the write. Contains an error if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> update(
      @Nonnull DocumentReference documentReference, @Nonnull Map<String, Object> fields) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future = bulkCommitBatch.update(documentReference, fields);
    sendReadyBatches();
    return future;
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
   * @return An ApiFuture containing the result of the write. Contains an error if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> update(
      @Nonnull DocumentReference documentReference,
      @Nonnull Map<String, Object> fields,
      Precondition precondition) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future = bulkCommitBatch.update(documentReference, fields, precondition);
    sendReadyBatches();
    return future;
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
   * @return An ApiFuture containing the result of the write. Contains an error if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> update(
      @Nonnull DocumentReference documentReference,
      @Nonnull String field,
      @Nullable Object value,
      Object... moreFieldsAndValues) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future =
        bulkCommitBatch.update(documentReference, field, value, moreFieldsAndValues);
    sendReadyBatches();
    return future;
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
   * @return An ApiFuture containing the result of the write. Contains an error if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> update(
      @Nonnull DocumentReference documentReference,
      @Nonnull FieldPath fieldPath,
      @Nullable Object value,
      Object... moreFieldsAndValues) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future =
        bulkCommitBatch.update(documentReference, fieldPath, value, moreFieldsAndValues);
    sendReadyBatches();
    return future;
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
   * @return An ApiFuture containing the result of the write. Contains an error if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> update(
      @Nonnull DocumentReference documentReference,
      @Nonnull Precondition precondition,
      @Nonnull String field,
      @Nullable Object value,
      Object... moreFieldsAndValues) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future =
        bulkCommitBatch.update(documentReference, precondition, field, value, moreFieldsAndValues);
    sendReadyBatches();
    return future;
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
   * @return An ApiFuture containing the result of the write. Contains an error if the write fails.
   */
  @Nonnull
  public ApiFuture<WriteResult> update(
      @Nonnull DocumentReference documentReference,
      @Nonnull Precondition precondition,
      @Nonnull FieldPath fieldPath,
      @Nullable Object value,
      Object... moreFieldsAndValues) {
    verifyNotClosed();
    BulkCommitBatch bulkCommitBatch = getEligibleBatch(documentReference);
    ApiFuture<WriteResult> future =
        bulkCommitBatch.update(
            documentReference, precondition, fieldPath, value, moreFieldsAndValues);
    sendReadyBatches();
    return future;
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
    final SettableApiFuture<Void> flushComplete = SettableApiFuture.create();
    List<ApiFuture<Void>> writeFutures = new ArrayList<>();
    for (BulkCommitBatch batch : batchQueue) {
      writeFutures.add(batch.awaitBulkCommit());
    }
    sendReadyBatches();
    ApiFutures.allAsList(writeFutures)
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
   * <p>After calling `close()`, calling any method wil return an error.
   *
   * <p>Returns an ApiFuture that completes when there are no more pending writes. The ApiFuture
   * will never error. Calling this method will send all requests. The ApiFuture completes
   * immediately if there are no pending writes.
   *
   * @return An ApiFuture that completes when all enqueued writes up to this point have been
   *     committed.
   */
  @Nonnull
  public ApiFuture<Void> close() {
    ApiFuture<Void> flushFuture = flush();
    closed = true;
    return flushFuture;
  }

  private void verifyNotClosed() {
    if (this.closed) {
      throw new IllegalStateException("BulkWriter has already been closed.");
    }
  }

  /**
   * Return the first eligible batch that can hold a write to the provided reference, or creates one
   * if no eligible batches are found.
   */
  private BulkCommitBatch getEligibleBatch(DocumentReference documentReference) {
    if (batchQueue.size() > 0) {
      BulkCommitBatch lastBatch = batchQueue.get(batchQueue.size() - 1);
      if (lastBatch.getState() == UpdateBuilder.BatchState.OPEN
          && !lastBatch.getDocuments().contains(documentReference)) {
        return lastBatch;
      }
    }
    return createNewBatch();
  }

  /**
   * Creates a new batch and adds it to the BatchQueue. If there is already a batch enqueued, sends
   * the batch after a new one is created.
   */
  private BulkCommitBatch createNewBatch() {
    BulkCommitBatch newBatch = new BulkCommitBatch(firestore, maxBatchSize);

    if (batchQueue.size() > 0) {
      batchQueue.get(batchQueue.size() - 1).markReadyToSend();
      sendReadyBatches();
    }
    batchQueue.add(newBatch);
    return newBatch;
  }

  /**
   * Attempts to send batches starting from the front of the BatchQueue until a batch cannot be
   * sent.
   *
   * <p>After a batch is complete, try sending batches again.
   */
  private void sendReadyBatches() {
    List<BulkCommitBatch> unsentBatches =
        FluentIterable.from(batchQueue)
            .filter(
                new Predicate<BulkCommitBatch>() {
                  @Override
                  public boolean apply(BulkCommitBatch batch) {
                    return batch.getState() == UpdateBuilder.BatchState.READY_TO_SEND;
                  }
                })
            .toList();

    int index = 0;
    while (index < unsentBatches.size() && isBatchSendable(unsentBatches.get(index))) {
      final BulkCommitBatch batch = unsentBatches.get(index);

      // Send the batch if it is under the rate limit, or schedule another attempt after the
      // appropriate timeout.
      long delayMs = rateLimiter.getNextRequestDelayMs(batch.getOperationCount());
      Preconditions.checkState(delayMs != -1, "Batch size should be under capacity");
      if (delayMs == 0) {
        sendBatch(batch);
      } else {
        firestore
            .getClient()
            .getExecutor()
            .schedule(
                new Runnable() {
                  @Override
                  public void run() {
                    sendBatch(batch);
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
  private void sendBatch(final BulkCommitBatch batch) {
    boolean success = rateLimiter.tryMakeRequest(batch.getOperationCount());
    Preconditions.checkState(success, "Batch should be under rate limit to be sent.");
    try {
      final ApiFuture<List<BatchWriteResult>> commitFuture = batch.bulkCommit();
      commitFuture.addListener(
          new Runnable() {
            public void run() {
              try {
                batch.processResults(commitFuture.get(), null);
              } catch (Exception e) {
                batch.processResults(new ArrayList<BatchWriteResult>(), e);
              }
              // Remove the batch from BatchQueue after it has been processed.
              boolean removed = batchQueue.remove(batch);
              Preconditions.checkState(removed, "The batch should be in the BatchQueue.");
              sendReadyBatches();
            }
          },
          MoreExecutors.directExecutor());
    } catch (Exception e) {
      batch.processResults(new ArrayList<BatchWriteResult>(), e);
    }
  }

  /**
   * Checks that the provided batch is sendable. To be sendable, a batch must: (1) be marked as
   * READY_TO_SEND (2) not write to references that are currently in flight.
   */
  private boolean isBatchSendable(BulkCommitBatch batch) {
    if (!batch.getState().equals(UpdateBuilder.BatchState.READY_TO_SEND)) {
      return false;
    }

    for (final DocumentReference document : batch.getDocuments()) {
      boolean isRefInFlight =
          FluentIterable.from(batchQueue)
              .anyMatch(
                  new Predicate<BulkCommitBatch>() {
                    @Override
                    public boolean apply(BulkCommitBatch batch) {
                      return batch.getState().equals(BatchState.SENT)
                          && batch.getDocuments().contains(document);
                    }
                  });

      if (isRefInFlight) {
        logger.log(
            Level.WARNING,
            String.format(
                "Duplicate write to document %s detected. Writing to the same document multiple"
                    + " times will slow down BulkWriter. Write to unique documents in order to "
                    + "maximize throughput.",
                document));
        return false;
      }
    }

    return true;
  }

  @VisibleForTesting
  void setMaxBatchSize(int size) {
    maxBatchSize = size;
  }
}
