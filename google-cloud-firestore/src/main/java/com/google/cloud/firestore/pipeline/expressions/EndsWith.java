package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class EndsWith extends Function implements FilterCondition {
  @InternalApi
  EndsWith(Expr expr, Expr postfix) {
    super("ends_with", Lists.newArrayList(expr, postfix));
  }
}
