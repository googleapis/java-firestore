package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public final class MapGet extends Function {
  MapGet(Expr map, String name) {
    super("map_get", Lists.newArrayList(map, Constant.of(name)));
  }
}
