package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class If extends Function implements FilterCondition {
  @InternalApi
  If(FilterCondition condition, Expr trueExpr, Expr falseExpr) {
    super("if", Lists.newArrayList(condition, trueExpr, falseExpr));
  }
}
