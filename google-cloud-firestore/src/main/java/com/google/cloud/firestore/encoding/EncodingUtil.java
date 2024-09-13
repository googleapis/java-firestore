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

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Blob;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.GeoPoint;
import com.google.cloud.firestore.VectorValue;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.PropertyName;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/** A utility class for serializing and deserializing operations. */
class EncodingUtil {
  static void hardAssert(boolean assertion) {
    hardAssert(assertion, "Internal inconsistency");
  }

  static void hardAssert(boolean assertion, String message) {
    if (!assertion) {
      throw new RuntimeException("Hard assert failed: " + message);
    }
  }

  @SuppressWarnings("unchecked")
  static <T> T deserializeToPrimitive(Object o, Class<T> clazz, DeserializeContext context) {
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
      throw context.errorPath.deserializeError(
          String.format("Deserializing values to %s is not supported", clazz.getSimpleName()));
    }
  }

  @SuppressWarnings("unchecked")
  static <T> T deserializeToEnum(Object object, Class<T> clazz, DeserializeContext context) {
    if (object instanceof String) {
      String value = (String) object;
      // We cast to Class without generics here since we can't prove the bound
      // T extends Enum<T> statically

      // try to use PropertyName if exist
      Field[] enumFields = clazz.getFields();
      for (Field field : enumFields) {
        if (field.isEnumConstant()) {
          String propertyName = propertyName(field);
          if (value.equals(propertyName)) {
            value = field.getName();
            break;
          }
        }
      }

      try {
        return (T) Enum.valueOf((Class) clazz, value);
      } catch (IllegalArgumentException e) {
        throw context.errorPath.deserializeError(
            "Could not find enum value of " + clazz.getName() + " for value \"" + value + "\"");
      }
    } else {
      throw context.errorPath.deserializeError(
          "Expected a String while deserializing to enum "
              + clazz
              + " but got a "
              + object.getClass());
    }
  }

  @SuppressWarnings("unchecked")
  static Map<String, Object> expectMap(Object object, DeserializeContext context) {
    if (object instanceof Map) {
      // TODO: runtime validation of keys?
      return (Map<String, Object>) object;
    } else {
      throw context.errorPath.deserializeError(
          "Expected a Map while deserializing, but got a " + object.getClass());
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
        throw context.errorPath.deserializeError(
            "Numeric value out of 32-bit integer range: "
                + value
                + ". Did you mean to use a long or double instead of an int?");
      }
    } else {
      throw context.errorPath.deserializeError(
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
        throw context.errorPath.deserializeError(
            "Numeric value out of 64-bit long range: "
                + value
                + ". Did you mean to use a double instead of a long?");
      }
    } else {
      throw context.errorPath.deserializeError(
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
        throw context.errorPath.deserializeError(
            "Loss of precision while converting number to "
                + "double: "
                + o
                + ". Did you mean to use a 64-bit long instead?");
      }
    } else if (o instanceof Double) {
      return (Double) o;
    } else {
      throw context.errorPath.deserializeError(
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
      throw context.errorPath.deserializeError(
          "Failed to convert a value of type " + o.getClass().getName() + " to BigDecimal");
    }
  }

  private static Boolean convertBoolean(Object o, DeserializeContext context) {
    if (o instanceof Boolean) {
      return (Boolean) o;
    } else {
      throw context.errorPath.deserializeError(
          "Failed to convert value of type " + o.getClass().getName() + " to boolean");
    }
  }

  static String convertString(Object o, DeserializeContext context) {
    if (o instanceof String) {
      return (String) o;
    } else {
      throw context.errorPath.deserializeError(
          "Failed to convert value of type " + o.getClass().getName() + " to String");
    }
  }

  static Date convertDate(Object o, DeserializeContext context) {
    if (o instanceof Date) {
      return (Date) o;
    } else if (o instanceof Timestamp) {
      return ((Timestamp) o).toDate();
    } else {
      throw context.errorPath.deserializeError(
          "Failed to convert value of type " + o.getClass().getName() + " to Date");
    }
  }

  static Timestamp convertTimestamp(Object o, DeserializeContext context) {
    if (o instanceof Timestamp) {
      return (Timestamp) o;
    } else if (o instanceof Date) {
      return Timestamp.of((Date) o);
    } else {
      throw context.errorPath.deserializeError(
          "Failed to convert value of type " + o.getClass().getName() + " to Timestamp");
    }
  }

  static Instant convertInstant(Object o, DeserializeContext context) {
    if (o instanceof Timestamp) {
      Timestamp timestamp = (Timestamp) o;
      return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    } else if (o instanceof Date) {
      return Instant.ofEpochMilli(((Date) o).getTime());
    } else {
      throw context.errorPath.deserializeError(
          "Failed to convert value of type " + o.getClass().getName() + " to Instant");
    }
  }

  static Blob convertBlob(Object o, DeserializeContext context) {
    if (o instanceof Blob) {
      return (Blob) o;
    } else {
      throw context.errorPath.deserializeError(
          "Failed to convert value of type " + o.getClass().getName() + " to Blob");
    }
  }

  static GeoPoint convertGeoPoint(Object o, DeserializeContext context) {
    if (o instanceof GeoPoint) {
      return (GeoPoint) o;
    } else {
      throw context.errorPath.deserializeError(
          "Failed to convert value of type " + o.getClass().getName() + " to GeoPoint");
    }
  }

  static VectorValue convertVectorValue(Object o, DeserializeContext context) {
    if (o instanceof VectorValue) {
      return (VectorValue) o;
    } else {
      throw context.errorPath.deserializeError(
          "Failed to convert value of type " + o.getClass().getName() + " to VectorValue");
    }
  }

  static DocumentReference convertDocumentReference(Object o, DeserializeContext context) {
    if (o instanceof DocumentReference) {
      return (DocumentReference) o;
    } else {
      throw context.errorPath.deserializeError(
          "Failed to convert value of type " + o.getClass().getName() + " to DocumentReference");
    }
  }

  static String propertyName(Field field) {
    String annotatedName = annotatedName(field);
    return annotatedName != null ? annotatedName : field.getName();
  }

  static String propertyName(Method method) {
    String annotatedName = EncodingUtil.annotatedName(method);
    return annotatedName != null ? annotatedName : serializedName(method.getName());
  }

  static String annotatedName(AccessibleObject obj) {
    if (obj.isAnnotationPresent(PropertyName.class)) {
      PropertyName annotation = obj.getAnnotation(PropertyName.class);
      return annotation.value();
    }

    return null;
  }

  static String serializedName(String methodName) {
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

  static void validateDocumentIdType(String fieldDescription, String operation, Type type) {
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

  static void validateServerTimestampType(String fieldDescription, String operation, Type type) {
    if (type != Date.class && type != Timestamp.class && type != Instant.class) {
      throw new IllegalArgumentException(
          fieldDescription
              + " is annotated with @ServerTimestamp but "
              + operation
              + " "
              + type
              + " instead of Date, Timestamp, or Instant.");
    }
  }

  static boolean shouldIncludeGetter(Method method) {
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

  static boolean shouldIncludeSetter(Method method) {
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

  static boolean shouldIncludeField(Field field) {
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

  static boolean isSetterOverride(Method base, Method override) {
    // We expect an overridden setter here
    EncodingUtil.hardAssert(
        base.getDeclaringClass().isAssignableFrom(override.getDeclaringClass()),
        "Expected override from a base class");
    EncodingUtil.hardAssert(base.getReturnType().equals(Void.TYPE), "Expected void return type");
    EncodingUtil.hardAssert(
        override.getReturnType().equals(Void.TYPE), "Expected void return type");

    Type[] baseParameterTypes = base.getParameterTypes();
    Type[] overrideParameterTypes = override.getParameterTypes();
    EncodingUtil.hardAssert(baseParameterTypes.length == 1, "Expected exactly one parameter");
    EncodingUtil.hardAssert(overrideParameterTypes.length == 1, "Expected exactly one parameter");

    return base.getName().equals(override.getName())
        && baseParameterTypes[0].equals(overrideParameterTypes[0]);
  }
}
