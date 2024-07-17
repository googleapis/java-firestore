package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Lte extends Function implements FilterCondition {
  @InternalApi
  Lte(Expr left, Expr right) {
    super("lte", Lists.newArrayList(left, right));
  }
}
