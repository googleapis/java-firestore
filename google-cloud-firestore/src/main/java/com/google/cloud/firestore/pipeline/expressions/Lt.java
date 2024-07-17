package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Lt extends Function implements FilterCondition {
  @InternalApi
  Lt(Expr left, Expr right) {
    super("lt", Lists.newArrayList(left, right));
  }
}
