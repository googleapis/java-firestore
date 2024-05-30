package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Avg extends Function implements Accumulator {
  Avg(Expr value, boolean distinct) {
    super("avg", Lists.newArrayList(value));
  }
}
