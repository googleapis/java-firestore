@file:JvmName("Stages")
package com.google.cloud.firestore.pipeline

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.encodeValue
import com.google.firestore.v1.MapValue
import com.google.firestore.v1.Value
import java.util.Locale

internal interface Stage

internal data class Collection(internal val relativePath: String) : Stage {
  val name = "collection"
}

internal data class CollectionGroup(internal val collectionId: String) : Stage {
  val name = "collection_group"
}

internal class Database : Stage {
  val name = "database"
}

internal data class Documents(internal val documents: List<String>) : Stage {
  val name = "documents"

  companion object {
    @JvmStatic
    fun of(vararg documents: DocumentReference): Documents {
      return Documents(documents.map { "/" + it.path })
    }
  }
}

internal data class Select(internal val projections: Map<String, Expr>) : Stage {
  val name = "select"
}

internal data class AddFields(internal val fields: Map<String, Expr>) : Stage {
  val name = "add_fields"
}

internal data class Filter<T>(internal val condition: T) : Stage where
T : Function.FilterCondition,
T : Expr {
  val name = "filter"
}

internal class Offset(internal val offset: Int) : Stage {
  val name = "offset"
}

internal class Limit(internal val limit: Int) : Stage {
  val name = "limit"
}

class Aggregate
internal constructor(
  internal val groups: Map<String, Expr>,
  internal val accumulators: Map<String, Function.Accumulator>,
) : Stage {
  val name = "aggregate"

  internal constructor(
    vararg aggregators: AggregatorTarget
  ) : this(emptyMap(), aggregators.associate { it.fieldName to it.accumulator })
}

class FindNearest
internal constructor(
  internal val property: Field,
  internal val vector: DoubleArray,
  internal val distanceMeasure: DistanceMeasure,
  internal val options: FindNearestOptions,
) : Stage {
  val name = "find_nearest"

  interface DistanceMeasure {
    data object Euclidean : DistanceMeasure

    data object Cosine : DistanceMeasure

    data object DotProduct : DistanceMeasure

    class GenericDistanceMeasure(val name: String) : DistanceMeasure

    fun toProtoString(): String {
      return when (this) {
        is Euclidean -> "euclidean"
        is Cosine -> "cosine"
        is DotProduct -> "dot_product"
        is GenericDistanceMeasure -> name
        else -> throw IllegalArgumentException("Unknown distance measure")
      }
    }

    companion object {
      @JvmStatic fun euclidean() = Euclidean

      @JvmStatic fun cosine() = Cosine

      @JvmStatic fun dotProduct() = DotProduct

      @JvmStatic fun generic(name: String) = GenericDistanceMeasure(name)
    }
  }

  class FindNearestOptions internal constructor(
    val limit: Long,
    val distanceMeasure: DistanceMeasure,
    val output: Field? = null
  ) {
    companion object {
      @JvmStatic
      fun newInstance(limit: Long, distanceMeasure: DistanceMeasure, output: Field? = null) =
        FindNearestOptions(limit, distanceMeasure, output)
    }
  }
}

class Sort
internal constructor(
  internal val orders: List<Ordering>,
  internal val density: Density = Density.UNSPECIFIED,
  internal val truncation: Truncation = Truncation.UNSPECIFIED,
) : Stage {
  val name = "sort"

  enum class Density {
    UNSPECIFIED,
    REQUIRED;

    override fun toString(): String = name.lowercase(Locale.getDefault())
  }

  enum class Truncation {
    UNSPECIFIED,
    DISABLED;

    override fun toString(): String = name.lowercase(Locale.getDefault())
  }

  class Ordering
  internal constructor(private val expr: Expr, private val dir: Direction = Direction.ASCENDING) {
    enum class Direction {
      ASCENDING,
      DESCENDING;

      override fun toString(): String = name.lowercase(Locale.getDefault())
    }

    internal fun toProto(): Value {
      return Value.newBuilder()
        .setMapValue(
          MapValue.newBuilder()
            .putFields("direction", encodeValue(dir.toString()))
            .putFields("expression", encodeValue(expr))
            .build()
        )
        .build()
    }

    companion object {
      @JvmStatic
      fun of(expr: Expr, dir: Direction = Direction.ASCENDING): Ordering {
        return Ordering(expr, dir)
      }

      @JvmStatic
      fun of(expr: Expr): Ordering {
        return Ordering(expr, Direction.ASCENDING)
      }

      @JvmStatic
      fun ascending(expr: Expr): Ordering {
        return Ordering(expr, Direction.ASCENDING)
      }

      @JvmStatic
      fun descending(expr: Expr): Ordering {
        return Ordering(expr, Direction.DESCENDING)
      }
    }
  }
}

internal class GenericStage(internal val name: String, internal val params: List<Any>) : Stage {}

internal fun toStageProto(stage: Stage): com.google.firestore.v1.Pipeline.Stage {
  return when (stage) {
    is Collection ->
      com.google.firestore.v1.Pipeline.Stage.newBuilder()
        .setName(stage.name)
        .addArgs(Value.newBuilder().setReferenceValue("").build())
        .addArgs(Value.newBuilder().setStringValue(stage.relativePath).build())
        .build()
    is CollectionGroup ->
      com.google.firestore.v1.Pipeline.Stage.newBuilder()
        .setName(stage.name)
        .addArgs(Value.newBuilder().setReferenceValue("").build())
        .addArgs(encodeValue(stage.collectionId))
        .build()
    is Database -> com.google.firestore.v1.Pipeline.Stage.newBuilder().setName(stage.name).build()
    is Documents ->
      com.google.firestore.v1.Pipeline.Stage.newBuilder()
        .setName(stage.name)
        .addAllArgs(stage.documents.map { Value.newBuilder().setReferenceValue(it).build() })
        .build()
    is Select ->
      com.google.firestore.v1.Pipeline.Stage.newBuilder()
        .setName(stage.name)
        .addArgs(encodeValue(stage.projections))
        .build()
    is AddFields ->
      com.google.firestore.v1.Pipeline.Stage.newBuilder()
        .setName(stage.name)
        .addArgs(encodeValue(stage.fields))
        .build()
    is Filter<*> ->
      com.google.firestore.v1.Pipeline.Stage.newBuilder()
        .setName(stage.name)
        .addArgs(encodeValue(stage.condition))
        .build()
    is Sort ->
      com.google.firestore.v1.Pipeline.Stage.newBuilder()
        .setName(stage.name)
        .addAllArgs(stage.orders.map { it.toProto() })
        .putOptions("density", encodeValue(stage.density.toString()))
        .putOptions("truncation", encodeValue(stage.truncation.toString()))
        .build()
    is Offset ->
      com.google.firestore.v1.Pipeline.Stage.newBuilder()
        .setName(stage.name)
        .addArgs(encodeValue(stage.offset))
        .build()
    is Limit ->
      com.google.firestore.v1.Pipeline.Stage.newBuilder()
        .setName(stage.name)
        .addArgs(encodeValue(stage.limit))
        .build()
    is Aggregate ->
      com.google.firestore.v1.Pipeline.Stage.newBuilder()
        .setName(stage.name)
        .addArgs(encodeValue(stage.groups))
        .addArgs(encodeValue(stage.accumulators))
        .build()
    is FindNearest ->
      com.google.firestore.v1.Pipeline.Stage.newBuilder()
        .setName(stage.name)
        .addArgs(encodeValue(stage.property))
        .addArgs(encodeValue(stage.vector))
        .addArgs(encodeValue(stage.distanceMeasure.toProtoString()))
        .putOptions("limit", encodeValue(stage.options.limit))
        .putOptions("distance_field", encodeValue(stage.options.output))
        .build()
    is GenericStage ->
      com.google.firestore.v1.Pipeline.Stage.newBuilder()
        .setName(stage.name)
        .addAllArgs(stage.params.map { encodeValue(it) })
        .build()
    else -> {
      TODO()
    }
  }
}
