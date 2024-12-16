/*
 * Copyright 2024 Google LLC
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

package com.google.cloud.firestore;

import static com.google.cloud.firestore.pipeline.expressions.Function.and;
import static com.google.cloud.firestore.pipeline.expressions.Function.arrayContainsAny;
import static com.google.cloud.firestore.pipeline.expressions.Function.countAll;
import static com.google.cloud.firestore.pipeline.expressions.Function.inAny;
import static com.google.cloud.firestore.pipeline.expressions.Function.not;
import static com.google.cloud.firestore.pipeline.expressions.Function.or;
import static com.google.cloud.firestore.pipeline.expressions.FunctionUtils.exprToValue;

import com.google.api.core.InternalApi;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Query.ComparisonFilterInternal;
import com.google.cloud.firestore.Query.CompositeFilterInternal;
import com.google.cloud.firestore.Query.FilterInternal;
import com.google.cloud.firestore.Query.LimitType;
import com.google.cloud.firestore.Query.UnaryFilterInternal;
import com.google.cloud.firestore.pipeline.expressions.Accumulator;
import com.google.cloud.firestore.pipeline.expressions.AccumulatorTarget;
import com.google.cloud.firestore.pipeline.expressions.Expr;
import com.google.cloud.firestore.pipeline.expressions.ExprWithAlias;
import com.google.cloud.firestore.pipeline.expressions.Field;
import com.google.cloud.firestore.pipeline.expressions.FilterCondition;
import com.google.cloud.firestore.pipeline.expressions.Selectable;
import com.google.common.collect.Lists;
import com.google.firestore.v1.Cursor;
import com.google.firestore.v1.MapValue;
import com.google.firestore.v1.Value;
import com.google.protobuf.NullValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@InternalApi
public class PipelineUtils {
  @InternalApi
  public static Value encodeValue(Object value) {
    return UserDataConverter.encodeValue(FieldPath.empty(), value, UserDataConverter.ARGUMENT);
  }

  @InternalApi
  public static Value encodeValue(Expr value) {
    return exprToValue(value);
  }

  @InternalApi
  public static Value encodeValue(String value) {
    return Value.newBuilder().setStringValue(value).build();
  }

  @InternalApi
  public static Value encodeValue(boolean value) {
    return Value.newBuilder().setBooleanValue(value).build();
  }

  @InternalApi
  public static Value encodeValue(long value) {
    return Value.newBuilder().setIntegerValue(value).build();
  }

  @InternalApi
  public static Value encodeValue(double value) {
    return Value.newBuilder().setDoubleValue(value).build();
  }

  @InternalApi
  public static Value encodeValue(Map<String, Value> options) {
    return Value.newBuilder().setMapValue(MapValue.newBuilder().putAllFields(options).build()).build();
  }

  @InternalApi
  static FilterCondition toPipelineFilterCondition(FilterInternal f) {
    if (f instanceof ComparisonFilterInternal) {
      ComparisonFilterInternal comparisonFilter = (ComparisonFilterInternal) f;
      Field field = Field.of(comparisonFilter.fieldReference.getFieldPath());
      Value value = comparisonFilter.value;
      switch (comparisonFilter.operator) {
        case LESS_THAN:
          return and(field.exists(), field.lt(value));
        case LESS_THAN_OR_EQUAL:
          return and(field.exists(), field.lte(value));
        case GREATER_THAN:
          return and(field.exists(), field.gt(value));
        case GREATER_THAN_OR_EQUAL:
          return and(field.exists(), field.gte(value));
        case EQUAL:
          return and(field.exists(), field.eq(value));
        case NOT_EQUAL:
          return and(field.exists(), not(field.eq(value)));
        case ARRAY_CONTAINS:
          return and(field.exists(), field.arrayContains(value));
        case IN:
          List<Value> valuesList = value.getArrayValue().getValuesList();
          return and(field.exists(), inAny(field, Lists.newArrayList(valuesList)));
        case ARRAY_CONTAINS_ANY:
          List<Value> valuesListAny = value.getArrayValue().getValuesList();
          return and(field.exists(), arrayContainsAny(field, Lists.newArrayList(valuesListAny)));
        case NOT_IN:
          List<Value> notInValues = value.getArrayValue().getValuesList();
          return and(field.exists(), not(inAny(field, Lists.newArrayList(notInValues))));
        default:
          // Handle OPERATOR_UNSPECIFIED and UNRECOGNIZED cases as needed
          throw new IllegalArgumentException("Unsupported operator: " + comparisonFilter.operator);
      }
    } else if (f instanceof CompositeFilterInternal) {
      CompositeFilterInternal compositeFilter = (CompositeFilterInternal) f;
      switch (compositeFilter.getOperator()) {
        case AND:
          List<FilterCondition> conditions =
              compositeFilter.getFilters().stream()
                  .map(PipelineUtils::toPipelineFilterCondition)
                  .collect(Collectors.toList());
          return and(
              conditions.get(0),
              conditions.subList(1, conditions.size()).toArray(new FilterCondition[0]));
        case OR:
          List<FilterCondition> orConditions =
              compositeFilter.getFilters().stream()
                  .map(PipelineUtils::toPipelineFilterCondition)
                  .collect(Collectors.toList());
          return or(
              orConditions.get(0),
              orConditions.subList(1, orConditions.size()).toArray(new FilterCondition[0]));
        default:
          // Handle OPERATOR_UNSPECIFIED and UNRECOGNIZED cases as needed
          throw new IllegalArgumentException(
              "Unsupported operator: " + compositeFilter.getOperator());
      }
    } else if (f instanceof UnaryFilterInternal) {
      UnaryFilterInternal unaryFilter = (UnaryFilterInternal) f;
      Field field = Field.of(unaryFilter.fieldReference.getFieldPath());
      switch (unaryFilter.getOperator()) {
        case IS_NAN:
          return and(field.exists(), field.isNaN());
        case IS_NULL:
          return and(field.exists(), field.eq(null));
        case IS_NOT_NAN:
          return and(field.exists(), not(field.isNaN()));
        case IS_NOT_NULL:
          return and(field.exists(), not(field.eq(null)));
        default:
          // Handle OPERATOR_UNSPECIFIED and UNRECOGNIZED cases as needed
          throw new IllegalArgumentException("Unsupported operator: " + unaryFilter.getOperator());
      }
    } else {
      // Handle other FilterInternal types as needed
      throw new IllegalArgumentException("Unsupported filter type: " + f.getClass().getName());
    }
  }

  @InternalApi
  static Pipeline toPaginatedPipeline(
      Pipeline pipeline,
      Cursor start,
      Cursor end,
      Integer limit,
      LimitType limitType,
      Integer offset) {
    throw new UnsupportedOperationException(
        "Converting to pagination pipeline is not support yet.");
  }

  @InternalApi
  static ExprWithAlias<Accumulator> toPipelineAggregatorTarget(AggregateField f) {
    String operator = f.getOperator();
    String fieldPath = f.getFieldPath();

    switch (operator) {
      case "sum":
        return Field.of(fieldPath).sum().as(f.getAlias());

      case "count":
        return countAll().as(f.getAlias());
      case "average":
        return Field.of(fieldPath).avg().as(f.getAlias());
      default:
        // Handle the 'else' case appropriately in your Java code
        throw new IllegalArgumentException("Unsupported operator: " + operator);
    }
  }

  @InternalApi
  public static Map<String, Expr> selectablesToMap(Selectable... selectables) {
    Map<String, Expr> projMap = new HashMap<>();
    for (Selectable proj : selectables) {
      if (proj instanceof Field) {
        Field fieldProj = (Field) proj;
        projMap.put(fieldProj.getPath().getEncodedPath(), fieldProj);
      } else if (proj instanceof AccumulatorTarget) {
        AccumulatorTarget aggregatorProj = (AccumulatorTarget) proj;
        projMap.put(aggregatorProj.getFieldName(), aggregatorProj.getAccumulator());
      } else if (proj instanceof ExprWithAlias) {
        ExprWithAlias exprWithAlias = (ExprWithAlias) proj;
        projMap.put(exprWithAlias.getAlias(), exprWithAlias.getExpr());
      }
    }
    return projMap;
  }

  @InternalApi
  public static Map<String, Expr> fieldNamesToMap(String... fields) {
    Map<String, Expr> projMap = new HashMap<>();
    for (String field : fields) {
      projMap.put(field, Field.of(field));
    }
    return projMap;
  }
}
