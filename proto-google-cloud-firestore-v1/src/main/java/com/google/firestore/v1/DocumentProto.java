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
// source: google/firestore/v1/document.proto

// Protobuf Java Version: 3.25.3
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
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_Function_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_Function_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_Function_OptionsEntry_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_Function_OptionsEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_Pipeline_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_Pipeline_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_Pipeline_Stage_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_Pipeline_Stage_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_Pipeline_Stage_OptionsEntry_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_Pipeline_Stage_OptionsEntry_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n\"google/firestore/v1/document.proto\022\023go"
          + "ogle.firestore.v1\032\034google/protobuf/struc"
          + "t.proto\032\037google/protobuf/timestamp.proto"
          + "\032\030google/type/latlng.proto\"\200\002\n\010Document\022"
          + "\014\n\004name\030\001 \001(\t\0229\n\006fields\030\002 \003(\0132).google.f"
          + "irestore.v1.Document.FieldsEntry\022/\n\013crea"
          + "te_time\030\003 \001(\0132\032.google.protobuf.Timestam"
          + "p\022/\n\013update_time\030\004 \001(\0132\032.google.protobuf"
          + ".Timestamp\032I\n\013FieldsEntry\022\013\n\003key\030\001 \001(\t\022)"
          + "\n\005value\030\002 \001(\0132\032.google.firestore.v1.Valu"
          + "e:\0028\001\"\301\004\n\005Value\0220\n\nnull_value\030\013 \001(\0162\032.go"
          + "ogle.protobuf.NullValueH\000\022\027\n\rboolean_val"
          + "ue\030\001 \001(\010H\000\022\027\n\rinteger_value\030\002 \001(\003H\000\022\026\n\014d"
          + "ouble_value\030\003 \001(\001H\000\0225\n\017timestamp_value\030\n"
          + " \001(\0132\032.google.protobuf.TimestampH\000\022\026\n\014st"
          + "ring_value\030\021 \001(\tH\000\022\025\n\013bytes_value\030\022 \001(\014H"
          + "\000\022\031\n\017reference_value\030\005 \001(\tH\000\022.\n\017geo_poin"
          + "t_value\030\010 \001(\0132\023.google.type.LatLngH\000\0226\n\013"
          + "array_value\030\t \001(\0132\037.google.firestore.v1."
          + "ArrayValueH\000\0222\n\tmap_value\030\006 \001(\0132\035.google"
          + ".firestore.v1.MapValueH\000\022\037\n\025field_refere"
          + "nce_value\030\023 \001(\tH\000\0227\n\016function_value\030\024 \001("
          + "\0132\035.google.firestore.v1.FunctionH\000\0227\n\016pi"
          + "peline_value\030\025 \001(\0132\035.google.firestore.v1"
          + ".PipelineH\000B\014\n\nvalue_type\"8\n\nArrayValue\022"
          + "*\n\006values\030\001 \003(\0132\032.google.firestore.v1.Va"
          + "lue\"\220\001\n\010MapValue\0229\n\006fields\030\001 \003(\0132).googl"
          + "e.firestore.v1.MapValue.FieldsEntry\032I\n\013F"
          + "ieldsEntry\022\013\n\003key\030\001 \001(\t\022)\n\005value\030\002 \001(\0132\032"
          + ".google.firestore.v1.Value:\0028\001\"\313\001\n\010Funct"
          + "ion\022\014\n\004name\030\001 \001(\t\022(\n\004args\030\002 \003(\0132\032.google"
          + ".firestore.v1.Value\022;\n\007options\030\003 \003(\0132*.g"
          + "oogle.firestore.v1.Function.OptionsEntry"
          + "\032J\n\014OptionsEntry\022\013\n\003key\030\001 \001(\t\022)\n\005value\030\002"
          + " \001(\0132\032.google.firestore.v1.Value:\0028\001\"\220\002\n"
          + "\010Pipeline\0223\n\006stages\030\001 \003(\0132#.google.fires"
          + "tore.v1.Pipeline.Stage\032\316\001\n\005Stage\022\014\n\004name"
          + "\030\001 \001(\t\022(\n\004args\030\002 \003(\0132\032.google.firestore."
          + "v1.Value\022A\n\007options\030\003 \003(\01320.google.fires"
          + "tore.v1.Pipeline.Stage.OptionsEntry\032J\n\014O"
          + "ptionsEntry\022\013\n\003key\030\001 \001(\t\022)\n\005value\030\002 \001(\0132"
          + "\032.google.firestore.v1.Value:\0028\001B\305\001\n\027com."
          + "google.firestore.v1B\rDocumentProtoP\001Z;cl"
          + "oud.google.com/go/firestore/apiv1/firest"
          + "orepb;firestorepb\242\002\004GCFS\252\002\031Google.Cloud."
          + "Firestore.V1\312\002\031Google\\Cloud\\Firestore\\V1"
          + "\352\002\034Google::Cloud::Firestore::V1b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.protobuf.StructProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
              com.google.type.LatLngProto.getDescriptor(),
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
              "FieldReferenceValue",
              "FunctionValue",
              "PipelineValue",
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
    internal_static_google_firestore_v1_Function_descriptor =
        getDescriptor().getMessageTypes().get(4);
    internal_static_google_firestore_v1_Function_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_Function_descriptor,
            new java.lang.String[] {
              "Name", "Args", "Options",
            });
    internal_static_google_firestore_v1_Function_OptionsEntry_descriptor =
        internal_static_google_firestore_v1_Function_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_v1_Function_OptionsEntry_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_Function_OptionsEntry_descriptor,
            new java.lang.String[] {
              "Key", "Value",
            });
    internal_static_google_firestore_v1_Pipeline_descriptor =
        getDescriptor().getMessageTypes().get(5);
    internal_static_google_firestore_v1_Pipeline_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_Pipeline_descriptor,
            new java.lang.String[] {
              "Stages",
            });
    internal_static_google_firestore_v1_Pipeline_Stage_descriptor =
        internal_static_google_firestore_v1_Pipeline_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_v1_Pipeline_Stage_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_Pipeline_Stage_descriptor,
            new java.lang.String[] {
              "Name", "Args", "Options",
            });
    internal_static_google_firestore_v1_Pipeline_Stage_OptionsEntry_descriptor =
        internal_static_google_firestore_v1_Pipeline_Stage_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_v1_Pipeline_Stage_OptionsEntry_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_Pipeline_Stage_OptionsEntry_descriptor,
            new java.lang.String[] {
              "Key", "Value",
            });
    com.google.protobuf.StructProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
    com.google.type.LatLngProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
