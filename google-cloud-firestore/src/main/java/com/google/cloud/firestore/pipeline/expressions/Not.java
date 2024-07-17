package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Not extends Function implements FilterCondition {
  @InternalApi
  Not(Expr condition) {
    super("not", Lists.newArrayList(condition));
  }
}
