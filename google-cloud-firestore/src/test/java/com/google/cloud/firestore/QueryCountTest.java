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

import static com.google.cloud.firestore.LocalFirestoreHelper.COLLECTION_ID;
import static com.google.cloud.firestore.LocalFirestoreHelper.aggregationQuery;
import static com.google.cloud.firestore.LocalFirestoreHelper.aggregationQueryResponse;
import static com.google.cloud.firestore.LocalFirestoreHelper.aggregationQueryResponses;
import static com.google.cloud.firestore.LocalFirestoreHelper.endAt;
import static com.google.cloud.firestore.LocalFirestoreHelper.limit;
import static com.google.cloud.firestore.LocalFirestoreHelper.order;
import static com.google.cloud.firestore.LocalFirestoreHelper.query;
import static com.google.cloud.firestore.LocalFirestoreHelper.startAt;
import static com.google.cloud.firestore.LocalFirestoreHelper.string;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.spi.v1.FirestoreRpc;
import com.google.firestore.v1.RunAggregationQueryRequest;
import com.google.firestore.v1.RunAggregationQueryResponse;
import com.google.firestore.v1.StructuredQuery;
import java.util.concurrent.ExecutionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.Duration;

@RunWith(MockitoJUnitRunner.class)
public class QueryCountTest {

  @Spy
  private final FirestoreImpl firestoreMock =
      new FirestoreImpl(
          FirestoreOptions.newBuilder().setProjectId("test-project").build(),
          Mockito.mock(FirestoreRpc.class));

  @Captor private ArgumentCaptor<RunAggregationQueryRequest> runAggregationQuery;

  @Captor
  private ArgumentCaptor<ResponseObserver<RunAggregationQueryResponse>> streamObserverCapture;

  private Query query;

  @Before
  public void before() {
    doReturn(Duration.ZERO).when(firestoreMock).getTotalRequestTimeout();
    query = firestoreMock.collection(COLLECTION_ID);
  }

  @Test
  public void countShouldBeZeroForEmptyCollection() throws Exception {
    doAnswer(aggregationQueryResponse(0))
        .when(firestoreMock)
        .streamRequest(
            runAggregationQuery.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable>any());

    AggregateQuerySnapshot snapshot = query.count().get().get();

    assertThat(snapshot.getCount()).isEqualTo(0);
  }

  @Test
  public void countShouldBe99ForCollectionWith99Documents() throws Exception {
    doAnswer(aggregationQueryResponse(99))
        .when(firestoreMock)
        .streamRequest(
            runAggregationQuery.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable>any());

    AggregateQuerySnapshot snapshot = query.count().get().get();

    assertThat(snapshot.getCount()).isEqualTo(99);
  }

  @Test
  public void countShouldMakeCorrectRequestForACollection() throws Exception {
    doAnswer(aggregationQueryResponse(0))
        .when(firestoreMock)
        .streamRequest(
            runAggregationQuery.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable>any());

    CollectionReference collection = firestoreMock.collection(COLLECTION_ID);
    collection.count().get();

    assertThat(runAggregationQuery.getValue()).isEqualTo(aggregationQuery());
  }

  @Test
  public void countShouldMakeCorrectRequestForAComplexQuery() throws Exception {
    doAnswer(aggregationQueryResponse(0))
        .when(firestoreMock)
        .streamRequest(
            runAggregationQuery.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable>any());

    query.orderBy("foo").startAt("foo").endAt("bar").limitToLast(42).count().get().get();

    assertThat(runAggregationQuery.getValue())
        .isEqualTo(
            aggregationQuery(
                query(
                    limit(42),
                    order("foo", StructuredQuery.Direction.DESCENDING),
                    endAt(string("foo"), false),
                    startAt(string("bar"), true))));
  }

  @Test
  public void shouldReturnReadTimeFromResponse() throws Exception {
    doAnswer(aggregationQueryResponse(99, Timestamp.ofTimeSecondsAndNanos(123, 456)))
        .when(firestoreMock)
        .streamRequest(
            runAggregationQuery.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable>any());

    AggregateQuerySnapshot snapshot = query.count().get().get();

    assertThat(snapshot.getReadTime()).isEqualTo(Timestamp.ofTimeSecondsAndNanos(123, 456));
  }

  @Test
  public void shouldIgnoreExtraRunAggregationQueryResponses() throws Exception {
    doAnswer(aggregationQueryResponses(123, 456))
        .when(firestoreMock)
        .streamRequest(
            runAggregationQuery.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable>any());

    AggregateQuerySnapshot snapshot = query.count().get().get();

    assertThat(snapshot.getCount()).isEqualTo(123);
  }

  @Test
  public void shouldIgnoreExtraErrors() throws Exception {
    doAnswer(aggregationQueryResponses(123, new Exception()))
        .when(firestoreMock)
        .streamRequest(
            runAggregationQuery.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable>any());

    AggregateQuerySnapshot snapshot = query.count().get().get();

    assertThat(snapshot.getCount()).isEqualTo(123);
  }

  @Test
  public void shouldPropagateErrors() throws Exception {
    Exception exception = new Exception();
    doAnswer(aggregationQueryResponse(exception))
        .when(firestoreMock)
        .streamRequest(
            runAggregationQuery.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable>any());

    ApiFuture<AggregateQuerySnapshot> future = query.count().get();
    ExecutionException executionException = assertThrows(ExecutionException.class, future::get);
    assertThat(executionException).hasCauseThat().isSameInstanceAs(exception);
  }

  @Test
  public void aggregateQueryGetQueryShouldReturnCorrectValue() throws Exception {
    AggregateQuery aggregateQuery = query.count();
    assertThat(aggregateQuery.getQuery()).isSameInstanceAs(query);
  }

  @Test
  public void aggregateQuerySnapshotGetQueryShouldReturnCorrectValue() throws Exception {
    doAnswer(aggregationQueryResponse())
        .when(firestoreMock)
        .streamRequest(
            runAggregationQuery.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable>any());

    AggregateQuery aggregateQuery = query.count();
    AggregateQuerySnapshot snapshot = aggregateQuery.get().get();
    assertThat(snapshot.getQuery()).isSameInstanceAs(aggregateQuery);
  }
}
