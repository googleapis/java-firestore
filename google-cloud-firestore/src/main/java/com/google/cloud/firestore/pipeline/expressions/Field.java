package com.google.cloud.firestore.pipeline.expressions;

import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.Pipeline;
import com.google.firestore.v1.Value;
import javax.annotation.Nullable;

public final class Field implements Expr, Selectable {
  public static final String DOCUMENT_ID = "__name__";
  private final FieldPath path;
  @Nullable private Pipeline pipeline; // Nullable

  private Field(Pipeline pipeline, FieldPath path) {
    this.pipeline = pipeline;
    this.path = path;
  }

  private Field(FieldPath path) {
    this.pipeline = null;
    this.path = path;
  }

  public static Field of(String path) {
    return new Field(FieldPath.of(path));
  }

  public static Field of(Pipeline p, String path) {
    return new Field(p, FieldPath.of(path));
  }

  public Value toProto() {
    return Value.newBuilder().setFieldReferenceValue(path.toString()).build();
  }

  public Selectable usingPrefix(String prefix) {
    return this;
  }

  // Getters
  public FieldPath getPath() {
    return path;
  }

  public Pipeline getPipeline() {
    return pipeline;
  }
}
