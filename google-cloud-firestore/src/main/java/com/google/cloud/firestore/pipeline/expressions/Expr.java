package com.google.cloud.firestore.pipeline.expressions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface Expr {
  default Add add(Expr other) {
    return new Add(this, other);
  }

  default Add add(Object other) {
    return new Add(this, Constant.of(other));
  }

  default Subtract subtract(Expr other) {
    return new Subtract(this, other);
  }

  default Subtract subtract(Object other) {
    return new Subtract(this, Constant.of(other));
  }

  default Multiply multiply(Expr other) {
    return new Multiply(this, other);
  }

  default Multiply multiply(Object other) {
    return new Multiply(this, Constant.of(other));
  }

  default Divide divide(Expr other) {
    return new Divide(this, other);
  }

  default Divide divide(Object other) {
    return new Divide(this, Constant.of(other));
  }

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

  default ArrayConcat arrayConcat(Expr... elements) {
    return new ArrayConcat(this, Arrays.asList(elements));
  }

  default ArrayConcat arrayConcat(Object... elements) {
    return new ArrayConcat(
        this, Arrays.stream(elements).map(Constant::of).collect(Collectors.toList()));
  }

  default ArrayContains arrayContains(Expr element) {
    return new ArrayContains(this, element);
  }

  default ArrayContains arrayContains(Object element) {
    return new ArrayContains(this, Constant.of(element));
  }

  default ArrayContainsAll arrayContainsAll(Expr... elements) {
    return new ArrayContainsAll(this, Arrays.asList(elements));
  }

  default ArrayContainsAll arrayContainsAll(Object... elements) {
    return new ArrayContainsAll(
        this, Arrays.stream(elements).map(Constant::of).collect(Collectors.toList()));
  }

  default ArrayContainsAny arrayContainsAny(Expr... elements) {
    return new ArrayContainsAny(this, Arrays.asList(elements));
  }

  default ArrayContainsAny arrayContainsAny(Object... elements) {
    return new ArrayContainsAny(
        this, Arrays.stream(elements).map(Constant::of).collect(Collectors.toList()));
  }

  default ArrayFilter arrayFilter(FilterCondition filter) {
    return new ArrayFilter(this, filter);
  }

  default ArrayLength arrayLength() {
    return new ArrayLength(this);
  }

  default ArrayTransform arrayTransform(Function transform) {
    return new ArrayTransform(this, transform);
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

  default Length length() {
    return new Length(this);
  }

  default Like like(String pattern) {
    return new Like(this, Constant.of(pattern));
  }

  default RegexContains regexContains(String regex) {
    return new RegexContains(this, Constant.of(regex));
  }

  default StrConcat strConcat(List<Expr> elements) {
    return new StrConcat(this, elements);
  }

  default ToLowercase toLowercase() {
    return new ToLowercase(this);
  }

  default ToUppercase toUppercase() {
    return new ToUppercase(this);
  }

  default Trim trim() {
    return new Trim(this);
  }

  default MapGet mapGet(String key) {
    return new MapGet(this, key);
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
