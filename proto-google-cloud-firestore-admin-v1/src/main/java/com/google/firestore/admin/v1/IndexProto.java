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
// source: google/firestore/admin/v1/index.proto

package com.google.firestore.admin.v1;

public final class IndexProto {
  private IndexProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Index_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Index_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_Index_IndexField_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_Index_IndexField_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n%google/firestore/admin/v1/index.proto\022"
          + "\031google.firestore.admin.v1\032\034google/api/a"
          + "nnotations.proto\"\247\005\n\005Index\022\014\n\004name\030\001 \001(\t"
          + "\022@\n\013query_scope\030\002 \001(\0162+.google.firestore"
          + ".admin.v1.Index.QueryScope\022;\n\006fields\030\003 \003"
          + "(\0132+.google.firestore.admin.v1.Index.Ind"
          + "exField\0225\n\005state\030\004 \001(\0162&.google.firestor"
          + "e.admin.v1.Index.State\032\275\002\n\nIndexField\022\022\n"
          + "\nfield_path\030\001 \001(\t\022B\n\005order\030\002 \001(\01621.googl"
          + "e.firestore.admin.v1.Index.IndexField.Or"
          + "derH\000\022O\n\014array_config\030\003 \001(\01627.google.fir"
          + "estore.admin.v1.Index.IndexField.ArrayCo"
          + "nfigH\000\"=\n\005Order\022\025\n\021ORDER_UNSPECIFIED\020\000\022\r"
          + "\n\tASCENDING\020\001\022\016\n\nDESCENDING\020\002\"9\n\013ArrayCo"
          + "nfig\022\034\n\030ARRAY_CONFIG_UNSPECIFIED\020\000\022\014\n\010CO"
          + "NTAINS\020\001B\014\n\nvalue_mode\"O\n\nQueryScope\022\033\n\027"
          + "QUERY_SCOPE_UNSPECIFIED\020\000\022\016\n\nCOLLECTION\020"
          + "\001\022\024\n\020COLLECTION_GROUP\020\002\"I\n\005State\022\025\n\021STAT"
          + "E_UNSPECIFIED\020\000\022\014\n\010CREATING\020\001\022\t\n\005READY\020\002"
          + "\022\020\n\014NEEDS_REPAIR\020\003B\270\001\n\035com.google.firest"
          + "ore.admin.v1B\nIndexProtoP\001Z>google.golan"
          + "g.org/genproto/googleapis/firestore/admi"
          + "n/v1;admin\242\002\004GCFS\252\002\037Google.Cloud.Firesto"
          + "re.Admin.V1\312\002\037Google\\Cloud\\Firestore\\Adm"
          + "in\\V1b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
        descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.api.AnnotationsProto.getDescriptor(),
        },
        assigner);
    internal_static_google_firestore_admin_v1_Index_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_firestore_admin_v1_Index_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Index_descriptor,
            new java.lang.String[] {
              "Name", "QueryScope", "Fields", "State",
            });
    internal_static_google_firestore_admin_v1_Index_IndexField_descriptor =
        internal_static_google_firestore_admin_v1_Index_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_admin_v1_Index_IndexField_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_Index_IndexField_descriptor,
            new java.lang.String[] {
              "FieldPath", "Order", "ArrayConfig", "ValueMode",
            });
    com.google.api.AnnotationsProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
