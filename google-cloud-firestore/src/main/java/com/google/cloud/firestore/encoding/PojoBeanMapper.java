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
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

// Helper class to convert from maps to custom objects (Beans), and vice versa.
class PojoBeanMapper<T> extends BeanMapper<T> {
  private static final Logger LOGGER = Logger.getLogger(PojoBeanMapper.class.getName());

  private final Constructor<T> constructor;

  // Case insensitive mapping of properties to their case sensitive versions
  private final Map<String, String> properties;

  // Below are maps to find getter/setter/field from a given property name.
  // A property name is the name annotated by @PropertyName, if exists; or their property name
  // following the Java Bean convention: field name is kept as-is while getters/setters will have
  // their prefixes removed. See method propertyName for details.
  private final Map<String, Method> getters;
  private final Map<String, Method> setters;
  private final Map<String, Field> fields;

  PojoBeanMapper(Class<T> clazz) {
    super(clazz);
    properties = new HashMap<>();

    setters = new HashMap<>();
    getters = new HashMap<>();
    fields = new HashMap<>();

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
      if (EncodingUtil.shouldIncludeGetter(method)) {
        String propertyName = EncodingUtil.propertyName(method);
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
      if (EncodingUtil.shouldIncludeField(field)) {
        String propertyName = EncodingUtil.propertyName(field);
        addProperty(propertyName);
        applyFieldAnnotations(field);
      }
    }

    // We can use private setters and fields for known (public) properties/getters. Since
    // getMethods/getFields only returns public methods/fields we need to traverse the
    // class hierarchy to find the appropriate setter or field.
    Class<? super T> currentClass = clazz;
    do {
      // Add any setters
      for (Method method : currentClass.getDeclaredMethods()) {
        if (EncodingUtil.shouldIncludeSetter(method)) {
          String propertyName = EncodingUtil.propertyName(method);
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
                applySetterAnnotations(method);
              } else if (!EncodingUtil.isSetterOverride(method, existingSetter)) {
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
        String propertyName = EncodingUtil.propertyName(field);

        // Case sensitivity is checked at deserialization time
        // Fields are only added if they don't exist on a subclass
        if (properties.containsKey(propertyName.toLowerCase(Locale.US))
            && !fields.containsKey(propertyName)) {
          field.setAccessible(true);
          fields.put(propertyName, field);
          applyFieldAnnotations(field);
        }
      }

      // Traverse class hierarchy until we reach java.lang.Object which contains a bunch
      // of fields/getters we don't want to serialize
      currentClass = currentClass.getSuperclass();
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

  @Override
  Map<String, Object> serialize(T object, DeserializeContext.ErrorPath path) {
    verifyValidType(object);
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

      Object serializedValue = getSerializedValue(property, propertyValue, path);

      result.put(property, serializedValue);
    }
    return result;
  }

  @Override
  T deserialize(
      Map<String, Object> values,
      Map<TypeVariable<Class<T>>, Type> types,
      DeserializeContext context) {
    if (constructor == null) {
      throw context.errorPath.deserializeError(
          "Class "
              + getClazz().getName()
              + " does not define a no-argument constructor. If you are using ProGuard, make "
              + "sure these constructors are not stripped");
    }

    T instance;
    try {
      instance = constructor.newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
    HashSet<String> deserializedProperties = new HashSet<>();
    for (Map.Entry<String, Object> entry : values.entrySet()) {
      String propertyName = entry.getKey();
      DeserializeContext.ErrorPath childPath = context.errorPath.child(propertyName);
      if (setters.containsKey(propertyName)) {
        Method setter = setters.get(propertyName);
        Type[] params = setter.getGenericParameterTypes();
        if (params.length != 1) {
          throw childPath.deserializeError("Setter does not have exactly one parameter");
        }
        Type resolvedType = resolveType(params[0], types);
        Object value =
            CustomClassMapper.deserializeToType(
                entry.getValue(), resolvedType, context.newInstanceWithErrorPath(childPath));
        try {
          setter.invoke(instance, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
        deserializedProperties.add(propertyName);
      } else if (fields.containsKey(propertyName)) {
        Field field = fields.get(propertyName);
        Type resolvedType = resolveType(field.getGenericType(), types);
        Object value =
            CustomClassMapper.deserializeToType(
                entry.getValue(), resolvedType, context.newInstanceWithErrorPath(childPath));
        try {
          field.set(instance, value);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
        deserializedProperties.add(propertyName);
      } else {
        String message =
            "No setter/field for " + propertyName + " found on class " + getClazz().getName();
        if (properties.containsKey(propertyName.toLowerCase(Locale.US))) {
          message += " (fields/setters are case sensitive!)";
        }
        if (isThrowOnUnknownProperties()) {
          throw new RuntimeException(message);
        } else if (isWarnOnUnknownProperties()) {
          LOGGER.warning(message);
        }
      }
    }
    populateDocumentIdProperties(types, context, instance, deserializedProperties);

    return instance;
  }

  // Populate @DocumentId annotated fields. If there is a conflict (@DocumentId annotation is
  // applied to a property that is already deserialized from the firestore document)
  // a runtime exception will be thrown.
  private void populateDocumentIdProperties(
      Map<TypeVariable<Class<T>>, Type> types,
      DeserializeContext context,
      T instance,
      HashSet<String> deserializedProperties) {
    for (String docIdPropertyName : documentIdPropertyNames) {
      checkForDocIdConflict(docIdPropertyName, deserializedProperties, context);
      DeserializeContext.ErrorPath childPath = context.errorPath.child(docIdPropertyName);
      if (setters.containsKey(docIdPropertyName)) {
        Method setter = setters.get(docIdPropertyName);
        Type[] params = setter.getGenericParameterTypes();
        if (params.length != 1) {
          throw childPath.deserializeError("Setter does not have exactly one parameter");
        }
        Type resolvedType = resolveType(params[0], types);
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
        try {
          if (docIdField.getType() == String.class) {
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

  private void applyGetterAnnotations(Method method) {
    if (method.isAnnotationPresent(ServerTimestamp.class)) {
      Class<?> returnType = method.getReturnType();
      if (returnType != Date.class
          && returnType != Timestamp.class
          && returnType != Instant.class) {
        throw new IllegalArgumentException(
            "Method "
                + method.getName()
                + " is annotated with @ServerTimestamp but returns "
                + returnType
                + " instead of Date, Timestamp, or Instant.");
      }
      serverTimestamps.add(EncodingUtil.propertyName(method));
    }

    // Even though the value will be skipped, we still check for type matching for consistency.
    if (method.isAnnotationPresent(DocumentId.class)) {
      Class<?> returnType = method.getReturnType();
      EncodingUtil.ensureValidDocumentIdType("Method", "returns", returnType);
      documentIdPropertyNames.add(EncodingUtil.propertyName(method));
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
      EncodingUtil.ensureValidDocumentIdType("Method", "accepts", paramType);
      documentIdPropertyNames.add(EncodingUtil.propertyName(method));
    }
  }
}
