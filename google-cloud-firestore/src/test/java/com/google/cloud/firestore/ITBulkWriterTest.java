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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.BulkWriter.WriteErrorCallback;
import com.google.cloud.firestore.BulkWriter.WriteResultCallback;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class ITBulkWriterTest {
  private Firestore firestore;
  private CollectionReference randomColl;
  private DocumentReference randomDoc;

  @Rule public TestName testName = new TestName();

  @Before
  public void before() {
    FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().build();
    firestore = firestoreOptions.getService();
    randomColl =
        firestore.collection(
            String.format("java-%s-%s", testName.getMethodName(), LocalFirestoreHelper.autoId()));
    randomDoc = randomColl.document();
  }

  @After
  public void after() throws Exception {
    Preconditions.checkNotNull(
        firestore,
        "Error instantiating Firestore. Check that the service account credentials were properly set.");
    firestore.close();
  }

  @Test
  public void bulkWriterCreate() throws Exception {
    DocumentReference docRef = randomColl.document();
    firestore.bulkWriter();

    BulkWriter writer = firestore.bulkWriter();
    ApiFuture<WriteResult> result =
        writer.create(docRef, Collections.singletonMap("foo", (Object) "bar"));
    writer.close();

    assertNotNull(result.get().getUpdateTime());
    DocumentSnapshot snapshot = docRef.get().get();
    assertEquals("bar", snapshot.get("foo"));
  }

  @Test
  public void bulkWriterCreateAddsPrecondition() throws Exception {
    DocumentReference docRef = randomColl.document();
    docRef.set(Collections.singletonMap("foo", (Object) "bar")).get();

    BulkWriter writer = firestore.bulkWriter();
    ApiFuture<WriteResult> result =
        writer.create(docRef, Collections.singletonMap("foo", (Object) "bar"));
    writer.close();

    try {
      result.get();
      fail("Create operation should have thrown exception");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Document already exists"));
    }
  }

  @Test
  public void bulkWriterSet() throws Exception {
    DocumentReference docRef = randomColl.document();

    BulkWriter writer = firestore.bulkWriter();
    ApiFuture<WriteResult> result =
        writer.set(docRef, Collections.singletonMap("foo", (Object) "bar"));
    writer.close();

    assertNotNull(result.get().getUpdateTime());
    DocumentSnapshot snapshot = docRef.get().get();
    assertEquals("bar", snapshot.get("foo"));
  }

  @Test
  public void bulkWriterUpdate() throws Exception {
    DocumentReference docRef = randomColl.document();
    docRef.set(Collections.singletonMap("foo", "oldValue")).get();

    BulkWriter writer = firestore.bulkWriter();
    ApiFuture<WriteResult> result = writer.update(docRef, "foo", "newValue");
    writer.close();

    assertNotNull(result.get().getUpdateTime());
    DocumentSnapshot snapshot = docRef.get().get();
    assertEquals("newValue", snapshot.get("foo"));
  }

  @Test
  public void bulkWriterUpdateAddsPrecondition() throws Exception {
    DocumentReference docRef = randomColl.document();

    BulkWriter writer = firestore.bulkWriter();
    ApiFuture<WriteResult> result = writer.update(docRef, "foo", "newValue");
    writer.close();

    try {
      result.get();
      fail("Update operation should have thrown exception");
    } catch (Exception e) {
      assertTrue(e.getMessage().matches(".* No (document|entity) to update.*"));
    }
  }

  @Test
  public void bulkWriterDelete() throws Exception {
    DocumentReference docRef = randomColl.document();
    docRef.set(Collections.singletonMap("foo", "oldValue")).get();

    BulkWriter writer = firestore.bulkWriter();
    ApiFuture<WriteResult> result = writer.delete(docRef);
    writer.close();

    assertNotNull(result.get().getUpdateTime());
    // TODO(b/158502664): Remove this check once we can get write times.
    assertEquals(Timestamp.ofTimeSecondsAndNanos(0, 0), result.get().getUpdateTime());
    DocumentSnapshot snapshot = docRef.get().get();
    assertNull(snapshot.get("foo"));
  }

  @Test
  public void bulkWriterOnResult() throws Exception {
    class NamedThreadFactory implements ThreadFactory {
      public Thread newThread(Runnable r) {
        return new Thread(r, "bulkWriterSuccess");
      }
    }
    Executor executor = Executors.newSingleThreadExecutor(new NamedThreadFactory());
    final List<String> operations = new ArrayList<>();

    BulkWriter writer = firestore.bulkWriter();
    writer.addWriteResultListener(
        executor,
        new WriteResultCallback() {
          public void onResult(DocumentReference documentReference, WriteResult result) {
            operations.add("operation");
            assertTrue(Thread.currentThread().getName().contains("bulkWriterSuccess"));
          }
        });
    writer.set(randomDoc, Collections.singletonMap("foo", "bar"));
    writer.flush().get();
    assertEquals("operation", operations.get(0));
  }

  @Test
  public void bulkWriterOnError() throws Exception {
    class NamedThreadFactory implements ThreadFactory {
      public Thread newThread(Runnable r) {
        return new Thread(r, "bulkWriterException");
      }
    }
    Executor executor = Executors.newSingleThreadExecutor(new NamedThreadFactory());
    final List<String> operations = new ArrayList<>();

    BulkWriter writer = firestore.bulkWriter();
    writer.addWriteErrorListener(
        executor,
        new WriteErrorCallback() {
          public boolean onError(BulkWriterException error) {
            operations.add("operation-error");
            assertTrue(Thread.currentThread().getName().contains("bulkWriterException"));
            return false;
          }
        });

    writer.addWriteResultListener(
        executor,
        new WriteResultCallback() {
          public void onResult(DocumentReference documentReference, WriteResult result) {
            fail("The success listener shouldn't be called");
          }
        });
    writer.update(randomDoc, "foo", "bar");
    writer.flush().get();
    assertEquals("operation-error", operations.get(0));
  }
}
