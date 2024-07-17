package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Eq extends Function implements FilterCondition {
  @InternalApi
  Eq(Expr left, Expr right) {
    super("eq", Lists.newArrayList(left, right));
  }
}
