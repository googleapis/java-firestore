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

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.api.MetricDescriptor;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.telemetry.ClientIdentifier;
import com.google.cloud.firestore.telemetry.TelemetryConstants;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableSet;
import com.google.monitoring.v3.*;
import com.google.protobuf.util.Timestamps;
import io.grpc.Status;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.sdk.metrics.data.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

// This End-to-End test verifies Client-side Tracing Functionality instrumented using the
// OpenTelemetry API.
// The test depends on the following external APIs/Services:
// 1. Java OpenTelemetry SDK
// 2. Cloud Trace Exporter
// 3. TraceServiceClient from Cloud Trace API v1.
//
// Permissions required to run this test (https://cloud.google.com/trace/docs/iam#trace-roles):
// 1. gcloud auth application-default login must be run with the test user.
// 2. To write traces, test user must have one of roles/cloudtrace.[admin|agent|user] roles.
// 3. To read traces, test user must have one of roles/cloudtrace.[admin|user] roles.
//
// Each test-case has the following workflow:
// 1. OpenTelemetry SDK is initialized with Cloud Trace Exporter and 100% Trace Sampling
// 2. On initialization, Firestore client is provided the OpenTelemetry SDK object from (1)
// 3. A custom TraceID is generated and injected using a custom SpanContext
// 4. Firestore operations are run inside a root TraceSpan created using the custom SpanContext from
// (3).
// 5. Traces are read-back using TraceServiceClient and verified against expected Call Stacks.
// TODO In the future it would be great to have a single test-driver for this test and
// ITTracingTest.

@RunWith(JUnit4.class)
public class ITE2EMetricsTest extends ITBaseTest {

  private static MetricServiceClient metricClient;

  private static final Logger logger = Logger.getLogger(ITE2EMetricsTest.class.getName());

  private static boolean isNightlyTesting;
  private static String projectId;

  private static Firestore firestore;

  private static Attributes expectedBaseAttributes;

  private final String ClientUid = ClientIdentifier.getClientUid();
  private final String libraryVersion = this.getClass().getPackage().getImplementationVersion();

  private FirestoreOptions.Builder optionsBuilder;

  private static final Set<String> GAX_METRICS =
      ImmutableSet.of(
          TelemetryConstants.METRIC_NAME_OPERATION_LATENCY,
          TelemetryConstants.METRIC_NAME_ATTEMPT_LATENCY,
          TelemetryConstants.METRIC_NAME_OPERATION_COUNT,
          TelemetryConstants.METRIC_NAME_ATTEMPT_COUNT);

  @BeforeClass
  public static void setup() throws IOException {
    String jobType = System.getenv("GITHUB_ENV_VAR_KOKORO_JOB_TYPE");
    isNightlyTesting = jobType != null && jobType.equalsIgnoreCase("nightly");

    projectId = FirestoreOptions.getDefaultProjectId();
    logger.info("projectId:" + projectId);
  }

  @Before
  public void before() throws Exception {
    // We only perform end-to-end tracing tests on a nightly basis.
    //    assumeTrue(isNightlyTesting);

    metricClient = MetricServiceClient.create();

    optionsBuilder = FirestoreOptions.newBuilder();

    String namedDb = System.getProperty("FIRESTORE_NAMED_DATABASE");
    if (namedDb != null) {
      logger.log(Level.INFO, "Integration test using named database " + namedDb);
      optionsBuilder = optionsBuilder.setDatabaseId(namedDb);
    } else {
      logger.log(Level.INFO, "Integration test using default database.");
    }

    firestore = optionsBuilder.build().getService();
    Preconditions.checkNotNull(
        firestore,
        "Error instantiating Firestore. Check that the service account credentials "
            + "were properly set.");

    expectedBaseAttributes = buildExpectedAttributes();
  }

  @After
  public void after() throws Exception {
    firestore.shutdown();
    metricClient.close();
    //    metricClient.shutdown()
    //    metricClient.awaitTermination(2,TimeUnit.SECONDS);
  }

  private Attributes buildExpectedAttributes() {
    AttributesBuilder attributesBuilder = Attributes.builder();
    attributesBuilder.put(
        TelemetryConstants.METRIC_ATTRIBUTE_KEY_LIBRARY_NAME.getKey(),
        TelemetryConstants.FIRESTORE_LIBRARY_NAME);
    attributesBuilder.put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_CLIENT_UID.getKey(), ClientUid);
    if (libraryVersion != null) {
      attributesBuilder.put(
          TelemetryConstants.METRIC_ATTRIBUTE_KEY_LIBRARY_VERSION.getKey(), libraryVersion);
    }
    return attributesBuilder.build();
  }

  @Test
  public void testBuiltinMetricsWithDefaultOTEL() throws Exception {
    // This stopwatch is used for to limit fetching of metric data in verifyMetrics
    Stopwatch metricsPollingStopwatch = Stopwatch.createStarted();
    TimeInterval interval = createTimeInterval();

    firestore.collection("col").get().get();
    firestore.collection("col").whereGreaterThan("bla", "").get().get();

    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.RunQuery", Status.OK.getCode().toString(), 2)
            .build();

    for (String METRIC : GAX_METRICS) {
      verifyPublishedMetrics(METRIC, interval, metricsPollingStopwatch, expectedMetrics);
    }

    Set<String> SDK_METRICS =
        ImmutableSet.of(
            TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY,
            TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);

    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_QUERY_GET, Status.OK.getCode().toString(), 2)
            .build();

    for (String METRIC : SDK_METRICS) {
      verifyPublishedMetrics(METRIC, interval, metricsPollingStopwatch, expectedMetrics);
    }
  }

  @Test
  public void testBuiltinMetricsWithTransaction() throws Exception {
    // This stopwatch is used for to limit fetching of metric data in verifyMetrics
    Stopwatch metricsPollingStopwatch = Stopwatch.createStarted();
    TimeInterval interval = createTimeInterval();

    firestore
        .runTransaction( // Has end-to-end, first response latency and transaction metrics.
            transaction -> {
              // Document Query. Has end-to-end and first response latency which is marked
              // transactional
              transaction.get(firestore.collection("col")).get();
              transaction.get(firestore.collection("col").whereGreaterThan("bla", "")).get();

              // Commit 2 documents. Has end-to-end latency.
              transaction.set(
                  firestore.collection("foo").document("bar"),
                  Collections.singletonMap("foo", "bar"));
              transaction.set(
                  firestore.collection("foo").document("bar2"),
                  Collections.singletonMap("foo2", "bar2"));
              return 0;
            })
        .get();

    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.BeginTransaction", Status.OK.getCode().toString(), 1)
            .expectMetricData("Firestore.RunQuery", Status.OK.getCode().toString(), 2)
            .expectMetricData("Firestore.Commit", Status.OK.getCode().toString(), 1)
            .build();

    for (String METRIC : GAX_METRICS) {
      verifyPublishedMetrics(METRIC, interval, metricsPollingStopwatch, expectedMetrics);
    }

    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION, Status.OK.getCode().toString(), 1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_TRANSACTION_GET_QUERY,
                Status.OK.getCode().toString(),
                2)
            .build();

    verifyPublishedMetrics(
        TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY,
        interval,
        metricsPollingStopwatch,
        expectedMetrics);

    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION, Status.OK.getCode().toString(), 1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_TRANSACTION_GET_QUERY,
                Status.OK.getCode().toString(),
                2)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_TRANSACTION_COMMIT,
                Status.OK.getCode().toString(),
                1)
            .build();

    verifyPublishedMetrics(
        TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY,
        interval,
        metricsPollingStopwatch,
        expectedMetrics);

    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION, Status.OK.getCode().toString(), 1)
            .build();

    Set<String> SDK_METRICS =
        ImmutableSet.of(
            TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY,
            TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);

    for (String METRIC : SDK_METRICS) {
      verifyPublishedMetrics(METRIC, interval, metricsPollingStopwatch, expectedMetrics);
    }
  }

  private TimeInterval createTimeInterval() {
    Instant startTime = Instant.now();
    Instant endTime = Instant.now().plus(Duration.ofMinutes(1));
    System.out.println(
        "Test Start Time: " + startTime.toString() + " - Nanos: " + startTime.getNano());
    System.out.println("Test End Time: " + endTime.toString() + " - Nanos: " + endTime.getNano());

    return TimeInterval.newBuilder()
        .setStartTime(Timestamps.fromMillis(startTime.toEpochMilli()))
        .setEndTime(Timestamps.fromMillis(endTime.toEpochMilli()))
        .build();
  }

  private ListTimeSeriesResponse verifyPublishedMetrics(
      String metric,
      TimeInterval interval,
      Stopwatch metricsPollingStopwatch,
      Map<String, MetricInfo> expectedMetrics)
      throws Exception {

    String metricFilter =
        String.format("metric.type=\"%s/%s\"", TelemetryConstants.METRIC_PREFIX, metric);

    // 2. Calculate alignment period dynamically based on your interval
    //    long alignmentSeconds =
    //            Timestamps.toMillis(interval.getEndTime())
    //                    - Timestamps.toMillis(interval.getStartTime());
    //    Duration alignmentPeriod = Duration.ofMillis(alignmentSeconds);
    //
    //    // Convert Duration to com.google.protobuf.Duration
    //    com.google.protobuf.Duration protoAlignmentPeriod =
    //            com.google.protobuf.Duration.newBuilder()
    //                    .setSeconds(alignmentPeriod.getSeconds())
    //                    .setNanos(alignmentPeriod.getNano())
    //                    .build();

    ListTimeSeriesRequest request =
        ListTimeSeriesRequest.newBuilder()
            .setName("projects/" + projectId)
            .setFilter(metricFilter)
            .setInterval(interval)
            //            .setView(ListTimeSeriesRequest.TimeSeriesView.FULL)
            //                .setAggregation(
            //                        Aggregation.newBuilder()
            //                                .setAlignmentPeriod(protoAlignmentPeriod) // Dynamic
            // alignment period
            //                                .setPerSeriesAligner(Aggregation.Aligner.ALIGN_DELTA)
            // // Use ALIGN_DELTA
            //                )
            .build();

    System.out.println("ListTimeSeriesRequest: " + request); // Log the request

    ListTimeSeriesResponse response = metricClient.listTimeSeriesCallable().call(request);
    while (response.getTimeSeriesCount() == 0
        && metricsPollingStopwatch.elapsed(TimeUnit.MINUTES) < 3) {
      // Call listTimeSeries every minute
      Thread.sleep(Duration.ofMinutes(1).toMillis());
      response = metricClient.listTimeSeriesCallable().call(request);
    }

    System.out.println("ListTimeSeriesResponse: " + response); // Log the response

    assertWithMessage("Metric " + metric + " didn't return any data.")
        .that(response.getTimeSeriesCount())
        .isGreaterThan(0);

    assertThat(response.getTimeSeriesCount()).isEqualTo(expectedMetrics.size());

    for (TimeSeries timeSeries : response.getTimeSeriesList()) {
      boolean isDistribution = timeSeries.getValueType() == MetricDescriptor.ValueType.DISTRIBUTION;

      Map<String, String> metricLabels = timeSeries.getMetric().getLabelsMap();
      MetricInfo expectedMetricInfo = expectedMetrics.get(metricLabels.get("method"));

      if (isDistribution) {
        assertThat(expectedMetricInfo.count)
            .isEqualTo(timeSeries.getPoints(0).getValue().getDistributionValue().getCount());
      } else {
        assertThat(expectedMetricInfo.count)
            .isEqualTo((int) timeSeries.getPoints(0).getValue().getInt64Value());
      }

      assertThat(metricLabels)
          .containsAtLeastEntriesIn(
              expectedMetricInfo.attributes.asMap().entrySet().stream()
                  .collect(
                      Collectors.toMap(entry -> entry.getKey().getKey(), Map.Entry::getValue)));
    }

    return response;
  }

  class MetricInfo {
    // The expected number of measurements is called
    public int count;
    // Attributes expected to be recorded in the measurements
    public Attributes attributes;

    public MetricInfo(int expectedCount, Attributes expectedAttributes) {
      this.count = expectedCount;
      this.attributes = expectedAttributes;
    }
  }

  class MetricsExpectationBuilder {
    private final Map<String, MetricInfo> expectedMetrics = new HashMap<>();

    public MetricsExpectationBuilder expectMetricData(
        String method, String expectedStatus, int expectedCount) {
      Attributes attributes = buildAttributes(method, expectedStatus);
      expectedMetrics.put(method, new MetricInfo(expectedCount, attributes));
      return this;
    }

    public Map<String, MetricInfo> build() {
      return expectedMetrics;
    }
  }

  private Attributes buildAttributes(String method, String status) {
    return expectedBaseAttributes
        .toBuilder()
        .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, status)
        .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, method)
        .build();
  }
}
