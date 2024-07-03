package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Length extends Function {
  Length(Expr expr) {
    super("length", Lists.newArrayList(expr));
  }
}
