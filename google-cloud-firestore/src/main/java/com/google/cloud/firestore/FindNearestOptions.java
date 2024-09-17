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

package com.google.cloud.firestore;

import javax.annotation.Nullable;

/**
 * Represents the options that are used to configure the use of OpenTelemetry for telemetry
 * collection in the Firestore SDK.
 */
public class FindNearestOptions {
  private final @Nullable FieldPath distanceResultField;

  private final @Nullable Double distanceThreshold;

  @Nullable
  public FieldPath getDistanceResultField() {
    return distanceResultField;
  }

  @Nullable
  public Double getDistanceThreshold() {
    return distanceThreshold;
  }

  FindNearestOptions(FindNearestOptions.Builder builder) {
    this.distanceThreshold = builder.distanceThreshold;
    this.distanceResultField = builder.distanceResultField;
  }

  public static FindNearestOptions.Builder newBuilder() {
    return new FindNearestOptions.Builder();
  }

  public static final class Builder {
    private @Nullable FieldPath distanceResultField;

    private @Nullable Double distanceThreshold;

    private Builder() {
      distanceThreshold = null;
      distanceResultField = null;
    }

    private Builder(FindNearestOptions options) {
      this.distanceThreshold = options.distanceThreshold;
      this.distanceResultField = options.distanceResultField;
    }

    public Builder setDistanceResultField(String fieldPath) {
      this.distanceResultField = FieldPath.fromDotSeparatedString(fieldPath);
      return this;
    }

    public Builder setDistanceResultField(@Nullable FieldPath fieldPath) {
      this.distanceResultField = fieldPath;
      return this;
    }

    public Builder setDistanceThreshold(@Nullable Double distanceThreshold) {
      this.distanceThreshold = distanceThreshold;
      return this;
    }

    public FindNearestOptions build() {
      return new FindNearestOptions(this);
    }
  }
}
