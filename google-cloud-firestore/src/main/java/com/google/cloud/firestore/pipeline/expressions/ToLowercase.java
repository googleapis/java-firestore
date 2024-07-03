package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class ToLowercase extends Function {
  ToLowercase(Expr expr) {
    super("to_lowercase", Lists.newArrayList(expr));
  }
}
