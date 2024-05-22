package com.google.cloud.firestore.pipeline

import com.google.cloud.Timestamp
import com.google.cloud.firestore.Blob
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.GeoPoint
import com.google.cloud.firestore.Pipeline
import com.google.cloud.firestore.encodeValue
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
import com.google.cloud.firestore.pipeline.Function.NotEqual
import com.google.cloud.firestore.pipeline.Function.Sum
import com.google.cloud.firestore.pipeline.Sort.Ordering
import com.google.cloud.firestore.pipeline.Sort.Ordering.Direction
import com.google.firestore.v1.ArrayValue
import com.google.firestore.v1.Value
import java.util.Date

internal fun exprToValue(expr: Expr): Value {
  return when (expr) {
    is Constant -> expr.toProto()
    is Field -> expr.toProto()
    is Function -> expr.toProto()
    is ListOfExprs -> {
      Value.newBuilder()
        .setArrayValue(
          ArrayValue.newBuilder().addAllValues(expr.conditions.map { exprToValue(it) })
        )
        .build()
    }
    else -> {
      TODO()
    }
  }
}

interface Selectable

internal class ExprWithAlias internal constructor(val alias: String, val expr: Expr) : Selectable

interface Expr {
  // Infix functions returning Function subclasses
  infix fun equal(other: Expr) = Equal(this, other)

  infix fun equal(other: Any) = Equal(this, Constant.of(other))

  infix fun notEqual(other: Expr) = NotEqual(this, other)

  infix fun notEqual(other: Any) = NotEqual(this, Constant.of(other))

  infix fun greaterThan(other: Expr) = GreaterThan(this, other)

  infix fun greaterThan(other: Any) = GreaterThan(this, Constant.of(other))

  infix fun greaterThanOrEqual(other: Expr) = GreaterThanOrEqual(this, other)

  infix fun greaterThanOrEqual(other: Any) = GreaterThanOrEqual(this, Constant.of(other))

  infix fun lessThan(other: Expr) = LessThan(this, other)

  infix fun lessThan(other: Any) = LessThan(this, Constant.of(other))

  infix fun lessThanOrEqual(other: Expr) = LessThanOrEqual(this, other)

  infix fun lessThanOrEqual(other: Any) = LessThanOrEqual(this, Constant.of(other))

  fun inAny(vararg other: Any) =
    In(
      this,
      other.toList().map {
        when (it) {
          is Expr -> it
          else -> Constant.of(it)
        }
      },
    )

  fun notInAny(vararg other: Any) =
    Function.Not(
      In(
        this,
        other.toList().map {
          when (it) {
            is Expr -> it
            else -> Constant.of(it)
          }
        },
      )
    )

  infix fun arrayContains(element: Expr) = ArrayContains(this, element)

  infix fun arrayContains(element: Any) = ArrayContains(this, Constant.of(element))

  fun arrayContainsAny(vararg elements: Expr) = ArrayContainsAny(this, elements.toList())

  fun arrayContainsAny(vararg elements: Any) =
    ArrayContainsAny(this, elements.toList().map { Constant.of(it) })

  fun isNaN() = IsNaN(this)

  fun isNull() = IsNull(this)

  fun sum() = Sum(this, false)

  fun avg() = Avg(this, false)

  fun count() = Count(this, false)

  fun min() = Function.Min(this, false)

  fun max() = Function.Max(this, false)

  infix fun cosineDistance(other: Expr) = CosineDistance(this, other)

  infix fun cosineDistance(other: DoubleArray) = CosineDistance(this, Constant.ofVector(other))

  infix fun euclideanDistance(other: Expr) = EuclideanDistance(this, other)

  infix fun euclideanDistance(other: DoubleArray) =
    EuclideanDistance(this, Constant.ofVector(other))

  infix fun dotProductDistance(other: Expr) = DotProductDistance(this, other)

  infix fun dotProductDistance(other: DoubleArray) =
    DotProductDistance(this, Constant.ofVector(other))

  fun ascending(): Ordering {
    return Ordering(this, Direction.ASCENDING)
  }

  fun descending(): Ordering {
    return Ordering(this, Direction.DESCENDING)
  }

  fun asAlias(alias: String): Selectable {
    return ExprWithAlias(alias, this)
  }
}

// Convenient class for internal usage
internal data class ListOfExprs(val conditions: List<Expr>) : Expr

data class Constant internal constructor(private val value: Any?) : Expr {
  companion object {
    @JvmStatic
    fun of(value: String?): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun of(value: Number?): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun of(value: Date?): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun of(value: Timestamp?): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun of(value: Boolean?): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun of(value: GeoPoint?): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun of(value: Blob?): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun of(value: DocumentReference?): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun of(value: Value?): Constant {
      return Constant(value)
    }

    @JvmStatic
    internal fun of(value: Any?): Constant {
      if (value == null) {
        return Constant(null)
      }

      return when (value) {
        is String -> of(value)
        is Number -> of(value)
        is Date -> of(value)
        is Timestamp -> of(value)
        is Boolean -> of(value)
        is GeoPoint -> of(value)
        is Blob -> of(value)
        is DocumentReference -> of(value)
        is Value -> of(value)
        else -> TODO("Unknown type: $value")
      }
    }

    @JvmStatic
    fun <T : Any?> ofArray(value: Iterable<T>): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun <T : Any?> ofArray(value: Array<T>): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun <T : Any?> ofMap(value: Map<String, T>): Constant {
      return Constant(value)
    }

    @JvmStatic
    fun ofVector(value: DoubleArray): Constant {
      // TODO: Vector is really a map, not a list
      return Constant(value.asList())
    }
  }

  internal fun toProto(): Value {
    return encodeValue(value)!!
  }
}

data class Field internal constructor(
  internal val field: String,
  private var pipeline: Pipeline? = null
) :
  Expr, Selectable {
  companion object {
    const val DOCUMENT_ID: String = "__path__"

    @JvmStatic
    fun of(path: String): Field {
      return Field(path)
    }

    @JvmStatic
    fun ofAll(): Field {
      return Field("")
    }
  }

  internal fun toProto(): Value {
    return Value.newBuilder().setFieldReferenceValue(field).build()
  }
}

data class Fields internal constructor(internal val fs: List<Field>? = null) : Expr, Selectable {
  companion object {
    @JvmStatic
    fun of(f1: String, vararg f: String): Fields {
      return Fields(listOf(Field.of(f1)) + f.map(Field.Companion::of))
    }

    @JvmStatic
    fun ofAll(): Fields {
      return Fields(listOf(Field.of("")))
    }
  }
}

data class AggregatorTarget
internal constructor(
  internal val accumulator: Function.Accumulator,
  internal val fieldName: String,
  override var distinct: Boolean,
) : Selectable, Function.Accumulator

open class Function(val name: String, val params: List<Expr>) : Expr {
  interface FilterCondition : Expr

  interface Accumulator : Expr {
    var distinct: Boolean

    fun distinct(on: Boolean): Accumulator {
      this.distinct = on
      return this
    }

    fun toField(target: String) = AggregatorTarget(this, target, this.distinct)
  }

  internal fun toProto(): Value {
    return Value.newBuilder()
      .setFunctionValue(
        com.google.firestore.v1.Function.newBuilder()
          .setName(name)
          .addAllArgs(params.map { exprToValue(it) })
      )
      .build()
  }

  data class Equal internal constructor(private val left: Expr, private val right: Expr) :
    Function("eq", listOf(left, right)), FilterCondition

  data class NotEqual internal constructor(private val left: Expr, private val right: Expr) :
    Function("neq", listOf(left, right)), FilterCondition

  data class GreaterThan internal constructor(private val left: Expr, private val right: Expr) :
    Function("gt", listOf(left, right)), FilterCondition

  data class GreaterThanOrEqual internal constructor(
    private val left: Expr,
    private val right: Expr
  ) :
    Function("gte", listOf(left, right)), FilterCondition

  data class LessThan internal constructor(private val left: Expr, private val right: Expr) :
    Function("lt", listOf(left, right)), FilterCondition

  data class LessThanOrEqual internal constructor(private val left: Expr, private val right: Expr) :
    Function("lte", listOf(left, right)), FilterCondition

  data class In internal constructor(private val left: Expr, private val others: List<Expr>) :
    Function("in", listOf(left, ListOfExprs(others))), FilterCondition // For 'in'

  data class And<T> internal constructor(private val conditions: List<T>) :
    Function("and", conditions),
                                                       FilterCondition where
  T : FilterCondition

  data class Or<T> internal constructor(private val conditions: List<T>) :
    Function("or", conditions),
                                                      FilterCondition where
  T : FilterCondition

  data class Not internal constructor(private val condition: Expr) :
    Function("not", listOf(condition)), FilterCondition

  data class ArrayContains internal constructor(
    private val array: Expr,
    private val element: Expr
  ) :
    Function("array_contains", listOf(array, element)), FilterCondition

  data class ArrayContainsAny internal constructor(
    private val array: Expr,
    private val elements: List<Expr>
  ) :
    Function("array_contains_any", listOf(array, ListOfExprs(elements))), FilterCondition

  data class IsNaN internal constructor(private val value: Expr) :
    Function("is_nan", listOf(value)), FilterCondition

  data class IsNull internal constructor(private val value: Expr) :
    Function("is_null", listOf(value)), FilterCondition

  data class Sum internal constructor(private val value: Expr, override var distinct: Boolean) :
    Function("sum", listOf(value)), Accumulator

  data class Avg internal constructor(private val value: Expr, override var distinct: Boolean) :
    Function("avg", listOf(value)), Accumulator

  data class Count internal constructor(private val value: Expr?, override var distinct: Boolean) :
    Function("count", value?.let { listOf(it) } ?: emptyList()), Accumulator

  data class Min internal constructor(private val value: Expr, override var distinct: Boolean) :
    Function("min", listOf(value)), Accumulator

  data class Max internal constructor(private val value: Expr, override var distinct: Boolean) :
    Function("max", listOf(value)), Accumulator

  data class CosineDistance internal constructor(
    private val vector1: Expr,
    private val vector2: Expr
  ) :
    Function("cosine_distance", listOf(vector1, vector2))

  data class DotProductDistance internal constructor(
    private val vector1: Expr,
    private val vector2: Expr
  ) :
    Function("dot_product", listOf(vector1, vector2))

  data class EuclideanDistance internal constructor(
    private val vector1: Expr,
    private val vector2: Expr
  ) :
    Function("euclidean_distance", listOf(vector1, vector2))

  data class Generic internal constructor(private val n: String, private val ps: List<Expr>) :
    Function(n, ps)

  companion object {
    @JvmStatic fun equal(left: Expr, right: Expr) = Equal(left, right)

    @JvmStatic fun equal(left: Expr, right: Any) = Equal(left, Constant.of(right))

    @JvmStatic fun notEqual(left: Expr, right: Expr) = NotEqual(left, right)

    @JvmStatic fun notEqual(left: Expr, right: Any) = NotEqual(left, Constant.of(right))

    @JvmStatic fun greaterThan(left: Expr, right: Expr) = GreaterThan(left, right)

    @JvmStatic fun greaterThan(left: Expr, right: Any) = GreaterThan(left, Constant.of(right))

    @JvmStatic fun greaterThanOrEqual(left: Expr, right: Expr) = GreaterThanOrEqual(left, right)

    @JvmStatic
    fun greaterThanOrEqual(left: Expr, right: Any) = GreaterThanOrEqual(left, Constant.of(right))

    @JvmStatic fun lessThan(left: Expr, right: Expr) = LessThan(left, right)

    @JvmStatic fun lessThan(left: Expr, right: Any) = LessThan(left, Constant.of(right))

    @JvmStatic fun lessThanOrEqual(left: Expr, right: Expr) = LessThanOrEqual(left, right)

    @JvmStatic
    fun lessThanOrEqual(left: Expr, right: Any) = LessThanOrEqual(left, Constant.of(right))

    @JvmStatic
    fun inAny(left: Expr, values: List<Any>) =
      In(
        left,
        values.map {
          when (it) {
            is Expr -> it
            else -> Constant.of(it)
          }
        },
      )

    @JvmStatic
    fun notInAny(left: Expr, values: List<Any>) =
      Not(
        In(
          left,
          values.map {
            when (it) {
              is Expr -> it
              else -> Constant.of(it)
            }
          },
        )
      )

    @JvmStatic
    fun <T> and(left: T, right: T) where T : FilterCondition, T : Expr = And(listOf(left, right))

    @JvmStatic
    fun <T> and(left: T, vararg other: T) where T : FilterCondition, T : Expr =
      And(listOf(left) + other.toList())

    @JvmStatic
    fun <T> or(left: T, right: T) where T : FilterCondition, T : Expr = Or(listOf(left, right))

    @JvmStatic
    fun <T> or(left: T, vararg other: T) where T : FilterCondition, T : Expr =
      Or(listOf(left) + other.toList())

    @JvmStatic fun arrayContains(expr: Expr, element: Expr) = ArrayContains(expr, element)

    @JvmStatic
    fun arrayContains(expr: Expr, element: Any) = ArrayContains(expr, Constant.of(element))

    @JvmStatic
    fun arrayContainsAny(expr: Expr, vararg elements: Expr) =
      ArrayContainsAny(expr, elements.toList())

    @JvmStatic
    fun arrayContainsAny(expr: Expr, vararg elements: Any) =
      ArrayContainsAny(expr, elements.toList().map { Constant.of(it) })

    @JvmStatic fun isNaN(expr: Expr) = IsNaN(expr)

    @JvmStatic fun isNull(expr: Expr) = IsNull(expr)

    @JvmStatic fun not(expr: Expr) = Not(expr)

    @JvmStatic fun sum(expr: Expr) = Sum(expr, false)

    @JvmStatic fun avg(expr: Expr) = Avg(expr, false)

    @JvmStatic fun min(expr: Expr) = Sum(expr, false)

    @JvmStatic fun max(expr: Expr) = Avg(expr, false)

    @JvmStatic fun count(expr: Expr) = Count(expr, false)

    @JvmStatic fun countAll() = Count(null, false)

    @JvmStatic fun cosineDistance(expr: Expr, other: Expr) = CosineDistance(expr, other)

    @JvmStatic
    fun cosineDistance(expr: Expr, other: DoubleArray) =
      CosineDistance(expr, Constant.ofVector(other))

    @JvmStatic fun dotProductDistance(expr: Expr, other: Expr) = CosineDistance(expr, other)

    @JvmStatic
    fun dotProductDistance(expr: Expr, other: DoubleArray) =
      CosineDistance(expr, Constant.ofVector(other))

    @JvmStatic fun euclideanDistance(expr: Expr, other: Expr) = EuclideanDistance(expr, other)

    @JvmStatic
    fun euclideanDistance(expr: Expr, other: DoubleArray) =
      EuclideanDistance(expr, Constant.ofVector(other))

    @JvmStatic fun function(name: String, params: List<Expr>) = Generic(name, params)
  }
}
