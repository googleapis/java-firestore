package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class EuclideanDistance extends Function {
  EuclideanDistance(Expr vector1, Expr vector2) {
    super("euclidean_distance", Lists.newArrayList(vector1, vector2));
  }
}
