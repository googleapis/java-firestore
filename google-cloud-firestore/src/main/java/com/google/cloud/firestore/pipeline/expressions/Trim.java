package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Trim extends Function {
  @InternalApi
  Trim(Expr expr) {
    super("trim", Lists.newArrayList(expr));
  }
}
