package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Lt extends Function implements FilterCondition {
  Lt(Expr left, Expr right) {
    super("lt", Lists.newArrayList(left, right));
  }
}
