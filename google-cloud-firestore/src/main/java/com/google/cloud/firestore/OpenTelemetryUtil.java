/*
 * Copyright 2017 Google LLC
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

/**
 * Helper class that facilitates integration of the Firestore SDK with OpenTelemetry Trace, Metrics,
 * and Logs APIs.
 *
 * <p>This class currently supports tracing utility functions. Metrics and Logging methods will be
 * added in the future.
 */
public class OpenTelemetryUtil {
  private static String OPEN_TELEMETRY_ENV_VAR_NAME = "ENABLE_OPEN_TELEMETRY";
  private static final String SERVICE = "Firestore";

  // Indicates whether telemetry collection is enabled in this instance.
  private boolean enabled;

  @Nullable private OpenTelemetrySdk openTelemetrySdk;

  // The gRPC channel configurator that intercepts gRPC calls for tracing purposes.
  public static class OpenTelemetryGrpcChannelConfigurator
      implements ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> {
    @Override
    public ManagedChannelBuilder apply(ManagedChannelBuilder managedChannelBuilder) {
      GrpcTelemetry grpcTelemetry = GrpcTelemetry.create(GlobalOpenTelemetry.get());
      return managedChannelBuilder.intercept(grpcTelemetry.newClientInterceptor());
    }
  }

  public OpenTelemetryUtil(@Nullable Boolean enabled, @Nullable OpenTelemetrySdk openTelemetrySdk) {
    this.openTelemetrySdk = openTelemetrySdk;

    if (enabled == null) {
      // The caller did not specify whether telemetry collection should be enabled via code.
      // In such cases, we allow enabling/disabling the feature via an environment variable.
      String enableOpenTelemetryEnvVar = System.getenv(OPEN_TELEMETRY_ENV_VAR_NAME);
      if (enableOpenTelemetryEnvVar != null
          && (enableOpenTelemetryEnvVar.toLowerCase().equals("true")
              || enableOpenTelemetryEnvVar.toLowerCase().equals("on"))) {
        this.enabled = true;
      } else {
        this.enabled = false;
      }
    } else {
      this.enabled = enabled;
    }

    // Lastly, initialize an OpenTelemetrySdk if needed.
    if (this.enabled && this.openTelemetrySdk == null) {
      initOpenTelemetry();
    }
  }

  void initOpenTelemetry() {
    // Early exit if telemetry collection is disabled.
    if (!this.enabled || this.openTelemetrySdk != null) return;

    try {
      System.out.println("Initializing GlobalOpenTelemetry inside the SDK...");
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

  @Nullable
  ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> getChannelConfigurator() {
    if (this.enabled) {
      return new OpenTelemetryGrpcChannelConfigurator();
    }
    return null;
  }
}
