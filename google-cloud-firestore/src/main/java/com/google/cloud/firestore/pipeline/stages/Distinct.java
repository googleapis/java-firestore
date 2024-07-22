package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.expressions.Expr;
import java.util.Map;

@BetaApi
public final class Distinct implements Stage {

  private static final String name = "distinct";
  private final Map<String, Expr> groups;

  @InternalApi
  public Distinct(Map<String, Expr> groups) {
    this.groups = groups;
  }

  @InternalApi
  Map<String, Expr> getGroups() {
    return groups;
  }

  @Override
  public String getName() {
    return name;
  }
}
