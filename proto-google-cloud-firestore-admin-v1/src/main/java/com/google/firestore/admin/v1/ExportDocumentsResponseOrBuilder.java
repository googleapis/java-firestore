/*
 * Copyright 2024 Google LLC
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
// source: google/firestore/admin/v1/operation.proto

// Protobuf Java Version: 3.25.3
package com.google.firestore.admin.v1;

public interface ExportDocumentsResponseOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.admin.v1.ExportDocumentsResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Location of the output files. This can be used to begin an import
   * into Cloud Firestore (this project or another project) after the operation
   * completes successfully.
   * </pre>
   *
   * <code>string output_uri_prefix = 1;</code>
   *
   * @return The outputUriPrefix.
   */
  java.lang.String getOutputUriPrefix();
  /**
   *
   *
   * <pre>
   * Location of the output files. This can be used to begin an import
   * into Cloud Firestore (this project or another project) after the operation
   * completes successfully.
   * </pre>
   *
   * <code>string output_uri_prefix = 1;</code>
   *
   * @return The bytes for outputUriPrefix.
   */
  com.google.protobuf.ByteString getOutputUriPrefixBytes();
}
