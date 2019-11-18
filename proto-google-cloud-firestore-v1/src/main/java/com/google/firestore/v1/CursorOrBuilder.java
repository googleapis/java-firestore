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
// source: google/firestore/v1/query.proto

package com.google.firestore.v1;

public interface CursorOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.Cursor)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * The values that represent a position, in the order they appear in
   * the order by clause of a query.
   * Can contain fewer values than specified in the order by clause.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Value values = 1;</code>
   */
  java.util.List<com.google.firestore.v1.Value> 
      getValuesList();
  /**
   * <pre>
   * The values that represent a position, in the order they appear in
   * the order by clause of a query.
   * Can contain fewer values than specified in the order by clause.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Value values = 1;</code>
   */
  com.google.firestore.v1.Value getValues(int index);
  /**
   * <pre>
   * The values that represent a position, in the order they appear in
   * the order by clause of a query.
   * Can contain fewer values than specified in the order by clause.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Value values = 1;</code>
   */
  int getValuesCount();
  /**
   * <pre>
   * The values that represent a position, in the order they appear in
   * the order by clause of a query.
   * Can contain fewer values than specified in the order by clause.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Value values = 1;</code>
   */
  java.util.List<? extends com.google.firestore.v1.ValueOrBuilder> 
      getValuesOrBuilderList();
  /**
   * <pre>
   * The values that represent a position, in the order they appear in
   * the order by clause of a query.
   * Can contain fewer values than specified in the order by clause.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Value values = 1;</code>
   */
  com.google.firestore.v1.ValueOrBuilder getValuesOrBuilder(
      int index);

  /**
   * <pre>
   * If the position is just before or just after the given values, relative
   * to the sort order defined by the query.
   * </pre>
   *
   * <code>bool before = 2;</code>
   */
  boolean getBefore();
}
