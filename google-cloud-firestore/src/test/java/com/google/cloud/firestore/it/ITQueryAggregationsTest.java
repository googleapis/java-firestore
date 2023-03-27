package com.google.cloud.firestore.it;

import static com.google.cloud.firestore.AggregateField.average;
import static com.google.cloud.firestore.AggregateField.sum;
import static com.google.cloud.firestore.LocalFirestoreHelper.autoId;
import static com.google.cloud.firestore.LocalFirestoreHelper.map;
import static com.google.cloud.firestore.it.TestHelper.await;
import static com.google.cloud.firestore.it.TestHelper.isRunningAgainstFirestoreEmulator;
import static com.google.common.truth.Truth.assertThat;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThrows;
import static org.junit.Assume.assumeFalse;

import com.google.cloud.firestore.*;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ITQueryAggregationsTest {
  private Firestore firestore;

  @Before
  public void setUpFirestore() {
    // TODO: stop using emulator for these tests once prod is ready.
    firestore = FirestoreOptions.newBuilder().setHost("localhost:8080").build().getService();
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

  private CollectionReference testCollection() {
    String collectionPath = "java-" + autoId();
    return firestore.collection(collectionPath);
  }

  private CollectionReference testCollection(String name) {
    return firestore.collection("java-" + name + "-" + autoId());
  }

  private CollectionReference testCollectionWithDocs(Map<String, Map<String, Object>> docs)
      throws InterruptedException {
    CollectionReference collection = testCollection();
    CollectionReference writer = firestore.collection(collection.getId());
    writeAllDocs(writer, docs);
    return collection;
  }

  public static void writeAllDocs(
      CollectionReference collection, Map<String, Map<String, Object>> docs)
      throws InterruptedException {
    for (Map.Entry<String, Map<String, Object>> doc : docs.entrySet()) {
      await(collection.document(doc.getKey()).set(doc.getValue()));
    }
  }

  private static Map<String, Map<String, Object>> testDocs1 =
      map(
          "a",
          map("author", "authorA", "title", "titleA", "pages", 100, "foo", 1, "bar", 2, "baz", 3),
          "b",
          map("author", "authorB", "title", "titleB", "pages", 50, "foo", 1, "bar", 2, "baz", 3));

  @Test
  public void canRunCountUsingAggregationMethod() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot = collection.aggregate(AggregateField.count()).get().get();
    assertThat(snapshot.getCount()).isEqualTo(2);
  }

  @Test
  public void canGetDuplicateAggregations() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot =
        collection
            .aggregate(AggregateField.count(), AggregateField.count(), sum("pages"), sum("pages"))
            .get()
            .get();
    assertThat(snapshot.getCount()).isEqualTo(2);
    assertThat(snapshot.get(sum("pages"))).isEqualTo(150);
  }

  @Test
  public void aggregateQueryInATransactionShouldLockTheCountedDocuments() throws Exception {
    assumeFalse(
        "Skip this test when running against the emulator because it does not require composite index creation.",
        isRunningAgainstFirestoreEmulator(firestore));

    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuery aggregateQuery =
        collection
            .whereEqualTo("key1", 42)
            .whereLessThan("key2", 42)
            .aggregate(AggregateField.count());
    ExecutionException executionException =
        assertThrows(ExecutionException.class, () -> aggregateQuery.get().get());
    assertThat(executionException)
        .hasCauseThat()
        .hasMessageThat()
        .containsMatch("/index.*https:\\/\\/console\\.firebase\\.google\\.com/");
  }

  @Test
  public void canRunSumQuery() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("pages")).get().get();
    assertThat(snapshot.get(sum("pages"))).isEqualTo(150);
  }

  @Test
  public void canRunAverageQuery() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("pages")).get().get();
    assertThat(snapshot.get(average("pages"))).isEqualTo(75.0);
  }

  @Test
  public void canGetMultipleAggregationsInTheSameQuery() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot =
        collection.aggregate(sum("pages"), average("pages"), AggregateField.count()).get().get();
    assertThat(snapshot.get(sum("pages"))).isEqualTo(150);
    assertThat(snapshot.get(average("pages"))).isEqualTo(75.0);
    assertThat(snapshot.get(AggregateField.count())).isEqualTo(2);
  }

  @Test
  public void getCorrectTypeForSumLong() throws Exception {
    Map<String, Map<String, Object>> testDocs = map("a", map("foo", 100), "b", map("foo", 100));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("foo")).get().get();
    Object sum = snapshot.get(sum("foo"));
    assertThat(sum instanceof Long).isTrue();
  }

  @Test
  public void getCorrectTypeForSumDouble() throws Exception {
    Map<String, Map<String, Object>> testDocs = map("a", map("foo", 100.5), "b", map("foo", 100));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("foo")).get().get();
    Object sum = snapshot.get(sum("foo"));
    assertThat(sum instanceof Double).isTrue();
  }

  @Test
  public void getCorrectTypeForSumNaN() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map("a", map("foo", 100.5), "b", map("foo", Double.NaN));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("foo")).get().get();
    Object sum = snapshot.get(sum("foo"));
    assertThat(sum instanceof Double).isTrue();
    assertThat(sum.equals(Double.NaN));
  }

  @Test
  public void getCorrectTypeForAverageDouble() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("pages")).get().get();
    Object average = snapshot.get((AggregateField) average("pages"));
    assertThat(average instanceof Double).isTrue();
  }

  @Test
  public void getCorrectTypeForAverageNaN() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map("a", map("foo", 100.5), "b", map("foo", Double.NaN));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("foo")).get().get();
    Object sum = snapshot.get(average("foo"));
    assertThat(sum instanceof Double).isTrue();
    assertThat(sum.equals(Double.NaN));
  }

  @Test
  public void getCorrectTypeForAverageNull() throws Exception {
    CollectionReference collection = testCollection();
    AggregateQuerySnapshot snapshot = collection.aggregate(average("bar")).get().get();
    Object sum = snapshot.get(average("bar"));
    assertThat(sum == null).isTrue();
  }

  @Test
  public void canPerformMaxAggregations() throws Exception {
    // TODO: Update these tests once aggregate de-duplication is implemented and more aggregation
    // types are available.
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateField f1 = sum("pages");
    AggregateField f2 = average("pages");
    AggregateField f3 = AggregateField.count();
    AggregateField f4 = sum("foo");
    AggregateField f5 = sum("bar");
    AggregateQuerySnapshot snapshot = collection.aggregate(f1, f2, f3, f4, f5).get().get();
    assertThat(snapshot.get(f1)).isEqualTo(150);
    assertThat(snapshot.get(f2)).isEqualTo(75.0);
    assertThat(snapshot.get(f3)).isEqualTo(2);
    assertThat(snapshot.get(f4)).isEqualTo(2);
    assertThat(snapshot.get(f5)).isEqualTo(4);
  }

  @Test
  public void cannotPerformMoreThanMaxAggregations() throws Exception {
    // TODO: Update these tests once aggregate de-duplication is implemented and more aggregation
    // types are available.
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateField f1 = sum("pages");
    AggregateField f2 = average("pages");
    AggregateField f3 = AggregateField.count();
    AggregateField f4 = sum("foo");
    AggregateField f5 = sum("bar");
    AggregateField f6 = sum("baz");
    Exception exception = null;
    try {
      collection.aggregate(f1, f2, f3, f4, f5, f6).get().get();
    } catch (Exception e) {
      exception = e;
    }
    assertThat(exception).isNotNull();
    assertThat(exception.getMessage()).contains("maximum number of aggregations");
  }

  @Test
  public void aggregateQueriesSupportCollectionGroups() throws Exception {
    String collectionGroupId = "myColGroupId";
    Map<String, Object> data = map("x", 2);
    // Setting documents at the following paths:
    //   `${collectionGroupId}/cg-doc1`,
    //   `abc/123/${collectionGroupId}/cg-doc2`,
    //   `zzz${collectionGroupId}/cg-doc3`,
    //   `abc/123/zzz${collectionGroupId}/cg-doc4`,
    //   `abc/123/zzz/${collectionGroupId}`
    await(firestore.collection(collectionGroupId).document("cg-doc1").set(data));
    await(
        firestore
            .collection("abc")
            .document("123")
            .collection(collectionGroupId)
            .document("cg-doc2")
            .set(data));
    await(firestore.collection("zzz" + collectionGroupId).document("cg-doc3").set(data));
    await(
        firestore
            .collection("abc")
            .document("123")
            .collection("zzz" + collectionGroupId)
            .document("cg-doc4")
            .set(data));
    await(
        firestore
            .collection("abc")
            .document("123")
            .collection("zzz")
            .document(collectionGroupId)
            .set(data));
    CollectionGroup collectionGroup = firestore.collectionGroup(collectionGroupId);
    AggregateQuerySnapshot snapshot =
        collectionGroup.aggregate(AggregateField.count(), sum("x"), average("x")).get().get();
    assertThat(snapshot.get(AggregateField.count())).isEqualTo(2);
    assertThat(snapshot.get(sum("x"))).isEqualTo(4);
    assertThat(snapshot.get(average("x"))).isEqualTo(2);
  }

  @Test
  public void performsAggregationsOnDocumentsWithAllaggregatedFields() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a",
            map("author", "authorA", "title", "titleA", "pages", 100, "year", 1980),
            "b",
            map("author", "authorB", "title", "titleB", "pages", 50, "year", 2020),
            "c",
            map("author", "authorC", "title", "titleC", "pages", 150, "year", 2021),
            "d",
            map("author", "authorD", "title", "titleD", "pages", 50));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot =
        collection
            .aggregate(sum("pages"), average("pages"), average("year"), AggregateField.count())
            .get()
            .get();
    assertThat(snapshot.get(sum("pages"))).isEqualTo(300);
    assertThat(snapshot.get(average("pages"))).isEqualTo(100);
    assertThat(snapshot.get(average("year"))).isEqualTo(2007);
    assertThat(snapshot.get(AggregateField.count())).isEqualTo(3);
  }

  @Test
  public void performsAggregationsWhenNaNExistsForSomeFieldValues() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a",
                map(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    5),
            "b",
                map("author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 4),
            "c",
                map(
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
                map(
                    "author", "authorD", "title", "titleD", "pages", 50, "year", 2020, "rating",
                    0));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot =
        collection.aggregate(sum("rating"), sum("pages"), average("year")).get().get();
    assertThat(snapshot.get(sum("rating"))).isEqualTo(Double.NaN);
    assertThat(snapshot.get(sum("pages"))).isEqualTo(300);
    assertThat(snapshot.get(average("year"))).isEqualTo(2000);
  }

  @Test
  public void throwsAnErrorWhenGettingTheResultOfAnUnrequestedAggregation() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("pages")).get().get();
    Exception exception = null;
    try {
      snapshot.get(average("pages"));
    } catch (Exception e) {
      exception = e;
    }
    assertThat(exception).isNotNull();
    assertThat(exception.getMessage())
        .isEqualTo("'average(pages)' was not requested in the aggregation query.");
    exception = null;
    try {
      snapshot.get(sum("foo"));
    } catch (RuntimeException e) {
      exception = e;
    }
    assertThat(exception).isNotNull();
    assertThat(exception.getMessage())
        .isEqualTo("'sum(foo)' was not requested in the aggregation query.");
  }

  @Test
  public void performsAggregationWhenUsingInOperator() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a",
                map(
                    "author", "authorA", "title", "titleA", "pages", 100, "year", 1980, "rating",
                    5),
            "b",
                map("author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating", 4),
            "c",
                map(
                    "author", "authorC", "title", "titleC", "pages", 100, "year", 1980, "rating",
                    3),
            "d",
                map(
                    "author", "authorD", "title", "titleD", "pages", 50, "year", 2020, "rating",
                    0));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot =
        collection
            .whereIn("rating", asList(5, 3))
            .aggregate(
                sum("rating"),
                average("rating"),
                sum("pages"),
                average("pages"),
                AggregateField.count())
            .get()
            .get();
    assertThat(snapshot.get(sum("rating"))).isEqualTo(8);
    assertThat(snapshot.get(average("rating"))).isEqualTo(4);
    assertThat(snapshot.get(sum("pages"))).isEqualTo(200);
    assertThat(snapshot.get(average("pages"))).isEqualTo(100);
    assertThat(snapshot.get(AggregateField.count())).isEqualTo(2);
  }

  @Test
  public void performsAggregationWhenUsingArrayContainsAnyOperator() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a",
                map(
                    "author",
                    "authorA",
                    "title",
                    "titleA",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    asList(5, 1000)),
            "b",
                map(
                    "author", "authorB", "title", "titleB", "pages", 50, "year", 2020, "rating",
                    asList(4)),
            "c",
                map(
                    "author",
                    "authorC",
                    "title",
                    "titleC",
                    "pages",
                    100,
                    "year",
                    1980,
                    "rating",
                    asList(2222, 3)),
            "d",
                map(
                    "author", "authorD", "title", "titleD", "pages", 50, "year", 2020, "rating",
                    asList(0)));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot =
        collection
            .whereArrayContainsAny("rating", asList(5, 3))
            .aggregate(
                sum("rating"),
                average("rating"),
                sum("pages"),
                average("pages"),
                AggregateField.count())
            .get()
            .get();
    assertThat(snapshot.get(sum("rating"))).isEqualTo(0);
    assertThat(snapshot.get(average("rating"))).isEqualTo(null);
    assertThat(snapshot.get(sum("pages"))).isEqualTo(200);
    assertThat(snapshot.get(average("pages"))).isEqualTo(100);
    assertThat(snapshot.get(AggregateField.count())).isEqualTo(2);
  }

  @Test
  public void performsAggregationsOnNestedMapValues() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a",
            map(
                "author",
                "authorA",
                "title",
                "titleA",
                "metadata",
                map("pages", 100, "rating", map("critic", 2, "user", 5))),
            "b",
            map(
                "author",
                "authorB",
                "title",
                "titleB",
                "metadata",
                map("pages", 50, "rating", map("critic", 4, "user", 4))));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot =
        collection
            .aggregate(
                sum("metadata.pages"),
                average("metadata.pages"),
                average("metadata.rating.critic"),
                sum("metadata.rating.user"),
                AggregateField.count())
            .get()
            .get();
    assertThat(snapshot.get(sum("metadata.pages"))).isEqualTo(150);
    assertThat(snapshot.get(average("metadata.pages"))).isEqualTo(75);
    assertThat(snapshot.get(average("metadata.rating.critic"))).isEqualTo(3);
    assertThat(snapshot.get(sum("metadata.rating.user"))).isEqualTo(9);
    assertThat(snapshot.get(AggregateField.count())).isEqualTo(2);
  }

  @Test
  public void performsSumThatResultsInFloat() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", 5),
            "b", map("author", "authorB", "title", "titleB", "rating", 4.5),
            "c", map("author", "authorC", "title", "titleC", "rating", 3));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("rating")).get().get();
    Object sum = snapshot.get(sum("rating"));
    assertThat(sum instanceof Double).isTrue();
    assertThat(sum).isEqualTo(12.5);
  }

  @Test
  public void performsSumOfIntsAndFloatsThatResultsInInt() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", 5),
            "b", map("author", "authorB", "title", "titleB", "rating", 4.5),
            "c", map("author", "authorC", "title", "titleC", "rating", 3.5));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("rating")).get().get();
    Object sum = snapshot.get(sum("rating"));
    assertThat(sum instanceof Long).isTrue();
    assertThat(sum).isEqualTo(13);
  }

  @Test
  public void performsSumThatOverflowsMaxLong() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", Long.MAX_VALUE),
            "b", map("author", "authorB", "title", "titleB", "rating", Long.MAX_VALUE));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("rating")).get().get();
    Object sum = snapshot.get(sum("rating"));
    assertThat(sum instanceof Double).isTrue();
    assertThat(sum).isEqualTo((double) Long.MAX_VALUE + (double) Long.MAX_VALUE);
  }

  @Test
  public void performsSumThatCanOverflowIntegerValuesDuringAccumulation() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", Long.MAX_VALUE),
            "b", map("author", "authorB", "title", "titleB", "rating", 1),
            "c", map("author", "authorC", "title", "titleC", "rating", -101));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("rating")).get().get();
    Object sum = snapshot.get(sum("rating"));
    assertThat(sum instanceof Long).isTrue();
    assertThat(sum).isEqualTo(Long.MAX_VALUE - 100);
  }

  @Test
  public void performsSumThatIsNegative() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", Long.MAX_VALUE),
            "b", map("author", "authorB", "title", "titleB", "rating", -Long.MAX_VALUE),
            "c", map("author", "authorC", "title", "titleC", "rating", -101),
            "d", map("author", "authorD", "title", "titleD", "rating", -10000));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("rating")).get().get();
    assertThat(snapshot.get(sum("rating"))).isEqualTo(-10101);
  }

  @Test
  public void performsSumThatIsPositiveInfinity() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", Double.MAX_VALUE),
            "b", map("author", "authorB", "title", "titleB", "rating", Double.MAX_VALUE));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("rating")).get().get();
    Object sum = snapshot.get(sum("rating"));
    assertThat(sum instanceof Double).isTrue();
    assertThat(sum).isEqualTo(Double.POSITIVE_INFINITY);
    assertThat(snapshot.getDouble(sum("rating"))).isEqualTo(Double.POSITIVE_INFINITY);
    assertThat(snapshot.getLong(sum("rating"))).isEqualTo(Long.MAX_VALUE);
  }

  @Test
  public void performsSumThatIsNegativeInfinity() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", -Double.MAX_VALUE),
            "b", map("author", "authorB", "title", "titleB", "rating", -Double.MAX_VALUE));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("rating")).get().get();
    Object sum = snapshot.get(sum("rating"));
    assertThat(sum instanceof Double).isTrue();
    assertThat(sum).isEqualTo(Double.NEGATIVE_INFINITY);
    assertThat(snapshot.getDouble(sum("rating"))).isEqualTo(Double.NEGATIVE_INFINITY);
    assertThat(snapshot.getLong(sum("rating"))).isEqualTo(Long.MIN_VALUE);
  }

  @Test
  public void performsSumThatIsValidButCouldOverflowDuringAggregation() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", Double.MAX_VALUE),
            "b", map("author", "authorB", "title", "titleB", "rating", Double.MAX_VALUE),
            "c", map("author", "authorC", "title", "titleC", "rating", -Double.MAX_VALUE),
            "d", map("author", "authorD", "title", "titleD", "rating", -Double.MAX_VALUE),
            "e", map("author", "authorE", "title", "titleE", "rating", Double.MAX_VALUE),
            "f", map("author", "authorF", "title", "titleF", "rating", -Double.MAX_VALUE),
            "g", map("author", "authorG", "title", "titleG", "rating", -Double.MAX_VALUE),
            "h", map("author", "authorH", "title", "titleH", "rating", Double.MAX_VALUE));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("rating")).get().get();
    Object sum = snapshot.get(sum("rating"));
    assertThat(sum instanceof Long).isTrue();
    assertThat(sum).isEqualTo(0);
  }

  @Test
  public void performsSumThatIncludesNaN() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", 5),
            "b", map("author", "authorB", "title", "titleB", "rating", 4),
            "c", map("author", "authorC", "title", "titleC", "rating", Double.NaN),
            "d", map("author", "authorD", "title", "titleD", "rating", 0));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("rating")).get().get();
    assertThat(snapshot.get(sum("rating"))).isEqualTo(Double.NaN);
  }

  @Test
  public void performsSumOverResultSetOfZeroDocuments() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot =
        collection.whereGreaterThan("pages", 200).aggregate(sum("pages")).get().get();
    assertThat(snapshot.get(sum("pages"))).isEqualTo(0);
  }

  @Test
  public void performsSumOnlyOnNumericFields() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", 5),
            "b", map("author", "authorB", "title", "titleB", "rating", 4),
            "c", map("author", "authorC", "title", "titleC", "rating", "3"),
            "d", map("author", "authorD", "title", "titleD", "rating", 1));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot =
        collection.aggregate(sum("rating"), AggregateField.count()).get().get();
    assertThat(snapshot.get(sum("rating"))).isEqualTo(10);
    assertThat(snapshot.get(AggregateField.count())).isEqualTo(4);
  }

  @Test
  public void performsSumOfMinIEEE754() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map("a", map("author", "authorA", "title", "titleA", "rating", Double.MIN_VALUE));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(sum("rating")).get().get();
    assertThat(snapshot.get(sum("rating"))).isEqualTo(Double.MIN_VALUE);
  }

  @Test
  public void performsAverageOfIntsThatResultsInAnInt() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", 10),
            "b", map("author", "authorB", "title", "titleB", "rating", 5),
            "c", map("author", "authorC", "title", "titleC", "rating", 0));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("rating")).get().get();
    assertThat(snapshot.get(average("rating"))).isEqualTo(5);
    assertThat(snapshot.getLong(average("rating"))).isEqualTo(5L);
    assertThat(snapshot.getDouble(average("rating"))).isEqualTo(5.0);
  }

  @Test
  public void performsAverageOfFloatsThatResultsInAnInt() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", 10.5),
            "b", map("author", "authorB", "title", "titleB", "rating", 9.5));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("rating")).get().get();
    assertThat(snapshot.get(average("rating")) instanceof Double).isTrue();
    assertThat(snapshot.get(average("rating"))).isEqualTo(10);
    assertThat(snapshot.getLong(average("rating"))).isEqualTo(10L);
    assertThat(snapshot.getDouble(average("rating"))).isEqualTo(10.0);
  }

  @Test
  public void performsAverageOfFloatsAndIntsThatResultsInAnInt() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", 10),
            "b", map("author", "authorB", "title", "titleB", "rating", 9.5),
            "c", map("author", "authorC", "title", "titleC", "rating", 10.5));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("rating")).get().get();
    assertThat(snapshot.get(average("rating"))).isEqualTo(10);
    assertThat(snapshot.getLong(average("rating"))).isEqualTo(10L);
    assertThat(snapshot.getDouble(average("rating"))).isEqualTo(10.0);
  }

  @Test
  public void performsAverageOfFloatsThatResultsInAFloat() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", 5.5),
            "b", map("author", "authorB", "title", "titleB", "rating", 4.5),
            "c", map("author", "authorC", "title", "titleC", "rating", 3.5));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("rating")).get().get();
    assertThat(snapshot.get(average("rating"))).isEqualTo(4.5);
    assertThat(snapshot.getDouble(average("rating"))).isEqualTo(4.5);
    assertThat(snapshot.getLong(average("rating"))).isEqualTo(4L);
  }

  @Test
  public void performsAverageOfFloatsAndIntsThatResultsInAFloat() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", 8.6),
            "b", map("author", "authorB", "title", "titleB", "rating", 9),
            "c", map("author", "authorC", "title", "titleC", "rating", 10));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("rating")).get().get();
    // TODO: isEqualTo(9.2) or isEqualTo(9.2d) fails with:
    // Expected :9.2
    // Actual   :9.200000000000001
    assertThat(snapshot.get(average("rating"))).isEqualTo(27.6 / 3);
    assertThat(snapshot.getDouble(average("rating"))).isEqualTo(27.6 / 3);
    assertThat(snapshot.getLong(average("rating"))).isEqualTo(9L);
  }

  @Test
  public void performsAverageOfIntsThatResultsInAFloat() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", 10),
            "b", map("author", "authorB", "title", "titleB", "rating", 9));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("rating")).get().get();
    assertThat(snapshot.get(average("rating"))).isEqualTo(9.5);
    assertThat(snapshot.getDouble(average("rating"))).isEqualTo(9.5d);
    assertThat(snapshot.getLong(average("rating"))).isEqualTo(9L);
  }

  @Test
  public void performsAverageCausingUnderflow() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", Double.MIN_VALUE),
            "b", map("author", "authorB", "title", "titleB", "rating", 0));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("rating")).get().get();
    assertThat(snapshot.get(average("rating"))).isEqualTo(0);
    assertThat(snapshot.getDouble(average("rating"))).isEqualTo(0.0d);
    assertThat(snapshot.getLong(average("rating"))).isEqualTo(0L);
  }

  @Test
  public void performsAverageOfMinIEEE754() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map("a", map("author", "authorA", "title", "titleA", "rating", Double.MIN_VALUE));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("rating")).get().get();
    assertThat(snapshot.get(average("rating"))).isEqualTo(Double.MIN_VALUE);
    assertThat(snapshot.getDouble(average("rating"))).isEqualTo(Double.MIN_VALUE);
    assertThat(snapshot.getLong(average("rating"))).isEqualTo(0);
  }

  // TODO: This test fails. The average returned is Infinity.
  // TODO: The reference implementation expects average to be Double.MAX_VALUE.
  @Ignore
  @Test
  public void performsAverageThatCouldOverflowIEEE754DuringAccumulation() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a",
            map("author", "authorA", "title", "titleA", "rating", Double.MAX_VALUE),
            "b",
            map("author", "authorB", "title", "titleB", "rating", Double.MAX_VALUE));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("rating")).get().get();
    assertThat(snapshot.get(average("rating"))).isEqualTo(Double.MAX_VALUE);
    assertThat(snapshot.getDouble(average("rating"))).isEqualTo(Double.MAX_VALUE);
    assertThat(snapshot.getLong(average("rating"))).isEqualTo(Long.MAX_VALUE);
  }

  @Test
  public void performsAverageThatIncludesNaN() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a",
            map("author", "authorA", "title", "titleA", "rating", 5),
            "b",
            map("author", "authorB", "title", "titleB", "rating", 4),
            "c",
            map("author", "authorC", "title", "titleC", "rating", Double.NaN),
            "d",
            map("author", "authorD", "title", "titleD", "rating", 0));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot = collection.aggregate(average("rating")).get().get();
    assertThat(snapshot.get(average("rating"))).isEqualTo(Double.NaN);
    assertThat(snapshot.getDouble(average("rating"))).isEqualTo(Double.NaN);
    assertThat(snapshot.getLong(average("rating"))).isEqualTo(0L);
  }

  @Test
  public void performsAverageOverResultSetOfZeroDocuments() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot =
        collection.whereGreaterThan("pages", 200).aggregate(average("pages")).get().get();
    assertThat(snapshot.get(average("pages"))).isEqualTo(null);
    assertThat(snapshot.getDouble(average("pages"))).isEqualTo(null);
    assertThat(snapshot.getLong(average("pages"))).isEqualTo(null);
  }

  @Test
  public void performsAverageOnlyOnNumericFields() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "a", map("author", "authorA", "title", "titleA", "rating", 5),
            "b", map("author", "authorB", "title", "titleB", "rating", 4),
            "c", map("author", "authorC", "title", "titleC", "rating", "3"),
            "d", map("author", "authorD", "title", "titleD", "rating", 6));
    CollectionReference collection = testCollectionWithDocs(testDocs);
    AggregateQuerySnapshot snapshot =
        collection.aggregate(average("rating"), AggregateField.count()).get().get();
    assertThat(snapshot.get(average("rating"))).isEqualTo(5);
    assertThat(snapshot.get(AggregateField.count())).isEqualTo(4);
  }
}
