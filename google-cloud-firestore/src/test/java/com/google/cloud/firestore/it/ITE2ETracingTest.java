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

package com.google.cloud.firestore.it;

import static io.opentelemetry.semconv.resource.attributes.ResourceAttributes.SERVICE_NAME;
import static org.junit.Assert.assertEquals;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOpenTelemetryOptions;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.opentelemetry.trace.TraceConfiguration;
import com.google.cloud.opentelemetry.trace.TraceExporter;
import com.google.cloud.trace.v1.TraceServiceClient;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.devtools.cloudtrace.v1.ListTracesRequest;
import com.google.devtools.cloudtrace.v1.ListTracesRequest.ViewType;
import com.google.devtools.cloudtrace.v1.Trace;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ITE2ETracingTest {

  private static final Logger logger =
      Logger.getLogger(ITBaseTest.class.getName());

  private static String rootSpanName;

  // TODO(jimit) Generalize project ID
  private static final String PROJECT_ID = "jimit-test";

  // We use an InMemorySpanExporter for testing which keeps all generated trace spans
  // in memory so that we can check their correctness.
  protected static InMemorySpanExporter inMemorySpanExporter = InMemorySpanExporter.create();

  protected static TraceExporter traceExporter;

  // Required for reading back traces from Cloud Trace for validation
  protected static TraceServiceClient traceClient_v1;
  // Required to set custom-root span
  protected static OpenTelemetrySdk openTelemetrySdk;

  protected Firestore firestore;

  @Rule
  public TestName testName = new TestName();

  @Before
  public void before() throws Exception {
    // Set up OTel SDK
    Resource resource =
        Resource.getDefault().merge(Resource.builder().put(SERVICE_NAME, "Sparky").build());

    // TODO(jimit) Make it re-usable w/ InMemorySpanExporter
    traceExporter = TraceExporter.createWithConfiguration(
        TraceConfiguration.builder().setProjectId(PROJECT_ID).build());

    openTelemetrySdk = OpenTelemetrySdk.builder()
        .setTracerProvider(
            SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(BatchSpanProcessor.builder(traceExporter).build())
                .setSampler(Sampler.alwaysOn())
                .build()).build();

    traceClient_v1 = TraceServiceClient.create();

    // Initialize the Firestore DB w/ the OTel SDK
    FirestoreOptions.Builder optionsBuilder =
        FirestoreOptions.newBuilder()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setOpenTelemetry(openTelemetrySdk)
                    .setTracingEnabled(true).build());

    String namedDb = System.getProperty("FIRESTORE_NAMED_DATABASE");
    if (namedDb != null) {
      logger.log(Level.INFO, "Integration test using named database " + namedDb);
      optionsBuilder = optionsBuilder.setDatabaseId(namedDb);
    } else {
      logger.log(Level.INFO, "Integration test using default database.");
    }
    firestore = optionsBuilder.build().getService();
    rootSpanName = String.format("%s%d", this.getClass().getSimpleName(),
        System.currentTimeMillis());
  }

  @After
  public void after() throws Exception {
    Preconditions.checkNotNull(
        firestore,
        "Error instantiating Firestore. Check that the service account credentials were properly set.");
    traceClient_v1.close();
    firestore.shutdown();
    inMemorySpanExporter.reset();
  }

  void waitForTracesToComplete() throws Exception {
    CompletableResultCode completableResultCode = openTelemetrySdk.getSdkTracerProvider()
        .shutdown();
    completableResultCode.join(1000, TimeUnit.MILLISECONDS);
    // We need to call `firestore.close()` because that will also close the
    // gRPC channel and hence force the gRPC instrumentation library to flush
    // its spans.
    firestore.close();
  }

  @Test
  public void basicTraceTestWithRootSpan() throws Exception {
    // Build custom rootSpanName=ITE2ETracingTest<currenttimemillis>
    rootSpanName = String.format("%s%d", this.getClass().getSimpleName(),
        System.currentTimeMillis());
    System.out.println("rootSpanName=" + rootSpanName);

    Tracer tracer = firestore.getOptions()
        .getOpenTelemetryOptions()
        .getOpenTelemetry()
        .getTracer("mytest");

    // Add the root span with the custom name
    Span span = tracer.spanBuilder(rootSpanName).startSpan();
    try (Scope ss = span.makeCurrent()) {
      firestore.collection("col").count().get().get();
    } finally {
      span.end();
    }
    //Flush traces
    waitForTracesToComplete();

    // Query the custom rootSpanName
    ListTracesRequest listTracesRequest = ListTracesRequest.newBuilder()
        .setProjectId(PROJECT_ID)
        .setView(ViewType.COMPLETE)
        .setFilter(
            rootSpanName) // This filter returns 0 results. When this line is removed, the query returns non-zero results
        .build();

    // // Read back the traces
    TraceServiceClient.ListTracesPagedResponse listTraceResponse =
        traceClient_v1.listTraces(listTracesRequest);

    List<Trace> traceIdList = Lists.newArrayList(listTraceResponse.iterateAll());
    assertEquals(1, traceIdList.size()); // THIS FAILS
    System.out.println("trace:" + traceIdList.get(0));
  }
}
