package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.Pipeline;
import com.google.common.base.Objects;
import com.google.firestore.v1.Value;
import javax.annotation.Nullable;

@BetaApi
public final class Field implements Expr, Selectable {
  public static final String DOCUMENT_ID = "__name__";
  private final FieldPath path;
  @Nullable private Pipeline pipeline; // Nullable

  private Field(FieldPath path) {
    this.path = path;
  }

  @BetaApi
  public static Field of(String path) {
    if (path.equals(DOCUMENT_ID)) {
      return new Field(FieldPath.of("__path__"));
    }
    return new Field(FieldPath.fromDotSeparatedString(path));
  }

  @InternalApi
  public Value toProto() {
    return Value.newBuilder().setFieldReferenceValue(path.toString()).build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Field field = (Field) o;
    return Objects.equal(path, field.path) && Objects.equal(pipeline, field.pipeline);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(path, pipeline);
  }

  @InternalApi
  public FieldPath getPath() {
    return path;
  }

  @InternalApi
  public Pipeline getPipeline() {
    return pipeline;
  }
}
