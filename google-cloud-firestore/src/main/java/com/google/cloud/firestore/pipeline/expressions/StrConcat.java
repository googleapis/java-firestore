package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;
import java.util.List;

public final class StrConcat extends Function {
  StrConcat(Expr first, List<Expr> exprs) {
    super("str_concat", Lists.newArrayList(first, new ListOfExprs(exprs)));
  }
}
