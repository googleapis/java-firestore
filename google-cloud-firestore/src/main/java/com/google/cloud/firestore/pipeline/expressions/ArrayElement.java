package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.Lists;

@BetaApi
public class ArrayElement extends Function {
  @InternalApi
  ArrayElement() {
    super("array_element", Lists.newArrayList());
  }
}
