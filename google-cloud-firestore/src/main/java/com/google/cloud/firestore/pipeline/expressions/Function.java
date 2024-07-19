package com.google.cloud.firestore.pipeline.expressions;

import static com.google.cloud.firestore.pipeline.expressions.FunctionUtils.toExprList;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.firestore.DocumentReference;
import com.google.common.collect.Lists;
import com.google.firestore.v1.Value;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@BetaApi
public class Function implements Expr {
  private final String name;
  private final List<Expr> params;

  protected Function(String name, List<? extends Expr> params) {
    this.name = name;
    this.params = Collections.unmodifiableList(params);
  }

  @InternalApi
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

  @BetaApi
  public static CollectionId collectionId(String path) {
    return new CollectionId(Constant.of(path));
  }

  @BetaApi
  public static CollectionId collectionId(DocumentReference ref) {
    return new CollectionId(Constant.of(ref.getPath()));
  }

  @BetaApi
  public static Parent parent(String path) {
    return new Parent(Constant.of(path));
  }

  @BetaApi
  public static Parent parent(DocumentReference ref) {
    return new Parent(Constant.of(ref.getPath()));
  }

  @BetaApi
  public static Add add(Expr left, Expr right) {
    return new Add(left, right);
  }

  @BetaApi
  public static Add add(Expr left, Object right) {
    return new Add(left, Constant.of(right));
  }

  @BetaApi
  public static Add add(String left, Expr right) {
    return new Add(Field.of(left), right);
  }

  @BetaApi
  public static Add add(String left, Object right) {
    return new Add(Field.of(left), Constant.of(right));
  }

  @BetaApi
  public static Subtract subtract(Expr left, Expr right) {
    return new Subtract(left, right);
  }

  @BetaApi
  public static Subtract subtract(Expr left, Object right) {
    return new Subtract(left, Constant.of(right));
  }

  @BetaApi
  public static Subtract subtract(String left, Expr right) {
    return new Subtract(Field.of(left), right);
  }

  @BetaApi
  public static Subtract subtract(String left, Object right) {
    return new Subtract(Field.of(left), Constant.of(right));
  }

  @BetaApi
  public static Multiply multiply(Expr left, Expr right) {
    return new Multiply(left, right);
  }

  @BetaApi
  public static Multiply multiply(Expr left, Object right) {
    return new Multiply(left, Constant.of(right));
  }

  @BetaApi
  public static Multiply multiply(String left, Expr right) {
    return new Multiply(Field.of(left), right);
  }

  @BetaApi
  public static Multiply multiply(String left, Object right) {
    return new Multiply(Field.of(left), Constant.of(right));
  }

  @BetaApi
  public static Divide divide(Expr left, Expr right) {
    return new Divide(left, right);
  }

  @BetaApi
  public static Divide divide(Expr left, Object right) {
    return new Divide(left, Constant.of(right));
  }

  @BetaApi
  public static Divide divide(String left, Expr right) {
    return new Divide(Field.of(left), right);
  }

  @BetaApi
  public static Divide divide(String left, Object right) {
    return new Divide(Field.of(left), Constant.of(right));
  }

  @BetaApi
  public static Eq eq(Expr left, Expr right) {
    return new Eq(left, right);
  }

  @BetaApi
  public static Eq eq(Expr left, Object right) {
    return new Eq(left, Constant.of(right));
  }

  @BetaApi
  public static Eq eq(String left, Expr right) {
    return new Eq(Field.of(left), right);
  }

  @BetaApi
  public static Eq eq(String left, Object right) {
    return new Eq(Field.of(left), Constant.of(right));
  }

  @BetaApi
  public static Neq neq(Expr left, Expr right) {
    return new Neq(left, right);
  }

  @BetaApi
  public static Neq neq(Expr left, Object right) {
    return new Neq(left, Constant.of(right));
  }

  @BetaApi
  public static Neq neq(String left, Expr right) {
    return new Neq(Field.of(left), right);
  }

  @BetaApi
  public static Neq neq(String left, Object right) {
    return new Neq(Field.of(left), Constant.of(right));
  }

  @BetaApi
  public static Gt gt(Expr left, Expr right) {
    return new Gt(left, right);
  }

  @BetaApi
  public static Gt gt(Expr left, Object right) {
    return new Gt(left, Constant.of(right));
  }

  @BetaApi
  public static Gt gt(String left, Expr right) {
    return new Gt(Field.of(left), right);
  }

  @BetaApi
  public static Gt gt(String left, Object right) {
    return new Gt(Field.of(left), Constant.of(right));
  }

  @BetaApi
  public static Gte gte(Expr left, Expr right) {
    return new Gte(left, right);
  }

  @BetaApi
  public static Gte gte(Expr left, Object right) {
    return new Gte(left, Constant.of(right));
  }

  @BetaApi
  public static Gte gte(String left, Expr right) {
    return new Gte(Field.of(left), right);
  }

  @BetaApi
  public static Gte gte(String left, Object right) {
    return new Gte(Field.of(left), Constant.of(right));
  }

  @BetaApi
  public static Lt lt(Expr left, Expr right) {
    return new Lt(left, right);
  }

  @BetaApi
  public static Lt lt(Expr left, Object right) {
    return new Lt(left, Constant.of(right));
  }

  @BetaApi
  public static Lt lt(String left, Expr right) {
    return new Lt(Field.of(left), right);
  }

  @BetaApi
  public static Lt lt(String left, Object right) {
    return new Lt(Field.of(left), Constant.of(right));
  }

  @BetaApi
  public static Lte lte(Expr left, Expr right) {
    return new Lte(left, right);
  }

  @BetaApi
  public static Lte lte(Expr left, Object right) {
    return new Lte(left, Constant.of(right));
  }

  @BetaApi
  public static Lte lte(String left, Expr right) {
    return new Lte(Field.of(left), right);
  }

  @BetaApi
  public static Lte lte(String left, Object right) {
    return new Lte(Field.of(left), Constant.of(right));
  }

  @BetaApi
  public static Exists exists(String field) {
    return new Exists(Field.of(field));
  }

  @BetaApi
  public static Exists exists(Field field) {
    return new Exists(field);
  }

  @BetaApi
  public static In inAny(Expr left, List<Object> values) {
    List<Expr> othersAsExpr =
        values.stream()
            .map(obj -> (obj instanceof Expr) ? (Expr) obj : Constant.of(obj))
            .collect(Collectors.toList());
    return new In(left, othersAsExpr);
  }

  @BetaApi
  public static In inAny(String left, List<Object> values) {
    return inAny(Field.of(left), values);
  }

  @BetaApi
  public static Not notInAny(Expr left, List<Object> values) {
    return new Not(inAny(left, values));
  }

  @BetaApi
  public static Not notInAny(String left, List<Object> values) {
    return new Not(inAny(Field.of(left), values));
  }

  @BetaApi
  public static And and(FilterCondition left, FilterCondition right) {
    return new And(Lists.newArrayList(left, right));
  }

  @BetaApi
  public static And and(FilterCondition left, FilterCondition... other) {
    List<FilterCondition> conditions = Lists.newArrayList(left);
    conditions.addAll(Arrays.asList(other));
    return new And(conditions);
  }

  @BetaApi
  public static Or or(FilterCondition left, FilterCondition right) {
    return new Or(Lists.newArrayList(left, right));
  }

  @BetaApi
  public static Or or(FilterCondition left, FilterCondition... other) {
    List<FilterCondition> conditions = Lists.newArrayList(left);
    conditions.addAll(Arrays.asList(other));
    return new Or(conditions);
  }

  @BetaApi
  public static Xor xor(FilterCondition left, FilterCondition right) {
    return new Xor(Lists.newArrayList(left, right));
  }

  @BetaApi
  public static Xor xor(FilterCondition left, FilterCondition... other) {
    List<FilterCondition> conditions = Lists.newArrayList(left);
    conditions.addAll(Arrays.asList(other));
    return new Xor(conditions);
  }

  @BetaApi
  public static If ifThen(FilterCondition condition, Expr thenExpr) {
    return new If(condition, thenExpr, null);
  }

  @BetaApi
  public static If ifThenElse(FilterCondition condition, Expr thenExpr, Expr elseExpr) {
    return new If(condition, thenExpr, elseExpr);
  }

  @BetaApi
  public static ArrayConcat arrayConcat(Expr expr, Expr... elements) {
    return new ArrayConcat(expr, Arrays.asList(elements));
  }

  @BetaApi
  public static ArrayConcat arrayConcat(Expr expr, Object... elements) {
    return new ArrayConcat(expr, toExprList(elements));
  }

  @BetaApi
  public static ArrayConcat arrayConcat(String field, Expr... elements) {
    return new ArrayConcat(Field.of(field), Arrays.asList(elements));
  }

  @BetaApi
  public static ArrayConcat arrayConcat(String field, Object... elements) {
    return new ArrayConcat(Field.of(field), toExprList(elements));
  }

  @BetaApi
  public static ArrayContains arrayContains(Expr expr, Expr element) {
    return new ArrayContains(expr, element);
  }

  @BetaApi
  public static ArrayContains arrayContains(String field, Expr element) {
    return new ArrayContains(Field.of(field), element);
  }

  @BetaApi
  public static ArrayContains arrayContains(Expr expr, Object element) {
    return new ArrayContains(expr, Constant.of(element));
  }

  @BetaApi
  public static ArrayContains arrayContains(String field, Object element) {
    return new ArrayContains(Field.of(field), Constant.of(element));
  }

  @BetaApi
  public static ArrayContainsAll arrayContainsAll(Expr expr, Expr... elements) {
    return new ArrayContainsAll(expr, Arrays.asList(elements));
  }

  @BetaApi
  public static ArrayContainsAll arrayContainsAll(Expr expr, Object... elements) {
    return new ArrayContainsAll(expr, toExprList(elements));
  }

  @BetaApi
  public static ArrayContainsAll arrayContainsAll(String field, Expr... elements) {
    return new ArrayContainsAll(Field.of(field), Arrays.asList(elements));
  }

  @BetaApi
  public static ArrayContainsAll arrayContainsAll(String field, Object... elements) {
    return new ArrayContainsAll(Field.of(field), toExprList(elements));
  }

  @BetaApi
  public static ArrayContainsAny arrayContainsAny(Expr expr, Expr... elements) {
    return new ArrayContainsAny(expr, Arrays.asList(elements));
  }

  @BetaApi
  public static ArrayContainsAny arrayContainsAny(Expr expr, Object... elements) {
    return new ArrayContainsAny(expr, toExprList(elements));
  }

  @BetaApi
  public static ArrayContainsAny arrayContainsAny(String field, Expr... elements) {
    return new ArrayContainsAny(Field.of(field), Arrays.asList(elements));
  }

  @BetaApi
  public static ArrayContainsAny arrayContainsAny(String field, Object... elements) {
    return new ArrayContainsAny(Field.of(field), toExprList(elements));
  }

  @BetaApi
  public static ArrayFilter arrayFilter(Expr expr, FilterCondition filter) {
    return new ArrayFilter(expr, filter);
  }

  @BetaApi
  public static ArrayFilter arrayFilter(String field, FilterCondition filter) {
    return new ArrayFilter(Field.of(field), filter);
  }

  @BetaApi
  public static ArrayLength arrayLength(Expr expr) {
    return new ArrayLength(expr);
  }

  @BetaApi
  public static ArrayLength arrayLength(String field) {
    return new ArrayLength(Field.of(field));
  }

  @BetaApi
  public static ArrayTransform arrayTransform(Expr expr, Function transform) {
    return new ArrayTransform(expr, transform);
  }

  @BetaApi
  public static ArrayTransform arrayTransform(String field, Function transform) {
    return new ArrayTransform(Field.of(field), transform);
  }

  @BetaApi
  public static Length length(Expr expr) {
    return new Length(expr);
  }

  @BetaApi
  public static Length length(String field) {
    return new Length(Field.of(field));
  }

  @BetaApi
  public static Like like(Expr expr, String pattern) {
    return new Like(expr, Constant.of(pattern));
  }

  @BetaApi
  public static Like like(String field, String pattern) {
    return new Like(Field.of(field), Constant.of(pattern));
  }

  @BetaApi
  public static RegexContains regexContains(Expr expr, String pattern) {
    return new RegexContains(expr, Constant.of(pattern));
  }

  @BetaApi
  public static RegexContains regexContains(String field, String pattern) {
    return new RegexContains(Field.of(field), Constant.of(pattern));
  }

  @BetaApi
  public static RegexMatch regexMatch(Expr expr, String pattern) {
    return new RegexMatch(expr, Constant.of(pattern));
  }

  @BetaApi
  public static RegexMatch regexMatch(String field, String pattern) {
    return new RegexMatch(Field.of(field), Constant.of(pattern));
  }

  @BetaApi
  public static StrConcat strConcat(Expr expr, Expr... elements) {
    return new StrConcat(expr, Arrays.asList(elements));
  }

  @BetaApi
  public static StrConcat strConcat(Expr expr, Object... elements) {
    return new StrConcat(expr, toExprList(elements));
  }

  @BetaApi
  public static StrConcat strConcat(String field, Expr... elements) {
    return new StrConcat(Field.of(field), Arrays.asList(elements));
  }

  @BetaApi
  public static StrConcat strConcat(String field, Object... elements) {
    return new StrConcat(Field.of(field), toExprList(elements));
  }

  @BetaApi
  public static ToLowercase toLowercase(Expr expr) {
    return new ToLowercase(expr);
  }

  @BetaApi
  public static ToLowercase toLowercase(String field) {
    return new ToLowercase(Field.of(field));
  }

  @BetaApi
  public static ToUppercase toUppercase(Expr expr) {
    return new ToUppercase(expr);
  }

  @BetaApi
  public static ToUppercase toUppercase(String field) {
    return new ToUppercase(Field.of(field));
  }

  @BetaApi
  public static Trim trim(Expr expr) {
    return new Trim(expr);
  }

  @BetaApi
  public static Trim trim(String field) {
    return new Trim(Field.of(field));
  }

  @BetaApi
  public static IsNaN isNaN(Expr expr) {
    return new IsNaN(expr);
  }

  @BetaApi
  public static IsNaN isNaN(String field) {
    return new IsNaN(Field.of(field));
  }

  @BetaApi
  public static IsNull isNull(Expr expr) {
    return new IsNull(expr);
  }

  @BetaApi
  public static IsNull isNull(String field) {
    return new IsNull(Field.of(field));
  }

  @BetaApi
  public static Not not(FilterCondition expr) {
    return new Not(expr);
  }

  @BetaApi
  public static Sum sum(Expr expr) {
    return new Sum(expr, false);
  }

  @BetaApi
  public static Sum sum(String field) {
    return new Sum(Field.of(field), false);
  }

  @BetaApi
  public static Avg avg(Expr expr) {
    return new Avg(expr, false);
  }

  @BetaApi
  public static Avg avg(String field) {
    return new Avg(Field.of(field), false);
  }

  @BetaApi
  public static Min min(Expr expr) {
    return new Min(expr, false); // Corrected constructor call
  }

  @BetaApi
  public static Min min(String field) {
    return new Min(Field.of(field), false); // Corrected constructor call
  }

  @BetaApi
  public static Max max(Expr expr) {
    return new Max(expr, false); // Corrected constructor call
  }

  @BetaApi
  public static Max max(String field) {
    return new Max(Field.of(field), false); // Corrected constructor call
  }

  @BetaApi
  public static Count count(Expr expr) {
    return new Count(expr, false);
  }

  @BetaApi
  public static Count count(String field) {
    return new Count(Field.of(field), false);
  }

  @BetaApi
  public static CountIf countIf(FilterCondition condition) {
    return new CountIf(condition, false);
  }

  @BetaApi
  public static Count countAll() {
    return new Count(null, false);
  }

  @BetaApi
  public static CosineDistance cosineDistance(Expr expr, Expr other) {
    return new CosineDistance(expr, other);
  }

  @BetaApi
  public static CosineDistance cosineDistance(Expr expr, double[] other) {
    return new CosineDistance(expr, Constant.ofVector(other));
  }

  @BetaApi
  public static CosineDistance cosineDistance(String field, Expr other) {
    return new CosineDistance(Field.of(field), other);
  }

  @BetaApi
  public static CosineDistance cosineDistance(String field, double[] other) {
    return new CosineDistance(Field.of(field), Constant.ofVector(other));
  }

  @BetaApi
  public static DotProductDistance dotProductDistance(Expr expr, Expr other) {
    return new DotProductDistance(expr, other);
  }

  @BetaApi
  public static DotProductDistance dotProductDistance(Expr expr, double[] other) {
    return new DotProductDistance(expr, Constant.ofVector(other));
  }

  @BetaApi
  public static DotProductDistance dotProductDistance(String field, Expr other) {
    return new DotProductDistance(Field.of(field), other);
  }

  @BetaApi
  public static DotProductDistance dotProductDistance(String field, double[] other) {
    return new DotProductDistance(Field.of(field), Constant.ofVector(other));
  }

  @BetaApi
  public static EuclideanDistance euclideanDistance(Expr expr, Expr other) {
    return new EuclideanDistance(expr, other);
  }

  @BetaApi
  public static EuclideanDistance euclideanDistance(Expr expr, double[] other) {
    return new EuclideanDistance(expr, Constant.ofVector(other));
  }

  @BetaApi
  public static EuclideanDistance euclideanDistance(String field, Expr other) {
    return new EuclideanDistance(Field.of(field), other);
  }

  @BetaApi
  public static EuclideanDistance euclideanDistance(String field, double[] other) {
    return new EuclideanDistance(Field.of(field), Constant.ofVector(other));
  }

  @BetaApi
  public static ArrayElement arrayElement() {
    return new ArrayElement();
  }

  @BetaApi
  public static Function function(String name, List<Expr> params) {
    return new Function(name, params);
  }
}
