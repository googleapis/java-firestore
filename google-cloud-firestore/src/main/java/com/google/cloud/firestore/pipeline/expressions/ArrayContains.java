package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class ArrayContains extends Function implements FilterCondition {
  ArrayContains(Expr array, Expr element) {
    super("array_contains", Lists.newArrayList(array, element));
  }
}
