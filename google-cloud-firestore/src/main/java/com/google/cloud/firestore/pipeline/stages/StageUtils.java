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
public final class StageUtils {
  @InternalApi
  public static com.google.firestore.v1.Pipeline.Stage toStageProto(Stage stage) {

    if (stage instanceof AbstractStage) {
      AbstractStage abstractStage = (AbstractStage) stage;
      return ((AbstractStage) stage).toStageProto();
    } else {
      // Handle the else case appropriately (e.g., throw an exception)
      throw new IllegalArgumentException("Unknown stage type: " + stage.getClass());
    }
  }
}
