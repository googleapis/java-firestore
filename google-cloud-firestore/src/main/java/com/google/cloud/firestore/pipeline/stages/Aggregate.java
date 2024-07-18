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
  public static Aggregate newInstance(){
    return new Aggregate(Collections.emptyMap(), Collections.emptyMap());
  }

  @BetaApi
  public Aggregate withGroups(String... fields){
    return new Aggregate(
        PipelineUtils.fieldNamesToMap(fields),
        this.accumulators);
  }

  @BetaApi
  public Aggregate withGroups(Selectable... selectables){
    return new Aggregate(
        PipelineUtils.selectablesToMap(selectables), this.accumulators);
  }

  @BetaApi
  public Aggregate withAccumulators(AccumulatorTarget... aggregators){
    return new Aggregate(this.groups, Arrays.stream(aggregators)
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
