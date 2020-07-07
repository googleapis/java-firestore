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

import com.google.cloud.Timestamp;
import io.grpc.Status;
import javax.annotation.Nullable;

/**
 * A BatchWriteResult wraps the write time and status returned by Firestore when making
 * BatchWriteRequests.
 */
public final class BatchWriteResult {
  private final String key;
  @Nullable private final Timestamp writeTime;
  private final Status status;
  private final String message;
  @Nullable private final Exception exception;

  BatchWriteResult(String key, @Nullable Timestamp timestamp, Status status, String message) {
    this.key = key;
    this.writeTime = timestamp;
    this.status = status;
    this.message = message;
    this.exception = null;
  }

  BatchWriteResult(
      String key,
      @Nullable Timestamp timestamp,
      Status status,
      String message,
      Exception exception) {
    this.key = key;
    this.writeTime = timestamp;
    this.status = status;
    this.message = message;
    this.exception = exception;
  }

  public String getKey() {
    return key;
  }

  @Nullable
  public Timestamp getWriteTime() {
    return writeTime;
  }

  @Nullable
  public Status getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  @Nullable
  public Exception getException() {
    return exception;
  }
}
