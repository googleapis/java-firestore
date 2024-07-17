package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Min extends Function implements Accumulator {
  @InternalApi
  Min(Expr value, boolean distinct) {
    super("min", Lists.newArrayList(value));
  }
}
