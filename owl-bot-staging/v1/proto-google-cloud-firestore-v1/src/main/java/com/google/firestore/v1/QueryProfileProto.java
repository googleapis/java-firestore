// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/firestore/v1/query_profile.proto

// Protobuf Java Version: 3.25.2
package com.google.firestore.v1;

public final class QueryProfileProto {
  private QueryProfileProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_ExplainOptions_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_ExplainOptions_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_ExplainMetrics_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_ExplainMetrics_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_PlanSummary_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_PlanSummary_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_ExecutionStats_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_ExecutionStats_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\'google/firestore/v1/query_profile.prot" +
      "o\022\023google.firestore.v1\032\037google/api/field" +
      "_behavior.proto\032\036google/protobuf/duratio" +
      "n.proto\032\034google/protobuf/struct.proto\"&\n" +
      "\016ExplainOptions\022\024\n\007analyze\030\001 \001(\010B\003\340A\001\"\206\001" +
      "\n\016ExplainMetrics\0226\n\014plan_summary\030\001 \001(\0132 " +
      ".google.firestore.v1.PlanSummary\022<\n\017exec" +
      "ution_stats\030\002 \001(\0132#.google.firestore.v1." +
      "ExecutionStats\"<\n\013PlanSummary\022-\n\014indexes" +
      "_used\030\001 \003(\0132\027.google.protobuf.Struct\"\250\001\n" +
      "\016ExecutionStats\022\030\n\020results_returned\030\001 \001(" +
      "\003\0225\n\022execution_duration\030\003 \001(\0132\031.google.p" +
      "rotobuf.Duration\022\027\n\017read_operations\030\004 \001(" +
      "\003\022,\n\013debug_stats\030\005 \001(\0132\027.google.protobuf" +
      ".StructB\311\001\n\027com.google.firestore.v1B\021Que" +
      "ryProfileProtoP\001Z;cloud.google.com/go/fi" +
      "restore/apiv1/firestorepb;firestorepb\242\002\004" +
      "GCFS\252\002\031Google.Cloud.Firestore.V1\312\002\031Googl" +
      "e\\Cloud\\Firestore\\V1\352\002\034Google::Cloud::Fi" +
      "restore::V1b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.api.FieldBehaviorProto.getDescriptor(),
          com.google.protobuf.DurationProto.getDescriptor(),
          com.google.protobuf.StructProto.getDescriptor(),
        });
    internal_static_google_firestore_v1_ExplainOptions_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_google_firestore_v1_ExplainOptions_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_ExplainOptions_descriptor,
        new java.lang.String[] { "Analyze", });
    internal_static_google_firestore_v1_ExplainMetrics_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_google_firestore_v1_ExplainMetrics_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_ExplainMetrics_descriptor,
        new java.lang.String[] { "PlanSummary", "ExecutionStats", });
    internal_static_google_firestore_v1_PlanSummary_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_google_firestore_v1_PlanSummary_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_PlanSummary_descriptor,
        new java.lang.String[] { "IndexesUsed", });
    internal_static_google_firestore_v1_ExecutionStats_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_google_firestore_v1_ExecutionStats_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_ExecutionStats_descriptor,
        new java.lang.String[] { "ResultsReturned", "ExecutionDuration", "ReadOperations", "DebugStats", });
    com.google.protobuf.ExtensionRegistry registry =
        com.google.protobuf.ExtensionRegistry.newInstance();
    registry.add(com.google.api.FieldBehaviorProto.fieldBehavior);
    com.google.protobuf.Descriptors.FileDescriptor
        .internalUpdateFileDescriptor(descriptor, registry);
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.protobuf.DurationProto.getDescriptor();
    com.google.protobuf.StructProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
