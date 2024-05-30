package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class LessThan extends Function implements FilterCondition {
  LessThan(Expr left, Expr right) {
    super("lt", Lists.newArrayList(left, right));
  }
}
