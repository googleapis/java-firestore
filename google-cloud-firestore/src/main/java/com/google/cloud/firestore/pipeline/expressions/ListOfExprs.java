package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.InternalApi;
import com.google.common.collect.ImmutableList;
import java.util.List;

@InternalApi
public final class ListOfExprs implements Expr {
  private final ImmutableList<Expr> conditions;

  @InternalApi
  ListOfExprs(List<Expr> list) {
    this.conditions = ImmutableList.copyOf(list);
  }

  @InternalApi
  public List<Expr> getConditions() {
    return conditions;
  }
}
