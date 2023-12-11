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
// source: google/firestore/bundle/bundle.proto

package com.google.firestore.bundle;

public interface BundleElementOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.bundle.BundleElement)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.google.firestore.bundle.BundleMetadata metadata = 1;</code>
   *
   * @return Whether the metadata field is set.
   */
  boolean hasMetadata();
  /**
   * <code>.google.firestore.bundle.BundleMetadata metadata = 1;</code>
   *
   * @return The metadata.
   */
  com.google.firestore.bundle.BundleMetadata getMetadata();
  /** <code>.google.firestore.bundle.BundleMetadata metadata = 1;</code> */
  com.google.firestore.bundle.BundleMetadataOrBuilder getMetadataOrBuilder();

  /**
   * <code>.google.firestore.bundle.NamedQuery named_query = 2;</code>
   *
   * @return Whether the namedQuery field is set.
   */
  boolean hasNamedQuery();
  /**
   * <code>.google.firestore.bundle.NamedQuery named_query = 2;</code>
   *
   * @return The namedQuery.
   */
  com.google.firestore.bundle.NamedQuery getNamedQuery();
  /** <code>.google.firestore.bundle.NamedQuery named_query = 2;</code> */
  com.google.firestore.bundle.NamedQueryOrBuilder getNamedQueryOrBuilder();

  /**
   * <code>.google.firestore.bundle.BundledDocumentMetadata document_metadata = 3;</code>
   *
   * @return Whether the documentMetadata field is set.
   */
  boolean hasDocumentMetadata();
  /**
   * <code>.google.firestore.bundle.BundledDocumentMetadata document_metadata = 3;</code>
   *
   * @return The documentMetadata.
   */
  com.google.firestore.bundle.BundledDocumentMetadata getDocumentMetadata();
  /** <code>.google.firestore.bundle.BundledDocumentMetadata document_metadata = 3;</code> */
  com.google.firestore.bundle.BundledDocumentMetadataOrBuilder getDocumentMetadataOrBuilder();

  /**
   * <code>.google.firestore.v1.Document document = 4;</code>
   *
   * @return Whether the document field is set.
   */
  boolean hasDocument();
  /**
   * <code>.google.firestore.v1.Document document = 4;</code>
   *
   * @return The document.
   */
  com.google.firestore.v1.Document getDocument();
  /** <code>.google.firestore.v1.Document document = 4;</code> */
  com.google.firestore.v1.DocumentOrBuilder getDocumentOrBuilder();

  com.google.firestore.bundle.BundleElement.ElementTypeCase getElementTypeCase();
}
