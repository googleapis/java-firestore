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

package com.google.cloud.firestore.it;

import static com.google.cloud.firestore.it.TestHelper.isRunningAgainstFirestoreEmulator;
import static com.google.common.primitives.Ints.asList;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assume.assumeTrue;

import com.google.api.client.util.Preconditions;
import com.google.cloud.firestore.AggregateQuery;
import com.google.cloud.firestore.AggregateQueryProfileInfo;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.LocalFirestoreHelper;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QueryProfileInfo;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ITQueryTest {

  private static Firestore firestore;

  @Rule public TestName testName = new TestName();

  @Before
  public void setUpFirestore() {
    firestore = FirestoreOptions.newBuilder().build().getService();
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

  private CollectionReference createEmptyCollection() {
    String collectionPath =
        "java-" + testName.getMethodName() + "-" + LocalFirestoreHelper.autoId();
    return firestore.collection(collectionPath);
  }

  public static <T> Map<String, T> map(Object... entries) {
    Map<String, T> res = new LinkedHashMap<>();
    for (int i = 0; i < entries.length; i += 2) {
      res.put((String) entries[i], (T) entries[i + 1]);
    }
    return res;
  }

  public static CollectionReference testCollectionWithDocs(Map<String, Map<String, Object>> docs)
      throws ExecutionException, InterruptedException, TimeoutException {
    CollectionReference collection = firestore.collection(LocalFirestoreHelper.autoId());
    for (Map.Entry<String, Map<String, Object>> doc : docs.entrySet()) {
      collection.document(doc.getKey()).set(doc.getValue()).get(5, TimeUnit.SECONDS);
    }
    return collection;
  }

  public static void checkQuerySnapshotContainsDocuments(Query query, String... docs)
      throws ExecutionException, InterruptedException {
    QuerySnapshot snapshot = query.get().get();
    List<String> result =
        snapshot.getDocuments().stream()
            .map(queryDocumentSnapshot -> queryDocumentSnapshot.getReference().getId())
            .collect(Collectors.toList());
    assertThat(result).isEqualTo(Arrays.asList(docs));
  }

  @Test
  public void orQueries() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", 0),
            "doc2", map("a", 2, "b", 1),
            "doc3", map("a", 3, "b", 2),
            "doc4", map("a", 1, "b", 3),
            "doc5", map("a", 1, "b", 1));

    CollectionReference collection = testCollectionWithDocs(testDocs);

    // Two equalities: a==1 || b==1.
    checkQuerySnapshotContainsDocuments(
        collection.where(Filter.or(Filter.equalTo("a", 1), Filter.equalTo("b", 1))),
        "doc1",
        "doc2",
        "doc4",
        "doc5");

    // (a==1 && b==0) || (a==3 && b==2)
    checkQuerySnapshotContainsDocuments(
        collection.where(
            Filter.or(
                Filter.and(Filter.equalTo("a", 1), Filter.equalTo("b", 0)),
                Filter.and(Filter.equalTo("a", 3), Filter.equalTo("b", 2)))),
        "doc1",
        "doc3");

    // a==1 && (b==0 || b==3).
    checkQuerySnapshotContainsDocuments(
        collection.where(
            Filter.and(
                Filter.equalTo("a", 1), Filter.or(Filter.equalTo("b", 0), Filter.equalTo("b", 3)))),
        "doc1",
        "doc4");

    // (a==2 || b==2) && (a==3 || b==3)
    checkQuerySnapshotContainsDocuments(
        collection.where(
            Filter.and(
                Filter.or(Filter.equalTo("a", 2), Filter.equalTo("b", 2)),
                Filter.or(Filter.equalTo("a", 3), Filter.equalTo("b", 3)))),
        "doc3");

    // Test with limits without orderBy (the __name__ ordering is the tiebreaker).
    checkQuerySnapshotContainsDocuments(
        collection.where(Filter.or(Filter.equalTo("a", 2), Filter.equalTo("b", 1))).limit(1),
        "doc2");
  }

  @Test
  public void orQueriesWithCompositeIndexes() throws Exception {
    assumeTrue(
        "Skip this test when running against production because these queries require a composite index.",
        isRunningAgainstFirestoreEmulator(firestore));
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", 0),
            "doc2", map("a", 2, "b", 1),
            "doc3", map("a", 3, "b", 2),
            "doc4", map("a", 1, "b", 3),
            "doc5", map("a", 1, "b", 1));

    CollectionReference collection = testCollectionWithDocs(testDocs);

    // with one inequality: a>2 || b==1.
    checkQuerySnapshotContainsDocuments(
        collection.where(Filter.or(Filter.greaterThan("a", 2), Filter.equalTo("b", 1))),
        "doc5",
        "doc2",
        "doc3");

    // Test with limits (implicit order by ASC): (a==1) || (b > 0) LIMIT 2
    checkQuerySnapshotContainsDocuments(
        collection.where(Filter.or(Filter.equalTo("a", 1), Filter.greaterThan("b", 0))).limit(2),
        "doc1",
        "doc2");

    // Test with limits (explicit order by): (a==1) || (b > 0) LIMIT_TO_LAST 2
    // Note: The public query API does not allow implicit ordering when limitToLast is used.
    checkQuerySnapshotContainsDocuments(
        collection
            .where(Filter.or(Filter.equalTo("a", 1), Filter.greaterThan("b", 0)))
            .limitToLast(2)
            .orderBy("b"),
        "doc3",
        "doc4");

    // Test with limits (explicit order by ASC): (a==2) || (b == 1) ORDER BY a LIMIT 1
    checkQuerySnapshotContainsDocuments(
        collection
            .where(Filter.or(Filter.equalTo("a", 2), Filter.equalTo("b", 1)))
            .limit(1)
            .orderBy("a"),
        "doc5");

    // Test with limits (explicit order by DESC): (a==2) || (b == 1) ORDER BY a LIMIT_TO_LAST 1
    checkQuerySnapshotContainsDocuments(
        collection
            .where(Filter.or(Filter.equalTo("a", 2), Filter.equalTo("b", 1)))
            .limitToLast(1)
            .orderBy("a"),
        "doc2");

    // Test with limits (explicit order by DESC): (a==2) || (b == 1) ORDER BY a DESC LIMIT 1
    checkQuerySnapshotContainsDocuments(
        collection
            .where(Filter.or(Filter.equalTo("a", 2), Filter.equalTo("b", 1)))
            .limit(1)
            .orderBy("a", Direction.DESCENDING),
        "doc2");
  }

  @Test
  public void orQueryDoesNotIncludeDocumentsWithMissingFields() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", 0),
            "doc2", map("b", 1),
            "doc3", map("a", 3, "b", 2),
            "doc4", map("a", 1, "b", 3),
            "doc5", map("a", 1),
            "doc6", map("a", 2));

    CollectionReference collection = testCollectionWithDocs(testDocs);

    // Query: a==1 || b==1
    // There's no explicit nor implicit orderBy. Documents with missing 'a' or missing 'b' should be
    // allowed if the document matches at least one disjunction term.
    Query query = collection.where(Filter.or(Filter.equalTo("a", 1), Filter.equalTo("b", 1)));
    checkQuerySnapshotContainsDocuments(query, "doc1", "doc2", "doc4", "doc5");
  }

  @Test
  public void orQueryDoesNotIncludeDocumentsWithMissingFields2() throws Exception {
    assumeTrue(
        "Skip this test when running against production because these queries require a composite index.",
        isRunningAgainstFirestoreEmulator(firestore));
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", 0),
            "doc2", map("b", 1),
            "doc3", map("a", 3, "b", 2),
            "doc4", map("a", 1, "b", 3),
            "doc5", map("a", 1),
            "doc6", map("a", 2));

    CollectionReference collection = testCollectionWithDocs(testDocs);

    // Query: a==1 || b==1 order by a.
    // doc2 should not be included because it's missing the field 'a', and we have "orderBy a".
    Query query1 =
        collection.where(Filter.or(Filter.equalTo("a", 1), Filter.equalTo("b", 1))).orderBy("a");
    checkQuerySnapshotContainsDocuments(query1, "doc1", "doc4", "doc5");

    // Query: a==1 || b==1 order by b.
    // doc5 should not be included because it's missing the field 'b', and we have "orderBy b".
    Query query2 =
        collection.where(Filter.or(Filter.equalTo("a", 1), Filter.equalTo("b", 1))).orderBy("b");
    checkQuerySnapshotContainsDocuments(query2, "doc1", "doc2", "doc4");

    // Query: a>2 || b==1.
    // This query has an implicit 'order by a'.
    // doc2 should not be included because it's missing the field 'a'.
    Query query3 = collection.where(Filter.or(Filter.greaterThan("a", 2), Filter.equalTo("b", 1)));
    checkQuerySnapshotContainsDocuments(query3, "doc3");

    // Query: a>1 || b==1 order by a order by b.
    // doc6 should not be included because it's missing the field 'b'.
    // doc2 should not be included because it's missing the field 'a'.
    Query query4 =
        collection
            .where(Filter.or(Filter.greaterThan("a", 1), Filter.equalTo("b", 1)))
            .orderBy("a")
            .orderBy("b");
    checkQuerySnapshotContainsDocuments(query4, "doc3");
  }

  @Test
  public void orQueriesWithIn() throws ExecutionException, InterruptedException, TimeoutException {
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", 0),
            "doc2", map("b", 1),
            "doc3", map("a", 3, "b", 2),
            "doc4", map("a", 1, "b", 3),
            "doc5", map("a", 1),
            "doc6", map("a", 2));
    CollectionReference collection = testCollectionWithDocs(testDocs);

    // a==2 || b in [2,3]
    checkQuerySnapshotContainsDocuments(
        collection.where(Filter.or(Filter.equalTo("a", 2), Filter.inArray("b", asList(2, 3)))),
        "doc3",
        "doc4",
        "doc6");
  }

  @Test
  public void orQueriesWithNotIn()
      throws ExecutionException, InterruptedException, TimeoutException {
    assumeTrue(
        "Skip this test when running against production because it is currently not supported.",
        isRunningAgainstFirestoreEmulator(firestore));
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", 0),
            "doc2", map("b", 1),
            "doc3", map("a", 3, "b", 2),
            "doc4", map("a", 1, "b", 3),
            "doc5", map("a", 1),
            "doc6", map("a", 2));
    CollectionReference collection = testCollectionWithDocs(testDocs);

    // a==2 || b not-in [2,3]
    // Has implicit orderBy b.
    checkQuerySnapshotContainsDocuments(
        collection.where(Filter.or(Filter.equalTo("a", 2), Filter.notInArray("b", asList(2, 3)))),
        "doc1",
        "doc2");
  }

  @Test
  public void orQueriesWithArrayMembership()
      throws ExecutionException, InterruptedException, TimeoutException {
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", asList(0)),
            "doc2", map("b", asList(1)),
            "doc3", map("a", 3, "b", asList(2, 7)),
            "doc4", map("a", 1, "b", asList(3, 7)),
            "doc5", map("a", 1),
            "doc6", map("a", 2));
    CollectionReference collection = testCollectionWithDocs(testDocs);

    // a==2 || b array-contains 7
    checkQuerySnapshotContainsDocuments(
        collection.where(Filter.or(Filter.equalTo("a", 2), Filter.arrayContains("b", 7))),
        "doc3",
        "doc4",
        "doc6");

    // a==2 || b array-contains-any [0, 3]
    checkQuerySnapshotContainsDocuments(
        collection.where(
            Filter.or(Filter.equalTo("a", 2), Filter.arrayContainsAny("b", asList(0, 3)))),
        "doc1",
        "doc4",
        "doc6");
  }

  @Test
  public void testUsingInWithArrayContains()
      throws ExecutionException, InterruptedException, TimeoutException {
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", asList(0)),
            "doc2", map("b", asList(1)),
            "doc3", map("a", 3, "b", asList(2, 7)),
            "doc4", map("a", 1, "b", asList(3, 7)),
            "doc5", map("a", 1),
            "doc6", map("a", 2));
    CollectionReference collection = testCollectionWithDocs(testDocs);

    Query query1 =
        collection.where(
            Filter.or(Filter.inArray("a", asList(2, 3)), Filter.arrayContains("b", 3)));
    checkQuerySnapshotContainsDocuments(query1, "doc3", "doc4", "doc6");

    Query query2 =
        collection.where(
            Filter.and(Filter.inArray("a", asList(2, 3)), Filter.arrayContains("b", 7)));
    checkQuerySnapshotContainsDocuments(query2, "doc3");

    Query query3 =
        collection.where(
            Filter.or(
                Filter.inArray("a", asList(2, 3)),
                Filter.and(Filter.arrayContains("b", 3), Filter.equalTo("a", 1))));
    checkQuerySnapshotContainsDocuments(query3, "doc3", "doc4", "doc6");

    Query query4 =
        collection.where(
            Filter.and(
                Filter.inArray("a", asList(2, 3)),
                Filter.or(Filter.arrayContains("b", 7), Filter.equalTo("a", 1))));
    checkQuerySnapshotContainsDocuments(query4, "doc3");
  }

  @Test
  public void testOrderByEquality()
      throws ExecutionException, InterruptedException, TimeoutException {
    assumeTrue(
        "Skip this test if running against production because order-by-equality is "
            + "not supported yet.",
        isRunningAgainstFirestoreEmulator(firestore));
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", asList(0)),
            "doc2", map("b", asList(1)),
            "doc3", map("a", 3, "b", asList(2, 7), "c", 10),
            "doc4", map("a", 1, "b", asList(3, 7)),
            "doc5", map("a", 1),
            "doc6", map("a", 2, "c", 20));
    CollectionReference collection = testCollectionWithDocs(testDocs);

    Query query1 = collection.where(Filter.equalTo("a", 1)).orderBy("a");
    checkQuerySnapshotContainsDocuments(query1, "doc1", "doc4", "doc5");

    Query query2 = collection.where(Filter.inArray("a", asList(2, 3))).orderBy("a");
    checkQuerySnapshotContainsDocuments(query2, "doc6", "doc3");
  }

  @Test
  public void testQueryPlan() throws ExecutionException, InterruptedException, TimeoutException {
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", asList(0)),
            "doc2", map("b", asList(1)),
            "doc3", map("a", 3, "b", asList(2, 7), "c", 10),
            "doc4", map("a", 1, "b", asList(3, 7)),
            "doc5", map("a", 1),
            "doc6", map("a", 2, "c", 20));
    CollectionReference collection = testCollectionWithDocs(testDocs);

    Query query = collection.where(Filter.equalTo("a", 1)).orderBy("a");
    Map<String, Object> plan = query.plan().get();
    System.out.println(plan);
  }

  @Test
  public void testQueryProfile() throws ExecutionException, InterruptedException, TimeoutException {
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", asList(0)),
            "doc2", map("b", asList(1)),
            "doc3", map("a", 3, "b", asList(2, 7), "c", 10),
            "doc4", map("a", 1, "b", asList(3, 7)),
            "doc5", map("a", 1),
            "doc6", map("a", 2, "c", 20));
    CollectionReference collection = testCollectionWithDocs(testDocs);

    Query query = collection.where(Filter.equalTo("a", 1)).orderBy("a");

    QueryProfileInfo profile = query.profile().get();
    System.out.println(profile.plan);
    System.out.println(profile.stats);
  }

  @Test
  public void testAggregateQueryPlan()
      throws ExecutionException, InterruptedException, TimeoutException {
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", asList(0)),
            "doc2", map("b", asList(1)),
            "doc3", map("a", 3, "b", asList(2, 7), "c", 10),
            "doc4", map("a", 1, "b", asList(3, 7)),
            "doc5", map("a", 1),
            "doc6", map("a", 2, "c", 20));
    CollectionReference collection = testCollectionWithDocs(testDocs);

    AggregateQuery query = collection.where(Filter.equalTo("a", 1)).orderBy("a").count();
    Map<String, Object> plan = query.plan().get();
    System.out.println(plan);
  }

  @Test
  public void testAggregateQueryProfile()
      throws ExecutionException, InterruptedException, TimeoutException {
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", asList(0)),
            "doc2", map("b", asList(1)),
            "doc3", map("a", 3, "b", asList(2, 7), "c", 10),
            "doc4", map("a", 1, "b", asList(3, 7)),
            "doc5", map("a", 1),
            "doc6", map("a", 2, "c", 20));
    CollectionReference collection = testCollectionWithDocs(testDocs);

    AggregateQuery query = collection.where(Filter.equalTo("a", 1)).orderBy("a").count();

    AggregateQueryProfileInfo profile = query.profile().get();
    System.out.println(profile.plan);
    System.out.println(profile.stats);
  }
}
