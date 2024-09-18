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

import com.google.api.core.BetaApi;
import io.opentelemetry.api.OpenTelemetry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents the options that are used to configure the use of OpenTelemetry for telemetry
 * collection in the Firestore SDK.
 */
@BetaApi
public class FirestoreOpenTelemetryOptions {
  private final boolean tracingEnabled;
  private final boolean metricsEnabled;
  private final @Nullable OpenTelemetry customOpenTelemetry;

  FirestoreOpenTelemetryOptions(Builder builder) {
    this.tracingEnabled = builder.tracingEnabled;
    this.metricsEnabled = builder.metricsEnabled;
    this.customOpenTelemetry = builder.customOpenTelemetry;
  }

  public boolean isTracingEnabled() {
    return tracingEnabled;
  }

  public boolean isMetricsEnabled() {
    return metricsEnabled;
  }

  public OpenTelemetry getOpenTelemetry() {
    return customOpenTelemetry;
  }

  @Nonnull
  public FirestoreOpenTelemetryOptions.Builder toBuilder() {
    return new FirestoreOpenTelemetryOptions.Builder(this);
  }

  @Nonnull
  public static FirestoreOpenTelemetryOptions.Builder newBuilder() {
    return new FirestoreOpenTelemetryOptions.Builder();
  }

  public static class Builder {

    private boolean tracingEnabled;

    private boolean metricsEnabled;

    @Nullable private OpenTelemetry customOpenTelemetry;

    private Builder() {
      tracingEnabled = false;
      metricsEnabled = false;
      customOpenTelemetry = null;
    }

    private Builder(FirestoreOpenTelemetryOptions options) {
      this.tracingEnabled = options.tracingEnabled;
      this.metricsEnabled = options.metricsEnabled;
      this.customOpenTelemetry = options.customOpenTelemetry;
    }

    @Nonnull
    public FirestoreOpenTelemetryOptions build() {
      return new FirestoreOpenTelemetryOptions(this);
    }

    /**
     * Sets whether tracing should be enabled.
     *
     * @param tracingEnabled Whether tracing should be enabled.
     */
    @Nonnull
    public FirestoreOpenTelemetryOptions.Builder setTracingEnabled(boolean tracingEnabled) {
      this.tracingEnabled = tracingEnabled;
      return this;
    }

    /**
     * Sets whether client side metrics should be enabled.
     *
     * @param metricsEnabled Whether client side metrics should be enabled.
     */
    @Nonnull
    public FirestoreOpenTelemetryOptions.Builder setMetricsEnabled(boolean metricsEnabled) {
      this.metricsEnabled = metricsEnabled;
      return this;
    }

    /**
     * Sets the {@link OpenTelemetry} to use with this Firestore instance. If telemetry collection
     * is enabled, but an `OpenTelemetry` is not provided, the Firestore SDK will attempt to use the
     * `GlobalOpenTelemetry`.
     *
     * @param openTelemetry The OpenTelemetry that should be used by this Firestore instance.
     */
    @Nonnull
    public FirestoreOpenTelemetryOptions.Builder setOpenTelemetry(
        @Nonnull OpenTelemetry openTelemetry) {
      this.customOpenTelemetry = openTelemetry;
      return this;
    }
  }
}
