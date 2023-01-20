/*
 * Copyright 2020 Google LLC
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

package com.google.firestore.v1;

public interface WriteRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.WriteRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The database name. In the format:
   * `projects/{project_id}/databases/{database_id}`.
   * This is only required in the first message.
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
   * This is only required in the first message.
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
   * The ID of the write stream to resume.
   * This may only be set in the first message. When left empty, a new write
   * stream will be created.
   * </pre>
   *
   * <code>string stream_id = 2;</code>
   *
   * @return The streamId.
   */
  java.lang.String getStreamId();
  /**
   *
   *
   * <pre>
   * The ID of the write stream to resume.
   * This may only be set in the first message. When left empty, a new write
   * stream will be created.
   * </pre>
   *
   * <code>string stream_id = 2;</code>
   *
   * @return The bytes for streamId.
   */
  com.google.protobuf.ByteString getStreamIdBytes();

  /**
   *
   *
   * <pre>
   * The writes to apply.
   * Always executed atomically and in order.
   * This must be empty on the first request.
   * This may be empty on the last request.
   * This must not be empty on all other requests.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 3;</code>
   */
  java.util.List<com.google.firestore.v1.Write> getWritesList();
  /**
   *
   *
   * <pre>
   * The writes to apply.
   * Always executed atomically and in order.
   * This must be empty on the first request.
   * This may be empty on the last request.
   * This must not be empty on all other requests.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 3;</code>
   */
  com.google.firestore.v1.Write getWrites(int index);
  /**
   *
   *
   * <pre>
   * The writes to apply.
   * Always executed atomically and in order.
   * This must be empty on the first request.
   * This may be empty on the last request.
   * This must not be empty on all other requests.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 3;</code>
   */
  int getWritesCount();
  /**
   *
   *
   * <pre>
   * The writes to apply.
   * Always executed atomically and in order.
   * This must be empty on the first request.
   * This may be empty on the last request.
   * This must not be empty on all other requests.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 3;</code>
   */
  java.util.List<? extends com.google.firestore.v1.WriteOrBuilder> getWritesOrBuilderList();
  /**
   *
   *
   * <pre>
   * The writes to apply.
   * Always executed atomically and in order.
   * This must be empty on the first request.
   * This may be empty on the last request.
   * This must not be empty on all other requests.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 3;</code>
   */
  com.google.firestore.v1.WriteOrBuilder getWritesOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * A stream token that was previously sent by the server.
   * The client should set this field to the token from the most recent
   * [WriteResponse][google.firestore.v1.WriteResponse] it has received. This acknowledges that the client has
   * received responses up to this token. After sending this token, earlier
   * tokens may not be used anymore.
   * The server may close the stream if there are too many unacknowledged
   * responses.
   * Leave this field unset when creating a new stream. To resume a stream at
   * a specific point, set this field and the `stream_id` field.
   * Leave this field unset when creating a new stream.
   * </pre>
   *
   * <code>bytes stream_token = 4;</code>
   *
   * @return The streamToken.
   */
  com.google.protobuf.ByteString getStreamToken();

  /**
   *
   *
   * <pre>
   * Labels associated with this write request.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 5;</code>
   */
  int getLabelsCount();
  /**
   *
   *
   * <pre>
   * Labels associated with this write request.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 5;</code>
   */
  boolean containsLabels(java.lang.String key);
  /** Use {@link #getLabelsMap()} instead. */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.String> getLabels();
  /**
   *
   *
   * <pre>
   * Labels associated with this write request.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 5;</code>
   */
  java.util.Map<java.lang.String, java.lang.String> getLabelsMap();
  /**
   *
   *
   * <pre>
   * Labels associated with this write request.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 5;</code>
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
   * Labels associated with this write request.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 5;</code>
   */
  java.lang.String getLabelsOrThrow(java.lang.String key);
}
