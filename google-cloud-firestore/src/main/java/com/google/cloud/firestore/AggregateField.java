/*
 * Copyright 2022 Google LLC
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

  private AggregateField() {}

  @Nonnull
  public static CountAggregateField count() {
    return new CountAggregateField();
  }

  @Nonnull
  public static MinAggregateField min(@Nonnull String field) {
    return min(FieldPath.fromDotSeparatedString(field));
  }

  @Nonnull
  public static MinAggregateField min(@Nonnull FieldPath field) {
    return new MinAggregateField(field);
  }

  @Nonnull
  public static MaxAggregateField max(@Nonnull String field) {
    return max(FieldPath.fromDotSeparatedString(field));
  }

  @Nonnull
  public static MaxAggregateField max(@Nonnull FieldPath field) {
    return new MaxAggregateField(field);
  }

  @Nonnull
  public static AverageAggregateField average(@Nonnull String field) {
    return average(FieldPath.fromDotSeparatedString(field));
  }

  @Nonnull
  public static AverageAggregateField average(@Nonnull FieldPath field) {
    return new AverageAggregateField(field);
  }

  @Nonnull
  public static SumAggregateField sum(@Nonnull String field) {
    return sum(FieldPath.fromDotSeparatedString(field));
  }

  @Nonnull
  public static SumAggregateField sum(@Nonnull FieldPath field) {
    return new SumAggregateField(field);
  }

  @Nonnull
  public static FirstAggregateField first(@Nonnull String field) {
    return first(FieldPath.fromDotSeparatedString(field));
  }

  @Nonnull
  public static FirstAggregateField first(@Nonnull FieldPath field) {
    return new FirstAggregateField(field);
  }

  @Nonnull
  public static LastAggregateField last(@Nonnull String field) {
    return last(FieldPath.fromDotSeparatedString(field));
  }

  @Nonnull
  public static LastAggregateField last(@Nonnull FieldPath field) {
    return new LastAggregateField(field);
  }

  @Override
  public abstract boolean equals(Object obj);

  @Override
  public abstract int hashCode();

  @Override
  public abstract String toString();

  public static final class CountAggregateField extends AggregateField {

    @Nullable private Integer upTo;

    CountAggregateField() {}

    CountAggregateField(@Nullable Integer upTo) {
      this.upTo = upTo;
    }

    public CountAggregateField upTo(int upTo) {
      if (upTo < 0) {
        throw new IllegalArgumentException("upTo==" + upTo);
      }
      return new CountAggregateField(upTo);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != this.getClass()) {
        return false;
      }
      CountAggregateField other = (CountAggregateField) obj;
      return Objects.equals(upTo, other.upTo);
    }

    @Override
    public int hashCode() {
      return Objects.hash("COUNT", upTo);
    }

    @Override
    public String toString() {
      if (upTo == null) {
        return "COUNT";
      } else {
        return "COUNT(upTo=" + upTo + ")";
      }
    }
  }

  public static final class MinAggregateField extends AggregateField {

    @Nonnull private FieldPath field;

    MinAggregateField(@Nonnull FieldPath field) {
      this.field = field;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != this.getClass()) {
        return false;
      }
      return field.equals(((MinAggregateField) obj).field);
    }

    @Override
    public int hashCode() {
      return toString().hashCode();
    }

    @Override
    public String toString() {
      return "MIN(" + field.toString() + ")";
    }
  }

  public static final class MaxAggregateField extends AggregateField {

    @Nonnull private FieldPath field;

    MaxAggregateField(@Nonnull FieldPath field) {
      this.field = field;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != this.getClass()) {
        return false;
      }
      return field.equals(((MaxAggregateField) obj).field);
    }

    @Override
    public int hashCode() {
      return toString().hashCode();
    }

    @Override
    public String toString() {
      return "MAX(" + field.toString() + ")";
    }
  }

  public static final class AverageAggregateField extends AggregateField {

    @Nonnull private FieldPath field;

    AverageAggregateField(@Nonnull FieldPath field) {
      this.field = field;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != this.getClass()) {
        return false;
      }
      return field.equals(((AverageAggregateField) obj).field);
    }

    @Override
    public int hashCode() {
      return toString().hashCode();
    }

    @Override
    public String toString() {
      return "AVERAGE(" + field.toString() + ")";
    }
  }

  public static final class SumAggregateField extends AggregateField {

    @Nonnull private FieldPath field;

    SumAggregateField(@Nonnull FieldPath field) {
      this.field = field;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != this.getClass()) {
        return false;
      }
      return field.equals(((SumAggregateField) obj).field);
    }

    @Override
    public int hashCode() {
      return toString().hashCode();
    }

    @Override
    public String toString() {
      return "SUM(" + field.toString() + ")";
    }
  }

  public static final class FirstAggregateField extends AggregateField {

    @Nonnull private FieldPath field;

    FirstAggregateField(@Nonnull FieldPath field) {
      this.field = field;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != this.getClass()) {
        return false;
      }
      return field.equals(((FirstAggregateField) obj).field);
    }

    @Override
    public int hashCode() {
      return toString().hashCode();
    }

    @Override
    public String toString() {
      return "FIRST(" + field.toString() + ")";
    }
  }

  public static final class LastAggregateField extends AggregateField {

    @Nonnull private FieldPath field;

    LastAggregateField(@Nonnull FieldPath field) {
      this.field = field;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != this.getClass()) {
        return false;
      }
      return field.equals(((LastAggregateField) obj).field);
    }

    @Override
    public int hashCode() {
      return toString().hashCode();
    }

    @Override
    public String toString() {
      return "LAST(" + field.toString() + ")";
    }
  }
}
