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

/** A QueryPlan contains information about the planning stage of a query. */
public final class QueryPlan {
  private static final QueryPlan DEFAULT_INSTANCE =
      new QueryPlan(com.google.firestore.v1.QueryPlan.newBuilder().build());

  private final Map<String, Object> planInfo;

  QueryPlan(com.google.firestore.v1.QueryPlan proto) {
    this.planInfo = UserDataConverter.decodeStruct(proto.getPlanInfo());
  }

  static QueryPlan getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  /*
   * Returns the plan info as a map.
   */
  @Nonnull
  public Map<String, Object> getPlanInfo() {
    return this.planInfo;
  }
}
