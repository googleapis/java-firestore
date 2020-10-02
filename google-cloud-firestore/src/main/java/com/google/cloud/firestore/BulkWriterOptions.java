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
final class BulkWriterOptions {
  static final double DEFAULT_UNSET_VALUE = 1.1;
  private final boolean throttling;
  private double initialOpsPerSecond = DEFAULT_UNSET_VALUE;
  private double maxOpsPerSecond = DEFAULT_UNSET_VALUE;

  BulkWriterOptions(boolean enableThrottling) {
    this.throttling = enableThrottling;
  }

  private BulkWriterOptions(double initialOpsPerSecond, double maxOpsPerSecond) {
    this.throttling = true;
    this.initialOpsPerSecond = initialOpsPerSecond;
    this.maxOpsPerSecond = maxOpsPerSecond;
  }

  boolean isThrottlingEnabled() {
    return throttling;
  }

  double getInitialOpsPerSecond() {
    return initialOpsPerSecond;
  }

  double getMaxOpsPerSecond() {
    return maxOpsPerSecond;
  }

  /**
   * An options object that will disable throttling in the created BulkWriter.
   *
   * @return The BulkWriterOptions object.
   */
  @Nonnull
  public static BulkWriterOptions withThrottlingDisabled() {
    return new BulkWriterOptions(false);
  }

  /**
   * An options object that will enable throttling in the created BulkWriter with the provided
   * starting rate.
   *
   * @param initialOpsPerSecond The initial maximum number of operations per second allowed by the
   *     throttler.
   * @return The BulkWriterOptions object.
   */
  @Nonnull
  public static BulkWriterOptions withInitialOpsPerSecondThrottling(int initialOpsPerSecond) {
    return new BulkWriterOptions(initialOpsPerSecond, DEFAULT_UNSET_VALUE);
  }

  /**
   * An options object that will enable throttling in the created BulkWriter with the provided
   * maximum rate.
   *
   * @param maxOpsPerSecond The maximum number of operations per second allowed by the throttler.
   *     The throttler's allowed operations per second does not ramp up past the specified
   *     operations per second.
   * @return The BulkWriterOptions object.
   */
  @Nonnull
  public static BulkWriterOptions withMaxOpsPerSecondThrottling(int maxOpsPerSecond) {
    return new BulkWriterOptions(DEFAULT_UNSET_VALUE, maxOpsPerSecond);
  }

  /**
   * An options object that will enable throttling in the created BulkWriter with the provided
   * starting and maximum rate.
   *
   * @param initialOpsPerSecond The initial maximum number of operations per second allowed by the
   *     throttler.
   * @param maxOpsPerSecond The maximum number of operations per second allowed by the throttler.
   *     The throttler's allowed operations per second does not ramp up past the specified
   *     operations per second.
   * @return The BulkWriterOptions object.
   */
  @Nonnull
  public static BulkWriterOptions withCustomThrottling(
      int initialOpsPerSecond, int maxOpsPerSecond) {
    return new BulkWriterOptions(initialOpsPerSecond, maxOpsPerSecond);
  }
}
