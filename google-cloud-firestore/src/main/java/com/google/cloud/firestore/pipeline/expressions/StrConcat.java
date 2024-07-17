package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;
import java.util.List;

@BetaApi
public final class StrConcat extends Function {
  @InternalApi
  StrConcat(Expr first, List<Expr> exprs) {
    super("str_concat", Lists.newArrayList(first, new ListOfExprs(exprs)));
  }
}
