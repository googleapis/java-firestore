/*
 * Copyright 2023 Google LLC
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

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.spi.v1.FirestoreRpc;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/** @author Eran Leshem */
@RunWith(MockitoJUnitRunner.class)
public class ToStringTest {

  @Spy
  private final FirestoreImpl firestoreMock =
      new FirestoreImpl(
          FirestoreOptions.newBuilder().setProjectId("test-project").build(),
          Mockito.mock(FirestoreRpc.class));

  private WriteBatch batch;
  private DocumentReference documentReference;

  @Before
  public void before() {
    batch = firestoreMock.batch();
    documentReference = firestoreMock.document("coll/doc");
  }

  @Test
  public void testDocumentSnapshot() {
    Assert.assertEquals(
        "DocumentSnapshot{docRef=DocumentReference{path=projects/test-project/databases/(default)"
            + "/documents/coll/doc}, fields={key=string_value: \"value\"\n"
            + "}, readTime=null, updateTime=null, createTime=null}",
        getDocumentSnapshot().toString());
  }

  @Test
  public void testWriteOperation() {
    Assert.assertEquals(
        "WriteOperation{write=update {\n"
            + "  name: \"projects/test-project/databases/(default)/documents/coll/doc\"\n"
            + "  fields {\n"
            + "    key: \"key\"\n"
            + "    value {\n"
            + "      string_value: \"value\"\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + ", documentReference=DocumentReference{path=projects/test-project/databases/(default)/documents/coll/doc}}",
        new UpdateBuilder.WriteOperation(documentReference, getDocumentSnapshot().toPb())
            .toString());
  }

  private DocumentSnapshot getDocumentSnapshot() {
    return DocumentSnapshot.fromObject(
        null,
        documentReference,
        Collections.singletonMap("key", "value"),
        UserDataConverter.NO_DELETES);
  }

  @Test
  public void testWriteBatchDelete() {
    batch.delete(documentReference);
    Assert.assertEquals(
        "WriteBatch{writes=[WriteOperation{write=delete: \"projects/test-project/databases/(default)"
            + "/documents/coll/doc\"\n, documentReference=DocumentReference{path=projects/test-project"
            + "/databases/(default)/documents/coll/doc}}], committed=false}",
        batch.toString());
  }

  @Test
  public void testWriteBatchSet() {
    batch.set(documentReference, Collections.singletonMap("key", "value"), SetOptions.OVERWRITE);
    Assert.assertEquals(
        "WriteBatch{writes=[WriteOperation{write=update {\n"
            + "  name: \"projects/test-project/databases/(default)/documents/coll/doc\"\n"
            + "  fields {\n"
            + "    key: \"key\"\n"
            + "    value {\n"
            + "      string_value: \"value\"\n"
            + "    }\n"
            + "  }\n"
            + "}\n, documentReference=DocumentReference{path=projects/test-project"
            + "/databases/(default)/documents/coll/doc}}], committed=false}",
        batch.toString());
  }

  @Test
  public void testWriteBatchUpdate() {
    batch.update(
        documentReference,
        Collections.singletonMap("key", "value"),
        Precondition.updatedAt(Timestamp.ofTimeMicroseconds(1)));
    Assert.assertEquals(
        "WriteBatch{writes=[WriteOperation{write=update {\n"
            + "  name: \"projects/test-project/databases/(default)/documents/coll/doc\"\n"
            + "  fields {\n"
            + "    key: \"key\"\n"
            + "    value {\n"
            + "      string_value: \"value\"\n"
            + "    }\n"
            + "  }\n"
            + "}\nupdate_mask {\n"
            + "  field_paths: \"key\"\n"
            + "}\n"
            + "current_document {\n"
            + "  update_time {\n"
            + "    nanos: 1000\n"
            + "  }\n"
            + "}\n, documentReference=DocumentReference{path=projects/test-project"
            + "/databases/(default)/documents/coll/doc}}], committed=false}",
        batch.toString());
  }
}
