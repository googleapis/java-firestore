package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.InternalApi;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface Expr {
  default Equal equal(Expr expr) {
    return new Equal(this, expr);
  }

  default Equal equal(Object other) {
    return new Equal(this, Constant.of(other));
  }

  default NotEqual notEqual(Expr other) {
    return new NotEqual(this, other);
  }

  default NotEqual notEqual(Object other) {
    return new NotEqual(this, Constant.of(other));
  }

  default GreaterThan greaterThan(Expr other) {
    return new GreaterThan(this, other);
  }

  default GreaterThan greaterThan(Object other) {
    return new GreaterThan(this, Constant.of(other));
  }

  default GreaterThanOrEqual greaterThanOrEqual(Expr other) {
    return new GreaterThanOrEqual(this, other);
  }

  default GreaterThanOrEqual greaterThanOrEqual(Object other) {
    return new GreaterThanOrEqual(this, Constant.of(other));
  }

  default LessThan lessThan(Expr other) {
    return new LessThan(this, other);
  }

  default LessThan lessThan(Object other) {
    return new LessThan(this, Constant.of(other));
  }

  default LessThanOrEqual lessThanOrEqual(Expr other) {
    return new LessThanOrEqual(this, other);
  }

  default LessThanOrEqual lessThanOrEqual(Object other) {
    return new LessThanOrEqual(this, Constant.of(other));
  }

  default In inAny(Object... other) {
    List<Expr> othersAsExpr =
        Arrays.stream(other)
            .map(obj -> (obj instanceof Expr) ? (Expr) obj : Constant.of(obj))
            .collect(Collectors.toList());
    return new In(this, othersAsExpr);
  }

  default Not notInAny(Object... other) {
    return new Not(inAny(other));
  }

  default ArrayContains arrayContains(Expr element) {
    return new ArrayContains(this, element);
  }

  default ArrayContains arrayContains(Object element) {
    return new ArrayContains(this, Constant.of(element));
  }

  default ArrayContainsAny arrayContainsAny(Expr... elements) {
    return new ArrayContainsAny(this, Arrays.asList(elements));
  }

  default ArrayContainsAny arrayContainsAny(Object... elements) {
    return new ArrayContainsAny(
        this, Arrays.stream(elements).map(Constant::of).collect(Collectors.toList()));
  }

  default IsNaN isNaN() {
    return new IsNaN(this);
  }

  default IsNull isNull() {
    return new IsNull(this);
  }

  default Sum sum() {
    return new Sum(this, false);
  }

  default Avg avg() {
    return new Avg(this, false);
  }

  default Count count() {
    return new Count(this, false);
  }

  default Min min() {
    return new Min(this, false);
  }

  default Max max() {
    return new Max(this, false);
  }

  default CosineDistance cosineDistance(Expr other) {
    return new CosineDistance(this, other);
  }

  default CosineDistance cosineDistance(double[] other) {
    return new CosineDistance(this, Constant.ofVector(other));
  }

  default EuclideanDistance euclideanDistance(Expr other) {
    return new EuclideanDistance(this, other);
  }

  default EuclideanDistance euclideanDistance(double[] other) {
    return new EuclideanDistance(this, Constant.ofVector(other));
  }

  default DotProductDistance dotProductDistance(Expr other) {
    return new DotProductDistance(this, other);
  }

  default DotProductDistance dotProductDistance(double[] other) {
    return new DotProductDistance(this, Constant.ofVector(other));
  }

  default Ordering ascending() {
    return Ordering.ascending(this);
  }

  default Ordering descending() {
    return Ordering.descending(this);
  }

  default Selectable asAlias(String alias) {
    return new ExprWithAlias(this, alias);
  }
}

@InternalApi
final class ExprWithAlias implements Selectable {

  private final String alias;
  private final Expr expr;

  @InternalApi
  ExprWithAlias(Expr expr, String alias) {
    this.expr = expr;
    this.alias = alias;
  }

  @InternalApi
  String getAlias() {
    return alias;
  }

  @InternalApi
  Expr getExpr() {
    return expr;
  }
}
