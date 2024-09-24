/*
 * Copyright 2024 Google LLC
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

package com.google.cloud.firestore.telemetry;

import static com.google.cloud.firestore.telemetry.BuiltinMetricsConstants.*;

import com.google.api.core.ApiFuture;
import com.google.api.core.InternalApi;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.cloud.firestore.FirestoreOptions;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * A utility interface for trace collection. Classes that implement this interface may make their
 * own design choices for how they approach trace collection. For instance, they may be no-op, or
 * they may use a particular tracing framework such as OpenTelemetry.
 */
@InternalApi
public interface MetricsUtil {

  static MetricsUtil getInstance(@Nonnull FirestoreOptions firestoreOptions) {
    boolean isBuiltInMetricsEnabled = createEnabledInstance(firestoreOptions);

    if (isBuiltInMetricsEnabled) {
      return new EnabledMetricsUtil(firestoreOptions);
    } else {
      return new DisabledMetricsUtil();
    }
  }

  static boolean createEnabledInstance(FirestoreOptions firestoreOptions) {
    // Start with the value from FirestoreOptions
    boolean createEnabledInstance = firestoreOptions.getOpenTelemetryOptions().isMetricsEnabled();

    // Override based on the environment variable
    String enableMetricsEnvVar = System.getenv(ENABLE_METRICS_ENV_VAR);
    if (enableMetricsEnvVar != null) {
      if (enableMetricsEnvVar.equalsIgnoreCase("true")
          || enableMetricsEnvVar.equalsIgnoreCase("on")) {
        createEnabledInstance = true;
      }
      if (enableMetricsEnvVar.equalsIgnoreCase("false")
          || enableMetricsEnvVar.equalsIgnoreCase("off")) {
        createEnabledInstance = false;
      }
    }

    return createEnabledInstance;
  }

  abstract MetricsContext createMetricsContext(String methodName);

  abstract void addMetricsTracerFactory(List<ApiTracerFactory> apiTracerFactories);

  interface MetricsContext {

    /**
     * If an operation ends in the future, its relevant metrics should be recorded _after_ the
     * future has been completed. This method "appends" the metrics recording code at the completion
     * of the given future.
     */
    <T> void recordEndToEndLatencyAtFuture(ApiFuture<T> futureValue);

    void recordEndToEndLatency();

    void recordEndToEndLatency(Throwable t);

    /** Records first response latency for the current operation. */
    void recordFirstResponseLatency();
  }
}
