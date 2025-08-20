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

package com.google.cloud.firestore;

import com.google.api.core.BetaApi;
import com.google.cloud.Timestamp;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A PipelineSnapshot contains the results of a pipeline execution. It can be used to access the
 * documents, execution time, and explain stats.
 */
@BetaApi
public final class PipelineSnapshot {

  private final Pipeline pipeline;
  private final Timestamp executionTime;
  private final List<PipelineResult> results;
  private final ExplainStats explainStats;

  PipelineSnapshot(
      @Nonnull Pipeline pipeline,
      @Nonnull List<PipelineResult> results,
      @Nonnull Timestamp executionTime,
      @Nullable ExplainStats explainStats) {
    this.pipeline = pipeline;
    this.results = results;
    this.executionTime = executionTime;
    this.explainStats = explainStats;
  }

  /**
   * The Pipeline on which you called `execute()` in order to get this `PipelineSnapshot`.
   *
   * @return The pipeline that was executed.
   */
  @Nonnull
  public Pipeline getPipeline() {
    return pipeline;
  }

  /**
   * An array of all the results in the `PipelineSnapshot`.
   *
   * @return The list of results.
   */
  @Nonnull
  public List<PipelineResult> getResults() {
    return results;
  }

  /**
   * The time at which the pipeline producing this result is executed.
   *
   * @return The execution time of the pipeline.
   */
  @Nonnull
  public Timestamp getExecutionTime() {
    return executionTime;
  }

  /**
   * Return stats from query explain.
   *
   * <p>If `explainOptions.mode` was set to `execute` or left unset, then this returns `null`.
   *
   * @return The explain stats, or `null` if not available.
   */
  @Nullable
  public ExplainStats getExplainStats() {
    return explainStats;
  }
}
