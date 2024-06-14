package com.google.cloud.firestore.pipeline.expressions;

public final class AggregatorTarget implements Selectable {
  private final Accumulator accumulator;
  private final String fieldName;

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

  public AggregatorTarget toField(String fieldName) {
    return null;
  }

  public AggregatorTarget distinct(boolean on) {
    return new AggregatorTarget(accumulator.distinct(on), fieldName);
  }
}
