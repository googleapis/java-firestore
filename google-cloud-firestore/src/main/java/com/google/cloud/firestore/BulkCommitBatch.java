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
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import java.util.Set;

/** Used to represent a batch on the BatchQueue. */
class BulkCommitBatch extends UpdateBuilder<ApiFuture<WriteResult>> {

  BulkCommitBatch(FirestoreImpl firestore, int maxBatchSize) {
    super(firestore, maxBatchSize);
  }

  BulkCommitBatch(
      FirestoreImpl firestore,
      BulkCommitBatch retryBatch,
      final Set<DocumentReference> docsToRetry) {
    super(firestore);
    this.writes.addAll(
        FluentIterable.from(retryBatch.writes)
            .filter(
                new Predicate<WriteOperation>() {
                  @Override
                  public boolean apply(WriteOperation writeOperation) {
                    return docsToRetry.contains(writeOperation.documentReference);
                  }
                })
            .toList());

    Preconditions.checkState(
        retryBatch.state == BatchState.SENT,
        "Batch should be SENT when creating a new BulkCommitBatch for retry");
    this.state = retryBatch.state;
    this.pendingOperations = retryBatch.pendingOperations;
  }

  ApiFuture<WriteResult> wrapResult(ApiFuture<WriteResult> result) {
    return result;
  }

  @Override
  boolean allowDuplicateDocs() {
    return false;
  }
}
