package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import java.util.List;

@BetaApi
public final class And extends Function implements FilterCondition {
  @InternalApi
  And(List<FilterCondition> conditions) {
    super("and", conditions);
  }
}
