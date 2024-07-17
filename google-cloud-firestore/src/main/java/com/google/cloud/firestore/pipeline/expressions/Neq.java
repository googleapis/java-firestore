package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Neq extends Function implements FilterCondition {
  @InternalApi
  Neq(Expr left, Expr right) {
    super("neq", Lists.newArrayList(left, right));
  }
}
