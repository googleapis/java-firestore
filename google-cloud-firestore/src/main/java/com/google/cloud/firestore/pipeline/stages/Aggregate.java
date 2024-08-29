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

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.firestore.PipelineUtils;
import com.google.cloud.firestore.pipeline.expressions.Accumulator;
import com.google.cloud.firestore.pipeline.expressions.Expr;
import com.google.cloud.firestore.pipeline.expressions.ExprWithAlias;
import com.google.cloud.firestore.pipeline.expressions.Selectable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@BetaApi
public final class Aggregate implements Stage {

  private static final String name = "aggregate";
  private final Map<String, Expr> groups;
  private final Map<String, Accumulator> accumulators;

  @BetaApi
  public Aggregate withGroups(String... fields) {
    return new Aggregate(PipelineUtils.fieldNamesToMap(fields), this.accumulators);
  }

  @BetaApi
  public Aggregate withGroups(Selectable... selectables) {
    return new Aggregate(PipelineUtils.selectablesToMap(selectables), this.accumulators);
  }

  @BetaApi
  public static Aggregate withAccumulators(ExprWithAlias<Accumulator>... accumulators) {
    if (accumulators.length == 0) {
      throw new IllegalArgumentException(
          "Must specify at least one accumulator for aggregate() stage. There is a distinct() stage if only distinct group values are needed.");
    }

    return new Aggregate(
        Collections.emptyMap(),
        Arrays.stream(accumulators)
            .collect(Collectors.toMap(ExprWithAlias::getAlias, ExprWithAlias::getExpr)));
  }

  private Aggregate(Map<String, Expr> groups, Map<String, Accumulator> accumulators) {
    this.groups = groups;
    this.accumulators = accumulators;
  }

  @InternalApi
  Map<String, Expr> getGroups() {
    return groups;
  }

  @InternalApi
  Map<String, Accumulator> getAccumulators() {
    return accumulators;
  }

  @Override
  public String getName() {
    return name;
  }
}
