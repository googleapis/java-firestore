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
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.opentelemetry.metric.GoogleCloudMetricExporter;
import com.google.cloud.opentelemetry.metric.MetricConfiguration;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.Status;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.View;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

/**
 * A utility interface for trace collection. Classes that implement this interface may make their
 * own design choices for how they approach trace collection. For instance, they may be no-op, or
 * they may use a particular tracing framework such as OpenTelemetry.
 */
public class EnabledMetricsUtil implements MetricsUtil {

  private final FirestoreOptions firestoreOptions;

  private static BuiltinMetricsProvider defaultOpenTelemetryMetricsProvider;
  private static BuiltinMetricsProvider customOpenTelemetryMetricsProvider;

  EnabledMetricsUtil(FirestoreOptions firestoreOptions) {
    this.firestoreOptions = firestoreOptions;
    try {
      createMetricsUtil();
    } catch (IOException e) {
      // TODO: Handle the exception appropriately (e.g., logging)
    }
  }

  private void createMetricsUtil() throws IOException {
    EnabledMetricsUtil.defaultOpenTelemetryMetricsProvider =
        new BuiltinMetricsProvider(getDefaultOpenTelemetryInstance());
    EnabledMetricsUtil.customOpenTelemetryMetricsProvider =
        new BuiltinMetricsProvider(firestoreOptions.getOpenTelemetryOptions().getOpenTelemetry());
  }

  private OpenTelemetry getDefaultOpenTelemetryInstance() throws IOException {

    SdkMeterProviderBuilder sdkMeterProviderBuilder = SdkMeterProvider.builder();

    // Filter out attributes that are not defined
    for (Map.Entry<InstrumentSelector, View> entry : getAllViews().entrySet()) {
      sdkMeterProviderBuilder.registerView(entry.getKey(), entry.getValue());
    }

    MetricExporter metricExporter =
        GoogleCloudMetricExporter.createWithConfiguration(
            MetricConfiguration.builder()
                .setProjectId(firestoreOptions.getProjectId())
                .setInstrumentationLibraryLabelsEnabled(false)
                // .setMonitoredResourceDescription((null))
                // .setUseServiceTimeSeries(false)
                .build());

    sdkMeterProviderBuilder.registerMetricReader(PeriodicMetricReader.create(metricExporter));
    return OpenTelemetrySdk.builder().setMeterProvider(sdkMeterProviderBuilder.build()).build();
  }

  @Override
  public MetricsContext createMetricsContext(String methodName) {
    return new MetricsContext(methodName);
  }

  @Override
  public void addMetricsTracerFactory(List<ApiTracerFactory> apiTracerFactories) {
    ApiTracerFactory defaultMetricsTracerFactory =
        defaultOpenTelemetryMetricsProvider.getOpenTelemetryApiTracerFactory();
    ApiTracerFactory customMetricsTracerFactory =
        customOpenTelemetryMetricsProvider.getOpenTelemetryApiTracerFactory();
    if (defaultMetricsTracerFactory != null) {
      apiTracerFactories.add(defaultMetricsTracerFactory);
    }
    if (customMetricsTracerFactory != null) {
      apiTracerFactories.add(customMetricsTracerFactory);
    }
  }

  static class MetricsContext implements MetricsUtil.MetricsContext {
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
      Map<String, String> attributes = new HashMap<>();
      attributes.put(METHOD_KEY.toString(), methodName);
      attributes.put(STATUS_KEY.toString(), status);

      defaultOpenTelemetryMetricsProvider.endToEndRequestLatencyRecorder(elapsedTime, attributes);
      customOpenTelemetryMetricsProvider.endToEndRequestLatencyRecorder(elapsedTime, attributes);
    }

    public void recordFirstResponseLatency() {
      double elapsedTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
      Map<String, String> attributes = new HashMap<>();
      attributes.put(METHOD_KEY.toString(), methodName);
      attributes.put(STATUS_KEY.toString(), StatusCode.Code.OK.toString());

      defaultOpenTelemetryMetricsProvider.firstResponseLatencyRecorder(elapsedTime, attributes);
      customOpenTelemetryMetricsProvider.firstResponseLatencyRecorder(elapsedTime, attributes);
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
