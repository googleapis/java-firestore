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
import javax.annotation.Nullable;

/**
 * A BatchWriteResult wraps the write time and status returned by Firestore when making
 * BatchWriteRequests.
 */
public final class BatchWriteResult {
  private final DocumentReference documentReference;
  @Nullable private final Timestamp writeTime;
  @Nullable private final Exception exception;

  BatchWriteResult(
      DocumentReference documentReference, @Nullable Timestamp timestamp, Exception exception) {
    this.documentReference = documentReference;
    this.writeTime = timestamp;
    this.exception = exception;
  }

  public DocumentReference getDocumentReference() {
    return documentReference;
  }

  @Nullable
  public Timestamp getWriteTime() {
    return writeTime;
  }

  @Nullable
  public Exception getException() {
    return exception;
  }
}
