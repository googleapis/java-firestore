package com.google.cloud.firestore.pipeline.expressions;

import static com.google.cloud.firestore.PipelineUtils.encodeValue;

import com.google.api.core.InternalApi;
import com.google.firestore.v1.MapValue;
import com.google.firestore.v1.Value;
import java.util.Locale;

public final class Ordering {

  private final Expr expr;
  private Ordering.Direction dir = Ordering.Direction.ASCENDING;

  private Ordering(Expr expr, Ordering.Direction dir) {
    this.expr = expr;
    this.dir = dir;
  }

  public enum Direction {
    ASCENDING,
    DESCENDING;

    @Override
    public String toString() {
      return name().toLowerCase(Locale.getDefault());
    }
  }

  @InternalApi
  public Value toProto() {
    return Value.newBuilder()
        .setMapValue(
            MapValue.newBuilder()
                .putFields("direction", encodeValue(dir.toString()))
                .putFields("expression", encodeValue(expr))
                .build())
        .build();
  }

  public static Ordering of(Expr expr, Ordering.Direction dir) {
    return new Ordering(expr, dir);
  }

  public static Ordering of(Expr expr) {
    return new Ordering(expr, Direction.ASCENDING);
  }

  public static Ordering ascending(Expr expr) {
    return new Ordering(expr, Direction.ASCENDING);
  }

  public static Ordering descending(Expr expr) {
    return new Ordering(expr, Direction.DESCENDING);
  }

  // Getters
  public Expr getExpr() {
    return expr;
  }

  public Ordering.Direction getDir() {
    return dir;
  }
}
