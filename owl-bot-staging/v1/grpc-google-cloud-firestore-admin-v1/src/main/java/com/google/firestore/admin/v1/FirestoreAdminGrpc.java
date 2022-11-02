package com.google.firestore.admin.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * The Cloud Firestore Admin API.
 * This API provides several administrative services for Cloud Firestore.
 * Project, Database, Namespace, Collection, Collection Group, and Document are
 * used as defined in the Google Cloud Firestore API.
 * Operation: An Operation represents work being performed in the background.
 * The index service manages Cloud Firestore indexes.
 * Index creation is performed asynchronously.
 * An Operation resource is created for each such asynchronous operation.
 * The state of the operation (including any errors encountered)
 * may be queried via the Operation resource.
 * The Operations collection provides a record of actions performed for the
 * specified Project (including any Operations in progress). Operations are not
 * created directly but through calls on other collections or resources.
 * An Operation that is done may be deleted so that it is no longer listed as
 * part of the Operation collection. Operations are garbage collected after
 * 30 days. By default, ListOperations will only return in progress and failed
 * operations. To list completed operation, issue a ListOperations request with
 * the filter `done: true`.
 * Operations are created by service `FirestoreAdmin`, but are accessed via
 * service `google.longrunning.Operations`.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler",
    comments = "Source: google/firestore/admin/v1/firestore_admin.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class FirestoreAdminGrpc {

  private FirestoreAdminGrpc() {}

  public static final String SERVICE_NAME = "google.firestore.admin.v1.FirestoreAdmin";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.google.firestore.admin.v1.CreateIndexRequest,
      com.google.longrunning.Operation> getCreateIndexMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateIndex",
      requestType = com.google.firestore.admin.v1.CreateIndexRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.firestore.admin.v1.CreateIndexRequest,
      com.google.longrunning.Operation> getCreateIndexMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.admin.v1.CreateIndexRequest, com.google.longrunning.Operation> getCreateIndexMethod;
    if ((getCreateIndexMethod = FirestoreAdminGrpc.getCreateIndexMethod) == null) {
      synchronized (FirestoreAdminGrpc.class) {
        if ((getCreateIndexMethod = FirestoreAdminGrpc.getCreateIndexMethod) == null) {
          FirestoreAdminGrpc.getCreateIndexMethod = getCreateIndexMethod =
              io.grpc.MethodDescriptor.<com.google.firestore.admin.v1.CreateIndexRequest, com.google.longrunning.Operation>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateIndex"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.CreateIndexRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.longrunning.Operation.getDefaultInstance()))
              .setSchemaDescriptor(new FirestoreAdminMethodDescriptorSupplier("CreateIndex"))
              .build();
        }
      }
    }
    return getCreateIndexMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ListIndexesRequest,
      com.google.firestore.admin.v1.ListIndexesResponse> getListIndexesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListIndexes",
      requestType = com.google.firestore.admin.v1.ListIndexesRequest.class,
      responseType = com.google.firestore.admin.v1.ListIndexesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ListIndexesRequest,
      com.google.firestore.admin.v1.ListIndexesResponse> getListIndexesMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ListIndexesRequest, com.google.firestore.admin.v1.ListIndexesResponse> getListIndexesMethod;
    if ((getListIndexesMethod = FirestoreAdminGrpc.getListIndexesMethod) == null) {
      synchronized (FirestoreAdminGrpc.class) {
        if ((getListIndexesMethod = FirestoreAdminGrpc.getListIndexesMethod) == null) {
          FirestoreAdminGrpc.getListIndexesMethod = getListIndexesMethod =
              io.grpc.MethodDescriptor.<com.google.firestore.admin.v1.ListIndexesRequest, com.google.firestore.admin.v1.ListIndexesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListIndexes"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.ListIndexesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.ListIndexesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FirestoreAdminMethodDescriptorSupplier("ListIndexes"))
              .build();
        }
      }
    }
    return getListIndexesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.firestore.admin.v1.GetIndexRequest,
      com.google.firestore.admin.v1.Index> getGetIndexMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetIndex",
      requestType = com.google.firestore.admin.v1.GetIndexRequest.class,
      responseType = com.google.firestore.admin.v1.Index.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.firestore.admin.v1.GetIndexRequest,
      com.google.firestore.admin.v1.Index> getGetIndexMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.admin.v1.GetIndexRequest, com.google.firestore.admin.v1.Index> getGetIndexMethod;
    if ((getGetIndexMethod = FirestoreAdminGrpc.getGetIndexMethod) == null) {
      synchronized (FirestoreAdminGrpc.class) {
        if ((getGetIndexMethod = FirestoreAdminGrpc.getGetIndexMethod) == null) {
          FirestoreAdminGrpc.getGetIndexMethod = getGetIndexMethod =
              io.grpc.MethodDescriptor.<com.google.firestore.admin.v1.GetIndexRequest, com.google.firestore.admin.v1.Index>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetIndex"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.GetIndexRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.Index.getDefaultInstance()))
              .setSchemaDescriptor(new FirestoreAdminMethodDescriptorSupplier("GetIndex"))
              .build();
        }
      }
    }
    return getGetIndexMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.firestore.admin.v1.DeleteIndexRequest,
      com.google.protobuf.Empty> getDeleteIndexMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteIndex",
      requestType = com.google.firestore.admin.v1.DeleteIndexRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.firestore.admin.v1.DeleteIndexRequest,
      com.google.protobuf.Empty> getDeleteIndexMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.admin.v1.DeleteIndexRequest, com.google.protobuf.Empty> getDeleteIndexMethod;
    if ((getDeleteIndexMethod = FirestoreAdminGrpc.getDeleteIndexMethod) == null) {
      synchronized (FirestoreAdminGrpc.class) {
        if ((getDeleteIndexMethod = FirestoreAdminGrpc.getDeleteIndexMethod) == null) {
          FirestoreAdminGrpc.getDeleteIndexMethod = getDeleteIndexMethod =
              io.grpc.MethodDescriptor.<com.google.firestore.admin.v1.DeleteIndexRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteIndex"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.DeleteIndexRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new FirestoreAdminMethodDescriptorSupplier("DeleteIndex"))
              .build();
        }
      }
    }
    return getDeleteIndexMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.firestore.admin.v1.GetFieldRequest,
      com.google.firestore.admin.v1.Field> getGetFieldMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetField",
      requestType = com.google.firestore.admin.v1.GetFieldRequest.class,
      responseType = com.google.firestore.admin.v1.Field.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.firestore.admin.v1.GetFieldRequest,
      com.google.firestore.admin.v1.Field> getGetFieldMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.admin.v1.GetFieldRequest, com.google.firestore.admin.v1.Field> getGetFieldMethod;
    if ((getGetFieldMethod = FirestoreAdminGrpc.getGetFieldMethod) == null) {
      synchronized (FirestoreAdminGrpc.class) {
        if ((getGetFieldMethod = FirestoreAdminGrpc.getGetFieldMethod) == null) {
          FirestoreAdminGrpc.getGetFieldMethod = getGetFieldMethod =
              io.grpc.MethodDescriptor.<com.google.firestore.admin.v1.GetFieldRequest, com.google.firestore.admin.v1.Field>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetField"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.GetFieldRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.Field.getDefaultInstance()))
              .setSchemaDescriptor(new FirestoreAdminMethodDescriptorSupplier("GetField"))
              .build();
        }
      }
    }
    return getGetFieldMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.firestore.admin.v1.UpdateFieldRequest,
      com.google.longrunning.Operation> getUpdateFieldMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateField",
      requestType = com.google.firestore.admin.v1.UpdateFieldRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.firestore.admin.v1.UpdateFieldRequest,
      com.google.longrunning.Operation> getUpdateFieldMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.admin.v1.UpdateFieldRequest, com.google.longrunning.Operation> getUpdateFieldMethod;
    if ((getUpdateFieldMethod = FirestoreAdminGrpc.getUpdateFieldMethod) == null) {
      synchronized (FirestoreAdminGrpc.class) {
        if ((getUpdateFieldMethod = FirestoreAdminGrpc.getUpdateFieldMethod) == null) {
          FirestoreAdminGrpc.getUpdateFieldMethod = getUpdateFieldMethod =
              io.grpc.MethodDescriptor.<com.google.firestore.admin.v1.UpdateFieldRequest, com.google.longrunning.Operation>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateField"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.UpdateFieldRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.longrunning.Operation.getDefaultInstance()))
              .setSchemaDescriptor(new FirestoreAdminMethodDescriptorSupplier("UpdateField"))
              .build();
        }
      }
    }
    return getUpdateFieldMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ListFieldsRequest,
      com.google.firestore.admin.v1.ListFieldsResponse> getListFieldsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListFields",
      requestType = com.google.firestore.admin.v1.ListFieldsRequest.class,
      responseType = com.google.firestore.admin.v1.ListFieldsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ListFieldsRequest,
      com.google.firestore.admin.v1.ListFieldsResponse> getListFieldsMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ListFieldsRequest, com.google.firestore.admin.v1.ListFieldsResponse> getListFieldsMethod;
    if ((getListFieldsMethod = FirestoreAdminGrpc.getListFieldsMethod) == null) {
      synchronized (FirestoreAdminGrpc.class) {
        if ((getListFieldsMethod = FirestoreAdminGrpc.getListFieldsMethod) == null) {
          FirestoreAdminGrpc.getListFieldsMethod = getListFieldsMethod =
              io.grpc.MethodDescriptor.<com.google.firestore.admin.v1.ListFieldsRequest, com.google.firestore.admin.v1.ListFieldsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListFields"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.ListFieldsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.ListFieldsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FirestoreAdminMethodDescriptorSupplier("ListFields"))
              .build();
        }
      }
    }
    return getListFieldsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ExportDocumentsRequest,
      com.google.longrunning.Operation> getExportDocumentsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ExportDocuments",
      requestType = com.google.firestore.admin.v1.ExportDocumentsRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ExportDocumentsRequest,
      com.google.longrunning.Operation> getExportDocumentsMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ExportDocumentsRequest, com.google.longrunning.Operation> getExportDocumentsMethod;
    if ((getExportDocumentsMethod = FirestoreAdminGrpc.getExportDocumentsMethod) == null) {
      synchronized (FirestoreAdminGrpc.class) {
        if ((getExportDocumentsMethod = FirestoreAdminGrpc.getExportDocumentsMethod) == null) {
          FirestoreAdminGrpc.getExportDocumentsMethod = getExportDocumentsMethod =
              io.grpc.MethodDescriptor.<com.google.firestore.admin.v1.ExportDocumentsRequest, com.google.longrunning.Operation>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ExportDocuments"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.ExportDocumentsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.longrunning.Operation.getDefaultInstance()))
              .setSchemaDescriptor(new FirestoreAdminMethodDescriptorSupplier("ExportDocuments"))
              .build();
        }
      }
    }
    return getExportDocumentsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ImportDocumentsRequest,
      com.google.longrunning.Operation> getImportDocumentsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ImportDocuments",
      requestType = com.google.firestore.admin.v1.ImportDocumentsRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ImportDocumentsRequest,
      com.google.longrunning.Operation> getImportDocumentsMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ImportDocumentsRequest, com.google.longrunning.Operation> getImportDocumentsMethod;
    if ((getImportDocumentsMethod = FirestoreAdminGrpc.getImportDocumentsMethod) == null) {
      synchronized (FirestoreAdminGrpc.class) {
        if ((getImportDocumentsMethod = FirestoreAdminGrpc.getImportDocumentsMethod) == null) {
          FirestoreAdminGrpc.getImportDocumentsMethod = getImportDocumentsMethod =
              io.grpc.MethodDescriptor.<com.google.firestore.admin.v1.ImportDocumentsRequest, com.google.longrunning.Operation>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ImportDocuments"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.ImportDocumentsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.longrunning.Operation.getDefaultInstance()))
              .setSchemaDescriptor(new FirestoreAdminMethodDescriptorSupplier("ImportDocuments"))
              .build();
        }
      }
    }
    return getImportDocumentsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.firestore.admin.v1.GetDatabaseRequest,
      com.google.firestore.admin.v1.Database> getGetDatabaseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetDatabase",
      requestType = com.google.firestore.admin.v1.GetDatabaseRequest.class,
      responseType = com.google.firestore.admin.v1.Database.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.firestore.admin.v1.GetDatabaseRequest,
      com.google.firestore.admin.v1.Database> getGetDatabaseMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.admin.v1.GetDatabaseRequest, com.google.firestore.admin.v1.Database> getGetDatabaseMethod;
    if ((getGetDatabaseMethod = FirestoreAdminGrpc.getGetDatabaseMethod) == null) {
      synchronized (FirestoreAdminGrpc.class) {
        if ((getGetDatabaseMethod = FirestoreAdminGrpc.getGetDatabaseMethod) == null) {
          FirestoreAdminGrpc.getGetDatabaseMethod = getGetDatabaseMethod =
              io.grpc.MethodDescriptor.<com.google.firestore.admin.v1.GetDatabaseRequest, com.google.firestore.admin.v1.Database>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetDatabase"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.GetDatabaseRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.Database.getDefaultInstance()))
              .setSchemaDescriptor(new FirestoreAdminMethodDescriptorSupplier("GetDatabase"))
              .build();
        }
      }
    }
    return getGetDatabaseMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ListDatabasesRequest,
      com.google.firestore.admin.v1.ListDatabasesResponse> getListDatabasesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListDatabases",
      requestType = com.google.firestore.admin.v1.ListDatabasesRequest.class,
      responseType = com.google.firestore.admin.v1.ListDatabasesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ListDatabasesRequest,
      com.google.firestore.admin.v1.ListDatabasesResponse> getListDatabasesMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.admin.v1.ListDatabasesRequest, com.google.firestore.admin.v1.ListDatabasesResponse> getListDatabasesMethod;
    if ((getListDatabasesMethod = FirestoreAdminGrpc.getListDatabasesMethod) == null) {
      synchronized (FirestoreAdminGrpc.class) {
        if ((getListDatabasesMethod = FirestoreAdminGrpc.getListDatabasesMethod) == null) {
          FirestoreAdminGrpc.getListDatabasesMethod = getListDatabasesMethod =
              io.grpc.MethodDescriptor.<com.google.firestore.admin.v1.ListDatabasesRequest, com.google.firestore.admin.v1.ListDatabasesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListDatabases"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.ListDatabasesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.ListDatabasesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FirestoreAdminMethodDescriptorSupplier("ListDatabases"))
              .build();
        }
      }
    }
    return getListDatabasesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.firestore.admin.v1.UpdateDatabaseRequest,
      com.google.longrunning.Operation> getUpdateDatabaseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateDatabase",
      requestType = com.google.firestore.admin.v1.UpdateDatabaseRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.firestore.admin.v1.UpdateDatabaseRequest,
      com.google.longrunning.Operation> getUpdateDatabaseMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.admin.v1.UpdateDatabaseRequest, com.google.longrunning.Operation> getUpdateDatabaseMethod;
    if ((getUpdateDatabaseMethod = FirestoreAdminGrpc.getUpdateDatabaseMethod) == null) {
      synchronized (FirestoreAdminGrpc.class) {
        if ((getUpdateDatabaseMethod = FirestoreAdminGrpc.getUpdateDatabaseMethod) == null) {
          FirestoreAdminGrpc.getUpdateDatabaseMethod = getUpdateDatabaseMethod =
              io.grpc.MethodDescriptor.<com.google.firestore.admin.v1.UpdateDatabaseRequest, com.google.longrunning.Operation>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateDatabase"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.firestore.admin.v1.UpdateDatabaseRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.longrunning.Operation.getDefaultInstance()))
              .setSchemaDescriptor(new FirestoreAdminMethodDescriptorSupplier("UpdateDatabase"))
              .build();
        }
      }
    }
    return getUpdateDatabaseMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FirestoreAdminStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FirestoreAdminStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FirestoreAdminStub>() {
        @java.lang.Override
        public FirestoreAdminStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FirestoreAdminStub(channel, callOptions);
        }
      };
    return FirestoreAdminStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FirestoreAdminBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FirestoreAdminBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FirestoreAdminBlockingStub>() {
        @java.lang.Override
        public FirestoreAdminBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FirestoreAdminBlockingStub(channel, callOptions);
        }
      };
    return FirestoreAdminBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FirestoreAdminFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FirestoreAdminFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FirestoreAdminFutureStub>() {
        @java.lang.Override
        public FirestoreAdminFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FirestoreAdminFutureStub(channel, callOptions);
        }
      };
    return FirestoreAdminFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * The Cloud Firestore Admin API.
   * This API provides several administrative services for Cloud Firestore.
   * Project, Database, Namespace, Collection, Collection Group, and Document are
   * used as defined in the Google Cloud Firestore API.
   * Operation: An Operation represents work being performed in the background.
   * The index service manages Cloud Firestore indexes.
   * Index creation is performed asynchronously.
   * An Operation resource is created for each such asynchronous operation.
   * The state of the operation (including any errors encountered)
   * may be queried via the Operation resource.
   * The Operations collection provides a record of actions performed for the
   * specified Project (including any Operations in progress). Operations are not
   * created directly but through calls on other collections or resources.
   * An Operation that is done may be deleted so that it is no longer listed as
   * part of the Operation collection. Operations are garbage collected after
   * 30 days. By default, ListOperations will only return in progress and failed
   * operations. To list completed operation, issue a ListOperations request with
   * the filter `done: true`.
   * Operations are created by service `FirestoreAdmin`, but are accessed via
   * service `google.longrunning.Operations`.
   * </pre>
   */
  public static abstract class FirestoreAdminImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Creates a composite index. This returns a [google.longrunning.Operation][google.longrunning.Operation]
     * which may be used to track the status of the creation. The metadata for
     * the operation will be the type [IndexOperationMetadata][google.firestore.admin.v1.IndexOperationMetadata].
     * </pre>
     */
    public void createIndex(com.google.firestore.admin.v1.CreateIndexRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateIndexMethod(), responseObserver);
    }

    /**
     * <pre>
     * Lists composite indexes.
     * </pre>
     */
    public void listIndexes(com.google.firestore.admin.v1.ListIndexesRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.ListIndexesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListIndexesMethod(), responseObserver);
    }

    /**
     * <pre>
     * Gets a composite index.
     * </pre>
     */
    public void getIndex(com.google.firestore.admin.v1.GetIndexRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.Index> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetIndexMethod(), responseObserver);
    }

    /**
     * <pre>
     * Deletes a composite index.
     * </pre>
     */
    public void deleteIndex(com.google.firestore.admin.v1.DeleteIndexRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteIndexMethod(), responseObserver);
    }

    /**
     * <pre>
     * Gets the metadata and configuration for a Field.
     * </pre>
     */
    public void getField(com.google.firestore.admin.v1.GetFieldRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.Field> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetFieldMethod(), responseObserver);
    }

    /**
     * <pre>
     * Updates a field configuration. Currently, field updates apply only to
     * single field index configuration. However, calls to
     * [FirestoreAdmin.UpdateField][google.firestore.admin.v1.FirestoreAdmin.UpdateField] should provide a field mask to avoid
     * changing any configuration that the caller isn't aware of. The field mask
     * should be specified as: `{ paths: "index_config" }`.
     * This call returns a [google.longrunning.Operation][google.longrunning.Operation] which may be used to
     * track the status of the field update. The metadata for
     * the operation will be the type [FieldOperationMetadata][google.firestore.admin.v1.FieldOperationMetadata].
     * To configure the default field settings for the database, use
     * the special `Field` with resource name:
     * `projects/{project_id}/databases/{database_id}/collectionGroups/__default__/fields/&#42;`.
     * </pre>
     */
    public void updateField(com.google.firestore.admin.v1.UpdateFieldRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateFieldMethod(), responseObserver);
    }

    /**
     * <pre>
     * Lists the field configuration and metadata for this database.
     * Currently, [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields] only supports listing fields
     * that have been explicitly overridden. To issue this query, call
     * [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields] with the filter set to
     * `indexConfig.usesAncestorConfig:false` .
     * </pre>
     */
    public void listFields(com.google.firestore.admin.v1.ListFieldsRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.ListFieldsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListFieldsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Exports a copy of all or a subset of documents from Google Cloud Firestore
     * to another storage system, such as Google Cloud Storage. Recent updates to
     * documents may not be reflected in the export. The export occurs in the
     * background and its progress can be monitored and managed via the
     * Operation resource that is created. The output of an export may only be
     * used once the associated operation is done. If an export operation is
     * cancelled before completion it may leave partial data behind in Google
     * Cloud Storage.
     * For more details on export behavior and output format, refer to:
     * https://cloud.google.com/firestore/docs/manage-data/export-import
     * </pre>
     */
    public void exportDocuments(com.google.firestore.admin.v1.ExportDocumentsRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExportDocumentsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Imports documents into Google Cloud Firestore. Existing documents with the
     * same name are overwritten. The import occurs in the background and its
     * progress can be monitored and managed via the Operation resource that is
     * created. If an ImportDocuments operation is cancelled, it is possible
     * that a subset of the data has already been imported to Cloud Firestore.
     * </pre>
     */
    public void importDocuments(com.google.firestore.admin.v1.ImportDocumentsRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getImportDocumentsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Gets information about a database.
     * </pre>
     */
    public void getDatabase(com.google.firestore.admin.v1.GetDatabaseRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.Database> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetDatabaseMethod(), responseObserver);
    }

    /**
     * <pre>
     * List all the databases in the project.
     * </pre>
     */
    public void listDatabases(com.google.firestore.admin.v1.ListDatabasesRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.ListDatabasesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListDatabasesMethod(), responseObserver);
    }

    /**
     * <pre>
     * Updates a database.
     * </pre>
     */
    public void updateDatabase(com.google.firestore.admin.v1.UpdateDatabaseRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateDatabaseMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCreateIndexMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.firestore.admin.v1.CreateIndexRequest,
                com.google.longrunning.Operation>(
                  this, METHODID_CREATE_INDEX)))
          .addMethod(
            getListIndexesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.firestore.admin.v1.ListIndexesRequest,
                com.google.firestore.admin.v1.ListIndexesResponse>(
                  this, METHODID_LIST_INDEXES)))
          .addMethod(
            getGetIndexMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.firestore.admin.v1.GetIndexRequest,
                com.google.firestore.admin.v1.Index>(
                  this, METHODID_GET_INDEX)))
          .addMethod(
            getDeleteIndexMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.firestore.admin.v1.DeleteIndexRequest,
                com.google.protobuf.Empty>(
                  this, METHODID_DELETE_INDEX)))
          .addMethod(
            getGetFieldMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.firestore.admin.v1.GetFieldRequest,
                com.google.firestore.admin.v1.Field>(
                  this, METHODID_GET_FIELD)))
          .addMethod(
            getUpdateFieldMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.firestore.admin.v1.UpdateFieldRequest,
                com.google.longrunning.Operation>(
                  this, METHODID_UPDATE_FIELD)))
          .addMethod(
            getListFieldsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.firestore.admin.v1.ListFieldsRequest,
                com.google.firestore.admin.v1.ListFieldsResponse>(
                  this, METHODID_LIST_FIELDS)))
          .addMethod(
            getExportDocumentsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.firestore.admin.v1.ExportDocumentsRequest,
                com.google.longrunning.Operation>(
                  this, METHODID_EXPORT_DOCUMENTS)))
          .addMethod(
            getImportDocumentsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.firestore.admin.v1.ImportDocumentsRequest,
                com.google.longrunning.Operation>(
                  this, METHODID_IMPORT_DOCUMENTS)))
          .addMethod(
            getGetDatabaseMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.firestore.admin.v1.GetDatabaseRequest,
                com.google.firestore.admin.v1.Database>(
                  this, METHODID_GET_DATABASE)))
          .addMethod(
            getListDatabasesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.firestore.admin.v1.ListDatabasesRequest,
                com.google.firestore.admin.v1.ListDatabasesResponse>(
                  this, METHODID_LIST_DATABASES)))
          .addMethod(
            getUpdateDatabaseMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.firestore.admin.v1.UpdateDatabaseRequest,
                com.google.longrunning.Operation>(
                  this, METHODID_UPDATE_DATABASE)))
          .build();
    }
  }

  /**
   * <pre>
   * The Cloud Firestore Admin API.
   * This API provides several administrative services for Cloud Firestore.
   * Project, Database, Namespace, Collection, Collection Group, and Document are
   * used as defined in the Google Cloud Firestore API.
   * Operation: An Operation represents work being performed in the background.
   * The index service manages Cloud Firestore indexes.
   * Index creation is performed asynchronously.
   * An Operation resource is created for each such asynchronous operation.
   * The state of the operation (including any errors encountered)
   * may be queried via the Operation resource.
   * The Operations collection provides a record of actions performed for the
   * specified Project (including any Operations in progress). Operations are not
   * created directly but through calls on other collections or resources.
   * An Operation that is done may be deleted so that it is no longer listed as
   * part of the Operation collection. Operations are garbage collected after
   * 30 days. By default, ListOperations will only return in progress and failed
   * operations. To list completed operation, issue a ListOperations request with
   * the filter `done: true`.
   * Operations are created by service `FirestoreAdmin`, but are accessed via
   * service `google.longrunning.Operations`.
   * </pre>
   */
  public static final class FirestoreAdminStub extends io.grpc.stub.AbstractAsyncStub<FirestoreAdminStub> {
    private FirestoreAdminStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FirestoreAdminStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FirestoreAdminStub(channel, callOptions);
    }

    /**
     * <pre>
     * Creates a composite index. This returns a [google.longrunning.Operation][google.longrunning.Operation]
     * which may be used to track the status of the creation. The metadata for
     * the operation will be the type [IndexOperationMetadata][google.firestore.admin.v1.IndexOperationMetadata].
     * </pre>
     */
    public void createIndex(com.google.firestore.admin.v1.CreateIndexRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateIndexMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Lists composite indexes.
     * </pre>
     */
    public void listIndexes(com.google.firestore.admin.v1.ListIndexesRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.ListIndexesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListIndexesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Gets a composite index.
     * </pre>
     */
    public void getIndex(com.google.firestore.admin.v1.GetIndexRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.Index> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetIndexMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Deletes a composite index.
     * </pre>
     */
    public void deleteIndex(com.google.firestore.admin.v1.DeleteIndexRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteIndexMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Gets the metadata and configuration for a Field.
     * </pre>
     */
    public void getField(com.google.firestore.admin.v1.GetFieldRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.Field> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetFieldMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Updates a field configuration. Currently, field updates apply only to
     * single field index configuration. However, calls to
     * [FirestoreAdmin.UpdateField][google.firestore.admin.v1.FirestoreAdmin.UpdateField] should provide a field mask to avoid
     * changing any configuration that the caller isn't aware of. The field mask
     * should be specified as: `{ paths: "index_config" }`.
     * This call returns a [google.longrunning.Operation][google.longrunning.Operation] which may be used to
     * track the status of the field update. The metadata for
     * the operation will be the type [FieldOperationMetadata][google.firestore.admin.v1.FieldOperationMetadata].
     * To configure the default field settings for the database, use
     * the special `Field` with resource name:
     * `projects/{project_id}/databases/{database_id}/collectionGroups/__default__/fields/&#42;`.
     * </pre>
     */
    public void updateField(com.google.firestore.admin.v1.UpdateFieldRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateFieldMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Lists the field configuration and metadata for this database.
     * Currently, [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields] only supports listing fields
     * that have been explicitly overridden. To issue this query, call
     * [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields] with the filter set to
     * `indexConfig.usesAncestorConfig:false` .
     * </pre>
     */
    public void listFields(com.google.firestore.admin.v1.ListFieldsRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.ListFieldsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListFieldsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Exports a copy of all or a subset of documents from Google Cloud Firestore
     * to another storage system, such as Google Cloud Storage. Recent updates to
     * documents may not be reflected in the export. The export occurs in the
     * background and its progress can be monitored and managed via the
     * Operation resource that is created. The output of an export may only be
     * used once the associated operation is done. If an export operation is
     * cancelled before completion it may leave partial data behind in Google
     * Cloud Storage.
     * For more details on export behavior and output format, refer to:
     * https://cloud.google.com/firestore/docs/manage-data/export-import
     * </pre>
     */
    public void exportDocuments(com.google.firestore.admin.v1.ExportDocumentsRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExportDocumentsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Imports documents into Google Cloud Firestore. Existing documents with the
     * same name are overwritten. The import occurs in the background and its
     * progress can be monitored and managed via the Operation resource that is
     * created. If an ImportDocuments operation is cancelled, it is possible
     * that a subset of the data has already been imported to Cloud Firestore.
     * </pre>
     */
    public void importDocuments(com.google.firestore.admin.v1.ImportDocumentsRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getImportDocumentsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Gets information about a database.
     * </pre>
     */
    public void getDatabase(com.google.firestore.admin.v1.GetDatabaseRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.Database> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetDatabaseMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * List all the databases in the project.
     * </pre>
     */
    public void listDatabases(com.google.firestore.admin.v1.ListDatabasesRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.ListDatabasesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListDatabasesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Updates a database.
     * </pre>
     */
    public void updateDatabase(com.google.firestore.admin.v1.UpdateDatabaseRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateDatabaseMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The Cloud Firestore Admin API.
   * This API provides several administrative services for Cloud Firestore.
   * Project, Database, Namespace, Collection, Collection Group, and Document are
   * used as defined in the Google Cloud Firestore API.
   * Operation: An Operation represents work being performed in the background.
   * The index service manages Cloud Firestore indexes.
   * Index creation is performed asynchronously.
   * An Operation resource is created for each such asynchronous operation.
   * The state of the operation (including any errors encountered)
   * may be queried via the Operation resource.
   * The Operations collection provides a record of actions performed for the
   * specified Project (including any Operations in progress). Operations are not
   * created directly but through calls on other collections or resources.
   * An Operation that is done may be deleted so that it is no longer listed as
   * part of the Operation collection. Operations are garbage collected after
   * 30 days. By default, ListOperations will only return in progress and failed
   * operations. To list completed operation, issue a ListOperations request with
   * the filter `done: true`.
   * Operations are created by service `FirestoreAdmin`, but are accessed via
   * service `google.longrunning.Operations`.
   * </pre>
   */
  public static final class FirestoreAdminBlockingStub extends io.grpc.stub.AbstractBlockingStub<FirestoreAdminBlockingStub> {
    private FirestoreAdminBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FirestoreAdminBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FirestoreAdminBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Creates a composite index. This returns a [google.longrunning.Operation][google.longrunning.Operation]
     * which may be used to track the status of the creation. The metadata for
     * the operation will be the type [IndexOperationMetadata][google.firestore.admin.v1.IndexOperationMetadata].
     * </pre>
     */
    public com.google.longrunning.Operation createIndex(com.google.firestore.admin.v1.CreateIndexRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateIndexMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Lists composite indexes.
     * </pre>
     */
    public com.google.firestore.admin.v1.ListIndexesResponse listIndexes(com.google.firestore.admin.v1.ListIndexesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListIndexesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Gets a composite index.
     * </pre>
     */
    public com.google.firestore.admin.v1.Index getIndex(com.google.firestore.admin.v1.GetIndexRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetIndexMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Deletes a composite index.
     * </pre>
     */
    public com.google.protobuf.Empty deleteIndex(com.google.firestore.admin.v1.DeleteIndexRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteIndexMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Gets the metadata and configuration for a Field.
     * </pre>
     */
    public com.google.firestore.admin.v1.Field getField(com.google.firestore.admin.v1.GetFieldRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetFieldMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Updates a field configuration. Currently, field updates apply only to
     * single field index configuration. However, calls to
     * [FirestoreAdmin.UpdateField][google.firestore.admin.v1.FirestoreAdmin.UpdateField] should provide a field mask to avoid
     * changing any configuration that the caller isn't aware of. The field mask
     * should be specified as: `{ paths: "index_config" }`.
     * This call returns a [google.longrunning.Operation][google.longrunning.Operation] which may be used to
     * track the status of the field update. The metadata for
     * the operation will be the type [FieldOperationMetadata][google.firestore.admin.v1.FieldOperationMetadata].
     * To configure the default field settings for the database, use
     * the special `Field` with resource name:
     * `projects/{project_id}/databases/{database_id}/collectionGroups/__default__/fields/&#42;`.
     * </pre>
     */
    public com.google.longrunning.Operation updateField(com.google.firestore.admin.v1.UpdateFieldRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateFieldMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Lists the field configuration and metadata for this database.
     * Currently, [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields] only supports listing fields
     * that have been explicitly overridden. To issue this query, call
     * [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields] with the filter set to
     * `indexConfig.usesAncestorConfig:false` .
     * </pre>
     */
    public com.google.firestore.admin.v1.ListFieldsResponse listFields(com.google.firestore.admin.v1.ListFieldsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListFieldsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Exports a copy of all or a subset of documents from Google Cloud Firestore
     * to another storage system, such as Google Cloud Storage. Recent updates to
     * documents may not be reflected in the export. The export occurs in the
     * background and its progress can be monitored and managed via the
     * Operation resource that is created. The output of an export may only be
     * used once the associated operation is done. If an export operation is
     * cancelled before completion it may leave partial data behind in Google
     * Cloud Storage.
     * For more details on export behavior and output format, refer to:
     * https://cloud.google.com/firestore/docs/manage-data/export-import
     * </pre>
     */
    public com.google.longrunning.Operation exportDocuments(com.google.firestore.admin.v1.ExportDocumentsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExportDocumentsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Imports documents into Google Cloud Firestore. Existing documents with the
     * same name are overwritten. The import occurs in the background and its
     * progress can be monitored and managed via the Operation resource that is
     * created. If an ImportDocuments operation is cancelled, it is possible
     * that a subset of the data has already been imported to Cloud Firestore.
     * </pre>
     */
    public com.google.longrunning.Operation importDocuments(com.google.firestore.admin.v1.ImportDocumentsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getImportDocumentsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Gets information about a database.
     * </pre>
     */
    public com.google.firestore.admin.v1.Database getDatabase(com.google.firestore.admin.v1.GetDatabaseRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetDatabaseMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * List all the databases in the project.
     * </pre>
     */
    public com.google.firestore.admin.v1.ListDatabasesResponse listDatabases(com.google.firestore.admin.v1.ListDatabasesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListDatabasesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Updates a database.
     * </pre>
     */
    public com.google.longrunning.Operation updateDatabase(com.google.firestore.admin.v1.UpdateDatabaseRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateDatabaseMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The Cloud Firestore Admin API.
   * This API provides several administrative services for Cloud Firestore.
   * Project, Database, Namespace, Collection, Collection Group, and Document are
   * used as defined in the Google Cloud Firestore API.
   * Operation: An Operation represents work being performed in the background.
   * The index service manages Cloud Firestore indexes.
   * Index creation is performed asynchronously.
   * An Operation resource is created for each such asynchronous operation.
   * The state of the operation (including any errors encountered)
   * may be queried via the Operation resource.
   * The Operations collection provides a record of actions performed for the
   * specified Project (including any Operations in progress). Operations are not
   * created directly but through calls on other collections or resources.
   * An Operation that is done may be deleted so that it is no longer listed as
   * part of the Operation collection. Operations are garbage collected after
   * 30 days. By default, ListOperations will only return in progress and failed
   * operations. To list completed operation, issue a ListOperations request with
   * the filter `done: true`.
   * Operations are created by service `FirestoreAdmin`, but are accessed via
   * service `google.longrunning.Operations`.
   * </pre>
   */
  public static final class FirestoreAdminFutureStub extends io.grpc.stub.AbstractFutureStub<FirestoreAdminFutureStub> {
    private FirestoreAdminFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FirestoreAdminFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FirestoreAdminFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Creates a composite index. This returns a [google.longrunning.Operation][google.longrunning.Operation]
     * which may be used to track the status of the creation. The metadata for
     * the operation will be the type [IndexOperationMetadata][google.firestore.admin.v1.IndexOperationMetadata].
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation> createIndex(
        com.google.firestore.admin.v1.CreateIndexRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateIndexMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Lists composite indexes.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.firestore.admin.v1.ListIndexesResponse> listIndexes(
        com.google.firestore.admin.v1.ListIndexesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListIndexesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Gets a composite index.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.firestore.admin.v1.Index> getIndex(
        com.google.firestore.admin.v1.GetIndexRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetIndexMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Deletes a composite index.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> deleteIndex(
        com.google.firestore.admin.v1.DeleteIndexRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteIndexMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Gets the metadata and configuration for a Field.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.firestore.admin.v1.Field> getField(
        com.google.firestore.admin.v1.GetFieldRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetFieldMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Updates a field configuration. Currently, field updates apply only to
     * single field index configuration. However, calls to
     * [FirestoreAdmin.UpdateField][google.firestore.admin.v1.FirestoreAdmin.UpdateField] should provide a field mask to avoid
     * changing any configuration that the caller isn't aware of. The field mask
     * should be specified as: `{ paths: "index_config" }`.
     * This call returns a [google.longrunning.Operation][google.longrunning.Operation] which may be used to
     * track the status of the field update. The metadata for
     * the operation will be the type [FieldOperationMetadata][google.firestore.admin.v1.FieldOperationMetadata].
     * To configure the default field settings for the database, use
     * the special `Field` with resource name:
     * `projects/{project_id}/databases/{database_id}/collectionGroups/__default__/fields/&#42;`.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation> updateField(
        com.google.firestore.admin.v1.UpdateFieldRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateFieldMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Lists the field configuration and metadata for this database.
     * Currently, [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields] only supports listing fields
     * that have been explicitly overridden. To issue this query, call
     * [FirestoreAdmin.ListFields][google.firestore.admin.v1.FirestoreAdmin.ListFields] with the filter set to
     * `indexConfig.usesAncestorConfig:false` .
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.firestore.admin.v1.ListFieldsResponse> listFields(
        com.google.firestore.admin.v1.ListFieldsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListFieldsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Exports a copy of all or a subset of documents from Google Cloud Firestore
     * to another storage system, such as Google Cloud Storage. Recent updates to
     * documents may not be reflected in the export. The export occurs in the
     * background and its progress can be monitored and managed via the
     * Operation resource that is created. The output of an export may only be
     * used once the associated operation is done. If an export operation is
     * cancelled before completion it may leave partial data behind in Google
     * Cloud Storage.
     * For more details on export behavior and output format, refer to:
     * https://cloud.google.com/firestore/docs/manage-data/export-import
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation> exportDocuments(
        com.google.firestore.admin.v1.ExportDocumentsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExportDocumentsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Imports documents into Google Cloud Firestore. Existing documents with the
     * same name are overwritten. The import occurs in the background and its
     * progress can be monitored and managed via the Operation resource that is
     * created. If an ImportDocuments operation is cancelled, it is possible
     * that a subset of the data has already been imported to Cloud Firestore.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation> importDocuments(
        com.google.firestore.admin.v1.ImportDocumentsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getImportDocumentsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Gets information about a database.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.firestore.admin.v1.Database> getDatabase(
        com.google.firestore.admin.v1.GetDatabaseRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetDatabaseMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * List all the databases in the project.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.firestore.admin.v1.ListDatabasesResponse> listDatabases(
        com.google.firestore.admin.v1.ListDatabasesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListDatabasesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Updates a database.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation> updateDatabase(
        com.google.firestore.admin.v1.UpdateDatabaseRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateDatabaseMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_INDEX = 0;
  private static final int METHODID_LIST_INDEXES = 1;
  private static final int METHODID_GET_INDEX = 2;
  private static final int METHODID_DELETE_INDEX = 3;
  private static final int METHODID_GET_FIELD = 4;
  private static final int METHODID_UPDATE_FIELD = 5;
  private static final int METHODID_LIST_FIELDS = 6;
  private static final int METHODID_EXPORT_DOCUMENTS = 7;
  private static final int METHODID_IMPORT_DOCUMENTS = 8;
  private static final int METHODID_GET_DATABASE = 9;
  private static final int METHODID_LIST_DATABASES = 10;
  private static final int METHODID_UPDATE_DATABASE = 11;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final FirestoreAdminImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(FirestoreAdminImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_INDEX:
          serviceImpl.createIndex((com.google.firestore.admin.v1.CreateIndexRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_LIST_INDEXES:
          serviceImpl.listIndexes((com.google.firestore.admin.v1.ListIndexesRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.ListIndexesResponse>) responseObserver);
          break;
        case METHODID_GET_INDEX:
          serviceImpl.getIndex((com.google.firestore.admin.v1.GetIndexRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.Index>) responseObserver);
          break;
        case METHODID_DELETE_INDEX:
          serviceImpl.deleteIndex((com.google.firestore.admin.v1.DeleteIndexRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_GET_FIELD:
          serviceImpl.getField((com.google.firestore.admin.v1.GetFieldRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.Field>) responseObserver);
          break;
        case METHODID_UPDATE_FIELD:
          serviceImpl.updateField((com.google.firestore.admin.v1.UpdateFieldRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_LIST_FIELDS:
          serviceImpl.listFields((com.google.firestore.admin.v1.ListFieldsRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.ListFieldsResponse>) responseObserver);
          break;
        case METHODID_EXPORT_DOCUMENTS:
          serviceImpl.exportDocuments((com.google.firestore.admin.v1.ExportDocumentsRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_IMPORT_DOCUMENTS:
          serviceImpl.importDocuments((com.google.firestore.admin.v1.ImportDocumentsRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_GET_DATABASE:
          serviceImpl.getDatabase((com.google.firestore.admin.v1.GetDatabaseRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.Database>) responseObserver);
          break;
        case METHODID_LIST_DATABASES:
          serviceImpl.listDatabases((com.google.firestore.admin.v1.ListDatabasesRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.admin.v1.ListDatabasesResponse>) responseObserver);
          break;
        case METHODID_UPDATE_DATABASE:
          serviceImpl.updateDatabase((com.google.firestore.admin.v1.UpdateDatabaseRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class FirestoreAdminBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FirestoreAdminBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.google.firestore.admin.v1.FirestoreAdminProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FirestoreAdmin");
    }
  }

  private static final class FirestoreAdminFileDescriptorSupplier
      extends FirestoreAdminBaseDescriptorSupplier {
    FirestoreAdminFileDescriptorSupplier() {}
  }

  private static final class FirestoreAdminMethodDescriptorSupplier
      extends FirestoreAdminBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    FirestoreAdminMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (FirestoreAdminGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FirestoreAdminFileDescriptorSupplier())
              .addMethod(getCreateIndexMethod())
              .addMethod(getListIndexesMethod())
              .addMethod(getGetIndexMethod())
              .addMethod(getDeleteIndexMethod())
              .addMethod(getGetFieldMethod())
              .addMethod(getUpdateFieldMethod())
              .addMethod(getListFieldsMethod())
              .addMethod(getExportDocumentsMethod())
              .addMethod(getImportDocumentsMethod())
              .addMethod(getGetDatabaseMethod())
              .addMethod(getListDatabasesMethod())
              .addMethod(getUpdateDatabaseMethod())
              .build();
        }
      }
    }
    return result;
  }
}
