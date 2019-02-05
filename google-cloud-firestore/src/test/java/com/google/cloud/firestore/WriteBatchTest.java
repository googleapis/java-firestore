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

import static com.google.cloud.firestore.LocalFirestoreHelper.commit;
import static com.google.cloud.firestore.LocalFirestoreHelper.commitResponse;
import static com.google.cloud.firestore.LocalFirestoreHelper.create;
import static com.google.cloud.firestore.LocalFirestoreHelper.delete;
import static com.google.cloud.firestore.LocalFirestoreHelper.map;
import static com.google.cloud.firestore.LocalFirestoreHelper.set;
import static com.google.cloud.firestore.LocalFirestoreHelper.update;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.spi.v1.FirestoreRpc;
import com.google.firestore.v1.CommitRequest;
import com.google.firestore.v1.CommitResponse;
import com.google.firestore.v1.Write;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WriteBatchTest {

  @Spy
  private FirestoreImpl firestoreMock =
      new FirestoreImpl(
          FirestoreOptions.newBuilder().setProjectId("test-project").build(),
          Mockito.mock(FirestoreRpc.class));

  @Captor private ArgumentCaptor<CommitRequest> commitCapture;

  private WriteBatch batch;
  private DocumentReference documentReference;

  @Before
  public void before() {
    batch = firestoreMock.batch();
    documentReference = firestoreMock.document("coll/doc");
  }

  @Test
  public void updateDocument() throws Exception {
    doReturn(commitResponse(4, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(), Matchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    List<com.google.firestore.v1.Precondition> preconditions =
        Arrays.asList(
            com.google.firestore.v1.Precondition.newBuilder().setExists(true).build(),
            com.google.firestore.v1.Precondition.newBuilder().setExists(true).build(),
            com.google.firestore.v1.Precondition.newBuilder()
                .setUpdateTime(com.google.protobuf.Timestamp.getDefaultInstance())
                .build(),
            com.google.firestore.v1.Precondition.newBuilder()
                .setUpdateTime(com.google.protobuf.Timestamp.getDefaultInstance())
                .build());

    Precondition updateTime = Precondition.updatedAt(Timestamp.ofTimeSecondsAndNanos(0, 0));

    batch.update(documentReference, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    batch.update(documentReference, "foo", "bar");
    batch.update(documentReference, updateTime, "foo", "bar");
    batch.update(documentReference, LocalFirestoreHelper.SINGLE_FIELD_MAP, updateTime);

    List<WriteResult> writeResults = batch.commit().get();
    List<Write> writes = new ArrayList<>();

    for (int i = 0; i < writeResults.size(); ++i) {
      assertEquals(Timestamp.ofTimeSecondsAndNanos(i, i), writeResults.get(i).getUpdateTime());
      writes.add(
          update(
              LocalFirestoreHelper.SINGLE_FIELD_PROTO,
              Collections.singletonList("foo"),
              preconditions.get(i)));
    }

    CommitRequest commitRequest = commitCapture.getValue();
    assertEquals(commit(writes.toArray(new Write[] {})), commitRequest);
  }

  @Test
  public void setDocument() throws Exception {
    doReturn(commitResponse(4, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(), Matchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    batch
        .set(documentReference, LocalFirestoreHelper.SINGLE_FIELD_MAP)
        .set(documentReference, LocalFirestoreHelper.SINGLE_FIELD_OBJECT)
        .set(documentReference, LocalFirestoreHelper.SINGLE_FIELD_MAP, SetOptions.merge())
        .set(documentReference, LocalFirestoreHelper.SINGLE_FIELD_OBJECT, SetOptions.merge());

    List<Write> writes = new ArrayList<>();
    writes.add(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO));
    writes.add(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO));
    writes.add(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, Arrays.asList("foo")));
    writes.add(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, Arrays.asList("foo")));

    List<WriteResult> writeResults = batch.commit().get();
    for (int i = 0; i < writeResults.size(); ++i) {
      assertEquals(Timestamp.ofTimeSecondsAndNanos(i, i), writeResults.get(i).getUpdateTime());
    }

    CommitRequest commitRequest = commitCapture.getValue();
    assertEquals(commit(writes.toArray(new Write[] {})), commitRequest);
  }

  @Test
  public void omitWriteResultForDocumentTransforms() throws Exception {
    doReturn(commitResponse(2, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(), Matchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    batch.set(documentReference, map("time", FieldValue.serverTimestamp()));

    List<WriteResult> writeResults = batch.commit().get();
    assertEquals(1, writeResults.size());
  }

  @Test
  public void createDocument() throws Exception {
    doReturn(commitResponse(2, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(), Matchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    batch
        .create(documentReference, LocalFirestoreHelper.SINGLE_FIELD_MAP)
        .create(documentReference, LocalFirestoreHelper.SINGLE_FIELD_OBJECT);

    List<WriteResult> writeResults = batch.commit().get();
    List<Write> writes = new ArrayList<>();

    for (int i = 0; i < writeResults.size(); ++i) {
      assertEquals(Timestamp.ofTimeSecondsAndNanos(i, i), writeResults.get(i).getUpdateTime());
      writes.add(create(LocalFirestoreHelper.SINGLE_FIELD_PROTO));
    }

    CommitRequest commitRequest = commitCapture.getValue();
    assertEquals(commit(writes.toArray(new Write[] {})), commitRequest);
  }

  @Test
  public void deleteDocument() throws Exception {
    doReturn(commitResponse(2, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(), Matchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    List<Write> writes = new ArrayList<>();
    batch.delete(documentReference);
    writes.add(delete());

    batch.delete(documentReference, Precondition.updatedAt(Timestamp.ofTimeSecondsAndNanos(1, 2)));
    com.google.firestore.v1.Precondition.Builder precondition =
        com.google.firestore.v1.Precondition.newBuilder();
    precondition.getUpdateTimeBuilder().setSeconds(1).setNanos(2);
    writes.add(delete(precondition.build()));

    List<WriteResult> writeResults = batch.commit().get();

    for (int i = 0; i < writeResults.size(); ++i) {
      assertEquals(Timestamp.ofTimeSecondsAndNanos(i, i), writeResults.get(i).getUpdateTime());
    }

    CommitRequest commitRequest = commitCapture.getValue();
    assertEquals(commit(writes.toArray(new Write[] {})), commitRequest);
  }
}
