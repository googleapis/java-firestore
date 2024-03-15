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
// source: google/firestore/admin/v1/firestore_admin.proto

// Protobuf Java Version: 3.25.2
package com.google.firestore.admin.v1;

public interface ListBackupsResponseOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.admin.v1.ListBackupsResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * List of all backups for the project.
   * </pre>
   *
   * <code>repeated .google.firestore.admin.v1.Backup backups = 1;</code>
   */
  java.util.List<com.google.firestore.admin.v1.Backup> getBackupsList();
  /**
   *
   *
   * <pre>
   * List of all backups for the project.
   * </pre>
   *
   * <code>repeated .google.firestore.admin.v1.Backup backups = 1;</code>
   */
  com.google.firestore.admin.v1.Backup getBackups(int index);
  /**
   *
   *
   * <pre>
   * List of all backups for the project.
   * </pre>
   *
   * <code>repeated .google.firestore.admin.v1.Backup backups = 1;</code>
   */
  int getBackupsCount();
  /**
   *
   *
   * <pre>
   * List of all backups for the project.
   * </pre>
   *
   * <code>repeated .google.firestore.admin.v1.Backup backups = 1;</code>
   */
  java.util.List<? extends com.google.firestore.admin.v1.BackupOrBuilder> getBackupsOrBuilderList();
  /**
   *
   *
   * <pre>
   * List of all backups for the project.
   * </pre>
   *
   * <code>repeated .google.firestore.admin.v1.Backup backups = 1;</code>
   */
  com.google.firestore.admin.v1.BackupOrBuilder getBackupsOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * List of locations that existing backups were not able to be fetched from.
   *
   * Instead of failing the entire requests when a single location is
   * unreachable, this response returns a partial result set and list of
   * locations unable to be reached here. The request can be retried against a
   * single location to get a concrete error.
   * </pre>
   *
   * <code>repeated string unreachable = 3;</code>
   *
   * @return A list containing the unreachable.
   */
  java.util.List<java.lang.String> getUnreachableList();
  /**
   *
   *
   * <pre>
   * List of locations that existing backups were not able to be fetched from.
   *
   * Instead of failing the entire requests when a single location is
   * unreachable, this response returns a partial result set and list of
   * locations unable to be reached here. The request can be retried against a
   * single location to get a concrete error.
   * </pre>
   *
   * <code>repeated string unreachable = 3;</code>
   *
   * @return The count of unreachable.
   */
  int getUnreachableCount();
  /**
   *
   *
   * <pre>
   * List of locations that existing backups were not able to be fetched from.
   *
   * Instead of failing the entire requests when a single location is
   * unreachable, this response returns a partial result set and list of
   * locations unable to be reached here. The request can be retried against a
   * single location to get a concrete error.
   * </pre>
   *
   * <code>repeated string unreachable = 3;</code>
   *
   * @param index The index of the element to return.
   * @return The unreachable at the given index.
   */
  java.lang.String getUnreachable(int index);
  /**
   *
   *
   * <pre>
   * List of locations that existing backups were not able to be fetched from.
   *
   * Instead of failing the entire requests when a single location is
   * unreachable, this response returns a partial result set and list of
   * locations unable to be reached here. The request can be retried against a
   * single location to get a concrete error.
   * </pre>
   *
   * <code>repeated string unreachable = 3;</code>
   *
   * @param index The index of the value to return.
   * @return The bytes of the unreachable at the given index.
   */
  com.google.protobuf.ByteString getUnreachableBytes(int index);
}
