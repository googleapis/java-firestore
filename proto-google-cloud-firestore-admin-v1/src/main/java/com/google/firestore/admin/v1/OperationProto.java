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

package com.google.firestore.admin.v1;

public final class OperationProto {
  private OperationProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_IndexOperationMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_IndexOperationMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_FieldOperationMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_FieldOperationMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_FieldOperationMetadata_IndexConfigDelta_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_FieldOperationMetadata_IndexConfigDelta_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_FieldOperationMetadata_TtlConfigDelta_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_FieldOperationMetadata_TtlConfigDelta_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_ExportDocumentsMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_ExportDocumentsMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_ImportDocumentsMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_ImportDocumentsMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_ExportDocumentsResponse_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_ExportDocumentsResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Progress_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Progress_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n)google/firestore/admin/v1/operation.pr"
          + "oto\022\031google.firestore.admin.v1\032\031google/a"
          + "pi/resource.proto\032%google/firestore/admi"
          + "n/v1/index.proto\032\037google/protobuf/timest"
          + "amp.proto\"\275\002\n\026IndexOperationMetadata\022.\n\n"
          + "start_time\030\001 \001(\0132\032.google.protobuf.Times"
          + "tamp\022,\n\010end_time\030\002 \001(\0132\032.google.protobuf"
          + ".Timestamp\022\r\n\005index\030\003 \001(\t\0228\n\005state\030\004 \001(\016"
          + "2).google.firestore.admin.v1.OperationSt"
          + "ate\022?\n\022progress_documents\030\005 \001(\0132#.google"
          + ".firestore.admin.v1.Progress\022;\n\016progress"
          + "_bytes\030\006 \001(\0132#.google.firestore.admin.v1"
          + ".Progress\"\231\007\n\026FieldOperationMetadata\022.\n\n"
          + "start_time\030\001 \001(\0132\032.google.protobuf.Times"
          + "tamp\022,\n\010end_time\030\002 \001(\0132\032.google.protobuf"
          + ".Timestamp\022\r\n\005field\030\003 \001(\t\022_\n\023index_confi"
          + "g_deltas\030\004 \003(\0132B.google.firestore.admin."
          + "v1.FieldOperationMetadata.IndexConfigDel"
          + "ta\0228\n\005state\030\005 \001(\0162).google.firestore.adm"
          + "in.v1.OperationState\022?\n\022progress_documen"
          + "ts\030\006 \001(\0132#.google.firestore.admin.v1.Pro"
          + "gress\022;\n\016progress_bytes\030\007 \001(\0132#.google.f"
          + "irestore.admin.v1.Progress\022Z\n\020ttl_config"
          + "_delta\030\010 \001(\0132@.google.firestore.admin.v1"
          + ".FieldOperationMetadata.TtlConfigDelta\032\347"
          + "\001\n\020IndexConfigDelta\022b\n\013change_type\030\001 \001(\016"
          + "2M.google.firestore.admin.v1.FieldOperat"
          + "ionMetadata.IndexConfigDelta.ChangeType\022"
          + "/\n\005index\030\002 \001(\0132 .google.firestore.admin."
          + "v1.Index\">\n\nChangeType\022\033\n\027CHANGE_TYPE_UN"
          + "SPECIFIED\020\000\022\007\n\003ADD\020\001\022\n\n\006REMOVE\020\002\032\262\001\n\016Ttl"
          + "ConfigDelta\022`\n\013change_type\030\001 \001(\0162K.googl"
          + "e.firestore.admin.v1.FieldOperationMetad"
          + "ata.TtlConfigDelta.ChangeType\">\n\nChangeT"
          + "ype\022\033\n\027CHANGE_TYPE_UNSPECIFIED\020\000\022\007\n\003ADD\020"
          + "\001\022\n\n\006REMOVE\020\002\"\266\003\n\027ExportDocumentsMetadat"
          + "a\022.\n\nstart_time\030\001 \001(\0132\032.google.protobuf."
          + "Timestamp\022,\n\010end_time\030\002 \001(\0132\032.google.pro"
          + "tobuf.Timestamp\022B\n\017operation_state\030\003 \001(\016"
          + "2).google.firestore.admin.v1.OperationSt"
          + "ate\022?\n\022progress_documents\030\004 \001(\0132#.google"
          + ".firestore.admin.v1.Progress\022;\n\016progress"
          + "_bytes\030\005 \001(\0132#.google.firestore.admin.v1"
          + ".Progress\022\026\n\016collection_ids\030\006 \003(\t\022\031\n\021out"
          + "put_uri_prefix\030\007 \001(\t\022\025\n\rnamespace_ids\030\010 "
          + "\003(\t\0221\n\rsnapshot_time\030\t \001(\0132\032.google.prot"
          + "obuf.Timestamp\"\202\003\n\027ImportDocumentsMetada"
          + "ta\022.\n\nstart_time\030\001 \001(\0132\032.google.protobuf"
          + ".Timestamp\022,\n\010end_time\030\002 \001(\0132\032.google.pr"
          + "otobuf.Timestamp\022B\n\017operation_state\030\003 \001("
          + "\0162).google.firestore.admin.v1.OperationS"
          + "tate\022?\n\022progress_documents\030\004 \001(\0132#.googl"
          + "e.firestore.admin.v1.Progress\022;\n\016progres"
          + "s_bytes\030\005 \001(\0132#.google.firestore.admin.v"
          + "1.Progress\022\026\n\016collection_ids\030\006 \003(\t\022\030\n\020in"
          + "put_uri_prefix\030\007 \001(\t\022\025\n\rnamespace_ids\030\010 "
          + "\003(\t\"4\n\027ExportDocumentsResponse\022\031\n\021output"
          + "_uri_prefix\030\001 \001(\t\":\n\010Progress\022\026\n\016estimat"
          + "ed_work\030\001 \001(\003\022\026\n\016completed_work\030\002 \001(\003*\236\001"
          + "\n\016OperationState\022\037\n\033OPERATION_STATE_UNSP"
          + "ECIFIED\020\000\022\020\n\014INITIALIZING\020\001\022\016\n\nPROCESSIN"
          + "G\020\002\022\016\n\nCANCELLING\020\003\022\016\n\nFINALIZING\020\004\022\016\n\nS"
          + "UCCESSFUL\020\005\022\n\n\006FAILED\020\006\022\r\n\tCANCELLED\020\007B\335"
          + "\001\n\035com.google.firestore.admin.v1B\016Operat"
          + "ionProtoP\001Z9cloud.google.com/go/firestor"
          + "e/apiv1/admin/adminpb;adminpb\242\002\004GCFS\252\002\037G"
          + "oogle.Cloud.Firestore.Admin.V1\312\002\037Google\\"
          + "Cloud\\Firestore\\Admin\\V1\352\002#Google::Cloud"
          + "::Firestore::Admin::V1b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.api.ResourceProto.getDescriptor(),
              com.google.firestore.admin.v1.IndexProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
            });
    internal_static_google_firestore_admin_v1_IndexOperationMetadata_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_firestore_admin_v1_IndexOperationMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_IndexOperationMetadata_descriptor,
            new java.lang.String[] {
              "StartTime", "EndTime", "Index", "State", "ProgressDocuments", "ProgressBytes",
            });
    internal_static_google_firestore_admin_v1_FieldOperationMetadata_descriptor =
        getDescriptor().getMessageTypes().get(1);
    internal_static_google_firestore_admin_v1_FieldOperationMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_FieldOperationMetadata_descriptor,
            new java.lang.String[] {
              "StartTime",
              "EndTime",
              "Field",
              "IndexConfigDeltas",
              "State",
              "ProgressDocuments",
              "ProgressBytes",
              "TtlConfigDelta",
            });
    internal_static_google_firestore_admin_v1_FieldOperationMetadata_IndexConfigDelta_descriptor =
        internal_static_google_firestore_admin_v1_FieldOperationMetadata_descriptor
            .getNestedTypes()
            .get(0);
    internal_static_google_firestore_admin_v1_FieldOperationMetadata_IndexConfigDelta_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_FieldOperationMetadata_IndexConfigDelta_descriptor,
            new java.lang.String[] {
              "ChangeType", "Index",
            });
    internal_static_google_firestore_admin_v1_FieldOperationMetadata_TtlConfigDelta_descriptor =
        internal_static_google_firestore_admin_v1_FieldOperationMetadata_descriptor
            .getNestedTypes()
            .get(1);
    internal_static_google_firestore_admin_v1_FieldOperationMetadata_TtlConfigDelta_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_FieldOperationMetadata_TtlConfigDelta_descriptor,
            new java.lang.String[] {
              "ChangeType",
            });
    internal_static_google_firestore_admin_v1_ExportDocumentsMetadata_descriptor =
        getDescriptor().getMessageTypes().get(2);
    internal_static_google_firestore_admin_v1_ExportDocumentsMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_ExportDocumentsMetadata_descriptor,
            new java.lang.String[] {
              "StartTime",
              "EndTime",
              "OperationState",
              "ProgressDocuments",
              "ProgressBytes",
              "CollectionIds",
              "OutputUriPrefix",
              "NamespaceIds",
              "SnapshotTime",
            });
    internal_static_google_firestore_admin_v1_ImportDocumentsMetadata_descriptor =
        getDescriptor().getMessageTypes().get(3);
    internal_static_google_firestore_admin_v1_ImportDocumentsMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_ImportDocumentsMetadata_descriptor,
            new java.lang.String[] {
              "StartTime",
              "EndTime",
              "OperationState",
              "ProgressDocuments",
              "ProgressBytes",
              "CollectionIds",
              "InputUriPrefix",
              "NamespaceIds",
            });
    internal_static_google_firestore_admin_v1_ExportDocumentsResponse_descriptor =
        getDescriptor().getMessageTypes().get(4);
    internal_static_google_firestore_admin_v1_ExportDocumentsResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_ExportDocumentsResponse_descriptor,
            new java.lang.String[] {
              "OutputUriPrefix",
            });
    internal_static_google_firestore_admin_v1_Progress_descriptor =
        getDescriptor().getMessageTypes().get(5);
    internal_static_google_firestore_admin_v1_Progress_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Progress_descriptor,
            new java.lang.String[] {
              "EstimatedWork", "CompletedWork",
            });
    com.google.api.ResourceProto.getDescriptor();
    com.google.firestore.admin.v1.IndexProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
