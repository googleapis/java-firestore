package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Gte extends Function implements FilterCondition {
  Gte(Expr left, Expr right) {
    super("gte", Lists.newArrayList(left, right));
  }
}
