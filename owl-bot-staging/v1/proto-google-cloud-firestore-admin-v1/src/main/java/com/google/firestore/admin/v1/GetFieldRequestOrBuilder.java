// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/firestore/admin/v1/firestore_admin.proto

// Protobuf Java Version: 3.25.3
package com.google.firestore.admin.v1;

public interface GetFieldRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.firestore.admin.v1.GetFieldRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Required. A name of the form
   * `projects/{project_id}/databases/{database_id}/collectionGroups/{collection_id}/fields/{field_id}`
   * </pre>
   *
   * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <pre>
   * Required. A name of the form
   * `projects/{project_id}/databases/{database_id}/collectionGroups/{collection_id}/fields/{field_id}`
   * </pre>
   *
   * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();
}
