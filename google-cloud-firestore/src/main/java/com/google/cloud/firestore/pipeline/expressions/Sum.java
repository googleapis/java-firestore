package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Sum extends Function implements Accumulator {
  private boolean distinct = false;

  Sum(Expr value, boolean distinct) {
    super("sum", Lists.newArrayList(value));
  }

  @Override
  public Accumulator distinct(boolean on) {
    this.distinct = on;
    return this;
  }
}
