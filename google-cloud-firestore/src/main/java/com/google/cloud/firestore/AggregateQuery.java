/*
 * Copyright 2022 Google LLC
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
import com.google.api.core.InternalExtensionOnly;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.Timestamp;
import com.google.firestore.v1.RunAggregationQueryRequest;
import com.google.firestore.v1.RunAggregationQueryResponse;
import com.google.firestore.v1.RunQueryRequest;
import com.google.firestore.v1.StructuredAggregationQuery;
import com.google.protobuf.ByteString;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// TODO(count) Make this class public
@InternalExtensionOnly
class AggregateQuery {

  /**
   * The "alias" to specify in the {@link RunAggregationQueryRequest} proto when running a count
   * query. The actual value is not meaningful, but will be used to get the count out of the {@link
   * RunAggregationQueryResponse}.
   */
  private static final String ALIAS_COUNT = "count";

  @Nonnull private final Query query;

  AggregateQuery(@Nonnull Query query) {
    this.query = query;
  }

  @Nonnull
  public Query getQuery() {
    return query;
  }

  @Nonnull
  public ApiFuture<AggregateQuerySnapshot> get() {
    return get(null);
  }

  @Nonnull
  ApiFuture<AggregateQuerySnapshot> get(@Nullable final ByteString transactionId) {
    RunAggregationQueryRequest request = toProto(transactionId);
    AggregateQueryResponseObserver responseObserver = new AggregateQueryResponseObserver();
    ServerStreamingCallable<RunAggregationQueryRequest, RunAggregationQueryResponse> callable =
        query.rpcContext.getClient().runAggregationQueryCallable();

    query.rpcContext.streamRequest(request, responseObserver, callable);

    return responseObserver.getFuture();
  }

  private final class AggregateQueryResponseObserver
      implements ResponseObserver<RunAggregationQueryResponse> {

    private AtomicReference<SettableApiFuture<AggregateQuerySnapshot>> future =
        new AtomicReference<>(SettableApiFuture.create());
    private StreamController streamController;

    SettableApiFuture<AggregateQuerySnapshot> getFuture() {
      return future.get();
    }

    @Override
    public void onStart(StreamController streamController) {
      this.streamController = streamController;
    }

    @Override
    public void onResponse(RunAggregationQueryResponse response) {
      SettableApiFuture<AggregateQuerySnapshot> future = this.future.getAndSet(null);
      if (future == null) {
        return;
      }

      Timestamp readTime = Timestamp.fromProto(response.getReadTime());
      long count = response.getResult().getAggregateFieldsMap().get(ALIAS_COUNT).getIntegerValue();
      future.set(new AggregateQuerySnapshot(AggregateQuery.this, readTime, count));

      // Close the stream to avoid it dangling, since we're not expecting any more responses.
      streamController.cancel();
    }

    @Override
    public void onError(Throwable throwable) {
      SettableApiFuture<AggregateQuerySnapshot> future = this.future.getAndSet(null);
      if (future == null) {
        return;
      }

      future.setException(throwable);
    }

    @Override
    public void onComplete() {}
  }

  @Nonnull
  public RunAggregationQueryRequest toProto() {
    return toProto(null);
  }

  @Nonnull
  RunAggregationQueryRequest toProto(@Nullable final ByteString transactionId) {
    RunQueryRequest runQueryRequest = query.toProto();

    RunAggregationQueryRequest.Builder request = RunAggregationQueryRequest.newBuilder();
    request.setParent(runQueryRequest.getParent());
    if (transactionId != null) {
      request.setTransaction(transactionId);
    }

    StructuredAggregationQuery.Builder structuredAggregationQuery =
        request.getStructuredAggregationQueryBuilder();
    structuredAggregationQuery.setStructuredQuery(runQueryRequest.getStructuredQuery());

    StructuredAggregationQuery.Aggregation.Builder aggregation =
        StructuredAggregationQuery.Aggregation.newBuilder();
    aggregation.setCount(StructuredAggregationQuery.Aggregation.Count.getDefaultInstance());
    aggregation.setAlias(ALIAS_COUNT);
    structuredAggregationQuery.addAggregations(aggregation);

    return request.build();
  }

  @Nonnull
  public static AggregateQuery fromProto(Firestore firestore, RunAggregationQueryRequest proto) {
    RunQueryRequest runQueryRequest =
        RunQueryRequest.newBuilder()
            .setParent(proto.getParent())
            .setStructuredQuery(proto.getStructuredAggregationQuery().getStructuredQuery())
            .build();
    Query query = Query.fromProto(firestore, runQueryRequest);
    return new AggregateQuery(query);
  }

  @Override
  public int hashCode() {
    return query.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    } else if (!(obj instanceof AggregateQuery)) {
      return false;
    }
    AggregateQuery other = (AggregateQuery) obj;
    return query.equals(other.query);
  }
}
