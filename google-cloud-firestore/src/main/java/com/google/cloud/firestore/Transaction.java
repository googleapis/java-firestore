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
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A Transaction is passed to a Function to provide the methods to read and write data within the
 * transaction context.
 *
 * @see Firestore#runTransaction(Function)
 */
public final class Transaction extends UpdateBuilder<Transaction> {

  private static final String READ_BEFORE_WRITE_ERROR_MSG =
      "Firestore transactions require all reads to be executed before all writes";

  /**
   * User callback that takes a Firestore Transaction.
   *
   * @param <T> The result type of the user callback.
   */
  public interface Function<T> {

    T updateCallback(Transaction transaction) throws Exception;
  }

  /**
   * User callback that takes a Firestore Async Transaction.
   *
   * @param <T> The result type of the user async callback.
   */
  public interface AsyncFunction<T> {

    ApiFuture<T> updateCallback(Transaction transaction);
  }

  private final TransactionOptions transactionOptions;
  private ByteString transactionId;

  // TODO(ehsan): storing Context could have overhead? Can we store the OpenTelemetryUtilContext instead?
  @Nullable
  private Context txnTraceContext;

  Transaction(
      FirestoreImpl firestore,
      TransactionOptions transactionOptions,
      @Nullable Transaction previousTransaction) {
    super(firestore);
    this.transactionOptions = transactionOptions;
    this.transactionId = previousTransaction != null ? previousTransaction.transactionId : null;
    this.txnTraceContext = Context.current();
  }

  public boolean hasTransactionId() {
    return transactionId != null;
  }

  Transaction wrapResult(int writeIndex) {
    return this;
  }

  /** Starts a transaction and obtains the transaction id. */
  ApiFuture<Void> begin() {
    OpenTelemetryUtil openTelemetryUtil = firestore.getOpenTelemetryUtil();
    OpenTelemetryUtil.Span span = openTelemetryUtil.startSpan(OpenTelemetryUtil.SPAN_NAME_TRANSACTION_BEGIN, txnTraceContext);
    try(io.opentelemetry.context.Scope ignored = span.makeCurrent()) {
      BeginTransactionRequest.Builder beginTransaction = BeginTransactionRequest.newBuilder();
      beginTransaction.setDatabase(firestore.getDatabaseName());

      if (TransactionOptionsType.READ_WRITE.equals(transactionOptions.getType()) && transactionId != null) {
        beginTransaction.getOptionsBuilder().getReadWriteBuilder().setRetryTransaction(transactionId);
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

      ApiFuture<Void> result = ApiFutures.transform(
              transactionBeginFuture,
              beginTransactionResponse -> {
                transactionId = beginTransactionResponse.getTransaction();
                return null;
              },
              MoreExecutors.directExecutor());
      span.endAtFuture(result);
      return result;
    } catch (Exception error) {
      span.end(error);
      throw error;
    }
  }

  /** Commits a transaction. */
  ApiFuture<List<WriteResult>> commit() {
    try (Scope ignored = txnTraceContext.makeCurrent()) {
      return super.commit(transactionId);
    }
  }

  /** Rolls a transaction back and releases all read locks. */
  ApiFuture<Void> rollback() {
    OpenTelemetryUtil openTelemetryUtil = firestore.getOpenTelemetryUtil();
    OpenTelemetryUtil.Span span = openTelemetryUtil.startSpan(OpenTelemetryUtil.SPAN_NAME_TRANSACTION_ROLLBACK, txnTraceContext);
    try(io.opentelemetry.context.Scope ignored = span.makeCurrent()) {
      RollbackRequest.Builder reqBuilder = RollbackRequest.newBuilder();
      reqBuilder.setTransaction(transactionId);
      reqBuilder.setDatabase(firestore.getDatabaseName());

      ApiFuture<Empty> rollbackFuture =
              firestore.sendRequest(reqBuilder.build(), firestore.getClient().rollbackCallable());

      ApiFuture<Void> result = ApiFutures.transform(rollbackFuture, rollbackResponse -> null, MoreExecutors.directExecutor());
      span.endAtFuture(result);
      return result;
    } catch (Exception error) {
      span.end(error);
      throw error;
    }
  }

  /**
   * Reads the document referred to by the provided DocumentReference. Holds a pessimistic lock on
   * the returned document.
   *
   * @return The contents of the Document at this DocumentReference.
   */
  @Nonnull
  public ApiFuture<DocumentSnapshot> get(@Nonnull DocumentReference documentRef) {
    Preconditions.checkState(isEmpty(), READ_BEFORE_WRITE_ERROR_MSG);

    OpenTelemetryUtil openTelemetryUtil = firestore.getOpenTelemetryUtil();
    OpenTelemetryUtil.Span span = openTelemetryUtil.startSpan(OpenTelemetryUtil.SPAN_NAME_TRANSACTION_GET_DOCUMENT, txnTraceContext);
      try(io.opentelemetry.context.Scope ignored = span.makeCurrent()) {
        ApiFuture<DocumentSnapshot> result =
        ApiFutures.transform(
                firestore.getAll(new DocumentReference[] {documentRef}, /*fieldMask=*/ null, transactionId),
                snapshots -> snapshots.isEmpty() ? null : snapshots.get(0),
                MoreExecutors.directExecutor());
        span.endAtFuture(result);
        return result;
      } catch (Exception error) {
        span.end(error);
        throw error;
      }
  }

  /**
   * Retrieves multiple documents from Firestore. Holds a pessimistic lock on all returned
   * documents.
   *
   * @param documentReferences List of Document References to fetch.
   */
  @Nonnull
  public ApiFuture<List<DocumentSnapshot>> getAll(
      @Nonnull DocumentReference... documentReferences) {
    Preconditions.checkState(isEmpty(), READ_BEFORE_WRITE_ERROR_MSG);

    OpenTelemetryUtil openTelemetryUtil = firestore.getOpenTelemetryUtil();
    OpenTelemetryUtil.Span span = openTelemetryUtil.startSpan(OpenTelemetryUtil.SPAN_NAME_TRANSACTION_GET_DOCUMENTS, txnTraceContext);
    try(io.opentelemetry.context.Scope ignored = span.makeCurrent()) {
      ApiFuture<List<DocumentSnapshot>> result = firestore.getAll(documentReferences, /*fieldMask=*/ null, transactionId);
      span.endAtFuture(result);
      return result;
    } catch (Exception error) {
      span.end(error);
      throw error;
    }
  }

  /**
   * Retrieves multiple documents from Firestore, while optionally applying a field mask to reduce
   * the amount of data transmitted from the backend. Holds a pessimistic lock on all returned
   * documents.
   *
   * @param documentReferences Array with Document References to fetch.
   * @param fieldMask If set, specifies the subset of fields to return.
   */
  @Nonnull
  public ApiFuture<List<DocumentSnapshot>> getAll(
      @Nonnull DocumentReference[] documentReferences, @Nullable FieldMask fieldMask) {
    Preconditions.checkState(isEmpty(), READ_BEFORE_WRITE_ERROR_MSG);

    OpenTelemetryUtil openTelemetryUtil = firestore.getOpenTelemetryUtil();
    OpenTelemetryUtil.Span span = openTelemetryUtil.startSpan(OpenTelemetryUtil.SPAN_NAME_TRANSACTION_GET_DOCUMENTS, txnTraceContext);
    try(io.opentelemetry.context.Scope ignored = span.makeCurrent()) {
      ApiFuture<List<DocumentSnapshot>> result = firestore.getAll(documentReferences, fieldMask, transactionId);
      span.endAtFuture(result);
      return result;
    } catch (Exception error) {
      span.end(error);
      throw error;
    }
  }

  /**
   * Returns the result set from the provided query. Holds a pessimistic lock on all returned
   * documents.
   *
   * @return The contents of the Document at this DocumentReference.
   */
  @Nonnull
  public ApiFuture<QuerySnapshot> get(@Nonnull Query query) {
    Preconditions.checkState(isEmpty(), READ_BEFORE_WRITE_ERROR_MSG);

    try (Scope ignored = txnTraceContext.makeCurrent()) {
      return query.get(transactionId);
    }
  }

  /**
   * Returns the result from the provided aggregate query. Holds a pessimistic lock on all accessed
   * documents.
   *
   * @return The result of the aggregation.
   */
  @Nonnull
  public ApiFuture<AggregateQuerySnapshot> get(@Nonnull AggregateQuery query) {
    Preconditions.checkState(isEmpty(), READ_BEFORE_WRITE_ERROR_MSG);

    try (Scope ignored = txnTraceContext.makeCurrent()) {
      return query.get(transactionId);
    }
  }
}
