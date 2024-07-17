package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;
import java.util.List;

@BetaApi
public final class In extends Function implements FilterCondition {
  @InternalApi
  In(Expr left, List<Expr> others) {
    super("in", Lists.newArrayList(left, new ListOfExprs(others)));
  }
}
