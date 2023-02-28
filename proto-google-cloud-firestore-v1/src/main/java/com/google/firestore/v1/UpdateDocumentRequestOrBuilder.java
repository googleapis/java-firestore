// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/firestore/v1/firestore.proto

package com.google.firestore.v1;

public interface UpdateDocumentRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.UpdateDocumentRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The updated document.
   * Creates the document if it does not already exist.
   * </pre>
   *
   * <code>.google.firestore.v1.Document document = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return Whether the document field is set.
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
   * <code>.google.firestore.v1.Document document = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return The document.
   */
  com.google.firestore.v1.Document getDocument();
  /**
   *
   *
   * <pre>
   * Required. The updated document.
   * Creates the document if it does not already exist.
   * </pre>
   *
   * <code>.google.firestore.v1.Document document = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.firestore.v1.DocumentOrBuilder getDocumentOrBuilder();

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
   * <code>.google.firestore.v1.DocumentMask update_mask = 2;</code>
   *
   * @return Whether the updateMask field is set.
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
   * <code>.google.firestore.v1.DocumentMask update_mask = 2;</code>
   *
   * @return The updateMask.
   */
  com.google.firestore.v1.DocumentMask getUpdateMask();
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
   * <code>.google.firestore.v1.DocumentMask update_mask = 2;</code>
   */
  com.google.firestore.v1.DocumentMaskOrBuilder getUpdateMaskOrBuilder();

  /**
   *
   *
   * <pre>
   * The fields to return. If not set, returns all fields.
   * If the document has a field that is not present in this mask, that field
   * will not be returned in the response.
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentMask mask = 3;</code>
   *
   * @return Whether the mask field is set.
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
   * <code>.google.firestore.v1.DocumentMask mask = 3;</code>
   *
   * @return The mask.
   */
  com.google.firestore.v1.DocumentMask getMask();
  /**
   *
   *
   * <pre>
   * The fields to return. If not set, returns all fields.
   * If the document has a field that is not present in this mask, that field
   * will not be returned in the response.
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentMask mask = 3;</code>
   */
  com.google.firestore.v1.DocumentMaskOrBuilder getMaskOrBuilder();

  /**
   *
   *
   * <pre>
   * An optional precondition on the document.
   * The request will fail if this is set and not met by the target document.
   * </pre>
   *
   * <code>.google.firestore.v1.Precondition current_document = 4;</code>
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
   * <code>.google.firestore.v1.Precondition current_document = 4;</code>
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
   * <code>.google.firestore.v1.Precondition current_document = 4;</code>
   */
  com.google.firestore.v1.PreconditionOrBuilder getCurrentDocumentOrBuilder();
}
