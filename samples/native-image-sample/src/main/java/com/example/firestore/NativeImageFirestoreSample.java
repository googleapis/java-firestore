/*
 * Copyright 2020-2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sample Firestore application demonstrating basic operations.
 */
public class NativeImageFirestoreSample {

  private static final String USERS_COLLECTION = "nativeimage_test_users";

  /**
   * Entrypoint to the Firestore sample application.
   */
  public static void main(String[] args) throws Exception {
    FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance();
    Firestore db = firestoreOptions.getService();

    deleteCollection(db);
    createUserDocument(db);
    createUserDocumentPojo(db);
    readDocuments(db);
    runSampleQueries(db);
  }

  static void deleteCollection(Firestore db) throws Exception {
    Iterable<DocumentReference> documents = db.collection(USERS_COLLECTION).listDocuments();
    for (DocumentReference doc : documents) {
      doc.delete().get();
    }
  }

  static void createUserDocument(Firestore db) throws Exception {
    DocumentReference docRef = db.collection(USERS_COLLECTION).document("alovelace");
    Map<String, Object> data = new HashMap<>();
    data.put("id", "10");
    data.put("first", "Ada");
    data.put("last", "Lovelace");
    data.put("born", 1815);

    WriteResult result = docRef.set(data).get();
    System.out.println("Created user " + docRef.getId() + ". Timestamp: " + result.getUpdateTime());
  }

  static void createUserDocumentPojo(Firestore db) throws Exception {
    CollectionReference collectionReference = db.collection(USERS_COLLECTION);
    WriteResult result =
        collectionReference.document()
            .set(new Person("Alan", "Turing", 1912))
            .get();

    System.out.println("Created user by POJO. Timestamp: " + result.getUpdateTime());
  }

  static void readDocuments(Firestore db) throws Exception {
    ApiFuture<QuerySnapshot> query = db.collection(USERS_COLLECTION).get();
    QuerySnapshot querySnapshot = query.get();
    List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
    System.out.println("The following users were saved:");
    printUsers(documents);
  }

  static void runSampleQueries(Firestore db) throws Exception {
    List<QueryDocumentSnapshot> results =
        runQuery(db.collection(USERS_COLLECTION).whereLessThan("born", 1900));
    System.out.println("Number of users born before 1900: " + results.size());

    results = runQuery(
        db.collection(USERS_COLLECTION).whereGreaterThan(FieldPath.of("born"), 1900));
    System.out.println("Number of users born earlier after 1900: " + results.size());

    results = runQuery(
        db.collection(USERS_COLLECTION).whereEqualTo("name", "Ada"));
    System.out.println("Number of users whose first name is 'Ada': " + results.size());
  }

  private static List<QueryDocumentSnapshot> runQuery(Query query) throws Exception {
    QuerySnapshot querySnapshot = query.get().get();
    List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
    return documents;
  }

  private static void printUsers(Iterable<QueryDocumentSnapshot> documents) {
    for (QueryDocumentSnapshot document : documents) {
      System.out.printf(
          "Document: %s | %s %s born %d\n",
          document.getId(),
          document.getString("first"),
          document.getString("last"),
          document.getLong("born"));
    }
  }
}