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

import com.google.api.core.InternalExtensionOnly;
import com.google.cloud.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@InternalExtensionOnly
public class AggregateSnapshot {

  @Nonnull private final Timestamp readTime;
  private final long count;

  AggregateSnapshot(@Nonnull Timestamp readTime, long count) {
    this.readTime = readTime;
    this.count = count;
  }

  @Nonnull
  public Timestamp getReadTime() {
    return readTime;
  }

  @Nonnull
  public Map<AggregateField, Object> getAggregations() {
    throw new RuntimeException("not implemented");
  }

  public boolean contains(@Nonnull AggregateField field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Object get(@Nonnull AggregateField field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public <T> T get(@Nonnull AggregateField field, @Nonnull Class<T> valueType) {
    throw new RuntimeException("not implemented");
  }

  /** Returns the number of documents in the result set of the underlying query. */
  public long getCount() {
    return count;
  }

  // Overload get() specifically for COUNT since it has a well-defined type (i.e. long).
  public long get(@Nonnull AggregateField.CountAggregateField field) {
    throw new RuntimeException("not implemented");
  }

  // Overload get() specifically for SUM since it has a well-defined type (i.e. double).
  @Nullable
  public Double get(@Nonnull AggregateField.SumAggregateField field) {
    throw new RuntimeException("not implemented");
  }

  // Overload get() specifically for AVERAGE since it has a well-defined type (i.e. double).
  @Nullable
  public Double get(@Nonnull AggregateField.AverageAggregateField field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Boolean getBoolean(@Nonnull AggregateField field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Double getDouble(@Nonnull AggregateField field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public String getString(@Nonnull AggregateField field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Long getLong(@Nonnull AggregateField field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Date getDate(@Nonnull AggregateField field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Timestamp getTimestamp(@Nonnull AggregateField field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Blob getBlob(@Nonnull AggregateField field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public GeoPoint getGeoPoint(@Nonnull AggregateField field) {
    throw new RuntimeException("not implemented");
  }

  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    } else if (!(object instanceof AggregateSnapshot)) {
      return false;
    }

    AggregateSnapshot other = (AggregateSnapshot) object;

    // Don't check `readTime`, because `DocumentSnapshot.equals()` doesn't either.
    return count == other.count;
  }

  /**
   * Calculates and returns the hash code for this object.
   *
   * @return the hash code for this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(count);
  }
}
