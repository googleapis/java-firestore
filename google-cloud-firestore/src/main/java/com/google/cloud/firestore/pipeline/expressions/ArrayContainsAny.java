package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;
import java.util.List;

public final class ArrayContainsAny extends Function implements FilterCondition {
  ArrayContainsAny(Expr array, List<Expr> elements) {
    super("array_contains_any", Lists.newArrayList(array, new ListOfExprs(elements)));
  }
}
