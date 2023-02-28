package com.google.cloud.firestore.it;

import static com.google.cloud.firestore.LocalFirestoreHelper.autoId;
import static com.google.cloud.firestore.LocalFirestoreHelper.map;
import static com.google.cloud.firestore.it.TestHelper.await;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.cloud.firestore.*;
import com.google.common.base.Preconditions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Map;

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

  private CollectionReference testCollectionWithDocs(Map<String, Map<String, Object>> docs) throws InterruptedException {
    CollectionReference collection = testCollection();
    CollectionReference writer = firestore.collection(collection.getId());
    writeAllDocs(writer, docs);
    return collection;
  }

  public static void writeAllDocs(
          CollectionReference collection, Map<String, Map<String, Object>> docs) throws InterruptedException {
    for (Map.Entry<String, Map<String, Object>> doc : docs.entrySet()) {
      await(collection.document(doc.getKey()).set(doc.getValue()));
    }
  }

  private static Map<String, Map<String, Object>> testDocs1 =
          map("a", map("author", "authorA", "title", "titleA", "pages", 100, "foo", 1, "bar", 2, "baz", 3),
                  "b", map("author", "authorB", "title", "titleB", "pages", 50, "foo", 1, "bar", 2, "baz", 3));

  @Test
  public void canRunCountUsingAggregationMethod() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot = collection.aggregate(AggregateField.count()).get().get();
    assertThat(snapshot.getCount()).isEqualTo(2);
  }

  @Test
  public void canGetDuplicateAggregations() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot = collection.aggregate(
            AggregateField.count(), AggregateField.count(), AggregateField.sum("pages"), AggregateField.sum("pages")).get().get();
    assertThat(snapshot.getCount()).isEqualTo(2);
    assertThat(snapshot.get(AggregateField.sum("pages"))).isEqualTo(150);
  }

  @Test
  public void canRunSumQuery() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot = collection.aggregate(AggregateField.sum("pages")).get().get();
    assertThat(snapshot.get(AggregateField.sum("pages"))).isEqualTo(150);
  }

  @Test
  public void canRunAverageQuery() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot = collection.aggregate(AggregateField.average("pages")).get().get();
    assertThat(snapshot.get(AggregateField.average("pages"))).isEqualTo(75.0);
  }

  @Test
  public void canGetMultipleAggregationsInTheSameQuery() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot = collection.aggregate(AggregateField.sum("pages"), AggregateField.average("pages"), AggregateField.count()).get().get();
    assertThat(snapshot.get(AggregateField.sum("pages"))).isEqualTo(150);
    assertThat(snapshot.get(AggregateField.average("pages"))).isEqualTo(75.0);
    assertThat(snapshot.get(AggregateField.count())).isEqualTo(2);
  }

  @Test
  public void getCorrectTypeForSum() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot = collection.aggregate(AggregateField.sum("pages")).get().get();
    Object sum = snapshot.get(AggregateField.sum("pages"));
    assertThat(sum instanceof Long).isTrue();
  }

  @Test
  public void getCorrectTypeForAverage() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateQuerySnapshot snapshot = collection.aggregate(AggregateField.average("pages")).get().get();
    Object average = snapshot.get((AggregateField) AggregateField.average("pages"));
    assertThat(average instanceof Double).isTrue();
  }

  @Test
  public void canPerformMaxAggregations() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateField f1 = AggregateField.sum("pages");
    AggregateField f2 = AggregateField.average("pages");
    AggregateField f3 = AggregateField.count();
    AggregateField f4 = AggregateField.sum("foo");
    AggregateField f5 = AggregateField.sum("bar");
    AggregateQuerySnapshot snapshot = collection.aggregate(f1, f2, f3, f4, f5).get().get();
    assertThat(snapshot.get(f1)).isEqualTo(150);
    assertThat(snapshot.get(f2)).isEqualTo(75.0);
    assertThat(snapshot.get(f3)).isEqualTo(2);
    assertThat(snapshot.get(f4)).isEqualTo(2);
    assertThat(snapshot.get(f5)).isEqualTo(4);
  }

  @Test
  public void cannotPerformMoreThanMaxAggregations() throws Exception {
    CollectionReference collection = testCollectionWithDocs(testDocs1);
    AggregateField f1 = AggregateField.sum("pages");
    AggregateField f2 = AggregateField.average("pages");
    AggregateField f3 = AggregateField.count();
    AggregateField f4 = AggregateField.sum("foo");
    AggregateField f5 = AggregateField.sum("bar");
    AggregateField f6 = AggregateField.sum("baz");
    Exception exception = null;
    try {
      collection.aggregate(f1,f2,f3,f4,f5,f6).get().get();
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
    await(firestore.collection("abc").document("123").collection(collectionGroupId).document("cg-doc2").set(data));
    await(firestore.collection("zzz"+collectionGroupId).document("cg-doc3").set(data));
    await(firestore.collection("abc").document("123").collection("zzz"+collectionGroupId).document("cg-doc4").set(data));
    await(firestore.collection("abc").document("123").collection("zzz").document(collectionGroupId).set(data));
    CollectionGroup collectionGroup = firestore.collectionGroup(collectionGroupId);
    AggregateQuerySnapshot snapshot = collectionGroup.aggregate(AggregateField.count(), AggregateField.sum("x"), AggregateField.average("x")).get().get();
    assertThat(snapshot.get(AggregateField.count())).isEqualTo(2);
    assertThat(snapshot.get(AggregateField.sum("x"))).isEqualTo(4);
    assertThat(snapshot.get(AggregateField.average("x"))).isEqualTo(2);
  }


}
