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

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.firestore.v1.DatabaseRootName;
import java.util.Arrays;
import java.util.Comparator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** An immutable representation of a Firestore path to a Document or Collection. */
@AutoValue
abstract class ResourcePath extends BasePath<ResourcePath> {

  /**
   * Creates a new Path.
   *
   * @param databaseName The Firestore database name.
   * @param segments The segments of the path relative to the root collections.
   */
  static ResourcePath create(DatabaseRootName databaseName, ImmutableList<String> segments) {
    return new AutoValue_ResourcePath(segments, databaseName);
  }

  /**
   * Creates a new Path to the root.
   *
   * @param databaseName The Firestore database name.
   */
  static ResourcePath create(DatabaseRootName databaseName) {
    return new AutoValue_ResourcePath(ImmutableList.of(), databaseName);
  }

  /**
   * Create a new Path from its string representation.
   *
   * @param resourceName The Firestore resource name of this path.
   */
  static ResourcePath create(String resourceName) {
    String[] parts = resourceName.split("/");

    if (parts.length >= 5 && parts[0].equals("projects") && parts[2].equals("databases")) {
      String[] path = Arrays.copyOfRange(parts, 5, parts.length);
      return create(
          DatabaseRootName.of(parts[1], parts[3]),
          ImmutableList.<String>builder().add(path).build());
    }

    return create(DatabaseRootName.parse(resourceName));
  }

  /**
   * Returns the database name.
   *
   * @return The Firestore database name.
   */
  abstract DatabaseRootName getDatabaseName();

  /** Returns whether this path points to a document. */
  boolean isDocument() {
    int size = getSegments().size();
    return size > 0 && size % 2 == 0;
  }

  /** Returns whether this path points to a collection. */
  boolean isCollection() {
    return getSegments().size() % 2 == 1;
  }

  /**
   * The Path's id (last component).
   *
   * @return The last component of the Path or null if the path points to the root.
   */
  @Nullable
  String getId() {
    ImmutableList<String> parts = getSegments();

    if (!parts.isEmpty()) {
      return parts.get(parts.size() - 1);
    } else {
      return null;
    }
  }

  /**
   * The Path's name (the location relative to the root of the database).
   *
   * @return The resource path relative to the root of the database.
   */
  String getPath() {
    return String.join("/", getSegments());
  }

  /**
   * String representation as expected by the Firestore API.
   *
   * @return The formatted name of the resource.
   */
  String getName() {
    String path = getPath();
    if (path.isEmpty()) {
      return getDatabaseName() + "/documents";
    } else {
      return getDatabaseName() + "/documents/" + path;
    }
  }

  /**
   * Compare the current path against another ResourcePath object.
   *
   * @param other The path to compare to.
   * @return -1 if current < other, 1 if current > other, 0 if equal
   */
  @Override
  public int compareTo(@Nonnull ResourcePath other) {
    DatabaseRootName thisDatabaseName = this.getDatabaseName();
    DatabaseRootName otherDatabaseName = other.getDatabaseName();

    if (thisDatabaseName != otherDatabaseName) {
      int cmp = thisDatabaseName.getProject().compareTo(otherDatabaseName.getProject());

      if (cmp != 0) {
        return cmp;
      }

      cmp = thisDatabaseName.getDatabase().compareTo(otherDatabaseName.getDatabase());

      if (cmp != 0) {
        return cmp;
      }
    }

    return super.compareTo(other);
  }

  /**
   * Pops the last segment from this `ResourcePath` and returns a newly constructed `ResourcePath`
   * without the last segment. This does not change the ResourcePath, since `ResourcePath` is
   * immutable.
   *
   * @return The newly created Path.
   */
  ResourcePath popLast() {
    ImmutableList<String> segments = getSegments();
    return createPathWithSegments(segments.subList(0, segments.size() - 1));
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  String[] splitChildPath(String name) {
    return name.split("/");
  }

  @Override
  ResourcePath createPathWithSegments(ImmutableList<String> segments) {
    return create(getDatabaseName(), segments);
  }

  public static Comparator<ResourcePath> comparator() {
    return Comparator.naturalOrder();
  }
}
