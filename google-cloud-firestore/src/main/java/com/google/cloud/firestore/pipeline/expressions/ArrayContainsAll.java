package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;
import java.util.List;

public final class ArrayContainsAll extends Function implements FilterCondition {
  ArrayContainsAll(Expr array, List<Expr> elements) {
    super("array_contains_all", Lists.newArrayList(array, new ListOfExprs(elements)));
  }
}
