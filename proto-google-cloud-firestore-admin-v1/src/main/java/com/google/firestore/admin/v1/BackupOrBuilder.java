/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/firestore/admin/v1/backup.proto

// Protobuf Java Version: 3.25.2
package com.google.firestore.admin.v1;

public interface BackupOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.admin.v1.Backup)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Output only. The unique resource name of the Backup.
   *
   * Format is `projects/{project}/locations/{location}/backups/{backup}`.
   * </pre>
   *
   * <code>string name = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
   *
   * @return The name.
   */
  java.lang.String getName();
  /**
   *
   *
   * <pre>
   * Output only. The unique resource name of the Backup.
   *
   * Format is `projects/{project}/locations/{location}/backups/{backup}`.
   * </pre>
   *
   * <code>string name = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
   *
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString getNameBytes();

  /**
   *
   *
   * <pre>
   * Output only. Name of the Firestore database that the backup is from.
   *
   * Format is `projects/{project}/databases/{database}`.
   * </pre>
   *
   * <code>
   * string database = 2 [(.google.api.field_behavior) = OUTPUT_ONLY, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The database.
   */
  java.lang.String getDatabase();
  /**
   *
   *
   * <pre>
   * Output only. Name of the Firestore database that the backup is from.
   *
   * Format is `projects/{project}/databases/{database}`.
   * </pre>
   *
   * <code>
   * string database = 2 [(.google.api.field_behavior) = OUTPUT_ONLY, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The bytes for database.
   */
  com.google.protobuf.ByteString getDatabaseBytes();

  /**
   *
   *
   * <pre>
   * Output only. The system-generated UUID4 for the Firestore database that the
   * backup is from.
   * </pre>
   *
   * <code>string database_uid = 7 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
   *
   * @return The databaseUid.
   */
  java.lang.String getDatabaseUid();
  /**
   *
   *
   * <pre>
   * Output only. The system-generated UUID4 for the Firestore database that the
   * backup is from.
   * </pre>
   *
   * <code>string database_uid = 7 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
   *
   * @return The bytes for databaseUid.
   */
  com.google.protobuf.ByteString getDatabaseUidBytes();

  /**
   *
   *
   * <pre>
   * Output only. The backup contains an externally consistent copy of the
   * database at this time.
   * </pre>
   *
   * <code>
   * .google.protobuf.Timestamp snapshot_time = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return Whether the snapshotTime field is set.
   */
  boolean hasSnapshotTime();
  /**
   *
   *
   * <pre>
   * Output only. The backup contains an externally consistent copy of the
   * database at this time.
   * </pre>
   *
   * <code>
   * .google.protobuf.Timestamp snapshot_time = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return The snapshotTime.
   */
  com.google.protobuf.Timestamp getSnapshotTime();
  /**
   *
   *
   * <pre>
   * Output only. The backup contains an externally consistent copy of the
   * database at this time.
   * </pre>
   *
   * <code>
   * .google.protobuf.Timestamp snapshot_time = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   */
  com.google.protobuf.TimestampOrBuilder getSnapshotTimeOrBuilder();

  /**
   *
   *
   * <pre>
   * Output only. The timestamp at which this backup expires.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp expire_time = 4 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return Whether the expireTime field is set.
   */
  boolean hasExpireTime();
  /**
   *
   *
   * <pre>
   * Output only. The timestamp at which this backup expires.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp expire_time = 4 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return The expireTime.
   */
  com.google.protobuf.Timestamp getExpireTime();
  /**
   *
   *
   * <pre>
   * Output only. The timestamp at which this backup expires.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp expire_time = 4 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   */
  com.google.protobuf.TimestampOrBuilder getExpireTimeOrBuilder();

  /**
   *
   *
   * <pre>
   * Output only. Statistics about the backup.
   *
   * This data only becomes available after the backup is fully materialized to
   * secondary storage. This field will be empty till then.
   * </pre>
   *
   * <code>
   * .google.firestore.admin.v1.Backup.Stats stats = 6 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return Whether the stats field is set.
   */
  boolean hasStats();
  /**
   *
   *
   * <pre>
   * Output only. Statistics about the backup.
   *
   * This data only becomes available after the backup is fully materialized to
   * secondary storage. This field will be empty till then.
   * </pre>
   *
   * <code>
   * .google.firestore.admin.v1.Backup.Stats stats = 6 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return The stats.
   */
  com.google.firestore.admin.v1.Backup.Stats getStats();
  /**
   *
   *
   * <pre>
   * Output only. Statistics about the backup.
   *
   * This data only becomes available after the backup is fully materialized to
   * secondary storage. This field will be empty till then.
   * </pre>
   *
   * <code>
   * .google.firestore.admin.v1.Backup.Stats stats = 6 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   */
  com.google.firestore.admin.v1.Backup.StatsOrBuilder getStatsOrBuilder();

  /**
   *
   *
   * <pre>
   * Output only. The current state of the backup.
   * </pre>
   *
   * <code>
   * .google.firestore.admin.v1.Backup.State state = 8 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return The enum numeric value on the wire for state.
   */
  int getStateValue();
  /**
   *
   *
   * <pre>
   * Output only. The current state of the backup.
   * </pre>
   *
   * <code>
   * .google.firestore.admin.v1.Backup.State state = 8 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return The state.
   */
  com.google.firestore.admin.v1.Backup.State getState();
}