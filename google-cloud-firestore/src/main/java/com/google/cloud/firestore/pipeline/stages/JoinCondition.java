package com.google.cloud.firestore.pipeline.stages;

import com.google.cloud.firestore.pipeline.expressions.Expr;
import com.google.cloud.firestore.pipeline.expressions.Field;
import java.util.Set;

public interface JoinCondition {

  class Expression implements JoinCondition {
    private final Expr expr;

    public Expression(Expr expr) {
      this.expr = expr;
    }

    public Expr getExpr() {
      return expr;
    }
  }

  class Using implements JoinCondition {
    private final Set<Field> fields;

    public Using(Set<Field> fields) {
      this.fields = fields;
    }

    public Set<Field> getFields() {
      return fields;
    }
  }
}
