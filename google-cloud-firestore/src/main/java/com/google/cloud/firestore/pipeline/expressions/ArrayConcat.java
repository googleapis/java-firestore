package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;
import java.util.List;

public final class ArrayConcat extends Function {
  ArrayConcat(Expr array, List<Expr> rest) {
    super("array_concat", Lists.newArrayList(array, new ListOfExprs(rest)));
  }
}
