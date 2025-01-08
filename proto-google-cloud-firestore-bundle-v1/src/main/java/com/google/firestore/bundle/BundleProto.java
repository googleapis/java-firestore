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
// source: google/firestore/bundle/bundle.proto

// Protobuf Java Version: 3.25.5
package com.google.firestore.bundle;

public final class BundleProto {
  private BundleProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_bundle_BundledQuery_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_bundle_BundledQuery_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_bundle_NamedQuery_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_bundle_NamedQuery_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_bundle_BundledDocumentMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_bundle_BundledDocumentMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_bundle_BundleMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_bundle_BundleMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_bundle_BundleElement_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_bundle_BundleElement_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n$google/firestore/bundle/bundle.proto\022\027"
          + "google.firestore.bundle\032\"google/firestor"
          + "e/v1/document.proto\032\037google/firestore/v1"
          + "/query.proto\032\037google/protobuf/timestamp."
          + "proto\"\325\001\n\014BundledQuery\022\016\n\006parent\030\001 \001(\t\022@"
          + "\n\020structured_query\030\002 \001(\0132$.google.firest"
          + "ore.v1.StructuredQueryH\000\022C\n\nlimit_type\030\003"
          + " \001(\0162/.google.firestore.bundle.BundledQu"
          + "ery.LimitType\" \n\tLimitType\022\t\n\005FIRST\020\000\022\010\n"
          + "\004LAST\020\001B\014\n\nquery_type\"\207\001\n\nNamedQuery\022\014\n\004"
          + "name\030\001 \001(\t\022<\n\rbundled_query\030\002 \001(\0132%.goog"
          + "le.firestore.bundle.BundledQuery\022-\n\tread"
          + "_time\030\003 \001(\0132\032.google.protobuf.Timestamp\""
          + "w\n\027BundledDocumentMetadata\022\014\n\004name\030\001 \001(\t"
          + "\022-\n\tread_time\030\002 \001(\0132\032.google.protobuf.Ti"
          + "mestamp\022\016\n\006exists\030\003 \001(\010\022\017\n\007queries\030\004 \003(\t"
          + "\"\214\001\n\016BundleMetadata\022\n\n\002id\030\001 \001(\t\022/\n\013creat"
          + "e_time\030\002 \001(\0132\032.google.protobuf.Timestamp"
          + "\022\017\n\007version\030\003 \001(\r\022\027\n\017total_documents\030\004 \001"
          + "(\r\022\023\n\013total_bytes\030\005 \001(\004\"\232\002\n\rBundleElemen"
          + "t\022;\n\010metadata\030\001 \001(\0132\'.google.firestore.b"
          + "undle.BundleMetadataH\000\022:\n\013named_query\030\002 "
          + "\001(\0132#.google.firestore.bundle.NamedQuery"
          + "H\000\022M\n\021document_metadata\030\003 \001(\01320.google.f"
          + "irestore.bundle.BundledDocumentMetadataH"
          + "\000\0221\n\010document\030\004 \001(\0132\035.google.firestore.v"
          + "1.DocumentH\000B\016\n\014element_typeB\222\001\n\033com.goo"
          + "gle.firestore.bundleB\013BundleProtoP\001Z6clo"
          + "ud.google.com/go/firestore/bundle/bundle"
          + "pb;bundlepb\242\002\005FSTPB\252\002\020Firestore.Bundle\312\002"
          + "\020Firestore\\Bundleb\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.firestore.v1.DocumentProto.getDescriptor(),
              com.google.firestore.v1.QueryProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
            });
    internal_static_google_firestore_bundle_BundledQuery_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_firestore_bundle_BundledQuery_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_bundle_BundledQuery_descriptor,
            new java.lang.String[] {
              "Parent", "StructuredQuery", "LimitType", "QueryType",
            });
    internal_static_google_firestore_bundle_NamedQuery_descriptor =
        getDescriptor().getMessageTypes().get(1);
    internal_static_google_firestore_bundle_NamedQuery_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_bundle_NamedQuery_descriptor,
            new java.lang.String[] {
              "Name", "BundledQuery", "ReadTime",
            });
    internal_static_google_firestore_bundle_BundledDocumentMetadata_descriptor =
        getDescriptor().getMessageTypes().get(2);
    internal_static_google_firestore_bundle_BundledDocumentMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_bundle_BundledDocumentMetadata_descriptor,
            new java.lang.String[] {
              "Name", "ReadTime", "Exists", "Queries",
            });
    internal_static_google_firestore_bundle_BundleMetadata_descriptor =
        getDescriptor().getMessageTypes().get(3);
    internal_static_google_firestore_bundle_BundleMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_bundle_BundleMetadata_descriptor,
            new java.lang.String[] {
              "Id", "CreateTime", "Version", "TotalDocuments", "TotalBytes",
            });
    internal_static_google_firestore_bundle_BundleElement_descriptor =
        getDescriptor().getMessageTypes().get(4);
    internal_static_google_firestore_bundle_BundleElement_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_bundle_BundleElement_descriptor,
            new java.lang.String[] {
              "Metadata", "NamedQuery", "DocumentMetadata", "Document", "ElementType",
            });
    com.google.firestore.v1.DocumentProto.getDescriptor();
    com.google.firestore.v1.QueryProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
