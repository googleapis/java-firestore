/*
 * Copyright 2023 Google LLC
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

import io.opentelemetry.sdk.OpenTelemetrySdk;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FirestoreOpenTelemetryOptions {
  private final Boolean enabled;
  @Nullable private final OpenTelemetrySdk sdk;

  private final Double traceSamplingRate;

  FirestoreOpenTelemetryOptions(Builder builder) {
    this.enabled = builder.enabled;
    this.sdk = builder.sdk;
    this.traceSamplingRate = builder.traceSamplingRate;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public OpenTelemetrySdk getSdk() {
    return sdk;
  }

  public Double getTraceSamplingRate() {
    return traceSamplingRate;
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
    private Boolean enabled;
    @Nullable private OpenTelemetrySdk sdk;
    private Double traceSamplingRate;

    private Builder() {
      enabled = false;
      sdk = null;
      traceSamplingRate = null;
    }

    private Builder(FirestoreOpenTelemetryOptions options) {
      this.enabled = options.enabled;
      this.sdk = options.sdk;
      this.traceSamplingRate = options.traceSamplingRate;
    }

    @Nonnull
    public FirestoreOpenTelemetryOptions build() {
      return new FirestoreOpenTelemetryOptions(this);
    }

    /**
     * Sets whether the Firestore SDK should collect telemetry information.
     *
     * @param enable Whether telemetry collection should be enabled.
     */
    @Nonnull
    public FirestoreOpenTelemetryOptions.Builder setTelemetryCollectionEnabled(boolean enable) {
      this.enabled = enable;
      return this;
    }

    /**
     * Sets the {@link OpenTelemetrySdk} to use with this Firestore client. In the absence of an
     * OpenTelemetrySdk, the Firestore SDK will create and globally register an OpenTelemetrySdk
     * instance which transmits telemetry information to Google Cloud.
     *
     * @param sdk The OpenTelemetrySdk that can be used by this client.
     */
    @Nonnull
    public FirestoreOpenTelemetryOptions.Builder setOpenTelemetrySdk(
        @Nonnull OpenTelemetrySdk sdk) {
      if (this.traceSamplingRate != null) {
        throw new IllegalArgumentException(
            "OpenTelemetry trace sampling rate should not be set because you are providing an OpenTelemetrySdk object. Please set the sampling rate directly on the OpenTelemetrySdk object.");
      }
      this.sdk = sdk;
      return this;
    }

    /**
     * Sets the trace sampling rate. This will only take effect if no OpenTelemetrySdk is provided.
     *
     * @param samplingRate The trace sampling rate between 0.0 and 1.0.
     */
    @Nonnull
    public FirestoreOpenTelemetryOptions.Builder setOpenTelemetryTraceSamplingRate(
        double samplingRate) {
      if (this.sdk != null) {
        throw new IllegalArgumentException(
            "Cannot set OpenTelemetry trace sampling rate because you have already provided an OpenTelemetrySdk object. Please set the sampling rate directly on the OpenTelemetrySdk object.");
      }
      this.traceSamplingRate = samplingRate;
      return this;
    }
  }
}
