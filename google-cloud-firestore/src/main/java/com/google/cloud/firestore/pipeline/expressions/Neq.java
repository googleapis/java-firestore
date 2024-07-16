package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Neq extends Function implements FilterCondition {
  Neq(Expr left, Expr right) {
    super("neq", Lists.newArrayList(left, right));
  }
}
