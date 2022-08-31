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

import static com.google.cloud.firestore.LocalFirestoreHelper.autoId;
import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertThrows;

import com.google.api.core.ApiFuture;
import com.google.auto.value.AutoValue;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.AggregateQuery;
import com.google.cloud.firestore.AggregateQuerySnapshot;
import com.google.cloud.firestore.CollectionGroup;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.TransactionOptions;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ITQueryCountTest {

  @Rule public TestName testName = new TestName();

  private Firestore firestore;

  @Before
  public void setUpFirestore() {
    // TODO(dconeybe) Remove the hardcoded "host" and "projectId" once COUNT is supported by prod.
    firestore =
        FirestoreOptions.newBuilder()
            .setHost("localhost:8080")
            .setProjectId("my-cool-project")
            .build()
            .getService();
    Preconditions.checkNotNull(
        firestore,
        "Error instantiating Firestore. Check that the service account credentials were properly set.");
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
    CollectionReference collection = createEmptyCollection();
    AggregateQuerySnapshot snapshot = collection.count().get().get();
    assertThat(snapshot.getCount()).isEqualTo(0);
  }

  @Test
  public void countShouldReturnNumDocumentsInNonEmptyCollection() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(5).collection();
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
    CollectionReference collection = createCollectionWithDocuments(5).collection();
    AggregateQuerySnapshot snapshot = collection.limit(2).count().get().get();
    assertThat(snapshot.getCount()).isEqualTo(2);
  }

  @Test
  public void countShouldRespectOffset() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(5).collection();
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
    // Note: A subtle side effect of order-by is that it filters out documents that do not have the
    // order-by field.
    assertThat(snapshot.getCount()).isEqualTo(7);
  }

  @Test
  public void countShouldRespectStartAtAndEndAt() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(10).collection();
    List<QueryDocumentSnapshot> documentSnapshots = collection.get().get().getDocuments();
    AggregateQuerySnapshot snapshot =
        collection
            .startAt(documentSnapshots.get(2))
            .endAt(documentSnapshots.get(7))
            .count()
            .get()
            .get();
    assertThat(snapshot.getCount()).isEqualTo(6);
  }

  @Test
  public void countShouldReturnNumberOfDocumentsForCollectionGroups() throws Exception {
    CollectionGroup collectionGroup = createCollectionGroupWithDocuments(13);
    AggregateQuerySnapshot snapshot = collectionGroup.count().get().get();
    assertThat(snapshot.getCount()).isEqualTo(13);
  }

  @Test
  public void inFlightCountQueriesShouldCompleteSuccessfullyWhenFirestoreIsClosed()
      throws Exception {
    CollectionReference collection = createCollectionWithDocuments(20).collection();
    ApiFuture<AggregateQuerySnapshot> task = collection.count().get();
    collection.getFirestore().close();
    assertThat(task.get().getCount()).isEqualTo(20);
  }

  @Test
  public void inFlightCountQueriesShouldCompleteSuccessfullyWhenFirestoreIsShutDownGracefully()
      throws Exception {
    CollectionReference collection = createCollectionWithDocuments(20).collection();
    ApiFuture<AggregateQuerySnapshot> task = collection.count().get();
    collection.getFirestore().shutdown();
    assertThat(task.get().getCount()).isEqualTo(20);
  }

  @Test
  public void inFlightCountQueriesShouldRunToCompletionWhenFirestoreIsShutDownForcefully()
      throws Exception {
    CollectionReference collection = createCollectionWithDocuments(20).collection();
    ApiFuture<AggregateQuerySnapshot> task = collection.count().get();
    collection.getFirestore().shutdownNow();
    await(task);
  }

  @Test
  public void countQueriesShouldFailIfStartedOnAClosedFirestoreInstance() throws Exception {
    CollectionReference collection = createEmptyCollection();
    AggregateQuery aggregateQuery = collection.count();
    collection.getFirestore().close();
    assertThrows(IllegalStateException.class, aggregateQuery::get);
  }

  @Test
  public void countQueriesShouldFailIfStartedOnAShutDownFirestoreInstance() throws Exception {
    CollectionReference collection = createEmptyCollection();
    AggregateQuery aggregateQuery = collection.count();
    collection.getFirestore().shutdown();
    assertThrows(IllegalStateException.class, aggregateQuery::get);
  }

  @Test
  public void aggregateSnapshotShouldHaveReasonableReadTime() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(5).collection();
    AggregateQuerySnapshot snapshot1 = collection.count().get().get();
    AggregateQuerySnapshot snapshot2 = collection.count().get().get();
    long readTimeMs1 = msFromTimestamp(snapshot1.getReadTime());
    long readTimeMs2 = msFromTimestamp(snapshot2.getReadTime());
    assertThat(readTimeMs1).isLessThan(readTimeMs2);
  }

  @Test
  public void aggregateSnapshotShouldHaveCorrectQuery() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(5).collection();
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

  @Test
  public void aggregateQueryShouldWorkInATransaction() throws Exception {
    CollectionReference collection = createCollectionWithDocuments(7).collection();
    ApiFuture<AggregateQuerySnapshot> transactionFuture =
        collection.getFirestore().runTransaction(t -> t.get(collection.count()).get());
    AggregateQuerySnapshot snapshot = transactionFuture.get();
    assertThat(snapshot.getCount()).isEqualTo(7);
  }

  @Test
  public void aggregateQueryInATransactionShouldLockTheCountedDocuments() throws Exception {
    CollectionReference collection = createEmptyCollection();
    DocumentReference document = createDocumentInCollection(collection);
    CountDownLatch aggregateQueryExecutedSignal = new CountDownLatch(1);
    CountDownLatch transactionContinueSignal = new CountDownLatch(1);

    ApiFuture<Object> transactionFuture =
        collection
            .getFirestore()
            .runTransaction(
                t -> {
                  t.get(collection.count()).get();
                  aggregateQueryExecutedSignal.countDown();
                  transactionContinueSignal.await();
                  return null;
                });

    ExecutionException executionException;
    try {
      aggregateQueryExecutedSignal.await();
      ApiFuture<WriteResult> documentSetTask = document.set(singletonMap("abc", "def"));
      executionException = assertThrows(ExecutionException.class, documentSetTask::get);
    } finally {
      transactionContinueSignal.countDown();
    }

    assertThat(executionException)
        .hasCauseThat()
        .hasMessageThat()
        .ignoringCase()
        .contains("transaction lock timeout");

    // Wait for the transaction to finish.
    transactionFuture.get();
  }

  @Test
  public void aggregateQueryInATransactionRespectReadTime() throws Exception {
    CreatedCollectionInfo createdCollectionInfo = createCollectionWithDocuments(10);
    CollectionReference collection = createdCollectionInfo.collection();
    List<DocumentReference> documents = createdCollectionInfo.documents();

    // Get a snapshot of the query to get its read time.
    AggregateQuerySnapshot snapshot = collection.count().get().get();
    assertThat(snapshot.getCount()).isEqualTo(5);

    // Delete one of the documents from the result set so that, below, we can verify that specifying
    // the read time before this deletion will count the documents at the old read time.
    documents.get(0).delete().get();

    ApiFuture<Long> transactionFuture =
        collection
            .getFirestore()
            .runTransaction(
                t -> t.get(collection.count()).get().getCount(),
                TransactionOptions.createReadOnlyOptionsBuilder()
                    .setReadTime(snapshot.getReadTime().toProto())
                    .build());

    Long transactionCount = transactionFuture.get();

    // NOTE: Snapshot reads are not yet implemented in the Firestore emulator (b/220918135). As a
    // result, this test will fail when run against the Firestore emulator because it will
    // incorrectly ignore the read time and return the count of the documents at the current time.
    assertThat(transactionCount).isEqualTo(4);
  }

  private CollectionReference createEmptyCollection() {
    String collectionPath = "java-" + testName.getMethodName() + "-" + autoId();
    return firestore.collection(collectionPath);
  }

  private CreatedCollectionInfo createCollectionWithDocuments(int numDocuments)
      throws ExecutionException, InterruptedException {
    CollectionReference collection = createEmptyCollection();
    List<DocumentReference> createdDocuments =
        createDocumentsWithKeyValuePair(collection, numDocuments, "key", 42);
    return CreatedCollectionInfo.create(collection, createdDocuments);
  }

  private List<DocumentReference> createDocumentsWithKeyValuePair(
      CollectionReference collection, int numDocumentsToCreate, String key, int value)
      throws ExecutionException, InterruptedException {
    WriteBatch writeBatch = firestore.batch();
    ArrayList<DocumentReference> createdDocuments = new ArrayList<>();
    for (int i = 0; i < numDocumentsToCreate; i++) {
      DocumentReference doc = collection.document();
      createdDocuments.add(doc);
      writeBatch.create(doc, singletonMap(key, value));
    }
    writeBatch.commit().get();
    return createdDocuments;
  }

  private List<DocumentReference> createDocumentsInCollection(
      CollectionReference collection, int numDocumentsToCreate)
      throws ExecutionException, InterruptedException {
    return createDocumentsWithKeyValuePair(collection, numDocumentsToCreate, "meaning", 42);
  }

  private DocumentReference createDocumentInCollection(CollectionReference collection)
      throws ExecutionException, InterruptedException {
    WriteBatch writeBatch = collection.getFirestore().batch();
    DocumentReference createdDocument =
        createDocumentInCollection(writeBatch, collection, "age", 42);
    writeBatch.commit();
    return createdDocument;
  }

  private void createDocumentInCollection(WriteBatch writeBatch, CollectionReference collection)
      throws ExecutionException, InterruptedException {
    createDocumentInCollection(writeBatch, collection, "age", 42);
  }

  private DocumentReference createDocumentInCollection(
      WriteBatch writeBatch, CollectionReference collection, String key, int value)
      throws ExecutionException, InterruptedException {
    DocumentReference doc = collection.document();
    writeBatch.create(doc, singletonMap(key, value));
    return doc;
  }

  private CollectionGroup createCollectionGroupWithDocuments(int numDocumentsToCreate)
      throws ExecutionException, InterruptedException {
    String collectionId = autoId();

    // Create some collections to participate in the group.
    ArrayList<CollectionReference> collections = new ArrayList<>();
    for (int i = 0; i <= numDocumentsToCreate / 3; i++) {
      collections.add(createEmptyCollection().document().collection(collectionId));
    }

    // Populate the collections with documents.
    WriteBatch writeBatch = firestore.batch();
    for (int i = 0; i < numDocumentsToCreate; i++) {
      CollectionReference collection = collections.get(i % collections.size());
      createDocumentInCollection(writeBatch, collection);
    }

    writeBatch.commit().get();

    return firestore.collectionGroup(collectionId);
  }

  /** Converts a {@link Timestamp} to the equivalent number of milliseconds. */
  private static long msFromTimestamp(Timestamp timestamp) {
    return (timestamp.getSeconds() * 1_000) + (timestamp.getNanos() / 1_000_000);
  }

  /**
   * Blocks the calling thread until the given future completes. Note that this method does not
   * check the success or failure of the future; it returns regardless of its success or failure.
   */
  private static void await(ApiFuture<?> future) throws InterruptedException {
    AtomicBoolean done = new AtomicBoolean(false);
    ExecutorService executor = Executors.newSingleThreadExecutor();
    future.addListener(
        () -> {
          synchronized (done) {
            done.set(true);
            done.notifyAll();
          }
        },
        executor);

    synchronized (done) {
      while (!done.get()) {
        done.wait();
      }
    }

    executor.shutdown();
  }

  @AutoValue
  abstract static class CreatedCollectionInfo {

    abstract CollectionReference collection();

    abstract List<DocumentReference> documents();

    static CreatedCollectionInfo create(
        CollectionReference collection, List<DocumentReference> documents) {
      return new AutoValue_ITQueryCountTest_CreatedCollectionInfo(collection, documents);
    }
  }
}
