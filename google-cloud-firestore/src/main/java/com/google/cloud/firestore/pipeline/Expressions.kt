package com.google.cloud.firestore.pipeline

import com.google.cloud.firestore.Pipeline
import com.google.firestore.v1.Value

interface Projectable

// Convenient class for internal usage
internal data class ListOfExprs(val conditions: List<Expr>) : Expr
internal data class ListOfConditions(val conditions: List<Expr.Function.FilterCondition>) : Expr,
                                                                                            Expr.Function.FilterCondition

data class Fields(val fs: List<Expr.Field>) {
  companion object {
    @JvmStatic
    fun of(vararg f: String): Fields {
      return Fields(f.map(Expr.Field.Companion::of))
    }
  }
}

sealed interface Expr {
  data class Constant internal constructor(val value: Any) : Expr {
    companion object {
      @JvmStatic
      fun of(value: Any): Constant {
        return Constant(value)
      }
    }
  }

  data class Field(val field: String, var pipeline: Pipeline? = null) : Expr, Projectable {
    companion object {
      @JvmStatic
      fun of(path: String): Field {
        return Field(path)
      }

      @JvmStatic
      fun ofAll(): AllFields {
        return AllFields()
      }
    }
  }

  data class AllFields(var pipeline: Pipeline? = null) : Expr, Projectable {

    fun withPrefix(prefix: String): AllFields {
      return this
    }
  }


  data class ExprAsAlias(val current: Expr, val alias: String) : Expr, Projectable

  data class AggregatorTarget(val current: Function.Accumulator, val target: String) : Expr,
                                                                                       Function.Accumulator

  sealed class Function(val name: String, val params: Map<String, Expr>?) : Expr {
    interface FilterCondition

    interface Accumulator {
      fun toField(target: String) = AggregatorTarget(this, target)
    }

    data class Equal internal constructor(val left: Expr, val right: Expr) :
      Function("equal", mapOf("left" to left, "right" to right)), FilterCondition

    data class NotEqual(val left: Expr, val right: Expr) :
      Function("not_equal", mapOf("left" to left, "right" to right)), FilterCondition

    data class GreaterThan(val left: Expr, val right: Expr) :
      Function("greater_than", mapOf("left" to left, "right" to right)), FilterCondition

    data class GreaterThanOrEqual(val left: Expr, val right: Expr) :
      Function("greater_than_equal", mapOf("left" to left, "right" to right)), FilterCondition

    data class In(val left: Expr, val others: List<Expr>) :
      Function("in", mapOf("left" to left, "others" to ListOfExprs(others))),
      FilterCondition // For 'in'

    data class LessThan(val left: Expr, val right: Expr) :
      Function("less_than", mapOf("left" to left, "right" to right)), FilterCondition

    data class LessThanOrEqual(val left: Expr, val right: Expr) :
      Function("less_than_equal", mapOf("left" to left, "right" to right)), FilterCondition

    data class NotIn(val left: Expr, val others: List<Expr>) :
      Function("not_in", mapOf("left" to left, "others" to ListOfExprs(others))),
      FilterCondition // For 'not in'

    data class And(val conditions: List<FilterCondition>) :
      Function("and", mapOf("conditions" to ListOfConditions(conditions))), FilterCondition

    data class Or(val conditions: List<FilterCondition>) :
      Function("or", mapOf("conditions" to ListOfConditions(conditions))), FilterCondition

    data class Not(val condition: Expr) : Function("not", mapOf("condition" to condition)),
                                          FilterCondition

    data class Exists(val current: Field) : Function("exists", mapOf("current" to current)),
                                            FilterCondition

    data class MapGet(val map: Expr, val key: String) : Function(
      "map_get",
      mapOf(
        "map" to map,
        "key" to Constant(Value.getDefaultInstance().toBuilder().setStringValue(key).build())
      )
    )

    data class ArrayContains(val array: Expr, val element: Expr) :
      Function("array_contains", mapOf("array" to array, "element" to element)), FilterCondition

    data class ArrayContainsAny(val array: Expr, val elements: List<Expr>) :
      Function("array_contains_any", mapOf("array" to array, "elements" to ListOfExprs(elements))),
      FilterCondition

    data class IsNaN(val value: Expr) : Function("is_nan", mapOf("value" to value)), FilterCondition
    data class IsNull(val value: Expr) : Function("is_null", mapOf("value" to value)),
                                         FilterCondition

    data class Sum(val value: Expr) : Function("sum", mapOf("value" to value)), Accumulator
    data class Avg(val value: Expr) : Function("avg", mapOf("value" to value)), Accumulator
    data class Count(val value: Expr) : Function("count", mapOf("value" to value)), Accumulator

    data class CosineDistance(val vector1: Expr, val vector2: Expr) :
      Function("cosine_distance", mapOf("vector1" to vector1, "vector2" to vector2))

    data class EuclideanDistance(val vector1: Expr, val vector2: Expr) :
      Function("euclidean_distance", mapOf("vector1" to vector1, "vector2" to vector2))

    data class HasAncestor(val child: Expr, val ancestor: Expr) :
      Function("has_ancestor", mapOf("child" to child, "ancestor" to ancestor)), FilterCondition

    data class Generic(val n: String, val ps: Map<String, Expr>?) : Function(n, ps)
  }

  // Infix functions returning Function subclasses
  infix fun equal(other: Expr) = Function.Equal(this, other)
  infix fun notEqual(other: Expr) = Function.NotEqual(this, other)
  infix fun greaterThan(other: Expr) = Function.GreaterThan(this, other)
  infix fun greaterThanOrEqual(other: Expr) = Function.GreaterThanOrEqual(this, other)
  fun inAny(vararg other: Expr) = Function.In(this, other.toList())
  infix fun lessThan(other: Expr) = Function.LessThan(this, other)
  infix fun lessThanOrEqual(other: Expr) = Function.LessThanOrEqual(this, other)
  fun notInAny(left: Expr, vararg other: Expr) = Function.NotIn(left, other.toList())


  fun exists() = Function.Exists(this as Field)

  infix fun mapGet(key: String) = Function.MapGet(this, key)
  infix fun arrayContains(element: Expr) = Function.ArrayContains(this, element)
  fun arrayContainsAny(vararg elements: Expr) = Function.ArrayContainsAny(this, elements.toList())
  fun isNaN() = Function.IsNaN(this)
  fun isNull() = Function.IsNull(this)
  fun sum() = Function.Sum(this)
  fun avg() = Function.Avg(this)
  fun count() = Function.Count(this)
  infix fun cosineDistance(other: Expr) = Function.CosineDistance(this, other)
  infix fun cosineDistance(other: DoubleArray) = Function.CosineDistance(this, Constant.of(other))
  infix fun euclideanDistance(other: Expr) = Function.EuclideanDistance(this, other)
  infix fun euclideanDistance(other: DoubleArray) =
    Function.EuclideanDistance(this, Constant.of(other))

  infix fun hasAncestor(ancestor: Expr) = Function.HasAncestor(this, ancestor)

  fun asAlias(alias: String): ExprAsAlias = ExprAsAlias(this, alias)

  companion object {
    @JvmStatic
    fun equal(left: Expr, right: Expr) = Function.Equal(left, right)

    @JvmStatic
    fun notEqual(left: Expr, right: Expr) = Function.NotEqual(left, right)

    @JvmStatic
    fun greaterThan(left: Expr, right: Expr) = Function.GreaterThan(left, right)

    @JvmStatic
    fun greaterThanOrEqual(left: Expr, right: Expr) = Function.GreaterThanOrEqual(left, right)

    @JvmStatic
    fun inAny(left: Expr, vararg other: Expr) = Function.In(left, other.toList())

    @JvmStatic
    fun lessThan(left: Expr, right: Expr) = Function.LessThan(left, right)

    @JvmStatic
    fun lessThanOrEqual(left: Expr, right: Expr) = Function.LessThanOrEqual(left, right)

    @JvmStatic
    fun notInAny(left: Expr, vararg other: Expr) = Function.NotIn(left, other.toList())

    @JvmStatic
    fun and(left: Function.FilterCondition, right: Function.FilterCondition) =
      Function.And(listOf(left, right))

    @JvmStatic
    fun and(left: Function.FilterCondition, vararg other: Function.FilterCondition) =
      Function.And(listOf(left) + other.toList())

    @JvmStatic
    fun or(left: Function.FilterCondition, right: Function.FilterCondition) =
      Function.Or(listOf(left, right))

    @JvmStatic
    fun or(left: Function.FilterCondition, vararg other: Function.FilterCondition) =
      Function.Or(listOf(left) + other.toList())

    @JvmStatic
    fun exists(expr: Field) = Function.Exists(expr)

    @JvmStatic
    fun mapGet(expr: Expr, key: String) = Function.MapGet(expr, key)

    @JvmStatic
    fun arrayContains(expr: Expr, element: Expr) = Function.ArrayContains(expr, element)

    @JvmStatic
    fun arrayContainsAny(expr: Expr, vararg elements: Expr) =
      Function.ArrayContainsAny(expr, elements.toList())

    @JvmStatic
    fun isNaN(expr: Expr) = Function.IsNaN(expr)

    @JvmStatic
    fun isNull(expr: Expr) = Function.IsNull(expr)

    @JvmStatic
    fun not(expr: Expr) = Function.Not(expr)

    @JvmStatic
    fun sum(expr: Expr) = Function.Sum(expr)

    @JvmStatic
    fun avg(expr: Expr) = Function.Avg(expr)

    @JvmStatic
    fun count(expr: Expr) = Function.Count(expr)

    @JvmStatic
    fun cosineDistance(expr: Expr, other: Expr) = Function.CosineDistance(expr, other)

    @JvmStatic
    fun cosineDistance(expr: Expr, other: DoubleArray) =
      Function.CosineDistance(expr, Constant.of(other))

    @JvmStatic
    fun euclideanDistance(expr: Expr, other: Expr) = Function.EuclideanDistance(expr, other)

    @JvmStatic
    fun euclideanDistance(expr: Expr, other: DoubleArray) =
      Function.EuclideanDistance(expr, Constant.of(other))

    @JvmStatic
    fun hasAncestor(expr: Expr, ancestor: Expr) = Function.HasAncestor(expr, ancestor)

    @JvmStatic
    fun asAlias(expr: Expr, alias: String): ExprAsAlias = ExprAsAlias(expr, alias)

    @JvmStatic
    fun function(name: String, params: Map<String, Expr>?) = Function.Generic(name, params)
  }

}
