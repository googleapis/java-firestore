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

import static com.google.cloud.firestore.LocalFirestoreHelper.batchWrite;
import static com.google.cloud.firestore.LocalFirestoreHelper.create;
import static com.google.cloud.firestore.LocalFirestoreHelper.delete;
import static com.google.cloud.firestore.LocalFirestoreHelper.set;
import static com.google.cloud.firestore.LocalFirestoreHelper.update;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.LocalFirestoreHelper.ResponseStubber;
import com.google.cloud.firestore.LocalFirestoreHelper.SerialResponseStubber;
import com.google.cloud.firestore.spi.v1.FirestoreRpc;
import com.google.firestore.v1.BatchWriteRequest;
import com.google.firestore.v1.BatchWriteResponse;
import com.google.firestore.v1.Write;
import com.google.protobuf.GeneratedMessageV3;
import com.google.rpc.Code;
import io.grpc.Status;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class BulkWriterTest {

  @Spy private final FirestoreRpc firestoreRpc = Mockito.mock(FirestoreRpc.class);

  /** Executor that executes delayed tasks without delay. */
  private final ScheduledExecutorService immediateExecutor =
      new ScheduledThreadPoolExecutor(1) {
        @Override
        @Nonnull
        public <V> ScheduledFuture<V> schedule(Callable<V> command, long delay, TimeUnit unit) {
          return super.schedule(command, 0, TimeUnit.MILLISECONDS);
        }
      };

  @Spy
  private final FirestoreImpl firestoreMock =
      new FirestoreImpl(
          FirestoreOptions.newBuilder().setProjectId("test-project").build(), firestoreRpc);

  @Captor private ArgumentCaptor<BatchWriteRequest> batchWriteCapture;

  private BulkWriter bulkWriter;
  private DocumentReference doc1;
  private DocumentReference doc2;

  private ApiFuture<BatchWriteResponse> successResponse(int updateTimeSeconds) {
    BatchWriteResponse.Builder response = BatchWriteResponse.newBuilder();
    response.addWriteResultsBuilder().getUpdateTimeBuilder().setSeconds(updateTimeSeconds).build();
    response.addStatusBuilder().build();
    return ApiFutures.immediateFuture(response.build());
  }

  private ApiFuture<BatchWriteResponse> failedResponse(int code) {
    BatchWriteResponse.Builder response = BatchWriteResponse.newBuilder();
    response.addWriteResultsBuilder().build();
    response.addStatusBuilder().setCode(code).build();
    return ApiFutures.immediateFuture(response.build());
  }

  private ApiFuture<BatchWriteResponse> failedResponse() {
    return failedResponse(Code.DEADLINE_EXCEEDED_VALUE);
  }

  private ApiFuture<BatchWriteResponse> mergeResponses(ApiFuture<BatchWriteResponse>... responses)
      throws Exception {
    BatchWriteResponse.Builder response = BatchWriteResponse.newBuilder();
    for (ApiFuture<BatchWriteResponse> future : responses) {
      BatchWriteResponse res = future.get();
      response.addStatus(res.getStatus(0));
      response.addWriteResults(res.getWriteResults(0));
    }
    return ApiFutures.immediateFuture(response.build());
  }

  private void verifyRequests(List<BatchWriteRequest> requests, ResponseStubber responseStubber) {
    int index = 0;
    for (GeneratedMessageV3 request : responseStubber.keySet()) {
      assertEquals(request, requests.get(index++));
    }
  }

  @Before
  public void before() {
    doReturn(immediateExecutor).when(firestoreRpc).getExecutor();
    bulkWriter = firestoreMock.bulkWriter();
    doc1 = firestoreMock.document("coll/doc1");
    doc2 = firestoreMock.document("coll/doc2");
  }

  @Test
  public void hasSetMethod() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                successResponse(2));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    ApiFuture<WriteResult> result = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close().get();

    List<BatchWriteRequest> requests = batchWriteCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());

    verifyRequests(requests, responseStubber);
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result.get().getUpdateTime());
  }

  @Test
  public void hasUpdateMethod() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(update(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                successResponse(2));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    ApiFuture<WriteResult> result = bulkWriter.update(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close().get();

    List<BatchWriteRequest> requests = batchWriteCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());

    verifyRequests(requests, responseStubber);
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result.get().getUpdateTime());
  }

  @Test
  public void hasDeleteMethod() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(batchWrite(delete("coll/doc1")), successResponse(2));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    ApiFuture<WriteResult> result = bulkWriter.delete(doc1);
    bulkWriter.close().get();

    List<BatchWriteRequest> requests = batchWriteCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());

    verifyRequests(requests, responseStubber);
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result.get().getUpdateTime());
  }

  @Test
  public void hasCreateMethod() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(create(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                successResponse(2));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    ApiFuture<WriteResult> result = bulkWriter.create(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close().get();

    List<BatchWriteRequest> requests = batchWriteCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());

    verifyRequests(requests, responseStubber);
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result.get().getUpdateTime());
  }

  @Test
  public void surfacesErrors() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                failedResponse());
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    ApiFuture<WriteResult> result = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close().get();

    List<BatchWriteRequest> requests = batchWriteCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());

    verifyRequests(requests, responseStubber);
    try {
      result.get();
      fail("set() should have failed");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof FirestoreException);
      assertEquals(Status.DEADLINE_EXCEEDED, ((FirestoreException) e.getCause()).getStatus());
    }
  }

  @Test
  public void flushResolvesImmediatelyIfNoWrites() throws Exception {
    bulkWriter.flush().get();
  }

  @Test
  public void addsWritesToNewBatchAfterFlush() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(create(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                successResponse(1));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2")),
                successResponse(2));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    ApiFuture<WriteResult> result1 = bulkWriter.create(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.flush();
    ApiFuture<WriteResult> result2 = bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close().get();

    List<BatchWriteRequest> requests = batchWriteCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());

    verifyRequests(requests, responseStubber);
    assertEquals(Timestamp.ofTimeSecondsAndNanos(1, 0), result1.get().getUpdateTime());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result2.get().getUpdateTime());
  }

  @Test
  public void closeResolvesImmediatelyIfNoWrites() throws Exception {
    bulkWriter.close().get();
  }

  @Test
  public void cannotCallMethodsAfterClose() throws Exception {
    String expected = "BulkWriter has already been closed.";
    bulkWriter.close().get();
    try {
      bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
      fail("set() should have failed");
    } catch (Exception e) {
      assertEquals(expected, e.getMessage());
    }
    try {
      bulkWriter.create(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
      fail("create() should have failed");
    } catch (Exception e) {
      assertEquals(expected, e.getMessage());
    }
    try {
      bulkWriter.update(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
      fail("update() should have failed");
    } catch (Exception e) {
      assertEquals(expected, e.getMessage());
    }
    try {
      bulkWriter.delete(doc1);
      fail("delete() should have failed");
    } catch (Exception e) {
      assertEquals(expected, e.getMessage());
    }
    try {
      bulkWriter.flush();
      fail("flush() should have failed");
    } catch (Exception e) {
      assertEquals(expected, e.getMessage());
    }
    try {
      bulkWriter.close();
      fail("close() should have failed");
    } catch (Exception e) {
      assertEquals(expected, e.getMessage());
    }
  }

  @Test
  public void sendsWritesToSameDocInSeparateBatches() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                successResponse(1));
            put(
                batchWrite(update(LocalFirestoreHelper.UPDATED_FIELD_PROTO, "coll/doc1")),
                successResponse(2));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    // Create another document reference pointing to the same document.
    DocumentReference sameDoc = firestoreMock.document(doc1.getPath());
    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result2 =
        bulkWriter.update(sameDoc, LocalFirestoreHelper.UPDATED_FIELD_MAP);
    bulkWriter.close().get();

    List<BatchWriteRequest> requests = batchWriteCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());

    verifyRequests(requests, responseStubber);
    assertEquals(Timestamp.ofTimeSecondsAndNanos(1, 0), result1.get().getUpdateTime());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result2.get().getUpdateTime());
  }

  @Test
  public void sendWritesToDifferentDocsInSameBatch() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1"),
                    update(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2")),
                mergeResponses(successResponse(1), successResponse(2)));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result2 = bulkWriter.update(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close().get();

    List<BatchWriteRequest> requests = batchWriteCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());

    verifyRequests(requests, responseStubber);
    assertEquals(Timestamp.ofTimeSecondsAndNanos(1, 0), result1.get().getUpdateTime());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result2.get().getUpdateTime());
  }

  @Test
  public void sendBatchesWhenSizeLimitIsReached() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1"),
                    update(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2"),
                    create(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc3")),
                mergeResponses(successResponse(1), successResponse(2), successResponse(3)));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    bulkWriter.setMaxBatchSize(3);
    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result2 = bulkWriter.update(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result3 =
        bulkWriter.create(
            firestoreMock.document("coll/doc3"), LocalFirestoreHelper.SINGLE_FIELD_MAP);

    // The 4th write should not be sent because it should be in a new batch.
    bulkWriter.delete(firestoreMock.document("coll/doc4"));

    List<BatchWriteRequest> requests = batchWriteCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());

    verifyRequests(requests, responseStubber);
    assertEquals(Timestamp.ofTimeSecondsAndNanos(1, 0), result1.get().getUpdateTime());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result2.get().getUpdateTime());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(3, 0), result3.get().getUpdateTime());
  }

  @Test
  public void retriesIndividualWritesThatFailWithAborted() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1"),
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2"),
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc3")),
                mergeResponses(
                    failedResponse(),
                    failedResponse(Code.UNAVAILABLE_VALUE),
                    failedResponse(Code.ABORTED_VALUE)));
            put(
                batchWrite(
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2"),
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc3")),
                mergeResponses(successResponse(2), failedResponse(Code.ABORTED_VALUE)));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc3")),
                successResponse(3));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result2 = bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result3 =
        bulkWriter.set(firestoreMock.document("coll/doc3"), LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close().get();

    try {
      result1.get();
      fail("set() should have failed");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof FirestoreException);
      assertEquals(Status.DEADLINE_EXCEEDED, ((FirestoreException) e.getCause()).getStatus());
    }
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result2.get().getUpdateTime());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(3, 0), result3.get().getUpdateTime());

    List<BatchWriteRequest> requests = batchWriteCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());
  }

  @Test
  public void doesNotSendBatchesIfSameDocIsInFlight() throws Exception {
    final SerialResponseStubber responseStubber =
        new SerialResponseStubber() {
          {
            put(
                batchWrite(
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1"),
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2")),
                mergeResponses(successResponse(1), successResponse(2)));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                successResponse(3));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);
    bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);

    // Schedule flush on separate thread to avoid blocking main thread while waiting for
    // activeRequestComplete.
    ScheduledFuture<ApiFuture<Void>> flush1 =
        Executors.newSingleThreadScheduledExecutor()
            .schedule(
                new Callable<ApiFuture<Void>>() {
                  public ApiFuture<Void> call() {
                    return bulkWriter.flush();
                  }
                },
                0,
                TimeUnit.MILLISECONDS);

    // Wait for flush() to perform logic and reach the stubbed response. This simulates a first
    // batch that has been sent with its response still pending.
    responseStubber.awaitRequest();

    bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<Void> flush2 = bulkWriter.flush();

    // Wait for flush() to receive its response and process the batch.
    responseStubber.markAllRequestsComplete();
    flush1.get().get();
    flush2.get();
    bulkWriter.close().get();

    List<BatchWriteRequest> requests = batchWriteCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());

    verifyRequests(requests, responseStubber);
  }

  @Test
  public void doesNotSendBatchesIfDoingSoExceedsRateLimit() throws Exception {
    // The test is considered a success if BulkWriter tries to send the second batch again after a
    // timeout.

    final List<Write> requests1 = new ArrayList<>();
    final List<ApiFuture<BatchWriteResponse>> responses1 = new ArrayList<>();
    for (int i = 0; i < 500; ++i) {
      requests1.add(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc" + i));
      responses1.add(successResponse(i));
    }
    List<Integer> arrayRange2 = Arrays.asList(500, 501, 502, 503, 504);
    final List<Write> requests2 = new ArrayList<>();
    final List<ApiFuture<BatchWriteResponse>> responses2 = new ArrayList<>();
    for (int i : arrayRange2) {
      requests2.add(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc" + i));
      responses2.add(successResponse(i));
    }

    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(requests1.toArray(new Write[500])),
                mergeResponses(responses1.toArray(new ApiFuture[500])));
            put(
                batchWrite(requests2.toArray(new Write[5])),
                mergeResponses(responses2.toArray(new ApiFuture[5])));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    for (int i = 0; i < 500; ++i) {
      bulkWriter.set(firestoreMock.document("coll/doc" + i), LocalFirestoreHelper.SINGLE_FIELD_MAP);
    }
    bulkWriter.flush();

    final boolean[] mockCalled = {false};
    ScheduledExecutorService mockScheduledExecutorService =
        Mockito.mock(ScheduledExecutorService.class);
    doReturn(mockScheduledExecutorService).when(firestoreRpc).getExecutor();
    Mockito.doAnswer(
            new Answer<ScheduledFuture<BatchWriteResponse>>() {
              public ScheduledFuture<BatchWriteResponse> answer(InvocationOnMock invocation) {
                // Calling this mocked method means that the batch was scheduled after a delay.
                // Mark the test as success and verify that a delay was scheduled.
                mockCalled[0] = true;
                assertTrue(invocation.getArgumentAt(1, Long.class) > 0);
                return null;
              }
            })
        .when(mockScheduledExecutorService)
        .schedule(any(Runnable.class), anyInt(), any(TimeUnit.class));

    for (int i = 500; i < 505; ++i) {
      bulkWriter.set(firestoreMock.document("coll/doc" + i), LocalFirestoreHelper.SINGLE_FIELD_MAP);
    }
    bulkWriter.flush();

    assertTrue(mockCalled[0]);
  }

  @Test
  public void flushSucceedsEvenIfBulkCommitFails() throws Exception {
    doThrow(new IllegalStateException("Mock batchWrite failed in test"))
        .when(firestoreMock)
        .sendRequest(
            batchWriteCapture.capture(),
            Matchers.<UnaryCallable<BatchWriteRequest, BatchWriteResponse>>any());
    bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.flush().get();
  }

  @Test
  public void closeSucceedsEvenIfBulkCommitFails() throws Exception {
    doThrow(new IllegalStateException("Mock batchWrite failed in test"))
        .when(firestoreMock)
        .sendRequest(
            batchWriteCapture.capture(),
            Matchers.<UnaryCallable<BatchWriteRequest, BatchWriteResponse>>any());
    bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close().get();
  }

  @Test
  public void individualWritesErrorIfBulkCommitFails() throws Exception {
    doThrow(
            FirestoreException.serverRejected(
                Status.DEADLINE_EXCEEDED, "Mock batchWrite failed in test"))
        .when(firestoreMock)
        .sendRequest(
            batchWriteCapture.capture(),
            Matchers.<UnaryCallable<BatchWriteRequest, BatchWriteResponse>>any());
    int opCount = 0;
    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result2 = bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close().get();

    for (ApiFuture<WriteResult> result : Arrays.asList(result1, result2)) {
      try {
        result.get();
      } catch (Exception e) {
        assertTrue(e.getMessage().contains("Mock batchWrite failed in test"));
        ++opCount;
      }
    }
    assertEquals(2, opCount);
  }

  @Test
  public void individualWritesErrorIfBulkCommitFailsWithNonFirestoreException() throws Exception {
    doThrow(new IllegalStateException("Mock batchWrite failed in test"))
        .when(firestoreMock)
        .sendRequest(
            batchWriteCapture.capture(),
            Matchers.<UnaryCallable<BatchWriteRequest, BatchWriteResponse>>any());
    int opCount = 0;
    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result2 = bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close().get();

    for (ApiFuture<WriteResult> result : Arrays.asList(result1, result2)) {
      try {
        result.get();
      } catch (Exception e) {
        assertTrue(
            e.getMessage()
                .contains("java.lang.IllegalStateException: Mock batchWrite failed in test"));
        ++opCount;
      }
    }
    assertEquals(2, opCount);
  }

  @Test
  public void retriesWritesWhenBatchWriteFailsWithRetryableError() throws Exception {
    FirestoreException retryableError =
        FirestoreException.serverRejected(
            Status.fromCode(Status.Code.ABORTED), "Mock batchWrite failed in test");

    doThrow(retryableError)
        .doThrow(retryableError)
        .doThrow(retryableError)
        .doReturn(successResponse(3))
        .when(firestoreMock)
        .sendRequest(
            batchWriteCapture.capture(),
            Matchers.<UnaryCallable<BatchWriteRequest, BatchWriteResponse>>any());

    ApiFuture<WriteResult> result = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close().get();

    assertEquals(Timestamp.ofTimeSecondsAndNanos(3, 0), result.get().getUpdateTime());
  }

  @Test
  public void failsWritesAfterAllRetryAttemptsFail() throws Exception {
    doThrow(
            FirestoreException.serverRejected(
                Status.fromCode(Status.Code.ABORTED), "Mock batchWrite failed in test"))
        .when(firestoreMock)
        .sendRequest(
            batchWriteCapture.capture(),
            Matchers.<UnaryCallable<BatchWriteRequest, BatchWriteResponse>>any());
    int opCount = 0;
    ApiFuture<WriteResult> result = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close().get();

    try {
      result.get();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Mock batchWrite failed in test"));
      ++opCount;
    }
    assertEquals(1, opCount);
  }
}
