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

// [START firestore_v1_generated_Firestore_BatchWrite_async]
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.v1.FirestoreClient;
import com.google.firestore.v1.BatchWriteRequest;
import com.google.firestore.v1.BatchWriteResponse;
import com.google.firestore.v1.Write;
import java.util.ArrayList;
import java.util.HashMap;

public class AsyncBatchWrite {

  public static void main(String[] args) throws Exception {
    asyncBatchWrite();
  }

  public static void asyncBatchWrite() throws Exception {
    // This snippet has been automatically generated and should be regarded as a code template only.
    // It will require modifications to work:
    // - It may require correct/in-range values for request initialization.
    // - It may require specifying regional endpoints when creating the service client as shown in
    // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
    try (FirestoreClient firestoreClient = FirestoreClient.create()) {
      BatchWriteRequest request =
          BatchWriteRequest.newBuilder()
              .setDatabase("database1789464955")
              .addAllWrites(new ArrayList<Write>())
              .putAllLabels(new HashMap<String, String>())
              .build();
      ApiFuture<BatchWriteResponse> future =
          firestoreClient.batchWriteCallable().futureCall(request);
      // Do something.
      BatchWriteResponse response = future.get();
    }
  }
}
// [END firestore_v1_generated_Firestore_BatchWrite_async]
