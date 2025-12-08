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

// [START firestore_v1_generated_Firestore_RunAggregationQuery_async]
import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.firestore.v1.FirestoreClient;
import com.google.firestore.v1.ExplainOptions;
import com.google.firestore.v1.RunAggregationQueryRequest;
import com.google.firestore.v1.RunAggregationQueryResponse;

public class AsyncRunAggregationQuery {

  public static void main(String[] args) throws Exception {
    asyncRunAggregationQuery();
  }

  public static void asyncRunAggregationQuery() throws Exception {
    // This snippet has been automatically generated and should be regarded as a code template only.
    // It will require modifications to work:
    // - It may require correct/in-range values for request initialization.
    // - It may require specifying regional endpoints when creating the service client as shown in
    // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
    try (FirestoreClient firestoreClient = FirestoreClient.create()) {
      RunAggregationQueryRequest request =
          RunAggregationQueryRequest.newBuilder()
              .setParent("parent-995424086")
              .setExplainOptions(ExplainOptions.newBuilder().build())
              .build();
      ServerStream<RunAggregationQueryResponse> stream =
          firestoreClient.runAggregationQueryCallable().call(request);
      for (RunAggregationQueryResponse response : stream) {
        // Do something when a response is received.
      }
    }
  }
}
// [END firestore_v1_generated_Firestore_RunAggregationQuery_async]
