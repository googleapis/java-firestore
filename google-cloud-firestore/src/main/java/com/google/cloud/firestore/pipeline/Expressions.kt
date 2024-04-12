package com.google.cloud.firestore.pipeline

import com.google.cloud.firestore.Pipeline
import com.google.cloud.firestore.pipeline.Function.ArrayContains
import com.google.cloud.firestore.pipeline.Function.ArrayContainsAny
import com.google.cloud.firestore.pipeline.Function.Avg
import com.google.cloud.firestore.pipeline.Function.CosineDistance
import com.google.cloud.firestore.pipeline.Function.Count
import com.google.cloud.firestore.pipeline.Function.DotProductDistance
import com.google.cloud.firestore.pipeline.Function.Equal
import com.google.cloud.firestore.pipeline.Function.EuclideanDistance
import com.google.cloud.firestore.pipeline.Function.GreaterThan
import com.google.cloud.firestore.pipeline.Function.GreaterThanOrEqual
import com.google.cloud.firestore.pipeline.Function.In
import com.google.cloud.firestore.pipeline.Function.IsNaN
import com.google.cloud.firestore.pipeline.Function.IsNull
import com.google.cloud.firestore.pipeline.Function.LessThan
import com.google.cloud.firestore.pipeline.Function.LessThanOrEqual
import com.google.cloud.firestore.pipeline.Function.MapGet
import com.google.cloud.firestore.pipeline.Function.NotEqual
import com.google.cloud.firestore.pipeline.Function.NotIn
import com.google.cloud.firestore.pipeline.Function.Sum
import com.google.firestore.v1.Value

internal interface ToProto {
  fun toProto(): Value
}

internal fun exprToValue(expr: Expr): Value{
  return when(expr) {
    is Constant -> expr.toProto()
    is Field -> expr.toProto()
    is Function -> expr.toProto()
    // is ExprAsAlias ->
    else -> {
      TODO()
    }
  }
}

sealed interface Projectable

interface Expr {
  // Infix functions returning Function subclasses
  infix fun equal(other: Expr) = Equal(this, other)
  infix fun equal(other: Number) = Equal(this, Constant.of(other))
  infix fun equal(other: String) = Equal(this, Constant.of(other))
  infix fun equal(other: Any) = Equal(this, Constant.of(other))
  infix fun notEqual(other: Expr) = NotEqual(this, other)
  infix fun notEqual(other: Number) = NotEqual(this, Constant.of(other))
  infix fun notEqual(other: String) = NotEqual(this, Constant.of(other))
  infix fun notEqual(other: Any) = NotEqual(this, Constant.of(other))
  infix fun greaterThan(other: Expr) = GreaterThan(this, other)
  infix fun greaterThan(other: Number) = GreaterThan(this, Constant.of(other))
  infix fun greaterThan(other: String) = GreaterThan(this, Constant.of(other))
  infix fun greaterThan(other: Any) = GreaterThan(this, Constant.of(other))
  infix fun greaterThanOrEqual(other: Expr) = GreaterThanOrEqual(this, other)
  infix fun greaterThanOrEqual(other: Number) = GreaterThanOrEqual(this, Constant.of(other))
  infix fun greaterThanOrEqual(other: String) = GreaterThanOrEqual(this, Constant.of(other))
  infix fun greaterThanOrEqual(other: Any) = GreaterThanOrEqual(this, Constant.of(other))
  infix fun lessThan(other: Expr) = LessThan(this, other)
  infix fun lessThan(other: Number) = LessThan(this, Constant.of(other))
  infix fun lessThan(other: String) = LessThan(this, Constant.of(other))
  infix fun lessThan(other: Any) = LessThan(this, Constant.of(other))
  infix fun lessThanOrEqual(other: Expr) = LessThanOrEqual(this, other)
  infix fun lessThanOrEqual(other: Number) = LessThanOrEqual(this, Constant.of(other))
  infix fun lessThanOrEqual(other: String) = LessThanOrEqual(this, Constant.of(other))
  infix fun lessThanOrEqual(other: Any) = LessThanOrEqual(this, Constant.of(other))
  fun inAny(vararg other: Expr) = In(this, other.toList())
  fun notInAny(vararg other: Expr) = NotIn(this, other.toList())

  infix fun mapGet(key: String) = MapGet(this, key)

  infix fun arrayContains(element: Expr) = ArrayContains(this, element)
  infix fun arrayContains(element: Number) = ArrayContains(this, Constant.of(element))
  infix fun arrayContains(element: String) = ArrayContains(this, Constant.of(element))
  infix fun arrayContains(element: Any) = ArrayContains(this, Constant.of(element))
  fun arrayContainsAny(vararg elements: Expr) = ArrayContainsAny(this, elements.toList())
  fun isNaN() = IsNaN(this)
  fun isNull() = IsNull(this)
  fun sum() = Sum(this, false)
  fun avg() = Avg(this, false)
  fun count() = Count(this, false)

  infix fun cosineDistance(other: Expr) = CosineDistance(this, other)
  infix fun cosineDistance(other: DoubleArray) =
    CosineDistance(this, Constant.ofVector(other))

  infix fun euclideanDistance(other: Expr) = EuclideanDistance(this, other)
  infix fun euclideanDistance(other: DoubleArray) =
    EuclideanDistance(this, Constant.ofVector(other))

  infix fun dotProductDistance(other: Expr) = DotProductDistance(this, other)
  infix fun dotProductDistance(other: DoubleArray) =
    DotProductDistance(this, Constant.ofVector(other))

  fun asAlias(alias: String): Projectable = ExprAsAlias(this, alias)
}

// Convenient class for internal usage
internal data class ListOfExprs(val conditions: List<Expr>) : Expr
internal data class ListOfConditions(val conditions: List<Function.FilterCondition>) : Expr,
                                                                                       Function.FilterCondition
data class Constant internal constructor(val value: Any) : Expr, ToProto {
  companion object {
    @JvmStatic
    fun of(value: String): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun of(value: Number): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun of(value: Any): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun <T> ofArray(value: Iterable<T>): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun <T> ofMap(value: Map<String, T>): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun ofVector(value: DoubleArray): Constant {
      return Constant(value)
    }
  }

  override fun toProto(): Value {
    return Value.newBuilder().build()
  }
}

data class Field internal constructor(val field: String, var pipeline: Pipeline? = null) : Expr,
                                                                                           Projectable,
                                                                                           ToProto {
  companion object {
    const val DOCUMENT_ID: String = "__path__"

    @JvmStatic
    fun of(path: String): Field {
      return Field(path)
    }
  }

  fun exists() = Function.Exists(this)

  override fun toProto(): Value {
    return Value.newBuilder().setFieldReferenceValue(field).build()
  }
}

data class Fields internal constructor(val fs: List<Field>? = null) : Expr, Projectable {
  companion object {
    @JvmStatic
    fun of(f1: String, vararg f: String): Fields {
      return Fields(listOf(Field.of(f1)) + f.map(Field.Companion::of))
    }

    @JvmStatic
    fun ofAll(): Fields {
      return Fields(null)
    }
  }
}

internal data class ExprAsAlias(val current: Expr, val alias: String) : Expr, Projectable

data class AggregatorTarget internal constructor(
  val current: Function.Accumulator, val target: String,
  override var distinct: Boolean
) : Expr,
    Projectable,
    Function.Accumulator

sealed class Function(val name: String, val params: List<Expr>): Expr, ToProto {
  interface FilterCondition

  interface Accumulator: Expr {
    var distinct: Boolean

    fun distinct(on: Boolean): Accumulator {
      this.distinct = on
      return this
    }

    fun toField(target: String) = AggregatorTarget(this, target, this.distinct)
  }

  override fun toProto(): Value {
    return Value.newBuilder().setFunctionValue(com.google.firestore.v1.Function.newBuilder()
                                          .setName(name)
                                          .addAllArgs(params.map { exprToValue(it) })).build()
  }

  data class Equal internal constructor(val left: Expr, val right: Expr) :
    Function("equal", listOf(left, right)), FilterCondition

  data class NotEqual(val left: Expr, val right: Expr) :
    Function("not_equal", listOf(left, right)), FilterCondition

  data class GreaterThan(val left: Expr, val right: Expr) :
    Function("greater_than", listOf(left, right)), FilterCondition

  data class GreaterThanOrEqual(val left: Expr, val right: Expr) :
    Function("greater_than_equal", listOf(left, right)), FilterCondition

  data class In(val left: Expr, val others: List<Expr>) :
    Function("in", listOf(left, ListOfExprs(others))),
    FilterCondition // For 'in'

  data class LessThan(val left: Expr, val right: Expr) :
    Function("less_than", listOf(left, right)), FilterCondition

  data class LessThanOrEqual(val left: Expr, val right: Expr) :
    Function("less_than_equal", listOf(left, right)), FilterCondition

  data class NotIn(val left: Expr, val others: List<Expr>) :
    Function("not_in", listOf(left, ListOfExprs(others))),
    FilterCondition // For 'not in'

  data class And(val conditions: List<FilterCondition>) :
    Function("and", listOf(ListOfConditions(conditions))), FilterCondition

  data class Or(val conditions: List<FilterCondition>) :
    Function("or", listOf(ListOfConditions(conditions))), FilterCondition

  data class Not(val condition: Expr) : Function("not", listOf(condition)),
                                        FilterCondition

  data class Exists(val current: Field) : Function("exists", listOf(current)),
                                          FilterCondition

  data class MapGet(val map: Expr, val key: String) : Function(
    "map_get",
    listOf(
      map,
      Constant(Value.getDefaultInstance().toBuilder().setStringValue(key).build())
    )
  )

  data class ArrayContains(val array: Expr, val element: Expr) :
    Function("array_contains", listOf(array, element)), FilterCondition

  data class ArrayContainsAny(val array: Expr, val elements: List<Expr>) :
    Function("array_contains_any", listOf(array, ListOfExprs(elements))),
    FilterCondition

  data class IsNaN(val value: Expr) : Function("is_nan", listOf(value)), FilterCondition
  data class IsNull(val value: Expr) : Function("is_null", listOf(value)),
                                       FilterCondition

  data class Sum(val value: Expr, override var distinct: Boolean) :
    Function("sum", listOf(value)), Accumulator

  data class Avg(val value: Expr, override var distinct: Boolean) :
    Function("avg", listOf(value)), Accumulator

  data class Count(val value: Expr, override var distinct: Boolean) :
    Function("count", listOf(value)), Accumulator

  data class CosineDistance(val vector1: Expr, val vector2: Expr) :
    Function("cosine_distance", listOf(vector1, vector2))

  data class DotProductDistance(val vector1: Expr, val vector2: Expr) :
    Function("dot_product_distance", listOf(vector1, vector2))

  data class EuclideanDistance(val vector1: Expr, val vector2: Expr) :
    Function("euclidean_distance", listOf(vector1, vector2))

  data class Generic(val n: String, val ps: List<Expr>) : Function(n, ps)


  companion object {
    @JvmStatic
    fun equal(left: Expr, right: Expr) = Equal(left, right)

    @JvmStatic
    fun equal(left: Expr, right: String) = Equal(left, Constant.of(right))

    @JvmStatic
    fun equal(left: Expr, right: Number) = Equal(left, Constant.of(right))

    @JvmStatic
    fun equal(left: Expr, right: Any) = Equal(left, Constant.of(right))

    @JvmStatic
    fun notEqual(left: Expr, right: Expr) = NotEqual(left, right)

    @JvmStatic
    fun notEqual(left: Expr, right: String) = NotEqual(left, Constant.of(right))

    @JvmStatic
    fun notEqual(left: Expr, right: Number) = NotEqual(left, Constant.of(right))

    @JvmStatic
    fun notEqual(left: Expr, right: Any) = NotEqual(left, Constant.of(right))

    @JvmStatic
    fun greaterThan(left: Expr, right: Expr) = GreaterThan(left, right)

    @JvmStatic
    fun greaterThan(left: Expr, right: String) =
      GreaterThan(left, Constant.of(right))

    @JvmStatic
    fun greaterThan(left: Expr, right: Number) =
      GreaterThan(left, Constant.of(right))

    @JvmStatic
    fun greaterThan(left: Expr, right: Any) =
      GreaterThan(left, Constant.of(right))

    @JvmStatic
    fun greaterThanOrEqual(left: Expr, right: Expr) = GreaterThanOrEqual(left, right)

    @JvmStatic
    fun greaterThanOrEqual(left: Expr, right: String) = GreaterThanOrEqual(left, Constant.of(right))

    @JvmStatic
    fun greaterThanOrEqual(left: Expr, right: Number) = GreaterThanOrEqual(left, Constant.of(right))

    @JvmStatic
    fun greaterThanOrEqual(left: Expr, right: Any) = GreaterThanOrEqual(left, Constant.of(right))

    @JvmStatic
    fun lessThan(left: Expr, right: Expr) = LessThan(left, right)

    @JvmStatic
    fun lessThan(left: Expr, right: String) = LessThan(left, Constant.of(right))

    @JvmStatic
    fun lessThan(left: Expr, right: Number) = LessThan(left, Constant.of(right))

    @JvmStatic
    fun lessThan(left: Expr, right: Any) = LessThan(left, Constant.of(right))

    @JvmStatic
    fun lessThanOrEqual(left: Expr, right: Expr) = LessThanOrEqual(left, right)

    @JvmStatic
    fun lessThanOrEqual(left: Expr, right: String) = LessThanOrEqual(left, Constant.of(right))

    @JvmStatic
    fun lessThanOrEqual(left: Expr, right: Number) = LessThanOrEqual(left, Constant.of(right))

    @JvmStatic
    fun lessThanOrEqual(left: Expr, right: Any) = LessThanOrEqual(left, Constant.of(right))

    @JvmStatic
    fun inAny(left: Expr, values: List<Expr>) = In(left, values)

    @JvmStatic
    fun notInAny(left: Expr, values: List<Expr>) = NotIn(left, values)

    @JvmStatic
    fun and(left: FilterCondition, right: FilterCondition) =
      And(listOf(left, right))

    @JvmStatic
    fun and(left: FilterCondition, vararg other: FilterCondition) =
      And(listOf(left) + other.toList())

    @JvmStatic
    fun or(left: FilterCondition, right: FilterCondition) =
      Or(listOf(left, right))

    @JvmStatic
    fun or(left: FilterCondition, vararg other: FilterCondition) =
      Or(listOf(left) + other.toList())

    @JvmStatic
    fun exists(expr: Field) = Exists(expr)

    @JvmStatic
    fun mapGet(expr: Expr, key: String) = MapGet(expr, key)

    @JvmStatic
    fun arrayContains(expr: Expr, element: Expr) = ArrayContains(expr, element)

    @JvmStatic
    fun arrayContains(expr: Expr, element: Number) = ArrayContains(expr, Constant.of(element))

    @JvmStatic
    fun arrayContains(expr: Expr, element: String) = ArrayContains(expr, Constant.of(element))

    @JvmStatic
    fun arrayContains(expr: Expr, element: Any) = ArrayContains(expr, Constant.of(element))

    @JvmStatic
    fun arrayContainsAny(expr: Expr, vararg elements: Expr) =
      ArrayContainsAny(expr, elements.toList())

    @JvmStatic
    fun isNaN(expr: Expr) = IsNaN(expr)

    @JvmStatic
    fun isNull(expr: Expr) = IsNull(expr)

    @JvmStatic
    fun not(expr: Expr) = Not(expr)

    @JvmStatic
    fun sum(expr: Expr) = Sum(expr, false)

    @JvmStatic
    fun avg(expr: Expr) = Avg(expr, false)

    @JvmStatic
    fun count(expr: Expr) = Count(expr, false)

    @JvmStatic
    fun cosineDistance(expr: Expr, other: Expr) = CosineDistance(expr, other)

    @JvmStatic
    fun cosineDistance(expr: Expr, other: DoubleArray) =
      CosineDistance(expr, Constant.ofVector(other))

    @JvmStatic
    fun dotProductDistance(expr: Expr, other: Expr) = CosineDistance(expr, other)

    @JvmStatic
    fun dotProductDistance(expr: Expr, other: DoubleArray) =
      CosineDistance(expr, Constant.ofVector(other))

    @JvmStatic
    fun euclideanDistance(expr: Expr, other: Expr) = EuclideanDistance(expr, other)

    @JvmStatic
    fun euclideanDistance(expr: Expr, other: DoubleArray) =
      EuclideanDistance(expr, Constant.ofVector(other))

    @JvmStatic
    fun asAlias(expr: Expr, alias: String): Projectable = ExprAsAlias(expr, alias)

    @JvmStatic
    fun function(name: String, params: List<Expr>) = Generic(name, params)
  }
}

