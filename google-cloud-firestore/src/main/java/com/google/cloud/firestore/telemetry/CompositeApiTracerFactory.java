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

package com.google.cloud.firestore.telemetry;

import com.google.api.core.InternalApi;
import com.google.api.gax.tracing.ApiTracer;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.BaseApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

/** Combines multiple {@link ApiTracerFactory} into a single {@link ApiTracerFactory}. */
@InternalApi
public class CompositeApiTracerFactory extends BaseApiTracerFactory {

  private final List<ApiTracerFactory> apiTracerFactories;

  public CompositeApiTracerFactory(List<ApiTracerFactory> apiTracerFactories) {
    this.apiTracerFactories = ImmutableList.copyOf(apiTracerFactories);
  }

  @Override
  public ApiTracer newTracer(ApiTracer parent, SpanName spanName, OperationType operationType) {
    List<ApiTracer> children = new ArrayList<>(apiTracerFactories.size());

    for (ApiTracerFactory factory : apiTracerFactories) {
      children.add(factory.newTracer(parent, spanName, operationType));
    }
    return new CompositeApiTracer(children);
  }
}
