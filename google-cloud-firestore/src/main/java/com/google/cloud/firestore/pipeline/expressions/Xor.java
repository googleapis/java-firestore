package com.google.cloud.firestore.pipeline.expressions;

import java.util.List;

public final class Xor extends Function implements FilterCondition {
  Xor(List<FilterCondition> conditions) {
    super("xor", conditions);
  }
}
