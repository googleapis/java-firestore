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
// source: google/firestore/v1/firestore.proto

// Protobuf Java Version: 3.25.5
package com.google.firestore.v1;

public interface ListDocumentsResponseOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.ListDocumentsResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The Documents found.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Document documents = 1;</code>
   */
  java.util.List<com.google.firestore.v1.Document> getDocumentsList();
  /**
   *
   *
   * <pre>
   * The Documents found.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Document documents = 1;</code>
   */
  com.google.firestore.v1.Document getDocuments(int index);
  /**
   *
   *
   * <pre>
   * The Documents found.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Document documents = 1;</code>
   */
  int getDocumentsCount();
  /**
   *
   *
   * <pre>
   * The Documents found.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Document documents = 1;</code>
   */
  java.util.List<? extends com.google.firestore.v1.DocumentOrBuilder> getDocumentsOrBuilderList();
  /**
   *
   *
   * <pre>
   * The Documents found.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.Document documents = 1;</code>
   */
  com.google.firestore.v1.DocumentOrBuilder getDocumentsOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * A token to retrieve the next page of documents.
   *
   * If this field is omitted, there are no subsequent pages.
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
   * A token to retrieve the next page of documents.
   *
   * If this field is omitted, there are no subsequent pages.
   * </pre>
   *
   * <code>string next_page_token = 2;</code>
   *
   * @return The bytes for nextPageToken.
   */
  com.google.protobuf.ByteString getNextPageTokenBytes();
}
