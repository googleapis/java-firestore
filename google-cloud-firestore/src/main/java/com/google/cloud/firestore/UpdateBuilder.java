/*
 * Copyright 2017 Google LLC
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

import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.core.SettableApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.UserDataConverter.EncodingOptions;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firestore.v1.BatchWriteRequest;
import com.google.firestore.v1.BatchWriteResponse;
import com.google.firestore.v1.CommitRequest;
import com.google.firestore.v1.CommitResponse;
import com.google.firestore.v1.Write;
import com.google.protobuf.ByteString;
import io.grpc.Status;
import io.opencensus.trace.AttributeValue;
import io.opencensus.trace.Tracing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Abstract class that collects and bundles all write operations for {@link Transaction} and {@link
 * WriteBatch}.
 */
public abstract class UpdateBuilder<T> {

  final FirestoreImpl firestore;
  private final List<Write.Builder> writes;
  private boolean committed;
  private final int maxBatchSize;

  private BatchState state = BatchState.OPEN;
  private final HashSet<DocumentReference> documents = new HashSet<>();
  private final SettableApiFuture<Void> completeFuture = SettableApiFuture.create();
  private final Map<Integer, SettableApiFuture<WriteResult>> resultsMap = new HashMap<>();

  /**
   * Used to represent the state of batch.
   *
   * <p>Writes can only be added while the batch is OPEN. For a batch to be sent, the batch must be
   * READY_TO_SEND. After a batch is sent, it is marked as SENT.
   */
  enum BatchState {
    OPEN,
    READY_TO_SEND,
    SENT,
  }

  UpdateBuilder(FirestoreImpl firestore) {
    this(firestore, BulkWriter.MAX_BATCH_SIZE);
  }

  UpdateBuilder(FirestoreImpl firestore, int maxBatchSize) {
    this.firestore = firestore;
    this.maxBatchSize = maxBatchSize;
    this.writes = new ArrayList<>();
  }

  /**
   * Wraps the result of the write operation before it is returned.
   *
   * <p>This method is used to generate the return value for all public methods. It allows
   * operations on Transaction and Writebatch to return the object for chaining, while also allowing
   * BulkWriter operations to return the future directly.
   */
  abstract T wrapResult(ApiFuture<WriteResult> result);

  /** Whether to allow multiple writes to the same document in a batch. */
  boolean allowDuplicateDocs() {
    return true;
  }

  /**
   * Turns a field map that contains field paths into a nested map. Turns {a.b : c} into {a : {b :
   * c}}.
   */
  private static Map<String, Object> expandObject(Map<FieldPath, Object> data) {
    Map<String, Object> result = new HashMap<>();

    SortedSet<FieldPath> sortedFields = new TreeSet<>(data.keySet());

    FieldPath lastField = null;

    for (FieldPath field : sortedFields) {
      if (lastField != null && lastField.isPrefixOf(field)) {
        throw new IllegalArgumentException(
            String.format("Detected ambiguous definition for field '%s'.", lastField));
      }

      List<String> segments = field.getSegments();
      Object value = data.get(field);
      Map<String, Object> currentMap = result;

      for (int i = 0; i < segments.size(); ++i) {
        if (i == segments.size() - 1) {
          currentMap.put(segments.get(i), value);
        } else {
          if (!currentMap.containsKey(segments.get(i))) {
            currentMap.put(segments.get(i), new HashMap<>());
          }

          currentMap = (Map<String, Object>) currentMap.get(segments.get(i));
        }
      }

      lastField = field;
    }

    return result;
  }

  /**
   * Creates a new Document at the DocumentReference's location. It fails the write if the document
   * exists.
   *
   * @param documentReference The DocumentReference to create.
   * @param fields A map of the fields and values for the document.
   * @return The instance for chaining.
   */
  @Nonnull
  public T create(
      @Nonnull DocumentReference documentReference, @Nonnull Map<String, Object> fields) {
    return performCreate(documentReference, fields);
  }

  private T performCreate(
      @Nonnull DocumentReference documentReference, @Nonnull Map<String, Object> fields) {
    verifyNotCommitted();
    DocumentSnapshot documentSnapshot =
        DocumentSnapshot.fromObject(
            firestore, documentReference, fields, UserDataConverter.NO_DELETES);
    DocumentTransform documentTransform =
        DocumentTransform.fromFieldPathMap(
            documentReference, convertToFieldPaths(fields, /* splitOnDots= */ false));

    Write.Builder write = documentSnapshot.toPb();
    write.setCurrentDocument(Precondition.exists(false).toPb());

    if (!documentTransform.isEmpty()) {
      write.addAllUpdateTransforms(documentTransform.toPb());
    }

    writes.add(write);

    return wrapResult(processOperation(documentReference));
  }

  private void verifyNotCommitted() {
    Preconditions.checkState(
        !committed, "Cannot modify a WriteBatch that has already been committed.");
  }

  /**
   * Creates a new Document at the DocumentReference location. It fails the write if the document
   * exists.
   *
   * @param documentReference The DocumentReference to create.
   * @param pojo The POJO that will be used to populate the document contents.
   * @return The instance for chaining.
   */
  @Nonnull
  public T create(@Nonnull DocumentReference documentReference, @Nonnull Object pojo) {
    Object data = CustomClassMapper.convertToPlainJavaTypes(pojo);
    if (!(data instanceof Map)) {
      throw FirestoreException.invalidState("Can't set a document's data to an array or primitive");
    }
    return performCreate(documentReference, (Map<String, Object>) data);
  }

  /**
   * Overwrites the document referred to by this DocumentReference. If the document doesn't exist
   * yet, it will be created. If a document already exists, it will be overwritten.
   *
   * @param documentReference The DocumentReference to overwrite.
   * @param fields A map of the field paths and values for the document.
   * @return The instance for chaining.
   */
  @Nonnull
  public T set(@Nonnull DocumentReference documentReference, @Nonnull Map<String, Object> fields) {
    return set(documentReference, fields, SetOptions.OVERWRITE);
  }

  /**
   * Overwrites the document referred to by this DocumentReference. If the document doesn't exist
   * yet, it will be created. If you pass {@link SetOptions}, the provided data can be merged into
   * an existing document.
   *
   * @param documentReference The DocumentReference to overwrite.
   * @param fields A map of the field paths and values for the document.
   * @param options An object to configure the set behavior.
   * @return The instance for chaining.
   */
  @Nonnull
  public T set(
      @Nonnull DocumentReference documentReference,
      @Nonnull Map<String, Object> fields,
      @Nonnull SetOptions options) {
    return performSet(documentReference, fields, options);
  }

  /**
   * Overwrites the document referred to by this DocumentReference. If the document doesn't exist
   * yet, it will be created. If a document already exists, it will be overwritten.
   *
   * @param documentReference The DocumentReference to overwrite.
   * @param pojo The POJO that will be used to populate the document contents.
   * @return The instance for chaining.
   */
  @Nonnull
  public T set(@Nonnull DocumentReference documentReference, @Nonnull Object pojo) {
    return set(documentReference, pojo, SetOptions.OVERWRITE);
  }

  /**
   * Overwrites the document referred to by this DocumentReference. If the document doesn't exist
   * yet, it will be created. If you pass {@link SetOptions}, the provided data can be merged into
   * an existing document.
   *
   * @param documentReference The DocumentReference to overwrite.
   * @param pojo The POJO that will be used to populate the document contents.
   * @param options An object to configure the set behavior.
   * @return The instance for chaining.
   */
  @Nonnull
  public T set(
      @Nonnull DocumentReference documentReference,
      @Nonnull Object pojo,
      @Nonnull SetOptions options) {
    Object data = CustomClassMapper.convertToPlainJavaTypes(pojo);
    if (!(data instanceof Map)) {
      throw new IllegalArgumentException("Can't set a document's data to an array or primitive");
    }
    return performSet(documentReference, (Map<String, Object>) data, options);
  }

  private T performSet(
      @Nonnull DocumentReference documentReference,
      @Nonnull Map<String, Object> fields,
      @Nonnull SetOptions options) {
    verifyNotCommitted();
    Map<FieldPath, Object> documentData;

    if (options.getFieldMask() != null) {
      documentData = applyFieldMask(fields, options.getFieldMask());
    } else {
      documentData = convertToFieldPaths(fields, /* splitOnDots= */ false);
    }

    DocumentSnapshot documentSnapshot =
        DocumentSnapshot.fromObject(
            firestore, documentReference, expandObject(documentData), options.getEncodingOptions());
    FieldMask documentMask = FieldMask.EMPTY_MASK;
    DocumentTransform documentTransform =
        DocumentTransform.fromFieldPathMap(documentReference, documentData);

    if (options.getFieldMask() != null) {
      List<FieldPath> fieldMask = new ArrayList<>(options.getFieldMask());
      fieldMask.removeAll(documentTransform.getFields());
      documentMask = new FieldMask(fieldMask);
    } else if (options.isMerge()) {
      documentMask = FieldMask.fromObject(fields);
    }

    Write.Builder write = documentSnapshot.toPb();
    if (!documentTransform.isEmpty()) {
      write.addAllUpdateTransforms(documentTransform.toPb());
    }

    if (options.isMerge() || options.getFieldMask() != null) {
      write.setUpdateMask(documentMask.toPb());
    }

    writes.add(write);

    return wrapResult(processOperation(documentReference));
  }

  /** Removes all values in 'fields' that are not specified in 'fieldMask'. */
  private Map<FieldPath, Object> applyFieldMask(
      Map<String, Object> fields, List<FieldPath> fieldMask) {
    List<FieldPath> remainingFields = new ArrayList<>(fieldMask);
    Map<FieldPath, Object> filteredData =
        applyFieldMask(fields, remainingFields, FieldPath.empty());

    if (!remainingFields.isEmpty()) {
      throw new IllegalArgumentException(
          String.format(
              "Field masks contains invalid path. No data exist at field '%s'.",
              remainingFields.get(0)));
    }

    return filteredData;
  }

  /**
   * Strips all values in 'fields' that are not specified in 'fieldMask'. Modifies 'fieldMask'
   * inline and removes all matched fields.
   */
  private Map<FieldPath, Object> applyFieldMask(
      Map<String, Object> fields, List<FieldPath> fieldMask, FieldPath root) {
    Map<FieldPath, Object> filteredMap = new HashMap<>();

    for (Entry<String, Object> entry : fields.entrySet()) {
      FieldPath currentField = root.append(FieldPath.of(entry.getKey()));
      if (fieldMask.remove(currentField)) {
        filteredMap.put(currentField, entry.getValue());
      } else if (entry.getValue() instanceof Map) {
        filteredMap.putAll(
            applyFieldMask((Map<String, Object>) entry.getValue(), fieldMask, currentField));
      } else if (entry.getValue() == FieldValue.DELETE_SENTINEL) {
        throw new IllegalArgumentException(
            String.format(
                "Cannot specify FieldValue.delete() for non-merged field '%s'.", currentField));
      }
    }

    return filteredMap;
  }

  private Map<FieldPath, Object> convertToFieldPaths(
      @Nonnull Map<String, Object> fields, boolean splitOnDots) {
    Map<FieldPath, Object> fieldPaths = new HashMap<>();

    for (Map.Entry<String, Object> entry : fields.entrySet()) {
      if (splitOnDots) {
        fieldPaths.put(FieldPath.fromDotSeparatedString(entry.getKey()), entry.getValue());
      } else {
        fieldPaths.put(FieldPath.of(entry.getKey()), entry.getValue());
      }
    }

    return fieldPaths;
  }

  /**
   * Updates fields in the document referred to by this DocumentReference. If the document doesn't
   * exist yet, the update will fail.
   *
   * @param documentReference The DocumentReference to update.
   * @param fields A Map containing the fields and values with which to update the document.
   * @return The instance for chaining.
   */
  @Nonnull
  public T update(
      @Nonnull DocumentReference documentReference, @Nonnull Map<String, Object> fields) {
    return performUpdate(
        documentReference,
        convertToFieldPaths(fields, /* splitOnDots= */ true),
        Precondition.exists(true));
  }

  /**
   * Updates fields in the document referred to by this DocumentReference. If the document doesn't
   * exist yet, the update will fail.
   *
   * @param documentReference The DocumentReference to update.
   * @param fields A Map containing the fields and values with which to update the document.
   * @param precondition Precondition to enforce on this update.
   * @return The instance for chaining.
   */
  @Nonnull
  public T update(
      @Nonnull DocumentReference documentReference,
      @Nonnull Map<String, Object> fields,
      Precondition precondition) {
    Preconditions.checkArgument(
        !precondition.hasExists(), "Precondition 'exists' cannot be specified for update() calls.");
    return performUpdate(
        documentReference, convertToFieldPaths(fields, /* splitOnDots= */ true), precondition);
  }

  /**
   * Updates the fields in the document referred to by this DocumentReference. If the document
   * doesn't exist yet, the update will fail.
   *
   * @param documentReference The DocumentReference to update.
   * @param field The first field to set.
   * @param value The first value to set.
   * @param moreFieldsAndValues String and Object pairs with more fields to be set.
   * @return The instance for chaining.
   */
  @Nonnull
  public T update(
      @Nonnull DocumentReference documentReference,
      @Nonnull String field,
      @Nullable Object value,
      Object... moreFieldsAndValues) {
    return performUpdate(
        documentReference,
        Precondition.exists(true),
        FieldPath.fromDotSeparatedString(field),
        value,
        moreFieldsAndValues);
  }

  /**
   * Updates the fields in the document referred to by this DocumentReference. If the document
   * doesn't exist yet, the update will fail.
   *
   * @param documentReference The DocumentReference to update.
   * @param fieldPath The first field to set.
   * @param value The first value to set.
   * @param moreFieldsAndValues String and Object pairs with more fields to be set.
   * @return The instance for chaining.
   */
  @Nonnull
  public T update(
      @Nonnull DocumentReference documentReference,
      @Nonnull FieldPath fieldPath,
      @Nullable Object value,
      Object... moreFieldsAndValues) {
    return performUpdate(
        documentReference, Precondition.exists(true), fieldPath, value, moreFieldsAndValues);
  }

  /**
   * Updates the fields in the document referred to by this DocumentReference. If the document
   * doesn't exist yet, the update will fail.
   *
   * @param documentReference The DocumentReference to update.
   * @param precondition Precondition to enforce on this update.
   * @param field The first field to set.
   * @param value The first value to set.
   * @param moreFieldsAndValues String and Object pairs with more fields to be set.
   * @return The instance for chaining.
   */
  @Nonnull
  public T update(
      @Nonnull DocumentReference documentReference,
      @Nonnull Precondition precondition,
      @Nonnull String field,
      @Nullable Object value,
      Object... moreFieldsAndValues) {
    Preconditions.checkArgument(
        !precondition.hasExists(), "Precondition 'exists' cannot be specified for update() calls.");
    return performUpdate(
        documentReference,
        precondition,
        FieldPath.fromDotSeparatedString(field),
        value,
        moreFieldsAndValues);
  }

  /**
   * Updates the fields in the document referred to by this DocumentReference. If the document
   * doesn't exist yet, the update will fail.
   *
   * @param documentReference The DocumentReference to update.
   * @param precondition Precondition to enforce on this update.
   * @param fieldPath The first field to set.
   * @param value The first value to set.
   * @param moreFieldsAndValues String and Object pairs with more fields to be set.
   * @return The instance for chaining.
   */
  @Nonnull
  public T update(
      @Nonnull DocumentReference documentReference,
      @Nonnull Precondition precondition,
      @Nonnull FieldPath fieldPath,
      @Nullable Object value,
      Object... moreFieldsAndValues) {
    Preconditions.checkArgument(
        !precondition.hasExists(), "Precondition 'exists' cannot be specified for update() calls.");
    return performUpdate(documentReference, precondition, fieldPath, value, moreFieldsAndValues);
  }

  private T performUpdate(
      @Nonnull DocumentReference documentReference,
      @Nonnull Precondition precondition,
      @Nonnull FieldPath fieldPath,
      @Nullable Object value,
      Object[] moreFieldsAndValues) {
    Object data = CustomClassMapper.convertToPlainJavaTypes(value);
    Map<FieldPath, Object> fields = new HashMap<>();
    fields.put(fieldPath, data);

    Preconditions.checkArgument(
        moreFieldsAndValues.length % 2 == 0, "moreFieldsAndValues must be key-value pairs.");
    for (int i = 0; i < moreFieldsAndValues.length; i += 2) {
      Object objectPath = moreFieldsAndValues[i];
      Object objectValue = moreFieldsAndValues[i + 1];

      FieldPath currentPath;

      if (objectPath instanceof String) {
        currentPath = FieldPath.fromDotSeparatedString((String) objectPath);
      } else if (objectPath instanceof FieldPath) {
        currentPath = (FieldPath) objectPath;
      } else {
        throw new IllegalArgumentException(
            "Field '" + objectPath + "' is not of type String or Field Path.");
      }

      if (fields.containsKey(currentPath)) {
        throw new IllegalArgumentException(
            "Field value for field '" + objectPath + "' was specified multiple times.");
      }

      fields.put(currentPath, objectValue);
    }

    return performUpdate(documentReference, fields, precondition);
  }

  private T performUpdate(
      @Nonnull DocumentReference documentReference,
      @Nonnull final Map<FieldPath, Object> fields,
      @Nonnull Precondition precondition) {
    verifyNotCommitted();
    Preconditions.checkArgument(!fields.isEmpty(), "Data for update() cannot be empty.");

    Map<String, Object> deconstructedMap = expandObject(fields);
    DocumentSnapshot documentSnapshot =
        DocumentSnapshot.fromObject(
            firestore,
            documentReference,
            deconstructedMap,
            new EncodingOptions() {
              @Override
              public boolean allowDelete(FieldPath fieldPath) {
                return fields.containsKey(fieldPath);
              }

              @Override
              public boolean allowTransform() {
                return true;
              }
            });
    List<FieldPath> fieldPaths = new ArrayList<>(fields.keySet());
    DocumentTransform documentTransform =
        DocumentTransform.fromFieldPathMap(documentReference, fields);
    fieldPaths.removeAll(documentTransform.getFields());
    FieldMask fieldMask = new FieldMask(fieldPaths);

    Write.Builder write = documentSnapshot.toPb();
    write.setCurrentDocument(precondition.toPb());
    write.setUpdateMask(fieldMask.toPb());

    if (!documentTransform.isEmpty()) {
      write.addAllUpdateTransforms(documentTransform.toPb());
    }
    writes.add(write);

    return wrapResult(processOperation(documentReference));
  }

  /**
   * Deletes the document referred to by this DocumentReference.
   *
   * @param documentReference The DocumentReference to delete.
   * @param precondition Precondition for the delete operation.
   * @return The instance for chaining.
   */
  @Nonnull
  public T delete(
      @Nonnull DocumentReference documentReference, @Nonnull Precondition precondition) {
    return performDelete(documentReference, precondition);
  }

  /**
   * Deletes the document referred to by this DocumentReference.
   *
   * @param documentReference The DocumentReference to delete.
   * @return The instance for chaining.
   */
  @Nonnull
  public T delete(@Nonnull DocumentReference documentReference) {
    return performDelete(documentReference, Precondition.NONE);
  }

  private T performDelete(
      @Nonnull DocumentReference documentReference, @Nonnull Precondition precondition) {
    verifyNotCommitted();
    Write.Builder write = Write.newBuilder().setDelete(documentReference.getName());

    if (!precondition.isEmpty()) {
      write.setCurrentDocument(precondition.toPb());
    }
    writes.add(write);

    return wrapResult(processOperation(documentReference));
  }

  /** Commit the current batch. */
  ApiFuture<List<WriteResult>> commit(@Nullable ByteString transactionId) {
    Tracing.getTracer()
        .getCurrentSpan()
        .addAnnotation(
            "CloudFirestore.Commit",
            ImmutableMap.of("numDocuments", AttributeValue.longAttributeValue(writes.size())));

    final CommitRequest.Builder request = CommitRequest.newBuilder();
    request.setDatabase(firestore.getDatabaseName());

    for (Write.Builder write : writes) {
      request.addWrites(write);
    }

    if (transactionId != null) {
      request.setTransaction(transactionId);
    }

    committed = true;

    ApiFuture<CommitResponse> response =
        firestore.sendRequest(request.build(), firestore.getClient().commitCallable());

    return ApiFutures.transform(
        response,
        new ApiFunction<CommitResponse, List<WriteResult>>() {
          @Override
          public List<WriteResult> apply(CommitResponse commitResponse) {
            List<com.google.firestore.v1.WriteResult> writeResults =
                commitResponse.getWriteResultsList();

            List<WriteResult> result = new ArrayList<>();

            for (com.google.firestore.v1.WriteResult writeResult : writeResults) {
              result.add(WriteResult.fromProto(writeResult, commitResponse.getCommitTime()));
            }

            return result;
          }
        },
        MoreExecutors.directExecutor());
  }

  /**
   * Commits all pending operations to the database and verifies all preconditions.
   *
   * <p>The writes in the batch are not applied atomically and can be applied out of order.
   */
  ApiFuture<List<BatchWriteResult>> bulkCommit() {
    Tracing.getTracer()
        .getCurrentSpan()
        .addAnnotation(
            "CloudFirestore.BatchWrite",
            ImmutableMap.of("numDocuments", AttributeValue.longAttributeValue(writes.size())));

    Preconditions.checkState(
        state == BatchState.READY_TO_SEND,
        "The batch should be marked as READY_TO_SEND before committing");
    state = BatchState.SENT;

    final BatchWriteRequest.Builder request = BatchWriteRequest.newBuilder();
    request.setDatabase(firestore.getDatabaseName());

    for (Write.Builder write : writes) {
      request.addWrites(write);
    }

    committed = true;

    ApiFuture<BatchWriteResponse> response =
        firestore.sendRequest(request.build(), firestore.getClient().batchWriteCallable());

    return ApiFutures.transform(
        response,
        new ApiFunction<BatchWriteResponse, List<BatchWriteResult>>() {
          @Override
          public List<BatchWriteResult> apply(BatchWriteResponse batchWriteResponse) {
            List<com.google.firestore.v1.WriteResult> writeResults =
                batchWriteResponse.getWriteResultsList();

            List<com.google.rpc.Status> statuses = batchWriteResponse.getStatusList();

            List<BatchWriteResult> result = new ArrayList<>();

            for (int i = 0; i < writeResults.size(); ++i) {
              com.google.firestore.v1.WriteResult writeResult = writeResults.get(i);
              com.google.rpc.Status status = statuses.get(i);
              Status code = Status.fromCodeValue(status.getCode());
              Timestamp updateTime =
                  code == Status.OK ? Timestamp.fromProto(writeResult.getUpdateTime()) : null;
              result.add(new BatchWriteResult(updateTime, code, status.getMessage()));
            }

            return result;
          }
        },
        MoreExecutors.directExecutor());
  }

  /** Checks whether any updates have been queued. */
  boolean isEmpty() {
    return writes.isEmpty();
  }

  /** Get the number of writes. */
  public int getMutationsSize() {
    return writes.size();
  }

  BatchState getState() {
    return state;
  }

  Set<DocumentReference> getDocuments() {
    return documents;
  }

  int getOperationCount() {
    return resultsMap.size();
  }

  private ApiFuture<WriteResult> processOperation(DocumentReference documentReference) {
    Preconditions.checkState(
        allowDuplicateDocs() || !documents.contains(documentReference.getPath()),
        "Batch should not contain writes to the same document");
    Preconditions.checkState(state == BatchState.OPEN, "Batch should be OPEN when adding writes");
    documents.add(documentReference);
    SettableApiFuture<WriteResult> result = SettableApiFuture.create();
    resultsMap.put(getOperationCount(), result);

    if (getOperationCount() == maxBatchSize) {
      state = BatchState.READY_TO_SEND;
    }

    return result;
  }
  /** Resolves the individual operations in the batch with the results. */
  void processResults(List<BatchWriteResult> results, @Nullable Exception error) {
    for (int i = 0; i < resultsMap.size(); ++i) {
      if (error == null) {
        SettableApiFuture<WriteResult> future = resultsMap.get(i);
        convertBatchWriteResult(results.get(i), future);
      } else {
        resultsMap.get(i).setException(error);
      }
    }
    completeFuture.set(null);
  }

  private void convertBatchWriteResult(
      BatchWriteResult result, SettableApiFuture<WriteResult> future) {
    if (result.getWriteTime() != null) {
      future.set(new WriteResult(result.getWriteTime()));
    } else {
      future.setException(FirestoreException.serverRejected(result.getStatus(), result.getMessage() ));
    }
  }

  /** Returns a future that returns when the batch has been sent, and a response is received. */
  ApiFuture<Void> awaitBulkCommit() {
    markReadyToSend();
    return completeFuture;
  }

  void markReadyToSend() {
    if (state == BatchState.OPEN) {
      state = BatchState.READY_TO_SEND;
    }
  }
}
