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

package com.google.cloud.firestore.encoding;

import com.google.api.core.InternalApi;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Blob;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.GeoPoint;
import com.google.cloud.firestore.VectorValue;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.firestore.v1.Value;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** Helper class to convert to/from custom POJO classes and plain Java types. */
@InternalApi
public class CustomClassMapper {

  /** Maximum depth before we give up and assume it's a recursive object graph. */
  private static final int MAX_DEPTH = 500;

  private static final ConcurrentMap<Class<?>, BeanMapper<?>> mappers = new ConcurrentHashMap<>();

  /**
   * Converts a Java representation of JSON data to standard library Java data types: Map, Array,
   * String, Double, Integer and Boolean. POJOs are converted to Java Maps.
   *
   * @param object The representation of the JSON data
   * @return JSON representation containing only standard library Java types
   */
  public static Object convertToPlainJavaTypes(Object object) {
    return serialize(object);
  }

  public static Map<String, Object> convertToPlainJavaTypes(Map<?, Object> update) {
    Object converted = serialize(update);
    EncodingUtil.hardAssert(converted instanceof Map);
    @SuppressWarnings("unchecked")
    Map<String, Object> convertedMap = (Map<String, Object>) converted;
    return convertedMap;
  }

  /**
   * Converts a standard library Java representation of JSON data to an object of the provided
   * class.
   *
   * @param object The representation of the JSON data
   * @param clazz The class of the object to convert to
   * @param docRef The value to set to {@link DocumentId} annotated fields in the custom class.
   * @return The POJO object.
   */
  public static <T> T convertToCustomClass(
      Object object, Class<T> clazz, DocumentReference docRef) {
    return deserializeToClass(
        object, clazz, new DeserializeContext(DeserializeContext.ErrorPath.EMPTY, docRef));
  }

  public static <T> Object serialize(T o) {
    return serialize(o, DeserializeContext.ErrorPath.EMPTY);
  }

  @SuppressWarnings("unchecked")
  static <T> Object serialize(T o, DeserializeContext.ErrorPath path) {
    if (path.getLength() > MAX_DEPTH) {
      throw path.serializeError(
          "Exceeded maximum depth of "
              + MAX_DEPTH
              + ", which likely indicates there's an object cycle");
    }
    if (o == null) {
      return null;
    } else if (o instanceof Number) {
      if (o instanceof Long || o instanceof Integer || o instanceof Double || o instanceof Float) {
        return o;
      } else if (o instanceof BigDecimal) {
        return String.valueOf(o);
      } else {
        throw path.serializeError(
            String.format(
                "Numbers of type %s are not supported, please use an int, long, float, double or BigDecimal",
                o.getClass().getSimpleName()));
      }
    } else if (o instanceof String) {
      return o;
    } else if (o instanceof Boolean) {
      return o;
    } else if (o instanceof Character) {
      throw path.serializeError("Characters are not supported, please use Strings");
    } else if (o instanceof Map) {
      Map<String, Object> result = new HashMap<>();
      for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) o).entrySet()) {
        Object key = entry.getKey();
        if (key instanceof String) {
          String keyString = (String) key;
          result.put(keyString, serialize(entry.getValue(), path.child(keyString)));
        } else {
          throw path.serializeError("Maps with non-string keys are not supported");
        }
      }
      return result;
    } else if (o instanceof Collection) {
      if (o instanceof List) {
        List<Object> list = (List<Object>) o;
        List<Object> result = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
          result.add(serialize(list.get(i), path.child("[" + i + "]")));
        }
        return result;
      } else {
        throw path.serializeError(
            "Serializing Collections is not supported, please use Lists instead");
      }
    } else if (o.getClass().isArray()) {
      throw path.serializeError("Serializing Arrays is not supported, please use Lists instead");
    } else if (o instanceof Enum) {
      String enumName = ((Enum<?>) o).name();
      try {
        Field enumField = o.getClass().getField(enumName);
        return EncodingUtil.propertyName(enumField);
      } catch (NoSuchFieldException ex) {
        return enumName;
      }
    } else if (o instanceof Date
        || o instanceof Timestamp
        || o instanceof GeoPoint
        || o instanceof Blob
        || o instanceof DocumentReference
        || o instanceof FieldValue
        || o instanceof Value
        || o instanceof VectorValue) {
      return o;
    } else if (o instanceof Instant) {
      Instant instant = (Instant) o;
      return Timestamp.ofTimeSecondsAndNanos(instant.getEpochSecond(), instant.getNano());
    } else {
      Class<T> clazz = (Class<T>) o.getClass();
      BeanMapper<T> mapper = loadOrCreateBeanMapperForClass(clazz);
      return mapper.serialize(o, path);
    }
  }

  @SuppressWarnings({"unchecked", "TypeParameterUnusedInFormals"})
  static <T> T deserializeToType(Object o, Type type, DeserializeContext context) {
    if (o == null) {
      return null;
    } else if (type instanceof ParameterizedType) {
      return deserializeToParameterizedType(o, (ParameterizedType) type, context);
    } else if (type instanceof Class) {
      return deserializeToClass(o, (Class<T>) type, context);
    } else if (type instanceof WildcardType) {
      Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
      if (lowerBounds.length > 0) {
        throw context.errorPath.deserializeError(
            "Generic lower-bounded wildcard types are not supported");
      }

      // Upper bounded wildcards are of the form <? extends Foo>. Multiple upper bounds are allowed
      // but if any of the bounds are of class type, that bound must come first in this array. Note
      // that this array always has at least one element, since the unbounded wildcard <?> always
      // has at least an upper bound of Object.
      Type[] upperBounds = ((WildcardType) type).getUpperBounds();
      EncodingUtil.hardAssert(upperBounds.length > 0, "Unexpected type bounds on wildcard " + type);
      return deserializeToType(o, upperBounds[0], context);
    } else if (type instanceof TypeVariable) {
      // As above, TypeVariables always have at least one upper bound of Object.
      Type[] upperBounds = ((TypeVariable<?>) type).getBounds();
      EncodingUtil.hardAssert(
          upperBounds.length > 0, "Unexpected type bounds on type variable " + type);
      return deserializeToType(o, upperBounds[0], context);

    } else if (type instanceof GenericArrayType) {
      throw context.errorPath.deserializeError(
          "Generic Arrays are not supported, please use Lists instead");
    } else {
      throw context.errorPath.deserializeError("Unknown type encountered: " + type);
    }
  }

  @SuppressWarnings({"unchecked", "TypeParameterUnusedInFormals"})
  private static <T> T deserializeToParameterizedType(
      Object o, ParameterizedType type, DeserializeContext context) {
    // getRawType should always return a Class<?>
    Class<?> rawType = (Class<?>) type.getRawType();
    if (List.class.isAssignableFrom(rawType)) {
      Type genericType = type.getActualTypeArguments()[0];
      if (o instanceof List) {
        List<Object> list = (List<Object>) o;
        List<Object> result;
        try {
          result =
              (rawType == List.class)
                  ? new ArrayList<>(list.size())
                  : (List<Object>) rawType.getDeclaredConstructor().newInstance();
        } catch (InstantiationException
            | IllegalAccessException
            | NoSuchMethodException
            | InvocationTargetException e) {
          throw context.errorPath.deserializeError(
              String.format(
                  "Unable to deserialize to %s: %s", rawType.getSimpleName(), e.toString()));
        }
        for (int i = 0; i < list.size(); i++) {
          result.add(
              deserializeToType(
                  list.get(i),
                  genericType,
                  context.newInstanceWithErrorPath(context.errorPath.child("[" + i + "]"))));
        }
        return (T) result;
      } else {
        throw context.errorPath.deserializeError("Expected a List, but got a " + o.getClass());
      }
    } else if (Map.class.isAssignableFrom(rawType)) {
      Type keyType = type.getActualTypeArguments()[0];
      Type valueType = type.getActualTypeArguments()[1];
      if (!keyType.equals(String.class)) {
        throw context.errorPath.deserializeError(
            "Only Maps with string keys are supported, but found Map with key type " + keyType);
      }
      Map<String, Object> map = EncodingUtil.expectMap(o, context);
      HashMap<String, Object> result;
      try {
        result =
            (rawType == Map.class)
                ? new HashMap<>()
                : (HashMap<String, Object>) rawType.getDeclaredConstructor().newInstance();
      } catch (InstantiationException
          | IllegalAccessException
          | NoSuchMethodException
          | InvocationTargetException e) {
        throw context.errorPath.deserializeError(
            String.format(
                "Unable to deserialize to %s: %s", rawType.getSimpleName(), e.toString()));
      }
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        result.put(
            entry.getKey(),
            deserializeToType(
                entry.getValue(),
                valueType,
                context.newInstanceWithErrorPath(context.errorPath.child(entry.getKey()))));
      }
      return (T) result;
    } else if (Collection.class.isAssignableFrom(rawType)) {
      throw context.errorPath.deserializeError(
          "Collections are not supported, please use Lists instead");
    } else {
      Map<String, Object> map = EncodingUtil.expectMap(o, context);
      BeanMapper<T> mapper = (BeanMapper<T>) loadOrCreateBeanMapperForClass(rawType);
      HashMap<TypeVariable<Class<T>>, Type> typeMapping = new HashMap<>();
      TypeVariable<Class<T>>[] typeVariables = mapper.getClazz().getTypeParameters();
      Type[] types = type.getActualTypeArguments();
      if (types.length != typeVariables.length) {
        throw new IllegalStateException("Mismatched lengths for type variables and actual types");
      }
      for (int i = 0; i < typeVariables.length; i++) {
        typeMapping.put(typeVariables[i], types[i]);
      }
      return mapper.deserialize(map, typeMapping, context);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> T deserializeToClass(Object o, Class<T> clazz, DeserializeContext context) {
    if (o == null) {
      return null;
    } else if (clazz.isPrimitive()
        || Number.class.isAssignableFrom(clazz)
        || Boolean.class.isAssignableFrom(clazz)
        || Character.class.isAssignableFrom(clazz)) {
      return EncodingUtil.deserializeToPrimitive(o, clazz, context);
    } else if (String.class.isAssignableFrom(clazz)) {
      return (T) EncodingUtil.convertString(o, context);
    } else if (Date.class.isAssignableFrom(clazz)) {
      return (T) EncodingUtil.convertDate(o, context);
    } else if (Timestamp.class.isAssignableFrom(clazz)) {
      return (T) EncodingUtil.convertTimestamp(o, context);
    } else if (Instant.class.isAssignableFrom(clazz)) {
      return (T) EncodingUtil.convertInstant(o, context);
    } else if (Blob.class.isAssignableFrom(clazz)) {
      return (T) EncodingUtil.convertBlob(o, context);
    } else if (GeoPoint.class.isAssignableFrom(clazz)) {
      return (T) EncodingUtil.convertGeoPoint(o, context);
    } else if (VectorValue.class.isAssignableFrom(clazz)) {
      return (T) EncodingUtil.convertVectorValue(o, context);
    } else if (DocumentReference.class.isAssignableFrom(clazz)) {
      return (T) EncodingUtil.convertDocumentReference(o, context);
    } else if (clazz.isArray()) {
      throw context.errorPath.deserializeError(
          "Converting to Arrays is not supported, please use Lists instead");
    } else if (clazz.getTypeParameters().length > 0) {
      throw context.errorPath.deserializeError(
          "Class "
              + clazz.getName()
              + " has generic type parameters, please use GenericTypeIndicator instead");
    } else if (clazz.equals(Object.class)) {
      return (T) o;
    } else if (clazz.isEnum()) {
      return EncodingUtil.deserializeToEnum(o, clazz, context);
    } else {
      return convertBean(o, clazz, context);
    }
  }

  private static <T> T convertBean(Object o, Class<T> clazz, DeserializeContext context) {
    BeanMapper<T> mapper = loadOrCreateBeanMapperForClass(clazz);
    if (o instanceof Map) {
      return mapper.deserialize(EncodingUtil.expectMap(o, context), context);
    } else {
      throw context.errorPath.deserializeError(
          "Can't convert object of type " + o.getClass().getName() + " to type " + clazz.getName());
    }
  }

  private static <T> BeanMapper<T> loadOrCreateBeanMapperForClass(Class<T> clazz) {
    @SuppressWarnings("unchecked")
    BeanMapper<T> mapper = (BeanMapper<T>) mappers.get(clazz);
    if (mapper == null) {
      if (isRecordType(clazz)) {
        mapper = new RecordMapper<>(clazz);
      } else {
        mapper = new PojoBeanMapper<>(clazz);
      }
      // Inserting without checking is fine because mappers are "pure" and it's okay
      // if we create and use multiple by different threads temporarily
      mappers.put(clazz, mapper);
    }
    return mapper;
  }

  private static boolean isRecordType(Class<?> cls) {
    Class<?> parent = cls.getSuperclass();
    return parent != null && "java.lang.Record".equals(parent.getName());
  }
}
