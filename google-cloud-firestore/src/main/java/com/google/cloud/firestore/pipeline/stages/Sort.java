package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.expressions.Ordering;
import com.google.common.collect.Lists;
import java.util.List;

public final class Sort implements Stage {

  private static final String name = "sort";
  private final List<Ordering> orders;

  @InternalApi
  public Sort(Ordering... orders) {
    this.orders = Lists.newArrayList(orders);
  }

  @InternalApi
  public String getName() {
    return name;
  }

  @InternalApi
  public List<Ordering> getOrders() {
    return orders;
  }
}
