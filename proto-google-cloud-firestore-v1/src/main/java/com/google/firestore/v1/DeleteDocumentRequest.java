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
// source: google/firestore/v1/firestore.proto

package com.google.firestore.v1;

/**
 *
 *
 * <pre>
 * The request for [Firestore.DeleteDocument][google.firestore.v1.Firestore.DeleteDocument].
 * </pre>
 *
 * Protobuf type {@code google.firestore.v1.DeleteDocumentRequest}
 */
public final class DeleteDocumentRequest extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.firestore.v1.DeleteDocumentRequest)
    DeleteDocumentRequestOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use DeleteDocumentRequest.newBuilder() to construct.
  private DeleteDocumentRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private DeleteDocumentRequest() {
    name_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new DeleteDocumentRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
    return this.unknownFields;
  }

  private DeleteDocumentRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10:
            {
              java.lang.String s = input.readStringRequireUtf8();

              name_ = s;
              break;
            }
          case 18:
            {
              com.google.firestore.v1.Precondition.Builder subBuilder = null;
              if (currentDocument_ != null) {
                subBuilder = currentDocument_.toBuilder();
              }
              currentDocument_ =
                  input.readMessage(
                      com.google.firestore.v1.Precondition.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(currentDocument_);
                currentDocument_ = subBuilder.buildPartial();
              }

              break;
            }
          default:
            {
              if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.firestore.v1.FirestoreProto
        .internal_static_google_firestore_v1_DeleteDocumentRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.v1.FirestoreProto
        .internal_static_google_firestore_v1_DeleteDocumentRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.v1.DeleteDocumentRequest.class,
            com.google.firestore.v1.DeleteDocumentRequest.Builder.class);
  }

  public static final int NAME_FIELD_NUMBER = 1;
  private volatile java.lang.Object name_;
  /**
   *
   *
   * <pre>
   * Required. The resource name of the Document to delete. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * </pre>
   *
   * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The name.
   */
  @java.lang.Override
  public java.lang.String getName() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      name_ = s;
      return s;
    }
  }
  /**
   *
   *
   * <pre>
   * Required. The resource name of the Document to delete. In the format:
   * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
   * </pre>
   *
   * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The bytes for name.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getNameBytes() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
      name_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int CURRENT_DOCUMENT_FIELD_NUMBER = 2;
  private com.google.firestore.v1.Precondition currentDocument_;
  /**
   *
   *
   * <pre>
   * An optional precondition on the document.
   * The request will fail if this is set and not met by the target document.
   * </pre>
   *
   * <code>.google.firestore.v1.Precondition current_document = 2;</code>
   *
   * @return Whether the currentDocument field is set.
   */
  @java.lang.Override
  public boolean hasCurrentDocument() {
    return currentDocument_ != null;
  }
  /**
   *
   *
   * <pre>
   * An optional precondition on the document.
   * The request will fail if this is set and not met by the target document.
   * </pre>
   *
   * <code>.google.firestore.v1.Precondition current_document = 2;</code>
   *
   * @return The currentDocument.
   */
  @java.lang.Override
  public com.google.firestore.v1.Precondition getCurrentDocument() {
    return currentDocument_ == null
        ? com.google.firestore.v1.Precondition.getDefaultInstance()
        : currentDocument_;
  }
  /**
   *
   *
   * <pre>
   * An optional precondition on the document.
   * The request will fail if this is set and not met by the target document.
   * </pre>
   *
   * <code>.google.firestore.v1.Precondition current_document = 2;</code>
   */
  @java.lang.Override
  public com.google.firestore.v1.PreconditionOrBuilder getCurrentDocumentOrBuilder() {
    return getCurrentDocument();
  }

  private byte memoizedIsInitialized = -1;

  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
    if (!getNameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, name_);
    }
    if (currentDocument_ != null) {
      output.writeMessage(2, getCurrentDocument());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getNameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, name_);
    }
    if (currentDocument_ != null) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(2, getCurrentDocument());
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof com.google.firestore.v1.DeleteDocumentRequest)) {
      return super.equals(obj);
    }
    com.google.firestore.v1.DeleteDocumentRequest other =
        (com.google.firestore.v1.DeleteDocumentRequest) obj;

    if (!getName().equals(other.getName())) return false;
    if (hasCurrentDocument() != other.hasCurrentDocument()) return false;
    if (hasCurrentDocument()) {
      if (!getCurrentDocument().equals(other.getCurrentDocument())) return false;
    }
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + NAME_FIELD_NUMBER;
    hash = (53 * hash) + getName().hashCode();
    if (hasCurrentDocument()) {
      hash = (37 * hash) + CURRENT_DOCUMENT_FIELD_NUMBER;
      hash = (53 * hash) + getCurrentDocument().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.v1.DeleteDocumentRequest parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.v1.DeleteDocumentRequest parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.v1.DeleteDocumentRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.v1.DeleteDocumentRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.v1.DeleteDocumentRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.v1.DeleteDocumentRequest parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.v1.DeleteDocumentRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.v1.DeleteDocumentRequest parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.v1.DeleteDocumentRequest parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.firestore.v1.DeleteDocumentRequest parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.v1.DeleteDocumentRequest parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.v1.DeleteDocumentRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() {
    return newBuilder();
  }

  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }

  public static Builder newBuilder(com.google.firestore.v1.DeleteDocumentRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }

  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   *
   *
   * <pre>
   * The request for [Firestore.DeleteDocument][google.firestore.v1.Firestore.DeleteDocument].
   * </pre>
   *
   * Protobuf type {@code google.firestore.v1.DeleteDocumentRequest}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.firestore.v1.DeleteDocumentRequest)
      com.google.firestore.v1.DeleteDocumentRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.firestore.v1.FirestoreProto
          .internal_static_google_firestore_v1_DeleteDocumentRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.v1.FirestoreProto
          .internal_static_google_firestore_v1_DeleteDocumentRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.v1.DeleteDocumentRequest.class,
              com.google.firestore.v1.DeleteDocumentRequest.Builder.class);
    }

    // Construct using com.google.firestore.v1.DeleteDocumentRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }

    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders) {}
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      name_ = "";

      if (currentDocumentBuilder_ == null) {
        currentDocument_ = null;
      } else {
        currentDocument_ = null;
        currentDocumentBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.firestore.v1.FirestoreProto
          .internal_static_google_firestore_v1_DeleteDocumentRequest_descriptor;
    }

    @java.lang.Override
    public com.google.firestore.v1.DeleteDocumentRequest getDefaultInstanceForType() {
      return com.google.firestore.v1.DeleteDocumentRequest.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.firestore.v1.DeleteDocumentRequest build() {
      com.google.firestore.v1.DeleteDocumentRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.firestore.v1.DeleteDocumentRequest buildPartial() {
      com.google.firestore.v1.DeleteDocumentRequest result =
          new com.google.firestore.v1.DeleteDocumentRequest(this);
      result.name_ = name_;
      if (currentDocumentBuilder_ == null) {
        result.currentDocument_ = currentDocument_;
      } else {
        result.currentDocument_ = currentDocumentBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }

    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
      return super.setField(field, value);
    }

    @java.lang.Override
    public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }

    @java.lang.Override
    public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }

    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field, int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }

    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.google.firestore.v1.DeleteDocumentRequest) {
        return mergeFrom((com.google.firestore.v1.DeleteDocumentRequest) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.v1.DeleteDocumentRequest other) {
      if (other == com.google.firestore.v1.DeleteDocumentRequest.getDefaultInstance()) return this;
      if (!other.getName().isEmpty()) {
        name_ = other.name_;
        onChanged();
      }
      if (other.hasCurrentDocument()) {
        mergeCurrentDocument(other.getCurrentDocument());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.google.firestore.v1.DeleteDocumentRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.google.firestore.v1.DeleteDocumentRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object name_ = "";
    /**
     *
     *
     * <pre>
     * Required. The resource name of the Document to delete. In the format:
     * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
     * </pre>
     *
     * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>
     *
     * @return The name.
     */
    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        name_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     *
     *
     * <pre>
     * Required. The resource name of the Document to delete. In the format:
     * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
     * </pre>
     *
     * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>
     *
     * @return The bytes for name.
     */
    public com.google.protobuf.ByteString getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     *
     *
     * <pre>
     * Required. The resource name of the Document to delete. In the format:
     * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
     * </pre>
     *
     * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>
     *
     * @param value The name to set.
     * @return This builder for chaining.
     */
    public Builder setName(java.lang.String value) {
      if (value == null) {
        throw new NullPointerException();
      }

      name_ = value;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Required. The resource name of the Document to delete. In the format:
     * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
     * </pre>
     *
     * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearName() {

      name_ = getDefaultInstance().getName();
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Required. The resource name of the Document to delete. In the format:
     * `projects/{project_id}/databases/{database_id}/documents/{document_path}`.
     * </pre>
     *
     * <code>string name = 1 [(.google.api.field_behavior) = REQUIRED];</code>
     *
     * @param value The bytes for name to set.
     * @return This builder for chaining.
     */
    public Builder setNameBytes(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      checkByteStringIsUtf8(value);

      name_ = value;
      onChanged();
      return this;
    }

    private com.google.firestore.v1.Precondition currentDocument_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.firestore.v1.Precondition,
            com.google.firestore.v1.Precondition.Builder,
            com.google.firestore.v1.PreconditionOrBuilder>
        currentDocumentBuilder_;
    /**
     *
     *
     * <pre>
     * An optional precondition on the document.
     * The request will fail if this is set and not met by the target document.
     * </pre>
     *
     * <code>.google.firestore.v1.Precondition current_document = 2;</code>
     *
     * @return Whether the currentDocument field is set.
     */
    public boolean hasCurrentDocument() {
      return currentDocumentBuilder_ != null || currentDocument_ != null;
    }
    /**
     *
     *
     * <pre>
     * An optional precondition on the document.
     * The request will fail if this is set and not met by the target document.
     * </pre>
     *
     * <code>.google.firestore.v1.Precondition current_document = 2;</code>
     *
     * @return The currentDocument.
     */
    public com.google.firestore.v1.Precondition getCurrentDocument() {
      if (currentDocumentBuilder_ == null) {
        return currentDocument_ == null
            ? com.google.firestore.v1.Precondition.getDefaultInstance()
            : currentDocument_;
      } else {
        return currentDocumentBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * An optional precondition on the document.
     * The request will fail if this is set and not met by the target document.
     * </pre>
     *
     * <code>.google.firestore.v1.Precondition current_document = 2;</code>
     */
    public Builder setCurrentDocument(com.google.firestore.v1.Precondition value) {
      if (currentDocumentBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        currentDocument_ = value;
        onChanged();
      } else {
        currentDocumentBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * An optional precondition on the document.
     * The request will fail if this is set and not met by the target document.
     * </pre>
     *
     * <code>.google.firestore.v1.Precondition current_document = 2;</code>
     */
    public Builder setCurrentDocument(
        com.google.firestore.v1.Precondition.Builder builderForValue) {
      if (currentDocumentBuilder_ == null) {
        currentDocument_ = builderForValue.build();
        onChanged();
      } else {
        currentDocumentBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * An optional precondition on the document.
     * The request will fail if this is set and not met by the target document.
     * </pre>
     *
     * <code>.google.firestore.v1.Precondition current_document = 2;</code>
     */
    public Builder mergeCurrentDocument(com.google.firestore.v1.Precondition value) {
      if (currentDocumentBuilder_ == null) {
        if (currentDocument_ != null) {
          currentDocument_ =
              com.google.firestore.v1.Precondition.newBuilder(currentDocument_)
                  .mergeFrom(value)
                  .buildPartial();
        } else {
          currentDocument_ = value;
        }
        onChanged();
      } else {
        currentDocumentBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * An optional precondition on the document.
     * The request will fail if this is set and not met by the target document.
     * </pre>
     *
     * <code>.google.firestore.v1.Precondition current_document = 2;</code>
     */
    public Builder clearCurrentDocument() {
      if (currentDocumentBuilder_ == null) {
        currentDocument_ = null;
        onChanged();
      } else {
        currentDocument_ = null;
        currentDocumentBuilder_ = null;
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * An optional precondition on the document.
     * The request will fail if this is set and not met by the target document.
     * </pre>
     *
     * <code>.google.firestore.v1.Precondition current_document = 2;</code>
     */
    public com.google.firestore.v1.Precondition.Builder getCurrentDocumentBuilder() {

      onChanged();
      return getCurrentDocumentFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * An optional precondition on the document.
     * The request will fail if this is set and not met by the target document.
     * </pre>
     *
     * <code>.google.firestore.v1.Precondition current_document = 2;</code>
     */
    public com.google.firestore.v1.PreconditionOrBuilder getCurrentDocumentOrBuilder() {
      if (currentDocumentBuilder_ != null) {
        return currentDocumentBuilder_.getMessageOrBuilder();
      } else {
        return currentDocument_ == null
            ? com.google.firestore.v1.Precondition.getDefaultInstance()
            : currentDocument_;
      }
    }
    /**
     *
     *
     * <pre>
     * An optional precondition on the document.
     * The request will fail if this is set and not met by the target document.
     * </pre>
     *
     * <code>.google.firestore.v1.Precondition current_document = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.firestore.v1.Precondition,
            com.google.firestore.v1.Precondition.Builder,
            com.google.firestore.v1.PreconditionOrBuilder>
        getCurrentDocumentFieldBuilder() {
      if (currentDocumentBuilder_ == null) {
        currentDocumentBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.firestore.v1.Precondition,
                com.google.firestore.v1.Precondition.Builder,
                com.google.firestore.v1.PreconditionOrBuilder>(
                getCurrentDocument(), getParentForChildren(), isClean());
        currentDocument_ = null;
      }
      return currentDocumentBuilder_;
    }

    @java.lang.Override
    public final Builder setUnknownFields(final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }

    // @@protoc_insertion_point(builder_scope:google.firestore.v1.DeleteDocumentRequest)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.v1.DeleteDocumentRequest)
  private static final com.google.firestore.v1.DeleteDocumentRequest DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.firestore.v1.DeleteDocumentRequest();
  }

  public static com.google.firestore.v1.DeleteDocumentRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<DeleteDocumentRequest> PARSER =
      new com.google.protobuf.AbstractParser<DeleteDocumentRequest>() {
        @java.lang.Override
        public DeleteDocumentRequest parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new DeleteDocumentRequest(input, extensionRegistry);
        }
      };

  public static com.google.protobuf.Parser<DeleteDocumentRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<DeleteDocumentRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.firestore.v1.DeleteDocumentRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
