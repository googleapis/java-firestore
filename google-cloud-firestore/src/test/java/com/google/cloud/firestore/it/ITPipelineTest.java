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

import static com.google.cloud.firestore.pipeline.Function.avg;
import static com.google.cloud.firestore.pipeline.Function.cosineDistance;
import static com.google.cloud.firestore.pipeline.Function.equal;
import static com.google.cloud.firestore.pipeline.Function.lessThan;
import static com.google.cloud.firestore.pipeline.Function.not;
import static com.google.cloud.firestore.pipeline.Function.or;
import static com.google.cloud.firestore.pipeline.Sort.Ordering.ascending;
import static com.google.cloud.firestore.pipeline.Sort.Ordering.descending;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.PaginatingPipeline;
import com.google.cloud.firestore.Pipeline;
import com.google.cloud.firestore.PipelineResult;
import com.google.cloud.firestore.pipeline.Constant;
import com.google.cloud.firestore.pipeline.Field;
import com.google.cloud.firestore.pipeline.Fields;
import com.google.cloud.firestore.pipeline.Sort;
import com.google.cloud.firestore.pipeline.Sort.Ordering.Direction;
import java.util.Iterator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ITPipelineTest {

  protected Firestore firestore;

  @Before
  public void before() throws Exception {
    firestore = FirestoreOptions.newBuilder().build().getService();
  }

  @Test
  public void projections() throws Exception {
    Pipeline p = Pipeline.fromCollection("coll1").project(Field.of("foo"));
    ApiFuture<List<PipelineResult>> results = p.execute(firestore);

    // More compact
    p = Pipeline.fromCollection("coll1").project(Fields.of("foo", "bar", "baz"));
    results = p.execute(firestore);
  }

  @Test
  public void filters() throws Exception {
    Pipeline p =
        Pipeline.fromCollectionGroup("coll1")
            .filter(Field.of("foo").equal(Constant.of(42)))
            .filter(
                or(
                    Field.of("bar").lessThan(Constant.of(100)),
                    Constant.of("value").equal(Field.of("key"))))
            .filter(not(Constant.of(128).inAny(Field.of("f1"), Field.of("f2"))));
    ApiFuture<List<PipelineResult>> results = p.execute(firestore);

    p =
        Pipeline.fromCollectionGroup("coll1")
            .filter(equal(Field.of("foo"), 42))
            .filter(
                or(lessThan(Field.of("bar"), 100), equal(Field.of("key"), Constant.of("value"))))
            .filter(not(Constant.of(128).inAny(Field.of("f1"), Field.of("f2"))));
    results = p.execute(firestore);
  }

  @Test
  public void inFilters() throws Exception {
    Pipeline p =
        Pipeline.fromCollectionGroup("coll1")
            .filter(Field.of("foo").inAny(Constant.of(42), Field.of("bar")));
  }

  @Test
  public void aggregateWithoutGrouping() throws Exception {
    Pipeline p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(42, Field.of("bar")))
            .aggregate(avg(Field.of("score")).toField("avg_score_1"));
  }

  @Test
  public void sorts() throws Exception {
    Pipeline p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(Constant.of(42), Field.of("bar")))
            .sort(
                Field.of("rank").ascending(),
                Sort.Ordering.of(
                    cosineDistance(Field.of("embedding1"), Field.of("embedding2")),
                    Direction.DESCENDING))
            .limit(100);

    // equivalent but more concise.
    p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(Constant.of(42), Field.of("bar")))
            .sort(
                ascending(Field.of("rank")),
                descending(cosineDistance(Field.of("embedding1"), Field.of("embedding2"))))
            .limit(100);
  }

  @Test
  public void pagination() throws Exception {
    PaginatingPipeline p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(Constant.of(42), Field.of("bar")))
            .paginate(
                100, cosineDistance(Field.of("embedding1"), Field.of("embedding2")).descending());

    Iterator<PipelineResult> result = firestore.execute(p.firstPage()).get();
    // Iterator<PipelineResult> second = firestore.execute(p.startAfter(result.next())).get();
  }

  @Test
  public void limit() throws Exception {
    Pipeline p =
        Pipeline.fromDatabase()
            .filter(Field.of("foo").inAny(Constant.of(42), Field.of("bar")))
            .limit(10);

    Iterator<PipelineResult> result = firestore.execute(p).get();
  }

  @Test
  public void offset() throws Exception {
    Pipeline p =
        Pipeline.fromDocuments(firestore.document("foo/bar1"), firestore.document("foo/bar2"))
            .offset(1);

    Iterator<PipelineResult> result = firestore.execute(p).get();
  }

  @Test
  public void fluentAllTheWay() throws Exception {
    PaginatingPipeline p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(Constant.of(42), Field.of("bar")))
            .paginate(
                100, cosineDistance(Field.of("embedding1"), Field.of("embedding2")).descending());

    ApiFuture<List<PipelineResult>> result = p.firstPage().execute(firestore);
    // List<PipelineResult> second =
    // p.startAfter(result.iterator().next()).execute(firestore).get();
  }
}
