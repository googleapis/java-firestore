package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;

@BetaApi
public final class AccumulatorTarget implements Selectable {
  private final Accumulator accumulator;
  private final String fieldName;

  @InternalApi
  AccumulatorTarget(Accumulator accumulator, String fieldName, boolean distinct) {
    this.accumulator = accumulator;
    this.fieldName = fieldName;
  }

  // Getters (for accumulator, fieldName, and distinct)
  @InternalApi
  public Accumulator getAccumulator() {
    return accumulator;
  }

  @InternalApi
  public String getFieldName() {
    return fieldName;
  }
}
