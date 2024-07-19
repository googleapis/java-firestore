package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.InternalApi;
import com.google.firestore.v1.ArrayValue;
import com.google.firestore.v1.Value;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@InternalApi
public final class FunctionUtils {
  @InternalApi
  public static Value exprToValue(Expr expr) {
    if (expr instanceof Constant) {
      return ((Constant) expr).toProto();
    } else if (expr instanceof Field) {
      return ((Field) expr).toProto();
    } else if (expr instanceof Function) {
      return ((Function) expr).toProto();
    } else if (expr instanceof ListOfExprs) {
      ListOfExprs listOfExprs = (ListOfExprs) expr;
      return Value.newBuilder()
          .setArrayValue(
              ArrayValue.newBuilder()
                  .addAllValues(
                      listOfExprs.getConditions().stream()
                          .map(FunctionUtils::exprToValue)
                          .collect(Collectors.toList())))
          .build();
    } else {
      throw new IllegalArgumentException("Unsupported expression type: " + expr.getClass());
    }
  }

  @InternalApi
  static List<Expr> toExprList(Object[] other) {
    return Arrays.stream(other)
        .map(obj -> (obj instanceof Expr) ? (Expr) obj : Constant.of(obj))
        .collect(Collectors.toList());
  }
}
