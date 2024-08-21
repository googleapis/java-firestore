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

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.MetricsTracerFactory;
import com.google.api.gax.tracing.OpenTelemetryMetricsRecorder;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.opentelemetry.metric.GoogleCloudMetricExporter;
import com.google.cloud.opentelemetry.metric.MetricConfiguration;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.Status;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.View;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/**
 * A utility interface for trace collection. Classes that implement this interface may make their
 * own design choices for how they approach trace collection. For instance, they may be no-op, or
 * they may use a particular tracing framework such as OpenTelemetry.
 */
public class MetricsUtil {
  static final String FIRESTORE_METER_NAME = "firestore_java";

  // TODO: change to firestore.googleapis.com
  public static final String METER_NAME = "custom.googleapis.com/internal/client";
  public static final String GAX_METER_NAME = OpenTelemetryMetricsRecorder.GAX_METER_NAME;

  // Metric attribute keys for monitored resource
  static final AttributeKey<String> PROJECT_ID_KEY = AttributeKey.stringKey("project_id");
  // public static final AttributeKey<String> INSTANCE_ID_KEY = AttributeKey.stringKey("instance");

  // Metric attribute keys for labels
  // static final AttributeKey<String> LANGUAGE_KEY = AttributeKey.stringKey("language");
  static final AttributeKey<String> METHOD_NAME_KEY = AttributeKey.stringKey("method_name");
  static final AttributeKey<String> STATUS_KEY = AttributeKey.stringKey("status");
  public static final AttributeKey<String> DATABASE_ID_KEY = AttributeKey.stringKey("database_id");
  static final AttributeKey<String> CLIENT_LIBRARY_KEY = AttributeKey.stringKey("client_library");
  static final AttributeKey<String> LIBRARY_VERSION_KEY = AttributeKey.stringKey("library_version");
  static final AttributeKey<String> CLIENT_UID_KEY = AttributeKey.stringKey("client_uid");

  static final AttributeKey<String> Language_KEY = AttributeKey.stringKey("language");
  public static final AttributeKey<String> SERVICE_NAME = AttributeKey.stringKey("service.name");
  // Metric
  static final String OPERATION_LATENCY_NAME = "operation_latency";
  static final String OPERATION_COUNT_NAME = "operation_count";
  static final String ATTEMPT_LATENCY_NAME = "attempt_latency";
  static final String ATTEMPT_COUNT_NAME = "attempt_count";
  static final String FIRST_RESPONSE_LATENCY_NAME = "first_response_latency";
  static final String END_TO_END_LATENCY_NAME = "end_to_end_latency";
  static final String TRANSACTION_LATENCY_NAME = "transaction_latency";
  static final String TRANSACTION_ATTEMPT_COUNT_NAME = "transaction_attempt_count";

  static final String MILLISECOND_UNIT = "ms";

  static final String ENABLE_METRICS_ENV_VAR = "FIRESTORE_ENABLE_TRACING";

  public static final Set<AttributeKey> COMMON_ATTRIBUTES =
      ImmutableSet.of(
          PROJECT_ID_KEY,
          DATABASE_ID_KEY,
          CLIENT_UID_KEY,
          STATUS_KEY,
          CLIENT_LIBRARY_KEY,
          LIBRARY_VERSION_KEY);

  public static final Set<String> BUILTIN_METRICS =
      ImmutableSet.of(
              OPERATION_LATENCY_NAME,
              ATTEMPT_LATENCY_NAME,
              OPERATION_COUNT_NAME,
              ATTEMPT_COUNT_NAME,
              FIRST_RESPONSE_LATENCY_NAME,
              END_TO_END_LATENCY_NAME,
              TRANSACTION_LATENCY_NAME,
              TRANSACTION_ATTEMPT_COUNT_NAME)
          .stream()
          .map(m -> METER_NAME + '/' + m)
          .collect(Collectors.toSet());

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
      createMetricsUtil();
    }
  }

  private void createMetricsUtil() {
    this.openTelemetry = getDefaultOpenTelemetryInstance();

    OpenTelemetryMetricsRecorder recorder =
        new OpenTelemetryMetricsRecorder(openTelemetry, METER_NAME);

    this.otelApiTracerFactory = new MetricsTracerFactory(recorder);

    registerMetrics();
  }

  void registerMetrics() {
    this.meter = openTelemetry.getMeter(FIRESTORE_METER_NAME);

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

  private OpenTelemetry getDefaultOpenTelemetryInstance() {
    OpenTelemetry openTelemetry = firestoreOptions.getOpenTelemetryOptions().getOpenTelemetry();

    // If metrics is enabled, but an OpenTelemetry instance is not provided, create a default OTel
    // instance.
    if (openTelemetry == null) {
      MetricExporter metricExporter =
          GoogleCloudMetricExporter.createWithConfiguration(
              MetricConfiguration.builder().setProjectId(firestoreOptions.getProjectId()).build());

      SdkMeterProviderBuilder sdkMeterProviderBuilder = SdkMeterProvider.builder();

      // Remove "Language" attributes from GAX metrics
      for (Map.Entry<InstrumentSelector, View> entry : getAllViews().entrySet()) {
        sdkMeterProviderBuilder.registerView(entry.getKey(), entry.getValue());
      }

      // This adds to Metrics.Resource.Attributes, doesn't show up on GC monitoring dashboard.
      Package pkg = this.getClass().getPackage();
      Resource resource =
          Resource.getDefault()
              .merge(
                  Resource.builder()
                      .put(CLIENT_LIBRARY_KEY, FIRESTORE_METER_NAME)
                      .put(LIBRARY_VERSION_KEY, pkg.getImplementationVersion())
                      .build());
      sdkMeterProviderBuilder.setResource(resource);

      SdkMeterProvider meterProvider =
          sdkMeterProviderBuilder
              .registerMetricReader(PeriodicMetricReader.builder(metricExporter).build())
              .build();

      openTelemetry = OpenTelemetrySdk.builder().setMeterProvider(meterProvider).build();
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

  static void defineView(
      ImmutableMap.Builder<InstrumentSelector, View> viewMap,
      String MeterName,
      String id,
      Set<AttributeKey> attributes) {
    InstrumentSelector selector =
        InstrumentSelector.builder().setName(id).setMeterName(MeterName).build();
    Set<String> attributesFilter =
        ImmutableSet.<String>builder()
            .addAll(attributes.stream().map(AttributeKey::getKey).collect(Collectors.toSet()))
            .build();
    View view = View.builder().setName(id).setAttributeFilter(attributesFilter).build();

    viewMap.put(selector, view);
  }

  public static Map<InstrumentSelector, View> getAllViews() {
    ImmutableMap.Builder<InstrumentSelector, View> views = ImmutableMap.builder();

    defineView(
        views,
        GAX_METER_NAME,
        OPERATION_LATENCY_NAME,
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(METHOD_NAME_KEY)
            .build());
    defineView(
        views,
        GAX_METER_NAME,
        ATTEMPT_LATENCY_NAME,
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(METHOD_NAME_KEY)
            .build());

    defineView(
        views,
        GAX_METER_NAME,
        OPERATION_COUNT_NAME,
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(METHOD_NAME_KEY)
            .build());

    defineView(
        views,
        GAX_METER_NAME,
        ATTEMPT_COUNT_NAME,
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(METHOD_NAME_KEY)
            .build());

    defineView(
        views,
        FIRESTORE_METER_NAME,
        FIRST_RESPONSE_LATENCY_NAME,
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(METHOD_NAME_KEY)
            .build());

    defineView(
        views,
        FIRESTORE_METER_NAME,
        END_TO_END_LATENCY_NAME,
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(METHOD_NAME_KEY)
            .build());
    return views.build();
  }
}
