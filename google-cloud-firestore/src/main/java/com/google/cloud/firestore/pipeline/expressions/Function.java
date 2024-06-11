package com.google.cloud.firestore.pipeline.expressions;

import com.google.common.collect.Lists;
import com.google.firestore.v1.Value;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Function implements Expr {
  private final String name;
  private final List<Expr> params;

  protected Function(String name, List<? extends Expr> params) {
    this.name = name;
    this.params = Collections.unmodifiableList(params);
  }

  Value toProto() {
    return Value.newBuilder()
        .setFunctionValue(
            com.google.firestore.v1.Function.newBuilder()
                .setName(this.name)
                .addAllArgs(
                    this.params.stream()
                        .map(FunctionUtils::exprToValue)
                        .collect(Collectors.toList())))
        .build();
  }

  public static Equal equal(Expr left, Expr right) {
    return new Equal(left, right);
  }

  public static Equal equal(Expr left, Object right) {
    return new Equal(left, Constant.of(right));
  }

  public static Equal equal(String left, Expr right) {
    return new Equal(Field.of(left), right);
  }

  public static Equal equal(String left, Object right) {
    return new Equal(Field.of(left), Constant.of(right));
  }

  public static NotEqual notEqual(Expr left, Expr right) {
    return new NotEqual(left, right);
  }

  public static NotEqual notEqual(Expr left, Object right) {
    return new NotEqual(left, Constant.of(right));
  }

  public static NotEqual notEqual(String left, Expr right) {
    return new NotEqual(Field.of(left), right);
  }

  public static NotEqual notEqual(String left, Object right) {
    return new NotEqual(Field.of(left), Constant.of(right));
  }

  public static GreaterThan greaterThan(Expr left, Expr right) {
    return new GreaterThan(left, right);
  }

  public static GreaterThan greaterThan(Expr left, Object right) {
    return new GreaterThan(left, Constant.of(right));
  }

  public static GreaterThan greaterThan(String left, Expr right) {
    return new GreaterThan(Field.of(left), right);
  }

  public static GreaterThan greaterThan(String left, Object right) {
    return new GreaterThan(Field.of(left), Constant.of(right));
  }

  public static GreaterThanOrEqual greaterThanOrEqual(Expr left, Expr right) {
    return new GreaterThanOrEqual(left, right);
  }

  public static GreaterThanOrEqual greaterThanOrEqual(Expr left, Object right) {
    return new GreaterThanOrEqual(left, Constant.of(right));
  }

  public static GreaterThanOrEqual greaterThanOrEqual(String left, Expr right) {
    return new GreaterThanOrEqual(Field.of(left), right);
  }

  public static GreaterThanOrEqual greaterThanOrEqual(String left, Object right) {
    return new GreaterThanOrEqual(Field.of(left), Constant.of(right));
  }

  public static LessThan lessThan(Expr left, Expr right) {
    return new LessThan(left, right);
  }

  public static LessThan lessThan(Expr left, Object right) {
    return new LessThan(left, Constant.of(right));
  }

  public static LessThan lessThan(String left, Expr right) {
    return new LessThan(Field.of(left), right);
  }

  public static LessThan lessThan(String left, Object right) {
    return new LessThan(Field.of(left), Constant.of(right));
  }

  public static LessThanOrEqual lessThanOrEqual(Expr left, Expr right) {
    return new LessThanOrEqual(left, right);
  }

  public static LessThanOrEqual lessThanOrEqual(Expr left, Object right) {
    return new LessThanOrEqual(left, Constant.of(right));
  }

  public static LessThanOrEqual lessThanOrEqual(String left, Expr right) {
    return new LessThanOrEqual(Field.of(left), right);
  }

  public static LessThanOrEqual lessThanOrEqual(String left, Object right) {
    return new LessThanOrEqual(Field.of(left), Constant.of(right));
  }

  public static In inAny(Expr left, List<Object> values) {
    List<Expr> othersAsExpr =
        values.stream()
            .map(obj -> (obj instanceof Expr) ? (Expr) obj : Constant.of(obj))
            .collect(Collectors.toList());
    return new In(left, othersAsExpr);
  }

  public static In inAny(String left, List<Object> values) {
    return inAny(Field.of(left), values);
  }

  public static Not notInAny(Expr left, List<Object> values) {
    return new Not(inAny(left, values));
  }

  public static Not notInAny(String left, List<Object> values) {
    return new Not(inAny(Field.of(left), values));
  }

  public static And and(FilterCondition left, FilterCondition right) {
    return new And(Lists.newArrayList(left, right));
  }

  public static And and(FilterCondition left, FilterCondition... other) {
    List<FilterCondition> conditions = Lists.newArrayList(left);
    conditions.addAll(Arrays.asList(other));
    return new And(conditions);
  }

  public static Or or(FilterCondition left, FilterCondition right) {
    return new Or(Lists.newArrayList(left, right));
  }

  public static Or or(FilterCondition left, FilterCondition... other) {
    List<FilterCondition> conditions = Lists.newArrayList(left);
    conditions.addAll(Arrays.asList(other));
    return new Or(conditions);
  }

  // File: FunctionUtils.java (or similar)

  // ... other static methods ...

  public static ArrayContains arrayContains(Expr expr, Expr element) {
    return new ArrayContains(expr, element);
  }

  public static ArrayContains arrayContains(String field, Expr element) {
    return new ArrayContains(Field.of(field), element);
  }

  public static ArrayContains arrayContains(Expr expr, Object element) {
    return new ArrayContains(expr, Constant.of(element));
  }

  public static ArrayContains arrayContains(String field, Object element) {
    return new ArrayContains(Field.of(field), Constant.of(element));
  }

  public static ArrayContainsAny arrayContainsAny(Expr expr, Expr... elements) {
    return new ArrayContainsAny(expr, Arrays.asList(elements));
  }

  public static ArrayContainsAny arrayContainsAny(Expr expr, Object... elements) {
    return new ArrayContainsAny(
        expr, Arrays.stream(elements).map(Constant::of).collect(Collectors.toList()));
  }

  public static ArrayContainsAny arrayContainsAny(String field, Expr... elements) {
    return new ArrayContainsAny(Field.of(field), Arrays.asList(elements));
  }

  public static ArrayContainsAny arrayContainsAny(String field, Object... elements) {
    return new ArrayContainsAny(
        Field.of(field), Arrays.stream(elements).map(Constant::of).collect(Collectors.toList()));
  }

  public static IsNaN isNaN(Expr expr) {
    return new IsNaN(expr);
  }

  public static IsNaN isNaN(String field) {
    return new IsNaN(Field.of(field));
  }

  public static IsNull isNull(Expr expr) {
    return new IsNull(expr);
  }

  public static IsNull isNull(String field) {
    return new IsNull(Field.of(field));
  }

  public static Not not(Expr expr) {
    return new Not(expr);
  }

  public static Sum sum(Expr expr) {
    return new Sum(expr, false);
  }

  public static Sum sum(String field) {
    return new Sum(Field.of(field), false);
  }

  public static Avg avg(Expr expr) {
    return new Avg(expr, false);
  }

  public static Avg avg(String field) {
    return new Avg(Field.of(field), false);
  }

  // Note: There seems to be a typo in the Kotlin code.
  //       `min` and `max` are calling `Sum` and `Avg` constructors respectively
  public static Min min(Expr expr) {
    return new Min(expr, false); // Corrected constructor call
  }

  public static Min min(String field) {
    return new Min(Field.of(field), false); // Corrected constructor call
  }

  public static Max max(Expr expr) {
    return new Max(expr, false); // Corrected constructor call
  }

  public static Max max(String field) {
    return new Max(Field.of(field), false); // Corrected constructor call
  }

  public static Count count(Expr expr) {
    return new Count(expr, false);
  }

  public static Count count(String field) {
    return new Count(Field.of(field), false);
  }

  public static Count countAll() {
    return new Count(null, false);
  }

  public static CosineDistance cosineDistance(Expr expr, Expr other) {
    return new CosineDistance(expr, other);
  }

  public static CosineDistance cosineDistance(Expr expr, double[] other) {
    return new CosineDistance(expr, Constant.ofVector(other));
  }

  public static CosineDistance cosineDistance(String field, Expr other) {
    return new CosineDistance(Field.of(field), other);
  }

  public static CosineDistance cosineDistance(String field, double[] other) {
    return new CosineDistance(Field.of(field), Constant.ofVector(other));
  }

  // Typo in original code: dotProductDistance should return DotProductDistance objects
  public static DotProductDistance dotProductDistance(Expr expr, Expr other) {
    return new DotProductDistance(expr, other);
  }

  public static DotProductDistance dotProductDistance(Expr expr, double[] other) {
    return new DotProductDistance(expr, Constant.ofVector(other));
  }

  public static DotProductDistance dotProductDistance(String field, Expr other) {
    return new DotProductDistance(Field.of(field), other);
  }

  public static DotProductDistance dotProductDistance(String field, double[] other) {
    return new DotProductDistance(Field.of(field), Constant.ofVector(other));
  }

  public static EuclideanDistance euclideanDistance(Expr expr, Expr other) {
    return new EuclideanDistance(expr, other);
  }

  public static EuclideanDistance euclideanDistance(Expr expr, double[] other) {
    return new EuclideanDistance(expr, Constant.ofVector(other));
  }

  public static EuclideanDistance euclideanDistance(String field, Expr other) {
    return new EuclideanDistance(Field.of(field), other);
  }

  public static EuclideanDistance euclideanDistance(String field, double[] other) {
    return new EuclideanDistance(Field.of(field), Constant.ofVector(other));
  }

  public static Function function(String name, List<Expr> params) {
    return new Function(name, params);
  }
}
