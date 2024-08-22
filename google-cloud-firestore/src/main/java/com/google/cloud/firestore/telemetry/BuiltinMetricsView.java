package com.google.cloud.firestore.telemetry;

/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static com.google.cloud.firestore.telemetry.BuiltinMetricsConstants.*;

import com.google.cloud.opentelemetry.metric.GoogleCloudMetricExporter;
import com.google.cloud.opentelemetry.metric.MetricConfiguration;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.View;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import java.io.IOException;
import java.util.Map;

public class BuiltinMetricsView {

  private BuiltinMetricsView() {}

  /** Register built-in metrics on the {@link SdkMeterProviderBuilder} with credentials. */
  public static void registerBuiltinMetrics(String projectId, SdkMeterProviderBuilder builder)
      throws IOException {

    // Attach built-in exporter
    MetricExporter metricExporter =
        GoogleCloudMetricExporter.createWithConfiguration(
            MetricConfiguration.builder().setProjectId(projectId).build());

    builder.registerMetricReader(PeriodicMetricReader.builder(metricExporter).build());

    // Add static attributes to resource
    //        Package pkg = this.getClass().getPackage();

    // Filter out attributes that are not defined
    for (Map.Entry<InstrumentSelector, View> entry : getAllViews().entrySet()) {
      builder.registerView(entry.getKey(), entry.getValue());
    }

    builder.registerMetricReader(PeriodicMetricReader.create(metricExporter));
  }
}
