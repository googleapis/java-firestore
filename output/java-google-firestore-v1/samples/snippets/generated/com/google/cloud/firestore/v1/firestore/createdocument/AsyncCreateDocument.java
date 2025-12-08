/*
 * Copyright 2025 Google LLC
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

package com.google.cloud.firestore.v1.samples;

// [START firestore_v1_generated_Firestore_CreateDocument_async]
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.v1.FirestoreClient;
import com.google.firestore.v1.CreateDocumentRequest;
import com.google.firestore.v1.Document;
import com.google.firestore.v1.DocumentMask;

public class AsyncCreateDocument {

  public static void main(String[] args) throws Exception {
    asyncCreateDocument();
  }

  public static void asyncCreateDocument() throws Exception {
    // This snippet has been automatically generated and should be regarded as a code template only.
    // It will require modifications to work:
    // - It may require correct/in-range values for request initialization.
    // - It may require specifying regional endpoints when creating the service client as shown in
    // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
    try (FirestoreClient firestoreClient = FirestoreClient.create()) {
      CreateDocumentRequest request =
          CreateDocumentRequest.newBuilder()
              .setParent("parent-995424086")
              .setCollectionId("collectionId1636075609")
              .setDocumentId("documentId-814940266")
              .setDocument(Document.newBuilder().build())
              .setMask(DocumentMask.newBuilder().build())
              .build();
      ApiFuture<Document> future = firestoreClient.createDocumentCallable().futureCall(request);
      // Do something.
      Document response = future.get();
    }
  }
}
// [END firestore_v1_generated_Firestore_CreateDocument_async]
