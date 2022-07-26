// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/firestore/v1/write.proto

package com.google.firestore.v1;

public interface ExistenceFilterOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.ExistenceFilter)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * The target ID to which this filter applies.
   * </pre>
   *
   * <code>int32 target_id = 1;</code>
   * @return The targetId.
   */
  int getTargetId();

  /**
   * <pre>
   * The total count of documents that match [target_id][google.firestore.v1.ExistenceFilter.target_id].
   * If different from the count of documents in the client that match, the
   * client must manually determine which documents no longer match the target.
   * </pre>
   *
   * <code>int32 count = 2;</code>
   * @return The count.
   */
  int getCount();
}
