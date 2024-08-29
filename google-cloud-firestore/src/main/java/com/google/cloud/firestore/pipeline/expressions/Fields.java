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

package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a selection of multiple {@link Field} instances.
 *
 * <p>This class is used to conveniently specify multiple fields for operations like selecting
 * fields in a pipeline.
 *
 * <p>Example:
 *
 * <pre>{@code
 * // Select the 'name', 'email', and 'age' fields
 * Fields selectedFields = Fields.of("name", "email", "age");
 *
 * firestore.pipeline().collection("users")
 *     .select(selectedFields)
 *     .execute();
 * }</pre>
 */
@BetaApi
public final class Fields implements Expr, Selectable {
  private final List<Field> fields;

  private Fields(List<Field> fs) {
    this.fields = fs;
  }

  /**
   * Creates a {@code Fields} instance containing the specified fields.
   *
   * @param f1 The first field to include.
   * @param f Additional fields to include.
   * @return A new {@code Fields} instance containing the specified fields.
   */
  @BetaApi
  public static Fields of(String f1, String... f) {
    List<Field> fields = Arrays.stream(f).map(Field::of).collect(Collectors.toList());
    fields.add(0, Field.of(f1)); // Add f1 at the beginning
    return new Fields(fields);
  }

  /**
   * Creates a {@code Fields} instance representing a selection of all fields.
   *
   * <p>This is equivalent to not specifying any fields in a select operation, resulting in all
   * fields being included in the output.
   *
   * @return A new {@code Fields} instance representing all fields.
   */
  @BetaApi
  public static Fields ofAll() {
    return new Fields(Collections.singletonList(Field.of("")));
  }

  /**
   * Returns the list of {@link Field} instances contained in this {@code Fields} object.
   *
   * @return The list of fields.
   */
  @InternalApi
  public List<Field> getFields() {
    return fields;
  }
}
