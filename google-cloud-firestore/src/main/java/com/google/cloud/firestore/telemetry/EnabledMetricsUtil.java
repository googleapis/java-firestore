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

import static com.google.cloud.firestore.telemetry.TelemetryConstants.*;
import static com.google.cloud.opentelemetry.detection.GCPPlatformDetector.SupportedPlatform.GOOGLE_KUBERNETES_ENGINE;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.telemetry.TelemetryConstants.MetricType;
import com.google.cloud.opentelemetry.detection.AttributeKeys;
import com.google.cloud.opentelemetry.detection.DetectedPlatform;
import com.google.cloud.opentelemetry.detection.GCPPlatformDetector;
import com.google.cloud.opentelemetry.metric.GoogleCloudMetricExporter;
import com.google.cloud.opentelemetry.metric.MetricConfiguration;
import com.google.cloud.opentelemetry.metric.MonitoredResourceDescription;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.Status;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.View;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.MetricReader;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/**
 * An implementation of {@link MetricsUtil} that uses OpenTelemetry to collect and export metrics.
 * `FirestoreOpenTelemetryOptions` in `FirestoreOptions` can be used to configure its behavior.
 */
class EnabledMetricsUtil implements MetricsUtil {

  private FirestoreOptions firestoreOptions;
  private BuiltinMetricsProvider defaultMetricsProvider;
  private BuiltinMetricsProvider customMetricsProvider;
  private MetricReader metricReader;

  private static final Logger logger = Logger.getLogger(EnabledMetricsUtil.class.getName());

  EnabledMetricsUtil(FirestoreOptions firestoreOptions) {
    this.firestoreOptions = firestoreOptions;
    try {
      this.defaultMetricsProvider = configureDefaultMetricsProvider();
      this.customMetricsProvider = configureCustomMetricsProvider();
    } catch (Exception e) {
      logger.warning(
          "Unable to create MetricsUtil object for client side metrics, will skip exporting client"
              + " side metrics"
              + e);
    }
  }

  private BuiltinMetricsProvider configureDefaultMetricsProvider() {
    OpenTelemetry defaultOpenTelemetry = OpenTelemetry.noop();
    if (firestoreOptions.getOpenTelemetryOptions().exportBuiltinMetricsToGoogleCloudMonitoring()) {
      if (firestoreOptions.getProjectId() == null) {
        logger.warning(
            "Project ID is null, skipping client side metrics export to Cloud Monitoring.");
      } else {
        try {
          defaultOpenTelemetry = getDefaultOpenTelemetryInstance();
        } catch (Exception e) {
          logger.warning(
              "Unable to create default OpenTelemetry instance for client side metrics, will skip"
                  + " exporting client side metrics to Cloud Monitoring: "
                  + e);
        }
      }
    }
    return new BuiltinMetricsProvider(defaultOpenTelemetry);
  }

  private BuiltinMetricsProvider configureCustomMetricsProvider() throws IOException {
    OpenTelemetry customOpenTelemetry =
        firestoreOptions.getOpenTelemetryOptions().getOpenTelemetry();
    if (customOpenTelemetry == null) {
      customOpenTelemetry = GlobalOpenTelemetry.get();
    }
    return new BuiltinMetricsProvider(customOpenTelemetry);
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

  @VisibleForTesting
  BuiltinMetricsProvider getCustomMetricsProvider() {
    return customMetricsProvider;
  }

  @VisibleForTesting
  BuiltinMetricsProvider getDefaultMetricsProvider() {
    return defaultMetricsProvider;
  }

  /**
   * Creates a default {@link OpenTelemetry} instance to collect and export built-in client side
   * metrics to Google Cloud Monitoring.
   */
  private OpenTelemetry getDefaultOpenTelemetryInstance() throws IOException {
    SdkMeterProviderBuilder sdkMeterProviderBuilder = SdkMeterProvider.builder();

    // Filter out attributes that are not defined
    for (Map.Entry<InstrumentSelector, View> entry : getAllViews().entrySet()) {
      sdkMeterProviderBuilder.registerView(entry.getKey(), entry.getValue());
    }

    sdkMeterProviderBuilder.setResource(Resource.create(createResourceAttributes()));

    MonitoredResourceDescription monitoredResourceMapping =
        new MonitoredResourceDescription(FIRESTORE_RESOURCE_TYPE, FIRESTORE_RESOURCE_LABELS);

    // TODO: uncomment the configuration below
    MetricExporter metricExporter =
        GoogleCloudMetricExporter.createWithConfiguration(
            MetricConfiguration.builder()
                .setProjectId(firestoreOptions.getProjectId())
                //               .setPrefix("firestore.googleapis.com")
                // Ignore library info as it is collected by the metric attributes as well
                .setInstrumentationLibraryLabelsEnabled(false)
                //                .setMonitoredResourceDescription(monitoredResourceMapping)
                //                 .setUseServiceTimeSeries(true)
                .build());
    metricReader = PeriodicMetricReader.create(metricExporter);
    sdkMeterProviderBuilder.registerMetricReader(metricReader);

    return OpenTelemetrySdk.builder().setMeterProvider(sdkMeterProviderBuilder.build()).build();
  }

  Attributes createResourceAttributes() {
    AttributesBuilder attributesBuilder =
        Attributes.builder()
            .put(RESOURCE_KEY_LOCATION, detectClientLocation())
            .put(RESOURCE_KEY_PROJECT, firestoreOptions.getProjectId())
            .put(RESOURCE_KEY_DATABASE, firestoreOptions.getDatabaseId())
            .put(RESOURCE_KEY_UID, ClientIdentifier.getClientUid());

    String pkgVersion = this.getClass().getPackage().getImplementationVersion();
    attributesBuilder.put(
        RESOURCE_KEY_INSTANCE, "java_" + (pkgVersion != null ? pkgVersion : "unknown"));

    return attributesBuilder.build();
  }

  private String detectClientLocation() {
    GCPPlatformDetector detector = GCPPlatformDetector.DEFAULT_INSTANCE;
    DetectedPlatform detectedPlatform = detector.detectPlatform();
    // All platform except GKE uses "cloud_region" for region attribute.
    String region = detectedPlatform.getAttributes().get("cloud_region");
    if (detectedPlatform.getSupportedPlatform() == GOOGLE_KUBERNETES_ENGINE) {
      region = detectedPlatform.getAttributes().get(AttributeKeys.GKE_LOCATION_TYPE_REGION);
    }
    return region == null ? "global" : region;
  }

  private static Map<InstrumentSelector, View> getAllViews() {
    ImmutableMap.Builder<InstrumentSelector, View> views = ImmutableMap.builder();
    GAX_METRICS.forEach(metric -> defineView(views, metric, GAX_METER_NAME));
    FIRESTORE_METRICS.forEach(metric -> defineView(views, metric, FIRESTORE_METER_NAME));
    return views.build();
  }

  private static void defineView(
      ImmutableMap.Builder<InstrumentSelector, View> viewMap, String id, String meter) {
    InstrumentSelector selector =
        InstrumentSelector.builder().setMeterName(meter).setName(METRIC_PREFIX + "/" + id).build();
    Set<String> attributesFilter =
        ImmutableSet.<String>builder()
            .addAll(COMMON_ATTRIBUTES.stream().collect(Collectors.toSet()))
            .build();
    View view = View.builder().setAttributeFilter(attributesFilter).build();

    viewMap.put(selector, view);
  }

  private void addTracerFactory(
      List<ApiTracerFactory> apiTracerFactories, BuiltinMetricsProvider metricsProvider) {
    ApiTracerFactory tracerFactory = metricsProvider.getApiTracerFactory();
    if (tracerFactory != null) {
      apiTracerFactories.add(tracerFactory);
    }
  }

  @Override
  public void shutdown() {
    // Gracefully shutdown the metric reader registered to the default OTEL instance inside the sdk.
    if (metricReader != null) {
      try {
        metricReader.shutdown();
      } catch (Exception e) {
        // Handle the exception or retry with exponential backoff
        logger.warning("Error shutting down MetricReader: " + e.getMessage());
      }
    }
  }

  class MetricsContext implements MetricsUtil.MetricsContext {
    private final Stopwatch stopwatch;
    private int counter;
    protected final String methodName;

    public MetricsContext(String methodName) {
      this.stopwatch = Stopwatch.createStarted();
      this.methodName = methodName;
      this.counter = 0;
    }

    public <T> void recordLatencyAtFuture(MetricType metric, ApiFuture<T> futureValue) {
      ApiFutures.addCallback(
          futureValue,
          new ApiFutureCallback<T>() {
            @Override
            public void onFailure(Throwable t) {
              recordLatency(metric, t);
            }

            @Override
            public void onSuccess(T result) {
              recordLatency(metric);
            }
          },
          MoreExecutors.directExecutor());
    }

    public void recordLatency(MetricType metric) {
      recordLatency(metric, StatusCode.Code.OK.toString());
    }

    public void recordLatency(MetricType metric, Throwable t) {
      recordLatency(metric, extractErrorStatus(t));
    }

    private void recordLatency(MetricType metric, String status) {
      double elapsedTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
      Map<String, String> attributes = createAttributes(status, methodName);
      defaultMetricsProvider.latencyRecorder(metric, elapsedTime, attributes);
      customMetricsProvider.latencyRecorder(metric, elapsedTime, attributes);
    }

    public void incrementCounter() {
      counter++;
    }

    public <T> void recordCounterAtFuture(MetricType metric, ApiFuture<T> futureValue) {
      ApiFutures.addCallback(
          futureValue,
          new ApiFutureCallback<T>() {
            @Override
            public void onFailure(Throwable t) {
              recordCounter(metric, extractErrorStatus(t));
            }

            @Override
            public void onSuccess(T result) {
              recordCounter(metric, StatusCode.Code.OK.toString());
            }
          },
          MoreExecutors.directExecutor());
    }

    private void recordCounter(MetricType metric, String status) {
      Map<String, String> attributes = createAttributes(status, methodName);
      defaultMetricsProvider.counterRecorder(metric, (long) counter, attributes);
      customMetricsProvider.counterRecorder(metric, (long) counter, attributes);
    }
  }

  private Map<String, String> createAttributes(String status, String methodName) {
    Map<String, String> attributes = new HashMap<>();
    attributes.put(METRIC_ATTRIBUTE_KEY_METHOD, methodName);
    attributes.put(METRIC_ATTRIBUTE_KEY_STATUS, status);
    return attributes;
  }

  @VisibleForTesting
  String extractErrorStatus(@Nullable Throwable throwable) {
    Status status = null;

    if (throwable instanceof FirestoreException) {
      status = ((FirestoreException) throwable).getStatus();
    } else if (throwable instanceof ApiException) {
      status = FirestoreException.forApiException((ApiException) throwable).getStatus();
    }

    if (status == null) {
      return Status.Code.UNKNOWN.toString();
    }
    return status.getCode().name();
  }
}
