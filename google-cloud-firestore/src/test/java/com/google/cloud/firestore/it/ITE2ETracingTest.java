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

import static io.opentelemetry.semconv.resource.attributes.ResourceAttributes.SERVICE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOpenTelemetryOptions;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.opentelemetry.trace.TraceConfiguration;
import com.google.cloud.opentelemetry.trace.TraceExporter;
import com.google.cloud.trace.v1.TraceServiceClient;
import com.google.common.base.Preconditions;
import com.google.devtools.cloudtrace.v1.Trace;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.TraceFlags;
import io.opentelemetry.api.trace.TraceState;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ITE2ETracingTest extends ITBaseTest {

  private static final Logger logger = Logger.getLogger(ITBaseTest.class.getName());

  private static final int NUM_TRACE_ID_BYTES = 32;

  private static final int NUM_SPAN_ID_BYTES = 16;

  private static final int GET_TRACE_RETRY_COUNT = 10;

  private static final int GET_TRACE_RETRY_BACKOFF_MILLIS = 1000;

  private static final int TRACE_FORCE_FLUSH_MILLIS = 1000;

  private static final int TRACE_PROVIDER_SHUTDOWN_MILLIS = 1000;

  // Random int generator for trace ID and span ID
  private static Random random;

  private static TraceExporter traceExporter;

  // Required for reading back traces from Cloud Trace for validation
  private static TraceServiceClient traceClient_v1;

  // Trace received
  private static Trace retrievedTrace;

  private static String rootSpanName;
  private static Tracer tracer;

  // Required to set custom-root span
  private static OpenTelemetrySdk openTelemetrySdk;

  private static String projectId;

  private static Firestore firestore;

  @BeforeClass
  public static void setup() throws IOException {
    projectId = FirestoreOptions.getDefaultProjectId();
    logger.info("projectId:" + projectId);

    // Set up OTel SDK
    Resource resource =
        Resource.getDefault().merge(Resource.builder().put(SERVICE_NAME, "Sparky").build());

    // TODO(jimit) Make it re-usable w/ InMemorySpanExporter
    traceExporter =
        TraceExporter.createWithConfiguration(
            TraceConfiguration.builder().setProjectId(projectId).build());

    openTelemetrySdk =
        OpenTelemetrySdk.builder()
            .setTracerProvider(
                SdkTracerProvider.builder()
                    .setResource(resource)
                    .addSpanProcessor(BatchSpanProcessor.builder(traceExporter).build())
                    .setSampler(Sampler.alwaysOn())
                    .build())
            .build();

    traceClient_v1 = TraceServiceClient.create();

    // Initialize the Firestore DB w/ the OTel SDK
    FirestoreOptions.Builder optionsBuilder =
        FirestoreOptions.newBuilder()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setOpenTelemetry(openTelemetrySdk)
                    .setTracingEnabled(true)
                    .build());

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
    random = new Random();
  }

  @Before
  public void before() throws Exception {
    rootSpanName =
        String.format("%s%d", this.getClass().getSimpleName(), System.currentTimeMillis());
    tracer =
        firestore.getOptions().getOpenTelemetryOptions().getOpenTelemetry().getTracer(rootSpanName);
    assertNull(retrievedTrace);
  }

  @After
  public void after() throws Exception {
    rootSpanName = null;
    tracer = null;
    retrievedTrace = null;
  }

  @AfterClass
  public static void teardown() {
    traceClient_v1.close();
    CompletableResultCode completableResultCode =
        openTelemetrySdk.getSdkTracerProvider().shutdown();
    completableResultCode.join(TRACE_PROVIDER_SHUTDOWN_MILLIS, TimeUnit.MILLISECONDS);
    firestore.shutdown();
  }

  // Generates a random 32-byte hex string
  private String generateRandomHexString(int numBytes) {
    StringBuffer newTraceId = new StringBuffer();
    while (newTraceId.length() < numBytes) {
      newTraceId.append(Integer.toHexString(random.nextInt()));
    }
    return newTraceId.substring(0, numBytes);
  }

  protected String generateNewTraceId() {
    return generateRandomHexString(NUM_TRACE_ID_BYTES);
  }

  // Generates a random 16-byte hex string
  protected String generateNewSpanId() {
    return generateRandomHexString(NUM_SPAN_ID_BYTES);
  }

  // Cloud Trace indexes traces w/ eventual consistency, even when indexing traceId, therefore the
  // test must retry a few times before the trace is available.
  protected Trace getTraceWithRetry(String project, String traceId)
      throws InterruptedException, RuntimeException {
    int retryCount = GET_TRACE_RETRY_COUNT;

    while (retryCount-- > 0) {
      try {
        Trace t = traceClient_v1.getTrace(project, traceId);
        return t;
      } catch (NotFoundException notFound) {
        logger.warning("Trace not found, retrying in " + GET_TRACE_RETRY_BACKOFF_MILLIS + " ms");
        Thread.sleep(GET_TRACE_RETRY_BACKOFF_MILLIS);
      }
    }
    throw new RuntimeException(
        "Trace " + traceId + " for project " + project + " not found in Cloud Trace.");
  }

  // Generates a new SpanContext w/ random traceId,spanId
  protected SpanContext getNewSpanContext() {
    String traceId = generateNewTraceId();
    String spanId = generateNewSpanId();
    logger.info("traceId=" + traceId + ", spanId=" + spanId);

    return SpanContext.create(traceId, spanId, TraceFlags.getSampled(), TraceState.getDefault());
  }

  protected Span getNewRootSpanWithContext(SpanContext spanContext) {
    // Execute the DB operation in the context of the custom root span.
    return tracer.spanBuilder(rootSpanName)
        .setParent(Context.root().with(Span.wrap(spanContext)))
        .startSpan();
  }

  protected void waitForTracesToComplete() throws Exception {
    CompletableResultCode completableResultCode =
        openTelemetrySdk.getSdkTracerProvider().forceFlush();
    completableResultCode.join(TRACE_FORCE_FLUSH_MILLIS, TimeUnit.MILLISECONDS);

    // We need to call `firestore.close()` because that will also close the
    // gRPC channel and hence force the gRPC instrumentation library to flush
    // its spans.
    firestore.close();
  }

  // Validates `retrievedTrace`
  protected void fetchAndValidateTraces(String traceId, String... spanNames)
      throws InterruptedException {
    // Fetch traces
    retrievedTrace = getTraceWithRetry(projectId, traceId);
    List<String> spanNameList = Arrays.asList(spanNames);

    // Validate trace spans
    assertEquals(retrievedTrace.getTraceId(), traceId);
    assertEquals(retrievedTrace.getSpans(0).getName(), rootSpanName);
    for (int i = 0; i < spanNameList.size() ; ++i) {
      assertEquals(retrievedTrace.getSpans(i+1).getName(), spanNameList.get(i));
    }
  }

  @Test
  // Trace an Aggregation.Get request
  public void aggregateQueryGet() throws Exception {
    // Create custom span context for trace ID injection
    SpanContext newCtx = getNewSpanContext();

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext(newCtx);
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").count().get().get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(newCtx.getTraceId(), "AggregationQuery.Get");
  }

  public void bulkWriterCommit() throws Exception {
  }

  @Test
  public void partitionQuery() throws Exception {
  }

  @Test
  public void collectionListDocuments() throws Exception {
  }

  public void docRefCreate() throws Exception {
  }

  @Test
  public void docRefCreate2() throws Exception {
  }

  @Test
  public void docRefSet() throws Exception {}

  @Test
  public void docRefSet2() throws Exception {}

  @Test
  public void docRefSet3() throws Exception {}

  public void docRefSet4() throws Exception {}

  @Test
  public void docRefUpdate() throws Exception {}

  @Test
  public void docRefUpdate2() throws Exception {}

  @Test
  public void docRefUpdate3() throws Exception {}

  @Test
  public void docRefUpdate4() throws Exception {}

  @Test
  public void docRefUpdate5() throws Exception {}

  @Test
  public void docRefUpdate6() throws Exception {}

  @Test
  public void docRefDelete() throws Exception {}

  @Test
  public void docRefDelete2() throws Exception {}

  @Test
  public void docRefGet() throws Exception {}

  @Test
  public void docRefGet2() throws Exception {}

  @Test
  public void docListCollections() throws Exception {}

  @Test
  public void getAll() throws Exception {}

  @Test
  public void queryGet() throws Exception {}

  @Test
  public void transaction() throws Exception {}

  @Test
  public void transactionRollback() throws Exception {}

  @Test
  public void writeBatch() throws Exception {}
}
