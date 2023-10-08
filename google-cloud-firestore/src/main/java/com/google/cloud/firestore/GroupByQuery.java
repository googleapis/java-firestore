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

import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.cloud.firestore.Query.Direction;
import java.util.concurrent.Executor;
import javax.annotation.Nonnull;

public interface GroupByQuery {

  @Nonnull
  Query getQuery();

  @Nonnull
  ApiFuture<GroupByQuerySnapshot> get();

  void stream(@Nonnull final ApiStreamObserver<GroupSnapshot> responseObserver);

  @Nonnull
  ListenerRegistration addSnapshotListener(@Nonnull EventListener<GroupByQuerySnapshot> listener);

  @Nonnull
  ListenerRegistration addSnapshotListener(
      @Nonnull Executor executor, @Nonnull EventListener<GroupByQuerySnapshot> listener);

  // Note: Specifying an empty list of aggregates to this method, or not invoking it at all, is
  // equivalent to an SQL "DISTINCT" operator.
  @Nonnull
  GroupByQuery aggregate(@Nonnull AggregateField... fields);

  @Nonnull
  GroupByQuery groupLimit(int maxGroups);

  // Question: Do we want to support group-by "limitToLast" queries? In the Query class this is
  // implemented entirely client side by issuing the requested query with inverted order-by. We
  // would need to verify at runtime that the underlying query has the correct order-by clause and
  // possibly invert first/last aggregations to maintain their expected semantics.
  @Nonnull
  GroupByQuery groupLimitToLast(int maxGroups);

  @Nonnull
  GroupByQuery groupOffset(long groupOffset);

  @Nonnull
  GroupByQuery groupStartAt(Object... fieldValues);

  @Nonnull
  GroupByQuery groupStartAt(@Nonnull GroupSnapshot snapshot);

  @Nonnull
  GroupByQuery groupStartAfter(Object... fieldValues);

  @Nonnull
  GroupByQuery groupStartAfter(@Nonnull GroupSnapshot snapshot);

  @Nonnull
  GroupByQuery groupEndAt(Object... fieldValues);

  @Nonnull
  GroupByQuery groupEndAt(@Nonnull GroupSnapshot snapshot);

  @Nonnull
  GroupByQuery groupEndBefore(Object... fieldValues);

  @Nonnull
  GroupByQuery groupEndBefore(@Nonnull GroupSnapshot snapshot);

  @Nonnull
  GroupByQuery groupOrderBy(@Nonnull String groupByField);

  @Nonnull
  GroupByQuery groupOrderBy(@Nonnull FieldPath groupByField);

  @Nonnull
  GroupByQuery groupOrderBy(@Nonnull AggregateField aggregateField);

  @Nonnull
  GroupByQuery groupOrderBy(@Nonnull String groupByField, @Nonnull Direction direction);

  @Nonnull
  GroupByQuery groupOrderBy(@Nonnull FieldPath groupByField, @Nonnull Direction direction);

  @Nonnull
  GroupByQuery groupOrderBy(@Nonnull AggregateField aggregateField, @Nonnull Direction direction);

  @Override
  int hashCode();

  @Override
  boolean equals(Object obj);
}
