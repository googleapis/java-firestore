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

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AggregateField {
  @Nonnull
  public static CountAggregateField count() {
    return new CountAggregateField();
  }

  @Nonnull
  public static SumAggregateField sum(@Nonnull String field) {
    return new SumAggregateField(FieldPath.fromDotSeparatedString(field));
  }

  @Nonnull
  public static SumAggregateField sum(@Nonnull FieldPath fieldPath) {
    return new SumAggregateField(fieldPath);
  }

  @Nonnull
  public static AverageAggregateField average(@Nonnull String field) {
    return new AverageAggregateField(FieldPath.fromDotSeparatedString(field));
  }

  @Nonnull
  public static AverageAggregateField average(@Nonnull FieldPath fieldPath) {
    return new AverageAggregateField(fieldPath);
  }

  @Nullable FieldPath fieldPath;

  /** Returns the alias used internally for this aggregate field. */
  @Nonnull
  String getAlias() {
    // Use $operator_$field format if it's an aggregation of a specific field. For example: sum_foo.
    // Use $operator format if there's no field. For example: count.
    return getOperator() + (fieldPath == null ? "" : "_" + fieldPath.getEncodedPath());
  }

  /**
   * Returns the field on which the aggregation takes place. Returns an empty string if there's no
   * field (e.g. for count).
   */
  @Nonnull
  String getFieldPath() {
    return fieldPath == null ? "" : fieldPath.getEncodedPath();
  }

  /** Returns a string representation of this aggregation's operator. For example: "sum" */
  abstract @Nonnull String getOperator();

  /**
   * Returns true if the given object is equal to this object. Two `AggregateField` objects are
   * considered equal if they have the same operator and operate on the same field.
   */
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof AggregateField)) {
      return false;
    }
    AggregateField otherAggregateField = (AggregateField) other;
    return getOperator().equals(otherAggregateField.getOperator())
        && getFieldPath().equals(otherAggregateField.getFieldPath());
  }

  /** Calculates and returns the hash code for this object. */
  @Override
  public int hashCode() {
    return Objects.hash(getOperator(), getFieldPath());
  }

  public static class SumAggregateField extends AggregateField {
    private SumAggregateField(@Nonnull FieldPath field) {
      fieldPath = field;
    }

    @Override
    @Nonnull
    public String getOperator() {
      return "sum";
    }
  }

  public static class AverageAggregateField extends AggregateField {
    private AverageAggregateField(@Nonnull FieldPath field) {
      fieldPath = field;
    }

    @Override
    @Nonnull
    public String getOperator() {
      return "average";
    }
  }

  public static class CountAggregateField extends AggregateField {
    private CountAggregateField() {}

    @Override
    @Nonnull
    public String getOperator() {
      return "count";
    }
  }
}
