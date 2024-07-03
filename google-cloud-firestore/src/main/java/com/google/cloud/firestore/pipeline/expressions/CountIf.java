package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;
import java.util.Collections;

public final class CountIf extends Function implements Accumulator {
  CountIf(Expr value, boolean distinct) {
    super("count_if", (value != null) ? Lists.newArrayList(value) : Collections.emptyList());
  }
}
