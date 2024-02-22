package com.google.cloud.firestore.pipeline

import com.google.cloud.firestore.Pipeline

interface Operation

class Database : Operation
data class Collection(val path: String) : Operation
data class CollectionGroup(val path: String) : Operation

data class Project(val projections: Map<Expr.Field, Expr>) : Operation
data class AddFields(val additions: Map<Expr.Field, Expr>) : Operation
data class RemoveFields(val removals: List<Expr.Field>) : Operation
data class Filter(val condition: Expr.Function.FilterCondition) : Operation
data class Offset(val offset: Int) : Operation
data class Limit(val limit: Int) : Operation
data class UnionWith(val pipeline: Pipeline, val distinct: Boolean) : Operation

data class Group(val fields: Map<Expr.Field, Expr>, val accumulators: Map<Expr.Field, Expr>) :
  Operation

data class FindNearest(
  val property: Expr.Field,
  val vector: DoubleArray,
  val options: FindNearestOptions
) : Operation {
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
    val output: Expr.Field
  )
}

sealed interface JoinCondition {
  data class Expression(val expr: Expr) : JoinCondition
  data class Using(val fields: Set<Expr.Field>) : JoinCondition
}

data class Join(
  val type: Type,
  val condition: JoinCondition,
  val alias: Expr.Field,
  val otherPipeline: Pipeline,
  val otherAlias: Expr.Field
) : Operation {
  enum class Type {
    CROSS,
    INNER,
    FULL,
    LEFT,
    RIGHT,
    LEFT_SEMI,
    RIGHT_SEMI,
    LEFT_ANTI_SEMI,
    RIGHT_ANTI_SEMI,
  }
}

data class Ordering(val expr: Expr, val dir: Direction = Direction.ASC) {
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
  }
}

data class Sort(
  val orders: List<Ordering>,
  val density: Density = Density.UNSPECIFIED,
  val truncation: Truncation = Truncation.UNSPECIFIED
) : Operation {
  enum class Density {
    UNSPECIFIED,
    REQUIRED
  }

  enum class Truncation {
    UNSPECIFIED,
    DISABLED
  }
}

data class Unnest(val mode: Mode, val field: Expr.Field) : Operation {
  enum class Mode {
    FULL_REPLACE,
    MERGE_PREFER_NEST,
    MERGE_PREFER_PARENT;
  }
}

data class GenericOperation(val name: String, val params: Map<String, Any>?) : Operation

