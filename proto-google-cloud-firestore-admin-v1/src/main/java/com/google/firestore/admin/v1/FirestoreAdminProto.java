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
// source: google/firestore/admin/v1/firestore_admin.proto

package com.google.firestore.admin.v1;

public final class FirestoreAdminProto {
  private FirestoreAdminProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_ListDatabasesRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_ListDatabasesRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_ListDatabasesResponse_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_ListDatabasesResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_GetDatabaseRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_GetDatabaseRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_UpdateDatabaseRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_UpdateDatabaseRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_UpdateDatabaseMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_UpdateDatabaseMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_CreateIndexRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_CreateIndexRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_ListIndexesRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_ListIndexesRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_ListIndexesResponse_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_ListIndexesResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_GetIndexRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_GetIndexRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_DeleteIndexRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_DeleteIndexRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_UpdateFieldRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_UpdateFieldRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_GetFieldRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_GetFieldRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_ListFieldsRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_ListFieldsRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_ListFieldsResponse_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_ListFieldsResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_ExportDocumentsRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_ExportDocumentsRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_firestore_admin_v1_ImportDocumentsRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_admin_v1_ImportDocumentsRequest_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n/google/firestore/admin/v1/firestore_ad"
          + "min.proto\022\031google.firestore.admin.v1\032\034go"
          + "ogle/api/annotations.proto\032\027google/api/c"
          + "lient.proto\032\037google/api/field_behavior.p"
          + "roto\032\031google/api/resource.proto\032(google/"
          + "firestore/admin/v1/database.proto\032%googl"
          + "e/firestore/admin/v1/field.proto\032%google"
          + "/firestore/admin/v1/index.proto\032)google/"
          + "firestore/admin/v1/operation.proto\032#goog"
          + "le/longrunning/operations.proto\032\033google/"
          + "protobuf/empty.proto\032 google/protobuf/fi"
          + "eld_mask.proto\032\037google/protobuf/timestam"
          + "p.proto\"Q\n\024ListDatabasesRequest\0229\n\006paren"
          + "t\030\001 \001(\tB)\340A\002\372A#\022!firestore.googleapis.co"
          + "m/Database\"O\n\025ListDatabasesResponse\0226\n\td"
          + "atabases\030\001 \003(\0132#.google.firestore.admin."
          + "v1.Database\"M\n\022GetDatabaseRequest\0227\n\004nam"
          + "e\030\001 \001(\tB)\340A\002\372A#\n!firestore.googleapis.co"
          + "m/Database\"\204\001\n\025UpdateDatabaseRequest\022:\n\010"
          + "database\030\001 \001(\0132#.google.firestore.admin."
          + "v1.DatabaseB\003\340A\002\022/\n\013update_mask\030\002 \001(\0132\032."
          + "google.protobuf.FieldMask\"\030\n\026UpdateDatab"
          + "aseMetadata\"\214\001\n\022CreateIndexRequest\022@\n\006pa"
          + "rent\030\001 \001(\tB0\340A\002\372A*\n(firestore.googleapis"
          + ".com/CollectionGroup\0224\n\005index\030\002 \001(\0132 .go"
          + "ogle.firestore.admin.v1.IndexB\003\340A\002\"\215\001\n\022L"
          + "istIndexesRequest\022@\n\006parent\030\001 \001(\tB0\340A\002\372A"
          + "*\n(firestore.googleapis.com/CollectionGr"
          + "oup\022\016\n\006filter\030\002 \001(\t\022\021\n\tpage_size\030\003 \001(\005\022\022"
          + "\n\npage_token\030\004 \001(\t\"a\n\023ListIndexesRespons"
          + "e\0221\n\007indexes\030\001 \003(\0132 .google.firestore.ad"
          + "min.v1.Index\022\027\n\017next_page_token\030\002 \001(\t\"G\n"
          + "\017GetIndexRequest\0224\n\004name\030\001 \001(\tB&\340A\002\372A \n\036"
          + "firestore.googleapis.com/Index\"J\n\022Delete"
          + "IndexRequest\0224\n\004name\030\001 \001(\tB&\340A\002\372A \n\036fire"
          + "store.googleapis.com/Index\"{\n\022UpdateFiel"
          + "dRequest\0224\n\005field\030\001 \001(\0132 .google.firesto"
          + "re.admin.v1.FieldB\003\340A\002\022/\n\013update_mask\030\002 "
          + "\001(\0132\032.google.protobuf.FieldMask\"G\n\017GetFi"
          + "eldRequest\0224\n\004name\030\001 \001(\tB&\340A\002\372A \n\036firest"
          + "ore.googleapis.com/Field\"\214\001\n\021ListFieldsR"
          + "equest\022@\n\006parent\030\001 \001(\tB0\340A\002\372A*\n(firestor"
          + "e.googleapis.com/CollectionGroup\022\016\n\006filt"
          + "er\030\002 \001(\t\022\021\n\tpage_size\030\003 \001(\005\022\022\n\npage_toke"
          + "n\030\004 \001(\t\"_\n\022ListFieldsResponse\0220\n\006fields\030"
          + "\001 \003(\0132 .google.firestore.admin.v1.Field\022"
          + "\027\n\017next_page_token\030\002 \001(\t\"\204\001\n\026ExportDocum"
          + "entsRequest\0227\n\004name\030\001 \001(\tB)\340A\002\372A#\n!fires"
          + "tore.googleapis.com/Database\022\026\n\016collecti"
          + "on_ids\030\002 \003(\t\022\031\n\021output_uri_prefix\030\003 \001(\t\""
          + "\203\001\n\026ImportDocumentsRequest\0227\n\004name\030\001 \001(\t"
          + "B)\340A\002\372A#\n!firestore.googleapis.com/Datab"
          + "ase\022\026\n\016collection_ids\030\002 \003(\t\022\030\n\020input_uri"
          + "_prefix\030\003 \001(\t2\222\023\n\016FirestoreAdmin\022\333\001\n\013Cre"
          + "ateIndex\022-.google.firestore.admin.v1.Cre"
          + "ateIndexRequest\032\035.google.longrunning.Ope"
          + "ration\"~\202\323\344\223\002G\">/v1/{parent=projects/*/d"
          + "atabases/*/collectionGroups/*}/indexes:\005"
          + "index\332A\014parent,index\312A\037\n\005Index\022\026IndexOpe"
          + "rationMetadata\022\275\001\n\013ListIndexes\022-.google."
          + "firestore.admin.v1.ListIndexesRequest\032.."
          + "google.firestore.admin.v1.ListIndexesRes"
          + "ponse\"O\202\323\344\223\002@\022>/v1/{parent=projects/*/da"
          + "tabases/*/collectionGroups/*}/indexes\332A\006"
          + "parent\022\247\001\n\010GetIndex\022*.google.firestore.a"
          + "dmin.v1.GetIndexRequest\032 .google.firesto"
          + "re.admin.v1.Index\"M\202\323\344\223\002@\022>/v1/{name=pro"
          + "jects/*/databases/*/collectionGroups/*/i"
          + "ndexes/*}\332A\004name\022\243\001\n\013DeleteIndex\022-.googl"
          + "e.firestore.admin.v1.DeleteIndexRequest\032"
          + "\026.google.protobuf.Empty\"M\202\323\344\223\002@*>/v1/{na"
          + "me=projects/*/databases/*/collectionGrou"
          + "ps/*/indexes/*}\332A\004name\022\246\001\n\010GetField\022*.go"
          + "ogle.firestore.admin.v1.GetFieldRequest\032"
          + " .google.firestore.admin.v1.Field\"L\202\323\344\223\002"
          + "?\022=/v1/{name=projects/*/databases/*/coll"
          + "ectionGroups/*/fields/*}\332A\004name\022\331\001\n\013Upda"
          + "teField\022-.google.firestore.admin.v1.Upda"
          + "teFieldRequest\032\035.google.longrunning.Oper"
          + "ation\"|\202\323\344\223\002L2C/v1/{field.name=projects/"
          + "*/databases/*/collectionGroups/*/fields/"
          + "*}:\005field\332A\005field\312A\037\n\005Field\022\026FieldOperat"
          + "ionMetadata\022\271\001\n\nListFields\022,.google.fire"
          + "store.admin.v1.ListFieldsRequest\032-.googl"
          + "e.firestore.admin.v1.ListFieldsResponse\""
          + "N\202\323\344\223\002?\022=/v1/{parent=projects/*/database"
          + "s/*/collectionGroups/*}/fields\332A\006parent\022"
          + "\335\001\n\017ExportDocuments\0221.google.firestore.a"
          + "dmin.v1.ExportDocumentsRequest\032\035.google."
          + "longrunning.Operation\"x\202\323\344\223\0026\"1/v1/{name"
          + "=projects/*/databases/*}:exportDocuments"
          + ":\001*\332A\004name\312A2\n\027ExportDocumentsResponse\022\027"
          + "ExportDocumentsMetadata\022\333\001\n\017ImportDocume"
          + "nts\0221.google.firestore.admin.v1.ImportDo"
          + "cumentsRequest\032\035.google.longrunning.Oper"
          + "ation\"v\202\323\344\223\0026\"1/v1/{name=projects/*/data"
          + "bases/*}:importDocuments:\001*\332A\004name\312A0\n\025g"
          + "oogle.protobuf.Empty\022\027ImportDocumentsMet"
          + "adata\022\223\001\n\013GetDatabase\022-.google.firestore"
          + ".admin.v1.GetDatabaseRequest\032#.google.fi"
          + "restore.admin.v1.Database\"0\202\323\344\223\002#\022!/v1/{"
          + "name=projects/*/databases/*}\332A\004name\022\246\001\n\r"
          + "ListDatabases\022/.google.firestore.admin.v"
          + "1.ListDatabasesRequest\0320.google.firestor"
          + "e.admin.v1.ListDatabasesResponse\"2\202\323\344\223\002#"
          + "\022!/v1/{parent=projects/*}/databases\332A\006pa"
          + "rent\022\333\001\n\016UpdateDatabase\0220.google.firesto"
          + "re.admin.v1.UpdateDatabaseRequest\032\035.goog"
          + "le.longrunning.Operation\"x\202\323\344\223\00262*/v1/{d"
          + "atabase.name=projects/*/databases/*}:\010da"
          + "tabase\332A\024database,update_mask\312A\"\n\010Databa"
          + "se\022\026UpdateDatabaseMetadata\032v\312A\030firestore"
          + ".googleapis.com\322AXhttps://www.googleapis"
          + ".com/auth/cloud-platform,https://www.goo"
          + "gleapis.com/auth/datastoreB\252\003\n\035com.googl"
          + "e.firestore.admin.v1B\023FirestoreAdminProt"
          + "oP\001Z>google.golang.org/genproto/googleap"
          + "is/firestore/admin/v1;admin\242\002\004GCFS\252\002\037Goo"
          + "gle.Cloud.Firestore.Admin.V1\312\002\037Google\\Cl"
          + "oud\\Firestore\\Admin\\V1\352\002#Google::Cloud::"
          + "Firestore::Admin::V1\352AL\n!firestore.googl"
          + "eapis.com/Location\022\'projects/{project}/l"
          + "ocations/{location}\352Aq\n(firestore.google"
          + "apis.com/CollectionGroup\022Eprojects/{proj"
          + "ect}/databases/{database}/collectionGrou"
          + "ps/{collection}b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.api.AnnotationsProto.getDescriptor(),
              com.google.api.ClientProto.getDescriptor(),
              com.google.api.FieldBehaviorProto.getDescriptor(),
              com.google.api.ResourceProto.getDescriptor(),
              com.google.firestore.admin.v1.DatabaseProto.getDescriptor(),
              com.google.firestore.admin.v1.FieldProto.getDescriptor(),
              com.google.firestore.admin.v1.IndexProto.getDescriptor(),
              com.google.firestore.admin.v1.OperationProto.getDescriptor(),
              com.google.longrunning.OperationsProto.getDescriptor(),
              com.google.protobuf.EmptyProto.getDescriptor(),
              com.google.protobuf.FieldMaskProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
            });
    internal_static_google_firestore_admin_v1_ListDatabasesRequest_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_firestore_admin_v1_ListDatabasesRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_ListDatabasesRequest_descriptor,
            new java.lang.String[] {
              "Parent",
            });
    internal_static_google_firestore_admin_v1_ListDatabasesResponse_descriptor =
        getDescriptor().getMessageTypes().get(1);
    internal_static_google_firestore_admin_v1_ListDatabasesResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_ListDatabasesResponse_descriptor,
            new java.lang.String[] {
              "Databases",
            });
    internal_static_google_firestore_admin_v1_GetDatabaseRequest_descriptor =
        getDescriptor().getMessageTypes().get(2);
    internal_static_google_firestore_admin_v1_GetDatabaseRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_GetDatabaseRequest_descriptor,
            new java.lang.String[] {
              "Name",
            });
    internal_static_google_firestore_admin_v1_UpdateDatabaseRequest_descriptor =
        getDescriptor().getMessageTypes().get(3);
    internal_static_google_firestore_admin_v1_UpdateDatabaseRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_UpdateDatabaseRequest_descriptor,
            new java.lang.String[] {
              "Database", "UpdateMask",
            });
    internal_static_google_firestore_admin_v1_UpdateDatabaseMetadata_descriptor =
        getDescriptor().getMessageTypes().get(4);
    internal_static_google_firestore_admin_v1_UpdateDatabaseMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_UpdateDatabaseMetadata_descriptor,
            new java.lang.String[] {});
    internal_static_google_firestore_admin_v1_CreateIndexRequest_descriptor =
        getDescriptor().getMessageTypes().get(5);
    internal_static_google_firestore_admin_v1_CreateIndexRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_CreateIndexRequest_descriptor,
            new java.lang.String[] {
              "Parent", "Index",
            });
    internal_static_google_firestore_admin_v1_ListIndexesRequest_descriptor =
        getDescriptor().getMessageTypes().get(6);
    internal_static_google_firestore_admin_v1_ListIndexesRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_ListIndexesRequest_descriptor,
            new java.lang.String[] {
              "Parent", "Filter", "PageSize", "PageToken",
            });
    internal_static_google_firestore_admin_v1_ListIndexesResponse_descriptor =
        getDescriptor().getMessageTypes().get(7);
    internal_static_google_firestore_admin_v1_ListIndexesResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_ListIndexesResponse_descriptor,
            new java.lang.String[] {
              "Indexes", "NextPageToken",
            });
    internal_static_google_firestore_admin_v1_GetIndexRequest_descriptor =
        getDescriptor().getMessageTypes().get(8);
    internal_static_google_firestore_admin_v1_GetIndexRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_GetIndexRequest_descriptor,
            new java.lang.String[] {
              "Name",
            });
    internal_static_google_firestore_admin_v1_DeleteIndexRequest_descriptor =
        getDescriptor().getMessageTypes().get(9);
    internal_static_google_firestore_admin_v1_DeleteIndexRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_DeleteIndexRequest_descriptor,
            new java.lang.String[] {
              "Name",
            });
    internal_static_google_firestore_admin_v1_UpdateFieldRequest_descriptor =
        getDescriptor().getMessageTypes().get(10);
    internal_static_google_firestore_admin_v1_UpdateFieldRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_UpdateFieldRequest_descriptor,
            new java.lang.String[] {
              "Field", "UpdateMask",
            });
    internal_static_google_firestore_admin_v1_GetFieldRequest_descriptor =
        getDescriptor().getMessageTypes().get(11);
    internal_static_google_firestore_admin_v1_GetFieldRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_GetFieldRequest_descriptor,
            new java.lang.String[] {
              "Name",
            });
    internal_static_google_firestore_admin_v1_ListFieldsRequest_descriptor =
        getDescriptor().getMessageTypes().get(12);
    internal_static_google_firestore_admin_v1_ListFieldsRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_ListFieldsRequest_descriptor,
            new java.lang.String[] {
              "Parent", "Filter", "PageSize", "PageToken",
            });
    internal_static_google_firestore_admin_v1_ListFieldsResponse_descriptor =
        getDescriptor().getMessageTypes().get(13);
    internal_static_google_firestore_admin_v1_ListFieldsResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_ListFieldsResponse_descriptor,
            new java.lang.String[] {
              "Fields", "NextPageToken",
            });
    internal_static_google_firestore_admin_v1_ExportDocumentsRequest_descriptor =
        getDescriptor().getMessageTypes().get(14);
    internal_static_google_firestore_admin_v1_ExportDocumentsRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_ExportDocumentsRequest_descriptor,
            new java.lang.String[] {
              "Name", "CollectionIds", "OutputUriPrefix",
            });
    internal_static_google_firestore_admin_v1_ImportDocumentsRequest_descriptor =
        getDescriptor().getMessageTypes().get(15);
    internal_static_google_firestore_admin_v1_ImportDocumentsRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_firestore_admin_v1_ImportDocumentsRequest_descriptor,
            new java.lang.String[] {
              "Name", "CollectionIds", "InputUriPrefix",
            });
    com.google.protobuf.ExtensionRegistry registry =
        com.google.protobuf.ExtensionRegistry.newInstance();
    registry.add(com.google.api.ClientProto.defaultHost);
    registry.add(com.google.api.FieldBehaviorProto.fieldBehavior);
    registry.add(com.google.api.AnnotationsProto.http);
    registry.add(com.google.api.ClientProto.methodSignature);
    registry.add(com.google.api.ClientProto.oauthScopes);
    registry.add(com.google.api.ResourceProto.resourceDefinition);
    registry.add(com.google.api.ResourceProto.resourceReference);
    registry.add(com.google.longrunning.OperationsProto.operationInfo);
    com.google.protobuf.Descriptors.FileDescriptor.internalUpdateFileDescriptor(
        descriptor, registry);
    com.google.api.AnnotationsProto.getDescriptor();
    com.google.api.ClientProto.getDescriptor();
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.api.ResourceProto.getDescriptor();
    com.google.firestore.admin.v1.DatabaseProto.getDescriptor();
    com.google.firestore.admin.v1.FieldProto.getDescriptor();
    com.google.firestore.admin.v1.IndexProto.getDescriptor();
    com.google.firestore.admin.v1.OperationProto.getDescriptor();
    com.google.longrunning.OperationsProto.getDescriptor();
    com.google.protobuf.EmptyProto.getDescriptor();
    com.google.protobuf.FieldMaskProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
