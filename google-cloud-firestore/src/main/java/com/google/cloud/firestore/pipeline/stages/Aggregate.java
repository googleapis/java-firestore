package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.expressions.Accumulator;
import com.google.cloud.firestore.pipeline.expressions.AccumulatorTarget;
import com.google.cloud.firestore.pipeline.expressions.Expr;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@InternalApi
public final class Aggregate implements Stage {

  private static final String name = "aggregate";
  private final Map<String, Expr> groups;
  private final Map<String, Accumulator> accumulators;

  @InternalApi
  Aggregate(Map<String, Expr> groups, Map<String, Accumulator> accumulators) {
    this.groups = groups;
    this.accumulators = accumulators;
  }

  @InternalApi
  public Aggregate(AccumulatorTarget... aggregators) {
    this(
        Collections.emptyMap(),
        Arrays.stream(aggregators)
            .collect(
                Collectors.toMap(
                    AccumulatorTarget::getFieldName, AccumulatorTarget::getAccumulator)));
  }

  @InternalApi
  public Map<String, Expr> getGroups() {
    return groups;
  }

  @InternalApi
  public Map<String, Accumulator> getAccumulators() {
    return accumulators;
  }

  @Override
  public String getName() {
    return name;
  }
}
