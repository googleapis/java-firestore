package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class IsNull extends Function implements FilterCondition {
  IsNull(Expr value) {
    super("is_null", Lists.newArrayList(value));
  }
}
