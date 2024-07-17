package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Length extends Function {
  @InternalApi
  Length(Expr expr) {
    super("length", Lists.newArrayList(expr));
  }
}
