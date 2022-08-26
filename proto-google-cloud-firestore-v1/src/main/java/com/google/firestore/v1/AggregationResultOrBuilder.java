/*
 * Copyright 2022 Google LLC
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
// source: google/firestore/v1/aggregation_result.proto

package com.google.firestore.v1;

public interface AggregationResultOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.AggregationResult)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
   * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
   * assigned to the aggregation function on input and the size of this map
   * equals the number of aggregation functions in the query.
   * </pre>
   *
   * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
   */
  int getAggregateFieldsCount();
  /**
   * <pre>
   * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
   * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
   * assigned to the aggregation function on input and the size of this map
   * equals the number of aggregation functions in the query.
   * </pre>
   *
   * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
   */
  boolean containsAggregateFields(
      java.lang.String key);
  /**
   * Use {@link #getAggregateFieldsMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, com.google.firestore.v1.Value>
  getAggregateFields();
  /**
   * <pre>
   * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
   * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
   * assigned to the aggregation function on input and the size of this map
   * equals the number of aggregation functions in the query.
   * </pre>
   *
   * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
   */
  java.util.Map<java.lang.String, com.google.firestore.v1.Value>
  getAggregateFieldsMap();
  /**
   * <pre>
   * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
   * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
   * assigned to the aggregation function on input and the size of this map
   * equals the number of aggregation functions in the query.
   * </pre>
   *
   * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
   */

  /* nullable */
com.google.firestore.v1.Value getAggregateFieldsOrDefault(
      java.lang.String key,
      /* nullable */
com.google.firestore.v1.Value defaultValue);
  /**
   * <pre>
   * The result of the aggregation functions, ex: `COUNT(*) AS total_docs`.
   * The key is the [alias][google.firestore.v1.StructuredAggregationQuery.Aggregation.alias]
   * assigned to the aggregation function on input and the size of this map
   * equals the number of aggregation functions in the query.
   * </pre>
   *
   * <code>map&lt;string, .google.firestore.v1.Value&gt; aggregate_fields = 2;</code>
   */

  com.google.firestore.v1.Value getAggregateFieldsOrThrow(
      java.lang.String key);
}
