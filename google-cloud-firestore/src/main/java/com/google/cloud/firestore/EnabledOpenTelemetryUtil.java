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
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.opentelemetry.trace.TraceExporter;
import com.google.common.base.Throwables;
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
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
    private String spanName;

    public Span(io.opentelemetry.api.trace.Span span, String spanName) {
      this.span = span;
      this.spanName = spanName;
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
    public <T> void endAtFuture(ApiFuture<T> futureValue) {
      Context asyncContext = Context.current();
      ApiFutures.addCallback(
          futureValue,
          new ApiFutureCallback<T>() {
            @Override
            public void onFailure(Throwable t) {
              try (io.opentelemetry.context.Scope scope = asyncContext.makeCurrent()) {
                span.addEvent(spanName + " failed.");
                end(t);
              }
            }

            @Override
            public void onSuccess(T result) {
              try (io.opentelemetry.context.Scope scope = asyncContext.makeCurrent()) {
                span.addEvent(spanName + " succeeded.");
                end();
              }
            }
          });
    }

    /** Adds the given event to this span. */
    @Override
    public OpenTelemetryUtil.Span addEvent(String name) {
      span.addEvent(name);
      return this;
    }

    @Override
    public OpenTelemetryUtil.Span addEvent(String name, Attributes attributes) {
      span.addEvent(name, attributes);
      return this;
    }

    @Override
    public OpenTelemetryUtil.Span setAttribute(String key, int value) {
      span.setAttribute(key, value);
      return this;
    }

    @Override
    public OpenTelemetryUtil.Span setAttribute(String key, String value) {
      span.setAttribute(key, value);
      return this;
    }

    @Override
    public Scope makeCurrent() {
      return span.makeCurrent();
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
    // The trace sampling rate environment variable can override the sampling rate in options.
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

    return firestoreOptions.getOpenTelemetryOptions().getTraceSamplingRate();
  }

  private void initializeOpenTelemetry() {
    try {
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
              .build();
      Logger.getLogger("Firestore OpenTelemetry")
          .log(
              Level.INFO, "OpenTelemetry SDK was not provided. Creating one in the Firestore SDK.");
      Logger.getLogger("Firestore OpenTelemetry")
          .log(Level.INFO, String.format("Trace sampling rate = %f", getTraceSamplingRate()));
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
    span.setAllAttributes(
        Attributes.builder()
            .put(ATTRIBUTE_SERVICE_PREFIX + "settings.databaseId", firestoreOptions.getDatabaseId())
            .put(ATTRIBUTE_SERVICE_PREFIX + "settings.hos5", firestoreOptions.getHost())
            .put(ATTRIBUTE_SERVICE_PREFIX + "settings.databaseId", firestoreOptions.getDatabaseId())
            .put(ATTRIBUTE_SERVICE_PREFIX + "settings.host", firestoreOptions.getHost())
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.channel.transportName",
                firestoreOptions.getTransportChannelProvider().getTransportName())
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.channel.needsCredentials",
                String.valueOf(firestoreOptions.getTransportChannelProvider().needsCredentials()))
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.channel.needsEndpoint",
                String.valueOf(firestoreOptions.getTransportChannelProvider().needsEndpoint()))
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.channel.needsHeaders",
                String.valueOf(firestoreOptions.getTransportChannelProvider().needsHeaders()))
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.channel.shouldAutoClose",
                String.valueOf(firestoreOptions.getTransportChannelProvider().shouldAutoClose()))
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.credentials.authenticationType",
                firestoreOptions.getCredentials().getAuthenticationType())
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.retrySettings.initialRetryDelay",
                firestoreOptions.getRetrySettings().getInitialRetryDelay().toString())
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.retrySettings.maxRetryDelay",
                firestoreOptions.getRetrySettings().getMaxRetryDelay().toString())
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.retrySettings.retryDelayMultiplier",
                String.valueOf(firestoreOptions.getRetrySettings().getRetryDelayMultiplier()))
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.retrySettings.maxAttempts",
                String.valueOf(firestoreOptions.getRetrySettings().getMaxAttempts()))
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.retrySettings.initialRpcTimeout",
                firestoreOptions.getRetrySettings().getInitialRpcTimeout().toString())
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.retrySettings.maxRpcTimeout",
                firestoreOptions.getRetrySettings().getMaxRpcTimeout().toString())
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.retrySettings.rpcTimeoutMultiplier",
                String.valueOf(firestoreOptions.getRetrySettings().getRpcTimeoutMultiplier()))
            .put(
                ATTRIBUTE_SERVICE_PREFIX + "settings.retrySettings.totalTimeout",
                firestoreOptions.getRetrySettings().getTotalTimeout().toString())
            .build());
  }

  @Override
  @Nullable
  public ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> getChannelConfigurator() {
    return new OpenTelemetryGrpcChannelConfigurator();
  }

  /** Starts a new span with the given name, sets it as the current span, and returns it. */
  @Override
  @Nullable
  public Span startSpan(String spanName) {
    io.opentelemetry.api.trace.Span span =
        getTracer().spanBuilder(spanName).setSpanKind(SpanKind.CLIENT).startSpan();
    this.addSettingsAttributesToCurrentSpan();
    return new Span(span, spanName);
  }

  @Nullable
  @Override
  public OpenTelemetryUtil.Span startSpan(String spanName, Context parent) {
    io.opentelemetry.api.trace.Span span =
        getTracer()
            .spanBuilder(spanName)
            .setSpanKind(SpanKind.CLIENT)
            .setParent(parent)
            .startSpan();
    return new Span(span, spanName);
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
    return new Span(io.opentelemetry.api.trace.Span.current(), "");
  }

  @Override
  public void close() {
    openTelemetrySdk.close();
  }
}
