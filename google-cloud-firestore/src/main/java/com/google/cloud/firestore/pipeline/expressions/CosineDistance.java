package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class CosineDistance extends Function {
  CosineDistance(Expr vector1, Expr vector2) {
    super("cosine_distance", Lists.newArrayList(vector1, vector2));
  }
}
