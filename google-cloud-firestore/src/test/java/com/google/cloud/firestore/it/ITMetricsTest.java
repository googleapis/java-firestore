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

import static com.google.cloud.firestore.LocalFirestoreHelper.map;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.api.core.ApiFuture;
import com.google.api.gax.grpc.GrpcStatusCode;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.telemetry.ClientIdentifier;
import com.google.cloud.firestore.telemetry.TelemetryConstants;
import com.google.common.base.Preconditions;
import io.grpc.Status;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.data.HistogramPointData;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.data.MetricDataType;
import io.opentelemetry.sdk.metrics.data.PointData;
import io.opentelemetry.sdk.testing.exporter.InMemoryMetricReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.threeten.bp.Duration;

public class ITMetricsTest {
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
    openTelemetrySdk = setupOpenTelemetrySdk();
    firestore = setupFirestoreService();
    Preconditions.checkNotNull(
        firestore,
        "Error instantiating Firestore. Check that the service account credentials were properly set.");
    expectedBaseAttributes = buildExpectedAttributes();
  }

  private OpenTelemetrySdk setupOpenTelemetrySdk() {
    SdkMeterProviderBuilder meterProvider =
        SdkMeterProvider.builder().registerMetricReader(metricReader);
    return OpenTelemetrySdk.builder().setMeterProvider(meterProvider.build()).build();
  }

  private Firestore setupFirestoreService() {
    return FirestoreOptions.newBuilder()
        .setOpenTelemetryOptions(
            FirestoreOpenTelemetryOptions.newBuilder().setOpenTelemetry(openTelemetrySdk).build())
        .build()
        .getService();
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

  @After
  public void tearDown() {
    try {
      metricReader.shutdown();
    } finally {
      firestore.shutdown();
    }
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

  @Test
  public void queryGet() throws Exception {
    firestore.collection("col").get().get();

    // Validate GAX layer metrics
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.RunQuery", Status.OK.getCode().toString(), 1)
            .build();
    validateGaxMetrics(expectedMetrics);

    // Validate SDK layer metric
    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_QUERY_GET, Status.OK.getCode().toString(), 1)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY, expectedMetrics);
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
  }

  @Test
  public void queryExplain() throws Exception {
    Query query = firestore.collection("col");
    ApiFuture<ExplainMetrics> metricsFuture =
        query.explainStream(
            ExplainOptions.builder().setAnalyze(false).build(),
            new ApiStreamObserver<DocumentSnapshot>() {
              @Override
              public void onNext(DocumentSnapshot documentSnapshot) {
                fail("No DocumentSnapshot should be received because analyze option was disabled.");
              }

              @Override
              public void onError(Throwable throwable) {
                fail(throwable.getMessage());
              }

              @Override
              public void onCompleted() {}
            });

    ExplainMetrics metrics = metricsFuture.get();
    assertThat(metrics.getPlanSummary().getIndexesUsed().size()).isGreaterThan(0);
    assertThat(metrics.getExecutionStats()).isNull();

    // Validate GAX layer metrics
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.RunQuery", Status.OK.getCode().toString(), 1)
            .build();
    validateGaxMetrics(expectedMetrics);

    // Validate SDK layer metric
    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_QUERY_EXPLAIN, Status.OK.getCode().toString(), 1)
            .build();

    validateSDKMetrics(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY, expectedMetrics);
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
  }

  @Test
  public void aggregateQueryGet() throws Exception {
    firestore.collection("col").count().get().get();

    // Validate GAX layer metrics
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.RunAggregationQuery", Status.OK.getCode().toString(), 1)
            .build();
    validateGaxMetrics(expectedMetrics);

    // Validate SDK layer metric
    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_AGGREGATION_QUERY_GET,
                Status.OK.getCode().toString(),
                1)
            .build();

    validateSDKMetrics(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY, expectedMetrics);
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
  }

  @Test
  public void writeBatch() throws Exception {
    WriteBatch batch = firestore.batch();
    DocumentReference docRef = firestore.collection("foo").document();
    batch.create(docRef, Collections.singletonMap("foo", "bar"));
    batch.update(docRef, Collections.singletonMap("foo", "bar"));
    batch.delete(docRef);
    batch.commit().get();

    // Validate GAX layer metrics
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.Commit", Status.OK.getCode().toString(), 1)
            .build();
    validateGaxMetrics(expectedMetrics);

    // Validate SDK layer metric
    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_BATCH_COMMIT, Status.OK.getCode().toString(), 1)
            .build();

    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
  }

  @Test
  public void bulkWriterCommit() throws Exception {
    ScheduledExecutorService bulkWriterExecutor = Executors.newSingleThreadScheduledExecutor();
    BulkWriter bulkWriter =
        firestore.bulkWriter(BulkWriterOptions.builder().setExecutor(bulkWriterExecutor).build());
    bulkWriter.set(
        firestore.collection("col").document("foo"),
        Collections.singletonMap("bulk-foo", "bulk-bar"));
    bulkWriter.close();
    bulkWriterExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);

    // Validate GAX layer metrics
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.BatchWrite", Status.OK.getCode().toString(), 1)
            .build();
    validateGaxMetrics(expectedMetrics);

    // Validate SDK layer metric
    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_BULK_WRITER_COMMIT,
                Status.OK.getCode().toString(),
                1)
            .build();

    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
  }

  @Test
  public void partitionQuery() throws Exception {
    CollectionGroup collectionGroup = firestore.collectionGroup("col");
    collectionGroup.getPartitions(3).get();

    // Note: pagedCallable requests are not traced at GAX layer
    // Validate SDK layer metric
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_PARTITION_QUERY, Status.OK.getCode().toString(), 1)
            .build();

    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
  }

  @Test
  public void listCollection() throws Exception {
    firestore.collection("col").document("doc0").listCollections();

    // Note: pagedCallable requests are not traced at GAX layer
    // Validate SDK layer metric
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_DOC_REF_LIST_COLLECTIONS,
                Status.OK.getCode().toString(),
                1)
            .build();

    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
  }

  @Test
  public void collectionListDocuments() throws Exception {
    firestore.collection("col").listDocuments();

    // Note: pagedCallable requests are not traced at GAX layer
    // Validate SDK layer metric
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_COL_REF_LIST_DOCUMENTS,
                Status.OK.getCode().toString(),
                1)
            .build();

    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
  }

  @Test
  public void docRefSet() throws Exception {
    firestore
        .collection("col")
        .document("foo")
        .set(Collections.singletonMap("foo", "bar"), SetOptions.merge())
        .get();

    // Validate GAX layer metrics
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.Commit", Status.OK.getCode().toString(), 1)
            .build();
    validateGaxMetrics(expectedMetrics);

    // Validate SDK layer metric
    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_BATCH_COMMIT, Status.OK.getCode().toString(), 1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_DOC_REF_SET, Status.OK.getCode().toString(), 1)
            .build();

    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
  }

  @Test
  public void getAll() throws Exception {
    DocumentReference docRef0 = firestore.collection("col").document();
    DocumentReference docRef1 = firestore.collection("col").document();
    DocumentReference[] docs = {docRef0, docRef1};
    firestore.getAll(docs).get();

    // Validate GAX layer metrics
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.BatchGetDocuments", Status.OK.getCode().toString(), 1)
            .build();
    validateGaxMetrics(expectedMetrics);

    // Validate SDK layer metric
    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_BATCH_GET_DOCUMENTS,
                Status.OK.getCode().toString(),
                1)
            .build();

    validateSDKMetrics(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY, expectedMetrics);
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
  }

  @Test
  public void transaction() throws Exception {
    firestore
        .runTransaction( // Has end-to-end,  first response latency and transaction metrics.
            transaction -> {
              Query q = firestore.collection("col").whereGreaterThan("bla", "");
              // Document Query. Has end-to-end and first response latency which is marked
              // transactional
              transaction.get(q).get();

              // Aggregation Query. Has end-to-end and first response latency which is marked
              // transactional
              transaction.get(q.count());

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

    // Validate GAX layer metrics
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.BeginTransaction", Status.OK.getCode().toString(), 1)
            .expectMetricData("Firestore.RunQuery", Status.OK.getCode().toString(), 1)
            .expectMetricData("Firestore.RunAggregationQuery", Status.OK.getCode().toString(), 1)
            .expectMetricData("Firestore.Commit", Status.OK.getCode().toString(), 1)
            .build();

    validateGaxMetrics(expectedMetrics);

    // Validate SDK layer metric
    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION, Status.OK.getCode().toString(), 1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_TRANSACTION_GET_QUERY,
                Status.OK.getCode().toString(),
                1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_TRANSACTION_GET_AGGREGATION_QUERY,
                Status.OK.getCode().toString(),
                1)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY, expectedMetrics);

    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION, Status.OK.getCode().toString(), 1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_TRANSACTION_GET_QUERY,
                Status.OK.getCode().toString(),
                1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_TRANSACTION_GET_AGGREGATION_QUERY,
                Status.OK.getCode().toString(),
                1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_TRANSACTION_COMMIT,
                Status.OK.getCode().toString(),
                1)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);

    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION, Status.OK.getCode().toString(), 1)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY, expectedMetrics);
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT, expectedMetrics);
  }

  @Test
  public void transactionWithRollback() throws Exception {
    String myErrorMessage = "My error message.";
    try {
      firestore
          .runTransaction(
              // BeginTransaction is successful, RunTransaction receives its first response.
              transaction -> {
                if (true) {
                  // Transaction encounters not retryable error, ends with failure
                  throw (new Exception(myErrorMessage));
                }
                return 0;
              })
          .get();
    } catch (Exception e) {
      // Catch and move on.
    }

    // Validate GAX layer metrics
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.BeginTransaction", Status.OK.getCode().toString(), 1)
            .expectMetricData("Firestore.Rollback", Status.OK.getCode().toString(), 1)
            .build();
    validateGaxMetrics(expectedMetrics);

    // Validate SDK layer metric
    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION, Status.OK.getCode().toString(), 1)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY, expectedMetrics);

    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION,
                Status.UNKNOWN.getCode().toString(),
                1)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY, expectedMetrics);
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT, expectedMetrics);
  }

  @Test
  public void transactionWithFailure() throws Exception {
    Firestore invalidFirestore =
        FirestoreOptions.newBuilder()
            .setDatabaseId("foo.bar") // Invalid databaseId
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setOpenTelemetry(openTelemetrySdk)
                    .build())
            .build()
            .getService();

    try {
      invalidFirestore
          .runTransaction(
              // Transaction cannot get started due to the invalid database ID
              transaction -> {
                return 0;
              })
          .get();
    } catch (Exception e) {
      // Catch and move on.
    }

    // Validate GAX layer metrics
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                "Firestore.BeginTransaction", Status.NOT_FOUND.getCode().toString(), 1)
            .build();
    validateGaxMetrics(expectedMetrics);

    // Validate SDK layer metric
    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION,
                Status.UNKNOWN
                    .getCode()
                    .toString(), // TODO(b/305998085):Change this to correct status code
                1)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY, expectedMetrics);
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY, expectedMetrics);
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT, expectedMetrics);
  }

  @Test
  public void transactionWithRetry() throws Exception {
    // Throw a retryable error
    ApiException RETRYABLE_API_EXCEPTION =
        new ApiException(
            new Exception("Test exception"), GrpcStatusCode.of(Status.Code.UNKNOWN), true);

    try {
      firestore
          .runTransaction(
              // RunTransaction retries from BeginTransaction for 3 times
              new Transaction.Function<Integer>() {
                int cnt = 1;

                @Override
                public Integer updateCallback(Transaction transaction) throws Exception {
                  cnt++;
                  if (cnt <= 3) {
                    throw RETRYABLE_API_EXCEPTION;
                  }
                  return 0;
                }
              })
          .get();
    } catch (Exception e) {
      // Catch and move on.
    }

    // Validate GAX layer metrics
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.BeginTransaction", Status.OK.getCode().toString(), 3)
            .expectMetricData("Firestore.Rollback", Status.OK.getCode().toString(), 2)
            .expectMetricData("Firestore.Commit", Status.OK.getCode().toString(), 1)
            .build();
    validateGaxMetrics(expectedMetrics);

    // Validate SDK layer metric
    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION, Status.OK.getCode().toString(), 1)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY, expectedMetrics);

    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION, Status.OK.getCode().toString(), 1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_TRANSACTION_COMMIT,
                Status.OK.getCode().toString(),
                1)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);

    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION, Status.OK.getCode().toString(), 1)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY, expectedMetrics);

    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION, Status.OK.getCode().toString(), 3)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT, expectedMetrics);
  }

  @Test
  public void multipleOperations() throws Exception {
    // 2 get query
    firestore.collection("col1").get().get();
    firestore.collection("col2").get().get();
    // 1 aggregation get query
    firestore.collection("col").count().get().get();
    // 3 batch commits on document reference: set, update,delete
    DocumentReference ref = firestore.collection("col").document("doc1");
    ref.set(Collections.singletonMap("foo", "bar")).get();
    ref.update(map("foo", "newBar")).get();
    ref.delete().get();

    // Validate GAX layer metrics
    Map<String, MetricInfo> expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData("Firestore.RunQuery", Status.OK.getCode().toString(), 2)
            .expectMetricData("Firestore.RunAggregationQuery", Status.OK.getCode().toString(), 1)
            .expectMetricData("Firestore.Commit", Status.OK.getCode().toString(), 3)
            .build();
    validateGaxMetrics(expectedMetrics);

    // Validate SDK layer metric
    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_QUERY_GET, Status.OK.getCode().toString(), 2)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_AGGREGATION_QUERY_GET,
                Status.OK.getCode().toString(),
                1)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY, expectedMetrics);

    expectedMetrics =
        new MetricsExpectationBuilder()
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_QUERY_GET, Status.OK.getCode().toString(), 2)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_AGGREGATION_QUERY_GET,
                Status.OK.getCode().toString(),
                1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_DOC_REF_SET, Status.OK.getCode().toString(), 1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_DOC_REF_UPDATE, Status.OK.getCode().toString(), 1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_DOC_REF_DELETE, Status.OK.getCode().toString(), 1)
            .expectMetricData(
                TelemetryConstants.METHOD_NAME_BATCH_COMMIT, Status.OK.getCode().toString(), 3)
            .build();
    validateSDKMetrics(TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY, expectedMetrics);

    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    assertMetricAbsent(TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
  }

  private Attributes buildAttributes(String method, String status) {
    return expectedBaseAttributes
        .toBuilder()
        .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, status)
        .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, method)
        .build();
  }

  private void validateSDKMetrics(String metric, Map<String, MetricInfo> expectedMetrics) {
    MetricData dataFromReader = getMetricData(metric);
    validateMetricData(dataFromReader, expectedMetrics);
  }

  private void validateGaxMetrics(Map<String, MetricInfo> expectedMetrics) {
    List<String> gaxMetricNames =
        Arrays.asList(
            TelemetryConstants.METRIC_NAME_ATTEMPT_LATENCY,
            TelemetryConstants.METRIC_NAME_ATTEMPT_COUNT,
            TelemetryConstants.METRIC_NAME_OPERATION_COUNT,
            TelemetryConstants.METRIC_NAME_OPERATION_LATENCY);
    for (String metricName : gaxMetricNames) {
      MetricData dataFromReader = getMetricData(metricName);
      validateMetricData(dataFromReader, expectedMetrics);
    }
  }

  private void validateMetricData(MetricData metricData, Map<String, MetricInfo> expectedMetrics) {
    boolean isHistogram = metricData.getType() == MetricDataType.HISTOGRAM;
    Collection<? extends PointData> points =
        isHistogram
            ? metricData.getHistogramData().getPoints()
            : metricData.getLongSumData().getPoints();
    assertThat(points.size()).isEqualTo(expectedMetrics.size());

    for (PointData point : points) {
      String method = point.getAttributes().get(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD);
      MetricInfo expectedMetricInfo = expectedMetrics.get(method);
      if (isHistogram) {
        assertThat(((HistogramPointData) point).getCount()).isEqualTo(expectedMetricInfo.count);
        assertThat(((HistogramPointData) point).getSum()).isGreaterThan(0.0);
      } else {
        assertThat(((LongPointData) point).getValue()).isEqualTo(expectedMetricInfo.count);
      }
      assertThat(point.getAttributes().asMap())
          .containsAtLeastEntriesIn(expectedMetricInfo.attributes.asMap());
    }
  }

  private MetricData getMetricData(String metricName) {
    String fullMetricName = TelemetryConstants.METRIC_PREFIX + "/" + metricName;
    // Fetch the MetricData with retries
    for (int attemptsLeft = 10; attemptsLeft > 0; attemptsLeft--) {
      List<MetricData> matchingMetadata =
          metricReader.collectAllMetrics().stream()
              .filter(md -> md.getName().equals(fullMetricName))
              .collect(Collectors.toList());
      assertWithMessage(
              "Found unexpected MetricData with the same name: %s, in: %s",
              fullMetricName, matchingMetadata)
          .that(matchingMetadata.size())
          .isAtMost(1);

      // Tests could be flaky as the metric reader could have matching data, but it is partial.
      if (!matchingMetadata.isEmpty()) {
        return matchingMetadata.get(0);
      }

      try {
        Thread.sleep(Duration.ofSeconds(1).toMillis());
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(interruptedException);
      }
    }

    assertTrue(String.format("MetricData is missing for metric %s", fullMetricName), false);
    return null;
  }

  private void assertMetricAbsent(String metricName) {
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
