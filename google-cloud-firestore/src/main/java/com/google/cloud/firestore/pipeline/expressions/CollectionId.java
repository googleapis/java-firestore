package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public final class CollectionId extends Function {

  @InternalApi
  CollectionId(Expr value) {
    super("collection_id", Lists.newArrayList(value));
  }
}
