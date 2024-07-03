package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;

public class ArrayElement extends Function {
  ArrayElement() {
    super("array_element", Lists.newArrayList());
  }
}
