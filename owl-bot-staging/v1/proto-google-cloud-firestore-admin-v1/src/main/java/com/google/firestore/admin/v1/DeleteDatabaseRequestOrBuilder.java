// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/firestore/admin/v1/firestore_admin.proto

package com.google.firestore.admin.v1;

public interface DeleteDatabaseRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.firestore.admin.v1.DeleteDatabaseRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Required. A name of the form
   * `projects/{project_id}/databases/{database_id}`
   * </pre>
   *
   * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <pre>
   * Required. A name of the form
   * `projects/{project_id}/databases/{database_id}`
   * </pre>
   *
   * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <pre>
   * The current etag of the Database.
   * If an etag is provided and does not match the current etag of the database,
   * deletion will be blocked and a FAILED_PRECONDITION error will be returned.
   * </pre>
   *
   * <code>string etag = 3;</code>
   * @return The etag.
   */
  java.lang.String getEtag();
  /**
   * <pre>
   * The current etag of the Database.
   * If an etag is provided and does not match the current etag of the database,
   * deletion will be blocked and a FAILED_PRECONDITION error will be returned.
   * </pre>
   *
   * <code>string etag = 3;</code>
   * @return The bytes for etag.
   */
  com.google.protobuf.ByteString
      getEtagBytes();
}
