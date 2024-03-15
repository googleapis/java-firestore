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
// source: google/firestore/admin/v1/schedule.proto

// Protobuf Java Version: 3.25.2
package com.google.firestore.admin.v1;

/**
 *
 *
 * <pre>
 * Represents a recurring schedule that runs on a specified day of the week.
 *
 * The time zone is UTC.
 * </pre>
 *
 * Protobuf type {@code google.firestore.admin.v1.WeeklyRecurrence}
 */
public final class WeeklyRecurrence extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.firestore.admin.v1.WeeklyRecurrence)
    WeeklyRecurrenceOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use WeeklyRecurrence.newBuilder() to construct.
  private WeeklyRecurrence(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private WeeklyRecurrence() {
    day_ = 0;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new WeeklyRecurrence();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.firestore.admin.v1.ScheduleProto
        .internal_static_google_firestore_admin_v1_WeeklyRecurrence_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.firestore.admin.v1.ScheduleProto
        .internal_static_google_firestore_admin_v1_WeeklyRecurrence_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.firestore.admin.v1.WeeklyRecurrence.class,
            com.google.firestore.admin.v1.WeeklyRecurrence.Builder.class);
  }

  public static final int DAY_FIELD_NUMBER = 2;
  private int day_ = 0;
  /**
   *
   *
   * <pre>
   * The day of week to run.
   *
   * DAY_OF_WEEK_UNSPECIFIED is not allowed.
   * </pre>
   *
   * <code>.google.type.DayOfWeek day = 2;</code>
   *
   * @return The enum numeric value on the wire for day.
   */
  @java.lang.Override
  public int getDayValue() {
    return day_;
  }
  /**
   *
   *
   * <pre>
   * The day of week to run.
   *
   * DAY_OF_WEEK_UNSPECIFIED is not allowed.
   * </pre>
   *
   * <code>.google.type.DayOfWeek day = 2;</code>
   *
   * @return The day.
   */
  @java.lang.Override
  public com.google.type.DayOfWeek getDay() {
    com.google.type.DayOfWeek result = com.google.type.DayOfWeek.forNumber(day_);
    return result == null ? com.google.type.DayOfWeek.UNRECOGNIZED : result;
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
    if (day_ != com.google.type.DayOfWeek.DAY_OF_WEEK_UNSPECIFIED.getNumber()) {
      output.writeEnum(2, day_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (day_ != com.google.type.DayOfWeek.DAY_OF_WEEK_UNSPECIFIED.getNumber()) {
      size += com.google.protobuf.CodedOutputStream.computeEnumSize(2, day_);
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
    if (!(obj instanceof com.google.firestore.admin.v1.WeeklyRecurrence)) {
      return super.equals(obj);
    }
    com.google.firestore.admin.v1.WeeklyRecurrence other =
        (com.google.firestore.admin.v1.WeeklyRecurrence) obj;

    if (day_ != other.day_) return false;
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
    hash = (37 * hash) + DAY_FIELD_NUMBER;
    hash = (53 * hash) + day_;
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence parseFrom(
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

  public static Builder newBuilder(com.google.firestore.admin.v1.WeeklyRecurrence prototype) {
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
   * Represents a recurring schedule that runs on a specified day of the week.
   *
   * The time zone is UTC.
   * </pre>
   *
   * Protobuf type {@code google.firestore.admin.v1.WeeklyRecurrence}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.firestore.admin.v1.WeeklyRecurrence)
      com.google.firestore.admin.v1.WeeklyRecurrenceOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.firestore.admin.v1.ScheduleProto
          .internal_static_google_firestore_admin_v1_WeeklyRecurrence_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.firestore.admin.v1.ScheduleProto
          .internal_static_google_firestore_admin_v1_WeeklyRecurrence_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.firestore.admin.v1.WeeklyRecurrence.class,
              com.google.firestore.admin.v1.WeeklyRecurrence.Builder.class);
    }

    // Construct using com.google.firestore.admin.v1.WeeklyRecurrence.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      day_ = 0;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.firestore.admin.v1.ScheduleProto
          .internal_static_google_firestore_admin_v1_WeeklyRecurrence_descriptor;
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.WeeklyRecurrence getDefaultInstanceForType() {
      return com.google.firestore.admin.v1.WeeklyRecurrence.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.WeeklyRecurrence build() {
      com.google.firestore.admin.v1.WeeklyRecurrence result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.firestore.admin.v1.WeeklyRecurrence buildPartial() {
      com.google.firestore.admin.v1.WeeklyRecurrence result =
          new com.google.firestore.admin.v1.WeeklyRecurrence(this);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.google.firestore.admin.v1.WeeklyRecurrence result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.day_ = day_;
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
      if (other instanceof com.google.firestore.admin.v1.WeeklyRecurrence) {
        return mergeFrom((com.google.firestore.admin.v1.WeeklyRecurrence) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.firestore.admin.v1.WeeklyRecurrence other) {
      if (other == com.google.firestore.admin.v1.WeeklyRecurrence.getDefaultInstance()) return this;
      if (other.day_ != 0) {
        setDayValue(other.getDayValue());
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
            case 16:
              {
                day_ = input.readEnum();
                bitField0_ |= 0x00000001;
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

    private int day_ = 0;
    /**
     *
     *
     * <pre>
     * The day of week to run.
     *
     * DAY_OF_WEEK_UNSPECIFIED is not allowed.
     * </pre>
     *
     * <code>.google.type.DayOfWeek day = 2;</code>
     *
     * @return The enum numeric value on the wire for day.
     */
    @java.lang.Override
    public int getDayValue() {
      return day_;
    }
    /**
     *
     *
     * <pre>
     * The day of week to run.
     *
     * DAY_OF_WEEK_UNSPECIFIED is not allowed.
     * </pre>
     *
     * <code>.google.type.DayOfWeek day = 2;</code>
     *
     * @param value The enum numeric value on the wire for day to set.
     * @return This builder for chaining.
     */
    public Builder setDayValue(int value) {
      day_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The day of week to run.
     *
     * DAY_OF_WEEK_UNSPECIFIED is not allowed.
     * </pre>
     *
     * <code>.google.type.DayOfWeek day = 2;</code>
     *
     * @return The day.
     */
    @java.lang.Override
    public com.google.type.DayOfWeek getDay() {
      com.google.type.DayOfWeek result = com.google.type.DayOfWeek.forNumber(day_);
      return result == null ? com.google.type.DayOfWeek.UNRECOGNIZED : result;
    }
    /**
     *
     *
     * <pre>
     * The day of week to run.
     *
     * DAY_OF_WEEK_UNSPECIFIED is not allowed.
     * </pre>
     *
     * <code>.google.type.DayOfWeek day = 2;</code>
     *
     * @param value The day to set.
     * @return This builder for chaining.
     */
    public Builder setDay(com.google.type.DayOfWeek value) {
      if (value == null) {
        throw new NullPointerException();
      }
      bitField0_ |= 0x00000001;
      day_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The day of week to run.
     *
     * DAY_OF_WEEK_UNSPECIFIED is not allowed.
     * </pre>
     *
     * <code>.google.type.DayOfWeek day = 2;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearDay() {
      bitField0_ = (bitField0_ & ~0x00000001);
      day_ = 0;
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

    // @@protoc_insertion_point(builder_scope:google.firestore.admin.v1.WeeklyRecurrence)
  }

  // @@protoc_insertion_point(class_scope:google.firestore.admin.v1.WeeklyRecurrence)
  private static final com.google.firestore.admin.v1.WeeklyRecurrence DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.firestore.admin.v1.WeeklyRecurrence();
  }

  public static com.google.firestore.admin.v1.WeeklyRecurrence getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<WeeklyRecurrence> PARSER =
      new com.google.protobuf.AbstractParser<WeeklyRecurrence>() {
        @java.lang.Override
        public WeeklyRecurrence parsePartialFrom(
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

  public static com.google.protobuf.Parser<WeeklyRecurrence> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<WeeklyRecurrence> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.firestore.admin.v1.WeeklyRecurrence getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
