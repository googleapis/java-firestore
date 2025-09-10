/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.BetaApi;
import com.google.common.collect.ImmutableList;
import com.google.firestore.v1.Value;
import java.util.stream.Collectors;

@BetaApi
public class AggregateFunction {
  private final String name;
  private final ImmutableList<Expression> params;

  private AggregateFunction(String name, Expression... params) {
    this.name = name;
    this.params = ImmutableList.copyOf(params);
  }

  private AggregateFunction(String name, String fieldName) {
    this(name, Expression.field(fieldName));
  }

  @BetaApi
  public static AggregateFunction generic(String name, Expression... expr) {
    return new AggregateFunction(name, expr);
  }

  @BetaApi
  public static AggregateFunction countAll() {
    return new AggregateFunction("count");
  }

  @BetaApi
  public static AggregateFunction count(String fieldName) {
    return new AggregateFunction("count", fieldName);
  }

  @BetaApi
  public static AggregateFunction count(Expression expression) {
    return new AggregateFunction("count", expression);
  }

  @BetaApi
  public static AggregateFunction countDistinct(String fieldName) {
    return new AggregateFunction("count_distinct", fieldName);
  }

  @BetaApi
  public static AggregateFunction countDistinct(Expression expression) {
    return new AggregateFunction("count_distinct", expression);
  }

  @BetaApi
  public static AggregateFunction countIf(BooleanExpression condition) {
    return new AggregateFunction("count_if", condition);
  }

  @BetaApi
  public static AggregateFunction sum(String fieldName) {
    return new AggregateFunction("sum", fieldName);
  }

  @BetaApi
  public static AggregateFunction sum(Expression expression) {
    return new AggregateFunction("sum", expression);
  }

  @BetaApi
  public static AggregateFunction average(String fieldName) {
    return new AggregateFunction("average", fieldName);
  }

  @BetaApi
  public static AggregateFunction average(Expression expression) {
    return new AggregateFunction("average", expression);
  }

  @BetaApi
  public static AggregateFunction minimum(String fieldName) {
    return new AggregateFunction("minimum", fieldName);
  }

  @BetaApi
  public static AggregateFunction minimum(Expression expression) {
    return new AggregateFunction("minimum", expression);
  }

  @BetaApi
  public static AggregateFunction maximum(String fieldName) {
    return new AggregateFunction("maximum", fieldName);
  }

  @BetaApi
  public static AggregateFunction maximum(Expression expression) {
    return new AggregateFunction("maximum", expression);
  }

  @BetaApi
  public AliasedAggregate as(String alias) {
    return new AliasedAggregate(alias, this);
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
}
