package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Like extends Function implements FilterCondition {
  @InternalApi
  Like(Expr expr, Expr pattern) {
    super("like", Lists.newArrayList(expr, pattern));
  }
}
