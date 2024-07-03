package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class ToUppercase extends Function {
  ToUppercase(Expr expr) {
    super("to_uppercase", Lists.newArrayList(expr));
  }
}
