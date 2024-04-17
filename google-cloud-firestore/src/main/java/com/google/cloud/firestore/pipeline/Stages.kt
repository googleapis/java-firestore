package com.google.cloud.firestore.pipeline

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.encodeValue
import com.google.firestore.v1.MapValue
import com.google.firestore.v1.Value
import java.util.Locale

internal interface Stage

internal data class Collection(val relativePath: String) : Stage {
  val name = "collection"
}

internal data class CollectionGroup(val collectionId: String) : Stage {
  val name = "collection_group"
}

internal class Database: Stage {
  val name = "database"
}

internal data class Documents(val documents: List<String>): Stage {
  val name = "documents"
  companion object {
    @JvmStatic
    fun of(vararg documents: DocumentReference): Documents {
      return Documents(documents.map { it.path })
    }
  }
}

internal data class Project(val projections: Map<String, Expr>) : Stage {
  val name = "project"
}

internal data class AddFields(val fields: Map<String, Expr>) : Stage {
  val name = "add_fields"
}

internal data class Filter<T> (val condition: T) : Stage where T:Function.FilterCondition, T:Expr {
  val name = "filter"
}

internal class Offset(val offset: Int) : Stage {
  val name = "offset"
}

internal class Limit(val limit: Int) : Stage {
  val name = "limit"
}

class Aggregate internal constructor(
  val groups: Map<String, Expr>,
  val accumulators: Map<String, Function.Accumulator>
) : Stage {
  val name = "aggregate"

  internal constructor(vararg aggregators: AggregatorTarget) :
    this(emptyMap(), aggregators.associate { it.fieldName to it.accumulator })
}

class FindNearest internal constructor(
  val property: Field,
  val vector: DoubleArray,
  val distanceMeasure: DistanceMeasure,
  val options: FindNearestOptions
) : Stage {
  val name = "find_nearest"

  sealed interface DistanceMeasure {
    data object Euclidean : DistanceMeasure
    data object Cosine : DistanceMeasure
    data object DotProduct : DistanceMeasure

    class GenericDistanceMeasure(val name: String) : DistanceMeasure

    fun toProtoString(): String{
      return when (this) {
        is Euclidean -> "euclidean"
        is Cosine -> "cosine"
        is DotProduct -> "dot_product"
        is GenericDistanceMeasure -> name
      }
    }

    companion object {
      @JvmStatic
      fun euclidean() = Euclidean

      @JvmStatic
      fun cosine() = Cosine

      @JvmStatic
      fun dotProduct() = DotProduct

      @JvmStatic
      fun generic(name: String) = GenericDistanceMeasure(name)
    }
  }

  data class FindNearestOptions(
    val limit: Long?,
    val output: Field? = null
  )
}

class Sort internal constructor(
  val orders: List<Ordering>,
  val density: Density = Density.UNSPECIFIED,
  val truncation: Truncation = Truncation.UNSPECIFIED
) : Stage {
  val name = "sort"

  enum class Density {
    UNSPECIFIED,
    REQUIRED;
    override fun toString(): String
      = name.lowercase(Locale.getDefault())
  }

  enum class Truncation {
    UNSPECIFIED,
    DISABLED;
    override fun toString(): String
      = name.lowercase(Locale.getDefault())
  }

  class Ordering internal constructor(private val expr: Expr, private val dir: Direction = Direction.ASCENDING) {
    enum class Direction {
      ASCENDING,
      DESCENDING;

      override fun toString(): String
        = name.lowercase(Locale.getDefault())
    }

    internal fun toProto(): Value {
      return Value.newBuilder().setMapValue(MapValue.newBuilder()
                                              .putFields("direction", encodeValue(dir.toString()))
                                              .putFields("expression", encodeValue(expr))
                                              .build()).build()
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

// internal class Pagination(): Stage, GenericStage()

internal open class GenericStage(val name: String, val params: List<Any>) : Stage {
}

internal fun toStageProto(stage: Stage): com.google.firestore.v1.Pipeline.Stage {
  return when (stage) {
    is Collection -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .addArgs(
        Value.newBuilder().setReferenceValue(stage.relativePath).build()
      )
      .build()

    is CollectionGroup -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .addArgs(
        Value.newBuilder().setReferenceValue("").build()
      )
      .addArgs(
        encodeValue(
          stage.collectionId,
        )
      )
      .build()

    is Database -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .build()

    is Documents -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .addAllArgs(stage.documents.map {
        Value.newBuilder().setReferenceValue(it).build()
      })
      .build()

    is Project -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .addArgs(
        encodeValue(
          stage.projections,
        )
      )
      .build()

    is AddFields -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .addArgs(
        encodeValue(
          stage.fields,
        )
      )
      .build()

    is Filter<*> -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .addArgs(
        encodeValue(
          stage.condition,
        )
      )
      .build()

    is Sort -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .addAllArgs(
        stage.orders.map { it.toProto() }
      )
      .putOptions("density", encodeValue(stage.density))
      .putOptions("truncation", encodeValue(stage.truncation))
      .build()

    is Offset -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .addArgs(
        encodeValue(
          stage.offset
        )
      )
      .build()

    is Limit -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .addArgs(
        encodeValue(
          stage.limit
        )
      )
      .build()

    is Aggregate -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .addArgs(encodeValue(stage.groups))
      .addArgs(encodeValue(stage.accumulators))
      .build()

    is FindNearest -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .addArgs(
        encodeValue(
          stage.property
        )
      )
      .addArgs(
        encodeValue(
          stage.vector
        )
      )
      .addArgs(
        encodeValue(
          stage.distanceMeasure.toProtoString()
        )
      )
      .putOptions("limit", encodeValue(stage.options.limit))
      .putOptions("distance_field", encodeValue(stage.options.output))
      .build()

    is GenericStage -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName(stage.name)
      .addAllArgs(
        stage.params.map { encodeValue(it) }
      )
      .build()

    else -> {
      TODO()
    }
  }
}
