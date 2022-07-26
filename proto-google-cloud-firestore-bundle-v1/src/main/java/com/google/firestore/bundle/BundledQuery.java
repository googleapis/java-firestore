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
// source: google/firestore/bundle/bundle.proto

package com.google.firestore.bundle;

/**
 *
 *
 * <pre>
 * Encodes a query saved in the bundle.
 * </pre>
 *
 * Protobuf type {@code google.firestore.bundle.BundledQuery}
 */
public final class BundledQuery extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.firestore.bundle.BundledQuery)
    BundledQueryOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use BundledQuery.newBuilder() to construct.
  private BundledQuery(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private BundledQuery() {
    parent_ = "";
    limitType_ = 0;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new BundledQuery();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
    return this.unknownFields;
  }

  private BundledQuery(
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

              parent_ = s;
              break;
            }
          case 18:
            {
              com.google.firestore.v1.StructuredQuery.Builder subBuilder = null;
              if (queryTypeCase_ == 2) {
                subBuilder = ((com.google.firestore.v1.StructuredQuery) queryType_).toBuilder();
              }
              queryType_ =
                  input.readMessage(
                      com.google.firestore.v1.StructuredQuery.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom((com.google.firestore.v1.StructuredQuery) queryType_);
                queryType_ = subBuilder.buildPartial();
              }
              queryTypeCase_ = 2;
              break;
            }
          case 24:
            {
              int rawValue = input.readEnum();

              limitType_ = rawValue;
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
    } catch (com.google.protobuf.UninitializedMessageException e) {
      throw e.asInvalidProtocolBufferException().setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.firestore.bundle.BundleProto
        .internal_static_google_firestore_bundle_BundledQuery_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.bundle.BundleProto
        .internal_static_google_firestore_bundle_BundledQuery_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.bundle.BundledQuery.class,
            com.google.firestore.bundle.BundledQuery.Builder.class);
  }

  /**
   *
   *
   * <pre>
   * If the query is a limit query, should the limit be applied to the beginning or
   * the end of results.
   * </pre>
   *
   * Protobuf enum {@code google.firestore.bundle.BundledQuery.LimitType}
   */
  public enum LimitType implements com.google.protobuf.ProtocolMessageEnum {
    /** <code>FIRST = 0;</code> */
    FIRST(0),
    /** <code>LAST = 1;</code> */
    LAST(1),
    UNRECOGNIZED(-1),
    ;

    /** <code>FIRST = 0;</code> */
    public static final int FIRST_VALUE = 0;
    /** <code>LAST = 1;</code> */
    public static final int LAST_VALUE = 1;

    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @param value The numeric wire value of the corresponding enum entry.
     * @return The enum associated with the given numeric wire value.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static LimitType valueOf(int value) {
      return forNumber(value);
    }

    /**
     * @param value The numeric wire value of the corresponding enum entry.
     * @return The enum associated with the given numeric wire value.
     */
    public static LimitType forNumber(int value) {
      switch (value) {
        case 0:
          return FIRST;
        case 1:
          return LAST;
        default:
          return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<LimitType> internalGetValueMap() {
      return internalValueMap;
    }

    private static final com.google.protobuf.Internal.EnumLiteMap<LimitType> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<LimitType>() {
          public LimitType findValueByNumber(int number) {
            return LimitType.forNumber(number);
          }
        };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor getValueDescriptor() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalStateException(
            "Can't get the descriptor of an unrecognized enum value.");
      }
      return getDescriptor().getValues().get(ordinal());
    }

    public final com.google.protobuf.Descriptors.EnumDescriptor getDescriptorForType() {
      return getDescriptor();
    }

    public static final com.google.protobuf.Descriptors.EnumDescriptor getDescriptor() {
      return com.google.firestore.bundle.BundledQuery.getDescriptor().getEnumTypes().get(0);
    }

    private static final LimitType[] VALUES = values();

    public static LimitType valueOf(com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException("EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private LimitType(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:google.firestore.bundle.BundledQuery.LimitType)
  }

  private int queryTypeCase_ = 0;
  private java.lang.Object queryType_;

  public enum QueryTypeCase
      implements
          com.google.protobuf.Internal.EnumLite,
          com.google.protobuf.AbstractMessage.InternalOneOfEnum {
    STRUCTURED_QUERY(2),
    QUERYTYPE_NOT_SET(0);
    private final int value;

    private QueryTypeCase(int value) {
      this.value = value;
    }
    /**
     * @param value The number of the enum to look for.
     * @return The enum associated with the given number.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static QueryTypeCase valueOf(int value) {
      return forNumber(value);
    }

    public static QueryTypeCase forNumber(int value) {
      switch (value) {
        case 2:
          return STRUCTURED_QUERY;
        case 0:
          return QUERYTYPE_NOT_SET;
        default:
          return null;
      }
    }

    public int getNumber() {
      return this.value;
    }
  };

  public QueryTypeCase getQueryTypeCase() {
    return QueryTypeCase.forNumber(queryTypeCase_);
  }

  public static final int PARENT_FIELD_NUMBER = 1;
  private volatile java.lang.Object parent_;
  /**
   *
   *
   * <pre>
   * The parent resource name.
   * </pre>
   *
   * <code>string parent = 1;</code>
   *
   * @return The parent.
   */
  @java.lang.Override
  public java.lang.String getParent() {
    java.lang.Object ref = parent_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      parent_ = s;
      return s;
    }
  }
  /**
   *
   *
   * <pre>
   * The parent resource name.
   * </pre>
   *
   * <code>string parent = 1;</code>
   *
   * @return The bytes for parent.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getParentBytes() {
    java.lang.Object ref = parent_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
      parent_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int STRUCTURED_QUERY_FIELD_NUMBER = 2;
  /**
   *
   *
   * <pre>
   * A structured query.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
   *
   * @return Whether the structuredQuery field is set.
   */
  @java.lang.Override
  public boolean hasStructuredQuery() {
    return queryTypeCase_ == 2;
  }
  /**
   *
   *
   * <pre>
   * A structured query.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
   *
   * @return The structuredQuery.
   */
  @java.lang.Override
  public com.google.firestore.v1.StructuredQuery getStructuredQuery() {
    if (queryTypeCase_ == 2) {
      return (com.google.firestore.v1.StructuredQuery) queryType_;
    }
    return com.google.firestore.v1.StructuredQuery.getDefaultInstance();
  }
  /**
   *
   *
   * <pre>
   * A structured query.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
   */
  @java.lang.Override
  public com.google.firestore.v1.StructuredQueryOrBuilder getStructuredQueryOrBuilder() {
    if (queryTypeCase_ == 2) {
      return (com.google.firestore.v1.StructuredQuery) queryType_;
    }
    return com.google.firestore.v1.StructuredQuery.getDefaultInstance();
  }

  public static final int LIMIT_TYPE_FIELD_NUMBER = 3;
  private int limitType_;
  /**
   * <code>.google.firestore.bundle.BundledQuery.LimitType limit_type = 3;</code>
   *
   * @return The enum numeric value on the wire for limitType.
   */
  @java.lang.Override
  public int getLimitTypeValue() {
    return limitType_;
  }
  /**
   * <code>.google.firestore.bundle.BundledQuery.LimitType limit_type = 3;</code>
   *
   * @return The limitType.
   */
  @java.lang.Override
  public com.google.firestore.bundle.BundledQuery.LimitType getLimitType() {
    @SuppressWarnings("deprecation")
    com.google.firestore.bundle.BundledQuery.LimitType result =
        com.google.firestore.bundle.BundledQuery.LimitType.valueOf(limitType_);
    return result == null
        ? com.google.firestore.bundle.BundledQuery.LimitType.UNRECOGNIZED
        : result;
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(parent_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, parent_);
    }
    if (queryTypeCase_ == 2) {
      output.writeMessage(2, (com.google.firestore.v1.StructuredQuery) queryType_);
    }
    if (limitType_ != com.google.firestore.bundle.BundledQuery.LimitType.FIRST.getNumber()) {
      output.writeEnum(3, limitType_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(parent_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, parent_);
    }
    if (queryTypeCase_ == 2) {
      size +=
          com.google.protobuf.CodedOutputStream.computeMessageSize(
              2, (com.google.firestore.v1.StructuredQuery) queryType_);
    }
    if (limitType_ != com.google.firestore.bundle.BundledQuery.LimitType.FIRST.getNumber()) {
      size += com.google.protobuf.CodedOutputStream.computeEnumSize(3, limitType_);
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
    if (!(obj instanceof com.google.firestore.bundle.BundledQuery)) {
      return super.equals(obj);
    }
    com.google.firestore.bundle.BundledQuery other = (com.google.firestore.bundle.BundledQuery) obj;

    if (!getParent().equals(other.getParent())) return false;
    if (limitType_ != other.limitType_) return false;
    if (!getQueryTypeCase().equals(other.getQueryTypeCase())) return false;
    switch (queryTypeCase_) {
      case 2:
        if (!getStructuredQuery().equals(other.getStructuredQuery())) return false;
        break;
      case 0:
      default:
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
    hash = (37 * hash) + PARENT_FIELD_NUMBER;
    hash = (53 * hash) + getParent().hashCode();
    hash = (37 * hash) + LIMIT_TYPE_FIELD_NUMBER;
    hash = (53 * hash) + limitType_;
    switch (queryTypeCase_) {
      case 2:
        hash = (37 * hash) + STRUCTURED_QUERY_FIELD_NUMBER;
        hash = (53 * hash) + getStructuredQuery().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.bundle.BundledQuery parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.bundle.BundledQuery parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.bundle.BundledQuery parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.bundle.BundledQuery parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.bundle.BundledQuery parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.bundle.BundledQuery parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.bundle.BundledQuery parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.bundle.BundledQuery parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.bundle.BundledQuery parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.firestore.bundle.BundledQuery parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.bundle.BundledQuery parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.bundle.BundledQuery parseFrom(
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

  public static Builder newBuilder(com.google.firestore.bundle.BundledQuery prototype) {
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
   * Encodes a query saved in the bundle.
   * </pre>
   *
   * Protobuf type {@code google.firestore.bundle.BundledQuery}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.firestore.bundle.BundledQuery)
      com.google.firestore.bundle.BundledQueryOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.firestore.bundle.BundleProto
          .internal_static_google_firestore_bundle_BundledQuery_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.bundle.BundleProto
          .internal_static_google_firestore_bundle_BundledQuery_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.bundle.BundledQuery.class,
              com.google.firestore.bundle.BundledQuery.Builder.class);
    }

    // Construct using com.google.firestore.bundle.BundledQuery.newBuilder()
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
      parent_ = "";

      limitType_ = 0;

      queryTypeCase_ = 0;
      queryType_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.firestore.bundle.BundleProto
          .internal_static_google_firestore_bundle_BundledQuery_descriptor;
    }

    @java.lang.Override
    public com.google.firestore.bundle.BundledQuery getDefaultInstanceForType() {
      return com.google.firestore.bundle.BundledQuery.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.firestore.bundle.BundledQuery build() {
      com.google.firestore.bundle.BundledQuery result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.firestore.bundle.BundledQuery buildPartial() {
      com.google.firestore.bundle.BundledQuery result =
          new com.google.firestore.bundle.BundledQuery(this);
      result.parent_ = parent_;
      if (queryTypeCase_ == 2) {
        if (structuredQueryBuilder_ == null) {
          result.queryType_ = queryType_;
        } else {
          result.queryType_ = structuredQueryBuilder_.build();
        }
      }
      result.limitType_ = limitType_;
      result.queryTypeCase_ = queryTypeCase_;
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
      if (other instanceof com.google.firestore.bundle.BundledQuery) {
        return mergeFrom((com.google.firestore.bundle.BundledQuery) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.bundle.BundledQuery other) {
      if (other == com.google.firestore.bundle.BundledQuery.getDefaultInstance()) return this;
      if (!other.getParent().isEmpty()) {
        parent_ = other.parent_;
        onChanged();
      }
      if (other.limitType_ != 0) {
        setLimitTypeValue(other.getLimitTypeValue());
      }
      switch (other.getQueryTypeCase()) {
        case STRUCTURED_QUERY:
          {
            mergeStructuredQuery(other.getStructuredQuery());
            break;
          }
        case QUERYTYPE_NOT_SET:
          {
            break;
          }
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
      com.google.firestore.bundle.BundledQuery parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.google.firestore.bundle.BundledQuery) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int queryTypeCase_ = 0;
    private java.lang.Object queryType_;

    public QueryTypeCase getQueryTypeCase() {
      return QueryTypeCase.forNumber(queryTypeCase_);
    }

    public Builder clearQueryType() {
      queryTypeCase_ = 0;
      queryType_ = null;
      onChanged();
      return this;
    }

    private java.lang.Object parent_ = "";
    /**
     *
     *
     * <pre>
     * The parent resource name.
     * </pre>
     *
     * <code>string parent = 1;</code>
     *
     * @return The parent.
     */
    public java.lang.String getParent() {
      java.lang.Object ref = parent_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        parent_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     *
     *
     * <pre>
     * The parent resource name.
     * </pre>
     *
     * <code>string parent = 1;</code>
     *
     * @return The bytes for parent.
     */
    public com.google.protobuf.ByteString getParentBytes() {
      java.lang.Object ref = parent_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
        parent_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     *
     *
     * <pre>
     * The parent resource name.
     * </pre>
     *
     * <code>string parent = 1;</code>
     *
     * @param value The parent to set.
     * @return This builder for chaining.
     */
    public Builder setParent(java.lang.String value) {
      if (value == null) {
        throw new NullPointerException();
      }

      parent_ = value;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The parent resource name.
     * </pre>
     *
     * <code>string parent = 1;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearParent() {

      parent_ = getDefaultInstance().getParent();
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The parent resource name.
     * </pre>
     *
     * <code>string parent = 1;</code>
     *
     * @param value The bytes for parent to set.
     * @return This builder for chaining.
     */
    public Builder setParentBytes(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      checkByteStringIsUtf8(value);

      parent_ = value;
      onChanged();
      return this;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.firestore.v1.StructuredQuery,
            com.google.firestore.v1.StructuredQuery.Builder,
            com.google.firestore.v1.StructuredQueryOrBuilder>
        structuredQueryBuilder_;
    /**
     *
     *
     * <pre>
     * A structured query.
     * </pre>
     *
     * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
     *
     * @return Whether the structuredQuery field is set.
     */
    @java.lang.Override
    public boolean hasStructuredQuery() {
      return queryTypeCase_ == 2;
    }
    /**
     *
     *
     * <pre>
     * A structured query.
     * </pre>
     *
     * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
     *
     * @return The structuredQuery.
     */
    @java.lang.Override
    public com.google.firestore.v1.StructuredQuery getStructuredQuery() {
      if (structuredQueryBuilder_ == null) {
        if (queryTypeCase_ == 2) {
          return (com.google.firestore.v1.StructuredQuery) queryType_;
        }
        return com.google.firestore.v1.StructuredQuery.getDefaultInstance();
      } else {
        if (queryTypeCase_ == 2) {
          return structuredQueryBuilder_.getMessage();
        }
        return com.google.firestore.v1.StructuredQuery.getDefaultInstance();
      }
    }
    /**
     *
     *
     * <pre>
     * A structured query.
     * </pre>
     *
     * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
     */
    public Builder setStructuredQuery(com.google.firestore.v1.StructuredQuery value) {
      if (structuredQueryBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        queryType_ = value;
        onChanged();
      } else {
        structuredQueryBuilder_.setMessage(value);
      }
      queryTypeCase_ = 2;
      return this;
    }
    /**
     *
     *
     * <pre>
     * A structured query.
     * </pre>
     *
     * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
     */
    public Builder setStructuredQuery(
        com.google.firestore.v1.StructuredQuery.Builder builderForValue) {
      if (structuredQueryBuilder_ == null) {
        queryType_ = builderForValue.build();
        onChanged();
      } else {
        structuredQueryBuilder_.setMessage(builderForValue.build());
      }
      queryTypeCase_ = 2;
      return this;
    }
    /**
     *
     *
     * <pre>
     * A structured query.
     * </pre>
     *
     * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
     */
    public Builder mergeStructuredQuery(com.google.firestore.v1.StructuredQuery value) {
      if (structuredQueryBuilder_ == null) {
        if (queryTypeCase_ == 2
            && queryType_ != com.google.firestore.v1.StructuredQuery.getDefaultInstance()) {
          queryType_ =
              com.google.firestore.v1.StructuredQuery.newBuilder(
                      (com.google.firestore.v1.StructuredQuery) queryType_)
                  .mergeFrom(value)
                  .buildPartial();
        } else {
          queryType_ = value;
        }
        onChanged();
      } else {
        if (queryTypeCase_ == 2) {
          structuredQueryBuilder_.mergeFrom(value);
        } else {
          structuredQueryBuilder_.setMessage(value);
        }
      }
      queryTypeCase_ = 2;
      return this;
    }
    /**
     *
     *
     * <pre>
     * A structured query.
     * </pre>
     *
     * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
     */
    public Builder clearStructuredQuery() {
      if (structuredQueryBuilder_ == null) {
        if (queryTypeCase_ == 2) {
          queryTypeCase_ = 0;
          queryType_ = null;
          onChanged();
        }
      } else {
        if (queryTypeCase_ == 2) {
          queryTypeCase_ = 0;
          queryType_ = null;
        }
        structuredQueryBuilder_.clear();
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * A structured query.
     * </pre>
     *
     * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
     */
    public com.google.firestore.v1.StructuredQuery.Builder getStructuredQueryBuilder() {
      return getStructuredQueryFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * A structured query.
     * </pre>
     *
     * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
     */
    @java.lang.Override
    public com.google.firestore.v1.StructuredQueryOrBuilder getStructuredQueryOrBuilder() {
      if ((queryTypeCase_ == 2) && (structuredQueryBuilder_ != null)) {
        return structuredQueryBuilder_.getMessageOrBuilder();
      } else {
        if (queryTypeCase_ == 2) {
          return (com.google.firestore.v1.StructuredQuery) queryType_;
        }
        return com.google.firestore.v1.StructuredQuery.getDefaultInstance();
      }
    }
    /**
     *
     *
     * <pre>
     * A structured query.
     * </pre>
     *
     * <code>.google.firestore.v1.StructuredQuery structured_query = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.firestore.v1.StructuredQuery,
            com.google.firestore.v1.StructuredQuery.Builder,
            com.google.firestore.v1.StructuredQueryOrBuilder>
        getStructuredQueryFieldBuilder() {
      if (structuredQueryBuilder_ == null) {
        if (!(queryTypeCase_ == 2)) {
          queryType_ = com.google.firestore.v1.StructuredQuery.getDefaultInstance();
        }
        structuredQueryBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.firestore.v1.StructuredQuery,
                com.google.firestore.v1.StructuredQuery.Builder,
                com.google.firestore.v1.StructuredQueryOrBuilder>(
                (com.google.firestore.v1.StructuredQuery) queryType_,
                getParentForChildren(),
                isClean());
        queryType_ = null;
      }
      queryTypeCase_ = 2;
      onChanged();
      ;
      return structuredQueryBuilder_;
    }

    private int limitType_ = 0;
    /**
     * <code>.google.firestore.bundle.BundledQuery.LimitType limit_type = 3;</code>
     *
     * @return The enum numeric value on the wire for limitType.
     */
    @java.lang.Override
    public int getLimitTypeValue() {
      return limitType_;
    }
    /**
     * <code>.google.firestore.bundle.BundledQuery.LimitType limit_type = 3;</code>
     *
     * @param value The enum numeric value on the wire for limitType to set.
     * @return This builder for chaining.
     */
    public Builder setLimitTypeValue(int value) {

      limitType_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>.google.firestore.bundle.BundledQuery.LimitType limit_type = 3;</code>
     *
     * @return The limitType.
     */
    @java.lang.Override
    public com.google.firestore.bundle.BundledQuery.LimitType getLimitType() {
      @SuppressWarnings("deprecation")
      com.google.firestore.bundle.BundledQuery.LimitType result =
          com.google.firestore.bundle.BundledQuery.LimitType.valueOf(limitType_);
      return result == null
          ? com.google.firestore.bundle.BundledQuery.LimitType.UNRECOGNIZED
          : result;
    }
    /**
     * <code>.google.firestore.bundle.BundledQuery.LimitType limit_type = 3;</code>
     *
     * @param value The limitType to set.
     * @return This builder for chaining.
     */
    public Builder setLimitType(com.google.firestore.bundle.BundledQuery.LimitType value) {
      if (value == null) {
        throw new NullPointerException();
      }

      limitType_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.google.firestore.bundle.BundledQuery.LimitType limit_type = 3;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearLimitType() {

      limitType_ = 0;
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

    // @@protoc_insertion_point(builder_scope:google.firestore.bundle.BundledQuery)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.bundle.BundledQuery)
  private static final com.google.firestore.bundle.BundledQuery DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.firestore.bundle.BundledQuery();
  }

  public static com.google.firestore.bundle.BundledQuery getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<BundledQuery> PARSER =
      new com.google.protobuf.AbstractParser<BundledQuery>() {
        @java.lang.Override
        public BundledQuery parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new BundledQuery(input, extensionRegistry);
        }
      };

  public static com.google.protobuf.Parser<BundledQuery> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<BundledQuery> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.firestore.bundle.BundledQuery getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
