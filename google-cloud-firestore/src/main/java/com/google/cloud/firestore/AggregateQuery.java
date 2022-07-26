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
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.Timestamp;
import com.google.firestore.v1.RunAggregationQueryRequest;
import com.google.firestore.v1.RunAggregationQueryResponse;
import com.google.firestore.v1.RunQueryRequest;
import com.google.firestore.v1.StructuredAggregationQuery;

import javax.annotation.Nonnull;

@InternalExtensionOnly
public class AggregateQuery {

  private static final String ALIAS_COUNT = "count";

  @Nonnull Query query;

  AggregateQuery(@Nonnull Query query) {
    this.query = query;
  }

  @Nonnull
  public Query getQuery() {
    return query;
  }

  @Nonnull
  public ApiFuture<AggregateQuerySnapshot> get() {
    RunQueryRequest runQueryRequest = query.toProto();

    RunAggregationQueryRequest.Builder request = RunAggregationQueryRequest.newBuilder();
    request.setParent(runQueryRequest.getParent());

    StructuredAggregationQuery.Builder structuredAggregationQuery = request.getStructuredAggregationQueryBuilder();
    structuredAggregationQuery.setStructuredQuery(runQueryRequest.getStructuredQuery());

    StructuredAggregationQuery.Aggregation.Builder aggregation = StructuredAggregationQuery.Aggregation.newBuilder();
    aggregation.setCount(StructuredAggregationQuery.Aggregation.Count.getDefaultInstance());
    aggregation.setAlias(ALIAS_COUNT);
    structuredAggregationQuery.addAggregations(aggregation);

    AggregateQueryResponseObserver responseObserver = new AggregateQueryResponseObserver();

    query.rpcContext.streamRequest(
            request.build(),
            responseObserver,
            query.rpcContext.getClient().runAggregationQueryCallable());

    return responseObserver.getFuture();
  }

  private final class AggregateQueryResponseObserver implements ResponseObserver<RunAggregationQueryResponse> {

    private final SettableApiFuture<AggregateQuerySnapshot> future =
            SettableApiFuture.create();

    SettableApiFuture<AggregateQuerySnapshot> getFuture() {
      return future;
    }

    @Override
    public void onStart(StreamController streamController) {
    }

    @Override
    public void onResponse(RunAggregationQueryResponse response) {
      Timestamp readTime = Timestamp.fromProto(response.getReadTime());
      long count = response.getResult().getAggregateFieldsMap().get(ALIAS_COUNT).getIntegerValue();
      future.set(new AggregateQuerySnapshot(AggregateQuery.this, readTime, count));
    }

    @Override
    public void onError(Throwable throwable) {
      future.setException(throwable);
    }

    @Override
    public void onComplete() {
    }
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
