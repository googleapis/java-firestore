package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;

@InternalApi
public final class Offset implements Stage {

  private static final String name = "offset";
  private final int offset;

  @InternalApi
  public Offset(int offset) {
    this.offset = offset;
  }

  public int getOffset() {
    return offset;
  }

  @Override
  public String getName() {
    return name;
  }
}
