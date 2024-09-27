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

public interface RunQueryRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.RunQueryRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The parent resource name. In the format:
   * `projects/{project_id}/databases/{database_id}/documents` or
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * For example:
   * `projects/my-project/databases/my-database/documents` or
   * `projects/my-project/databases/my-database/documents/chatrooms/my-chatroom`
   * </pre>
   *
   * <code>string parent = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The parent.
   */
  java.lang.String getParent();
  /**
   *
   *
   * <pre>
   * Required. The parent resource name. In the format:
   * `projects/{project_id}/databases/{database_id}/documents` or
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * For example:
   * `projects/my-project/databases/my-database/documents` or
   * `projects/my-project/databases/my-database/documents/chatrooms/my-chatroom`
   * </pre>
   *
   * <code>string parent = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The bytes for parent.
   */
  com.google.protobuf.ByteString getParentBytes();

  /**
   *
   *
   * <pre>
   * A structured query.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
   *
   * @return Whether the structuredQuery field is set.
   */
  boolean hasStructuredQuery();
  /**
   *
   *
   * <pre>
   * A structured query.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
   *
   * @return The structuredQuery.
   */
  com.google.firestore.v1.StructuredQuery getStructuredQuery();
  /**
   *
   *
   * <pre>
   * A structured query.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
   */
  com.google.firestore.v1.StructuredQueryOrBuilder getStructuredQueryOrBuilder();

  /**
   *
   *
   * <pre>
   * Run the query within an already active transaction.
   *
   * The value here is the opaque transaction ID to execute the query in.
   * </pre>
   *
   * <code>bytes transaction = 5;</code>
   *
   * @return Whether the transaction field is set.
   */
  boolean hasTransaction();
  /**
   *
   *
   * <pre>
   * Run the query within an already active transaction.
   *
   * The value here is the opaque transaction ID to execute the query in.
   * </pre>
   *
   * <code>bytes transaction = 5;</code>
   *
   * @return The transaction.
   */
  com.google.protobuf.ByteString getTransaction();

  /**
   *
   *
   * <pre>
   * Starts a new transaction and reads the documents.
   * Defaults to a read-only transaction.
   * The new transaction ID will be returned as the first response in the
   * stream.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions new_transaction = 6;</code>
   *
   * @return Whether the newTransaction field is set.
   */
  boolean hasNewTransaction();
  /**
   *
   *
   * <pre>
   * Starts a new transaction and reads the documents.
   * Defaults to a read-only transaction.
   * The new transaction ID will be returned as the first response in the
   * stream.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions new_transaction = 6;</code>
   *
   * @return The newTransaction.
   */
  com.google.firestore.v1.TransactionOptions getNewTransaction();
  /**
   *
   *
   * <pre>
   * Starts a new transaction and reads the documents.
   * Defaults to a read-only transaction.
   * The new transaction ID will be returned as the first response in the
   * stream.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions new_transaction = 6;</code>
   */
  com.google.firestore.v1.TransactionOptionsOrBuilder getNewTransactionOrBuilder();

  /**
   *
   *
   * <pre>
   * Reads documents as they were at the given time.
   *
   * This must be a microsecond precision timestamp within the past one hour,
   * or if Point-in-Time Recovery is enabled, can additionally be a whole
   * minute timestamp within the past 7 days.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 7;</code>
   *
   * @return Whether the readTime field is set.
   */
  boolean hasReadTime();
  /**
   *
   *
   * <pre>
   * Reads documents as they were at the given time.
   *
   * This must be a microsecond precision timestamp within the past one hour,
   * or if Point-in-Time Recovery is enabled, can additionally be a whole
   * minute timestamp within the past 7 days.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 7;</code>
   *
   * @return The readTime.
   */
  com.google.protobuf.Timestamp getReadTime();
  /**
   *
   *
   * <pre>
   * Reads documents as they were at the given time.
   *
   * This must be a microsecond precision timestamp within the past one hour,
   * or if Point-in-Time Recovery is enabled, can additionally be a whole
   * minute timestamp within the past 7 days.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 7;</code>
   */
  com.google.protobuf.TimestampOrBuilder getReadTimeOrBuilder();

  /**
   *
   *
   * <pre>
   * Optional. Explain options for the query. If set, additional query
   * statistics will be returned. If not, only query results will be returned.
   * </pre>
   *
   * <code>
   * .google.firestore.v1.ExplainOptions explain_options = 10 [(.google.api.field_behavior) = OPTIONAL];
   * </code>
   *
   * @return Whether the explainOptions field is set.
   */
  boolean hasExplainOptions();
  /**
   *
   *
   * <pre>
   * Optional. Explain options for the query. If set, additional query
   * statistics will be returned. If not, only query results will be returned.
   * </pre>
   *
   * <code>
   * .google.firestore.v1.ExplainOptions explain_options = 10 [(.google.api.field_behavior) = OPTIONAL];
   * </code>
   *
   * @return The explainOptions.
   */
  com.google.firestore.v1.ExplainOptions getExplainOptions();
  /**
   *
   *
   * <pre>
   * Optional. Explain options for the query. If set, additional query
   * statistics will be returned. If not, only query results will be returned.
   * </pre>
   *
   * <code>
   * .google.firestore.v1.ExplainOptions explain_options = 10 [(.google.api.field_behavior) = OPTIONAL];
   * </code>
   */
  com.google.firestore.v1.ExplainOptionsOrBuilder getExplainOptionsOrBuilder();

  com.google.firestore.v1.RunQueryRequest.QueryTypeCase getQueryTypeCase();

  com.google.firestore.v1.RunQueryRequest.ConsistencySelectorCase getConsistencySelectorCase();
}
