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
import com.google.firestore.v1.*;
import com.google.firestore.v1.StructuredAggregationQuery.Aggregation;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.List;
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

  @Nonnull private List<AggregateField> aggregateFieldList;

  AggregateQuery(@Nonnull Query query, @Nonnull List<AggregateField> aggregateFields) {
    this.query = query;
    this.aggregateFieldList = aggregateFields;
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

    void deliverResult(Map<String, Value> data, Timestamp readTime) {
      if (isFutureCompleted.compareAndSet(false, true)) {
        future.set(new AggregateQuerySnapshot(AggregateQuery.this, readTime, data));
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

      // Deliver the result; even though the `RunAggregationQuery` RPC is a "streaming" RPC, meaning
      // that `onResponse()` can be called multiple times, it _should_ only be called once. But even
      // if it is called more than once, `responseDeliverer` will drop superfluous results.
      responseDeliverer.deliverResult(response.getResult().getAggregateFieldsMap(), readTime);
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

    List<StructuredAggregationQuery.Aggregation> aggregations = new ArrayList<>();
    aggregateFieldList.forEach(aggregateField -> {
      StructuredAggregationQuery.Aggregation.Builder aggregation = StructuredAggregationQuery.Aggregation.newBuilder();
      if(aggregateField instanceof AggregateField.CountAggregateField) {
        aggregation.setCount(StructuredAggregationQuery.Aggregation.Count.getDefaultInstance());
      } else if(aggregateField instanceof AggregateField.SumAggregateField) {
        StructuredQuery.FieldReference field = StructuredQuery.FieldReference.newBuilder().setFieldPath(aggregateField.getFieldPath()).build();
        aggregation.setSum(StructuredAggregationQuery.Aggregation.Sum.newBuilder().setField(field).build());
      } else if(aggregateField instanceof AggregateField.AverageAggregateField) {
        StructuredQuery.FieldReference field = StructuredQuery.FieldReference.newBuilder().setFieldPath(aggregateField.getFieldPath()).build();
        aggregation.setAvg(StructuredAggregationQuery.Aggregation.Avg.newBuilder().setField(field).build());
      } else {
        throw new RuntimeException("Unsupported aggregation");
      }
      aggregation.setAlias(aggregateField.getAlias());
      aggregations.add(aggregation.build());
    });
    structuredAggregationQuery.addAllAggregations(aggregations);
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

    List<AggregateField> aggregateFields = new ArrayList<>();
    List<Aggregation> aggregations = proto.getStructuredAggregationQuery().getAggregationsList();
    aggregations.forEach(
        aggregation -> {
          if (aggregation.hasCount()) {
            aggregateFields.add(AggregateField.count());
          } else if (aggregation.hasAvg()) {
            aggregateFields.add(
                AggregateField.average(aggregation.getAvg().getField().getFieldPath()));
          } else if (aggregation.hasSum()) {
            aggregateFields.add(AggregateField.sum(aggregation.getSum().getField().getFieldPath()));
          } else {
            throw new RuntimeException("Unsupported aggregation");
          }
        });
    return new AggregateQuery(query, aggregateFields);
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
