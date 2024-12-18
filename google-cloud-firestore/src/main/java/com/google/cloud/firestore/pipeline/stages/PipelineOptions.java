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

package com.google.cloud.firestore.pipeline.stages;

import com.google.cloud.firestore.PipelineUtils;
import com.google.firestore.v1.Value;

public final class PipelineOptions extends AbstractOptions<PipelineOptions> {

  public static PipelineOptions DEFAULT = new PipelineOptions(InternalOptions.EMPTY);

  public enum ExecutionMode {
    EXECUTE("execute"),
    EXPLAIN("explain"),
    PROFILE("profile");

    private final Value value;
    ExecutionMode(String profile) {
      value = PipelineUtils.encodeValue(profile);
    }
  }

  PipelineOptions(InternalOptions options) {
    super(options);
  }

  @Override
  PipelineOptions self(InternalOptions options) {
    return new PipelineOptions(options);
  }

  public PipelineOptions withExecutionMode(ExecutionMode mode) {
    return with("execution_mode", mode.value);
  }

  public PipelineOptions withIndexRecommendationEnabled() {
    return with("index_recommendation", true);
  }

  public PipelineOptions withShowAlternativePlanEnabled() {
    return with("show_alternative_plans", true);
  }

  public PipelineOptions withRedactEnabled() {
    return with("redact", true);
  }
}
