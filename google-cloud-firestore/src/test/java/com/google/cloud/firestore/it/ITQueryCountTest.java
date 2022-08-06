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

package com.google.cloud.firestore.it;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.LocalFirestoreHelper.*;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.Transaction.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.firestore.bundle.BundleElement;
import com.google.firestore.bundle.BundledDocumentMetadata;
import com.google.firestore.bundle.BundledQuery.LimitType;
import com.google.firestore.bundle.NamedQuery;
import com.google.firestore.v1.RunQueryRequest;
import io.grpc.Status;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.threeten.bp.Duration;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.cloud.firestore.FirestoreBundleTest.*;
import static com.google.cloud.firestore.LocalFirestoreHelper.*;
import static com.google.common.truth.Truth.assertThat;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ITQueryCountTest {

  @Rule public TestName testName = new TestName();

  private Firestore firestore;

  @Before
  public void setUpFirestore() {
    firestore = FirestoreOptions.newBuilder().setHost("localhost:8080").setProjectId("dconeybe-testing").build().getService();
    Preconditions.checkNotNull(firestore, "Error instantiating Firestore. Check that the service account credentials were properly set.");
  }

  @After
  public void tearDownFirestore() throws Exception {
    if (firestore != null) {
      firestore.close();
      firestore = null;
    }
  }

  @Test
  public void countShouldReturnZeroForEmptyCollection() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(0);
    AggregateQuerySnapshot snapshot = collection.count().get().get();
    assertEquals(Long.valueOf(0), snapshot.getCount());
  }

  @Test
  public void countShouldReturnNumDocumentsInNonEmptyCollection() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(5);
    AggregateQuerySnapshot snapshot = collection.count().get().get();
    assertEquals(Long.valueOf(5), snapshot.getCount());
  }

  @Test
  public void countShouldReturnNumMatchingDocuments() throws Exception {
    CollectionReference collection = createEmptyCollection();
    createDocumentsWithKeyValuePair(collection, 3, "key", 42);
    createDocumentsWithKeyValuePair(collection, 5, "key", 24);
    AggregateQuerySnapshot snapshot = collection.whereEqualTo("key", 42).count().get().get();
    assertEquals(Long.valueOf(3), snapshot.getCount());
  }

  @Test
  public void countShouldRespectLimit() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(5);
    AggregateQuerySnapshot snapshot = collection.limit(2).count().get().get();
    assertEquals(Long.valueOf(2), snapshot.getCount());
  }

  @Test
  public void countShouldRespectOffset() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(5);
    AggregateQuerySnapshot snapshot = collection.offset(2).count().get().get();
    assertEquals(Long.valueOf(3), snapshot.getCount());
  }

  @Test
  public void countShouldRespectOrderBy() throws Exception {
    CollectionReference collection = createEmptyCollection();
    createDocumentsWithKeyValuePair(collection, 3, "key1", 42);
    createDocumentsWithKeyValuePair(collection, 5, "key2", 24);
    createDocumentsWithKeyValuePair(collection, 4, "key1", 99);
    AggregateQuerySnapshot snapshot = collection.orderBy("key1").count().get().get();
    // Note: A subtle side effect of order-by is that it filters out documents that do not have the order-by field.
    assertEquals(Long.valueOf(7), snapshot.getCount());
  }

  @Test
  public void countShouldRespectStartAtAndEndAt() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(10);
    List<QueryDocumentSnapshot> documentSnapshots = collection.get().get().getDocuments();
    AggregateQuerySnapshot snapshot = collection.startAt(documentSnapshots.get(2)).endAt(documentSnapshots.get(7)).count().get().get();
    assertEquals(Long.valueOf(6), snapshot.getCount());
  }

  private CollectionReference createEmptyCollection() {
    String collectionPath = "java-" + testName.getMethodName() + "-" + LocalFirestoreHelper.autoId();
    return firestore.collection(collectionPath);
  }

  private CollectionReference createCollectionWithDocuments(int numDocuments) throws ExecutionException, InterruptedException {
    CollectionReference collection = createEmptyCollection();
    createDocumentsWithKeyValuePair(collection, numDocuments, "key", 42);
    return collection;
  }

  private CollectionReference createDocumentsWithKeyValuePair(CollectionReference collection, int numDocumentsToCreate, String key, int value) throws ExecutionException, InterruptedException {
    for (int i=0; i<numDocumentsToCreate; i++) {
      DocumentReference doc = collection.document();
      HashMap<String, Object> data = new HashMap<>();
      data.put(key, value);
      doc.set(data).get();
    }
    return collection;
  }

}
