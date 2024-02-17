package com.google.cloud.firestore.pipeline

import com.google.firestore.v1.Value

interface Projectable


sealed interface Expr {
  data class Constant(val value: Value): Expr, Projectable {}
  data class FieldReference(val field: String): Expr {}

  data class ListOfExprs(val exprs: List<Expr>): Expr {}

  data class ExprAsAlias(val current: Expr, val alias: String): Expr, Projectable {}

  sealed class Function(val name: String, val params: Map<String, Expr>?) : Expr {
    interface ProducingBoolean
    data class Equal(val left: Expr, val right: Expr) : Function("equal", mapOf("left" to left, "right" to right)), ProducingBoolean
    data class NotEqual(val left: Expr, val right: Expr) : Function("not_equal", mapOf("left" to left, "right" to right)), ProducingBoolean
    data class GreaterThan(val left: Expr, val right: Expr) : Function("greater_than", mapOf("left" to left, "right" to right)), ProducingBoolean
    data class GreaterThanOrEqual(val left: Expr, val right: Expr) : Function("greater_than_equal", mapOf("left" to left, "right" to right)), ProducingBoolean
    data class In(val left: Expr, val others: List<Expr>) : Function("in", mapOf("left" to left, "others" to ListOfExprs(others))), ProducingBoolean // For 'in'
    data class LessThan(val left: Expr, val right: Expr) : Function("less_than", mapOf("left" to left, "right" to right)), ProducingBoolean
    data class LessThanOrEqual(val left: Expr, val right: Expr) : Function("less_than_equal", mapOf("left" to left, "right" to right)), ProducingBoolean
    data class NotIn(val left: Expr, val others: List<Expr>) : Function("not_in", mapOf("left" to left, "others" to ListOfExprs(others))), ProducingBoolean // For 'not in'
    data class And(val conditions: List<Expr>) : Function("and", mapOf("conditions" to ListOfExprs(conditions))), ProducingBoolean
    data class Or(val conditions: List<Expr>) : Function("or", mapOf("conditions" to ListOfExprs(conditions))), ProducingBoolean
    data class Not(val condition: Expr) : Function("not", mapOf("condition" to condition)), ProducingBoolean
    data class Exists(val current: FieldReference) : Function("exists", mapOf("current" to current)), ProducingBoolean

    data class MapGet(val map: Expr, val key: String) : Function("map_get", mapOf("map" to map, "key" to Constant(Value.getDefaultInstance().toBuilder().setStringValue(key).build())))

    data class ArrayContains(val array: Expr, val element: Expr) : Function("array_contains", mapOf("array" to array, "element" to element)), ProducingBoolean
    data class ArrayContainsAny(val array: Expr, val elements: List<Expr>) : Function("array_contains_any", mapOf("array" to array, "elements" to ListOfExprs(elements))), ProducingBoolean
    data class IsNaN(val value: Expr) : Function("is_nan", mapOf("value" to value)), ProducingBoolean
    data class IsNull(val value: Expr) : Function("is_null", mapOf("value" to value)), ProducingBoolean

    data class Sum(val value: Expr) : Function("sum", mapOf("value" to value))
    data class Avg(val value: Expr) : Function("avg", mapOf("value" to value))
    data class Count(val value: Expr) : Function("count", mapOf("value" to value))

    data class CosineDistance(val vector1: Expr, val vector2: Expr) : Function("cosine_distance", mapOf("vector1" to vector1, "vector2" to vector2))
    data class EuclideanDistance(val vector1: Expr, val vector2: Expr) : Function("euclidean_distance", mapOf("vector1" to vector1, "vector2" to vector2))
    data class HasAncestor(val child: Expr, val ancestor: Expr): Function("has_ancestor", mapOf("child" to child, "ancestor" to ancestor)), ProducingBoolean
    data class Raw(val n: String, val ps: Map<String, Expr>?): Function(n, ps)
  }

  // Infix functions returning Function subclasses
  infix fun equal(other: Expr): Function = Function.Equal(this, other)
  infix fun notEqual(other: Expr): Function = Function.NotEqual(this, other)
  infix fun greaterThan(other: Expr): Function = Function.GreaterThan(this, other)
  infix fun greaterThanOrEqual(other: Expr): Function = Function.GreaterThanOrEqual(this, other)
  fun inAny(left: Expr, vararg other: Expr): Function = Function.In(left, other.toList())
  infix fun lessThan(other: Expr): Function = Function.LessThan(this, other)
  infix fun lessThanOrEqual(other: Expr): Function = Function.LessThanOrEqual(this, other)
  fun notInAny(left: Expr, vararg other: Expr): Function = Function.NotIn(left, other.toList())
  infix fun and(other: Expr): Function = Function.And(listOf(this, other))
  fun and(vararg other: Expr): Function = Function.And(listOf(this) + other.toList())
  infix fun or(other: Expr): Function = Function.Or(listOf(this, other))
  fun or(vararg other: Expr): Function = Function.Or(listOf(this) + other.toList())
  fun exists(): Function = Function.Exists(this as FieldReference)

  infix fun mapGet(key: String): Function = Function.MapGet(this, key)
  infix fun arrayContains(element: Expr): Function = Function.ArrayContains(this, element)
  fun arrayContainsAny(vararg elements: Expr): Function = Function.ArrayContainsAny(this, elements.toList())
  fun isNaN(): Function = Function.IsNaN(this)
  fun isNull(): Function = Function.IsNull(this)
  infix fun not(other: Expr): Function = Function.Not(this) // Likely use 'this' if 'not' is unary
  fun sum(): Function = Function.Sum(this)
  fun avg(): Function = Function.Avg(this)
  fun count(): Function = Function.Count(this)
  infix fun cosineDistance(other: Expr): Function = Function.CosineDistance(this, other)
  infix fun euclideanDistance(other: Expr): Function = Function.EuclideanDistance(this, other)
  infix fun hasAncestor(ancestor: Expr): Function = Function.HasAncestor(this, ancestor)

  fun function(name: String, params: Map<String, Expr>?): Function = Function.Raw(name, params)

  fun withAlias(alias: String): ExprAsAlias {
    return ExprAsAlias(this, alias)
  }
}
