package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class Not extends Function implements FilterCondition {
  Not(Expr condition) {
    super("not", Lists.newArrayList(condition));
  }
}
