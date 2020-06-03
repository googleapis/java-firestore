/*
 * Copyright 2020 Google LLC
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

package com.google.cloud.firestore;

import javax.annotation.Nonnull;

/** Options used to disable request throttling in BulkWriter. */
public class BulkWriterOptions {
  private final boolean disableThrottling;

  BulkWriterOptions(boolean disableThrottling) {
    this.disableThrottling = disableThrottling;
  }

  public boolean getDisableThrottling() {
    return disableThrottling;
  }

  /**
   * Create a set of options to disable or enable throttling.
   *
   * @param disableThrottling Whether to disable throttling.
   * @return The BulkWriterOptions object.
   */
  @Nonnull
  public static BulkWriterOptions create(boolean disableThrottling) {
    return new BulkWriterOptions(disableThrottling);
  }
}
