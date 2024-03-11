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

import static com.google.cloud.firestore.telemetry.TraceUtil.*;
import static io.opentelemetry.semconv.resource.attributes.ResourceAttributes.SERVICE_NAME;
import static org.junit.Assert.*;

import com.google.cloud.firestore.*;
import com.google.common.base.Preconditions;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.data.EventData;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ITTracingTest {
  private static final Logger logger =
      Logger.getLogger(com.google.cloud.firestore.it.ITBaseTest.class.getName());

  private static final String SERVICE = "google.firestore.v1.Firestore/";
  private static final String LIST_DOCUMENTS_RPC_NAME = "ListDocuments";
  private static final String BATCH_WRITE_RPC_NAME = "BatchWrite";

  // We use an InMemorySpanExporter for testing which keeps all generated trace spans
  // in memory so that we can check their correctness.
  protected static InMemorySpanExporter inMemorySpanExporter = InMemorySpanExporter.create();

  protected Firestore firestore;

  HashMap<String, String> spanNameToSpanId = new HashMap<>();;
  HashMap<String, String> spanIdToParentSpanId = new HashMap<>();;
  HashMap<String, SpanData> spanNameToSpanData = new HashMap<>();;

  @Rule public TestName testName = new TestName();

  @BeforeClass
  public static void beforeAll() {
    // Create Firestore with an in-memory span exporter.
    GlobalOpenTelemetry.resetForTest();
    Resource resource =
        Resource.getDefault().merge(Resource.builder().put(SERVICE_NAME, "Sparky").build());
    SpanProcessor inMemorySpanProcessor = SimpleSpanProcessor.create(inMemorySpanExporter);
    OpenTelemetrySdk.builder()
        .setTracerProvider(
            SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(inMemorySpanProcessor)
                .setSampler(Sampler.alwaysOn())
                .build())
        .buildAndRegisterGlobal();
  }

  @Before
  public void before() {
    FirestoreOptions.Builder optionsBuilder =
        FirestoreOptions.newBuilder()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder().setTracingEnabled(true).build());
    String namedDb = System.getProperty("FIRESTORE_NAMED_DATABASE");
    if (namedDb != null) {
      logger.log(Level.INFO, "Integration test using named database " + namedDb);
      optionsBuilder = optionsBuilder.setDatabaseId(namedDb);
    } else {
      logger.log(Level.INFO, "Integration test using default database.");
    }
    firestore = optionsBuilder.build().getService();

    // Clean up existing maps.
    spanNameToSpanId.clear();
    spanIdToParentSpanId.clear();
    spanNameToSpanData.clear();
  }

  @After
  public void after() throws Exception {
    Preconditions.checkNotNull(
        firestore,
        "Error instantiating Firestore. Check that the service account credentials were properly set.");
    firestore.shutdown();
    inMemorySpanExporter.reset();
  }

  void waitForTracesToComplete() throws Exception {
    // We need to call `firestore.close()` because that will also close the
    // gRPC channel and hence force the gRPC instrumentation library to flush
    // its spans.
    firestore.close();
  }

  // Prepares all the spans in memory for inspection.
  List<SpanData> prepareSpans() throws Exception {
    waitForTracesToComplete();
    List<SpanData> spans = inMemorySpanExporter.getFinishedSpanItems();
    buildSpanMaps(spans);
    printSpans();
    return spans;
  }

  void buildSpanMaps(List<SpanData> spans) {
    for (SpanData spanData : spans) {
      spanNameToSpanData.put(spanData.getName(), spanData);
      spanNameToSpanId.put(spanData.getName(), spanData.getSpanId());
      spanIdToParentSpanId.put(spanData.getSpanId(), spanData.getParentSpanId());
    }
  }

  // Returns the SpanData object for the span with the given name.
  // Returns null if no span with the given name exists.
  @Nullable
  SpanData getSpanByName(String spanName) {
    return spanNameToSpanData.get(spanName);
  }

  // Returns the SpanData object for the gRPC span with the given RPC name.
  // Returns null if no such span exists.
  @Nullable
  SpanData getGrpcSpanByName(String rpcName) {
    return getSpanByName(SERVICE + rpcName);
  }

  String grpcSpanName(String rpcName) {
    return SERVICE + rpcName;
  }

  void assertSameTrace(SpanData... spans) {
    if (spans.length > 1) {
      String traceId = spans[0].getTraceId();
      for (SpanData spanData : spans) {
        assertEquals(traceId, spanData.getTraceId());
      }
    }
  }

  // Helper to see the spans in standard output while developing tests
  void printSpans() {
    for (SpanData spanData : spanNameToSpanData.values()) {
      System.out.printf(
          "SPAN ID:%s, ParentID:%s, KIND:%s, TRACE ID:%s, NAME:%s, ATTRIBUTES:%s, EVENTS:%s\n",
          spanData.getSpanId(),
          spanData.getParentSpanId(),
          spanData.getKind(),
          spanData.getTraceId(),
          spanData.getName(),
          spanData.getAttributes().toString(),
          spanData.getEvents().toString());
    }
  }

  // Asserts that the span hierarchy exists for the given span names. The hierarchy starts with the
  // root span, followed
  // by the child span, grandchild span, and so on. It also asserts that all the given spans belong
  // to the same trace,
  // and that Firestore-generated spans contain the expected Firestore attributes.
  void assertSpanHierarchy(String... spanNamesHierarchy) {
    List<String> spanNames = Arrays.asList(spanNamesHierarchy);

    for (int i = 0; i + 1 < spanNames.size(); ++i) {
      String parentSpanName = spanNames.get(i);
      String childSpanName = spanNames.get(i + 1);
      SpanData parentSpan = getSpanByName(parentSpanName);
      SpanData childSpan = getSpanByName(childSpanName);
      assertNotNull(parentSpan);
      assertNotNull(childSpan);
      assertEquals(childSpan.getParentSpanId(), parentSpan.getSpanId());
      assertSameTrace(childSpan, parentSpan);
      // gRPC spans do not have Firestore attributes.
      if (!parentSpanName.startsWith(SERVICE)) {
        assertHasExpectedAttributes(parentSpan);
      }
      if (!childSpanName.startsWith(SERVICE)) {
        assertHasExpectedAttributes(childSpan);
      }
    }
  }

  void assertHasExpectedAttributes(SpanData spanData, String... additionalExpectedAttributes) {
    // All Firestore-generated spans have the settings attributes.
    List<String> expectedAttributes =
        Arrays.asList(
            "gcp.firestore.memoryUtilization",
            "gcp.firestore.settings.host",
            "gcp.firestore.settings.databaseId",
            "gcp.firestore.settings.channel.needsCredentials",
            "gcp.firestore.settings.channel.needsEndpoint",
            "gcp.firestore.settings.channel.needsHeaders",
            "gcp.firestore.settings.channel.shouldAutoClose",
            "gcp.firestore.settings.channel.transportName",
            "gcp.firestore.settings.retrySettings.maxRpcTimeout",
            "gcp.firestore.settings.retrySettings.retryDelayMultiplier",
            "gcp.firestore.settings.retrySettings.initialRetryDelay",
            "gcp.firestore.settings.credentials.authenticationType",
            "gcp.firestore.settings.retrySettings.maxAttempts",
            "gcp.firestore.settings.retrySettings.maxRetryDelay",
            "gcp.firestore.settings.retrySettings.rpcTimeoutMultiplier",
            "gcp.firestore.settings.retrySettings.totalTimeout",
            "gcp.firestore.settings.retrySettings.initialRpcTimeout");

    expectedAttributes.addAll(Arrays.asList(additionalExpectedAttributes));

    Attributes spanAttributes = spanData.getAttributes();
    for (String expectedAttribute : expectedAttributes) {
      assertNotNull(spanAttributes.get(AttributeKey.stringKey(expectedAttribute)));
    }
  }

  @Test
  public void aggregateQueryGet() throws Exception {
    firestore.collection("col").count().get().get();
    waitForTracesToComplete();
    List<SpanData> spans = inMemorySpanExporter.getFinishedSpanItems();
    buildSpanMaps(spans);
    printSpans();
    assertEquals(2, spans.size());
    SpanData getSpan = getSpanByName(SPAN_NAME_AGGREGATION_QUERY_GET);
    SpanData grpcSpan = getGrpcSpanByName(SPAN_NAME_RUN_AGGREGATION_QUERY);
    assertNotNull(getSpan);
    assertNotNull(grpcSpan);
    assertEquals(grpcSpan.getParentSpanId(), getSpan.getSpanId());
    assertSameTrace(getSpan, grpcSpan);
    assertHasExpectedAttributes(getSpan);
    List<EventData> events = getSpan.getEvents();
    assertTrue(events.size() > 0);
    assertTrue(events.get(0).getAttributes().size() > 0);
    assertEquals(events.get(0).getName(), "RunAggregationQuery Stream started.");
    assertEquals(
        events.get(0).getAttributes().get(AttributeKey.booleanKey("isRetryAttempt")), false);
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

    List<SpanData> spans = prepareSpans();
    assertEquals(2, spans.size());
    assertSpanHierarchy(SPAN_NAME_BULK_WRITER_COMMIT, grpcSpanName(BATCH_WRITE_RPC_NAME));
  }

  @Test
  public void partitionQuery() throws Exception {
    CollectionGroup collectionGroup = firestore.collectionGroup("col");
    collectionGroup.getPartitions(3).get();

    List<SpanData> spans = prepareSpans();
    assertEquals(2, spans.size());
    assertSpanHierarchy(SPAN_NAME_PARTITION_QUERY, grpcSpanName(SPAN_NAME_PARTITION_QUERY));
  }

  @Test
  public void collectionListDocuments() throws Exception {
    firestore.collection("col").listDocuments();

    List<SpanData> spans = prepareSpans();
    assertEquals(2, spans.size());
    assertSpanHierarchy(SPAN_NAME_COL_REF_LIST_DOCUMENTS, grpcSpanName(LIST_DOCUMENTS_RPC_NAME));
  }
}
