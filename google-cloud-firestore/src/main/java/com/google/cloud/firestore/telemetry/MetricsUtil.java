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
import com.google.cloud.firestore.FirestoreOptions;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.MoreExecutors;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class MetricsUtil {
  private final OpenTelemetry openTelemetry;
  //  private final FirestoreOptions firestoreOptions;
  private final Meter meter;
  private final DoubleHistogram endToEndRequestLatency;
  private final DoubleHistogram firstResponseLatency;

  public MetricsUtil(FirestoreOptions firestoreOptions) {
    OpenTelemetry openTelemetry = firestoreOptions.getOpenTelemetryOptions().getOpenTelemetry();

    // If tracing is enabled, but an OpenTelemetry instance is not provided, fall back
    // to using GlobalOpenTelemetry.
    if (openTelemetry == null) {
      openTelemetry = GlobalOpenTelemetry.get();
    }

    this.openTelemetry = openTelemetry;

    Package pkg = this.getClass().getPackage();

    meter =
        openTelemetry
            .meterBuilder("firestore-java")
            .setInstrumentationVersion(pkg.getImplementationVersion())
            .build();

    endToEndRequestLatency =
        meter
            .histogramBuilder("E2ERequestLatency")
            .setDescription("Firestore E2E metrics")
            .setUnit("ms")
            .build();

    firstResponseLatency =
        meter
            .histogramBuilder("FirstResponseLatency")
            .setDescription("Firestore query first response latency")
            .setUnit("ms")
            .build();
  }

  public OpenTelemetry getOpenTelemetry() {
    return openTelemetry;
  }

  public void endToEndRequestLatencyRecorder(double latency, Map<String, String> attributes) {
    System.out.println(
        "Firestore-> end to end request latency: "
            + latency
            + ", attributes: "
            + attributes.values());
    endToEndRequestLatency.record(latency, toOtelAttributes(attributes));
  }

  public void firstResponseLatencyRecorder(double latency, Map<String, String> attributes) {
    System.out.println(
        "Firestore-> first response latency: " + latency + ", attributes: " + attributes.values());
    firstResponseLatency.record(latency, toOtelAttributes(attributes));
  }

  @VisibleForTesting
  Attributes toOtelAttributes(Map<String, String> attributes) {
    AttributesBuilder attributesBuilder = Attributes.builder();
    attributes.forEach(attributesBuilder::put);
    return attributesBuilder.build();
  }


  public <T> void end(ApiFuture<T> futureValue, double start, String method) {
    Map<String, String> attributes = new HashMap<>();
    attributes.put("language", "java");
    attributes.put("method", method);
    attributes.put("status", "OK");
    double end = System.currentTimeMillis();
    double elapsedTime = end - start;
    endToEndRequestLatencyRecorder(elapsedTime, attributes);
  }


  public <T> void endAtFuture(ApiFuture<T> futureValue, double start, String method) {
    Map<String, String> attributes = new HashMap<>();
    attributes.put("language", "java");
    attributes.put("method", method);

    ApiFutures.addCallback(
        futureValue,
        new ApiFutureCallback<T>() {
          @Override
          public void onFailure(Throwable t) {
            double end = System.currentTimeMillis();
            double elapsedTime = end - start;
            attributes.put("status", "failed");
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
}
