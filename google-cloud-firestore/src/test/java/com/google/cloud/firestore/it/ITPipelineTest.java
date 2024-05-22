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

import static com.google.cloud.firestore.it.ITQueryTest.map;
import static com.google.cloud.firestore.pipeline.Function.and;
import static com.google.cloud.firestore.pipeline.Function.avg;
import static com.google.cloud.firestore.pipeline.Function.concat;
import static com.google.cloud.firestore.pipeline.Function.cosineDistance;
import static com.google.cloud.firestore.pipeline.Function.equal;
import static com.google.cloud.firestore.pipeline.Function.lessThan;
import static com.google.cloud.firestore.pipeline.Function.not;
import static com.google.cloud.firestore.pipeline.Function.or;
import static com.google.cloud.firestore.pipeline.Function.toLower;
import static com.google.cloud.firestore.pipeline.Function.trim;
import static com.google.cloud.firestore.pipeline.Sort.Ordering.ascending;
import static com.google.cloud.firestore.pipeline.Sort.Ordering.descending;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.LocalFirestoreHelper;
import com.google.cloud.firestore.PaginatingPipeline;
import com.google.cloud.firestore.Pipeline;
import com.google.cloud.firestore.PipelineResult;
import com.google.cloud.firestore.pipeline.Constant;
import com.google.cloud.firestore.pipeline.Field;
import com.google.cloud.firestore.pipeline.Fields;
import com.google.cloud.firestore.pipeline.FindNearest.DistanceMeasure;
import com.google.cloud.firestore.pipeline.FindNearest.FindNearestOptions;
import com.google.cloud.firestore.pipeline.Function;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ITPipelineTest extends ITBaseTest {
  public CollectionReference testCollectionWithDocs(Map<String, Map<String, Object>> docs)
      throws ExecutionException, InterruptedException, TimeoutException {
    CollectionReference collection = firestore.collection(LocalFirestoreHelper.autoId());
    for (Map.Entry<String, Map<String, Object>> doc : docs.entrySet()) {
      collection.document(doc.getKey()).set(doc.getValue()).get(5, TimeUnit.SECONDS);
    }
    return collection;
  }

  @Test
  public void fromSources() throws Exception {
    Map<String, Map<String, Object>> testDocs =
        map(
            "doc1", map("a", 1, "b", 0),
            "doc2", map("a", 2, "b", 1),
            "doc3", map("a", 3, "b", 2),
            "doc4", map("a", 1, "b", 3),
            "doc5", map("a", 1, "b", 1));

    CollectionReference collection = testCollectionWithDocs(testDocs);

    Pipeline p = Pipeline.fromCollectionGroup(collection.getId());
    List<PipelineResult> results = p.execute(firestore).get();
    System.out.println(results.size());
  }

  @Test
  public void projections() throws Exception {
    Pipeline p = Pipeline.fromCollectionGroup("coll1").select(Field.of("foo"));
    List<PipelineResult> results = p.execute(firestore).get();
    System.out.println(results.size());

    p = Pipeline.fromCollectionGroup("coll1").select(Fields.of("foo", "bar", "baz"));
    results = p.execute(firestore).get();
  }

  @Test
  public void filters() throws Exception {
    Pipeline p =
        Pipeline.fromCollectionGroup("coll1")
            .filter(Field.of("foo").equal(42))
            .filter(or(Field.of("bar").lessThan(100), Constant.of("value").equal(Field.of("key"))))
            .filter(not(Constant.of(128).inAny("f1", "f2")));
    List<PipelineResult> results = p.execute(firestore).get();

    p =
        Pipeline.fromCollectionGroup("coll1")
            .filter(equal(Field.of("foo"), 42))
            .filter(
                or(lessThan(Field.of("bar"), 100), equal(Field.of("key"), Constant.of("value"))))
            .filter(not(Constant.of(128).inAny("f1", "f2")));
    results = p.execute(firestore).get();
  }

  @Test
  public void inFilters() throws Exception {
    Pipeline p = Pipeline.fromCollectionGroup("coll1").filter(Field.of("foo").inAny(42, "42"));
    List<PipelineResult> results = p.execute(firestore).get();
  }

  @Test
  public void sorts() throws Exception {
    Pipeline p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(42, "42"))
            .sort(
                Field.of("rank").ascending(),
                cosineDistance(Field.of("embedding1"), Field.of("embedding2")).descending())
            .limit(100);
    List<PipelineResult> results = p.execute(firestore).get();

    // equivalent but more concise.
    p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(42, false))
            .sort(
                ascending(Field.of("rank")),
                descending(cosineDistance(Field.of("embedding1"), Field.of("embedding2"))))
            .limit(100);
    results = p.execute(firestore).get();
  }

  @Test
  public void pagination() throws Exception {
    PaginatingPipeline p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(42, "bar"))
            .paginate(
                100, cosineDistance(Field.of("embedding1"), Field.of("embedding2")).descending());

    List<PipelineResult> results = p.firstPage().execute(firestore).get();
    List<PipelineResult> secondPage =
        p.startAfter(results.get(results.size() - 1)).firstPage().execute(firestore).get();
  }

  @Test
  public void limit() throws Exception {
    Pipeline p = Pipeline.fromDatabase().filter(Field.of("foo").inAny(42, "bar")).limit(10);

    List<PipelineResult> result = p.execute(firestore).get();
  }

  @Test
  public void offset() throws Exception {
    Pipeline p =
        Pipeline.fromDocuments(firestore.document("foo/bar1"), firestore.document("foo/bar2"))
            .offset(1);

    List<PipelineResult> result = p.execute(firestore).get();
  }
  @Test
  public void groupBy() throws Exception {
    Pipeline p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(42, Field.of("bar")))
            .group(Fields.of("given_name", "family_name"))
            .aggregate(Field.of("score").avg().toField("avg_score_2"));

    List<PipelineResult> results = p.execute(firestore).get();
  }

  @Test
  public void aggregateWithoutGrouping() throws Exception {
    Pipeline p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(Constant.of(42), Field.of("bar")))
            .aggregate(avg(Field.of("score")).toField("avg_score_1"));
  }

  @Test
  public void joins() throws Exception {
    Pipeline p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(Constant.of(42), Field.of("bar")));
    Pipeline pipe =
        Pipeline.fromCollection("users")
            .findNearest(
                Field.of("embedding"),
                new double[] {1.0, 2.0},
                FindNearestOptions.newInstance(1000, DistanceMeasure.euclidean(), Field.of("distance")))
            .innerJoin(p)
            .on(
                and(
                    Field.of("foo").equal(Field.of(p, "bar")),
                    Field.of(p, "requirement").greaterThan(Field.of("distance"))))
            // select is optional in case user want to resolve conflict manually
            .select(Fields.ofAll().usingPrefix("left"), Fields.ofAll(p).usingPrefix("right"));

    Pipeline another =
        Pipeline.fromCollection("users")
            .innerJoin(p)
            .on(Fields.of("foo", "bar"))
            // select is optional in case user want to resolve conflict manually
            .select(
                Fields.ofAll().usingPrefix("left"), Field.ofAll(p).usingPrefix("right"));
  }

  @Test
  public void functionComposition() throws Exception {
    // A normalized value by joining the first and last name, triming surrounding whitespace and
    // convert to lower case
    Function normalized = concat(Field.of("first_name"), Constant.of(" "), Field.of("last_name"));
    normalized = trim(normalized);
    normalized = toLower(normalized);

    Pipeline p =
        Pipeline.fromCollection("users")
            .filter(
                or(
                    normalized.equal(Constant.of("john smith")),
                    normalized.equal(Constant.of("alice baker"))));
  }
}
