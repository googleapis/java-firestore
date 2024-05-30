package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.expressions.FilterCondition;

@InternalApi
public final class Filter implements Stage {

  private static final String name = "filter";
  private final FilterCondition condition;

  @InternalApi
  public Filter(FilterCondition condition) {
    this.condition = condition;
  }

  public FilterCondition getCondition() {
    return condition;
  }

  @Override
  public String getName() {
    return name;
  }
}
