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
import static com.google.cloud.firestore.pipeline.expressions.Function.avg;
import static com.google.cloud.firestore.pipeline.expressions.Function.cosineDistance;
import static com.google.cloud.firestore.pipeline.expressions.Function.countAll;
import static com.google.cloud.firestore.pipeline.expressions.Function.endsWith;
import static com.google.cloud.firestore.pipeline.expressions.Function.eq;
import static com.google.cloud.firestore.pipeline.expressions.Function.euclideanDistance;
import static com.google.cloud.firestore.pipeline.expressions.Function.gt;
import static com.google.cloud.firestore.pipeline.expressions.Function.logicalMax;
import static com.google.cloud.firestore.pipeline.expressions.Function.logicalMin;
import static com.google.cloud.firestore.pipeline.expressions.Function.lt;
import static com.google.cloud.firestore.pipeline.expressions.Function.neq;
import static com.google.cloud.firestore.pipeline.expressions.Function.not;
import static com.google.cloud.firestore.pipeline.expressions.Function.or;
import static com.google.cloud.firestore.pipeline.expressions.Function.startsWith;
import static com.google.cloud.firestore.pipeline.expressions.Function.strConcat;
import static com.google.cloud.firestore.pipeline.expressions.Function.subtract;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.LocalFirestoreHelper;
import com.google.cloud.firestore.Pipeline;
import com.google.cloud.firestore.PipelineResult;
import com.google.cloud.firestore.pipeline.expressions.Constant;
import com.google.cloud.firestore.pipeline.expressions.Field;
import com.google.cloud.firestore.pipeline.expressions.Function;
import com.google.cloud.firestore.pipeline.stages.Aggregate;
import com.google.cloud.firestore.pipeline.stages.SampleOptions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.HashMap;
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
  private Map<String, Map<String, Object>> bookDocs;

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

    bookDocs = ImmutableMap.of(
        "book1",
            ImmutableMap.of(
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
                ImmutableList.of("comedy", "space", "adventure"),
                "awards",
                ImmutableMap.of("hugo", true, "nebula", false)),
        "book2",
            ImmutableMap.of(
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
                ImmutableList.of("classic", "social commentary", "love"),
                "awards",
                ImmutableMap.of("none", true)),
        "book3",
            ImmutableMap.of(
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
                ImmutableList.of("family", "history", "fantasy"),
                "awards",
                ImmutableMap.of("nobel", true, "nebula", false)),
        "book4",
            ImmutableMap.of(
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
                ImmutableList.of("adventure", "magic", "epic"),
                "awards",
                ImmutableMap.of("hugo", false, "nebula", false)),
        "book5",
            ImmutableMap.of(
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
                ImmutableList.of("feminism", "totalitarianism", "resistance"),
                "awards",
                ImmutableMap.of("arthur c. clarke", true, "booker prize", false)),
        "book6",
            ImmutableMap.of(
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
                ImmutableList.of("philosophy", "crime", "redemption"),
                "awards",
                ImmutableMap.of("none", true)),
        "book7",
            ImmutableMap.of(
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
                ImmutableList.of("racism", "injustice", "coming-of-age"),
                "awards",
                ImmutableMap.of("pulitzer", true)),
        "book8",
            ImmutableMap.of(
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
                ImmutableList.of("surveillance", "totalitarianism", "propaganda"),
                "awards",
                ImmutableMap.of("prometheus", true)),
        "book9",
            ImmutableMap.of(
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
                ImmutableList.of("wealth", "american dream", "love"),
                "awards",
                ImmutableMap.of("none", true)),
        "book10",
            ImmutableMap.of(
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
                ImmutableList.of("politics", "desert", "ecology"),
                "awards",
                ImmutableMap.of("hugo", true, "nebula", true)));
    collection = testCollectionWithDocs(bookDocs);
  }

  @Test
  public void testAggregates() throws Exception {
    List<PipelineResult> results =
        firestore
            .pipeline()
            .collection(collection.getPath())
            .aggregate(countAll().as("count"))
            .execute()
            .get();
    assertThat(data(results)).isEqualTo(Lists.newArrayList(map("count", 10L)));

    results =
        collection
            .pipeline()
            .where(eq("genre", "Science Fiction"))
            .aggregate(
                countAll().as("count"),
                avg("rating").as("avg_rating"),
                Field.of("rating").max().as("max_rating"))
            .execute()
            .get();
    assertThat(data(results))
        .isEqualTo(Lists.newArrayList(map("count", 2L, "avg_rating", 4.4, "max_rating", 4.6)));
  }

  @Test
  public void testGroupBysWithoutAccumulators() throws Exception {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          collection
              .pipeline()
              .where(lt("published", 1900))
              .aggregate(Aggregate.withAccumulators().withGroups("genre"));
        });
  }

  @Test
  public void testDistinct() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .where(lt("published", 1900))
            .distinct(Field.of("genre").toLower().as("lower_genre"))
            .execute()
            .get();
    assertThat(data(results))
        .containsExactly(
            map("lower_genre", "romance"), map("lower_genre", "psychological thriller"));
  }

  @Test
  public void testGroupBysAndAggregate() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .where(lt("published", 1984))
            .aggregate(
                Aggregate.withAccumulators(avg("rating").as("avg_rating")).withGroups("genre"))
            .where(gt("avg_rating", 4.3))
            .execute()
            .get();
    assertThat(data(results))
        .containsExactly(
            map("avg_rating", 4.7, "genre", "Fantasy"),
            map("avg_rating", 4.5, "genre", "Romance"),
            map("avg_rating", 4.4, "genre", "Science Fiction"));
  }

  @Test
  public void testMinMax() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .aggregate(
                countAll().as("count"),
                Field.of("rating").max().as("max_rating"),
                Field.of("published").min().as("min_published"))
            .execute()
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
        firestore
            .pipeline()
            .collection(collection.getPath())
            .select("title", "author")
            .sort(Field.of("author").ascending())
            .execute()
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
  public void addAndRemoveFields() throws Exception {
    List<PipelineResult> results =
        firestore
            .pipeline()
            .collection(collection.getPath())
            .addFields(
                strConcat(Field.of("author"), "_", Field.of("title")).as("author_title"),
                strConcat(Field.of("title"), "_", Field.of("author")).as("title_author"))
            .removeFields("title_author", "tags", "awards", "rating", "title")
            .removeFields(Field.of("published"), Field.of("genre"), Field.of("nestedField"))
            .sort(Field.of("author_title").ascending())
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map(
                    "author_title",
                    "Douglas Adams_The Hitchhiker's Guide to the Galaxy",
                    "author",
                    "Douglas Adams"),
                map(
                    "author_title",
                    "F. Scott Fitzgerald_The Great Gatsby",
                    "author",
                    "F. Scott Fitzgerald"),
                map("author_title", "Frank Herbert_Dune", "author", "Frank Herbert"),
                map(
                    "author_title",
                    "Fyodor Dostoevsky_Crime and Punishment",
                    "author",
                    "Fyodor Dostoevsky"),
                map(
                    "author_title",
                    "Gabriel García Márquez_One Hundred Years of Solitude",
                    "author",
                    "Gabriel García Márquez"),
                map("author_title", "George Orwell_1984", "author", "George Orwell"),
                map("author_title", "Harper Lee_To Kill a Mockingbird", "author", "Harper Lee"),
                map(
                    "author_title",
                    "J.R.R. Tolkien_The Lord of the Rings",
                    "author",
                    "J.R.R. Tolkien"),
                map("author_title", "Jane Austen_Pride and Prejudice", "author", "Jane Austen"),
                map(
                    "author_title",
                    "Margaret Atwood_The Handmaid's Tale",
                    "author",
                    "Margaret Atwood")));
  }

  @Test
  public void whereByMultipleConditions() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .where(and(gt("rating", 4.5), eq("genre", "Science Fiction")))
            .execute()
            .get();

    // It's Dune
    assertThat(data(results))
        .isEqualTo(Lists.newArrayList(collection.document("book10").get().get().getData()));
    assertThat(results.get(0).getReference()).isEqualTo(collection.document("book10"));
  }

  @Test
  public void whereByOrCondition() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .where(or(eq("genre", "Romance"), eq("genre", "Dystopian")))
            .select("title")
            .execute()
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
        firestore
            .pipeline()
            .collection(collection.getPath())
            .sort(Field.of("author").ascending())
            .offset(5)
            .limit(3)
            .select("title", "author")
            .execute()
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
        collection.pipeline().where(arrayContains("tags", "comedy")).execute().get();
    assertThat(data(results))
        // The Hitchhiker's Guide to the Galaxy
        .isEqualTo(Lists.newArrayList(collection.document("book1").get().get().getData()));
  }

  @Test
  public void testArrayContainsAny() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .where(arrayContainsAny("tags", Lists.newArrayList("comedy", "classic")))
            .select("title")
            .execute()
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
            .pipeline()
            .where(arrayContainsAll("tags", Lists.newArrayList("adventure", "magic")))
            .select("title")
            .execute()
            .get();

    assertThat(data(results)).isEqualTo(Lists.newArrayList(map("title", "The Lord of the Rings")));
  }

  @Test
  public void testArrayLength() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .select(Field.of("tags").arrayLength().as("tagsCount"))
            .where(eq("tagsCount", 3))
            .execute()
            .get();

    // All documents have 3 tags in the test dataset
    assertThat(data(results)).hasSize(10);
  }

  @Test
  public void testArrayConcat() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .select(
                Field.of("tags")
                    .arrayConcat(Lists.newArrayList("newTag1", "newTag2"))
                    .as("modifiedTags"))
            .limit(1)
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map(
                    "modifiedTags",
                    Lists.newArrayList("comedy", "space", "adventure", "newTag1", "newTag2"))));
  }

  // @Test
  // public void testArrayFilter() throws Exception {
  //   List<PipelineResult> results =
  //       collection
  //           .pipeline()
  //           .select(
  //               arrayFilter(Field.of("tags"), Function.eq(arrayElement(), "comedy"))
  //                   .as("filteredTags"))
  //           .limit(1)
  //           .execute()
  //           .get();
  //
  //   assertThat(data(results))
  //       .isEqualTo(Lists.newArrayList(map("filteredTags", Lists.newArrayList("comedy"))));
  // }

  // @Test
  // public void testArrayTransform() throws Exception {
  //   List<PipelineResult> results =
  //       collection
  //           .pipeline()
  //           .select(
  //               arrayTransform(Field.of("tags"), strConcat(arrayElement(), "transformed"))
  //                   .as("transformedTags"))
  //           .limit(1)
  //           .execute()
  //           .get();
  //
  //   assertThat(data(results))
  //       .isEqualTo(
  //           Lists.newArrayList(
  //               map(
  //                   "transformedTags",
  //                   Lists.newArrayList(
  //                       "comedytransformed", "spacetransformed", "adventuretransformed"))));
  // }

  @Test
  public void testStrConcat() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .select(strConcat(Field.of("author"), " - ", Field.of("title")).as("bookInfo"))
            .limit(1)
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("bookInfo", "Douglas Adams - The Hitchhiker's Guide to the Galaxy")));
  }

  @Test
  public void testStartsWith() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .where(startsWith("title", "The"))
            .select("title")
            .sort(Field.of("title").ascending())
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("title", "The Great Gatsby"),
                map("title", "The Handmaid's Tale"),
                map("title", "The Hitchhiker's Guide to the Galaxy"),
                map("title", "The Lord of the Rings")));
  }

  @Test
  public void testEndsWith() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .where(endsWith(Field.of("title"), Constant.of("y")))
            .select("title")
            .sort(Field.of("title").descending())
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("title", "The Hitchhiker's Guide to the Galaxy"),
                map("title", "The Great Gatsby")));
  }

  @Test
  public void testLength() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .select(Field.of("title").charLength().as("titleLength"), Field.of("title"))
            .where(gt("titleLength", 20))
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(Lists.newArrayList(map("titleLength", 32L), map("titleLength", 27L)));
  }

  @Test
  public void testStringFunctions() throws Exception {
    List<PipelineResult> results;

    // Reverse
    results =
        firestore
            .pipeline()
            .collection(collection.getPath())
            .select(Field.of("title").reverse().as("reversed_title"))
            .where(Field.of("author").eq("Douglas Adams"))
            .execute()
            .get();
    assertThat(data(results).get(0).get("reversed_title"))
        .isEqualTo("yxalaG ot ediug s'reknhiHcH ehT");

    // ReplaceFirst
    results =
        collection
            .pipeline()
            .select(Field.of("title").replaceFirst("The", "A").as("replaced_title"))
            .where(Field.of("author").eq("Douglas Adams"))
            .execute()
            .get();
    assertThat(data(results).get(0).get("replaced_title"))
        .isEqualTo("A Hitchhiker's Guide to the Galaxy");

    // ReplaceAll
    results =
        collection
            .pipeline()
            .select(Field.of("title").replaceAll(" ", "_").as("replaced_title"))
            .where(Field.of("author").eq("Douglas Adams"))
            .execute()
            .get();
    assertThat(data(results).get(0).get("replaced_title"))
        .isEqualTo("The_Hitchhiker's_Guide_to_the_Galaxy");

    // CharLength
    results =
        collection
            .pipeline()
            .select(Field.of("title").charLength().as("title_length"))
            .where(Field.of("author").eq("Douglas Adams"))
            .execute()
            .get();
    assertThat(data(results).get(0).get("title_length")).isEqualTo(30L);

    // ByteLength
    results =
        collection
            .pipeline()
            .select(Field.of("title").strConcat("_银河系漫游指南").byteLength().as("title_byte_length"))
            .where(Field.of("author").eq("Douglas Adams"))
            .execute()
            .get();
    assertThat(data(results).get(0).get("title_byte_length"))
        .isEqualTo(30L); // Assuming UTF-8 encoding where each character is 1 byte
  }

  @Test
  public void testToLowercase() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .select(Field.of("title").toLower().as("lowercaseTitle"))
            .limit(1)
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(map("lowercaseTitle", "the hitchhiker's guide to the galaxy")));
  }

  @Test
  public void testToUppercase() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .select(Field.of("author").toUpper().as("uppercaseAuthor"))
            .limit(1)
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(Lists.newArrayList(map("uppercaseAuthor", "DOUGLAS ADAMS")));
  }

  @Test
  public void testTrim() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .addFields(
                strConcat(Constant.of(" "), Field.of("title"), Constant.of(" ")).as("spacedTitle"))
            .select(Field.of("spacedTitle").trim().as("trimmedTitle"), Field.of("spacedTitle"))
            .limit(1)
            .execute()
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
            .pipeline()
            .where(Field.of("title").like("%Guide%"))
            .select("title")
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(Lists.newArrayList(map("title", "The Hitchhiker's Guide to the Galaxy")));
  }

  @Test
  public void testRegexContains() throws Exception {
    // Find titles that contain either "the" or "of" (case-insensitive)
    List<PipelineResult> results =
        collection
            .pipeline()
            .where(Field.of("title").regexContains("(?i)(the|of)"))
            .execute()
            .get();

    assertThat(data(results)).hasSize(5);
  }

  @Test
  public void testRegexMatches() throws Exception {
    // Find titles that contain either "the" or "of" (case-insensitive)
    List<PipelineResult> results =
        collection
            .pipeline()
            .where(Function.regexMatch("title", ".*(?i)(the|of).*"))
            .execute()
            .get();

    assertThat(data(results)).hasSize(5);
  }

  @Test
  public void testArithmeticOperations() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .select(
                add(Field.of("rating"), 1).as("ratingPlusOne"),
                subtract(Field.of("published"), 1900).as("yearsSince1900"),
                Field.of("rating").multiply(10).as("ratingTimesTen"),
                Field.of("rating").divide(2).as("ratingDividedByTwo"))
            .limit(1)
            .execute()
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
            .pipeline()
            .where(
                and(
                    gt("rating", 4.2),
                    Field.of("rating").lte(4.5),
                    neq("genre", "Science Fiction")))
            .select("rating", "title")
            .sort(Field.of("title").ascending())
            .execute()
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
            .pipeline()
            .where(
                or(and(gt("rating", 4.5), eq("genre", "Science Fiction")), lt("published", 1900)))
            .select("title")
            .sort(Field.of("title").ascending())
            .execute()
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
        firestore
            .pipeline()
            .collection(collection.getPath())
            .where(not(Field.of("rating").isNaN())) // Filter out any documents with NaN rating
            .select(
                eq("rating", null).as("ratingIsNull"),
                not(Field.of("rating").isNaN()).as("ratingIsNotNaN"))
            .limit(1)
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(Lists.newArrayList(map("ratingIsNull", false, "ratingIsNotNaN", true)));
  }

  // @Test
  // public void testBitwiseOperations() throws Exception {
  //   List<PipelineResult> results;
  //
  //   // Bitwise AND
  //   results =
  //       collection
  //           .pipeline()
  //           .where(Field.of("author").eq("Douglas Adams"))
  //           .select(
  //               Field.of("published").bitAnd(0xFF).as("published_masked"),
  //               Function.bitAnd(Field.of("published"), 0xFF).as("published_masked_func"))
  //           .execute()
  //           .get();
  //   assertThat(data(results))
  //       .containsExactly(
  //           map("published_masked", 1979 & 0xFF, "published_masked_func", 1979 & 0xFF));
  //
  //   // Bitwise OR
  //   results =
  //       collection
  //           .pipeline()
  //           .where(Field.of("author").eq("Douglas Adams"))
  //           .select(
  //               Field.of("published").bitOr(0x100).as("published_ored"),
  //               Function.bitOr(Field.of("published"), 0x100).as("published_ored_func"))
  //           .execute()
  //           .get();
  //   assertThat(data(results))
  //       .containsExactly(map("published_ored", 1979 | 0x100, "published_ored_func", 1979 |
  // 0x100));
  //
  //   // Bitwise XOR
  //   results =
  //       collection
  //           .pipeline()
  //           .where(Field.of("author").eq("Douglas Adams"))
  //           .select(
  //               Field.of("published").bitXor(0x100).as("published_xored"),
  //               Function.bitXor(Field.of("published"), 0x100).as("published_xored_func"))
  //           .execute()
  //           .get();
  //   assertThat(data(results))
  //       .containsExactly(
  //           map("published_xored", 1979 ^ 0x100, "published_xored_func", 1979 ^ 0x100));
  //
  //   // Bitwise NOT
  //   results =
  //       collection
  //           .pipeline()
  //           .where(Field.of("author").eq("Douglas Adams"))
  //           .select(
  //               Field.of("published").bitNot().as("published_not"),
  //               Function.bitNot(Field.of("published")).as("published_not_func"))
  //           .execute()
  //           .get();
  //   assertThat(data(results))
  //       .containsExactly(map("published_not", ~1979, "published_not_func", ~1979));
  //
  //   // Bitwise Left Shift
  //   results =
  //       collection
  //           .pipeline()
  //           .where(Field.of("author").eq("Douglas Adams"))
  //           .select(
  //               Field.of("published").bitLeftShift(2).as("published_shifted_left"),
  //               Function.bitLeftShift(Field.of("published"),
  // 2).as("published_shifted_left_func"))
  //           .execute()
  //           .get();
  //   assertThat(data(results))
  //       .containsExactly(
  //           map("published_shifted_left", 1979 << 2, "published_shifted_left_func", 1979 << 2));
  //
  //   // Bitwise Right Shift
  //   results =
  //       collection
  //           .pipeline()
  //           .where(Field.of("author").eq("Douglas Adams"))
  //           .select(
  //               Field.of("published").bitRightShift(2).as("published_shifted_right"),
  //               Function.bitRightShift(Field.of("published"),
  // 2).as("published_shifted_right_func"))
  //           .execute()
  //           .get();
  //   assertThat(data(results))
  //       .containsExactly(
  //           map("published_shifted_right", 1979 >> 2, "published_shifted_right_func", 1979 >>
  // 2));
  // }

  @Test
  public void testLogicalMinMax() throws Exception {
    List<PipelineResult> results;

    // logicalMax
    results =
        collection
            .pipeline()
            .where(Field.of("author").eq("Douglas Adams"))
            .select(
                Field.of("rating").logicalMax(4.5).as("max_rating"),
                logicalMax(Field.of("published"), 1900).as("max_published"))
            .execute()
            .get();
    assertThat(data(results)).containsExactly(map("max_rating", 4.5, "max_published", 1979L));

    // logicalMin
    results =
        collection
            .pipeline()
            .select(
                Field.of("rating").logicalMin(4.5).as("min_rating"),
                logicalMin(Field.of("published"), 1900).as("min_published"))
            .execute()
            .get();
    assertThat(data(results)).containsExactly(map("min_rating", 4.2, "min_published", 1900L));
  }

  @Test
  public void testMapGet() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .select(Field.of("awards").mapGet("hugo").as("hugoAward"), Field.of("title"))
            .where(eq("hugoAward", true))
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("hugoAward", true, "title", "The Hitchhiker's Guide to the Galaxy"),
                map("hugoAward", true, "title", "Dune")));
  }

  @Test
  public void testDistanceFunctions() throws Exception {
    double[] sourceVector = {0.1, 0.1};
    double[] targetVector = {0.5, 0.8};
    List<PipelineResult> results =
        firestore
            .pipeline()
            .collection(collection.getPath())
            .select(
                cosineDistance(Constant.vector(sourceVector), targetVector).as("cosineDistance"),
                Function.dotProduct(Constant.vector(sourceVector), targetVector)
                    .as("dotProductDistance"),
                euclideanDistance(Constant.vector(sourceVector), targetVector)
                    .as("euclideanDistance"))
            .limit(1)
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map(
                    "cosineDistance", 0.02560880430538015,
                    "dotProductDistance", 0.13,
                    "euclideanDistance", 0.806225774829855)));
  }

  @Test
  public void testNestedFields() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .where(eq("awards.hugo", true))
            .select("title", "awards.hugo")
            .execute()
            .get();

    assertThat(data(results))
        .isEqualTo(
            Lists.newArrayList(
                map("title", "The Hitchhiker's Guide to the Galaxy", "awards.hugo", true),
                map("title", "Dune", "awards.hugo", true)));
  }

  @Test
  public void testPipelineInTransactions() throws Exception {
    Pipeline pipeline =
        collection
            .pipeline()
            .where(eq("awards.hugo", true))
            .select("title", "awards.hugo", Field.DOCUMENT_ID);

    firestore
        .runTransaction(
            transaction -> {
              List<PipelineResult> results = transaction.execute(pipeline).get();

              assertThat(data(results))
                  .isEqualTo(
                      Lists.newArrayList(
                          map("title", "The Hitchhiker's Guide to the Galaxy", "awards.hugo", true),
                          map("title", "Dune", "awards.hugo", true)));

              transaction.update(collection.document("book1"), map("foo", "bar"));

              return "done";
            })
        .get();

    List<PipelineResult> result =
        collection.pipeline().where(eq("foo", "bar")).select("title").execute().get();
    assertThat(data(result))
        .isEqualTo(Lists.newArrayList(map("title", "The Hitchhiker's Guide to the Galaxy")));
  }

  @Test
  public void testReplace() throws Exception {
    List<PipelineResult> results =
        collection.pipeline()
            .replace("awards")
            .execute()
            .get();

    List<HashMap<String, Object>> list = bookDocs.values().stream()
        .map(book -> {
          HashMap<String, Object> awards = (HashMap<String, Object>) book.get("awards");
          HashMap<String, Object> map = Maps.newHashMap(book);
          // Remove "awards" field.
          map.remove("awards");
          // Life nested "awards".
          map.putAll(awards);
          return map;
        })
        .collect(Collectors.toList());

    assertThat(data(results))
        .containsExactly(list);
  }

  @Test
  public void testSampleLimit() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .sample(3)
            .execute()
            .get();

    assertThat(results).hasSize(3);
  }

  @Test
  public void testSamplePercentage() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .sample(SampleOptions.percentage(0.6))
            .execute()
            .get();

    assertThat(results).hasSize(6);
  }

  @Test
  public void testUnion() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .union(collection.pipeline())
            .execute()
            .get();

    assertThat(results).hasSize(20);
  }

  @Test
  public void testUnnest() throws Exception {
    List<PipelineResult> results =
        collection
            .pipeline()
            .where(eq(Field.of("title"), "The Hitchhiker's Guide to the Galaxy"))
            .unnest("tags")
            .execute()
            .get();

    assertThat(results).hasSize(3);
  }
}
