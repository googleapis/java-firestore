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
  /** Sampling rate of 10% is chosen for traces by default. */
  static double DEFAULT_TRACE_SAMPLING_RATE = 0.1;

  private final boolean enabled;
  @Nullable private final OpenTelemetrySdk sdk;

  private final double traceSamplingRate;

  FirestoreOpenTelemetryOptions(Builder builder) {
    this.enabled = builder.enabled;
    this.sdk = builder.sdk;
    if (builder.traceSamplingRate == null) {
      this.traceSamplingRate = DEFAULT_TRACE_SAMPLING_RATE;
    } else {
      this.traceSamplingRate = builder.traceSamplingRate;
    }
  }

  public boolean getEnabled() {
    return enabled;
  }

  public OpenTelemetrySdk getSdk() {
    return sdk;
  }

  public double getTraceSamplingRate() {
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
    private boolean enabled;
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
     * OpenTelemetrySdk, the Firestore SDK will create an OpenTelemetrySdk instance which transmits
     * telemetry information to Google Cloud.
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
    public FirestoreOpenTelemetryOptions.Builder setTraceSamplingRate(
        double samplingRate) {
      if (this.sdk != null) {
        throw new IllegalArgumentException(
            "Cannot set OpenTelemetry trace sampling rate because you have already provided an OpenTelemetrySdk object. Please set the sampling rate directly on the OpenTelemetrySdk object.");
      }
      if (samplingRate < 0.0 || samplingRate > 1.0) {
        throw new IllegalArgumentException("Trace sampling rate must be a number between 0.0 and 1.0.");
      }
      this.traceSamplingRate = samplingRate;
      return this;
    }
  }
}
