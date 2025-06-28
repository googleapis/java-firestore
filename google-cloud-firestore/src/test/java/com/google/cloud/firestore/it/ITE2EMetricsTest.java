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

package com.google.cloud.firestore.it;

import static com.google.common.truth.Truth.assertWithMessage;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOpenTelemetryOptions;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.telemetry.TelemetryConstants;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.common.collect.ImmutableSet;
import com.google.monitoring.v3.ListTimeSeriesRequest;
import com.google.monitoring.v3.ListTimeSeriesResponse;
import com.google.monitoring.v3.TimeInterval;
import com.google.monitoring.v3.TimeSeries;
import com.google.protobuf.util.Timestamps;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.testing.exporter.InMemoryMetricReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;

@RunWith(JUnit4.class)
public class ITE2EMetricsTest extends ITBaseTest {

  private static MetricServiceClient metricClient;

  private static final Logger logger = Logger.getLogger(ITE2EMetricsTest.class.getName());

  private static boolean isNightlyTesting;
  private static String projectId;

  private static Firestore firestore;

  private FirestoreOptions.Builder optionsBuilder;

  @BeforeClass
  public static void setup() throws IOException {
    String jobType = System.getenv("GITHUB_ENV_VAR_KOKORO_JOB_TYPE");
    isNightlyTesting = jobType != null && jobType.equalsIgnoreCase("nightly");

    projectId = FirestoreOptions.getDefaultProjectId();
    logger.info("projectId:" + projectId);
  }

  @Before
  public void before() throws Exception {
    metricClient = MetricServiceClient.create();

    optionsBuilder = FirestoreOptions.newBuilder();

    String namedDb = System.getProperty("FIRESTORE_NAMED_DATABASE");
    if (namedDb != null) {
      logger.log(Level.INFO, "Integration test using named database " + namedDb);
      optionsBuilder = optionsBuilder.setDatabaseId(namedDb);
    } else {
      logger.log(Level.INFO, "Integration test using default database.");
    }

    // These end-to-end metrics tests are resource-intensive and are only intended to run in a
    // nightly testing environment.
    // assumeTrue(isNightlyTesting);
  }

  @After
  public void after() throws Exception {
    if (firestore != null) {
      // Shutting down Firestore can trigger a final export of metrics, potentially leading to
      // "frequent write" errors in Cloud Monitoring. This sleep attempts to mitigate this by
      // allowing a brief pause.
      Thread.sleep(Duration.ofSeconds(30).toMillis());
      firestore.shutdown();
    }
    metricClient.shutdown();
  }

  @Test
  public void builtinMetricsWithDefaultOTEL() throws Exception {
    firestore = optionsBuilder.build().getService();
    TimeInterval interval = createTimeInterval();

    firestore.collection("col").get().get();

    Set<String> METRICS =
        ImmutableSet.of(
            TelemetryConstants.METRIC_NAME_OPERATION_LATENCY,
            TelemetryConstants.METRIC_NAME_ATTEMPT_LATENCY,
            TelemetryConstants.METRIC_NAME_OPERATION_COUNT,
            TelemetryConstants.METRIC_NAME_ATTEMPT_COUNT,
            TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);

    // Verify metric data are published to Cloud Monitoring
    for (String METRIC : METRICS) {
      assertMetricsArePublished(METRIC, interval);
    }
  }

  @Test
  public void builtinMetricsWithDefaultAndCustomOTEL() throws Exception {
    InMemoryMetricReader metricReader = InMemoryMetricReader.create();
    SdkMeterProviderBuilder meterProvider =
        SdkMeterProvider.builder().registerMetricReader(metricReader);
    OpenTelemetrySdk customOpenTelemetrySdk =
        OpenTelemetrySdk.builder().setMeterProvider(meterProvider.build()).build();
    firestore =
        optionsBuilder
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setOpenTelemetry(customOpenTelemetrySdk)
                    .build())
            .build()
            .getService();
    TimeInterval interval = createTimeInterval();

    firestore.collection("col").count().get().get();

    // Verify metric data are published to Cloud Monitoring
    Set<String> METRICS =
        ImmutableSet.of(
            TelemetryConstants.METRIC_NAME_OPERATION_LATENCY,
            TelemetryConstants.METRIC_NAME_ATTEMPT_LATENCY,
            TelemetryConstants.METRIC_NAME_OPERATION_COUNT,
            TelemetryConstants.METRIC_NAME_ATTEMPT_COUNT,
            TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);

    for (String METRIC : METRICS) {
      assertMetricsArePublished(METRIC, interval);
    }

    // Verify metric data are collected to 3rd party backend (InMemoryMetricReader)
    METRICS =
        ImmutableSet.of(
            TelemetryConstants.METRIC_NAME_OPERATION_LATENCY,
            TelemetryConstants.METRIC_NAME_ATTEMPT_LATENCY,
            TelemetryConstants.METRIC_NAME_OPERATION_COUNT,
            TelemetryConstants.METRIC_NAME_ATTEMPT_COUNT,
            TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    for (String METRIC : METRICS) {
      assertMetricsAreCollected(metricReader, METRIC);
    }

    // Verify no transaction related metric data are collected
    METRICS =
        ImmutableSet.of(
            TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY,
            TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);

    for (String METRIC : METRICS) {
      assertMetricsAreAbsent(metricReader, METRIC);
    }
    metricReader.shutdown();
  }

  @Test
  public void builtinMetricsCreatedByTransaction() throws Exception {
    firestore = optionsBuilder.build().getService();
    TimeInterval interval = createTimeInterval();

    firestore
        .runTransaction(
            transaction -> {
              transaction.get(firestore.collection("col")).get();
              transaction.set(
                  firestore.collection("foo").document("bar"),
                  Collections.singletonMap("foo", "bar"));
              return 0;
            })
        .get();

    Set<String> METRICS =
        ImmutableSet.of(
            TelemetryConstants.METRIC_NAME_OPERATION_LATENCY,
            TelemetryConstants.METRIC_NAME_ATTEMPT_LATENCY,
            TelemetryConstants.METRIC_NAME_OPERATION_COUNT,
            TelemetryConstants.METRIC_NAME_ATTEMPT_COUNT,
            TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY,
            TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY,
            TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);

    // Verify metric data are published to Cloud Monitoring
    for (String METRIC : METRICS) {
      assertMetricsArePublished(METRIC, interval);
    }
  }

  private TimeInterval createTimeInterval() {
    Instant startTime = Instant.now().minus(Duration.ofSeconds(10));
    // Set a wider time interval to make sure SDK has finished the batching (60s) and exporting of
    // the metrics
    Instant endTime = Instant.now().plus(Duration.ofSeconds(90));
    return TimeInterval.newBuilder()
        .setStartTime(Timestamps.fromMillis(startTime.toEpochMilli()))
        .setEndTime(Timestamps.fromMillis(endTime.toEpochMilli()))
        .build();
  }

  private void assertMetricsArePublished(String metric, TimeInterval interval) throws Exception {
    String metricFilter =
        String.format("metric.type=\"%s/%s\"", TelemetryConstants.METRIC_PREFIX, metric);
    ListTimeSeriesRequest request =
        ListTimeSeriesRequest.newBuilder()
            .setName("projects/" + projectId)
            .setFilter(metricFilter)
            .setInterval(interval)
            .build();

    List<TimeSeries> filteredData = Collections.emptyList();
    // Metrics are batched and exported periodically in the SDK (e.g., every 60s). And there is a
    // potential delay between when the metric is published by the client library and when it
    // becomes available. So we retry the fetching for multiple times with specified interval, to
    // allow the monitoring system to catch up and for the data to become available.
    int maxAttempts = 5;
    Duration retryDelay = Duration.ofSeconds(30);

    for (int attempt = 0; attempt < maxAttempts; attempt++) {
      // Query Cloud Monitoring for the specific metric within the test's time window.
      ListTimeSeriesResponse response = metricClient.listTimeSeriesCallable().call(request);

      // In the testing environment where multiple tests might be running, the initial
      // response could contain data from previous test runs. Filter the response to
      // ensure we're validating the metrics generated by this specific test run and
      // avoid interference from older data.
      filteredData =
          response.getTimeSeriesList().stream()
              .filter(
                  ts ->
                      ts.getPoints(0).getInterval().getStartTime().getSeconds()
                          >= interval.getStartTime().getSeconds())
              .collect(Collectors.toList());
      if (!filteredData.isEmpty()) {
        break;
      }
      logger.info(
          "Retry fetching metrics data from Cloud Monitoring after "
              + retryDelay.getSeconds()
              + " seconds.");
      Thread.sleep(retryDelay.toMillis());
    }

    assertWithMessage("Metric " + metric + " didn't return any data.")
        .that(filteredData.size())
        .isGreaterThan(0);
  }

  private void assertMetricsAreCollected(InMemoryMetricReader metricReader, String metricName) {
    String fullMetricName = TelemetryConstants.METRIC_PREFIX + "/" + metricName;
    List<MetricData> matchingMetadata =
        metricReader.collectAllMetrics().stream()
            .filter(md -> md.getName().equals(fullMetricName))
            .collect(Collectors.toList());

    // Fetch the MetricData with retries
    int attemptsMade = 0;
    while (matchingMetadata.size() == 0 && attemptsMade < 10) {
      // Fetch metric data every seconds
      try {
        Thread.sleep(Duration.ofSeconds(1).toMillis());
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(interruptedException);
      }
      matchingMetadata =
          metricReader.collectAllMetrics().stream()
              .filter(md -> md.getName().equals(fullMetricName))
              .collect(Collectors.toList());
      attemptsMade++;
    }

    assertWithMessage(
            "Found unexpected MetricData with the same name: %s, in: %s",
            fullMetricName, matchingMetadata)
        .that(matchingMetadata.size())
        .isAtMost(1);

    assertWithMessage("MetricData is missing for metric %s", fullMetricName)
        .that(matchingMetadata.size())
        .isEqualTo(1);
  }

  private void assertMetricsAreAbsent(InMemoryMetricReader metricReader, String metricName) {
    String fullMetricName = TelemetryConstants.METRIC_PREFIX + "/" + metricName;
    List<MetricData> matchingMetadata =
        metricReader.collectAllMetrics().stream()
            .filter(md -> md.getName().equals(fullMetricName))
            .collect(Collectors.toList());
    assertWithMessage(
            "Found unexpected MetricData with the same name: %s, in: %s",
            fullMetricName, matchingMetadata)
        .that(matchingMetadata.size())
        .isEqualTo(0);
  }
}
