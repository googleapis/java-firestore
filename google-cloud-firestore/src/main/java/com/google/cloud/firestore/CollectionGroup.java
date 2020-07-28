package com.google.cloud.firestore;

import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.ApiExceptions;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.cloud.firestore.v1.FirestoreClient;
import com.google.common.collect.ImmutableList;
import com.google.firestore.v1.Cursor;
import com.google.firestore.v1.PartitionQueryRequest;
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
            .setFieldOrders(ImmutableList.of(FieldOrder.defaultOrder()))
            .setAllDescendants(true)
            .build());
  }

  /**
   * Partitions a query by returning partition cursors that can be used to run the query in
   * parallel. The returned partition cursors are split points that an be used starting/end points
   * for the query results.
   *
   * @param desiredPartitionCount The desired maximum number of partition points. The number must be
   *     strictly positive. The actual number of partitions returned may be fewer.
   * @param observer a stream observer that receives the result of the Partition request.
   */
  public void getPartitions(
      long desiredPartitionCount, ApiStreamObserver<QueryPartition> observer) {
    PartitionQueryRequest.Builder request = PartitionQueryRequest.newBuilder();
    request.setStructuredQuery(buildQuery());
    request.setParent(options.getParentPath().toString());

    // Since we are always returning an extra partition (with en empty endBefore cursor), we
    // reduce the desired partition count by one.
    request.setPartitionCount(desiredPartitionCount - 1);

    final FirestoreClient.PartitionQueryPagedResponse response;
    try {
      response =
          ApiExceptions.callAndTranslateApiException(
              rpcContext.sendRequest(
                  request.build(), rpcContext.getClient().partitionQueryPagedCallable()));
    } catch (ApiException exception) {
      throw FirestoreException.apiException(exception);
    }

    @Nullable Object[] lastCursor = null;
    for (Cursor cursor : response.iterateAll()) {
      Object[] decodedCursorValue = new Object[cursor.getValuesCount()];
      for (int i = 0; i < cursor.getValuesCount(); ++i) {
        decodedCursorValue[i] = UserDataConverter.decodeValue(rpcContext, cursor.getValues(i));
      }
      observer.onNext(new QueryPartition(this, lastCursor, decodedCursorValue));
      lastCursor = decodedCursorValue;
    }
    observer.onNext(new QueryPartition(this, lastCursor, null));
    observer.onCompleted();
  }
}
