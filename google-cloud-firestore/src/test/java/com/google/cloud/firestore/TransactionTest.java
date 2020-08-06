/*
 * Copyright 2017 Google LLC
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

import static com.google.cloud.firestore.LocalFirestoreHelper.IMMEDIATE_RETRY_SETTINGS;
import static com.google.cloud.firestore.LocalFirestoreHelper.SINGLE_FIELD_PROTO;
import static com.google.cloud.firestore.LocalFirestoreHelper.TRANSACTION_ID;
import static com.google.cloud.firestore.LocalFirestoreHelper.begin;
import static com.google.cloud.firestore.LocalFirestoreHelper.beginResponse;
import static com.google.cloud.firestore.LocalFirestoreHelper.commit;
import static com.google.cloud.firestore.LocalFirestoreHelper.commitResponse;
import static com.google.cloud.firestore.LocalFirestoreHelper.create;
import static com.google.cloud.firestore.LocalFirestoreHelper.delete;
import static com.google.cloud.firestore.LocalFirestoreHelper.get;
import static com.google.cloud.firestore.LocalFirestoreHelper.getAll;
import static com.google.cloud.firestore.LocalFirestoreHelper.getAllResponse;
import static com.google.cloud.firestore.LocalFirestoreHelper.query;
import static com.google.cloud.firestore.LocalFirestoreHelper.queryResponse;
import static com.google.cloud.firestore.LocalFirestoreHelper.rollback;
import static com.google.cloud.firestore.LocalFirestoreHelper.rollbackResponse;
import static com.google.cloud.firestore.LocalFirestoreHelper.set;
import static com.google.cloud.firestore.LocalFirestoreHelper.update;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.grpc.GrpcStatusCode;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.LocalFirestoreHelper.ResponseStubber;
import com.google.cloud.firestore.TransactionOptions.EitherReadOnlyOrReadWrite;
import com.google.cloud.firestore.TransactionOptions.ReadOnlyOptions;
import com.google.cloud.firestore.TransactionOptions.ReadOnlyOptionsBuilder;
import com.google.cloud.firestore.TransactionOptions.ReadWriteOptions;
import com.google.cloud.firestore.TransactionOptions.ReadWriteOptionsBuilder;
import com.google.cloud.firestore.TransactionOptions.TransactionOptionsType;
import com.google.cloud.firestore.spi.v1.FirestoreRpc;
import com.google.firestore.v1.BatchGetDocumentsRequest;
import com.google.firestore.v1.DocumentMask;
import com.google.firestore.v1.TransactionOptions.ReadOnly;
import com.google.firestore.v1.TransactionOptions.ReadWrite;
import com.google.firestore.v1.Write;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import io.grpc.Status;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class TransactionTest {

  private final ApiFuture<GeneratedMessageV3> RETRYABLE_API_EXCEPTION =
      ApiFutures.immediateFailedFuture(
          new ApiException(
              new Exception("Test exception"), GrpcStatusCode.of(Status.Code.UNKNOWN), true));

  @Spy private FirestoreRpc firestoreRpc = mock(FirestoreRpc.class);

  @Spy
  private FirestoreImpl firestoreMock =
      new FirestoreImpl(
          FirestoreOptions.newBuilder()
              .setProjectId("test-project")
              .setRetrySettings(IMMEDIATE_RETRY_SETTINGS)
              .build(),
          firestoreRpc);

  @Captor private ArgumentCaptor<Message> requestCapture;
  @Captor private ArgumentCaptor<ApiStreamObserver<Message>> streamObserverCapture;

  private DocumentReference documentReference;
  private Query queryReference;
  private TransactionOptions options;

  @Before
  public void before() {
    doReturn(Executors.newSingleThreadScheduledExecutor()).when(firestoreRpc).getExecutor();

    documentReference = firestoreMock.document("coll/doc");
    queryReference = firestoreMock.collection("coll");
    options =
        TransactionOptions.create(
            Executors.newSingleThreadExecutor(
                new ThreadFactory() {
                  @Override
                  public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread(runnable);
                    thread.setName("user_provided");
                    return thread;
                  }
                }));
  }

  @Test
  public void returnsValue() throws Exception {
    doReturn(beginResponse())
        .doReturn(commitResponse(0, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    ApiFuture<String> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<String>() {
              @Override
              public String updateCallback(Transaction transaction) {
                Assert.assertEquals("user_provided", Thread.currentThread().getName());
                return "foo";
              }
            },
            options);

    assertEquals("foo", transaction.get());

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(2, requests.size());

    assertEquals(begin(), requests.get(0));
    assertEquals(commit(TRANSACTION_ID), requests.get(1));
  }

  @Test
  public void returnsValueAsync() throws Exception {
    doReturn(beginResponse())
        .doReturn(commitResponse(0, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    ApiFuture<String> transaction =
        firestoreMock.runAsyncTransaction(
            new Transaction.AsyncFunction<String>() {
              @Override
              public ApiFuture<String> updateCallback(Transaction transaction) {
                Assert.assertEquals("user_provided", Thread.currentThread().getName());
                return ApiFutures.immediateFuture("foo");
              }
            },
            options);

    assertEquals("foo", transaction.get());

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(2, requests.size());

    assertEquals(begin(), requests.get(0));
    assertEquals(commit(TRANSACTION_ID), requests.get(1));
  }

  @Test
  public void canReturnNull() throws Exception {
    doReturn(beginResponse())
        .doReturn(commitResponse(0, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    ApiFuture<String> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<String>() {
              @Override
              public String updateCallback(Transaction transaction) {
                return null;
              }
            },
            options);

    assertNull(transaction.get());
  }

  @Test
  public void canReturnNullAsync() throws Exception {
    doReturn(beginResponse())
        .doReturn(commitResponse(0, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    ApiFuture<String> transaction =
        firestoreMock.runAsyncTransaction(
            new Transaction.AsyncFunction<String>() {
              @Override
              public ApiFuture<String> updateCallback(Transaction transaction) {
                return ApiFutures.immediateFuture(null);
              }
            },
            options);

    assertNull(transaction.get());
  }

  @Test
  public void rollbackOnCallbackError() {
    doReturn(beginResponse())
        .doReturn(rollbackResponse())
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    ApiFuture<String> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<String>() {
              @Override
              public String updateCallback(Transaction transaction) throws Exception {
                throw new Exception("Expected exception");
              }
            },
            options);

    try {
      transaction.get();
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().endsWith("Expected exception"));
    }

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(2, requests.size());

    assertEquals(begin(), requests.get(0));
    assertEquals(rollback(), requests.get(1));
  }

  @Test
  public void rollbackOnCallbackErrorAsync() {
    doReturn(beginResponse())
        .doReturn(rollbackResponse())
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    ApiFuture<String> transaction =
        firestoreMock.runAsyncTransaction(
            new Transaction.AsyncFunction<String>() {
              @Override
              public ApiFuture<String> updateCallback(Transaction transaction) {
                return ApiFutures.immediateFailedFuture(new Exception("Expected exception"));
              }
            },
            options);

    try {
      transaction.get();
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().endsWith("Expected exception"));
    }

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(2, requests.size());

    assertEquals(begin(), requests.get(0));
    assertEquals(rollback(), requests.get(1));
  }

  @Test
  public void noRollbackOnBeginFailure() {
    doReturn(ApiFutures.immediateFailedFuture(new Exception("Expected exception")))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    ApiFuture<String> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<String>() {
              @Override
              public String updateCallback(Transaction transaction) {
                fail();
                return null;
              }
            },
            options);

    try {
      transaction.get();
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().endsWith("Expected exception"));
    }

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(1, requests.size());
  }

  @Test
  public void noRollbackOnBeginFailureAsync() {
    doReturn(ApiFutures.immediateFailedFuture(new Exception("Expected exception")))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    ApiFuture<String> transaction =
        firestoreMock.runAsyncTransaction(
            new Transaction.AsyncFunction<String>() {
              @Override
              public ApiFuture<String> updateCallback(Transaction transaction) {
                fail();
                return null;
              }
            },
            options);

    try {
      transaction.get();
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().endsWith("Expected exception"));
    }

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(1, requests.size());
  }

  @Test
  public void limitsRetriesWithFailure() {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(begin(), beginResponse("foo1"));
            put(commit("foo1"), RETRYABLE_API_EXCEPTION);
            put(rollback("foo1"), rollbackResponse());
            put(begin("foo1"), beginResponse("foo2"));
            put(commit("foo2"), RETRYABLE_API_EXCEPTION);
            put(rollback("foo2"), rollbackResponse());
            put(begin("foo2"), beginResponse("foo3"));
            put(commit("foo3"), RETRYABLE_API_EXCEPTION);
            put(rollback("foo3"), rollbackResponse());
            put(begin("foo3"), beginResponse("foo4"));
            put(commit("foo4"), RETRYABLE_API_EXCEPTION);
            put(rollback("foo4"), rollbackResponse());
            put(begin("foo4"), beginResponse("foo5"));
            put(commit("foo5"), RETRYABLE_API_EXCEPTION);
            put(rollback("foo5"), rollbackResponse());
          }
        };

    responseStubber.initializeStub(requestCapture, firestoreMock);

    final AtomicInteger retryCount = new AtomicInteger(1);

    ApiFuture<String> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<String>() {
              @Override
              public String updateCallback(Transaction transaction) {
                return "foo" + retryCount.getAndIncrement();
              }
            },
            TransactionOptions.create(options.getExecutor(), 5));

    try {
      transaction.get();
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().endsWith("Transaction was cancelled because of too many retries."));
    }

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());

    int index = 0;
    for (GeneratedMessageV3 request : responseStubber.keySet()) {
      assertEquals(request, requests.get(index++));
    }
  }

  @Test
  public void limitsRetriesWithSuccess() throws Exception {
    ResponseStubber responseStubber =
        new ResponseStubber() {
          {
            put(begin(), beginResponse("foo1"));
            put(commit("foo1"), RETRYABLE_API_EXCEPTION);
            put(rollback("foo1"), rollbackResponse());
            put(begin("foo1"), beginResponse("foo2"));
            put(commit("foo2"), RETRYABLE_API_EXCEPTION);
            put(rollback("foo2"), rollbackResponse());
            put(begin("foo2"), beginResponse("foo3"));
            put(commit("foo3"), RETRYABLE_API_EXCEPTION);
            put(rollback("foo3"), rollbackResponse());
            put(begin("foo3"), beginResponse("foo4"));
            put(commit("foo4"), RETRYABLE_API_EXCEPTION);
            put(rollback("foo4"), rollbackResponse());
            put(begin("foo4"), beginResponse("foo5"));
            put(commit("foo5"), RETRYABLE_API_EXCEPTION);
            put(rollback("foo5"), rollbackResponse());
            put(begin("foo5"), beginResponse("foo6"));
            put(commit("foo6"), commitResponse(0, 0));
          }
        };

    responseStubber.initializeStub(requestCapture, firestoreMock);

    final AtomicInteger retryCount = new AtomicInteger(1);

    ApiFuture<String> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<String>() {
              @Override
              public String updateCallback(Transaction transaction) {
                return "foo" + retryCount.getAndIncrement();
              }
            },
            TransactionOptions.create(options.getExecutor(), 6));

    assertEquals("foo6", transaction.get());

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(responseStubber.size(), requests.size());

    int index = 0;
    for (GeneratedMessageV3 request : responseStubber.keySet()) {
      assertEquals(request, requests.get(index++));
    }
  }

  @Test
  public void retriesBasedOnErrorCode() throws Exception {
    Map<Status.Code, Boolean> retryBehavior =
        new HashMap<Status.Code, Boolean>() {
          {
            put(Status.Code.CANCELLED, true);
            put(Status.Code.UNKNOWN, true);
            put(Status.Code.INVALID_ARGUMENT, false);
            put(Status.Code.DEADLINE_EXCEEDED, true);
            put(Status.Code.NOT_FOUND, false);
            put(Status.Code.ALREADY_EXISTS, false);
            put(Status.Code.RESOURCE_EXHAUSTED, true);
            put(Status.Code.FAILED_PRECONDITION, false);
            put(Status.Code.ABORTED, true);
            put(Status.Code.OUT_OF_RANGE, false);
            put(Status.Code.UNIMPLEMENTED, false);
            put(Status.Code.INTERNAL, true);
            put(Status.Code.UNAVAILABLE, true);
            put(Status.Code.DATA_LOSS, false);
            put(Status.Code.UNAUTHENTICATED, true);
          }
        };

    for (Map.Entry<Status.Code, Boolean> entry : retryBehavior.entrySet()) {
      StatusCode code = GrpcStatusCode.of(entry.getKey());
      boolean shouldRetry = entry.getValue();

      final ApiException apiException =
          new ApiException(new Exception("Test Exception"), code, shouldRetry);

      if (shouldRetry) {
        ResponseStubber responseStubber =
            new ResponseStubber() {
              {
                put(begin(), beginResponse("foo1"));
                put(
                    commit("foo1"),
                    ApiFutures.<GeneratedMessageV3>immediateFailedFuture(apiException));
                put(rollback("foo1"), rollbackResponse());
                put(begin("foo1"), beginResponse("foo2"));
                put(commit("foo2"), commitResponse(0, 0));
              }
            };
        responseStubber.initializeStub(requestCapture, firestoreMock);

        final int[] attempts = new int[] {0};

        ApiFuture<String> transaction =
            firestoreMock.runTransaction(
                new Transaction.Function<String>() {
                  @Override
                  public String updateCallback(Transaction transaction) {
                    ++attempts[0];
                    return null;
                  }
                });

        transaction.get();

        assertEquals(2, attempts[0]);
      } else {
        ResponseStubber responseStubber =
            new ResponseStubber() {
              {
                put(begin(), beginResponse("foo1"));
                put(
                    commit("foo1"),
                    ApiFutures.<GeneratedMessageV3>immediateFailedFuture(apiException));
                put(rollback("foo1"), rollbackResponse());
              }
            };

        responseStubber.initializeStub(requestCapture, firestoreMock);

        final int[] attempts = new int[] {0};

        ApiFuture<String> transaction =
            firestoreMock.runTransaction(
                new Transaction.Function<String>() {
                  @Override
                  public String updateCallback(Transaction transaction) {
                    ++attempts[0];
                    return null;
                  }
                });

        try {
          transaction.get();
          fail("Transaction should have failed");
        } catch (Exception ignored) {
        }

        assertEquals(1, attempts[0]);
      }
    }
  }

  @Test
  public void getDocument() throws Exception {
    doReturn(beginResponse())
        .doReturn(commitResponse(0, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    doAnswer(getAllResponse(SINGLE_FIELD_PROTO))
        .when(firestoreMock)
        .streamRequest(
            requestCapture.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable<Message, Message>>any());

    ApiFuture<DocumentSnapshot> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<DocumentSnapshot>() {
              @Override
              public DocumentSnapshot updateCallback(Transaction transaction)
                  throws ExecutionException, InterruptedException {
                return transaction.get(documentReference).get();
              }
            },
            options);

    assertEquals("doc", transaction.get().getId());

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(3, requests.size());

    assertEquals(begin(), requests.get(0));
    assertEquals(get(TRANSACTION_ID), requests.get(1));
    assertEquals(commit(TRANSACTION_ID), requests.get(2));
  }

  @Test
  public void getDocumentAsync() throws Exception {
    doReturn(beginResponse())
        .doReturn(commitResponse(0, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    doAnswer(getAllResponse(SINGLE_FIELD_PROTO))
        .when(firestoreMock)
        .streamRequest(
            requestCapture.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable<Message, Message>>any());

    ApiFuture<DocumentSnapshot> transaction =
        firestoreMock.runAsyncTransaction(
            new Transaction.AsyncFunction<DocumentSnapshot>() {
              @Override
              public ApiFuture<DocumentSnapshot> updateCallback(Transaction transaction) {
                return transaction.get(documentReference);
              }
            },
            options);

    assertEquals("doc", transaction.get().getId());

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(3, requests.size());

    assertEquals(begin(), requests.get(0));
    assertEquals(get(TRANSACTION_ID), requests.get(1));
    assertEquals(commit(TRANSACTION_ID), requests.get(2));
  }

  @Test
  public void getMultipleDocuments() throws Exception {
    final DocumentReference doc1 = firestoreMock.document("coll/doc1");
    final DocumentReference doc2 = firestoreMock.document("coll/doc2");

    doReturn(beginResponse())
        .doReturn(commitResponse(0, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    doAnswer(getAllResponse(SINGLE_FIELD_PROTO))
        .when(firestoreMock)
        .streamRequest(
            requestCapture.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable<Message, Message>>any());

    ApiFuture<List<DocumentSnapshot>> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<List<DocumentSnapshot>>() {
              @Override
              public List<DocumentSnapshot> updateCallback(Transaction transaction)
                  throws ExecutionException, InterruptedException {
                return transaction.getAll(doc1, doc2).get();
              }
            },
            options);

    assertEquals(2, transaction.get().size());

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(3, requests.size());

    assertEquals(begin(), requests.get(0));
    assertEquals(
        getAll(
            TRANSACTION_ID, doc1.getResourcePath().toString(), doc2.getResourcePath().toString()),
        requests.get(1));
    assertEquals(commit(TRANSACTION_ID), requests.get(2));
  }

  @Test
  public void getMultipleDocumentsWithFieldMask() throws Exception {
    doReturn(beginResponse())
        .doReturn(commitResponse(0, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    doAnswer(getAllResponse(SINGLE_FIELD_PROTO))
        .when(firestoreMock)
        .streamRequest(
            requestCapture.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable>any());

    final DocumentReference doc1 = firestoreMock.document("coll/doc1");
    final FieldMask fieldMask = FieldMask.of(FieldPath.of("foo", "bar"));

    ApiFuture<List<DocumentSnapshot>> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<List<DocumentSnapshot>>() {
              @Override
              public List<DocumentSnapshot> updateCallback(Transaction transaction)
                  throws ExecutionException, InterruptedException {
                return transaction.getAll(new DocumentReference[] {doc1}, fieldMask).get();
              }
            },
            options);
    transaction.get();

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(3, requests.size());

    assertEquals(begin(), requests.get(0));
    BatchGetDocumentsRequest expectedGetAll =
        getAll(TRANSACTION_ID, doc1.getResourcePath().toString());
    expectedGetAll =
        expectedGetAll
            .toBuilder()
            .setMask(DocumentMask.newBuilder().addFieldPaths("foo.bar"))
            .build();
    assertEquals(expectedGetAll, requests.get(1));
    assertEquals(commit(TRANSACTION_ID), requests.get(2));
  }

  @Test
  public void getQuery() throws Exception {
    doReturn(beginResponse())
        .doReturn(commitResponse(0, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    doAnswer(queryResponse())
        .when(firestoreMock)
        .streamRequest(
            requestCapture.capture(),
            streamObserverCapture.capture(),
            Matchers.<ServerStreamingCallable<Message, Message>>any());

    ApiFuture<QuerySnapshot> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<QuerySnapshot>() {
              @Override
              public QuerySnapshot updateCallback(Transaction transaction)
                  throws ExecutionException, InterruptedException {
                return transaction.get(queryReference).get();
              }
            },
            options);

    assertEquals(1, transaction.get().size());

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(3, requests.size());

    assertEquals(begin(), requests.get(0));
    assertEquals(query(TRANSACTION_ID, /* allDescendants= */ false), requests.get(1));
    assertEquals(commit(TRANSACTION_ID), requests.get(2));
  }

  @Test
  public void updateDocument() throws Exception {
    doReturn(beginResponse())
        .doReturn(commitResponse(2, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    ApiFuture<String> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<String>() {
              @Override
              public String updateCallback(Transaction transaction) {
                transaction.update(documentReference, LocalFirestoreHelper.SINGLE_FIELD_MAP);
                transaction.update(documentReference, "foo", "bar");
                return "foo";
              }
            },
            options);

    assertEquals("foo", transaction.get());

    List<Write> writes = new ArrayList<>();

    for (int i = 0; i < 2; ++i) {
      writes.add(update(SINGLE_FIELD_PROTO, Collections.singletonList("foo")));
    }

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(2, requests.size());

    assertEquals(begin(), requests.get(0));
    assertEquals(commit(TRANSACTION_ID, writes.toArray(new Write[] {})), requests.get(1));
  }

  @Test
  public void setDocument() throws Exception {
    doReturn(beginResponse())
        .doReturn(commitResponse(2, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    ApiFuture<String> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<String>() {
              @Override
              public String updateCallback(Transaction transaction) {
                transaction
                    .set(documentReference, LocalFirestoreHelper.SINGLE_FIELD_MAP)
                    .set(documentReference, LocalFirestoreHelper.SINGLE_FIELD_OBJECT);
                return "foo";
              }
            },
            options);

    assertEquals("foo", transaction.get());

    List<Write> writes = new ArrayList<>();

    for (int i = 0; i < 2; ++i) {
      writes.add(set(SINGLE_FIELD_PROTO));
    }

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(2, requests.size());

    assertEquals(begin(), requests.get(0));
    assertEquals(commit(TRANSACTION_ID, writes.toArray(new Write[] {})), requests.get(1));
  }

  @Test
  public void createDocument() throws Exception {
    doReturn(beginResponse())
        .doReturn(commitResponse(2, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    ApiFuture<String> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<String>() {
              @Override
              public String updateCallback(Transaction transaction) {
                transaction
                    .create(documentReference, LocalFirestoreHelper.SINGLE_FIELD_MAP)
                    .create(documentReference, LocalFirestoreHelper.SINGLE_FIELD_OBJECT);
                return "foo";
              }
            },
            options);

    assertEquals("foo", transaction.get());

    List<Write> writes = new ArrayList<>();

    for (int i = 0; i < 2; ++i) {
      writes.add(create(SINGLE_FIELD_PROTO));
    }

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(2, requests.size());

    assertEquals(begin(), requests.get(0));
    assertEquals(commit(TRANSACTION_ID, writes.toArray(new Write[] {})), requests.get(1));
  }

  @Test
  public void deleteDocument() throws Exception {
    doReturn(beginResponse())
        .doReturn(commitResponse(2, 0))
        .when(firestoreMock)
        .sendRequest(requestCapture.capture(), Matchers.<UnaryCallable<Message, Message>>any());

    ApiFuture<String> transaction =
        firestoreMock.runTransaction(
            new Transaction.Function<String>() {
              @Override
              public String updateCallback(Transaction transaction) {
                transaction.delete(documentReference);
                transaction.delete(
                    documentReference,
                    Precondition.updatedAt(Timestamp.ofTimeSecondsAndNanos(1, 2)));
                return "foo";
              }
            },
            options);

    assertEquals("foo", transaction.get());

    List<Write> writes = new ArrayList<>();
    writes.add(delete());

    com.google.firestore.v1.Precondition.Builder precondition =
        com.google.firestore.v1.Precondition.newBuilder();
    precondition.getUpdateTimeBuilder().setSeconds(1).setNanos(2);
    writes.add(delete(precondition.build()));

    List<Message> requests = requestCapture.getAllValues();
    assertEquals(2, requests.size());

    assertEquals(begin(), requests.get(0));
    assertEquals(commit(TRANSACTION_ID, writes.toArray(new Write[] {})), requests.get(1));
  }

  @Test
  public void readOnlyTransactionOptionsBuilder_setReadTime() {
    Executor executor = mock(Executor.class);
    final com.google.protobuf.Timestamp.Builder readTime =
        com.google.protobuf.Timestamp.getDefaultInstance().toBuilder().setSeconds(1).setNanos(0);
    final ReadOnlyOptionsBuilder builder =
        TransactionOptions.readOnlyOptionsBuilder().setExecutor(executor).setReadTime(readTime);
    final ReadOnly expectedReadOnly = ReadOnly.newBuilder().setReadTime(readTime).build();

    final TransactionOptions transactionOptions = builder.build();

    assertThat(builder.getExecutor()).isSameInstanceAs(executor);
    assertThat(builder.getReadTime()).isSameInstanceAs(readTime);

    assertThat(transactionOptions.getExecutor()).isSameInstanceAs(executor);
    final EitherReadOnlyOrReadWrite options = transactionOptions.getOptions();

    assertThat(options.getType()).isEqualTo(TransactionOptionsType.READ_ONLY);
    final ReadOnlyOptions readOnly = options.getReadOnly();
    // actually build the builder so we get a useful .equals method
    assertThat(readOnly.toProtoBuilder().build()).isEqualTo(expectedReadOnly);
  }

  @Test
  public void readOnlyTransactionOptionsBuilder_defaults() {
    final ReadOnlyOptionsBuilder builder = TransactionOptions.readOnlyOptionsBuilder();
    final ReadOnly expectedReadOnly = ReadOnly.newBuilder().build();

    final TransactionOptions transactionOptions = builder.build();

    assertThat(builder.getExecutor()).isNull();
    assertThat(builder.getReadTime()).isNull();

    final ReadOnlyOptions readOnly = transactionOptions.getOptions().getReadOnly();
    assertThat(readOnly.getReadTime()).isNull();
    // actually build the builder so we get a useful .equals method
    assertThat(readOnly.toProtoBuilder().build()).isEqualTo(expectedReadOnly);
  }

  @Test
  public void readOnlyTransactionOptionsBuilder_errorWhenGettingReadWrite() {
    final ReadOnlyOptionsBuilder builder = TransactionOptions.readOnlyOptionsBuilder();
    try {
      //noinspection ResultOfMethodCallIgnored
      builder.build().getOptions().getReadWrite();
      fail("Error expected");
    } catch (IllegalStateException ignore) {
      // expected
    }
  }

  @Test
  public void readWriteTransactionOptionsBuilder_setNumberOfAttempts() {
    Executor executor = mock(Executor.class);
    final ReadWriteOptionsBuilder builder =
        TransactionOptions.readWriteOptionsBuilder().setExecutor(executor).setNumberOfAttempts(2);
    final ReadWrite expectedReadWrite = ReadWrite.newBuilder().build();

    final TransactionOptions transactionOptions = builder.build();

    assertThat(builder.getExecutor()).isSameInstanceAs(executor);
    assertThat(builder.getNumberOfAttempts()).isEqualTo(2);

    assertThat(transactionOptions.getExecutor()).isSameInstanceAs(executor);
    final EitherReadOnlyOrReadWrite options = transactionOptions.getOptions();

    assertThat(options.getType()).isEqualTo(TransactionOptionsType.READ_WRITE);
    final ReadWriteOptions readWrite = options.getReadWrite();
    // actually build the builder so we get a useful .equals method
    assertThat(readWrite.toProtoBuilder().build()).isEqualTo(expectedReadWrite);
  }

  @Test
  public void readWriteTransactionOptionsBuilder_defaults() {
    final ReadWrite expectedReadWrite = ReadWrite.newBuilder().build();

    final TransactionOptions transactionOptions =
        TransactionOptions.readWriteOptionsBuilder().build();
    final ReadWriteOptions readWrite = transactionOptions.getOptions().getReadWrite();

    assertThat(transactionOptions.getExecutor()).isNull();
    assertThat(readWrite.getNumberOfAttempts()).isEqualTo(5);

    // actually build the builder so we get a useful .equals method
    assertThat(readWrite.toProtoBuilder().build()).isEqualTo(expectedReadWrite);
  }

  @Test
  public void readWriteTransactionOptionsBuilder_errorWhenGettingReadWrite() {
    final ReadWriteOptionsBuilder builder = TransactionOptions.readWriteOptionsBuilder();
    try {
      //noinspection ResultOfMethodCallIgnored
      builder.build().getOptions().getReadOnly();
      fail("Error expected");
    } catch (IllegalStateException ignore) {
      // expected
    }
  }

  @Test
  public void readWriteTransactionOptionsBuilder_errorAttemptingToSetNumAttemptsLessThanOne() {
    try {
      TransactionOptions.readWriteOptionsBuilder().setNumberOfAttempts(0);
      fail("Error expected");
    } catch (IllegalArgumentException ignore) {
      // expected
    }
  }
}
