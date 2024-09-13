/*
 * Copyright 2024 Google LLC
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

package com.google.cloud.firestore.encoding;

import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import com.google.cloud.firestore.annotation.ThrowOnExtraProperties;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

/** Base bean mapper class, providing common functionality for class and record serialization. */
abstract class BeanMapper<T> {
  private final Class<T> clazz;
  // Whether to throw exception if there are properties we don't know how to set to
  // custom object fields/setters or record components during deserialization.
  private final boolean throwOnUnknownProperties;
  // Whether to log a message if there are properties we don't know how to set to
  // custom object fields/setters or record components during deserialization.
  private final boolean warnOnUnknownProperties;
  // A set of property names that were annotated with @ServerTimestamp.
  final HashSet<String> serverTimestamps;
  // A set of property names that were annotated with @DocumentId. These properties will be
  // populated with document ID values during deserialization, and be skipped during
  // serialization.
  final HashSet<String> documentIdPropertyNames;

  BeanMapper(Class<T> clazz) {
    this.clazz = clazz;
    throwOnUnknownProperties = clazz.isAnnotationPresent(ThrowOnExtraProperties.class);
    warnOnUnknownProperties = !clazz.isAnnotationPresent(IgnoreExtraProperties.class);
    serverTimestamps = new HashSet<>();
    documentIdPropertyNames = new HashSet<>();
  }

  Class<T> getClazz() {
    return clazz;
  }

  boolean isThrowOnUnknownProperties() {
    return throwOnUnknownProperties;
  }

  boolean isWarnOnUnknownProperties() {
    return warnOnUnknownProperties;
  }

  /**
   * Serialize an object to a map.
   *
   * @param object the object to serialize
   * @param path the path to a specific field/component in an object, for use in error messages
   * @return the map
   */
  abstract Map<String, Object> serialize(T object, DeserializeContext.ErrorPath path);

  /**
   * Deserialize a map to an object.
   *
   * @param values the map to deserialize
   * @param types generic type mappings
   * @param context context information about the deserialization operation
   * @return the deserialized object
   */
  abstract T deserialize(
      Map<String, Object> values,
      Map<TypeVariable<Class<T>>, Type> types,
      DeserializeContext context);

  T deserialize(Map<String, Object> values, DeserializeContext context) {
    return deserialize(values, Collections.emptyMap(), context);
  }

  protected void verifyValidType(T object) {
    if (!clazz.isAssignableFrom(object.getClass())) {
      throw new IllegalArgumentException(
          "Can't serialize object of class "
              + object.getClass()
              + " with BeanMapper for class "
              + clazz);
    }
  }

  protected Type resolveType(Type type, Map<TypeVariable<Class<T>>, Type> types) {
    if (type instanceof TypeVariable) {
      Type resolvedType = types.get(type);
      if (resolvedType == null) {
        throw new IllegalStateException("Could not resolve type " + type);
      }

      return resolvedType;
    }

    return type;
  }

  protected void checkForDocIdConflict(
      String docIdPropertyName,
      Collection<String> deserializedProperties,
      DeserializeContext context) {
    if (deserializedProperties.contains(docIdPropertyName)) {
      String message =
          "'"
              + docIdPropertyName
              + "' was found from document "
              + context.documentRef.getPath()
              + ", cannot apply @DocumentId on this property for class "
              + clazz.getName();
      throw new RuntimeException(message);
    }
  }

  protected Object getSerializedValue(
      String property, Object propertyValue, DeserializeContext.ErrorPath path) {
    if (serverTimestamps.contains(property) && propertyValue == null) {
      // Replace null ServerTimestamp-annotated fields with the sentinel.
      return FieldValue.serverTimestamp();
    } else {
      return CustomClassMapper.serialize(propertyValue, path.child(property));
    }
  }

  protected void applyFieldAnnotations(Field field) {
    Class<?> fieldType = field.getType();
    if (field.isAnnotationPresent(ServerTimestamp.class)) {
      EncodingUtil.validateServerTimestampType("Field", "is", fieldType);
      serverTimestamps.add(EncodingUtil.propertyName(field));
    }
    if (field.isAnnotationPresent(DocumentId.class)) {
      EncodingUtil.validateDocumentIdType("Field", "is", fieldType);
      documentIdPropertyNames.add(EncodingUtil.propertyName(field));
    }
  }

  protected void applyGetterAnnotations(Method method) {
    Class<?> returnType = method.getReturnType();
    if (method.isAnnotationPresent(ServerTimestamp.class)) {
      EncodingUtil.validateServerTimestampType("Method", "returns", returnType);
      serverTimestamps.add(EncodingUtil.propertyName(method));
    }
    // Even though the value will be skipped, we still check for type matching for consistency.
    if (method.isAnnotationPresent(DocumentId.class)) {
      EncodingUtil.validateDocumentIdType("Method", "returns", returnType);
      documentIdPropertyNames.add(EncodingUtil.propertyName(method));
    }
  }

  protected void applySetterAnnotations(Method method) {
    if (method.isAnnotationPresent(ServerTimestamp.class)) {
      throw new IllegalArgumentException(
          "Method "
              + method.getName()
              + " is annotated with @ServerTimestamp but should not be. @ServerTimestamp can"
              + " only be applied to fields and getters, not setters.");
    }
    if (method.isAnnotationPresent(DocumentId.class)) {
      Class<?> paramType = method.getParameterTypes()[0];
      EncodingUtil.validateDocumentIdType("Method", "accepts", paramType);
      documentIdPropertyNames.add(EncodingUtil.propertyName(method));
    }
  }
}
