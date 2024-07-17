package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class EuclideanDistance extends Function {
  @InternalApi
  EuclideanDistance(Expr vector1, Expr vector2) {
    super("euclidean_distance", Lists.newArrayList(vector1, vector2));
  }
}
