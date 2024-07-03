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
import static com.google.cloud.firestore.pipeline.expressions.Function.add;
import static com.google.cloud.firestore.pipeline.expressions.Function.and;
import static com.google.cloud.firestore.pipeline.expressions.Function.arrayContains;
import static com.google.cloud.firestore.pipeline.expressions.Function.arrayContainsAll;
import static com.google.cloud.firestore.pipeline.expressions.Function.arrayContainsAny;
import static com.google.cloud.firestore.pipeline.expressions.Function.arrayElement;
import static com.google.cloud.firestore.pipeline.expressions.Function.arrayFilter;
import static com.google.cloud.firestore.pipeline.expressions.Function.arrayTransform;
import static com.google.cloud.firestore.pipeline.expressions.Function.avg;
import static com.google.cloud.firestore.pipeline.expressions.Function.collectionId;
import static com.google.cloud.firestore.pipeline.expressions.Function.cosineDistance;
import static com.google.cloud.firestore.pipeline.expressions.Function.countAll;
import static com.google.cloud.firestore.pipeline.expressions.Function.dotProductDistance;
import static com.google.cloud.firestore.pipeline.expressions.Function.equal;
import static com.google.cloud.firestore.pipeline.expressions.Function.euclideanDistance;
import static com.google.cloud.firestore.pipeline.expressions.Function.greaterThan;
import static com.google.cloud.firestore.pipeline.expressions.Function.isNull;
import static com.google.cloud.firestore.pipeline.expressions.Function.lessThan;
import static com.google.cloud.firestore.pipeline.expressions.Function.not;
import static com.google.cloud.firestore.pipeline.expressions.Function.notEqual;
import static com.google.cloud.firestore.pipeline.expressions.Function.or;
import static com.google.cloud.firestore.pipeline.expressions.Function.parent;
import static com.google.cloud.firestore.pipeline.expressions.Function.strConcat;
import static com.google.cloud.firestore.pipeline.expressions.Function.subtract;
import static com.google.common.truth.Truth.assertThat;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.LocalFirestoreHelper;
import com.google.cloud.firestore.PipelineResult;
import com.google.cloud.firestore.pipeline.expressions.Constant;
import com.google.cloud.firestore.pipeline.expressions.Field;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ITPipelineTest extends ITBaseTest {
  private CollectionReference collection;

  public CollectionReference testCollectionWithDocs(Map<String, Map<String, Object>> docs)
      throws ExecutionException, InterruptedException, TimeoutException {
    CollectionReference collection = firestore.collection(LocalFirestoreHelper.autoId());
    for (Map.Entry<String, Map<String, Object>> doc : docs.entrySet()) {
      collection.document(doc.getKey()).set(doc.getValue()).get(5, TimeUnit.SECONDS);
    }
    return collection;
  }

  List<Map<String, Object>> data(List<PipelineResult> results) {
    return results.stream().map(PipelineResult::getData).collect(Collectors.toList());
  }

  @Before
  public void setup() throws Exception {
    if (collection != null) {
      return;
    }

    Map<String, Map<String, Object>> bookDocs =
        map(
            "book1",
                map(
                    "title",
                    "The Hitchhiker's Guide to the Galaxy",
                    "author",
                    "Douglas Adams",
                    "genre",
                    "Science Fiction",
                    "published",
                    1979,
                    "rating",
                    4.2,
                    "tags",
                    Lists.newArrayList("comedy", "space", "adventure"),
                    "awards",
                    map("hugo", true, "nebula", false)),
            "book2",
                map(
                    "title",
                    "Pride and Prejudice",
                    "author",
                    "Jane Austen",
                    "genre",
                    "Romance",
                    "published",
                    1813,
                    "rating",
                    4.5,
                    "tags",
                    Lists.newArrayList("classic", "social commentary", "love"),
                    "awards",
                    map("none", true)),
            "book3",
                map(
                    "title",
                    "One Hundred Years of Solitude",
                    "author",
                    "Gabriel García Márquez",
                    "genre",
                    "Magical Realism",
                    "published",
                    1967,
                    "rating",
                    4.3,
                    "tags",
                    Lists.newArrayList("family", "history", "fantasy"),
                    "awards",
                    map("nobel", true, "nebula", false)),
            "book4",
                map(
                    "title",
                    "The Lord of the Rings",
                    "author",
                    "J.R.R. Tolkien",
                    "genre",
                    "Fantasy",
                    "published",
                    1954,
                    "rating",
                    4.7,
                    "tags",
                    Lists.newArrayList("adventure", "magic", "epic"),
                    "awards",
                    map("hugo", false, "nebula", false)),
            "book5",
                map(
                    "title",
                    "The Handmaid's Tale",
                    "author",
                    "Margaret Atwood",
                    "genre",
                    "Dystopian",
                    "published",
                    1985,
                    "rating",
                    4.1,
                    "tags",
                    Lists.newArrayList("feminism", "totalitarianism", "resistance"),
                    "awards",
                    map("arthur c. clarke", true, "booker prize", false)),
            "book6",
                map(
                    "title",
                    "Crime and Punishment",
                    "author",
                    "Fyodor Dostoevsky",
                    "genre",
                    "Psychological Thriller",
                    "published",
                    1866,
                    "rating",
                    4.3,
                    "tags",
                    Lists.newArrayList("philosophy", "crime", "redemption"),
                    "awards",
                    map("none", true)),
            "book7",
                map(
                    "title",
                    "To Kill a Mockingbird",
                    "author",
                    "Harper Lee",
                    "genre",
                    "Southern Gothic",
                    "published",
                    1960,
                    "rating",
                    4.2,
                    "tags",
                    Lists.newArrayList("racism", "injustice", "coming-of-age"),
                    "awards",
                    map("pulitzer", true)),
            "book8",
                map(
                    "title",
                    "1984",
                    "author",
                    "George Orwell",
                    "genre",
                    "Dystopian",
                    "published",
                    1949,
                    "rating",
                    4.2,
                    "tags",
                    Lists.newArrayList("surveillance", "totalitarianism", "propaganda"),
                    "awards",
                    map("prometheus", true)),
            "book9",
                map(
                    "title",
                    "The Great Gatsby",
                    "author",
                    "F. Scott Fitzgerald",
                    "genre",
                    "Modernist",
                    "published",
                    1925,
                    "rating",
                    4.0,
                    "tags",
                    Lists.newArrayList("wealth", "american dream", "love"),
                    "awards",
                    map("none", true)),
            "book10",
                map(
                    "title",
                    "Dune",
                    "author",
                    "Frank Herbert",
                    "genre",
                    "Science Fiction",
                    "published",
                    1965,
                    "rating",
                    4.6,
                    "tags",
                    Lists.newArrayList("politics", "desert", "ecology"),
                    "awards",
                    map("hugo", true, "nebula", true)));
    collection = testCollectionWithDocs(bookDocs);
  }

  @Test
  public void fromCollectionThenAggregate() throws Exception {
    List<PipelineResult> results =
        collection.toPipeline().aggregate(countAll().toField("count")).execute(firestore).get();
    assertThat(data(results)).isEqualTo(Lists.newArrayList(map("count", 10L)));

    results =
        collection
            .toPipeline()
            .filter(equal("genre", "Science Fiction"))
            .aggregate(
                countAll().toField("count"),
                avg("rating").toField("avg_rating"),
                Field.of("rating").max().toField("max_rating"))
            .execute(firestore)
            .get();
    assertThat(data(results))
        .isEqualTo(Lists.newArrayList(map("count", 2L, "avg_rating", 4.4, "max_rating", 4.6)));
  }

  @Test
  public void testMinMax() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .aggregate(
                countAll().toField("count"),
                Field.of("rating").max().toField("max_rating"),
                Field.of("published").min().toField("min_published"))
            .execute(firestore)
            .get();
    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map(
                    "count", 10L,
                    "max_rating", 4.7,
                    "min_published", 1813L)));
  }

  @Test
  public void selectSpecificFields() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select("title", "author")
            .sort(Field.of("author").ascending())
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("title", "The Hitchhiker's Guide to the Galaxy", "author", "Douglas Adams"),
                map("title", "The Great Gatsby", "author", "F. Scott Fitzgerald"),
                map("title", "Dune", "author", "Frank Herbert"),
                map("title", "Crime and Punishment", "author", "Fyodor Dostoevsky"),
                map("title", "One Hundred Years of Solitude", "author", "Gabriel García Márquez"),
                map("title", "1984", "author", "George Orwell"),
                map("title", "To Kill a Mockingbird", "author", "Harper Lee"),
                map("title", "The Lord of the Rings", "author", "J.R.R. Tolkien"),
                map("title", "Pride and Prejudice", "author", "Jane Austen"),
                map("title", "The Handmaid's Tale", "author", "Margaret Atwood")));
  }

  @Test
  public void filterByMultipleConditions() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .filter(and(greaterThan("rating", 4.5), equal("genre", "Science Fiction")))
            .execute(firestore)
            .get();

    // It's Dune
    assertThat(data(results))
        .isEqualTo(Lists.newArrayList(collection.document("book10").get().get().getData()));
    assertThat(results.get(0).getReference()).isEqualTo(collection.document("book10"));
  }

  @Test
  public void filterByOrCondition() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .filter(or(equal("genre", "Romance"), equal("genre", "Dystopian")))
            .select("title")
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("title", "Pride and Prejudice"),
                map("title", "The Handmaid's Tale"),
                map("title", "1984")));
  }

  @Test
  public void testPipelineWithOffsetAndLimit() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .sort(Field.of("author").ascending())
            .offset(5)
            .limit(3)
            .select("title", "author")
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("title", "1984", "author", "George Orwell"),
                map("title", "To Kill a Mockingbird", "author", "Harper Lee"),
                map("title", "The Lord of the Rings", "author", "J.R.R. Tolkien")));
  }

  @Test
  public void testArrayContains() throws Exception {
    List<PipelineResult> results =
        collection.toPipeline().filter(arrayContains("tags", "comedy")).execute(firestore).get();
    assertThat(data(results))
        // The Hitchhiker's Guide to the Galaxy
        .isEqualTo(Lists.newArrayList(collection.document("book1").get().get().getData()));
  }

  @Test
  public void testArrayContainsAny() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .filter(arrayContainsAny("tags", "comedy", "classic"))
            .select("title")
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("title", "The Hitchhiker's Guide to the Galaxy"),
                map("title", "Pride and Prejudice")));
  }

  @Test
  public void testArrayContainsAll() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .filter(arrayContainsAll("tags", "adventure", "magic"))
            .execute(firestore)
            .get();

    assertThat(data(results)).isEqualTo(Lists.newArrayList(map("title", "The Lord of the Rings")));
  }

  @Test
  public void testArrayLength() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(Field.of("tags").arrayLength().asAlias("tagsCount"))
            .filter(equal("tagsCount", 3))
            .execute(firestore)
            .get();

    // All documents have 3 tags in the test dataset
    assertThat(data(results)).hasSize(10);
  }

  @Test
  public void testArrayConcat() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(Field.of("tags").arrayConcat("newTag1", "newTag2").asAlias("modifiedTags"))
            .limit(1)
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map(
                    "modifiedTags",
                    Lists.newArrayList("comedy", "space", "adventure", "newTag1", "newTag2"))));
  }

  @Test
  public void testArrayFilter() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(
                arrayFilter(Field.of("tags"), equal(arrayElement(), "")).asAlias("filteredTags"))
            .limit(1)
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("filteredTags", Lists.newArrayList("comedy", "space", "adventure"))));
  }

  @Test
  public void testArrayTransform() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(
                arrayTransform(Field.of("tags"), strConcat(arrayElement(), "transformed"))
                    .asAlias("transformedTags"))
            .limit(1)
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map(
                    "transformedTags",
                    Lists.newArrayList(
                        "comedytransformed", "spacetransformed", "adventuretransformed"))));
  }

  @Test
  public void testStrConcat() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(strConcat(Field.of("author"), " - ", Field.of("title")).asAlias("bookInfo"))
            .limit(1)
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("bookInfo", "Douglas Adams - The Hitchhiker's Guide to the Galaxy")));
  }

  @Test
  public void testLength() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(Field.of("title").length().asAlias("titleLength"), Field.of("title"))
            .filter(greaterThan("titleLength", 20))
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(Lists.newArrayList(map("titleLength", 32L), map("titleLength", 27L)));
  }

  @Test
  public void testToLowercase() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(Field.of("title").toLowercase().asAlias("lowercaseTitle"))
            .limit(1)
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(map("lowercaseTitle", "the hitchhiker's guide to the galaxy")));
  }

  @Test
  public void testToUppercase() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(Field.of("author").toUppercase().asAlias("uppercaseAuthor"))
            .limit(1)
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(Lists.newArrayList(map("uppercaseAuthor", "DOUGLAS ADAMS")));
  }

  @Test
  public void testTrim() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .addFields(
                strConcat(Constant.of(" "), Field.of("title"), Constant.of(" "))
                    .asAlias("spacedTitle"))
            .select(Field.of("spacedTitle").trim().asAlias("trimmedTitle"), Field.of("spacedTitle"))
            .limit(1)
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map(
                    "spacedTitle", " The Hitchhiker's Guide to the Galaxy ",
                    "trimmedTitle", "The Hitchhiker's Guide to the Galaxy")));
  }

  @Test
  public void testLike() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .filter(Field.of("title").like("%Guide%"))
            .select("title")
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(Lists.newArrayList(map("title", "The Hitchhiker's Guide to the Galaxy")));
  }

  @Test
  public void testRegexContains() throws Exception {
    // Find titles that contain either "the" or "of" (case-insensitive)
    List<PipelineResult> results =
        collection
            .toPipeline()
            .filter(Field.of("title").regexContains(".*(?i)(the|of).*"))
            .execute(firestore)
            .get();

    assertThat(data(results)).hasSize(5);
  }

  @Test
  public void testArithmeticOperations() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(
                add(Field.of("rating"), 1).asAlias("ratingPlusOne"),
                subtract(Field.of("published"), 1900).asAlias("yearsSince1900"),
                Field.of("rating").multiply(10).asAlias("ratingTimesTen"),
                Field.of("rating").divide(2).asAlias("ratingDividedByTwo"))
            .limit(1)
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map(
                    "ratingPlusOne",
                    5.2,
                    "yearsSince1900",
                    79L,
                    "ratingTimesTen",
                    42.0,
                    "ratingDividedByTwo",
                    2.1)));
  }

  @Test
  public void testComparisonOperators() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .filter(
                and(
                    greaterThan("rating", 4.2),
                    Field.of("rating").lessThanOrEqual(4.5),
                    notEqual("genre", "Science Fiction")))
            .select("rating", "title")
            .sort(Field.of("title").ascending())
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("rating", 4.3, "title", "Crime and Punishment"),
                map("rating", 4.3, "title", "One Hundred Years of Solitude"),
                map("rating", 4.5, "title", "Pride and Prejudice")));
  }

  @Test
  public void testLogicalOperators() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .filter(
                or(
                    and(greaterThan("rating", 4.5), equal("genre", "Science Fiction")),
                    lessThan("published", 1900)))
            .select("title")
            .sort(Field.of("title").ascending())
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("title", "Crime and Punishment"),
                map("title", "Dune"),
                map("title", "Pride and Prejudice")));
  }

  @Test
  public void testChecks() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .filter(not(Field.of("rating").isNaN())) // Filter out any documents with NaN rating
            .select(
                isNull("rating").asAlias("ratingIsNull"),
                not(Field.of("rating").isNaN()).asAlias("ratingIsNotNaN"))
            .limit(1)
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(Lists.newArrayList(map("ratingIsNull", false, "ratingIsNotNaN", true)));
  }

  @Test
  public void testMapGet() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(Field.of("awards").mapGet("hugo").asAlias("hugoAward"), Field.of("title"))
            .filter(equal("hugoAward", true))
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("hugoAward", true, "title", "The Hitchhiker's Guide to the Galaxy"),
                map("hugoAward", true, "title", "Dune")));
  }

  @Test
  public void testParent() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(
                parent(collection.document("chile").collection("subCollection").getPath())
                    .asAlias("parent"))
            .limit(1)
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(map("parent", "projects/projectId/databases/(default)/documents")));
  }

  @Test
  public void testCollectionId() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(collectionId(collection.document("chile")).asAlias("collectionId"))
            .limit(1)
            .execute(firestore)
            .get();

    assertThat(data(results)).isEqualTo(Lists.newArrayList(map("collectionId", "books")));
  }

  @Test
  public void testDistanceFunctions() throws Exception {
    double[] sourceVector = {0.1, 0.1};
    double[] targetVector = {0.5, 0.8};
    List<PipelineResult> results =
        collection
            .toPipeline()
            .select(
                cosineDistance(Constant.ofVector(sourceVector), targetVector)
                    .asAlias("cosineDistance"),
                dotProductDistance(Constant.ofVector(sourceVector), targetVector)
                    .asAlias("dotProductDistance"),
                euclideanDistance(Constant.ofVector(sourceVector), targetVector)
                    .asAlias("euclideanDistance"))
            .limit(1)
            .execute(firestore)
            .get();
  }

  @Test
  public void testNestedFields() throws Exception {
    List<PipelineResult> results =
        collection
            .toPipeline()
            .filter(equal("awards.hugo", true))
            .select("title", "awards.hugo")
            .execute(firestore)
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("title", "The Hitchhiker's Guide to the Galaxy", "awards.hugo", true),
                map("title", "Dune", "awards.hugo", true)));
  }
}
