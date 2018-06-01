// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/firestore/v1beta1/firestore.proto

package com.google.firestore.v1beta1;

/**
 * <pre>
 * The response for [Firestore.ListDocuments][google.firestore.v1beta1.Firestore.ListDocuments].
 * </pre>
 *
 * Protobuf type {@code google.firestore.v1beta1.ListDocumentsResponse}
 */
public  final class ListDocumentsResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:google.firestore.v1beta1.ListDocumentsResponse)
    ListDocumentsResponseOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ListDocumentsResponse.newBuilder() to construct.
  private ListDocumentsResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ListDocumentsResponse() {
    documents_ = java.util.Collections.emptyList();
    nextPageToken_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ListDocumentsResponse(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
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
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
              documents_ = new java.util.ArrayList<com.google.firestore.v1beta1.Document>();
              mutable_bitField0_ |= 0x00000001;
            }
            documents_.add(
                input.readMessage(com.google.firestore.v1beta1.Document.parser(), extensionRegistry));
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            nextPageToken_ = s;
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
        documents_ = java.util.Collections.unmodifiableList(documents_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.google.firestore.v1beta1.FirestoreProto.internal_static_google_firestore_v1beta1_ListDocumentsResponse_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.v1beta1.FirestoreProto.internal_static_google_firestore_v1beta1_ListDocumentsResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.v1beta1.ListDocumentsResponse.class, com.google.firestore.v1beta1.ListDocumentsResponse.Builder.class);
  }

  private int bitField0_;
  public static final int DOCUMENTS_FIELD_NUMBER = 1;
  private java.util.List<com.google.firestore.v1beta1.Document> documents_;
  /**
   * <pre>
   * The Documents found.
   * </pre>
   *
   * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
   */
  public java.util.List<com.google.firestore.v1beta1.Document> getDocumentsList() {
    return documents_;
  }
  /**
   * <pre>
   * The Documents found.
   * </pre>
   *
   * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
   */
  public java.util.List<? extends com.google.firestore.v1beta1.DocumentOrBuilder> 
      getDocumentsOrBuilderList() {
    return documents_;
  }
  /**
   * <pre>
   * The Documents found.
   * </pre>
   *
   * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
   */
  public int getDocumentsCount() {
    return documents_.size();
  }
  /**
   * <pre>
   * The Documents found.
   * </pre>
   *
   * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
   */
  public com.google.firestore.v1beta1.Document getDocuments(int index) {
    return documents_.get(index);
  }
  /**
   * <pre>
   * The Documents found.
   * </pre>
   *
   * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
   */
  public com.google.firestore.v1beta1.DocumentOrBuilder getDocumentsOrBuilder(
      int index) {
    return documents_.get(index);
  }

  public static final int NEXT_PAGE_TOKEN_FIELD_NUMBER = 2;
  private volatile java.lang.Object nextPageToken_;
  /**
   * <pre>
   * The next page token.
   * </pre>
   *
   * <code>string next_page_token = 2;</code>
   */
  public java.lang.String getNextPageToken() {
    java.lang.Object ref = nextPageToken_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      nextPageToken_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * The next page token.
   * </pre>
   *
   * <code>string next_page_token = 2;</code>
   */
  public com.google.protobuf.ByteString
      getNextPageTokenBytes() {
    java.lang.Object ref = nextPageToken_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      nextPageToken_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    for (int i = 0; i < documents_.size(); i++) {
      output.writeMessage(1, documents_.get(i));
    }
    if (!getNextPageTokenBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, nextPageToken_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < documents_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, documents_.get(i));
    }
    if (!getNextPageTokenBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, nextPageToken_);
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
    if (!(obj instanceof com.google.firestore.v1beta1.ListDocumentsResponse)) {
      return super.equals(obj);
    }
    com.google.firestore.v1beta1.ListDocumentsResponse other = (com.google.firestore.v1beta1.ListDocumentsResponse) obj;

    boolean result = true;
    result = result && getDocumentsList()
        .equals(other.getDocumentsList());
    result = result && getNextPageToken()
        .equals(other.getNextPageToken());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getDocumentsCount() > 0) {
      hash = (37 * hash) + DOCUMENTS_FIELD_NUMBER;
      hash = (53 * hash) + getDocumentsList().hashCode();
    }
    hash = (37 * hash) + NEXT_PAGE_TOKEN_FIELD_NUMBER;
    hash = (53 * hash) + getNextPageToken().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.v1beta1.ListDocumentsResponse parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.google.firestore.v1beta1.ListDocumentsResponse parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.google.firestore.v1beta1.ListDocumentsResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.google.firestore.v1beta1.ListDocumentsResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.google.firestore.v1beta1.ListDocumentsResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.google.firestore.v1beta1.ListDocumentsResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.google.firestore.v1beta1.ListDocumentsResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.google.firestore.v1beta1.ListDocumentsResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.google.firestore.v1beta1.ListDocumentsResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.google.firestore.v1beta1.ListDocumentsResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.google.firestore.v1beta1.ListDocumentsResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.google.firestore.v1beta1.ListDocumentsResponse parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.google.firestore.v1beta1.ListDocumentsResponse prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
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
   * The response for [Firestore.ListDocuments][google.firestore.v1beta1.Firestore.ListDocuments].
   * </pre>
   *
   * Protobuf type {@code google.firestore.v1beta1.ListDocumentsResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:google.firestore.v1beta1.ListDocumentsResponse)
      com.google.firestore.v1beta1.ListDocumentsResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.firestore.v1beta1.FirestoreProto.internal_static_google_firestore_v1beta1_ListDocumentsResponse_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.v1beta1.FirestoreProto.internal_static_google_firestore_v1beta1_ListDocumentsResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.v1beta1.ListDocumentsResponse.class, com.google.firestore.v1beta1.ListDocumentsResponse.Builder.class);
    }

    // Construct using com.google.firestore.v1beta1.ListDocumentsResponse.newBuilder()
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
        getDocumentsFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      if (documentsBuilder_ == null) {
        documents_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        documentsBuilder_.clear();
      }
      nextPageToken_ = "";

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.google.firestore.v1beta1.FirestoreProto.internal_static_google_firestore_v1beta1_ListDocumentsResponse_descriptor;
    }

    public com.google.firestore.v1beta1.ListDocumentsResponse getDefaultInstanceForType() {
      return com.google.firestore.v1beta1.ListDocumentsResponse.getDefaultInstance();
    }

    public com.google.firestore.v1beta1.ListDocumentsResponse build() {
      com.google.firestore.v1beta1.ListDocumentsResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.google.firestore.v1beta1.ListDocumentsResponse buildPartial() {
      com.google.firestore.v1beta1.ListDocumentsResponse result = new com.google.firestore.v1beta1.ListDocumentsResponse(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (documentsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          documents_ = java.util.Collections.unmodifiableList(documents_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.documents_ = documents_;
      } else {
        result.documents_ = documentsBuilder_.build();
      }
      result.nextPageToken_ = nextPageToken_;
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.google.firestore.v1beta1.ListDocumentsResponse) {
        return mergeFrom((com.google.firestore.v1beta1.ListDocumentsResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.v1beta1.ListDocumentsResponse other) {
      if (other == com.google.firestore.v1beta1.ListDocumentsResponse.getDefaultInstance()) return this;
      if (documentsBuilder_ == null) {
        if (!other.documents_.isEmpty()) {
          if (documents_.isEmpty()) {
            documents_ = other.documents_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureDocumentsIsMutable();
            documents_.addAll(other.documents_);
          }
          onChanged();
        }
      } else {
        if (!other.documents_.isEmpty()) {
          if (documentsBuilder_.isEmpty()) {
            documentsBuilder_.dispose();
            documentsBuilder_ = null;
            documents_ = other.documents_;
            bitField0_ = (bitField0_ & ~0x00000001);
            documentsBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getDocumentsFieldBuilder() : null;
          } else {
            documentsBuilder_.addAllMessages(other.documents_);
          }
        }
      }
      if (!other.getNextPageToken().isEmpty()) {
        nextPageToken_ = other.nextPageToken_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.google.firestore.v1beta1.ListDocumentsResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.google.firestore.v1beta1.ListDocumentsResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<com.google.firestore.v1beta1.Document> documents_ =
      java.util.Collections.emptyList();
    private void ensureDocumentsIsMutable() {
      if (!((bitField0_ & 0x00000001) == 0x00000001)) {
        documents_ = new java.util.ArrayList<com.google.firestore.v1beta1.Document>(documents_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.google.firestore.v1beta1.Document, com.google.firestore.v1beta1.Document.Builder, com.google.firestore.v1beta1.DocumentOrBuilder> documentsBuilder_;

    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public java.util.List<com.google.firestore.v1beta1.Document> getDocumentsList() {
      if (documentsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(documents_);
      } else {
        return documentsBuilder_.getMessageList();
      }
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public int getDocumentsCount() {
      if (documentsBuilder_ == null) {
        return documents_.size();
      } else {
        return documentsBuilder_.getCount();
      }
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public com.google.firestore.v1beta1.Document getDocuments(int index) {
      if (documentsBuilder_ == null) {
        return documents_.get(index);
      } else {
        return documentsBuilder_.getMessage(index);
      }
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public Builder setDocuments(
        int index, com.google.firestore.v1beta1.Document value) {
      if (documentsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureDocumentsIsMutable();
        documents_.set(index, value);
        onChanged();
      } else {
        documentsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public Builder setDocuments(
        int index, com.google.firestore.v1beta1.Document.Builder builderForValue) {
      if (documentsBuilder_ == null) {
        ensureDocumentsIsMutable();
        documents_.set(index, builderForValue.build());
        onChanged();
      } else {
        documentsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public Builder addDocuments(com.google.firestore.v1beta1.Document value) {
      if (documentsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureDocumentsIsMutable();
        documents_.add(value);
        onChanged();
      } else {
        documentsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public Builder addDocuments(
        int index, com.google.firestore.v1beta1.Document value) {
      if (documentsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureDocumentsIsMutable();
        documents_.add(index, value);
        onChanged();
      } else {
        documentsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public Builder addDocuments(
        com.google.firestore.v1beta1.Document.Builder builderForValue) {
      if (documentsBuilder_ == null) {
        ensureDocumentsIsMutable();
        documents_.add(builderForValue.build());
        onChanged();
      } else {
        documentsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public Builder addDocuments(
        int index, com.google.firestore.v1beta1.Document.Builder builderForValue) {
      if (documentsBuilder_ == null) {
        ensureDocumentsIsMutable();
        documents_.add(index, builderForValue.build());
        onChanged();
      } else {
        documentsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public Builder addAllDocuments(
        java.lang.Iterable<? extends com.google.firestore.v1beta1.Document> values) {
      if (documentsBuilder_ == null) {
        ensureDocumentsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, documents_);
        onChanged();
      } else {
        documentsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public Builder clearDocuments() {
      if (documentsBuilder_ == null) {
        documents_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        documentsBuilder_.clear();
      }
      return this;
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public Builder removeDocuments(int index) {
      if (documentsBuilder_ == null) {
        ensureDocumentsIsMutable();
        documents_.remove(index);
        onChanged();
      } else {
        documentsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public com.google.firestore.v1beta1.Document.Builder getDocumentsBuilder(
        int index) {
      return getDocumentsFieldBuilder().getBuilder(index);
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public com.google.firestore.v1beta1.DocumentOrBuilder getDocumentsOrBuilder(
        int index) {
      if (documentsBuilder_ == null) {
        return documents_.get(index);  } else {
        return documentsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public java.util.List<? extends com.google.firestore.v1beta1.DocumentOrBuilder> 
         getDocumentsOrBuilderList() {
      if (documentsBuilder_ != null) {
        return documentsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(documents_);
      }
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public com.google.firestore.v1beta1.Document.Builder addDocumentsBuilder() {
      return getDocumentsFieldBuilder().addBuilder(
          com.google.firestore.v1beta1.Document.getDefaultInstance());
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public com.google.firestore.v1beta1.Document.Builder addDocumentsBuilder(
        int index) {
      return getDocumentsFieldBuilder().addBuilder(
          index, com.google.firestore.v1beta1.Document.getDefaultInstance());
    }
    /**
     * <pre>
     * The Documents found.
     * </pre>
     *
     * <code>repeated .google.firestore.v1beta1.Document documents = 1;</code>
     */
    public java.util.List<com.google.firestore.v1beta1.Document.Builder> 
         getDocumentsBuilderList() {
      return getDocumentsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.google.firestore.v1beta1.Document, com.google.firestore.v1beta1.Document.Builder, com.google.firestore.v1beta1.DocumentOrBuilder> 
        getDocumentsFieldBuilder() {
      if (documentsBuilder_ == null) {
        documentsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            com.google.firestore.v1beta1.Document, com.google.firestore.v1beta1.Document.Builder, com.google.firestore.v1beta1.DocumentOrBuilder>(
                documents_,
                ((bitField0_ & 0x00000001) == 0x00000001),
                getParentForChildren(),
                isClean());
        documents_ = null;
      }
      return documentsBuilder_;
    }

    private java.lang.Object nextPageToken_ = "";
    /**
     * <pre>
     * The next page token.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     */
    public java.lang.String getNextPageToken() {
      java.lang.Object ref = nextPageToken_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        nextPageToken_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     * The next page token.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     */
    public com.google.protobuf.ByteString
        getNextPageTokenBytes() {
      java.lang.Object ref = nextPageToken_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        nextPageToken_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * The next page token.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     */
    public Builder setNextPageToken(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      nextPageToken_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * The next page token.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     */
    public Builder clearNextPageToken() {
      
      nextPageToken_ = getDefaultInstance().getNextPageToken();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * The next page token.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     */
    public Builder setNextPageTokenBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      nextPageToken_ = value;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:google.firestore.v1beta1.ListDocumentsResponse)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.v1beta1.ListDocumentsResponse)
  private static final com.google.firestore.v1beta1.ListDocumentsResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.google.firestore.v1beta1.ListDocumentsResponse();
  }

  public static com.google.firestore.v1beta1.ListDocumentsResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ListDocumentsResponse>
      PARSER = new com.google.protobuf.AbstractParser<ListDocumentsResponse>() {
    public ListDocumentsResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new ListDocumentsResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ListDocumentsResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ListDocumentsResponse> getParserForType() {
    return PARSER;
  }

  public com.google.firestore.v1beta1.ListDocumentsResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

