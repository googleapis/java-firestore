/*
 * Copyright 2024 Google LLC
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.api.gax.rpc.AbortedException;
import com.google.cloud.firestore.AggregateField;
import com.google.cloud.firestore.AggregateQuerySnapshot;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.ExplainMetrics;
import com.google.cloud.firestore.ExplainOptions;
import com.google.cloud.firestore.ExplainResults;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class ITPortedTests {

  @Rule public TestName testName = new TestName();

  private Firestore firestore;
  private CollectionReference randomCol;

  @Before
  public void before() {
    firestore = FirestoreOptions.getDefaultInstance().getService();
    randomCol = firestore.collection(testName.getMethodName() + "_" + System.nanoTime());
  }

  @After
  public void after() throws Exception {
    firestore.close();
  }

  @Test
  public void testHasCollectionMethod() {
    assertEquals("col", firestore.collection("col").getId());
  }

  @Test
  public void testHasDocMethod() {
    assertEquals("doc", firestore.document("col/doc").getId());
  }

  @Test
  public void testGetAllMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref1 = randomCol.document("doc1");
    DocumentReference ref2 = randomCol.document("doc2");
    ref1.set(ImmutableMap.of("foo", "a")).get();
    ref2.set(ImmutableMap.of("foo", "a")).get();
    List<DocumentSnapshot> docs = firestore.getAll(ref1, ref2).get();
    assertEquals(2, docs.size());
  }

  @Test
  public void canPlanQueryUsingDefaultOptions() throws ExecutionException, InterruptedException {
    randomCol.document("doc1").set(ImmutableMap.of("foo", 1)).get();
    randomCol.document("doc2").set(ImmutableMap.of("foo", 2)).get();
    randomCol.document("doc3").set(ImmutableMap.of("foo", 1)).get();
    ExplainResults explainResults =
        randomCol.whereGreaterThan("foo", 1).explain(ExplainOptions.builder().build()).get();

    // Should have metrics.
    ExplainMetrics metrics = explainResults.getMetrics();
    assertNotNull(metrics);

    // Should have query plan.
    assertNotNull(metrics.getPlanSummary());

    // No execution stats and no snapshot.
    assertNull(metrics.getExecutionStats());
    assertNull(explainResults.getSnapshot());
  }

  @Test
  public void canPlanQuery() throws ExecutionException, InterruptedException {
    randomCol.document("doc1").set(ImmutableMap.of("foo", 1)).get();
    randomCol.document("doc2").set(ImmutableMap.of("foo", 2)).get();
    randomCol.document("doc3").set(ImmutableMap.of("foo", 1)).get();
    ExplainResults explainResults =
        randomCol
            .whereGreaterThan("foo", 1)
            .explain(ExplainOptions.builder().setAnalyze(false).build())
            .get();

    // Should have metrics.
    ExplainMetrics metrics = explainResults.getMetrics();
    assertNotNull(metrics);

    // Should have query plan.
    assertNotNull(metrics.getPlanSummary());

    // No execution stats and no snapshot.
    assertNull(metrics.getExecutionStats());
    assertNull(explainResults.getSnapshot());
  }

  @Test
  public void canProfileQuery() throws ExecutionException, InterruptedException {
    randomCol.document("doc1").set(ImmutableMap.of("foo", 1, "bar", 0)).get();
    randomCol.document("doc2").set(ImmutableMap.of("foo", 2, "bar", 1)).get();
    randomCol.document("doc3").set(ImmutableMap.of("foo", 1, "bar", 2)).get();
    ExplainResults explainResults =
        randomCol
            .whereEqualTo("foo", 1)
            .explain(ExplainOptions.builder().setAnalyze(true).build())
            .get();

    ExplainMetrics metrics = explainResults.getMetrics();

    assertNotNull(metrics.getPlanSummary());
    assertNotNull(metrics.getExecutionStats());
    assertNotNull(explainResults.getSnapshot());

    assertTrue(metrics.getPlanSummary().getIndexesUsed().size() > 0);

    com.google.cloud.firestore.ExecutionStats stats = metrics.getExecutionStats();
    assertTrue(stats.getReadOperations() > 0);
    assertEquals(2L, stats.getResultsReturned());
    assertTrue(
        stats.getExecutionDuration().getNano() > 0
            || stats.getExecutionDuration().getSeconds() > 0);
    assertTrue(stats.getDebugStats() != null);

    assertEquals(2, explainResults.getSnapshot().size());
  }

  @Test
  public void canProfileQueryThatDoesNotMatchAnyDocs()
      throws ExecutionException, InterruptedException {
    randomCol.document("doc1").set(ImmutableMap.of("foo", 1, "bar", 0)).get();
    randomCol.document("doc2").set(ImmutableMap.of("foo", 2, "bar", 1)).get();
    randomCol.document("doc3").set(ImmutableMap.of("foo", 1, "bar", 2)).get();
    QuerySnapshot results = randomCol.whereEqualTo("foo", 12345).get().get();
    assertTrue(results.isEmpty());
    assertEquals(0, results.getDocuments().size());
    assertTrue(results.getReadTime().toDate().getTime() > 0);

    ExplainResults explainResults =
        randomCol
            .whereEqualTo("foo", 12345)
            .explain(ExplainOptions.builder().setAnalyze(true).build())
            .get();

    ExplainMetrics metrics = explainResults.getMetrics();

    assertNotNull(metrics.getPlanSummary());
    assertNotNull(metrics.getExecutionStats());
    assertNotNull(explainResults.getSnapshot());

    assertTrue(metrics.getPlanSummary().getIndexesUsed().size() > 0);

    com.google.cloud.firestore.ExecutionStats stats = metrics.getExecutionStats();
    assertTrue(stats.getReadOperations() > 0);
    assertEquals(0L, stats.getResultsReturned());
    assertTrue(
        stats.getExecutionDuration().getNano() > 0
            || stats.getExecutionDuration().getSeconds() > 0);
    assertTrue(stats.getDebugStats() != null);

    assertEquals(0, explainResults.getSnapshot().size());
  }

  @Test
  public void canStreamExplainResultsWithDefaultOptions() throws InterruptedException {
    randomCol.document("doc1").set(ImmutableMap.of("foo", 1, "bar", 0));
    randomCol.document("doc2").set(ImmutableMap.of("foo", 2, "bar", 1));
    randomCol.document("doc3").set(ImmutableMap.of("foo", 1, "bar", 2));
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    final AtomicInteger totalResponses = new AtomicInteger();
    final AtomicInteger totalDocuments = new AtomicInteger();
    final ExplainMetrics[] metrics = new ExplainMetrics[1];
    randomCol.explain(
        ExplainOptions.builder().build(),
        new ApiStreamObserver<ExplainResults>() {
          @Override
          public void onNext(ExplainResults data) {
            totalResponses.incrementAndGet();
            if (data.getSnapshot() != null) {
              totalDocuments.addAndGet(data.getSnapshot().getDocuments().size());
            }
            if (data.getMetrics() != null) {
              metrics[0] = data.getMetrics();
            }
          }

          @Override
          public void onError(Throwable t) {}

          @Override
          public void onCompleted() {
            latch.countDown();
          }
        });
    latch.await();
    assertEquals(1, totalResponses.get());
    assertEquals(0, totalDocuments.get());
    assertNotNull(metrics[0]);
    assertTrue(metrics[0].getPlanSummary().getIndexesUsed().size() > 0);
    assertNull(metrics[0].getExecutionStats());
  }

  @Test
  public void canStreamExplainResultsWithoutAnalyze() throws InterruptedException {
    randomCol.document("doc1").set(ImmutableMap.of("foo", 1, "bar", 0));
    randomCol.document("doc2").set(ImmutableMap.of("foo", 2, "bar", 1));
    randomCol.document("doc3").set(ImmutableMap.of("foo", 1, "bar", 2));
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    final AtomicInteger totalResponses = new AtomicInteger();
    final AtomicInteger totalDocuments = new AtomicInteger();
    final ExplainMetrics[] metrics = new ExplainMetrics[1];
    randomCol.explain(
        ExplainOptions.builder().setAnalyze(false).build(),
        new ApiStreamObserver<ExplainResults>() {
          @Override
          public void onNext(ExplainResults data) {
            totalResponses.incrementAndGet();
            if (data.getSnapshot() != null) {
              totalDocuments.addAndGet(data.getSnapshot().getDocuments().size());
            }
            if (data.getMetrics() != null) {
              metrics[0] = data.getMetrics();
            }
          }

          @Override
          public void onError(Throwable t) {}

          @Override
          public void onCompleted() {
            latch.countDown();
          }
        });
    latch.await();
    assertEquals(1, totalResponses.get());
    assertEquals(0, totalDocuments.get());
    assertNotNull(metrics[0]);
    assertTrue(metrics[0].getPlanSummary().getIndexesUsed().size() > 0);
    assertNull(metrics[0].getExecutionStats());
  }

  @Test
  public void canStreamExplainResultsWithAnalyze() throws InterruptedException {
    randomCol.document("doc1").set(ImmutableMap.of("foo", 1, "bar", 0));
    randomCol.document("doc2").set(ImmutableMap.of("foo", 2, "bar", 1));
    randomCol.document("doc3").set(ImmutableMap.of("foo", 1, "bar", 2));
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    final AtomicInteger totalResponses = new AtomicInteger();
    final AtomicInteger totalDocuments = new AtomicInteger();
    final ExplainMetrics[] metrics = new ExplainMetrics[1];
    randomCol
        .whereEqualTo("foo", 1)
        .explain(
            ExplainOptions.builder().setAnalyze(true).build(),
            new ApiStreamObserver<ExplainResults>() {
              @Override
              public void onNext(ExplainResults data) {
                totalResponses.incrementAndGet();
                if (data.getSnapshot() != null) {
                  totalDocuments.addAndGet(data.getSnapshot().getDocuments().size());
                }
                if (data.getMetrics() != null) {
                  metrics[0] = data.getMetrics();
                }
              }

              @Override
              public void onError(Throwable t) {}

              @Override
              public void onCompleted() {
                latch.countDown();
              }
            });
    latch.await();
    assertEquals(2, totalResponses.get());
    assertEquals(2, totalDocuments.get());
    assertNotNull(metrics[0]);
    assertTrue(metrics[0].getPlanSummary().getIndexesUsed().size() > 0);
    assertNotNull(metrics[0].getExecutionStats());
    assertEquals(2L, metrics[0].getExecutionStats().getResultsReturned());
  }

  @Test
  public void canPlanAggregateQueryUsingDefaultOptions()
      throws ExecutionException, InterruptedException {
    randomCol.document("doc1").set(ImmutableMap.of("foo", 1)).get();
    randomCol.document("doc2").set(ImmutableMap.of("foo", 2)).get();
    randomCol.document("doc3").set(ImmutableMap.of("foo", 1)).get();
    ExplainResults explainResults =
        randomCol
            .whereGreaterThan("foo", 0)
            .count()
            .explain(ExplainOptions.builder().build())
            .get();

    ExplainMetrics metrics = explainResults.getMetrics();

    assertNotNull(metrics.getPlanSummary());
    assertTrue(metrics.getPlanSummary().getIndexesUsed().size() > 0);

    assertNull(metrics.getExecutionStats());
    assertNull(explainResults.getSnapshot());
  }

  @Test
  public void canPlanAggregateQuery() throws ExecutionException, InterruptedException {
    randomCol.document("doc1").set(ImmutableMap.of("foo", 1)).get();
    randomCol.document("doc2").set(ImmutableMap.of("foo", 2)).get();
    randomCol.document("doc3").set(ImmutableMap.of("foo", 1)).get();
    ExplainResults explainResults =
        randomCol
            .whereGreaterThan("foo", 0)
            .count()
            .explain(ExplainOptions.builder().setAnalyze(false).build())
            .get();

    ExplainMetrics metrics = explainResults.getMetrics();

    assertNotNull(metrics.getPlanSummary());
    assertTrue(metrics.getPlanSummary().getIndexesUsed().size() > 0);

    assertNull(metrics.getExecutionStats());
    assertNull(explainResults.getSnapshot());
  }

  @Test
  public void canProfileAggregateQuery() throws ExecutionException, InterruptedException {
    randomCol.document("doc1").set(ImmutableMap.of("foo", 1)).get();
    randomCol.document("doc2").set(ImmutableMap.of("foo", 2)).get();
    randomCol.document("doc3").set(ImmutableMap.of("foo", 1)).get();
    ExplainResults explainResults =
        randomCol
            .whereLessThan("foo", 3)
            .count()
            .explain(ExplainOptions.builder().setAnalyze(true).build())
            .get();

    ExplainMetrics metrics = explainResults.getMetrics();
    assertNotNull(metrics.getPlanSummary());
    assertTrue(metrics.getPlanSummary().getIndexesUsed().size() > 0);

    assertNotNull(metrics.getExecutionStats());
    com.google.cloud.firestore.ExecutionStats stats = metrics.getExecutionStats();
    assertTrue(stats.getReadOperations() > 0);
    assertEquals(1L, stats.getResultsReturned());
    assertTrue(
        stats.getExecutionDuration().getNano() > 0
            || stats.getExecutionDuration().getSeconds() > 0);
    assertTrue(stats.getDebugStats() != null);

    assertNotNull(explainResults.getSnapshot());
    assertEquals(3L, explainResults.getSnapshot().getCount());
  }

  @Test
  public void getAllSupportsArrayDestructuring() throws ExecutionException, InterruptedException {
    DocumentReference ref1 = randomCol.document("doc1");
    DocumentReference ref2 = randomCol.document("doc2");
    ref1.set(ImmutableMap.of("foo", "a")).get();
    ref2.set(ImmutableMap.of("foo", "a")).get();
    List<DocumentSnapshot> docs = firestore.getAll(ref1, ref2).get();
    assertEquals(2, docs.size());
  }

  @Test
  public void getAllSupportsFieldMask() throws ExecutionException, InterruptedException {
    DocumentReference ref1 = randomCol.document("doc1");
    ref1.set(ImmutableMap.of("foo", "a", "bar", "b")).get();
    List<DocumentSnapshot> docs =
        firestore
            .getAll(new DocumentReference[] {ref1}, com.google.cloud.firestore.FieldMask.of("foo"))
            .get();
    assertEquals(ImmutableMap.of("foo", "a"), docs.get(0).getData());
  }

  @Test
  public void getAllSupportsArrayDestructuringWithFieldMask()
      throws ExecutionException, InterruptedException {
    DocumentReference ref1 = randomCol.document("doc1");
    DocumentReference ref2 = randomCol.document("doc2");
    ref1.set(ImmutableMap.of("f", "a", "b", "b")).get();
    ref2.set(ImmutableMap.of("f", "a", "b", "b")).get();
    List<DocumentSnapshot> docs =
        firestore
            .getAll(
                new DocumentReference[] {ref1, ref2}, com.google.cloud.firestore.FieldMask.of("f"))
            .get();
    assertEquals(ImmutableMap.of("f", "a"), docs.get(0).getData());
    assertEquals(ImmutableMap.of("f", "a"), docs.get(1).getData());
  }

  @Test
  public void getAllSupportsGenerics() throws ExecutionException, InterruptedException {
    DocumentReference ref1 = randomCol.document("doc1");
    DocumentReference ref2 = randomCol.document("doc2");
    ref1.set(new Post("post1", "author1")).get();
    ref2.set(new Post("post2", "author2")).get();

    List<DocumentSnapshot> docs = firestore.getAll(ref1, ref2).get();
    assertEquals("post1, by author1", docs.get(0).toObject(Post.class).toString());
    assertEquals("post2, by author2", docs.get(1).toObject(Post.class).toString());
  }

  @Test
  public void cannotMakeCallsAfterTheClientHasBeenTerminated() throws Exception {
    DocumentReference ref1 = randomCol.document("doc1");
    firestore.close();
    try {
      ref1.set(ImmutableMap.of("foo", 100)).get();
      fail("should have thrown");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof IllegalStateException);
    }
  }

  @Test
  public void throwsAnErrorIfTerminateIsCalledWithActiveListeners() {
    DocumentReference ref = randomCol.document("doc-1");
    ref.addSnapshotListener(
        (snapshot, error) -> {
          // No-op
        });
    try {
      firestore.close();
      fail("should have thrown");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
    }
  }

  @Test
  public void throwsAnErrorIfTerminateIsCalledWithPendingBulkWriterOperations() throws Exception {
    firestore.bulkWriter().set(randomCol.document("doc-1"), ImmutableMap.of("foo", "bar"));
    try {
      firestore.close();
      fail("should have thrown");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
    }
  }

  // Skipping partition query tests when running against the emulator because
  // partition queries are not supported by the emulator.
  // @Ignore
  // @Test
  // public void partitionQuery() throws ExecutionException, InterruptedException {
  //   int desiredPartitionCount = 3;
  //   int documentCount = 2 * 128 + 127; // Minimum partition size is 128.

  //   CollectionReference collectionGroup = firestore.collection(randomCol.getId());

  //   WriteBatch batch = firestore.batch();
  //   for (int i = 0; i < documentCount; ++i) {
  //     batch.create(randomCol.document(), ImmutableMap.of("title", "post", "author", "author"));
  //   }
  //   batch.commit().get();

  //   List<QueryPartition> partitions =
  //       collectionGroup.getPartitions(desiredPartitionCount).get();

  //   assertFalse(partitions.isEmpty());
  // }

  // @Ignore
  // @Test
  // public void partitionQueryWithManualCursors() throws ExecutionException, InterruptedException {
  //   int desiredPartitionCount = 3;
  //   int documentCount = 2 * 128 + 127; // Minimum partition size is 128.

  //   CollectionReference collectionGroup = firestore.collection(randomCol.getId());

  //   WriteBatch batch = firestore.batch();
  //   for (int i = 0; i < documentCount; ++i) {
  //     batch.create(randomCol.document(), ImmutableMap.of("title", "post", "author", "author"));
  //   }
  //   batch.commit().get();

  //   List<QueryPartition> partitions =
  //       collectionGroup.getPartitions(desiredPartitionCount).get();

  //   assertFalse(partitions.isEmpty());
  // }

  // @Ignore
  // @Test
  // public void partitionQueryWithConverter() throws ExecutionException, InterruptedException {
  //   int desiredPartitionCount = 3;
  //   int documentCount = 2 * 128 + 127; // Minimum partition size is 128.

  //   CollectionReference collectionGroup = firestore.collection(randomCol.getId());

  //   WriteBatch batch = firestore.batch();
  //   for (int i = 0; i < documentCount; ++i) {
  //     batch.create(randomCol.document(), ImmutableMap.of("title", "post", "author", "author"));
  //   }
  //   batch.commit().get();

  //   List<QueryPartition> partitions =
  //       collectionGroup.withConverter(new
  // Post.PostConverter()).getPartitions(desiredPartitionCount).get();

  //   assertFalse(partitions.isEmpty());
  // }

  // @Ignore
  // @Test
  // public void emptyPartitionQuery() throws ExecutionException, InterruptedException {
  //   int desiredPartitionCount = 3;

  //   CollectionReference collectionGroup = firestore.collection(randomCol.getId());

  //   List<QueryPartition> partitions =
  //       collectionGroup.getPartitions(desiredPartitionCount).get();

  //   assertEquals(1, partitions.size());
  //   assertNull(partitions.get(0).getStartAt());
  //   assertNull(partitions.get(0).getEndBefore());
  // }

  @Test
  public void testCollectionReferenceHasFirestoreProperty() {
    CollectionReference ref = firestore.collection("col");
    assertEquals(firestore, ref.getFirestore());
  }

  @Test
  public void testCollectionReferenceHasIdProperty() {
    CollectionReference ref = firestore.collection("col");
    assertEquals("col", ref.getId());
  }

  @Test
  public void testCollectionReferenceHasParentProperty() {
    DocumentReference ref = firestore.collection("col/doc/col").getParent();
    assertEquals("doc", ref.getId());
  }

  @Test
  public void testCollectionReferenceHasPathProperty() {
    CollectionReference ref = firestore.collection("col/doc/col");
    assertEquals("col/doc/col", ref.getPath());
  }

  @Test
  public void testCollectionReferenceHasDocMethod() {
    DocumentReference ref = firestore.collection("col").document("doc");
    assertEquals("doc", ref.getId());
    ref = firestore.collection("col").document();
    assertEquals(20, ref.getId().length());
  }

  @Test
  public void testCollectionReferenceHasAddMethod()
      throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.add(ImmutableMap.of("foo", "a")).get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals("a", doc.getString("foo"));
  }

  @Test
  public void testListsMissingDocuments() throws ExecutionException, InterruptedException {
    firestore.batch().set(randomCol.document("a"), ImmutableMap.of()).commit().get();
    firestore.batch().set(randomCol.document("b/b/b"), ImmutableMap.of()).commit().get();
    firestore.batch().set(randomCol.document("c"), ImmutableMap.of()).commit().get();

    java.util.List<DocumentReference> documentRefs = new java.util.ArrayList<>();
    randomCol.listDocuments().forEach(documentRefs::add);
    List<DocumentSnapshot> documents =
        firestore.getAll(documentRefs.toArray(new DocumentReference[0])).get();

    List<String> existingDocs =
        documents.stream()
            .filter(DocumentSnapshot::exists)
            .map(doc -> doc.getId())
            .collect(java.util.stream.Collectors.toList());
    List<String> missingDocs =
        documents.stream()
            .filter(doc -> !doc.exists())
            .map(doc -> doc.getId())
            .collect(java.util.stream.Collectors.toList());

    assertEquals(Arrays.asList("a", "c"), existingDocs);
    assertEquals(Arrays.asList("b"), missingDocs);
  }

  @Test
  public void testListsDocumentsMoreThanTheMaxPageSize()
      throws ExecutionException, InterruptedException {
    com.google.cloud.firestore.WriteBatch batch = firestore.batch();
    List<String> expectedResults = new java.util.ArrayList<>();
    for (int i = 0; i < 400; i++) {
      String docId = String.format("%03d", i);
      DocumentReference docRef = randomCol.document(docId);
      batch.set(docRef, ImmutableMap.of("id", i));
      expectedResults.add(docRef.getId());
    }
    batch.commit().get();

    java.util.List<DocumentReference> documentRefs = new java.util.ArrayList<>();
    randomCol.listDocuments().forEach(documentRefs::add);

    List<String> actualDocIds =
        documentRefs.stream()
            .map(dr -> dr.getId())
            .sorted()
            .collect(java.util.stream.Collectors.toList());

    assertEquals(expectedResults, actualDocIds);
  }

  @Test
  public void testSupportsWithConverter() throws ExecutionException, InterruptedException {
    DocumentReference ref =
        randomCol.withConverter(new Post.PostConverter()).add(new Post("post", "author")).get();
    DocumentSnapshot postData = ref.get().get();
    Post post = postData.toObject(Post.class);
    assertNotNull(post);
    assertEquals("post, by author", post.toString());
  }

  @Test
  public void testDocumentReferenceHasFirestoreProperty() {
    DocumentReference ref = firestore.doc("col/doc");
    assertEquals(firestore, ref.getFirestore());
  }

  @Test
  public void testDocumentReferenceHasIdProperty() {
    DocumentReference ref = firestore.doc("col/doc");
    assertEquals("doc", ref.getId());
  }

  @Test
  public void testDocumentReferenceHasParentProperty() {
    CollectionReference ref = firestore.doc("col/doc").getParent();
    assertEquals("col", ref.getId());
  }

  @Test
  public void testDocumentReferenceHasPathProperty() {
    DocumentReference ref = firestore.doc("col/doc");
    assertEquals("col/doc", ref.getPath());
  }

  @Test
  public void testDocumentReferenceHasCollectionMethod() {
    CollectionReference ref = firestore.doc("col/doc").collection("subcol");
    assertEquals("subcol", ref.getId());
  }

  @Test
  public void testDocumentReferenceHasCreateGetMethod()
      throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document();
    ref.create(ImmutableMap.of("foo", "a")).get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals("a", doc.getString("foo"));
  }

  @Test
  public void testDocumentReferenceHasSetMethod() throws ExecutionException, InterruptedException {
    Map<String, Object> allSupportedTypesObject = new java.util.HashMap<>();
    allSupportedTypesObject.put("stringValue", "a");
    allSupportedTypesObject.put("trueValue", true);
    allSupportedTypesObject.put("falseValue", false);
    allSupportedTypesObject.put("integerValue", 10);
    allSupportedTypesObject.put("largeIntegerValue", 1234567890000L);
    allSupportedTypesObject.put("doubleValue", 0.1);
    allSupportedTypesObject.put("infinityValue", Double.POSITIVE_INFINITY);
    allSupportedTypesObject.put("negativeInfinityValue", Double.NEGATIVE_INFINITY);
    allSupportedTypesObject.put("objectValue", ImmutableMap.of("foo", "bar", "😀", "😜"));
    allSupportedTypesObject.put("emptyObject", Collections.emptyMap());
    allSupportedTypesObject.put(
        "dateValue", com.google.cloud.Timestamp.ofTimeSecondsAndNanos(479978400, 123000000));
    allSupportedTypesObject.put(
        "zeroDateValue", com.google.cloud.Timestamp.ofTimeSecondsAndNanos(0, 0));
    allSupportedTypesObject.put("pathValue", firestore.document("col1/ref1"));
    allSupportedTypesObject.put("arrayValue", Arrays.asList("foo", 42, "bar"));
    allSupportedTypesObject.put("emptyArray", Collections.emptyList());
    allSupportedTypesObject.put("nilValue", null);
    allSupportedTypesObject.put(
        "geoPointValue", new com.google.cloud.firestore.GeoPoint(50.1430847, -122.947778));
    allSupportedTypesObject.put("zeroGeoPointValue", new com.google.cloud.firestore.GeoPoint(0, 0));
    allSupportedTypesObject.put(
        "bytesValue", com.google.cloud.firestore.Blob.fromBytes(new byte[] {0x01, 0x02}));

    DocumentReference ref = randomCol.document("doc");
    ref.set(allSupportedTypesObject).get();
    DocumentSnapshot doc = ref.get().get();
    Map<String, Object> data = doc.getData();
    assertEquals(
        ((DocumentReference) allSupportedTypesObject.get("pathValue")).getPath(),
        ((DocumentReference) data.get("pathValue")).getPath());
    data.remove("pathValue");
    allSupportedTypesObject.remove("pathValue");
    assertEquals(allSupportedTypesObject, data);
  }

  @Test
  public void supportsNaNs() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("nanValue", Double.NaN)).get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals(Double.NaN, doc.getDouble("nanValue"), 0);
  }

  @Test
  public void roundTripsBigInts() throws ExecutionException, InterruptedException {
    long bigIntValue = Long.MAX_VALUE;

    Firestore firestoreWithBigInt =
        FirestoreOptions.newBuilder()
            .setProjectId("test-project")
            .setUseBigInt(true)
            .build()
            .getService();
    CollectionReference randomColWithBigInt =
        firestoreWithBigInt.collection(testName.getMethodName() + "_" + System.nanoTime());
    DocumentReference ref = randomColWithBigInt.document("doc");
    ref.set(ImmutableMap.of("bigIntValue", bigIntValue)).get();
    DocumentSnapshot doc = ref.get().get();
    ref.set(doc.getData()).get();
    doc = ref.get().get();
    assertEquals(bigIntValue, doc.getLong("bigIntValue").longValue());
  }

  @Test
  public void supportsServerTimestamps() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(
            ImmutableMap.of(
                "a", "bar",
                "b", ImmutableMap.of("remove", "bar"),
                "d", ImmutableMap.of("keep", "bar"),
                "f", FieldValue.serverTimestamp()))
        .get();
    DocumentSnapshot doc = ref.get().get();
    com.google.cloud.Timestamp setTimestamp = doc.getTimestamp("f");
    assertNotNull(setTimestamp);
    assertEquals(
        ImmutableMap.of(
            "a",
            "bar",
            "b",
            ImmutableMap.of("remove", "bar"),
            "d",
            ImmutableMap.of("keep", "bar"),
            "f",
            setTimestamp),
        doc.getData());
    ref.update(
            ImmutableMap.of(
                "a", FieldValue.serverTimestamp(),
                "b", ImmutableMap.of("c", FieldValue.serverTimestamp()),
                "d.e", FieldValue.serverTimestamp()))
        .get();
    doc = ref.get().get();
    com.google.cloud.Timestamp updateTimestamp = doc.getTimestamp("a");
    assertNotNull(updateTimestamp);
    assertEquals(
        ImmutableMap.of(
            "a",
            updateTimestamp,
            "b",
            ImmutableMap.of("c", updateTimestamp),
            "d",
            ImmutableMap.of("e", updateTimestamp, "keep", "bar"),
            "f",
            setTimestamp),
        doc.getData());
  }

  @Test
  public void supportsIncrement() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("sum", 1)).get();
    ref.update(ImmutableMap.of("sum", FieldValue.increment(1))).get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals(2L, doc.getLong("sum").longValue());
  }

  @Test
  public void supportsIncrementWithSetWithMerge() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("sum", 1)).get();
    ref.set(ImmutableMap.of("sum", FieldValue.increment(1)), SetOptions.merge()).get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals(2L, doc.getLong("sum").longValue());
  }

  @Test
  public void supportsArrayUnion() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(
            ImmutableMap.of(
                "a", Collections.emptyList(),
                "b", Arrays.asList("foo"),
                "c", ImmutableMap.of("d", Arrays.asList("foo"))))
        .get();
    ref.update(
            ImmutableMap.of(
                "a", FieldValue.arrayUnion("foo", "bar"),
                "b", FieldValue.arrayUnion("foo", "bar"),
                "c.d", FieldValue.arrayUnion("foo", "bar")))
        .get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals(Arrays.asList("foo", "bar"), doc.get("a"));
    assertEquals(Arrays.asList("foo", "bar"), doc.get("b"));
    assertEquals(ImmutableMap.of("d", Arrays.asList("foo", "bar")), doc.get("c"));
  }

  @Test
  public void supportsArrayRemove() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(
            ImmutableMap.of(
                "a", Collections.emptyList(),
                "b", Arrays.asList("foo", "foo", "baz"),
                "c", ImmutableMap.of("d", Arrays.asList("foo", "bar", "baz"))))
        .get();
    ref.update(
            ImmutableMap.of(
                "a", FieldValue.arrayRemove("foo"),
                "b", FieldValue.arrayRemove("foo"),
                "c.d", FieldValue.arrayRemove("foo", "bar")))
        .get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals(Collections.emptyList(), doc.get("a"));
    assertEquals(Arrays.asList("baz"), doc.get("b"));
    assertEquals(ImmutableMap.of("d", Arrays.asList("baz")), doc.get("c"));
  }

  @Test
  public void supportsSetWithMerge() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("a.1", "foo", "nested", ImmutableMap.of("b.1", "bar"))).get();
    ref.set(
            ImmutableMap.of("a.2", "foo", "nested", ImmutableMap.of("b.2", "bar")),
            SetOptions.merge())
        .get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals(
        ImmutableMap.of(
            "a.1", "foo",
            "a.2", "foo",
            "nested", ImmutableMap.of("b.1", "bar", "b.2", "bar")),
        doc.getData());
  }

  @Test
  public void supportsServerTimestampsForMerge() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("a", "b")).get();
    ref.set(ImmutableMap.of("c", FieldValue.serverTimestamp()), SetOptions.merge()).get();
    DocumentSnapshot doc = ref.get().get();
    com.google.cloud.Timestamp updateTimestamp = doc.getTimestamp("c");
    assertNotNull(updateTimestamp);
    assertEquals(ImmutableMap.of("a", "b", "c", updateTimestamp), doc.getData());
  }

  @Test
  public void hasUpdateMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    WriteResult res = ref.set(ImmutableMap.of("foo", "a")).get();
    ref.update(ImmutableMap.of("foo", "b")).get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals("b", doc.getString("foo"));
  }

  @Test
  public void enforcesThatUpdatedDocumentExists() {
    try {
      randomCol.document().update(ImmutableMap.of("foo", "b")).get();
      fail("should have thrown");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof AbortedException);
    }
  }

  @Test
  public void hasDeleteMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("foo", "a")).get();
    ref.delete().get();
    DocumentSnapshot result = ref.get().get();
    assertFalse(result.exists());
  }

  @Test
  public void canDeleteANonExistingDocument() throws ExecutionException, InterruptedException {
    DocumentReference ref = firestore.collection("col").document();
    ref.delete().get();
  }

  @Test
  public void willFailToDeleteDocumentWithExistsTrueIfDocDoesNotExist() {
    DocumentReference ref = randomCol.document();
    try {
      ref.delete(
              com.google.cloud.firestore.Precondition.fromUpdateTime(
                  com.google.cloud.Timestamp.ofTimeSecondsAndNanos(0, 0)))
          .get();
      fail("should have thrown");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof AbortedException);
    }
  }

  @Test
  public void supportsNonAlphanumericFieldNames() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("!.\\`", ImmutableMap.of("!.\\`", "value"))).get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals(ImmutableMap.of("!.\\`", ImmutableMap.of("!.\\`", "value")), doc.getData());
    ref.update("!.\\`.!.\\`", "new-value").get();
    doc = ref.get().get();
    assertEquals(ImmutableMap.of("!.\\`", ImmutableMap.of("!.\\`", "new-value")), doc.getData());
  }

  @Test
  public void hasListCollectionsMethod() throws ExecutionException, InterruptedException {
    List<String> collections = new java.util.ArrayList<>();
    List<com.google.api.core.ApiFuture<WriteResult>> promises = new java.util.ArrayList<>();

    for (int i = 0; i < 10; i++) {
      String collectionId = String.format("%03d", i);
      promises.add(
          randomCol.document("doc/" + collectionId + "/doc").create(Collections.emptyMap()));
      collections.add(collectionId);
    }

    for (com.google.api.core.ApiFuture<WriteResult> promise : promises) {
      promise.get();
    }

    java.util.List<CollectionReference> response = new java.util.ArrayList<>();
    randomCol.document("doc").listCollections().forEach(response::add);
    assertEquals(collections.size(), response.size());
    for (int i = 0; i < response.size(); ++i) {
      assertEquals(collections.get(i), response.get(i).getId());
    }
  }

  @Test
  public void watchHandlesChangingADoc() throws InterruptedException {
    final DocumentReference ref = randomCol.document("doc");
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(3);
    ref.addSnapshotListener(
        (snapshot, error) -> {
          latch.countDown();
        });
    latch.await();
  }

  @Test
  public void snapshotListenerSortsUnicodeStringsInDocumentKeySameAsServer()
      throws InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "Łukasiewicz", ImmutableMap.of("value", true),
            "Sierpiński", ImmutableMap.of("value", true),
            "岩澤", ImmutableMap.of("value", true),
            "🄟", ImmutableMap.of("value", true),
            "Ｐ", ImmutableMap.of("value", true),
            "︒", ImmutableMap.of("value", true),
            "🐵", ImmutableMap.of("value", true),
            "你好", ImmutableMap.of("value", true),
            "你顥", ImmutableMap.of("value", true),
            "😁", ImmutableMap.of("value", true),
            "😀", ImmutableMap.of("value", true)));

    Query query = randomCol.orderBy(com.google.cloud.firestore.FieldPath.documentId());
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    query.addSnapshotListener(
        (snapshot, error) -> {
          if (snapshot != null && snapshot.size() == 11) {
            latch.countDown();
          }
        });
    latch.await();
  }

  @Test
  public void snapshotListenerSortsUnicodeStringsInMapKeySameAsServer()
      throws InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a", ImmutableMap.of("value", ImmutableMap.of("Łukasiewicz", true)),
            "b", ImmutableMap.of("value", ImmutableMap.of("Sierpiński", true)),
            "c", ImmutableMap.of("value", ImmutableMap.of("岩澤", true)),
            "d", ImmutableMap.of("value", ImmutableMap.of("🄟", true)),
            "e", ImmutableMap.of("value", ImmutableMap.of("Ｐ", true)),
            "f", ImmutableMap.of("value", ImmutableMap.of("︒", true)),
            "g", ImmutableMap.of("value", ImmutableMap.of("🐵", true)),
            "h", ImmutableMap.of("value", ImmutableMap.of("你好", true)),
            "i", ImmutableMap.of("value", ImmutableMap.of("你顥", true)),
            "j", ImmutableMap.of("value", ImmutableMap.of("😁", true)),
            "k", ImmutableMap.of("value", ImmutableMap.of("😀", true))));

    Query query = randomCol.orderBy("value");
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    query.addSnapshotListener(
        (snapshot, error) -> {
          if (snapshot != null && snapshot.size() == 11) {
            latch.countDown();
          }
        });
    latch.await();
  }

  @Test
  public void snapshotListenerSortsUnicodeStringsInMapSameAsServer() throws InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a", ImmutableMap.of("value", ImmutableMap.of("foo", "Łukasiewicz")),
            "b", ImmutableMap.of("value", ImmutableMap.of("foo", "Sierpiński")),
            "c", ImmutableMap.of("value", ImmutableMap.of("foo", "岩澤")),
            "d", ImmutableMap.of("value", ImmutableMap.of("foo", "🄟")),
            "e", ImmutableMap.of("value", ImmutableMap.of("foo", "Ｐ")),
            "f", ImmutableMap.of("value", ImmutableMap.of("foo", "︒")),
            "g", ImmutableMap.of("value", ImmutableMap.of("foo", "🐵")),
            "h", ImmutableMap.of("value", ImmutableMap.of("foo", "你好")),
            "i", ImmutableMap.of("value", ImmutableMap.of("foo", "你顥")),
            "j", ImmutableMap.of("value", ImmutableMap.of("foo", "😁")),
            "k", ImmutableMap.of("value", ImmutableMap.of("foo", "😀"))));

    Query query = randomCol.orderBy("value");
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    query.addSnapshotListener(
        (snapshot, error) -> {
          if (snapshot != null && snapshot.size() == 11) {
            latch.countDown();
          }
        });
    latch.await();
  }

  @Test
  public void snapshotListenerSortsUnicodeStringsInArraySameAsServer() throws InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a", ImmutableMap.of("value", Arrays.asList("Łukasiewicz")),
            "b", ImmutableMap.of("value", Arrays.asList("Sierpiński")),
            "c", ImmutableMap.of("value", Arrays.asList("岩澤")),
            "d", ImmutableMap.of("value", Arrays.asList("🄟")),
            "e", ImmutableMap.of("value", Arrays.asList("Ｐ")),
            "f", ImmutableMap.of("value", Arrays.asList("︒")),
            "g", ImmutableMap.of("value", Arrays.asList("🐵")),
            "h", ImmutableMap.of("value", Arrays.asList("你好")),
            "i", ImmutableMap.of("value", Arrays.asList("你顥")),
            "j", ImmutableMap.of("value", Arrays.asList("😁")),
            "k", ImmutableMap.of("value", Arrays.asList("😀"))));

    Query query = randomCol.orderBy("value");
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    query.addSnapshotListener(
        (snapshot, error) -> {
          if (snapshot != null && snapshot.size() == 11) {
            latch.countDown();
          }
        });
    latch.await();
  }

  @Test
  public void snapshotListenerSortsUnicodeStringsSameAsServer() throws InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a", ImmutableMap.of("value", "Łukasiewicz"),
            "b", ImmutableMap.of("value", "Sierpiński"),
            "c", ImmutableMap.of("value", "岩澤"),
            "d", ImmutableMap.of("value", "🄟"),
            "e", ImmutableMap.of("value", "Ｐ"),
            "f", ImmutableMap.of("value", "︒"),
            "g", ImmutableMap.of("value", "🐵"),
            "h", ImmutableMap.of("value", "你好"),
            "i", ImmutableMap.of("value", "你顥"),
            "j", ImmutableMap.of("value", "😁"),
            "k", ImmutableMap.of("value", "😀")));

    Query query = randomCol.orderBy("value");
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    query.addSnapshotListener(
        (snapshot, error) -> {
          if (snapshot != null && snapshot.size() == 11) {
            latch.countDown();
          }
        });
    latch.await();
  }

  @Test
  public void sdkOrdersVectorFieldSameWayAsBackend()
      throws ExecutionException, InterruptedException {
    Object[] docsInOrder = {
      ImmutableMap.of("embedding", Arrays.asList(1, 2, 3, 4, 5, 6)),
      ImmutableMap.of("embedding", Arrays.asList(100)),
      ImmutableMap.of(
          "embedding",
          com.google.cloud.firestore.VectorValue.from(Arrays.asList(Double.NEGATIVE_INFINITY))),
      ImmutableMap.of(
          "embedding", com.google.cloud.firestore.VectorValue.from(Arrays.asList(-100.0))),
      ImmutableMap.of(
          "embedding", com.google.cloud.firestore.VectorValue.from(Arrays.asList(100.0))),
      ImmutableMap.of(
          "embedding",
          com.google.cloud.firestore.VectorValue.from(Arrays.asList(Double.POSITIVE_INFINITY))),
      ImmutableMap.of(
          "embedding", com.google.cloud.firestore.VectorValue.from(Arrays.asList(1.0, 2.0))),
      ImmutableMap.of(
          "embedding", com.google.cloud.firestore.VectorValue.from(Arrays.asList(2.0, 2.0))),
      ImmutableMap.of(
          "embedding", com.google.cloud.firestore.VectorValue.from(Arrays.asList(1.0, 2.0, 3.0))),
      ImmutableMap.of(
          "embedding",
          com.google.cloud.firestore.VectorValue.from(Arrays.asList(1.0, 2.0, 3.0, 4.0))),
      ImmutableMap.of(
          "embedding",
          com.google.cloud.firestore.VectorValue.from(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0))),
      ImmutableMap.of(
          "embedding",
          com.google.cloud.firestore.VectorValue.from(Arrays.asList(1.0, 2.0, 100.0, 4.0, 4.0))),
      ImmutableMap.of(
          "embedding",
          com.google.cloud.firestore.VectorValue.from(Arrays.asList(100.0, 2.0, 3.0, 4.0, 5.0))),
      ImmutableMap.of("embedding", ImmutableMap.of("HELLO", "WORLD")),
      ImmutableMap.of("embedding", ImmutableMap.of("hello", "world")),
    };

    for (Object doc : docsInOrder) {
      randomCol.add((Map<String, Object>) doc).get();
    }

    Query orderedQuery = randomCol.orderBy("embedding");

    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    orderedQuery.addSnapshotListener(
        (snapshot, error) -> {
          if (snapshot != null && snapshot.size() == 15) {
            latch.countDown();
          }
        });
    latch.await();
  }

  @Test
  public void snapshotListenerSortsFilteredQueryByDocumentIdSameWayAsServer()
      throws ExecutionException, InterruptedException {
    com.google.cloud.firestore.WriteBatch batch = firestore.batch();
    batch.set(randomCol.document("A"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("a"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("Aa"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("7"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("12"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id7__"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id12__"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id-2__"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id1_"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("_id1__"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id"), ImmutableMap.of("a", 1));
    // largest long number
    batch.set(randomCol.document("__id9223372036854775807__"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id9223372036854775806__"), ImmutableMap.of("a", 1));
    // smallest long number
    batch.set(randomCol.document("__id-9223372036854775808__"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id-9223372036854775807__"), ImmutableMap.of("a", 1));
    batch.commit().get();

    Query query =
        randomCol
            .whereGreaterThan(com.google.cloud.firestore.FieldPath.documentId(), "__id7__")
            .whereLessThanOrEqualTo(com.google.cloud.firestore.FieldPath.documentId(), "A")
            .orderBy(com.google.cloud.firestore.FieldPath.documentId());
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    query.addSnapshotListener(
        (snapshot, error) -> {
          if (snapshot != null && snapshot.size() == 6) {
            latch.countDown();
          }
        });
    latch.await();
  }

  @Test
  public void watchHandlesDeletingADocInQuery() throws InterruptedException, ExecutionException {
    final DocumentReference ref1 = randomCol.document("doc1");
    final DocumentReference ref2 = randomCol.document("doc2");
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(4);
    randomCol.addSnapshotListener(
        (snapshot, error) -> {
          latch.countDown();
        });
    ref1.set(ImmutableMap.of("foo", "a")).get();
    ref2.set(ImmutableMap.of("foo", "b")).get();
    ref2.delete().get();
    latch.await();
  }

  @Test
  public void watchHandlesDeletingADoc() throws InterruptedException {
    final DocumentReference ref = randomCol.document("doc");
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(3);
    ref.addSnapshotListener(
        (snapshot, error) -> {
          latch.countDown();
        });
    latch.await();
  }

  @Test
  public void watchHandlesMultipleDocs() throws InterruptedException {
    final DocumentReference doc1 = randomCol.document();
    final DocumentReference doc2 = randomCol.document();
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(6);
    doc1.addSnapshotListener(
        (snapshot, error) -> {
          latch.countDown();
        });
    doc2.addSnapshotListener(
        (snapshot, error) -> {
          latch.countDown();
        });
    latch.await();
  }

  @Test
  public void watchHandlesMultipleStreamsOnSameDoc() throws InterruptedException {
    final DocumentReference doc = randomCol.document();
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(6);
    doc.addSnapshotListener(
        (snapshot, error) -> {
          latch.countDown();
        });
    doc.addSnapshotListener(
        (snapshot, error) -> {
          latch.countDown();
        });
    latch.await();
  }

  @Test
  public void canGetLargeCollection() throws ExecutionException, InterruptedException {
    for (int i = 0; i < 1000; i++) {
      randomCol.add(ImmutableMap.of("foo", "a")).get();
    }
    QuerySnapshot res = randomCol.get().get();
    assertEquals(1000, res.size());
  }

  @Test
  public void canStreamLargeCollection() throws InterruptedException {
    for (int i = 0; i < 1000; i++) {
      randomCol.add(ImmutableMap.of("foo", "a"));
    }
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    final AtomicInteger received = new AtomicInteger();
    randomCol.stream(
        (doc, error) -> {
          received.incrementAndGet();
        },
        latch::countDown);
    latch.await();
    assertEquals(1000, received.get());
  }

  @Test
  public void queryHasFirestoreProperty() {
    Query query = randomCol.limit(0);
    assertEquals(firestore, query.getFirestore());
  }

  @Test
  public void queryHasSelectMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("foo", "bar", "bar", "foo")).get();
    QuerySnapshot res = randomCol.select("foo").get().get();
    assertEquals(ImmutableMap.of("foo", "bar"), res.getDocuments().get(0).getData());
  }

  @Test
  public void querySelectSupportsEmptyFields() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("foo", "bar", "bar", "foo")).get();
    QuerySnapshot res = randomCol.select().get().get();
    assertEquals("doc", res.getDocuments().get(0).getId());
    assertEquals(Collections.emptyMap(), res.getDocuments().get(0).getData());
  }

  @Test
  public void queryHasWhereMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("foo", "bar")).get();
    QuerySnapshot res = randomCol.whereEqualTo("foo", "bar").get().get();
    assertEquals(ImmutableMap.of("foo", "bar"), res.getDocuments().get(0).getData());
  }

  @Test
  public void querySupportsNaNAndNull() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("foo", Double.NaN, "bar", null)).get();
    QuerySnapshot res =
        randomCol.whereEqualTo("foo", Double.NaN).whereEqualTo("bar", null).get().get();
    assertEquals(Double.NaN, res.getDocuments().get(0).getDouble("foo"), 0);
    assertNull(res.getDocuments().get(0).get("bar"));
  }

  @Test
  public void querySupportsArrayContains() throws ExecutionException, InterruptedException {
    randomCol.add(ImmutableMap.of("foo", Arrays.asList("bar"))).get();
    randomCol.add(ImmutableMap.of("foo", Collections.emptyList())).get();
    QuerySnapshot res = randomCol.whereArrayContains("foo", "bar").get().get();
    assertEquals(1, res.size());
    assertEquals(Arrays.asList("bar"), res.getDocuments().get(0).get("foo"));
  }

  @Test
  public void querySupportsNotEquals() throws ExecutionException, InterruptedException {
    addDocs(
        ImmutableMap.of("zip", Double.NaN),
        ImmutableMap.of("zip", 91102),
        ImmutableMap.of("zip", 98101),
        ImmutableMap.of("zip", 98103),
        ImmutableMap.of("zip", Arrays.asList(98101)),
        ImmutableMap.of("zip", Arrays.asList("98101", ImmutableMap.of("zip", 98101))),
        ImmutableMap.of("zip", ImmutableMap.of("zip", 98101)),
        ImmutableMap.of("zip", null));

    QuerySnapshot res = randomCol.whereNotEqualTo("zip", 98101).get().get();
    assertEquals(5, res.size());
  }

  @Test
  public void querySupportsNotEqualsWithDocumentId()
      throws ExecutionException, InterruptedException {
    List<DocumentReference> refs =
        addDocs(
            ImmutableMap.of("count", 1), ImmutableMap.of("count", 2), ImmutableMap.of("count", 3));
    QuerySnapshot res =
        randomCol
            .whereNotEqualTo(com.google.cloud.firestore.FieldPath.documentId(), refs.get(0).getId())
            .get()
            .get();
    assertEquals(2, res.size());
  }

  @Test
  public void querySupportsNotIn() throws ExecutionException, InterruptedException {
    addDocs(
        ImmutableMap.of("zip", 98101),
        ImmutableMap.of("zip", 91102),
        ImmutableMap.of("zip", 98103),
        ImmutableMap.of("zip", Arrays.asList(98101)),
        ImmutableMap.of("zip", Arrays.asList("98101", ImmutableMap.of("zip", 98101))),
        ImmutableMap.of("zip", ImmutableMap.of("zip", 98101)));
    QuerySnapshot res = randomCol.whereNotIn("zip", Arrays.asList(98101, 98103)).get().get();
    assertEquals(4, res.size());
  }

  @Test
  public void querySupportsNotInWithDocumentIdArray()
      throws ExecutionException, InterruptedException {
    List<DocumentReference> refs =
        addDocs(
            ImmutableMap.of("count", 1), ImmutableMap.of("count", 2), ImmutableMap.of("count", 3));
    QuerySnapshot res =
        randomCol
            .whereNotIn(
                com.google.cloud.firestore.FieldPath.documentId(),
                Arrays.asList(refs.get(0).getId(), refs.get(1)))
            .get()
            .get();
    assertEquals(1, res.size());
  }

  @Test
  public void querySupportsIn() throws ExecutionException, InterruptedException {
    addDocs(
        ImmutableMap.of("zip", 98101),
        ImmutableMap.of("zip", 91102),
        ImmutableMap.of("zip", 98103),
        ImmutableMap.of("zip", Arrays.asList(98101)),
        ImmutableMap.of("zip", Arrays.asList("98101", ImmutableMap.of("zip", 98101))),
        ImmutableMap.of("zip", ImmutableMap.of("zip", 98101)));
    QuerySnapshot res = randomCol.whereIn("zip", Arrays.asList(98101, 98103)).get().get();
    assertEquals(2, res.size());
  }

  @Test
  public void querySupportsInWithDocumentIdArray() throws ExecutionException, InterruptedException {
    List<DocumentReference> refs =
        addDocs(
            ImmutableMap.of("count", 1), ImmutableMap.of("count", 2), ImmutableMap.of("count", 3));
    QuerySnapshot res =
        randomCol
            .whereIn(
                com.google.cloud.firestore.FieldPath.documentId(),
                Arrays.asList(refs.get(0).getId(), refs.get(1)))
            .get()
            .get();
    assertEquals(2, res.size());
  }

  @Test
  public void querySupportsArrayContainsAny() throws ExecutionException, InterruptedException {
    addDocs(
        ImmutableMap.of("array", Arrays.asList(42)),
        ImmutableMap.of("array", Arrays.asList("a", 42, "c")),
        ImmutableMap.of(
            "array", Arrays.asList(41.999, "42", ImmutableMap.of("a", Arrays.asList(42)))),
        ImmutableMap.of("array", Arrays.asList(42), "array2", Arrays.asList("sigh")),
        ImmutableMap.of("array", Arrays.asList(43)),
        ImmutableMap.of("array", Arrays.asList(ImmutableMap.of("a", 42))),
        ImmutableMap.of("array", 42));

    QuerySnapshot res = randomCol.whereArrayContainsAny("array", Arrays.asList(42, 43)).get().get();

    assertEquals(4, res.size());
  }

  private List<DocumentReference> addDocs(Map<String, Object>... docs)
      throws ExecutionException, InterruptedException {
    List<DocumentReference> refs = new java.util.ArrayList<>();
    for (Map<String, Object> doc : docs) {
      refs.add(randomCol.add(doc).get());
    }
    return refs;
  }

  @Test
  public void canQueryByFieldPathDocumentId() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("foo");
    ref.set(Collections.emptyMap()).get();
    QuerySnapshot res =
        randomCol
            .whereGreaterThanOrEqualTo(com.google.cloud.firestore.FieldPath.documentId(), "bar")
            .get()
            .get();
    assertEquals(1, res.size());
  }

  @Test
  public void hasOrderByMethod() throws ExecutionException, InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    QuerySnapshot res = randomCol.orderBy("foo").get().get();
    assertEquals("a", res.getDocuments().get(0).getString("foo"));
    assertEquals("b", res.getDocuments().get(1).getString("foo"));
    res = randomCol.orderBy("foo", Query.Direction.DESCENDING).get().get();
    assertEquals("b", res.getDocuments().get(0).getString("foo"));
    assertEquals("a", res.getDocuments().get(1).getString("foo"));
  }

  @Test
  public void canOrderByFieldPathDocumentId() throws ExecutionException, InterruptedException {
    DocumentReference ref1 = randomCol.document("doc1");
    DocumentReference ref2 = randomCol.document("doc2");
    ref1.set(ImmutableMap.of("foo", "a")).get();
    ref2.set(ImmutableMap.of("foo", "b")).get();
    QuerySnapshot res =
        randomCol.orderBy(com.google.cloud.firestore.FieldPath.documentId()).get().get();
    assertEquals("a", res.getDocuments().get(0).getString("foo"));
    assertEquals("b", res.getDocuments().get(1).getString("foo"));
  }

  @Test
  public void canRunGetOnEmptyCollection() throws ExecutionException, InterruptedException {
    QuerySnapshot res = randomCol.get().get();
    assertTrue(res.isEmpty());
  }

  @Test
  public void canRunStreamOnEmptyCollection() throws InterruptedException {
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    final AtomicInteger received = new AtomicInteger();
    randomCol.stream(
        (doc, error) -> {
          received.incrementAndGet();
        },
        latch::countDown);
    latch.await();
    assertEquals(0, received.get());
  }

  @Test
  public void hasLimitMethodOnGet() throws ExecutionException, InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    QuerySnapshot res = randomCol.orderBy("foo").limit(1).get().get();
    assertEquals(1, res.size());
    assertEquals("a", res.getDocuments().get(0).getString("foo"));
  }

  @Test
  public void hasLimitMethodOnStream() throws InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    final AtomicInteger received = new AtomicInteger();
    randomCol.orderBy("foo").limit(1).stream(
        (doc, error) -> {
          received.incrementAndGet();
        },
        latch::countDown);
    latch.await();
    assertEquals(1, received.get());
  }

  @Test
  public void canRunLimitNumWhereNumIsLargerThanTheCollectionSizeOnGet()
      throws ExecutionException, InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    QuerySnapshot res = randomCol.orderBy("foo").limit(3).get().get();
    assertEquals(2, res.size());
  }

  @Test
  public void canRunLimitNumWhereNumIsLargerThanTheCollectionSizeOnStream()
      throws InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    final AtomicInteger received = new AtomicInteger();
    randomCol.orderBy("foo").limit(3).stream(
        (doc, error) -> {
          received.incrementAndGet();
        },
        latch::countDown);
    latch.await();
    assertEquals(2, received.get());
  }

  @Test
  public void hasLimitToLastMethod() throws ExecutionException, InterruptedException {
    addDocs(ImmutableMap.of("doc", 1), ImmutableMap.of("doc", 2), ImmutableMap.of("doc", 3));
    QuerySnapshot res = randomCol.orderBy("doc").limitToLast(2).get().get();
    assertEquals(2, res.size());
    assertEquals(2L, res.getDocuments().get(0).getLong("doc").longValue());
    assertEquals(3L, res.getDocuments().get(1).getLong("doc").longValue());
  }

  @Test
  public void limitToLastSupportsQueryCursors() throws ExecutionException, InterruptedException {
    addDocs(
        ImmutableMap.of("doc", 1),
        ImmutableMap.of("doc", 2),
        ImmutableMap.of("doc", 3),
        ImmutableMap.of("doc", 4),
        ImmutableMap.of("doc", 5));
    QuerySnapshot res = randomCol.orderBy("doc").startAt(2).endAt(4).limitToLast(5).get().get();
    assertEquals(3, res.size());
    assertEquals(2L, res.getDocuments().get(0).getLong("doc").longValue());
    assertEquals(3L, res.getDocuments().get(1).getLong("doc").longValue());
    assertEquals(4L, res.getDocuments().get(2).getLong("doc").longValue());
  }

  @Test
  public void canUseOffsetMethodWithGet() throws ExecutionException, InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    QuerySnapshot res = randomCol.orderBy("foo").offset(1).get().get();
    assertEquals(1, res.size());
    assertEquals("b", res.getDocuments().get(0).getString("foo"));
  }

  @Test
  public void canUseOffsetMethodWithStream() throws InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    final AtomicInteger received = new AtomicInteger();
    randomCol.orderBy("foo").offset(1).stream(
        (doc, error) -> {
          received.incrementAndGet();
        },
        latch::countDown);
    latch.await();
    assertEquals(1, received.get());
  }

  @Test
  public void canRunOffsetNumWhereNumIsLargerThanTheCollectionSizeOnGet()
      throws ExecutionException, InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    QuerySnapshot res = randomCol.orderBy("foo").offset(3).get().get();
    assertTrue(res.isEmpty());
  }

  @Test
  public void canRunOffsetNumWhereNumIsLargerThanTheCollectionSizeOnStream()
      throws InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    final AtomicInteger received = new AtomicInteger();
    randomCol.orderBy("foo").offset(3).stream(
        (doc, error) -> {
          received.incrementAndGet();
        },
        latch::countDown);
    latch.await();
    assertEquals(0, received.get());
  }

  @Test
  public void supportsUnicodeInDocumentNames() throws ExecutionException, InterruptedException {
    CollectionReference collRef = randomCol.document("доброеутро").collection("coll");
    collRef.add(Collections.emptyMap()).get();
    QuerySnapshot snapshot = collRef.get().get();
    assertEquals(1, snapshot.size());
  }

  @Test
  public void supportsPagination() throws ExecutionException, InterruptedException {
    WriteBatch batch = firestore.batch();
    for (int i = 0; i < 10; ++i) {
      batch.set(randomCol.document("doc" + i), ImmutableMap.of("val", i));
    }
    batch.commit().get();
    Query query = randomCol.orderBy("val").limit(3);
    QuerySnapshot snapshot = query.get().get();
    assertEquals(3, snapshot.size());
  }

  @Test
  public void supportsPaginationWithWhereClauses() throws ExecutionException, InterruptedException {
    WriteBatch batch = firestore.batch();
    for (int i = 0; i < 10; ++i) {
      batch.set(randomCol.document("doc" + i), ImmutableMap.of("val", i));
    }
    batch.commit().get();
    Query query = randomCol.whereGreaterThanOrEqualTo("val", 1).limit(3);
    QuerySnapshot snapshot = query.get().get();
    assertEquals(3, snapshot.size());
  }

  @Test
  public void supportsPaginationWithArrayContainsFilter()
      throws ExecutionException, InterruptedException {
    WriteBatch batch = firestore.batch();
    for (int i = 0; i < 10; ++i) {
      batch.set(randomCol.document("doc" + i), ImmutableMap.of("array", Arrays.asList("foo")));
    }
    batch.commit().get();
    Query query = randomCol.whereArrayContains("array", "foo").limit(3);
    QuerySnapshot snapshot = query.get().get();
    assertEquals(3, snapshot.size());
  }

  @Test
  public void hasStartAtMethod() throws ExecutionException, InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    QuerySnapshot res = randomCol.orderBy("foo").startAt("b").get().get();
    assertEquals(1, res.size());
    assertEquals("b", res.getDocuments().get(0).getString("foo"));
  }

  @Test
  public void startAtAddsImplicitOrderByForDocumentSnapshot()
      throws ExecutionException, InterruptedException {
    List<DocumentReference> references =
        addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    DocumentSnapshot docSnap = references.get(1).get().get();
    QuerySnapshot res = randomCol.startAt(docSnap).get().get();
    assertEquals(1, res.size());
    assertEquals("b", res.getDocuments().get(0).getString("foo"));
  }

  @Test
  public void hasStartAfterMethod() throws ExecutionException, InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    QuerySnapshot res = randomCol.orderBy("foo").startAfter("a").get().get();
    assertEquals(1, res.size());
    assertEquals("b", res.getDocuments().get(0).getString("foo"));
  }

  @Test
  public void hasEndAtMethod() throws ExecutionException, InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    QuerySnapshot res = randomCol.orderBy("foo").endAt("b").get().get();
    assertEquals(2, res.size());
  }

  @Test
  public void hasEndBeforeMethod() throws ExecutionException, InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    QuerySnapshot res = randomCol.orderBy("foo").endBefore("b").get().get();
    assertEquals(1, res.size());
  }

  @Test
  public void hasStreamMethod() throws InterruptedException {
    addDocs(ImmutableMap.of("foo", "a"), ImmutableMap.of("foo", "b"));
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    final AtomicInteger received = new AtomicInteger();
    randomCol.stream(
        (doc, error) -> {
          received.incrementAndGet();
        },
        latch::countDown);
    latch.await();
    assertEquals(2, received.get());
  }

  @Test
  public void streamSupportsReadableSymbolAsyncIterator() throws InterruptedException {
    addDocs(ImmutableMap.of("foo", "bar"), ImmutableMap.of("foo", "bar"));
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    final AtomicInteger received = new AtomicInteger();
    randomCol.stream(
        (doc, error) -> {
          received.incrementAndGet();
        },
        latch::countDown);
    latch.await();
    assertEquals(2, received.get());
  }

  @Test
  public void canQueryCollectionGroups() throws ExecutionException, InterruptedException {
    String collectionGroup = "b" + randomCol.getId();
    String[] docPaths = {
      "abc/123/" + collectionGroup + "/cg-doc1",
      "abc/123/" + collectionGroup + "/cg-doc2",
      collectionGroup + "/cg-doc3",
      collectionGroup + "/cg-doc4",
      "def/456/" + collectionGroup + "/cg-doc5",
      collectionGroup + "/virtual-doc/nested-coll/not-cg-doc",
      "x" + collectionGroup + "/not-cg-doc",
      collectionGroup + "x/not-cg-doc",
      "abc/123/" + collectionGroup + "x/not-cg-doc",
      "abc/123/x" + collectionGroup + "/not-cg-doc",
      "abc/" + collectionGroup,
    };
    WriteBatch batch = firestore.batch();
    for (String docPath : docPaths) {
      batch.set(firestore.doc(docPath), ImmutableMap.of("x", 1));
    }
    batch.commit().get();

    QuerySnapshot querySnapshot = firestore.collectionGroup(collectionGroup).get().get();
    List<String> actualIds =
        querySnapshot.getDocuments().stream().map(d -> d.getId()).collect(Collectors.toList());
    assertEquals(Arrays.asList("cg-doc1", "cg-doc2", "cg-doc3", "cg-doc4", "cg-doc5"), actualIds);
  }

  @Test
  public void canQueryCollectionGroupsWithStartAtEndAtByArbitraryDocumentId()
      throws ExecutionException, InterruptedException {
    String collectionGroup = "b" + randomCol.getId();
    String[] docPaths = {
      "a/a/" + collectionGroup + "/cg-doc1",
      "a/b/a/b/" + collectionGroup + "/cg-doc2",
      "a/b/" + collectionGroup + "/cg-doc3",
      "a/b/c/d/" + collectionGroup + "/cg-doc4",
      "a/c/" + collectionGroup + "/cg-doc5",
      collectionGroup + "/cg-doc6",
      "a/b/nope/nope",
    };
    WriteBatch batch = firestore.batch();
    for (String docPath : docPaths) {
      batch.set(firestore.doc(docPath), ImmutableMap.of("x", 1));
    }
    batch.commit().get();

    QuerySnapshot querySnapshot =
        firestore
            .collectionGroup(collectionGroup)
            .orderBy(com.google.cloud.firestore.FieldPath.documentId())
            .startAt("a/b")
            .endAt("a/b0")
            .get()
            .get();
    List<String> actualIds =
        querySnapshot.getDocuments().stream().map(d -> d.getId()).collect(Collectors.toList());
    assertEquals(Arrays.asList("cg-doc2", "cg-doc3", "cg-doc4"), actualIds);

    querySnapshot =
        firestore
            .collectionGroup(collectionGroup)
            .orderBy(com.google.cloud.firestore.FieldPath.documentId())
            .startAfter("a/b")
            .endBefore("a/b/" + collectionGroup + "/cg-doc3")
            .get()
            .get();
    actualIds =
        querySnapshot.getDocuments().stream().map(d -> d.getId()).collect(Collectors.toList());
    assertEquals(Arrays.asList("cg-doc2"), actualIds);
  }

  @Test
  public void canQueryCollectionGroupsWithWhereFiltersOnArbitraryDocumentId()
      throws ExecutionException, InterruptedException {
    String collectionGroup = "b" + randomCol.getId();
    String[] docPaths = {
      "a/a/" + collectionGroup + "/cg-doc1",
      "a/b/a/b/" + collectionGroup + "/cg-doc2",
      "a/b/" + collectionGroup + "/cg-doc3",
      "a/b/c/d/" + collectionGroup + "/cg-doc4",
      "a/c/" + collectionGroup + "/cg-doc5",
      collectionGroup + "/cg-doc6",
      "a/b/nope/nope",
    };
    WriteBatch batch = firestore.batch();
    for (String docPath : docPaths) {
      batch.set(firestore.doc(docPath), ImmutableMap.of("x", 1));
    }
    batch.commit().get();

    QuerySnapshot querySnapshot =
        firestore
            .collectionGroup(collectionGroup)
            .whereGreaterThanOrEqualTo(com.google.cloud.firestore.FieldPath.documentId(), "a/b")
            .whereLessThanOrEqualTo(com.google.cloud.firestore.FieldPath.documentId(), "a/b0")
            .get()
            .get();
    List<String> actualIds =
        querySnapshot.getDocuments().stream().map(d -> d.getId()).collect(Collectors.toList());
    assertEquals(Arrays.asList("cg-doc2", "cg-doc3", "cg-doc4"), actualIds);

    querySnapshot =
        firestore
            .collectionGroup(collectionGroup)
            .whereGreaterThan(com.google.cloud.firestore.FieldPath.documentId(), "a/b")
            .whereLessThan(
                com.google.cloud.firestore.FieldPath.documentId(),
                "a/b/" + collectionGroup + "/cg-doc3")
            .get()
            .get();
    actualIds =
        querySnapshot.getDocuments().stream().map(d -> d.getId()).collect(Collectors.toList());
    assertEquals(Arrays.asList("cg-doc2"), actualIds);
  }

  @Test
  public void canQueryLargeCollections() throws ExecutionException, InterruptedException {
    WriteBatch batch = firestore.batch();
    for (int i = 0; i < 100; ++i) {
      batch.create(randomCol.document(), Collections.emptyMap());
    }
    batch.commit().get();

    QuerySnapshot snapshot = randomCol.get().get();
    assertEquals(100, snapshot.size());
  }

  @Test
  public void supportsOrQueries() throws ExecutionException, InterruptedException {
    addDocs(
        ImmutableMap.of("a", 1, "b", 0),
        ImmutableMap.of("a", 2, "b", 1),
        ImmutableMap.of("a", 3, "b", 2),
        ImmutableMap.of("a", 1, "b", 3),
        ImmutableMap.of("a", 1, "b", 1));

    // Two equalities: a==1 || b==1.
    QuerySnapshot snapshot =
        randomCol
            .where(
                com.google.cloud.firestore.Filter.or(
                    com.google.cloud.firestore.Filter.whereEqualTo("a", 1),
                    com.google.cloud.firestore.Filter.whereEqualTo("b", 1)))
            .get()
            .get();
    assertEquals(4, snapshot.size());
  }

  @Test
  public void supportsOrQueriesWithCompositeIndexes()
      throws ExecutionException, InterruptedException {
    addDocs(
        ImmutableMap.of("a", 1, "b", 0),
        ImmutableMap.of("a", 2, "b", 1),
        ImmutableMap.of("a", 3, "b", 2),
        ImmutableMap.of("a", 1, "b", 3),
        ImmutableMap.of("a", 1, "b", 1));

    // with one inequality: a>2 || b==1.
    QuerySnapshot snapshot =
        randomCol
            .where(
                com.google.cloud.firestore.Filter.or(
                    com.google.cloud.firestore.Filter.whereGreaterThan("a", 2),
                    com.google.cloud.firestore.Filter.whereEqualTo("b", 1)))
            .get()
            .get();
    assertEquals(3, snapshot.size());
  }

  @Test
  public void supportsOrQueriesOnDocumentsWithMissingFields()
      throws ExecutionException, InterruptedException {
    addDocs(
        ImmutableMap.of("a", 1, "b", 0),
        ImmutableMap.of("b", 1),
        ImmutableMap.of("a", 3, "b", 2),
        ImmutableMap.of("a", 1, "b", 3),
        ImmutableMap.of("a", 1),
        ImmutableMap.of("a", 2));

    // Query: a==1 || b==1
    QuerySnapshot snapshot =
        randomCol
            .where(
                com.google.cloud.firestore.Filter.or(
                    com.google.cloud.firestore.Filter.whereEqualTo("a", 1),
                    com.google.cloud.firestore.Filter.whereEqualTo("b", 1)))
            .get()
            .get();
    assertEquals(4, snapshot.size());
  }

  @Test
  public void supportsOrQueriesWithIn() throws ExecutionException, InterruptedException {
    addDocs(
        ImmutableMap.of("a", 1, "b", 0),
        ImmutableMap.of("b", 1),
        ImmutableMap.of("a", 3, "b", 2),
        ImmutableMap.of("a", 1, "b", 3),
        ImmutableMap.of("a", 1),
        ImmutableMap.of("a", 2));

    // Query: a==2 || b in [2, 3]
    QuerySnapshot snapshot =
        randomCol
            .where(
                com.google.cloud.firestore.Filter.or(
                    com.google.cloud.firestore.Filter.whereEqualTo("a", 2),
                    com.google.cloud.firestore.Filter.whereIn("b", Arrays.asList(2, 3))))
            .get()
            .get();
    assertEquals(3, snapshot.size());
  }

  @Test
  public void supportsOrQueriesWithNotIn() throws ExecutionException, InterruptedException {
    addDocs(
        ImmutableMap.of("a", 1, "b", 0),
        ImmutableMap.of("b", 1),
        ImmutableMap.of("a", 3, "b", 2),
        ImmutableMap.of("a", 1, "b", 3),
        ImmutableMap.of("a", 1),
        ImmutableMap.of("a", 2));

    // a==2 || (b != 2 && b != 3)
    // Has implicit "orderBy b"
    QuerySnapshot snapshot =
        randomCol
            .where(
                com.google.cloud.firestore.Filter.or(
                    com.google.cloud.firestore.Filter.whereEqualTo("a", 2),
                    com.google.cloud.firestore.Filter.whereNotIn("b", Arrays.asList(2, 3))))
            .get()
            .get();
    assertEquals(4, snapshot.size());
  }

  @Test
  public void supportsOrQueriesWithArrayMembership()
      throws ExecutionException, InterruptedException {
    addDocs(
        ImmutableMap.of("a", 1, "b", Arrays.asList(0)),
        ImmutableMap.of("b", Arrays.asList(1)),
        ImmutableMap.of("a", 3, "b", Arrays.asList(2, 7)),
        ImmutableMap.of("a", 1, "b", Arrays.asList(3, 7)),
        ImmutableMap.of("a", 1),
        ImmutableMap.of("a", 2));

    // Query: a==2 || b array-contains 7
    QuerySnapshot snapshot =
        randomCol
            .where(
                com.google.cloud.firestore.Filter.or(
                    com.google.cloud.firestore.Filter.whereEqualTo("a", 2),
                    com.google.cloud.firestore.Filter.whereArrayContains("b", 7)))
            .get()
            .get();
    assertEquals(3, snapshot.size());

    // a==2 || b array-contains-any [0, 3]
    // Has implicit "orderBy b"
    snapshot =
        randomCol
            .where(
                com.google.cloud.firestore.Filter.or(
                    com.google.cloud.firestore.Filter.whereEqualTo("a", 2),
                    com.google.cloud.firestore.Filter.whereArrayContainsAny(
                        "b", Arrays.asList(0, 3))))
            .get()
            .get();
    assertEquals(3, snapshot.size());
  }

  @Test
  public void watchHandlesChangingADocInQuery() throws InterruptedException, ExecutionException {
    final DocumentReference ref1 = randomCol.document("doc1");
    final DocumentReference ref2 = randomCol.document("doc2");
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(4);
    randomCol.addSnapshotListener(
        (snapshot, error) -> {
          latch.countDown();
        });
    ref1.set(ImmutableMap.of("foo", "a")).get();
    ref2.set(ImmutableMap.of("foo", "b")).get();
    ref2.set(ImmutableMap.of("bar", "c")).get();
    latch.await();
  }

  @Test
  public void watchHandlesChangingADocSoItDoesntMatch()
      throws InterruptedException, ExecutionException {
    final DocumentReference ref1 = randomCol.document("doc1");
    final DocumentReference ref2 = randomCol.document("doc2");
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(4);
    randomCol
        .whereEqualTo("included", "yes")
        .addSnapshotListener(
            (snapshot, error) -> {
              latch.countDown();
            });
    ref1.set(ImmutableMap.of("included", "yes")).get();
    ref2.set(ImmutableMap.of("included", "yes")).get();
    ref2.set(ImmutableMap.of("included", "no")).get();
    latch.await();
  }

  @Test
  public void ordersLimitToLastCorrectly() throws ExecutionException, InterruptedException {
    DocumentReference ref1 = randomCol.document("doc1");
    DocumentReference ref2 = randomCol.document("doc2");
    DocumentReference ref3 = randomCol.document("doc3");

    ref1.set(ImmutableMap.of("doc", 1)).get();
    ref2.set(ImmutableMap.of("doc", 2)).get();
    ref3.set(ImmutableMap.of("doc", 3)).get();

    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    randomCol
        .orderBy("doc")
        .limitToLast(2)
        .addSnapshotListener(
            (snapshot, error) -> {
              if (snapshot != null && snapshot.size() == 2) {
                assertEquals(2L, snapshot.getDocuments().get(0).getLong("doc").longValue());
                assertEquals(3L, snapshot.getDocuments().get(1).getLong("doc").longValue());
                latch.countDown();
              }
            });
    latch.await();
  }

  @Test
  public void snapshotListenerSortsQueryByDocumentIdSameWayAsServer()
      throws ExecutionException, InterruptedException {
    com.google.cloud.firestore.WriteBatch batch = firestore.batch();
    batch.set(randomCol.document("A"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("a"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("Aa"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("7"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("12"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id7__"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id12__"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id-2__"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id1_"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("_id1__"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id"), ImmutableMap.of("a", 1));
    // largest long number
    batch.set(randomCol.document("__id9223372036854775807__"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id9223372036854775806__"), ImmutableMap.of("a", 1));
    // smallest long number
    batch.set(randomCol.document("__id-9223372036854775808__"), ImmutableMap.of("a", 1));
    batch.set(randomCol.document("__id-9223372036854775807__"), ImmutableMap.of("a", 1));
    batch.commit().get();

    Query query = randomCol.orderBy(com.google.cloud.firestore.FieldPath.documentId());
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    query.addSnapshotListener(
        (snapshot, error) -> {
          if (snapshot != null && snapshot.size() == 15) {
            latch.countDown();
          }
        });
    latch.await();
  }

  @Test
  public void countQueriesRunOutsideTransaction() throws ExecutionException, InterruptedException {
    assertEquals(0, randomCol.count().get().get().getCount());

    randomCol.document("doc").set(ImmutableMap.of("foo", "bar")).get();
    assertEquals(1, randomCol.whereEqualTo("foo", "notbar").count().get().get().getCount());

    assertEquals(1, randomCol.count().get().get().getCount());

    randomCol.document("doc1").set(ImmutableMap.of("foo", "bar")).get();
    randomCol.document("doc2").set(ImmutableMap.of("foo", "bar")).get();
    randomCol.document("doc3").set(ImmutableMap.of("foo", "notbar")).get();
    randomCol.document("doc3").set(ImmutableMap.of("notfoo", "bar")).get();
    assertEquals(2, randomCol.whereEqualTo("foo", "bar").count().get().get().getCount());

    randomCol.document("doc4").set(ImmutableMap.of("foo", "bar")).get();
    randomCol.document("doc5").set(ImmutableMap.of("foo", "bar")).get();
    randomCol.document("doc6").set(ImmutableMap.of("foo", "bar")).get();
    randomCol.document("doc7").set(ImmutableMap.of("foo", "bar")).get();
    randomCol.document("doc8").set(ImmutableMap.of("foo", "bar")).get();
    assertEquals(5, randomCol.limit(5).count().get().get().getCount());
  }

  @Test
  public void countQueriesRunInsideTransaction() throws ExecutionException, InterruptedException {
    firestore
        .runTransaction(
            transaction -> {
              AggregateQuerySnapshot snapshot = transaction.get(randomCol.count()).get();
              assertEquals(0, snapshot.getCount());
              return null;
            })
        .get();

    randomCol.document("doc").set(ImmutableMap.of("foo", "bar")).get();
    firestore
        .runTransaction(
            transaction -> {
              AggregateQuerySnapshot snapshot =
                  transaction.get(randomCol.whereEqualTo("foo", "notbar").count()).get();
              assertEquals(0, snapshot.getCount());
              return null;
            })
        .get();

    firestore
        .runTransaction(
            transaction -> {
              AggregateQuerySnapshot snapshot = transaction.get(randomCol.count()).get();
              assertEquals(1, snapshot.getCount());
              return null;
            })
        .get();
  }

  @Test
  public void countQueriesUsingAggregateApiRunOutsideTransaction()
      throws ExecutionException, InterruptedException {
    assertEquals(
        0, randomCol.aggregate(AggregateField.count()).get().get().get(AggregateField.count()));

    randomCol.document("doc").set(ImmutableMap.of("foo", "bar")).get();
    assertEquals(
        0,
        randomCol
            .whereEqualTo("foo", "notbar")
            .aggregate(AggregateField.count())
            .get()
            .get()
            .get(AggregateField.count()));

    assertEquals(
        1, randomCol.aggregate(AggregateField.count()).get().get().get(AggregateField.count()));

    randomCol.document("doc1").set(ImmutableMap.of("foo", "bar")).get();
    randomCol.document("doc2").set(ImmutableMap.of("foo", "bar")).get();
    randomCol.document("doc3").set(ImmutableMap.of("foo", "notbar")).get();
    randomCol.document("doc3").set(ImmutableMap.of("notfoo", "bar")).get();
    assertEquals(
        2,
        randomCol
            .whereEqualTo("foo", "bar")
            .aggregate(AggregateField.count())
            .get()
            .get()
            .get(AggregateField.count()));

    randomCol.document("doc4").set(ImmutableMap.of("foo", "bar")).get();
    randomCol.document("doc5").set(ImmutableMap.of("foo", "bar")).get();
    randomCol.document("doc6").set(ImmutableMap.of("foo", "bar")).get();
    randomCol.document("doc7").set(ImmutableMap.of("foo", "bar")).get();
    randomCol.document("doc8").set(ImmutableMap.of("foo", "bar")).get();
    assertEquals(
        5,
        randomCol
            .limit(5)
            .aggregate(AggregateField.count())
            .get()
            .get()
            .get(AggregateField.count()));
  }

  @Test
  public void countQueriesUsingAggregateApiRunInsideTransaction()
      throws ExecutionException, InterruptedException {
    firestore
        .runTransaction(
            transaction -> {
              AggregateQuerySnapshot snapshot =
                  transaction.get(randomCol.aggregate(AggregateField.count())).get();
              assertEquals(0, snapshot.get(AggregateField.count()).longValue());
              return null;
            })
        .get();

    randomCol.document("doc").set(ImmutableMap.of("foo", "bar")).get();
    firestore
        .runTransaction(
            transaction -> {
              AggregateQuerySnapshot snapshot =
                  transaction
                      .get(
                          randomCol.whereEqualTo("foo", "notbar").aggregate(AggregateField.count()))
                      .get();
              assertEquals(0, snapshot.get(AggregateField.count()).longValue());
              return null;
            })
        .get();

    firestore
        .runTransaction(
            transaction -> {
              AggregateQuerySnapshot snapshot =
                  transaction.get(randomCol.aggregate(AggregateField.count())).get();
              assertEquals(1, snapshot.get(AggregateField.count()).longValue());
              return null;
            })
        .get();
  }

  @Test
  public void canRunSumQuery() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a", ImmutableMap.of("author", "authorA", "title", "titleA", "pages", 100),
            "b", ImmutableMap.of("author", "authorB", "title", "titleB", "pages", 50)));
    AggregateQuerySnapshot snapshot = randomCol.aggregate(AggregateField.sum("pages")).get().get();
    assertEquals(150, snapshot.get(AggregateField.sum("pages")).longValue());
  }

  @Test
  public void canRunAverageQuery() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a", ImmutableMap.of("author", "authorA", "title", "titleA", "pages", 100),
            "b", ImmutableMap.of("author", "authorB", "title", "titleB", "pages", 50)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.average("pages")).get().get();
    assertEquals(75.0, snapshot.get(AggregateField.average("pages")), 0);
  }

  @Test
  public void canGetMultipleAggregations() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a", ImmutableMap.of("author", "authorA", "title", "titleA", "pages", 100),
            "b", ImmutableMap.of("author", "authorB", "title", "titleB", "pages", 50)));
    AggregateQuerySnapshot snapshot =
        randomCol
            .aggregate(
                AggregateField.sum("pages"),
                AggregateField.average("pages"),
                AggregateField.count())
            .get()
            .get();
    assertEquals(150, snapshot.get(AggregateField.sum("pages")).longValue());
    assertEquals(75.0, snapshot.get(AggregateField.average("pages")), 0);
    assertEquals(2, snapshot.get(AggregateField.count()).longValue());
  }

  @Test
  public void canGetDuplicateAggregations() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a", ImmutableMap.of("author", "authorA", "title", "titleA", "pages", 100),
            "b", ImmutableMap.of("author", "authorB", "title", "titleB", "pages", 50)));
    AggregateQuerySnapshot snapshot =
        randomCol
            .aggregate(
                AggregateField.sum("pages"),
                AggregateField.average("pages"),
                AggregateField.sum("pages"),
                AggregateField.average("pages"))
            .get()
            .get();
    assertEquals(150, snapshot.get(AggregateField.sum("pages")).longValue());
    assertEquals(75.0, snapshot.get(AggregateField.average("pages")), 0);
  }

  @Test
  public void canPerformMax5Aggregations() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a", ImmutableMap.of("author", "authorA", "title", "titleA", "pages", 100),
            "b", ImmutableMap.of("author", "authorB", "title", "titleB", "pages", 50)));
    AggregateQuerySnapshot snapshot =
        randomCol
            .aggregate(
                AggregateField.sum("pages"),
                AggregateField.average("pages"),
                AggregateField.count(),
                AggregateField.sum("pages"),
                AggregateField.average("pages"))
            .get()
            .get();
    assertEquals(150, snapshot.get(AggregateField.sum("pages")).longValue());
    assertEquals(75.0, snapshot.get(AggregateField.average("pages")), 0);
    assertEquals(2, snapshot.get(AggregateField.count()).longValue());
  }

  @Test
  public void failsWhenExceedingTheMax5Aggregations() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          randomCol.aggregate(
              AggregateField.sum("pages"),
              AggregateField.average("pages"),
              AggregateField.count(),
              AggregateField.sum("pages"),
              AggregateField.average("pages"),
              AggregateField.count());
        });
  }

  @Test
  public void returnsUndefinedWhenGettingTheResultOfAnUnrequestedAggregation()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    5),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 4),
            "c",
                ImmutableMap.of(
                    "author", "authorC", "title", "titleC", "pages", 100, "year", 1980, "rating",
                    3),
            "d",
                ImmutableMap.of(
                    "author", "authorD", "title", "titleD", "pages", 50, "year", 2020, "rating",
                    0)));
    AggregateQuerySnapshot snapshot =
        randomCol
            .aggregate(AggregateField.sum("rating"), AggregateField.average("rating"))
            .get()
            .get();
    assertNull(snapshot.get(AggregateField.sum("totalPages")));
  }

  @Test
  public void performsSumThatResultsInFloat() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    5),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating",
                    4.5),
            "c",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 150, "year", 2021, "rating",
                    3)));
    AggregateQuerySnapshot snapshot = randomCol.aggregate(AggregateField.sum("rating")).get().get();
    assertEquals(12.5, snapshot.get(AggregateField.sum("rating")), 0);
  }

  @Test
  public void performsSumOfIntsAndFloatsThatResultsInAnInt()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    5),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating",
                    4.5),
            "c",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 150, "year", 2021, "rating",
                    3.5)));
    AggregateQuerySnapshot snapshot = randomCol.aggregate(AggregateField.sum("rating")).get().get();
    assertEquals(13.0, snapshot.get(AggregateField.sum("rating")), 0);
  }

  @Test
  public void performsSumThatOverflowsMaxInt() throws ExecutionException, InterruptedException {
    long maxLong = Long.MAX_VALUE;

    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    maxLong),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating",
                    maxLong)));
    AggregateQuerySnapshot snapshot = randomCol.aggregate(AggregateField.sum("rating")).get().get();
    assertEquals(
        (double) maxLong + (double) maxLong, snapshot.get(AggregateField.sum("rating")), 0);
  }

  @Test
  public void performsSumThatCanOverflowIntegerValuesDuringAccumulation()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author",
                    "authorA",
                    "title",
                    "titleA",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    Long.MAX_VALUE),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 1),
            "c",
                ImmutableMap.of(
                    "author", "authorC", "title", "titleC", "pages", 50, "year", 2020, "rating",
                    -101)));
    AggregateQuerySnapshot snapshot = randomCol.aggregate(AggregateField.sum("rating")).get().get();
    assertEquals(Long.MAX_VALUE - 100, snapshot.get(AggregateField.sum("rating")));
  }

  @Test
  public void performsSumThatIsNegative() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author",
                    "authorA",
                    "title",
                    "titleA",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    Long.MAX_VALUE),
            "b",
                ImmutableMap.of(
                    "author",
                    "authorB",
                    "title",
                    "titleB",
                    "pages",
                    50,
                    "year",
                    2020,
                    "rating",
                    Long.MIN_VALUE),
            "c",
                ImmutableMap.of(
                    "author", "authorC", "title", "titleC", "pages", 50, "year", 2020, "rating",
                    -101),
            "d",
                ImmutableMap.of(
                    "author", "authorD", "title", "titleD", "pages", 50, "year", 2020, "rating",
                    -10000)));
    AggregateQuerySnapshot snapshot = randomCol.aggregate(AggregateField.sum("rating")).get().get();
    assertEquals(-10101.0, snapshot.get(AggregateField.sum("rating")), 0);
  }

  @Test
  public void performsSumThatIsPositiveInfinity() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author",
                    "authorA",
                    "title",
                    "titleA",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    Double.MAX_VALUE),
            "b",
                ImmutableMap.of(
                    "author",
                    "authorB",
                    "title",
                    "titleB",
                    "pages",
                    50,
                    "year",
                    2020,
                    "rating",
                    Double.MAX_VALUE)));
    AggregateQuerySnapshot snapshot = randomCol.aggregate(AggregateField.sum("rating")).get().get();
    assertEquals(Double.POSITIVE_INFINITY, snapshot.get(AggregateField.sum("rating")), 0);
  }

  @Test
  public void performsSumThatIsPositiveInfinityV2()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author",
                    "authorA",
                    "title",
                    "titleA",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    Double.MAX_VALUE),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating",
                    1e293)));
    AggregateQuerySnapshot snapshot = randomCol.aggregate(AggregateField.sum("rating")).get().get();
    assertEquals(Double.POSITIVE_INFINITY, snapshot.get(AggregateField.sum("rating")), 0);
  }

  @Test
  public void performsSumThatIsNegativeInfinity() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author",
                    "authorA",
                    "title",
                    "titleA",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    -Double.MAX_VALUE),
            "b",
                ImmutableMap.of(
                    "author",
                    "authorB",
                    "title",
                    "titleB",
                    "pages",
                    50,
                    "year",
                    2020,
                    "rating",
                    -Double.MAX_VALUE)));
    AggregateQuerySnapshot snapshot = randomCol.aggregate(AggregateField.sum("rating")).get().get();
    assertEquals(Double.NEGATIVE_INFINITY, snapshot.get(AggregateField.sum("rating")), 0);
  }

  @Test
  public void performsSumThatIsValidButCouldOverflowDuringAggregation()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author",
                    "authorA",
                    "title",
                    "titleA",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    Double.MAX_VALUE),
            "b",
                ImmutableMap.of(
                    "author",
                    "authorB",
                    "title",
                    "titleB",
                    "pages",
                    50,
                    "year",
                    2020,
                    "rating",
                    Double.MAX_VALUE),
            "c",
                ImmutableMap.of(
                    "author",
                    "authorC",
                    "title",
                    "titleC",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    -Double.MAX_VALUE),
            "d",
                ImmutableMap.of(
                    "author",
                    "authorD",
                    "title",
                    "titleD",
                    "pages",
                    50,
                    "year",
                    2020,
                    "rating",
                    -Double.MAX_VALUE),
            "e",
                ImmutableMap.of(
                    "author",
                    "authorE",
                    "title",
                    "titleE",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    Double.MAX_VALUE),
            "f",
                ImmutableMap.of(
                    "author",
                    "authorF",
                    "title",
                    "titleF",
                    "pages",
                    50,
                    "year",
                    2020,
                    "rating",
                    -Double.MAX_VALUE),
            "g",
                ImmutableMap.of(
                    "author",
                    "authorG",
                    "title",
                    "titleG",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    -Double.MAX_VALUE),
            "h",
                ImmutableMap.of(
                    "author",
                    "authorH",
                    "title",
                    "titleDH",
                    "pages",
                    50,
                    "year",
                    2020,
                    "rating",
                    Double.MAX_VALUE)));
    AggregateQuerySnapshot snapshot = randomCol.aggregate(AggregateField.sum("rating")).get().get();
    assertTrue(
        snapshot.get(AggregateField.sum("rating")) == 0.0
            || snapshot.get(AggregateField.sum("rating")) == Double.NEGATIVE_INFINITY
            || snapshot.get(AggregateField.sum("rating")) == Double.POSITIVE_INFINITY);
  }

  @Test
  public void performsSumThatIncludesNaN() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    5),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 4),
            "c",
                ImmutableMap.of(
                    "author",
                    "authorC",
                    "title",
                    "titleC",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    Double.NaN),
            "d",
                ImmutableMap.of(
                    "author", "authorD", "title", "titleD", "pages", 50, "year", 2020, "rating",
                    0)));
    AggregateQuerySnapshot snapshot = randomCol.aggregate(AggregateField.sum("rating")).get().get();
    assertEquals(Double.NaN, snapshot.get(AggregateField.sum("rating")), 0);
  }

  @Test
  public void performsSumOverAResultSetOfZeroDocuments()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    4),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 4),
            "c",
                ImmutableMap.of(
                    "author", "authorC", "title", "titleC", "pages", 100, "year", 1980, "rating",
                    3),
            "d",
                ImmutableMap.of(
                    "author", "authorD", "title", "titleD", "pages", 50, "year", 2020, "rating",
                    0)));
    AggregateQuerySnapshot snapshot =
        randomCol.whereGreaterThan("rating", 4).aggregate(AggregateField.sum("rating")).get().get();
    assertEquals(0.0, snapshot.get(AggregateField.sum("rating")), 0);
  }

  @Test
  public void performsSumOnlyOnNumericFields() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    5),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 4),
            "c",
                ImmutableMap.of(
                    "author", "authorC", "title", "titleC", "pages", 100, "year", 1980, "rating",
                    "3"),
            "d",
                ImmutableMap.of(
                    "author", "authorD", "title", "titleD", "pages", 50, "year", 2020, "rating",
                    1)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.sum("rating"), AggregateField.count()).get().get();
    assertEquals(10.0, snapshot.get(AggregateField.sum("rating")), 0);
    assertEquals(4, snapshot.get(AggregateField.count()).longValue());
  }

  @Test
  public void performsSumOfMinIEEE754() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
            ImmutableMap.of(
                "author",
                "authorA",
                "title",
                "titleA",
                "pages",
                100,
                "year",
                1980,
                "rating",
                Double.MIN_VALUE)));
    AggregateQuerySnapshot snapshot = randomCol.aggregate(AggregateField.sum("rating")).get().get();
    assertEquals(Double.MIN_VALUE, snapshot.get(AggregateField.sum("rating")), 0);
  }

  @Test
  public void performsAverageOfIntsThatResultsInAnInt()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    10),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 5),
            "c",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 150, "year", 2021, "rating",
                    0)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.average("rating")).get().get();
    assertEquals(5.0, snapshot.get(AggregateField.average("rating")), 0);
  }

  @Test
  public void performsAverageOfFloatsThatResultsInAnInt()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    10.5),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating",
                    9.5)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.average("rating")).get().get();
    assertEquals(10.0, snapshot.get(AggregateField.average("rating")), 0);
  }

  @Test
  public void performsAverageOfFloatsAndIntsThatResultsInAnInt()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    10),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating",
                    9.5),
            "c",
                ImmutableMap.of(
                    "author", "authorC", "title", "titleC", "pages", 150, "year", 2021, "rating",
                    10.5)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.average("rating")).get().get();
    assertEquals(10.0, snapshot.get(AggregateField.average("rating")), 0);
  }

  @Test
  public void performsAverageOfFloatThatResultsInFloat()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    5.5),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating",
                    4.5),
            "c",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 150, "year", 2021, "rating",
                    3.5)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.average("rating")).get().get();
    assertEquals(4.5, snapshot.get(AggregateField.average("rating")), 0);
  }

  @Test
  public void performsAverageOfFloatsAndIntsThatResultsInAFloat()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    8.6),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 9),
            "c",
                ImmutableMap.of(
                    "author", "authorC", "title", "titleC", "pages", 150, "year", 2021, "rating",
                    10)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.average("rating")).get().get();
    assertEquals(9.2, snapshot.get(AggregateField.average("rating")), 0.0000001);
  }

  @Test
  public void performsAverageOfIntsThatResultsInAFloat()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    10),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating",
                    9)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.average("rating")).get().get();
    assertEquals(9.5, snapshot.get(AggregateField.average("rating")), 0);
  }

  @Test
  public void performsAverageCausingUnderflow() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author",
                    "authorA",
                    "title",
                    "titleA",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    Double.MIN_VALUE),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating",
                    0)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.average("rating")).get().get();
    assertEquals(0.0, snapshot.get(AggregateField.average("rating")), 0);
  }

  @Test
  public void performsAverageOfMinIEEE754() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
            ImmutableMap.of(
                "author",
                "authorA",
                "title",
                "titleA",
                "pages",
                100,
                "year",
                1980,
                "rating",
                Double.MIN_VALUE)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.average("rating")).get().get();
    assertEquals(Double.MIN_VALUE, snapshot.get(AggregateField.average("rating")), 0);
  }

  @Test
  public void performsAverageThatOverflowsIEEE754DuringAccumulation()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author",
                    "authorA",
                    "title",
                    "titleA",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    Double.MAX_VALUE),
            "b",
                ImmutableMap.of(
                    "author",
                    "authorB",
                    "title",
                    "titleB",
                    "pages",
                    50,
                    "year",
                    2020,
                    "rating",
                    Double.MAX_VALUE)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.average("rating")).get().get();
    assertEquals(Double.POSITIVE_INFINITY, snapshot.get(AggregateField.average("rating")), 0);
  }

  @Test
  public void performsAverageThatIncludesNaN() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    5),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 4),
            "c",
                ImmutableMap.of(
                    "author",
                    "authorC",
                    "title",
                    "titleC",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    Double.NaN),
            "d",
                ImmutableMap.of(
                    "author", "authorD", "title", "titleD", "pages", 50, "year", 2020, "rating",
                    0)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.average("rating")).get().get();
    assertEquals(Double.NaN, snapshot.get(AggregateField.average("rating")), 0);
  }

  @Test
  public void performsAverageOverAResultSetOfZeroDocuments()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    4),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 4),
            "c",
                ImmutableMap.of(
                    "author", "authorC", "title", "titleC", "pages", 100, "year", 1980, "rating",
                    3),
            "d",
                ImmutableMap.of(
                    "author", "authorD", "title", "titleD", "pages", 50, "year", 2020, "rating",
                    0)));
    AggregateQuerySnapshot snapshot =
        randomCol
            .whereGreaterThan("rating", 4)
            .aggregate(AggregateField.average("rating"))
            .get()
            .get();
    assertNull(snapshot.get(AggregateField.average("rating")));
  }

  @Test
  public void performsAverageOnlyOnNumericFields() throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    5),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 4),
            "c",
                ImmutableMap.of(
                    "author", "authorC", "title", "titleC", "pages", 100, "year", 1980, "rating",
                    "3"),
            "d",
                ImmutableMap.of(
                    "author", "authorD", "title", "titleD", "pages", 50, "year", 2020, "rating",
                    6)));
    AggregateQuerySnapshot snapshot =
        randomCol.aggregate(AggregateField.average("rating"), AggregateField.count()).get().get();
    assertEquals(5.0, snapshot.get(AggregateField.average("rating")), 0);
    assertEquals(4, snapshot.get(AggregateField.count()).longValue());
  }

  @Test
  public void allowsAliasesWithLengthGreaterThan1500Bytes()
      throws ExecutionException, InterruptedException {
    // Alias string length is bytes of UTF-8 encoded alias + 1;
    String longAlias = new String(new char[1500]).replace("\0", "0123456789");
    String longerAlias = longAlias + longAlias;

    testCollectionWithDocs(
        ImmutableMap.of(
            "a", ImmutableMap.of("num", 3),
            "b", ImmutableMap.of("num", 5)));
    AggregateQuerySnapshot snapshot =
        randomCol
            .aggregate(
                AggregateField.count().withAlias(longAlias),
                AggregateField.count().withAlias(longerAlias))
            .get()
            .get();
    assertEquals(2, snapshot.get(longAlias));
    assertEquals(2, snapshot.get(longerAlias));
  }

  @Test
  public void performsAggregationsOnNestedMapValues()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author",
                    "authorA",
                    "title",
                    "titleA",
                    "metadata",
                    ImmutableMap.of(
                        "pages", 100, "rating", ImmutableMap.of("critic", 2, "user", 5))),
            "b",
                ImmutableMap.of(
                    "author",
                    "authorB",
                    "title",
                    "titleB",
                    "metadata",
                    ImmutableMap.of(
                        "pages", 50, "rating", ImmutableMap.of("critic", 4, "user", 4)))));
    AggregateQuerySnapshot snapshot =
        randomCol
            .aggregate(
                AggregateField.sum("metadata.pages").withAlias("totalPages"),
                AggregateField.average("metadata.pages").withAlias("averagePages"),
                AggregateField.count())
            .get()
            .get();
    assertEquals(150, snapshot.get("totalPages"));
    assertEquals(75.0, snapshot.get("averagePages"));
    assertEquals(2, snapshot.get(AggregateField.count()).longValue());
  }

  @Test
  public void performsAggregatesWhenUsingInOperator()
      throws ExecutionException, InterruptedException {
    testCollectionWithDocs(
        ImmutableMap.of(
            "a",
                ImmutableMap.of(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    5),
            "b",
                ImmutableMap.of(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 4),
            "c",
                ImmutableMap.of(
                    "author", "authorC", "title", "titleC", "pages", 100, "year", 1980, "rating",
                    3),
            "d",
                ImmutableMap.of(
                    "author", "authorD", "title", "titleD", "pages", 50, "year", 2020, "rating",
                    0)));
    AggregateQuerySnapshot snapshot =
        randomCol
            .whereIn("rating", Arrays.asList(5, 3))
            .aggregate(
                AggregateField.sum("rating").withAlias("totalRating"),
                AggregateField.average("rating").withAlias("averageRating"),
                AggregateField.count().withAlias("countOfDocs"))
            .get()
            .get();
    assertEquals(8.0, snapshot.get("totalRating"));
    assertEquals(4.0, snapshot.get("averageRating"));
    assertEquals(2, snapshot.get("countOfDocs"));
  }

  @Test
  public void transactionHasGetMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("foo", "bar")).get();
    String result =
        firestore
            .runTransaction(
                transaction -> {
                  DocumentSnapshot doc = transaction.get(ref).get();
                  return doc.getString("foo");
                })
            .get();
    assertEquals("bar", result);
  }

  @Test
  public void transactionHasGetAllMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref1 = randomCol.document("doc1");
    DocumentReference ref2 = randomCol.document("doc2");
    ref1.set(Collections.emptyMap()).get();
    ref2.set(Collections.emptyMap()).get();
    int result =
        firestore
            .runTransaction(
                transaction -> {
                  List<DocumentSnapshot> docs = transaction.getAll(ref1, ref2).get();
                  return docs.size();
                })
            .get();
    assertEquals(2, result);
  }

  @Test
  public void getAllSupportsArrayDestructuringInTransaction()
      throws ExecutionException, InterruptedException {
    DocumentReference ref1 = randomCol.document("doc1");
    DocumentReference ref2 = randomCol.document("doc2");
    ref1.set(Collections.emptyMap()).get();
    ref2.set(Collections.emptyMap()).get();
    int result =
        firestore
            .runTransaction(
                transaction -> {
                  List<DocumentSnapshot> docs =
                      transaction.getAll(new DocumentReference[] {ref1, ref2}).get();
                  return docs.size();
                })
            .get();
    assertEquals(2, result);
  }

  @Test
  public void getAllSupportsFieldMaskInTransaction()
      throws ExecutionException, InterruptedException {
    DocumentReference ref1 = randomCol.document("doc1");
    ref1.set(ImmutableMap.of("foo", "a", "bar", "b")).get();
    DocumentSnapshot result =
        firestore
            .runTransaction(
                transaction -> {
                  List<DocumentSnapshot> docs =
                      transaction.getAll(new DocumentReference[] {ref1}, "foo").get();
                  return docs.get(0);
                })
            .get();
    assertEquals(ImmutableMap.of("foo", "a"), result.getData());
  }

  @Test
  public void getAllSupportsArrayDestructuringWithFieldMaskInTransaction()
      throws ExecutionException, InterruptedException {
    DocumentReference ref1 = randomCol.document("doc1");
    DocumentReference ref2 = randomCol.document("doc2");
    ref1.set(ImmutableMap.of("f", "a", "b", "b")).get();
    ref2.set(ImmutableMap.of("f", "a", "b", "b")).get();
    List<DocumentSnapshot> result =
        firestore
            .runTransaction(
                transaction -> {
                  List<DocumentSnapshot> docs =
                      transaction.getAll(new DocumentReference[] {ref1, ref2}, "f").get();
                  return docs;
                })
            .get();
    assertEquals(ImmutableMap.of("f", "a"), result.get(0).getData());
    assertEquals(ImmutableMap.of("f", "a"), result.get(1).getData());
  }

  @Test
  public void getAllSupportsGenericsInTransaction()
      throws ExecutionException, InterruptedException {
    DocumentReference ref1 = randomCol.document("doc1").withConverter(new Post.PostConverter());
    DocumentReference ref2 = randomCol.document("doc2").withConverter(new Post.PostConverter());
    ref1.set(new Post("post1", "author1")).get();
    ref2.set(new Post("post2", "author2")).get();

    List<DocumentSnapshot> result =
        firestore
            .runTransaction(
                transaction -> {
                  List<DocumentSnapshot> docs = transaction.getAll(ref1, ref2).get();
                  return docs;
                })
            .get();
    assertEquals("post1, by author1", result.get(0).toObject(Post.class).toString());
    assertEquals("post2, by author2", result.get(1).toObject(Post.class).toString());
  }

  @Test
  public void setAndGetSupportWithConverterInTransaction()
      throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc1").withConverter(new Post.PostConverter());
    ref.set(new Post("post", "author")).get();
    firestore
        .runTransaction(
            transaction -> {
              transaction.get(ref).get();
              transaction.set(ref, new Post("new post", "author"));
              return null;
            })
        .get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals("new post, by author", doc.toObject(Post.class).toString());
  }

  @Test
  public void transactionHasGetWithQuery() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    Query query = randomCol.whereEqualTo("foo", "bar");
    ref.set(ImmutableMap.of("foo", "bar")).get();
    String result =
        firestore
            .runTransaction(
                transaction -> {
                  QuerySnapshot res = transaction.get(query).get();
                  return res.getDocuments().get(0).getString("foo");
                })
            .get();
    assertEquals("bar", result);
  }

  @Test
  public void transactionHasSetMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    firestore
        .runTransaction(
            transaction -> {
              transaction.set(ref, ImmutableMap.of("foo", "foobar"));
              return null;
            })
        .get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals("foobar", doc.getString("foo"));
  }

  @Test
  public void transactionHasUpdateMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("boo", Arrays.asList("ghost", "sebastian"), "moo", "chicken")).get();
    firestore
        .runTransaction(
            transaction -> {
              transaction.get(ref).get();
              transaction.update(
                  ref, ImmutableMap.of("boo", FieldValue.arrayRemove("sebastian"), "moo", "cow"));
              return null;
            })
        .get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals(ImmutableMap.of("boo", Arrays.asList("ghost"), "moo", "cow"), doc.getData());
  }

  @Test
  public void transactionHasDeleteMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("foo", "bar")).get();
    firestore
        .runTransaction(
            transaction -> {
              transaction.delete(ref);
              return null;
            })
        .get();
    DocumentSnapshot result = ref.get().get();
    assertFalse(result.exists());
  }

  @Test
  public void doesNotRetryTransactionThatFailWithFailedPrecondition() {
    DocumentReference ref = firestore.collection("col").document();
    AtomicInteger attempts = new AtomicInteger();
    assertThrows(
        ExecutionException.class,
        () -> {
          firestore
              .runTransaction(
                  transaction -> {
                    attempts.incrementAndGet();
                    transaction.update(ref, ImmutableMap.of("foo", "b"));
                    return null;
                  })
              .get();
        });
    assertEquals(1, attempts.get());
  }

  @Test
  public void retriesTransactionsThatFailWithContention()
      throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    AtomicInteger attempts = new AtomicInteger();
    final java.util.concurrent.CountDownLatch latch1 = new java.util.concurrent.CountDownLatch(1);
    final java.util.concurrent.CountDownLatch latch2 = new java.util.concurrent.CountDownLatch(1);

    com.google.api.core.ApiFuture<Void> firstTransaction =
        firestore.runTransaction(
            transaction -> {
              attempts.incrementAndGet();
              transaction.get(ref).get();
              latch1.countDown();
              latch2.await();
              transaction.set(ref, ImmutableMap.of("first", true), SetOptions.merge());
              return null;
            });

    com.google.api.core.ApiFuture<Void> secondTransaction =
        firestore.runTransaction(
            transaction -> {
              attempts.incrementAndGet();
              transaction.get(ref).get();
              latch2.countDown();
              latch1.await();
              transaction.set(ref, ImmutableMap.of("second", true), SetOptions.merge());
              return null;
            });

    firstTransaction.get();
    secondTransaction.get();

    assertEquals(3, attempts.get());

    DocumentSnapshot finalSnapshot = ref.get().get();
    assertEquals(ImmutableMap.of("first", true, "second", true), finalSnapshot.getData());
  }

  @Test
  public void supportsReadOnlyTransactions() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    ref.set(ImmutableMap.of("foo", "bar")).get();
    DocumentSnapshot snapshot =
        firestore
            .runTransaction(
                transaction -> {
                  return transaction.get(ref).get();
                },
                TransactionOptions.createReadOnly())
            .get();
    assertTrue(snapshot.exists());
  }

  @Test
  public void supportsReadOnlyTransactionsWithCustomReadTime()
      throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    WriteResult writeResult = ref.set(ImmutableMap.of("foo", 1)).get();
    ref.set(ImmutableMap.of("foo", 2)).get();
    DocumentSnapshot snapshot =
        firestore
            .runTransaction(
                transaction -> {
                  return transaction.get(ref).get();
                },
                TransactionOptions.createReadOnly(writeResult.getUpdateTime()))
            .get();
    assertTrue(snapshot.exists());
    assertEquals(1L, snapshot.getLong("foo").longValue());
  }

  @Test
  public void writeBatchSupportsEmptyBatches() throws ExecutionException, InterruptedException {
    firestore.batch().commit().get();
  }

  @Test
  public void writeBatchHasCreateMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document();
    WriteBatch batch = firestore.batch();
    batch.create(ref, ImmutableMap.of("foo", "a"));
    batch.commit().get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals("a", doc.getString("foo"));
  }

  @Test
  public void writeBatchHasSetMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    WriteBatch batch = firestore.batch();
    batch.set(ref, ImmutableMap.of("foo", "a"));
    batch.commit().get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals("a", doc.getString("foo"));
  }

  @Test
  public void setSupportsPartialsInWriteBatch() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc").withConverter(new Post.PostConverterMerge());
    ref.set(new Post("walnut", "author")).get();
    WriteBatch batch = firestore.batch();
    batch.set(ref, new Post("olive", null), SetOptions.merge());
    batch.commit().get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals("olive", doc.getString("title"));
    assertEquals("author", doc.getString("author"));
  }

  @Test
  public void writeBatchHasUpdateMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    WriteBatch batch = firestore.batch();
    batch.set(ref, ImmutableMap.of("foo", "a"));
    batch.update(ref, ImmutableMap.of("foo", "b"));
    batch.commit().get();
    DocumentSnapshot doc = ref.get().get();
    assertEquals("b", doc.getString("foo"));
  }

  @Test
  public void omitsDocumentTransformsFromWriteResults()
      throws ExecutionException, InterruptedException {
    WriteBatch batch = firestore.batch();
    batch.set(randomCol.document(), ImmutableMap.of("foo", "a"));
    batch.set(randomCol.document(), ImmutableMap.of("foo", FieldValue.serverTimestamp()));
    List<WriteResult> writeResults = batch.commit().get();
    assertEquals(2, writeResults.size());
  }

  @Test
  public void enforcesThatUpdatedDocumentExistsInWriteBatch() {
    DocumentReference ref = randomCol.document();
    com.google.cloud.firestore.WriteBatch batch = firestore.batch();
    batch.update(ref, ImmutableMap.of("foo", "b"));
    try {
      batch.commit().get();
      fail("should have thrown");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof AbortedException);
    }
  }

  @Test
  public void writeBatchHasDeleteMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc");
    WriteBatch batch = firestore.batch();
    batch.set(ref, ImmutableMap.of("foo", "a"));
    batch.delete(ref);
    batch.commit().get();
    DocumentSnapshot result = ref.get().get();
    assertFalse(result.exists());
  }

  @Test
  public void querySnapshotHasQueryProperty() throws ExecutionException, InterruptedException {
    QuerySnapshot snapshot = randomCol.get().get();
    snapshot.getQuery().get().get();
  }

  @Test
  public void querySnapshotHasEmptyProperty() throws ExecutionException, InterruptedException {
    QuerySnapshot snapshot = randomCol.get().get();
    assertTrue(snapshot.isEmpty());
    randomCol.document().set(ImmutableMap.of("foo", "bar")).get();
    snapshot = randomCol.get().get();
    assertFalse(snapshot.isEmpty());
  }

  @Test
  public void querySnapshotHasSizeProperty() throws ExecutionException, InterruptedException {
    QuerySnapshot snapshot = randomCol.get().get();
    assertEquals(0, snapshot.size());
    randomCol.document().set(ImmutableMap.of("foo", "bar")).get();
    snapshot = randomCol.get().get();
    assertEquals(1, snapshot.size());
  }

  @Test
  public void querySnapshotHasDocsProperty() throws ExecutionException, InterruptedException {
    randomCol.document("doc1").set(ImmutableMap.of("foo", "a")).get();
    randomCol.document("doc2").set(ImmutableMap.of("foo", "a")).get();
    QuerySnapshot snapshot = randomCol.get().get();
    assertEquals(2, snapshot.getDocuments().size());
    assertEquals("a", snapshot.getDocuments().get(0).getString("foo"));
  }

  @Test
  public void querySnapshotHasForEachMethod() throws ExecutionException, InterruptedException {
    randomCol.document("doc1").set(ImmutableMap.of("foo", "a")).get();
    randomCol.document("doc2").set(ImmutableMap.of("foo", "a")).get();
    QuerySnapshot snapshot = randomCol.get().get();
    AtomicInteger count = new AtomicInteger();
    snapshot.forEach(
        doc -> {
          assertEquals("a", doc.getString("foo"));
          count.incrementAndGet();
        });
    assertEquals(2, count.get());
  }

  @Test
  public void bulkWriterHasCreateMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc1");
    com.google.cloud.firestore.BulkWriter writer = firestore.bulkWriter();
    writer.create(ref, ImmutableMap.of("foo", "bar"));
    writer.close();
    DocumentSnapshot result = ref.get().get();
    assertEquals(ImmutableMap.of("foo", "bar"), result.getData());
  }

  @Test
  public void bulkWriterHasSetMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc1");
    com.google.cloud.firestore.BulkWriter writer = firestore.bulkWriter();
    writer.set(ref, ImmutableMap.of("foo", "bar"));
    writer.close();
    DocumentSnapshot result = ref.get().get();
    assertEquals(ImmutableMap.of("foo", "bar"), result.getData());
  }

  @Test
  public void bulkWriterHasUpdateMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc1");
    ref.set(ImmutableMap.of("foo", "bar")).get();
    com.google.cloud.firestore.BulkWriter writer = firestore.bulkWriter();
    writer.update(ref, ImmutableMap.of("foo", "bar2"));
    writer.close();
    DocumentSnapshot result = ref.get().get();
    assertEquals(ImmutableMap.of("foo", "bar2"), result.getData());
  }

  @Test
  public void bulkWriterHasDeleteMethod() throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc1");
    ref.set(ImmutableMap.of("foo", "bar")).get();
    com.google.cloud.firestore.BulkWriter writer = firestore.bulkWriter();
    writer.delete(ref);
    writer.close();
    DocumentSnapshot result = ref.get().get();
    assertFalse(result.exists());
  }

  @Test
  public void canWriteToTheSameDocumentTwiceInBulkWriter()
      throws ExecutionException, InterruptedException {
    DocumentReference ref = randomCol.document("doc1");
    com.google.cloud.firestore.BulkWriter writer = firestore.bulkWriter();
    writer.set(ref, ImmutableMap.of("foo", "bar"));
    writer.set(ref, ImmutableMap.of("foo", "bar2"));
    writer.close();
    DocumentSnapshot result = ref.get().get();
    assertNotNull(result.getString("foo"));
  }

  @Test
  public void canTerminateOnceBulkWriterIsClosed() throws Exception {
    DocumentReference ref = randomCol.document("doc1");
    com.google.cloud.firestore.BulkWriter writer = firestore.bulkWriter();
    writer.set(ref, ImmutableMap.of("foo", "bar"));
    writer.close();
    firestore.terminate();
  }

  @Test
  public void recursiveDeleteOnTopLevelCollection()
      throws ExecutionException, InterruptedException {
    WriteBatch batch = firestore.batch();
    batch.set(randomCol.document("anna"), ImmutableMap.of("name", "anna"));
    batch.set(randomCol.document("bob"), ImmutableMap.of("name", "bob"));
    batch.set(randomCol.document("bob/parentsCol/charlie"), ImmutableMap.of("name", "charlie"));
    batch.set(randomCol.document("bob/parentsCol/daniel"), ImmutableMap.of("name", "daniel"));
    batch.set(
        randomCol.document("bob/parentsCol/daniel/childCol/ernie"),
        ImmutableMap.of("name", "ernie"));
    batch.set(
        randomCol.document("bob/parentsCol/daniel/childCol/francis"),
        ImmutableMap.of("name", "francis"));
    batch.commit().get();

    firestore.recursiveDelete(randomCol).get();
    assertEquals(0, countDocumentChildren(randomCol.document()));
  }

  @Test
  public void canRetryFailedWritesWithAProvidedCallback() {
    AtomicInteger retryCount = new AtomicInteger();
    AtomicReference<com.google.cloud.firestore.BulkWriterException> code = new AtomicReference<>();
    com.google.cloud.firestore.BulkWriter writer = firestore.bulkWriter();
    writer.addWriteErrorListener(
        error -> {
          retryCount.set(error.getFailedAttempts());
          return error.getFailedAttempts() < 3;
        });

    DocumentReference ref = randomCol.document("__doc__");
    writer
        .create(ref, ImmutableMap.of("foo", "bar"))
        .addCallback(
            new com.google.api.core.ApiFutureCallback<WriteResult>() {
              @Override
              public void onFailure(Throwable t) {
                code.set((com.google.cloud.firestore.BulkWriterException) t);
              }

              @Override
              public void onSuccess(WriteResult result) {}
            });
    writer.close();
    assertEquals(3, retryCount.get());
    assertEquals(
        com.google.api.gax.rpc.StatusCode.Code.INVALID_ARGUMENT, code.get().getStatus().getCode());
  }

  @Test
  public void clientInitializationSucceedsForCollectionReferenceGet()
      throws ExecutionException, InterruptedException {
    randomCol.get().get();
  }

  @Test
  public void clientInitializationSucceedsForCollectionReferenceAdd()
      throws ExecutionException, InterruptedException {
    randomCol.add(ImmutableMap.of("foo", "bar")).get();
  }

  @Test
  public void clientInitializationSucceedsForCollectionReferenceStream()
      throws InterruptedException {
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    randomCol.stream((doc, error) -> {}, latch::countDown);
    latch.await();
  }

  @Test
  public void clientInitializationSucceedsForCollectionReferenceListDocuments()
      throws ExecutionException, InterruptedException {
    randomCol.listDocuments().get();
  }

  @Test
  public void clientInitializationSucceedsForCollectionReferenceOnSnapshot()
      throws InterruptedException {
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    randomCol.addSnapshotListener(
        (snapshot, error) -> {
          latch.countDown();
        });
    latch.await();
  }

  @Test
  public void clientInitializationSucceedsForDocumentReferenceGet()
      throws ExecutionException, InterruptedException {
    randomCol.document().get().get();
  }

  @Test
  public void clientInitializationSucceedsForDocumentReferenceCreate()
      throws ExecutionException, InterruptedException {
    randomCol.document().create(ImmutableMap.of("foo", "bar")).get();
  }

  @Test
  public void clientInitializationSucceedsForDocumentReferenceSet()
      throws ExecutionException, InterruptedException {
    randomCol.document().set(ImmutableMap.of("foo", "bar")).get();
  }

  @Test
  public void clientInitializationSucceedsForDocumentReferenceUpdate() {
    try {
      randomCol.document().update("foo", "bar").get();
      fail("should have thrown");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof AbortedException);
    }
  }

  @Test
  public void clientInitializationSucceedsForDocumentReferenceDelete()
      throws ExecutionException, InterruptedException {
    randomCol.document().delete().get();
  }

  @Test
  public void clientInitializationSucceedsForDocumentReferenceListCollections()
      throws ExecutionException, InterruptedException {
    randomCol.document().listCollections().get();
  }

  @Test
  public void clientInitializationSucceedsForDocumentReferenceOnSnapshot()
      throws InterruptedException {
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    randomCol
        .document()
        .addSnapshotListener(
            (snapshot, error) -> {
              latch.countDown();
            });
    latch.await();
  }

  // @Test
  // public void clientInitializationSucceedsForCollectionGroupGetPartitions()
  //     throws ExecutionException, InterruptedException {
  //   firestore.collectionGroup("id").getPartitions(2).get();
  // }

  @Test
  public void clientInitializationSucceedsForFirestoreRunTransaction()
      throws ExecutionException, InterruptedException {
    firestore
        .runTransaction(
            transaction -> {
              return transaction.get(randomCol).get();
            })
        .get();
  }

  @Test
  public void clientInitializationSucceedsForFirestoreGetAll()
      throws ExecutionException, InterruptedException {
    firestore.getAll(randomCol.document()).get();
  }

  @Test
  public void clientInitializationSucceedsForFirestoreBatch()
      throws ExecutionException, InterruptedException {
    firestore.batch().commit().get();
  }

  @Test
  public void clientInitializationSucceedsForFirestoreTerminate() throws Exception {
    firestore.close();
  }

  @Test
  public void bundleBuildingSucceedsWhenThereAreNoResults()
      throws ExecutionException, InterruptedException {
    com.google.cloud.firestore.bundle.FirestoreBundle bundle = firestore.bundle("test-bundle");
    Query query = randomCol.whereEqualTo("value", "42");
    QuerySnapshot snap = query.get().get();

    bundle.add("query", snap);
    // `elements` is expected to be [bundleMeta, query].
    java.nio.ByteBuffer elements = bundle.build();
    assertNotNull(elements);
  }

  @Test
  public void bundleBuildingSucceedsWhenAddedDocumentDoesNotExist()
      throws ExecutionException, InterruptedException {
    com.google.cloud.firestore.bundle.FirestoreBundle bundle = firestore.bundle("test-bundle");
    DocumentSnapshot snap = randomCol.document("doc5-not-exist").get().get();

    bundle.add(snap);
    // `elements` is expected to be [bundleMeta, docMeta].
    java.nio.ByteBuffer elements = bundle.build();
    assertNotNull(elements);
  }

  @Test
  public void bundleBuildingSucceedsToSaveLimitAndLimitToLastQueries()
      throws ExecutionException, InterruptedException {
    com.google.cloud.firestore.bundle.FirestoreBundle bundle = firestore.bundle("test-bundle");
    Query limitQuery = randomCol.orderBy("sort", Query.Direction.DESCENDING).limit(1);
    QuerySnapshot limitSnap = limitQuery.get().get();
    Query limitToLastQuery = randomCol.orderBy("sort", Query.Direction.ASCENDING).limitToLast(1);
    QuerySnapshot limitToLastSnap = limitToLastQuery.get().get();

    bundle.add("limitQuery", limitSnap);
    bundle.add("limitToLastQuery", limitToLastSnap);
    // `elements` is expected to be [bundleMeta, limitQuery, limitToLastQuery, doc4Meta, doc4Snap].
    java.nio.ByteBuffer elements = bundle.build();
    assertNotNull(elements);
  }

  @Test
  public void typesTestNestedPartialSupport() throws ExecutionException, InterruptedException {
    DocumentReference doc = randomCol.document();
    doc.set(
            ImmutableMap.of(
                "outerString",
                "foo",
                "outerArr",
                Collections.emptyList(),
                "nested",
                ImmutableMap.of(
                    "innerNested",
                    ImmutableMap.of("innerNestedNum", 2),
                    "innerArr",
                    FieldValue.arrayUnion(2),
                    "timestamp",
                    FieldValue.serverTimestamp())))
        .get();
    DocumentReference ref = doc.withConverter(new TestObject.TestObjectConverterMerge());
    ref.set(
            new TestObject(
                null,
                null,
                new TestObject.Nested(
                    new TestObject.InnerNested(FieldValue.increment(1)),
                    FieldValue.arrayUnion(2),
                    FieldValue.serverTimestamp())),
            SetOptions.merge())
        .get();
    ref.set(
            new TestObject(null, null, new TestObject.Nested(FieldValue.delete(), null, null)),
            SetOptions.merge())
        .get();
  }

  @Test
  public void typesTestWithFieldValue() throws ExecutionException, InterruptedException {
    DocumentReference doc = randomCol.document();
    doc.set(
            ImmutableMap.of(
                "outerString",
                "foo",
                "outerArr",
                Collections.emptyList(),
                "nested",
                ImmutableMap.of(
                    "innerNested",
                    ImmutableMap.of("innerNestedNum", 2),
                    "innerArr",
                    FieldValue.arrayUnion(2),
                    "timestamp",
                    FieldValue.serverTimestamp())))
        .get();
    DocumentReference ref = doc.withConverter(new TestObject.TestObjectConverterMerge());
    ref.set(
            new TestObject(
                "foo",
                Collections.emptyList(),
                new TestObject.Nested(
                    new TestObject.InnerNested(FieldValue.increment(1)),
                    FieldValue.arrayUnion(2),
                    FieldValue.serverTimestamp())))
        .get();
  }

  @Test
  public void typesTestUpdateData() throws ExecutionException, InterruptedException {
    DocumentReference doc = randomCol.document();
    doc.set(
            ImmutableMap.of(
                "outerString",
                "foo",
                "outerArr",
                Collections.emptyList(),
                "nested",
                ImmutableMap.of(
                    "innerNested",
                    ImmutableMap.of("innerNestedNum", 2),
                    "innerArr",
                    FieldValue.arrayUnion(2),
                    "timestamp",
                    FieldValue.serverTimestamp())))
        .get();
    DocumentReference ref = doc.withConverter(new TestObject.TestObjectConverterMerge());
    ref.update(
            "nested.innerNested.innerNestedNum",
            FieldValue.increment(2),
            "nested.innerArr",
            FieldValue.arrayUnion(3))
        .get();
  }

  private int countDocumentChildren(DocumentReference ref)
      throws ExecutionException, InterruptedException {
    int count = 0;
    for (CollectionReference collection : ref.listCollections().get()) {
      count += countCollectionChildren(collection);
    }
    return count;
  }

  private int countCollectionChildren(CollectionReference ref)
      throws ExecutionException, InterruptedException {
    int count = 0;
    for (DocumentReference doc : ref.listDocuments().get()) {
      count += countDocumentChildren(doc) + 1;
    }
    return count;
  }

  private void testCollectionWithDocs(Map<String, Object>... docs)
      throws ExecutionException, InterruptedException {
    com.google.cloud.firestore.WriteBatch batch = firestore.batch();
    for (Map<String, Object> doc : docs) {
      batch.set(randomCol.document(), doc);
    }
    batch.commit().get();
  }
}
