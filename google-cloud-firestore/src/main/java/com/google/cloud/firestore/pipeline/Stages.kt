package com.google.cloud.firestore.pipeline

interface Stage

internal data class Collection(val path: String) : Stage
internal data class CollectionGroup(val path: String) : Stage

internal data class Project(val projections: Map<Field, Expr>) : Stage
internal data class Filter(val condition: Function.FilterCondition) : Stage
internal data class Offset(val offset: Int) : Stage
internal data class Limit(val limit: Int) : Stage

data class FindNearest internal constructor(
  val property: Field,
  val vector: DoubleArray,
  val options: FindNearestOptions
) : Stage {
  sealed interface Similarity {
    data object Euclidean : Similarity
    data object Cosine : Similarity
    data object DotProduct : Similarity

    class GenericSimilarity(val name: String) : Similarity

    companion object {
      @JvmStatic
      fun euclidean() = Euclidean

      @JvmStatic
      fun cosine() = Cosine

      @JvmStatic
      fun dotProduct() = DotProduct

      @JvmStatic
      fun generic(name: String) = GenericSimilarity(name)
    }
  }

  data class FindNearestOptions(
    val similarity: Similarity,
    val limit: Long,
    val output: Field? = null
  )
}

data class Ordering internal constructor(val expr: Expr, val dir: Direction = Direction.ASC) {
  enum class Direction {
    ASC,
    DESC
  }

  companion object {
    @JvmStatic
    fun of(expr: Expr, dir: Direction = Direction.ASC): Ordering {
      return Ordering(expr, dir)
    }

    @JvmStatic
    fun of(expr: Expr): Ordering {
      return Ordering(expr, Direction.ASC)
    }

    @JvmStatic
    fun ascending(expr: Expr): Ordering {
      return Ordering(expr, Direction.ASC)
    }

    @JvmStatic
    fun descending(expr: Expr): Ordering {
      return Ordering(expr, Direction.DESC)
    }
  }
}

data class Sort internal constructor(
  val orders: List<Ordering>,
  val density: Density = Density.UNSPECIFIED,
  val truncation: Truncation = Truncation.UNSPECIFIED
) : Stage {
  enum class Density {
    UNSPECIFIED,
    REQUIRED
  }

  enum class Truncation {
    UNSPECIFIED,
    DISABLED
  }
}

data class GenericStage(val name: String, val params: Map<String, Any>?) : Stage

