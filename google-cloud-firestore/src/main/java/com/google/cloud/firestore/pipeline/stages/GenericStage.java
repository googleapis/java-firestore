package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import java.util.List;

@InternalApi
public final class GenericStage implements Stage {

  private final String name;
  private List<Object> params;

  @InternalApi
  public GenericStage(String name, List<Object> params) {
    this.name = name;
    this.params = params;
  }

  @Override
  public String getName() {
    return name;
  }

  @InternalApi
  public List<Object> getParams() {
    return params;
  }
}
