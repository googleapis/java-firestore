package com.google.cloud.firestore.pipeline;

import com.google.cloud.firestore.Pipeline;
import com.google.cloud.firestore.pipeline.expressions.AggregatorTarget;
import com.google.cloud.firestore.pipeline.expressions.Selectable;
import java.util.List;

public class GroupingPipeline {
  private final Pipeline p;
  private final List<Selectable> by;

  public GroupingPipeline(Pipeline p, List<Selectable> by) {
    this.p = p;
    this.by = by;
  }

  public Pipeline aggregate(AggregatorTarget... aggregator) {
    // TODO: Implement actual aggregation logic
    return this.p;
  }
}
