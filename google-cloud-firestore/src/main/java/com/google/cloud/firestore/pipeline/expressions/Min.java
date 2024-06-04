package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Min extends Function implements Accumulator {
  private boolean distinct = false;

  Min(Expr value, boolean distinct) {
    super("min", Lists.newArrayList(value));
  }

  @Override
  public Accumulator distinct(boolean on) {
    this.distinct = on;
    return this;
  }
}
