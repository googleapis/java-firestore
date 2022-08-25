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

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.common.base.Preconditions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.google.common.truth.Truth.assertThat;

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
    assertThat(snapshot.getCount()).isEqualTo(0);
  }

  @Test
  public void countShouldReturnNumDocumentsInNonEmptyCollection() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(5);
    AggregateQuerySnapshot snapshot = collection.count().get().get();
    assertThat(snapshot.getCount()).isEqualTo(5);
  }

  @Test
  public void countShouldReturnNumMatchingDocuments() throws Exception {
    CollectionReference collection = createEmptyCollection();
    createDocumentsWithKeyValuePair(collection, 3, "key", 42);
    createDocumentsWithKeyValuePair(collection, 5, "key", 24);
    AggregateQuerySnapshot snapshot = collection.whereEqualTo("key", 42).count().get().get();
    assertThat(snapshot.getCount()).isEqualTo(3);
  }

  @Test
  public void countShouldRespectLimit() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(5);
    AggregateQuerySnapshot snapshot = collection.limit(2).count().get().get();
    assertThat(snapshot.getCount()).isEqualTo(2);
  }

  @Test
  public void countShouldRespectOffset() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(5);
    AggregateQuerySnapshot snapshot = collection.offset(2).count().get().get();
    assertThat(snapshot.getCount()).isEqualTo(3);
  }

  @Test
  public void countShouldRespectOrderBy() throws Exception {
    CollectionReference collection = createEmptyCollection();
    createDocumentsWithKeyValuePair(collection, 3, "key1", 42);
    createDocumentsWithKeyValuePair(collection, 5, "key2", 24);
    createDocumentsWithKeyValuePair(collection, 4, "key1", 99);
    AggregateQuerySnapshot snapshot = collection.orderBy("key1").count().get().get();
    // Note: A subtle side effect of order-by is that it filters out documents that do not have the order-by field.
    assertThat(snapshot.getCount()).isEqualTo(7);
  }

  @Test
  public void countShouldRespectStartAtAndEndAt() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(10);
    List<QueryDocumentSnapshot> documentSnapshots = collection.get().get().getDocuments();
    AggregateQuerySnapshot snapshot = collection.startAt(documentSnapshots.get(2)).endAt(documentSnapshots.get(7)).count().get().get();
    assertThat(snapshot.getCount()).isEqualTo(6);
  }

  @Test
  public void countShouldReturnNumberOfDocumentsForCollectionGroups() throws Exception {
    CollectionGroup collectionGroup = createCollectionGroupWithDocuments(13);
    AggregateQuerySnapshot snapshot = collectionGroup.count().get().get();
    assertThat(snapshot.getCount()).isEqualTo(13);
  }

  @Test
  public void aggregateSnapshotShouldHaveReasonableReadTime() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(5);
    AggregateQuerySnapshot snapshot1 = collection.count().get().get();
    AggregateQuerySnapshot snapshot2 = collection.count().get().get();
    long readTimeMs1 = msFromTimestamp(snapshot1.getReadTime());
    long readTimeMs2 = msFromTimestamp(snapshot2.getReadTime());
    assertThat(readTimeMs1).isLessThan(readTimeMs2);
    assertThat(readTimeMs2 - readTimeMs1).isLessThan(500);
  }

  @Test
  public void aggregateSnapshotShouldHaveCorrectQuery() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(5);
    AggregateQuery aggregateQuery = collection.count();
    AggregateQuerySnapshot snapshot1 = aggregateQuery.get().get();
    AggregateQuerySnapshot snapshot2 = aggregateQuery.get().get();
    assertThat(snapshot1.getQuery()).isSameInstanceAs(aggregateQuery);
    assertThat(snapshot2.getQuery()).isSameInstanceAs(aggregateQuery);
  }

  @Test
  public void aggregateQueryShouldHaveCorrectQuery() {
    CollectionReference collection = firestore.collection("abc");
    AggregateQuery aggregateQuery = collection.count();
    assertThat(aggregateQuery.getQuery()).isSameInstanceAs(collection);
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

  private void createDocumentsWithKeyValuePair(CollectionReference collection, int numDocumentsToCreate, String key, int value) throws ExecutionException, InterruptedException {
    for (int i=0; i<numDocumentsToCreate; i++) {
      createDocumentInCollection(collection, key, value);
    }
  }

  private void createDocumentInCollection(CollectionReference collection) throws ExecutionException, InterruptedException {
    createDocumentInCollection(collection, "age", 42);
  }

  private void createDocumentInCollection(CollectionReference collection, String key, int value) throws ExecutionException, InterruptedException {
    DocumentReference doc = collection.document();
    System.out.println("zzyzx created document: " + doc);
    HashMap<String, Object> data = new HashMap<>();
    data.put(key, value);
    doc.set(data).get();
  }

  private CollectionGroup createCollectionGroupWithDocuments(int numDocumentsToCreate) throws ExecutionException, InterruptedException {
    String collectionId = generateUniqueId();

    // Create some collections to participate in the group.
    ArrayList<CollectionReference> collections = new ArrayList<>();
    for (int i=0; i<=numDocumentsToCreate/3; i++) {
      collections.add(createEmptyCollection().document().collection(collectionId));
    }

    // Populate the collections with documents.
    Iterator<CollectionReference> collectionIterator = loopInfinitely(collections);
    for (int i=0; i<numDocumentsToCreate; i++) {
      createDocumentInCollection(collectionIterator.next());
    }

    return firestore.collectionGroup(collectionId);
  }

  /**
   * Generates and returns a globally unique string.
   */
  private static String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  /**
   * Converts a {@link Timestamp} to the equivalent number of milliseconds.
   */
  private static long msFromTimestamp(Timestamp timestamp) {
    return (timestamp.getSeconds() * 1_000) + (timestamp.getNanos() / 1_000_000);
  }

  /**
   * Creates and returns an infinite iterator that iterates over the given collection in the same order as its
   * {@link Collection#iterator} method does, looping around back to the beginning when it reaches the end.
   */
  private static <T> Iterator<T> loopInfinitely(Collection<T> collection) {
    return new Iterator<T>() {
      private Iterator<T> it = collection.iterator();

      @Override
      public boolean hasNext() {
        // NOTE: it.hasNext() will ALWAYS return true, except in the case that the given collection is empty.
        return it.hasNext();
      }

      @Override
      public T next() {
        T element = it.next();
        // If we've reached the end of the iterator, create a new one.
        if (! it.hasNext()) {
          it = collection.iterator();
        }
        return element;
      }
    };
  }

}
