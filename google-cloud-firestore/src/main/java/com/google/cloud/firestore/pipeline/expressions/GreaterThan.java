package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class GreaterThan extends Function implements FilterCondition {
  GreaterThan(Expr left, Expr right) {
    super("gt", Lists.newArrayList(left, right));
  }
}
