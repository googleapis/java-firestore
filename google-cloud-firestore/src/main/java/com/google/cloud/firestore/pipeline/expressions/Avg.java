package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Avg extends Function implements Accumulator {
  private boolean distinct = false;

  Avg(Expr value, boolean distinct) {
    super("avg", Lists.newArrayList(value));
  }

  @Override
  public Accumulator distinct(boolean on) {
    this.distinct = on;
    return this;
  }
}
