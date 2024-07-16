package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Lte extends Function implements FilterCondition {
  Lte(Expr left, Expr right) {
    super("lte", Lists.newArrayList(left, right));
  }
}
