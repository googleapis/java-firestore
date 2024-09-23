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

import com.google.cloud.opentelemetry.metric.GoogleCloudMetricExporter;
import com.google.cloud.opentelemetry.metric.MetricConfiguration;
import com.google.common.annotations.VisibleForTesting;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.metrics.InstrumentType;
import io.opentelemetry.sdk.metrics.data.AggregationTemporality;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

class FirestoreCloudMonitoringExporter implements MetricExporter {

  //   private static final Logger logger =
  //       LoggerFactory.getLogger(FirestoreCloudMonitoringExporter.class);
  private final MetricExporter delegate;

  private FirestoreCloudMonitoringExporter(MetricConfiguration configuration) {
    this.delegate = GoogleCloudMetricExporter.createWithConfiguration(configuration);
  }

  public static FirestoreCloudMonitoringExporter create(String projectId) {
    return new FirestoreCloudMonitoringExporter(
        MetricConfiguration.builder()
            .setProjectId(projectId)
            .setInstrumentationLibraryLabelsEnabled(false)
            // .setMonitoredResourceDescription((null))
            // .setUseServiceTimeSeries(false)
            .build());
  }

  @VisibleForTesting
  FirestoreCloudMonitoringExporter createWithConfiguration(MetricConfiguration configuration) {
    return new FirestoreCloudMonitoringExporter(configuration);
  }

  @Override
  public CompletableResultCode export(@Nonnull Collection<MetricData> metrics) {
    // Filter out non built-in metrics
    Collection<MetricData> filteredMetrics =
        metrics.stream()
            .filter(md -> BUILTIN_METRICS.contains(md.getName()))
            .collect(Collectors.toList());

    // Skips exporting if there's none
    if (filteredMetrics.isEmpty()) {
      return CompletableResultCode.ofSuccess();
    }

    return this.delegate.export(filteredMetrics);
  }

  @Override
  public CompletableResultCode flush() {
    return this.delegate.flush();
  }

  @Override
  public CompletableResultCode shutdown() {
    return this.delegate.shutdown();
  }

  @Override
  public AggregationTemporality getAggregationTemporality(@Nonnull InstrumentType instrumentType) {
    return this.delegate.getAggregationTemporality(instrumentType);
  }
}
