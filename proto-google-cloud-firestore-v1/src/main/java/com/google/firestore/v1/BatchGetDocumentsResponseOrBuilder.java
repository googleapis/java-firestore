/*
 * Copyright 2023 Google LLC
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

public interface BatchGetDocumentsResponseOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.BatchGetDocumentsResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * A document that was requested.
   * </pre>
   *
   * <code>.google.firestore.v1.Document found = 1;</code>
   *
   * @return Whether the found field is set.
   */
  boolean hasFound();
  /**
   *
   *
   * <pre>
   * A document that was requested.
   * </pre>
   *
   * <code>.google.firestore.v1.Document found = 1;</code>
   *
   * @return The found.
   */
  com.google.firestore.v1.Document getFound();
  /**
   *
   *
   * <pre>
   * A document that was requested.
   * </pre>
   *
   * <code>.google.firestore.v1.Document found = 1;</code>
   */
  com.google.firestore.v1.DocumentOrBuilder getFoundOrBuilder();

  /**
   *
   *
   * <pre>
   * A document name that was requested but does not exist. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * </pre>
   *
   * <code>string missing = 2;</code>
   *
   * @return Whether the missing field is set.
   */
  boolean hasMissing();
  /**
   *
   *
   * <pre>
   * A document name that was requested but does not exist. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * </pre>
   *
   * <code>string missing = 2;</code>
   *
   * @return The missing.
   */
  java.lang.String getMissing();
  /**
   *
   *
   * <pre>
   * A document name that was requested but does not exist. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * </pre>
   *
   * <code>string missing = 2;</code>
   *
   * @return The bytes for missing.
   */
  com.google.protobuf.ByteString getMissingBytes();

  /**
   *
   *
   * <pre>
   * The transaction that was started as part of this request.
   * Will only be set in the first response, and only if
   * [BatchGetDocumentsRequest.new_transaction][google.firestore.v1.BatchGetDocumentsRequest.new_transaction]
   * was set in the request.
   * </pre>
   *
   * <code>bytes transaction = 3;</code>
   *
   * @return The transaction.
   */
  com.google.protobuf.ByteString getTransaction();

  /**
   *
   *
   * <pre>
   * The time at which the document was read.
   * This may be monotically increasing, in this case the previous documents in
   * the result stream are guaranteed not to have changed between their
   * read_time and this one.
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
   * The time at which the document was read.
   * This may be monotically increasing, in this case the previous documents in
   * the result stream are guaranteed not to have changed between their
   * read_time and this one.
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
   * The time at which the document was read.
   * This may be monotically increasing, in this case the previous documents in
   * the result stream are guaranteed not to have changed between their
   * read_time and this one.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 4;</code>
   */
  com.google.protobuf.TimestampOrBuilder getReadTimeOrBuilder();

  com.google.firestore.v1.BatchGetDocumentsResponse.ResultCase getResultCase();
}
