package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Exists extends Function implements FilterCondition {
  Exists(Field field) {
    super("exists", Lists.newArrayList(field));
  }
}
