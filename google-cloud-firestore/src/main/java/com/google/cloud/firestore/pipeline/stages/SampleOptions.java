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

public class SampleOptions extends AbstractOptions<SampleOptions> {

  public static SampleOptions DEFAULT = new SampleOptions(InternalOptions.EMPTY);

  public SampleOptions(InternalOptions options) {
    super(options);
  }

  @Override
  SampleOptions self(InternalOptions options) {
    return new SampleOptions(options);
  }
}
