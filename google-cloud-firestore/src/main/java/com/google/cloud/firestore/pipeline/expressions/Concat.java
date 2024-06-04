package com.google.cloud.firestore.pipeline.expressions;

import java.util.List;

public final class Concat extends Function {
  Concat(List<Expr> exprs) {
    super("concat", exprs);
  }
}
