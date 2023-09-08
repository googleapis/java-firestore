// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/firestore/v1/query.proto

package com.google.firestore.v1;

public final class QueryProto {
  private QueryProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredQuery_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredQuery_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredQuery_CollectionSelector_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredQuery_CollectionSelector_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredQuery_Filter_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredQuery_Filter_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredQuery_CompositeFilter_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredQuery_CompositeFilter_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredQuery_FieldFilter_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredQuery_FieldFilter_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredQuery_UnaryFilter_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredQuery_UnaryFilter_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredQuery_Order_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredQuery_Order_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredQuery_FieldReference_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredQuery_FieldReference_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredQuery_Projection_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredQuery_Projection_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredAggregationQuery_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredAggregationQuery_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Count_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Count_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Sum_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Sum_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Avg_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Avg_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_firestore_v1_Cursor_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_firestore_v1_Cursor_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\037google/firestore/v1/query.proto\022\023googl" +
      "e.firestore.v1\032\037google/api/field_behavio" +
      "r.proto\032\"google/firestore/v1/document.pr" +
      "oto\032\036google/protobuf/wrappers.proto\"\276\017\n\017" +
      "StructuredQuery\022?\n\006select\030\001 \001(\0132/.google" +
      ".firestore.v1.StructuredQuery.Projection" +
      "\022E\n\004from\030\002 \003(\01327.google.firestore.v1.Str" +
      "ucturedQuery.CollectionSelector\022:\n\005where" +
      "\030\003 \001(\0132+.google.firestore.v1.StructuredQ" +
      "uery.Filter\022<\n\010order_by\030\004 \003(\0132*.google.f" +
      "irestore.v1.StructuredQuery.Order\022-\n\010sta" +
      "rt_at\030\007 \001(\0132\033.google.firestore.v1.Cursor" +
      "\022+\n\006end_at\030\010 \001(\0132\033.google.firestore.v1.C" +
      "ursor\022\016\n\006offset\030\006 \001(\005\022*\n\005limit\030\005 \001(\0132\033.g" +
      "oogle.protobuf.Int32Value\032D\n\022CollectionS" +
      "elector\022\025\n\rcollection_id\030\002 \001(\t\022\027\n\017all_de" +
      "scendants\030\003 \001(\010\032\375\001\n\006Filter\022P\n\020composite_" +
      "filter\030\001 \001(\01324.google.firestore.v1.Struc" +
      "turedQuery.CompositeFilterH\000\022H\n\014field_fi" +
      "lter\030\002 \001(\01320.google.firestore.v1.Structu" +
      "redQuery.FieldFilterH\000\022H\n\014unary_filter\030\003" +
      " \001(\01320.google.firestore.v1.StructuredQue" +
      "ry.UnaryFilterH\000B\r\n\013filter_type\032\321\001\n\017Comp" +
      "ositeFilter\022I\n\002op\030\001 \001(\0162=.google.firesto" +
      "re.v1.StructuredQuery.CompositeFilter.Op" +
      "erator\022<\n\007filters\030\002 \003(\0132+.google.firesto" +
      "re.v1.StructuredQuery.Filter\"5\n\010Operator" +
      "\022\030\n\024OPERATOR_UNSPECIFIED\020\000\022\007\n\003AND\020\001\022\006\n\002O" +
      "R\020\002\032\230\003\n\013FieldFilter\022B\n\005field\030\001 \001(\01323.goo" +
      "gle.firestore.v1.StructuredQuery.FieldRe" +
      "ference\022E\n\002op\030\002 \001(\01629.google.firestore.v" +
      "1.StructuredQuery.FieldFilter.Operator\022)" +
      "\n\005value\030\003 \001(\0132\032.google.firestore.v1.Valu" +
      "e\"\322\001\n\010Operator\022\030\n\024OPERATOR_UNSPECIFIED\020\000" +
      "\022\r\n\tLESS_THAN\020\001\022\026\n\022LESS_THAN_OR_EQUAL\020\002\022" +
      "\020\n\014GREATER_THAN\020\003\022\031\n\025GREATER_THAN_OR_EQU" +
      "AL\020\004\022\t\n\005EQUAL\020\005\022\r\n\tNOT_EQUAL\020\006\022\022\n\016ARRAY_" +
      "CONTAINS\020\007\022\006\n\002IN\020\010\022\026\n\022ARRAY_CONTAINS_ANY" +
      "\020\t\022\n\n\006NOT_IN\020\n\032\212\002\n\013UnaryFilter\022E\n\002op\030\001 \001" +
      "(\01629.google.firestore.v1.StructuredQuery" +
      ".UnaryFilter.Operator\022D\n\005field\030\002 \001(\01323.g" +
      "oogle.firestore.v1.StructuredQuery.Field" +
      "ReferenceH\000\"^\n\010Operator\022\030\n\024OPERATOR_UNSP" +
      "ECIFIED\020\000\022\n\n\006IS_NAN\020\002\022\013\n\007IS_NULL\020\003\022\016\n\nIS" +
      "_NOT_NAN\020\004\022\017\n\013IS_NOT_NULL\020\005B\016\n\014operand_t" +
      "ype\032\216\001\n\005Order\022B\n\005field\030\001 \001(\01323.google.fi" +
      "restore.v1.StructuredQuery.FieldReferenc" +
      "e\022A\n\tdirection\030\002 \001(\0162..google.firestore." +
      "v1.StructuredQuery.Direction\032$\n\016FieldRef" +
      "erence\022\022\n\nfield_path\030\002 \001(\t\032Q\n\nProjection" +
      "\022C\n\006fields\030\002 \003(\01323.google.firestore.v1.S" +
      "tructuredQuery.FieldReference\"E\n\tDirecti" +
      "on\022\031\n\025DIRECTION_UNSPECIFIED\020\000\022\r\n\tASCENDI" +
      "NG\020\001\022\016\n\nDESCENDING\020\002\"\273\005\n\032StructuredAggre" +
      "gationQuery\022@\n\020structured_query\030\001 \001(\0132$." +
      "google.firestore.v1.StructuredQueryH\000\022W\n" +
      "\014aggregations\030\003 \003(\0132;.google.firestore.v" +
      "1.StructuredAggregationQuery.Aggregation" +
      "B\004\342A\001\001\032\363\003\n\013Aggregation\022R\n\005count\030\001 \001(\0132A." +
      "google.firestore.v1.StructuredAggregatio" +
      "nQuery.Aggregation.CountH\000\022N\n\003sum\030\002 \001(\0132" +
      "?.google.firestore.v1.StructuredAggregat" +
      "ionQuery.Aggregation.SumH\000\022N\n\003avg\030\003 \001(\0132" +
      "?.google.firestore.v1.StructuredAggregat" +
      "ionQuery.Aggregation.AvgH\000\022\023\n\005alias\030\007 \001(" +
      "\tB\004\342A\001\001\0329\n\005Count\0220\n\005up_to\030\001 \001(\0132\033.google" +
      ".protobuf.Int64ValueB\004\342A\001\001\032I\n\003Sum\022B\n\005fie" +
      "ld\030\001 \001(\01323.google.firestore.v1.Structure" +
      "dQuery.FieldReference\032I\n\003Avg\022B\n\005field\030\001 " +
      "\001(\01323.google.firestore.v1.StructuredQuer" +
      "y.FieldReferenceB\n\n\010operatorB\014\n\nquery_ty" +
      "pe\"D\n\006Cursor\022*\n\006values\030\001 \003(\0132\032.google.fi" +
      "restore.v1.Value\022\016\n\006before\030\002 \001(\010B\302\001\n\027com" +
      ".google.firestore.v1B\nQueryProtoP\001Z;clou" +
      "d.google.com/go/firestore/apiv1/firestor" +
      "epb;firestorepb\242\002\004GCFS\252\002\031Google.Cloud.Fi" +
      "restore.V1\312\002\031Google\\Cloud\\Firestore\\V1\352\002" +
      "\034Google::Cloud::Firestore::V1b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.api.FieldBehaviorProto.getDescriptor(),
          com.google.firestore.v1.DocumentProto.getDescriptor(),
          com.google.protobuf.WrappersProto.getDescriptor(),
        });
    internal_static_google_firestore_v1_StructuredQuery_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_google_firestore_v1_StructuredQuery_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredQuery_descriptor,
        new java.lang.String[] { "Select", "From", "Where", "OrderBy", "StartAt", "EndAt", "Offset", "Limit", });
    internal_static_google_firestore_v1_StructuredQuery_CollectionSelector_descriptor =
      internal_static_google_firestore_v1_StructuredQuery_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_v1_StructuredQuery_CollectionSelector_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredQuery_CollectionSelector_descriptor,
        new java.lang.String[] { "CollectionId", "AllDescendants", });
    internal_static_google_firestore_v1_StructuredQuery_Filter_descriptor =
      internal_static_google_firestore_v1_StructuredQuery_descriptor.getNestedTypes().get(1);
    internal_static_google_firestore_v1_StructuredQuery_Filter_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredQuery_Filter_descriptor,
        new java.lang.String[] { "CompositeFilter", "FieldFilter", "UnaryFilter", "FilterType", });
    internal_static_google_firestore_v1_StructuredQuery_CompositeFilter_descriptor =
      internal_static_google_firestore_v1_StructuredQuery_descriptor.getNestedTypes().get(2);
    internal_static_google_firestore_v1_StructuredQuery_CompositeFilter_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredQuery_CompositeFilter_descriptor,
        new java.lang.String[] { "Op", "Filters", });
    internal_static_google_firestore_v1_StructuredQuery_FieldFilter_descriptor =
      internal_static_google_firestore_v1_StructuredQuery_descriptor.getNestedTypes().get(3);
    internal_static_google_firestore_v1_StructuredQuery_FieldFilter_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredQuery_FieldFilter_descriptor,
        new java.lang.String[] { "Field", "Op", "Value", });
    internal_static_google_firestore_v1_StructuredQuery_UnaryFilter_descriptor =
      internal_static_google_firestore_v1_StructuredQuery_descriptor.getNestedTypes().get(4);
    internal_static_google_firestore_v1_StructuredQuery_UnaryFilter_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredQuery_UnaryFilter_descriptor,
        new java.lang.String[] { "Op", "Field", "OperandType", });
    internal_static_google_firestore_v1_StructuredQuery_Order_descriptor =
      internal_static_google_firestore_v1_StructuredQuery_descriptor.getNestedTypes().get(5);
    internal_static_google_firestore_v1_StructuredQuery_Order_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredQuery_Order_descriptor,
        new java.lang.String[] { "Field", "Direction", });
    internal_static_google_firestore_v1_StructuredQuery_FieldReference_descriptor =
      internal_static_google_firestore_v1_StructuredQuery_descriptor.getNestedTypes().get(6);
    internal_static_google_firestore_v1_StructuredQuery_FieldReference_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredQuery_FieldReference_descriptor,
        new java.lang.String[] { "FieldPath", });
    internal_static_google_firestore_v1_StructuredQuery_Projection_descriptor =
      internal_static_google_firestore_v1_StructuredQuery_descriptor.getNestedTypes().get(7);
    internal_static_google_firestore_v1_StructuredQuery_Projection_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredQuery_Projection_descriptor,
        new java.lang.String[] { "Fields", });
    internal_static_google_firestore_v1_StructuredAggregationQuery_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_google_firestore_v1_StructuredAggregationQuery_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredAggregationQuery_descriptor,
        new java.lang.String[] { "StructuredQuery", "Aggregations", "QueryType", });
    internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_descriptor =
      internal_static_google_firestore_v1_StructuredAggregationQuery_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_descriptor,
        new java.lang.String[] { "Count", "Sum", "Avg", "Alias", "Operator", });
    internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Count_descriptor =
      internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_descriptor.getNestedTypes().get(0);
    internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Count_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Count_descriptor,
        new java.lang.String[] { "UpTo", });
    internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Sum_descriptor =
      internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_descriptor.getNestedTypes().get(1);
    internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Sum_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Sum_descriptor,
        new java.lang.String[] { "Field", });
    internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Avg_descriptor =
      internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_descriptor.getNestedTypes().get(2);
    internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Avg_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_StructuredAggregationQuery_Aggregation_Avg_descriptor,
        new java.lang.String[] { "Field", });
    internal_static_google_firestore_v1_Cursor_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_google_firestore_v1_Cursor_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_firestore_v1_Cursor_descriptor,
        new java.lang.String[] { "Values", "Before", });
    com.google.protobuf.ExtensionRegistry registry =
        com.google.protobuf.ExtensionRegistry.newInstance();
    registry.add(com.google.api.FieldBehaviorProto.fieldBehavior);
    com.google.protobuf.Descriptors.FileDescriptor
        .internalUpdateFileDescriptor(descriptor, registry);
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.firestore.v1.DocumentProto.getDescriptor();
    com.google.protobuf.WrappersProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
