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

// Protobuf Java Version: 3.25.4
package com.google.firestore.admin.v1;

public interface CreateDatabaseRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.admin.v1.CreateDatabaseRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. A parent name of the form
   * `projects/{project_id}`
   * </pre>
   *
   * <code>
   * string parent = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The parent.
   */
  java.lang.String getParent();
  /**
   *
   *
   * <pre>
   * Required. A parent name of the form
   * `projects/{project_id}`
   * </pre>
   *
   * <code>
   * string parent = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The bytes for parent.
   */
  com.google.protobuf.ByteString getParentBytes();

  /**
   *
   *
   * <pre>
   * Required. The Database to create.
   * </pre>
   *
   * <code>
   * .google.firestore.admin.v1.Database database = 2 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return Whether the database field is set.
   */
  boolean hasDatabase();
  /**
   *
   *
   * <pre>
   * Required. The Database to create.
   * </pre>
   *
   * <code>
   * .google.firestore.admin.v1.Database database = 2 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return The database.
   */
  com.google.firestore.admin.v1.Database getDatabase();
  /**
   *
   *
   * <pre>
   * Required. The Database to create.
   * </pre>
   *
   * <code>
   * .google.firestore.admin.v1.Database database = 2 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.firestore.admin.v1.DatabaseOrBuilder getDatabaseOrBuilder();

  /**
   *
   *
   * <pre>
   * Required. The ID to use for the database, which will become the final
   * component of the database's resource name.
   *
   * This value should be 4-63 characters. Valid characters are /[a-z][0-9]-/
   * with first character a letter and the last a letter or a number. Must not
   * be UUID-like /[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}/.
   *
   * "(default)" database ID is also valid.
   * </pre>
   *
   * <code>string database_id = 3 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The databaseId.
   */
  java.lang.String getDatabaseId();
  /**
   *
   *
   * <pre>
   * Required. The ID to use for the database, which will become the final
   * component of the database's resource name.
   *
   * This value should be 4-63 characters. Valid characters are /[a-z][0-9]-/
   * with first character a letter and the last a letter or a number. Must not
   * be UUID-like /[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}/.
   *
   * "(default)" database ID is also valid.
   * </pre>
   *
   * <code>string database_id = 3 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The bytes for databaseId.
   */
  com.google.protobuf.ByteString getDatabaseIdBytes();
}
