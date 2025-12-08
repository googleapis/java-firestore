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

// [START firestore_v1_generated_FirestoreAdmin_CreateIndex_LRO_async]
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.firestore.v1.FirestoreAdminClient;
import com.google.firestore.admin.v1.CollectionGroupName;
import com.google.firestore.admin.v1.CreateIndexRequest;
import com.google.firestore.admin.v1.Index;
import com.google.firestore.admin.v1.IndexOperationMetadata;

public class AsyncCreateIndexLRO {

  public static void main(String[] args) throws Exception {
    asyncCreateIndexLRO();
  }

  public static void asyncCreateIndexLRO() throws Exception {
    // This snippet has been automatically generated and should be regarded as a code template only.
    // It will require modifications to work:
    // - It may require correct/in-range values for request initialization.
    // - It may require specifying regional endpoints when creating the service client as shown in
    // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
    try (FirestoreAdminClient firestoreAdminClient = FirestoreAdminClient.create()) {
      CreateIndexRequest request =
          CreateIndexRequest.newBuilder()
              .setParent(
                  CollectionGroupName.of("[PROJECT]", "[DATABASE]", "[COLLECTION]").toString())
              .setIndex(Index.newBuilder().build())
              .build();
      OperationFuture<Index, IndexOperationMetadata> future =
          firestoreAdminClient.createIndexOperationCallable().futureCall(request);
      // Do something.
      Index response = future.get();
    }
  }
}
// [END firestore_v1_generated_FirestoreAdmin_CreateIndex_LRO_async]
