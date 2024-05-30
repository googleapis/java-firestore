package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Min extends Function implements Accumulator {
  Min(Expr value, boolean distinct) {
    super("min", Lists.newArrayList(value));
  }
}
