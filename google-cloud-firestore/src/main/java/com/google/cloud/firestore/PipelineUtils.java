package com.google.cloud.firestore;

import static com.google.cloud.firestore.pipeline.expressions.Function.and;
import static com.google.cloud.firestore.pipeline.expressions.Function.arrayContainsAny;
import static com.google.cloud.firestore.pipeline.expressions.Function.countAll;
import static com.google.cloud.firestore.pipeline.expressions.Function.inAny;
import static com.google.cloud.firestore.pipeline.expressions.Function.not;
import static com.google.cloud.firestore.pipeline.expressions.Function.or;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.Query.ComparisonFilterInternal;
import com.google.cloud.firestore.Query.CompositeFilterInternal;
import com.google.cloud.firestore.Query.FilterInternal;
import com.google.cloud.firestore.Query.LimitType;
import com.google.cloud.firestore.Query.UnaryFilterInternal;
import com.google.cloud.firestore.pipeline.PaginatingPipeline;
import com.google.cloud.firestore.pipeline.expressions.AggregatorTarget;
import com.google.cloud.firestore.pipeline.expressions.Constant;
import com.google.cloud.firestore.pipeline.expressions.Field;
import com.google.cloud.firestore.pipeline.expressions.FilterCondition;
import com.google.firestore.v1.Cursor;
import com.google.firestore.v1.Value;
import java.util.List;
import java.util.stream.Collectors;

@InternalApi
public class PipelineUtils {
  @InternalApi
  public static Value encodeValue(Object value) {
    return UserDataConverter.encodeValue(FieldPath.empty(), value, UserDataConverter.ARGUMENT);
  }

  @InternalApi
  static FilterCondition toPipelineFilterCondition(FilterInternal f) {
    if (f instanceof ComparisonFilterInternal) {
      ComparisonFilterInternal comparisonFilter = (ComparisonFilterInternal) f;
      String fieldPath = comparisonFilter.fieldReference.getFieldPath();
      Value value = comparisonFilter.value;
      switch (comparisonFilter.operator) {
        case LESS_THAN:
          return Field.of(fieldPath).lessThan(value);
        case LESS_THAN_OR_EQUAL:
          return Field.of(fieldPath).lessThanOrEqual(value);
        case GREATER_THAN:
          return Field.of(fieldPath).greaterThan(value);
        case GREATER_THAN_OR_EQUAL:
          return Field.of(fieldPath).greaterThanOrEqual(value);
        case EQUAL:
          return Field.of(fieldPath).equal(value);
        case NOT_EQUAL:
          return not(Field.of(fieldPath).equal(value));
        case ARRAY_CONTAINS:
          return Field.of(fieldPath).arrayContains(value);
        case IN:
          List<Value> valuesList = value.getArrayValue().getValuesList();
          return inAny(
              Field.of(fieldPath),
              valuesList.stream().map(Constant::of).collect(Collectors.toList()));
        case ARRAY_CONTAINS_ANY:
          List<Value> valuesListAny = value.getArrayValue().getValuesList();
          return arrayContainsAny(
              Field.of(fieldPath),
              valuesListAny.stream().map(Constant::of).collect(Collectors.toList()));
        case NOT_IN:
          List<Value> notInValues = value.getArrayValue().getValuesList();
          return not(
              inAny(
                  Field.of(fieldPath),
                  notInValues.stream().map(Constant::of).collect(Collectors.toList())));
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
      String fieldPath = unaryFilter.fieldReference.getFieldPath();
      switch (unaryFilter.getOperator()) {
        case IS_NAN:
          return Field.of(fieldPath).isNaN();
        case IS_NULL:
          return Field.of(fieldPath).isNull();
        case IS_NOT_NAN:
          return not(Field.of(fieldPath).isNaN());
        case IS_NOT_NULL:
          return not(Field.of(fieldPath).isNull());
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

    // Handle null limit, setting a default maximum
    int effectiveLimit = (limit != null) ? limit : Integer.MAX_VALUE;

    PaginatingPipeline paginate = pipeline.paginate(effectiveLimit);

    // Apply start and end cursors if present
    if (start != null) {
      paginate = paginate.withStartCursor(start);
    }
    if (end != null) {
      paginate = paginate.withEndCursor(end);
    }
    if (offset != null) {
      paginate = paginate.offset(offset);
    }

    // Handle limitType, defaulting to firstPage
    if (limitType != null) {
      switch (limitType) {
        case First:
          return paginate.firstPage();
        case Last:
          return paginate.lastPage();
        default:
          // Handle other LimitType cases as needed, or throw an exception
          throw new IllegalArgumentException("Unsupported limit type: " + limitType);
      }
    } else {
      return paginate.firstPage();
    }
  }

  @InternalApi
  static AggregatorTarget toPipelineAggregatorTarget(AggregateField f) {
    String operator = f.getOperator();
    String fieldPath = f.getFieldPath(); // Assuming you have a method to get FieldPath

    switch (operator) {
      case "sum":
        return Field.of(fieldPath)
            .sum()
            .toField(f.getAlias()); // Note: 'toField' is assumed to be a method in your context

      case "count":
        return countAll().toField(f.getAlias());
      case "avg":
        return Field.of(fieldPath).avg().toField(f.getAlias());
      default:
        // Handle the 'else' case appropriately in your Java code
        throw new IllegalArgumentException("Unsupported operator: " + operator);
    }
  }
}
