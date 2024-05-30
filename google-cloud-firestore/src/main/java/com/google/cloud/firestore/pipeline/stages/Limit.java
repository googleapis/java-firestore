package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;

@InternalApi
public final class Limit implements Stage {

  private static final String name = "limit";
  private int limit;

  @InternalApi
  public Limit(int limit) {
    this.limit = limit;
  }

  public int getLimit() {
    return limit;
  }

  @Override
  public String getName() {
    return name;
  }
}
