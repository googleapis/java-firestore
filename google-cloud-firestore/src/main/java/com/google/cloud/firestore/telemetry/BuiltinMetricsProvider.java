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

import static com.google.cloud.firestore.telemetry.TelemetryConstants.FIRESTORE_METER_NAME;
import static com.google.cloud.firestore.telemetry.TelemetryConstants.METRIC_KEY_CLIENT_UID;
import static com.google.cloud.firestore.telemetry.TelemetryConstants.METRIC_KEY_LIBRARY_NAME;
import static com.google.cloud.firestore.telemetry.TelemetryConstants.METRIC_KEY_LIBRARY_VERSION;
import static com.google.cloud.firestore.telemetry.TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY;
import static com.google.cloud.firestore.telemetry.TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY;
import static com.google.cloud.firestore.telemetry.TelemetryConstants.METRIC_PREFIX;

import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.MetricsTracerFactory;
import com.google.api.gax.tracing.OpenTelemetryMetricsRecorder;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.Meter;
import java.util.HashMap;
import java.util.Map;

public class BuiltinMetricsProvider {
  private OpenTelemetry openTelemetry;
  private ApiTracerFactory apiTracerFactory;

  private Meter meter;
  private DoubleHistogram endToEndRequestLatency;
  private DoubleHistogram firstResponseLatency;

  private static final String MILLISECOND_UNIT = "ms";
  private static final String FIRESTORE_LIBRARY_NAME = "java_firestore";

  public BuiltinMetricsProvider(OpenTelemetry openTelemetry) {
    this.openTelemetry = openTelemetry;

    if (openTelemetry != null) {
      configureGaxLayerMetrics();
      configureFirestoreLayerMetrics();
    }
  }

  public ApiTracerFactory getApiTracerFactory() {
    return this.apiTracerFactory;
  }

  void configureGaxLayerMetrics() {
    OpenTelemetryMetricsRecorder recorder =
        new OpenTelemetryMetricsRecorder(openTelemetry, METRIC_PREFIX);
    this.apiTracerFactory = new MetricsTracerFactory(recorder, createStaticAttributes());
  }

  void configureFirestoreLayerMetrics() {
    this.meter = openTelemetry.getMeter(FIRESTORE_METER_NAME);

    this.endToEndRequestLatency =
        meter
            .histogramBuilder(METRIC_PREFIX + "/" + METRIC_NAME_END_TO_END_LATENCY)
            .setDescription("Firestore E2E metrics")
            .setUnit(MILLISECOND_UNIT)
            .build();

    this.firstResponseLatency =
        meter
            .histogramBuilder(METRIC_PREFIX + "/" + METRIC_NAME_FIRST_RESPONSE_LATENCY)
            .setDescription("Firestore query first response latency")
            .setUnit(MILLISECOND_UNIT)
            .build();
    // TODO(metrics): add transaction latency and retry count metrics
  }

  public void endToEndRequestLatencyRecorder(double latency, Map<String, String> attributes) {
    if (endToEndRequestLatency != null) {
      attributes.putAll(createStaticAttributes());
      endToEndRequestLatency.record(latency, toOtelAttributes(attributes));
    }
  }

  public void firstResponseLatencyRecorder(double latency, Map<String, String> attributes) {
    if (firstResponseLatency != null) {
      attributes.putAll(createStaticAttributes());
      firstResponseLatency.record(latency, toOtelAttributes(attributes));
    }
  }

  private Map<String, String> createStaticAttributes() {
    Map<String, String> staticAttributes = new HashMap<>();
    staticAttributes.put(METRIC_KEY_CLIENT_UID.toString(), TelemetryHelper.getClientUid());
    staticAttributes.put(METRIC_KEY_LIBRARY_NAME.toString(), FIRESTORE_LIBRARY_NAME);
    String pkgVersion = this.getClass().getPackage().getImplementationVersion();
    if (pkgVersion != null) {
      staticAttributes.put(METRIC_KEY_LIBRARY_VERSION.toString(), pkgVersion);
    }
    return staticAttributes;
  }

  private Attributes toOtelAttributes(Map<String, String> attributes) {
    AttributesBuilder attributesBuilder = Attributes.builder();
    attributes.forEach(attributesBuilder::put);
    return attributesBuilder.build();
  }
}
