package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.expressions.Field;

@InternalApi
public final class UnnestArray implements Stage {

  private static final String name = "unnest_array";
  private final Field field;

  @InternalApi
  public UnnestArray(Field arrayField) {
    this.field = arrayField;
  }

  public Field getField() {
    return field;
  }

  @Override
  public String getName() {
    return name;
  }
}
