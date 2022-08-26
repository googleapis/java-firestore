/*
 * Copyright 2022 Google LLC
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
// source: google/firestore/v1/aggregation_result.proto

package com.google.firestore.v1;

/**
 * <pre>
 * The result of a single bucket from a Firestore aggregation query.
 * The keys of `aggregate_fields` are the same for all results in an aggregation
 * query, unlike document queries which can have different fields present for
 * each result.
 * </pre>
 *
 * Protobuf type {@code google.firestore.v1.AggregationResult}
 */
public final class AggregationResult extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:google.firestore.v1.AggregationResult)
    AggregationResultOrBuilder {
private static final long serialVersionUID = 0L;
  // Use AggregationResult.newBuilder() to construct.
  private AggregationResult(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private AggregationResult() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new AggregationResult();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private AggregationResult(
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
          case 18: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              aggregateFields_ = com.google.protobuf.MapField.newMapField(
                  AggregateFieldsDefaultEntryHolder.defaultEntry);
              mutable_bitField0_ |= 0x00000001;
            }
            com.google.protobuf.MapEntry<java.lang.String, com.google.firestore.v1.Value>
            aggregateFields__ = input.readMessage(
                AggregateFieldsDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistry);
            aggregateFields_.getMutableMap().put(
                aggregateFields__.getKey(), aggregateFields__.getValue());
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
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
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.google.firestore.v1.AggregationResultProto.internal_static_google_firestore_v1_AggregationResult_descriptor;
  }

  @SuppressWarnings({"rawtypes"})
  @java.lang.Override
  protected com.google.protobuf.MapField internalGetMapField(
      int number) {
    switch (number) {
      case 2:
        return internalGetAggregateFields();
      default:
        throw new RuntimeException(
            "Invalid map field number: " + number);
    }
  }
  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.v1.AggregationResultProto.internal_static_google_firestore_v1_AggregationResult_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.v1.AggregationResult.class, com.google.firestore.v1.AggregationResult.Builder.class);
  }

  public static final int AGGREGATE_FIELDS_FIELD_NUMBER = 2;
  private static final class AggregateFieldsDefaultEntryHolder {
    static final com.google.protobuf.MapEntry<
        java.lang.String, com.google.firestore.v1.Value> defaultEntry =
            com.google.protobuf.MapEntry
            .<java.lang.String, com.google.firestore.v1.Value>newDefaultInstance(
                com.google.firestore.v1.AggregationResultProto.internal_static_google_firestore_v1_AggregationResult_AggregateFieldsEntry_descriptor, 
                com.google.protobuf.WireFormat.FieldType.STRING,
                "",
                com.google.protobuf.WireFormat.FieldType.MESSAGE,
                com.google.firestore.v1.Value.getDefaultInstance());
  }
  private com.google.protobuf.MapField<
      java.lang.String, com.google.firestore.v1.Value> aggregateFields_;
  private com.google.protobuf.MapField<java.lang.String, com.google.firestore.v1.Value>
  internalGetAggregateFields() {
    if (aggregateFields_ == null) {
      return com.google.protobuf.MapField.emptyMapField(
          AggregateFieldsDefaultEntryHolder.defaultEntry);
    }
    return aggregateFields_;
  }

  public int getAggregateFieldsCount() {
    return internalGetAggregateFields().getMap().size();
  }
  /**
   * <pre>
   * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
   * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
   * assigned to the aggregation function on input and the size of this map
   * equals the number of aggregation functions in the query.
   * </pre>
   *
   * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
   */

  @java.lang.Override
  public boolean containsAggregateFields(
      java.lang.String key) {
    if (key == null) { throw new NullPointerException("map key"); }
    return internalGetAggregateFields().getMap().containsKey(key);
  }
  /**
   * Use {@link #getAggregateFieldsMap()} instead.
   */
  @java.lang.Override
  @java.lang.Deprecated
  public java.util.Map<java.lang.String, com.google.firestore.v1.Value> getAggregateFields() {
    return getAggregateFieldsMap();
  }
  /**
   * <pre>
   * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
   * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
   * assigned to the aggregation function on input and the size of this map
   * equals the number of aggregation functions in the query.
   * </pre>
   *
   * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
   */
  @java.lang.Override

  public java.util.Map<java.lang.String, com.google.firestore.v1.Value> getAggregateFieldsMap() {
    return internalGetAggregateFields().getMap();
  }
  /**
   * <pre>
   * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
   * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
   * assigned to the aggregation function on input and the size of this map
   * equals the number of aggregation functions in the query.
   * </pre>
   *
   * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
   */
  @java.lang.Override

  public com.google.firestore.v1.Value getAggregateFieldsOrDefault(
      java.lang.String key,
      com.google.firestore.v1.Value defaultValue) {
    if (key == null) { throw new NullPointerException("map key"); }
    java.util.Map<java.lang.String, com.google.firestore.v1.Value> map =
        internalGetAggregateFields().getMap();
    return map.containsKey(key) ? map.get(key) : defaultValue;
  }
  /**
   * <pre>
   * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
   * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
   * assigned to the aggregation function on input and the size of this map
   * equals the number of aggregation functions in the query.
   * </pre>
   *
   * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
   */
  @java.lang.Override

  public com.google.firestore.v1.Value getAggregateFieldsOrThrow(
      java.lang.String key) {
    if (key == null) { throw new NullPointerException("map key"); }
    java.util.Map<java.lang.String, com.google.firestore.v1.Value> map =
        internalGetAggregateFields().getMap();
    if (!map.containsKey(key)) {
      throw new java.lang.IllegalArgumentException();
    }
    return map.get(key);
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
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    com.google.protobuf.GeneratedMessageV3
      .serializeStringMapTo(
        output,
        internalGetAggregateFields(),
        AggregateFieldsDefaultEntryHolder.defaultEntry,
        2);
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (java.util.Map.Entry<java.lang.String, com.google.firestore.v1.Value> entry
         : internalGetAggregateFields().getMap().entrySet()) {
      com.google.protobuf.MapEntry<java.lang.String, com.google.firestore.v1.Value>
      aggregateFields__ = AggregateFieldsDefaultEntryHolder.defaultEntry.newBuilderForType()
          .setKey(entry.getKey())
          .setValue(entry.getValue())
          .build();
      size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, aggregateFields__);
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
    if (!(obj instanceof com.google.firestore.v1.AggregationResult)) {
      return super.equals(obj);
    }
    com.google.firestore.v1.AggregationResult other = (com.google.firestore.v1.AggregationResult) obj;

    if (!internalGetAggregateFields().equals(
        other.internalGetAggregateFields())) return false;
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
    if (!internalGetAggregateFields().getMap().isEmpty()) {
      hash = (37 * hash) + AGGREGATE_FIELDS_FIELD_NUMBER;
      hash = (53 * hash) + internalGetAggregateFields().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.v1.AggregationResult parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.google.firestore.v1.AggregationResult parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.google.firestore.v1.AggregationResult parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.google.firestore.v1.AggregationResult parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.google.firestore.v1.AggregationResult parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.google.firestore.v1.AggregationResult parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.google.firestore.v1.AggregationResult parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.google.firestore.v1.AggregationResult parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.google.firestore.v1.AggregationResult parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.google.firestore.v1.AggregationResult parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.google.firestore.v1.AggregationResult parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.google.firestore.v1.AggregationResult parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.google.firestore.v1.AggregationResult prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * The result of a single bucket from a Firestore aggregation query.
   * The keys of `aggregate_fields` are the same for all results in an aggregation
   * query, unlike document queries which can have different fields present for
   * each result.
   * </pre>
   *
   * Protobuf type {@code google.firestore.v1.AggregationResult}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:google.firestore.v1.AggregationResult)
      com.google.firestore.v1.AggregationResultOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.firestore.v1.AggregationResultProto.internal_static_google_firestore_v1_AggregationResult_descriptor;
    }

    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMapField(
        int number) {
      switch (number) {
        case 2:
          return internalGetAggregateFields();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMutableMapField(
        int number) {
      switch (number) {
        case 2:
          return internalGetMutableAggregateFields();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.v1.AggregationResultProto.internal_static_google_firestore_v1_AggregationResult_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.v1.AggregationResult.class, com.google.firestore.v1.AggregationResult.Builder.class);
    }

    // Construct using com.google.firestore.v1.AggregationResult.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      internalGetMutableAggregateFields().clear();
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.google.firestore.v1.AggregationResultProto.internal_static_google_firestore_v1_AggregationResult_descriptor;
    }

    @java.lang.Override
    public com.google.firestore.v1.AggregationResult getDefaultInstanceForType() {
      return com.google.firestore.v1.AggregationResult.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.firestore.v1.AggregationResult build() {
      com.google.firestore.v1.AggregationResult result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.firestore.v1.AggregationResult buildPartial() {
      com.google.firestore.v1.AggregationResult result = new com.google.firestore.v1.AggregationResult(this);
      int from_bitField0_ = bitField0_;
      result.aggregateFields_ = internalGetAggregateFields();
      result.aggregateFields_.makeImmutable();
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.google.firestore.v1.AggregationResult) {
        return mergeFrom((com.google.firestore.v1.AggregationResult)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.v1.AggregationResult other) {
      if (other == com.google.firestore.v1.AggregationResult.getDefaultInstance()) return this;
      internalGetMutableAggregateFields().mergeFrom(
          other.internalGetAggregateFields());
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
      com.google.firestore.v1.AggregationResult parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.google.firestore.v1.AggregationResult) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private com.google.protobuf.MapField<
        java.lang.String, com.google.firestore.v1.Value> aggregateFields_;
    private com.google.protobuf.MapField<java.lang.String, com.google.firestore.v1.Value>
    internalGetAggregateFields() {
      if (aggregateFields_ == null) {
        return com.google.protobuf.MapField.emptyMapField(
            AggregateFieldsDefaultEntryHolder.defaultEntry);
      }
      return aggregateFields_;
    }
    private com.google.protobuf.MapField<java.lang.String, com.google.firestore.v1.Value>
    internalGetMutableAggregateFields() {
      onChanged();;
      if (aggregateFields_ == null) {
        aggregateFields_ = com.google.protobuf.MapField.newMapField(
            AggregateFieldsDefaultEntryHolder.defaultEntry);
      }
      if (!aggregateFields_.isMutable()) {
        aggregateFields_ = aggregateFields_.copy();
      }
      return aggregateFields_;
    }

    public int getAggregateFieldsCount() {
      return internalGetAggregateFields().getMap().size();
    }
    /**
     * <pre>
     * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
     * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
     * assigned to the aggregation function on input and the size of this map
     * equals the number of aggregation functions in the query.
     * </pre>
     *
     * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
     */

    @java.lang.Override
    public boolean containsAggregateFields(
        java.lang.String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      return internalGetAggregateFields().getMap().containsKey(key);
    }
    /**
     * Use {@link #getAggregateFieldsMap()} instead.
     */
    @java.lang.Override
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, com.google.firestore.v1.Value> getAggregateFields() {
      return getAggregateFieldsMap();
    }
    /**
     * <pre>
     * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
     * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
     * assigned to the aggregation function on input and the size of this map
     * equals the number of aggregation functions in the query.
     * </pre>
     *
     * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
     */
    @java.lang.Override

    public java.util.Map<java.lang.String, com.google.firestore.v1.Value> getAggregateFieldsMap() {
      return internalGetAggregateFields().getMap();
    }
    /**
     * <pre>
     * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
     * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
     * assigned to the aggregation function on input and the size of this map
     * equals the number of aggregation functions in the query.
     * </pre>
     *
     * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
     */
    @java.lang.Override

    public com.google.firestore.v1.Value getAggregateFieldsOrDefault(
        java.lang.String key,
        com.google.firestore.v1.Value defaultValue) {
      if (key == null) { throw new NullPointerException("map key"); }
      java.util.Map<java.lang.String, com.google.firestore.v1.Value> map =
          internalGetAggregateFields().getMap();
      return map.containsKey(key) ? map.get(key) : defaultValue;
    }
    /**
     * <pre>
     * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
     * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
     * assigned to the aggregation function on input and the size of this map
     * equals the number of aggregation functions in the query.
     * </pre>
     *
     * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
     */
    @java.lang.Override

    public com.google.firestore.v1.Value getAggregateFieldsOrThrow(
        java.lang.String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      java.util.Map<java.lang.String, com.google.firestore.v1.Value> map =
          internalGetAggregateFields().getMap();
      if (!map.containsKey(key)) {
        throw new java.lang.IllegalArgumentException();
      }
      return map.get(key);
    }

    public Builder clearAggregateFields() {
      internalGetMutableAggregateFields().getMutableMap()
          .clear();
      return this;
    }
    /**
     * <pre>
     * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
     * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
     * assigned to the aggregation function on input and the size of this map
     * equals the number of aggregation functions in the query.
     * </pre>
     *
     * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
     */

    public Builder removeAggregateFields(
        java.lang.String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      internalGetMutableAggregateFields().getMutableMap()
          .remove(key);
      return this;
    }
    /**
     * Use alternate mutation accessors instead.
     */
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, com.google.firestore.v1.Value>
    getMutableAggregateFields() {
      return internalGetMutableAggregateFields().getMutableMap();
    }
    /**
     * <pre>
     * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
     * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
     * assigned to the aggregation function on input and the size of this map
     * equals the number of aggregation functions in the query.
     * </pre>
     *
     * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
     */
    public Builder putAggregateFields(
        java.lang.String key,
        com.google.firestore.v1.Value value) {
      if (key == null) { throw new NullPointerException("map key"); }
      if (value == null) {
  throw new NullPointerException("map value");
}

      internalGetMutableAggregateFields().getMutableMap()
          .put(key, value);
      return this;
    }
    /**
     * <pre>
     * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
     * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
     * assigned to the aggregation function on input and the size of this map
     * equals the number of aggregation functions in the query.
     * </pre>
     *
     * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
     */

    public Builder putAllAggregateFields(
        java.util.Map<java.lang.String, com.google.firestore.v1.Value> values) {
      internalGetMutableAggregateFields().getMutableMap()
          .putAll(values);
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:google.firestore.v1.AggregationResult)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.v1.AggregationResult)
  private static final com.google.firestore.v1.AggregationResult DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.google.firestore.v1.AggregationResult();
  }

  public static com.google.firestore.v1.AggregationResult getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<AggregationResult>
      PARSER = new com.google.protobuf.AbstractParser<AggregationResult>() {
    @java.lang.Override
    public AggregationResult parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new AggregationResult(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<AggregationResult> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<AggregationResult> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.firestore.v1.AggregationResult getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

