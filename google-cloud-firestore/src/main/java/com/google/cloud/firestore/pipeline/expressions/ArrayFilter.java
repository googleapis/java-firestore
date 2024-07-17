package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class ArrayFilter extends Function {
  @InternalApi
  ArrayFilter(Expr array, FilterCondition filter) {
    super("array_filter", Lists.newArrayList(array, filter));
  }
}
