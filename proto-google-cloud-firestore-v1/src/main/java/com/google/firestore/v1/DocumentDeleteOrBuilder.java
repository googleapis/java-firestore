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
// source: google/firestore/v1/write.proto

// Protobuf Java Version: 3.25.4
package com.google.firestore.v1;

public interface DocumentDeleteOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.DocumentDelete)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The resource name of the [Document][google.firestore.v1.Document] that was
   * deleted.
   * </pre>
   *
   * <code>string document = 1;</code>
   *
   * @return The document.
   */
  java.lang.String getDocument();
  /**
   *
   *
   * <pre>
   * The resource name of the [Document][google.firestore.v1.Document] that was
   * deleted.
   * </pre>
   *
   * <code>string document = 1;</code>
   *
   * @return The bytes for document.
   */
  com.google.protobuf.ByteString getDocumentBytes();

  /**
   *
   *
   * <pre>
   * A set of target IDs for targets that previously matched this entity.
   * </pre>
   *
   * <code>repeated int32 removed_target_ids = 6;</code>
   *
   * @return A list containing the removedTargetIds.
   */
  java.util.List<java.lang.Integer> getRemovedTargetIdsList();
  /**
   *
   *
   * <pre>
   * A set of target IDs for targets that previously matched this entity.
   * </pre>
   *
   * <code>repeated int32 removed_target_ids = 6;</code>
   *
   * @return The count of removedTargetIds.
   */
  int getRemovedTargetIdsCount();
  /**
   *
   *
   * <pre>
   * A set of target IDs for targets that previously matched this entity.
   * </pre>
   *
   * <code>repeated int32 removed_target_ids = 6;</code>
   *
   * @param index The index of the element to return.
   * @return The removedTargetIds at the given index.
   */
  int getRemovedTargetIds(int index);

  /**
   *
   *
   * <pre>
   * The read timestamp at which the delete was observed.
   *
   * Greater or equal to the `commit_time` of the delete.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 4;</code>
   *
   * @return Whether the readTime field is set.
   */
  boolean hasReadTime();
  /**
   *
   *
   * <pre>
   * The read timestamp at which the delete was observed.
   *
   * Greater or equal to the `commit_time` of the delete.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 4;</code>
   *
   * @return The readTime.
   */
  com.google.protobuf.Timestamp getReadTime();
  /**
   *
   *
   * <pre>
   * The read timestamp at which the delete was observed.
   *
   * Greater or equal to the `commit_time` of the delete.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 4;</code>
   */
  com.google.protobuf.TimestampOrBuilder getReadTimeOrBuilder();
}
