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
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.OpenTelemetrySdk;
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
    <T> ApiFuture<T> endAtFuture(ApiFuture<T> futureValue);

    /** Adds the given event to this span. */
    Span addEvent(String name);
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
   * @param enabled Whether telemetry should be collected. Can be {@code null} if unspecified.
   * @param openTelemetrySdk An instance of the OpenTelemetrySdk that should be used. Can be {@code
   *     null} if unspecified.
   * @param firestoreOptions The FirestoreOptions object that is requesting an instance of
   *     OpenTelemetryUtil.
   * @return An instance of the OpenTelemetryUtil class.
   */
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
}
