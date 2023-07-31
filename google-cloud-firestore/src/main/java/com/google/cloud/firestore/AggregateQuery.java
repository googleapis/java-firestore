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
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.v1.FirestoreSettings;
import com.google.firestore.v1.RunAggregationQueryRequest;
import com.google.firestore.v1.RunAggregationQueryResponse;
import com.google.firestore.v1.RunQueryRequest;
import com.google.firestore.v1.StructuredAggregationQuery;
import com.google.firestore.v1.Value;
import com.google.protobuf.ByteString;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** A query that calculates aggregations over an underlying query. */
@InternalExtensionOnly
public class AggregateQuery {

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

  /** Returns the query whose aggregations will be calculated by this object. */
  @Nonnull
  public Query getQuery() {
    return query;
  }

  /**
   * Executes this query.
   *
   * @return An {@link ApiFuture} that will be resolved with the results of the query.
   */
  @Nonnull
  public ApiFuture<AggregateQuerySnapshot> get() {
    return get(null);
  }

  /**
   * Performs the planning stage of this query, without actually executing the query. Returns an
   * ApiFuture that will be resolved with the result of the query planning information.
   *
   * <p>Note: the information included in the output of this function is subject to change.
   *
   * @return An ApiFuture that will be resolved with the results of the query planning information.
   */
  @Nonnull
  public ApiFuture<Map<String, Object>> plan() {
    Map<String, Object> plan = new HashMap<>();
    plan.put("foo", "bar");
    final SettableApiFuture<Map<String, Object>> result = SettableApiFuture.create();
    result.set(plan);
    return result;
  }

  /**
   * Plans and executes this query. Returns an ApiFuture that will be resolved with the planner
   * information, statistics from the query execution, and the query results.
   *
   * <p>Note: the information included in the output of this function is subject to change.
   *
   * @return An ApiFuture that will be resolved with the planner information, statistics from the
   *     query execution, and the query results.
   */
  @Nonnull
  public ApiFuture<QueryProfileInfo<AggregateQuerySnapshot>> profile() {
    Map<String, Object> plan = new HashMap<>();
    plan.put("foo", "bar");
    Map<String, Object> stats = new HashMap<>();
    stats.put("cpu", "3ms");
    final SettableApiFuture<QueryProfileInfo<AggregateQuerySnapshot>> result =
        SettableApiFuture.create();
    final AggregateQuerySnapshot mockSnapshot = new AggregateQuerySnapshot(this, null, 5);
    QueryProfileInfo<AggregateQuerySnapshot> mock = new QueryProfileInfo(plan, stats, mockSnapshot);
    result.set(mock);
    return result;
  }

  @Nonnull
  ApiFuture<AggregateQuerySnapshot> get(@Nullable final ByteString transactionId) {
    AggregateQueryResponseDeliverer responseDeliverer =
        new AggregateQueryResponseDeliverer(
            transactionId, /* startTimeNanos= */ query.rpcContext.getClock().nanoTime());
    runQuery(responseDeliverer);
    return responseDeliverer.getFuture();
  }

  private void runQuery(AggregateQueryResponseDeliverer responseDeliverer) {
    RunAggregationQueryRequest request = toProto(responseDeliverer.getTransactionId());
    AggregateQueryResponseObserver responseObserver =
        new AggregateQueryResponseObserver(responseDeliverer);
    ServerStreamingCallable<RunAggregationQueryRequest, RunAggregationQueryResponse> callable =
        query.rpcContext.getClient().runAggregationQueryCallable();
    query.rpcContext.streamRequest(request, responseObserver, callable);
  }

  private final class AggregateQueryResponseDeliverer {

    @Nullable private final ByteString transactionId;
    private final long startTimeNanos;
    private final SettableApiFuture<AggregateQuerySnapshot> future = SettableApiFuture.create();
    private final AtomicBoolean isFutureCompleted = new AtomicBoolean(false);

    AggregateQueryResponseDeliverer(@Nullable ByteString transactionId, long startTimeNanos) {
      this.transactionId = transactionId;
      this.startTimeNanos = startTimeNanos;
    }

    ApiFuture<AggregateQuerySnapshot> getFuture() {
      return future;
    }

    @Nullable
    ByteString getTransactionId() {
      return transactionId;
    }

    long getStartTimeNanos() {
      return startTimeNanos;
    }

    void deliverResult(long count, Timestamp readTime) {
      if (isFutureCompleted.compareAndSet(false, true)) {
        future.set(new AggregateQuerySnapshot(AggregateQuery.this, readTime, count));
      }
    }

    void deliverError(Throwable throwable) {
      if (isFutureCompleted.compareAndSet(false, true)) {
        future.setException(throwable);
      }
    }
  }

  private final class AggregateQueryResponseObserver
      implements ResponseObserver<RunAggregationQueryResponse> {

    private final AggregateQueryResponseDeliverer responseDeliverer;
    private StreamController streamController;

    AggregateQueryResponseObserver(AggregateQueryResponseDeliverer responseDeliverer) {
      this.responseDeliverer = responseDeliverer;
    }

    @Override
    public void onStart(StreamController streamController) {
      this.streamController = streamController;
    }

    @Override
    public void onResponse(RunAggregationQueryResponse response) {
      // Close the stream to avoid it dangling, since we're not expecting any more responses.
      streamController.cancel();

      // Extract the count and read time from the RunAggregationQueryResponse.
      Timestamp readTime = Timestamp.fromProto(response.getReadTime());
      Value value = response.getResult().getAggregateFieldsMap().get(ALIAS_COUNT);
      if (value == null) {
        throw new IllegalArgumentException(
            "RunAggregationQueryResponse is missing required alias: " + ALIAS_COUNT);
      } else if (value.getValueTypeCase() != Value.ValueTypeCase.INTEGER_VALUE) {
        throw new IllegalArgumentException(
            "RunAggregationQueryResponse alias "
                + ALIAS_COUNT
                + " has incorrect type: "
                + value.getValueTypeCase());
      }
      long count = value.getIntegerValue();

      // Deliver the result; even though the `RunAggregationQuery` RPC is a "streaming" RPC, meaning
      // that `onResponse()` can be called multiple times, it _should_ only be called once for count
      // queries. But even if it is called more than once, `responseDeliverer` will drop superfluous
      // results.
      responseDeliverer.deliverResult(count, readTime);
    }

    @Override
    public void onError(Throwable throwable) {
      if (shouldRetry(throwable)) {
        runQuery(responseDeliverer);
      } else {
        responseDeliverer.deliverError(throwable);
      }
    }

    private boolean shouldRetry(Throwable throwable) {
      Set<StatusCode.Code> retryableCodes =
          FirestoreSettings.newBuilder().runAggregationQuerySettings().getRetryableCodes();
      return query.shouldRetryQuery(
          throwable,
          responseDeliverer.getTransactionId(),
          responseDeliverer.getStartTimeNanos(),
          retryableCodes);
    }

    @Override
    public void onComplete() {}
  }

  /**
   * Returns the {@link RunAggregationQueryRequest} that this AggregateQuery instance represents.
   * The request contain the serialized form of all aggregations and Query constraints.
   *
   * @return the serialized RunAggregationQueryRequest
   */
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

  /**
   * Returns an AggregateQuery instance that can be used to execute the provided {@link
   * RunAggregationQueryRequest}.
   *
   * <p>Only RunAggregationQueryRequests that pertain to the same project as the Firestore instance
   * can be deserialized.
   *
   * @param firestore a Firestore instance to apply the query to.
   * @param proto the serialized RunAggregationQueryRequest.
   * @return a AggregateQuery instance that can be used to execute the RunAggregationQueryRequest.
   */
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

  /**
   * Calculates and returns the hash code for this object.
   *
   * @return the hash code for this object.
   */
  @Override
  public int hashCode() {
    return query.hashCode();
  }

  /**
   * Compares this object with the given object for equality.
   *
   * <p>This object is considered "equal" to the other object if and only if all of the following
   * conditions are satisfied:
   *
   * <ol>
   *   <li>{@code object} is a non-null instance of {@link AggregateQuery}.
   *   <li>{@code object} performs the same aggregations as this {@link AggregateQuery}.
   *   <li>The underlying {@link Query} of {@code object} compares equal to that of this object.
   * </ol>
   *
   * @param object The object to compare to this object for equality.
   * @return {@code true} if this object is "equal" to the given object, as defined above, or {@code
   *     false} otherwise.
   */
  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    } else if (!(object instanceof AggregateQuery)) {
      return false;
    }
    AggregateQuery other = (AggregateQuery) object;
    return query.equals(other.query);
  }
}
