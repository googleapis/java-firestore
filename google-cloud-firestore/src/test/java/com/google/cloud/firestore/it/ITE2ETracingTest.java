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

import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_BATCH_COMMIT;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_BULK_WRITER_COMMIT;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_COL_REF_LIST_DOCUMENTS;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_DOC_REF_CREATE;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_DOC_REF_DELETE;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_DOC_REF_GET;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_DOC_REF_LIST_COLLECTIONS;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_DOC_REF_SET;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_DOC_REF_UPDATE;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_PARTITION_QUERY;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_QUERY_GET;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_TRANSACTION_BEGIN;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_TRANSACTION_COMMIT;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_TRANSACTION_GET_AGGREGATION_QUERY;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_TRANSACTION_GET_DOCUMENTS;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_TRANSACTION_GET_QUERY;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_TRANSACTION_ROLLBACK;
import static com.google.cloud.firestore.telemetry.TraceUtil.SPAN_NAME_TRANSACTION_RUN;
import static io.opentelemetry.semconv.resource.attributes.ResourceAttributes.SERVICE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.firestore.BulkWriter;
import com.google.cloud.firestore.BulkWriterOptions;
import com.google.cloud.firestore.CollectionGroup;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldMask;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOpenTelemetryOptions;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Precondition;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.it.ITTracingTest.Pojo;
import com.google.cloud.opentelemetry.trace.TraceConfiguration;
import com.google.cloud.opentelemetry.trace.TraceExporter;
import com.google.cloud.trace.v1.TraceServiceClient;
import com.google.common.base.Preconditions;
import com.google.devtools.cloudtrace.v1.Trace;
import com.google.devtools.cloudtrace.v1.TraceSpan;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
public class ITE2ETracingTest extends ITBaseTest {

  // Helper class to track call-stacks in a trace
  protected class TraceContainer {

    // Maps Span ID to TraceSpan
    private Map<Long, TraceSpan> idSpanMap;

    // Maps Parent Span ID to a list of Child SpanIDs, useful for top-down traversal
    private Map<Long, List<Long>> parentChildIdMap;

    // Tracks the Root Span ID
    private long rootId;

    public TraceContainer(String rootSpanName, Trace trace) {
      idSpanMap = new TreeMap<Long, TraceSpan>();
      parentChildIdMap = new TreeMap<Long, List<Long>>();
      for (TraceSpan span : trace.getSpansList()) {
        long spanId = span.getSpanId();
        idSpanMap.put(spanId, span);
        if (rootSpanName.equals(span.getName())) {
          rootId = span.getSpanId();
        }

        // Add self as a child of the parent span
        if (!parentChildIdMap.containsKey(span.getParentSpanId())) {
          parentChildIdMap.put(span.getParentSpanId(), new ArrayList<Long>());
        }
        parentChildIdMap.get(span.getParentSpanId()).add(spanId);
      }
    }

    String spanName(long spanId) {
      return idSpanMap.get(spanId).getName();
    }

    List<Long> childSpans(long spanId) {
      return parentChildIdMap.get(spanId);
    }

    // This method only works for matching call stacks with traces which have children of distinct
    // type at all
    // levels. This is good enough as the intention is to validate if the e2e path is WAI - the
    // intention is not to validate Cloud Trace's correctness w.r.t. durability of all kinds of
    // traces.
    boolean containsCallStack(String... callStack) throws RuntimeException {
      ArrayList<String> expectedCallStack = new ArrayList<String>();
      for (String call : callStack) {
        expectedCallStack.add(call);
      }
      if (expectedCallStack.isEmpty()) {
        throw new RuntimeException("Input callStack is empty");
      }
      return dfsContainsCallStack(rootId, expectedCallStack);
    }

    // Depth-first check for call stack in the trace
    private boolean dfsContainsCallStack(long spanId, List<String> expectedCallStack) {
      logger.info(
          "span="
              + spanName(spanId)
              + ", expectedCallStack[0]="
              + (expectedCallStack.isEmpty() ? "null" : expectedCallStack.get(0)));
      if (!expectedCallStack.isEmpty() && spanName(spanId).equals(expectedCallStack.get(0))) {
        if (childSpans(spanId) == null) {
          logger.info("No more chilren for " + spanName(spanId));
          return true;
        } else {
          for (Long childSpan : childSpans(spanId)) {
            int callStackListSize = expectedCallStack.size();
            logger.info(
                "childSpan="
                    + spanName(childSpan)
                    + ", expectedCallStackSize="
                    + callStackListSize);
            if (dfsContainsCallStack(
                childSpan,
                expectedCallStack.subList(
                    /*fromIndexInclusive=*/ 1, /*toIndexExclusive*/ callStackListSize))) {
              return true;
            }
          }
        }
      } else {
        if (!expectedCallStack.isEmpty()) {
          logger.warning(spanName(spanId) + " didn't match " + expectedCallStack.get(0));
        }
      }
      return false;
    }
  }

  private static final Logger logger = Logger.getLogger(ITBaseTest.class.getName());

  private static final String SERVICE = "google.firestore.v1.Firestore/";

  private static final String BATCH_GET_DOCUMENTS_RPC_NAME = "BatchGetDocuments";

  private static final String BATCH_WRITE_RPC_NAME = "BatchWrite";

  private static final String BEGIN_TRANSACTION_RPC_NAME = "BeginTransaction";

  private static final String COMMIT_RPC_NAME = "Commit";

  private static final String LIST_COLLECTIONS_RPC_NAME = "ListCollectionIds";

  private static final String LIST_DOCUMENTS_RPC_NAME = "ListDocuments";

  private static final String ROLLBACK_RPC_NAME = "Rollback";

  private static final String RUN_AGGREGATION_QUERY_RPC_NAME = "RunAggregationQuery";

  private static final String RUN_QUERY_RPC_NAME = "RunQuery";

  private static final int NUM_TRACE_ID_BYTES = 32;

  private static final int NUM_SPAN_ID_BYTES = 16;

  private static final int GET_TRACE_RETRY_COUNT = 15;

  private static final int GET_TRACE_RETRY_BACKOFF_MILLIS = 1000;

  private static final int TRACE_FORCE_FLUSH_MILLIS = 3000;

  private static final int TRACE_PROVIDER_SHUTDOWN_MILLIS = 1000;

  // Random int generator for trace ID and span ID
  private static Random random;

  private static TraceExporter traceExporter;

  // Required for reading back traces from Cloud Trace for validation
  private static TraceServiceClient traceClient_v1;

  // Custom SpanContext for each test, required for TraceID injection
  private static SpanContext customSpanContext;

  // Trace read back from Cloud Trace using traceClient_v1 for verification
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

    random = new Random();
  }

  @Before
  public void before() throws Exception {
    // Initialize the Firestore DB w/ the OTel SDK. Ideally we'd do this is the @BeforeAll method
    // but because gRPC traces need to be deterministically force-flushed, firestore.shutdown()
    // must be called in @After for each test.
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
    rootSpanName =
        String.format("%s%d", this.getClass().getSimpleName(), System.currentTimeMillis());
    tracer =
        firestore.getOptions().getOpenTelemetryOptions().getOpenTelemetry().getTracer(rootSpanName);

    // Get up a new SpanContext (ergo TraceId) for each test
    customSpanContext = getNewSpanContext();
    assertNotNull(customSpanContext);
    assertNull(retrievedTrace);
  }

  @After
  public void after() throws Exception {
    firestore.shutdown();
    rootSpanName = null;
    tracer = null;
    retrievedTrace = null;
    customSpanContext = null;
  }

  @AfterClass
  public static void teardown() throws Exception {
    traceClient_v1.close();
    CompletableResultCode completableResultCode =
        openTelemetrySdk.getSdkTracerProvider().shutdown();
    completableResultCode.join(TRACE_PROVIDER_SHUTDOWN_MILLIS, TimeUnit.MILLISECONDS);
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

  // Generates a new SpanContext w/ random traceId,spanId
  protected SpanContext getNewSpanContext() {
    String traceId = generateNewTraceId();
    String spanId = generateNewSpanId();
    logger.info("traceId=" + traceId + ", spanId=" + spanId);

    return SpanContext.create(traceId, spanId, TraceFlags.getSampled(), TraceState.getDefault());
  }

  protected Span getNewRootSpanWithContext() {
    // Execute the DB operation in the context of the custom root span.
    return tracer
        .spanBuilder(rootSpanName)
        .setParent(Context.root().with(Span.wrap(customSpanContext)))
        .startSpan();
  }

  protected String grpcSpanName(String rpcName) {
    return "Sent." + SERVICE + rpcName;
  }

  protected void waitForTracesToComplete() throws Exception {
    logger.info("Flushing traces...");
    CompletableResultCode completableResultCode =
        openTelemetrySdk.getSdkTracerProvider().forceFlush();
    completableResultCode.join(TRACE_FORCE_FLUSH_MILLIS, TimeUnit.MILLISECONDS);
  }

  // Validates `retrievedTrace`. Cloud Trace indexes traces w/ eventual consistency, even when
  // indexing traceId, therefore the
  // test must retry a few times before the complete trace is available.
  protected void fetchAndValidateTransactionTrace(
      String traceId, int numExpectedSpans, String... callStack) throws InterruptedException {
    // Large enough count to accommodate eventually consistent Cloud Trace backend
    int numRetries = GET_TRACE_RETRY_COUNT;
    do {
      try {
        // Fetch traces
        retrievedTrace = traceClient_v1.getTrace(projectId, traceId);
        assertEquals(traceId, retrievedTrace.getTraceId());

        ArrayList<String> expectedCallStack = new ArrayList<String>(Arrays.asList(callStack));

        // numExpectedSpans should account for rootSpanName (which is not passed in callStack)
        expectedCallStack.add(0, rootSpanName);
        numExpectedSpans++;

        System.out.println(
            "expectedSpanCount="
                + numExpectedSpans
                + ", retrievedSpanCount="
                + retrievedTrace.getSpansCount());
        // *Maybe* the full trace was returned
        if (retrievedTrace.getSpansCount() == numExpectedSpans) {
          System.out.println("Checking if TraceContainer containsCallStack");
          TraceContainer traceContainer = new TraceContainer(rootSpanName, retrievedTrace);
          String[] temp = new String[expectedCallStack.size()];
          if (traceContainer.containsCallStack(expectedCallStack.toArray(temp))) {
            return;
          }
          logger.severe("CallStack not found in TraceContainer.");
        } // else the trace may not have been fully committed to Cloud Trace storage
      } catch (NotFoundException notFound) {
        logger.info("Trace not found, retrying in " + GET_TRACE_RETRY_BACKOFF_MILLIS + " ms");
        Thread.sleep(GET_TRACE_RETRY_BACKOFF_MILLIS);
        // getTrace could fail
      } catch (IndexOutOfBoundsException outOfBoundsException) {
        logger.info("Call stack not found in trace. Retrying.");
        Thread.sleep(GET_TRACE_RETRY_BACKOFF_MILLIS);
      }
      // Retrieved trace doesn't have expected number of spans
    } while (numRetries-- > 0);
    throw new RuntimeException(
        "Expected spans: "
            + callStack.toString()
            + ", Actual spans: "
            + (retrievedTrace != null ? retrievedTrace.getSpansList().toString() : "null"));
  }

  // Validates `retrievedTrace`. Cloud Trace indexes traces w/ eventual consistency, even when
  // indexing traceId, therefore the
  // test must retry a few times before the complete trace is available.
  protected void fetchAndValidateTraces(String traceId, String... spanNames)
      throws InterruptedException {
    int numRetries = GET_TRACE_RETRY_COUNT;
    do {
      try {
        // Fetch traces
        retrievedTrace = traceClient_v1.getTrace(projectId, traceId);
        ArrayList<String> spanNameList = new ArrayList<String>(Arrays.asList(spanNames));
        spanNameList.add(0, rootSpanName);
        // Validate trace spans
        assertEquals(traceId, retrievedTrace.getTraceId());
        for (int i = 0; i < spanNameList.size(); ++i) {
          assertEquals(spanNameList.get(i), retrievedTrace.getSpans(i).getName());
        }
        assertEquals(spanNameList.size(), retrievedTrace.getSpansCount());
        return;
      } catch (NotFoundException notFound) {
        logger.info("Trace not found, retrying in " + GET_TRACE_RETRY_BACKOFF_MILLIS + " ms");
        Thread.sleep(GET_TRACE_RETRY_BACKOFF_MILLIS);
      } catch (IndexOutOfBoundsException outOfBoundsException) {
        logger.info(
            "Expected # spans: "
                + (spanNames.length + 1)
                + "Actual # spans: "
                + retrievedTrace.getSpansCount());
        Thread.sleep(GET_TRACE_RETRY_BACKOFF_MILLIS);
      }
    } while (numRetries-- > 0);
    throw new RuntimeException(
        "Expected spans: "
            + spanNames.toString()
            + ", Actual spans: "
            + (retrievedTrace != null ? retrievedTrace.getSpansList().toString() : "null"));
  }

  @Test
  public void traceContainerTest() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").whereEqualTo("foo", "my_non_existent_value").get().get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    Trace traceResp = null;
    int expectedSpanCount = 3;

    int numRetries = GET_TRACE_RETRY_COUNT;
    do {
      try {
        traceResp = traceClient_v1.getTrace(projectId, customSpanContext.getTraceId());
        if (traceResp.getSpansCount() == expectedSpanCount) {
          break;
        }
      } catch (NotFoundException notFoundException) {
        Thread.sleep(GET_TRACE_RETRY_BACKOFF_MILLIS);
        logger.info("Trace not found, retrying in " + GET_TRACE_RETRY_BACKOFF_MILLIS + " ms");
      }
      numRetries--;
    } while (numRetries > 0);

    TraceContainer traceCont = new TraceContainer(rootSpanName, traceResp);

    // Contains exact path
    assertTrue(
        traceCont.containsCallStack(
            rootSpanName, SPAN_NAME_QUERY_GET, grpcSpanName(RUN_QUERY_RPC_NAME)));

    // Top-level mismatch
    assertFalse(traceCont.containsCallStack(SPAN_NAME_QUERY_GET, RUN_QUERY_RPC_NAME));

    // Mid-level match
    assertFalse(traceCont.containsCallStack(rootSpanName, SPAN_NAME_QUERY_GET));

    // Leaf-level mismatch/missing
    assertFalse(
        traceCont.containsCallStack(
            rootSpanName, SPAN_NAME_QUERY_GET, RUN_AGGREGATION_QUERY_RPC_NAME));
  }

  @Test
  // Trace an Aggregation.Get request
  public void aggregateQueryGetTraceTest() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      // Execute the Firestore SDK op
      firestore.collection("col").count().get().get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        "AggregationQuery.Get",
        grpcSpanName(RUN_AGGREGATION_QUERY_RPC_NAME));
  }

  @Test
  public void bulkWriterCommitTraceTest() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      // Execute the Firestore SDK op
      ScheduledExecutorService bulkWriterExecutor = Executors.newSingleThreadScheduledExecutor();
      BulkWriter bulkWriter =
          firestore.bulkWriter(BulkWriterOptions.builder().setExecutor(bulkWriterExecutor).build());
      bulkWriter.set(
          firestore.collection("col").document("foo"),
          Collections.singletonMap("bulk-foo", "bulk-bar"));
      bulkWriter.close();
      bulkWriterExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_BULK_WRITER_COMMIT,
        grpcSpanName(BATCH_WRITE_RPC_NAME));
  }

  @Test
  public void partitionQueryTraceTest() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      CollectionGroup collectionGroup = firestore.collectionGroup("col");
      collectionGroup.getPartitions(3).get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_PARTITION_QUERY,
        grpcSpanName(SPAN_NAME_PARTITION_QUERY));
  }

  @Test
  public void collectionListDocumentsTraceTest() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").listDocuments();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_COL_REF_LIST_DOCUMENTS,
        grpcSpanName(LIST_DOCUMENTS_RPC_NAME));
  }

  @Test
  public void docRefCreateTraceTest() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").document().create(Collections.singletonMap("foo", "bar")).get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_CREATE,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefCreate2TraceTest() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").document().create(new Pojo(1)).get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_CREATE,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefSetTraceTest() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").document("foo").set(Collections.singletonMap("foo", "bar")).get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_SET,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefSet2TraceTest() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore
          .collection("col")
          .document("foo")
          .set(Collections.singletonMap("foo", "bar"), SetOptions.merge())
          .get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_SET,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefSet3TraceTest() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").document("foo").set(new Pojo(1)).get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_SET,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefSet4TraceTest() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").document("foo").set(new Pojo(1), SetOptions.merge()).get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_SET,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefUpdate() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore
          .collection("col")
          .document("foo")
          .update(Collections.singletonMap("foo", "bar"))
          .get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_UPDATE,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefUpdate2() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore
          .collection("col")
          .document("foo")
          .update(Collections.singletonMap("foo", "bar"), Precondition.NONE)
          .get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_UPDATE,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefUpdate3() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").document("foo").update("key", "value", "key2", "value2").get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_UPDATE,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefUpdate4() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore
          .collection("col")
          .document("foo")
          .update(FieldPath.of("key"), "value", FieldPath.of("key2"), "value2")
          .get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_UPDATE,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefUpdate5() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore
          .collection("col")
          .document("foo")
          .update(Precondition.NONE, "key", "value", "key2", "value2")
          .get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_UPDATE,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefUpdate6() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore
          .collection("col")
          .document("foo")
          .update(Precondition.NONE, FieldPath.of("key"), "value", FieldPath.of("key2"), "value2")
          .get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_UPDATE,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefDelete() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").document("doc0").delete().get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_DELETE,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefDelete2() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").document("doc0").delete(Precondition.NONE).get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_DELETE,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void docRefGet() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").document("doc0").get().get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_GET,
        grpcSpanName(BATCH_GET_DOCUMENTS_RPC_NAME));
  }

  @Test
  public void docRefGet2() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").document("doc0").get(FieldMask.of("foo")).get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_GET,
        grpcSpanName(BATCH_GET_DOCUMENTS_RPC_NAME));
  }

  @Test
  public void docListCollections() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").document("doc0").listCollections();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    // Read and validate traces
    fetchAndValidateTraces(
        customSpanContext.getTraceId(),
        SPAN_NAME_DOC_REF_LIST_COLLECTIONS,
        grpcSpanName(LIST_COLLECTIONS_RPC_NAME));
  }

  @Test
  public void getAll() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      DocumentReference docRef0 = firestore.collection("col").document();
      DocumentReference docRef1 = firestore.collection("col").document();
      DocumentReference[] docs = {docRef0, docRef1};
      firestore.getAll(docs).get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    fetchAndValidateTraces(
        customSpanContext.getTraceId(), grpcSpanName(BATCH_GET_DOCUMENTS_RPC_NAME));
  }

  @Test
  public void queryGet() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      firestore.collection("col").whereEqualTo("foo", "my_non_existent_value").get().get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    fetchAndValidateTraces(
        customSpanContext.getTraceId(), SPAN_NAME_QUERY_GET, grpcSpanName(RUN_QUERY_RPC_NAME));
  }

  @Test
  public void transaction() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
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

                // Get multiple documents.
                transaction.getAll(d, d).get();

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
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    fetchAndValidateTransactionTrace(
        customSpanContext.getTraceId(),
        /*numExpectedSpans=*/ 11,
        SPAN_NAME_TRANSACTION_RUN,
        SPAN_NAME_TRANSACTION_BEGIN,
        grpcSpanName(BEGIN_TRANSACTION_RPC_NAME));

    fetchAndValidateTransactionTrace(
        customSpanContext.getTraceId(),
        /*numExpectedSpans=*/ 11,
        SPAN_NAME_TRANSACTION_RUN,
        SPAN_NAME_TRANSACTION_GET_QUERY,
        grpcSpanName(RUN_QUERY_RPC_NAME));

    fetchAndValidateTransactionTrace(
        customSpanContext.getTraceId(),
        /*numExpectedSpans=*/ 11,
        SPAN_NAME_TRANSACTION_RUN,
        SPAN_NAME_TRANSACTION_GET_AGGREGATION_QUERY,
        grpcSpanName(RUN_AGGREGATION_QUERY_RPC_NAME));

    fetchAndValidateTransactionTrace(
        customSpanContext.getTraceId(),
        /*numExpectedSpans=*/ 11,
        SPAN_NAME_TRANSACTION_RUN,
        SPAN_NAME_TRANSACTION_GET_DOCUMENTS,
        grpcSpanName(BATCH_GET_DOCUMENTS_RPC_NAME));

    fetchAndValidateTransactionTrace(
        customSpanContext.getTraceId(),
        /*numExpectedSpans=*/ 11,
        SPAN_NAME_TRANSACTION_RUN,
        SPAN_NAME_TRANSACTION_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }

  @Test
  public void transactionRollback() throws Exception {
    String myErrorMessage = "My error message.";
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
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
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    fetchAndValidateTransactionTrace(
        customSpanContext.getTraceId(),
        /*numExpectedSpans=*/ 5,
        SPAN_NAME_TRANSACTION_RUN,
        SPAN_NAME_TRANSACTION_BEGIN,
        grpcSpanName(BEGIN_TRANSACTION_RPC_NAME));

    fetchAndValidateTransactionTrace(
        customSpanContext.getTraceId(),
        /*numExpectedSpans=*/ 5,
        SPAN_NAME_TRANSACTION_RUN,
        SPAN_NAME_TRANSACTION_ROLLBACK,
        grpcSpanName(ROLLBACK_RPC_NAME));
  }

  @Test
  public void writeBatch() throws Exception {
    // Make sure the test has a new SpanContext (and TraceId for injection)
    assertNotNull(customSpanContext);

    // Inject new trace ID
    Span rootSpan = getNewRootSpanWithContext();
    try (Scope ss = rootSpan.makeCurrent()) {
      WriteBatch batch = firestore.batch();
      DocumentReference docRef = firestore.collection("foo").document();
      batch.create(docRef, Collections.singletonMap("foo", "bar"));
      batch.update(docRef, Collections.singletonMap("foo", "bar"));
      batch.delete(docRef);
      batch.commit().get();
    } finally {
      rootSpan.end();
    }
    waitForTracesToComplete();

    fetchAndValidateTransactionTrace(
        customSpanContext.getTraceId(),
        /*numExpectedSpans=*/ 2,
        SPAN_NAME_BATCH_COMMIT,
        grpcSpanName(COMMIT_RPC_NAME));
  }
}