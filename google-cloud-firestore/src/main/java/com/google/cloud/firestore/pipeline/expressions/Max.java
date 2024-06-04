package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Max extends Function implements Accumulator {
  private boolean distinct = false;

  Max(Expr value, boolean distinct) {
    super("max", Lists.newArrayList(value));
  }

  @Override
  public Accumulator distinct(boolean on) {
    this.distinct = on;
    return this;
  }
}
