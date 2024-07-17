package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class RegexContains extends Function implements FilterCondition {
  @InternalApi
  RegexContains(Expr expr, Expr regex) {
    super("regex_contains", Lists.newArrayList(expr, regex));
  }
}
