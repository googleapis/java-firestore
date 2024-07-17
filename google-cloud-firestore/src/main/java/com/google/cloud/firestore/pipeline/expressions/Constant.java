package com.google.cloud.firestore.pipeline.expressions;

import static com.google.cloud.firestore.PipelineUtils.encodeValue;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Blob;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.GeoPoint;
import com.google.firestore.v1.Value;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@BetaApi
public final class Constant implements Expr {
  private final Object value;

  private Constant(Object value) {
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
    return new Constant(Arrays.asList(value)); // Convert array to list
  }

  @BetaApi
  public static <T> Constant of(Map<String, T> value) {
    return new Constant(value);
  }

  @BetaApi
  public static Constant ofVector(double[] value) {
    // Convert double array to List<Double>
    return new Constant(Arrays.stream(value).boxed().collect(Collectors.toList()));
  }

  @InternalApi
  public Value toProto() {
    return encodeValue(value);
  }
}
