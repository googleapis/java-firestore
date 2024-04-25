// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/firestore/admin/v1/field.proto

// Protobuf Java Version: 3.25.3
package com.google.firestore.admin.v1;

public final class FieldProto {
  private FieldProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_admin_v1_Field_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Field_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_admin_v1_Field_IndexConfig_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Field_IndexConfig_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_admin_v1_Field_TtlConfig_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Field_TtlConfig_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n%google/firestore/admin/v1/field.proto\022" +
      "\031google.firestore.admin.v1\032\037google/api/f" +
      "ield_behavior.proto\032\031google/api/resource" +
      ".proto\032%google/firestore/admin/v1/index." +
      "proto\"\305\004\n\005Field\022\021\n\004name\030\001 \001(\tB\003\340A\002\022B\n\014in" +
      "dex_config\030\002 \001(\0132,.google.firestore.admi" +
      "n.v1.Field.IndexConfig\022>\n\nttl_config\030\003 \001" +
      "(\0132*.google.firestore.admin.v1.Field.Ttl" +
      "Config\032\211\001\n\013IndexConfig\0221\n\007indexes\030\001 \003(\0132" +
      " .google.firestore.admin.v1.Index\022\034\n\024use" +
      "s_ancestor_config\030\002 \001(\010\022\026\n\016ancestor_fiel" +
      "d\030\003 \001(\t\022\021\n\treverting\030\004 \001(\010\032\235\001\n\tTtlConfig" +
      "\022D\n\005state\030\001 \001(\01620.google.firestore.admin" +
      ".v1.Field.TtlConfig.StateB\003\340A\003\"J\n\005State\022" +
      "\025\n\021STATE_UNSPECIFIED\020\000\022\014\n\010CREATING\020\001\022\n\n\006" +
      "ACTIVE\020\002\022\020\n\014NEEDS_REPAIR\020\003:y\352Av\n\036firesto" +
      "re.googleapis.com/Field\022Tprojects/{proje" +
      "ct}/databases/{database}/collectionGroup" +
      "s/{collection}/fields/{field}B\331\001\n\035com.go" +
      "ogle.firestore.admin.v1B\nFieldProtoP\001Z9c" +
      "loud.google.com/go/firestore/apiv1/admin" +
      "/adminpb;adminpb\242\002\004GCFS\252\002\037Google.Cloud.F" +
      "irestore.Admin.V1\312\002\037Google\\Cloud\\Firesto" +
      "re\\Admin\\V1\352\002#Google::Cloud::Firestore::" +
      "Admin::V1b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.api.FieldBehaviorProto.getDescriptor(),
          com.google.api.ResourceProto.getDescriptor(),
          com.google.firestore.admin.v1.IndexProto.getDescriptor(),
        });
    internal_static_google_firestore_admin_v1_Field_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_google_firestore_admin_v1_Field_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_admin_v1_Field_descriptor,
        new java.lang.String[] { "Name", "IndexConfig", "TtlConfig", });
    internal_static_google_firestore_admin_v1_Field_IndexConfig_descriptor =
      internal_static_google_firestore_admin_v1_Field_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_admin_v1_Field_IndexConfig_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_admin_v1_Field_IndexConfig_descriptor,
        new java.lang.String[] { "Indexes", "UsesAncestorConfig", "AncestorField", "Reverting", });
    internal_static_google_firestore_admin_v1_Field_TtlConfig_descriptor =
      internal_static_google_firestore_admin_v1_Field_descriptor.getNestedTypes().get(1);
    internal_static_google_firestore_admin_v1_Field_TtlConfig_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_admin_v1_Field_TtlConfig_descriptor,
        new java.lang.String[] { "State", });
    com.google.protobuf.ExtensionRegistry registry =
        com.google.protobuf.ExtensionRegistry.newInstance();
    registry.add(com.google.api.FieldBehaviorProto.fieldBehavior);
    registry.add(com.google.api.ResourceProto.resource);
    com.google.protobuf.Descriptors.FileDescriptor
        .internalUpdateFileDescriptor(descriptor, registry);
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.api.ResourceProto.getDescriptor();
    com.google.firestore.admin.v1.IndexProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
