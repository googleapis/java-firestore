package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Gte extends Function implements FilterCondition {
  @InternalApi
  Gte(Expr left, Expr right) {
    super("gte", Lists.newArrayList(left, right));
  }
}
