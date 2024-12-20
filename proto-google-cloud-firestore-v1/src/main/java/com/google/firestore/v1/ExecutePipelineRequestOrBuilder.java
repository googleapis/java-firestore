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

// Protobuf Java Version: 3.25.2
package com.google.firestore.v1;

public interface ExecutePipelineRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.ExecutePipelineRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Database identifier, in the form `projects/{project}/databases/{database}`.
   * </pre>
   *
   * <code>string database = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The database.
   */
  java.lang.String getDatabase();
  /**
   *
   *
   * <pre>
   * Database identifier, in the form `projects/{project}/databases/{database}`.
   * </pre>
   *
   * <code>string database = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The bytes for database.
   */
  com.google.protobuf.ByteString getDatabaseBytes();

  /**
   *
   *
   * <pre>
   * A pipelined operation.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredPipeline structured_pipeline = 2;</code>
   *
   * @return Whether the structuredPipeline field is set.
   */
  boolean hasStructuredPipeline();
  /**
   *
   *
   * <pre>
   * A pipelined operation.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredPipeline structured_pipeline = 2;</code>
   *
   * @return The structuredPipeline.
   */
  com.google.firestore.v1.StructuredPipeline getStructuredPipeline();
  /**
   *
   *
   * <pre>
   * A pipelined operation.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredPipeline structured_pipeline = 2;</code>
   */
  com.google.firestore.v1.StructuredPipelineOrBuilder getStructuredPipelineOrBuilder();

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
   * Execute the pipeline in a new transaction.
   *
   * The identifier of the newly created transaction will be returned in the
   * first response on the stream. This defaults to a read-only transaction.
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
   * Execute the pipeline in a new transaction.
   *
   * The identifier of the newly created transaction will be returned in the
   * first response on the stream. This defaults to a read-only transaction.
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
   * Execute the pipeline in a new transaction.
   *
   * The identifier of the newly created transaction will be returned in the
   * first response on the stream. This defaults to a read-only transaction.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions new_transaction = 6;</code>
   */
  com.google.firestore.v1.TransactionOptionsOrBuilder getNewTransactionOrBuilder();

  /**
   *
   *
   * <pre>
   * Execute the pipeline in a snapshot transaction at the given time.
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
   * Execute the pipeline in a snapshot transaction at the given time.
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
   * Execute the pipeline in a snapshot transaction at the given time.
   *
   * This must be a microsecond precision timestamp within the past one hour,
   * or if Point-in-Time Recovery is enabled, can additionally be a whole
   * minute timestamp within the past 7 days.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 7;</code>
   */
  com.google.protobuf.TimestampOrBuilder getReadTimeOrBuilder();

  com.google.firestore.v1.ExecutePipelineRequest.PipelineTypeCase getPipelineTypeCase();

  com.google.firestore.v1.ExecutePipelineRequest.ConsistencySelectorCase
      getConsistencySelectorCase();
}