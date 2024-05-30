package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class NotEqual extends Function implements FilterCondition {
  NotEqual(Expr left, Expr right) {
    super("neq", Lists.newArrayList(left, right));
  }
}
