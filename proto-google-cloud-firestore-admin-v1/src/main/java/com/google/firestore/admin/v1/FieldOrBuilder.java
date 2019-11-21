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
// source: google/firestore/admin/v1/field.proto

package com.google.firestore.admin.v1;

public interface FieldOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.admin.v1.Field)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * A field name of the form
   * `projects/{project_id}/databases/{database_id}/collectionGroups/{collection_id}/fields/{field_path}`
   * A field path may be a simple field name, e.g. `address` or a path to fields
   * within map_value , e.g. `address.city`,
   * or a special field path. The only valid special field is `*`, which
   * represents any field.
   * Field paths may be quoted using ` (backtick). The only character that needs
   * to be escaped within a quoted field path is the backtick character itself,
   * escaped using a backslash. Special characters in field paths that
   * must be quoted include: `*`, `.`,
   * ``` (backtick), `[`, `]`, as well as any ascii symbolic characters.
   * Examples:
   * (Note: Comments here are written in markdown syntax, so there is an
   *  additional layer of backticks to represent a code block)
   * `&#92;`address.city&#92;`` represents a field named `address.city`, not the map key
   * `city` in the field `address`.
   * `&#92;`*&#92;`` represents a field named `*`, not any field.
   * A special `Field` contains the default indexing settings for all fields.
   * This field's resource name is:
   * `projects/{project_id}/databases/{database_id}/collectionGroups/__default__/fields/&#42;`
   * Indexes defined on this `Field` will be applied to all fields which do not
   * have their own `Field` index configuration.
   * </pre>
   *
   * <code>string name = 1;</code>
   */
  java.lang.String getName();
  /**
   *
   *
   * <pre>
   * A field name of the form
   * `projects/{project_id}/databases/{database_id}/collectionGroups/{collection_id}/fields/{field_path}`
   * A field path may be a simple field name, e.g. `address` or a path to fields
   * within map_value , e.g. `address.city`,
   * or a special field path. The only valid special field is `*`, which
   * represents any field.
   * Field paths may be quoted using ` (backtick). The only character that needs
   * to be escaped within a quoted field path is the backtick character itself,
   * escaped using a backslash. Special characters in field paths that
   * must be quoted include: `*`, `.`,
   * ``` (backtick), `[`, `]`, as well as any ascii symbolic characters.
   * Examples:
   * (Note: Comments here are written in markdown syntax, so there is an
   *  additional layer of backticks to represent a code block)
   * `&#92;`address.city&#92;`` represents a field named `address.city`, not the map key
   * `city` in the field `address`.
   * `&#92;`*&#92;`` represents a field named `*`, not any field.
   * A special `Field` contains the default indexing settings for all fields.
   * This field's resource name is:
   * `projects/{project_id}/databases/{database_id}/collectionGroups/__default__/fields/&#42;`
   * Indexes defined on this `Field` will be applied to all fields which do not
   * have their own `Field` index configuration.
   * </pre>
   *
   * <code>string name = 1;</code>
   */
  com.google.protobuf.ByteString getNameBytes();

  /**
   *
   *
   * <pre>
   * The index configuration for this field. If unset, field indexing will
   * revert to the configuration defined by the `ancestor_field`. To
   * explicitly remove all indexes for this field, specify an index config
   * with an empty list of indexes.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Field.IndexConfig index_config = 2;</code>
   */
  boolean hasIndexConfig();
  /**
   *
   *
   * <pre>
   * The index configuration for this field. If unset, field indexing will
   * revert to the configuration defined by the `ancestor_field`. To
   * explicitly remove all indexes for this field, specify an index config
   * with an empty list of indexes.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Field.IndexConfig index_config = 2;</code>
   */
  com.google.firestore.admin.v1.Field.IndexConfig getIndexConfig();
  /**
   *
   *
   * <pre>
   * The index configuration for this field. If unset, field indexing will
   * revert to the configuration defined by the `ancestor_field`. To
   * explicitly remove all indexes for this field, specify an index config
   * with an empty list of indexes.
   * </pre>
   *
   * <code>.google.firestore.admin.v1.Field.IndexConfig index_config = 2;</code>
   */
  com.google.firestore.admin.v1.Field.IndexConfigOrBuilder getIndexConfigOrBuilder();
}
