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
// source: google/firestore/v1beta1/firestore.proto

package com.google.firestore.v1beta1;

/**
 *
 *
 * <pre>
 * The response for [Firestore.RunQuery][google.firestore.v1beta1.Firestore.RunQuery].
 * </pre>
 *
 * Protobuf type {@code google.firestore.v1beta1.RunQueryResponse}
 */
public final class RunQueryResponse extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.firestore.v1beta1.RunQueryResponse)
    RunQueryResponseOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use RunQueryResponse.newBuilder() to construct.
  private RunQueryResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private RunQueryResponse() {
    transaction_ = com.google.protobuf.ByteString.EMPTY;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
    return this.unknownFields;
  }

  private RunQueryResponse(
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
              com.google.firestore.v1beta1.Document.Builder subBuilder = null;
              if (document_ != null) {
                subBuilder = document_.toBuilder();
              }
              document_ =
                  input.readMessage(
                      com.google.firestore.v1beta1.Document.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(document_);
                document_ = subBuilder.buildPartial();
              }

              break;
            }
          case 18:
            {
              transaction_ = input.readBytes();
              break;
            }
          case 26:
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
          case 32:
            {
              skippedResults_ = input.readInt32();
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
    return com.google.firestore.v1beta1.FirestoreProto
        .internal_static_google_firestore_v1beta1_RunQueryResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.v1beta1.FirestoreProto
        .internal_static_google_firestore_v1beta1_RunQueryResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.v1beta1.RunQueryResponse.class,
            com.google.firestore.v1beta1.RunQueryResponse.Builder.class);
  }

  public static final int TRANSACTION_FIELD_NUMBER = 2;
  private com.google.protobuf.ByteString transaction_;
  /**
   *
   *
   * <pre>
   * The transaction that was started as part of this request.
   * Can only be set in the first response, and only if
   * [RunQueryRequest.new_transaction][google.firestore.v1beta1.RunQueryRequest.new_transaction] was set in the request.
   * If set, no other fields will be set in this response.
   * </pre>
   *
   * <code>bytes transaction = 2;</code>
   */
  public com.google.protobuf.ByteString getTransaction() {
    return transaction_;
  }

  public static final int DOCUMENT_FIELD_NUMBER = 1;
  private com.google.firestore.v1beta1.Document document_;
  /**
   *
   *
   * <pre>
   * A query result.
   * Not set when reporting partial progress.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.Document document = 1;</code>
   */
  public boolean hasDocument() {
    return document_ != null;
  }
  /**
   *
   *
   * <pre>
   * A query result.
   * Not set when reporting partial progress.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.Document document = 1;</code>
   */
  public com.google.firestore.v1beta1.Document getDocument() {
    return document_ == null
        ? com.google.firestore.v1beta1.Document.getDefaultInstance()
        : document_;
  }
  /**
   *
   *
   * <pre>
   * A query result.
   * Not set when reporting partial progress.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.Document document = 1;</code>
   */
  public com.google.firestore.v1beta1.DocumentOrBuilder getDocumentOrBuilder() {
    return getDocument();
  }

  public static final int READ_TIME_FIELD_NUMBER = 3;
  private com.google.protobuf.Timestamp readTime_;
  /**
   *
   *
   * <pre>
   * The time at which the document was read. This may be monotonically
   * increasing; in this case, the previous documents in the result stream are
   * guaranteed not to have changed between their `read_time` and this one.
   * If the query returns no results, a response with `read_time` and no
   * `document` will be sent, and this represents the time at which the query
   * was run.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 3;</code>
   */
  public boolean hasReadTime() {
    return readTime_ != null;
  }
  /**
   *
   *
   * <pre>
   * The time at which the document was read. This may be monotonically
   * increasing; in this case, the previous documents in the result stream are
   * guaranteed not to have changed between their `read_time` and this one.
   * If the query returns no results, a response with `read_time` and no
   * `document` will be sent, and this represents the time at which the query
   * was run.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 3;</code>
   */
  public com.google.protobuf.Timestamp getReadTime() {
    return readTime_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : readTime_;
  }
  /**
   *
   *
   * <pre>
   * The time at which the document was read. This may be monotonically
   * increasing; in this case, the previous documents in the result stream are
   * guaranteed not to have changed between their `read_time` and this one.
   * If the query returns no results, a response with `read_time` and no
   * `document` will be sent, and this represents the time at which the query
   * was run.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 3;</code>
   */
  public com.google.protobuf.TimestampOrBuilder getReadTimeOrBuilder() {
    return getReadTime();
  }

  public static final int SKIPPED_RESULTS_FIELD_NUMBER = 4;
  private int skippedResults_;
  /**
   *
   *
   * <pre>
   * The number of results that have been skipped due to an offset between
   * the last response and the current response.
   * </pre>
   *
   * <code>int32 skipped_results = 4;</code>
   */
  public int getSkippedResults() {
    return skippedResults_;
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
    if (document_ != null) {
      output.writeMessage(1, getDocument());
    }
    if (!transaction_.isEmpty()) {
      output.writeBytes(2, transaction_);
    }
    if (readTime_ != null) {
      output.writeMessage(3, getReadTime());
    }
    if (skippedResults_ != 0) {
      output.writeInt32(4, skippedResults_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (document_ != null) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, getDocument());
    }
    if (!transaction_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream.computeBytesSize(2, transaction_);
    }
    if (readTime_ != null) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(3, getReadTime());
    }
    if (skippedResults_ != 0) {
      size += com.google.protobuf.CodedOutputStream.computeInt32Size(4, skippedResults_);
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
    if (!(obj instanceof com.google.firestore.v1beta1.RunQueryResponse)) {
      return super.equals(obj);
    }
    com.google.firestore.v1beta1.RunQueryResponse other =
        (com.google.firestore.v1beta1.RunQueryResponse) obj;

    if (!getTransaction().equals(other.getTransaction())) return false;
    if (hasDocument() != other.hasDocument()) return false;
    if (hasDocument()) {
      if (!getDocument().equals(other.getDocument())) return false;
    }
    if (hasReadTime() != other.hasReadTime()) return false;
    if (hasReadTime()) {
      if (!getReadTime().equals(other.getReadTime())) return false;
    }
    if (getSkippedResults() != other.getSkippedResults()) return false;
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
    hash = (37 * hash) + TRANSACTION_FIELD_NUMBER;
    hash = (53 * hash) + getTransaction().hashCode();
    if (hasDocument()) {
      hash = (37 * hash) + DOCUMENT_FIELD_NUMBER;
      hash = (53 * hash) + getDocument().hashCode();
    }
    if (hasReadTime()) {
      hash = (37 * hash) + READ_TIME_FIELD_NUMBER;
      hash = (53 * hash) + getReadTime().hashCode();
    }
    hash = (37 * hash) + SKIPPED_RESULTS_FIELD_NUMBER;
    hash = (53 * hash) + getSkippedResults();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.v1beta1.RunQueryResponse parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.v1beta1.RunQueryResponse parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.v1beta1.RunQueryResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.v1beta1.RunQueryResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.v1beta1.RunQueryResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.v1beta1.RunQueryResponse parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.v1beta1.RunQueryResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.v1beta1.RunQueryResponse parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.v1beta1.RunQueryResponse parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.firestore.v1beta1.RunQueryResponse parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.v1beta1.RunQueryResponse parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.v1beta1.RunQueryResponse parseFrom(
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

  public static Builder newBuilder(com.google.firestore.v1beta1.RunQueryResponse prototype) {
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
   * The response for [Firestore.RunQuery][google.firestore.v1beta1.Firestore.RunQuery].
   * </pre>
   *
   * Protobuf type {@code google.firestore.v1beta1.RunQueryResponse}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.firestore.v1beta1.RunQueryResponse)
      com.google.firestore.v1beta1.RunQueryResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.firestore.v1beta1.FirestoreProto
          .internal_static_google_firestore_v1beta1_RunQueryResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.v1beta1.FirestoreProto
          .internal_static_google_firestore_v1beta1_RunQueryResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.v1beta1.RunQueryResponse.class,
              com.google.firestore.v1beta1.RunQueryResponse.Builder.class);
    }

    // Construct using com.google.firestore.v1beta1.RunQueryResponse.newBuilder()
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
      transaction_ = com.google.protobuf.ByteString.EMPTY;

      if (documentBuilder_ == null) {
        document_ = null;
      } else {
        document_ = null;
        documentBuilder_ = null;
      }
      if (readTimeBuilder_ == null) {
        readTime_ = null;
      } else {
        readTime_ = null;
        readTimeBuilder_ = null;
      }
      skippedResults_ = 0;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.firestore.v1beta1.FirestoreProto
          .internal_static_google_firestore_v1beta1_RunQueryResponse_descriptor;
    }

    @java.lang.Override
    public com.google.firestore.v1beta1.RunQueryResponse getDefaultInstanceForType() {
      return com.google.firestore.v1beta1.RunQueryResponse.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.firestore.v1beta1.RunQueryResponse build() {
      com.google.firestore.v1beta1.RunQueryResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.firestore.v1beta1.RunQueryResponse buildPartial() {
      com.google.firestore.v1beta1.RunQueryResponse result =
          new com.google.firestore.v1beta1.RunQueryResponse(this);
      result.transaction_ = transaction_;
      if (documentBuilder_ == null) {
        result.document_ = document_;
      } else {
        result.document_ = documentBuilder_.build();
      }
      if (readTimeBuilder_ == null) {
        result.readTime_ = readTime_;
      } else {
        result.readTime_ = readTimeBuilder_.build();
      }
      result.skippedResults_ = skippedResults_;
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
      if (other instanceof com.google.firestore.v1beta1.RunQueryResponse) {
        return mergeFrom((com.google.firestore.v1beta1.RunQueryResponse) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.v1beta1.RunQueryResponse other) {
      if (other == com.google.firestore.v1beta1.RunQueryResponse.getDefaultInstance()) return this;
      if (other.getTransaction() != com.google.protobuf.ByteString.EMPTY) {
        setTransaction(other.getTransaction());
      }
      if (other.hasDocument()) {
        mergeDocument(other.getDocument());
      }
      if (other.hasReadTime()) {
        mergeReadTime(other.getReadTime());
      }
      if (other.getSkippedResults() != 0) {
        setSkippedResults(other.getSkippedResults());
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
      com.google.firestore.v1beta1.RunQueryResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.google.firestore.v1beta1.RunQueryResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.google.protobuf.ByteString transaction_ = com.google.protobuf.ByteString.EMPTY;
    /**
     *
     *
     * <pre>
     * The transaction that was started as part of this request.
     * Can only be set in the first response, and only if
     * [RunQueryRequest.new_transaction][google.firestore.v1beta1.RunQueryRequest.new_transaction] was set in the request.
     * If set, no other fields will be set in this response.
     * </pre>
     *
     * <code>bytes transaction = 2;</code>
     */
    public com.google.protobuf.ByteString getTransaction() {
      return transaction_;
    }
    /**
     *
     *
     * <pre>
     * The transaction that was started as part of this request.
     * Can only be set in the first response, and only if
     * [RunQueryRequest.new_transaction][google.firestore.v1beta1.RunQueryRequest.new_transaction] was set in the request.
     * If set, no other fields will be set in this response.
     * </pre>
     *
     * <code>bytes transaction = 2;</code>
     */
    public Builder setTransaction(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }

      transaction_ = value;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The transaction that was started as part of this request.
     * Can only be set in the first response, and only if
     * [RunQueryRequest.new_transaction][google.firestore.v1beta1.RunQueryRequest.new_transaction] was set in the request.
     * If set, no other fields will be set in this response.
     * </pre>
     *
     * <code>bytes transaction = 2;</code>
     */
    public Builder clearTransaction() {

      transaction_ = getDefaultInstance().getTransaction();
      onChanged();
      return this;
    }

    private com.google.firestore.v1beta1.Document document_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.firestore.v1beta1.Document,
            com.google.firestore.v1beta1.Document.Builder,
            com.google.firestore.v1beta1.DocumentOrBuilder>
        documentBuilder_;
    /**
     *
     *
     * <pre>
     * A query result.
     * Not set when reporting partial progress.
     * </pre>
     *
     * <code>.google.firestore.v1beta1.Document document = 1;</code>
     */
    public boolean hasDocument() {
      return documentBuilder_ != null || document_ != null;
    }
    /**
     *
     *
     * <pre>
     * A query result.
     * Not set when reporting partial progress.
     * </pre>
     *
     * <code>.google.firestore.v1beta1.Document document = 1;</code>
     */
    public com.google.firestore.v1beta1.Document getDocument() {
      if (documentBuilder_ == null) {
        return document_ == null
            ? com.google.firestore.v1beta1.Document.getDefaultInstance()
            : document_;
      } else {
        return documentBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * A query result.
     * Not set when reporting partial progress.
     * </pre>
     *
     * <code>.google.firestore.v1beta1.Document document = 1;</code>
     */
    public Builder setDocument(com.google.firestore.v1beta1.Document value) {
      if (documentBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        document_ = value;
        onChanged();
      } else {
        documentBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * A query result.
     * Not set when reporting partial progress.
     * </pre>
     *
     * <code>.google.firestore.v1beta1.Document document = 1;</code>
     */
    public Builder setDocument(com.google.firestore.v1beta1.Document.Builder builderForValue) {
      if (documentBuilder_ == null) {
        document_ = builderForValue.build();
        onChanged();
      } else {
        documentBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * A query result.
     * Not set when reporting partial progress.
     * </pre>
     *
     * <code>.google.firestore.v1beta1.Document document = 1;</code>
     */
    public Builder mergeDocument(com.google.firestore.v1beta1.Document value) {
      if (documentBuilder_ == null) {
        if (document_ != null) {
          document_ =
              com.google.firestore.v1beta1.Document.newBuilder(document_)
                  .mergeFrom(value)
                  .buildPartial();
        } else {
          document_ = value;
        }
        onChanged();
      } else {
        documentBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * A query result.
     * Not set when reporting partial progress.
     * </pre>
     *
     * <code>.google.firestore.v1beta1.Document document = 1;</code>
     */
    public Builder clearDocument() {
      if (documentBuilder_ == null) {
        document_ = null;
        onChanged();
      } else {
        document_ = null;
        documentBuilder_ = null;
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * A query result.
     * Not set when reporting partial progress.
     * </pre>
     *
     * <code>.google.firestore.v1beta1.Document document = 1;</code>
     */
    public com.google.firestore.v1beta1.Document.Builder getDocumentBuilder() {

      onChanged();
      return getDocumentFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * A query result.
     * Not set when reporting partial progress.
     * </pre>
     *
     * <code>.google.firestore.v1beta1.Document document = 1;</code>
     */
    public com.google.firestore.v1beta1.DocumentOrBuilder getDocumentOrBuilder() {
      if (documentBuilder_ != null) {
        return documentBuilder_.getMessageOrBuilder();
      } else {
        return document_ == null
            ? com.google.firestore.v1beta1.Document.getDefaultInstance()
            : document_;
      }
    }
    /**
     *
     *
     * <pre>
     * A query result.
     * Not set when reporting partial progress.
     * </pre>
     *
     * <code>.google.firestore.v1beta1.Document document = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.firestore.v1beta1.Document,
            com.google.firestore.v1beta1.Document.Builder,
            com.google.firestore.v1beta1.DocumentOrBuilder>
        getDocumentFieldBuilder() {
      if (documentBuilder_ == null) {
        documentBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.firestore.v1beta1.Document,
                com.google.firestore.v1beta1.Document.Builder,
                com.google.firestore.v1beta1.DocumentOrBuilder>(
                getDocument(), getParentForChildren(), isClean());
        document_ = null;
      }
      return documentBuilder_;
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
     * The time at which the document was read. This may be monotonically
     * increasing; in this case, the previous documents in the result stream are
     * guaranteed not to have changed between their `read_time` and this one.
     * If the query returns no results, a response with `read_time` and no
     * `document` will be sent, and this represents the time at which the query
     * was run.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
     */
    public boolean hasReadTime() {
      return readTimeBuilder_ != null || readTime_ != null;
    }
    /**
     *
     *
     * <pre>
     * The time at which the document was read. This may be monotonically
     * increasing; in this case, the previous documents in the result stream are
     * guaranteed not to have changed between their `read_time` and this one.
     * If the query returns no results, a response with `read_time` and no
     * `document` will be sent, and this represents the time at which the query
     * was run.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
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
     * The time at which the document was read. This may be monotonically
     * increasing; in this case, the previous documents in the result stream are
     * guaranteed not to have changed between their `read_time` and this one.
     * If the query returns no results, a response with `read_time` and no
     * `document` will be sent, and this represents the time at which the query
     * was run.
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
     * The time at which the document was read. This may be monotonically
     * increasing; in this case, the previous documents in the result stream are
     * guaranteed not to have changed between their `read_time` and this one.
     * If the query returns no results, a response with `read_time` and no
     * `document` will be sent, and this represents the time at which the query
     * was run.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
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
     * The time at which the document was read. This may be monotonically
     * increasing; in this case, the previous documents in the result stream are
     * guaranteed not to have changed between their `read_time` and this one.
     * If the query returns no results, a response with `read_time` and no
     * `document` will be sent, and this represents the time at which the query
     * was run.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
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
     * The time at which the document was read. This may be monotonically
     * increasing; in this case, the previous documents in the result stream are
     * guaranteed not to have changed between their `read_time` and this one.
     * If the query returns no results, a response with `read_time` and no
     * `document` will be sent, and this represents the time at which the query
     * was run.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
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
     * The time at which the document was read. This may be monotonically
     * increasing; in this case, the previous documents in the result stream are
     * guaranteed not to have changed between their `read_time` and this one.
     * If the query returns no results, a response with `read_time` and no
     * `document` will be sent, and this represents the time at which the query
     * was run.
     * </pre>
     *
     * <code>.google.protobuf.Timestamp read_time = 3;</code>
     */
    public com.google.protobuf.Timestamp.Builder getReadTimeBuilder() {

      onChanged();
      return getReadTimeFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * The time at which the document was read. This may be monotonically
     * increasing; in this case, the previous documents in the result stream are
     * guaranteed not to have changed between their `read_time` and this one.
     * If the query returns no results, a response with `read_time` and no
     * `document` will be sent, and this represents the time at which the query
     * was run.
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
     * The time at which the document was read. This may be monotonically
     * increasing; in this case, the previous documents in the result stream are
     * guaranteed not to have changed between their `read_time` and this one.
     * If the query returns no results, a response with `read_time` and no
     * `document` will be sent, and this represents the time at which the query
     * was run.
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

    private int skippedResults_;
    /**
     *
     *
     * <pre>
     * The number of results that have been skipped due to an offset between
     * the last response and the current response.
     * </pre>
     *
     * <code>int32 skipped_results = 4;</code>
     */
    public int getSkippedResults() {
      return skippedResults_;
    }
    /**
     *
     *
     * <pre>
     * The number of results that have been skipped due to an offset between
     * the last response and the current response.
     * </pre>
     *
     * <code>int32 skipped_results = 4;</code>
     */
    public Builder setSkippedResults(int value) {

      skippedResults_ = value;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The number of results that have been skipped due to an offset between
     * the last response and the current response.
     * </pre>
     *
     * <code>int32 skipped_results = 4;</code>
     */
    public Builder clearSkippedResults() {

      skippedResults_ = 0;
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

    // @@protoc_insertion_point(builder_scope:google.firestore.v1beta1.RunQueryResponse)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.v1beta1.RunQueryResponse)
  private static final com.google.firestore.v1beta1.RunQueryResponse DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.firestore.v1beta1.RunQueryResponse();
  }

  public static com.google.firestore.v1beta1.RunQueryResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RunQueryResponse> PARSER =
      new com.google.protobuf.AbstractParser<RunQueryResponse>() {
        @java.lang.Override
        public RunQueryResponse parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new RunQueryResponse(input, extensionRegistry);
        }
      };

  public static com.google.protobuf.Parser<RunQueryResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RunQueryResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.firestore.v1beta1.RunQueryResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
