package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Max extends Function implements Accumulator {
  @InternalApi
  Max(Expr value, boolean distinct) {
    super("max", Lists.newArrayList(value));
  }
}
