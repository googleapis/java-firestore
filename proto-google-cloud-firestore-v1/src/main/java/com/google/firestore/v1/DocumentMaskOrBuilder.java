/*
 * Copyright 2023 Google LLC
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

public interface DocumentMaskOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.DocumentMask)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The list of field paths in the mask. See
   * [Document.fields][google.firestore.v1.Document.fields] for a field path
   * syntax reference.
   * </pre>
   *
   * <code>repeated string field_paths = 1;</code>
   *
   * @return A list containing the fieldPaths.
   */
  java.util.List<java.lang.String> getFieldPathsList();
  /**
   *
   *
   * <pre>
   * The list of field paths in the mask. See
   * [Document.fields][google.firestore.v1.Document.fields] for a field path
   * syntax reference.
   * </pre>
   *
   * <code>repeated string field_paths = 1;</code>
   *
   * @return The count of fieldPaths.
   */
  int getFieldPathsCount();
  /**
   *
   *
   * <pre>
   * The list of field paths in the mask. See
   * [Document.fields][google.firestore.v1.Document.fields] for a field path
   * syntax reference.
   * </pre>
   *
   * <code>repeated string field_paths = 1;</code>
   *
   * @param index The index of the element to return.
   * @return The fieldPaths at the given index.
   */
  java.lang.String getFieldPaths(int index);
  /**
   *
   *
   * <pre>
   * The list of field paths in the mask. See
   * [Document.fields][google.firestore.v1.Document.fields] for a field path
   * syntax reference.
   * </pre>
   *
   * <code>repeated string field_paths = 1;</code>
   *
   * @param index The index of the value to return.
   * @return The bytes of the fieldPaths at the given index.
   */
  com.google.protobuf.ByteString getFieldPathsBytes(int index);
}
