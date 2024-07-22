package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.firestore.PipelineUtils;
import com.google.cloud.firestore.pipeline.expressions.Accumulator;
import com.google.cloud.firestore.pipeline.expressions.AccumulatorTarget;
import com.google.cloud.firestore.pipeline.expressions.Expr;
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
  public static Aggregate withAccumulators(AccumulatorTarget... accumulators) {
    if (accumulators.length == 0) {
      throw new IllegalArgumentException(
          "Must specify at least one accumulator for aggregate() stage. There is a distinct() stage if only distinct group values are needed.");
    }

    return new Aggregate(
        Collections.emptyMap(),
        Arrays.stream(accumulators)
            .collect(
                Collectors.toMap(
                    AccumulatorTarget::getFieldName, AccumulatorTarget::getAccumulator)));
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
