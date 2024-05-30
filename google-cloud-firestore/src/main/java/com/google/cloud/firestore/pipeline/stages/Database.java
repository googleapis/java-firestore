package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;

@InternalApi
public final class Database implements Stage {
  private static final String name = "database";

  @InternalApi
  public Database() {}

  @Override
  public String getName() {
    return name;
  }
}
