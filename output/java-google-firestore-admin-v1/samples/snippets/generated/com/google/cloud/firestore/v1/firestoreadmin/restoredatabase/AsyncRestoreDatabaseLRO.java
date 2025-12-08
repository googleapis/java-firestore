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

// [START firestore_v1_generated_FirestoreAdmin_RestoreDatabase_LRO_async]
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.firestore.v1.FirestoreAdminClient;
import com.google.firestore.admin.v1.BackupName;
import com.google.firestore.admin.v1.Database;
import com.google.firestore.admin.v1.ProjectName;
import com.google.firestore.admin.v1.RestoreDatabaseMetadata;
import com.google.firestore.admin.v1.RestoreDatabaseRequest;
import java.util.HashMap;

public class AsyncRestoreDatabaseLRO {

  public static void main(String[] args) throws Exception {
    asyncRestoreDatabaseLRO();
  }

  public static void asyncRestoreDatabaseLRO() throws Exception {
    // This snippet has been automatically generated and should be regarded as a code template only.
    // It will require modifications to work:
    // - It may require correct/in-range values for request initialization.
    // - It may require specifying regional endpoints when creating the service client as shown in
    // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
    try (FirestoreAdminClient firestoreAdminClient = FirestoreAdminClient.create()) {
      RestoreDatabaseRequest request =
          RestoreDatabaseRequest.newBuilder()
              .setParent(ProjectName.of("[PROJECT]").toString())
              .setDatabaseId("databaseId1688905718")
              .setBackup(BackupName.of("[PROJECT]", "[LOCATION]", "[BACKUP]").toString())
              .setEncryptionConfig(Database.EncryptionConfig.newBuilder().build())
              .putAllTags(new HashMap<String, String>())
              .build();
      OperationFuture<Database, RestoreDatabaseMetadata> future =
          firestoreAdminClient.restoreDatabaseOperationCallable().futureCall(request);
      // Do something.
      Database response = future.get();
    }
  }
}
// [END firestore_v1_generated_FirestoreAdmin_RestoreDatabase_LRO_async]
