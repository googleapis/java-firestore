package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.InternalApi;

@InternalApi
public final class ExprWithAlias implements Selectable {

  private final String alias;
  private final Expr expr;

  @InternalApi
  ExprWithAlias(Expr expr, String alias) {
    this.expr = expr;
    this.alias = alias;
  }

  @InternalApi
  public String getAlias() {
    return alias;
  }

  @InternalApi
  public Expr getExpr() {
    return expr;
  }
}
