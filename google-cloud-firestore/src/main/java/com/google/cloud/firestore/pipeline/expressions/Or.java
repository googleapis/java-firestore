package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import java.util.List;

@BetaApi
public final class Or extends Function implements FilterCondition {
  @InternalApi
  Or(List<FilterCondition> conditions) {
    super("or", conditions);
  }
}
