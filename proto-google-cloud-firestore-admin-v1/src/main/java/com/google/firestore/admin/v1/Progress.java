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
// source: google/firestore/admin/v1/operation.proto

// Protobuf Java Version: 3.25.3
package com.google.firestore.admin.v1;

/**
 *
 *
 * <pre>
 * Describes the progress of the operation.
 * Unit of work is generic and must be interpreted based on where
 * [Progress][google.firestore.admin.v1.Progress] is used.
 * </pre>
 *
 * Protobuf type {@code google.firestore.admin.v1.Progress}
 */
public final class Progress extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.firestore.admin.v1.Progress)
    ProgressOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use Progress.newBuilder() to construct.
  private Progress(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private Progress() {}

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new Progress();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.firestore.admin.v1.OperationProto
        .internal_static_google_firestore_admin_v1_Progress_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.admin.v1.OperationProto
        .internal_static_google_firestore_admin_v1_Progress_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.admin.v1.Progress.class,
            com.google.firestore.admin.v1.Progress.Builder.class);
  }

  public static final int ESTIMATED_WORK_FIELD_NUMBER = 1;
  private long estimatedWork_ = 0L;
  /**
   *
   *
   * <pre>
   * The amount of work estimated.
   * </pre>
   *
   * <code>int64 estimated_work = 1;</code>
   *
   * @return The estimatedWork.
   */
  @java.lang.Override
  public long getEstimatedWork() {
    return estimatedWork_;
  }

  public static final int COMPLETED_WORK_FIELD_NUMBER = 2;
  private long completedWork_ = 0L;
  /**
   *
   *
   * <pre>
   * The amount of work completed.
   * </pre>
   *
   * <code>int64 completed_work = 2;</code>
   *
   * @return The completedWork.
   */
  @java.lang.Override
  public long getCompletedWork() {
    return completedWork_;
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
    if (estimatedWork_ != 0L) {
      output.writeInt64(1, estimatedWork_);
    }
    if (completedWork_ != 0L) {
      output.writeInt64(2, completedWork_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (estimatedWork_ != 0L) {
      size += com.google.protobuf.CodedOutputStream.computeInt64Size(1, estimatedWork_);
    }
    if (completedWork_ != 0L) {
      size += com.google.protobuf.CodedOutputStream.computeInt64Size(2, completedWork_);
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
    if (!(obj instanceof com.google.firestore.admin.v1.Progress)) {
      return super.equals(obj);
    }
    com.google.firestore.admin.v1.Progress other = (com.google.firestore.admin.v1.Progress) obj;

    if (getEstimatedWork() != other.getEstimatedWork()) return false;
    if (getCompletedWork() != other.getCompletedWork()) return false;
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
    hash = (37 * hash) + ESTIMATED_WORK_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(getEstimatedWork());
    hash = (37 * hash) + COMPLETED_WORK_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(getCompletedWork());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.admin.v1.Progress parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.Progress parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.Progress parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.Progress parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.Progress parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.Progress parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.Progress parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.Progress parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.Progress parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.Progress parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.Progress parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.Progress parseFrom(
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

  public static Builder newBuilder(com.google.firestore.admin.v1.Progress prototype) {
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
   * Describes the progress of the operation.
   * Unit of work is generic and must be interpreted based on where
   * [Progress][google.firestore.admin.v1.Progress] is used.
   * </pre>
   *
   * Protobuf type {@code google.firestore.admin.v1.Progress}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.firestore.admin.v1.Progress)
      com.google.firestore.admin.v1.ProgressOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.firestore.admin.v1.OperationProto
          .internal_static_google_firestore_admin_v1_Progress_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.admin.v1.OperationProto
          .internal_static_google_firestore_admin_v1_Progress_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.admin.v1.Progress.class,
              com.google.firestore.admin.v1.Progress.Builder.class);
    }

    // Construct using com.google.firestore.admin.v1.Progress.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      estimatedWork_ = 0L;
      completedWork_ = 0L;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.firestore.admin.v1.OperationProto
          .internal_static_google_firestore_admin_v1_Progress_descriptor;
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.Progress getDefaultInstanceForType() {
      return com.google.firestore.admin.v1.Progress.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.Progress build() {
      com.google.firestore.admin.v1.Progress result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.Progress buildPartial() {
      com.google.firestore.admin.v1.Progress result =
          new com.google.firestore.admin.v1.Progress(this);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.google.firestore.admin.v1.Progress result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.estimatedWork_ = estimatedWork_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.completedWork_ = completedWork_;
      }
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
      if (other instanceof com.google.firestore.admin.v1.Progress) {
        return mergeFrom((com.google.firestore.admin.v1.Progress) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.admin.v1.Progress other) {
      if (other == com.google.firestore.admin.v1.Progress.getDefaultInstance()) return this;
      if (other.getEstimatedWork() != 0L) {
        setEstimatedWork(other.getEstimatedWork());
      }
      if (other.getCompletedWork() != 0L) {
        setCompletedWork(other.getCompletedWork());
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
            case 8:
              {
                estimatedWork_ = input.readInt64();
                bitField0_ |= 0x00000001;
                break;
              } // case 8
            case 16:
              {
                completedWork_ = input.readInt64();
                bitField0_ |= 0x00000002;
                break;
              } // case 16
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

    private long estimatedWork_;
    /**
     *
     *
     * <pre>
     * The amount of work estimated.
     * </pre>
     *
     * <code>int64 estimated_work = 1;</code>
     *
     * @return The estimatedWork.
     */
    @java.lang.Override
    public long getEstimatedWork() {
      return estimatedWork_;
    }
    /**
     *
     *
     * <pre>
     * The amount of work estimated.
     * </pre>
     *
     * <code>int64 estimated_work = 1;</code>
     *
     * @param value The estimatedWork to set.
     * @return This builder for chaining.
     */
    public Builder setEstimatedWork(long value) {

      estimatedWork_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The amount of work estimated.
     * </pre>
     *
     * <code>int64 estimated_work = 1;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearEstimatedWork() {
      bitField0_ = (bitField0_ & ~0x00000001);
      estimatedWork_ = 0L;
      onChanged();
      return this;
    }

    private long completedWork_;
    /**
     *
     *
     * <pre>
     * The amount of work completed.
     * </pre>
     *
     * <code>int64 completed_work = 2;</code>
     *
     * @return The completedWork.
     */
    @java.lang.Override
    public long getCompletedWork() {
      return completedWork_;
    }
    /**
     *
     *
     * <pre>
     * The amount of work completed.
     * </pre>
     *
     * <code>int64 completed_work = 2;</code>
     *
     * @param value The completedWork to set.
     * @return This builder for chaining.
     */
    public Builder setCompletedWork(long value) {

      completedWork_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The amount of work completed.
     * </pre>
     *
     * <code>int64 completed_work = 2;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearCompletedWork() {
      bitField0_ = (bitField0_ & ~0x00000002);
      completedWork_ = 0L;
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

    // @@protoc_insertion_point(builder_scope:google.firestore.admin.v1.Progress)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.admin.v1.Progress)
  private static final com.google.firestore.admin.v1.Progress DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.firestore.admin.v1.Progress();
  }

  public static com.google.firestore.admin.v1.Progress getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Progress> PARSER =
      new com.google.protobuf.AbstractParser<Progress>() {
        @java.lang.Override
        public Progress parsePartialFrom(
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

  public static com.google.protobuf.Parser<Progress> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Progress> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.firestore.admin.v1.Progress getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
