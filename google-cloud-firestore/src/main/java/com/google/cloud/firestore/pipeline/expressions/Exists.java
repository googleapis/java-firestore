package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Exists extends Function implements FilterCondition {
  @InternalApi
  Exists(Expr expr) {
    super("exists", Lists.newArrayList(expr));
  }
}
