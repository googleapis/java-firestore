package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Max extends Function implements Accumulator {
  Max(Expr value, boolean distinct) {
    super("max", Lists.newArrayList(value));
  }
}
