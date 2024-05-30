package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;
import java.util.List;

public final class In extends Function implements FilterCondition {
  In(Expr left, List<Expr> others) {
    super("in", Lists.newArrayList(left, new ListOfExprs(others)));
  }
}
