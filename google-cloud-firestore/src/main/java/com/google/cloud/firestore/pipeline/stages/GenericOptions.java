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

package com.google.cloud.firestore.pipeline.stages;

import static com.google.cloud.firestore.PipelineUtils.encodeValue;

import com.google.cloud.firestore.pipeline.expressions.Field;

public final class GenericOptions extends AbstractOptions<GenericOptions> {

  public GenericOptions() {
    this(InternalOptions.EMPTY);
  }

  public static GenericOptions of(String key, String value) {
    return new GenericOptions(InternalOptions.of(key, encodeValue(value)));
  }

  public static GenericOptions of(String key, boolean value) {
    return new GenericOptions(InternalOptions.of(key, encodeValue(value)));
  }

  public static GenericOptions of(String key, long value) {
    return new GenericOptions(InternalOptions.of(key, encodeValue(value)));
  }

  public static GenericOptions of(String key, double value) {
    return new GenericOptions(InternalOptions.of(key, encodeValue(value)));
  }

  public static GenericOptions of(String key, Field value) {
    return new GenericOptions(InternalOptions.of(key, value.toProto()));
  }

  GenericOptions(InternalOptions options) {
    super(options);
  }

  @Override
  protected GenericOptions self(InternalOptions options) {
    return new GenericOptions(options);
  }
}
