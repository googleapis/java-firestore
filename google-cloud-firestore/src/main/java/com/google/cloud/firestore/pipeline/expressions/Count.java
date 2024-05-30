package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;
import java.util.Collections;

public final class Count extends Function implements Accumulator {
  Count(Expr value, boolean distinct) {
    super("count", (value != null) ? Lists.newArrayList(value) : Collections.emptyList());
  }
}
