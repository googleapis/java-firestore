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

// Protobuf Java Version: 3.25.5
package com.google.firestore.v1;

public interface DeleteDocumentRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.DeleteDocumentRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The resource name of the Document to delete. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * </pre>
   *
   * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The name.
   */
  java.lang.String getName();
  /**
   *
   *
   * <pre>
   * Required. The resource name of the Document to delete. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * </pre>
   *
   * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString getNameBytes();

  /**
   *
   *
   * <pre>
   * An optional precondition on the document.
   * The request will fail if this is set and not met by the target document.
   * </pre>
   *
   * <code>.google.firestore.v1.Precondition current_document = 2;</code>
   *
   * @return Whether the currentDocument field is set.
   */
  boolean hasCurrentDocument();
  /**
   *
   *
   * <pre>
   * An optional precondition on the document.
   * The request will fail if this is set and not met by the target document.
   * </pre>
   *
   * <code>.google.firestore.v1.Precondition current_document = 2;</code>
   *
   * @return The currentDocument.
   */
  com.google.firestore.v1.Precondition getCurrentDocument();
  /**
   *
   *
   * <pre>
   * An optional precondition on the document.
   * The request will fail if this is set and not met by the target document.
   * </pre>
   *
   * <code>.google.firestore.v1.Precondition current_document = 2;</code>
   */
  com.google.firestore.v1.PreconditionOrBuilder getCurrentDocumentOrBuilder();
}
