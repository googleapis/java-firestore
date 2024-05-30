package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class GreaterThanOrEqual extends Function implements FilterCondition {
  GreaterThanOrEqual(Expr left, Expr right) {
    super("gte", Lists.newArrayList(left, right));
  }
}
