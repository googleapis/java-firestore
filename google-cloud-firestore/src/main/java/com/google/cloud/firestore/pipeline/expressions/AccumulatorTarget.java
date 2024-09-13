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

@BetaApi
public final class AccumulatorTarget implements Selectable {
  private final Accumulator accumulator;
  private final String fieldName;

  @InternalApi
  AccumulatorTarget(Accumulator accumulator, String fieldName, boolean distinct) {
    this.accumulator = accumulator;
    this.fieldName = fieldName;
  }

  // Getters (for accumulator, fieldName, and distinct)
  @InternalApi
  public Accumulator getAccumulator() {
    return accumulator;
  }

  @InternalApi
  public String getFieldName() {
    return fieldName;
  }
}
