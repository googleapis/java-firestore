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

import com.google.api.core.ApiFuture;
import com.google.common.base.Preconditions;

/** Used to represent a batch on the BatchQueue. */
class BulkCommitBatch extends UpdateBuilder<ApiFuture<WriteResult>> {

  BulkCommitBatch(FirestoreImpl firestore, int maxBatchSize) {
    super(firestore, maxBatchSize);
  }

  BulkCommitBatch(FirestoreImpl firestore, BulkCommitBatch retryBatch) {
    super(firestore);

    // Create a new BulkCommitBatch containing only the indexes from the provided indexes to retry.
    for (int index : retryBatch.getPendingIndexes()) {
      this.writes.add(retryBatch.writes.get(index));
    }

    Preconditions.checkState(
        retryBatch.state == BatchState.SENT,
        "Batch should be SENT when creating a new BulkCommitBatch for retry");
    this.state = retryBatch.state;
    this.pendingOperations = retryBatch.pendingOperations;
  }

  ApiFuture<WriteResult> wrapResult(ApiFuture<WriteResult> result) {
    return result;
  }
}
