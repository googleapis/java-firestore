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

public class PipelineOptions extends AbstractOptions<PipelineOptions> {

  public static PipelineOptions DEFAULT = new PipelineOptions(InternalOptions.EMPTY)
      .withExecutionMode("execute");

  PipelineOptions(InternalOptions options) {
    super(options);
  }

  @Override
  PipelineOptions self(InternalOptions options) {
    return new PipelineOptions(options);
  }

  public final PipelineOptions withExplainExecutionMode() {
    return withExecutionMode("explain");
  }

  public PipelineOptions withProfileExecutionMode() {
    return withExecutionMode("profile");
  }

  private PipelineOptions withExecutionMode(String mode) {
    return with("execution_mode", mode);
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
