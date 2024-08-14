/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.firestore.snippets;

import com.example.firestore.snippets.model.City;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/** Snippets to support firestore querying data documentation. */
class MultipleRangeInequalitySnippets {

  private final Firestore db;

  MultipleRangeInequalitySnippets(Firestore db) {
    this.db = db;
  }

  /**
   * Creates cities collection and add sample documents to test queries.
   */
  void prepareExamples() throws Exception {
    private static void addCityDocument(CollectionReference citiesRef, String documentId, Map<String, Object> data) {
      ApiFuture<WriteResult> future = citiesRef.document(documentId).set(data);
      try {
        WriteResult result = future.get();
        System.out.println("Update time : " + result.getUpdateTime());
      } catch (Exception e) {
        System.err.println("Error adding document: " + e.getMessage());
      }
    }

    CollectionReference cities = db.collection("cities");

    // Data for each city
    Map<String, Object> sfData = new HashMap<>();
    sfData.put("Name", "San Francisco");
    sfData.put("State", "CA");
    sfData.put("Country", "USA");
    sfData.put("Capital", false);
    sfData.put("Population", 860000L);
    sfData.put("Density", 18000L);
    sfData.put("Regions", Arrays.asList("west_coast", "norcal"));

    Map<String, Object> laData = new HashMap<>();
    laData.put("Name", "Los Angeles");
    laData.put("State", "CA");
    laData.put("Country", "USA");
    laData.put("Capital", false);
    laData.put("Population", 3900000L);
    laData.put("Density", 8300L);
    laData.put("Regions", Arrays.asList("west_coast", "socal"));

    Map<String, Object> dcData = new HashMap<>();
    dcData.put("Name", "Washington D.C.");
    dcData.put("State", null);
    dcData.put("Country", "USA");
    dcData.put("Capital", true);
    dcData.put("Population", 680000L);
    dcData.put("Density", 11300L);
    dcData.put("Regions", Arrays.asList("east_coast"));

    Map<String, Object> tokData = new HashMap<>();
    tokData.put("Name", "Tokyo");
    tokData.put("State", null);
    tokData.put("Country", "Japan");
    tokData.put("Capital", true);
    tokData.put("Population", 9000000L);
    tokData.put("Density", 16000L);
    tokData.put("Regions", Arrays.asList("kanto", "honshu"));

    Map<String, Object> bjData = new HashMap<>();
    bjData.put("Name", "Beijing");
    bjData.put("State", null);
    bjData.put("Country", "China");
    bjData.put("Capital", true);
    bjData.put("Population", 21500000L);
    bjData.put("Density", 3500L);
    bjData.put("Regions", Arrays.asList("jingjinji", "hebei"));

    // Add each city document
    addCityDocument(citiesRef, "SF", sfData);
    addCityDocument(citiesRef, "LA", laData);
    addCityDocument(citiesRef, "DC", dcData);
    addCityDocument(citiesRef, "TOK", tokData);
    addCityDocument(citiesRef, "BJ", bjData);

    System.out.println("Data added successfully!");
  }

  /* Example of Query with range and inequality filters. */
  Query rangeMultipleInequalityFilter() {
    // [START firestore_query_filter_compound_multi_ineq]
    Query query = db.collection("cities")
        .whereGreaterThan("population", 1000000)
        .whereLessThan("density", 10000);
    // [END firestore_query_filter_compound_multi_ineq]

    return query;
  }

  /** Closes the gRPC channels associated with this instance and frees up their resources. */
  void close() throws Exception {
    db.close();
  }
}
