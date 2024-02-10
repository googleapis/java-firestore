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
import com.google.cloud.firestore.TransactionOptions.TransactionOptionsType;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firestore.v1.BeginTransactionRequest;
import com.google.firestore.v1.BeginTransactionResponse;
import com.google.firestore.v1.RollbackRequest;
import com.google.firestore.v1.TransactionOptions.ReadOnly;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.opencensus.trace.Tracing;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class ServerSideTransaction extends Transaction {

  private static final Logger LOGGER = Logger.getLogger(ServerSideTransaction.class.getName());

  private static final String READ_BEFORE_WRITE_ERROR_MSG =
      "Firestore transactions require all reads to be executed before all writes";

  private final FirestoreImpl firestore;

  final ByteString transactionId;

  private ServerSideTransaction(FirestoreImpl firestore, ByteString transactionId) {
    super(firestore);
    this.firestore = firestore;
    this.transactionId = transactionId;
  }

  public ByteString getTransactionId() {
    return transactionId;
  }

  public static ApiFuture<ServerSideTransaction> begin(
      FirestoreImpl firestore,
      TransactionOptions transactionOptions,
      @Nullable ServerSideTransaction previousTransaction) {
    Tracing.getTracer().getCurrentSpan().addAnnotation(TraceUtil.SPAN_NAME_BEGINTRANSACTION);
    BeginTransactionRequest.Builder beginTransaction = BeginTransactionRequest.newBuilder();
    beginTransaction.setDatabase(firestore.getDatabaseName());
    ByteString previousTransactionId =
        previousTransaction != null ? previousTransaction.transactionId : null;

    if (TransactionOptionsType.READ_WRITE.equals(transactionOptions.getType())
        && previousTransactionId != null) {
      beginTransaction
          .getOptionsBuilder()
          .getReadWriteBuilder()
          .setRetryTransaction(previousTransactionId);
    } else if (TransactionOptionsType.READ_ONLY.equals(transactionOptions.getType())) {
      final ReadOnly.Builder readOnlyBuilder = ReadOnly.newBuilder();
      if (transactionOptions.getReadTime() != null) {
        readOnlyBuilder.setReadTime(transactionOptions.getReadTime());
      }
      beginTransaction.getOptionsBuilder().setReadOnly(readOnlyBuilder);
    }

    ApiFuture<BeginTransactionResponse> transactionBeginFuture =
        firestore.sendRequest(
            beginTransaction.build(), firestore.getClient().beginTransactionCallable());

    return ApiFutures.transform(
        transactionBeginFuture,
        beginTransactionResponse ->
            new ServerSideTransaction(firestore, beginTransactionResponse.getTransaction()),
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

  @Override
  public boolean hasTransactionId() {
    return true;
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
    Tracing.getTracer().getCurrentSpan().addAnnotation(TraceUtil.SPAN_NAME_GETDOCUMENT);
    Preconditions.checkState(isEmpty(), READ_BEFORE_WRITE_ERROR_MSG);
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
