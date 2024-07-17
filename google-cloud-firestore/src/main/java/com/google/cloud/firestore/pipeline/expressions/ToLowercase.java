package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class ToLowercase extends Function {
  @InternalApi
  ToLowercase(Expr expr) {
    super("to_lowercase", Lists.newArrayList(expr));
  }
}
