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
import static com.google.cloud.firestore.pipeline.Ordering.ascending;
import static com.google.cloud.firestore.pipeline.Ordering.descending;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.PaginatingPipeline;
import com.google.cloud.firestore.Pipeline;
import com.google.cloud.firestore.PipelineResult;
import com.google.cloud.firestore.pipeline.Constant;
import com.google.cloud.firestore.pipeline.Field;
import com.google.cloud.firestore.pipeline.Fields;
import com.google.cloud.firestore.pipeline.Ordering;
import com.google.cloud.firestore.pipeline.Ordering.Direction;
import java.util.Iterator;
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
    Pipeline p =
        Pipeline.fromCollection("coll1")
            .project(
                Field.of("foo"),
                Constant.of("emptyValue").asAlias("emptyField"),
                Field.of("embedding").cosineDistance(new double[] {1, 2, 3.0}).asAlias("distance"));

    // More compact
    p =
        Pipeline.fromCollection("coll1")
            .project(Fields.of("foo", "bar", "baz"), Constant.of(42).asAlias("emptyField"));
    p =
        Pipeline.fromCollection("coll1")
            // basically an addField
            .project(Fields.ofAll(), Constant.of(42).asAlias("emptyField"));
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

    p =
        Pipeline.fromCollectionGroup("coll1")
            .filter(equal(Field.of("foo"), 42))
            .filter(
                or(lessThan(Field.of("bar"), 100), equal(Field.of("key"), Constant.of("value"))))
            .filter(not(Constant.of(128).inAny(Field.of("f1"), Field.of("f2"))));
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
            .filter(Field.of("foo").inAny(Constant.of(42), Field.of("bar")))
            .aggregate(avg(Field.of("score")).toField("avg_score_1"));
  }

  @Test
  public void sorts() throws Exception {
    Pipeline p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(Constant.of(42), Field.of("bar")))
            .sort(
                Ordering.of(Field.of("rank")),
                Ordering.of(
                    cosineDistance(Field.of("embedding1"), Field.of("embedding2")), Direction.DESC))
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
                100,
                Ordering.of(
                    cosineDistance(Field.of("embedding1"), Field.of("embedding2")),
                    Direction.DESC));

    Iterator<PipelineResult> result = firestore.execute(p.firstPage()).get();
    Iterator<PipelineResult> second = firestore.execute(p.startAfter(result.next())).get();
  }

  @Test
  public void fluentAllTheWay() throws Exception {
    PaginatingPipeline p =
        Pipeline.fromCollection("coll1")
            .filter(Field.of("foo").inAny(Constant.of(42), Field.of("bar")))
            .paginate(
                100,
                Ordering.of(
                    cosineDistance(Field.of("embedding1"), Field.of("embedding2")),
                    Direction.DESC));

    Iterator<PipelineResult> result = p.firstPage().execute(firestore).get();
    Iterator<PipelineResult> second = p.startAfter(result.next()).execute(firestore).get();
  }
}
