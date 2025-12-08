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

// [START firestore_v1_generated_Firestore_PartitionQuery_Paged_async]
import com.google.cloud.firestore.v1.FirestoreClient;
import com.google.common.base.Strings;
import com.google.firestore.v1.Cursor;
import com.google.firestore.v1.PartitionQueryRequest;
import com.google.firestore.v1.PartitionQueryResponse;

public class AsyncPartitionQueryPaged {

  public static void main(String[] args) throws Exception {
    asyncPartitionQueryPaged();
  }

  public static void asyncPartitionQueryPaged() throws Exception {
    // This snippet has been automatically generated and should be regarded as a code template only.
    // It will require modifications to work:
    // - It may require correct/in-range values for request initialization.
    // - It may require specifying regional endpoints when creating the service client as shown in
    // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
    try (FirestoreClient firestoreClient = FirestoreClient.create()) {
      PartitionQueryRequest request =
          PartitionQueryRequest.newBuilder()
              .setParent("parent-995424086")
              .setPartitionCount(-1738969222)
              .setPageToken("pageToken873572522")
              .setPageSize(883849137)
              .build();
      while (true) {
        PartitionQueryResponse response = firestoreClient.partitionQueryCallable().call(request);
        for (Cursor element : response.getPartitionsList()) {
          // doThingsWith(element);
        }
        String nextPageToken = response.getNextPageToken();
        if (!Strings.isNullOrEmpty(nextPageToken)) {
          request = request.toBuilder().setPageToken(nextPageToken).build();
        } else {
          break;
        }
      }
    }
  }
}
// [END firestore_v1_generated_Firestore_PartitionQuery_Paged_async]
