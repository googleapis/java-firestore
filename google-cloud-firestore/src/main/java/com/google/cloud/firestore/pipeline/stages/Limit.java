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

import com.google.api.core.InternalApi;
import com.google.firestore.v1.Pipeline;

@InternalApi
public final class Limit extends Stage {

  private static final String name = "limit";
  private final int limit;

  @InternalApi
  public Limit(int limit) {
    this.limit = limit;
  }

  @Override
  Pipeline.Stage toStageProto() {
    return Pipeline.Stage.newBuilder().setName(name).addArgs(encodeValue(limit)).build();
  }
}
