package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class MapGet extends Function {
  @InternalApi
  MapGet(Expr map, String name) {
    super("map_get", Lists.newArrayList(map, Constant.of(name)));
  }
}
