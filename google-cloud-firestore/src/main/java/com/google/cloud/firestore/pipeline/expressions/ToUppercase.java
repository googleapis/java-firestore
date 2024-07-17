package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class ToUppercase extends Function {
  @InternalApi
  ToUppercase(Expr expr) {
    super("to_uppercase", Lists.newArrayList(expr));
  }
}
