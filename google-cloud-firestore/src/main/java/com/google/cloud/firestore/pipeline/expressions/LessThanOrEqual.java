package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class LessThanOrEqual extends Function implements FilterCondition {
  LessThanOrEqual(Expr left, Expr right) {
    super("lte", Lists.newArrayList(left, right));
  }
}
