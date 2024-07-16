package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Eq extends Function implements FilterCondition {
  Eq(Expr left, Expr right) {
    super("eq", Lists.newArrayList(left, right));
  }
}
