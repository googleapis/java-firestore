package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.expressions.Expr;
import java.util.Map;

@InternalApi
public final class AddFields implements Stage {

  private static final String name = "add_fields";
  private final Map<String, Expr> fields;

  @InternalApi
  public AddFields(Map<String, Expr> fields) {
    this.fields = fields;
  }

  @InternalApi
  public Map<String, Expr> getFields() {
    return fields;
  }

  @Override
  public String getName() {
    return name;
  }
}
