package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class Parent extends Function {

  @InternalApi
  Parent(Expr value) {
    super("parent", Lists.newArrayList(value));
  }
}
