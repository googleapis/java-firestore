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

import static com.google.cloud.firestore.LocalFirestoreHelper.SINGLE_FIELD_OBJECT;
import static com.google.cloud.firestore.LocalFirestoreHelper.SINGLE_FIELD_PROTO;
import static com.google.cloud.firestore.LocalFirestoreHelper.SINGLE_FIELD_VALUE;
import static com.google.cloud.firestore.LocalFirestoreHelper.arrayRemove;
import static com.google.cloud.firestore.LocalFirestoreHelper.arrayUnion;
import static com.google.cloud.firestore.LocalFirestoreHelper.commit;
import static com.google.cloud.firestore.LocalFirestoreHelper.commitResponse;
import static com.google.cloud.firestore.LocalFirestoreHelper.getAllResponseWithoutOnComplete;
import static com.google.cloud.firestore.LocalFirestoreHelper.transform;
import static com.google.cloud.firestore.LocalFirestoreHelper.update;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.firestore.spi.v1.FirestoreRpc;
import com.google.firestore.v1.BatchGetDocumentsRequest;
import com.google.firestore.v1.CommitRequest;
import com.google.firestore.v1.CommitResponse;
import com.google.protobuf.Message;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FirestoreTest {

  @Spy
  private final FirestoreImpl firestoreMock =
      new FirestoreImpl(
          FirestoreOptions.newBuilder().setProjectId("test-project").build(),
          Mockito.mock(FirestoreRpc.class));

  @Captor private ArgumentCaptor<BatchGetDocumentsRequest> getAllCapture;

  @Captor private ArgumentCaptor<ResponseObserver<Message>> streamObserverCapture;

  @Captor private ArgumentCaptor<CommitRequest> commitCapture;

  @Test
  public void encodeFieldPath() {
    assertEquals("foo", FieldPath.of("foo").getEncodedPath());
    assertEquals("foo.bar", FieldPath.of("foo", "bar").getEncodedPath());
    assertEquals("`.`", FieldPath.of(".").getEncodedPath());
    assertEquals("`\\``", FieldPath.of("`").getEncodedPath());
    assertEquals("foo.`.`.`\\\\`", FieldPath.of("foo", ".", "\\").getEncodedPath());
    assertEquals("`.\\\\.\\\\.`", FieldPath.of(".\\.\\.").getEncodedPath());
  }

  @Test
  public void illegalFieldPath() throws Exception {
    doAnswer(getAllResponseWithoutOnComplete(SINGLE_FIELD_PROTO))
        .when(firestoreMock)
        .streamRequest(
            getAllCapture.capture(),
            streamObserverCapture.capture(),
            ArgumentMatchers.<ServerStreamingCallable>any());

    DocumentReference doc = firestoreMock.document("coll/doc");
    DocumentSnapshot snapshot = doc.get().get();

    char[] prohibited = new char[] {'*', '~', '/', '[', ']'};

    for (char c : prohibited) {
      try {
        snapshot.contains("foo" + c + "bar");
        fail();
      } catch (IllegalArgumentException e) {
        assertEquals("Use FieldPath.of() for field names containing '˜*/[]'.", e.getMessage());
      }
    }
  }

  @Test
  public void exposesOptions() {
    assertEquals("test-project", firestoreMock.getOptions().getProjectId());
  }

  @Test
  public void getAll() throws Exception {
    doAnswer(
            getAllResponseWithoutOnComplete(
                SINGLE_FIELD_PROTO, SINGLE_FIELD_PROTO, SINGLE_FIELD_PROTO, SINGLE_FIELD_PROTO))
        .when(firestoreMock)
        .streamRequest(
            getAllCapture.capture(),
            streamObserverCapture.capture(),
            ArgumentMatchers.<ServerStreamingCallable>any());

    DocumentReference doc1 = firestoreMock.document("coll/doc1");
    DocumentReference doc2 = firestoreMock.document("coll/doc2");
    DocumentReference doc3 = firestoreMock.document("coll/doc3");
    DocumentReference doc4 = firestoreMock.document("coll/doc4");
    List<DocumentSnapshot> snapshot = firestoreMock.getAll(doc1, doc2, doc4, doc3).get();
    assertEquals("doc1", snapshot.get(0).getId());
    assertEquals("doc2", snapshot.get(1).getId());
    // Note that we sort based on the order in the getAll() call.
    assertEquals("doc4", snapshot.get(2).getId());
    assertEquals("doc3", snapshot.get(3).getId());
  }

  @Test
  public void getAllWithFieldMask() throws Exception {
    doAnswer(getAllResponseWithoutOnComplete(SINGLE_FIELD_PROTO))
        .when(firestoreMock)
        .streamRequest(
            getAllCapture.capture(),
            streamObserverCapture.capture(),
            ArgumentMatchers.<ServerStreamingCallable>any());

    DocumentReference doc1 = firestoreMock.document("coll/doc1");
    FieldMask fieldMask = FieldMask.of(FieldPath.of("foo", "bar"));

    firestoreMock.getAll(new DocumentReference[] {doc1}, fieldMask).get();

    BatchGetDocumentsRequest request = getAllCapture.getValue();
    assertEquals(1, request.getMask().getFieldPathsCount());
    assertEquals("foo.bar", request.getMask().getFieldPaths(0));
  }

  @Test
  public void arrayUnionEquals() {
    FieldValue arrayUnion1 = FieldValue.arrayUnion("foo", "bar");
    FieldValue arrayUnion2 = FieldValue.arrayUnion("foo", "bar");
    FieldValue arrayUnion3 = FieldValue.arrayUnion("foo", "baz");
    FieldValue arrayRemove = FieldValue.arrayRemove("foo", "bar");
    assertEquals(arrayUnion1, arrayUnion1);
    assertEquals(arrayUnion1, arrayUnion2);
    assertNotEquals(arrayUnion1, arrayUnion3);
    assertNotEquals(arrayUnion1, arrayRemove);
  }

  @Test
  public void arrayRemoveEquals() {
    FieldValue arrayRemove1 = FieldValue.arrayRemove("foo", "bar");
    FieldValue arrayRemove2 = FieldValue.arrayRemove("foo", "bar");
    FieldValue arrayRemove3 = FieldValue.arrayRemove("foo", "baz");
    FieldValue arrayUnion = FieldValue.arrayUnion("foo", "bar");
    assertEquals(arrayRemove1, arrayRemove1);
    assertEquals(arrayRemove1, arrayRemove2);
    assertNotEquals(arrayRemove1, arrayRemove3);
    assertNotEquals(arrayRemove1, arrayUnion);
  }

  @Test
  public void incrementEquals() {
    FieldValue increment1 = FieldValue.increment(42);
    FieldValue increment2 = FieldValue.increment(42);
    FieldValue increment3 = FieldValue.increment(42.0);
    FieldValue increment4 = FieldValue.increment(42.0);
    assertEquals(increment1, increment2);
    assertEquals(increment3, increment4);
    assertNotEquals(increment1, increment3);
    assertNotEquals(increment2, increment4);
  }

  @Test
  public void maximumEquals() {
    FieldValue maximum1 = FieldValue.maximum(42);
    FieldValue maximum2 = FieldValue.maximum(42);
    FieldValue maximum3 = FieldValue.maximum(42.0);
    FieldValue maximum4 = FieldValue.maximum(42.0);
    assertEquals(maximum1, maximum2);
    assertEquals(maximum3, maximum4);
    assertNotEquals(maximum1, maximum3);
    assertNotEquals(maximum2, maximum4);
  }

  @Test
  public void minimumEquals() {
    FieldValue minimum1 = FieldValue.minimum(42);
    FieldValue minimum2 = FieldValue.minimum(42);
    FieldValue minimum3 = FieldValue.minimum(42.0);
    FieldValue minimum4 = FieldValue.minimum(42.0);
    assertEquals(minimum1, minimum2);
    assertEquals(minimum3, minimum4);
    assertNotEquals(minimum1, minimum3);
    assertNotEquals(minimum2, minimum4);
  }

  @Test
  public void arrayUnionWithPojo() throws ExecutionException, InterruptedException {
    doReturn(commitResponse(1, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(),
            ArgumentMatchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    DocumentReference doc = firestoreMock.document("coll/doc");
    doc.update("array", FieldValue.arrayUnion(SINGLE_FIELD_OBJECT)).get();

    CommitRequest expectedRequest =
        commit(
            update(Collections.emptyMap(), new ArrayList<>()),
            transform("array", arrayUnion(SINGLE_FIELD_VALUE)));
    CommitRequest actualRequest = commitCapture.getValue();
    assertEquals(expectedRequest, actualRequest);
  }

  @Test
  public void arrayRemoveWithPojo() throws ExecutionException, InterruptedException {
    doReturn(commitResponse(1, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(),
            ArgumentMatchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    DocumentReference doc = firestoreMock.document("coll/doc");
    doc.update("array", FieldValue.arrayRemove(SINGLE_FIELD_OBJECT)).get();

    CommitRequest expectedRequest =
        commit(
            update(Collections.emptyMap(), new ArrayList<>()),
            transform("array", arrayRemove(SINGLE_FIELD_VALUE)));
    CommitRequest actualRequest = commitCapture.getValue();
    assertEquals(expectedRequest, actualRequest);
  }
}
