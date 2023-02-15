package com.google.cloud.firestore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AggregateField {
  @Nonnull
  public static CountAggregateField count() {
    return new CountAggregateField();
  }

  @Nonnull
  public static SumAggregateField sum(String field) {
    return new SumAggregateField(FieldPath.fromDotSeparatedString(field));
  }

  @Nonnull
  public static SumAggregateField sum(FieldPath fieldPath) {
    return new SumAggregateField(fieldPath);
  }

  @Nonnull
  public static AverageAggregateField average(String field) {
    return new AverageAggregateField(FieldPath.fromDotSeparatedString(field));
  }

  @Nonnull
  public static AverageAggregateField average(FieldPath fieldPath) {
    return new AverageAggregateField(fieldPath);
  }

  @Nullable
  public String getAlias() {
    return null;
  }

  @Nullable
  public String getFieldPath() {
    return null;
  }

  @Nullable
  public String getOperator() {
    return null;
  }

  public static class SumAggregateField extends AggregateField {
    private SumAggregateField(FieldPath field) {
      fieldPath = field;
    }

    @Override
    public String getAlias() {
      return "sum_" + fieldPath.getEncodedPath();
    }

    public String getFieldPath() {
      return fieldPath.getEncodedPath();
    }

    @Override
    @Nullable
    public String getOperator() {
      return "sum";
    }

    private FieldPath fieldPath;
  }

  public static class AverageAggregateField extends AggregateField {
    private AverageAggregateField(FieldPath field) {
      fieldPath = field;
    }

    @Override
    public String getAlias() {
      return "avg_" + fieldPath.getEncodedPath();
    }

    @Override
    public String getFieldPath() {
      return fieldPath.getEncodedPath();
    }

    @Override
    @Nullable
    public String getOperator() {
      return "avg";
    }

    private FieldPath fieldPath;
  }

  public static class CountAggregateField extends AggregateField {
    private CountAggregateField() {}

    @Override
    public String getAlias() {
      return "count";
    }

    public String getFieldPath() {
      return "";
    }

    @Override
    @Nullable
    public String getOperator() {
      return "count";
    }
  }
}
