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

// [START firestore_v1_generated_FirestoreAdmin_DeleteBackupSchedule_sync]
import com.google.cloud.firestore.v1.FirestoreAdminClient;
import com.google.firestore.admin.v1.BackupScheduleName;
import com.google.firestore.admin.v1.DeleteBackupScheduleRequest;
import com.google.protobuf.Empty;

public class SyncDeleteBackupSchedule {

  public static void main(String[] args) throws Exception {
    syncDeleteBackupSchedule();
  }

  public static void syncDeleteBackupSchedule() throws Exception {
    // This snippet has been automatically generated and should be regarded as a code template only.
    // It will require modifications to work:
    // - It may require correct/in-range values for request initialization.
    // - It may require specifying regional endpoints when creating the service client as shown in
    // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
    try (FirestoreAdminClient firestoreAdminClient = FirestoreAdminClient.create()) {
      DeleteBackupScheduleRequest request =
          DeleteBackupScheduleRequest.newBuilder()
              .setName(
                  BackupScheduleName.of("[PROJECT]", "[DATABASE]", "[BACKUP_SCHEDULE]").toString())
              .build();
      firestoreAdminClient.deleteBackupSchedule(request);
    }
  }
}
// [END firestore_v1_generated_FirestoreAdmin_DeleteBackupSchedule_sync]
