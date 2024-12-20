/*
 * Copyright 2024 Google LLC
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

import com.google.firestore.v1.MapValue;
import java.io.Serializable;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a vector in Firestore documents. Create an instance with {@link FieldValue#vector}.
 */
public final class VectorValue implements Serializable {
  private final double[] values;

  VectorValue(@Nullable double[] values) {
    if (values == null) this.values = new double[] {};
    else this.values = values.clone();
  }

  /**
   * Returns a representation of the vector as an array of doubles.
   *
   * @return A representation of the vector as an array of doubles
   */
  @Nonnull
  public double[] toArray() {
    return this.values.clone();
  }

  /**
   * Returns true if this VectorValue is equal to the provided object.
   *
   * @param obj The object to compare against.
   * @return Whether this VectorValue is equal to the provided object.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    VectorValue otherArray = (VectorValue) obj;
    return Arrays.equals(this.values, otherArray.values);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(values);
  }

  MapValue toProto() {
    return UserDataConverter.encodeVector(this.values);
  }

  /**
   * Returns the number of dimensions of the vector. Note: package private until API review is done.
   */
  int size() {
    return this.values.length;
  }

  @Override
  public String toString() {
    return String.format("VectorValue{values=%s}", Arrays.toString(values));
  }
}
