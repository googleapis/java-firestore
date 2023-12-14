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
import com.google.cloud.firestore.FirestoreOptions;
import com.google.common.annotations.VisibleForTesting;
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.grpc.v1_6.GrpcTelemetry;
import javax.annotation.Nullable;

public class EnabledOpenTelemetryUtil extends OpenTelemetryUtil {
  private OpenTelemetry openTelemetry;
  private final EnabledTraceUtil traceUtil;

  @Override
  public TraceUtil getTraceUtil() {
    return traceUtil;
  }

  @VisibleForTesting
  OpenTelemetry getOpenTelemetry() {
    return openTelemetry;
  }

  // The gRPC channel configurator that intercepts gRPC calls for tracing purposes.
  public static class OpenTelemetryGrpcChannelConfigurator
      implements ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> {
    @Override
    public ManagedChannelBuilder apply(ManagedChannelBuilder managedChannelBuilder) {
      GrpcTelemetry grpcTelemetry = GrpcTelemetry.create(GlobalOpenTelemetry.get());
      return managedChannelBuilder.intercept(grpcTelemetry.newClientInterceptor());
    }
  }

  @Override
  @Nullable
  public ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> getChannelConfigurator() {
    return new OpenTelemetryGrpcChannelConfigurator();
  }

  public EnabledOpenTelemetryUtil(FirestoreOptions firestoreOptions) {
    this.openTelemetry = firestoreOptions.getOpenTelemetryOptions().getOpenTelemetry();

    // If telemetry collection is enabled, but an OpenTelemetry instance is not provided, fall back
    // to using GlobalOpenTelemetry.
    if (this.openTelemetry == null) {
      this.openTelemetry = GlobalOpenTelemetry.get();
    }

    this.traceUtil =
        new EnabledTraceUtil(firestoreOptions, this.openTelemetry.getTracer(LIBRARY_NAME));
  }
}
