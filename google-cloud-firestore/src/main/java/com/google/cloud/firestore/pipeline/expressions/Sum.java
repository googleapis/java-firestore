package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Sum extends Function implements Accumulator {
  Sum(Expr value, boolean distinct) {
    super("sum", Lists.newArrayList(value));
  }
}
