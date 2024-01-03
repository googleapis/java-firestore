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

import java.util.Map;
import javax.annotation.Nonnull;

/** A QueryProfile contains information about planning, execution, and results of a query. */
public final class QueryProfile<T> {
  /** A Map that contains information about the query plan. Contents are subject to change. */
  @Nonnull private final QueryPlan plan;

  /**
   * A Map that contains statistics about the execution of the query. Contents are subject to
   * change.
   */
  @Nonnull private final Map<String, Object> stats;

  /** The snapshot that contains the results of executing the query. */
  @Nonnull private final T snapshot;

  QueryProfile(@Nonnull QueryPlan plan, @Nonnull Map<String, Object> stats, @Nonnull T snapshot) {
    this.plan = plan;
    this.stats = stats;
    this.snapshot = snapshot;
  }

  @Nonnull
  public QueryPlan getPlan() {
    return plan;
  }

  @Nonnull
  public Map<String, Object> getStats() {
    return stats;
  }

  @Nonnull
  public T getSnapshot() {
    return snapshot;
  }
}
