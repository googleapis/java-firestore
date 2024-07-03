package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class ArrayFilter extends Function {
  ArrayFilter(Expr array, FilterCondition filter) {
    super("array_filter", Lists.newArrayList(array, filter));
  }
}
