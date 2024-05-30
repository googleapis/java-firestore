package com.google.cloud.firestore.pipeline.expressions;

import java.util.List;

public final class Or extends Function implements FilterCondition {
  Or(List<FilterCondition> conditions) {
    super("or", conditions);
  }
}
