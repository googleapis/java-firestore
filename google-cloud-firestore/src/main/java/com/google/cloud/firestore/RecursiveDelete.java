/*
 * Copyright 2021 Google LLC
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

import com.google.api.core.ApiAsyncFunction;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.core.BetaApi;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.cloud.firestore.Query.QueryOptions;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.Status;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Class used to store state required for running a recursive delete operation. Each recursive
 * delete call should use a new instance of the class.
 */
@BetaApi
public final class RecursiveDelete {
  /**
   * Datastore allowed numeric IDs where Firestore only allows strings. Numeric IDs are exposed to
   * Firestore as __idNUM__, so this is the lowest possible negative numeric value expressed in that
   * format.
   *
   * <p>This constant is used to specify startAt/endAt values when querying for all descendants in a
   * single collection.
   */
  public static final String REFERENCE_NAME_MIN_ID = "__id-9223372036854775808__";

  /*!
   * The query limit used for recursive deletes when fetching all descendants of
   * the specified reference to delete. This is done to prevent the query stream
   * from streaming documents faster than Firestore can delete.
   */
  public static final int MAX_PENDING_OPS = 5000;

  /**
   * The number of pending BulkWriter operations at which RecursiveDelete starts the next limit
   * query to fetch descendants. By starting the query while there are pending operations, Firestore
   * can improve BulkWriter throughput. This helps prevent BulkWriter from idling while Firestore
   * fetches the next query.
   */
  public static final int MIN_PENDING_OPS = 1000;

  private final FirestoreRpcContext<?> firestoreRpcContext;
  private final BulkWriter writer;
  @Nullable private final CollectionReference collectionReference;
  @Nullable private final DocumentReference documentReference;

  /** The number of deletes that failed with a permanent error. */
  private int errorCount = 0;

  /**
   * The most recently thrown error. Used to populate the developer-facing error message when the
   * recursive delete operation completes.
   */
  @Nullable private Throwable lastError;

  /** Whether there are still documents to delete that still need to be fetched. */
  private boolean documentsPending = true;

  /** A deferred promise that resolves when the recursive delete operation is completed. */
  private final SettableApiFuture<Void> completionFuture = SettableApiFuture.create();

  /** Whether a query stream is currently in progress. Only one stream can be run at a time. */
  private boolean streamInProgress = false;

  /** Query limit to use when fetching all descendants. */
  private int maxPendingOps = MAX_PENDING_OPS;

  /**
   * The number of pending BulkWriter operations at which RecursiveDelete starts the next limit
   * query to fetch descendants.
   */
  private int minPendingOps = MIN_PENDING_OPS;

  /**
   * The last document snapshot returned by the stream. Used to set the startAfter() field in the
   * subsequent stream.
   */
  @Nullable private DocumentSnapshot lastDocumentSnap;

  /**
   * The number of pending BulkWriter operations. Used to determine when the next query can be run.
   */
  private int pendingOperationsCount = 0;

  RecursiveDelete(
      FirestoreRpcContext<?> firestoreRpcContext,
      BulkWriter writer,
      CollectionReference reference) {
    Preconditions.checkState(reference != null, "CollectionReference cannot be null");
    this.firestoreRpcContext = firestoreRpcContext;
    this.writer = writer;
    this.collectionReference = reference;
    this.documentReference = null;
  }

  RecursiveDelete(
      FirestoreImpl firestoreRpcContext, BulkWriter writer, DocumentReference reference) {
    Preconditions.checkState(reference != null, "DocumentReference cannot be null");
    this.firestoreRpcContext = firestoreRpcContext;
    this.writer = writer;
    this.documentReference = reference;
    this.collectionReference = null;
  }

  public ApiFuture<Void> run() {
    Preconditions.checkState(
        documentsPending, "The recursive delete operation has already been completed");

    // TODO: figure out if this is safe, or a way to lock this.
    writer.verifyNotClosedLocked();

    streamDescendants();
    return completionFuture;
  }

  private void streamDescendants() {
    Preconditions.checkState(
        (documentReference == null && collectionReference != null)
            || (documentReference != null && collectionReference == null),
        "Either documentReference or collectionReference should be set");

    Query query;
    if (collectionReference != null) {
      query = getAllDescendantsQuery(collectionReference);
    } else {
      query = getAllDescendantsQuery(documentReference);
    }
    streamInProgress = true;
    final int[] streamedDocsCount = {0};
    final ApiStreamObserver<DocumentSnapshot> responseObserver =
        new ApiStreamObserver<DocumentSnapshot>() {
          public void onNext(DocumentSnapshot snapshot) {
            streamedDocsCount[0]++;
            lastDocumentSnap = snapshot;
            deleteReference(snapshot.getReference());
          }

          public void onError(Throwable throwable) {
            lastError =
                new FirestoreException("Failed to fetch children documents.", Status.UNAVAILABLE);
            onQueryEnd();
          }

          public void onCompleted() {
            streamInProgress = false;
            // If there are fewer than the number of documents specified in the limit() field, we
            // know that the query is complete.
            if (streamedDocsCount[0] < maxPendingOps) {
              onQueryEnd();
            } else if (pendingOperationsCount == 0) {
              streamDescendants();
            }
          }
        };

    query.stream(responseObserver);
  }

  private Query getAllDescendantsQuery(@Nonnull CollectionReference collectionReference) {
    // The parent is the closest ancestor document to the location we're deleting. Since we are
    // deleting a collection, the parent is the path of the document containing that collection (or
    // the database root, if it is a root collection).
    ResourcePath parentPath = collectionReference.getResourcePath();
    parentPath = parentPath.popLast();
    String collectionId = collectionReference.getId();

    Query query = baseAllDescendantsQuery(parentPath, collectionId);

    // To find all descendants of a collection reference, we need to use a
    // composite filter that captures all documents that start with the
    // collection prefix. The MIN_ID constant represents the minimum key in
    // this collection, and a null byte + the MIN_ID represents the minimum
    // key is the next possible collection.
    char nullChar = '\0';
    String startAt = collectionId + "/" + REFERENCE_NAME_MIN_ID;
    String endAt = collectionId + nullChar + "/" + REFERENCE_NAME_MIN_ID;
    query =
        query
            .whereGreaterThanOrEqualTo(FieldPath.documentId(), startAt)
            .whereLessThan(FieldPath.documentId(), endAt);

    // startAfter() needs to be added after the where() filters since it creates an implicit
    // orderBy.
    if (lastDocumentSnap != null) {
      query = query.startAfter(lastDocumentSnap);
    }

    return query;
  }

  private Query getAllDescendantsQuery(@Nonnull DocumentReference documentReference) {
    // The parent is the closest ancestor document to the location we're deleting. Since we are
    // deleting a document, the parent is the path of that document.
    ResourcePath parentPath = documentReference.getResourcePath();
    String collectionId = documentReference.getParent().getId();

    return baseAllDescendantsQuery(parentPath, collectionId);
  }

  private Query baseAllDescendantsQuery(ResourcePath resourcePath, String collectionId) {
    Query query =
        new Query(
            firestoreRpcContext,
            QueryOptions.builder()
                .setParentPath(resourcePath)
                .setCollectionId(collectionId)
                .setAllDescendants(true)
                .setKindless(true)
                .setRequireConsistency(false)
                .build());

    // Query for names only to fetch empty snapshots.
    query = query.select(FieldPath.documentId()).limit(maxPendingOps);

    return query;
  }

  /**
   * Called when all descendants of the provided reference have been streamed or if a permanent
   * error occurs during the stream. Deletes the developer provided reference and wraps any errors
   * that occurred.
   */
  private void onQueryEnd() {
    documentsPending = false;

    // Used to aggregate flushFuture and deleteFuture to use with ApiFutures.allAsList(), in order
    // to ensure that the delete catchingAsync() callback is run before the flushFuture callback.
    List<ApiFuture<Void>> pendingFutures = new ArrayList<>();

    // Delete the provided document reference if one was provided.
    if (documentReference != null) {
      ApiFuture<Void> voidDeleteFuture =
          ApiFutures.transformAsync(
              catchingDelete(documentReference),
              new ApiAsyncFunction<WriteResult, Void>() {
                public ApiFuture<Void> apply(WriteResult result) {
                  return ApiFutures.immediateFuture(null);
                }
              },
              MoreExecutors.directExecutor());
      pendingFutures.add(voidDeleteFuture);
    }

    ApiFuture<Void> flushFuture = writer.flush();
    pendingFutures.add(flushFuture);

    // Completes the future returned by run() and sets the exception if an error occurred.
    ApiFutures.transformAsync(
        ApiFutures.allAsList(pendingFutures),
        new ApiAsyncFunction<List<Void>, Void>() {
          public ApiFuture<Void> apply(List<Void> unused) {
            if (lastError == null) {
              completionFuture.set(null);
            } else {
              String message =
                  errorCount
                      + (errorCount != 1 ? " deletes" : " delete")
                      + " failed. "
                      + lastError.getMessage();
              if (lastError instanceof FirestoreException) {
                lastError =
                    new FirestoreException(message, ((FirestoreException) lastError).getStatus());
              } else {
                lastError = new Throwable(message, lastError);
              }
              completionFuture.setException(lastError);
            }
            return ApiFutures.immediateFuture(null);
          }
        },
        MoreExecutors.directExecutor());
  }

  /** Deletes the provided reference and increments the error count if the delete fails. */
  private ApiFuture<WriteResult> catchingDelete(DocumentReference reference) {
    ApiFuture<WriteResult> deleteFuture = writer.delete(reference);
    return ApiFutures.catchingAsync(
        deleteFuture,
        Throwable.class,
        new ApiAsyncFunction<Throwable, WriteResult>() {
          public ApiFuture<WriteResult> apply(Throwable e) {
            incrementErrorCount(e);
            return ApiFutures.immediateFuture(null);
          }
        },
        MoreExecutors.directExecutor());
  }

  /** Deletes the provided reference and starts the next stream if conditions are met. */
  private void deleteReference(final DocumentReference reference) {
    pendingOperationsCount++;
    ApiFutures.transformAsync(
        catchingDelete(reference),
        new ApiAsyncFunction<WriteResult, Object>() {

          public ApiFuture<Object> apply(WriteResult result) {
            pendingOperationsCount--;
            // We wait until the previous stream has ended in order to ensure the
            // startAfter document is correct. Starting the next stream while
            // there are pending operations allows Firestore to maximize
            // BulkWriter throughput.
            if (documentsPending && !streamInProgress && pendingOperationsCount < minPendingOps) {
              streamDescendants();
            }
            return ApiFutures.immediateFuture(null);
          }
        },
        MoreExecutors.directExecutor());
  }

  private void incrementErrorCount(Throwable e) {
    errorCount++;
    lastError = e;
  }

  /**
   * Used to test the query startAfter() resumption without needing to stream MAX_PENDING_OPS docs.
   */
  @VisibleForTesting
  void setMaxPendingOps(int limit) {
    maxPendingOps = limit;
  }

  /** Used to test the query startAfter() resumption. */
  @VisibleForTesting
  void setMinPendingOps(int limit) {
    minPendingOps = limit;
  }
}
