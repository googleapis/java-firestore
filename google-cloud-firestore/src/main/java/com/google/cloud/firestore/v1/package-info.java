/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * The interfaces provided are listed below, along with usage samples.
 *
 * <p>======================= FirestoreClient =======================
 *
 * <p>Service Description: The Cloud Firestore service.
 *
 * <p>Cloud Firestore is a fast, fully managed, serverless, cloud-native NoSQL document database
 * that simplifies storing, syncing, and querying data for your mobile, web, and IoT apps at global
 * scale. Its client libraries provide live synchronization and offline support, while its security
 * features and integrations with Firebase and Google Cloud Platform (GCP) accelerate building truly
 * serverless apps.
 *
 * <p>Sample for FirestoreClient:
 *
 * <pre>{@code
 * try (FirestoreClient firestoreClient = FirestoreClient.create()) {
 *   GetDocumentRequest request =
 *       GetDocumentRequest.newBuilder()
 *           .setName("name3373707")
 *           .setMask(DocumentMask.newBuilder().build())
 *           .build();
 *   Document response = firestoreClient.getDocument(request);
 * }
 * }</pre>
 */
@Generated("by gapic-generator-java")
package com.google.cloud.firestore.v1;

import javax.annotation.Generated;
