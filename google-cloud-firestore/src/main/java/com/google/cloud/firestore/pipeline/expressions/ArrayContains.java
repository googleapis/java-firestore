package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class ArrayContains extends Function implements FilterCondition {
  @InternalApi
  ArrayContains(Expr array, Expr element) {
    super("array_contains", Lists.newArrayList(array, element));
  }
}
