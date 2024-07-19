package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;
import java.util.List;

@BetaApi
public final class ArrayConcat extends Function {
  @InternalApi
  ArrayConcat(Expr array, List<Expr> rest) {
    super("array_concat", Lists.asList(array, rest.toArray(new Expr[] {})));
  }
}
