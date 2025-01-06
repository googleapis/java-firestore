/*
 * Copyright 2025 Google LLC
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
// source: google/firestore/v1/query_profile.proto

// Protobuf Java Version: 3.25.5
package com.google.firestore.v1;

public interface PlanSummaryOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.PlanSummary)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The indexes selected for the query. For example:
   *  [
   *    {"query_scope": "Collection", "properties": "(foo ASC, __name__ ASC)"},
   *    {"query_scope": "Collection", "properties": "(bar ASC, __name__ ASC)"}
   *  ]
   * </pre>
   *
   * <code>repeated .google.protobuf.Struct indexes_used = 1;</code>
   */
  java.util.List<com.google.protobuf.Struct> getIndexesUsedList();
  /**
   *
   *
   * <pre>
   * The indexes selected for the query. For example:
   *  [
   *    {"query_scope": "Collection", "properties": "(foo ASC, __name__ ASC)"},
   *    {"query_scope": "Collection", "properties": "(bar ASC, __name__ ASC)"}
   *  ]
   * </pre>
   *
   * <code>repeated .google.protobuf.Struct indexes_used = 1;</code>
   */
  com.google.protobuf.Struct getIndexesUsed(int index);
  /**
   *
   *
   * <pre>
   * The indexes selected for the query. For example:
   *  [
   *    {"query_scope": "Collection", "properties": "(foo ASC, __name__ ASC)"},
   *    {"query_scope": "Collection", "properties": "(bar ASC, __name__ ASC)"}
   *  ]
   * </pre>
   *
   * <code>repeated .google.protobuf.Struct indexes_used = 1;</code>
   */
  int getIndexesUsedCount();
  /**
   *
   *
   * <pre>
   * The indexes selected for the query. For example:
   *  [
   *    {"query_scope": "Collection", "properties": "(foo ASC, __name__ ASC)"},
   *    {"query_scope": "Collection", "properties": "(bar ASC, __name__ ASC)"}
   *  ]
   * </pre>
   *
   * <code>repeated .google.protobuf.Struct indexes_used = 1;</code>
   */
  java.util.List<? extends com.google.protobuf.StructOrBuilder> getIndexesUsedOrBuilderList();
  /**
   *
   *
   * <pre>
   * The indexes selected for the query. For example:
   *  [
   *    {"query_scope": "Collection", "properties": "(foo ASC, __name__ ASC)"},
   *    {"query_scope": "Collection", "properties": "(bar ASC, __name__ ASC)"}
   *  ]
   * </pre>
   *
   * <code>repeated .google.protobuf.Struct indexes_used = 1;</code>
   */
  com.google.protobuf.StructOrBuilder getIndexesUsedOrBuilder(int index);
}
