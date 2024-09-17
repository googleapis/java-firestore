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
// source: google/firestore/v1/write.proto

// Protobuf Java Version: 3.25.4
package com.google.firestore.v1;

public final class WriteProto {
  private WriteProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_Write_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_Write_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_DocumentTransform_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_DocumentTransform_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_DocumentTransform_FieldTransform_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_DocumentTransform_FieldTransform_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_WriteResult_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_WriteResult_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_DocumentChange_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_DocumentChange_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_DocumentDelete_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_DocumentDelete_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_DocumentRemove_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_DocumentRemove_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_v1_ExistenceFilter_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_ExistenceFilter_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n\037google/firestore/v1/write.proto\022\023googl"
          + "e.firestore.v1\032&google/firestore/v1/bloo"
          + "m_filter.proto\032 google/firestore/v1/comm"
          + "on.proto\032\"google/firestore/v1/document.p"
          + "roto\032\037google/protobuf/timestamp.proto\"\333\002"
          + "\n\005Write\022/\n\006update\030\001 \001(\0132\035.google.firesto"
          + "re.v1.DocumentH\000\022\020\n\006delete\030\002 \001(\tH\000\022;\n\ttr"
          + "ansform\030\006 \001(\0132&.google.firestore.v1.Docu"
          + "mentTransformH\000\0226\n\013update_mask\030\003 \001(\0132!.g"
          + "oogle.firestore.v1.DocumentMask\022P\n\021updat"
          + "e_transforms\030\007 \003(\01325.google.firestore.v1"
          + ".DocumentTransform.FieldTransform\022;\n\020cur"
          + "rent_document\030\004 \001(\0132!.google.firestore.v"
          + "1.PreconditionB\013\n\toperation\"\345\004\n\021Document"
          + "Transform\022\020\n\010document\030\001 \001(\t\022O\n\020field_tra"
          + "nsforms\030\002 \003(\01325.google.firestore.v1.Docu"
          + "mentTransform.FieldTransform\032\354\003\n\016FieldTr"
          + "ansform\022\022\n\nfield_path\030\001 \001(\t\022`\n\023set_to_se"
          + "rver_value\030\002 \001(\0162A.google.firestore.v1.D"
          + "ocumentTransform.FieldTransform.ServerVa"
          + "lueH\000\022/\n\tincrement\030\003 \001(\0132\032.google.firest"
          + "ore.v1.ValueH\000\022-\n\007maximum\030\004 \001(\0132\032.google"
          + ".firestore.v1.ValueH\000\022-\n\007minimum\030\005 \001(\0132\032"
          + ".google.firestore.v1.ValueH\000\022B\n\027append_m"
          + "issing_elements\030\006 \001(\0132\037.google.firestore"
          + ".v1.ArrayValueH\000\022@\n\025remove_all_from_arra"
          + "y\030\007 \001(\0132\037.google.firestore.v1.ArrayValue"
          + "H\000\"=\n\013ServerValue\022\034\n\030SERVER_VALUE_UNSPEC"
          + "IFIED\020\000\022\020\n\014REQUEST_TIME\020\001B\020\n\016transform_t"
          + "ype\"u\n\013WriteResult\022/\n\013update_time\030\001 \001(\0132"
          + "\032.google.protobuf.Timestamp\0225\n\021transform"
          + "_results\030\002 \003(\0132\032.google.firestore.v1.Val"
          + "ue\"q\n\016DocumentChange\022/\n\010document\030\001 \001(\0132\035"
          + ".google.firestore.v1.Document\022\022\n\ntarget_"
          + "ids\030\005 \003(\005\022\032\n\022removed_target_ids\030\006 \003(\005\"m\n"
          + "\016DocumentDelete\022\020\n\010document\030\001 \001(\t\022\032\n\022rem"
          + "oved_target_ids\030\006 \003(\005\022-\n\tread_time\030\004 \001(\013"
          + "2\032.google.protobuf.Timestamp\"m\n\016Document"
          + "Remove\022\020\n\010document\030\001 \001(\t\022\032\n\022removed_targ"
          + "et_ids\030\002 \003(\005\022-\n\tread_time\030\004 \001(\0132\032.google"
          + ".protobuf.Timestamp\"n\n\017ExistenceFilter\022\021"
          + "\n\ttarget_id\030\001 \001(\005\022\r\n\005count\030\002 \001(\005\0229\n\017unch"
          + "anged_names\030\003 \001(\0132 .google.firestore.v1."
          + "BloomFilterB\302\001\n\027com.google.firestore.v1B"
          + "\nWriteProtoP\001Z;cloud.google.com/go/fires"
          + "tore/apiv1/firestorepb;firestorepb\242\002\004GCF"
          + "S\252\002\031Google.Cloud.Firestore.V1\312\002\031Google\\C"
          + "loud\\Firestore\\V1\352\002\034Google::Cloud::Fires"
          + "tore::V1b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.firestore.v1.BloomFilterProto.getDescriptor(),
              com.google.firestore.v1.CommonProto.getDescriptor(),
              com.google.firestore.v1.DocumentProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
            });
    internal_static_google_firestore_v1_Write_descriptor = getDescriptor().getMessageTypes().get(0);
    internal_static_google_firestore_v1_Write_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_Write_descriptor,
            new java.lang.String[] {
              "Update",
              "Delete",
              "Transform",
              "UpdateMask",
              "UpdateTransforms",
              "CurrentDocument",
              "Operation",
            });
    internal_static_google_firestore_v1_DocumentTransform_descriptor =
        getDescriptor().getMessageTypes().get(1);
    internal_static_google_firestore_v1_DocumentTransform_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_DocumentTransform_descriptor,
            new java.lang.String[] {
              "Document", "FieldTransforms",
            });
    internal_static_google_firestore_v1_DocumentTransform_FieldTransform_descriptor =
        internal_static_google_firestore_v1_DocumentTransform_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_v1_DocumentTransform_FieldTransform_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_DocumentTransform_FieldTransform_descriptor,
            new java.lang.String[] {
              "FieldPath",
              "SetToServerValue",
              "Increment",
              "Maximum",
              "Minimum",
              "AppendMissingElements",
              "RemoveAllFromArray",
              "TransformType",
            });
    internal_static_google_firestore_v1_WriteResult_descriptor =
        getDescriptor().getMessageTypes().get(2);
    internal_static_google_firestore_v1_WriteResult_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_WriteResult_descriptor,
            new java.lang.String[] {
              "UpdateTime", "TransformResults",
            });
    internal_static_google_firestore_v1_DocumentChange_descriptor =
        getDescriptor().getMessageTypes().get(3);
    internal_static_google_firestore_v1_DocumentChange_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_DocumentChange_descriptor,
            new java.lang.String[] {
              "Document", "TargetIds", "RemovedTargetIds",
            });
    internal_static_google_firestore_v1_DocumentDelete_descriptor =
        getDescriptor().getMessageTypes().get(4);
    internal_static_google_firestore_v1_DocumentDelete_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_DocumentDelete_descriptor,
            new java.lang.String[] {
              "Document", "RemovedTargetIds", "ReadTime",
            });
    internal_static_google_firestore_v1_DocumentRemove_descriptor =
        getDescriptor().getMessageTypes().get(5);
    internal_static_google_firestore_v1_DocumentRemove_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_DocumentRemove_descriptor,
            new java.lang.String[] {
              "Document", "RemovedTargetIds", "ReadTime",
            });
    internal_static_google_firestore_v1_ExistenceFilter_descriptor =
        getDescriptor().getMessageTypes().get(6);
    internal_static_google_firestore_v1_ExistenceFilter_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_v1_ExistenceFilter_descriptor,
            new java.lang.String[] {
              "TargetId", "Count", "UnchangedNames",
            });
    com.google.firestore.v1.BloomFilterProto.getDescriptor();
    com.google.firestore.v1.CommonProto.getDescriptor();
    com.google.firestore.v1.DocumentProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
