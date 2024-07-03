package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class ArrayTransform extends Function {
  ArrayTransform(Expr array, Function transform) {
    super("array_transform", Lists.newArrayList(array, transform));
  }
}
