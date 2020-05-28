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

import static com.google.cloud.firestore.LocalFirestoreHelper.UPDATED_SINGLE_FIELD_PROTO;
import static com.google.cloud.firestore.LocalFirestoreHelper.UPDATE_SINGLE_FIELD_OBJECT;
import static com.google.cloud.firestore.LocalFirestoreHelper.batchWrite;
import static com.google.cloud.firestore.LocalFirestoreHelper.commit;
import static com.google.cloud.firestore.LocalFirestoreHelper.commitResponse;
import static com.google.cloud.firestore.LocalFirestoreHelper.create;
import static com.google.cloud.firestore.LocalFirestoreHelper.delete;
import static com.google.cloud.firestore.LocalFirestoreHelper.map;
import static com.google.cloud.firestore.LocalFirestoreHelper.set;
import static com.google.cloud.firestore.LocalFirestoreHelper.update;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;

import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.spi.v1.FirestoreRpc;
import com.google.firestore.v1.BatchWriteRequest;
import com.google.firestore.v1.BatchWriteResponse;
import com.google.firestore.v1.CommitRequest;
import com.google.firestore.v1.CommitResponse;
import com.google.firestore.v1.Write;
import com.google.rpc.Status;
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
  @Captor private ArgumentCaptor<BatchWriteRequest> batchWriteCapture;

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

    assertEquals(4, batch.getMutationsSize());

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
  public void updateDocumentWithPOJO() throws Exception {
    doReturn(commitResponse(1, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(), Matchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    batch.update(documentReference, "foo", UPDATE_SINGLE_FIELD_OBJECT);
    assertEquals(1, batch.getMutationsSize());

    List<WriteResult> writeResults = batch.commit().get();
    assertEquals(1, writeResults.size());

    CommitRequest actual = commitCapture.getValue();
    CommitRequest expected =
        commit(update(UPDATED_SINGLE_FIELD_PROTO, Collections.singletonList("foo")));
    assertEquals(expected, actual);
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

    assertEquals(4, batch.getMutationsSize());

    List<WriteResult> writeResults = batch.commit().get();
    for (int i = 0; i < writeResults.size(); ++i) {
      assertEquals(Timestamp.ofTimeSecondsAndNanos(i, i), writeResults.get(i).getUpdateTime());
    }

    CommitRequest commitRequest = commitCapture.getValue();
    assertEquals(commit(writes.toArray(new Write[] {})), commitRequest);
  }

  @Test
  public void setDocumentWithValue() throws Exception {
    doReturn(commitResponse(4, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(), Matchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    batch
        .set(documentReference, LocalFirestoreHelper.SINGLE_FIELD_PROTO)
        .set(documentReference, LocalFirestoreHelper.SINGLE_FIELD_OBJECT)
        .set(documentReference, LocalFirestoreHelper.SINGLE_FIELD_PROTO, SetOptions.merge())
        .set(documentReference, LocalFirestoreHelper.SINGLE_FIELD_OBJECT, SetOptions.merge());

    List<Write> writes = new ArrayList<>();
    writes.add(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO));
    writes.add(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO));
    writes.add(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, Arrays.asList("foo")));
    writes.add(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO, Arrays.asList("foo")));

    assertEquals(4, batch.getMutationsSize());

    List<WriteResult> writeResults = batch.commit().get();
    for (int i = 0; i < writeResults.size(); ++i) {
      assertEquals(Timestamp.ofTimeSecondsAndNanos(i, i), writeResults.get(i).getUpdateTime());
    }

    CommitRequest commitRequest = commitCapture.getValue();
    assertEquals(commit(writes.toArray(new Write[] {})), commitRequest);
  }

  @Test
  public void setDocumentWithFloat() throws Exception {
    doReturn(commitResponse(1, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(), Matchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    batch.set(documentReference, LocalFirestoreHelper.SINGLE_FLOAT_MAP);

    List<Write> writes = new ArrayList<>();
    writes.add(set(LocalFirestoreHelper.SINGLE_FLOAT_PROTO));

    assertEquals(1, batch.getMutationsSize());

    List<WriteResult> writeResults = batch.commit().get();
    for (int i = 0; i < writeResults.size(); ++i) {
      assertEquals(Timestamp.ofTimeSecondsAndNanos(i, i), writeResults.get(i).getUpdateTime());
    }

    CommitRequest commitRequest = commitCapture.getValue();
    assertEquals(commit(writes.toArray(new Write[] {})), commitRequest);
  }

  @Test
  public void omitWriteResultForDocumentTransforms() throws Exception {
    doReturn(commitResponse(1, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(), Matchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    batch.set(documentReference, map("time", FieldValue.serverTimestamp()));

    assertEquals(1, batch.getMutationsSize());

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

    assertEquals(2, batch.getMutationsSize());

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
  public void createDocumentWithValue() throws Exception {
    doReturn(commitResponse(2, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(), Matchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    batch
        .create(documentReference, LocalFirestoreHelper.SINGLE_FIELD_PROTO)
        .create(documentReference, LocalFirestoreHelper.SINGLE_FIELD_OBJECT);

    assertEquals(2, batch.getMutationsSize());

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
  public void createDocumentWithFloat() throws Exception {
    doReturn(commitResponse(1, 0))
        .when(firestoreMock)
        .sendRequest(
            commitCapture.capture(), Matchers.<UnaryCallable<CommitRequest, CommitResponse>>any());

    batch.create(documentReference, LocalFirestoreHelper.SINGLE_FLOAT_MAP);

    assertEquals(1, batch.getMutationsSize());

    List<WriteResult> writeResults = batch.commit().get();
    List<Write> writes = new ArrayList<>();

    for (int i = 0; i < writeResults.size(); ++i) {
      assertEquals(Timestamp.ofTimeSecondsAndNanos(i, i), writeResults.get(i).getUpdateTime());
      writes.add(create(LocalFirestoreHelper.SINGLE_FLOAT_PROTO));
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

    assertEquals(2, batch.getMutationsSize());

    List<WriteResult> writeResults = batch.commit().get();

    for (int i = 0; i < writeResults.size(); ++i) {
      assertEquals(Timestamp.ofTimeSecondsAndNanos(i, i), writeResults.get(i).getUpdateTime());
    }

    CommitRequest commitRequest = commitCapture.getValue();
    assertEquals(commit(writes.toArray(new Write[] {})), commitRequest);
  }

  @Test
  public void bulkCommit() throws Exception {
    BatchWriteResponse.Builder response = BatchWriteResponse.newBuilder();
    response.addWriteResultsBuilder().getUpdateTimeBuilder().setSeconds(0).setNanos(1);
    response.addWriteResultsBuilder();
    response.addStatusBuilder().setCode(0).build();
    response.addStatusBuilder().setCode(14).build();
    doReturn(ApiFutures.immediateFuture(response.build()))
        .when(firestoreMock)
        .sendRequest(
            batchWriteCapture.capture(),
            Matchers.<UnaryCallable<BatchWriteRequest, BatchWriteResponse>>any());

    List<Write> writes = new ArrayList<>();
    batch.set(documentReference, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    writes.add(set(LocalFirestoreHelper.SINGLE_FIELD_PROTO));

    batch.create(documentReference, LocalFirestoreHelper.SINGLE_FIELD_MAP);
    writes.add(create(LocalFirestoreHelper.SINGLE_FIELD_PROTO));

    assertEquals(2, batch.getMutationsSize());

    List<BatchWriteResult> batchWriteResults = batch.bulkCommit().get();

    assertEquals(Timestamp.ofTimeSecondsAndNanos(0, 1), batchWriteResults.get(0).getWriteTime());
    assertEquals(Status.newBuilder().setCode(0).build(), batchWriteResults.get(0).getStatus());
    assertNull(batchWriteResults.get(1).getWriteTime());
    assertEquals(Status.newBuilder().setCode(14).build(), batchWriteResults.get(1).getStatus());

    BatchWriteRequest batchWriteRequest = batchWriteCapture.getValue();
    assertEquals(batchWrite(writes.toArray(new Write[] {})), batchWriteRequest);
  }
}
