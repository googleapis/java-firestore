package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;
import java.util.Collections;

@BetaApi
public final class Count extends Function implements Accumulator {
  @InternalApi
  Count(Expr value, boolean distinct) {
    super("count", (value != null) ? Lists.newArrayList(value) : Collections.emptyList());
  }
}
