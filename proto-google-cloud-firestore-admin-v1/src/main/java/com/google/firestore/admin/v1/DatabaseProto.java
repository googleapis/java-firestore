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
// source: google/firestore/admin/v1/database.proto

// Protobuf Java Version: 3.25.5
package com.google.firestore.admin.v1;

public final class DatabaseProto {
  private DatabaseProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Database_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Database_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Database_CmekConfig_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Database_CmekConfig_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Database_SourceInfo_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Database_SourceInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Database_SourceInfo_BackupSource_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Database_SourceInfo_BackupSource_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Database_EncryptionConfig_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Database_EncryptionConfig_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Database_EncryptionConfig_GoogleDefaultEncryptionOptions_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Database_EncryptionConfig_GoogleDefaultEncryptionOptions_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Database_EncryptionConfig_SourceEncryptionOptions_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Database_EncryptionConfig_SourceEncryptionOptions_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Database_EncryptionConfig_CustomerManagedEncryptionOptions_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Database_EncryptionConfig_CustomerManagedEncryptionOptions_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n"
          + "(google/firestore/admin/v1/database.pro"
          + "to\022\031google.firestore.admin.v1\032\037google/ap"
          + "i/field_behavior.proto\032\031google/api/resou"
          + "rce.proto\032\036google/protobuf/duration.proto\032\037google/protobuf/timestamp.proto\"\334\023\n"
          + "\010Database\022\014\n"
          + "\004name\030\001 \001(\t\022\020\n"
          + "\003uid\030\003 \001(\tB\003\340A\003\0224\n"
          + "\013create_time\030\005 \001(\0132\032.google.protobuf.TimestampB\003\340A\003\0224\n"
          + "\013update_time\030\006 \001(\0132\032.google.protobuf.TimestampB\003\340A\003\0224\n"
          + "\013delete_time\030\007"
          + " \001(\0132\032.google.protobuf.TimestampB\003\340A\003\022\023\n"
          + "\013location_id\030\t \001(\t\022>\n"
          + "\004type\030\n"
          + " \001(\01620.google.firestore.admin.v1.Database.DatabaseType\022M\n"
          + "\020concurrency_mode\030\017 \001(\01623.googl"
          + "e.firestore.admin.v1.Database.ConcurrencyMode\022@\n"
          + "\030version_retention_period\030\021"
          + " \001(\0132\031.google.protobuf.DurationB\003\340A\003\022>\n"
          + "\025earliest_version_time\030\022"
          + " \001(\0132\032.google.protobuf.TimestampB\003\340A\003\022l\n"
          + "!point_in_time_recovery_enablement\030\025 \001(\0162A.google.firestore.ad"
          + "min.v1.Database.PointInTimeRecoveryEnablement\022a\n"
          + "\033app_engine_integration_mode\030\023 \001"
          + "(\0162<.google.firestore.admin.v1.Database.AppEngineIntegrationMode\022\027\n\n"
          + "key_prefix\030\024 \001(\tB\003\340A\003\022Z\n"
          + "\027delete_protection_state\030\026 \001"
          + "(\01629.google.firestore.admin.v1.Database.DeleteProtectionState\022H\n"
          + "\013cmek_config\030\027 \001"
          + "(\0132..google.firestore.admin.v1.Database.CmekConfigB\003\340A\001\022\030\n"
          + "\013previous_id\030\031 \001(\tB\003\340A\003\022H\n"
          + "\013source_info\030\032"
          + " \001(\0132..google.firestore.admin.v1.Database.SourceInfoB\003\340A\003\022\014\n"
          + "\004etag\030c \001(\t\032H\n\n"
          + "CmekConfig\022\031\n"
          + "\014kms_key_name\030\001 \001(\tB\003\340A\002\022\037\n"
          + "\022active_key_version\030\002 \003(\tB\003\340A\003\032\347\001\n\n"
          + "SourceInfo\022M\n"
          + "\006backup\030\001 \001(\0132;.goo"
          + "gle.firestore.admin.v1.Database.SourceInfo.BackupSourceH\000\022:\n"
          + "\toperation\030\003 \001(\tB\'\372A$\n"
          + "\"firestore.googleapis.com/Operation\032D\n"
          + "\014BackupSource\0224\n"
          + "\006backup\030\001 \001(\tB$\372A!\n"
          + "\037firestore.googleapis.com/BackupB\010\n"
          + "\006source\032\210\004\n"
          + "\020EncryptionConfig\022x\n"
          + "\031google_default_encryption\030\001 \001(\0132S.google.firestore.admin.v"
          + "1.Database.EncryptionConfig.GoogleDefaultEncryptionOptionsH\000\022m\n"
          + "\025use_source_encryption\030\002 \001(\0132L.google.firestore.admin.v1."
          + "Database.EncryptionConfig.SourceEncryptionOptionsH\000\022|\n"
          + "\033customer_managed_encryption\030\003 \001(\0132U.google.firestore.admin.v1.Dat"
          + "abase.EncryptionConfig.CustomerManagedEncryptionOptionsH\000\032 \n"
          + "\036GoogleDefaultEncryptionOptions\032\031\n"
          + "\027SourceEncryptionOptions\032=\n"
          + " CustomerManagedEncryptionOptions\022\031\n"
          + "\014kms_key_name\030\001 \001(\tB\003\340A\002B\021\n"
          + "\017encryption_type\"W\n"
          + "\014DatabaseType\022\035\n"
          + "\031DATABASE_TYPE_UNSPECIFIED\020\000\022\024\n"
          + "\020FIRESTORE_NATIVE\020\001\022\022\n"
          + "\016DATASTORE_MODE\020\002\"w\n"
          + "\017ConcurrencyMode\022 \n"
          + "\034CONCURRENCY_MODE_UNSPECIFIED\020\000\022\016\n\n"
          + "OPTIMISTIC\020\001\022\017\n"
          + "\013PESSIMISTIC\020\002\022!\n"
          + "\035OPTIMISTIC_WITH_ENTITY_GROUPS\020\003\"\233\001\n"
          + "\035PointInTimeRecoveryEnablement\0221\n"
          + "-POINT_IN_TIME_RECOVERY_ENABLEMENT_UNSPECIFIED\020\000\022\"\n"
          + "\036POINT_IN_TIME_RECOVERY_ENABLED\020\001\022#\n"
          + "\037POINT_IN_TIME_RECOVERY_DISABLED\020\002\"b\n"
          + "\030AppEngineIntegrationMode\022+\n"
          + "\'APP_ENGINE_INTEGRATION_MODE_UNSPECIFIED\020\000\022\013\n"
          + "\007ENABLED\020\001\022\014\n"
          + "\010DISABLED\020\002\"\177\n"
          + "\025DeleteProtectionState\022\'\n"
          + "#DELETE_PROTECTION_STATE_UNSPECIFIED\020\000\022\036\n"
          + "\032DELETE_PROTECTION_DISABLED\020\001\022\035\n"
          + "\031DELETE_PROTECTION_ENABLED\020\002:R\352AO\n"
          + "!firestore.googleapis.com/Database\022\'p"
          + "rojects/{project}/databases/{database}R\001\001B\303\002\n"
          + "\035com.google.firestore.admin.v1B\r"
          + "DatabaseProtoP\001Z9cloud.google.com/go/firest"
          + "ore/apiv1/admin/adminpb;adminpb\242\002\004GCFS\252\002"
          + "\037Google.Cloud.Firestore.Admin.V1\312\002\037Googl"
          + "e\\Cloud\\Firestore\\Admin\\V1\352\002#Google::Cloud::Firestore::Admin::V1\352Ad\n"
          + "\"firestore.googleapis.com/Operation\022>projects/{proje"
          + "ct}/databases/{database}/operations/{operation}b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.api.FieldBehaviorProto.getDescriptor(),
              com.google.api.ResourceProto.getDescriptor(),
              com.google.protobuf.DurationProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
            });
    internal_static_google_firestore_admin_v1_Database_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_firestore_admin_v1_Database_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Database_descriptor,
            new java.lang.String[] {
              "Name",
              "Uid",
              "CreateTime",
              "UpdateTime",
              "DeleteTime",
              "LocationId",
              "Type",
              "ConcurrencyMode",
              "VersionRetentionPeriod",
              "EarliestVersionTime",
              "PointInTimeRecoveryEnablement",
              "AppEngineIntegrationMode",
              "KeyPrefix",
              "DeleteProtectionState",
              "CmekConfig",
              "PreviousId",
              "SourceInfo",
              "Etag",
            });
    internal_static_google_firestore_admin_v1_Database_CmekConfig_descriptor =
        internal_static_google_firestore_admin_v1_Database_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_admin_v1_Database_CmekConfig_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Database_CmekConfig_descriptor,
            new java.lang.String[] {
              "KmsKeyName", "ActiveKeyVersion",
            });
    internal_static_google_firestore_admin_v1_Database_SourceInfo_descriptor =
        internal_static_google_firestore_admin_v1_Database_descriptor.getNestedTypes().get(1);
    internal_static_google_firestore_admin_v1_Database_SourceInfo_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Database_SourceInfo_descriptor,
            new java.lang.String[] {
              "Backup", "Operation", "Source",
            });
    internal_static_google_firestore_admin_v1_Database_SourceInfo_BackupSource_descriptor =
        internal_static_google_firestore_admin_v1_Database_SourceInfo_descriptor
            .getNestedTypes()
            .get(0);
    internal_static_google_firestore_admin_v1_Database_SourceInfo_BackupSource_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Database_SourceInfo_BackupSource_descriptor,
            new java.lang.String[] {
              "Backup",
            });
    internal_static_google_firestore_admin_v1_Database_EncryptionConfig_descriptor =
        internal_static_google_firestore_admin_v1_Database_descriptor.getNestedTypes().get(2);
    internal_static_google_firestore_admin_v1_Database_EncryptionConfig_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Database_EncryptionConfig_descriptor,
            new java.lang.String[] {
              "GoogleDefaultEncryption",
              "UseSourceEncryption",
              "CustomerManagedEncryption",
              "EncryptionType",
            });
    internal_static_google_firestore_admin_v1_Database_EncryptionConfig_GoogleDefaultEncryptionOptions_descriptor =
        internal_static_google_firestore_admin_v1_Database_EncryptionConfig_descriptor
            .getNestedTypes()
            .get(0);
    internal_static_google_firestore_admin_v1_Database_EncryptionConfig_GoogleDefaultEncryptionOptions_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Database_EncryptionConfig_GoogleDefaultEncryptionOptions_descriptor,
            new java.lang.String[] {});
    internal_static_google_firestore_admin_v1_Database_EncryptionConfig_SourceEncryptionOptions_descriptor =
        internal_static_google_firestore_admin_v1_Database_EncryptionConfig_descriptor
            .getNestedTypes()
            .get(1);
    internal_static_google_firestore_admin_v1_Database_EncryptionConfig_SourceEncryptionOptions_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Database_EncryptionConfig_SourceEncryptionOptions_descriptor,
            new java.lang.String[] {});
    internal_static_google_firestore_admin_v1_Database_EncryptionConfig_CustomerManagedEncryptionOptions_descriptor =
        internal_static_google_firestore_admin_v1_Database_EncryptionConfig_descriptor
            .getNestedTypes()
            .get(2);
    internal_static_google_firestore_admin_v1_Database_EncryptionConfig_CustomerManagedEncryptionOptions_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Database_EncryptionConfig_CustomerManagedEncryptionOptions_descriptor,
            new java.lang.String[] {
              "KmsKeyName",
            });
    com.google.protobuf.ExtensionRegistry registry =
        com.google.protobuf.ExtensionRegistry.newInstance();
    registry.add(com.google.api.FieldBehaviorProto.fieldBehavior);
    registry.add(com.google.api.ResourceProto.resource);
    registry.add(com.google.api.ResourceProto.resourceDefinition);
    registry.add(com.google.api.ResourceProto.resourceReference);
    com.google.protobuf.Descriptors.FileDescriptor.internalUpdateFileDescriptor(
        descriptor, registry);
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.api.ResourceProto.getDescriptor();
    com.google.protobuf.DurationProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
