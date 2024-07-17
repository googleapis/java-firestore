package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Multiply extends Function {
  @InternalApi
  Multiply(Expr left, Expr right) {
    super("multiply", Lists.newArrayList(left, right));
  }
}
