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
// source: google/firestore/bundle/bundle.proto

// Protobuf Java Version: 3.25.3
package com.google.firestore.bundle;

/**
 *
 *
 * <pre>
 * A Query associated with a name, created as part of the bundle file, and can be read
 * by client SDKs once the bundle containing them is loaded.
 * </pre>
 *
 * Protobuf type {@code google.firestore.bundle.NamedQuery}
 */
public final class NamedQuery extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.firestore.bundle.NamedQuery)
    NamedQueryOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use NamedQuery.newBuilder() to construct.
  private NamedQuery(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private NamedQuery() {
    name_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new NamedQuery();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.firestore.bundle.BundleProto
        .internal_static_google_firestore_bundle_NamedQuery_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.bundle.BundleProto
        .internal_static_google_firestore_bundle_NamedQuery_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.bundle.NamedQuery.class,
            com.google.firestore.bundle.NamedQuery.Builder.class);
  }

  private int bitField0_;
  public static final int NAME_FIELD_NUMBER = 1;

  @SuppressWarnings("serial")
  private volatile java.lang.Object name_ = "";
  /**
   *
   *
   * <pre>
   * Name of the query, such that client can use the name to load this query
   * from bundle, and resume from when the query results are materialized
   * into this bundle.
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
   * Name of the query, such that client can use the name to load this query
   * from bundle, and resume from when the query results are materialized
   * into this bundle.
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

  public static final int BUNDLED_QUERY_FIELD_NUMBER = 2;
  private com.google.firestore.bundle.BundledQuery bundledQuery_;
  /**
   *
   *
   * <pre>
   * The query saved in the bundle.
   * </pre>
   *
   * <code>.google.firestore.bundle.BundledQuery bundled_query = 2;</code>
   *
   * @return Whether the bundledQuery field is set.
   */
  @java.lang.Override
  public boolean hasBundledQuery() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   *
   *
   * <pre>
   * The query saved in the bundle.
   * </pre>
   *
   * <code>.google.firestore.bundle.BundledQuery bundled_query = 2;</code>
   *
   * @return The bundledQuery.
   */
  @java.lang.Override
  public com.google.firestore.bundle.BundledQuery getBundledQuery() {
    return bundledQuery_ == null
        ? com.google.firestore.bundle.BundledQuery.getDefaultInstance()
        : bundledQuery_;
  }
  /**
   *
   *
   * <pre>
   * The query saved in the bundle.
   * </pre>
   *
   * <code>.google.firestore.bundle.BundledQuery bundled_query = 2;</code>
   */
  @java.lang.Override
  public com.google.firestore.bundle.BundledQueryOrBuilder getBundledQueryOrBuilder() {
    return bundledQuery_ == null
        ? com.google.firestore.bundle.BundledQuery.getDefaultInstance()
        : bundledQuery_;
  }

  public static final int READ_TIME_FIELD_NUMBER = 3;
  private com.google.protobuf.Timestamp readTime_;
  /**
   *
   *
   * <pre>
   * The read time of the query, when it is used to build the bundle. This is useful to
   * resume the query from the bundle, once it is loaded by client SDKs.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 3;</code>
   *
   * @return Whether the readTime field is set.
   */
  @java.lang.Override
  public boolean hasReadTime() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   *
   *
   * <pre>
   * The read time of the query, when it is used to build the bundle. This is useful to
   * resume the query from the bundle, once it is loaded by client SDKs.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 3;</code>
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
   * The read time of the query, when it is used to build the bundle. This is useful to
   * resume the query from the bundle, once it is loaded by client SDKs.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 3;</code>
   */
  @java.lang.Override
  public com.google.protobuf.TimestampOrBuilder getReadTimeOrBuilder() {
    return readTime_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : readTime_;
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
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeMessage(2, getBundledQuery());
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeMessage(3, getReadTime());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, name_);
    }
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(2, getBundledQuery());
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(3, getReadTime());
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
    if (!(obj instanceof com.google.firestore.bundle.NamedQuery)) {
      return super.equals(obj);
    }
    com.google.firestore.bundle.NamedQuery other = (com.google.firestore.bundle.NamedQuery) obj;

    if (!getName().equals(other.getName())) return false;
    if (hasBundledQuery() != other.hasBundledQuery()) return false;
    if (hasBundledQuery()) {
      if (!getBundledQuery().equals(other.getBundledQuery())) return false;
    }
    if (hasReadTime() != other.hasReadTime()) return false;
    if (hasReadTime()) {
      if (!getReadTime().equals(other.getReadTime())) return false;
    }
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
    hash = (37 * hash) + NAME_FIELD_NUMBER;
    hash = (53 * hash) + getName().hashCode();
    if (hasBundledQuery()) {
      hash = (37 * hash) + BUNDLED_QUERY_FIELD_NUMBER;
      hash = (53 * hash) + getBundledQuery().hashCode();
    }
    if (hasReadTime()) {
      hash = (37 * hash) + READ_TIME_FIELD_NUMBER;
      hash = (53 * hash) + getReadTime().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.bundle.NamedQuery parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.bundle.NamedQuery parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.bundle.NamedQuery parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.bundle.NamedQuery parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.bundle.NamedQuery parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.bundle.NamedQuery parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.bundle.NamedQuery parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.bundle.NamedQuery parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.bundle.NamedQuery parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.firestore.bundle.NamedQuery parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.bundle.NamedQuery parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.bundle.NamedQuery parseFrom(
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

  public static Builder newBuilder(com.google.firestore.bundle.NamedQuery prototype) {
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
   * A Query associated with a name, created as part of the bundle file, and can be read
   * by client SDKs once the bundle containing them is loaded.
   * </pre>
   *
   * Protobuf type {@code google.firestore.bundle.NamedQuery}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.firestore.bundle.NamedQuery)
      com.google.firestore.bundle.NamedQueryOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.firestore.bundle.BundleProto
          .internal_static_google_firestore_bundle_NamedQuery_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.bundle.BundleProto
          .internal_static_google_firestore_bundle_NamedQuery_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.bundle.NamedQuery.class,
              com.google.firestore.bundle.NamedQuery.Builder.class);
    }

    // Construct using com.google.firestore.bundle.NamedQuery.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }

    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders) {
        getBundledQueryFieldBuilder();
        getReadTimeFieldBuilder();
      }
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      name_ = "";
      bundledQuery_ = null;
      if (bundledQueryBuilder_ != null) {
        bundledQueryBuilder_.dispose();
        bundledQueryBuilder_ = null;
      }
      readTime_ = null;
      if (readTimeBuilder_ != null) {
        readTimeBuilder_.dispose();
        readTimeBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.firestore.bundle.BundleProto
          .internal_static_google_firestore_bundle_NamedQuery_descriptor;
    }

    @java.lang.Override
    public com.google.firestore.bundle.NamedQuery getDefaultInstanceForType() {
      return com.google.firestore.bundle.NamedQuery.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.firestore.bundle.NamedQuery build() {
      com.google.firestore.bundle.NamedQuery result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.firestore.bundle.NamedQuery buildPartial() {
      com.google.firestore.bundle.NamedQuery result =
          new com.google.firestore.bundle.NamedQuery(this);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.google.firestore.bundle.NamedQuery result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.name_ = name_;
      }
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.bundledQuery_ =
            bundledQueryBuilder_ == null ? bundledQuery_ : bundledQueryBuilder_.build();
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.readTime_ = readTimeBuilder_ == null ? readTime_ : readTimeBuilder_.build();
        to_bitField0_ |= 0x00000002;
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
      if (other instanceof com.google.firestore.bundle.NamedQuery) {
        return mergeFrom((com.google.firestore.bundle.NamedQuery) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.bundle.NamedQuery other) {
      if (other == com.google.firestore.bundle.NamedQuery.getDefaultInstance()) return this;
      if (!other.getName().isEmpty()) {
        name_ = other.name_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (other.hasBundledQuery()) {
        mergeBundledQuery(other.getBundledQuery());
      }
      if (other.hasReadTime()) {
        mergeReadTime(other.getReadTime());
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
                name_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000001;
                break;
              } // case 10
            case 18:
              {
                input.readMessage(getBundledQueryFieldBuilder().getBuilder(), extensionRegistry);
                bitField0_ |= 0x00000002;
                break;
              } // case 18
            case 26:
              {
                input.readMessage(getReadTimeFieldBuilder().getBuilder(), extensionRegistry);
                bitField0_ |= 0x00000004;
                break;
              } // case 26
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

    private java.lang.Object name_ = "";
    /**
     *
     *
     * <pre>
     * Name of the query, such that client can use the name to load this query
     * from bundle, and resume from when the query results are materialized
     * into this bundle.
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
     * Name of the query, such that client can use the name to load this query
     * from bundle, and resume from when the query results are materialized
     * into this bundle.
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
     * Name of the query, such that client can use the name to load this query
     * from bundle, and resume from when the query results are materialized
     * into this bundle.
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
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Name of the query, such that client can use the name to load this query
     * from bundle, and resume from when the query results are materialized
     * into this bundle.
     * </pre>
     *
     * <code>string name = 1;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearName() {
      name_ = getDefaultInstance().getName();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Name of the query, such that client can use the name to load this query
     * from bundle, and resume from when the query results are materialized
     * into this bundle.
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
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private com.google.firestore.bundle.BundledQuery bundledQuery_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.firestore.bundle.BundledQuery,
            com.google.firestore.bundle.BundledQuery.Builder,
            com.google.firestore.bundle.BundledQueryOrBuilder>
        bundledQueryBuilder_;
    /**
     *
     *
     * <pre>
     * The query saved in the bundle.
     * </pre>
     *
     * <code>.google.firestore.bundle.BundledQuery bundled_query = 2;</code>
     *
     * @return Whether the bundledQuery field is set.
     */
    public boolean hasBundledQuery() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     *
     *
     * <pre>
     * The query saved in the bundle.
     * </pre>
     *
     * <code>.google.firestore.bundle.BundledQuery bundled_query = 2;</code>
     *
     * @return The bundledQuery.
     */
    public com.google.firestore.bundle.BundledQuery getBundledQuery() {
      if (bundledQueryBuilder_ == null) {
        return bundledQuery_ == null
            ? com.google.firestore.bundle.BundledQuery.getDefaultInstance()
            : bundledQuery_;
      } else {
        return bundledQueryBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * The query saved in the bundle.
     * </pre>
     *
     * <code>.google.firestore.bundle.BundledQuery bundled_query = 2;</code>
     */
    public Builder setBundledQuery(com.google.firestore.bundle.BundledQuery value) {
      if (bundledQueryBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        bundledQuery_ = value;
      } else {
        bundledQueryBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The query saved in the bundle.
     * </pre>
     *
     * <code>.google.firestore.bundle.BundledQuery bundled_query = 2;</code>
     */
    public Builder setBundledQuery(
        com.google.firestore.bundle.BundledQuery.Builder builderForValue) {
      if (bundledQueryBuilder_ == null) {
        bundledQuery_ = builderForValue.build();
      } else {
        bundledQueryBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The query saved in the bundle.
     * </pre>
     *
     * <code>.google.firestore.bundle.BundledQuery bundled_query = 2;</code>
     */
    public Builder mergeBundledQuery(com.google.firestore.bundle.BundledQuery value) {
      if (bundledQueryBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0)
            && bundledQuery_ != null
            && bundledQuery_ != com.google.firestore.bundle.BundledQuery.getDefaultInstance()) {
          getBundledQueryBuilder().mergeFrom(value);
        } else {
          bundledQuery_ = value;
        }
      } else {
        bundledQueryBuilder_.mergeFrom(value);
      }
      if (bundledQuery_ != null) {
        bitField0_ |= 0x00000002;
        onChanged();
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The query saved in the bundle.
     * </pre>
     *
     * <code>.google.firestore.bundle.BundledQuery bundled_query = 2;</code>
     */
    public Builder clearBundledQuery() {
      bitField0_ = (bitField0_ & ~0x00000002);
      bundledQuery_ = null;
      if (bundledQueryBuilder_ != null) {
        bundledQueryBuilder_.dispose();
        bundledQueryBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The query saved in the bundle.
     * </pre>
     *
     * <code>.google.firestore.bundle.BundledQuery bundled_query = 2;</code>
     */
    public com.google.firestore.bundle.BundledQuery.Builder getBundledQueryBuilder() {
      bitField0_ |= 0x00000002;
      onChanged();
      return getBundledQueryFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * The query saved in the bundle.
     * </pre>
     *
     * <code>.google.firestore.bundle.BundledQuery bundled_query = 2;</code>
     */
    public com.google.firestore.bundle.BundledQueryOrBuilder getBundledQueryOrBuilder() {
      if (bundledQueryBuilder_ != null) {
        return bundledQueryBuilder_.getMessageOrBuilder();
      } else {
        return bundledQuery_ == null
            ? com.google.firestore.bundle.BundledQuery.getDefaultInstance()
            : bundledQuery_;
      }
    }
    /**
     *
     *
     * <pre>
     * The query saved in the bundle.
     * </pre>
     *
     * <code>.google.firestore.bundle.BundledQuery bundled_query = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.firestore.bundle.BundledQuery,
            com.google.firestore.bundle.BundledQuery.Builder,
            com.google.firestore.bundle.BundledQueryOrBuilder>
        getBundledQueryFieldBuilder() {
      if (bundledQueryBuilder_ == null) {
        bundledQueryBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.firestore.bundle.BundledQuery,
                com.google.firestore.bundle.BundledQuery.Builder,
                com.google.firestore.bundle.BundledQueryOrBuilder>(
                getBundledQuery(), getParentForChildren(), isClean());
        bundledQuery_ = null;
      }
      return bundledQueryBuilder_;
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
     * The read time of the query, when it is used to build the bundle. This is useful to
     * resume the query from the bundle, once it is loaded by client SDKs.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
     *
     * @return Whether the readTime field is set.
     */
    public boolean hasReadTime() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     *
     *
     * <pre>
     * The read time of the query, when it is used to build the bundle. This is useful to
     * resume the query from the bundle, once it is loaded by client SDKs.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
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
     * The read time of the query, when it is used to build the bundle. This is useful to
     * resume the query from the bundle, once it is loaded by client SDKs.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
     */
    public Builder setReadTime(com.google.protobuf.Timestamp value) {
      if (readTimeBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        readTime_ = value;
      } else {
        readTimeBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The read time of the query, when it is used to build the bundle. This is useful to
     * resume the query from the bundle, once it is loaded by client SDKs.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
     */
    public Builder setReadTime(com.google.protobuf.Timestamp.Builder builderForValue) {
      if (readTimeBuilder_ == null) {
        readTime_ = builderForValue.build();
      } else {
        readTimeBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The read time of the query, when it is used to build the bundle. This is useful to
     * resume the query from the bundle, once it is loaded by client SDKs.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
     */
    public Builder mergeReadTime(com.google.protobuf.Timestamp value) {
      if (readTimeBuilder_ == null) {
        if (((bitField0_ & 0x00000004) != 0)
            && readTime_ != null
            && readTime_ != com.google.protobuf.Timestamp.getDefaultInstance()) {
          getReadTimeBuilder().mergeFrom(value);
        } else {
          readTime_ = value;
        }
      } else {
        readTimeBuilder_.mergeFrom(value);
      }
      if (readTime_ != null) {
        bitField0_ |= 0x00000004;
        onChanged();
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The read time of the query, when it is used to build the bundle. This is useful to
     * resume the query from the bundle, once it is loaded by client SDKs.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
     */
    public Builder clearReadTime() {
      bitField0_ = (bitField0_ & ~0x00000004);
      readTime_ = null;
      if (readTimeBuilder_ != null) {
        readTimeBuilder_.dispose();
        readTimeBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The read time of the query, when it is used to build the bundle. This is useful to
     * resume the query from the bundle, once it is loaded by client SDKs.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
     */
    public com.google.protobuf.Timestamp.Builder getReadTimeBuilder() {
      bitField0_ |= 0x00000004;
      onChanged();
      return getReadTimeFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * The read time of the query, when it is used to build the bundle. This is useful to
     * resume the query from the bundle, once it is loaded by client SDKs.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
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
     * The read time of the query, when it is used to build the bundle. This is useful to
     * resume the query from the bundle, once it is loaded by client SDKs.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
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

    @java.lang.Override
    public final Builder setUnknownFields(final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }

    // @@protoc_insertion_point(builder_scope:google.firestore.bundle.NamedQuery)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.bundle.NamedQuery)
  private static final com.google.firestore.bundle.NamedQuery DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.firestore.bundle.NamedQuery();
  }

  public static com.google.firestore.bundle.NamedQuery getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<NamedQuery> PARSER =
      new com.google.protobuf.AbstractParser<NamedQuery>() {
        @java.lang.Override
        public NamedQuery parsePartialFrom(
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

  public static com.google.protobuf.Parser<NamedQuery> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<NamedQuery> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.firestore.bundle.NamedQuery getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
