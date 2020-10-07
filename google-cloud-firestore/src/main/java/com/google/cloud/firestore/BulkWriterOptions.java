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

import com.google.auto.value.AutoValue;

/** Options used to configure request throttling in BulkWriter. */
@AutoValue
abstract class BulkWriterOptions {
  /**
   * Return whether throttling is enabled.
   *
   * @return Whether throttling is enabled.
   */
  abstract boolean getThrottlingEnabled();

  /**
   * Returns the initial maximum number of operations per second allowed by the throttler.
   *
   * @return The initial maximum number of operations per second allowed by the throttler.
   */
  abstract double getInitialOpsPerSecond();

  /**
   * Returns the maximum number of operations per second allowed by the throttler.
   *
   * <p>The throttler's allowed operations per second does not ramp up past the specified operations
   * per second.
   *
   * @return The maximum number of operations per second allowed by the throttler.
   */
  abstract double getMaxOpsPerSecond();

  static Builder builder() {
    return new AutoValue_BulkWriterOptions.Builder()
        .setMaxOpsPerSecond(Double.NaN)
        .setInitialOpsPerSecond(Double.NaN)
        .setThrottlingEnabled(true);
  }

  abstract Builder toBuilder();

  @AutoValue.Builder
  abstract static class Builder {
    /**
     * Sets whether throttling should be enabled. By default, throttling is enabled.
     *
     * @param enabled Whether throttling should be enabled.
     */
    abstract Builder setThrottlingEnabled(boolean enabled);

    /**
     * Set the initial maximum number of operations per second allowed by the throttler.
     *
     * @param initialOpsPerSecond The initial maximum number of operations per second allowed by the
     *     throttler.
     */
    abstract Builder setInitialOpsPerSecond(double initialOpsPerSecond);

    /**
     * Set the maximum number of operations per second allowed by the throttler.
     *
     * @param maxOpsPerSecond The maximum number of operations per second allowed by the throttler.
     *     The throttler's allowed operations per second does not ramp up past the specified
     *     operations per second.
     */
    abstract Builder setMaxOpsPerSecond(double maxOpsPerSecond);

    abstract BulkWriterOptions build();
  }
}
