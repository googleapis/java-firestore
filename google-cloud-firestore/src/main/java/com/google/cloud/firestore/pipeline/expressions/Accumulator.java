package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;

@BetaApi
public interface Accumulator extends Expr {
  @BetaApi
  default AccumulatorTarget toField(String fieldName) {
    return new AccumulatorTarget(this, fieldName, false);
  }
}
