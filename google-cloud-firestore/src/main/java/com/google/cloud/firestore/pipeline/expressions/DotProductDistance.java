package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class DotProductDistance extends Function {
  DotProductDistance(Expr vector1, Expr vector2) {
    super("dot_product", Lists.newArrayList(vector1, vector2));
  }
}
