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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@InternalExtensionOnly
public class GroupSnapshot extends AggregateSnapshot {

  GroupSnapshot(@Nonnull Timestamp readTime, long count) {
    super(readTime, count);
  }

  @Nonnull
  public Map<FieldPath, Object> getFields() {
    throw new RuntimeException("not implemented");
  }

  public boolean contains(@Nonnull String field) {
    throw new RuntimeException("not implemented");
  }

  public boolean contains(@Nonnull FieldPath field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Object get(@Nonnull String field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public <T> T get(@Nonnull String field, @Nonnull Class<T> valueType) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Object get(@Nonnull FieldPath field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public <T> T get(@Nonnull FieldPath field, @Nonnull Class<T> valueType) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Boolean getBoolean(@Nonnull String field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Boolean getBoolean(@Nonnull FieldPath field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Double getDouble(@Nonnull String field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Double getDouble(@Nonnull FieldPath field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public String getString(@Nonnull String field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public String getString(@Nonnull FieldPath field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Long getLong(@Nonnull String field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Long getLong(@Nonnull FieldPath field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Date getDate(@Nonnull String field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Date getDate(@Nonnull FieldPath field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Timestamp getTimestamp(@Nonnull String field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Timestamp getTimestamp(@Nonnull FieldPath field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Blob getBlob(@Nonnull String field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public Blob getBlob(@Nonnull FieldPath field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public GeoPoint getGeoPoint(@Nonnull String field) {
    throw new RuntimeException("not implemented");
  }

  @Nullable
  public GeoPoint getGeoPoint(@Nonnull FieldPath field) {
    throw new RuntimeException("not implemented");
  }
}
