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
import com.google.firestore.v1.Value;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** The results of executing an {@link AggregateQuery}. */
@InternalExtensionOnly
public class AggregateQuerySnapshot {

  @Nonnull private final AggregateQuery query;
  @Nonnull private final Timestamp readTime;
  private final Map<String, Value> data;

  AggregateQuerySnapshot(
      @Nonnull AggregateQuery query, @Nonnull Timestamp readTime, Map<String, Value> data) {
    this.query = query;
    this.readTime = readTime;
    this.data = data;
  }

  /** Returns the query that was executed to produce this result. */
  @Nonnull
  public AggregateQuery getQuery() {
    return query;
  }

  /** Returns the time at which this snapshot was read. */
  @Nonnull
  public Timestamp getReadTime() {
    return readTime;
  }

  /** Returns the number of documents in the result set of the underlying query. */
  public long getCount() {
    AggregateField countField = AggregateField.count();
    Object value = get(countField);
    if (value == null) {
      throw new IllegalArgumentException(
          "RunAggregationQueryResponse alias " + countField.getAlias() + " is null");
    } else if (!(value instanceof Long)) {
      throw new IllegalArgumentException(
          "RunAggregationQueryResponse alias "
              + countField.getAlias()
              + " has incorrect type: "
              + value.getClass().getName());
    }
    return (Long) value;
  }

  // TODO(ehsan): add documentation.
  // Returns a result from the server without loss of precision. No coercion of data types.
  // Throws java.lang.RuntimeException if the `aggregateField` was not requested
  //   when calling `query.aggregate(...)`
  @Nullable
  public Object get(@Nonnull AggregateField aggregateField) {
    // TODO(ehsan): Web returns 'undefined' for such cases. Double check that other SDKs should
    // throw.
    if (!data.containsKey(aggregateField.getAlias())) {
      throw new IllegalArgumentException(
          "'"
              + aggregateField.getOperator()
              + "("
              + aggregateField.getFieldPath()
              + ")"
              + "' was not requested in the aggregation query.");
    }
    Value value = data.get(aggregateField.getAlias());
    if (value.hasNullValue()) {
      return null;
    } else if (value.hasDoubleValue()) {
      return value.getDoubleValue();
    } else if (value.hasIntegerValue()) {
      return value.getIntegerValue();
    } else {
      throw new IllegalStateException("Found aggregation result that is not an integer nor double");
    }
  }

  // TODO(ehsan): add public documentation.
  // Special overload for "average" because it always evaluates to a double.
  // Throws RuntimeException if the `aggregateField` was not requested
  //   when calling `query.aggregate(...)`
  @Nullable
  public Double get(@Nonnull AggregateField.AverageAggregateField averageAggregateField) {
    return (Double) get((AggregateField) averageAggregateField);
  }

  // TODO(ehsan): add public documentation.
  // Behaves the same as DocumentSnapshot.getDouble(field) with respect to
  // retrieving a value that is not a floating point number. Coerces all numeric values
  // and throws a RuntimeException if the result of the aggregate is non-numeric.
  //
  // Numeric coercion (matches existing behavior in DocumentSnapshot):
  //   * If the result is a long, this may result in a loss of precision in coercion to double.
  @Nullable
  public Double getDouble(@Nonnull AggregateField aggregateField) {
    Number result = (Number) get(aggregateField);
    return result == null ? null : result.doubleValue();
  }

  // TODO(ehsan): add public documentation.
  // Behaves the same as DocumentSnapshot.getLong(field) with respect to
  // retrieving a value that is not a floating point number. Coerces numeric values
  // and throws on other types.
  //
  // Numeric coercion (matches existing behavior in DocumentSnapshot):
  //   * Result is NaN - returns 0L
  //   * Result is +/- infinity - returns Long.MAX_VALUE/MIN_VALUE
  //   * Result is greater than Long.MAX_VALUE/MIN_VALUE - returns Long.MAX_VALUE/MIN_VALUE
  @Nullable
  public Long getLong(@Nonnull AggregateField aggregateField) {
    Number result = (Number) get(aggregateField);
    return result == null ? null : result.longValue();
  }

  /**
   * Compares this object with the given object for equality.
   *
   * <p>This object is considered "equal" to the other object if and only if all of the following
   * conditions are satisfied:
   *
   * <ol>
   *   <li>{@code object} is a non-null instance of {@link AggregateQuerySnapshot}.
   *   <li>The {@link AggregateQuery} of {@code object} compares equal to that of this object.
   *   <li>{@code object} has the same results as this object.
   * </ol>
   *
   * @param object The object to compare to this object for equality.
   * @return {@code true} if this object is "equal" to the given object, as defined above, or {@code
   *     false} otherwise.
   */
  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    } else if (!(object instanceof AggregateQuerySnapshot)) {
      return false;
    }

    AggregateQuerySnapshot other = (AggregateQuerySnapshot) object;

    // Don't check `readTime`, because `DocumentSnapshot.equals()` doesn't either.
    // TODO(ehsan): Why do we not compare read time?
    // two aggregations performed on the same collection at different times are different snapshots.
    return query.equals(other.query) && data.equals(other.data);
  }

  /**
   * Calculates and returns the hash code for this object.
   *
   * @return the hash code for this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(query, data);
  }
}
