package com.google.cloud.firestore

import com.google.cloud.firestore.pipeline.AddFields
import com.google.cloud.firestore.pipeline.Collection
import com.google.cloud.firestore.pipeline.CollectionGroup
import com.google.cloud.firestore.pipeline.Database
import com.google.cloud.firestore.pipeline.Expr
import com.google.cloud.firestore.pipeline.Field
import com.google.cloud.firestore.pipeline.Filter
import com.google.cloud.firestore.pipeline.FindNearest
import com.google.cloud.firestore.pipeline.Group
import com.google.cloud.firestore.pipeline.Join
import com.google.cloud.firestore.pipeline.JoinCondition
import com.google.cloud.firestore.pipeline.Limit
import com.google.cloud.firestore.pipeline.Offset
import com.google.cloud.firestore.pipeline.Operation
import com.google.cloud.firestore.pipeline.Ordering
import com.google.cloud.firestore.pipeline.Project
import com.google.cloud.firestore.pipeline.RawOperation
import com.google.cloud.firestore.pipeline.RemoveFields
import com.google.cloud.firestore.pipeline.SemiJoin
import com.google.cloud.firestore.pipeline.Sort
import com.google.cloud.firestore.pipeline.Union
import com.google.cloud.firestore.pipeline.Unnest

class Pipeline {
  private val operations: MutableList<Operation> = mutableListOf()

  private constructor(db: Database) {
    operations.add(db)
  }

  private constructor(collection: Collection) {
    operations.add(collection)
  }

  private constructor(group: CollectionGroup) {
    operations.add(group)
  }

  companion object {
    @JvmStatic
    fun from(collectionName: String): Pipeline {
      return Pipeline(Collection(collectionName))
    }

    @JvmStatic
    fun fromCollectionGroup(group: String): Pipeline {
      return Pipeline(CollectionGroup(group))
    }

    @JvmStatic
    fun entireDatabase(): Pipeline {
      return Pipeline(Database())
    }
  }

  // Fluent API

  fun project(projections: Map<Field, Expr>): Pipeline {
    operations.add(Project(projections))
    return this
  }

  fun addFields(additions: Map<Field, Expr>): Pipeline {
    operations.add(AddFields(additions))
    return this
  }

  fun removeFields(removals: List<Field>): Pipeline {
    operations.add(RemoveFields(removals))
    return this
  }

  fun filter(condition: Expr): Pipeline {
    operations.add(Filter(condition))
    return this
  }

  fun offset(offset: Int): Pipeline {
    operations.add(Offset(offset))
    return this
  }

  fun limit(limit: Int): Pipeline {
    operations.add(Limit(limit))
    return this
  }

  fun union(pipeline: Pipeline, distinct: Boolean): Pipeline {
    operations.add(Union(pipeline, distinct))
    return this
  }

  fun group(fields: Map<Field, Expr>, accumulators: Map<Field, Expr>): Pipeline {
    operations.add(Group(fields, accumulators))
    return this
  }

  fun findNearest(
    property: Field,
    vector: Array<Double>,
    options: FindNearest.FindNearestOptions
  ): Pipeline {
    operations.add(FindNearest(property, vector, options))
    return this
  }

  fun innerJoin(
    condition: JoinCondition,
    alias: Field,
    otherPipeline: Pipeline,
    otherAlias: Field
  ): Pipeline {
    operations.add(Join(Join.Type.INNER, condition, alias, otherPipeline, otherAlias))
    return this
  }

  fun crossJoin(
    condition: JoinCondition,
    alias: Field,
    otherPipeline: Pipeline,
    otherAlias: Field
  ): Pipeline {
    operations.add(Join(Join.Type.CROSS, condition, alias, otherPipeline, otherAlias))
    return this
  }

  fun fullJoin(
    condition: JoinCondition,
    alias: Field,
    otherPipeline: Pipeline,
    otherAlias: Field
  ): Pipeline {
    operations.add(Join(Join.Type.FULL, condition, alias, otherPipeline, otherAlias))
    return this
  }

  fun leftJoin(
    condition: JoinCondition,
    alias: Field,
    otherPipeline: Pipeline,
    otherAlias: Field
  ): Pipeline {
    operations.add(Join(Join.Type.LEFT, condition, alias, otherPipeline, otherAlias))
    return this
  }

  fun rightJoin(
    condition: JoinCondition,
    alias: Field,
    otherPipeline: Pipeline,
    otherAlias: Field
  ): Pipeline {
    operations.add(Join(Join.Type.RIGHT, condition, alias, otherPipeline, otherAlias))
    return this
  }

  fun leftSemiJoin(
    condition: JoinCondition,
    alias: Field,
    otherPipeline: Pipeline,
    otherAlias: Field
  ): Pipeline {
    operations.add(SemiJoin(SemiJoin.Type.LEFT_SEMI, condition, alias, otherPipeline, otherAlias))
    return this
  }

  fun rightSemiJoin(
    condition: JoinCondition,
    alias: Field,
    otherPipeline: Pipeline,
    otherAlias: Field
  ): Pipeline {
    operations.add(SemiJoin(SemiJoin.Type.RIGHT_SEMI, condition, alias, otherPipeline, otherAlias))
    return this
  }

  fun leftAntiSemiJoin(
    condition: JoinCondition,
    alias: Field,
    otherPipeline: Pipeline,
    otherAlias: Field
  ): Pipeline {
    operations.add(
      SemiJoin(
        SemiJoin.Type.LEFT_ANTI_SEMI,
        condition,
        alias,
        otherPipeline,
        otherAlias
      )
    )
    return this
  }

  fun rightAntiSemiJoin(
    condition: JoinCondition,
    alias: Field,
    otherPipeline: Pipeline,
    otherAlias: Field
  ): Pipeline {
    operations.add(
      SemiJoin(
        SemiJoin.Type.RIGHT_ANTI_SEMI,
        condition,
        alias,
        otherPipeline,
        otherAlias
      )
    )
    return this
  }

  fun sort(
    orders: List<Ordering>,
    density: Sort.Density = Sort.Density.UNSPECIFIED,
    truncation: Sort.Truncation = Sort.Truncation.UNSPECIFIED
  ): Pipeline {
    operations.add(Sort(orders, density, truncation))
    return this
  }

  fun unnest(mode: Unnest.Mode, field: Field): Pipeline {
    operations.add(Unnest(mode, field))
    return this
  }

  fun rawOperation(name: String, params: Map<String, Any>? = null): Pipeline {
    operations.add(RawOperation(name, params))
    return this
  }
}
