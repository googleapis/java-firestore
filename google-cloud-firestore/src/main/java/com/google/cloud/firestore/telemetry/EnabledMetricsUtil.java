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

import static com.google.cloud.firestore.telemetry.TelemetryConstants.METRIC_KEY_METHOD;
import static com.google.cloud.firestore.telemetry.TelemetryConstants.METRIC_KEY_STATUS;

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
import io.opentelemetry.api.GlobalOpenTelemetry;
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
import java.util.logging.Logger;
import javax.annotation.Nullable;

class EnabledMetricsUtil implements MetricsUtil {
  private BuiltinMetricsProvider defaultMetricsProvider;
  private BuiltinMetricsProvider customMetricsProvider;

  private static final Logger logger = Logger.getLogger(EnabledMetricsUtil.class.getName());

  EnabledMetricsUtil(FirestoreOptions firestoreOptions) {
    try {
      OpenTelemetry defaultOpenTelemetry =
          getDefaultOpenTelemetryInstance(firestoreOptions.getProjectId());
      this.defaultMetricsProvider = new BuiltinMetricsProvider(defaultOpenTelemetry);

      OpenTelemetry customOpenTelemetry =
          firestoreOptions.getOpenTelemetryOptions().getOpenTelemetry();
      if (customOpenTelemetry == null) {
        customOpenTelemetry = GlobalOpenTelemetry.get();
      }
      this.customMetricsProvider = new BuiltinMetricsProvider(customOpenTelemetry);
    } catch (IOException e) {
      logger.warning(
          "Unable to get create MetricsUtil object for client side metrics, will skip exporting client side metrics"
              + e);
    }
  }

  private OpenTelemetry getDefaultOpenTelemetryInstance(String projectId) throws IOException {
    SdkMeterProviderBuilder sdkMeterProviderBuilder = SdkMeterProvider.builder();

    // Filter out attributes that are not defined
    for (Map.Entry<InstrumentSelector, View> entry : TelemetryHelper.getAllViews().entrySet()) {
      sdkMeterProviderBuilder.registerView(entry.getKey(), entry.getValue());
    }

    MetricExporter metricExporter =
        GoogleCloudMetricExporter.createWithConfiguration(
            MetricConfiguration.builder()
                .setProjectId(projectId)
                // Ignore library info as it is collected by the metric attributes as well
                .setInstrumentationLibraryLabelsEnabled(false)
                .build());

    // TODO: utlize the new features on GoogleCloudMetricExporter when backend is ready
    // MonitoredResourceDescription monitoredResourceMapping =
    //     new MonitoredResourceDescription(FIRESTORE_RESOURCE_TYPE, FIRESTORE_RESOURCE_LABELS);

    // MetricExporter metricExporter =
    //     GoogleCloudMetricExporter.createWithConfiguration(
    //         MetricConfiguration.builder()
    //             .setProjectId(projectId)
    //             // Ignore library info as it is collected by the metric attributes as well
    //             .setInstrumentationLibraryLabelsEnabled(false)
    //             .setMonitoredResourceDescription(monitoredResourceMapping)
    //             .setUseServiceTimeSeries(true)
    //             .build());

    sdkMeterProviderBuilder.registerMetricReader(PeriodicMetricReader.create(metricExporter));
    return OpenTelemetrySdk.builder().setMeterProvider(sdkMeterProviderBuilder.build()).build();
  }

  @Override
  public MetricsContext createMetricsContext(String methodName) {
    return new MetricsContext(methodName);
  }

  @Override
  public void addMetricsTracerFactory(List<ApiTracerFactory> apiTracerFactories) {
    addTracerFactory(apiTracerFactories, defaultMetricsProvider);
    addTracerFactory(apiTracerFactories, customMetricsProvider);
  }

  private void addTracerFactory(
      List<ApiTracerFactory> apiTracerFactories, BuiltinMetricsProvider metricsProvider) {
    ApiTracerFactory tracerFactory = metricsProvider.getApiTracerFactory();
    if (tracerFactory != null) {
      apiTracerFactories.add(tracerFactory);
    }
  }

  class MetricsContext implements MetricsUtil.MetricsContext {
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
      Map<String, String> attributes = createAttributes(status);
      defaultMetricsProvider.endToEndRequestLatencyRecorder(elapsedTime, attributes);
      customMetricsProvider.endToEndRequestLatencyRecorder(elapsedTime, attributes);
    }

    public void recordFirstResponseLatency() {
      double elapsedTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
      Map<String, String> attributes = createAttributes(StatusCode.Code.OK.toString());
      defaultMetricsProvider.firstResponseLatencyRecorder(elapsedTime, attributes);
      customMetricsProvider.firstResponseLatencyRecorder(elapsedTime, attributes);
    }

    private Map<String, String> createAttributes(String status) {
      Map<String, String> attributes = new HashMap<>();
      attributes.put(METRIC_KEY_METHOD.toString(), methodName);
      attributes.put(METRIC_KEY_STATUS.toString(), status);
      return attributes;
    }

    /** Function to extract the status of the error as a string */
    private String extractErrorStatus(@Nullable Throwable throwable) {
      if (!(throwable instanceof FirestoreException)) {
        return StatusCode.Code.UNKNOWN.toString();
      }
      Status status = ((FirestoreException) throwable).getStatus();
      return status.getCode().name();
    }
  }
}
