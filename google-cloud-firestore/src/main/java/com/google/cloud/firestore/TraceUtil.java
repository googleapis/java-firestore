/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.firestore;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.firestore.spi.v1.GrpcFirestoreRpc;
import io.opencensus.contrib.grpc.util.StatusConverter;
import io.opencensus.trace.EndSpanOptions;
import io.opencensus.trace.Span;
import io.opencensus.trace.Status;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;

/**
 * Helper class for tracing utility. It is used for instrumenting {@link GrpcFirestoreRpc} with
 * OpenCensus APIs.
 *
 * <p>TraceUtil instances are created by the {@link TraceUtil#getInstance()} method.
 */
final class TraceUtil {

  private final Tracer tracer = Tracing.getTracer();
  private static final TraceUtil traceUtil = new TraceUtil();
  static final String SPAN_NAME_GETDOCUMENT = "CloudFirestoreOperation.GetDocument";
  static final String SPAN_NAME_CREATEDOCUMENT = "CloudFirestoreOperation.CreateDocument";
  static final String SPAN_NAME_UPDATEDOCUMENT = "CloudFirestoreOperation.UpdateDocument";
  static final String SPAN_NAME_DELETEDOCUMENT = "CloudFirestoreOperation.DeleteDocument";
  static final String SPAN_NAME_LISTCOLLECTIONIDS = "CloudFirestoreOperation.ListCollectionIds";
  static final String SPAN_NAME_LISTDOCUMENTS = "CloudFirestoreOperation.ListDocuments";
  static final String SPAN_NAME_BEGINTRANSACTION = "CloudFirestoreOperation.BeginTransaction";
  static final String SPAN_NAME_COMMIT = "CloudFirestoreOperation.Commit";
  static final String SPAN_NAME_ROLLBACK = "CloudFirestoreOperation.Rollback";
  static final String SPAN_NAME_RUNQUERY = "CloudFirestoreOperation.RunQuery";
  static final String SPAN_NAME_PARTITIONQUERY = "CloudFirestoreOperation.partitionQuery";
  static final String SPAN_NAME_LISTEN = "CloudFirestoreOperation.Listen";
  static final String SPAN_NAME_BATCHGETDOCUMENTS = "CloudFirestoreOperation.BatchGetDocuments";
  static final String SPAN_NAME_BATCHWRITE = "CloudFirestoreOperation.BatchWrite";
  static final String SPAN_NAME_WRITE = "CloudFirestoreOperation.Write";

  static final EndSpanOptions END_SPAN_OPTIONS =
      EndSpanOptions.builder().setSampleToLocalSpanStore(true).build();

  /**
   * Starts a new span.
   *
   * @param spanName The name of the returned Span.
   * @return The newly created {@link Span}.
   */
  protected Span startSpan(String spanName) {
    return tracer.spanBuilder(spanName).startSpan();
  }

  /**
   * Return the global {@link Tracer}.
   *
   * @return The global {@link Tracer}.
   */
  public Tracer getTracer() {
    return tracer;
  }

  /**
   * Return TraceUtil Object.
   *
   * @return An instance of {@link TraceUtil}
   */
  public static TraceUtil getInstance() {
    return traceUtil;
  }

  private TraceUtil() {}

  public static Status statusFromApiException(ApiException exception) {
    if (exception.getStatusCode().getTransportCode() instanceof io.grpc.Status) {
      io.grpc.Status grpcStatus = (io.grpc.Status) exception.getStatusCode().getTransportCode();
      return StatusConverter.fromGrpcStatus(grpcStatus);
    }

    return Status.UNKNOWN.withDescription(exception.getMessage());
  }
}
