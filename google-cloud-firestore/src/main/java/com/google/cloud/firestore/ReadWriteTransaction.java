/*
 * Copyright 2017 Google LLC
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
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firestore.v1.BeginTransactionRequest;
import com.google.firestore.v1.BeginTransactionResponse;
import com.google.firestore.v1.RollbackRequest;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.opencensus.trace.Tracing;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A Transaction is passed to a Function to provide the methods to read and write data within the
 * transaction context.
 *
 * @see Firestore#runTransaction(Function)
 */
final class ReadWriteTransaction extends UpdateBuilder<ReadWriteTransaction>
    implements Transaction {

  private static final Logger LOGGER = Logger.getLogger(ReadWriteTransaction.class.getName());
  private static final String READ_BEFORE_WRITE_ERROR_MSG =
      "Firestore transactions require all reads to be executed before all writes";

  private ByteString transactionId;

  ReadWriteTransaction(
      FirestoreImpl firestore, @Nullable ReadWriteTransaction previousTransaction) {
    super(firestore);
    this.transactionId = previousTransaction != null ? previousTransaction.transactionId : null;
  }

  public boolean hasTransactionId() {
    return transactionId != null;
  }

  @Override
  ReadWriteTransaction wrapResult(int writeIndex) {
    return this;
  }

  /** Starts a transaction and obtains the transaction id. */
  ApiFuture<Void> begin() {
    Tracing.getTracer().getCurrentSpan().addAnnotation(TraceUtil.SPAN_NAME_BEGINTRANSACTION);
    BeginTransactionRequest.Builder beginTransaction = BeginTransactionRequest.newBuilder();
    beginTransaction.setDatabase(firestore.getDatabaseName());

    if (transactionId != null) {
      beginTransaction.getOptionsBuilder().getReadWriteBuilder().setRetryTransaction(transactionId);
    }

    ApiFuture<BeginTransactionResponse> transactionBeginFuture =
        firestore.sendRequest(
            beginTransaction.build(), firestore.getClient().beginTransactionCallable());

    return ApiFutures.transform(
        transactionBeginFuture,
        beginTransactionResponse -> {
          transactionId = beginTransactionResponse.getTransaction();
          return null;
        },
        MoreExecutors.directExecutor());
  }

  /** Commits a transaction. */
  ApiFuture<List<WriteResult>> commit() {
    return super.commit(transactionId);
  }

  /** Rolls a transaction back and releases all read locks. */
  ApiFuture<Void> rollback() {
    Tracing.getTracer().getCurrentSpan().addAnnotation(TraceUtil.SPAN_NAME_ROLLBACK);
    RollbackRequest req =
        RollbackRequest.newBuilder()
            .setTransaction(transactionId)
            .setDatabase(firestore.getDatabaseName())
            .build();

    ApiFuture<Empty> rollbackFuture =
        firestore.sendRequest(req, firestore.getClient().rollbackCallable());

    ApiFuture<Void> transform =
        ApiFutures.transform(rollbackFuture, resp -> null, MoreExecutors.directExecutor());

    return ApiFutures.catching(
        transform,
        Throwable.class,
        (error) -> {
          LOGGER.log(
              Level.WARNING,
              "Failed best effort to rollback of transaction " + transactionId,
              error);
          return null;
        },
        MoreExecutors.directExecutor());
  }

  /**
   * Reads the document referred to by the provided DocumentReference. Holds a pessimistic lock on
   * the returned document.
   *
   * @return The contents of the Document at this DocumentReference.
   */
  @Override
  @Nonnull
  public ApiFuture<DocumentSnapshot> get(@Nonnull DocumentReference documentRef) {
    Preconditions.checkState(isEmpty(), READ_BEFORE_WRITE_ERROR_MSG);
    Tracing.getTracer().getCurrentSpan().addAnnotation(TraceUtil.SPAN_NAME_GETDOCUMENT);
    return ApiFutures.transform(
        firestore.getAll(
            new DocumentReference[] {documentRef},
            /*fieldMask=*/ null,
            transactionId,
            /*readTime=*/ null),
        snapshots -> snapshots.isEmpty() ? null : snapshots.get(0),
        MoreExecutors.directExecutor());
  }

  /**
   * Retrieves multiple documents from Firestore. Holds a pessimistic lock on all returned
   * documents.
   *
   * @param documentReferences List of Document References to fetch.
   */
  @Override
  @Nonnull
  public ApiFuture<List<DocumentSnapshot>> getAll(
      @Nonnull DocumentReference... documentReferences) {
    Preconditions.checkState(isEmpty(), READ_BEFORE_WRITE_ERROR_MSG);

    return firestore.getAll(
        documentReferences, /*fieldMask=*/ null, transactionId, /*readTime=*/ null);
  }

  /**
   * Retrieves multiple documents from Firestore, while optionally applying a field mask to reduce
   * the amount of data transmitted from the backend. Holds a pessimistic lock on all returned
   * documents.
   *
   * @param documentReferences Array with Document References to fetch.
   * @param fieldMask If set, specifies the subset of fields to return.
   */
  @Override
  @Nonnull
  public ApiFuture<List<DocumentSnapshot>> getAll(
      @Nonnull DocumentReference[] documentReferences, @Nullable FieldMask fieldMask) {
    Preconditions.checkState(isEmpty(), READ_BEFORE_WRITE_ERROR_MSG);

    return firestore.getAll(documentReferences, fieldMask, transactionId, /*readTime=*/ null);
  }

  /**
   * Returns the result set from the provided query. Holds a pessimistic lock on all returned
   * documents.
   *
   * @return The contents of the Document at this DocumentReference.
   */
  @Override
  @Nonnull
  public ApiFuture<QuerySnapshot> get(@Nonnull Query query) {
    Preconditions.checkState(isEmpty(), READ_BEFORE_WRITE_ERROR_MSG);

    return query.get(transactionId, /*readTime=*/ null);
  }

  /**
   * Returns the result from the provided aggregate query. Holds a pessimistic lock on all accessed
   * documents.
   *
   * @return The result of the aggregation.
   */
  @Override
  @Nonnull
  public ApiFuture<AggregateQuerySnapshot> get(@Nonnull AggregateQuery query) {
    Preconditions.checkState(isEmpty(), READ_BEFORE_WRITE_ERROR_MSG);

    return query.get(transactionId, null);
  }
}
