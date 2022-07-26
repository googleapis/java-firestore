/*
 * Copyright 2020 Google LLC
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
// source: google/firestore/v1/firestore.proto

package com.google.firestore.v1;

public interface PartitionQueryResponseOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.PartitionQueryResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Partition results.
   * Each partition is a split point that can be used by RunQuery as a starting
   * or end point for the query results. The RunQuery requests must be made with
   * the same query supplied to this PartitionQuery request. The partition
   * cursors will be ordered according to same ordering as the results of the
   * query supplied to PartitionQuery.
   * For example, if a PartitionQuery request returns partition cursors A and B,
   * running the following three queries will return the entire result set of
   * the original query:
   *  * query, end_at A
   *  * query, start_at A, end_at B
   *  * query, start_at B
   * An empty result may indicate that the query has too few results to be
   * partitioned.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Cursor partitions = 1;</code>
   */
  java.util.List<com.google.firestore.v1.Cursor> getPartitionsList();
  /**
   *
   *
   * <pre>
   * Partition results.
   * Each partition is a split point that can be used by RunQuery as a starting
   * or end point for the query results. The RunQuery requests must be made with
   * the same query supplied to this PartitionQuery request. The partition
   * cursors will be ordered according to same ordering as the results of the
   * query supplied to PartitionQuery.
   * For example, if a PartitionQuery request returns partition cursors A and B,
   * running the following three queries will return the entire result set of
   * the original query:
   *  * query, end_at A
   *  * query, start_at A, end_at B
   *  * query, start_at B
   * An empty result may indicate that the query has too few results to be
   * partitioned.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Cursor partitions = 1;</code>
   */
  com.google.firestore.v1.Cursor getPartitions(int index);
  /**
   *
   *
   * <pre>
   * Partition results.
   * Each partition is a split point that can be used by RunQuery as a starting
   * or end point for the query results. The RunQuery requests must be made with
   * the same query supplied to this PartitionQuery request. The partition
   * cursors will be ordered according to same ordering as the results of the
   * query supplied to PartitionQuery.
   * For example, if a PartitionQuery request returns partition cursors A and B,
   * running the following three queries will return the entire result set of
   * the original query:
   *  * query, end_at A
   *  * query, start_at A, end_at B
   *  * query, start_at B
   * An empty result may indicate that the query has too few results to be
   * partitioned.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Cursor partitions = 1;</code>
   */
  int getPartitionsCount();
  /**
   *
   *
   * <pre>
   * Partition results.
   * Each partition is a split point that can be used by RunQuery as a starting
   * or end point for the query results. The RunQuery requests must be made with
   * the same query supplied to this PartitionQuery request. The partition
   * cursors will be ordered according to same ordering as the results of the
   * query supplied to PartitionQuery.
   * For example, if a PartitionQuery request returns partition cursors A and B,
   * running the following three queries will return the entire result set of
   * the original query:
   *  * query, end_at A
   *  * query, start_at A, end_at B
   *  * query, start_at B
   * An empty result may indicate that the query has too few results to be
   * partitioned.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Cursor partitions = 1;</code>
   */
  java.util.List<? extends com.google.firestore.v1.CursorOrBuilder> getPartitionsOrBuilderList();
  /**
   *
   *
   * <pre>
   * Partition results.
   * Each partition is a split point that can be used by RunQuery as a starting
   * or end point for the query results. The RunQuery requests must be made with
   * the same query supplied to this PartitionQuery request. The partition
   * cursors will be ordered according to same ordering as the results of the
   * query supplied to PartitionQuery.
   * For example, if a PartitionQuery request returns partition cursors A and B,
   * running the following three queries will return the entire result set of
   * the original query:
   *  * query, end_at A
   *  * query, start_at A, end_at B
   *  * query, start_at B
   * An empty result may indicate that the query has too few results to be
   * partitioned.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Cursor partitions = 1;</code>
   */
  com.google.firestore.v1.CursorOrBuilder getPartitionsOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * A page token that may be used to request an additional set of results, up
   * to the number specified by `partition_count` in the PartitionQuery request.
   * If blank, there are no more results.
   * </pre>
   *
   * <code>string next_page_token = 2;</code>
   *
   * @return The nextPageToken.
   */
  java.lang.String getNextPageToken();
  /**
   *
   *
   * <pre>
   * A page token that may be used to request an additional set of results, up
   * to the number specified by `partition_count` in the PartitionQuery request.
   * If blank, there are no more results.
   * </pre>
   *
   * <code>string next_page_token = 2;</code>
   *
   * @return The bytes for nextPageToken.
   */
  com.google.protobuf.ByteString getNextPageTokenBytes();
}
