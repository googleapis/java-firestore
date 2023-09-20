/*
 * Copyright 2017 Google LLC
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
import com.google.api.core.ApiFutures;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import java.awt.*;
import javax.annotation.Nullable;

/**
 * Helper class that facilitates integration of the Firestore SDK with OpenTelemetry Trace, Metrics,
 * and Logs APIs.
 *
 * <p>This class currently supports tracing utility functions. Metrics and Logging methods will be
 * added in the future.
 */
public interface OpenTelemetryUtil {
  String OPEN_TELEMETRY_ENV_VAR_NAME = "ENABLE_OPEN_TELEMETRY";

  class Span {
    private io.opentelemetry.api.trace.Span span;

    public Span() {
      this(null);
    }

    public Span(@Nullable io.opentelemetry.api.trace.Span span) {
      this.span = span;
    }

    void end() {
      if (span == null) return;
      span.end();
    }

    void end(Throwable error) {
      if (span == null) return;
      span.setStatus(StatusCode.ERROR, error.getMessage());
      span.recordException(
          error,
          Attributes.builder()
              .put("exception.message", error.getMessage())
              .put("exception.type", error.getClass().getName())
              .put("exception.stacktrace", Throwables.getStackTraceAsString(error))
              .build());
      span.end();
    }

    /**
     * If an operation ends in the future, its relevant span should end _after_ the future has been
     * completed. This method "appends" the span completion code at the completion of the given
     * future. In order for telemetry info to be recorded, the future returned by this method should
     * be completed.
     */
    <T> ApiFuture<T> endAtFuture(ApiFuture<T> futureValue) {
      if (span == null) return futureValue;

      return ApiFutures.transform(
          futureValue,
          value -> {
            span.end();
            return value;
          },
          MoreExecutors.directExecutor());
    }
  }

  static OpenTelemetryUtil getInstance(
      @Nullable Boolean enabled,
      @Nullable OpenTelemetrySdk openTelemetrySdk,
      FirestoreOptions firestoreOptions) {

    boolean createEnabledInstance;

    if (enabled == null) {
      // The caller did not specify whether telemetry collection should be enabled via code.
      // In such cases, we allow enabling/disabling the feature via an environment variable.
      String enableOpenTelemetryEnvVar = System.getenv(OPEN_TELEMETRY_ENV_VAR_NAME);
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
      return new EnabledOpenTelemetryUtil(openTelemetrySdk, firestoreOptions);
    } else {
      return new DisabledOpenTelemetryUtil();
    }
  }

  /** Returns a channel configurator for gRPC, or null if telemetry collection is disabled. */
  @Nullable
  ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> getChannelConfigurator();

  /** Starts a new span with the given name, sets it as the current span, and returns it. */
  @Nullable
  Span startSpan(String spanName, boolean addSettingsAttributes);

  /** Returns the OpenTelemetry tracer if enabled, and null otherwise. */
  @Nullable
  Tracer getTracer();

  /** Shuts down the underlying OpenTelemetry SDK instance, if any. */
  void close();
}
