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

import com.google.firestore.v1.Value;

/**
 * Parent to all stages.
 *
 * This class is package private to prevent public access to these methods. Methods in this class
 * support internal polymorphic processing, that would otherwise require conditional processing
 * based on type. This should eliminate `instanceof` usage with respect to `Stage` implementations.
 */
abstract class AbstractStage implements Stage {
  abstract Value getProtoArgs();
}
