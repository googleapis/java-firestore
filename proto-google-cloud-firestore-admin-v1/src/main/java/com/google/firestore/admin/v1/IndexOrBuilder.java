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
// source: google/firestore/admin/v1/index.proto

package com.google.firestore.admin.v1;

public interface IndexOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.admin.v1.Index)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Output only. A server defined name for this index.
   * The form of this name for composite indexes will be:
   * `projects/{project_id}/databases/{database_id}/collectionGroups/{collection_id}/indexes/{composite_index_id}`
   * For single field indexes, this field will be empty.
   * </pre>
   *
   * <code>string name = 1;</code>
   *
   * @return The name.
   */
  java.lang.String getName();
  /**
   *
   *
   * <pre>
   * Output only. A server defined name for this index.
   * The form of this name for composite indexes will be:
   * `projects/{project_id}/databases/{database_id}/collectionGroups/{collection_id}/indexes/{composite_index_id}`
   * For single field indexes, this field will be empty.
   * </pre>
   *
   * <code>string name = 1;</code>
   *
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString getNameBytes();

  /**
   *
   *
   * <pre>
   * Indexes with a collection query scope specified allow queries
   * against a collection that is the child of a specific document, specified at
   * query time, and that has the same collection id.
   * Indexes with a collection group query scope specified allow queries against
   * all collections descended from a specific document, specified at query
   * time, and that have the same collection id as this index.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Index.QueryScope query_scope = 2;</code>
   *
   * @return The enum numeric value on the wire for queryScope.
   */
  int getQueryScopeValue();
  /**
   *
   *
   * <pre>
   * Indexes with a collection query scope specified allow queries
   * against a collection that is the child of a specific document, specified at
   * query time, and that has the same collection id.
   * Indexes with a collection group query scope specified allow queries against
   * all collections descended from a specific document, specified at query
   * time, and that have the same collection id as this index.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Index.QueryScope query_scope = 2;</code>
   *
   * @return The queryScope.
   */
  com.google.firestore.admin.v1.Index.QueryScope getQueryScope();

  /**
   *
   *
   * <pre>
   * The fields supported by this index.
   * For composite indexes, this is always 2 or more fields.
   * The last field entry is always for the field path `__name__`. If, on
   * creation, `__name__` was not specified as the last field, it will be added
   * automatically with the same direction as that of the last field defined. If
   * the final field in a composite index is not directional, the `__name__`
   * will be ordered ASCENDING (unless explicitly specified).
   * For single field indexes, this will always be exactly one entry with a
   * field path equal to the field path of the associated field.
   * </pre>
   *
   * <code>repeated .google.firestore.admin.v1.Index.IndexField fields = 3;</code>
   */
  java.util.List<com.google.firestore.admin.v1.Index.IndexField> getFieldsList();
  /**
   *
   *
   * <pre>
   * The fields supported by this index.
   * For composite indexes, this is always 2 or more fields.
   * The last field entry is always for the field path `__name__`. If, on
   * creation, `__name__` was not specified as the last field, it will be added
   * automatically with the same direction as that of the last field defined. If
   * the final field in a composite index is not directional, the `__name__`
   * will be ordered ASCENDING (unless explicitly specified).
   * For single field indexes, this will always be exactly one entry with a
   * field path equal to the field path of the associated field.
   * </pre>
   *
   * <code>repeated .google.firestore.admin.v1.Index.IndexField fields = 3;</code>
   */
  com.google.firestore.admin.v1.Index.IndexField getFields(int index);
  /**
   *
   *
   * <pre>
   * The fields supported by this index.
   * For composite indexes, this is always 2 or more fields.
   * The last field entry is always for the field path `__name__`. If, on
   * creation, `__name__` was not specified as the last field, it will be added
   * automatically with the same direction as that of the last field defined. If
   * the final field in a composite index is not directional, the `__name__`
   * will be ordered ASCENDING (unless explicitly specified).
   * For single field indexes, this will always be exactly one entry with a
   * field path equal to the field path of the associated field.
   * </pre>
   *
   * <code>repeated .google.firestore.admin.v1.Index.IndexField fields = 3;</code>
   */
  int getFieldsCount();
  /**
   *
   *
   * <pre>
   * The fields supported by this index.
   * For composite indexes, this is always 2 or more fields.
   * The last field entry is always for the field path `__name__`. If, on
   * creation, `__name__` was not specified as the last field, it will be added
   * automatically with the same direction as that of the last field defined. If
   * the final field in a composite index is not directional, the `__name__`
   * will be ordered ASCENDING (unless explicitly specified).
   * For single field indexes, this will always be exactly one entry with a
   * field path equal to the field path of the associated field.
   * </pre>
   *
   * <code>repeated .google.firestore.admin.v1.Index.IndexField fields = 3;</code>
   */
  java.util.List<? extends com.google.firestore.admin.v1.Index.IndexFieldOrBuilder>
      getFieldsOrBuilderList();
  /**
   *
   *
   * <pre>
   * The fields supported by this index.
   * For composite indexes, this is always 2 or more fields.
   * The last field entry is always for the field path `__name__`. If, on
   * creation, `__name__` was not specified as the last field, it will be added
   * automatically with the same direction as that of the last field defined. If
   * the final field in a composite index is not directional, the `__name__`
   * will be ordered ASCENDING (unless explicitly specified).
   * For single field indexes, this will always be exactly one entry with a
   * field path equal to the field path of the associated field.
   * </pre>
   *
   * <code>repeated .google.firestore.admin.v1.Index.IndexField fields = 3;</code>
   */
  com.google.firestore.admin.v1.Index.IndexFieldOrBuilder getFieldsOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * Output only. The serving state of the index.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Index.State state = 4;</code>
   *
   * @return The enum numeric value on the wire for state.
   */
  int getStateValue();
  /**
   *
   *
   * <pre>
   * Output only. The serving state of the index.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Index.State state = 4;</code>
   *
   * @return The state.
   */
  com.google.firestore.admin.v1.Index.State getState();
}
