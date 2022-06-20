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

package com.google.cloud.firestore;

import static com.google.cloud.firestore.AggregateField.count;
import static com.google.cloud.firestore.AggregateField.last;
import static com.google.cloud.firestore.AggregateField.max;
import static com.google.cloud.firestore.AggregateField.min;
import static com.google.cloud.firestore.FieldPath.documentId;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AggregateDemo {

  public static void Demo1A_CountOfDocumentsInACollection(Firestore db) throws Exception {
    Query query = db.collection("games").document("halo").collection("players");
    AggregateSnapshot snapshot = query.aggregate(count()).get().get();
    assertEqual(snapshot.get(count()), 5_000_000);
  }

  public static void Demo1B_LimitNumberOfDocumentsScannedWithLimit(Firestore db) throws Exception {
    // Limit the work / documents scanned by restricting underlying query.
    Query query = db.collection("games").document("halo").collection("players").limit(1000);
    AggregateSnapshot snapshot = query.aggregate(count()).get().get();
    assertEqual(snapshot.get(count()), 1000);
  }

  public static void Demo1C_LimitNumberOfDocumentsScannedWithUpTo(Firestore db) throws Exception {
    // Limit the work / documents scanned by specifying upTo on the aggregation.
    Query query = db.collection("games").document("halo").collection("players");
    AggregateSnapshot snapshot = query.aggregate(count().upTo(1000)).get().get();
    assertEqual(snapshot.get(count()), 1000);
  }

  public static void Demo2_GroupBySupport(Firestore db) throws Exception {
    Query query = db.collectionGroup("players").whereEqualTo("state", "active");
    GroupByQuerySnapshot snapshot = query.groupBy("game").aggregate(count()).get().get();
    assertEqual(snapshot.size(), 3);
    List<GroupSnapshot> groups = snapshot.getGroups();
    assertEqual(groups.get(0).getString("game"), "cyber_punk");
    assertEqual(groups.get(0).get(count()), 5);
    assertEqual(groups.get(1).getString("game"), "halo");
    assertEqual(groups.get(1).get(count()), 55);
    assertEqual(groups.get(2).getString("game"), "mine_craft");
    assertEqual(groups.get(2).get(count()), 5_000_000);
  }

  public static void Demo3_FieldRenamingAliasing() {
    // Aliasing / renaming of aggregations is not exposed from the API surface.
    // I've requested that the proto allow non-aggregate fields to also be
    // aliased so that the implementation of the Firestore clients can rename
    // both aggregate and non-aggregate fields to guarantee that there is NEVER
    // a conflict.
  }

  public static void Demo4_LimitTheNumberOfDocumentsScanned(Firestore db) throws Exception {
    // This is a duplicate of Demo1B_LimitNumberOfDocumentsScannedWithLimit.
  }

  public static void Demo5_LimitAggregationBuckets(Firestore db) throws Exception {
    Query query = db.collectionGroup("players");
    GroupByQuerySnapshot snapshot =
        query.groupBy("game").groupLimit(1).groupOffset(1).aggregate(count()).get().get();
    assertEqual(snapshot.size(), 1);
    GroupSnapshot aggregateSnapshot = snapshot.getGroups().get(0);
    assertEqual(aggregateSnapshot.getString("game"), "halo");
    assertEqual(aggregateSnapshot.get(count()), 55);
  }

  public static void Demo6_LimitWorkPerAggregationBucket(Firestore db) throws Exception {
    Query query = db.collection("games").document("halo").collection("players");
    GroupByQuerySnapshot snapshot = query.groupBy("game").aggregate(count().upTo(50)).get().get();
    assertEqual(snapshot.size(), 3);
    List<GroupSnapshot> groups = snapshot.getGroups();
    assertEqual(groups.get(0).getString("game"), "cyber_punk");
    assertEqual(groups.get(0).get(count()), 5);
    assertEqual(groups.get(1).getString("game"), "halo");
    assertEqual(groups.get(1).get(count()), 50); // count is capped at 50
    assertEqual(groups.get(2).getString("game"), "mine_craft");
    assertEqual(groups.get(2).get(count()), 50); // count is capped at 50
  }

  public static void Demo7_OffsetOnNonGroupByQuery() {
    // The API does not provide a way to specify an offset for a non-group-by query.
  }

  public static void Demo8_PaginationOverAggregationBuckets(Firestore db) throws Exception {
    Query query = db.collectionGroup("players").whereEqualTo("state", "active");
    // .orderBy("game") is implied by the group by
    GroupByQuerySnapshot snapshot =
        query.groupBy("game").groupStartAfter("cyber_punk").aggregate(count()).get().get();
    assertEqual(snapshot.size(), 2);
    List<GroupSnapshot> groups = snapshot.getGroups();
    assertEqual(groups.get(0).getString("game"), "halo");
    assertEqual(groups.get(0).get(count()), 55);
    assertEqual(groups.get(1).getString("game"), "mine_craft");
    assertEqual(groups.get(1).get(count()), 5_000_000);
  }

  public static void Demo9A_ResumeTokens(Firestore db) throws Exception {
    Query baseQuery = db.collectionGroup("players").limit(1000).orderBy(documentId());
    long playerCount = 0;

    Query query = baseQuery;
    while (true) {
      AggregateSnapshot snapshot = query.aggregate(count(), last(documentId())).get().get();
      Long count = snapshot.get(count());
      if (count == null) {
        throw new NullPointerException("this should never happen");
      }

      playerCount += count;
      if (count < 1000) {
        break;
      }

      // NOTE: If count==0 then snapshot.getString(last(documentId())) returns null.
      String lastDocumentId = snapshot.getString(last(documentId()));
      query = baseQuery.startAfter(lastDocumentId);
    }

    System.out.println("There are " + playerCount + " players");
  }

  public static void Demo9B_ResumeTokensWithGroupBy(Firestore db) throws Exception {
    Query baseQuery = db.collectionGroup("players").limit(1000).orderBy(documentId());
    HashMap<String, Long> countByCountry = new HashMap<>();

    Query query = baseQuery;
    while (true) {
      GroupByQuerySnapshot snapshot =
          query.groupBy("country").aggregate(count(), last(documentId())).get().get();
      long curTotalCount = 0;
      String lastDocumentId = null;

      for (GroupSnapshot group : snapshot.getGroups()) {
        String country = group.getString("country");
        Long count = group.get(count());
        if (country == null || count == null) {
          throw new NullPointerException("this should never happen");
        }

        if (countByCountry.containsKey(country)) {
          countByCountry.put(country, countByCountry.get(country) + count);
        } else {
          countByCountry.put(country, count);
        }

        curTotalCount += count;

        // NOTE: last(documentId()) will be exactly the same for all groups; it just gets repeated
        // in each group.
        lastDocumentId = group.getString(last(documentId()));
        if (lastDocumentId == null) {
          if (curTotalCount > 0) {
            throw new AssertionError(
                "lastDocumentId should only be null if no documents were scanned");
          }
        }
      }

      if (curTotalCount < 1000) {
        break;
      }

      query = baseQuery.startAfter(lastDocumentId);
    }

    for (String country : countByCountry.keySet()) {
      System.out.println(country + " has " + countByCountry.get(country) + " players");
    }
  }

  public static void Demo10_Max(Firestore db) throws Exception {
    Query query = db.collectionGroup("matches").whereEqualTo("game", "halo").orderBy("user");
    GroupByQuerySnapshot snapshot = query.groupBy("user").aggregate(max("timestamp")).get().get();
    assertEqual(snapshot.size(), 2);
    List<GroupSnapshot> groups = snapshot.getGroups();
    assertEqual(groups.get(0).getString("user"), "alice");
    assertEqual(groups.get(0).getString(max("timestamp")), "2022-01-06");
    assertEqual(groups.get(1).getString("user"), "bob");
    assertEqual(groups.get(1).getString(max("timestamp")), "2021-12-24");
  }

  public static void Demo11_MultipleAggregations(Firestore db) throws Exception {
    Query query = db.collectionGroup("matches").whereEqualTo("game", "halo").orderBy("user");
    GroupByQuerySnapshot snapshot =
        query.groupBy("user").aggregate(min("score"), max("score")).get().get();
    assertEqual(snapshot.size(), 2);
    List<GroupSnapshot> groups = snapshot.getGroups();
    assertEqual(groups.get(0).getString("user"), "alice");
    assertEqual(groups.get(0).getLong(min("score")), 0);
    assertEqual(groups.get(0).getLong(max("score")), 500);
    assertEqual(groups.get(1).getString("user"), "bob");
    assertEqual(groups.get(1).getLong(min("score")), 50);
    assertEqual(groups.get(1).getLong(max("score")), 250);
  }

  public static void Demo12_Transaction(Firestore db) throws Exception {
    db.runTransaction(
        txn -> {
          AggregateQuery query =
              db.collection("games").document("halo").collection("players").aggregate(count());
          AggregateQuerySnapshot snapshot = txn.get(query).get();
          assertEqual(snapshot.get(count()), 5_000_000);
          return null;
        });
  }

  private static void assertEqual(Long num1, Long num2) {
    if (!Objects.equals(num1, num2)) {
      throw new AssertionError("num1!=num2");
    }
  }

  private static void assertEqual(Long num1, int num2) {
    assertEqual(num1, Long.valueOf(num2));
  }

  private static void assertEqual(Integer num1, Integer num2) {
    if (!Objects.equals(num1, num2)) {
      throw new AssertionError("num1!=num2");
    }
  }

  private static void assertEqual(String num1, String num2) {
    if (!Objects.equals(num1, num2)) {
      throw new AssertionError("num1!=num2");
    }
  }
}
