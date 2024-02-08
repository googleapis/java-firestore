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

import com.google.api.core.ApiFuture;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Transaction {

  /**
   * User callback that takes a Firestore Transaction.
   *
   * @param <T> The result type of the user callback.
   */
  public interface Function<T> {

    T updateCallback(Transaction transaction) throws Exception;
  }

  /**
   * User callback that takes a Firestore Async Transaction.
   *
   * @param <T> The result type of the user async callback.
   */
  public interface AsyncFunction<T> {

    ApiFuture<T> updateCallback(Transaction transaction);
  }

  @Nonnull
  ApiFuture<DocumentSnapshot> get(@Nonnull DocumentReference documentRef);

  @Nonnull
  ApiFuture<List<DocumentSnapshot>> getAll(@Nonnull DocumentReference... documentReferences);

  @Nonnull
  ApiFuture<List<DocumentSnapshot>> getAll(
      @Nonnull DocumentReference[] documentReferences, @Nullable FieldMask fieldMask);

  @Nonnull
  ApiFuture<QuerySnapshot> get(@Nonnull Query query);

  @Nonnull
  ApiFuture<AggregateQuerySnapshot> get(@Nonnull AggregateQuery query);

  @Nonnull
  Transaction create(
      @Nonnull DocumentReference documentReference, @Nonnull Map<String, Object> fields);

  @Nonnull
  Transaction create(@Nonnull DocumentReference documentReference, @Nonnull Object pojo);

  @Nonnull
  Transaction set(
      @Nonnull DocumentReference documentReference, @Nonnull Map<String, Object> fields);

  @Nonnull
  Transaction set(
      @Nonnull DocumentReference documentReference,
      @Nonnull Map<String, Object> fields,
      @Nonnull SetOptions options);

  @Nonnull
  Transaction set(@Nonnull DocumentReference documentReference, @Nonnull Object pojo);

  @Nonnull
  Transaction set(
      @Nonnull DocumentReference documentReference,
      @Nonnull Object pojo,
      @Nonnull SetOptions options);

  @Nonnull
  Transaction update(
      @Nonnull DocumentReference documentReference, @Nonnull Map<String, Object> fields);

  @Nonnull
  Transaction update(
      @Nonnull DocumentReference documentReference,
      @Nonnull Map<String, Object> fields,
      Precondition precondition);

  @Nonnull
  Transaction update(
      @Nonnull DocumentReference documentReference,
      @Nonnull String field,
      @Nullable Object value,
      Object... moreFieldsAndValues);

  @Nonnull
  Transaction update(
      @Nonnull DocumentReference documentReference,
      @Nonnull FieldPath fieldPath,
      @Nullable Object value,
      Object... moreFieldsAndValues);

  @Nonnull
  Transaction update(
      @Nonnull DocumentReference documentReference,
      @Nonnull Precondition precondition,
      @Nonnull String field,
      @Nullable Object value,
      Object... moreFieldsAndValues);

  @Nonnull
  Transaction update(
      @Nonnull DocumentReference documentReference,
      @Nonnull Precondition precondition,
      @Nonnull FieldPath fieldPath,
      @Nullable Object value,
      Object... moreFieldsAndValues);

  @Nonnull
  Transaction delete(
      @Nonnull DocumentReference documentReference, @Nonnull Precondition precondition);

  @Nonnull
  Transaction delete(@Nonnull DocumentReference documentReference);
}
