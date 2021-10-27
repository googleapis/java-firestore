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
// source: google/firestore/bundle/bundle.proto

package com.google.firestore.bundle;

/**
 *
 *
 * <pre>
 * Metadata describing a Firestore document saved in the bundle.
 * </pre>
 *
 * Protobuf type {@code google.firestore.bundle.BundledDocumentMetadata}
 */
public final class BundledDocumentMetadata extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.firestore.bundle.BundledDocumentMetadata)
    BundledDocumentMetadataOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use BundledDocumentMetadata.newBuilder() to construct.
  private BundledDocumentMetadata(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private BundledDocumentMetadata() {
    name_ = "";
    queries_ = com.google.protobuf.LazyStringArrayList.EMPTY;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new BundledDocumentMetadata();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
    return this.unknownFields;
  }

  private BundledDocumentMetadata(
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
              com.google.protobuf.Timestamp.Builder subBuilder = null;
              if (readTime_ != null) {
                subBuilder = readTime_.toBuilder();
              }
              readTime_ =
                  input.readMessage(com.google.protobuf.Timestamp.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(readTime_);
                readTime_ = subBuilder.buildPartial();
              }

              break;
            }
          case 24:
            {
              exists_ = input.readBool();
              break;
            }
          case 34:
            {
              java.lang.String s = input.readStringRequireUtf8();
              if (!((mutable_bitField0_ & 0x00000001) != 0)) {
                queries_ = new com.google.protobuf.LazyStringArrayList();
                mutable_bitField0_ |= 0x00000001;
              }
              queries_.add(s);
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
        queries_ = queries_.getUnmodifiableView();
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.firestore.bundle.BundleProto
        .internal_static_google_firestore_bundle_BundledDocumentMetadata_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.bundle.BundleProto
        .internal_static_google_firestore_bundle_BundledDocumentMetadata_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.bundle.BundledDocumentMetadata.class,
            com.google.firestore.bundle.BundledDocumentMetadata.Builder.class);
  }

  public static final int NAME_FIELD_NUMBER = 1;
  private volatile java.lang.Object name_;
  /**
   *
   *
   * <pre>
   * The document key of a bundled document.
   * </pre>
   *
   * <code>string name = 1;</code>
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
   * The document key of a bundled document.
   * </pre>
   *
   * <code>string name = 1;</code>
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

  public static final int READ_TIME_FIELD_NUMBER = 2;
  private com.google.protobuf.Timestamp readTime_;
  /**
   *
   *
   * <pre>
   * The snapshot version of the document data bundled.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 2;</code>
   *
   * @return Whether the readTime field is set.
   */
  @java.lang.Override
  public boolean hasReadTime() {
    return readTime_ != null;
  }
  /**
   *
   *
   * <pre>
   * The snapshot version of the document data bundled.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 2;</code>
   *
   * @return The readTime.
   */
  @java.lang.Override
  public com.google.protobuf.Timestamp getReadTime() {
    return readTime_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : readTime_;
  }
  /**
   *
   *
   * <pre>
   * The snapshot version of the document data bundled.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 2;</code>
   */
  @java.lang.Override
  public com.google.protobuf.TimestampOrBuilder getReadTimeOrBuilder() {
    return getReadTime();
  }

  public static final int EXISTS_FIELD_NUMBER = 3;
  private boolean exists_;
  /**
   *
   *
   * <pre>
   * Whether the document exists.
   * </pre>
   *
   * <code>bool exists = 3;</code>
   *
   * @return The exists.
   */
  @java.lang.Override
  public boolean getExists() {
    return exists_;
  }

  public static final int QUERIES_FIELD_NUMBER = 4;
  private com.google.protobuf.LazyStringList queries_;
  /**
   *
   *
   * <pre>
   * The names of the queries in this bundle that this document matches to.
   * </pre>
   *
   * <code>repeated string queries = 4;</code>
   *
   * @return A list containing the queries.
   */
  public com.google.protobuf.ProtocolStringList getQueriesList() {
    return queries_;
  }
  /**
   *
   *
   * <pre>
   * The names of the queries in this bundle that this document matches to.
   * </pre>
   *
   * <code>repeated string queries = 4;</code>
   *
   * @return The count of queries.
   */
  public int getQueriesCount() {
    return queries_.size();
  }
  /**
   *
   *
   * <pre>
   * The names of the queries in this bundle that this document matches to.
   * </pre>
   *
   * <code>repeated string queries = 4;</code>
   *
   * @param index The index of the element to return.
   * @return The queries at the given index.
   */
  public java.lang.String getQueries(int index) {
    return queries_.get(index);
  }
  /**
   *
   *
   * <pre>
   * The names of the queries in this bundle that this document matches to.
   * </pre>
   *
   * <code>repeated string queries = 4;</code>
   *
   * @param index The index of the value to return.
   * @return The bytes of the queries at the given index.
   */
  public com.google.protobuf.ByteString getQueriesBytes(int index) {
    return queries_.getByteString(index);
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, name_);
    }
    if (readTime_ != null) {
      output.writeMessage(2, getReadTime());
    }
    if (exists_ != false) {
      output.writeBool(3, exists_);
    }
    for (int i = 0; i < queries_.size(); i++) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, queries_.getRaw(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, name_);
    }
    if (readTime_ != null) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(2, getReadTime());
    }
    if (exists_ != false) {
      size += com.google.protobuf.CodedOutputStream.computeBoolSize(3, exists_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < queries_.size(); i++) {
        dataSize += computeStringSizeNoTag(queries_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getQueriesList().size();
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
    if (!(obj instanceof com.google.firestore.bundle.BundledDocumentMetadata)) {
      return super.equals(obj);
    }
    com.google.firestore.bundle.BundledDocumentMetadata other =
        (com.google.firestore.bundle.BundledDocumentMetadata) obj;

    if (!getName().equals(other.getName())) return false;
    if (hasReadTime() != other.hasReadTime()) return false;
    if (hasReadTime()) {
      if (!getReadTime().equals(other.getReadTime())) return false;
    }
    if (getExists() != other.getExists()) return false;
    if (!getQueriesList().equals(other.getQueriesList())) return false;
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
    if (hasReadTime()) {
      hash = (37 * hash) + READ_TIME_FIELD_NUMBER;
      hash = (53 * hash) + getReadTime().hashCode();
    }
    hash = (37 * hash) + EXISTS_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(getExists());
    if (getQueriesCount() > 0) {
      hash = (37 * hash) + QUERIES_FIELD_NUMBER;
      hash = (53 * hash) + getQueriesList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata parseFrom(
      java.nio.ByteBuffer data) throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata parseFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata parseFrom(
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

  public static Builder newBuilder(com.google.firestore.bundle.BundledDocumentMetadata prototype) {
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
   * Metadata describing a Firestore document saved in the bundle.
   * </pre>
   *
   * Protobuf type {@code google.firestore.bundle.BundledDocumentMetadata}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.firestore.bundle.BundledDocumentMetadata)
      com.google.firestore.bundle.BundledDocumentMetadataOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.firestore.bundle.BundleProto
          .internal_static_google_firestore_bundle_BundledDocumentMetadata_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.bundle.BundleProto
          .internal_static_google_firestore_bundle_BundledDocumentMetadata_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.bundle.BundledDocumentMetadata.class,
              com.google.firestore.bundle.BundledDocumentMetadata.Builder.class);
    }

    // Construct using com.google.firestore.bundle.BundledDocumentMetadata.newBuilder()
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

      if (readTimeBuilder_ == null) {
        readTime_ = null;
      } else {
        readTime_ = null;
        readTimeBuilder_ = null;
      }
      exists_ = false;

      queries_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000001);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.firestore.bundle.BundleProto
          .internal_static_google_firestore_bundle_BundledDocumentMetadata_descriptor;
    }

    @java.lang.Override
    public com.google.firestore.bundle.BundledDocumentMetadata getDefaultInstanceForType() {
      return com.google.firestore.bundle.BundledDocumentMetadata.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.firestore.bundle.BundledDocumentMetadata build() {
      com.google.firestore.bundle.BundledDocumentMetadata result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.firestore.bundle.BundledDocumentMetadata buildPartial() {
      com.google.firestore.bundle.BundledDocumentMetadata result =
          new com.google.firestore.bundle.BundledDocumentMetadata(this);
      int from_bitField0_ = bitField0_;
      result.name_ = name_;
      if (readTimeBuilder_ == null) {
        result.readTime_ = readTime_;
      } else {
        result.readTime_ = readTimeBuilder_.build();
      }
      result.exists_ = exists_;
      if (((bitField0_ & 0x00000001) != 0)) {
        queries_ = queries_.getUnmodifiableView();
        bitField0_ = (bitField0_ & ~0x00000001);
      }
      result.queries_ = queries_;
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
      if (other instanceof com.google.firestore.bundle.BundledDocumentMetadata) {
        return mergeFrom((com.google.firestore.bundle.BundledDocumentMetadata) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.bundle.BundledDocumentMetadata other) {
      if (other == com.google.firestore.bundle.BundledDocumentMetadata.getDefaultInstance())
        return this;
      if (!other.getName().isEmpty()) {
        name_ = other.name_;
        onChanged();
      }
      if (other.hasReadTime()) {
        mergeReadTime(other.getReadTime());
      }
      if (other.getExists() != false) {
        setExists(other.getExists());
      }
      if (!other.queries_.isEmpty()) {
        if (queries_.isEmpty()) {
          queries_ = other.queries_;
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          ensureQueriesIsMutable();
          queries_.addAll(other.queries_);
        }
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
      com.google.firestore.bundle.BundledDocumentMetadata parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage =
            (com.google.firestore.bundle.BundledDocumentMetadata) e.getUnfinishedMessage();
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
     * The document key of a bundled document.
     * </pre>
     *
     * <code>string name = 1;</code>
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
     * The document key of a bundled document.
     * </pre>
     *
     * <code>string name = 1;</code>
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
     * The document key of a bundled document.
     * </pre>
     *
     * <code>string name = 1;</code>
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
     * The document key of a bundled document.
     * </pre>
     *
     * <code>string name = 1;</code>
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
     * The document key of a bundled document.
     * </pre>
     *
     * <code>string name = 1;</code>
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

    private com.google.protobuf.Timestamp readTime_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.protobuf.Timestamp,
            com.google.protobuf.Timestamp.Builder,
            com.google.protobuf.TimestampOrBuilder>
        readTimeBuilder_;
    /**
     *
     *
     * <pre>
     * The snapshot version of the document data bundled.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 2;</code>
     *
     * @return Whether the readTime field is set.
     */
    public boolean hasReadTime() {
      return readTimeBuilder_ != null || readTime_ != null;
    }
    /**
     *
     *
     * <pre>
     * The snapshot version of the document data bundled.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 2;</code>
     *
     * @return The readTime.
     */
    public com.google.protobuf.Timestamp getReadTime() {
      if (readTimeBuilder_ == null) {
        return readTime_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : readTime_;
      } else {
        return readTimeBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * The snapshot version of the document data bundled.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 2;</code>
     */
    public Builder setReadTime(com.google.protobuf.Timestamp value) {
      if (readTimeBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        readTime_ = value;
        onChanged();
      } else {
        readTimeBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * The snapshot version of the document data bundled.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 2;</code>
     */
    public Builder setReadTime(com.google.protobuf.Timestamp.Builder builderForValue) {
      if (readTimeBuilder_ == null) {
        readTime_ = builderForValue.build();
        onChanged();
      } else {
        readTimeBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * The snapshot version of the document data bundled.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 2;</code>
     */
    public Builder mergeReadTime(com.google.protobuf.Timestamp value) {
      if (readTimeBuilder_ == null) {
        if (readTime_ != null) {
          readTime_ =
              com.google.protobuf.Timestamp.newBuilder(readTime_).mergeFrom(value).buildPartial();
        } else {
          readTime_ = value;
        }
        onChanged();
      } else {
        readTimeBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * The snapshot version of the document data bundled.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 2;</code>
     */
    public Builder clearReadTime() {
      if (readTimeBuilder_ == null) {
        readTime_ = null;
        onChanged();
      } else {
        readTime_ = null;
        readTimeBuilder_ = null;
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * The snapshot version of the document data bundled.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 2;</code>
     */
    public com.google.protobuf.Timestamp.Builder getReadTimeBuilder() {

      onChanged();
      return getReadTimeFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * The snapshot version of the document data bundled.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 2;</code>
     */
    public com.google.protobuf.TimestampOrBuilder getReadTimeOrBuilder() {
      if (readTimeBuilder_ != null) {
        return readTimeBuilder_.getMessageOrBuilder();
      } else {
        return readTime_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : readTime_;
      }
    }
    /**
     *
     *
     * <pre>
     * The snapshot version of the document data bundled.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.protobuf.Timestamp,
            com.google.protobuf.Timestamp.Builder,
            com.google.protobuf.TimestampOrBuilder>
        getReadTimeFieldBuilder() {
      if (readTimeBuilder_ == null) {
        readTimeBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.protobuf.Timestamp,
                com.google.protobuf.Timestamp.Builder,
                com.google.protobuf.TimestampOrBuilder>(
                getReadTime(), getParentForChildren(), isClean());
        readTime_ = null;
      }
      return readTimeBuilder_;
    }

    private boolean exists_;
    /**
     *
     *
     * <pre>
     * Whether the document exists.
     * </pre>
     *
     * <code>bool exists = 3;</code>
     *
     * @return The exists.
     */
    @java.lang.Override
    public boolean getExists() {
      return exists_;
    }
    /**
     *
     *
     * <pre>
     * Whether the document exists.
     * </pre>
     *
     * <code>bool exists = 3;</code>
     *
     * @param value The exists to set.
     * @return This builder for chaining.
     */
    public Builder setExists(boolean value) {

      exists_ = value;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Whether the document exists.
     * </pre>
     *
     * <code>bool exists = 3;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearExists() {

      exists_ = false;
      onChanged();
      return this;
    }

    private com.google.protobuf.LazyStringList queries_ =
        com.google.protobuf.LazyStringArrayList.EMPTY;

    private void ensureQueriesIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        queries_ = new com.google.protobuf.LazyStringArrayList(queries_);
        bitField0_ |= 0x00000001;
      }
    }
    /**
     *
     *
     * <pre>
     * The names of the queries in this bundle that this document matches to.
     * </pre>
     *
     * <code>repeated string queries = 4;</code>
     *
     * @return A list containing the queries.
     */
    public com.google.protobuf.ProtocolStringList getQueriesList() {
      return queries_.getUnmodifiableView();
    }
    /**
     *
     *
     * <pre>
     * The names of the queries in this bundle that this document matches to.
     * </pre>
     *
     * <code>repeated string queries = 4;</code>
     *
     * @return The count of queries.
     */
    public int getQueriesCount() {
      return queries_.size();
    }
    /**
     *
     *
     * <pre>
     * The names of the queries in this bundle that this document matches to.
     * </pre>
     *
     * <code>repeated string queries = 4;</code>
     *
     * @param index The index of the element to return.
     * @return The queries at the given index.
     */
    public java.lang.String getQueries(int index) {
      return queries_.get(index);
    }
    /**
     *
     *
     * <pre>
     * The names of the queries in this bundle that this document matches to.
     * </pre>
     *
     * <code>repeated string queries = 4;</code>
     *
     * @param index The index of the value to return.
     * @return The bytes of the queries at the given index.
     */
    public com.google.protobuf.ByteString getQueriesBytes(int index) {
      return queries_.getByteString(index);
    }
    /**
     *
     *
     * <pre>
     * The names of the queries in this bundle that this document matches to.
     * </pre>
     *
     * <code>repeated string queries = 4;</code>
     *
     * @param index The index to set the value at.
     * @param value The queries to set.
     * @return This builder for chaining.
     */
    public Builder setQueries(int index, java.lang.String value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureQueriesIsMutable();
      queries_.set(index, value);
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The names of the queries in this bundle that this document matches to.
     * </pre>
     *
     * <code>repeated string queries = 4;</code>
     *
     * @param value The queries to add.
     * @return This builder for chaining.
     */
    public Builder addQueries(java.lang.String value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureQueriesIsMutable();
      queries_.add(value);
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The names of the queries in this bundle that this document matches to.
     * </pre>
     *
     * <code>repeated string queries = 4;</code>
     *
     * @param values The queries to add.
     * @return This builder for chaining.
     */
    public Builder addAllQueries(java.lang.Iterable<java.lang.String> values) {
      ensureQueriesIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(values, queries_);
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The names of the queries in this bundle that this document matches to.
     * </pre>
     *
     * <code>repeated string queries = 4;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearQueries() {
      queries_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The names of the queries in this bundle that this document matches to.
     * </pre>
     *
     * <code>repeated string queries = 4;</code>
     *
     * @param value The bytes of the queries to add.
     * @return This builder for chaining.
     */
    public Builder addQueriesBytes(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      checkByteStringIsUtf8(value);
      ensureQueriesIsMutable();
      queries_.add(value);
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

    // @@protoc_insertion_point(builder_scope:google.firestore.bundle.BundledDocumentMetadata)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.bundle.BundledDocumentMetadata)
  private static final com.google.firestore.bundle.BundledDocumentMetadata DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.firestore.bundle.BundledDocumentMetadata();
  }

  public static com.google.firestore.bundle.BundledDocumentMetadata getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<BundledDocumentMetadata> PARSER =
      new com.google.protobuf.AbstractParser<BundledDocumentMetadata>() {
        @java.lang.Override
        public BundledDocumentMetadata parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new BundledDocumentMetadata(input, extensionRegistry);
        }
      };

  public static com.google.protobuf.Parser<BundledDocumentMetadata> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<BundledDocumentMetadata> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.firestore.bundle.BundledDocumentMetadata getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
