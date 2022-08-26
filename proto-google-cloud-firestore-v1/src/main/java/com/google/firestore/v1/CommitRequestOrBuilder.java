// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/firestore/v1/firestore.proto

package com.google.firestore.v1;

public interface CommitRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.CommitRequest)
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
   * The writes to apply.
   * Always executed atomically and in order.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 2;</code>
   */
  java.util.List<com.google.firestore.v1.Write> 
      getWritesList();
  /**
   * <pre>
   * The writes to apply.
   * Always executed atomically and in order.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 2;</code>
   */
  com.google.firestore.v1.Write getWrites(int index);
  /**
   * <pre>
   * The writes to apply.
   * Always executed atomically and in order.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 2;</code>
   */
  int getWritesCount();
  /**
   * <pre>
   * The writes to apply.
   * Always executed atomically and in order.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 2;</code>
   */
  java.util.List<? extends com.google.firestore.v1.WriteOrBuilder> 
      getWritesOrBuilderList();
  /**
   * <pre>
   * The writes to apply.
   * Always executed atomically and in order.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Write writes = 2;</code>
   */
  com.google.firestore.v1.WriteOrBuilder getWritesOrBuilder(
      int index);

  /**
   * <pre>
   * If set, applies all writes in this transaction, and commits it.
   * </pre>
   *
   * <code>bytes transaction = 3;</code>
   * @return The transaction.
   */
  com.google.protobuf.ByteString getTransaction();
}
