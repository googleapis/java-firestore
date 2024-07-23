package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.expressions.FilterCondition;

@InternalApi
public final class Where implements Stage {

  private static final String name = "where";
  private final FilterCondition condition;

  @InternalApi
  public Where(FilterCondition condition) {
    this.condition = condition;
  }

  @InternalApi
  public FilterCondition getCondition() {
    return condition;
  }

  @Override
  public String getName() {
    return name;
  }
}
