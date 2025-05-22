/*
 * Copyright 2023 Google Inc.
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

import static org.junit.Assert.assertNotNull;

import com.example.firestore.BaseIntegrationTest;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.v1.FirestoreAdminClient;
import com.google.firestore.admin.v1.Database;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
@SuppressWarnings("checkstyle:abbreviationaswordinname")
public class RegionalEndpointSnippetsIT extends BaseIntegrationTest {

  private static RegionalEndpointSnippets regionalEndpointSnippets;

  private static FirestoreAdminClient adminClient;

  private static String getEnvVar(String varName) {
    String value = System.getenv(varName);
    assertNotNull(
        String.format("Environment variable '%s' must be set to perform these tests.", varName),
        value);
    return value;
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    regionalEndpointSnippets = new RegionalEndpointSnippets();

    adminClient = FirestoreAdminClient.create();
  }

  @Test
  public void testRegionalEndoint() throws Exception {
    projectId = getEnvVar("FIRESTORE_PROJECT_ID");
    // Get database location of the default database in the project
    Database dbInfo = adminClient.getDatabase("projects/" + projectId + "/databases/(default)");
    String locationId = dbInfo.getLocationId();

    // Test regional endpoint with same location as default database
    Firestore dbWithEndpoint = regionalEndpointSnippets.regionalEndpoint(projectId,
        locationId + "-firestore.googleapis.com:443");
    assertNotNull(dbWithEndpoint);

    // Retrieve a document to confirm client connection
    DocumentReference docRef = dbWithEndpoint.collection("cities").document("SF");
    // asynchronously retrieve the document
    ApiFuture<DocumentSnapshot> future = docRef.get();
    assertNotNull(future);

    dbWithEndpoint.close();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    db.close();
    adminClient.close();
  }
}
