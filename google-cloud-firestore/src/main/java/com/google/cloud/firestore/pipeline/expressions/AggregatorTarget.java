package com.google.cloud.firestore.pipeline.expressions;

public final class AggregatorTarget implements Selectable{
  private final Accumulator accumulator;
  private final String fieldName;

  AggregatorTarget(Accumulator accumulator, String fieldName, boolean distinct) {
    this.accumulator = accumulator;
    this.fieldName = fieldName;
  }

  // Getters (for accumulator, fieldName, and distinct)
  public Accumulator getAccumulator() {
    return accumulator;
  }

  public String getFieldName() {
    return fieldName;
  }
}
