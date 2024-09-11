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
import com.google.common.base.Stopwatch;
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
import java.util.concurrent.TimeUnit;
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

    // TODO(mila): this checking should be done inside FirestoreOptions builder, so that we can
    // setApiTracerFactory
    // to collect GAX layer metrics
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

    // TODO(mila): re-assess if it should follow tracing's design: enabled/disabled MetricsUtil
    if (createEnabledInstance) {
      try {
        createMetricsUtil(firestoreOptions);
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }

  private void createMetricsUtil(FirestoreOptions firestoreOptions) throws IOException {
    // TODO(mila): re-locate this create otel instance logic to FirestoreOptions, as we might need
    // 2 different OTELs based on customers config
    this.openTelemetry = getDefaultOpenTelemetryInstance();

    OpenTelemetryMetricsRecorder recorder =
        new OpenTelemetryMetricsRecorder(openTelemetry, METER_NAME);

    this.otelApiTracerFactory = new MetricsTracerFactory(recorder, createStaticAttributes());
    registerMetrics();
  }

  private Map<String, String> createStaticAttributes() {
    Map<String, String> staticAttributes = new HashMap<>();
    // TODO(mila): add client_uid to static attributes
    staticAttributes.put(DATABASE_ID_KEY.toString(), firestoreOptions.getDatabaseId());
    staticAttributes.put(CLIENT_LIBRARY_KEY.toString(), FIRESTORE_LIBRARY_NAME);
    String pkgVersion = this.getClass().getPackage().getImplementationVersion();
    if (pkgVersion != null) {
      staticAttributes.put(LIBRARY_VERSION_KEY.toString(), pkgVersion);
    }

    return staticAttributes;
  }

  @VisibleForTesting
  Attributes toOtelAttributes(Map<String, String> attributes) {
    AttributesBuilder attributesBuilder = Attributes.builder();
    attributes.forEach(attributesBuilder::put);
    return attributesBuilder.build();
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
    // TODO(mila): add transaction latency and retry count metrics
  }

  public void endToEndRequestLatencyRecorder(double latency, Attributes attributes) {
    if (endToEndRequestLatency != null) {
      endToEndRequestLatency.record(latency, attributes);
    }
  }

  public void firstResponseLatencyRecorder(double latency, Attributes attributes) {
    if (firstResponseLatency != null) {
      firstResponseLatency.record(latency, attributes);
    }
  }

  public MetricsContext createMetricsContext(String methodName) {
    return new MetricsContext(methodName);
  }

  public class MetricsContext {
    private final Stopwatch stopwatch;
    private final String methodName;

    public MetricsContext(String methodName) {
      this.stopwatch = Stopwatch.createStarted();
      this.methodName = methodName;
    }

    public <T> void recordEndToEndLatencyAtFuture(ApiFuture<T> futureValue) {
      ApiFutures.addCallback(
          futureValue,
          new ApiFutureCallback<T>() {
            @Override
            public void onFailure(Throwable t) {
              recordEndToEndLatency(t);
            }

            @Override
            public void onSuccess(T result) {
              recordEndToEndLatency();
            }
          },
          MoreExecutors.directExecutor());
    }

    public void recordEndToEndLatency() {
      recordEndToEndLatency(StatusCode.Code.OK.toString());
    }

    public void recordEndToEndLatency(Throwable t) {
      recordEndToEndLatency(extractErrorStatus(t));
    }

    private void recordEndToEndLatency(String status) {
      double elapsedTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
      Attributes attributes =
          Attributes.builder()
              .put(METHOD_KEY, methodName)
              .put(STATUS_KEY, status)
              .putAll(toOtelAttributes(createStaticAttributes()))
              .build();
      endToEndRequestLatencyRecorder(elapsedTime, attributes);
    }

    public void recordFirstResponseLatency() {
      double elapsedTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
      Attributes attributes =
          Attributes.builder()
              .put(METHOD_KEY, methodName)
              .put(STATUS_KEY, StatusCode.Code.OK.toString())
              .putAll(toOtelAttributes(createStaticAttributes()))
              .build();
      firstResponseLatencyRecorder(elapsedTime, attributes);
    }

    /** Function to extract the status of the error as a string */
    public String extractErrorStatus(@Nullable Throwable throwable) {
      if (!(throwable instanceof FirestoreException)) {
        return StatusCode.Code.UNKNOWN.toString();
      }
      Status status = ((FirestoreException) throwable).getStatus();
      return status.getCode().name();
    }
  }
}
