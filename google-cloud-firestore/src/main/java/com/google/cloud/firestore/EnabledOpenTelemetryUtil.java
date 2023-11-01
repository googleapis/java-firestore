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

package com.google.cloud.firestore;

import static io.opentelemetry.semconv.resource.attributes.ResourceAttributes.SERVICE_NAME;

import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.opentelemetry.trace.TraceExporter;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.instrumentation.grpc.v1_6.GrpcTelemetry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public class EnabledOpenTelemetryUtil implements OpenTelemetryUtil {
  private static final String SERVICE = "Firestore";
  private static final String LIBRARY_NAME = "com.google.cloud.firestore";
  static final String SPAN_NAME_GETDOCUMENT = "CloudFirestoreOperation.GetDocument";
  static final String SPAN_NAME_CREATEDOCUMENT = "CloudFirestoreOperation.CreateDocument";
  static final String SPAN_NAME_UPDATEDOCUMENT = "CloudFirestoreOperation.UpdateDocument";
  static final String SPAN_NAME_DELETEDOCUMENT = "CloudFirestoreOperation.DeleteDocument";
  static final String SPAN_NAME_LISTCOLLECTIONIDS = "CloudFirestoreOperation.ListCollectionIds";
  static final String SPAN_NAME_LISTDOCUMENTS = "CloudFirestoreOperation.ListDocuments";
  static final String SPAN_NAME_BEGINTRANSACTION = "CloudFirestoreOperation.BeginTransaction";
  static final String SPAN_NAME_COMMIT = "CloudFirestoreOperation.Commit";
  static final String SPAN_NAME_ROLLBACK = "CloudFirestoreOperation.Rollback";
  static final String SPAN_NAME_RUNQUERY = "CloudFirestoreOperation.RunQuery";
  static final String SPAN_NAME_PARTITIONQUERY = "CloudFirestoreOperation.partitionQuery";
  static final String SPAN_NAME_LISTEN = "CloudFirestoreOperation.Listen";
  static final String SPAN_NAME_BATCHGETDOCUMENTS = "CloudFirestoreOperation.BatchGetDocuments";
  static final String SPAN_NAME_BATCHWRITE = "CloudFirestoreOperation.BatchWrite";
  static final String SPAN_NAME_WRITE = "CloudFirestoreOperation.Write";

  @Nullable private OpenTelemetrySdk openTelemetrySdk;
  private FirestoreOptions firestoreOptions;

  // The gRPC channel configurator that intercepts gRPC calls for tracing purposes.
  public static class OpenTelemetryGrpcChannelConfigurator
      implements ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> {
    @Override
    public ManagedChannelBuilder apply(ManagedChannelBuilder managedChannelBuilder) {
      GrpcTelemetry grpcTelemetry = GrpcTelemetry.create(GlobalOpenTelemetry.get());
      return managedChannelBuilder.intercept(grpcTelemetry.newClientInterceptor());
    }
  }

  class Span implements OpenTelemetryUtil.Span {
    private io.opentelemetry.api.trace.Span span;

    public Span(@Nullable io.opentelemetry.api.trace.Span span) {
      this.span = span;
    }

    /** Ends this span. */
    @Override
    public void end() {
      span.end();
    }

    /** Ends this span in an error. */
    @Override
    public void end(Throwable error) {
      span.setStatus(StatusCode.ERROR, error.getMessage());
      span.recordException(
          error,
          Attributes.builder()
              .put("exception.message", error.getMessage())
              .put("exception.type", error.getClass().getName())
              .put("exception.stacktrace", Throwables.getStackTraceAsString(error))
              .build());
      span.end();
    }

    /**
     * If an operation ends in the future, its relevant span should end _after_ the future has been
     * completed. This method "appends" the span completion code at the completion of the given
     * future. In order for telemetry info to be recorded, the future returned by this method should
     * be completed.
     */
    @Override
    public <T> ApiFuture<T> endAtFuture(ApiFuture<T> futureValue) {
      return ApiFutures.transform(
          futureValue,
          value -> {
            span.end();
            return value;
          },
          MoreExecutors.directExecutor());
    }

    /** Adds the given event to this span. */
    @Override
    public OpenTelemetryUtil.Span addEvent(String name) {
      span.addEvent(name);
      return this;
    }
  }

  public EnabledOpenTelemetryUtil(FirestoreOptions firestoreOptions) {
    this.firestoreOptions = firestoreOptions;
    this.openTelemetrySdk = firestoreOptions.getOpenTelemetryOptions().getSdk();

    // It is possible that the user of the SDK does not provide an OpenTelemetrySdk instance to be
    // used.
    // In such cases, we'll create an instance, configure it, and use it.
    if (this.openTelemetrySdk == null) {
      initializeOpenTelemetry();
      if (this.openTelemetrySdk == null) {
        throw new RuntimeException("Error: unable to initialize OpenTelemetry.");
      }
    }
  }

  public double getTraceSamplingRate() {
    Double rate = firestoreOptions.getOpenTelemetryOptions().getTraceSamplingRate();
    if (rate != null) {
      return rate;
    }

    // If the public API for setting the trace sampling rate was not called,
    // use the default sampling rate. The default sampling rate can be modified
    // by an environment variable.
    String traceSamplingEnvVar = System.getenv(OPEN_TELEMETRY_TRACE_SAMPLING_RATE_ENV_VAR_NAME);
    if (traceSamplingEnvVar != null) {
      try {
        return Double.parseDouble(traceSamplingEnvVar);
      } catch (NumberFormatException error) {
        Logger.getLogger(OpenTelemetryUtil.class.getName())
            .log(
                Level.WARNING,
                String.format(
                    "Ignoring the %s environment variable as its value (%s) is not a valid number format.",
                    OPEN_TELEMETRY_TRACE_SAMPLING_RATE_ENV_VAR_NAME, traceSamplingEnvVar));
      }
    }

    return DEFAULT_TRACE_SAMPLING_RATE;
  }

  private void initializeOpenTelemetry() {
    try {
      System.out.println("Initializing GlobalOpenTelemetry inside the SDK...");
      System.out.println(String.format("Trace sampling rate = %f", getTraceSamplingRate()));

      // Include required service.name resource attribute on all spans and metrics
      Resource resource =
          Resource.getDefault().merge(Resource.builder().put(SERVICE_NAME, SERVICE).build());
      SpanExporter gcpTraceExporter = TraceExporter.createWithDefaultConfiguration();
      SpanProcessor gcpSpanProcessor = SimpleSpanProcessor.create(gcpTraceExporter);
      LoggingSpanExporter loggingSpanExporter = LoggingSpanExporter.create();
      SpanProcessor loggingSpanProcessor = SimpleSpanProcessor.create(loggingSpanExporter);

      this.openTelemetrySdk =
          OpenTelemetrySdk.builder()
              .setTracerProvider(
                  SdkTracerProvider.builder()
                      .setResource(resource)
                      .setSampler(Sampler.traceIdRatioBased(getTraceSamplingRate()))
                      .addSpanProcessor(gcpSpanProcessor)
                      .addSpanProcessor(loggingSpanProcessor)
                      .build())
              .buildAndRegisterGlobal();
    } catch (Exception e) {
      // During parallel testing, the OpenTelemetry SDK may get initialized multiple times which is
      // not allowed.
      Logger.getLogger("Firestore OpenTelemetry")
          .log(Level.FINE, "GlobalOpenTelemetry has already been configured.");
    }
  }

  /** Applies the current Firestore instance settings as attributes to the current Span */
  private void addSettingsAttributesToCurrentSpan() {
    io.opentelemetry.api.trace.Span span = io.opentelemetry.api.trace.Span.current();
    span.setAttribute("settings.databaseId", firestoreOptions.getDatabaseId());
    span.setAttribute("settings.host", firestoreOptions.getHost());
    span.setAttribute(
        "settings.channel.transportName",
        firestoreOptions.getTransportChannelProvider().getTransportName());
    span.setAttribute(
        "settings.channel.needsCredentials",
        String.valueOf(firestoreOptions.getTransportChannelProvider().needsCredentials()));
    span.setAttribute(
        "settings.channel.needsEndpoint",
        String.valueOf(firestoreOptions.getTransportChannelProvider().needsEndpoint()));
    span.setAttribute(
        "settings.channel.needsHeaders",
        String.valueOf(firestoreOptions.getTransportChannelProvider().needsHeaders()));
    span.setAttribute(
        "settings.channel.shouldAutoClose",
        String.valueOf(firestoreOptions.getTransportChannelProvider().shouldAutoClose()));
    span.setAttribute(
        "settings.credentials.authenticationType",
        firestoreOptions.getCredentials().getAuthenticationType());
    span.setAttribute(
        "settings.retrySettings.initialRetryDelay",
        firestoreOptions.getRetrySettings().getInitialRetryDelay().toString());
    span.setAttribute(
        "settings.retrySettings.maxRetryDelay",
        firestoreOptions.getRetrySettings().getMaxRetryDelay().toString());
    span.setAttribute(
        "settings.retrySettings.retryDelayMultiplier",
        String.valueOf(firestoreOptions.getRetrySettings().getRetryDelayMultiplier()));
    span.setAttribute(
        "settings.retrySettings.maxAttempts",
        String.valueOf(firestoreOptions.getRetrySettings().getMaxAttempts()));
    span.setAttribute(
        "settings.retrySettings.initialRpcTimeout",
        firestoreOptions.getRetrySettings().getInitialRpcTimeout().toString());
    span.setAttribute(
        "settings.retrySettings.maxRpcTimeout",
        firestoreOptions.getRetrySettings().getMaxRpcTimeout().toString());
    span.setAttribute(
        "settings.retrySettings.rpcTimeoutMultiplier",
        String.valueOf(firestoreOptions.getRetrySettings().getRpcTimeoutMultiplier()));
    span.setAttribute(
        "settings.retrySettings.totalTimeout",
        firestoreOptions.getRetrySettings().getTotalTimeout().toString());
  }

  @Override
  @Nullable
  public ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> getChannelConfigurator() {
    return new OpenTelemetryGrpcChannelConfigurator();
  }

  /** Starts a new span with the given name, sets it as the current span, and returns it. */
  @Override
  @Nullable
  public Span startSpan(String spanName, boolean addSettingsAttributes) {
    io.opentelemetry.api.trace.Span span = getTracer().spanBuilder(spanName).startSpan();
    span.makeCurrent();
    if (addSettingsAttributes) {
      this.addSettingsAttributesToCurrentSpan();
    }
    return new Span(span);
  }

  /** Returns the OpenTelemetry tracer if enabled, and {@code null} otherwise. */
  @Override
  @Nullable
  public Tracer getTracer() {
    return openTelemetrySdk.getTracer(LIBRARY_NAME);
  }

  /** Returns the current span. */
  @Override
  @Nullable
  public OpenTelemetryUtil.Span currentSpan() {
    return new Span(io.opentelemetry.api.trace.Span.current());
  }

  @Override
  public void close() {
    openTelemetrySdk.close();
  }
}
