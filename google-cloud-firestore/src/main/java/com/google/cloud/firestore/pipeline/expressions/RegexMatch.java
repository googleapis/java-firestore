package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class RegexMatch extends Function implements FilterCondition {
  RegexMatch(Expr expr, Constant regex) {
    super("regex_match", Lists.newArrayList(expr, regex));
  }
}
