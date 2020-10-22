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

import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.ApiExceptions;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.cloud.firestore.v1.FirestoreClient;
import com.google.common.base.Preconditions;
import com.google.firestore.v1.Cursor;
import com.google.firestore.v1.PartitionQueryRequest;
import io.opencensus.common.Scope;
import io.opencensus.trace.Span;
import io.opencensus.trace.Status;
import javax.annotation.Nullable;

/**
 * A Collection Group query matches all documents that are contained in a collection or
 * subcollection with a specific collection ID.
 */
public class CollectionGroup extends Query {
  CollectionGroup(FirestoreRpcContext<?> rpcContext, String collectionId) {
    super(
        rpcContext,
        QueryOptions.builder()
            .setParentPath(rpcContext.getResourcePath())
            .setCollectionId(collectionId)
            .setAllDescendants(true)
            .build());
  }

  /**
   * Partitions a query by returning partition cursors that can be used to run the query in
   * parallel. The returned partition cursors are split points that can be used as starting/end
   * points for the query results.
   *
   * @param desiredPartitionCount The desired maximum number of partition points. The number must be
   *     strictly positive. The actual number of partitions returned may be fewer.
   * @param observer a stream observer that receives the result of the Partition request.
   */
  public void getPartitions(
      long desiredPartitionCount, ApiStreamObserver<QueryPartition> observer) {
    Preconditions.checkArgument(
        desiredPartitionCount > 0, "Desired partition count must be one or greater");

    // Partition queries require explicit ordering by __name__.
    Query queryWithDefaultOrder = orderBy(FieldPath.DOCUMENT_ID);

    if (desiredPartitionCount == 1) {
      // Short circuit if the user only requested a single partition.
      observer.onNext(new QueryPartition(queryWithDefaultOrder, null, null));
    } else {
      PartitionQueryRequest.Builder request = PartitionQueryRequest.newBuilder();
      request.setStructuredQuery(queryWithDefaultOrder.buildQuery());
      request.setParent(options.getParentPath().toString());

      // Since we are always returning an extra partition (with en empty endBefore cursor), we
      // reduce the desired partition count by one.
      request.setPartitionCount(desiredPartitionCount - 1);

      final FirestoreClient.PartitionQueryPagedResponse response;
      final TraceUtil traceUtil = TraceUtil.getInstance();
      Span span = traceUtil.startSpan(TraceUtil.SPAN_NAME_PARTITIONQUERY);
      try (Scope scope = traceUtil.getTracer().withSpan(span)) {
        response =
            ApiExceptions.callAndTranslateApiException(
                rpcContext.sendRequest(
                    request.build(), rpcContext.getClient().partitionQueryPagedCallable()));
      } catch (ApiException exception) {
        span.setStatus(Status.UNKNOWN.withDescription(exception.getMessage()));
        throw FirestoreException.apiException(exception);
      } finally {
        span.end(TraceUtil.END_SPAN_OPTIONS);
      }

      @Nullable Object[] lastCursor = null;
      for (Cursor cursor : response.iterateAll()) {
        Object[] decodedCursorValue = new Object[cursor.getValuesCount()];
        for (int i = 0; i < cursor.getValuesCount(); ++i) {
          decodedCursorValue[i] = UserDataConverter.decodeValue(rpcContext, cursor.getValues(i));
        }
        observer.onNext(new QueryPartition(queryWithDefaultOrder, lastCursor, decodedCursorValue));
        lastCursor = decodedCursorValue;
      }
      observer.onNext(new QueryPartition(queryWithDefaultOrder, lastCursor, null));
    }

    observer.onCompleted();
  }
}
