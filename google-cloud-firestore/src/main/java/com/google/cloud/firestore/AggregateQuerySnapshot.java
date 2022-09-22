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

@InternalExtensionOnly
public class AggregateQuerySnapshot {

  @Nonnull private final AggregateQuery query;
  @Nonnull private final Timestamp readTime;
  private final long count;

  AggregateQuerySnapshot(@Nonnull AggregateQuery query, @Nonnull Timestamp readTime, long count) {
    this.query = query;
    this.readTime = readTime;
    this.count = count;
  }

  @Nonnull
  public AggregateQuery getQuery() {
    return query;
  }

  @Nonnull
  public Timestamp getReadTime() {
    return readTime;
  }

  public long getCount() {
    return count;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    } else if (!(obj instanceof AggregateQuerySnapshot)) {
      return false;
    }

    AggregateQuerySnapshot other = (AggregateQuerySnapshot) obj;
    return query.equals(other.query) && readTime.equals(other.readTime) && count == other.count;
  }

  @Override
  public int hashCode() {
    return Objects.hash(query, readTime, count);
  }
}
