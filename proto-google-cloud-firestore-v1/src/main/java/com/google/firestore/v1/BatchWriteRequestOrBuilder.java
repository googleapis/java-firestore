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
// source: google/firestore/v1/firestore.proto

// Protobuf Java Version: 3.25.4
package com.google.firestore.v1;

public interface BatchWriteRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.BatchWriteRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The database name. In the format:
   * `projects/{project_id}/databases/{database_id}`.
   * </pre>
   *
   * <code>string database = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The database.
   */
  java.lang.String getDatabase();
  /**
   *
   *
   * <pre>
   * Required. The database name. In the format:
   * `projects/{project_id}/databases/{database_id}`.
   * </pre>
   *
   * <code>string database = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The bytes for database.
   */
  com.google.protobuf.ByteString getDatabaseBytes();

  /**
   *
   *
   * <pre>
   * The writes to apply.
   *
   * Method does not apply writes atomically and does not guarantee ordering.
   * Each write succeeds or fails independently. You cannot write to the same
   * document more than once per request.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 2;</code>
   */
  java.util.List<com.google.firestore.v1.Write> getWritesList();
  /**
   *
   *
   * <pre>
   * The writes to apply.
   *
   * Method does not apply writes atomically and does not guarantee ordering.
   * Each write succeeds or fails independently. You cannot write to the same
   * document more than once per request.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 2;</code>
   */
  com.google.firestore.v1.Write getWrites(int index);
  /**
   *
   *
   * <pre>
   * The writes to apply.
   *
   * Method does not apply writes atomically and does not guarantee ordering.
   * Each write succeeds or fails independently. You cannot write to the same
   * document more than once per request.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 2;</code>
   */
  int getWritesCount();
  /**
   *
   *
   * <pre>
   * The writes to apply.
   *
   * Method does not apply writes atomically and does not guarantee ordering.
   * Each write succeeds or fails independently. You cannot write to the same
   * document more than once per request.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 2;</code>
   */
  java.util.List<? extends com.google.firestore.v1.WriteOrBuilder> getWritesOrBuilderList();
  /**
   *
   *
   * <pre>
   * The writes to apply.
   *
   * Method does not apply writes atomically and does not guarantee ordering.
   * Each write succeeds or fails independently. You cannot write to the same
   * document more than once per request.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 2;</code>
   */
  com.google.firestore.v1.WriteOrBuilder getWritesOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * Labels associated with this batch write.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 3;</code>
   */
  int getLabelsCount();
  /**
   *
   *
   * <pre>
   * Labels associated with this batch write.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 3;</code>
   */
  boolean containsLabels(java.lang.String key);
  /** Use {@link #getLabelsMap()} instead. */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.String> getLabels();
  /**
   *
   *
   * <pre>
   * Labels associated with this batch write.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 3;</code>
   */
  java.util.Map<java.lang.String, java.lang.String> getLabelsMap();
  /**
   *
   *
   * <pre>
   * Labels associated with this batch write.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 3;</code>
   */
  /* nullable */
  java.lang.String getLabelsOrDefault(
      java.lang.String key,
      /* nullable */
      java.lang.String defaultValue);
  /**
   *
   *
   * <pre>
   * Labels associated with this batch write.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 3;</code>
   */
  java.lang.String getLabelsOrThrow(java.lang.String key);
}
