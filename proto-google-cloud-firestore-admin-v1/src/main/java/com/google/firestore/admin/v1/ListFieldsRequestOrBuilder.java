/*
 * Copyright 2025 Google LLC
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

// Protobuf Java Version: 3.25.5
package com.google.firestore.admin.v1;

public interface ListFieldsRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.admin.v1.ListFieldsRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. A parent name of the form
   * `projects/{project_id}/databases/{database_id}/collectionGroups/{collection_id}`
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
   * `projects/{project_id}/databases/{database_id}/collectionGroups/{collection_id}`
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
   * The filter to apply to list results. Currently,
   * [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields]
   * only supports listing fields that have been explicitly overridden. To issue
   * this query, call
   * [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields]
   * with a filter that includes `indexConfig.usesAncestorConfig:false` or
   * `ttlConfig:*`.
   * </pre>
   *
   * <code>string filter = 2;</code>
   *
   * @return The filter.
   */
  java.lang.String getFilter();

  /**
   *
   *
   * <pre>
   * The filter to apply to list results. Currently,
   * [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields]
   * only supports listing fields that have been explicitly overridden. To issue
   * this query, call
   * [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields]
   * with a filter that includes `indexConfig.usesAncestorConfig:false` or
   * `ttlConfig:*`.
   * </pre>
   *
   * <code>string filter = 2;</code>
   *
   * @return The bytes for filter.
   */
  com.google.protobuf.ByteString getFilterBytes();

  /**
   *
   *
   * <pre>
   * The number of results to return.
   * </pre>
   *
   * <code>int32 page_size = 3;</code>
   *
   * @return The pageSize.
   */
  int getPageSize();

  /**
   *
   *
   * <pre>
   * A page token, returned from a previous call to
   * [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields],
   * that may be used to get the next page of results.
   * </pre>
   *
   * <code>string page_token = 4;</code>
   *
   * @return The pageToken.
   */
  java.lang.String getPageToken();

  /**
   *
   *
   * <pre>
   * A page token, returned from a previous call to
   * [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields],
   * that may be used to get the next page of results.
   * </pre>
   *
   * <code>string page_token = 4;</code>
   *
   * @return The bytes for pageToken.
   */
  com.google.protobuf.ByteString getPageTokenBytes();
}
