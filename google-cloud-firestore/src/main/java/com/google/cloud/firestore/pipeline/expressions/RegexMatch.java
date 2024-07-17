package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class RegexMatch extends Function implements FilterCondition {
  @InternalApi
  RegexMatch(Expr expr, Expr regex) {
    super("regex_match", Lists.newArrayList(expr, regex));
  }
}
