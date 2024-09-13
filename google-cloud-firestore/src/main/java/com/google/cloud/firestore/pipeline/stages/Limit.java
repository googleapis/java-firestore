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

import com.google.api.core.InternalApi;

@InternalApi
public final class Limit implements Stage {

  private static final String name = "limit";
  private int limit;

  @InternalApi
  public Limit(int limit) {
    this.limit = limit;
  }

  @InternalApi
  public int getLimit() {
    return limit;
  }

  @Override
  @InternalApi
  public String getName() {
    return name;
  }
}
