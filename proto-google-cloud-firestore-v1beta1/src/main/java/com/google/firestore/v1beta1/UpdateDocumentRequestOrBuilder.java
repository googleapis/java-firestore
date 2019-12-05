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
// source: google/firestore/v1beta1/firestore.proto

package com.google.firestore.v1beta1;

public interface UpdateDocumentRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1beta1.UpdateDocumentRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The updated document.
   * Creates the document if it does not already exist.
   * </pre>
   *
   * <code>
   * .google.firestore.v1beta1.Document document = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  boolean hasDocument();
  /**
   *
   *
   * <pre>
   * Required. The updated document.
   * Creates the document if it does not already exist.
   * </pre>
   *
   * <code>
   * .google.firestore.v1beta1.Document document = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.firestore.v1beta1.Document getDocument();
  /**
   *
   *
   * <pre>
   * Required. The updated document.
   * Creates the document if it does not already exist.
   * </pre>
   *
   * <code>
   * .google.firestore.v1beta1.Document document = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.firestore.v1beta1.DocumentOrBuilder getDocumentOrBuilder();

  /**
   *
   *
   * <pre>
   * The fields to update.
   * None of the field paths in the mask may contain a reserved name.
   * If the document exists on the server and has fields not referenced in the
   * mask, they are left unchanged.
   * Fields referenced in the mask, but not present in the input document, are
   * deleted from the document on the server.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.DocumentMask update_mask = 2;</code>
   */
  boolean hasUpdateMask();
  /**
   *
   *
   * <pre>
   * The fields to update.
   * None of the field paths in the mask may contain a reserved name.
   * If the document exists on the server and has fields not referenced in the
   * mask, they are left unchanged.
   * Fields referenced in the mask, but not present in the input document, are
   * deleted from the document on the server.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.DocumentMask update_mask = 2;</code>
   */
  com.google.firestore.v1beta1.DocumentMask getUpdateMask();
  /**
   *
   *
   * <pre>
   * The fields to update.
   * None of the field paths in the mask may contain a reserved name.
   * If the document exists on the server and has fields not referenced in the
   * mask, they are left unchanged.
   * Fields referenced in the mask, but not present in the input document, are
   * deleted from the document on the server.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.DocumentMask update_mask = 2;</code>
   */
  com.google.firestore.v1beta1.DocumentMaskOrBuilder getUpdateMaskOrBuilder();

  /**
   *
   *
   * <pre>
   * The fields to return. If not set, returns all fields.
   * If the document has a field that is not present in this mask, that field
   * will not be returned in the response.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.DocumentMask mask = 3;</code>
   */
  boolean hasMask();
  /**
   *
   *
   * <pre>
   * The fields to return. If not set, returns all fields.
   * If the document has a field that is not present in this mask, that field
   * will not be returned in the response.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.DocumentMask mask = 3;</code>
   */
  com.google.firestore.v1beta1.DocumentMask getMask();
  /**
   *
   *
   * <pre>
   * The fields to return. If not set, returns all fields.
   * If the document has a field that is not present in this mask, that field
   * will not be returned in the response.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.DocumentMask mask = 3;</code>
   */
  com.google.firestore.v1beta1.DocumentMaskOrBuilder getMaskOrBuilder();

  /**
   *
   *
   * <pre>
   * An optional precondition on the document.
   * The request will fail if this is set and not met by the target document.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.Precondition current_document = 4;</code>
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
   * <code>.google.firestore.v1beta1.Precondition current_document = 4;</code>
   */
  com.google.firestore.v1beta1.Precondition getCurrentDocument();
  /**
   *
   *
   * <pre>
   * An optional precondition on the document.
   * The request will fail if this is set and not met by the target document.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.Precondition current_document = 4;</code>
   */
  com.google.firestore.v1beta1.PreconditionOrBuilder getCurrentDocumentOrBuilder();
}
