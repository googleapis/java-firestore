package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class ArrayLength extends Function {
  @InternalApi
  ArrayLength(Expr array) {
    super("array_length", Lists.newArrayList(array));
  }
}
