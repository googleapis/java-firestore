package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Gt extends Function implements FilterCondition {
  @InternalApi
  Gt(Expr left, Expr right) {
    super("gt", Lists.newArrayList(left, right));
  }
}
