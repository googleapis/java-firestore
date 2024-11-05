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
import com.google.firestore.v1.Pipeline;

@InternalApi
public final class Database extends Stage {
  private static final String name = "database";

  @InternalApi
  public Database() {}

  @Override
  Pipeline.Stage toStageProto() {
    return Pipeline.Stage.newBuilder().setName(name).build();
  }
}
