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
// source: google/firestore/admin/v1/operation.proto

package com.google.firestore.admin.v1;

/**
 *
 *
 * <pre>
 * Returned in the [google.longrunning.Operation][google.longrunning.Operation] response field.
 * </pre>
 *
 * Protobuf type {@code google.firestore.admin.v1.ExportDocumentsResponse}
 */
public final class ExportDocumentsResponse extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.firestore.admin.v1.ExportDocumentsResponse)
    ExportDocumentsResponseOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use ExportDocumentsResponse.newBuilder() to construct.
  private ExportDocumentsResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private ExportDocumentsResponse() {
    outputUriPrefix_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new ExportDocumentsResponse();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
    return this.unknownFields;
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.firestore.admin.v1.OperationProto
        .internal_static_google_firestore_admin_v1_ExportDocumentsResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.admin.v1.OperationProto
        .internal_static_google_firestore_admin_v1_ExportDocumentsResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.admin.v1.ExportDocumentsResponse.class,
            com.google.firestore.admin.v1.ExportDocumentsResponse.Builder.class);
  }

  public static final int OUTPUT_URI_PREFIX_FIELD_NUMBER = 1;
  private volatile java.lang.Object outputUriPrefix_;
  /**
   *
   *
   * <pre>
   * Location of the output files. This can be used to begin an import
   * into Cloud Firestore (this project or another project) after the operation
   * completes successfully.
   * </pre>
   *
   * <code>string output_uri_prefix = 1;</code>
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
   * Location of the output files. This can be used to begin an import
   * into Cloud Firestore (this project or another project) after the operation
   * completes successfully.
   * </pre>
   *
   * <code>string output_uri_prefix = 1;</code>
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(outputUriPrefix_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, outputUriPrefix_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(outputUriPrefix_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, outputUriPrefix_);
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
    if (!(obj instanceof com.google.firestore.admin.v1.ExportDocumentsResponse)) {
      return super.equals(obj);
    }
    com.google.firestore.admin.v1.ExportDocumentsResponse other =
        (com.google.firestore.admin.v1.ExportDocumentsResponse) obj;

    if (!getOutputUriPrefix().equals(other.getOutputUriPrefix())) return false;
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
    hash = (37 * hash) + OUTPUT_URI_PREFIX_FIELD_NUMBER;
    hash = (53 * hash) + getOutputUriPrefix().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse parseFrom(
      java.nio.ByteBuffer data) throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse parseFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse parseFrom(
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

  public static Builder newBuilder(
      com.google.firestore.admin.v1.ExportDocumentsResponse prototype) {
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
   * Returned in the [google.longrunning.Operation][google.longrunning.Operation] response field.
   * </pre>
   *
   * Protobuf type {@code google.firestore.admin.v1.ExportDocumentsResponse}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.firestore.admin.v1.ExportDocumentsResponse)
      com.google.firestore.admin.v1.ExportDocumentsResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.firestore.admin.v1.OperationProto
          .internal_static_google_firestore_admin_v1_ExportDocumentsResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.admin.v1.OperationProto
          .internal_static_google_firestore_admin_v1_ExportDocumentsResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.admin.v1.ExportDocumentsResponse.class,
              com.google.firestore.admin.v1.ExportDocumentsResponse.Builder.class);
    }

    // Construct using com.google.firestore.admin.v1.ExportDocumentsResponse.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      outputUriPrefix_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.firestore.admin.v1.OperationProto
          .internal_static_google_firestore_admin_v1_ExportDocumentsResponse_descriptor;
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.ExportDocumentsResponse getDefaultInstanceForType() {
      return com.google.firestore.admin.v1.ExportDocumentsResponse.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.ExportDocumentsResponse build() {
      com.google.firestore.admin.v1.ExportDocumentsResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.ExportDocumentsResponse buildPartial() {
      com.google.firestore.admin.v1.ExportDocumentsResponse result =
          new com.google.firestore.admin.v1.ExportDocumentsResponse(this);
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
      if (other instanceof com.google.firestore.admin.v1.ExportDocumentsResponse) {
        return mergeFrom((com.google.firestore.admin.v1.ExportDocumentsResponse) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.admin.v1.ExportDocumentsResponse other) {
      if (other == com.google.firestore.admin.v1.ExportDocumentsResponse.getDefaultInstance())
        return this;
      if (!other.getOutputUriPrefix().isEmpty()) {
        outputUriPrefix_ = other.outputUriPrefix_;
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
                outputUriPrefix_ = input.readStringRequireUtf8();

                break;
              } // case 10
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

    private java.lang.Object outputUriPrefix_ = "";
    /**
     *
     *
     * <pre>
     * Location of the output files. This can be used to begin an import
     * into Cloud Firestore (this project or another project) after the operation
     * completes successfully.
     * </pre>
     *
     * <code>string output_uri_prefix = 1;</code>
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
     * Location of the output files. This can be used to begin an import
     * into Cloud Firestore (this project or another project) after the operation
     * completes successfully.
     * </pre>
     *
     * <code>string output_uri_prefix = 1;</code>
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
     * Location of the output files. This can be used to begin an import
     * into Cloud Firestore (this project or another project) after the operation
     * completes successfully.
     * </pre>
     *
     * <code>string output_uri_prefix = 1;</code>
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
     * Location of the output files. This can be used to begin an import
     * into Cloud Firestore (this project or another project) after the operation
     * completes successfully.
     * </pre>
     *
     * <code>string output_uri_prefix = 1;</code>
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
     * Location of the output files. This can be used to begin an import
     * into Cloud Firestore (this project or another project) after the operation
     * completes successfully.
     * </pre>
     *
     * <code>string output_uri_prefix = 1;</code>
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

    // @@protoc_insertion_point(builder_scope:google.firestore.admin.v1.ExportDocumentsResponse)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.admin.v1.ExportDocumentsResponse)
  private static final com.google.firestore.admin.v1.ExportDocumentsResponse DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.firestore.admin.v1.ExportDocumentsResponse();
  }

  public static com.google.firestore.admin.v1.ExportDocumentsResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ExportDocumentsResponse> PARSER =
      new com.google.protobuf.AbstractParser<ExportDocumentsResponse>() {
        @java.lang.Override
        public ExportDocumentsResponse parsePartialFrom(
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

  public static com.google.protobuf.Parser<ExportDocumentsResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ExportDocumentsResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.firestore.admin.v1.ExportDocumentsResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
