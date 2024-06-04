package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class ToLower extends Function {
  ToLower(Expr expr) {
    super("to_lower", Lists.newArrayList(expr));
  }
}
