package com.google.cloud.firestore.pipeline.expressions;

import java.util.List;

public final class Generic extends Function {
  Generic(String name, List<Expr> params) {
    super(name, params);
  }
}
