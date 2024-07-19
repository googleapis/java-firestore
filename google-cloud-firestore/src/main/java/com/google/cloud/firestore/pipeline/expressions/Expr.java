package com.google.cloud.firestore.pipeline.expressions;

import static com.google.cloud.firestore.pipeline.expressions.FunctionUtils.toExprList;

import com.google.api.core.BetaApi;
import java.util.Arrays;
import java.util.List;

@BetaApi
public interface Expr {
  @BetaApi
  default Add add(Expr other) {
    return new Add(this, other);
  }

  @BetaApi
  default Add add(Object other) {
    return new Add(this, Constant.of(other));
  }

  @BetaApi
  default Subtract subtract(Expr other) {
    return new Subtract(this, other);
  }

  @BetaApi
  default Subtract subtract(Object other) {
    return new Subtract(this, Constant.of(other));
  }

  @BetaApi
  default Multiply multiply(Expr other) {
    return new Multiply(this, other);
  }

  @BetaApi
  default Multiply multiply(Object other) {
    return new Multiply(this, Constant.of(other));
  }

  @BetaApi
  default Divide divide(Expr other) {
    return new Divide(this, other);
  }

  @BetaApi
  default Divide divide(Object other) {
    return new Divide(this, Constant.of(other));
  }

  @BetaApi
  default Eq eq(Expr expr) {
    return new Eq(this, expr);
  }

  @BetaApi
  default Eq eq(Object other) {
    return new Eq(this, Constant.of(other));
  }

  @BetaApi
  default Neq neq(Expr other) {
    return new Neq(this, other);
  }

  @BetaApi
  default Neq neq(Object other) {
    return new Neq(this, Constant.of(other));
  }

  @BetaApi
  default Gt gt(Expr other) {
    return new Gt(this, other);
  }

  @BetaApi
  default Gt gt(Object other) {
    return new Gt(this, Constant.of(other));
  }

  @BetaApi
  default Gte gte(Expr other) {
    return new Gte(this, other);
  }

  @BetaApi
  default Gte gte(Object other) {
    return new Gte(this, Constant.of(other));
  }

  @BetaApi
  default Lt lt(Expr other) {
    return new Lt(this, other);
  }

  @BetaApi
  default Lt lt(Object other) {
    return new Lt(this, Constant.of(other));
  }

  @BetaApi
  default Lte lte(Expr other) {
    return new Lte(this, other);
  }

  @BetaApi
  default Lte lte(Object other) {
    return new Lte(this, Constant.of(other));
  }

  @BetaApi
  default In inAny(Object... other) {
    return new In(this, toExprList(other));
  }

  @BetaApi
  default Not notInAny(Object... other) {
    return new Not(inAny(other));
  }

  @BetaApi
  default ArrayConcat arrayConcat(Expr... elements) {
    return new ArrayConcat(this, Arrays.asList(elements));
  }

  @BetaApi
  default ArrayConcat arrayConcat(Object... elements) {
    return new ArrayConcat(this, toExprList(elements));
  }

  @BetaApi
  default ArrayContains arrayContains(Expr element) {
    return new ArrayContains(this, element);
  }

  @BetaApi
  default ArrayContains arrayContains(Object element) {
    return new ArrayContains(this, Constant.of(element));
  }

  @BetaApi
  default ArrayContainsAll arrayContainsAll(Expr... elements) {
    return new ArrayContainsAll(this, Arrays.asList(elements));
  }

  @BetaApi
  default ArrayContainsAll arrayContainsAll(Object... elements) {
    return new ArrayContainsAll(this, toExprList(elements));
  }

  @BetaApi
  default ArrayContainsAny arrayContainsAny(Expr... elements) {
    return new ArrayContainsAny(this, Arrays.asList(elements));
  }

  @BetaApi
  default ArrayContainsAny arrayContainsAny(Object... elements) {
    return new ArrayContainsAny(this, toExprList(elements));
  }

  @BetaApi
  default ArrayFilter arrayFilter(FilterCondition filter) {
    return new ArrayFilter(this, filter);
  }

  @BetaApi
  default ArrayLength arrayLength() {
    return new ArrayLength(this);
  }

  @BetaApi
  default ArrayTransform arrayTransform(Function transform) {
    return new ArrayTransform(this, transform);
  }

  @BetaApi
  default IsNaN isNaN() {
    return new IsNaN(this);
  }

  @BetaApi
  default IsNull isNull() {
    return new IsNull(this);
  }

  @BetaApi
  default Sum sum() {
    return new Sum(this, false);
  }

  @BetaApi
  default Avg avg() {
    return new Avg(this, false);
  }

  @BetaApi
  default Count count() {
    return new Count(this, false);
  }

  @BetaApi
  default Min min() {
    return new Min(this, false);
  }

  @BetaApi
  default Max max() {
    return new Max(this, false);
  }

  @BetaApi
  default Length length() {
    return new Length(this);
  }

  @BetaApi
  default Like like(String pattern) {
    return new Like(this, Constant.of(pattern));
  }

  @BetaApi
  default RegexContains regexContains(String regex) {
    return new RegexContains(this, Constant.of(regex));
  }

  @BetaApi
  default RegexMatch regexMatches(String regex) {
    return new RegexMatch(this, Constant.of(regex));
  }

  @BetaApi
  default StartsWith startsWith(String prefix) {
    return new StartsWith(this, Constant.of(prefix));
  }

  @BetaApi
  default StartsWith startsWith(Expr prefix) {
    return new StartsWith(this, prefix);
  }

  @BetaApi
  default EndsWith endsWith(String postfix) {
    return new EndsWith(this, Constant.of(postfix));
  }

  @BetaApi
  default EndsWith endsWith(Expr postfix) {
    return new EndsWith(this, postfix);
  }

  @BetaApi
  default StrConcat strConcat(List<Expr> elements) {
    return new StrConcat(this, elements);
  }

  @BetaApi
  default ToLowercase toLowercase() {
    return new ToLowercase(this);
  }

  @BetaApi
  default ToUppercase toUppercase() {
    return new ToUppercase(this);
  }

  @BetaApi
  default Trim trim() {
    return new Trim(this);
  }

  @BetaApi
  default MapGet mapGet(String key) {
    return new MapGet(this, key);
  }

  @BetaApi
  default CosineDistance cosineDistance(Expr other) {
    return new CosineDistance(this, other);
  }

  @BetaApi
  default CosineDistance cosineDistance(double[] other) {
    return new CosineDistance(this, Constant.ofVector(other));
  }

  @BetaApi
  default EuclideanDistance euclideanDistance(Expr other) {
    return new EuclideanDistance(this, other);
  }

  @BetaApi
  default EuclideanDistance euclideanDistance(double[] other) {
    return new EuclideanDistance(this, Constant.ofVector(other));
  }

  @BetaApi
  default DotProductDistance dotProductDistance(Expr other) {
    return new DotProductDistance(this, other);
  }

  @BetaApi
  default DotProductDistance dotProductDistance(double[] other) {
    return new DotProductDistance(this, Constant.ofVector(other));
  }

  @BetaApi
  default Ordering ascending() {
    return Ordering.ascending(this);
  }

  @BetaApi
  default Ordering descending() {
    return Ordering.descending(this);
  }

  @BetaApi
  default Selectable as(String alias) {
    return new ExprWithAlias(this, alias);
  }
}
