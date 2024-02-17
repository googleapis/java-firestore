package com.google.cloud.firestore.pipeline

import com.google.cloud.firestore.Pipeline

interface Operation {}

data class Field internal constructor(val path: String): Projectable {
  companion object {
    fun of(path: String): Field {
      return Field(path)
    }
  }
}

class Database(): Operation
data class Collection(val path: String): Operation
data class CollectionGroup(val path: String): Operation

data class Project(val projections: Map<Field, Expr>): Operation
data class AddFields(val additions: Map<Field, Expr>): Operation
data class RemoveFields(val removals: List<Field>): Operation
data class Filter(val condition: Expr.Function.ProducingBoolean): Operation
data class Offset(val offset: Int): Operation
data class Limit(val limit: Int): Operation
data class UnionWith(val pipeline: Pipeline, val distinct: Boolean): Operation

data class Group(val fields: Map<Field, Expr>, val accumulators: Map<Field, Expr>): Operation

data class FindNearest(val property: Field,
                       val vector: Array<Double>,
                       val options: FindNearestOptions): Operation {
  enum class Similarity {
    EUCLIDEAN,
    COSINE,
    DOT_PRODUCT
  }

  data class FindNearestOptions(val similarity: Similarity, val limit: Long, val output: Field) {}
}

sealed interface JoinCondition {
  data class Expression(val expr: Expr): JoinCondition
  data class Using(val fields: Set<Field>): JoinCondition
}

data class Join(val type: Type,
                val condition: JoinCondition,
                val alias: Field,
                val otherPipeline: Pipeline,
                val otherAlias: Field): Operation {
  enum class Type {
    CROSS,
    INNER,
    FULL,
    LEFT,
    RIGHT
  }
}

data class SemiJoin(val type: Type,
                    val condition: JoinCondition,
                    val alias: Field,
                    val otherPipeline: Pipeline,
                    val otherAlias: Field): Operation {
  enum class Type {
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
}

data class Sort(val orders: List<Ordering>,
                val density: Density = Density.UNSPECIFIED,
                val truncation: Truncation = Truncation.UNSPECIFIED): Operation {
  enum class Density{
    UNSPECIFIED,
    REQUIRED
  }

  enum class Truncation {
    UNSPECIFIED,
    DISABLED
  }
}

data class Unnest(val mode: Mode, val field: Field): Operation {
  enum class Mode {
    FULL_REPLACE,
    MERGE_PREFER_NEST,
    MERGE_PREFER_PARENT;
  }
}

data class RawOperation(val name: String, val params: Map<String, Any>?): Operation

