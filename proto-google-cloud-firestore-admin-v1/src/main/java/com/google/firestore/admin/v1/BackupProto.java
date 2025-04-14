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
// source: google/firestore/admin/v1/backup.proto

// Protobuf Java Version: 3.25.5
package com.google.firestore.admin.v1;

public final class BackupProto {
  private BackupProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Backup_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Backup_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Backup_Stats_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Backup_Stats_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n"
          + "&google/firestore/admin/v1/backup.proto"
          + "\022\031google.firestore.admin.v1\032\037google/api/"
          + "field_behavior.proto\032\031google/api/resourc"
          + "e.proto\032\037google/protobuf/timestamp.proto\"\340\004\n"
          + "\006Backup\022\021\n"
          + "\004name\030\001 \001(\tB\003\340A\003\022;\n"
          + "\010database\030\002 \001(\tB)\340A\003\372A#\n"
          + "!firestore.googleapis.com/Database\022\031\n"
          + "\014database_uid\030\007 \001(\tB\003\340A\003\0226\n\r"
          + "snapshot_time\030\003 \001(\0132\032.google.protobuf.TimestampB\003\340A\003\0224\n"
          + "\013expire_time\030\004 \001(\0132\032.google.protobuf.TimestampB\003\340A\003\022;\n"
          + "\005stats\030\006 "
          + "\001(\0132\'.google.firestore.admin.v1.Backup.StatsB\003\340A\003\022;\n"
          + "\005state\030\010"
          + " \001(\0162\'.google.firestore.admin.v1.Backup.StateB\003\340A\003\032W\n"
          + "\005Stats\022\027\n\n"
          + "size_bytes\030\001 \001(\003B\003\340A\003\022\033\n"
          + "\016document_count\030\002 \001(\003B\003\340A\003\022\030\n"
          + "\013index_count\030\003 \001(\003B\003\340A\003\"J\n"
          + "\005State\022\025\n"
          + "\021STATE_UNSPECIFIED\020\000\022\014\n"
          + "\010CREATING\020\001\022\t\n"
          + "\005READY\020\002\022\021\n\r"
          + "NOT_AVAILABLE\020\003:^\352A[\n"
          + "\037firestore.googleapis.com/Backup\0228proje"
          + "cts/{project}/locations/{location}/backups/{backup}B\332\001\n"
          + "\035com.google.firestore.admin.v1B\013BackupProtoP\001Z9cloud.google.com/g"
          + "o/firestore/apiv1/admin/adminpb;adminpb\242"
          + "\002\004GCFS\252\002\037Google.Cloud.Firestore.Admin.V1"
          + "\312\002\037Google\\Cloud\\Firestore\\Admin\\V1\352\002#Goo"
          + "gle::Cloud::Firestore::Admin::V1b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.api.FieldBehaviorProto.getDescriptor(),
              com.google.api.ResourceProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
            });
    internal_static_google_firestore_admin_v1_Backup_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_firestore_admin_v1_Backup_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Backup_descriptor,
            new java.lang.String[] {
              "Name", "Database", "DatabaseUid", "SnapshotTime", "ExpireTime", "Stats", "State",
            });
    internal_static_google_firestore_admin_v1_Backup_Stats_descriptor =
        internal_static_google_firestore_admin_v1_Backup_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_admin_v1_Backup_Stats_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Backup_Stats_descriptor,
            new java.lang.String[] {
              "SizeBytes", "DocumentCount", "IndexCount",
            });
    com.google.protobuf.ExtensionRegistry registry =
        com.google.protobuf.ExtensionRegistry.newInstance();
    registry.add(com.google.api.FieldBehaviorProto.fieldBehavior);
    registry.add(com.google.api.ResourceProto.resource);
    registry.add(com.google.api.ResourceProto.resourceReference);
    com.google.protobuf.Descriptors.FileDescriptor.internalUpdateFileDescriptor(
        descriptor, registry);
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.api.ResourceProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
