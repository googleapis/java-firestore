package com.google.cloud.firestore.pipeline.expressions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Fields implements Expr, Selectable {
  private final List<Field> fields;

  private Fields(List<Field> fs) {
    this.fields = fs;
  }

  public static Fields of(String f1, String... f) {
    List<Field> fields = Arrays.stream(f).map(Field::of).collect(Collectors.toList());
    fields.add(0, Field.of(f1)); // Add f1 at the beginning
    return new Fields(fields);
  }

  public static Fields ofAll() {
    return new Fields(Collections.singletonList(Field.of("")));
  }

  // Getters
  public List<Field> getFields() {
    return fields;
  }
}
