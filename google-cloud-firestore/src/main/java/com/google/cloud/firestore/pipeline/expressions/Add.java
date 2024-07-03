package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

public final class Add extends Function {
  @InternalApi
  Add(Expr left, Expr right) {
    super("add", Lists.newArrayList(left, right));
  }
}
