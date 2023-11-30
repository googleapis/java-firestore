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

import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import javax.annotation.Nullable;

public class DisabledOpenTelemetryUtil implements OpenTelemetryUtil {
  class Span implements OpenTelemetryUtil.Span {
    @Override
    public void end() {}

    @Override
    public void end(Throwable error) {}

    @Override
    public <T> void endAtFuture(ApiFuture<T> futureValue) {}

    @Override
    public OpenTelemetryUtil.Span addEvent(String name) {
      return this;
    }

    @Override
    public OpenTelemetryUtil.Span addEvent(String name, Attributes attributes) {
      return this;
    }

    @Override
    public OpenTelemetryUtil.Span setAttribute(String key, int value) {
      return this;
    }

    @Override
    public OpenTelemetryUtil.Span setAttribute(String key, String value) {
      return this;
    }

    @Override
    public Scope makeCurrent() {
      return null;
    }
  }

  @Override
  @Nullable
  public ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> getChannelConfigurator() {
    return null;
  }

  @Override
  @Nullable
  public Span startSpan(String spanName) {
    return new Span();
  }

  @Nullable
  @Override
  public OpenTelemetryUtil.Span startSpan(String spanName, Context parent) {
    return new Span();
  }

  @Override
  @Nullable
  public Tracer getTracer() {
    return null;
  }

  @Override
  @Nullable
  public OpenTelemetryUtil.Span currentSpan() {
    return new Span();
  }

  @Override
  public void close() {}
}
