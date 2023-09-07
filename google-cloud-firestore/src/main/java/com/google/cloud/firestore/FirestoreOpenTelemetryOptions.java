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

import io.opentelemetry.api.OpenTelemetry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FirestoreOpenTelemetryOptions {
  private final boolean enabled;

  @Nullable private final OpenTelemetry openTelemetry;

  FirestoreOpenTelemetryOptions(Builder builder) {
    this.enabled = builder.enabled;
    this.openTelemetry = builder.openTelemetry;
  }

  public boolean getEnabled() {
    return enabled;
  }

  public OpenTelemetry getOpenTelemetry() {
    return openTelemetry;
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

    @Nullable private OpenTelemetry openTelemetry;

    private Builder() {
      enabled = false;
      openTelemetry = null;
    }

    private Builder(FirestoreOpenTelemetryOptions options) {
      this.enabled = options.enabled;
      this.openTelemetry = options.openTelemetry;
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
     * Sets the {@link OpenTelemetry} to use with this Firestore client. If telemetry collection is
     * is enabled, but an `OpenTelemetry` is not provided, the Firestore SDK will attempt to use the
     * GlobalOpenTelemetry.
     *
     * @param openTelemetry The OpenTelemetry that can be used by this client.
     */
    @Nonnull
    public FirestoreOpenTelemetryOptions.Builder setOpenTelemetry(
        @Nonnull OpenTelemetry openTelemetry) {
      this.openTelemetry = openTelemetry;
      return this;
    }
  }
}
