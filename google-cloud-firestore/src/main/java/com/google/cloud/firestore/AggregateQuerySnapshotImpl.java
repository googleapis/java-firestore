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

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.AggregateField.CountAggregateField;
import com.google.firestore.v1.AggregationResult;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class AggregateQuerySnapshotImpl implements AggregateQuerySnapshot {

  @Nonnull
  private final AggregateQuery aggregateQuery;
  @Nonnull
  private final Timestamp readTime;
  @Nonnull
  private final AggregationResult aggregationResult;

  AggregateQuerySnapshotImpl(@Nonnull AggregateQuery aggregateQuery,
      @Nonnull Timestamp readTime, @Nonnull AggregationResult aggregationResult) {
    this.aggregateQuery = aggregateQuery;
    this.readTime = readTime;
    this.aggregationResult = aggregationResult;
  }

  @Nonnull
  @Override
  public AggregateQuery getQuery() {
    return aggregateQuery;
  }

  @Nonnull
  @Override
  public Timestamp getReadTime() {
    return readTime;
  }

  @Nullable
  @Override
  public Long get(@Nonnull CountAggregateField field) {
    return aggregationResult.getAggregateFieldsMap().get("agg_alias_count").getIntegerValue();
  }

}
