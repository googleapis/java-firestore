package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class If extends Function implements FilterCondition {
  If(FilterCondition condition, Expr trueExpr, Expr falseExpr) {
    super("if", Lists.newArrayList(condition, trueExpr, falseExpr));
  }
}
