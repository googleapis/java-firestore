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

import java.time.Duration;
import java.util.Map;

/** A ExecutionStats contains information about the execution of a query. */
public final class ExecutionStats {
  final long resultsReturned;
  final Duration executionDuration;
  final long readOperations;
  final Map<String, Object> debugStats;

  ExecutionStats(com.google.firestore.v1.ExecutionStats proto) {
    this.resultsReturned = proto.getResultsReturned();
    this.executionDuration =
        Duration.ofSeconds(
            proto.getExecutionDuration().getSeconds(), proto.getExecutionDuration().getNanos());
    this.readOperations = proto.getReadOperations();
    this.debugStats = UserDataConverter.decodeStruct(proto.getDebugStats());
  }

  /** Returns the number of query results. */
  public long getResultsReturned() {
    return resultsReturned;
  }

  /** Returns the total execution time of the query. */
  // API Review Note: uses java.time.Duration
  public Duration getExecutionDuration() {
    return executionDuration;
  }

  /** Returns the number of read operations that occurred when executing the query. */
  public long getReadOperations() {
    return readOperations;
  }

  /**
   * Returns a map that contains additional statistics related to query execution. Note: The content
   * of this map are subject to change.
   */
  public Map<String, Object> getDebugStats() {
    return debugStats;
  }
}
