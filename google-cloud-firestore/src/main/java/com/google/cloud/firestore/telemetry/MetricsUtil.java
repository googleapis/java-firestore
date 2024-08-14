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

import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.MetricsTracerFactory;
import com.google.api.gax.tracing.OpenTelemetryMetricsRecorder;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.opentelemetry.metric.GoogleCloudMetricExporter;
import com.google.cloud.opentelemetry.metric.MetricConfiguration;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;

/**
 * A utility interface for trace collection. Classes that implement this interface may make their
 * own design choices for how they approach trace collection. For instance, they may be no-op, or
 * they may use a particular tracing framework such as OpenTelemetry.
 */
public class MetricsUtil {
  static final String FIRESTORE_LIBRARY_NAME = "firestore_java";
  static final String METRICS_NAMESPACE = "custom.googleapis.com";
  public static final String METRICS_INTERNAL = "internal";

  // Metric attribute keys for monitored resource
  static final AttributeKey<String> PROJECT_ID_KEY = AttributeKey.stringKey("project_id");
  // public static final AttributeKey<String> INSTANCE_ID_KEY = AttributeKey.stringKey("instance");

  // Metric attribute keys for labels
  static final AttributeKey<String> LANGUAGE_KEY = AttributeKey.stringKey("language");
  static final AttributeKey<String> METHOD_NAME_KEY = AttributeKey.stringKey("method_name");
  static final AttributeKey<String> STATUS_KEY = AttributeKey.stringKey("status");
  public static final AttributeKey<String> DATABASE_ID_KEY = AttributeKey.stringKey("database_id");

  static final AttributeKey<String> CLIENT_UID_KEY = AttributeKey.stringKey("client_uid");

  // Metric
  static final String OPERATION_LATENCIES_NAME = "operation_latencies";
  static final String OPERATION_COUNT_NAME = "operation_count";
  static final String ATTEMPT_LATENCIES_NAME = "attempt_latencies";
  static final String ATTEMPT_COUNT_NAME = "attempt_count";
  static final String FIRST_RESPONSE_LATENCIES_NAME = "first_response_latencies";
  static final String END_TO_END_LATENCIES_NAME = "end_to_end_latencies";
  static final String TRANSACTION_LATENCIES_NAME = "transaction_latencies";
  static final String TRANSACTION_ATTEMPT_COUNT_NAME = "transaction_attempt_count";

  static final String MILLISECOND_UNIT = "ms";

  static final String ENABLE_METRICS_ENV_VAR = "FIRESTORE_ENABLE_TRACING";

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
      createMtericsUtil();
    }
  }

  private void createMtericsUtil() {
    this.openTelemetry = getDefaultOpenTelemetryInstance();

    OpenTelemetryMetricsRecorder recorder =
        new OpenTelemetryMetricsRecorder(openTelemetry, METRICS_INTERNAL);

    this.otelApiTracerFactory = new MetricsTracerFactory(recorder);
    
    registerMetrics();
  }

  void registerMetrics() {
    Package pkg = this.getClass().getPackage();
    this.meter =
        openTelemetry
            .meterBuilder(FIRESTORE_LIBRARY_NAME)
            .setInstrumentationVersion(pkg.getImplementationVersion())
            .build();

    this.endToEndRequestLatency =
        meter
            .histogramBuilder(METRICS_INTERNAL+"/"+END_TO_END_LATENCIES_NAME)
            .setDescription("Firestore E2E metrics")
            .setUnit(MILLISECOND_UNIT)
            .build();

    this.firstResponseLatency =
        meter
            .histogramBuilder(METRICS_INTERNAL+"/"+FIRST_RESPONSE_LATENCIES_NAME)
            .setDescription("Firestore query first response latency")
            .setUnit(MILLISECOND_UNIT)
            .build();
  }

  private OpenTelemetry getDefaultOpenTelemetryInstance() {
    OpenTelemetry openTelemetry = firestoreOptions.getOpenTelemetryOptions().getOpenTelemetry();

    // If metrics is enabled, but an OpenTelemetry instance is not provided, create a default OTel
    // instance.
    if (openTelemetry == null) {
      MetricExporter metricExporter =
          GoogleCloudMetricExporter.createWithConfiguration(
              MetricConfiguration.builder()
                  .setProjectId(firestoreOptions.getProjectId())
                  .setPrefix(METRICS_NAMESPACE)
                  .build());

      SdkMeterProvider METER_PROVIDER =
          SdkMeterProvider.builder()
              .registerMetricReader(PeriodicMetricReader.builder(metricExporter).build())
              .build();

      openTelemetry = OpenTelemetrySdk.builder().setMeterProvider(METER_PROVIDER).build();
    }
    return openTelemetry;
  }

  public ApiTracerFactory getOpenTelemetryApiTracerFactory() {
    return this.otelApiTracerFactory;
  }
}
