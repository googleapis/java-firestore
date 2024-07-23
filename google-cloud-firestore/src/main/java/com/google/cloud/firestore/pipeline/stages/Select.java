package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.expressions.Expr;
import java.util.Map;

@InternalApi
public final class Select implements Stage {

  private static final String name = "select";
  private final Map<String, Expr> projections;

  @InternalApi
  public Select(Map<String, Expr> projections) {
    this.projections = projections;
  }

  @InternalApi
  public Map<String, Expr> getProjections() {
    return projections;
  }

  @Override
  @InternalApi
  public String getName() {
    return name;
  }
}
