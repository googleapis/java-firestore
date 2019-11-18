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
// source: google/firestore/v1beta1/firestore.proto

package com.google.firestore.v1beta1;

public interface RunQueryResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1beta1.RunQueryResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * The transaction that was started as part of this request.
   * Can only be set in the first response, and only if
   * [RunQueryRequest.new_transaction][google.firestore.v1beta1.RunQueryRequest.new_transaction] was set in the request.
   * If set, no other fields will be set in this response.
   * </pre>
   *
   * <code>bytes transaction = 2;</code>
   */
  com.google.protobuf.ByteString getTransaction();

  /**
   * <pre>
   * A query result.
   * Not set when reporting partial progress.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.Document document = 1;</code>
   */
  boolean hasDocument();
  /**
   * <pre>
   * A query result.
   * Not set when reporting partial progress.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.Document document = 1;</code>
   */
  com.google.firestore.v1beta1.Document getDocument();
  /**
   * <pre>
   * A query result.
   * Not set when reporting partial progress.
   * </pre>
   *
   * <code>.google.firestore.v1beta1.Document document = 1;</code>
   */
  com.google.firestore.v1beta1.DocumentOrBuilder getDocumentOrBuilder();

  /**
   * <pre>
   * The time at which the document was read. This may be monotonically
   * increasing; in this case, the previous documents in the result stream are
   * guaranteed not to have changed between their `read_time` and this one.
   * If the query returns no results, a response with `read_time` and no
   * `document` will be sent, and this represents the time at which the query
   * was run.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 3;</code>
   */
  boolean hasReadTime();
  /**
   * <pre>
   * The time at which the document was read. This may be monotonically
   * increasing; in this case, the previous documents in the result stream are
   * guaranteed not to have changed between their `read_time` and this one.
   * If the query returns no results, a response with `read_time` and no
   * `document` will be sent, and this represents the time at which the query
   * was run.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 3;</code>
   */
  com.google.protobuf.Timestamp getReadTime();
  /**
   * <pre>
   * The time at which the document was read. This may be monotonically
   * increasing; in this case, the previous documents in the result stream are
   * guaranteed not to have changed between their `read_time` and this one.
   * If the query returns no results, a response with `read_time` and no
   * `document` will be sent, and this represents the time at which the query
   * was run.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp read_time = 3;</code>
   */
  com.google.protobuf.TimestampOrBuilder getReadTimeOrBuilder();

  /**
   * <pre>
   * The number of results that have been skipped due to an offset between
   * the last response and the current response.
   * </pre>
   *
   * <code>int32 skipped_results = 4;</code>
   */
  int getSkippedResults();
}
