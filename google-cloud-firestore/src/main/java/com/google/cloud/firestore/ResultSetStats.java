/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.firestore;

import com.google.api.core.InternalApi;
import com.google.cloud.Structs;
import com.google.common.base.Objects;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Model class containing the planning and execution stats for a query. If QueryMode.EXPLAIN_ANALYZE
 * was set, QueryPlan and QueryStats will be present. If QueryMode.EXPLAIN was set, QueryPlan will
 * be present and QueryStats will return null.
 */
public class ResultSetStats {

  private QueryPlan queryPlan = null;
  private Map<String, Object> queryStats = null;

  @InternalApi
  public ResultSetStats(com.google.firestore.v1.ResultSetStats proto) {
    if (proto.hasQueryPlan()) {
      this.queryPlan = new QueryPlan(proto.getQueryPlan());
    }
    if (proto.hasQueryStats()) {
      this.queryStats = Structs.asMap(proto.getQueryStats());
    }
  }

  /**
   * Returns the plan for the query, if EXPLAIN or EXPLAIN_ANALYZE was set. Otherwise, returns null.
   */
  @Nullable
  public QueryPlan getQueryPlan() {
    return this.queryPlan;
  }

  /** Returns the stats for the query if EXPLAIN_ANALYZE was set. Otherwise, returns null. */
  @Nullable
  public Map<String, Object> getQueryStats() {
    return this.queryStats;
  }

  public boolean hasQueryStats() {
    return this.queryStats != null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ResultSetStats)) {
      return false;
    }
    ResultSetStats that = (ResultSetStats) o;
    return Objects.equal(queryPlan, that.queryPlan) && Objects.equal(queryStats, that.queryStats);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(queryPlan, queryStats);
  }
}
