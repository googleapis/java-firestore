package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Equal extends Function implements FilterCondition {
  Equal(Expr left, Expr right) {
    super("eq", Lists.newArrayList(left, right));
  }
}
