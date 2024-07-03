package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Trim extends Function {
  Trim(Expr expr) {
    super("trim", Lists.newArrayList(expr));
  }
}
