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

import com.google.api.core.BetaApi;
import com.google.cloud.firestore.pipeline.expressions.Field;
import javax.annotation.Nullable;

@BetaApi
public class FindNearestOptions {

  @Nullable private final Long limit;

  @Nullable private final Field distanceField;

  private FindNearestOptions(Long limit, Field distanceField) {
    this.limit = limit;
    this.distanceField = distanceField;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Nullable
  public Long getLimit() {
    return limit;
  }

  @Nullable
  public Field getDistanceField() {
    return distanceField;
  }

  public static class Builder {
    @Nullable private Long limit;
    @Nullable private Field distanceField;

    public Builder limit(Long limit) {
      this.limit = limit;
      return this;
    }

    public Builder distanceField(Field distanceField) {
      this.distanceField = distanceField;
      return this;
    }

    public FindNearestOptions build() {
      return new FindNearestOptions(limit, distanceField);
    }
  }
}
