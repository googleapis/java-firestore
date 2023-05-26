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
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import com.google.cloud.firestore.annotation.StrictCollectionTypes;
import com.google.cloud.firestore.annotation.ThrowOnExtraProperties;
import com.google.firestore.v1.Value;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/** Helper class to convert to/from custom POJO classes and plain Java types. */
class CustomClassMapper {
  private static final Logger LOGGER = Logger.getLogger(CustomClassMapper.class.getName());

  /** Maximum depth before we give up and assume it's a recursive object graph. */
  private static final int MAX_DEPTH = 500;

  private static final ConcurrentMap<Class<?>, BeanMapper<?>> mappers = new ConcurrentHashMap<>();

  private static void hardAssert(boolean assertion) {
    hardAssert(assertion, "Internal inconsistency");
  }

  private static void hardAssert(boolean assertion, String message) {
    if (!assertion) {
      throw new RuntimeException("Hard assert failed: " + message);
    }
  }

  /**
   * Converts a Java representation of JSON data to standard library Java data types: Map, Array,
   * String, Double, Integer and Boolean. POJOs are converted to Java Maps.
   *
   * @param object The representation of the JSON data
   * @return JSON representation containing only standard library Java types
   */
  static Object convertToPlainJavaTypes(Object object) {
    return serialize(object);
  }

  public static Map<String, Object> convertToPlainJavaTypes(Map<?, Object> update) {
    Object converted = serialize(update);
    hardAssert(converted instanceof Map);
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
  static <T> T convertToCustomClass(Object object, Class<T> clazz, DocumentReference docRef) {
    return deserializeToClass(object, clazz, new DeserializeContext(ErrorPath.EMPTY, docRef));
  }

  static <T> Object serialize(T o) {
    return serialize(o, ErrorPath.EMPTY);
  }

  @SuppressWarnings("unchecked")
  private static <T> Object serialize(T o, ErrorPath path) {
    if (path.getLength() > MAX_DEPTH) {
      throw serializeError(
          path,
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
        throw serializeError(
            path,
            String.format(
                "Numbers of type %s are not supported, please use an int, long, float, double or BigDecimal",
                o.getClass().getSimpleName()));
      }
    } else if (o instanceof String) {
      return o;
    } else if (o instanceof Boolean) {
      return o;
    } else if (o instanceof Character) {
      throw serializeError(path, "Characters are not supported, please use Strings");
    } else if (o instanceof Map) {
      Map<String, Object> result = new HashMap<>();
      for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) o).entrySet()) {
        Object key = entry.getKey();
        if (key instanceof String) {
          String keyString = (String) key;
          result.put(keyString, serialize(entry.getValue(), path.child(keyString)));
        } else {
          throw serializeError(path, "Maps with non-string keys are not supported");
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
        throw serializeError(
            path, "Serializing Collections is not supported, please use Lists instead");
      }
    } else if (o.getClass().isArray()) {
      throw serializeError(path, "Serializing Arrays is not supported, please use Lists instead");
    } else if (o instanceof Enum) {
      String enumName = ((Enum<?>) o).name();
      try {
        Field enumField = o.getClass().getField(enumName);
        return BeanMapper.propertyName(enumField);
      } catch (NoSuchFieldException ex) {
        return enumName;
      }
    } else if (o instanceof Date
        || o instanceof Timestamp
        || o instanceof GeoPoint
        || o instanceof Blob
        || o instanceof DocumentReference
        || o instanceof FieldValue
        || o instanceof Value) {
      return o;
    } else {
      Class<T> clazz = (Class<T>) o.getClass();
      BeanMapper<T> mapper = loadOrCreateBeanMapperForClass(clazz);
      return mapper.serialize(o, path);
    }
  }

  @SuppressWarnings({"unchecked", "TypeParameterUnusedInFormals"})
  private static <T> T deserializeToType(
      Object o, Type type, TypeMapper typeMapper, DeserializeContext context) {
    if (o == null) {
      return null;
    } else if (type instanceof ParameterizedType) {
      return deserializeToParameterizedType(o, (ParameterizedType) type, typeMapper, context);
    } else if (type instanceof Class) {
      return deserializeToClass(o, (Class<T>) type, context);
    } else if (type instanceof WildcardType) {
      Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
      if (lowerBounds.length > 0) {
        throw deserializeError(
            context.errorPath, "Generic lower-bounded wildcard types are not supported");
      }

      // Upper bounded wildcards are of the form <? extends Foo>. Multiple upper bounds are allowed
      // but if any of the bounds are of class type, that bound must come first in this array. Note
      // that this array always has at least one element, since the unbounded wildcard <?> always
      // has at least an upper bound of Object.
      Type[] upperBounds = ((WildcardType) type).getUpperBounds();
      hardAssert(upperBounds.length > 0, "Unexpected type bounds on wildcard " + type);
      return deserializeToType(o, upperBounds[0], typeMapper, context);
    } else if (type instanceof TypeVariable) {
      // As above, TypeVariables always have at least one upper bound of Object.
      Type[] upperBounds = ((TypeVariable<?>) type).getBounds();
      hardAssert(upperBounds.length > 0, "Unexpected type bounds on type variable " + type);
      return deserializeToType(o, upperBounds[0], typeMapper, context);

    } else if (type instanceof GenericArrayType) {
      throw deserializeError(
          context.errorPath, "Generic Arrays are not supported, please use Lists instead");
    } else {
      throw deserializeError(context.errorPath, "Unknown type encountered: " + type);
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
      return deserializeToPrimitive(o, clazz, context);
    } else if (String.class.isAssignableFrom(clazz)) {
      return (T) convertString(o, context);
    } else if (Date.class.isAssignableFrom(clazz)) {
      return (T) convertDate(o, context);
    } else if (Timestamp.class.isAssignableFrom(clazz)) {
      return (T) convertTimestamp(o, context);
    } else if (Blob.class.isAssignableFrom(clazz)) {
      return (T) convertBlob(o, context);
    } else if (GeoPoint.class.isAssignableFrom(clazz)) {
      return (T) convertGeoPoint(o, context);
    } else if (DocumentReference.class.isAssignableFrom(clazz)) {
      return (T) convertDocumentReference(o, context);
    } else if (clazz.isArray()) {
      throw deserializeError(
          context.errorPath, "Converting to Arrays is not supported, please use Lists instead");
    } else if (clazz.getTypeParameters().length > 0) {
      throw deserializeError(
          context.errorPath,
          "Class "
              + clazz.getName()
              + " has generic type parameters, please use GenericTypeIndicator instead");
    } else if (clazz.equals(Object.class)) {
      return (T) o;
    } else if (clazz.isEnum()) {
      return deserializeToEnum(o, clazz, context);
    } else {
      return convertBean(o, clazz, context);
    }
  }

  @SuppressWarnings({"unchecked", "TypeParameterUnusedInFormals"})
  private static <T> T deserializeToParameterizedType(
      Object o, ParameterizedType type, TypeMapper typeMapper, DeserializeContext context) {
    // getRawType should always return a Class<?>
    Class<?> rawType = (Class<?>) type.getRawType();
    if (List.class.isAssignableFrom(rawType)) {
      Type genericType = typeMapper.resolve(type.getActualTypeArguments()[0], true);
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
          throw deserializeError(
              context.errorPath,
              String.format(
                  "Unable to deserialize to %s: %s", rawType.getSimpleName(), e.toString()));
        }
        for (int i = 0; i < list.size(); i++) {
          result.add(
              deserializeToType(
                  list.get(i),
                  genericType,
                  typeMapper,
                  context.newInstanceWithErrorPath(context.errorPath.child("[" + i + "]"))));
        }
        return (T) result;
      } else {
        throw deserializeError(context.errorPath, "Expected a List, but got a " + o.getClass());
      }
    } else if (Map.class.isAssignableFrom(rawType)) {
      Type keyType = type.getActualTypeArguments()[0];
      Type valueType = typeMapper.resolve(type.getActualTypeArguments()[1], true);
      if (!keyType.equals(String.class)) {
        throw deserializeError(
            context.errorPath,
            "Only Maps with string keys are supported, but found Map with key type " + keyType);
      }
      Map<String, Object> map = expectMap(o, context);
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
        throw deserializeError(
            context.errorPath,
            String.format(
                "Unable to deserialize to %s: %s", rawType.getSimpleName(), e.toString()));
      }
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        result.put(
            entry.getKey(),
            deserializeToType(
                entry.getValue(),
                valueType,
                typeMapper,
                context.newInstanceWithErrorPath(context.errorPath.child(entry.getKey()))));
      }
      return (T) result;
    } else if (Collection.class.isAssignableFrom(rawType)) {
      throw deserializeError(
          context.errorPath, "Collections are not supported, please use Lists instead");
    } else {
      Map<String, Object> map = expectMap(o, context);
      BeanMapper<T> mapper = (BeanMapper<T>) loadOrCreateBeanMapperForClass(rawType);
      return mapper.deserialize(
          map, TypeMapper.of(TypeMapper.of(type, mapper.clazz), typeMapper), context);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> T deserializeToPrimitive(
      Object o, Class<T> clazz, DeserializeContext context) {
    if (Integer.class.isAssignableFrom(clazz) || int.class.isAssignableFrom(clazz)) {
      return (T) convertInteger(o, context);
    } else if (Boolean.class.isAssignableFrom(clazz) || boolean.class.isAssignableFrom(clazz)) {
      return (T) convertBoolean(o, context);
    } else if (Double.class.isAssignableFrom(clazz) || double.class.isAssignableFrom(clazz)) {
      return (T) convertDouble(o, context);
    } else if (Long.class.isAssignableFrom(clazz) || long.class.isAssignableFrom(clazz)) {
      return (T) convertLong(o, context);
    } else if (BigDecimal.class.isAssignableFrom(clazz)) {
      return (T) convertBigDecimal(o, context);
    } else if (Float.class.isAssignableFrom(clazz) || float.class.isAssignableFrom(clazz)) {
      return (T) (Float) convertDouble(o, context).floatValue();
    } else {
      throw deserializeError(
          context.errorPath,
          String.format("Deserializing values to %s is not supported", clazz.getSimpleName()));
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> T deserializeToEnum(
      Object object, Class<T> clazz, DeserializeContext context) {
    if (object instanceof String) {
      String value = (String) object;
      // We cast to Class without generics here since we can't prove the bound
      // T extends Enum<T> statically

      // try to use PropertyName if exist
      Field[] enumFields = clazz.getFields();
      for (Field field : enumFields) {
        if (field.isEnumConstant()) {
          String propertyName = BeanMapper.propertyName(field);
          if (value.equals(propertyName)) {
            value = field.getName();
            break;
          }
        }
      }

      try {
        return (T) Enum.valueOf((Class) clazz, value);
      } catch (IllegalArgumentException e) {
        throw deserializeError(
            context.errorPath,
            "Could not find enum value of " + clazz.getName() + " for value \"" + value + "\"");
      }
    } else {
      throw deserializeError(
          context.errorPath,
          "Expected a String while deserializing to enum "
              + clazz
              + " but got a "
              + object.getClass());
    }
  }

  private static <T> BeanMapper<T> loadOrCreateBeanMapperForClass(Class<T> clazz) {
    @SuppressWarnings("unchecked")
    BeanMapper<T> mapper = (BeanMapper<T>) mappers.get(clazz);
    if (mapper == null) {
      mapper = new BeanMapper<>(clazz);
      // Inserting without checking is fine because mappers are "pure" and it's okay
      // if we create and use multiple by different threads temporarily
      mappers.put(clazz, mapper);
    }
    return mapper;
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Object> expectMap(Object object, DeserializeContext context) {
    if (object instanceof Map) {
      // TODO: runtime validation of keys?
      return (Map<String, Object>) object;
    } else {
      throw deserializeError(
          context.errorPath, "Expected a Map while deserializing, but got a " + object.getClass());
    }
  }

  private static Integer convertInteger(Object o, DeserializeContext context) {
    if (o instanceof Integer) {
      return (Integer) o;
    } else if (o instanceof Long || o instanceof Double) {
      double value = ((Number) o).doubleValue();
      if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
        return ((Number) o).intValue();
      } else {
        throw deserializeError(
            context.errorPath,
            "Numeric value out of 32-bit integer range: "
                + value
                + ". Did you mean to use a long or double instead of an int?");
      }
    } else {
      throw deserializeError(
          context.errorPath,
          "Failed to convert a value of type " + o.getClass().getName() + " to int");
    }
  }

  private static Long convertLong(Object o, DeserializeContext context) {
    if (o instanceof Integer) {
      return ((Integer) o).longValue();
    } else if (o instanceof Long) {
      return (Long) o;
    } else if (o instanceof Double) {
      Double value = (Double) o;
      if (value >= Long.MIN_VALUE && value <= Long.MAX_VALUE) {
        return value.longValue();
      } else {
        throw deserializeError(
            context.errorPath,
            "Numeric value out of 64-bit long range: "
                + value
                + ". Did you mean to use a double instead of a long?");
      }
    } else {
      throw deserializeError(
          context.errorPath,
          "Failed to convert a value of type " + o.getClass().getName() + " to long");
    }
  }

  private static Double convertDouble(Object o, DeserializeContext context) {
    if (o instanceof Integer) {
      return ((Integer) o).doubleValue();
    } else if (o instanceof Long) {
      Long value = (Long) o;
      Double doubleValue = ((Long) o).doubleValue();
      if (doubleValue.longValue() == value) {
        return doubleValue;
      } else {
        throw deserializeError(
            context.errorPath,
            "Loss of precision while converting number to "
                + "double: "
                + o
                + ". Did you mean to use a 64-bit long instead?");
      }
    } else if (o instanceof Double) {
      return (Double) o;
    } else {
      throw deserializeError(
          context.errorPath,
          "Failed to convert a value of type " + o.getClass().getName() + " to double");
    }
  }

  private static BigDecimal convertBigDecimal(Object o, DeserializeContext context) {
    if (o instanceof Integer) {
      return BigDecimal.valueOf(((Integer) o).intValue());
    } else if (o instanceof Long) {
      return BigDecimal.valueOf(((Long) o).longValue());
    } else if (o instanceof Double) {
      return BigDecimal.valueOf(((Double) o).doubleValue()).abs();
    } else if (o instanceof BigDecimal) {
      return (BigDecimal) o;
    } else if (o instanceof String) {
      return new BigDecimal((String) o);
    } else {
      throw deserializeError(
          context.errorPath,
          "Failed to convert a value of type " + o.getClass().getName() + " to BigDecimal");
    }
  }

  private static Boolean convertBoolean(Object o, DeserializeContext context) {
    if (o instanceof Boolean) {
      return (Boolean) o;
    } else {
      throw deserializeError(
          context.errorPath,
          "Failed to convert value of type " + o.getClass().getName() + " to boolean");
    }
  }

  private static String convertString(Object o, DeserializeContext context) {
    if (o instanceof String) {
      return (String) o;
    } else {
      throw deserializeError(
          context.errorPath,
          "Failed to convert value of type " + o.getClass().getName() + " to String");
    }
  }

  private static Date convertDate(Object o, DeserializeContext context) {
    if (o instanceof Date) {
      return (Date) o;
    } else if (o instanceof Timestamp) {
      return ((Timestamp) o).toDate();
    } else {
      throw deserializeError(
          context.errorPath,
          "Failed to convert value of type " + o.getClass().getName() + " to Date");
    }
  }

  private static Timestamp convertTimestamp(Object o, DeserializeContext context) {
    if (o instanceof Timestamp) {
      return (Timestamp) o;
    } else if (o instanceof Date) {
      return Timestamp.of((Date) o);
    } else {
      throw deserializeError(
          context.errorPath,
          "Failed to convert value of type " + o.getClass().getName() + " to Timestamp");
    }
  }

  private static Blob convertBlob(Object o, DeserializeContext context) {
    if (o instanceof Blob) {
      return (Blob) o;
    } else {
      throw deserializeError(
          context.errorPath,
          "Failed to convert value of type " + o.getClass().getName() + " to Blob");
    }
  }

  private static GeoPoint convertGeoPoint(Object o, DeserializeContext context) {
    if (o instanceof GeoPoint) {
      return (GeoPoint) o;
    } else {
      throw deserializeError(
          context.errorPath,
          "Failed to convert value of type " + o.getClass().getName() + " to GeoPoint");
    }
  }

  private static DocumentReference convertDocumentReference(Object o, DeserializeContext context) {
    if (o instanceof DocumentReference) {
      return (DocumentReference) o;
    } else {
      throw deserializeError(
          context.errorPath,
          "Failed to convert value of type " + o.getClass().getName() + " to DocumentReference");
    }
  }

  private static <T> T convertBean(Object o, Class<T> clazz, DeserializeContext context) {
    BeanMapper<T> mapper = loadOrCreateBeanMapperForClass(clazz);
    if (o instanceof Map) {
      return mapper.deserialize(expectMap(o, context), context);
    } else {
      throw deserializeError(
          context.errorPath,
          "Can't convert object of type " + o.getClass().getName() + " to type " + clazz.getName());
    }
  }

  private static IllegalArgumentException serializeError(ErrorPath path, String reason) {
    reason = "Could not serialize object. " + reason;
    if (path.getLength() > 0) {
      reason = reason + " (found in field '" + path.toString() + "')";
    }
    return new IllegalArgumentException(reason);
  }

  private static RuntimeException deserializeError(ErrorPath path, String reason) {
    reason = "Could not deserialize object. " + reason;
    if (path.getLength() > 0) {
      reason = reason + " (found in field '" + path.toString() + "')";
    }
    return new RuntimeException(reason);
  }

  // Helper class to convert from maps to custom objects (Beans), and vice versa.
  private static class BeanMapper<T> {
    private final Class<T> clazz;
    private final Constructor<T> constructor;
    // Whether to throw exception if there are properties we don't know how to set to
    // custom object fields/setters during deserialization.
    private final boolean throwOnUnknownProperties;
    // Whether to log a message if there are properties we don't know how to set to
    // custom object fields/setters during deserialization.
    private final boolean warnOnUnknownProperties;

    // Case insensitive mapping of properties to their case sensitive versions
    private final Map<String, String> properties;

    // Below are maps to find getter/setter/field from a given property name.
    // A property name is the name annotated by @PropertyName, if exists; or their property name
    // following the Java Bean convention: field name is kept as-is while getters/setters will have
    // their prefixes removed. See method propertyName for details.
    private final Map<String, Method> getters;
    private final Map<String, Method> setters;
    private final Map<String, Field> fields;
    private final Map<Member, TypeMapper> _typeMappers = new HashMap();

    // A set of property names that were annotated with @ServerTimestamp.
    private final HashSet<String> serverTimestamps;

    // A set of property names that were annotated with @DocumentId. These properties will be
    // populated with document ID values during deserialization, and be skipped during
    // serialization.
    private final HashSet<String> documentIdPropertyNames;

    BeanMapper(Class<T> clazz) {
      this.clazz = clazz;
      throwOnUnknownProperties = clazz.isAnnotationPresent(ThrowOnExtraProperties.class);
      warnOnUnknownProperties = !clazz.isAnnotationPresent(IgnoreExtraProperties.class);
      properties = new HashMap<>();

      setters = new HashMap<>();
      getters = new HashMap<>();
      fields = new HashMap<>();

      serverTimestamps = new HashSet<>();
      documentIdPropertyNames = new HashSet<>();

      Constructor<T> constructor;
      try {
        constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
      } catch (NoSuchMethodException e) {
        // We will only fail at deserialization time if no constructor is present
        constructor = null;
      }
      this.constructor = constructor;
      // Add any public getters to properties (including isXyz())
      for (Method method : clazz.getMethods()) {
        if (shouldIncludeGetter(method)) {
          String propertyName = propertyName(method);
          addProperty(propertyName);
          method.setAccessible(true);
          if (getters.containsKey(propertyName)) {
            throw new RuntimeException(
                "Found conflicting getters for name "
                    + method.getName()
                    + " on class "
                    + clazz.getName());
          }
          getters.put(propertyName, method);
          applyGetterAnnotations(method);
        }
      }

      // Add any public fields to properties
      for (Field field : clazz.getFields()) {
        if (shouldIncludeField(field)) {
          String propertyName = propertyName(field);
          addProperty(propertyName);
          applyFieldAnnotations(field);
        }
      }

      // We can use private setters and fields for known (public) properties/getters. Since
      // getMethods/getFields only returns public methods/fields we need to traverse the
      // class hierarchy to find the appropriate setter or field.
      Class<?> currentClass = clazz;
      TypeMapper typeMapper = TypeMapper.empty();
      do {
        // Add any setters
        for (Method method : currentClass.getDeclaredMethods()) {
          if (shouldIncludeSetter(method)) {
            String propertyName = propertyName(method);
            String existingPropertyName = properties.get(propertyName.toLowerCase(Locale.US));
            if (existingPropertyName != null) {
              if (!existingPropertyName.equals(propertyName)) {
                throw new RuntimeException(
                    "Found setter on "
                        + currentClass.getName()
                        + " with invalid case-sensitive name: "
                        + method.getName());
              } else {
                Method existingSetter = setters.get(propertyName);
                if (existingSetter == null) {
                  method.setAccessible(true);
                  setters.put(propertyName, method);
                  _typeMappers.put(method, typeMapper);
                  applySetterAnnotations(method);
                } else if (!isSetterOverride(method, existingSetter)) {
                  // We require that setters with conflicting property names are
                  // overrides from a base class
                  if (currentClass == clazz) {
                    // TODO: Should we support overloads?
                    throw new RuntimeException(
                        "Class "
                            + clazz.getName()
                            + " has multiple setter overloads with name "
                            + method.getName());
                  } else {
                    throw new RuntimeException(
                        "Found conflicting setters "
                            + "with name: "
                            + method.getName()
                            + " (conflicts with "
                            + existingSetter.getName()
                            + " defined on "
                            + existingSetter.getDeclaringClass().getName()
                            + ")");
                  }
                }
              }
            }
          }
        }

        for (Field field : currentClass.getDeclaredFields()) {
          String propertyName = propertyName(field);

          // Case sensitivity is checked at deserialization time
          // Fields are only added if they don't exist on a subclass
          if (properties.containsKey(propertyName.toLowerCase(Locale.US))
              && !fields.containsKey(propertyName)) {
            field.setAccessible(true);
            fields.put(propertyName, field);
            _typeMappers.put(field, typeMapper);
            applyFieldAnnotations(field);
          }
        }

        // Traverse class hierarchy until we reach java.lang.Object which contains a bunch
        // of fields/getters we don't want to serialize
        Class<?> superclass = currentClass.getSuperclass();
        Type genericSuperclass = currentClass.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
          typeMapper = TypeMapper.of((ParameterizedType) genericSuperclass, superclass);
        } else {
          typeMapper = TypeMapper.empty();
        }
        currentClass = superclass;
      } while (currentClass != null && !currentClass.equals(Object.class));

      if (properties.isEmpty()) {
        throw new RuntimeException("No properties to serialize found on class " + clazz.getName());
      }

      // Make sure we can write to @DocumentId annotated properties before proceeding.
      for (String docIdProperty : documentIdPropertyNames) {
        if (!setters.containsKey(docIdProperty) && !fields.containsKey(docIdProperty)) {
          throw new RuntimeException(
              "@DocumentId is annotated on property "
                  + docIdProperty
                  + " of class "
                  + clazz.getName()
                  + " but no field or public setter was found");
        }
      }
    }

    private void addProperty(String property) {
      String oldValue = properties.put(property.toLowerCase(Locale.US), property);
      if (oldValue != null && !property.equals(oldValue)) {
        throw new RuntimeException(
            "Found two getters or fields with conflicting case "
                + "sensitivity for property: "
                + property.toLowerCase(Locale.US));
      }
    }

    T deserialize(Map<String, Object> values, DeserializeContext context) {
      return deserialize(values, TypeMapper.empty(), context);
    }

    T deserialize(Map<String, Object> values, TypeMapper typeMapper, DeserializeContext context) {
      if (constructor == null) {
        throw deserializeError(
            context.errorPath,
            "Class "
                + clazz.getName()
                + " does not define a no-argument constructor. If you are using ProGuard, make "
                + "sure these constructors are not stripped");
      }

      T instance;
      try {
        instance = constructor.newInstance();
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
      HashSet<String> deserialzedProperties = new HashSet<>();
      for (Map.Entry<String, Object> entry : values.entrySet()) {
        String propertyName = entry.getKey();
        ErrorPath childPath = context.errorPath.child(propertyName);
        if (setters.containsKey(propertyName)) {
          Method setter = setters.get(propertyName);
          Type[] params = setter.getGenericParameterTypes();
          if (params.length != 1) {
            throw deserializeError(childPath, "Setter does not have exactly one parameter");
          }
          TypeMapper setterTypeMapper = TypeMapper.of(_typeMappers.get(setter), typeMapper);
          Type resolvedType = setterTypeMapper.resolve(params[0], false);
          Object value =
              CustomClassMapper.deserializeToType(
                  entry.getValue(),
                  resolvedType,
                  setterTypeMapper,
                  context.newInstanceWithErrorPath(childPath));
          try {
            setter.invoke(instance, value);
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
          }
          deserialzedProperties.add(propertyName);
        } else if (fields.containsKey(propertyName)) {
          Field field = fields.get(propertyName);
          TypeMapper fieldTypeMapper = TypeMapper.of(_typeMappers.get(field), typeMapper);
          Type resolvedType = fieldTypeMapper.resolve(field.getGenericType(), false);
          Object value =
              CustomClassMapper.deserializeToType(
                  entry.getValue(),
                  resolvedType,
                  fieldTypeMapper,
                  context.newInstanceWithErrorPath(childPath));
          try {
            field.set(instance, value);
          } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
          }
          deserialzedProperties.add(propertyName);
        } else {
          String message =
              "No setter/field for " + propertyName + " found on class " + clazz.getName();
          if (properties.containsKey(propertyName.toLowerCase(Locale.US))) {
            message += " (fields/setters are case sensitive!)";
          }
          if (throwOnUnknownProperties) {
            throw new RuntimeException(message);
          } else if (warnOnUnknownProperties) {
            LOGGER.warning(message);
          }
        }
      }
      populateDocumentIdProperties(typeMapper, context, instance, deserialzedProperties);

      return instance;
    }

    // Populate @DocumentId annotated fields. If there is a conflict (@DocumentId annotation is
    // applied to a property that is already deserialized from the firestore document)
    // a runtime exception will be thrown.
    private void populateDocumentIdProperties(
        TypeMapper typeMapper,
        DeserializeContext context,
        T instance,
        HashSet<String> deserialzedProperties) {
      for (String docIdPropertyName : documentIdPropertyNames) {
        if (deserialzedProperties.contains(docIdPropertyName)) {
          String message =
              "'"
                  + docIdPropertyName
                  + "' was found from document "
                  + context.documentRef.getPath()
                  + ", cannot apply @DocumentId on this property for class "
                  + clazz.getName();
          throw new RuntimeException(message);
        }
        ErrorPath childPath = context.errorPath.child(docIdPropertyName);
        if (setters.containsKey(docIdPropertyName)) {
          Method setter = setters.get(docIdPropertyName);
          Type[] params = setter.getGenericParameterTypes();
          if (params.length != 1) {
            throw deserializeError(childPath, "Setter does not have exactly one parameter");
          }
          Type resolvedType =
              TypeMapper.of(_typeMappers.get(setter), typeMapper).resolve(params[0], false);
          try {
            if (resolvedType == String.class) {
              setter.invoke(instance, context.documentRef.getId());
            } else {
              setter.invoke(instance, context.documentRef);
            }
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
          }
        } else {
          Field docIdField = fields.get(docIdPropertyName);
          Type resolvedType =
              TypeMapper.of(_typeMappers.get(docIdField), typeMapper)
                  .resolve(docIdField.getType(), false);
          try {
            if (resolvedType == String.class) {
              docIdField.set(instance, context.documentRef.getId());
            } else {
              docIdField.set(instance, context.documentRef);
            }
          } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }

    Map<String, Object> serialize(T object, ErrorPath path) {
      if (!clazz.isAssignableFrom(object.getClass())) {
        throw new IllegalArgumentException(
            "Can't serialize object of class "
                + object.getClass()
                + " with BeanMapper for class "
                + clazz);
      }
      Map<String, Object> result = new HashMap<>();
      for (String property : properties.values()) {
        // Skip @DocumentId annotated properties;
        if (documentIdPropertyNames.contains(property)) {
          continue;
        }

        Object propertyValue;
        if (getters.containsKey(property)) {
          Method getter = getters.get(property);
          try {
            propertyValue = getter.invoke(object);
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
          }
        } else {
          // Must be a field
          Field field = fields.get(property);
          if (field == null) {
            throw new IllegalStateException("Bean property without field or getter: " + property);
          }
          try {
            propertyValue = field.get(object);
          } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
          }
        }

        Object serializedValue;
        if (serverTimestamps.contains(property) && propertyValue == null) {
          // Replace null ServerTimestamp-annotated fields with the sentinel.
          serializedValue = FieldValue.serverTimestamp();
        } else {
          serializedValue = CustomClassMapper.serialize(propertyValue, path.child(property));
        }
        result.put(property, serializedValue);
      }
      return result;
    }

    private void applyFieldAnnotations(Field field) {
      if (field.isAnnotationPresent(ServerTimestamp.class)) {
        Class<?> fieldType = field.getType();
        if (fieldType != Date.class && fieldType != Timestamp.class) {
          throw new IllegalArgumentException(
              "Field "
                  + field.getName()
                  + " is annotated with @ServerTimestamp but is "
                  + fieldType
                  + " instead of Date or Timestamp.");
        }
        serverTimestamps.add(propertyName(field));
      }

      if (field.isAnnotationPresent(DocumentId.class)) {
        Class<?> fieldType = field.getType();
        ensureValidDocumentIdType("Field", "is", fieldType);
        documentIdPropertyNames.add(propertyName(field));
      }
    }

    private void applyGetterAnnotations(Method method) {
      if (method.isAnnotationPresent(ServerTimestamp.class)) {
        Class<?> returnType = method.getReturnType();
        if (returnType != Date.class && returnType != Timestamp.class) {
          throw new IllegalArgumentException(
              "Method "
                  + method.getName()
                  + " is annotated with @ServerTimestamp but returns "
                  + returnType
                  + " instead of Date or Timestamp.");
        }
        serverTimestamps.add(propertyName(method));
      }

      // Even though the value will be skipped, we still check for type matching for consistency.
      if (method.isAnnotationPresent(DocumentId.class)) {
        Class<?> returnType = method.getReturnType();
        ensureValidDocumentIdType("Method", "returns", returnType);
        documentIdPropertyNames.add(propertyName(method));
      }
    }

    private void applySetterAnnotations(Method method) {
      if (method.isAnnotationPresent(ServerTimestamp.class)) {
        throw new IllegalArgumentException(
            "Method "
                + method.getName()
                + " is annotated with @ServerTimestamp but should not be. @ServerTimestamp can"
                + " only be applied to fields and getters, not setters.");
      }

      if (method.isAnnotationPresent(DocumentId.class)) {
        Class<?> paramType = method.getParameterTypes()[0];
        ensureValidDocumentIdType("Method", "accepts", paramType);
        documentIdPropertyNames.add(propertyName(method));
      }
    }

    private void ensureValidDocumentIdType(String fieldDescription, String operation, Type type) {
      if (type != String.class && type != DocumentReference.class) {
        throw new IllegalArgumentException(
            fieldDescription
                + " is annotated with @DocumentId but "
                + operation
                + " "
                + type
                + " instead of String or DocumentReference.");
      }
    }

    private static boolean shouldIncludeGetter(Method method) {
      if (!method.getName().startsWith("get") && !method.getName().startsWith("is")) {
        return false;
      }
      // Exclude methods from Object.class
      if (method.getDeclaringClass().equals(Object.class)) {
        return false;
      }
      // Non-public methods
      if (!Modifier.isPublic(method.getModifiers())) {
        return false;
      }
      // Static methods
      if (Modifier.isStatic(method.getModifiers())) {
        return false;
      }
      // No return type
      if (method.getReturnType().equals(Void.TYPE)) {
        return false;
      }
      // Non-zero parameters
      if (method.getParameterTypes().length != 0) {
        return false;
      }
      // Excluded methods
      if (method.isAnnotationPresent(Exclude.class)) {
        return false;
      }
      return true;
    }

    private static boolean shouldIncludeSetter(Method method) {
      if (!method.getName().startsWith("set")) {
        return false;
      }
      // Exclude methods from Object.class
      if (method.getDeclaringClass().equals(Object.class)) {
        return false;
      }
      // Static methods
      if (Modifier.isStatic(method.getModifiers())) {
        return false;
      }
      // Has a return type
      if (!method.getReturnType().equals(Void.TYPE)) {
        return false;
      }
      // Methods without exactly one parameters
      if (method.getParameterTypes().length != 1) {
        return false;
      }
      // Excluded methods
      if (method.isAnnotationPresent(Exclude.class)) {
        return false;
      }
      return true;
    }

    private static boolean shouldIncludeField(Field field) {
      // Exclude methods from Object.class
      if (field.getDeclaringClass().equals(Object.class)) {
        return false;
      }
      // Non-public fields
      if (!Modifier.isPublic(field.getModifiers())) {
        return false;
      }
      // Static fields
      if (Modifier.isStatic(field.getModifiers())) {
        return false;
      }
      // Transient fields
      if (Modifier.isTransient(field.getModifiers())) {
        return false;
      }
      // Excluded fields
      if (field.isAnnotationPresent(Exclude.class)) {
        return false;
      }
      return true;
    }

    private static boolean isSetterOverride(Method base, Method override) {
      // We expect an overridden setter here
      hardAssert(
          base.getDeclaringClass().isAssignableFrom(override.getDeclaringClass()),
          "Expected override from a base class");
      hardAssert(base.getReturnType().equals(Void.TYPE), "Expected void return type");
      hardAssert(override.getReturnType().equals(Void.TYPE), "Expected void return type");

      Type[] baseParameterTypes = base.getParameterTypes();
      Type[] overrideParameterTypes = override.getParameterTypes();
      hardAssert(baseParameterTypes.length == 1, "Expected exactly one parameter");
      hardAssert(overrideParameterTypes.length == 1, "Expected exactly one parameter");

      return base.getName().equals(override.getName())
          && baseParameterTypes[0].equals(overrideParameterTypes[0]);
    }

    private static String propertyName(Field field) {
      String annotatedName = annotatedName(field);
      return annotatedName != null ? annotatedName : field.getName();
    }

    private static String propertyName(Method method) {
      String annotatedName = annotatedName(method);
      return annotatedName != null ? annotatedName : serializedName(method.getName());
    }

    private static String annotatedName(AccessibleObject obj) {
      if (obj.isAnnotationPresent(PropertyName.class)) {
        PropertyName annotation = obj.getAnnotation(PropertyName.class);
        return annotation.value();
      }

      return null;
    }

    private static String serializedName(String methodName) {
      String[] prefixes = new String[] {"get", "set", "is"};
      String methodPrefix = null;
      for (String prefix : prefixes) {
        if (methodName.startsWith(prefix)) {
          methodPrefix = prefix;
        }
      }
      if (methodPrefix == null) {
        throw new IllegalArgumentException("Unknown Bean prefix for method: " + methodName);
      }
      String strippedName = methodName.substring(methodPrefix.length());

      // Make sure the first word or upper-case prefix is converted to lower-case
      char[] chars = strippedName.toCharArray();
      int pos = 0;
      while (pos < chars.length && Character.isUpperCase(chars[pos])) {
        chars[pos] = Character.toLowerCase(chars[pos]);
        pos++;
      }
      return new String(chars);
    }
  }

  /**
   * Resolves generic type variables to actual types, based on context. Special-cases collection
   * types, for backward compatibility: If the containing class is annotated with {@link
   * StrictCollectionTypes}, then resolution is performed normally. Otherwise, the type is not
   * resolved, allowing the collection to take values of any type.
   */
  private interface TypeMapper {
    TypeMapper EMPTY = (typeVariable, collection) -> null;

    static TypeMapper of(ParameterizedType type, Class<?> clazz) {
      TypeVariable<? extends Class<?>>[] typeVariables = clazz.getTypeParameters();
      Type[] types = type.getActualTypeArguments();
      if (types.length != typeVariables.length) {
        throw new IllegalStateException("Mismatched lengths for type variables and actual types");
      }
      Map<TypeVariable<?>, Type> typeMapping = new HashMap<>();
      for (int i = 0; i < typeVariables.length; i++) {
        typeMapping.put(typeVariables[i], types[i]);
      }

      boolean strictCollectionTypes = clazz.isAnnotationPresent(StrictCollectionTypes.class);
      return (typeVariable, collection) ->
          collection && !strictCollectionTypes ? typeVariable : typeMapping.get(typeVariable);
    }

    static TypeMapper of(TypeMapper innerMapper, TypeMapper outerMapper) {
      return (typeVariable, collection) -> map(typeVariable, innerMapper, outerMapper, collection);
    }

    static TypeMapper empty() {
      return EMPTY;
    }

    default Type resolve(Type type, boolean collection) {
      if (type instanceof TypeVariable) {
        Type resolvedType = get((TypeVariable<?>) type, collection);
        if (resolvedType == null) {
          throw new IllegalStateException("Could not resolve type " + type);
        }

        return resolvedType;
      }

      return type;
    }

    static Type map(
        TypeVariable<?> typeVariable,
        TypeMapper innerMapper,
        TypeMapper outerMapper,
        boolean collection) {
      Type type = innerMapper.get(typeVariable, collection);
      if (type != null) {
        return type;
      }

      return outerMapper.get(typeVariable, collection);
    }

    Type get(TypeVariable<?> typeVariable, boolean collection);
  }

  /**
   * Immutable class representing the path to a specific field in an object. Used to provide better
   * error messages.
   */
  static class ErrorPath {
    private final int length;
    private final ErrorPath parent;
    private final String name;

    static final ErrorPath EMPTY = new ErrorPath(null, null, 0);

    ErrorPath(ErrorPath parent, String name, int length) {
      this.parent = parent;
      this.name = name;
      this.length = length;
    }

    int getLength() {
      return length;
    }

    ErrorPath child(String name) {
      return new ErrorPath(this, name, length + 1);
    }

    @Override
    public String toString() {
      if (length == 0) {
        return "";
      } else if (length == 1) {
        return name;
      } else {
        // This is not very efficient, but it's only hit if there's an error.
        return parent.toString() + "." + name;
      }
    }
  }

  /** Holds information a deserialization operation needs to complete the job. */
  private static class DeserializeContext {

    /** Current path to the field being deserialized, used for better error messages. */
    final ErrorPath errorPath;

    /** Value used to set to {@link DocumentId} annotated fields during deserialization, if any. */
    final DocumentReference documentRef;

    DeserializeContext(ErrorPath path, DocumentReference docRef) {
      errorPath = path;
      documentRef = docRef;
    }

    DeserializeContext newInstanceWithErrorPath(ErrorPath newPath) {
      return new DeserializeContext(newPath, documentRef);
    }
  }
}
