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
// source: google/firestore/admin/v1/firestore_admin.proto

package com.google.firestore.admin.v1;

/**
 *
 *
 * <pre>
 * The request for [FirestoreAdmin.ExportDocuments][google.firestore.admin.v1.FirestoreAdmin.ExportDocuments].
 * </pre>
 *
 * Protobuf type {@code google.firestore.admin.v1.ExportDocumentsRequest}
 */
public final class ExportDocumentsRequest extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.firestore.admin.v1.ExportDocumentsRequest)
    ExportDocumentsRequestOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use ExportDocumentsRequest.newBuilder() to construct.
  private ExportDocumentsRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private ExportDocumentsRequest() {
    name_ = "";
    collectionIds_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    outputUriPrefix_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new ExportDocumentsRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
    return this.unknownFields;
  }

  private ExportDocumentsRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
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
              java.lang.String s = input.readStringRequireUtf8();
              if (!((mutable_bitField0_ & 0x00000001) != 0)) {
                collectionIds_ = new com.google.protobuf.LazyStringArrayList();
                mutable_bitField0_ |= 0x00000001;
              }
              collectionIds_.add(s);
              break;
            }
          case 26:
            {
              java.lang.String s = input.readStringRequireUtf8();

              outputUriPrefix_ = s;
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
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        collectionIds_ = collectionIds_.getUnmodifiableView();
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.firestore.admin.v1.FirestoreAdminProto
        .internal_static_google_firestore_admin_v1_ExportDocumentsRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.admin.v1.FirestoreAdminProto
        .internal_static_google_firestore_admin_v1_ExportDocumentsRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.admin.v1.ExportDocumentsRequest.class,
            com.google.firestore.admin.v1.ExportDocumentsRequest.Builder.class);
  }

  public static final int NAME_FIELD_NUMBER = 1;
  private volatile java.lang.Object name_;
  /**
   *
   *
   * <pre>
   * Required. Database to export. Should be of the form:
   * `projects/{project_id}/databases/{database_id}`.
   * </pre>
   *
   * <code>
   * string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
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
   * Required. Database to export. Should be of the form:
   * `projects/{project_id}/databases/{database_id}`.
   * </pre>
   *
   * <code>
   * string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
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

  public static final int COLLECTION_IDS_FIELD_NUMBER = 2;
  private com.google.protobuf.LazyStringList collectionIds_;
  /**
   *
   *
   * <pre>
   * Which collection ids to export. Unspecified means all collections.
   * </pre>
   *
   * <code>repeated string collection_ids = 2;</code>
   *
   * @return A list containing the collectionIds.
   */
  public com.google.protobuf.ProtocolStringList getCollectionIdsList() {
    return collectionIds_;
  }
  /**
   *
   *
   * <pre>
   * Which collection ids to export. Unspecified means all collections.
   * </pre>
   *
   * <code>repeated string collection_ids = 2;</code>
   *
   * @return The count of collectionIds.
   */
  public int getCollectionIdsCount() {
    return collectionIds_.size();
  }
  /**
   *
   *
   * <pre>
   * Which collection ids to export. Unspecified means all collections.
   * </pre>
   *
   * <code>repeated string collection_ids = 2;</code>
   *
   * @param index The index of the element to return.
   * @return The collectionIds at the given index.
   */
  public java.lang.String getCollectionIds(int index) {
    return collectionIds_.get(index);
  }
  /**
   *
   *
   * <pre>
   * Which collection ids to export. Unspecified means all collections.
   * </pre>
   *
   * <code>repeated string collection_ids = 2;</code>
   *
   * @param index The index of the value to return.
   * @return The bytes of the collectionIds at the given index.
   */
  public com.google.protobuf.ByteString getCollectionIdsBytes(int index) {
    return collectionIds_.getByteString(index);
  }

  public static final int OUTPUT_URI_PREFIX_FIELD_NUMBER = 3;
  private volatile java.lang.Object outputUriPrefix_;
  /**
   *
   *
   * <pre>
   * The output URI. Currently only supports Google Cloud Storage URIs of the
   * form: `gs://BUCKET_NAME[/NAMESPACE_PATH]`, where `BUCKET_NAME` is the name
   * of the Google Cloud Storage bucket and `NAMESPACE_PATH` is an optional
   * Google Cloud Storage namespace path. When
   * choosing a name, be sure to consider Google Cloud Storage naming
   * guidelines: https://cloud.google.com/storage/docs/naming.
   * If the URI is a bucket (without a namespace path), a prefix will be
   * generated based on the start time.
   * </pre>
   *
   * <code>string output_uri_prefix = 3;</code>
   *
   * @return The outputUriPrefix.
   */
  @java.lang.Override
  public java.lang.String getOutputUriPrefix() {
    java.lang.Object ref = outputUriPrefix_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      outputUriPrefix_ = s;
      return s;
    }
  }
  /**
   *
   *
   * <pre>
   * The output URI. Currently only supports Google Cloud Storage URIs of the
   * form: `gs://BUCKET_NAME[/NAMESPACE_PATH]`, where `BUCKET_NAME` is the name
   * of the Google Cloud Storage bucket and `NAMESPACE_PATH` is an optional
   * Google Cloud Storage namespace path. When
   * choosing a name, be sure to consider Google Cloud Storage naming
   * guidelines: https://cloud.google.com/storage/docs/naming.
   * If the URI is a bucket (without a namespace path), a prefix will be
   * generated based on the start time.
   * </pre>
   *
   * <code>string output_uri_prefix = 3;</code>
   *
   * @return The bytes for outputUriPrefix.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getOutputUriPrefixBytes() {
    java.lang.Object ref = outputUriPrefix_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
      outputUriPrefix_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
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
    for (int i = 0; i < collectionIds_.size(); i++) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, collectionIds_.getRaw(i));
    }
    if (!getOutputUriPrefixBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, outputUriPrefix_);
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
    {
      int dataSize = 0;
      for (int i = 0; i < collectionIds_.size(); i++) {
        dataSize += computeStringSizeNoTag(collectionIds_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getCollectionIdsList().size();
    }
    if (!getOutputUriPrefixBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, outputUriPrefix_);
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
    if (!(obj instanceof com.google.firestore.admin.v1.ExportDocumentsRequest)) {
      return super.equals(obj);
    }
    com.google.firestore.admin.v1.ExportDocumentsRequest other =
        (com.google.firestore.admin.v1.ExportDocumentsRequest) obj;

    if (!getName().equals(other.getName())) return false;
    if (!getCollectionIdsList().equals(other.getCollectionIdsList())) return false;
    if (!getOutputUriPrefix().equals(other.getOutputUriPrefix())) return false;
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
    if (getCollectionIdsCount() > 0) {
      hash = (37 * hash) + COLLECTION_IDS_FIELD_NUMBER;
      hash = (53 * hash) + getCollectionIdsList().hashCode();
    }
    hash = (37 * hash) + OUTPUT_URI_PREFIX_FIELD_NUMBER;
    hash = (53 * hash) + getOutputUriPrefix().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest parseFrom(
      java.nio.ByteBuffer data) throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest parseFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest parseFrom(
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

  public static Builder newBuilder(com.google.firestore.admin.v1.ExportDocumentsRequest prototype) {
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
   * The request for [FirestoreAdmin.ExportDocuments][google.firestore.admin.v1.FirestoreAdmin.ExportDocuments].
   * </pre>
   *
   * Protobuf type {@code google.firestore.admin.v1.ExportDocumentsRequest}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.firestore.admin.v1.ExportDocumentsRequest)
      com.google.firestore.admin.v1.ExportDocumentsRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.firestore.admin.v1.FirestoreAdminProto
          .internal_static_google_firestore_admin_v1_ExportDocumentsRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.admin.v1.FirestoreAdminProto
          .internal_static_google_firestore_admin_v1_ExportDocumentsRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.admin.v1.ExportDocumentsRequest.class,
              com.google.firestore.admin.v1.ExportDocumentsRequest.Builder.class);
    }

    // Construct using com.google.firestore.admin.v1.ExportDocumentsRequest.newBuilder()
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

      collectionIds_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000001);
      outputUriPrefix_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.firestore.admin.v1.FirestoreAdminProto
          .internal_static_google_firestore_admin_v1_ExportDocumentsRequest_descriptor;
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.ExportDocumentsRequest getDefaultInstanceForType() {
      return com.google.firestore.admin.v1.ExportDocumentsRequest.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.ExportDocumentsRequest build() {
      com.google.firestore.admin.v1.ExportDocumentsRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.ExportDocumentsRequest buildPartial() {
      com.google.firestore.admin.v1.ExportDocumentsRequest result =
          new com.google.firestore.admin.v1.ExportDocumentsRequest(this);
      int from_bitField0_ = bitField0_;
      result.name_ = name_;
      if (((bitField0_ & 0x00000001) != 0)) {
        collectionIds_ = collectionIds_.getUnmodifiableView();
        bitField0_ = (bitField0_ & ~0x00000001);
      }
      result.collectionIds_ = collectionIds_;
      result.outputUriPrefix_ = outputUriPrefix_;
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
      if (other instanceof com.google.firestore.admin.v1.ExportDocumentsRequest) {
        return mergeFrom((com.google.firestore.admin.v1.ExportDocumentsRequest) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.admin.v1.ExportDocumentsRequest other) {
      if (other == com.google.firestore.admin.v1.ExportDocumentsRequest.getDefaultInstance())
        return this;
      if (!other.getName().isEmpty()) {
        name_ = other.name_;
        onChanged();
      }
      if (!other.collectionIds_.isEmpty()) {
        if (collectionIds_.isEmpty()) {
          collectionIds_ = other.collectionIds_;
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          ensureCollectionIdsIsMutable();
          collectionIds_.addAll(other.collectionIds_);
        }
        onChanged();
      }
      if (!other.getOutputUriPrefix().isEmpty()) {
        outputUriPrefix_ = other.outputUriPrefix_;
        onChanged();
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
      com.google.firestore.admin.v1.ExportDocumentsRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage =
            (com.google.firestore.admin.v1.ExportDocumentsRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int bitField0_;

    private java.lang.Object name_ = "";
    /**
     *
     *
     * <pre>
     * Required. Database to export. Should be of the form:
     * `projects/{project_id}/databases/{database_id}`.
     * </pre>
     *
     * <code>
     * string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
     * </code>
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
     * Required. Database to export. Should be of the form:
     * `projects/{project_id}/databases/{database_id}`.
     * </pre>
     *
     * <code>
     * string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
     * </code>
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
     * Required. Database to export. Should be of the form:
     * `projects/{project_id}/databases/{database_id}`.
     * </pre>
     *
     * <code>
     * string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
     * </code>
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
     * Required. Database to export. Should be of the form:
     * `projects/{project_id}/databases/{database_id}`.
     * </pre>
     *
     * <code>
     * string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
     * </code>
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
     * Required. Database to export. Should be of the form:
     * `projects/{project_id}/databases/{database_id}`.
     * </pre>
     *
     * <code>
     * string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
     * </code>
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

    private com.google.protobuf.LazyStringList collectionIds_ =
        com.google.protobuf.LazyStringArrayList.EMPTY;

    private void ensureCollectionIdsIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        collectionIds_ = new com.google.protobuf.LazyStringArrayList(collectionIds_);
        bitField0_ |= 0x00000001;
      }
    }
    /**
     *
     *
     * <pre>
     * Which collection ids to export. Unspecified means all collections.
     * </pre>
     *
     * <code>repeated string collection_ids = 2;</code>
     *
     * @return A list containing the collectionIds.
     */
    public com.google.protobuf.ProtocolStringList getCollectionIdsList() {
      return collectionIds_.getUnmodifiableView();
    }
    /**
     *
     *
     * <pre>
     * Which collection ids to export. Unspecified means all collections.
     * </pre>
     *
     * <code>repeated string collection_ids = 2;</code>
     *
     * @return The count of collectionIds.
     */
    public int getCollectionIdsCount() {
      return collectionIds_.size();
    }
    /**
     *
     *
     * <pre>
     * Which collection ids to export. Unspecified means all collections.
     * </pre>
     *
     * <code>repeated string collection_ids = 2;</code>
     *
     * @param index The index of the element to return.
     * @return The collectionIds at the given index.
     */
    public java.lang.String getCollectionIds(int index) {
      return collectionIds_.get(index);
    }
    /**
     *
     *
     * <pre>
     * Which collection ids to export. Unspecified means all collections.
     * </pre>
     *
     * <code>repeated string collection_ids = 2;</code>
     *
     * @param index The index of the value to return.
     * @return The bytes of the collectionIds at the given index.
     */
    public com.google.protobuf.ByteString getCollectionIdsBytes(int index) {
      return collectionIds_.getByteString(index);
    }
    /**
     *
     *
     * <pre>
     * Which collection ids to export. Unspecified means all collections.
     * </pre>
     *
     * <code>repeated string collection_ids = 2;</code>
     *
     * @param index The index to set the value at.
     * @param value The collectionIds to set.
     * @return This builder for chaining.
     */
    public Builder setCollectionIds(int index, java.lang.String value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureCollectionIdsIsMutable();
      collectionIds_.set(index, value);
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Which collection ids to export. Unspecified means all collections.
     * </pre>
     *
     * <code>repeated string collection_ids = 2;</code>
     *
     * @param value The collectionIds to add.
     * @return This builder for chaining.
     */
    public Builder addCollectionIds(java.lang.String value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureCollectionIdsIsMutable();
      collectionIds_.add(value);
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Which collection ids to export. Unspecified means all collections.
     * </pre>
     *
     * <code>repeated string collection_ids = 2;</code>
     *
     * @param values The collectionIds to add.
     * @return This builder for chaining.
     */
    public Builder addAllCollectionIds(java.lang.Iterable<java.lang.String> values) {
      ensureCollectionIdsIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(values, collectionIds_);
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Which collection ids to export. Unspecified means all collections.
     * </pre>
     *
     * <code>repeated string collection_ids = 2;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearCollectionIds() {
      collectionIds_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Which collection ids to export. Unspecified means all collections.
     * </pre>
     *
     * <code>repeated string collection_ids = 2;</code>
     *
     * @param value The bytes of the collectionIds to add.
     * @return This builder for chaining.
     */
    public Builder addCollectionIdsBytes(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      checkByteStringIsUtf8(value);
      ensureCollectionIdsIsMutable();
      collectionIds_.add(value);
      onChanged();
      return this;
    }

    private java.lang.Object outputUriPrefix_ = "";
    /**
     *
     *
     * <pre>
     * The output URI. Currently only supports Google Cloud Storage URIs of the
     * form: `gs://BUCKET_NAME[/NAMESPACE_PATH]`, where `BUCKET_NAME` is the name
     * of the Google Cloud Storage bucket and `NAMESPACE_PATH` is an optional
     * Google Cloud Storage namespace path. When
     * choosing a name, be sure to consider Google Cloud Storage naming
     * guidelines: https://cloud.google.com/storage/docs/naming.
     * If the URI is a bucket (without a namespace path), a prefix will be
     * generated based on the start time.
     * </pre>
     *
     * <code>string output_uri_prefix = 3;</code>
     *
     * @return The outputUriPrefix.
     */
    public java.lang.String getOutputUriPrefix() {
      java.lang.Object ref = outputUriPrefix_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        outputUriPrefix_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     *
     *
     * <pre>
     * The output URI. Currently only supports Google Cloud Storage URIs of the
     * form: `gs://BUCKET_NAME[/NAMESPACE_PATH]`, where `BUCKET_NAME` is the name
     * of the Google Cloud Storage bucket and `NAMESPACE_PATH` is an optional
     * Google Cloud Storage namespace path. When
     * choosing a name, be sure to consider Google Cloud Storage naming
     * guidelines: https://cloud.google.com/storage/docs/naming.
     * If the URI is a bucket (without a namespace path), a prefix will be
     * generated based on the start time.
     * </pre>
     *
     * <code>string output_uri_prefix = 3;</code>
     *
     * @return The bytes for outputUriPrefix.
     */
    public com.google.protobuf.ByteString getOutputUriPrefixBytes() {
      java.lang.Object ref = outputUriPrefix_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
        outputUriPrefix_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     *
     *
     * <pre>
     * The output URI. Currently only supports Google Cloud Storage URIs of the
     * form: `gs://BUCKET_NAME[/NAMESPACE_PATH]`, where `BUCKET_NAME` is the name
     * of the Google Cloud Storage bucket and `NAMESPACE_PATH` is an optional
     * Google Cloud Storage namespace path. When
     * choosing a name, be sure to consider Google Cloud Storage naming
     * guidelines: https://cloud.google.com/storage/docs/naming.
     * If the URI is a bucket (without a namespace path), a prefix will be
     * generated based on the start time.
     * </pre>
     *
     * <code>string output_uri_prefix = 3;</code>
     *
     * @param value The outputUriPrefix to set.
     * @return This builder for chaining.
     */
    public Builder setOutputUriPrefix(java.lang.String value) {
      if (value == null) {
        throw new NullPointerException();
      }

      outputUriPrefix_ = value;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The output URI. Currently only supports Google Cloud Storage URIs of the
     * form: `gs://BUCKET_NAME[/NAMESPACE_PATH]`, where `BUCKET_NAME` is the name
     * of the Google Cloud Storage bucket and `NAMESPACE_PATH` is an optional
     * Google Cloud Storage namespace path. When
     * choosing a name, be sure to consider Google Cloud Storage naming
     * guidelines: https://cloud.google.com/storage/docs/naming.
     * If the URI is a bucket (without a namespace path), a prefix will be
     * generated based on the start time.
     * </pre>
     *
     * <code>string output_uri_prefix = 3;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearOutputUriPrefix() {

      outputUriPrefix_ = getDefaultInstance().getOutputUriPrefix();
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The output URI. Currently only supports Google Cloud Storage URIs of the
     * form: `gs://BUCKET_NAME[/NAMESPACE_PATH]`, where `BUCKET_NAME` is the name
     * of the Google Cloud Storage bucket and `NAMESPACE_PATH` is an optional
     * Google Cloud Storage namespace path. When
     * choosing a name, be sure to consider Google Cloud Storage naming
     * guidelines: https://cloud.google.com/storage/docs/naming.
     * If the URI is a bucket (without a namespace path), a prefix will be
     * generated based on the start time.
     * </pre>
     *
     * <code>string output_uri_prefix = 3;</code>
     *
     * @param value The bytes for outputUriPrefix to set.
     * @return This builder for chaining.
     */
    public Builder setOutputUriPrefixBytes(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      checkByteStringIsUtf8(value);

      outputUriPrefix_ = value;
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

    // @@protoc_insertion_point(builder_scope:google.firestore.admin.v1.ExportDocumentsRequest)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.admin.v1.ExportDocumentsRequest)
  private static final com.google.firestore.admin.v1.ExportDocumentsRequest DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.firestore.admin.v1.ExportDocumentsRequest();
  }

  public static com.google.firestore.admin.v1.ExportDocumentsRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ExportDocumentsRequest> PARSER =
      new com.google.protobuf.AbstractParser<ExportDocumentsRequest>() {
        @java.lang.Override
        public ExportDocumentsRequest parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new ExportDocumentsRequest(input, extensionRegistry);
        }
      };

  public static com.google.protobuf.Parser<ExportDocumentsRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ExportDocumentsRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.firestore.admin.v1.ExportDocumentsRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
