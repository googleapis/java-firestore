package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Avg extends Function implements Accumulator {
  @InternalApi
  Avg(Expr value, boolean distinct) {
    super("avg", Lists.newArrayList(value));
  }
}
