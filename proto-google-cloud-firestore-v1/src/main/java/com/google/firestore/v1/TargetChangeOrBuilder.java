// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/firestore/v1/firestore.proto

package com.google.firestore.v1;

public interface TargetChangeOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.TargetChange)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The type of change that occurred.
   * </pre>
   *
   * <code>.google.firestore.v1.TargetChange.TargetChangeType target_change_type = 1;</code>
   *
   * @return The enum numeric value on the wire for targetChangeType.
   */
  int getTargetChangeTypeValue();
  /**
   *
   *
   * <pre>
   * The type of change that occurred.
   * </pre>
   *
   * <code>.google.firestore.v1.TargetChange.TargetChangeType target_change_type = 1;</code>
   *
   * @return The targetChangeType.
   */
  com.google.firestore.v1.TargetChange.TargetChangeType getTargetChangeType();

  /**
   *
   *
   * <pre>
   * The target IDs of targets that have changed.
   * If empty, the change applies to all targets.
   * The order of the target IDs is not defined.
   * </pre>
   *
   * <code>repeated int32 target_ids = 2;</code>
   *
   * @return A list containing the targetIds.
   */
  java.util.List<java.lang.Integer> getTargetIdsList();
  /**
   *
   *
   * <pre>
   * The target IDs of targets that have changed.
   * If empty, the change applies to all targets.
   * The order of the target IDs is not defined.
   * </pre>
   *
   * <code>repeated int32 target_ids = 2;</code>
   *
   * @return The count of targetIds.
   */
  int getTargetIdsCount();
  /**
   *
   *
   * <pre>
   * The target IDs of targets that have changed.
   * If empty, the change applies to all targets.
   * The order of the target IDs is not defined.
   * </pre>
   *
   * <code>repeated int32 target_ids = 2;</code>
   *
   * @param index The index of the element to return.
   * @return The targetIds at the given index.
   */
  int getTargetIds(int index);

  /**
   *
   *
   * <pre>
   * The error that resulted in this change, if applicable.
   * </pre>
   *
   * <code>.google.rpc.Status cause = 3;</code>
   *
   * @return Whether the cause field is set.
   */
  boolean hasCause();
  /**
   *
   *
   * <pre>
   * The error that resulted in this change, if applicable.
   * </pre>
   *
   * <code>.google.rpc.Status cause = 3;</code>
   *
   * @return The cause.
   */
  com.google.rpc.Status getCause();
  /**
   *
   *
   * <pre>
   * The error that resulted in this change, if applicable.
   * </pre>
   *
   * <code>.google.rpc.Status cause = 3;</code>
   */
  com.google.rpc.StatusOrBuilder getCauseOrBuilder();

  /**
   *
   *
   * <pre>
   * A token that can be used to resume the stream for the given `target_ids`,
   * or all targets if `target_ids` is empty.
   * Not set on every target change.
   * </pre>
   *
   * <code>bytes resume_token = 4;</code>
   *
   * @return The resumeToken.
   */
  com.google.protobuf.ByteString getResumeToken();

  /**
   *
   *
   * <pre>
   * The consistent `read_time` for the given `target_ids` (omitted when the
   * target_ids are not at a consistent snapshot).
   * The stream is guaranteed to send a `read_time` with `target_ids` empty
   * whenever the entire stream reaches a new consistent snapshot. ADD,
   * CURRENT, and RESET messages are guaranteed to (eventually) result in a
   * new consistent snapshot (while NO_CHANGE and REMOVE messages are not).
   * For a given stream, `read_time` is guaranteed to be monotonically
   * increasing.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 6;</code>
   *
   * @return Whether the readTime field is set.
   */
  boolean hasReadTime();
  /**
   *
   *
   * <pre>
   * The consistent `read_time` for the given `target_ids` (omitted when the
   * target_ids are not at a consistent snapshot).
   * The stream is guaranteed to send a `read_time` with `target_ids` empty
   * whenever the entire stream reaches a new consistent snapshot. ADD,
   * CURRENT, and RESET messages are guaranteed to (eventually) result in a
   * new consistent snapshot (while NO_CHANGE and REMOVE messages are not).
   * For a given stream, `read_time` is guaranteed to be monotonically
   * increasing.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 6;</code>
   *
   * @return The readTime.
   */
  com.google.protobuf.Timestamp getReadTime();
  /**
   *
   *
   * <pre>
   * The consistent `read_time` for the given `target_ids` (omitted when the
   * target_ids are not at a consistent snapshot).
   * The stream is guaranteed to send a `read_time` with `target_ids` empty
   * whenever the entire stream reaches a new consistent snapshot. ADD,
   * CURRENT, and RESET messages are guaranteed to (eventually) result in a
   * new consistent snapshot (while NO_CHANGE and REMOVE messages are not).
   * For a given stream, `read_time` is guaranteed to be monotonically
   * increasing.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 6;</code>
   */
  com.google.protobuf.TimestampOrBuilder getReadTimeOrBuilder();
}
