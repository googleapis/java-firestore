package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class ToUpper extends Function {
  ToUpper(Expr expr) {
    super("to_upper", Lists.newArrayList(expr));
  }
}
