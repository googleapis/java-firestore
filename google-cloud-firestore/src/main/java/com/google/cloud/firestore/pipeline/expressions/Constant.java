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

package com.google.cloud.firestore.pipeline.expressions;

import static com.google.cloud.firestore.PipelineUtils.encodeValue;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Blob;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.GeoPoint;
import com.google.firestore.v1.Value;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@BetaApi
public final class Constant implements Expr {
  private final Object value;

  Constant(Object value) {
    this.value = value;
  }

  @BetaApi
  public static Constant of(String value) {
    return new Constant(value);
  }

  @BetaApi
  public static Constant of(Number value) {
    return new Constant(value);
  }

  @BetaApi
  public static Constant of(Date value) {
    return new Constant(value);
  }

  @BetaApi
  public static Constant of(Timestamp value) {
    return new Constant(value);
  }

  @BetaApi
  public static Constant of(Boolean value) {
    return new Constant(value);
  }

  @BetaApi
  public static Constant of(GeoPoint value) {
    return new Constant(value);
  }

  @BetaApi
  public static Constant of(Blob value) {
    return new Constant(value);
  }

  @BetaApi
  public static Constant of(DocumentReference value) {
    return new Constant(value);
  }

  @InternalApi
  public static Constant of(Value value) {
    return new Constant(value);
  }

  @InternalApi
  public static Constant nullValue() {
    return new Constant(null);
  }

  @BetaApi
  static Constant of(Object value) {
    if (value == null) {
      return new Constant(null);
    }

    if (value instanceof String) {
      return of((String) value);
    } else if (value instanceof Number) {
      return of((Number) value);
    } else if (value instanceof Date) {
      return of((Date) value);
    } else if (value instanceof Timestamp) {
      return of((Timestamp) value);
    } else if (value instanceof Boolean) {
      return of((Boolean) value);
    } else if (value instanceof GeoPoint) {
      return of((GeoPoint) value);
    } else if (value instanceof Blob) {
      return of((Blob) value);
    } else if (value instanceof DocumentReference) {
      return of((DocumentReference) value);
    } else if (value instanceof Value) {
      return of((Value) value);
    } else {
      throw new IllegalArgumentException("Unknown type: " + value);
    }
  }

  @BetaApi
  public static <T> Constant of(Iterable<T> value) {
    return new Constant(value);
  }

  @BetaApi
  public static <T> Constant of(T[] value) {
    return new Constant(Arrays.asList(value.clone())); // Convert array to list
  }

  @BetaApi
  public static <T> Constant of(Map<String, T> value) {
    return new Constant(value);
  }

  @BetaApi
  public static Constant vector(double[] value) {
    return new Constant(FieldValue.vector(value));
  }

  @InternalApi
  public Value toProto() {
    return encodeValue(value);
  }
}
