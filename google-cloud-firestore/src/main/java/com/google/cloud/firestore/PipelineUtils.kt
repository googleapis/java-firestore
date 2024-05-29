@file:JvmName("PipelineUtils")
package com.google.cloud.firestore

import com.google.cloud.firestore.Query.ComparisonFilterInternal
import com.google.cloud.firestore.Query.CompositeFilterInternal
import com.google.cloud.firestore.Query.FilterInternal
import com.google.cloud.firestore.Query.LimitType
import com.google.cloud.firestore.Query.UnaryFilterInternal
import com.google.cloud.firestore.pipeline.AggregatorTarget
import com.google.cloud.firestore.pipeline.Constant
import com.google.cloud.firestore.pipeline.Field
import com.google.cloud.firestore.pipeline.Function
import com.google.cloud.firestore.pipeline.Function.Companion.countAll
import com.google.cloud.firestore.pipeline.Function.Companion.not
import com.google.firestore.v1.Cursor
import com.google.firestore.v1.StructuredQuery

internal fun toPipelineFilterCondition(f: FilterInternal): Function.FilterCondition {
  return when (f) {
    is ComparisonFilterInternal -> {
      when (f.operator) {
        StructuredQuery.FieldFilter.Operator.OPERATOR_UNSPECIFIED -> {
          TODO()
        }
        StructuredQuery.FieldFilter.Operator.LESS_THAN -> {
          Field.of(f.fieldReference.fieldPath).lessThan(f.value)
        }
        StructuredQuery.FieldFilter.Operator.LESS_THAN_OR_EQUAL -> {
          Field.of(f.fieldReference.fieldPath).lessThanOrEqual(f.value)
        }
        StructuredQuery.FieldFilter.Operator.GREATER_THAN -> {
          Field.of(f.fieldReference.fieldPath).greaterThan(f.value)
        }
        StructuredQuery.FieldFilter.Operator.GREATER_THAN_OR_EQUAL -> {
          Field.of(f.fieldReference.fieldPath).greaterThanOrEqual(f.value)
        }
        StructuredQuery.FieldFilter.Operator.EQUAL -> {
          Field.of(f.fieldReference.fieldPath).equal(f.value)
        }
        StructuredQuery.FieldFilter.Operator.NOT_EQUAL -> {
          not(Field.of(f.fieldReference.fieldPath).equal(f.value))
        }
        StructuredQuery.FieldFilter.Operator.ARRAY_CONTAINS -> {
          Field.of(f.fieldReference.fieldPath).arrayContains(f.value)
        }
        StructuredQuery.FieldFilter.Operator.IN -> {
          Function.In(
            Field.of(f.fieldReference.fieldPath),
            f.value?.arrayValue?.valuesList?.map { Constant.of(it) } ?: emptyList<Constant>(),
          )
        }
        StructuredQuery.FieldFilter.Operator.ARRAY_CONTAINS_ANY -> {
          Function.ArrayContainsAny(
            Field.of(f.fieldReference.fieldPath),
            f.value?.arrayValue?.valuesList?.map { Constant.of(it) } ?: emptyList<Constant>(),
          )
        }
        StructuredQuery.FieldFilter.Operator.NOT_IN -> {
          not(
            Function.In(
              Field.of(f.fieldReference.fieldPath),
              f.value?.arrayValue?.valuesList?.map { Constant.of(it) } ?: emptyList<Constant>(),
            )
          )
        }
        StructuredQuery.FieldFilter.Operator.UNRECOGNIZED -> {
          TODO()
        }
      }
    }
    is CompositeFilterInternal -> {
      when (f.operator) {
        StructuredQuery.CompositeFilter.Operator.OPERATOR_UNSPECIFIED -> {
          TODO()
        }
        StructuredQuery.CompositeFilter.Operator.AND -> {
          Function.And(f.filters.map { toPipelineFilterCondition(it) })
        }
        StructuredQuery.CompositeFilter.Operator.OR -> {
          Function.Or(f.filters.map { toPipelineFilterCondition(it) })
        }
        StructuredQuery.CompositeFilter.Operator.UNRECOGNIZED -> {
          TODO()
        }
      }
    }
    is UnaryFilterInternal -> {
      when (f.operator) {
        StructuredQuery.UnaryFilter.Operator.IS_NAN -> Field.of(f.fieldReference.fieldPath).isNaN()
        StructuredQuery.UnaryFilter.Operator.IS_NULL ->
          Field.of(f.fieldReference.fieldPath).isNull()
        StructuredQuery.UnaryFilter.Operator.IS_NOT_NAN ->
          not(Field.of(f.fieldReference.fieldPath).isNaN())
        StructuredQuery.UnaryFilter.Operator.IS_NOT_NULL ->
          not(Field.of(f.fieldReference.fieldPath).isNull())
        StructuredQuery.UnaryFilter.Operator.OPERATOR_UNSPECIFIED -> TODO()
        StructuredQuery.UnaryFilter.Operator.UNRECOGNIZED -> TODO()
      }
    }
    else -> {
      TODO()
    }
  }
}

internal fun toPaginatedPipeline(
  pipeline: Pipeline,
  start: Cursor?,
  end: Cursor?,
  limit: Int?,
  limitType: LimitType?,
  offset: Int?,
): Pipeline {
  var paginate: PaginatingPipeline = pipeline.paginate(limit ?: Int.MAX_VALUE)

  start?.let { paginate = setStartCursor(paginate, it) }
  end?.let { paginate = setEndCursor(paginate, it) }
  offset?.let { paginate = paginate.offset(it) }

  return limitType?.let {
    when (it) {
      LimitType.First -> paginate.firstPage()
      LimitType.Last -> paginate.lastPage()
    }
  } ?: paginate.firstPage()
}

internal fun toPipelineAggregatorTarget(f: AggregateField): AggregatorTarget {
  return when (f.operator) {
    "sum" -> {
      Field.of(f.getFieldPath()).sum().toField(f.alias)
    }
    "count" -> {
      countAll().toField(f.alias)
    }
    "avg" -> {
      Field.of(f.getFieldPath()).avg().toField(f.alias)
    }
    else -> TODO()
  }
}
