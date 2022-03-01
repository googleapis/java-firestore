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
}
