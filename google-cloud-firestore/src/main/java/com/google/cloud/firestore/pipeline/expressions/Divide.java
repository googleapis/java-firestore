package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Divide extends Function {
  @InternalApi
  Divide(Expr left, Expr right) {
    super("divide", Lists.newArrayList(left, right));
  }
}
