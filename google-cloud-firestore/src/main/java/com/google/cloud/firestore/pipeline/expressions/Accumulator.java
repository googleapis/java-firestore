package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;

@BetaApi
public interface Accumulator extends Expr {
  @BetaApi
  @Override
  default ExprWithAlias<Accumulator> as(String fieldName) {
    return new ExprWithAlias<>(this, fieldName);
  }
}
