package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class ArrayTransform extends Function {
  @InternalApi
  ArrayTransform(Expr array, Function transform) {
    super("array_transform", Lists.newArrayList(array, transform));
  }
}
