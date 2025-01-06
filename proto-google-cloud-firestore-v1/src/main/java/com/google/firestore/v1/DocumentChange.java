/*
 * Copyright 2025 Google LLC
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
// source: google/firestore/v1/write.proto

// Protobuf Java Version: 3.25.5
package com.google.firestore.v1;

/**
 *
 *
 * <pre>
 * A [Document][google.firestore.v1.Document] has changed.
 *
 * May be the result of multiple [writes][google.firestore.v1.Write], including
 * deletes, that ultimately resulted in a new value for the
 * [Document][google.firestore.v1.Document].
 *
 * Multiple [DocumentChange][google.firestore.v1.DocumentChange] messages may be
 * returned for the same logical change, if multiple targets are affected.
 * </pre>
 *
 * Protobuf type {@code google.firestore.v1.DocumentChange}
 */
public final class DocumentChange extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.firestore.v1.DocumentChange)
    DocumentChangeOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use DocumentChange.newBuilder() to construct.
  private DocumentChange(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private DocumentChange() {
    targetIds_ = emptyIntList();
    removedTargetIds_ = emptyIntList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new DocumentChange();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.firestore.v1.WriteProto
        .internal_static_google_firestore_v1_DocumentChange_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.v1.WriteProto
        .internal_static_google_firestore_v1_DocumentChange_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.v1.DocumentChange.class,
            com.google.firestore.v1.DocumentChange.Builder.class);
  }

  private int bitField0_;
  public static final int DOCUMENT_FIELD_NUMBER = 1;
  private com.google.firestore.v1.Document document_;
  /**
   *
   *
   * <pre>
   * The new state of the [Document][google.firestore.v1.Document].
   *
   * If `mask` is set, contains only fields that were updated or added.
   * </pre>
   *
   * <code>.google.firestore.v1.Document document = 1;</code>
   *
   * @return Whether the document field is set.
   */
  @java.lang.Override
  public boolean hasDocument() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   *
   *
   * <pre>
   * The new state of the [Document][google.firestore.v1.Document].
   *
   * If `mask` is set, contains only fields that were updated or added.
   * </pre>
   *
   * <code>.google.firestore.v1.Document document = 1;</code>
   *
   * @return The document.
   */
  @java.lang.Override
  public com.google.firestore.v1.Document getDocument() {
    return document_ == null ? com.google.firestore.v1.Document.getDefaultInstance() : document_;
  }
  /**
   *
   *
   * <pre>
   * The new state of the [Document][google.firestore.v1.Document].
   *
   * If `mask` is set, contains only fields that were updated or added.
   * </pre>
   *
   * <code>.google.firestore.v1.Document document = 1;</code>
   */
  @java.lang.Override
  public com.google.firestore.v1.DocumentOrBuilder getDocumentOrBuilder() {
    return document_ == null ? com.google.firestore.v1.Document.getDefaultInstance() : document_;
  }

  public static final int TARGET_IDS_FIELD_NUMBER = 5;

  @SuppressWarnings("serial")
  private com.google.protobuf.Internal.IntList targetIds_ = emptyIntList();
  /**
   *
   *
   * <pre>
   * A set of target IDs of targets that match this document.
   * </pre>
   *
   * <code>repeated int32 target_ids = 5;</code>
   *
   * @return A list containing the targetIds.
   */
  @java.lang.Override
  public java.util.List<java.lang.Integer> getTargetIdsList() {
    return targetIds_;
  }
  /**
   *
   *
   * <pre>
   * A set of target IDs of targets that match this document.
   * </pre>
   *
   * <code>repeated int32 target_ids = 5;</code>
   *
   * @return The count of targetIds.
   */
  public int getTargetIdsCount() {
    return targetIds_.size();
  }
  /**
   *
   *
   * <pre>
   * A set of target IDs of targets that match this document.
   * </pre>
   *
   * <code>repeated int32 target_ids = 5;</code>
   *
   * @param index The index of the element to return.
   * @return The targetIds at the given index.
   */
  public int getTargetIds(int index) {
    return targetIds_.getInt(index);
  }

  private int targetIdsMemoizedSerializedSize = -1;

  public static final int REMOVED_TARGET_IDS_FIELD_NUMBER = 6;

  @SuppressWarnings("serial")
  private com.google.protobuf.Internal.IntList removedTargetIds_ = emptyIntList();
  /**
   *
   *
   * <pre>
   * A set of target IDs for targets that no longer match this document.
   * </pre>
   *
   * <code>repeated int32 removed_target_ids = 6;</code>
   *
   * @return A list containing the removedTargetIds.
   */
  @java.lang.Override
  public java.util.List<java.lang.Integer> getRemovedTargetIdsList() {
    return removedTargetIds_;
  }
  /**
   *
   *
   * <pre>
   * A set of target IDs for targets that no longer match this document.
   * </pre>
   *
   * <code>repeated int32 removed_target_ids = 6;</code>
   *
   * @return The count of removedTargetIds.
   */
  public int getRemovedTargetIdsCount() {
    return removedTargetIds_.size();
  }
  /**
   *
   *
   * <pre>
   * A set of target IDs for targets that no longer match this document.
   * </pre>
   *
   * <code>repeated int32 removed_target_ids = 6;</code>
   *
   * @param index The index of the element to return.
   * @return The removedTargetIds at the given index.
   */
  public int getRemovedTargetIds(int index) {
    return removedTargetIds_.getInt(index);
  }

  private int removedTargetIdsMemoizedSerializedSize = -1;

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
    getSerializedSize();
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeMessage(1, getDocument());
    }
    if (getTargetIdsList().size() > 0) {
      output.writeUInt32NoTag(42);
      output.writeUInt32NoTag(targetIdsMemoizedSerializedSize);
    }
    for (int i = 0; i < targetIds_.size(); i++) {
      output.writeInt32NoTag(targetIds_.getInt(i));
    }
    if (getRemovedTargetIdsList().size() > 0) {
      output.writeUInt32NoTag(50);
      output.writeUInt32NoTag(removedTargetIdsMemoizedSerializedSize);
    }
    for (int i = 0; i < removedTargetIds_.size(); i++) {
      output.writeInt32NoTag(removedTargetIds_.getInt(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, getDocument());
    }
    {
      int dataSize = 0;
      for (int i = 0; i < targetIds_.size(); i++) {
        dataSize +=
            com.google.protobuf.CodedOutputStream.computeInt32SizeNoTag(targetIds_.getInt(i));
      }
      size += dataSize;
      if (!getTargetIdsList().isEmpty()) {
        size += 1;
        size += com.google.protobuf.CodedOutputStream.computeInt32SizeNoTag(dataSize);
      }
      targetIdsMemoizedSerializedSize = dataSize;
    }
    {
      int dataSize = 0;
      for (int i = 0; i < removedTargetIds_.size(); i++) {
        dataSize +=
            com.google.protobuf.CodedOutputStream.computeInt32SizeNoTag(
                removedTargetIds_.getInt(i));
      }
      size += dataSize;
      if (!getRemovedTargetIdsList().isEmpty()) {
        size += 1;
        size += com.google.protobuf.CodedOutputStream.computeInt32SizeNoTag(dataSize);
      }
      removedTargetIdsMemoizedSerializedSize = dataSize;
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof com.google.firestore.v1.DocumentChange)) {
      return super.equals(obj);
    }
    com.google.firestore.v1.DocumentChange other = (com.google.firestore.v1.DocumentChange) obj;

    if (hasDocument() != other.hasDocument()) return false;
    if (hasDocument()) {
      if (!getDocument().equals(other.getDocument())) return false;
    }
    if (!getTargetIdsList().equals(other.getTargetIdsList())) return false;
    if (!getRemovedTargetIdsList().equals(other.getRemovedTargetIdsList())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasDocument()) {
      hash = (37 * hash) + DOCUMENT_FIELD_NUMBER;
      hash = (53 * hash) + getDocument().hashCode();
    }
    if (getTargetIdsCount() > 0) {
      hash = (37 * hash) + TARGET_IDS_FIELD_NUMBER;
      hash = (53 * hash) + getTargetIdsList().hashCode();
    }
    if (getRemovedTargetIdsCount() > 0) {
      hash = (37 * hash) + REMOVED_TARGET_IDS_FIELD_NUMBER;
      hash = (53 * hash) + getRemovedTargetIdsList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.v1.DocumentChange parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.v1.DocumentChange parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.v1.DocumentChange parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.v1.DocumentChange parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.v1.DocumentChange parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.v1.DocumentChange parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.v1.DocumentChange parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.v1.DocumentChange parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.v1.DocumentChange parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.firestore.v1.DocumentChange parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.v1.DocumentChange parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.v1.DocumentChange parseFrom(
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

  public static Builder newBuilder(com.google.firestore.v1.DocumentChange prototype) {
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
   * A [Document][google.firestore.v1.Document] has changed.
   *
   * May be the result of multiple [writes][google.firestore.v1.Write], including
   * deletes, that ultimately resulted in a new value for the
   * [Document][google.firestore.v1.Document].
   *
   * Multiple [DocumentChange][google.firestore.v1.DocumentChange] messages may be
   * returned for the same logical change, if multiple targets are affected.
   * </pre>
   *
   * Protobuf type {@code google.firestore.v1.DocumentChange}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.firestore.v1.DocumentChange)
      com.google.firestore.v1.DocumentChangeOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.firestore.v1.WriteProto
          .internal_static_google_firestore_v1_DocumentChange_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.v1.WriteProto
          .internal_static_google_firestore_v1_DocumentChange_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.v1.DocumentChange.class,
              com.google.firestore.v1.DocumentChange.Builder.class);
    }

    // Construct using com.google.firestore.v1.DocumentChange.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }

    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders) {
        getDocumentFieldBuilder();
      }
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      document_ = null;
      if (documentBuilder_ != null) {
        documentBuilder_.dispose();
        documentBuilder_ = null;
      }
      targetIds_ = emptyIntList();
      removedTargetIds_ = emptyIntList();
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.firestore.v1.WriteProto
          .internal_static_google_firestore_v1_DocumentChange_descriptor;
    }

    @java.lang.Override
    public com.google.firestore.v1.DocumentChange getDefaultInstanceForType() {
      return com.google.firestore.v1.DocumentChange.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.firestore.v1.DocumentChange build() {
      com.google.firestore.v1.DocumentChange result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.firestore.v1.DocumentChange buildPartial() {
      com.google.firestore.v1.DocumentChange result =
          new com.google.firestore.v1.DocumentChange(this);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.google.firestore.v1.DocumentChange result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.document_ = documentBuilder_ == null ? document_ : documentBuilder_.build();
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        targetIds_.makeImmutable();
        result.targetIds_ = targetIds_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        removedTargetIds_.makeImmutable();
        result.removedTargetIds_ = removedTargetIds_;
      }
      result.bitField0_ |= to_bitField0_;
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
      if (other instanceof com.google.firestore.v1.DocumentChange) {
        return mergeFrom((com.google.firestore.v1.DocumentChange) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.v1.DocumentChange other) {
      if (other == com.google.firestore.v1.DocumentChange.getDefaultInstance()) return this;
      if (other.hasDocument()) {
        mergeDocument(other.getDocument());
      }
      if (!other.targetIds_.isEmpty()) {
        if (targetIds_.isEmpty()) {
          targetIds_ = other.targetIds_;
          targetIds_.makeImmutable();
          bitField0_ |= 0x00000002;
        } else {
          ensureTargetIdsIsMutable();
          targetIds_.addAll(other.targetIds_);
        }
        onChanged();
      }
      if (!other.removedTargetIds_.isEmpty()) {
        if (removedTargetIds_.isEmpty()) {
          removedTargetIds_ = other.removedTargetIds_;
          removedTargetIds_.makeImmutable();
          bitField0_ |= 0x00000004;
        } else {
          ensureRemovedTargetIdsIsMutable();
          removedTargetIds_.addAll(other.removedTargetIds_);
        }
        onChanged();
      }
      this.mergeUnknownFields(other.getUnknownFields());
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
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
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
                input.readMessage(getDocumentFieldBuilder().getBuilder(), extensionRegistry);
                bitField0_ |= 0x00000001;
                break;
              } // case 10
            case 40:
              {
                int v = input.readInt32();
                ensureTargetIdsIsMutable();
                targetIds_.addInt(v);
                break;
              } // case 40
            case 42:
              {
                int length = input.readRawVarint32();
                int limit = input.pushLimit(length);
                ensureTargetIdsIsMutable();
                while (input.getBytesUntilLimit() > 0) {
                  targetIds_.addInt(input.readInt32());
                }
                input.popLimit(limit);
                break;
              } // case 42
            case 48:
              {
                int v = input.readInt32();
                ensureRemovedTargetIdsIsMutable();
                removedTargetIds_.addInt(v);
                break;
              } // case 48
            case 50:
              {
                int length = input.readRawVarint32();
                int limit = input.pushLimit(length);
                ensureRemovedTargetIdsIsMutable();
                while (input.getBytesUntilLimit() > 0) {
                  removedTargetIds_.addInt(input.readInt32());
                }
                input.popLimit(limit);
                break;
              } // case 50
            default:
              {
                if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                  done = true; // was an endgroup tag
                }
                break;
              } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }

    private int bitField0_;

    private com.google.firestore.v1.Document document_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.firestore.v1.Document,
            com.google.firestore.v1.Document.Builder,
            com.google.firestore.v1.DocumentOrBuilder>
        documentBuilder_;
    /**
     *
     *
     * <pre>
     * The new state of the [Document][google.firestore.v1.Document].
     *
     * If `mask` is set, contains only fields that were updated or added.
     * </pre>
     *
     * <code>.google.firestore.v1.Document document = 1;</code>
     *
     * @return Whether the document field is set.
     */
    public boolean hasDocument() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     *
     *
     * <pre>
     * The new state of the [Document][google.firestore.v1.Document].
     *
     * If `mask` is set, contains only fields that were updated or added.
     * </pre>
     *
     * <code>.google.firestore.v1.Document document = 1;</code>
     *
     * @return The document.
     */
    public com.google.firestore.v1.Document getDocument() {
      if (documentBuilder_ == null) {
        return document_ == null
            ? com.google.firestore.v1.Document.getDefaultInstance()
            : document_;
      } else {
        return documentBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * The new state of the [Document][google.firestore.v1.Document].
     *
     * If `mask` is set, contains only fields that were updated or added.
     * </pre>
     *
     * <code>.google.firestore.v1.Document document = 1;</code>
     */
    public Builder setDocument(com.google.firestore.v1.Document value) {
      if (documentBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        document_ = value;
      } else {
        documentBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The new state of the [Document][google.firestore.v1.Document].
     *
     * If `mask` is set, contains only fields that were updated or added.
     * </pre>
     *
     * <code>.google.firestore.v1.Document document = 1;</code>
     */
    public Builder setDocument(com.google.firestore.v1.Document.Builder builderForValue) {
      if (documentBuilder_ == null) {
        document_ = builderForValue.build();
      } else {
        documentBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The new state of the [Document][google.firestore.v1.Document].
     *
     * If `mask` is set, contains only fields that were updated or added.
     * </pre>
     *
     * <code>.google.firestore.v1.Document document = 1;</code>
     */
    public Builder mergeDocument(com.google.firestore.v1.Document value) {
      if (documentBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)
            && document_ != null
            && document_ != com.google.firestore.v1.Document.getDefaultInstance()) {
          getDocumentBuilder().mergeFrom(value);
        } else {
          document_ = value;
        }
      } else {
        documentBuilder_.mergeFrom(value);
      }
      if (document_ != null) {
        bitField0_ |= 0x00000001;
        onChanged();
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The new state of the [Document][google.firestore.v1.Document].
     *
     * If `mask` is set, contains only fields that were updated or added.
     * </pre>
     *
     * <code>.google.firestore.v1.Document document = 1;</code>
     */
    public Builder clearDocument() {
      bitField0_ = (bitField0_ & ~0x00000001);
      document_ = null;
      if (documentBuilder_ != null) {
        documentBuilder_.dispose();
        documentBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The new state of the [Document][google.firestore.v1.Document].
     *
     * If `mask` is set, contains only fields that were updated or added.
     * </pre>
     *
     * <code>.google.firestore.v1.Document document = 1;</code>
     */
    public com.google.firestore.v1.Document.Builder getDocumentBuilder() {
      bitField0_ |= 0x00000001;
      onChanged();
      return getDocumentFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * The new state of the [Document][google.firestore.v1.Document].
     *
     * If `mask` is set, contains only fields that were updated or added.
     * </pre>
     *
     * <code>.google.firestore.v1.Document document = 1;</code>
     */
    public com.google.firestore.v1.DocumentOrBuilder getDocumentOrBuilder() {
      if (documentBuilder_ != null) {
        return documentBuilder_.getMessageOrBuilder();
      } else {
        return document_ == null
            ? com.google.firestore.v1.Document.getDefaultInstance()
            : document_;
      }
    }
    /**
     *
     *
     * <pre>
     * The new state of the [Document][google.firestore.v1.Document].
     *
     * If `mask` is set, contains only fields that were updated or added.
     * </pre>
     *
     * <code>.google.firestore.v1.Document document = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.firestore.v1.Document,
            com.google.firestore.v1.Document.Builder,
            com.google.firestore.v1.DocumentOrBuilder>
        getDocumentFieldBuilder() {
      if (documentBuilder_ == null) {
        documentBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.firestore.v1.Document,
                com.google.firestore.v1.Document.Builder,
                com.google.firestore.v1.DocumentOrBuilder>(
                getDocument(), getParentForChildren(), isClean());
        document_ = null;
      }
      return documentBuilder_;
    }

    private com.google.protobuf.Internal.IntList targetIds_ = emptyIntList();

    private void ensureTargetIdsIsMutable() {
      if (!targetIds_.isModifiable()) {
        targetIds_ = makeMutableCopy(targetIds_);
      }
      bitField0_ |= 0x00000002;
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs of targets that match this document.
     * </pre>
     *
     * <code>repeated int32 target_ids = 5;</code>
     *
     * @return A list containing the targetIds.
     */
    public java.util.List<java.lang.Integer> getTargetIdsList() {
      targetIds_.makeImmutable();
      return targetIds_;
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs of targets that match this document.
     * </pre>
     *
     * <code>repeated int32 target_ids = 5;</code>
     *
     * @return The count of targetIds.
     */
    public int getTargetIdsCount() {
      return targetIds_.size();
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs of targets that match this document.
     * </pre>
     *
     * <code>repeated int32 target_ids = 5;</code>
     *
     * @param index The index of the element to return.
     * @return The targetIds at the given index.
     */
    public int getTargetIds(int index) {
      return targetIds_.getInt(index);
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs of targets that match this document.
     * </pre>
     *
     * <code>repeated int32 target_ids = 5;</code>
     *
     * @param index The index to set the value at.
     * @param value The targetIds to set.
     * @return This builder for chaining.
     */
    public Builder setTargetIds(int index, int value) {

      ensureTargetIdsIsMutable();
      targetIds_.setInt(index, value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs of targets that match this document.
     * </pre>
     *
     * <code>repeated int32 target_ids = 5;</code>
     *
     * @param value The targetIds to add.
     * @return This builder for chaining.
     */
    public Builder addTargetIds(int value) {

      ensureTargetIdsIsMutable();
      targetIds_.addInt(value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs of targets that match this document.
     * </pre>
     *
     * <code>repeated int32 target_ids = 5;</code>
     *
     * @param values The targetIds to add.
     * @return This builder for chaining.
     */
    public Builder addAllTargetIds(java.lang.Iterable<? extends java.lang.Integer> values) {
      ensureTargetIdsIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(values, targetIds_);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs of targets that match this document.
     * </pre>
     *
     * <code>repeated int32 target_ids = 5;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearTargetIds() {
      targetIds_ = emptyIntList();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }

    private com.google.protobuf.Internal.IntList removedTargetIds_ = emptyIntList();

    private void ensureRemovedTargetIdsIsMutable() {
      if (!removedTargetIds_.isModifiable()) {
        removedTargetIds_ = makeMutableCopy(removedTargetIds_);
      }
      bitField0_ |= 0x00000004;
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs for targets that no longer match this document.
     * </pre>
     *
     * <code>repeated int32 removed_target_ids = 6;</code>
     *
     * @return A list containing the removedTargetIds.
     */
    public java.util.List<java.lang.Integer> getRemovedTargetIdsList() {
      removedTargetIds_.makeImmutable();
      return removedTargetIds_;
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs for targets that no longer match this document.
     * </pre>
     *
     * <code>repeated int32 removed_target_ids = 6;</code>
     *
     * @return The count of removedTargetIds.
     */
    public int getRemovedTargetIdsCount() {
      return removedTargetIds_.size();
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs for targets that no longer match this document.
     * </pre>
     *
     * <code>repeated int32 removed_target_ids = 6;</code>
     *
     * @param index The index of the element to return.
     * @return The removedTargetIds at the given index.
     */
    public int getRemovedTargetIds(int index) {
      return removedTargetIds_.getInt(index);
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs for targets that no longer match this document.
     * </pre>
     *
     * <code>repeated int32 removed_target_ids = 6;</code>
     *
     * @param index The index to set the value at.
     * @param value The removedTargetIds to set.
     * @return This builder for chaining.
     */
    public Builder setRemovedTargetIds(int index, int value) {

      ensureRemovedTargetIdsIsMutable();
      removedTargetIds_.setInt(index, value);
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs for targets that no longer match this document.
     * </pre>
     *
     * <code>repeated int32 removed_target_ids = 6;</code>
     *
     * @param value The removedTargetIds to add.
     * @return This builder for chaining.
     */
    public Builder addRemovedTargetIds(int value) {

      ensureRemovedTargetIdsIsMutable();
      removedTargetIds_.addInt(value);
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs for targets that no longer match this document.
     * </pre>
     *
     * <code>repeated int32 removed_target_ids = 6;</code>
     *
     * @param values The removedTargetIds to add.
     * @return This builder for chaining.
     */
    public Builder addAllRemovedTargetIds(java.lang.Iterable<? extends java.lang.Integer> values) {
      ensureRemovedTargetIdsIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(values, removedTargetIds_);
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * A set of target IDs for targets that no longer match this document.
     * </pre>
     *
     * <code>repeated int32 removed_target_ids = 6;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearRemovedTargetIds() {
      removedTargetIds_ = emptyIntList();
      bitField0_ = (bitField0_ & ~0x00000004);
      onChanged();
      return this;
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

    // @@protoc_insertion_point(builder_scope:google.firestore.v1.DocumentChange)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.v1.DocumentChange)
  private static final com.google.firestore.v1.DocumentChange DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.firestore.v1.DocumentChange();
  }

  public static com.google.firestore.v1.DocumentChange getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<DocumentChange> PARSER =
      new com.google.protobuf.AbstractParser<DocumentChange>() {
        @java.lang.Override
        public DocumentChange parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          Builder builder = newBuilder();
          try {
            builder.mergeFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(builder.buildPartial());
          } catch (com.google.protobuf.UninitializedMessageException e) {
            throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
          } catch (java.io.IOException e) {
            throw new com.google.protobuf.InvalidProtocolBufferException(e)
                .setUnfinishedMessage(builder.buildPartial());
          }
          return builder.buildPartial();
        }
      };

  public static com.google.protobuf.Parser<DocumentChange> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<DocumentChange> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.firestore.v1.DocumentChange getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
