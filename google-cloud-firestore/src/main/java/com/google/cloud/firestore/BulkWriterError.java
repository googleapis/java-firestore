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

import com.google.cloud.firestore.BulkWriter.OperationType;
import io.grpc.Status;

public class BulkWriterError extends Exception {
  public DocumentReference getDocumentReference() {
    return documentReference;
  }

  public int getFailedAttempts() {
    return failedAttempts;
  }

  public String getMessage() {
    return message;
  }

  public OperationType getOperationType() {
    return operationType;
  }

  public Status getStatus() {
    return status;
  }

  private final Status status;
  private final String message;
  private final DocumentReference documentReference;
  private final OperationType operationType;
  private final int failedAttempts;

  public BulkWriterError(
      Status status,
      String message,
      DocumentReference reference,
      OperationType type,
      int attempts) {
    this.status = status;
    this.message = message;
    documentReference = reference;
    operationType = type;
    failedAttempts = attempts;
  }
}
