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
import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.core.SettableApiFuture;
import com.google.cloud.Timestamp;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firestore.v1.BatchWriteRequest;
import com.google.firestore.v1.BatchWriteResponse;
import io.grpc.Status;
import io.opencensus.trace.AttributeValue;
import io.opencensus.trace.Tracing;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.Nullable;

/** Used to represent a batch on the BatchQueue. */
class BulkCommitBatch extends UpdateBuilder<ApiFuture<WriteResult>> {
  /**
   * Used to represent the state of batch.
   *
   * <p>Writes can only be added while the batch is OPEN. For a batch to be sent, the batch must be
   * READY_TO_SEND. After a batch is sent, it is marked as SENT.
   */
  enum BatchState {
    OPEN,
    READY_TO_SEND,
    SENT,
  }

  private BatchState state = BatchState.OPEN;

  private final List<SettableApiFuture<BatchWriteResult>> pendingOperations = new ArrayList<>();
  private final Set<DocumentReference> documents = new CopyOnWriteArraySet<>();
  private final int maxBatchSize;

  BulkCommitBatch(FirestoreImpl firestore, int maxBatchSize) {
    super(firestore);
    this.maxBatchSize = maxBatchSize;
  }

  ApiFuture<WriteResult> wrapResult(DocumentReference documentReference) {
    return processLastOperation(documentReference);
  }

  /**
   * Commits all pending operations to the database and verifies all preconditions.
   *
   * <p>The writes in the batch are not applied atomically and can be applied out of order.
   */
  ApiFuture<List<BatchWriteResult>> bulkCommit() {
    Tracing.getTracer()
        .getCurrentSpan()
        .addAnnotation(
            TraceUtil.SPAN_NAME_BATCHWRITE,
            ImmutableMap.of("numDocuments", AttributeValue.longAttributeValue(getWrites().size())));

    Preconditions.checkState(
        isReadyToSend(), "The batch should be marked as READY_TO_SEND before committing");
    state = BatchState.SENT;

    final BatchWriteRequest.Builder request = BatchWriteRequest.newBuilder();
    request.setDatabase(firestore.getDatabaseName());

    for (WriteOperation writeOperation : getWrites()) {
      request.addWrites(writeOperation.write);
    }

    ApiFuture<BatchWriteResponse> response =
        firestore.sendRequest(request.build(), firestore.getClient().batchWriteCallable());

    committed = true;

    return ApiFutures.transform(
        response,
        new ApiFunction<BatchWriteResponse, List<BatchWriteResult>>() {
          @Override
          public List<BatchWriteResult> apply(BatchWriteResponse batchWriteResponse) {
            List<com.google.firestore.v1.WriteResult> writeResults =
                batchWriteResponse.getWriteResultsList();

            List<com.google.rpc.Status> statuses = batchWriteResponse.getStatusList();

            List<BatchWriteResult> result = new ArrayList<>();

            for (int i = 0; i < writeResults.size(); ++i) {
              com.google.firestore.v1.WriteResult writeResult = writeResults.get(i);
              com.google.rpc.Status status = statuses.get(i);
              Status code = Status.fromCodeValue(status.getCode());
              @Nullable Timestamp updateTime = null;
              @Nullable Exception exception = null;
              if (code == Status.OK) {
                updateTime = Timestamp.fromProto(writeResult.getUpdateTime());
              } else {
                exception = FirestoreException.serverRejected(code, status.getMessage());
              }
              result.add(new BatchWriteResult(updateTime, exception));
            }

            return result;
          }
        },
        MoreExecutors.directExecutor());
  }

  int getPendingOperationCount() {
    return pendingOperations.size();
  }

  ApiFuture<WriteResult> processLastOperation(DocumentReference documentReference) {
    Preconditions.checkState(
        !documents.contains(documentReference),
        "Batch should not contain writes to the same document");
    documents.add(documentReference);
    Preconditions.checkState(state == BatchState.OPEN, "Batch should be OPEN when adding writes");
    SettableApiFuture<BatchWriteResult> resultFuture = SettableApiFuture.create();
    pendingOperations.add(resultFuture);

    if (getPendingOperationCount() == maxBatchSize) {
      state = BatchState.READY_TO_SEND;
    }

    return ApiFutures.transformAsync(
        resultFuture,
        new ApiAsyncFunction<BatchWriteResult, WriteResult>() {
          public ApiFuture<WriteResult> apply(BatchWriteResult batchWriteResult) throws Exception {
            if (batchWriteResult.getException() == null) {
              return ApiFutures.immediateFuture(new WriteResult(batchWriteResult.getWriteTime()));
            } else {
              throw batchWriteResult.getException();
            }
          }
        },
        MoreExecutors.directExecutor());
  }

  /**
   * Resolves the individual operations in the batch with the results and removes the entry from the
   * pendingOperations map if the result is not retryable.
   */
  void processResults(List<BatchWriteResult> results) {
    for (int i = 0; i < results.size(); i++) {
      SettableApiFuture<BatchWriteResult> resultFuture = pendingOperations.get(i);
      BatchWriteResult result = results.get(i);
      if (result.getException() == null) {
        resultFuture.set(result);
      } else {
        resultFuture.setException(result.getException());
      }
    }
  }

  void markReadyToSend() {
    if (state == BatchState.OPEN) {
      state = BatchState.READY_TO_SEND;
    }
  }

  boolean isOpen() {
    return state == BatchState.OPEN;
  }

  boolean isReadyToSend() {
    return state == BatchState.READY_TO_SEND;
  }

  boolean has(DocumentReference documentReference) {
    return documents.contains(documentReference);
  }
}
