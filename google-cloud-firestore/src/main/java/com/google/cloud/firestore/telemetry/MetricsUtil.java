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
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.MetricsTracerFactory;
import com.google.api.gax.tracing.OpenTelemetryMetricsRecorder;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.Status;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * A utility interface for trace collection. Classes that implement this interface may make their
 * own design choices for how they approach trace collection. For instance, they may be no-op, or
 * they may use a particular tracing framework such as OpenTelemetry.
 */
public class MetricsUtil {

  private final FirestoreOptions firestoreOptions;
  private OpenTelemetry openTelemetry;
  private ApiTracerFactory otelApiTracerFactory;

  private Meter meter;
  private DoubleHistogram endToEndRequestLatency;
  private DoubleHistogram firstResponseLatency;

  /**
   * Creates and returns an instance of the MetricsUtil class.
   *
   * @param firestoreOptions The FirestoreOptions object that is requesting an instance of
   *     MetricsUtil.
   * @return An instance of the MetricsUtil class.
   */
  public MetricsUtil(FirestoreOptions firestoreOptions) {
    this.firestoreOptions = firestoreOptions;

    boolean createEnabledInstance = firestoreOptions.getOpenTelemetryOptions().isMetricsEnabled();

    // The environment variable can override options to enable/disable telemetry collection.
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

    if (createEnabledInstance) {
      try {
        createMetricsUtil(firestoreOptions);
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }

  private void createMetricsUtil(FirestoreOptions firestoreOptions) throws IOException {
    this.openTelemetry = getDefaultOpenTelemetryInstance();

    OpenTelemetryMetricsRecorder recorder =
        new OpenTelemetryMetricsRecorder(openTelemetry, METER_NAME);

    Map<String, String> attributes = new HashMap<>();
    attributes.put(DATABASE_ID_KEY.toString(), firestoreOptions.getDatabaseId());
    attributes.put(CLIENT_LIBRARY_KEY.toString(), FIRESTORE_LIBRARY_NAME);
    String pkgVersion = this.getClass().getPackage().getImplementationVersion();
    if (pkgVersion != null) {
      attributes.put(LIBRARY_VERSION_KEY.toString(), pkgVersion);
    }

    this.otelApiTracerFactory = new MetricsTracerFactory(recorder, attributes);
    registerMetrics();
  }

  void registerMetrics() {
    this.meter = openTelemetry.getMeter(FIRESTORE_LIBRARY_NAME);

    this.endToEndRequestLatency =
        meter
            .histogramBuilder(METER_NAME + "/" + END_TO_END_LATENCY_NAME)
            .setDescription("Firestore E2E metrics")
            .setUnit(MILLISECOND_UNIT)
            .build();

    this.firstResponseLatency =
        meter
            .histogramBuilder(METER_NAME + "/" + FIRST_RESPONSE_LATENCY_NAME)
            .setDescription("Firestore query first response latency")
            .setUnit(MILLISECOND_UNIT)
            .build();
  }

  public void endToEndRequestLatencyRecorder(double latency, Map<String, String> attributes) {
    if (endToEndRequestLatency != null) {
      endToEndRequestLatency.record(latency, toOtelAttributes(attributes));
    }
  }

  public void firstResponseLatencyRecorder(double latency, Map<String, String> attributes) {
    if (firstResponseLatency != null) {
      firstResponseLatency.record(latency, toOtelAttributes(attributes));
    }
  }

  private OpenTelemetry getDefaultOpenTelemetryInstance() throws IOException {
    OpenTelemetry openTelemetry = firestoreOptions.getOpenTelemetryOptions().getOpenTelemetry();

    // If metrics is enabled, but an OpenTelemetry instance is not provided, create a default OTel
    // instance.
    if (openTelemetry == null) {
      SdkMeterProviderBuilder sdkMeterProviderBuilder = SdkMeterProvider.builder();

      BuiltinMetricsView.registerBuiltinMetrics(
          firestoreOptions.getProjectId(), sdkMeterProviderBuilder);

      openTelemetry =
          OpenTelemetrySdk.builder().setMeterProvider(sdkMeterProviderBuilder.build()).build();
    }

    return openTelemetry;
  }

  public ApiTracerFactory getOpenTelemetryApiTracerFactory() {
    return this.otelApiTracerFactory;
  }

  @VisibleForTesting
  Attributes toOtelAttributes(Map<String, String> attributes) {
    AttributesBuilder attributesBuilder = Attributes.builder();
    attributes.forEach(attributesBuilder::put);
    return attributesBuilder.build();
  }

  public <T> void endAtFuture(
      ApiFuture<T> futureValue, double start, Map<String, String> attributes) {
    ApiFutures.addCallback(
        futureValue,
        new ApiFutureCallback<T>() {
          @Override
          public void onFailure(Throwable t) {
            double end = System.currentTimeMillis();
            double elapsedTime = end - start;
            attributes.put("status", extractErrorStatus(t));
            endToEndRequestLatencyRecorder(elapsedTime, attributes);
          }

          @Override
          public void onSuccess(T result) {
            double end = System.currentTimeMillis();
            double elapsedTime = end - start;
            attributes.put("status", "OK");
            endToEndRequestLatencyRecorder(elapsedTime, attributes);
          }
        },
        MoreExecutors.directExecutor());
  }

  public void end(Throwable t, double start, Map<String, String> attributes) {
    attributes.put("status", extractErrorStatus(t));
    double end = System.currentTimeMillis();
    double elapsedTime = end - start;
    endToEndRequestLatencyRecorder(elapsedTime, attributes);
  }

  /** Function to extract the status of the error as a string */
  public static String extractErrorStatus(@Nullable Throwable throwable) {
    if (!(throwable instanceof FirestoreException)) {
      return StatusCode.Code.UNKNOWN.toString();
    }
    Status status = ((FirestoreException) throwable).getStatus();

    return status.getCode().name();
  }
}
