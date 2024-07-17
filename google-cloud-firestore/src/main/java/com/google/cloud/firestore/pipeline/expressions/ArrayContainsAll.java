package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;
import java.util.List;

@BetaApi
public final class ArrayContainsAll extends Function implements FilterCondition {
  @InternalApi
  ArrayContainsAll(Expr array, List<Expr> elements) {
    super("array_contains_all", Lists.newArrayList(array, new ListOfExprs(elements)));
  }
}
