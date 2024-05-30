package com.google.cloud.firestore.pipeline.expressions;

public interface Accumulator extends Expr {
  default AggregatorTarget toField(String fieldName) {
    return new AggregatorTarget(this, fieldName, false);
  }
}
