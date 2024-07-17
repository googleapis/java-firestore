package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Sum extends Function implements Accumulator {
  @InternalApi
  Sum(Expr value, boolean distinct) {
    super("sum", Lists.newArrayList(value));
  }
}
