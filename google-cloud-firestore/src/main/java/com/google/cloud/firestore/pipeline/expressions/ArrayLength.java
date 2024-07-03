package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class ArrayLength extends Function {
  ArrayLength(Expr array) {
    super("array_length", Lists.newArrayList(array));
  }
}
