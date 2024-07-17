package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;
import java.util.List;

@BetaApi
public final class ArrayContainsAny extends Function implements FilterCondition {
  @InternalApi
  ArrayContainsAny(Expr array, List<Expr> elements) {
    super("array_contains_any", Lists.newArrayList(array, new ListOfExprs(elements)));
  }
}
