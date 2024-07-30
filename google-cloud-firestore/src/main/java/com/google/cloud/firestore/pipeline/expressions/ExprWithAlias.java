package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.InternalApi;

@InternalApi
public final class ExprWithAlias<T extends Expr> implements Selectable {

  private final String alias;
  private final T expr;

  @InternalApi
  ExprWithAlias(T expr, String alias) {
    this.expr = expr;
    this.alias = alias;
  }

  @InternalApi
  public String getAlias() {
    return alias;
  }

  @InternalApi
  public T getExpr() {
    return expr;
  }
}
