package com.google.cloud.firestore.pipeline.expressions;

public final class AggregatorTarget implements Selectable, Accumulator {
  private final Accumulator accumulator;
  private final String fieldName;
  private boolean distinct = false;

  AggregatorTarget(Accumulator accumulator, String fieldName) {
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

  @Override
  public AggregatorTarget toField(String fieldName) {
    return null;
  }

  @Override
  public Accumulator distinct(boolean on) {
    this.distinct = on;
    return this;
  }
}
