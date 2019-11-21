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
// source: google/firestore/v1beta1/write.proto

package com.google.firestore.v1beta1;

public interface DocumentChangeOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1beta1.DocumentChange)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The new state of the [Document][google.firestore.v1beta1.Document].
   * If `mask` is set, contains only fields that were updated or added.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.Document document = 1;</code>
   */
  boolean hasDocument();
  /**
   *
   *
   * <pre>
   * The new state of the [Document][google.firestore.v1beta1.Document].
   * If `mask` is set, contains only fields that were updated or added.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.Document document = 1;</code>
   */
  com.google.firestore.v1beta1.Document getDocument();
  /**
   *
   *
   * <pre>
   * The new state of the [Document][google.firestore.v1beta1.Document].
   * If `mask` is set, contains only fields that were updated or added.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.Document document = 1;</code>
   */
  com.google.firestore.v1beta1.DocumentOrBuilder getDocumentOrBuilder();

  /**
   *
   *
   * <pre>
   * A set of target IDs of targets that match this document.
   * </pre>
   *
   * <code>repeated int32 target_ids = 5;</code>
   */
  java.util.List<java.lang.Integer> getTargetIdsList();
  /**
   *
   *
   * <pre>
   * A set of target IDs of targets that match this document.
   * </pre>
   *
   * <code>repeated int32 target_ids = 5;</code>
   */
  int getTargetIdsCount();
  /**
   *
   *
   * <pre>
   * A set of target IDs of targets that match this document.
   * </pre>
   *
   * <code>repeated int32 target_ids = 5;</code>
   */
  int getTargetIds(int index);

  /**
   *
   *
   * <pre>
   * A set of target IDs for targets that no longer match this document.
   * </pre>
   *
   * <code>repeated int32 removed_target_ids = 6;</code>
   */
  java.util.List<java.lang.Integer> getRemovedTargetIdsList();
  /**
   *
   *
   * <pre>
   * A set of target IDs for targets that no longer match this document.
   * </pre>
   *
   * <code>repeated int32 removed_target_ids = 6;</code>
   */
  int getRemovedTargetIdsCount();
  /**
   *
   *
   * <pre>
   * A set of target IDs for targets that no longer match this document.
   * </pre>
   *
   * <code>repeated int32 removed_target_ids = 6;</code>
   */
  int getRemovedTargetIds(int index);
}
