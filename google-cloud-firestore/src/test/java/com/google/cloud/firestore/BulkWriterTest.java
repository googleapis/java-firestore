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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.BulkWriter.WriteErrorCallback;
import com.google.cloud.firestore.BulkWriter.WriteResultCallback;
import com.google.cloud.firestore.LocalFirestoreHelper.ResponseStubber;
import com.google.cloud.firestore.spi.v1.FirestoreRpc;
import com.google.firestore.v1.BatchWriteRequest;
import com.google.firestore.v1.BatchWriteResponse;
import com.google.rpc.Code;
import io.grpc.Status;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
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

  @Rule public Timeout timeout = new Timeout(500, TimeUnit.MILLISECONDS);

  @Spy private final FirestoreRpc firestoreRpc = Mockito.mock(FirestoreRpc.class);

  /** Executor that executes delayed tasks without delay. */
  private final ScheduledExecutorService immediateExecutor =
      new ScheduledThreadPoolExecutor(1) {
        @Override
        @Nonnull
        public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
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
    bulkWriter.close();

    responseStubber.verifyAllRequestsSent();
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
    bulkWriter.flush().get();

    responseStubber.verifyAllRequestsSent();
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
    bulkWriter.close();

    responseStubber.verifyAllRequestsSent();
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
    bulkWriter.flush().get();

    responseStubber.verifyAllRequestsSent();
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
    bulkWriter.close();

    responseStubber.verifyAllRequestsSent();
    try {
      result.get();
      fail("set() should have failed");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof BulkWriterException);
      assertEquals(Status.DEADLINE_EXCEEDED, ((BulkWriterException) e.getCause()).getStatus());
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
    bulkWriter.close();

    responseStubber.verifyAllRequestsSent();
    assertEquals(Timestamp.ofTimeSecondsAndNanos(1, 0), result1.get().getUpdateTime());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result2.get().getUpdateTime());
  }

  @Test
  public void cannotCallMethodsAfterClose() throws Exception {
    String expected = "BulkWriter has already been closed.";
    bulkWriter.close();
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
  public void sendsWritesToSameDocInDifferentBatches() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                successResponse(1));
            put(
                batchWrite(update(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                successResponse(2));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    // Create another document reference pointing to the same document.
    DocumentReference sameDoc = firestoreMock.document(doc1.getPath());
    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result2 =
        bulkWriter.update(sameDoc, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close();

    responseStubber.verifyAllRequestsSent();
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
    bulkWriter.close();

    responseStubber.verifyAllRequestsSent();
    assertEquals(Timestamp.ofTimeSecondsAndNanos(1, 0), result1.get().getUpdateTime());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result2.get().getUpdateTime());
  }

  @Test
  public void runsSuccessHandler() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(
                    create(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1"),
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2"),
                    update(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc3"),
                    delete("coll/doc4")),
                mergeResponses(
                    successResponse(1),
                    successResponse(2),
                    successResponse(3),
                    successResponse(4)));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    final List<Integer> writeResults = new ArrayList();
    DocumentReference doc3 = firestoreMock.document("coll/doc3");
    DocumentReference doc4 = firestoreMock.document("coll/doc4");
    bulkWriter.addWriteResultListener(
        new WriteResultCallback() {
          public void onResult(DocumentReference documentReference, WriteResult result) {
            writeResults.add((int) result.getUpdateTime().getSeconds());
          }
        });
    bulkWriter.create(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.update(doc3, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.delete(doc4);
    bulkWriter.close();
    assertArrayEquals(new Integer[] {1, 2, 3, 4}, writeResults.toArray());
  }

  @Test
  public void retriesFailedOperationsWithGlobalErrorCallback() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(
                    create(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1"),
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2"),
                    update(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc3"),
                    delete("coll/doc4")),
                mergeResponses(
                    successResponse(1),
                    failedResponse(Code.INTERNAL_VALUE),
                    failedResponse(Code.INTERNAL_VALUE),
                    failedResponse(Code.INTERNAL_VALUE)));
            put(
                batchWrite(
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2"),
                    update(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc3"),
                    delete("coll/doc4")),
                mergeResponses(successResponse(2), successResponse(3), successResponse(4)));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    final List<Integer> writeResults = new ArrayList<>();
    final List<String> operations = new ArrayList<>();
    DocumentReference doc3 = firestoreMock.document("coll/doc3");
    DocumentReference doc4 = firestoreMock.document("coll/doc4");
    Executor userCallbackExecutor = Executors.newSingleThreadExecutor();
    bulkWriter.addWriteErrorListener(
        userCallbackExecutor,
        new WriteErrorCallback() {
          public boolean onError(BulkWriterException error) {
            operations.add(error.getOperationType().name());
            return true;
          }
        });
    bulkWriter.addWriteResultListener(
        new WriteResultCallback() {
          public void onResult(DocumentReference documentReference, WriteResult result) {
            operations.add("SUCCESS");
            writeResults.add((int) result.getUpdateTime().getSeconds());
          }
        });
    bulkWriter.create(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.update(doc3, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.delete(doc4);
    bulkWriter.close();
    assertArrayEquals(new Integer[] {1, 2, 3, 4}, writeResults.toArray());
    assertArrayEquals(
        new String[] {"SUCCESS", "SET", "UPDATE", "DELETE", "SUCCESS", "SUCCESS", "SUCCESS"},
        operations.toArray());
  }

  @Test
  public void errorSurfacedEvenWithRetryFunction() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                failedResponse(Code.INTERNAL_VALUE));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    final boolean[] errorListenerCalled = {false};
    bulkWriter.addWriteErrorListener(
        new WriteErrorCallback() {
          public boolean onError(BulkWriterException error) {
            errorListenerCalled[0] = true;
            assertEquals(Status.INTERNAL, error.getStatus());
            return false;
          }
        });

    ApiFuture<WriteResult> result = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close();
    assertTrue(errorListenerCalled[0]);
    try {
      result.get();
      fail("Operation should have failed in test");
    } catch (Exception e) {
      assertEquals(Status.INTERNAL, ((BulkWriterException) e.getCause()).getStatus());
    }
  }

  @Test
  public void surfacesExceptionsThrownByUserProvidedErrorListener() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                failedResponse(Code.INTERNAL_VALUE));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    bulkWriter.addWriteErrorListener(
        new WriteErrorCallback() {
          public boolean onError(BulkWriterException error) {
            throw new UnsupportedOperationException(
                "Test code threw UnsupportedOperationException");
          }
        });

    ApiFuture<WriteResult> result = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close();
    try {
      result.get();
      fail("Operation should have failed in test");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Test code threw UnsupportedOperationException"));
    }
  }

  @Test
  public void writeFailsIfUserProvidedSuccessListenerFails() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                successResponse(1));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    bulkWriter.addWriteResultListener(
        new WriteResultCallback() {
          public void onResult(DocumentReference documentReference, WriteResult result) {
            throw new UnsupportedOperationException(
                "Test code threw UnsupportedOperationException");
          }
        });

    ApiFuture<WriteResult> result = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close();
    try {
      result.get();
      fail("Operation should have failed in test");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Test code threw UnsupportedOperationException"));
    }
  }

  @Test
  public void retriesMultipleTimes() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                failedResponse(Code.INTERNAL_VALUE));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                failedResponse(Code.INTERNAL_VALUE));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                failedResponse(Code.INTERNAL_VALUE));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                successResponse(1));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    bulkWriter.addWriteErrorListener(
        new WriteErrorCallback() {
          public boolean onError(BulkWriterException error) {
            return true;
          }
        });

    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close();
    assertEquals(Timestamp.ofTimeSecondsAndNanos(1, 0), result1.get().getUpdateTime());
  }

  @Test
  public void retriesMaintainCorrectWriteResolutionOrdering() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                failedResponse(Code.INTERNAL_VALUE));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                successResponse(1));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2")),
                successResponse(2));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    // Use separate futures to track listener completion since the callbacks are run on a different
    // thread than the BulkWriter operations.
    final SettableApiFuture<Void> flushComplete = SettableApiFuture.create();
    final SettableApiFuture<Void> closeComplete = SettableApiFuture.create();

    final List<String> operations = new ArrayList<>();
    bulkWriter.addWriteErrorListener(
        new WriteErrorCallback() {
          public boolean onError(BulkWriterException error) {
            return true;
          }
        });

    bulkWriter
        .set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP)
        .addListener(
            new Runnable() {
              public void run() {
                operations.add("BEFORE_FLUSH");
              }
            },
            executor);
    bulkWriter
        .flush()
        .addListener(
            new Runnable() {
              public void run() {
                operations.add("FLUSH");
                flushComplete.set(null);
              }
            },
            executor);
    bulkWriter
        .set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP)
        .addListener(
            new Runnable() {
              public void run() {
                operations.add("AFTER_FLUSH");
                closeComplete.set(null);
              }
            },
            executor);
    flushComplete.get();

    // Verify that the 2nd operation did not complete as a result of the flush() call.
    assertArrayEquals(new String[] {"BEFORE_FLUSH", "FLUSH"}, operations.toArray());
    bulkWriter.close();
    closeComplete.get();
    assertArrayEquals(new String[] {"BEFORE_FLUSH", "FLUSH", "AFTER_FLUSH"}, operations.toArray());
  }

  @Test
  public void returnsTheErrorIfNoRetrySpecified() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                failedResponse(Code.INTERNAL_VALUE));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                failedResponse(Code.INTERNAL_VALUE));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                failedResponse(Code.INTERNAL_VALUE));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1")),
                failedResponse(Code.INTERNAL_VALUE));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    bulkWriter.addWriteErrorListener(
        new WriteErrorCallback() {
          public boolean onError(BulkWriterException error) {
            return error.getFailedAttempts() < 3;
          }
        });

    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close();
    try {
      result1.get();
      fail("Operation should have failed");
    } catch (Exception e) {
      assertEquals(Status.INTERNAL, ((BulkWriterException) e.getCause()).getStatus());
    }
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

    assertEquals(Timestamp.ofTimeSecondsAndNanos(1, 0), result1.get().getUpdateTime());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result2.get().getUpdateTime());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(3, 0), result3.get().getUpdateTime());

    responseStubber.verifyAllRequestsSent();
  }

  @Test
  public void retriesIndividualWritesThatFailWithAbortedOrUnavailable() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1"),
                    set(LocalFirestoreHelper.UPDATED_FIELD_PROTO, "coll/doc2"),
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc3")),
                mergeResponses(
                    failedResponse(),
                    failedResponse(Code.UNAVAILABLE_VALUE),
                    failedResponse(Code.ABORTED_VALUE)));
            put(
                batchWrite(
                    set(LocalFirestoreHelper.UPDATED_FIELD_PROTO, "coll/doc2"),
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc3")),
                mergeResponses(successResponse(2), failedResponse(Code.ABORTED_VALUE)));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc3")),
                successResponse(3));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    // Test writes to the same document in order to verify that retry logic unaffected by document
    // key.
    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result2 = bulkWriter.set(doc2, LocalFirestoreHelper.UPDATED_FIELD_MAP);
    ApiFuture<WriteResult> result3 =
        bulkWriter.set(firestoreMock.document("coll/doc3"), LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close();

    try {
      result1.get();
      fail("set() should have failed");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof BulkWriterException);
      assertEquals(Status.DEADLINE_EXCEEDED, ((BulkWriterException) e.getCause()).getStatus());
    }
    assertEquals(Timestamp.ofTimeSecondsAndNanos(2, 0), result2.get().getUpdateTime());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(3, 0), result3.get().getUpdateTime());

    responseStubber.verifyAllRequestsSent();
  }

  @Test
  public void writesCompleteInCorrectOrderBeforeFlush() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1"),
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2")),
                mergeResponses(successResponse(1), failedResponse(Code.ABORTED_VALUE)));
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2")),
                successResponse(2));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);
    final List<String> completions = new ArrayList<>();
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    final SettableApiFuture<Void> flushComplete = SettableApiFuture.create();

    bulkWriter
        .set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP)
        .addListener(
            new Runnable() {
              public void run() {
                completions.add("doc1");
              }
            },
            executor);
    bulkWriter
        .set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP)
        .addListener(
            new Runnable() {
              public void run() {
                completions.add("doc2");
              }
            },
            executor);
    ;

    ApiFuture<Void> flush = bulkWriter.flush();
    flush.addListener(
        new Runnable() {
          public void run() {
            completions.add("flush");
            flushComplete.set(null);
          }
        },
        executor);

    flushComplete.get();
    assertEquals("doc1", completions.get(0));
    assertEquals("doc2", completions.get(1));
    assertEquals("flush", completions.get(2));
  }

  @Test
  public void flushCompletesWhenAllWritesComplete() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc1"),
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc2"),
                    set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc3")),
                mergeResponses(failedResponse(), successResponse(1), successResponse(1)));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);

    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result2 = bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result3 =
        bulkWriter.set(firestoreMock.document("coll/doc3"), LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close();
    assertTrue(result1.isDone());
    assertTrue(result2.isDone());
    assertTrue(result3.isDone());
  }

  @Test
  public void doesNotSendBatchesIfDoingSoExceedsRateLimit() throws Exception {
    final boolean[] timeoutCalled = {false};

    final ScheduledExecutorService timeoutExecutor =
        new ScheduledThreadPoolExecutor(1) {
          @Override
          @Nonnull
          public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
            if (delay > 0) {
              timeoutCalled[0] = true;
            }
            return super.schedule(command, 0, TimeUnit.MILLISECONDS);
          }
        };

    // Stub responses for the BulkWriter batches.
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(
                batchWrite(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, "coll/doc")),
                successResponse(5));
          }
        };
    responseStubber.initializeStub(batchWriteCapture, firestoreMock);
    BulkWriter bulkWriter =
        firestoreMock.bulkWriter(
            BulkWriterOptions.builder().setInitialOpsPerSecond(5).build(), timeoutExecutor);

    for (int i = 0; i < 600; ++i) {
      bulkWriter.set(firestoreMock.document("coll/doc"), LocalFirestoreHelper.SINGLE_FIELD_MAP);
    }
    bulkWriter.flush();

    assertTrue(timeoutCalled[0]);
  }

  @Test
  public void flushSucceedsEvenIfBulkCommitFails() throws Exception {
    doReturn(
            ApiFutures.immediateFailedFuture(
                new IllegalStateException("Mock batchWrite failed in test")))
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
    doReturn(
            ApiFutures.immediateFailedFuture(
                new IllegalStateException("Mock batchWrite failed in test")))
        .when(firestoreMock)
        .sendRequest(
            batchWriteCapture.capture(),
            Matchers.<UnaryCallable<BatchWriteRequest, BatchWriteResponse>>any());
    bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close();
  }

  @Test
  public void individualWritesErrorIfBulkCommitFails() throws Exception {
    doReturn(
            ApiFutures.immediateFailedFuture(
                FirestoreException.serverRejected(
                    Status.DEADLINE_EXCEEDED, "Mock batchWrite failed in test")))
        .when(firestoreMock)
        .sendRequest(
            batchWriteCapture.capture(),
            Matchers.<UnaryCallable<BatchWriteRequest, BatchWriteResponse>>any());
    int opCount = 0;
    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result2 = bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close();

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
    doReturn(
            ApiFutures.immediateFailedFuture(
                new IllegalStateException("Mock batchWrite failed in test")))
        .when(firestoreMock)
        .sendRequest(
            batchWriteCapture.capture(),
            Matchers.<UnaryCallable<BatchWriteRequest, BatchWriteResponse>>any());
    int opCount = 0;
    ApiFuture<WriteResult> result1 = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    ApiFuture<WriteResult> result2 = bulkWriter.set(doc2, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close();

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

    ApiFuture<Void> errorFuture = ApiFutures.immediateFailedFuture(retryableError);

    doReturn(errorFuture)
        .doReturn(errorFuture)
        .doReturn(errorFuture)
        .doReturn(successResponse(3))
        .when(firestoreMock)
        .sendRequest(
            batchWriteCapture.capture(),
            Matchers.<UnaryCallable<BatchWriteRequest, BatchWriteResponse>>any());

    ApiFuture<WriteResult> result = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.close();

    assertEquals(Timestamp.ofTimeSecondsAndNanos(3, 0), result.get().getUpdateTime());
  }

  @Test
  public void failsWritesAfterAllRetryAttemptsFail() throws Exception {
    final int[] retryAttempts = {0};
    doAnswer(
            new Answer() {
              public ApiFuture<Object> answer(InvocationOnMock mock) {
                retryAttempts[0]++;
                return ApiFutures.immediateFailedFuture(
                    FirestoreException.serverRejected(
                        Status.fromCode(Status.Code.ABORTED), "Mock batchWrite failed in test"));
              }
            })
        .when(firestoreMock)
        .sendRequest(
            batchWriteCapture.capture(),
            Matchers.<UnaryCallable<BatchWriteRequest, BatchWriteResponse>>any());
    ApiFuture<WriteResult> result = bulkWriter.set(doc1, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    bulkWriter.flush().get();

    try {
      result.get();
      Assert.fail("Expected set() operation to fail");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Mock batchWrite failed in test"));
      assertEquals(retryAttempts[0], BulkWriter.MAX_RETRY_ATTEMPTS + 1);
    }
  }

  @Test
  public void optionsRequiresPositiveInteger() throws Exception {
    try {
      firestoreMock.bulkWriter(BulkWriterOptions.builder().setInitialOpsPerSecond(-1).build());
      fail("bulkWriter() call should have failed");
    } catch (Exception e) {
      assertEquals(
          e.getMessage(),
          "Value for argument 'initialOpsPerSecond' must be greater than 1, but was: -1");
    }

    try {
      firestoreMock.bulkWriter(BulkWriterOptions.builder().setMaxOpsPerSecond(-1).build());
      fail("bulkWriter() call should have failed");
    } catch (Exception e) {
      assertEquals(
          e.getMessage(),
          "Value for argument 'maxOpsPerSecond' must be greater than 1, but was: -1");
    }
  }

  @Test
  public void optionsRequiresMaxGreaterThanInitial() throws Exception {
    try {
      firestoreMock.bulkWriter(
          BulkWriterOptions.builder().setInitialOpsPerSecond(550).setMaxOpsPerSecond(500).build());
      fail("bulkWriter() call should have failed");
    } catch (Exception e) {
      assertEquals(e.getMessage(), "'maxOpsPerSecond' cannot be less than 'initialOpsPerSecond'.");
    }
  }

  @Test
  public void cannotSetThrottlingOptionsWithThrottlingDisabled() throws Exception {
    try {
      firestoreMock.bulkWriter(
          BulkWriterOptions.builder()
              .setThrottlingEnabled(false)
              .setInitialOpsPerSecond(500)
              .build());
      fail("bulkWriter() call should have failed");
    } catch (Exception e) {
      assertEquals(
          e.getMessage(),
          "Cannot set 'initialOpsPerSecond' or 'maxOpsPerSecond' when 'throttlingEnabled' is set to false.");
    }

    try {
      firestoreMock.bulkWriter(
          BulkWriterOptions.builder().setThrottlingEnabled(false).setMaxOpsPerSecond(500).build());
      fail("bulkWriter() call should have failed");
    } catch (Exception e) {
      assertEquals(
          e.getMessage(),
          "Cannot set 'initialOpsPerSecond' or 'maxOpsPerSecond' when 'throttlingEnabled' is set to false.");
    }
  }

  @Test
  public void optionsInitialAndMaxRatesAreProperlySet() throws Exception {
    BulkWriter bulkWriter =
        firestoreMock.bulkWriter(
            BulkWriterOptions.builder()
                .setInitialOpsPerSecond(500)
                .setMaxOpsPerSecond(550)
                .build());
    assertEquals(bulkWriter.getRateLimiter().getInitialCapacity(), 500);
    assertEquals(bulkWriter.getRateLimiter().getMaximumRate(), 550);

    bulkWriter =
        firestoreMock.bulkWriter(BulkWriterOptions.builder().setMaxOpsPerSecond(1000).build());
    assertEquals(bulkWriter.getRateLimiter().getInitialCapacity(), 500);
    assertEquals(bulkWriter.getRateLimiter().getMaximumRate(), 1000);

    bulkWriter =
        firestoreMock.bulkWriter(BulkWriterOptions.builder().setInitialOpsPerSecond(100).build());
    assertEquals(bulkWriter.getRateLimiter().getInitialCapacity(), 100);
    assertEquals(bulkWriter.getRateLimiter().getMaximumRate(), Integer.MAX_VALUE);

    bulkWriter =
        firestoreMock.bulkWriter(BulkWriterOptions.builder().setMaxOpsPerSecond(100).build());
    assertEquals(bulkWriter.getRateLimiter().getInitialCapacity(), 100);
    assertEquals(bulkWriter.getRateLimiter().getMaximumRate(), 100);

    bulkWriter = firestoreMock.bulkWriter();
    assertEquals(
        bulkWriter.getRateLimiter().getInitialCapacity(),
        BulkWriter.DEFAULT_STARTING_MAXIMUM_OPS_PER_SECOND);
    assertEquals(bulkWriter.getRateLimiter().getMaximumRate(), Integer.MAX_VALUE);

    bulkWriter =
        firestoreMock.bulkWriter(BulkWriterOptions.builder().setThrottlingEnabled(false).build());
    assertEquals(bulkWriter.getRateLimiter().getInitialCapacity(), Integer.MAX_VALUE);
    assertEquals(bulkWriter.getRateLimiter().getMaximumRate(), Integer.MAX_VALUE);
  }
}
