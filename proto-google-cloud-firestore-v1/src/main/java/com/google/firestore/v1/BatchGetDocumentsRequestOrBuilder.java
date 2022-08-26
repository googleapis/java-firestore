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
// source: google/firestore/v1/firestore.proto

package com.google.firestore.v1;

public interface BatchGetDocumentsRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.BatchGetDocumentsRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Required. The database name. In the format:
   * `projects/{project_id}/databases/{database_id}`.
   * </pre>
   *
   * <code>string database = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   * @return The database.
   */
  java.lang.String getDatabase();
  /**
   * <pre>
   * Required. The database name. In the format:
   * `projects/{project_id}/databases/{database_id}`.
   * </pre>
   *
   * <code>string database = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   * @return The bytes for database.
   */
  com.google.protobuf.ByteString
      getDatabaseBytes();

  /**
   * <pre>
   * The names of the documents to retrieve. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * The request will fail if any of the document is not a child resource of the
   * given `database`. Duplicate names will be elided.
   * </pre>
   *
   * <code>repeated string documents = 2;</code>
   * @return A list containing the documents.
   */
  java.util.List<java.lang.String>
      getDocumentsList();
  /**
   * <pre>
   * The names of the documents to retrieve. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * The request will fail if any of the document is not a child resource of the
   * given `database`. Duplicate names will be elided.
   * </pre>
   *
   * <code>repeated string documents = 2;</code>
   * @return The count of documents.
   */
  int getDocumentsCount();
  /**
   * <pre>
   * The names of the documents to retrieve. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * The request will fail if any of the document is not a child resource of the
   * given `database`. Duplicate names will be elided.
   * </pre>
   *
   * <code>repeated string documents = 2;</code>
   * @param index The index of the element to return.
   * @return The documents at the given index.
   */
  java.lang.String getDocuments(int index);
  /**
   * <pre>
   * The names of the documents to retrieve. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * The request will fail if any of the document is not a child resource of the
   * given `database`. Duplicate names will be elided.
   * </pre>
   *
   * <code>repeated string documents = 2;</code>
   * @param index The index of the value to return.
   * @return The bytes of the documents at the given index.
   */
  com.google.protobuf.ByteString
      getDocumentsBytes(int index);

  /**
   * <pre>
   * The fields to return. If not set, returns all fields.
   * If a document has a field that is not present in this mask, that field will
   * not be returned in the response.
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentMask mask = 3;</code>
   * @return Whether the mask field is set.
   */
  boolean hasMask();
  /**
   * <pre>
   * The fields to return. If not set, returns all fields.
   * If a document has a field that is not present in this mask, that field will
   * not be returned in the response.
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentMask mask = 3;</code>
   * @return The mask.
   */
  com.google.firestore.v1.DocumentMask getMask();
  /**
   * <pre>
   * The fields to return. If not set, returns all fields.
   * If a document has a field that is not present in this mask, that field will
   * not be returned in the response.
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentMask mask = 3;</code>
   */
  com.google.firestore.v1.DocumentMaskOrBuilder getMaskOrBuilder();

  /**
   * <pre>
   * Reads documents in a transaction.
   * </pre>
   *
   * <code>bytes transaction = 4;</code>
   * @return Whether the transaction field is set.
   */
  boolean hasTransaction();
  /**
   * <pre>
   * Reads documents in a transaction.
   * </pre>
   *
   * <code>bytes transaction = 4;</code>
   * @return The transaction.
   */
  com.google.protobuf.ByteString getTransaction();

  /**
   * <pre>
   * Starts a new transaction and reads the documents.
   * Defaults to a read-only transaction.
   * The new transaction ID will be returned as the first response in the
   * stream.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions new_transaction = 5;</code>
   * @return Whether the newTransaction field is set.
   */
  boolean hasNewTransaction();
  /**
   * <pre>
   * Starts a new transaction and reads the documents.
   * Defaults to a read-only transaction.
   * The new transaction ID will be returned as the first response in the
   * stream.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions new_transaction = 5;</code>
   * @return The newTransaction.
   */
  com.google.firestore.v1.TransactionOptions getNewTransaction();
  /**
   * <pre>
   * Starts a new transaction and reads the documents.
   * Defaults to a read-only transaction.
   * The new transaction ID will be returned as the first response in the
   * stream.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions new_transaction = 5;</code>
   */
  com.google.firestore.v1.TransactionOptionsOrBuilder getNewTransactionOrBuilder();

  /**
   * <pre>
   * Reads documents as they were at the given time.
   * This may not be older than 270 seconds.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 7;</code>
   * @return Whether the readTime field is set.
   */
  boolean hasReadTime();
  /**
   * <pre>
   * Reads documents as they were at the given time.
   * This may not be older than 270 seconds.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 7;</code>
   * @return The readTime.
   */
  com.google.protobuf.Timestamp getReadTime();
  /**
   * <pre>
   * Reads documents as they were at the given time.
   * This may not be older than 270 seconds.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 7;</code>
   */
  com.google.protobuf.TimestampOrBuilder getReadTimeOrBuilder();

  public com.google.firestore.v1.BatchGetDocumentsRequest.ConsistencySelectorCase getConsistencySelectorCase();
}
