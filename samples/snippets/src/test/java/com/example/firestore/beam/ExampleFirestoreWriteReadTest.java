/*
 * Copyright 2022 Google LLC
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

package com.example.firestore.beam;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.common.truth.Truth;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExampleFirestoreWriteReadTest {
  private ByteArrayOutputStream bout;
  private static String projectId;
  private static Firestore firestore;

  @BeforeClass
  public static void setUp() {
    projectId = requireEnv("GOOGLE_CLOUD_PROJECT");
    firestore = FirestoreOptions.getDefaultInstance().getService();
  }

  @Before
  public void setUpStream() {
    bout = new ByteArrayOutputStream();
    System.setOut(new PrintStream(bout));
  }

  @AfterClass
  public static void tearDown() {
    deleteCollection(firestore.collection("cities-collection"), 1);
  }

  @Test
  public void testWriteAndRead() throws ExecutionException, InterruptedException {
    String[] args = {
      "--project=" + projectId, "--region=us-central1", "--numWorkers=1", "--maxNumWorkers=1"
    };
    // write the data to Firestore
    ExampleFirestoreBeamWrite.main(args);

    ApiFuture<QuerySnapshot> apiFuture = firestore.collection("cities-collection").get();
    QuerySnapshot querySnapshot = apiFuture.get();
    List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
    Truth.assertThat(documents).hasSize(2);

    // filter and read it back
    ExampleFirestoreBeamRead.main(args);

    String output = bout.toString();
    Truth.assertThat(output).contains("/documents/cities-collection/NYC");
  }

  private static String requireEnv(String varName) {
    String value = System.getenv(varName);
    Assert.assertNotNull(
        String.format("Environment variable '%s' is required to perform these tests.", varName),
        value);
    return value;
  }

  // from: https://firebase.google.com/docs/firestore/manage-data/delete-data#collections
  private static void deleteCollection(CollectionReference collection, int batchSize) {
    try {
      // retrieve a small batch of documents to avoid out-of-memory errors
      ApiFuture<QuerySnapshot> future = collection.limit(batchSize).get();
      int deleted = 0;
      // future.get() blocks on document retrieval
      List<QueryDocumentSnapshot> documents = future.get().getDocuments();
      for (QueryDocumentSnapshot document : documents) {
        document.getReference().delete();
        ++deleted;
      }
      if (deleted >= batchSize) {
        // retrieve and delete another batch
        deleteCollection(collection, batchSize);
      }
    } catch (Exception e) {
      System.err.println("Error deleting collection : " + e.getMessage());
    }
  }
}
