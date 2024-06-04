package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class StartsWith extends Function implements FilterCondition {
  StartsWith(Expr expr, Expr prefix) {
    super("starts_with", Lists.newArrayList(expr, prefix));
  }
}
