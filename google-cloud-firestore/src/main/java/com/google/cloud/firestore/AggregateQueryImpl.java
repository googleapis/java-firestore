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
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.AggregateField.CountAggregateField;
import com.google.common.collect.ImmutableMap;
import com.google.firestore.v1.RunAggregationQueryRequest;
import com.google.firestore.v1.RunAggregationQueryResponse;
import com.google.firestore.v1.RunQueryRequest;
import com.google.firestore.v1.StructuredAggregationQuery;
import com.google.firestore.v1.StructuredAggregationQuery.Aggregation;
import io.opencensus.trace.AttributeValue;
import io.opencensus.trace.Tracing;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import javax.annotation.Nonnull;

final class AggregateQueryImpl implements AggregateQuery{

  @Nonnull
  private final Query query;
  @Nonnull
  private final AggregateField[] aggregateFields;

  AggregateQueryImpl(@Nonnull Query query, @Nonnull List<AggregateField> aggregateFields) {
    this.query = query;
    this.aggregateFields = aggregateFields.toArray(new AggregateField[0]);
  }

  @Nonnull
  @Override
  public Query getQuery() {
    return query;
  }

  @Nonnull
  @Override
  public ApiFuture<AggregateQuerySnapshot> get() {
    RunQueryRequest runQueryRequest = query.toProto();

    RunAggregationQueryRequest.Builder request = RunAggregationQueryRequest.newBuilder();
    request.setParent(runQueryRequest.getParent());

    StructuredAggregationQuery.Builder structuredAggregationQuery = request.getStructuredAggregationQueryBuilder();
    structuredAggregationQuery.setStructuredQuery(runQueryRequest.getStructuredQuery());

    for (AggregateField aggregateField : aggregateFields) {
      Aggregation.Builder aggregation = Aggregation.newBuilder();
      if (aggregateField instanceof CountAggregateField) {
        aggregation.setCount(Aggregation.Count.getDefaultInstance());
        aggregation.setAlias("agg_alias_count");
      } else {
        throw new RuntimeException("unsupported aggregation: " + aggregateField);
      }
      structuredAggregationQuery.addAggregations(aggregation);
    }

    AggregateQueryResponseObserver responseObserver = new AggregateQueryResponseObserver();

    query.rpcContext.streamRequest(
        request.build(),
        responseObserver,
        query.rpcContext.getClient().runAggregateQueryCallable());

    return responseObserver.getFuture();
  }

  private final class AggregateQueryResponseObserver implements ResponseObserver<RunAggregationQueryResponse> {

    private final SettableApiFuture<AggregateQuerySnapshot> future =
        SettableApiFuture.create();

    public SettableApiFuture<AggregateQuerySnapshot> getFuture() {
      return future;
    }

    @Override
    public void onStart(StreamController streamController) {
    }

    @Override
    public void onResponse(RunAggregationQueryResponse response) {
      future.set(new AggregateQuerySnapshotImpl(AggregateQueryImpl.this, Timestamp.fromProto(response.getReadTime()), response.getResult()));
    }

    @Override
    public void onError(Throwable throwable) {
      future.setException(throwable);
    }

    @Override
    public void onComplete() {
    }
  }

  @Nonnull
  @Override
  public ListenerRegistration addSnapshotListener(@Nonnull EventListener<AggregateQuerySnapshot> listener) {
    throw new RuntimeException("not implemented");
  }

  @Nonnull
  @Override
  public ListenerRegistration addSnapshotListener(@Nonnull Executor executor, @Nonnull EventListener<AggregateQuerySnapshot> listener) {
    throw new RuntimeException("not implemented");
  }

  @Override
  public int hashCode() {
    throw new RuntimeException("not implemented");
  }

  @Override
  public boolean equals(Object obj) {
    throw new RuntimeException("not implemented");
  }

}
