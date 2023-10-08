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
import java.util.Objects;
import javax.annotation.Nonnull;

/** The results of executing an {@link AggregateQuery}. */
@InternalExtensionOnly
public class AggregateQuerySnapshot extends AggregateSnapshot {

  @Nonnull private final AggregateQuery query;

  AggregateQuerySnapshot(@Nonnull AggregateQuery query, @Nonnull Timestamp readTime, long count) {
    super(readTime, count);
    this.query = query;
  }

  /** Returns the query that was executed to produce this result. */
  @Nonnull
  public AggregateQuery getQuery() {
    return query;
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
    if (!super.equals(object)) {
      return false;
    }
    if (!(object instanceof AggregateQuerySnapshot)) {
      return false;
    }
    return query.equals(((AggregateQuerySnapshot) object).query);
  }

  /**
   * Calculates and returns the hash code for this object.
   *
   * @return the hash code for this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), query);
  }
}
