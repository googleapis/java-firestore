package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import javax.annotation.Nonnull;

@InternalApi
public final class Collection implements Stage {

  private static final String name = "collection";
  @Nonnull private final String path;

  @InternalApi
  public Collection(@Nonnull String path) {
    if (!path.startsWith("/")) {
      this.path = "/" + path;
    } else {
      this.path = path;
    }
  }

  @InternalApi
  public String getPath() {
    return path;
  }

  @Override
  public String getName() {
    return name;
  }
}
