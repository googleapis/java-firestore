package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class IsNaN extends Function implements FilterCondition {
  @InternalApi
  IsNaN(Expr value) {
    super("is_nan", Lists.newArrayList(value));
  }
}
