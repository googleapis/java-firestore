package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Gt extends Function implements FilterCondition {
  Gt(Expr left, Expr right) {
    super("gt", Lists.newArrayList(left, right));
  }
}
