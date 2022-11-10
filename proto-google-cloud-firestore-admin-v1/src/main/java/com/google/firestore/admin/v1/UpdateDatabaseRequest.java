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
 * The request for [FirestoreAdmin.UpdateDatabase][google.firestore.admin.v1.FirestoreAdmin.UpdateDatabase].
 * </pre>
 *
 * Protobuf type {@code google.firestore.admin.v1.UpdateDatabaseRequest}
 */
public final class UpdateDatabaseRequest extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.firestore.admin.v1.UpdateDatabaseRequest)
    UpdateDatabaseRequestOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use UpdateDatabaseRequest.newBuilder() to construct.
  private UpdateDatabaseRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private UpdateDatabaseRequest() {}

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new UpdateDatabaseRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
    return this.unknownFields;
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.firestore.admin.v1.FirestoreAdminProto
        .internal_static_google_firestore_admin_v1_UpdateDatabaseRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.admin.v1.FirestoreAdminProto
        .internal_static_google_firestore_admin_v1_UpdateDatabaseRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.admin.v1.UpdateDatabaseRequest.class,
            com.google.firestore.admin.v1.UpdateDatabaseRequest.Builder.class);
  }

  public static final int DATABASE_FIELD_NUMBER = 1;
  private com.google.firestore.admin.v1.Database database_;
  /**
   *
   *
   * <pre>
   * Required. The database to update.
   * </pre>
   *
   * <code>
   * .google.firestore.admin.v1.Database database = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return Whether the database field is set.
   */
  @java.lang.Override
  public boolean hasDatabase() {
    return database_ != null;
  }
  /**
   *
   *
   * <pre>
   * Required. The database to update.
   * </pre>
   *
   * <code>
   * .google.firestore.admin.v1.Database database = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return The database.
   */
  @java.lang.Override
  public com.google.firestore.admin.v1.Database getDatabase() {
    return database_ == null
        ? com.google.firestore.admin.v1.Database.getDefaultInstance()
        : database_;
  }
  /**
   *
   *
   * <pre>
   * Required. The database to update.
   * </pre>
   *
   * <code>
   * .google.firestore.admin.v1.Database database = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  @java.lang.Override
  public com.google.firestore.admin.v1.DatabaseOrBuilder getDatabaseOrBuilder() {
    return getDatabase();
  }

  public static final int UPDATE_MASK_FIELD_NUMBER = 2;
  private com.google.protobuf.FieldMask updateMask_;
  /**
   *
   *
   * <pre>
   * The list of fields to be updated.
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2;</code>
   *
   * @return Whether the updateMask field is set.
   */
  @java.lang.Override
  public boolean hasUpdateMask() {
    return updateMask_ != null;
  }
  /**
   *
   *
   * <pre>
   * The list of fields to be updated.
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2;</code>
   *
   * @return The updateMask.
   */
  @java.lang.Override
  public com.google.protobuf.FieldMask getUpdateMask() {
    return updateMask_ == null ? com.google.protobuf.FieldMask.getDefaultInstance() : updateMask_;
  }
  /**
   *
   *
   * <pre>
   * The list of fields to be updated.
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2;</code>
   */
  @java.lang.Override
  public com.google.protobuf.FieldMaskOrBuilder getUpdateMaskOrBuilder() {
    return getUpdateMask();
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
    if (database_ != null) {
      output.writeMessage(1, getDatabase());
    }
    if (updateMask_ != null) {
      output.writeMessage(2, getUpdateMask());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (database_ != null) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, getDatabase());
    }
    if (updateMask_ != null) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(2, getUpdateMask());
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
    if (!(obj instanceof com.google.firestore.admin.v1.UpdateDatabaseRequest)) {
      return super.equals(obj);
    }
    com.google.firestore.admin.v1.UpdateDatabaseRequest other =
        (com.google.firestore.admin.v1.UpdateDatabaseRequest) obj;

    if (hasDatabase() != other.hasDatabase()) return false;
    if (hasDatabase()) {
      if (!getDatabase().equals(other.getDatabase())) return false;
    }
    if (hasUpdateMask() != other.hasUpdateMask()) return false;
    if (hasUpdateMask()) {
      if (!getUpdateMask().equals(other.getUpdateMask())) return false;
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
    if (hasDatabase()) {
      hash = (37 * hash) + DATABASE_FIELD_NUMBER;
      hash = (53 * hash) + getDatabase().hashCode();
    }
    if (hasUpdateMask()) {
      hash = (37 * hash) + UPDATE_MASK_FIELD_NUMBER;
      hash = (53 * hash) + getUpdateMask().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest parseFrom(
      java.nio.ByteBuffer data) throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest parseFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest parseFrom(
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

  public static Builder newBuilder(com.google.firestore.admin.v1.UpdateDatabaseRequest prototype) {
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
   * The request for [FirestoreAdmin.UpdateDatabase][google.firestore.admin.v1.FirestoreAdmin.UpdateDatabase].
   * </pre>
   *
   * Protobuf type {@code google.firestore.admin.v1.UpdateDatabaseRequest}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.firestore.admin.v1.UpdateDatabaseRequest)
      com.google.firestore.admin.v1.UpdateDatabaseRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.firestore.admin.v1.FirestoreAdminProto
          .internal_static_google_firestore_admin_v1_UpdateDatabaseRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.admin.v1.FirestoreAdminProto
          .internal_static_google_firestore_admin_v1_UpdateDatabaseRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.admin.v1.UpdateDatabaseRequest.class,
              com.google.firestore.admin.v1.UpdateDatabaseRequest.Builder.class);
    }

    // Construct using com.google.firestore.admin.v1.UpdateDatabaseRequest.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (databaseBuilder_ == null) {
        database_ = null;
      } else {
        database_ = null;
        databaseBuilder_ = null;
      }
      if (updateMaskBuilder_ == null) {
        updateMask_ = null;
      } else {
        updateMask_ = null;
        updateMaskBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.firestore.admin.v1.FirestoreAdminProto
          .internal_static_google_firestore_admin_v1_UpdateDatabaseRequest_descriptor;
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.UpdateDatabaseRequest getDefaultInstanceForType() {
      return com.google.firestore.admin.v1.UpdateDatabaseRequest.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.UpdateDatabaseRequest build() {
      com.google.firestore.admin.v1.UpdateDatabaseRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.UpdateDatabaseRequest buildPartial() {
      com.google.firestore.admin.v1.UpdateDatabaseRequest result =
          new com.google.firestore.admin.v1.UpdateDatabaseRequest(this);
      if (databaseBuilder_ == null) {
        result.database_ = database_;
      } else {
        result.database_ = databaseBuilder_.build();
      }
      if (updateMaskBuilder_ == null) {
        result.updateMask_ = updateMask_;
      } else {
        result.updateMask_ = updateMaskBuilder_.build();
      }
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
      if (other instanceof com.google.firestore.admin.v1.UpdateDatabaseRequest) {
        return mergeFrom((com.google.firestore.admin.v1.UpdateDatabaseRequest) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.admin.v1.UpdateDatabaseRequest other) {
      if (other == com.google.firestore.admin.v1.UpdateDatabaseRequest.getDefaultInstance())
        return this;
      if (other.hasDatabase()) {
        mergeDatabase(other.getDatabase());
      }
      if (other.hasUpdateMask()) {
        mergeUpdateMask(other.getUpdateMask());
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
                input.readMessage(getDatabaseFieldBuilder().getBuilder(), extensionRegistry);

                break;
              } // case 10
            case 18:
              {
                input.readMessage(getUpdateMaskFieldBuilder().getBuilder(), extensionRegistry);

                break;
              } // case 18
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

    private com.google.firestore.admin.v1.Database database_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.firestore.admin.v1.Database,
            com.google.firestore.admin.v1.Database.Builder,
            com.google.firestore.admin.v1.DatabaseOrBuilder>
        databaseBuilder_;
    /**
     *
     *
     * <pre>
     * Required. The database to update.
     * </pre>
     *
     * <code>
     * .google.firestore.admin.v1.Database database = 1 [(.google.api.field_behavior) = REQUIRED];
     * </code>
     *
     * @return Whether the database field is set.
     */
    public boolean hasDatabase() {
      return databaseBuilder_ != null || database_ != null;
    }
    /**
     *
     *
     * <pre>
     * Required. The database to update.
     * </pre>
     *
     * <code>
     * .google.firestore.admin.v1.Database database = 1 [(.google.api.field_behavior) = REQUIRED];
     * </code>
     *
     * @return The database.
     */
    public com.google.firestore.admin.v1.Database getDatabase() {
      if (databaseBuilder_ == null) {
        return database_ == null
            ? com.google.firestore.admin.v1.Database.getDefaultInstance()
            : database_;
      } else {
        return databaseBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * Required. The database to update.
     * </pre>
     *
     * <code>
     * .google.firestore.admin.v1.Database database = 1 [(.google.api.field_behavior) = REQUIRED];
     * </code>
     */
    public Builder setDatabase(com.google.firestore.admin.v1.Database value) {
      if (databaseBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        database_ = value;
        onChanged();
      } else {
        databaseBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * Required. The database to update.
     * </pre>
     *
     * <code>
     * .google.firestore.admin.v1.Database database = 1 [(.google.api.field_behavior) = REQUIRED];
     * </code>
     */
    public Builder setDatabase(com.google.firestore.admin.v1.Database.Builder builderForValue) {
      if (databaseBuilder_ == null) {
        database_ = builderForValue.build();
        onChanged();
      } else {
        databaseBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * Required. The database to update.
     * </pre>
     *
     * <code>
     * .google.firestore.admin.v1.Database database = 1 [(.google.api.field_behavior) = REQUIRED];
     * </code>
     */
    public Builder mergeDatabase(com.google.firestore.admin.v1.Database value) {
      if (databaseBuilder_ == null) {
        if (database_ != null) {
          database_ =
              com.google.firestore.admin.v1.Database.newBuilder(database_)
                  .mergeFrom(value)
                  .buildPartial();
        } else {
          database_ = value;
        }
        onChanged();
      } else {
        databaseBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * Required. The database to update.
     * </pre>
     *
     * <code>
     * .google.firestore.admin.v1.Database database = 1 [(.google.api.field_behavior) = REQUIRED];
     * </code>
     */
    public Builder clearDatabase() {
      if (databaseBuilder_ == null) {
        database_ = null;
        onChanged();
      } else {
        database_ = null;
        databaseBuilder_ = null;
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * Required. The database to update.
     * </pre>
     *
     * <code>
     * .google.firestore.admin.v1.Database database = 1 [(.google.api.field_behavior) = REQUIRED];
     * </code>
     */
    public com.google.firestore.admin.v1.Database.Builder getDatabaseBuilder() {

      onChanged();
      return getDatabaseFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * Required. The database to update.
     * </pre>
     *
     * <code>
     * .google.firestore.admin.v1.Database database = 1 [(.google.api.field_behavior) = REQUIRED];
     * </code>
     */
    public com.google.firestore.admin.v1.DatabaseOrBuilder getDatabaseOrBuilder() {
      if (databaseBuilder_ != null) {
        return databaseBuilder_.getMessageOrBuilder();
      } else {
        return database_ == null
            ? com.google.firestore.admin.v1.Database.getDefaultInstance()
            : database_;
      }
    }
    /**
     *
     *
     * <pre>
     * Required. The database to update.
     * </pre>
     *
     * <code>
     * .google.firestore.admin.v1.Database database = 1 [(.google.api.field_behavior) = REQUIRED];
     * </code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.firestore.admin.v1.Database,
            com.google.firestore.admin.v1.Database.Builder,
            com.google.firestore.admin.v1.DatabaseOrBuilder>
        getDatabaseFieldBuilder() {
      if (databaseBuilder_ == null) {
        databaseBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.firestore.admin.v1.Database,
                com.google.firestore.admin.v1.Database.Builder,
                com.google.firestore.admin.v1.DatabaseOrBuilder>(
                getDatabase(), getParentForChildren(), isClean());
        database_ = null;
      }
      return databaseBuilder_;
    }

    private com.google.protobuf.FieldMask updateMask_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.protobuf.FieldMask,
            com.google.protobuf.FieldMask.Builder,
            com.google.protobuf.FieldMaskOrBuilder>
        updateMaskBuilder_;
    /**
     *
     *
     * <pre>
     * The list of fields to be updated.
     * </pre>
     *
     * <code>.google.protobuf.FieldMask update_mask = 2;</code>
     *
     * @return Whether the updateMask field is set.
     */
    public boolean hasUpdateMask() {
      return updateMaskBuilder_ != null || updateMask_ != null;
    }
    /**
     *
     *
     * <pre>
     * The list of fields to be updated.
     * </pre>
     *
     * <code>.google.protobuf.FieldMask update_mask = 2;</code>
     *
     * @return The updateMask.
     */
    public com.google.protobuf.FieldMask getUpdateMask() {
      if (updateMaskBuilder_ == null) {
        return updateMask_ == null
            ? com.google.protobuf.FieldMask.getDefaultInstance()
            : updateMask_;
      } else {
        return updateMaskBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * The list of fields to be updated.
     * </pre>
     *
     * <code>.google.protobuf.FieldMask update_mask = 2;</code>
     */
    public Builder setUpdateMask(com.google.protobuf.FieldMask value) {
      if (updateMaskBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        updateMask_ = value;
        onChanged();
      } else {
        updateMaskBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of fields to be updated.
     * </pre>
     *
     * <code>.google.protobuf.FieldMask update_mask = 2;</code>
     */
    public Builder setUpdateMask(com.google.protobuf.FieldMask.Builder builderForValue) {
      if (updateMaskBuilder_ == null) {
        updateMask_ = builderForValue.build();
        onChanged();
      } else {
        updateMaskBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of fields to be updated.
     * </pre>
     *
     * <code>.google.protobuf.FieldMask update_mask = 2;</code>
     */
    public Builder mergeUpdateMask(com.google.protobuf.FieldMask value) {
      if (updateMaskBuilder_ == null) {
        if (updateMask_ != null) {
          updateMask_ =
              com.google.protobuf.FieldMask.newBuilder(updateMask_).mergeFrom(value).buildPartial();
        } else {
          updateMask_ = value;
        }
        onChanged();
      } else {
        updateMaskBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of fields to be updated.
     * </pre>
     *
     * <code>.google.protobuf.FieldMask update_mask = 2;</code>
     */
    public Builder clearUpdateMask() {
      if (updateMaskBuilder_ == null) {
        updateMask_ = null;
        onChanged();
      } else {
        updateMask_ = null;
        updateMaskBuilder_ = null;
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of fields to be updated.
     * </pre>
     *
     * <code>.google.protobuf.FieldMask update_mask = 2;</code>
     */
    public com.google.protobuf.FieldMask.Builder getUpdateMaskBuilder() {

      onChanged();
      return getUpdateMaskFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * The list of fields to be updated.
     * </pre>
     *
     * <code>.google.protobuf.FieldMask update_mask = 2;</code>
     */
    public com.google.protobuf.FieldMaskOrBuilder getUpdateMaskOrBuilder() {
      if (updateMaskBuilder_ != null) {
        return updateMaskBuilder_.getMessageOrBuilder();
      } else {
        return updateMask_ == null
            ? com.google.protobuf.FieldMask.getDefaultInstance()
            : updateMask_;
      }
    }
    /**
     *
     *
     * <pre>
     * The list of fields to be updated.
     * </pre>
     *
     * <code>.google.protobuf.FieldMask update_mask = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.protobuf.FieldMask,
            com.google.protobuf.FieldMask.Builder,
            com.google.protobuf.FieldMaskOrBuilder>
        getUpdateMaskFieldBuilder() {
      if (updateMaskBuilder_ == null) {
        updateMaskBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.protobuf.FieldMask,
                com.google.protobuf.FieldMask.Builder,
                com.google.protobuf.FieldMaskOrBuilder>(
                getUpdateMask(), getParentForChildren(), isClean());
        updateMask_ = null;
      }
      return updateMaskBuilder_;
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

    // @@protoc_insertion_point(builder_scope:google.firestore.admin.v1.UpdateDatabaseRequest)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.admin.v1.UpdateDatabaseRequest)
  private static final com.google.firestore.admin.v1.UpdateDatabaseRequest DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.firestore.admin.v1.UpdateDatabaseRequest();
  }

  public static com.google.firestore.admin.v1.UpdateDatabaseRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<UpdateDatabaseRequest> PARSER =
      new com.google.protobuf.AbstractParser<UpdateDatabaseRequest>() {
        @java.lang.Override
        public UpdateDatabaseRequest parsePartialFrom(
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

  public static com.google.protobuf.Parser<UpdateDatabaseRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<UpdateDatabaseRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.firestore.admin.v1.UpdateDatabaseRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
