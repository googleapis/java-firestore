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

import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.common.base.Throwables;
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.instrumentation.grpc.v1_6.GrpcTelemetry;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnabledTraceUtil implements TraceUtil {
  private final Tracer tracer;
  private final OpenTelemetry openTelemetry;
  private final FirestoreOptions firestoreOptions;

  EnabledTraceUtil(FirestoreOptions firestoreOptions) {
    OpenTelemetry openTelemetry = firestoreOptions.getOpenTelemetryOptions().getOpenTelemetry();

    // If tracing is enabled, but an OpenTelemetry instance is not provided, fall back
    // to using GlobalOpenTelemetry.
    if (openTelemetry == null) {
      openTelemetry = GlobalOpenTelemetry.get();
    }

    this.firestoreOptions = firestoreOptions;
    this.openTelemetry = openTelemetry;
    this.tracer = openTelemetry.getTracer(LIBRARY_NAME);
  }

  public OpenTelemetry getOpenTelemetry() {
    return openTelemetry;
  }

  // The gRPC channel configurator that intercepts gRPC calls for tracing purposes.
  public class OpenTelemetryGrpcChannelConfigurator
      implements ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> {
    @Override
    public ManagedChannelBuilder apply(ManagedChannelBuilder managedChannelBuilder) {
      GrpcTelemetry grpcTelemetry = GrpcTelemetry.create(getOpenTelemetry());
      return managedChannelBuilder.intercept(grpcTelemetry.newClientInterceptor());
    }
  }

  @Override
  @Nullable
  public ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> getChannelConfigurator() {
    return new OpenTelemetryGrpcChannelConfigurator();
  }

  static class Span implements TraceUtil.Span {
    private final io.opentelemetry.api.trace.Span span;
    private final String spanName;

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
      io.opentelemetry.context.Context asyncContext = io.opentelemetry.context.Context.current();
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
    public TraceUtil.Span addEvent(String name) {
      span.addEvent(name);
      return this;
    }

    @Override
    public TraceUtil.Span addEvent(String name, Map<String, Object> attributes) {
      AttributesBuilder attributesBuilder = Attributes.builder();
      attributes.forEach(
          (key, value) -> {
            if (value instanceof Integer) {
              attributesBuilder.put(key, (int) value);
            } else if (value instanceof Long) {
              attributesBuilder.put(key, (long) value);
            } else if (value instanceof Double) {
              attributesBuilder.put(key, (double) value);
            } else if (value instanceof Float) {
              attributesBuilder.put(key, (float) value);
            } else if (value instanceof Boolean) {
              attributesBuilder.put(key, (boolean) value);
            } else if (value instanceof String) {
              attributesBuilder.put(key, (String) value);
            } else {
              // OpenTelemetry APIs do not support any other type. Ignore this attribute.
            }
          });
      span.addEvent(name, attributesBuilder.build());
      return this;
    }

    @Override
    public TraceUtil.Span setAttribute(String key, int value) {
      span.setAttribute(ATTRIBUTE_SERVICE_PREFIX + key, value);
      return this;
    }

    @Override
    public TraceUtil.Span setAttribute(String key, String value) {
      span.setAttribute(ATTRIBUTE_SERVICE_PREFIX + key, value);
      return this;
    }

    @Override
    public Scope makeCurrent() {
      return new Scope(span.makeCurrent());
    }
  }

  static class Scope implements TraceUtil.Scope {
    private final io.opentelemetry.context.Scope scope;

    Scope(io.opentelemetry.context.Scope scope) {
      this.scope = scope;
    }

    @Override
    public void close() {
      scope.close();
    }
  }

  static class Context implements TraceUtil.Context {
    private final io.opentelemetry.context.Context context;

    Context(io.opentelemetry.context.Context context) {
      this.context = context;
    }

    @Override
    public Scope makeCurrent() {
      return new Scope(context.makeCurrent());
    }
  }

  /** Applies the current Firestore instance settings as attributes to the current Span */
  private SpanBuilder addSettingsAttributesToCurrentSpan(SpanBuilder spanBuilder) {
    spanBuilder =
        spanBuilder.setAllAttributes(
            Attributes.builder()
                .put(
                    ATTRIBUTE_SERVICE_PREFIX + "settings.databaseId",
                    firestoreOptions.getDatabaseId())
                .put(ATTRIBUTE_SERVICE_PREFIX + "settings.host", firestoreOptions.getHost())
                .build());

    if (firestoreOptions.getTransportChannelProvider() != null) {
      spanBuilder =
          spanBuilder.setAllAttributes(
              Attributes.builder()
                  .put(
                      ATTRIBUTE_SERVICE_PREFIX + "settings.channel.transportName",
                      firestoreOptions.getTransportChannelProvider().getTransportName())
                  .put(
                      ATTRIBUTE_SERVICE_PREFIX + "settings.channel.needsCredentials",
                      String.valueOf(
                          firestoreOptions.getTransportChannelProvider().needsCredentials()))
                  .put(
                      ATTRIBUTE_SERVICE_PREFIX + "settings.channel.needsEndpoint",
                      String.valueOf(
                          firestoreOptions.getTransportChannelProvider().needsEndpoint()))
                  .put(
                      ATTRIBUTE_SERVICE_PREFIX + "settings.channel.needsHeaders",
                      String.valueOf(firestoreOptions.getTransportChannelProvider().needsHeaders()))
                  .put(
                      ATTRIBUTE_SERVICE_PREFIX + "settings.channel.shouldAutoClose",
                      String.valueOf(
                          firestoreOptions.getTransportChannelProvider().shouldAutoClose()))
                  .build());
    }

    if (firestoreOptions.getCredentials() != null) {
      spanBuilder =
          spanBuilder.setAttribute(
              ATTRIBUTE_SERVICE_PREFIX + "settings.credentials.authenticationType",
              firestoreOptions.getCredentials().getAuthenticationType());
    }

    if (firestoreOptions.getRetrySettings() != null) {
      spanBuilder =
          spanBuilder.setAllAttributes(
              Attributes.builder()
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

    // Add the memory utilization of the client at the time this trace was collected.
    long totalMemory = Runtime.getRuntime().totalMemory();
    long freeMemory = Runtime.getRuntime().freeMemory();
    double memoryUtilization = ((double) (totalMemory - freeMemory)) / totalMemory;
    spanBuilder.setAttribute(
        ATTRIBUTE_SERVICE_PREFIX + "memoryUtilization",
        String.format("%.2f", memoryUtilization * 100) + "%");

    return spanBuilder;
  }

  @Override
  public Span startSpan(String spanName) {
    SpanBuilder spanBuilder = tracer.spanBuilder(spanName).setSpanKind(SpanKind.PRODUCER);
    io.opentelemetry.api.trace.Span span =
        addSettingsAttributesToCurrentSpan(spanBuilder).startSpan();
    return new Span(span, spanName);
  }

  @Override
  public TraceUtil.Span startSpan(String spanName, TraceUtil.Context parent) {
    assert (parent instanceof EnabledTraceUtil.Context);
    SpanBuilder spanBuilder =
        tracer
            .spanBuilder(spanName)
            .setSpanKind(SpanKind.PRODUCER)
            .setParent(((EnabledTraceUtil.Context) parent).context);
    io.opentelemetry.api.trace.Span span =
        addSettingsAttributesToCurrentSpan(spanBuilder).startSpan();
    return new Span(span, spanName);
  }

  @Nonnull
  @Override
  public TraceUtil.Span currentSpan() {
    return new Span(io.opentelemetry.api.trace.Span.current(), "");
  }

  @Nonnull
  @Override
  public TraceUtil.Context currentContext() {
    return new Context(io.opentelemetry.context.Context.current());
  }
}
