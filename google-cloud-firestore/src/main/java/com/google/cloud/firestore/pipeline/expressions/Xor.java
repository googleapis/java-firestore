package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import java.util.List;

@BetaApi
public final class Xor extends Function implements FilterCondition {
  @InternalApi
  Xor(List<FilterCondition> conditions) {
    super("xor", conditions);
  }
}
