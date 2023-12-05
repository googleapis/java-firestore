/*
 * Copyright 2023 Google LLC
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

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOpenTelemetryOptions;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.Nullable;

import static com.google.cloud.firestore.telemetry.TraceUtil.*;
import static io.opentelemetry.semconv.resource.attributes.ResourceAttributes.SERVICE_NAME;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TracingTest {
    private static final Logger logger = Logger.getLogger(com.google.cloud.firestore.it.ITBaseTest.class.getName());

    private static final String SERVICE = "google.firestore.v1.Firestore/";
    private static final String BATCH_GET_DOCUMENTS_RPC_NAME = "BatchGetDocuments";
    private static final String COMMIT_RPC_NAME = "Commit";

    // We use an InMemorySpanExporter for testing which keeps all generated trace spans
    // in memory so that we can check their correctness.
    protected static final InMemorySpanExporter inMemorySpanExporter = InMemorySpanExporter.create();

    protected Firestore firestore;

    HashMap<String, String> spanNameToSpanId;
    HashMap<String, String> spanIdToParentSpanId;
    HashMap<String, SpanData> spanNameToSpanData;

    static OpenTelemetrySdk openTelemetrySdkWithInMemorySpanExporter = getOpenTelemetrySdkWithInMemorySpanExporter();

    @Rule
    public TestName testName = new TestName();

    static private OpenTelemetrySdk getOpenTelemetrySdkWithInMemorySpanExporter() {
        Resource resource = Resource.getDefault().merge(Resource.builder().put(SERVICE_NAME, "Sparky").build());
        SpanProcessor inMemorySpanProcessor = SimpleSpanProcessor.create(inMemorySpanExporter);
        return OpenTelemetrySdk.builder()
                .setTracerProvider(SdkTracerProvider.builder()
                        .setResource(resource)
                        .addSpanProcessor(inMemorySpanProcessor)
                        .build())
                .buildAndRegisterGlobal();
    }

    @Before
    public void before() {
        // Clean up existing maps.
        spanNameToSpanId = new HashMap<>();
        spanIdToParentSpanId = new HashMap<>();
        spanNameToSpanData = new HashMap<>();

        // Create Firestore with an in-memory span exporter.
        FirestoreOptions.Builder optionsBuilder = FirestoreOptions.newBuilder().setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions
                        .newBuilder()
                        .setTelemetryCollectionEnabled(true)
                        .setOpenTelemetrySdk(openTelemetrySdkWithInMemorySpanExporter)
                        .build()
        );
        String namedDb = System.getProperty("FIRESTORE_NAMED_DATABASE");
        if (namedDb != null) {
            logger.log(Level.INFO, "Integration test using named database " + namedDb);
            optionsBuilder = optionsBuilder.setDatabaseId(namedDb);
        } else {
            logger.log(Level.INFO, "Integration test using default database.");
        }
        firestore = optionsBuilder.build().getService();
    }

    @After
    public void after() throws Exception {
        Preconditions.checkNotNull(
                firestore,
                "Error instantiating Firestore. Check that the service account credentials were properly set.");
        firestore.close();
        firestore = null;
        inMemorySpanExporter.reset();
    }

    void waitForTracesToComplete() throws Exception {
        // This forces the tracer to flush any remaining traces.
        // We need to do this before we can check spans for correctness.
        // Note that performing `OpenTelemetryUtil.shutdown()` is not enough. We need to call `Firestore.close()`
        // because that will also close the gRPC channel and hence force the gRPC instrumentation library to flush
        // their spans.
        firestore.close();
    }

    void buildSpanMaps(List<SpanData> spans) {
        for (SpanData spanData : spans) {
            spanNameToSpanData.put(spanData.getName(), spanData);
            spanNameToSpanId.put(spanData.getName(), spanData.getSpanId());
            spanIdToParentSpanId.put(spanData.getSpanId(), spanData.getParentSpanId());
        }
        printSpans();
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

    void assertSameTrace(SpanData... spans) {
        if(spans.length > 1) {
            String traceId = spans[0].getTraceId();
            for(SpanData spanData : spans) {
                assertEquals(traceId, spanData.getTraceId());
            }
        }
    }

    // Helper to see the spans in standard output while developing tests
    void printSpans() {
        for(SpanData spanData : spanNameToSpanData.values()) {
            System.out.printf("SPAN ID:%s, ParentID:%s, KIND:%s, TRACE ID:%s, NAME:%s%n",
              spanData.getSpanId(),
              spanData.getParentSpanId(),
              spanData.getKind(),
              spanData.getTraceId(),
              spanData.getName()
            );
        }
    }

  @Test
  public void testDocRefGetSpans() throws Exception {
      firestore.collection("col").document("doc0").get().get();
      waitForTracesToComplete();
      List<SpanData> spans = inMemorySpanExporter.getFinishedSpanItems();
      buildSpanMaps(spans);
      assertEquals(2, spans.size());
      SpanData getSpan = getSpanByName(SPAN_NAME_DOC_REF_GET);
      SpanData grpcSpan = getGrpcSpanByName(BATCH_GET_DOCUMENTS_RPC_NAME);
      assertNotNull(getSpan);
      assertNotNull(grpcSpan);
      assertEquals(grpcSpan.getParentSpanId(), getSpan.getSpanId());
      assertSameTrace(getSpan, grpcSpan);
  }

    @Test
    public void testDocRefCreateSpans() throws Exception {
        firestore.collection("col").document().create(Collections.singletonMap("foo", "bar")).get();
        waitForTracesToComplete();
        List<SpanData> spans = inMemorySpanExporter.getFinishedSpanItems();
        buildSpanMaps(spans);
        assertEquals(3, spans.size());
        SpanData createSpan = getSpanByName(SPAN_NAME_DOC_REF_CREATE);
        SpanData commitSpan = getSpanByName(SPAN_NAME_BATCH_COMMIT);
        SpanData grpcSpan = getGrpcSpanByName(COMMIT_RPC_NAME);
        assertNotNull(createSpan);
        assertNotNull(commitSpan);
        assertNotNull(grpcSpan);
        assertEquals(grpcSpan.getParentSpanId(), commitSpan.getSpanId());
        assertEquals(commitSpan.getParentSpanId(), createSpan.getSpanId());
        assertSameTrace(createSpan, commitSpan, grpcSpan);
    }

    @Test
    public void testDocRefSetSpans() throws Exception {
        firestore.collection("col").document("foo").set(Collections.singletonMap("foo", "bar")).get();
        waitForTracesToComplete();
        List<SpanData> spans = inMemorySpanExporter.getFinishedSpanItems();
        buildSpanMaps(spans);
        assertEquals(3, spans.size());
        SpanData setSpan = getSpanByName(SPAN_NAME_DOC_REF_SET);
        SpanData commitSpan = getSpanByName(SPAN_NAME_BATCH_COMMIT);
        SpanData grpcSpan = getGrpcSpanByName(COMMIT_RPC_NAME);
        assertNotNull(setSpan);
        assertNotNull(commitSpan);
        assertNotNull(grpcSpan);
        assertEquals(grpcSpan.getParentSpanId(), commitSpan.getSpanId());
        assertEquals(commitSpan.getParentSpanId(), setSpan.getSpanId());
        assertSameTrace(setSpan, commitSpan, grpcSpan);
    }

    @Test
    public void testDocRefUpdateSpans() throws Exception {
        firestore.collection("col").document("foo").update(Collections.singletonMap("foo", "bar")).get();
        waitForTracesToComplete();
        List<SpanData> spans = inMemorySpanExporter.getFinishedSpanItems();
        buildSpanMaps(spans);
        assertEquals(3, spans.size());
        SpanData updateSpan = getSpanByName(SPAN_NAME_DOC_REF_UPDATE);
        SpanData commitSpan = getSpanByName(SPAN_NAME_BATCH_COMMIT);
        SpanData grpcSpan = getGrpcSpanByName(COMMIT_RPC_NAME);
        assertNotNull(updateSpan);
        assertNotNull(commitSpan);
        assertNotNull(grpcSpan);
        assertEquals(grpcSpan.getParentSpanId(), commitSpan.getSpanId());
        assertEquals(commitSpan.getParentSpanId(), updateSpan.getSpanId());
        assertSameTrace(updateSpan, commitSpan, grpcSpan);
    }

    @Test
    public void testDocRefDeleteSpans() throws Exception {
        firestore.collection("col").document("doc0").delete().get();
        waitForTracesToComplete();
        List<SpanData> spans = inMemorySpanExporter.getFinishedSpanItems();
        buildSpanMaps(spans);
        assertEquals(3, spans.size());
        SpanData deleteSpan = getSpanByName(SPAN_NAME_DOC_REF_DELETE);
        SpanData commitSpan = getSpanByName(SPAN_NAME_BATCH_COMMIT);
        SpanData grpcSpan = getGrpcSpanByName(COMMIT_RPC_NAME);
        assertNotNull(deleteSpan);
        assertNotNull(commitSpan);
        assertNotNull(grpcSpan);
        assertEquals(grpcSpan.getParentSpanId(), commitSpan.getSpanId());
        assertEquals(commitSpan.getParentSpanId(), deleteSpan.getSpanId());
        assertSameTrace(deleteSpan, commitSpan, grpcSpan);
    }
}
