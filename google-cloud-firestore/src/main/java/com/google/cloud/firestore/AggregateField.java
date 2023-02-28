/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

  @Override
  public boolean equals(Object other) {
    if (other instanceof CountAggregateField) {
      return ((CountAggregateField) other).equals(this);
    } else if (other instanceof AverageAggregateField) {
      return ((AverageAggregateField) other).equals(this);
    } else if (other instanceof SumAggregateField) {
      return ((SumAggregateField) other).equals(this);
    }
    return false;
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

    @Override
    public boolean equals(Object other) {
      return other instanceof SumAggregateField
          && ((SumAggregateField) other).fieldPath.equals(fieldPath);
    }

    private final FieldPath fieldPath;
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

    @Override
    public boolean equals(Object other) {
      return other instanceof AverageAggregateField
          && ((AverageAggregateField) other).fieldPath.equals(fieldPath);
    }

    private final FieldPath fieldPath;
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

    @Override
    public boolean equals(Object other) {
      return other instanceof CountAggregateField;
    }
  }
}
