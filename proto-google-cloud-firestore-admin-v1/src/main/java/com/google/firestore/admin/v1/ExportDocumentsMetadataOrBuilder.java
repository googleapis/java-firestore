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
// source: google/firestore/admin/v1/operation.proto

// Protobuf Java Version: 3.25.5
package com.google.firestore.admin.v1;

public interface ExportDocumentsMetadataOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.admin.v1.ExportDocumentsMetadata)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The time this operation started.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp start_time = 1;</code>
   *
   * @return Whether the startTime field is set.
   */
  boolean hasStartTime();
  /**
   *
   *
   * <pre>
   * The time this operation started.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp start_time = 1;</code>
   *
   * @return The startTime.
   */
  com.google.protobuf.Timestamp getStartTime();
  /**
   *
   *
   * <pre>
   * The time this operation started.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp start_time = 1;</code>
   */
  com.google.protobuf.TimestampOrBuilder getStartTimeOrBuilder();

  /**
   *
   *
   * <pre>
   * The time this operation completed. Will be unset if operation still in
   * progress.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp end_time = 2;</code>
   *
   * @return Whether the endTime field is set.
   */
  boolean hasEndTime();
  /**
   *
   *
   * <pre>
   * The time this operation completed. Will be unset if operation still in
   * progress.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp end_time = 2;</code>
   *
   * @return The endTime.
   */
  com.google.protobuf.Timestamp getEndTime();
  /**
   *
   *
   * <pre>
   * The time this operation completed. Will be unset if operation still in
   * progress.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp end_time = 2;</code>
   */
  com.google.protobuf.TimestampOrBuilder getEndTimeOrBuilder();

  /**
   *
   *
   * <pre>
   * The state of the export operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.OperationState operation_state = 3;</code>
   *
   * @return The enum numeric value on the wire for operationState.
   */
  int getOperationStateValue();
  /**
   *
   *
   * <pre>
   * The state of the export operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.OperationState operation_state = 3;</code>
   *
   * @return The operationState.
   */
  com.google.firestore.admin.v1.OperationState getOperationState();

  /**
   *
   *
   * <pre>
   * The progress, in documents, of this operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Progress progress_documents = 4;</code>
   *
   * @return Whether the progressDocuments field is set.
   */
  boolean hasProgressDocuments();
  /**
   *
   *
   * <pre>
   * The progress, in documents, of this operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Progress progress_documents = 4;</code>
   *
   * @return The progressDocuments.
   */
  com.google.firestore.admin.v1.Progress getProgressDocuments();
  /**
   *
   *
   * <pre>
   * The progress, in documents, of this operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Progress progress_documents = 4;</code>
   */
  com.google.firestore.admin.v1.ProgressOrBuilder getProgressDocumentsOrBuilder();

  /**
   *
   *
   * <pre>
   * The progress, in bytes, of this operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Progress progress_bytes = 5;</code>
   *
   * @return Whether the progressBytes field is set.
   */
  boolean hasProgressBytes();
  /**
   *
   *
   * <pre>
   * The progress, in bytes, of this operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Progress progress_bytes = 5;</code>
   *
   * @return The progressBytes.
   */
  com.google.firestore.admin.v1.Progress getProgressBytes();
  /**
   *
   *
   * <pre>
   * The progress, in bytes, of this operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Progress progress_bytes = 5;</code>
   */
  com.google.firestore.admin.v1.ProgressOrBuilder getProgressBytesOrBuilder();

  /**
   *
   *
   * <pre>
   * Which collection IDs are being exported.
   * </pre>
   *
   * <code>repeated string collection_ids = 6;</code>
   *
   * @return A list containing the collectionIds.
   */
  java.util.List<java.lang.String> getCollectionIdsList();
  /**
   *
   *
   * <pre>
   * Which collection IDs are being exported.
   * </pre>
   *
   * <code>repeated string collection_ids = 6;</code>
   *
   * @return The count of collectionIds.
   */
  int getCollectionIdsCount();
  /**
   *
   *
   * <pre>
   * Which collection IDs are being exported.
   * </pre>
   *
   * <code>repeated string collection_ids = 6;</code>
   *
   * @param index The index of the element to return.
   * @return The collectionIds at the given index.
   */
  java.lang.String getCollectionIds(int index);
  /**
   *
   *
   * <pre>
   * Which collection IDs are being exported.
   * </pre>
   *
   * <code>repeated string collection_ids = 6;</code>
   *
   * @param index The index of the value to return.
   * @return The bytes of the collectionIds at the given index.
   */
  com.google.protobuf.ByteString getCollectionIdsBytes(int index);

  /**
   *
   *
   * <pre>
   * Where the documents are being exported to.
   * </pre>
   *
   * <code>string output_uri_prefix = 7;</code>
   *
   * @return The outputUriPrefix.
   */
  java.lang.String getOutputUriPrefix();
  /**
   *
   *
   * <pre>
   * Where the documents are being exported to.
   * </pre>
   *
   * <code>string output_uri_prefix = 7;</code>
   *
   * @return The bytes for outputUriPrefix.
   */
  com.google.protobuf.ByteString getOutputUriPrefixBytes();

  /**
   *
   *
   * <pre>
   * Which namespace IDs are being exported.
   * </pre>
   *
   * <code>repeated string namespace_ids = 8;</code>
   *
   * @return A list containing the namespaceIds.
   */
  java.util.List<java.lang.String> getNamespaceIdsList();
  /**
   *
   *
   * <pre>
   * Which namespace IDs are being exported.
   * </pre>
   *
   * <code>repeated string namespace_ids = 8;</code>
   *
   * @return The count of namespaceIds.
   */
  int getNamespaceIdsCount();
  /**
   *
   *
   * <pre>
   * Which namespace IDs are being exported.
   * </pre>
   *
   * <code>repeated string namespace_ids = 8;</code>
   *
   * @param index The index of the element to return.
   * @return The namespaceIds at the given index.
   */
  java.lang.String getNamespaceIds(int index);
  /**
   *
   *
   * <pre>
   * Which namespace IDs are being exported.
   * </pre>
   *
   * <code>repeated string namespace_ids = 8;</code>
   *
   * @param index The index of the value to return.
   * @return The bytes of the namespaceIds at the given index.
   */
  com.google.protobuf.ByteString getNamespaceIdsBytes(int index);

  /**
   *
   *
   * <pre>
   * The timestamp that corresponds to the version of the database that is being
   * exported. If unspecified, there are no guarantees about the consistency of
   * the documents being exported.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp snapshot_time = 9;</code>
   *
   * @return Whether the snapshotTime field is set.
   */
  boolean hasSnapshotTime();
  /**
   *
   *
   * <pre>
   * The timestamp that corresponds to the version of the database that is being
   * exported. If unspecified, there are no guarantees about the consistency of
   * the documents being exported.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp snapshot_time = 9;</code>
   *
   * @return The snapshotTime.
   */
  com.google.protobuf.Timestamp getSnapshotTime();
  /**
   *
   *
   * <pre>
   * The timestamp that corresponds to the version of the database that is being
   * exported. If unspecified, there are no guarantees about the consistency of
   * the documents being exported.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp snapshot_time = 9;</code>
   */
  com.google.protobuf.TimestampOrBuilder getSnapshotTimeOrBuilder();
}
