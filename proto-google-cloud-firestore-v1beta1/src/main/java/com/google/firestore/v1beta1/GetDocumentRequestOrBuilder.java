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

public interface GetDocumentRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1beta1.GetDocumentRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The resource name of the Document to get. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * </pre>
   *
   * <code>string name = 1;</code>
   */
  java.lang.String getName();
  /**
   *
   *
   * <pre>
   * The resource name of the Document to get. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * </pre>
   *
   * <code>string name = 1;</code>
   */
  com.google.protobuf.ByteString getNameBytes();

  /**
   *
   *
   * <pre>
   * The fields to return. If not set, returns all fields.
   * If the document has a field that is not present in this mask, that field
   * will not be returned in the response.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.DocumentMask mask = 2;</code>
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
   * <code>.google.firestore.v1beta1.DocumentMask mask = 2;</code>
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
   * <code>.google.firestore.v1beta1.DocumentMask mask = 2;</code>
   */
  com.google.firestore.v1beta1.DocumentMaskOrBuilder getMaskOrBuilder();

  /**
   *
   *
   * <pre>
   * Reads the document in a transaction.
   * </pre>
   *
   * <code>bytes transaction = 3;</code>
   */
  com.google.protobuf.ByteString getTransaction();

  /**
   *
   *
   * <pre>
   * Reads the version of the document at the given time.
   * This may not be older than 60 seconds.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 5;</code>
   */
  boolean hasReadTime();
  /**
   *
   *
   * <pre>
   * Reads the version of the document at the given time.
   * This may not be older than 60 seconds.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 5;</code>
   */
  com.google.protobuf.Timestamp getReadTime();
  /**
   *
   *
   * <pre>
   * Reads the version of the document at the given time.
   * This may not be older than 60 seconds.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 5;</code>
   */
  com.google.protobuf.TimestampOrBuilder getReadTimeOrBuilder();

  public com.google.firestore.v1beta1.GetDocumentRequest.ConsistencySelectorCase
      getConsistencySelectorCase();
}
