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

import static io.opentelemetry.semconv.resource.attributes.ResourceAttributes.SERVICE_NAME;

import com.google.api.core.ApiFunction;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.opentelemetry.trace.TraceExporter;
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.GlobalOpenTelemetry;
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
  private EnabledTraceUtil traceUtil;

  // The gRPC channel configurator that intercepts gRPC calls for tracing purposes.
  public static class OpenTelemetryGrpcChannelConfigurator
      implements ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> {
    @Override
    public ManagedChannelBuilder apply(ManagedChannelBuilder managedChannelBuilder) {
      GrpcTelemetry grpcTelemetry = GrpcTelemetry.create(GlobalOpenTelemetry.get());
      return managedChannelBuilder.intercept(grpcTelemetry.newClientInterceptor());
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

    this.traceUtil = new EnabledTraceUtil(firestoreOptions, openTelemetrySdk);
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

  @Override
  public TraceUtil getTraceUtil() {
    return traceUtil;
  }

  @Override
  public void close() {
    openTelemetrySdk.close();
  }
}
