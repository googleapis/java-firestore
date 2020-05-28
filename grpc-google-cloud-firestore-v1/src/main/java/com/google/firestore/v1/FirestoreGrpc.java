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
package com.google.firestore.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 *
 *
 * <pre>
 * The Cloud Firestore service.
 * Cloud Firestore is a fast, fully managed, serverless, cloud-native NoSQL
 * document database that simplifies storing, syncing, and querying data for
 * your mobile, web, and IoT apps at global scale. Its client libraries provide
 * live synchronization and offline support, while its security features and
 * integrations with Firebase and Google Cloud Platform (GCP) accelerate
 * building truly serverless apps.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler",
    comments = "Source: google/firestore/v1/firestore.proto")
public final class FirestoreGrpc {

  private FirestoreGrpc() {}

  public static final String SERVICE_NAME = "google.firestore.v1.Firestore";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.GetDocumentRequest, com.google.firestore.v1.Document>
      getGetDocumentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetDocument",
      requestType = com.google.firestore.v1.GetDocumentRequest.class,
      responseType = com.google.firestore.v1.Document.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.GetDocumentRequest, com.google.firestore.v1.Document>
      getGetDocumentMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.GetDocumentRequest, com.google.firestore.v1.Document>
        getGetDocumentMethod;
    if ((getGetDocumentMethod = FirestoreGrpc.getGetDocumentMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getGetDocumentMethod = FirestoreGrpc.getGetDocumentMethod) == null) {
          FirestoreGrpc.getGetDocumentMethod =
              getGetDocumentMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.GetDocumentRequest,
                          com.google.firestore.v1.Document>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetDocument"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.GetDocumentRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.Document.getDefaultInstance()))
                      .setSchemaDescriptor(new FirestoreMethodDescriptorSupplier("GetDocument"))
                      .build();
        }
      }
    }
    return getGetDocumentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.ListDocumentsRequest,
          com.google.firestore.v1.ListDocumentsResponse>
      getListDocumentsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListDocuments",
      requestType = com.google.firestore.v1.ListDocumentsRequest.class,
      responseType = com.google.firestore.v1.ListDocumentsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.ListDocumentsRequest,
          com.google.firestore.v1.ListDocumentsResponse>
      getListDocumentsMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.ListDocumentsRequest,
            com.google.firestore.v1.ListDocumentsResponse>
        getListDocumentsMethod;
    if ((getListDocumentsMethod = FirestoreGrpc.getListDocumentsMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getListDocumentsMethod = FirestoreGrpc.getListDocumentsMethod) == null) {
          FirestoreGrpc.getListDocumentsMethod =
              getListDocumentsMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.ListDocumentsRequest,
                          com.google.firestore.v1.ListDocumentsResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListDocuments"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.ListDocumentsRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.ListDocumentsResponse.getDefaultInstance()))
                      .setSchemaDescriptor(new FirestoreMethodDescriptorSupplier("ListDocuments"))
                      .build();
        }
      }
    }
    return getListDocumentsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.UpdateDocumentRequest, com.google.firestore.v1.Document>
      getUpdateDocumentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateDocument",
      requestType = com.google.firestore.v1.UpdateDocumentRequest.class,
      responseType = com.google.firestore.v1.Document.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.UpdateDocumentRequest, com.google.firestore.v1.Document>
      getUpdateDocumentMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.UpdateDocumentRequest, com.google.firestore.v1.Document>
        getUpdateDocumentMethod;
    if ((getUpdateDocumentMethod = FirestoreGrpc.getUpdateDocumentMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getUpdateDocumentMethod = FirestoreGrpc.getUpdateDocumentMethod) == null) {
          FirestoreGrpc.getUpdateDocumentMethod =
              getUpdateDocumentMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.UpdateDocumentRequest,
                          com.google.firestore.v1.Document>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateDocument"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.UpdateDocumentRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.Document.getDefaultInstance()))
                      .setSchemaDescriptor(new FirestoreMethodDescriptorSupplier("UpdateDocument"))
                      .build();
        }
      }
    }
    return getUpdateDocumentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.DeleteDocumentRequest, com.google.protobuf.Empty>
      getDeleteDocumentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteDocument",
      requestType = com.google.firestore.v1.DeleteDocumentRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.DeleteDocumentRequest, com.google.protobuf.Empty>
      getDeleteDocumentMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.DeleteDocumentRequest, com.google.protobuf.Empty>
        getDeleteDocumentMethod;
    if ((getDeleteDocumentMethod = FirestoreGrpc.getDeleteDocumentMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getDeleteDocumentMethod = FirestoreGrpc.getDeleteDocumentMethod) == null) {
          FirestoreGrpc.getDeleteDocumentMethod =
              getDeleteDocumentMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.DeleteDocumentRequest, com.google.protobuf.Empty>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteDocument"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.DeleteDocumentRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.protobuf.Empty.getDefaultInstance()))
                      .setSchemaDescriptor(new FirestoreMethodDescriptorSupplier("DeleteDocument"))
                      .build();
        }
      }
    }
    return getDeleteDocumentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.BatchGetDocumentsRequest,
          com.google.firestore.v1.BatchGetDocumentsResponse>
      getBatchGetDocumentsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "BatchGetDocuments",
      requestType = com.google.firestore.v1.BatchGetDocumentsRequest.class,
      responseType = com.google.firestore.v1.BatchGetDocumentsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.BatchGetDocumentsRequest,
          com.google.firestore.v1.BatchGetDocumentsResponse>
      getBatchGetDocumentsMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.BatchGetDocumentsRequest,
            com.google.firestore.v1.BatchGetDocumentsResponse>
        getBatchGetDocumentsMethod;
    if ((getBatchGetDocumentsMethod = FirestoreGrpc.getBatchGetDocumentsMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getBatchGetDocumentsMethod = FirestoreGrpc.getBatchGetDocumentsMethod) == null) {
          FirestoreGrpc.getBatchGetDocumentsMethod =
              getBatchGetDocumentsMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.BatchGetDocumentsRequest,
                          com.google.firestore.v1.BatchGetDocumentsResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "BatchGetDocuments"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.BatchGetDocumentsRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.BatchGetDocumentsResponse
                                  .getDefaultInstance()))
                      .setSchemaDescriptor(
                          new FirestoreMethodDescriptorSupplier("BatchGetDocuments"))
                      .build();
        }
      }
    }
    return getBatchGetDocumentsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.BeginTransactionRequest,
          com.google.firestore.v1.BeginTransactionResponse>
      getBeginTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "BeginTransaction",
      requestType = com.google.firestore.v1.BeginTransactionRequest.class,
      responseType = com.google.firestore.v1.BeginTransactionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.BeginTransactionRequest,
          com.google.firestore.v1.BeginTransactionResponse>
      getBeginTransactionMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.BeginTransactionRequest,
            com.google.firestore.v1.BeginTransactionResponse>
        getBeginTransactionMethod;
    if ((getBeginTransactionMethod = FirestoreGrpc.getBeginTransactionMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getBeginTransactionMethod = FirestoreGrpc.getBeginTransactionMethod) == null) {
          FirestoreGrpc.getBeginTransactionMethod =
              getBeginTransactionMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.BeginTransactionRequest,
                          com.google.firestore.v1.BeginTransactionResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "BeginTransaction"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.BeginTransactionRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.BeginTransactionResponse
                                  .getDefaultInstance()))
                      .setSchemaDescriptor(
                          new FirestoreMethodDescriptorSupplier("BeginTransaction"))
                      .build();
        }
      }
    }
    return getBeginTransactionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.CommitRequest, com.google.firestore.v1.CommitResponse>
      getCommitMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Commit",
      requestType = com.google.firestore.v1.CommitRequest.class,
      responseType = com.google.firestore.v1.CommitResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.CommitRequest, com.google.firestore.v1.CommitResponse>
      getCommitMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.CommitRequest, com.google.firestore.v1.CommitResponse>
        getCommitMethod;
    if ((getCommitMethod = FirestoreGrpc.getCommitMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getCommitMethod = FirestoreGrpc.getCommitMethod) == null) {
          FirestoreGrpc.getCommitMethod =
              getCommitMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.CommitRequest,
                          com.google.firestore.v1.CommitResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Commit"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.CommitRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.CommitResponse.getDefaultInstance()))
                      .setSchemaDescriptor(new FirestoreMethodDescriptorSupplier("Commit"))
                      .build();
        }
      }
    }
    return getCommitMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.RollbackRequest, com.google.protobuf.Empty>
      getRollbackMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Rollback",
      requestType = com.google.firestore.v1.RollbackRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.RollbackRequest, com.google.protobuf.Empty>
      getRollbackMethod() {
    io.grpc.MethodDescriptor<com.google.firestore.v1.RollbackRequest, com.google.protobuf.Empty>
        getRollbackMethod;
    if ((getRollbackMethod = FirestoreGrpc.getRollbackMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getRollbackMethod = FirestoreGrpc.getRollbackMethod) == null) {
          FirestoreGrpc.getRollbackMethod =
              getRollbackMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.RollbackRequest, com.google.protobuf.Empty>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Rollback"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.RollbackRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.protobuf.Empty.getDefaultInstance()))
                      .setSchemaDescriptor(new FirestoreMethodDescriptorSupplier("Rollback"))
                      .build();
        }
      }
    }
    return getRollbackMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.RunQueryRequest, com.google.firestore.v1.RunQueryResponse>
      getRunQueryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RunQuery",
      requestType = com.google.firestore.v1.RunQueryRequest.class,
      responseType = com.google.firestore.v1.RunQueryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.RunQueryRequest, com.google.firestore.v1.RunQueryResponse>
      getRunQueryMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.RunQueryRequest, com.google.firestore.v1.RunQueryResponse>
        getRunQueryMethod;
    if ((getRunQueryMethod = FirestoreGrpc.getRunQueryMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getRunQueryMethod = FirestoreGrpc.getRunQueryMethod) == null) {
          FirestoreGrpc.getRunQueryMethod =
              getRunQueryMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.RunQueryRequest,
                          com.google.firestore.v1.RunQueryResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RunQuery"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.RunQueryRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.RunQueryResponse.getDefaultInstance()))
                      .setSchemaDescriptor(new FirestoreMethodDescriptorSupplier("RunQuery"))
                      .build();
        }
      }
    }
    return getRunQueryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.WriteRequest, com.google.firestore.v1.WriteResponse>
      getWriteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Write",
      requestType = com.google.firestore.v1.WriteRequest.class,
      responseType = com.google.firestore.v1.WriteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.WriteRequest, com.google.firestore.v1.WriteResponse>
      getWriteMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.WriteRequest, com.google.firestore.v1.WriteResponse>
        getWriteMethod;
    if ((getWriteMethod = FirestoreGrpc.getWriteMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getWriteMethod = FirestoreGrpc.getWriteMethod) == null) {
          FirestoreGrpc.getWriteMethod =
              getWriteMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.WriteRequest, com.google.firestore.v1.WriteResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Write"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.WriteRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.WriteResponse.getDefaultInstance()))
                      .setSchemaDescriptor(new FirestoreMethodDescriptorSupplier("Write"))
                      .build();
        }
      }
    }
    return getWriteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.ListenRequest, com.google.firestore.v1.ListenResponse>
      getListenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Listen",
      requestType = com.google.firestore.v1.ListenRequest.class,
      responseType = com.google.firestore.v1.ListenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.ListenRequest, com.google.firestore.v1.ListenResponse>
      getListenMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.ListenRequest, com.google.firestore.v1.ListenResponse>
        getListenMethod;
    if ((getListenMethod = FirestoreGrpc.getListenMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getListenMethod = FirestoreGrpc.getListenMethod) == null) {
          FirestoreGrpc.getListenMethod =
              getListenMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.ListenRequest,
                          com.google.firestore.v1.ListenResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Listen"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.ListenRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.ListenResponse.getDefaultInstance()))
                      .setSchemaDescriptor(new FirestoreMethodDescriptorSupplier("Listen"))
                      .build();
        }
      }
    }
    return getListenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.ListCollectionIdsRequest,
          com.google.firestore.v1.ListCollectionIdsResponse>
      getListCollectionIdsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListCollectionIds",
      requestType = com.google.firestore.v1.ListCollectionIdsRequest.class,
      responseType = com.google.firestore.v1.ListCollectionIdsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.ListCollectionIdsRequest,
          com.google.firestore.v1.ListCollectionIdsResponse>
      getListCollectionIdsMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.ListCollectionIdsRequest,
            com.google.firestore.v1.ListCollectionIdsResponse>
        getListCollectionIdsMethod;
    if ((getListCollectionIdsMethod = FirestoreGrpc.getListCollectionIdsMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getListCollectionIdsMethod = FirestoreGrpc.getListCollectionIdsMethod) == null) {
          FirestoreGrpc.getListCollectionIdsMethod =
              getListCollectionIdsMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.ListCollectionIdsRequest,
                          com.google.firestore.v1.ListCollectionIdsResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListCollectionIds"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.ListCollectionIdsRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.ListCollectionIdsResponse
                                  .getDefaultInstance()))
                      .setSchemaDescriptor(
                          new FirestoreMethodDescriptorSupplier("ListCollectionIds"))
                      .build();
        }
      }
    }
    return getListCollectionIdsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.CreateDocumentRequest, com.google.firestore.v1.Document>
      getCreateDocumentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateDocument",
      requestType = com.google.firestore.v1.CreateDocumentRequest.class,
      responseType = com.google.firestore.v1.Document.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.CreateDocumentRequest, com.google.firestore.v1.Document>
      getCreateDocumentMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.CreateDocumentRequest, com.google.firestore.v1.Document>
        getCreateDocumentMethod;
    if ((getCreateDocumentMethod = FirestoreGrpc.getCreateDocumentMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getCreateDocumentMethod = FirestoreGrpc.getCreateDocumentMethod) == null) {
          FirestoreGrpc.getCreateDocumentMethod =
              getCreateDocumentMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.CreateDocumentRequest,
                          com.google.firestore.v1.Document>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateDocument"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.CreateDocumentRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.Document.getDefaultInstance()))
                      .setSchemaDescriptor(new FirestoreMethodDescriptorSupplier("CreateDocument"))
                      .build();
        }
      }
    }
    return getCreateDocumentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.firestore.v1.BatchWriteRequest, com.google.firestore.v1.BatchWriteResponse>
      getBatchWriteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "BatchWrite",
      requestType = com.google.firestore.v1.BatchWriteRequest.class,
      responseType = com.google.firestore.v1.BatchWriteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.firestore.v1.BatchWriteRequest, com.google.firestore.v1.BatchWriteResponse>
      getBatchWriteMethod() {
    io.grpc.MethodDescriptor<
            com.google.firestore.v1.BatchWriteRequest, com.google.firestore.v1.BatchWriteResponse>
        getBatchWriteMethod;
    if ((getBatchWriteMethod = FirestoreGrpc.getBatchWriteMethod) == null) {
      synchronized (FirestoreGrpc.class) {
        if ((getBatchWriteMethod = FirestoreGrpc.getBatchWriteMethod) == null) {
          FirestoreGrpc.getBatchWriteMethod =
              getBatchWriteMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.firestore.v1.BatchWriteRequest,
                          com.google.firestore.v1.BatchWriteResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "BatchWrite"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.BatchWriteRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.firestore.v1.BatchWriteResponse.getDefaultInstance()))
                      .setSchemaDescriptor(new FirestoreMethodDescriptorSupplier("BatchWrite"))
                      .build();
        }
      }
    }
    return getBatchWriteMethod;
  }

  /** Creates a new async stub that supports all call types for the service */
  public static FirestoreStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FirestoreStub> factory =
        new io.grpc.stub.AbstractStub.StubFactory<FirestoreStub>() {
          @java.lang.Override
          public FirestoreStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new FirestoreStub(channel, callOptions);
          }
        };
    return FirestoreStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FirestoreBlockingStub newBlockingStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FirestoreBlockingStub> factory =
        new io.grpc.stub.AbstractStub.StubFactory<FirestoreBlockingStub>() {
          @java.lang.Override
          public FirestoreBlockingStub newStub(
              io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new FirestoreBlockingStub(channel, callOptions);
          }
        };
    return FirestoreBlockingStub.newStub(factory, channel);
  }

  /** Creates a new ListenableFuture-style stub that supports unary calls on the service */
  public static FirestoreFutureStub newFutureStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FirestoreFutureStub> factory =
        new io.grpc.stub.AbstractStub.StubFactory<FirestoreFutureStub>() {
          @java.lang.Override
          public FirestoreFutureStub newStub(
              io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new FirestoreFutureStub(channel, callOptions);
          }
        };
    return FirestoreFutureStub.newStub(factory, channel);
  }

  /**
   *
   *
   * <pre>
   * The Cloud Firestore service.
   * Cloud Firestore is a fast, fully managed, serverless, cloud-native NoSQL
   * document database that simplifies storing, syncing, and querying data for
   * your mobile, web, and IoT apps at global scale. Its client libraries provide
   * live synchronization and offline support, while its security features and
   * integrations with Firebase and Google Cloud Platform (GCP) accelerate
   * building truly serverless apps.
   * </pre>
   */
  public abstract static class FirestoreImplBase implements io.grpc.BindableService {

    /**
     *
     *
     * <pre>
     * Gets a single document.
     * </pre>
     */
    public void getDocument(
        com.google.firestore.v1.GetDocumentRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.Document> responseObserver) {
      asyncUnimplementedUnaryCall(getGetDocumentMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists documents.
     * </pre>
     */
    public void listDocuments(
        com.google.firestore.v1.ListDocumentsRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.ListDocumentsResponse>
            responseObserver) {
      asyncUnimplementedUnaryCall(getListDocumentsMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Updates or inserts a document.
     * </pre>
     */
    public void updateDocument(
        com.google.firestore.v1.UpdateDocumentRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.Document> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateDocumentMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Deletes a document.
     * </pre>
     */
    public void deleteDocument(
        com.google.firestore.v1.DeleteDocumentRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getDeleteDocumentMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets multiple documents.
     * Documents returned by this method are not guaranteed to be returned in the
     * same order that they were requested.
     * </pre>
     */
    public void batchGetDocuments(
        com.google.firestore.v1.BatchGetDocumentsRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.BatchGetDocumentsResponse>
            responseObserver) {
      asyncUnimplementedUnaryCall(getBatchGetDocumentsMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Starts a new transaction.
     * </pre>
     */
    public void beginTransaction(
        com.google.firestore.v1.BeginTransactionRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.BeginTransactionResponse>
            responseObserver) {
      asyncUnimplementedUnaryCall(getBeginTransactionMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Commits a transaction, while optionally updating documents.
     * </pre>
     */
    public void commit(
        com.google.firestore.v1.CommitRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.CommitResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCommitMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Rolls back a transaction.
     * </pre>
     */
    public void rollback(
        com.google.firestore.v1.RollbackRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getRollbackMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Runs a query.
     * </pre>
     */
    public void runQuery(
        com.google.firestore.v1.RunQueryRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.RunQueryResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRunQueryMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Streams batches of document updates and deletes, in order.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.google.firestore.v1.WriteRequest> write(
        io.grpc.stub.StreamObserver<com.google.firestore.v1.WriteResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(getWriteMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Listens to changes.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.google.firestore.v1.ListenRequest> listen(
        io.grpc.stub.StreamObserver<com.google.firestore.v1.ListenResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(getListenMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists all the collection IDs underneath a document.
     * </pre>
     */
    public void listCollectionIds(
        com.google.firestore.v1.ListCollectionIdsRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.ListCollectionIdsResponse>
            responseObserver) {
      asyncUnimplementedUnaryCall(getListCollectionIdsMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Creates a new document.
     * </pre>
     */
    public void createDocument(
        com.google.firestore.v1.CreateDocumentRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.Document> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateDocumentMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Commit a batch of non-transactional writes.
     * </pre>
     */
    public void batchWrite(
        com.google.firestore.v1.BatchWriteRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.BatchWriteResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getBatchWriteMethod(), responseObserver);
    }

    @java.lang.Override
    public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
              getGetDocumentMethod(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.firestore.v1.GetDocumentRequest, com.google.firestore.v1.Document>(
                      this, METHODID_GET_DOCUMENT)))
          .addMethod(
              getListDocumentsMethod(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.firestore.v1.ListDocumentsRequest,
                      com.google.firestore.v1.ListDocumentsResponse>(
                      this, METHODID_LIST_DOCUMENTS)))
          .addMethod(
              getUpdateDocumentMethod(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.firestore.v1.UpdateDocumentRequest,
                      com.google.firestore.v1.Document>(this, METHODID_UPDATE_DOCUMENT)))
          .addMethod(
              getDeleteDocumentMethod(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.firestore.v1.DeleteDocumentRequest, com.google.protobuf.Empty>(
                      this, METHODID_DELETE_DOCUMENT)))
          .addMethod(
              getBatchGetDocumentsMethod(),
              asyncServerStreamingCall(
                  new MethodHandlers<
                      com.google.firestore.v1.BatchGetDocumentsRequest,
                      com.google.firestore.v1.BatchGetDocumentsResponse>(
                      this, METHODID_BATCH_GET_DOCUMENTS)))
          .addMethod(
              getBeginTransactionMethod(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.firestore.v1.BeginTransactionRequest,
                      com.google.firestore.v1.BeginTransactionResponse>(
                      this, METHODID_BEGIN_TRANSACTION)))
          .addMethod(
              getCommitMethod(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.firestore.v1.CommitRequest,
                      com.google.firestore.v1.CommitResponse>(this, METHODID_COMMIT)))
          .addMethod(
              getRollbackMethod(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.firestore.v1.RollbackRequest, com.google.protobuf.Empty>(
                      this, METHODID_ROLLBACK)))
          .addMethod(
              getRunQueryMethod(),
              asyncServerStreamingCall(
                  new MethodHandlers<
                      com.google.firestore.v1.RunQueryRequest,
                      com.google.firestore.v1.RunQueryResponse>(this, METHODID_RUN_QUERY)))
          .addMethod(
              getWriteMethod(),
              asyncBidiStreamingCall(
                  new MethodHandlers<
                      com.google.firestore.v1.WriteRequest, com.google.firestore.v1.WriteResponse>(
                      this, METHODID_WRITE)))
          .addMethod(
              getListenMethod(),
              asyncBidiStreamingCall(
                  new MethodHandlers<
                      com.google.firestore.v1.ListenRequest,
                      com.google.firestore.v1.ListenResponse>(this, METHODID_LISTEN)))
          .addMethod(
              getListCollectionIdsMethod(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.firestore.v1.ListCollectionIdsRequest,
                      com.google.firestore.v1.ListCollectionIdsResponse>(
                      this, METHODID_LIST_COLLECTION_IDS)))
          .addMethod(
              getCreateDocumentMethod(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.firestore.v1.CreateDocumentRequest,
                      com.google.firestore.v1.Document>(this, METHODID_CREATE_DOCUMENT)))
          .addMethod(
              getBatchWriteMethod(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.firestore.v1.BatchWriteRequest,
                      com.google.firestore.v1.BatchWriteResponse>(this, METHODID_BATCH_WRITE)))
          .build();
    }
  }

  /**
   *
   *
   * <pre>
   * The Cloud Firestore service.
   * Cloud Firestore is a fast, fully managed, serverless, cloud-native NoSQL
   * document database that simplifies storing, syncing, and querying data for
   * your mobile, web, and IoT apps at global scale. Its client libraries provide
   * live synchronization and offline support, while its security features and
   * integrations with Firebase and Google Cloud Platform (GCP) accelerate
   * building truly serverless apps.
   * </pre>
   */
  public static final class FirestoreStub extends io.grpc.stub.AbstractAsyncStub<FirestoreStub> {
    private FirestoreStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FirestoreStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FirestoreStub(channel, callOptions);
    }

    /**
     *
     *
     * <pre>
     * Gets a single document.
     * </pre>
     */
    public void getDocument(
        com.google.firestore.v1.GetDocumentRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.Document> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetDocumentMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists documents.
     * </pre>
     */
    public void listDocuments(
        com.google.firestore.v1.ListDocumentsRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.ListDocumentsResponse>
            responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListDocumentsMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Updates or inserts a document.
     * </pre>
     */
    public void updateDocument(
        com.google.firestore.v1.UpdateDocumentRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.Document> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateDocumentMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Deletes a document.
     * </pre>
     */
    public void deleteDocument(
        com.google.firestore.v1.DeleteDocumentRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeleteDocumentMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets multiple documents.
     * Documents returned by this method are not guaranteed to be returned in the
     * same order that they were requested.
     * </pre>
     */
    public void batchGetDocuments(
        com.google.firestore.v1.BatchGetDocumentsRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.BatchGetDocumentsResponse>
            responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getBatchGetDocumentsMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Starts a new transaction.
     * </pre>
     */
    public void beginTransaction(
        com.google.firestore.v1.BeginTransactionRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.BeginTransactionResponse>
            responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getBeginTransactionMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Commits a transaction, while optionally updating documents.
     * </pre>
     */
    public void commit(
        com.google.firestore.v1.CommitRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.CommitResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCommitMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Rolls back a transaction.
     * </pre>
     */
    public void rollback(
        com.google.firestore.v1.RollbackRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRollbackMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Runs a query.
     * </pre>
     */
    public void runQuery(
        com.google.firestore.v1.RunQueryRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.RunQueryResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getRunQueryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Streams batches of document updates and deletes, in order.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.google.firestore.v1.WriteRequest> write(
        io.grpc.stub.StreamObserver<com.google.firestore.v1.WriteResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getWriteMethod(), getCallOptions()), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Listens to changes.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.google.firestore.v1.ListenRequest> listen(
        io.grpc.stub.StreamObserver<com.google.firestore.v1.ListenResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getListenMethod(), getCallOptions()), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists all the collection IDs underneath a document.
     * </pre>
     */
    public void listCollectionIds(
        com.google.firestore.v1.ListCollectionIdsRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.ListCollectionIdsResponse>
            responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListCollectionIdsMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Creates a new document.
     * </pre>
     */
    public void createDocument(
        com.google.firestore.v1.CreateDocumentRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.Document> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateDocumentMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Commit a batch of non-transactional writes.
     * </pre>
     */
    public void batchWrite(
        com.google.firestore.v1.BatchWriteRequest request,
        io.grpc.stub.StreamObserver<com.google.firestore.v1.BatchWriteResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getBatchWriteMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   *
   *
   * <pre>
   * The Cloud Firestore service.
   * Cloud Firestore is a fast, fully managed, serverless, cloud-native NoSQL
   * document database that simplifies storing, syncing, and querying data for
   * your mobile, web, and IoT apps at global scale. Its client libraries provide
   * live synchronization and offline support, while its security features and
   * integrations with Firebase and Google Cloud Platform (GCP) accelerate
   * building truly serverless apps.
   * </pre>
   */
  public static final class FirestoreBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<FirestoreBlockingStub> {
    private FirestoreBlockingStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FirestoreBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FirestoreBlockingStub(channel, callOptions);
    }

    /**
     *
     *
     * <pre>
     * Gets a single document.
     * </pre>
     */
    public com.google.firestore.v1.Document getDocument(
        com.google.firestore.v1.GetDocumentRequest request) {
      return blockingUnaryCall(getChannel(), getGetDocumentMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Lists documents.
     * </pre>
     */
    public com.google.firestore.v1.ListDocumentsResponse listDocuments(
        com.google.firestore.v1.ListDocumentsRequest request) {
      return blockingUnaryCall(getChannel(), getListDocumentsMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Updates or inserts a document.
     * </pre>
     */
    public com.google.firestore.v1.Document updateDocument(
        com.google.firestore.v1.UpdateDocumentRequest request) {
      return blockingUnaryCall(getChannel(), getUpdateDocumentMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Deletes a document.
     * </pre>
     */
    public com.google.protobuf.Empty deleteDocument(
        com.google.firestore.v1.DeleteDocumentRequest request) {
      return blockingUnaryCall(getChannel(), getDeleteDocumentMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Gets multiple documents.
     * Documents returned by this method are not guaranteed to be returned in the
     * same order that they were requested.
     * </pre>
     */
    public java.util.Iterator<com.google.firestore.v1.BatchGetDocumentsResponse> batchGetDocuments(
        com.google.firestore.v1.BatchGetDocumentsRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getBatchGetDocumentsMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Starts a new transaction.
     * </pre>
     */
    public com.google.firestore.v1.BeginTransactionResponse beginTransaction(
        com.google.firestore.v1.BeginTransactionRequest request) {
      return blockingUnaryCall(
          getChannel(), getBeginTransactionMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Commits a transaction, while optionally updating documents.
     * </pre>
     */
    public com.google.firestore.v1.CommitResponse commit(
        com.google.firestore.v1.CommitRequest request) {
      return blockingUnaryCall(getChannel(), getCommitMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Rolls back a transaction.
     * </pre>
     */
    public com.google.protobuf.Empty rollback(com.google.firestore.v1.RollbackRequest request) {
      return blockingUnaryCall(getChannel(), getRollbackMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Runs a query.
     * </pre>
     */
    public java.util.Iterator<com.google.firestore.v1.RunQueryResponse> runQuery(
        com.google.firestore.v1.RunQueryRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getRunQueryMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Lists all the collection IDs underneath a document.
     * </pre>
     */
    public com.google.firestore.v1.ListCollectionIdsResponse listCollectionIds(
        com.google.firestore.v1.ListCollectionIdsRequest request) {
      return blockingUnaryCall(
          getChannel(), getListCollectionIdsMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Creates a new document.
     * </pre>
     */
    public com.google.firestore.v1.Document createDocument(
        com.google.firestore.v1.CreateDocumentRequest request) {
      return blockingUnaryCall(getChannel(), getCreateDocumentMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Commit a batch of non-transactional writes.
     * </pre>
     */
    public com.google.firestore.v1.BatchWriteResponse batchWrite(
        com.google.firestore.v1.BatchWriteRequest request) {
      return blockingUnaryCall(getChannel(), getBatchWriteMethod(), getCallOptions(), request);
    }
  }

  /**
   *
   *
   * <pre>
   * The Cloud Firestore service.
   * Cloud Firestore is a fast, fully managed, serverless, cloud-native NoSQL
   * document database that simplifies storing, syncing, and querying data for
   * your mobile, web, and IoT apps at global scale. Its client libraries provide
   * live synchronization and offline support, while its security features and
   * integrations with Firebase and Google Cloud Platform (GCP) accelerate
   * building truly serverless apps.
   * </pre>
   */
  public static final class FirestoreFutureStub
      extends io.grpc.stub.AbstractFutureStub<FirestoreFutureStub> {
    private FirestoreFutureStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FirestoreFutureStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FirestoreFutureStub(channel, callOptions);
    }

    /**
     *
     *
     * <pre>
     * Gets a single document.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.firestore.v1.Document>
        getDocument(com.google.firestore.v1.GetDocumentRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetDocumentMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Lists documents.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.firestore.v1.ListDocumentsResponse>
        listDocuments(com.google.firestore.v1.ListDocumentsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getListDocumentsMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Updates or inserts a document.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.firestore.v1.Document>
        updateDocument(com.google.firestore.v1.UpdateDocumentRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateDocumentMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Deletes a document.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty>
        deleteDocument(com.google.firestore.v1.DeleteDocumentRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeleteDocumentMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Starts a new transaction.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.firestore.v1.BeginTransactionResponse>
        beginTransaction(com.google.firestore.v1.BeginTransactionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getBeginTransactionMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Commits a transaction, while optionally updating documents.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.firestore.v1.CommitResponse>
        commit(com.google.firestore.v1.CommitRequest request) {
      return futureUnaryCall(getChannel().newCall(getCommitMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Rolls back a transaction.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> rollback(
        com.google.firestore.v1.RollbackRequest request) {
      return futureUnaryCall(getChannel().newCall(getRollbackMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Lists all the collection IDs underneath a document.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.firestore.v1.ListCollectionIdsResponse>
        listCollectionIds(com.google.firestore.v1.ListCollectionIdsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getListCollectionIdsMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Creates a new document.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.firestore.v1.Document>
        createDocument(com.google.firestore.v1.CreateDocumentRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateDocumentMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Commit a batch of non-transactional writes.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.firestore.v1.BatchWriteResponse>
        batchWrite(com.google.firestore.v1.BatchWriteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getBatchWriteMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_DOCUMENT = 0;
  private static final int METHODID_LIST_DOCUMENTS = 1;
  private static final int METHODID_UPDATE_DOCUMENT = 2;
  private static final int METHODID_DELETE_DOCUMENT = 3;
  private static final int METHODID_BATCH_GET_DOCUMENTS = 4;
  private static final int METHODID_BEGIN_TRANSACTION = 5;
  private static final int METHODID_COMMIT = 6;
  private static final int METHODID_ROLLBACK = 7;
  private static final int METHODID_RUN_QUERY = 8;
  private static final int METHODID_LIST_COLLECTION_IDS = 9;
  private static final int METHODID_CREATE_DOCUMENT = 10;
  private static final int METHODID_BATCH_WRITE = 11;
  private static final int METHODID_WRITE = 12;
  private static final int METHODID_LISTEN = 13;

  private static final class MethodHandlers<Req, Resp>
      implements io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
          io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
          io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
          io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final FirestoreImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(FirestoreImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_DOCUMENT:
          serviceImpl.getDocument(
              (com.google.firestore.v1.GetDocumentRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.v1.Document>) responseObserver);
          break;
        case METHODID_LIST_DOCUMENTS:
          serviceImpl.listDocuments(
              (com.google.firestore.v1.ListDocumentsRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.v1.ListDocumentsResponse>)
                  responseObserver);
          break;
        case METHODID_UPDATE_DOCUMENT:
          serviceImpl.updateDocument(
              (com.google.firestore.v1.UpdateDocumentRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.v1.Document>) responseObserver);
          break;
        case METHODID_DELETE_DOCUMENT:
          serviceImpl.deleteDocument(
              (com.google.firestore.v1.DeleteDocumentRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_BATCH_GET_DOCUMENTS:
          serviceImpl.batchGetDocuments(
              (com.google.firestore.v1.BatchGetDocumentsRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.v1.BatchGetDocumentsResponse>)
                  responseObserver);
          break;
        case METHODID_BEGIN_TRANSACTION:
          serviceImpl.beginTransaction(
              (com.google.firestore.v1.BeginTransactionRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.v1.BeginTransactionResponse>)
                  responseObserver);
          break;
        case METHODID_COMMIT:
          serviceImpl.commit(
              (com.google.firestore.v1.CommitRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.v1.CommitResponse>)
                  responseObserver);
          break;
        case METHODID_ROLLBACK:
          serviceImpl.rollback(
              (com.google.firestore.v1.RollbackRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_RUN_QUERY:
          serviceImpl.runQuery(
              (com.google.firestore.v1.RunQueryRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.v1.RunQueryResponse>)
                  responseObserver);
          break;
        case METHODID_LIST_COLLECTION_IDS:
          serviceImpl.listCollectionIds(
              (com.google.firestore.v1.ListCollectionIdsRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.v1.ListCollectionIdsResponse>)
                  responseObserver);
          break;
        case METHODID_CREATE_DOCUMENT:
          serviceImpl.createDocument(
              (com.google.firestore.v1.CreateDocumentRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.v1.Document>) responseObserver);
          break;
        case METHODID_BATCH_WRITE:
          serviceImpl.batchWrite(
              (com.google.firestore.v1.BatchWriteRequest) request,
              (io.grpc.stub.StreamObserver<com.google.firestore.v1.BatchWriteResponse>)
                  responseObserver);
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
        case METHODID_WRITE:
          return (io.grpc.stub.StreamObserver<Req>)
              serviceImpl.write(
                  (io.grpc.stub.StreamObserver<com.google.firestore.v1.WriteResponse>)
                      responseObserver);
        case METHODID_LISTEN:
          return (io.grpc.stub.StreamObserver<Req>)
              serviceImpl.listen(
                  (io.grpc.stub.StreamObserver<com.google.firestore.v1.ListenResponse>)
                      responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private abstract static class FirestoreBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier,
          io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FirestoreBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.google.firestore.v1.FirestoreProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Firestore");
    }
  }

  private static final class FirestoreFileDescriptorSupplier
      extends FirestoreBaseDescriptorSupplier {
    FirestoreFileDescriptorSupplier() {}
  }

  private static final class FirestoreMethodDescriptorSupplier
      extends FirestoreBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    FirestoreMethodDescriptorSupplier(String methodName) {
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
      synchronized (FirestoreGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor =
              result =
                  io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                      .setSchemaDescriptor(new FirestoreFileDescriptorSupplier())
                      .addMethod(getGetDocumentMethod())
                      .addMethod(getListDocumentsMethod())
                      .addMethod(getUpdateDocumentMethod())
                      .addMethod(getDeleteDocumentMethod())
                      .addMethod(getBatchGetDocumentsMethod())
                      .addMethod(getBeginTransactionMethod())
                      .addMethod(getCommitMethod())
                      .addMethod(getRollbackMethod())
                      .addMethod(getRunQueryMethod())
                      .addMethod(getWriteMethod())
                      .addMethod(getListenMethod())
                      .addMethod(getListCollectionIdsMethod())
                      .addMethod(getCreateDocumentMethod())
                      .addMethod(getBatchWriteMethod())
                      .build();
        }
      }
    }
    return result;
  }
}
