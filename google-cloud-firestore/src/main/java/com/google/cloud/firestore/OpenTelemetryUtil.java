/*
 * Copyright 2023 Google LLC
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

import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import io.opentelemetry.api.common.Attributes;
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Helper class that facilitates integration of the Firestore SDK with OpenTelemetry Trace, Metrics,
 * and Logs APIs.
 *
 * <p>This class currently supports tracing utility functions. Metrics and Logging methods will be
 * added in the future.
 */
public interface OpenTelemetryUtil {
  String ENABLE_OPEN_TELEMETRY_ENV_VAR_NAME = "ENABLE_OPEN_TELEMETRY";
  String OPEN_TELEMETRY_TRACE_SAMPLING_RATE_ENV_VAR_NAME = "OPEN_TELEMETRY_TRACE_SAMPLING_RATE";
  static final String SERVICE = "Firestore";
  static final String LIBRARY_NAME = "com.google.cloud.firestore";
  static final String SPAN_NAME_DOC_REF_CREATE = "DocumentReference.Create";
  static final String SPAN_NAME_DOC_REF_SET = "DocumentReference.Set";
  static final String SPAN_NAME_DOC_REF_UPDATE = "DocumentReference.Update";
  static final String SPAN_NAME_DOC_REF_DELETE = "DocumentReference.Delete";
  static final String SPAN_NAME_DOC_REF_GET = "DocumentReference.Get";
  static final String SPAN_NAME_DOC_REF_LIST_COLLECTIONS = "DocumentReference.ListCollections";
  static final String SPAN_NAME_COL_REF_ADD = "CollectionReference.Add";
  static final String SPAN_NAME_COL_REF_LIST_DOCUMENTS = "CollectionReference.ListDocuments";
  static final String SPAN_NAME_QUERY_GET = "Query.Get";
  static final String SPAN_NAME_AGGREGATION_QUERY_GET = "AggregationQuery.Get";
  static final String SPAN_NAME_RUN_QUERY = "RunQuery";
  static final String SPAN_NAME_RUN_AGGREGATION_QUERY = "RunAggregationQuery";
  static final String SPAN_NAME_BATCH_GET_DOCUMENTS = "BatchGetDocuments";
  static final String SPAN_NAME_TRANSACTION_BEGIN = "BeginTransaction";
  static final String SPAN_NAME_TRANSACTION_GET_QUERY = "Transaction.Get.Query";
  static final String SPAN_NAME_TRANSACTION_GET_DOCUMENT = "Transaction.Get.Document";
  static final String SPAN_NAME_TRANSACTION_GET_DOCUMENTS = "Transaction.Get.Documents";
  static final String SPAN_NAME_TRANSACTION_ROLLBACK = "Rollback";

  // TODO. WriteBatch.commit.
  static final String SPAN_NAME_BATCH_COMMIT = "Batch.Commit";
  static final String SPAN_NAME_TRANSACTION_COMMIT = "Transaction.Commit";
  static final String SPAN_NAME_PARTITIONQUERY = "partitionQuery";
  // For BulkWriter.
  static final String SPAN_NAME_BATCHWRITE = "BatchWrite";


  /** Sampling rate of 10% is chosen for traces by default. */
  double DEFAULT_TRACE_SAMPLING_RATE = 0.1;

  interface Span {
    /** Ends this span. */
    void end();

    /** Ends this span in an error. */
    void end(Throwable error);

    /**
     * If an operation ends in the future, its relevant span should end _after_ the future has been
     * completed. This method "appends" the span completion code at the completion of the given
     * future. In order for telemetry info to be recorded, the future returned by this method should
     * be completed.
     */
    <T> void endAtFuture(ApiFuture<T> futureValue);

    /** Adds the given event to this span. */
    Span addEvent(String name);

    /** Adds the given event with the given attributes to this span. */
    // TODO: Can we avoid using the Attributes object (is there any overhead)?
    Span addEvent(String name, Attributes attributes);

    /** Adds the given attribute to this span */
    Span setAttribute(String key, int value);

    Scope makeCurrent();
  }

  /**
   * Returns a channel configurator for gRPC, or {@code null} if telemetry collection is disabled.
   */
  @Nullable
  ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> getChannelConfigurator();

  /** Starts a new span with the given name, sets it as the current span, and returns it. */
  @Nullable
  Span startSpan(String spanName, boolean addSettingsAttributes);

  /** Returns the OpenTelemetry tracer if enabled, and {@code null} otherwise. */
  @Nullable
  Tracer getTracer();

  /** Returns the current span. */
  @Nullable
  Span currentSpan();

  /** Shuts down the underlying OpenTelemetry SDK instance, if any. */
  void close();

  /**
   * Creates and returns an instance of the OpenTelemetryUtil class.
   *
   * @param firestoreOptions The FirestoreOptions object that is requesting an instance of
   *     OpenTelemetryUtil.
   * @return An instance of the OpenTelemetryUtil class.
   */
  static OpenTelemetryUtil getInstance(@Nonnull FirestoreOptions firestoreOptions) {
    boolean createEnabledInstance;

    FirestoreOpenTelemetryOptions openTelemetryOptions = firestoreOptions.getOpenTelemetryOptions();

    if (openTelemetryOptions == null) {
      return new DisabledOpenTelemetryUtil();
    }

    Boolean enabled = openTelemetryOptions.getEnabled();
    if (enabled == null) {
      // The caller did not specify whether telemetry collection should be enabled via code.
      // In such cases, we allow enabling/disabling the feature via an environment variable.
      String enableOpenTelemetryEnvVar = System.getenv(ENABLE_OPEN_TELEMETRY_ENV_VAR_NAME);
      if (enableOpenTelemetryEnvVar != null
          && (enableOpenTelemetryEnvVar.toLowerCase().equals("true")
              || enableOpenTelemetryEnvVar.toLowerCase().equals("on"))) {
        createEnabledInstance = true;
      } else {
        createEnabledInstance = false;
      }
    } else {
      createEnabledInstance = enabled;
    }

    if (createEnabledInstance) {
      return new EnabledOpenTelemetryUtil(firestoreOptions);
    } else {
      return new DisabledOpenTelemetryUtil();
    }
  }
}
