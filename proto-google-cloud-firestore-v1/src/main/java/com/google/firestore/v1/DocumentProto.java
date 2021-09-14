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
// source: google/firestore/v1/document.proto

package com.google.firestore.v1;

public final class DocumentProto {
  private DocumentProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_Document_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_Document_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_Document_FieldsEntry_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_Document_FieldsEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_Value_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_Value_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_ArrayValue_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_ArrayValue_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_MapValue_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_MapValue_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_MapValue_FieldsEntry_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_MapValue_FieldsEntry_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n\"google/firestore/v1/document.proto\022\023go"
          + "ogle.firestore.v1\032\034google/protobuf/struc"
          + "t.proto\032\037google/protobuf/timestamp.proto"
          + "\032\030google/type/latlng.proto\032\034google/api/a"
          + "nnotations.proto\"\200\002\n\010Document\022\014\n\004name\030\001 "
          + "\001(\t\0229\n\006fields\030\002 \003(\0132).google.firestore.v"
          + "1.Document.FieldsEntry\022/\n\013create_time\030\003 "
          + "\001(\0132\032.google.protobuf.Timestamp\022/\n\013updat"
          + "e_time\030\004 \001(\0132\032.google.protobuf.Timestamp"
          + "\032I\n\013FieldsEntry\022\013\n\003key\030\001 \001(\t\022)\n\005value\030\002 "
          + "\001(\0132\032.google.firestore.v1.Value:\0028\001\"\256\003\n\005"
          + "Value\0220\n\nnull_value\030\013 \001(\0162\032.google.proto"
          + "buf.NullValueH\000\022\027\n\rboolean_value\030\001 \001(\010H\000"
          + "\022\027\n\rinteger_value\030\002 \001(\003H\000\022\026\n\014double_valu"
          + "e\030\003 \001(\001H\000\0225\n\017timestamp_value\030\n \001(\0132\032.goo"
          + "gle.protobuf.TimestampH\000\022\026\n\014string_value"
          + "\030\021 \001(\tH\000\022\025\n\013bytes_value\030\022 \001(\014H\000\022\031\n\017refer"
          + "ence_value\030\005 \001(\tH\000\022.\n\017geo_point_value\030\010 "
          + "\001(\0132\023.google.type.LatLngH\000\0226\n\013array_valu"
          + "e\030\t \001(\0132\037.google.firestore.v1.ArrayValue"
          + "H\000\0222\n\tmap_value\030\006 \001(\0132\035.google.firestore"
          + ".v1.MapValueH\000B\014\n\nvalue_type\"8\n\nArrayVal"
          + "ue\022*\n\006values\030\001 \003(\0132\032.google.firestore.v1"
          + ".Value\"\220\001\n\010MapValue\0229\n\006fields\030\001 \003(\0132).go"
          + "ogle.firestore.v1.MapValue.FieldsEntry\032I"
          + "\n\013FieldsEntry\022\013\n\003key\030\001 \001(\t\022)\n\005value\030\002 \001("
          + "\0132\032.google.firestore.v1.Value:\0028\001B\306\001\n\027co"
          + "m.google.firestore.v1B\rDocumentProtoP\001Z<"
          + "google.golang.org/genproto/googleapis/fi"
          + "restore/v1;firestore\242\002\004GCFS\252\002\031Google.Clo"
          + "ud.Firestore.V1\312\002\031Google\\Cloud\\Firestore"
          + "\\V1\352\002\034Google::Cloud::Firestore::V1b\006prot"
          + "o3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.protobuf.StructProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
              com.google.type.LatLngProto.getDescriptor(),
              com.google.api.AnnotationsProto.getDescriptor(),
            });
    internal_static_google_firestore_v1_Document_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_firestore_v1_Document_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_Document_descriptor,
            new java.lang.String[] {
              "Name", "Fields", "CreateTime", "UpdateTime",
            });
    internal_static_google_firestore_v1_Document_FieldsEntry_descriptor =
        internal_static_google_firestore_v1_Document_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_v1_Document_FieldsEntry_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_Document_FieldsEntry_descriptor,
            new java.lang.String[] {
              "Key", "Value",
            });
    internal_static_google_firestore_v1_Value_descriptor = getDescriptor().getMessageTypes().get(1);
    internal_static_google_firestore_v1_Value_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_Value_descriptor,
            new java.lang.String[] {
              "NullValue",
              "BooleanValue",
              "IntegerValue",
              "DoubleValue",
              "TimestampValue",
              "StringValue",
              "BytesValue",
              "ReferenceValue",
              "GeoPointValue",
              "ArrayValue",
              "MapValue",
              "ValueType",
            });
    internal_static_google_firestore_v1_ArrayValue_descriptor =
        getDescriptor().getMessageTypes().get(2);
    internal_static_google_firestore_v1_ArrayValue_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_ArrayValue_descriptor,
            new java.lang.String[] {
              "Values",
            });
    internal_static_google_firestore_v1_MapValue_descriptor =
        getDescriptor().getMessageTypes().get(3);
    internal_static_google_firestore_v1_MapValue_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_MapValue_descriptor,
            new java.lang.String[] {
              "Fields",
            });
    internal_static_google_firestore_v1_MapValue_FieldsEntry_descriptor =
        internal_static_google_firestore_v1_MapValue_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_v1_MapValue_FieldsEntry_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_MapValue_FieldsEntry_descriptor,
            new java.lang.String[] {
              "Key", "Value",
            });
    com.google.protobuf.StructProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
    com.google.type.LatLngProto.getDescriptor();
    com.google.api.AnnotationsProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
