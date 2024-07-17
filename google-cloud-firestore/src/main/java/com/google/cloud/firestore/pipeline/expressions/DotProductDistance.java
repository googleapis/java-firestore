package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class DotProductDistance extends Function {
  @InternalApi
  DotProductDistance(Expr vector1, Expr vector2) {
    super("dot_product", Lists.newArrayList(vector1, vector2));
  }
}
