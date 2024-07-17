package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class IsNull extends Function implements FilterCondition {
  @InternalApi
  IsNull(Expr value) {
    super("is_null", Lists.newArrayList(value));
  }
}
