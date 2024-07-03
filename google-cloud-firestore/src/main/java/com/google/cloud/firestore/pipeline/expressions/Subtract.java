package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

public final class Subtract extends Function {
  @InternalApi
  Subtract(Expr left, Expr right) {
    super("subtract", Lists.newArrayList(left, right));
  }
}
