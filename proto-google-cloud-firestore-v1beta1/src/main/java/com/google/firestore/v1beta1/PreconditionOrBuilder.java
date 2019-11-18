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
// source: google/firestore/v1beta1/common.proto

package com.google.firestore.v1beta1;

public interface PreconditionOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1beta1.Precondition)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * When set to `true`, the target document must exist.
   * When set to `false`, the target document must not exist.
   * </pre>
   *
   * <code>bool exists = 1;</code>
   */
  boolean getExists();

  /**
   * <pre>
   * When set, the target document must exist and have been last updated at
   * that time.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp update_time = 2;</code>
   */
  boolean hasUpdateTime();
  /**
   * <pre>
   * When set, the target document must exist and have been last updated at
   * that time.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp update_time = 2;</code>
   */
  com.google.protobuf.Timestamp getUpdateTime();
  /**
   * <pre>
   * When set, the target document must exist and have been last updated at
   * that time.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp update_time = 2;</code>
   */
  com.google.protobuf.TimestampOrBuilder getUpdateTimeOrBuilder();

  public com.google.firestore.v1beta1.Precondition.ConditionTypeCase getConditionTypeCase();
}
