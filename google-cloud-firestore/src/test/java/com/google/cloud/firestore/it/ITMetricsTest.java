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
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.cloud.firestore.BulkWriter;
import com.google.cloud.firestore.BulkWriterOptions;
import com.google.cloud.firestore.CollectionGroup;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.ExplainMetrics;
import com.google.cloud.firestore.ExplainOptions;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOpenTelemetryOptions;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.telemetry.ClientIdentifier;
import com.google.cloud.firestore.telemetry.TelemetryConstants;
import com.google.common.base.Preconditions;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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
      firestore.shutdown();
    }
  }

  @Test
  public void queryGet() throws Exception {
    firestore.collection("col").get().get();

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
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD,
                TelemetryConstants.METHOD_NAME_QUERY_GET)
            .build();
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributes);

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    validateMetricData(dataFromReader, attributes);
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
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD,
                TelemetryConstants.METHOD_NAME_QUERY_EXPLAIN)
            .build();
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributes);

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    validateMetricData(dataFromReader, attributes);
  }

  @Test
  public void aggregateQueryGet() throws Exception {
    firestore.collection("col").count().get().get();

    Attributes attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, "Firestore.RunAggregationQuery")
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
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD,
                TelemetryConstants.METHOD_NAME_AGGREGATION_QUERY_GET)
            .build();
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributes);

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    validateMetricData(dataFromReader, attributes);
  }

  @Test
  public void writeBatch() throws Exception {
    WriteBatch batch = firestore.batch();
    DocumentReference docRef = firestore.collection("foo").document();
    batch.create(docRef, Collections.singletonMap("foo", "bar"));
    batch.update(docRef, Collections.singletonMap("foo", "bar"));
    batch.delete(docRef);
    batch.commit().get();

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
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD,
                TelemetryConstants.METHOD_NAME_BATCH_COMMIT)
            .build();

    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributes);
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

    Attributes attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, "Firestore.BatchWrite")
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
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD,
                TelemetryConstants.METHOD_NAME_BULK_WRITER_COMMIT)
            .build();

    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributes);
  }

  @Test
  public void partitionQuery() throws Exception {
    CollectionGroup collectionGroup = firestore.collectionGroup("col");
    collectionGroup.getPartitions(3).get();
    // TODO(Metrics): pagedCalled reqeusts are not traced at GAX layer

    // Validate SDK layer metric
    Attributes attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD,
                TelemetryConstants.METHOD_NAME_PARTITION_QUERY)
            .build();

    MetricData dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributes);
  }

  @Test
  public void listCollection() throws Exception {
    firestore.collection("col").document("doc0").listCollections();
    // TODO(Metrics): pagedCalled reqeusts are not traced at GAX layer

    // Validate SDK layer metric
    Attributes attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD,
                TelemetryConstants.METHOD_NAME_DOC_REF_LIST_COLLECTIONS)
            .build();

    MetricData dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributes);
  }

  @Test
  public void collectionListDocuments() throws Exception {
    firestore.collection("col").listDocuments();
    // TODO(Metrics): pagedCalled reqeusts are not traced at GAX layer

    // Validate SDK layer metric
    Attributes attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD,
                TelemetryConstants.METHOD_NAME_COL_REF_LIST_DOCUMENTS)
            .build();

    MetricData dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributes);
  }

  @Test
  public void docRefSet() throws Exception {
    firestore
        .collection("col")
        .document("foo")
        .set(Collections.singletonMap("foo", "bar"), SetOptions.merge())
        .get();

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

    List<Attributes> attributesList = new ArrayList<>();
    String[] methods = {
      TelemetryConstants.METHOD_NAME_BATCH_COMMIT, TelemetryConstants.METHOD_NAME_DOC_REF_SET
    };

    for (String method : methods) {
      attributes =
          expectedBaseAttributes
              .toBuilder()
              .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
              .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, method)
              .build();
      attributesList.add(attributes);
    }

    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributesList);
  }

  @Test
  public void getAll() throws Exception {
    DocumentReference docRef0 = firestore.collection("col").document();
    DocumentReference docRef1 = firestore.collection("col").document();
    DocumentReference[] docs = {docRef0, docRef1};
    firestore.getAll(docs).get();

    Attributes attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, "Firestore.BatchGetDocuments")
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
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD,
                TelemetryConstants.METHOD_NAME_BATCH_GET_DOCUMENTS)
            .build();

    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributes);

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    validateMetricData(dataFromReader, attributes);
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

              // Commit 2 documents. Only has RPC layer metrics
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

    // Validate SDK layer metric
    attributesList = new ArrayList<>();

    String[] methods2 = {
      TelemetryConstants.METHOD_NAME_RUN_TRANSACTION,
      TelemetryConstants.METHOD_NAME_TRANSACTION_GET_QUERY,
      TelemetryConstants.METHOD_NAME_TRANSACTION_GET_AGGREGATION_QUERY,
    };

    for (String method : methods2) {
      Attributes attributes =
          expectedBaseAttributes
              .toBuilder()
              .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
              .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, method)
              .build();
      attributesList.add(attributes);
    }
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributesList);

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    validateMetricData(dataFromReader, attributesList);

    Attributes attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD,
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION)
            .build();

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    validateMetricData(dataFromReader, attributes);

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
    validateMetricData(dataFromReader, attributes);
  }

  @Test
  public void transactionWithRollback() throws Exception {
    String myErrorMessage = "My error message.";
    try {
      firestore
          .runTransaction(
              transaction -> {
                if (true) {
                  throw (new Exception(myErrorMessage));
                }
                return 0;
              })
          .get();
    } catch (Exception e) {
      // Catch and move on.
    }

    List<Attributes> attributesList = new ArrayList<>();
    String[] methods = {
      "Firestore.BeginTransaction", "Firestore.Rollback",
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

    // Validate SDK layer metric
    Attributes attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS,
                "OK") // Transaction began successfully.
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD,
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION)
            .build();

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    validateMetricData(dataFromReader, attributes);

    attributes =
        expectedBaseAttributes
            .toBuilder()
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS,
                "UNKNOWN") // Transaction ended with exception
            .put(
                TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD,
                TelemetryConstants.METHOD_NAME_RUN_TRANSACTION)
            .build();

    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributes);

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_TRANSACTION_LATENCY);
    validateMetricData(dataFromReader, attributes);

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_TRANSACTION_ATTEMPT_COUNT);
    validateMetricData(dataFromReader, attributes);
  }

  @Test
  public void multipleOperations() throws Exception {
    CollectionReference col = firestore.collection("col");
    col.get().get();
    col.count().get().get();
    DocumentReference ref = col.document("doc1");
    ref.set(Collections.singletonMap("foo", "bar")).get();
    ref.update(map("foo", "newBar")).get();
    ref.delete().get();

    List<Attributes> attributesList = new ArrayList<>();
    String[] methods = {"Firestore.RunQuery", "Firestore.RunAggregationQuery", "Firestore.Commit"};

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

    // Validate SDK layer metric
    attributesList = new ArrayList<>();

    String[] methods2 = {
      TelemetryConstants.METHOD_NAME_QUERY_GET,
      TelemetryConstants.METHOD_NAME_AGGREGATION_QUERY_GET,
      TelemetryConstants.METHOD_NAME_DOC_REF_SET,
      TelemetryConstants.METHOD_NAME_DOC_REF_UPDATE,
      TelemetryConstants.METHOD_NAME_DOC_REF_DELETE,
      TelemetryConstants.METHOD_NAME_BATCH_COMMIT,
    };

    for (String method : methods2) {
      Attributes attributes =
          expectedBaseAttributes
              .toBuilder()
              .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
              .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, method)
              .build();
      attributesList.add(attributes);
    }
    dataFromReader = getMetricData(metricReader, TelemetryConstants.METRIC_NAME_END_TO_END_LATENCY);
    validateMetricData(dataFromReader, attributesList);

    attributesList = new ArrayList<>();

    String[] methods3 = {
      "RunQuery.Get", "RunAggregationQuery.Get",
    };

    for (String method : methods3) {
      Attributes attributes =
          expectedBaseAttributes
              .toBuilder()
              .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS, "OK")
              .put(TelemetryConstants.METRIC_ATTRIBUTE_KEY_METHOD, method)
              .build();
      attributesList.add(attributes);
    }

    dataFromReader =
        getMetricData(metricReader, TelemetryConstants.METRIC_NAME_FIRST_RESPONSE_LATENCY);
    validateMetricData(dataFromReader, attributesList);
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
        assertThat(((LongPointData) point).getValue()).isAtLeast(1);
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
