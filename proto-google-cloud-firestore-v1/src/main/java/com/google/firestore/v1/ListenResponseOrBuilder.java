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
// source: google/firestore/v1/firestore.proto

package com.google.firestore.v1;

public interface ListenResponseOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.ListenResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Targets have changed.
   * </pre>
   *
   * <code>.google.firestore.v1.TargetChange target_change = 2;</code>
   */
  boolean hasTargetChange();
  /**
   *
   *
   * <pre>
   * Targets have changed.
   * </pre>
   *
   * <code>.google.firestore.v1.TargetChange target_change = 2;</code>
   */
  com.google.firestore.v1.TargetChange getTargetChange();
  /**
   *
   *
   * <pre>
   * Targets have changed.
   * </pre>
   *
   * <code>.google.firestore.v1.TargetChange target_change = 2;</code>
   */
  com.google.firestore.v1.TargetChangeOrBuilder getTargetChangeOrBuilder();

  /**
   *
   *
   * <pre>
   * A [Document][google.firestore.v1.Document] has changed.
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentChange document_change = 3;</code>
   */
  boolean hasDocumentChange();
  /**
   *
   *
   * <pre>
   * A [Document][google.firestore.v1.Document] has changed.
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentChange document_change = 3;</code>
   */
  com.google.firestore.v1.DocumentChange getDocumentChange();
  /**
   *
   *
   * <pre>
   * A [Document][google.firestore.v1.Document] has changed.
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentChange document_change = 3;</code>
   */
  com.google.firestore.v1.DocumentChangeOrBuilder getDocumentChangeOrBuilder();

  /**
   *
   *
   * <pre>
   * A [Document][google.firestore.v1.Document] has been deleted.
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentDelete document_delete = 4;</code>
   */
  boolean hasDocumentDelete();
  /**
   *
   *
   * <pre>
   * A [Document][google.firestore.v1.Document] has been deleted.
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentDelete document_delete = 4;</code>
   */
  com.google.firestore.v1.DocumentDelete getDocumentDelete();
  /**
   *
   *
   * <pre>
   * A [Document][google.firestore.v1.Document] has been deleted.
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentDelete document_delete = 4;</code>
   */
  com.google.firestore.v1.DocumentDeleteOrBuilder getDocumentDeleteOrBuilder();

  /**
   *
   *
   * <pre>
   * A [Document][google.firestore.v1.Document] has been removed from a target (because it is no longer
   * relevant to that target).
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentRemove document_remove = 6;</code>
   */
  boolean hasDocumentRemove();
  /**
   *
   *
   * <pre>
   * A [Document][google.firestore.v1.Document] has been removed from a target (because it is no longer
   * relevant to that target).
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentRemove document_remove = 6;</code>
   */
  com.google.firestore.v1.DocumentRemove getDocumentRemove();
  /**
   *
   *
   * <pre>
   * A [Document][google.firestore.v1.Document] has been removed from a target (because it is no longer
   * relevant to that target).
   * </pre>
   *
   * <code>.google.firestore.v1.DocumentRemove document_remove = 6;</code>
   */
  com.google.firestore.v1.DocumentRemoveOrBuilder getDocumentRemoveOrBuilder();

  /**
   *
   *
   * <pre>
   * A filter to apply to the set of documents previously returned for the
   * given target.
   * Returned when documents may have been removed from the given target, but
   * the exact documents are unknown.
   * </pre>
   *
   * <code>.google.firestore.v1.ExistenceFilter filter = 5;</code>
   */
  boolean hasFilter();
  /**
   *
   *
   * <pre>
   * A filter to apply to the set of documents previously returned for the
   * given target.
   * Returned when documents may have been removed from the given target, but
   * the exact documents are unknown.
   * </pre>
   *
   * <code>.google.firestore.v1.ExistenceFilter filter = 5;</code>
   */
  com.google.firestore.v1.ExistenceFilter getFilter();
  /**
   *
   *
   * <pre>
   * A filter to apply to the set of documents previously returned for the
   * given target.
   * Returned when documents may have been removed from the given target, but
   * the exact documents are unknown.
   * </pre>
   *
   * <code>.google.firestore.v1.ExistenceFilter filter = 5;</code>
   */
  com.google.firestore.v1.ExistenceFilterOrBuilder getFilterOrBuilder();

  public com.google.firestore.v1.ListenResponse.ResponseTypeCase getResponseTypeCase();
}
