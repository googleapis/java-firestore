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

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.UserDataConverter.EncodingOptions;
import com.google.common.base.Preconditions;
import com.google.firestore.v1.Document;
import com.google.firestore.v1.Value;
import com.google.firestore.v1.Write;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A DocumentSnapshot contains data read from a document in a Firestore database. The data can be
 * extracted with the {@link #getData()} or {@link #get(String)} methods.
 *
 * <p>If the DocumentSnapshot points to a non-existing document, getData() and its corresponding
 * methods will return null. You can always explicitly check for a document's existence by calling
 * {@link #exists()}.
 *
 * <p><b>Subclassing Note</b>: Firestore classes are not meant to be subclassed except for use in
 * test mocks. Subclassing is not supported in production code and new SDK releases may break code
 * that does so.
 */
public class DocumentSnapshot {

  private final FirestoreImpl firestore;
  private final DocumentReference docRef;
  @Nullable private final Map<String, Value> fields;
  @Nullable private final Timestamp readTime;
  @Nullable private final Timestamp updateTime;
  @Nullable private final Timestamp createTime;

  protected DocumentSnapshot(
      FirestoreImpl firestore,
      DocumentReference docRef,
      @Nullable Map<String, Value> fields,
      @Nullable Timestamp readTime,
      @Nullable Timestamp updateTime,
      @Nullable Timestamp createTime) { // Elevated access level for mocking.
    this.firestore = firestore;
    this.docRef = docRef;
    this.fields = fields;
    this.readTime = readTime;
    this.updateTime = updateTime;
    this.createTime = createTime;
  }

  /**
   * Returns the ID of the document contained in this snapshot.
   *
   * @return The id of the document.
   */
  @Nonnull
  public String getId() {
    return docRef.getId();
  }

  static DocumentSnapshot fromObject(
      FirestoreImpl firestore,
      DocumentReference docRef,
      Map<String, Object> values,
      EncodingOptions options) {
    Map<String, Value> fields = new HashMap<>();
    for (Map.Entry<String, Object> entry : values.entrySet()) {
      Value encodedValue =
          UserDataConverter.encodeValue(FieldPath.of(entry.getKey()), entry.getValue(), options);
      if (encodedValue != null) {
        fields.put(entry.getKey(), encodedValue);
      }
    }
    return new DocumentSnapshot(firestore, docRef, fields, null, null, null);
  }

  static DocumentSnapshot fromDocument(
      FirestoreImpl firestore, Timestamp readTime, Document document) {
    return new DocumentSnapshot(
        firestore,
        new DocumentReference(firestore, ResourcePath.create(document.getName())),
        document.getFieldsMap(),
        readTime,
        Timestamp.fromProto(document.getUpdateTime()),
        Timestamp.fromProto(document.getCreateTime()));
  }

  static DocumentSnapshot fromMissing(
      FirestoreImpl firestore, DocumentReference documentReference, Timestamp readTime) {
    return new DocumentSnapshot(firestore, documentReference, null, readTime, null, null);
  }

  private Object decodeValue(Value v) {
    Value.ValueTypeCase typeCase = v.getValueTypeCase();
    switch (typeCase) {
      case NULL_VALUE:
        return null;
      case BOOLEAN_VALUE:
        return v.getBooleanValue();
      case INTEGER_VALUE:
        return v.getIntegerValue();
      case DOUBLE_VALUE:
        return v.getDoubleValue();
      case TIMESTAMP_VALUE:
        return Timestamp.fromProto(v.getTimestampValue());
      case STRING_VALUE:
        return v.getStringValue();
      case BYTES_VALUE:
        return Blob.fromByteString(v.getBytesValue());
      case REFERENCE_VALUE:
        String pathName = v.getReferenceValue();
        return new DocumentReference(firestore, ResourcePath.create(pathName));
      case GEO_POINT_VALUE:
        return new GeoPoint(
            v.getGeoPointValue().getLatitude(), v.getGeoPointValue().getLongitude());
      case ARRAY_VALUE:
        List<Object> list = new ArrayList<>();
        List<Value> lv = v.getArrayValue().getValuesList();
        for (Value iv : lv) {
          list.add(decodeValue(iv));
        }
        return list;
      case MAP_VALUE:
        Map<String, Object> outputMap = new HashMap<>();
        Map<String, Value> inputMap = v.getMapValue().getFieldsMap();
        for (Map.Entry<String, Value> entry : inputMap.entrySet()) {
          outputMap.put(entry.getKey(), decodeValue(entry.getValue()));
        }
        return outputMap;
      default:
        throw FirestoreException.invalidState(String.format("Unknown Value Type: %s", typeCase));
    }
  }

  /**
   * Returns the time at which this snapshot was read.
   *
   * @return The read time of this snapshot.
   */
  @Nullable
  public Timestamp getReadTime() {
    return readTime;
  }

  /**
   * Returns the time at which this document was last updated. Returns null for non-existing
   * documents.
   *
   * @return The last time the document in the snapshot was updated. Null if the document doesn't
   *     exist.
   */
  @Nullable
  public Timestamp getUpdateTime() {
    return updateTime;
  }

  /**
   * Returns the time at which this document was created. Returns null for non-existing documents.
   *
   * @return The last time the document in the snapshot was created. Null if the document doesn't
   *     exist.
   */
  @Nullable
  public Timestamp getCreateTime() {
    return createTime;
  }

  /**
   * Returns whether or not the field exists in the document. Returns false if the document does not
   * exist.
   *
   * @return whether the document existed in this snapshot.
   */
  public boolean exists() {
    return fields != null;
  }

  /** Checks whether this DocumentSnapshot contains any fields. */
  boolean isEmpty() {
    return fields == null || fields.isEmpty();
  }

  /**
   * Returns the fields of the document as a Map or null if the document doesn't exist. Field values
   * will be converted to their native Java representation.
   *
   * @return The fields of the document as a Map or null if the document doesn't exist.
   */
  @Nullable
  public Map<String, Object> getData() {
    if (fields == null) {
      return null;
    }

    Map<String, Object> decodedFields = new HashMap<>();
    for (Map.Entry<String, Value> entry : fields.entrySet()) {
      Object decodedValue = decodeValue(entry.getValue());
      decodedValue = convertToDateIfNecessary(decodedValue);
      decodedFields.put(entry.getKey(), decodedValue);
    }
    return decodedFields;
  }

  /**
   * Returns the contents of the document converted to a POJO or null if the document doesn't exist.
   *
   * @param valueType The Java class to create
   * @return The contents of the document in an object of type T or null if the document doesn't
   *     exist.
   */
  @Nullable
  public <T> T toObject(@Nonnull Class<T> valueType) {
    Map<String, Object> data = getData();
    return data == null ? null : CustomClassMapper.convertToCustomClass(getData(), valueType);
  }

  /**
   * Returns whether or not the field exists in the document. Returns false if the document does not
   * exist.
   *
   * @param field the path to the field.
   * @return true iff the field exists.
   */
  public boolean contains(@Nonnull String field) {
    return contains(FieldPath.fromDotSeparatedString(field));
  }

  /**
   * Returns whether or not the field exists in the document. Returns false if the document does not
   * exist.
   *
   * @param fieldPath the path to the field.
   * @return true iff the field exists.
   */
  public boolean contains(@Nonnull FieldPath fieldPath) {
    return this.extractField(fieldPath) != null;
  }

  /**
   * Returns the value at the field or null if the field doesn't exist.
   *
   * @param field The path to the field.
   * @return The value at the given field or null.
   */
  @Nullable
  public Object get(@Nonnull String field) {
    return get(FieldPath.fromDotSeparatedString(field));
  }

  /**
   * Returns the value at the field or null if the field doesn't exist.
   *
   * @param fieldPath The path to the field.
   * @return The value at the given field or null.
   */
  @Nullable
  public Object get(@Nonnull FieldPath fieldPath) {
    Value value = extractField(fieldPath);

    if (value == null) {
      return null;
    }

    Object decodedValue = decodeValue(value);
    return convertToDateIfNecessary(decodedValue);
  }

  private Object convertToDateIfNecessary(Object decodedValue) {
    if (decodedValue instanceof Timestamp) {
      if (!this.firestore.areTimestampsInSnapshotsEnabled()) {
        decodedValue = ((Timestamp) decodedValue).toDate();
      }
    }
    return decodedValue;
  }

  /** Returns the Value Proto at 'fieldPath'. Returns null if the field was not found. */
  @Nullable
  Value extractField(@Nonnull FieldPath fieldPath) {
    Value value = null;

    if (fields != null) {
      Iterator<String> components = fieldPath.getSegments().iterator();
      value = fields.get(components.next());

      while (value != null && components.hasNext()) {
        if (value.getValueTypeCase() != Value.ValueTypeCase.MAP_VALUE) {
          return null;
        }
        value = value.getMapValue().getFieldsOrDefault(components.next(), null);
      }
    }

    return value;
  }

  /**
   * Returns the value of the field as a boolean.
   *
   * @param field The path to the field.
   * @throws RuntimeException if the value is not a Boolean.
   * @return The value of the field.
   */
  @Nullable
  public Boolean getBoolean(@Nonnull String field) {
    return (Boolean) get(field);
  }

  /**
   * Returns the value of the field as a double.
   *
   * @param field The path to the field.
   * @throws RuntimeException if the value is not a Number.
   * @return The value of the field.
   */
  @Nullable
  public Double getDouble(@Nonnull String field) {
    Number number = (Number) get(field);
    return number == null ? null : number.doubleValue();
  }

  /**
   * Returns the value of the field as a String.
   *
   * @param field The path to the field.
   * @throws RuntimeException if the value is not a String.
   * @return The value of the field.
   */
  @Nullable
  public String getString(@Nonnull String field) {
    return (String) get(field);
  }

  /**
   * Returns the value of the field as a long.
   *
   * @param field The path to the field.
   * @throws RuntimeException if the value is not a Number.
   * @return The value of the field.
   */
  @Nullable
  public Long getLong(@Nonnull String field) {
    Number number = (Number) get(field);
    return number == null ? null : number.longValue();
  }

  /**
   * Returns the value of the field as a Date.
   *
   * <p>This method ignores the global setting {@link
   * FirestoreOptions#areTimestampsInSnapshotsEnabled}.
   *
   * @param field The path to the field.
   * @throws RuntimeException if the value is not a Date.
   * @return The value of the field.
   */
  @Nullable
  public Date getDate(@Nonnull String field) {
    return ((Timestamp) get(field)).toDate();
  }

  /**
   * Returns the value of the field as a {@link Timestamp}.
   *
   * <p>This method ignores the global setting {@link
   * FirestoreOptions#areTimestampsInSnapshotsEnabled}.
   *
   * @param field The path to the field.
   * @throws RuntimeException if the value is not a Date.
   * @return The value of the field.
   */
  @Nullable
  public Timestamp getTimestamp(@Nonnull String field) {
    return (Timestamp) get(field);
  }

  /**
   * Returns the value of the field as a Blob.
   *
   * @param field The path to the field.
   * @throws RuntimeException if the value is not a Blob.
   * @return The value of the field.
   */
  @Nullable
  public Blob getBlob(@Nonnull String field) {
    return (Blob) get(field);
  }

  /**
   * Returns the value of the field as a GeoPoint.
   *
   * @param field The path to the field.
   * @throws RuntimeException if the value is not a GeoPoint.
   * @return The value of the field.
   */
  @Nullable
  public GeoPoint getGeoPoint(@Nonnull String field) {
    return (GeoPoint) get(field);
  }

  /**
   * Gets the reference to the document.
   *
   * @return The reference to the document.
   */
  @Nonnull
  public DocumentReference getReference() {
    return docRef;
  }

  Write.Builder toPb() {
    Preconditions.checkState(exists(), "Can't call toDocument() on a document that doesn't exist");
    Write.Builder write = Write.newBuilder();
    Document.Builder document = write.getUpdateBuilder();
    document.setName(docRef.getName());
    document.putAllFields(fields);
    return write;
  }

  /**
   * Returns true if the document's data and path in this DocumentSnapshot equals the provided
   * snapshot.
   *
   * @param obj The object to compare against.
   * @return Whether this DocumentSnapshot is equal to the provided object.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || !(obj instanceof DocumentSnapshot)) {
      return false;
    }
    DocumentSnapshot that = (DocumentSnapshot) obj;
    return Objects.equals(firestore, that.firestore)
        && Objects.equals(docRef, that.docRef)
        && Objects.equals(fields, that.fields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firestore, docRef, fields);
  }
}
