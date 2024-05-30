package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.expressions.Accumulator;
import com.google.cloud.firestore.pipeline.expressions.AggregatorTarget;
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
  public Aggregate(AggregatorTarget... aggregators) {
    this(
        Collections.emptyMap(),
        Arrays.stream(aggregators)
            .collect(
                Collectors.toMap(
                    AggregatorTarget::getFieldName, AggregatorTarget::getAccumulator)));
  }

  public Map<String, Expr> getGroups() {
    return groups;
  }

  public Map<String, Accumulator> getAccumulators() {
    return accumulators;
  }

  @Override
  public String getName() {
    return name;
  }
}
