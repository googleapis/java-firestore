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
import static org.junit.Assert.assertTrue;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOpenTelemetryOptions;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.telemetry.ClientIdentifier;
import com.google.cloud.firestore.telemetry.TelemetryConstants;
import com.google.common.base.Preconditions;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.data.*;
import io.opentelemetry.sdk.testing.exporter.InMemoryMetricReader;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.junit.*;
import org.junit.rules.TestName;

public class ITMetricsTest {

  private static final Logger logger =
      Logger.getLogger(com.google.cloud.firestore.it.ITMetricsTest.class.getName());

  private static OpenTelemetrySdk openTelemetrySdk;

  protected InMemoryMetricReader metricReader;

  protected Firestore firestore;

  private static Attributes expectedBaseAttributes;

  private final String ClientUid = ClientIdentifier.getClientUid();
  private final String libraryVersion = this.getClass().getPackage().getImplementationVersion();

  @Rule public TestName testName = new TestName();

  @Before
  public void setup() {
    metricReader = InMemoryMetricReader.create();
    SdkMeterProviderBuilder meterProvider =
        SdkMeterProvider.builder().registerMetricReader(metricReader);

    openTelemetrySdk = OpenTelemetrySdk.builder().setMeterProvider(meterProvider.build()).build();

    firestore =
        FirestoreOptions.newBuilder()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setOpenTelemetry(openTelemetrySdk)
                    .build())
            .build()
            .getService();

    AttributesBuilder expectedAttributes = Attributes.builder();
    expectedAttributes.put(
        TelemetryConstants.METRIC_ATTRIBUTE_KEY_LIBRARY_NAME.getKey(),
        TelemetryConstants.FIRESTORE_LIBRARY_NAME);
    expectedAttributes.put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_CLIENT_UID.getKey(), ClientUid);
    if (libraryVersion != null) {
      expectedAttributes.put(
          TelemetryConstants.METRIC_ATTRIBUTE_KEY_LIBRARY_VERSION.getKey(), libraryVersion);
    }

    expectedBaseAttributes = expectedAttributes.build();
  }

  @After
  public void tearDown() throws Exception {
    Preconditions.checkNotNull(
        firestore,
        "Error instantiating Firestore. Check that the service account credentials were properly set.");
    try {
      metricReader.close();
    } finally {
      // This block will always execute, even if an exception occurs above
      firestore.shutdown();
    }
  }

  @Test
  public void simpleOperation() throws Exception {
    firestore.collection("col").document().create(Collections.singletonMap("foo", "bar")).get();

    Attributes attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, "Firestore.Commit")
            .build();

    // Validate GAX layer metrics
    MetricData dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_ATTEMPT_LATENCY);
    validateMetricData(dataFromReader, attributes);
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_ATTEMPT_COUNT);
    validateMetricData(dataFromReader, attributes);
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_OPERATION_COUNT);
    validateMetricData(dataFromReader, attributes);
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_OPERATION_LATENCY);
    validateMetricData(dataFromReader, attributes);

    // Validate SDK layer metric
    attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, "DocumentReference.Create")
            .build();
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributes);
  }

  @Test
  public void operationWithFirstResponseLatency() throws Exception {
    firestore.collection("col").get();

    Attributes attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, "Firestore.RunQuery")
            .build();

    // Validate GAX layer metrics
    MetricData dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_ATTEMPT_LATENCY);
    validateMetricData(dataFromReader, attributes);
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_ATTEMPT_COUNT);
    validateMetricData(dataFromReader, attributes);
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_OPERATION_COUNT);
    validateMetricData(dataFromReader, attributes);
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_OPERATION_LATENCY);
    validateMetricData(dataFromReader, attributes);

    // Validate SDK layer metric
    attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, "RunQuery.Get")
            .build();
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributes);

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    validateMetricData(dataFromReader, attributes);
  }

  @Test
  public void operationWithTransaction() throws Exception {
    firestore
        .runTransaction(
            transaction -> {
              Query q = firestore.collection("col").whereGreaterThan("bla", "");
              DocumentReference d = firestore.collection("col").document("foo");
              DocumentReference[] docList = {d, d};
              // Document Query.
              transaction.get(q).get();

              // Aggregation Query.
              transaction.get(q.count());

              // Commit 2 documents.
              transaction.set(
                  firestore.collection("foo").document("bar"),
                  Collections.singletonMap("foo", "bar"));
              transaction.set(
                  firestore.collection("foo").document("bar2"),
                  Collections.singletonMap("foo2", "bar2"));
              return 0;
            })
        .get();

    List<Attributes> attributesList = new ArrayList<>();
    String[] methods = {
      "Firestore.BeginTransaction",
      "Firestore.RunQuery",
      "Firestore.RunAggregationQuery",
      "Firestore.Commit"
    };

    for (String method : methods) {
      Attributes attributes =
          expectedBaseAttributes
              .toBuilder()
              .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
              .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, method)
              .build();
      attributesList.add(attributes);
    }

    // Validate GAX layer metrics
    MetricData dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_ATTEMPT_LATENCY);
    validateMetricData(dataFromReader, attributesList);
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_ATTEMPT_COUNT);
    validateMetricData(dataFromReader, attributesList);
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_OPERATION_COUNT);
    validateMetricData(dataFromReader, attributesList);
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_OPERATION_LATENCY);
    validateMetricData(dataFromReader, attributesList);

    //     Validate SDK layer metric
    List<Attributes> attributesList2 = new ArrayList<>();

    String[] methods2 = {
      "RunTransaction", "RunQuery.Transactional", "RunAggregationQuery.Transactional",
    };

    for (String method : methods2) {
      Attributes attributes =
          expectedBaseAttributes
              .toBuilder()
              .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
              .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, method)
              .build();
      attributesList2.add(attributes);
    }
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributesList2);

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    validateMetricData(dataFromReader, attributesList2);

    Attributes attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, "RunTransaction")
            .build();

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    validateMetricData(dataFromReader, attributes);

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
    validateMetricData(dataFromReader, attributes);
  }

  private void validateMetricData(MetricData metricData, Attributes expectedAttributes) {
    validateMetricData(metricData, Arrays.asList(expectedAttributes));
  }

  private void validateMetricData(MetricData metricData, List<Attributes> expectedAttributesList) {
    boolean isHistogram = metricData.getType() == MetricDataType.HISTOGRAM;
    Collection<? extends PointData> points =
        isHistogram
            ? metricData.getHistogramData().getPoints()
            : metricData.getLongSumData().getPoints();

    assertThat(points.size()).isEqualTo(expectedAttributesList.size());

    List<Attributes> actualAttributesList = new ArrayList<>();
    for (PointData point : points) {
      if (isHistogram) {
        assertThat(((HistogramPointData) point).getCount()).isAtLeast(1);
        assertThat(((HistogramPointData) point).getSum()).isGreaterThan(0.0);
      } else {
        assertThat(((LongPointData) point).getValue()).isEqualTo(1);
      }
      actualAttributesList.add(point.getAttributes());
    }

    for (Attributes expectedAttributes : expectedAttributesList) {
      assertThat(
              actualAttributesList.stream()
                  .anyMatch(
                      actualAttributes ->
                          actualAttributes
                              .asMap()
                              .entrySet()
                              .containsAll(expectedAttributes.asMap().entrySet())))
          .isTrue();
    }
  }

  private MetricData getMetricData(InMemoryMetricReader reader, String metricName) {
    String fullMetricName = TelemetryConstants.METRIC_PREFIX + "/" + metricName;
    // Fetch the MetricData with retries
    for (int attemptsLeft = 1000; attemptsLeft > 0; attemptsLeft--) {
      List<MetricData> matchingMetadata =
          reader.collectAllMetrics().stream()
              .filter(md -> md.getName().equals(fullMetricName))
              .collect(Collectors.toList());
      assertWithMessage(
              "Found unexpected MetricData with the same name: %s, in: %s",
              fullMetricName, matchingMetadata)
          .that(matchingMetadata.size())
          .isAtMost(1);

      if (!matchingMetadata.isEmpty()) {
        return matchingMetadata.get(0);
      }

      try {
        Thread.sleep(1);
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(interruptedException);
      }
    }

    assertTrue(String.format("MetricData is missing for metric %s", fullMetricName), false);
    return null;
  }
}
