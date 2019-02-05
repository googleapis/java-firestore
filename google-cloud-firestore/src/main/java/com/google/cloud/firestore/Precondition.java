/*
 * Copyright 2017 Google LLC
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
import java.util.Objects;
import javax.annotation.Nonnull;

/** Preconditions that can be used to restrict update() calls. */
public final class Precondition {

  /** An empty Precondition that adds no enforcements */
  public static final Precondition NONE = new Precondition(null, null);

  private final Boolean exists;
  private final Timestamp updateTime;

  private Precondition(Boolean exists, Timestamp updateTime) {
    this.exists = exists;
    this.updateTime = updateTime;
  }

  /**
   * Creates a Precondition that enforces that a document exists.
   *
   * @param exists Whether the document should exist.
   * @return A new Precondition
   */
  // TODO: Make public once backend supports verify.
  @Nonnull
  static Precondition exists(Boolean exists) {
    return new Precondition(exists, null);
  }

  /**
   * Creates a Precondition that enforces that the existing document was written at the specified
   * time.
   *
   * @param updateTime The write time to enforce on the existing document.
   * @return A new Precondition
   */
  @Nonnull
  public static Precondition updatedAt(Timestamp updateTime) {
    return new Precondition(null, updateTime);
  }

  boolean isEmpty() {
    return exists == null && updateTime == null;
  }

  boolean hasExists() {
    return exists != null;
  }

  com.google.firestore.v1.Precondition toPb() {
    com.google.firestore.v1.Precondition.Builder precondition =
        com.google.firestore.v1.Precondition.newBuilder();

    if (exists != null) {
      precondition.setExists(exists);
    }

    if (updateTime != null) {
      precondition.setUpdateTime(updateTime.toProto());
    }

    return precondition.build();
  }

  /**
   * Returns true if this Precondition is equal to the provided object.
   *
   * @param obj The object to compare against.
   * @return Whether this Precondition is equal to the provided object.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Precondition that = (Precondition) obj;
    return Objects.equals(exists, that.exists) && Objects.equals(updateTime, that.updateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(exists, updateTime);
  }
}
