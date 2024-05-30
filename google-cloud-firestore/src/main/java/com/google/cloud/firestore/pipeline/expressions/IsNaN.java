package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class IsNaN extends Function implements FilterCondition {
  IsNaN(Expr value) {
    super("is_nan", Lists.newArrayList(value));
  }
}
