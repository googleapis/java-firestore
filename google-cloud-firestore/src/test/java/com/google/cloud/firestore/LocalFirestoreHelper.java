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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.Timestamp;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.firestore.v1.ArrayValue;
import com.google.firestore.v1.BatchGetDocumentsRequest;
import com.google.firestore.v1.BatchGetDocumentsResponse;
import com.google.firestore.v1.BatchWriteRequest;
import com.google.firestore.v1.BeginTransactionRequest;
import com.google.firestore.v1.BeginTransactionResponse;
import com.google.firestore.v1.CommitRequest;
import com.google.firestore.v1.CommitResponse;
import com.google.firestore.v1.Cursor;
import com.google.firestore.v1.DatabaseRootName;
import com.google.firestore.v1.Document;
import com.google.firestore.v1.DocumentMask;
import com.google.firestore.v1.DocumentTransform.FieldTransform;
import com.google.firestore.v1.MapValue;
import com.google.firestore.v1.Precondition;
import com.google.firestore.v1.RollbackRequest;
import com.google.firestore.v1.RunQueryRequest;
import com.google.firestore.v1.RunQueryResponse;
import com.google.firestore.v1.StructuredQuery;
import com.google.firestore.v1.StructuredQuery.CollectionSelector;
import com.google.firestore.v1.StructuredQuery.CompositeFilter;
import com.google.firestore.v1.StructuredQuery.FieldFilter;
import com.google.firestore.v1.StructuredQuery.UnaryFilter;
import com.google.firestore.v1.Value;
import com.google.firestore.v1.Write;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.google.protobuf.NullValue;
import com.google.type.LatLng;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.threeten.bp.Duration;

public final class LocalFirestoreHelper {

  protected static RetrySettings IMMEDIATE_RETRY_SETTINGS =
      RetrySettings.newBuilder()
          .setInitialRetryDelay(Duration.ZERO)
          .setMaxRetryDelay(Duration.ZERO)
          .setRetryDelayMultiplier(1)
          .setJittered(false)
          .build();

  public static final String DATABASE_NAME;
  public static final String COLLECTION_ID;
  public static final String DOCUMENT_PATH;
  public static final String DOCUMENT_NAME;
  public static final String DOCUMENT_ROOT;
  public static final String TRANSACTION_ID;

  public static final Map<String, Value> EMPTY_MAP_PROTO;

  public static final Map<String, Object> SINGLE_FIELD_MAP;
  public static final Map<String, Object> SINGLE_FILED_MAP_WITH_DOT;
  public static final SingleField SINGLE_FIELD_OBJECT;
  public static final Map<String, Value> SINGLE_FIELD_PROTO;
  public static final DocumentSnapshot SINGLE_FIELD_SNAPSHOT;
  public static final Value SINGLE_FIELD_VALUE;
  public static final SingleField UPDATE_SINGLE_FIELD_OBJECT;
  public static final Map<String, Object> UPDATED_FIELD_MAP;
  public static final Map<String, Value> UPDATED_FIELD_PROTO;
  public static final Map<String, Value> UPDATED_SINGLE_FIELD_PROTO;
  public static final Map<String, Value> UPDATED_POJO_PROTO;

  public static final Map<String, Float> SINGLE_FLOAT_MAP;
  public static final Map<String, Value> SINGLE_FLOAT_PROTO;

  public static final NestedClass NESTED_CLASS_OBJECT;

  public static final Map<String, Object> SERVER_TIMESTAMP_MAP;
  public static final ServerTimestamp SERVER_TIMESTAMP_OBJECT;
  public static final Map<String, Value> SERVER_TIMESTAMP_PROTO;

  public static final Map<String, Object> ALL_SUPPORTED_TYPES_MAP;
  public static final AllSupportedTypes ALL_SUPPORTED_TYPES_OBJECT;
  public static final Map<String, Value> ALL_SUPPORTED_TYPES_PROTO;

  public static final CommitRequest SINGLE_CREATE_COMMIT_REQUEST;
  public static final ApiFuture<CommitResponse> SINGLE_DELETE_COMMIT_RESPONSE;
  public static final ApiFuture<CommitResponse> SINGLE_WRITE_COMMIT_RESPONSE;

  public static final ApiFuture<CommitResponse> FIELD_TRANSFORM_COMMIT_RESPONSE;

  public static final Map<String, Object> UPDATED_POJO;

  public static final Date DATE;
  public static final Timestamp TIMESTAMP;
  public static final GeoPoint GEO_POINT;
  public static final Blob BLOB;
  public static final FooList<SingleField> FOO_LIST = new FooList<>();

  public static final Precondition UPDATE_PRECONDITION;

  public static class SingleField {

    public String foo = "bar";

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      SingleField that = (SingleField) o;

      return foo.equals(that.foo);
    }
  }

  public static class FooList<E> extends ArrayList<SingleField> {
    public FooList() {
      super();
    }
  }

  public static class CustomList {
    public CustomList() {}

    public FooList<SingleField> fooList;
  }

  public static class NestedClass {
    public SingleField first = new SingleField();
    public AllSupportedTypes second = new AllSupportedTypes();
  }

  public static class ServerTimestamp {

    @com.google.cloud.firestore.annotation.ServerTimestamp public Date foo;
    public Inner inner = new Inner();

    class Inner {

      @com.google.cloud.firestore.annotation.ServerTimestamp public Date bar;
    }
  }

  public static class InvalidPOJO {
    @Nullable BigInteger bigIntegerValue = null;
    @Nullable Byte byteValue = null;
    @Nullable Short shortValue = null;

    @Nullable
    public BigInteger getBigIntegerValue() {
      return bigIntegerValue;
    }

    public void setBigIntegerValue(@Nullable BigInteger bigIntegerValue) {
      this.bigIntegerValue = bigIntegerValue;
    }

    @Nullable
    public Byte getByteValue() {
      return byteValue;
    }

    public void setByteValue(@Nullable Byte byteValue) {
      this.byteValue = byteValue;
    }

    @Nullable
    public Short getShortValue() {
      return shortValue;
    }

    public void setShortValue(@Nullable Short shortValue) {
      this.shortValue = shortValue;
    }
  }

  public static <K, V> Map<K, V> map(K key, V value, Object... moreKeysAndValues) {
    Map<K, V> map = new HashMap<>();
    map.put(key, value);

    for (int i = 0; i < moreKeysAndValues.length; i += 2) {
      map.put((K) moreKeysAndValues[i], (V) moreKeysAndValues[i + 1]);
    }

    return map;
  }

  public static Map<String, Object> map() {
    return new HashMap<>();
  }

  public static Answer<BatchGetDocumentsResponse> getAllResponse(
      final Map<String, Value>... fields) {
    BatchGetDocumentsResponse[] responses = new BatchGetDocumentsResponse[fields.length];

    for (int i = 0; i < fields.length; ++i) {
      String name = DOCUMENT_NAME;
      if (fields.length > 1) {
        name += i + 1;
      }
      BatchGetDocumentsResponse.Builder response = BatchGetDocumentsResponse.newBuilder();
      response
          .getFoundBuilder()
          .setCreateTime(com.google.protobuf.Timestamp.newBuilder().setSeconds(1).setNanos(2));
      response
          .getFoundBuilder()
          .setUpdateTime(com.google.protobuf.Timestamp.newBuilder().setSeconds(3).setNanos(4));
      response.setReadTime(com.google.protobuf.Timestamp.newBuilder().setSeconds(5).setNanos(6));
      response.getFoundBuilder().setName(name).putAllFields(fields[i]);
      responses[i] = response.build();
    }

    return streamingResponse(responses);
  }

  public static ApiFuture<Empty> rollbackResponse() {
    return ApiFutures.immediateFuture(Empty.getDefaultInstance());
  }

  public static Answer<RunQueryResponse> queryResponse() {
    return queryResponse(DOCUMENT_NAME);
  }

  public static Answer<RunQueryResponse> queryResponse(String... documentNames) {
    RunQueryResponse[] responses = new RunQueryResponse[documentNames.length];

    for (int i = 0; i < documentNames.length; ++i) {
      final RunQueryResponse.Builder runQueryResponse = RunQueryResponse.newBuilder();
      runQueryResponse.setDocument(
          Document.newBuilder().setName(documentNames[i]).putAllFields(SINGLE_FIELD_PROTO));
      runQueryResponse.setReadTime(
          com.google.protobuf.Timestamp.newBuilder().setSeconds(1).setNanos(2));
      responses[i] = runQueryResponse.build();
    }
    return streamingResponse(responses);
  }

  public static <T> Answer<T> streamingResponse(final T... response) {
    return new Answer<T>() {
      public T answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        ApiStreamObserver<T> observer = (ApiStreamObserver<T>) args[1];
        for (T resp : response) {
          observer.onNext(resp);
        }
        observer.onCompleted();
        return null;
      }
    };
  }

  public static ApiFuture<CommitResponse> commitResponse(int adds, int deletes) {
    CommitResponse.Builder commitResponse = CommitResponse.newBuilder();
    commitResponse.getCommitTimeBuilder().setSeconds(0).setNanos(0);
    for (int i = 0; i < adds; ++i) {
      commitResponse.addWriteResultsBuilder().getUpdateTimeBuilder().setSeconds(i).setNanos(i);
    }
    for (int i = 0; i < deletes; ++i) {
      commitResponse.addWriteResultsBuilder();
    }
    return ApiFutures.immediateFuture(commitResponse.build());
  }

  public static BeginTransactionRequest begin() {
    return begin(null);
  }

  public static BeginTransactionRequest begin(@Nullable String previousTransactionId) {
    BeginTransactionRequest.Builder begin = BeginTransactionRequest.newBuilder();
    begin.setDatabase(DATABASE_NAME);

    if (previousTransactionId != null) {
      begin
          .getOptionsBuilder()
          .getReadWriteBuilder()
          .setRetryTransaction(ByteString.copyFromUtf8(previousTransactionId));
    }

    return begin.build();
  }

  public static ApiFuture<BeginTransactionResponse> beginResponse() {
    return beginResponse(TRANSACTION_ID);
  }

  public static ApiFuture<BeginTransactionResponse> beginResponse(String transactionId) {
    BeginTransactionResponse.Builder beginResponse = BeginTransactionResponse.newBuilder();
    beginResponse.setTransaction(ByteString.copyFromUtf8(transactionId));
    return ApiFutures.immediateFuture(beginResponse.build());
  }

  public static RollbackRequest rollback() {
    return rollback(TRANSACTION_ID);
  }

  public static RollbackRequest rollback(String transactionId) {
    RollbackRequest.Builder rollback = RollbackRequest.newBuilder();
    rollback.setDatabase(DATABASE_NAME);
    rollback.setTransaction(ByteString.copyFromUtf8(transactionId));
    return rollback.build();
  }

  public static FieldTransform serverTimestamp() {
    return FieldTransform.newBuilder()
        .setSetToServerValue(FieldTransform.ServerValue.REQUEST_TIME)
        .build();
  }

  public static FieldTransform increment(Value value) {
    return FieldTransform.newBuilder().setIncrement(value).build();
  }

  public static FieldTransform arrayUnion(Value... values) {
    return FieldTransform.newBuilder()
        .setAppendMissingElements(ArrayValue.newBuilder().addAllValues(Arrays.asList(values)))
        .build();
  }

  public static FieldTransform arrayRemove(Value... values) {
    return FieldTransform.newBuilder()
        .setRemoveAllFromArray(ArrayValue.newBuilder().addAllValues(Arrays.asList(values)))
        .build();
  }

  public static List<FieldTransform> transform(
      String fieldPath, FieldTransform fieldTransform, Object... fieldPathOrTransform) {

    List<FieldTransform> transforms = new ArrayList<>();
    FieldTransform.Builder transformBuilder = FieldTransform.newBuilder();
    transformBuilder.setFieldPath(fieldPath).mergeFrom(fieldTransform);

    transforms.add(transformBuilder.build());

    for (int i = 0; i < fieldPathOrTransform.length; i += 2) {
      String path = (String) fieldPathOrTransform[i];
      FieldTransform transform = (FieldTransform) fieldPathOrTransform[i + 1];
      transforms.add(FieldTransform.newBuilder().setFieldPath(path).mergeFrom(transform).build());
    }
    return transforms;
  }

  public static Write create(Map<String, Value> fields, String docPath) {
    Write.Builder write = Write.newBuilder();
    Document.Builder document = write.getUpdateBuilder();
    document.setName(DOCUMENT_ROOT + docPath);
    document.putAllFields(fields);
    write.getCurrentDocumentBuilder().setExists(false);
    return write.build();
  }

  public static Write create(Map<String, Value> fields) {
    return create(fields, DOCUMENT_PATH);
  }

  public static Write set(Map<String, Value> fields) {
    return set(fields, null, DOCUMENT_PATH);
  }

  public static Write set(Map<String, Value> fields, String docPath) {
    return set(fields, null, docPath);
  }

  public static Write set(Map<String, Value> fields, @Nullable List<String> fieldMap) {
    return set(fields, fieldMap, DOCUMENT_PATH);
  }

  public static Write set(
      Map<String, Value> fields, @Nullable List<String> fieldMap, String docPath) {
    Write.Builder write = Write.newBuilder();
    Document.Builder document = write.getUpdateBuilder();
    document.setName(DOCUMENT_ROOT + docPath);
    document.putAllFields(fields);

    if (fieldMap != null) {
      write.getUpdateMaskBuilder().addAllFieldPaths(fieldMap);
    }

    return write.build();
  }

  public static Write delete() {
    return delete(DOCUMENT_PATH);
  }

  public static Write delete(Precondition precondition) {
    Write.Builder write = Write.newBuilder();
    write.setDelete(DOCUMENT_NAME);
    write.getCurrentDocumentBuilder().mergeFrom(precondition);
    return write.build();
  }

  public static Write delete(String docPath) {
    Write.Builder write = Write.newBuilder();
    write.setDelete(DOCUMENT_ROOT + docPath);
    return write.build();
  }

  public static Write update(Map<String, Value> fields, List<String> fieldMap) {
    return update(fields, fieldMap, UPDATE_PRECONDITION);
  }

  public static Write update(
      Map<String, Value> fields, List<String> fieldMap, @Nullable Precondition precondition) {
    return update(fields, fieldMap, precondition, DOCUMENT_PATH);
  }

  public static Write update(Map<String, Value> fields, String docPath) {
    return update(fields, Collections.singletonList("foo"), UPDATE_PRECONDITION, docPath);
  }

  public static Write update(
      Map<String, Value> fields,
      @Nullable List<String> fieldMap,
      @Nullable Precondition precondition,
      String docPath) {
    Write.Builder write = Write.newBuilder();
    Document.Builder document = write.getUpdateBuilder();
    document.setName(DOCUMENT_ROOT + docPath);
    document.putAllFields(fields);
    write.getUpdateMaskBuilder().addAllFieldPaths(fieldMap);
    if (precondition != null) {
      write.getCurrentDocumentBuilder().mergeFrom(precondition);
    }
    return write.build();
  }

  public static CommitRequest commit(@Nullable String transactionId, Write... writes) {
    CommitRequest.Builder commitRequest = CommitRequest.newBuilder();
    commitRequest.setDatabase(DATABASE_NAME);
    commitRequest.addAllWrites(Arrays.asList(writes));

    if (transactionId != null) {
      commitRequest.setTransaction(ByteString.copyFromUtf8(transactionId));
    }

    return commitRequest.build();
  }

  public static CommitRequest commit(Write... writes) {
    return commit(null, writes);
  }

  public static CommitRequest commit(Write write, List<FieldTransform> transforms) {
    return commit((String) null, write.toBuilder().addAllUpdateTransforms(transforms).build());
  }

  public static BatchWriteRequest batchWrite(Write... writes) {
    BatchWriteRequest.Builder batchWriteRequest = BatchWriteRequest.newBuilder();
    batchWriteRequest.setDatabase(DATABASE_NAME);
    batchWriteRequest.addAllWrites(Arrays.asList(writes));
    return batchWriteRequest.build();
  }

  public static StructuredQuery filter(StructuredQuery.FieldFilter.Operator operator) {
    return filter(operator, "foo", "bar");
  }

  public static StructuredQuery filter(
      StructuredQuery.FieldFilter.Operator operator, String path, String value) {
    return filter(operator, path, string(value));
  }

  public static StructuredQuery filter(
      StructuredQuery.FieldFilter.Operator operator, String path, Value value) {
    StructuredQuery.Builder structuredQuery = StructuredQuery.newBuilder();
    StructuredQuery.CompositeFilter.Builder compositeFilter =
        structuredQuery.getWhereBuilder().getCompositeFilterBuilder();
    compositeFilter.setOp(StructuredQuery.CompositeFilter.Operator.AND);

    StructuredQuery.FieldFilter.Builder fieldFilter =
        compositeFilter.addFiltersBuilder().getFieldFilterBuilder();
    fieldFilter.setField(StructuredQuery.FieldReference.newBuilder().setFieldPath(path));
    fieldFilter.setOp(operator);
    fieldFilter.setValue(value);

    return structuredQuery.build();
  }

  public static StructuredQuery unaryFilter(StructuredQuery.UnaryFilter.Operator operator) {
    StructuredQuery.Builder structuredQuery = StructuredQuery.newBuilder();
    StructuredQuery.CompositeFilter.Builder compositeFilter =
        structuredQuery.getWhereBuilder().getCompositeFilterBuilder();
    compositeFilter.setOp(StructuredQuery.CompositeFilter.Operator.AND);

    StructuredQuery.UnaryFilter.Builder fieldFilter =
        compositeFilter.addFiltersBuilder().getUnaryFilterBuilder();
    fieldFilter.setField(StructuredQuery.FieldReference.newBuilder().setFieldPath("foo"));
    fieldFilter.setOp(operator);

    return structuredQuery.build();
  }

  public static RunQueryRequest query(StructuredQuery... query) {
    return query(null, false, query);
  }

  public static RunQueryRequest query(
      @Nullable String transactionId, boolean allDescendants, StructuredQuery... query) {
    RunQueryRequest.Builder request = RunQueryRequest.newBuilder();
    request.setParent(LocalFirestoreHelper.DATABASE_NAME + "/documents");
    StructuredQuery.Builder structuredQuery = request.getStructuredQueryBuilder();

    CollectionSelector collectionSelector =
        CollectionSelector.newBuilder()
            .setCollectionId("coll")
            .setAllDescendants(allDescendants)
            .build();
    structuredQuery.addFrom(collectionSelector);

    for (StructuredQuery option : query) {
      structuredQuery.mergeFrom(option);
    }

    CompositeFilter compositeFilter = structuredQuery.getWhere().getCompositeFilter();
    if (compositeFilter.getFiltersCount() == 1) {
      if (compositeFilter.getFilters(0).hasFieldFilter()) {
        FieldFilter fieldFilter = compositeFilter.getFilters(0).getFieldFilter();
        structuredQuery.getWhereBuilder().setFieldFilter(fieldFilter);
      } else {
        UnaryFilter unaryFilter = compositeFilter.getFilters(0).getUnaryFilter();
        structuredQuery.getWhereBuilder().setUnaryFilter(unaryFilter);
      }
    }

    if (transactionId != null) {
      request.setTransaction(ByteString.copyFromUtf8(transactionId));
    }

    return request.build();
  }

  public static BatchGetDocumentsRequest get() {
    return getAll(null, DOCUMENT_NAME);
  }

  public static BatchGetDocumentsRequest get(@Nullable String transactionId) {
    return getAll(transactionId, DOCUMENT_NAME);
  }

  public static BatchGetDocumentsRequest getAll(
      @Nullable String transactionId, String... documentNames) {
    BatchGetDocumentsRequest.Builder request = BatchGetDocumentsRequest.newBuilder();
    request.setDatabase(DATABASE_NAME);

    for (String documentName : documentNames) {
      request.addDocuments(documentName);
    }

    if (transactionId != null) {
      request.setTransaction(ByteString.copyFromUtf8(transactionId));
    }

    return request.build();
  }

  public static StructuredQuery order(String fieldPath, StructuredQuery.Direction direction) {
    StructuredQuery.Builder structuredQuery = StructuredQuery.newBuilder();
    structuredQuery.addOrderByBuilder().setField(field(fieldPath)).setDirection(direction);
    return structuredQuery.build();
  }

  public static StructuredQuery select(String fieldPath) {
    StructuredQuery.Builder structuredQuery = StructuredQuery.newBuilder();
    structuredQuery.getSelectBuilder().addFields(field(fieldPath));
    return structuredQuery.build();
  }

  public static StructuredQuery select(FieldPath fieldPath) {
    StructuredQuery.Builder structuredQuery = StructuredQuery.newBuilder();
    structuredQuery.getSelectBuilder().addFields(field(fieldPath.getEncodedPath()));
    return structuredQuery.build();
  }

  public static StructuredQuery startAt(boolean before) {
    return startAt(string(), before);
  }

  public static StructuredQuery startAt(Value value, boolean before) {
    StructuredQuery.Builder structuredQuery = StructuredQuery.newBuilder();
    structuredQuery.setStartAt(Cursor.newBuilder().setBefore(before).addValues(value));
    return structuredQuery.build();
  }

  public static StructuredQuery endAt(boolean before) {
    return endAt(string(), before);
  }

  public static StructuredQuery endAt(Value value, boolean before) {
    StructuredQuery.Builder structuredQuery = StructuredQuery.newBuilder();
    structuredQuery.setEndAt(Cursor.newBuilder().setBefore(before).addValues(value));
    return structuredQuery.build();
  }

  public static StructuredQuery limit(int n) {
    StructuredQuery.Builder structuredQuery = StructuredQuery.newBuilder();
    structuredQuery.getLimitBuilder().setValue(n);
    return structuredQuery.build();
  }

  public static StructuredQuery offset(int n) {
    StructuredQuery.Builder structuredQuery = StructuredQuery.newBuilder();
    structuredQuery.setOffset(n);
    return structuredQuery.build();
  }

  public static Value string() {
    return string("bar");
  }

  public static Value string(String value) {
    return Value.newBuilder().setStringValue(value).build();
  }

  public static Value object(String key, Value value) {
    Value.Builder result = Value.newBuilder();
    result.getMapValueBuilder().putFields(key, value);
    return result.build();
  }

  public static Value reference(String value) {
    return Value.newBuilder().setReferenceValue(value).build();
  }

  public static StructuredQuery.FieldReference.Builder field(String fieldPath) {
    return StructuredQuery.FieldReference.newBuilder().setFieldPath(fieldPath);
  }

  public static void assertCommitEquals(CommitRequest expected, CommitRequest actual) {
    assertEquals(sortCommit(expected), sortCommit(actual));
  }

  private static CommitRequest sortCommit(CommitRequest commit) {
    CommitRequest.Builder builder = commit.toBuilder();

    for (Write.Builder writes : builder.getWritesBuilderList()) {
      if (writes.hasUpdateMask()) {
        ArrayList<String> updateMask = new ArrayList<>(writes.getUpdateMask().getFieldPathsList());
        Collections.sort(updateMask);
        writes.setUpdateMask(DocumentMask.newBuilder().addAllFieldPaths(updateMask));
      }

      if (!writes.getUpdateTransformsList().isEmpty()) {
        ArrayList<FieldTransform> transformList = new ArrayList<>(writes.getUpdateTransformsList());
        Collections.sort(
            transformList,
            new Comparator<FieldTransform>() {
              @Override
              public int compare(FieldTransform t1, FieldTransform t2) {
                return t1.getFieldPath().compareTo(t2.getFieldPath());
              }
            });
        writes.clearUpdateTransforms().addAllUpdateTransforms(transformList);
      }
    }

    return builder.build();
  }

  public static class AllSupportedTypes {

    public String foo = "bar";
    public Double doubleValue = 0.0;
    public long longValue = 0L;
    public double nanValue = Double.NaN;
    public double infValue = Double.POSITIVE_INFINITY;
    public double negInfValue = Double.NEGATIVE_INFINITY;
    public boolean trueValue = true;
    public boolean falseValue = false;
    public SingleField objectValue = new SingleField();
    public Date dateValue = DATE;
    public Timestamp timestampValue = TIMESTAMP;
    public List<String> arrayValue = ImmutableList.of("foo");
    public String nullValue = null;
    public Blob bytesValue = BLOB;
    public GeoPoint geoPointValue = GEO_POINT;
    public Map<String, Object> model = ImmutableMap.of("foo", (Object) SINGLE_FIELD_OBJECT.foo);

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      AllSupportedTypes that = (AllSupportedTypes) o;
      return longValue == that.longValue
          && Double.compare(that.nanValue, nanValue) == 0
          && Double.compare(that.infValue, infValue) == 0
          && Double.compare(that.negInfValue, negInfValue) == 0
          && trueValue == that.trueValue
          && falseValue == that.falseValue
          && Objects.equals(foo, that.foo)
          && Objects.equals(doubleValue, that.doubleValue)
          && Objects.equals(objectValue, that.objectValue)
          && Objects.equals(dateValue, that.dateValue)
          && Objects.equals(timestampValue, that.timestampValue)
          && Objects.equals(arrayValue, that.arrayValue)
          && Objects.equals(nullValue, that.nullValue)
          && Objects.equals(bytesValue, that.bytesValue)
          && Objects.equals(geoPointValue, that.geoPointValue)
          && Objects.equals(model, that.model);
    }
  }

  static {
    TRANSACTION_ID = "foo";

    try {
      DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z").parse("1985-03-18 08:20:00.123 CET");
    } catch (ParseException e) {
      throw new RuntimeException("Failed to parse date", e);
    }

    TIMESTAMP =
        Timestamp.ofTimeSecondsAndNanos(
            TimeUnit.MILLISECONDS.toSeconds(DATE.getTime()),
            123000); // Firestore truncates to microsecond precision.
    GEO_POINT = new GeoPoint(50.1430847, -122.9477780);
    BLOB = Blob.fromBytes(new byte[] {1, 2, 3});
    SINGLE_FLOAT_MAP = map("float", 0.1F);
    SINGLE_FLOAT_PROTO = map("float", Value.newBuilder().setDoubleValue((Float) 0.1F).build());

    DATABASE_NAME = "projects/test-project/databases/(default)";
    COLLECTION_ID = "coll";
    DOCUMENT_PATH = "coll/doc";
    DOCUMENT_NAME = DATABASE_NAME + "/documents/" + DOCUMENT_PATH;
    DOCUMENT_ROOT = DATABASE_NAME + "/documents/";

    EMPTY_MAP_PROTO =
        map("inner", Value.newBuilder().setMapValue(MapValue.getDefaultInstance()).build());

    SINGLE_FIELD_MAP = map("foo", (Object) "bar");
    SINGLE_FILED_MAP_WITH_DOT = map("c.d", (Object) "bar");
    SINGLE_FIELD_OBJECT = new SingleField();
    FOO_LIST.add(SINGLE_FIELD_OBJECT);
    SINGLE_FIELD_PROTO = map("foo", Value.newBuilder().setStringValue("bar").build());
    UPDATED_POJO_PROTO =
        map(
            "model",
            Value.newBuilder()
                .setMapValue(
                    MapValue.newBuilder()
                        .putFields("foo", Value.newBuilder().setStringValue("foobar").build()))
                .build());
    SINGLE_FIELD_SNAPSHOT =
        new DocumentSnapshot(
            null,
            new DocumentReference(
                null,
                ResourcePath.create(
                    DatabaseRootName.of("test-project", "(default)"),
                    ImmutableList.of("coll", "doc"))),
            SINGLE_FIELD_PROTO,
            Timestamp.ofTimeSecondsAndNanos(5, 6),
            Timestamp.ofTimeSecondsAndNanos(3, 4),
            Timestamp.ofTimeSecondsAndNanos(1, 2));

    Value.Builder singleFieldValueBuilder = Value.newBuilder();
    singleFieldValueBuilder.getMapValueBuilder().putAllFields(SINGLE_FIELD_PROTO);
    SINGLE_FIELD_VALUE = singleFieldValueBuilder.build();
    UPDATE_SINGLE_FIELD_OBJECT = new SingleField();
    UPDATE_SINGLE_FIELD_OBJECT.foo = "foobar";

    UPDATED_FIELD_MAP = map("foo", (Object) "foobar");
    UPDATED_FIELD_PROTO = map("foo", Value.newBuilder().setStringValue("foobar").build());
    UPDATED_SINGLE_FIELD_PROTO =
        ImmutableMap.<String, Value>builder()
            .put(
                "foo",
                Value.newBuilder()
                    .setMapValue(
                        MapValue.newBuilder()
                            .putFields("foo", Value.newBuilder().setStringValue("foobar").build()))
                    .build())
            .build();
    SERVER_TIMESTAMP_MAP = new HashMap<>();
    SERVER_TIMESTAMP_MAP.put("foo", FieldValue.serverTimestamp());
    SERVER_TIMESTAMP_MAP.put("inner", new HashMap<String, Object>());
    ((Map<String, Object>) SERVER_TIMESTAMP_MAP.get("inner"))
        .put("bar", FieldValue.serverTimestamp());

    Value.Builder mapValue = Value.newBuilder();
    mapValue.getMapValueBuilder();
    SERVER_TIMESTAMP_PROTO = Collections.emptyMap();
    SERVER_TIMESTAMP_OBJECT = new ServerTimestamp();

    ALL_SUPPORTED_TYPES_MAP = new HashMap<>();
    ALL_SUPPORTED_TYPES_MAP.put("foo", "bar");
    ALL_SUPPORTED_TYPES_MAP.put("doubleValue", 0.0);
    ALL_SUPPORTED_TYPES_MAP.put("longValue", 0L);
    ALL_SUPPORTED_TYPES_MAP.put("nanValue", Double.NaN);
    ALL_SUPPORTED_TYPES_MAP.put("infValue", Double.POSITIVE_INFINITY);
    ALL_SUPPORTED_TYPES_MAP.put("negInfValue", Double.NEGATIVE_INFINITY);
    ALL_SUPPORTED_TYPES_MAP.put("trueValue", true);
    ALL_SUPPORTED_TYPES_MAP.put("falseValue", false);
    ALL_SUPPORTED_TYPES_MAP.put("objectValue", map("foo", (Object) "bar"));
    ALL_SUPPORTED_TYPES_MAP.put("dateValue", Timestamp.of(DATE));
    ALL_SUPPORTED_TYPES_MAP.put("timestampValue", TIMESTAMP);
    ALL_SUPPORTED_TYPES_MAP.put("arrayValue", ImmutableList.of("foo"));
    ALL_SUPPORTED_TYPES_MAP.put("nullValue", null);
    ALL_SUPPORTED_TYPES_MAP.put("bytesValue", BLOB);
    ALL_SUPPORTED_TYPES_MAP.put("geoPointValue", GEO_POINT);
    ALL_SUPPORTED_TYPES_MAP.put("model", map("foo", SINGLE_FIELD_OBJECT.foo));
    ALL_SUPPORTED_TYPES_PROTO =
        ImmutableMap.<String, Value>builder()
            .put("foo", Value.newBuilder().setStringValue("bar").build())
            .put("doubleValue", Value.newBuilder().setDoubleValue(0.0).build())
            .put("longValue", Value.newBuilder().setIntegerValue(0L).build())
            .put("nanValue", Value.newBuilder().setDoubleValue(Double.NaN).build())
            .put("infValue", Value.newBuilder().setDoubleValue(Double.POSITIVE_INFINITY).build())
            .put("negInfValue", Value.newBuilder().setDoubleValue(Double.NEGATIVE_INFINITY).build())
            .put("trueValue", Value.newBuilder().setBooleanValue(true).build())
            .put("falseValue", Value.newBuilder().setBooleanValue(false).build())
            .put(
                "objectValue",
                Value.newBuilder()
                    .setMapValue(MapValue.newBuilder().putAllFields(SINGLE_FIELD_PROTO))
                    .build())
            .put(
                "dateValue",
                Value.newBuilder()
                    .setTimestampValue(
                        com.google.protobuf.Timestamp.newBuilder()
                            .setSeconds(479978400)
                            .setNanos(123000000)) // Dates only support millisecond precision.
                    .build())
            .put(
                "timestampValue",
                Value.newBuilder()
                    .setTimestampValue(
                        com.google.protobuf.Timestamp.newBuilder()
                            .setSeconds(479978400)
                            .setNanos(123000)) // Timestamps supports microsecond precision.
                    .build())
            .put(
                "arrayValue",
                Value.newBuilder()
                    .setArrayValue(
                        ArrayValue.newBuilder().addValues(Value.newBuilder().setStringValue("foo")))
                    .build())
            .put("nullValue", Value.newBuilder().setNullValue(NullValue.NULL_VALUE).build())
            .put("bytesValue", Value.newBuilder().setBytesValue(BLOB.toByteString()).build())
            .put(
                "geoPointValue",
                Value.newBuilder()
                    .setGeoPointValue(
                        LatLng.newBuilder().setLatitude(50.1430847).setLongitude(-122.9477780))
                    .build())
            .put(
                "model",
                Value.newBuilder()
                    .setMapValue(MapValue.newBuilder().putAllFields(SINGLE_FIELD_PROTO))
                    .build())
            .build();
    ALL_SUPPORTED_TYPES_OBJECT = new AllSupportedTypes();
    SINGLE_WRITE_COMMIT_RESPONSE = commitResponse(/* adds= */ 1, /* deletes= */ 0);
    SINGLE_DELETE_COMMIT_RESPONSE = commitResponse(/* adds= */ 0, /* deletes= */ 1);
    SINGLE_CREATE_COMMIT_REQUEST = commit(create(SINGLE_FIELD_PROTO));

    FIELD_TRANSFORM_COMMIT_RESPONSE = commitResponse(/* adds= */ 2, /* deletes= */ 0);

    NESTED_CLASS_OBJECT = new NestedClass();

    UPDATE_PRECONDITION = Precondition.newBuilder().setExists(true).build();
    UPDATED_POJO = map("model", (Object) UPDATE_SINGLE_FIELD_OBJECT);
  }

  public static String autoId() {
    return FirestoreImpl.autoId();
  }

  @SuppressWarnings("unchecked")
  public static <T> Map<String, T> mapAnyType(Object... entries) {
    Map<String, T> res = new HashMap<>();
    for (int i = 0; i < entries.length; i += 2) {
      res.put((String) entries[i], (T) entries[i + 1]);
    }
    return res;
  }

  private static Map<String, Object> fromJsonString(String json) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Map<String, Object> fromSingleQuotedString(String json) {
    return fromJsonString(json.replace("'", "\""));
  }

  /**
   * Contains a map of request/response pairs that are used to create stub responses when
   * `sendRequest()` is called.
   */
  static class ResponseStubber
      extends LinkedHashMap<GeneratedMessageV3, ApiFuture<? extends GeneratedMessageV3>> {

    /**
     * Verifies the response before returning. This method can be overridden to perform logic before
     * the stubbed response is returned.
     */
    ApiFuture<? extends GeneratedMessageV3> verifyResponse(
        ApiFuture<? extends GeneratedMessageV3> response) {
      return response;
    }

    void initializeStub(
        ArgumentCaptor<? extends Message> argumentCaptor, FirestoreImpl firestoreMock) {
      Stubber stubber = null;
      for (final ApiFuture<? extends GeneratedMessageV3> response : values()) {
        Answer<ApiFuture<? extends GeneratedMessageV3>> answer =
            new Answer<ApiFuture<? extends GeneratedMessageV3>>() {
              @Override
              public ApiFuture<? extends GeneratedMessageV3> answer(
                  InvocationOnMock invocationOnMock) throws Throwable {
                return verifyResponse(response);
              }
            };
        stubber = (stubber != null) ? stubber.doAnswer(answer) : doAnswer(answer);
      }
      Preconditions.checkNotNull(stubber, "Stubber should not be null");
      stubber
          .when(firestoreMock)
          .sendRequest(argumentCaptor.capture(), Matchers.<UnaryCallable<Message, Message>>any());
    }
  }

  /**
   * Contains a map of request/response pairs that are used to create stub responses when
   * `sendRequest()` is called.
   *
   * <p>Enforces that only one active request can be pending at a time.
   */
  static class SerialResponseStubber extends ResponseStubber {
    int activeRequestCounter = 0;
    SettableApiFuture<Void> activeRequestComplete = SettableApiFuture.create();
    Semaphore semaphore = new Semaphore(0);

    void markAllRequestsComplete() {
      activeRequestComplete.set(null);
    }

    void awaitRequest() {
      activeRequestComplete = SettableApiFuture.create();
      try {
        semaphore.acquire();
      } catch (Exception e) {
        fail("sempahore.acquire() should not fail");
      }
    }

    @Override
    ApiFuture<? extends GeneratedMessageV3> verifyResponse(
        ApiFuture<? extends GeneratedMessageV3> response) {
      ++activeRequestCounter;

      // This assert is used to test that only one request is made at a time.
      assertEquals(1, activeRequestCounter);
      try {
        semaphore.release();
        activeRequestComplete.get();
      } catch (Exception e) {
        fail("activeRequestComplete.get() should not fail");
      }
      --activeRequestCounter;
      return response;
    }
  }
}
