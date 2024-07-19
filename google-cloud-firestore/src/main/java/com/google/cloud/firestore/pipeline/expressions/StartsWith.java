package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class StartsWith extends Function implements FilterCondition {
  @InternalApi
  StartsWith(Expr expr, Expr prefix) {
    super("starts_with", Lists.newArrayList(expr, prefix));
  }
}
