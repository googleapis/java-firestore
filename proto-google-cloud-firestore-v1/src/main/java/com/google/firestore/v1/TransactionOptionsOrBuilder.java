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
// source: google/firestore/v1/common.proto

package com.google.firestore.v1;

public interface TransactionOptionsOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.TransactionOptions)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * The transaction can only be used for read operations.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions.ReadOnly read_only = 2;</code>
   */
  boolean hasReadOnly();
  /**
   * <pre>
   * The transaction can only be used for read operations.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions.ReadOnly read_only = 2;</code>
   */
  com.google.firestore.v1.TransactionOptions.ReadOnly getReadOnly();
  /**
   * <pre>
   * The transaction can only be used for read operations.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions.ReadOnly read_only = 2;</code>
   */
  com.google.firestore.v1.TransactionOptions.ReadOnlyOrBuilder getReadOnlyOrBuilder();

  /**
   * <pre>
   * The transaction can be used for both read and write operations.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions.ReadWrite read_write = 3;</code>
   */
  boolean hasReadWrite();
  /**
   * <pre>
   * The transaction can be used for both read and write operations.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions.ReadWrite read_write = 3;</code>
   */
  com.google.firestore.v1.TransactionOptions.ReadWrite getReadWrite();
  /**
   * <pre>
   * The transaction can be used for both read and write operations.
   * </pre>
   *
   * <code>.google.firestore.v1.TransactionOptions.ReadWrite read_write = 3;</code>
   */
  com.google.firestore.v1.TransactionOptions.ReadWriteOrBuilder getReadWriteOrBuilder();

  public com.google.firestore.v1.TransactionOptions.ModeCase getModeCase();
}
