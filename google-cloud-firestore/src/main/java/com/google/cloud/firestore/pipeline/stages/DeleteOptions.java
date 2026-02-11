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

/** Options for a Delete pipeline stage. */
@BetaApi
public class DeleteOptions extends WriteOptions<DeleteOptions> {

  /** Creates a new, empty `DeleteOptions` object. */
  public DeleteOptions() {
    super(InternalOptions.EMPTY);
  }

  DeleteOptions(InternalOptions options) {
    super(options);
  }

  @Override
  DeleteOptions self(InternalOptions options) {
    return new DeleteOptions(options);
  }
}
