/*
 * Copyright 2019 Google LLC
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

package com.google.firestore.admin.v1;

public interface FieldOperationMetadataOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.admin.v1.FieldOperationMetadata)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The time this operation started.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp start_time = 1;</code>
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
   * The field resource that this operation is acting on. For example:
   * `projects/{project_id}/databases/{database_id}/collectionGroups/{collection_id}/fields/{field_path}`
   * </pre>
   *
   * <code>string field = 3;</code>
   */
  java.lang.String getField();
  /**
   *
   *
   * <pre>
   * The field resource that this operation is acting on. For example:
   * `projects/{project_id}/databases/{database_id}/collectionGroups/{collection_id}/fields/{field_path}`
   * </pre>
   *
   * <code>string field = 3;</code>
   */
  com.google.protobuf.ByteString getFieldBytes();

  /**
   *
   *
   * <pre>
   * A list of [IndexConfigDelta][google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDelta], which describe the intent of this
   * operation.
   * </pre>
   *
   * <code>
   * repeated .google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDelta index_config_deltas = 4;
   * </code>
   */
  java.util.List<com.google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDelta>
      getIndexConfigDeltasList();
  /**
   *
   *
   * <pre>
   * A list of [IndexConfigDelta][google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDelta], which describe the intent of this
   * operation.
   * </pre>
   *
   * <code>
   * repeated .google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDelta index_config_deltas = 4;
   * </code>
   */
  com.google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDelta getIndexConfigDeltas(
      int index);
  /**
   *
   *
   * <pre>
   * A list of [IndexConfigDelta][google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDelta], which describe the intent of this
   * operation.
   * </pre>
   *
   * <code>
   * repeated .google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDelta index_config_deltas = 4;
   * </code>
   */
  int getIndexConfigDeltasCount();
  /**
   *
   *
   * <pre>
   * A list of [IndexConfigDelta][google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDelta], which describe the intent of this
   * operation.
   * </pre>
   *
   * <code>
   * repeated .google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDelta index_config_deltas = 4;
   * </code>
   */
  java.util.List<
          ? extends com.google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDeltaOrBuilder>
      getIndexConfigDeltasOrBuilderList();
  /**
   *
   *
   * <pre>
   * A list of [IndexConfigDelta][google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDelta], which describe the intent of this
   * operation.
   * </pre>
   *
   * <code>
   * repeated .google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDelta index_config_deltas = 4;
   * </code>
   */
  com.google.firestore.admin.v1.FieldOperationMetadata.IndexConfigDeltaOrBuilder
      getIndexConfigDeltasOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * The state of the operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.OperationState state = 5;</code>
   */
  int getStateValue();
  /**
   *
   *
   * <pre>
   * The state of the operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.OperationState state = 5;</code>
   */
  com.google.firestore.admin.v1.OperationState getState();

  /**
   *
   *
   * <pre>
   * The progress, in documents, of this operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Progress progress_documents = 6;</code>
   */
  boolean hasProgressDocuments();
  /**
   *
   *
   * <pre>
   * The progress, in documents, of this operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Progress progress_documents = 6;</code>
   */
  com.google.firestore.admin.v1.Progress getProgressDocuments();
  /**
   *
   *
   * <pre>
   * The progress, in documents, of this operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Progress progress_documents = 6;</code>
   */
  com.google.firestore.admin.v1.ProgressOrBuilder getProgressDocumentsOrBuilder();

  /**
   *
   *
   * <pre>
   * The progress, in bytes, of this operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Progress progress_bytes = 7;</code>
   */
  boolean hasProgressBytes();
  /**
   *
   *
   * <pre>
   * The progress, in bytes, of this operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Progress progress_bytes = 7;</code>
   */
  com.google.firestore.admin.v1.Progress getProgressBytes();
  /**
   *
   *
   * <pre>
   * The progress, in bytes, of this operation.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Progress progress_bytes = 7;</code>
   */
  com.google.firestore.admin.v1.ProgressOrBuilder getProgressBytesOrBuilder();
}
