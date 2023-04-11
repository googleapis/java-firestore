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

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;


/**
 * Snippets to demonstrate how to set a regional endpoint.
 */
public class RegionalEndpointSnippets {

  /**
   * Create a client with a regional endpoint.
   **/
  public Firestore regionalEndpoint(String projectId, String endpoint) throws Exception {
    // [START firestore_regional_endpoint]
    FirestoreOptions firestoreOptions =
        FirestoreOptions.newBuilder()
            .setProjectId(projectId)
            .setCredentials(GoogleCredentials.getApplicationDefault())
            // set endpoint like nam5-firestore.googleapis.com:443
            .setHost(endpoint)
            .build();
    Firestore dbWithEndpoint = firestoreOptions.getService();
    // [END firestore_regional_endpoint]
    return dbWithEndpoint;
  }

}
