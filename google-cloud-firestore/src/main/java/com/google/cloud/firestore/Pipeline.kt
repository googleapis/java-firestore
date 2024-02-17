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
import com.google.cloud.firestore.pipeline.Projectable
import com.google.cloud.firestore.pipeline.RawOperation
import com.google.cloud.firestore.pipeline.RemoveFields
import com.google.cloud.firestore.pipeline.SemiJoin
import com.google.cloud.firestore.pipeline.Sort
import com.google.cloud.firestore.pipeline.UnionWith
import com.google.cloud.firestore.pipeline.Unnest

class Pipeline {
  private val operations: MutableList<Operation> = mutableListOf()
  private var name: String

  private constructor(db: Database) {
    operations.add(db)
    name = "(database)"
  }

  private constructor(collection: Collection) {
    operations.add(collection)
    name = collection.path
  }

  private constructor(group: CollectionGroup) {
    operations.add(group)
    name = group.path
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

  fun withName(name: String) {
    this.name = name
  }

  fun project(projections: Map<Field, Expr>): Pipeline {
    operations.add(Project(projections))
    return this
  }

  // Sugar for project
  fun project(vararg fields: Field): Pipeline {
    return this
  }

  fun project(vararg exprs: Expr.ExprAsAlias): Pipeline {
    return this
  }

  fun project(vararg projections: Projectable): Pipeline {
    return this
  }

  fun addFields(additions: Map<Field, Expr>): Pipeline {
    operations.add(AddFields(additions))
    return this
  }

  // Sugar
  fun addFields(vararg additions: Expr.ExprAsAlias): Pipeline {
    return this
  }

  fun removeFields(vararg removals: Field): Pipeline {
    operations.add(RemoveFields(removals.toList()))
    return this
  }

  fun filter(condition: Expr.Function.ProducingBoolean): Pipeline {
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

  fun unionWith(pipeline: Pipeline, distinct: Boolean): Pipeline {
    operations.add(UnionWith(pipeline, distinct))
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
    otherPipeline: Pipeline,
    condition: JoinCondition,
    alias: Field = Field.of(this.name),
    otherAlias: Field = Field.of(otherPipeline.name)
  ): Pipeline {
    operations.add(Join(Join.Type.INNER, condition, alias, otherPipeline, otherAlias))
    return this
  }

  fun crossJoin(
    otherPipeline: Pipeline,
    condition: JoinCondition,
    alias: Field = Field.of(this.name),
    otherAlias: Field = Field.of(otherPipeline.name)
  ): Pipeline {
    operations.add(Join(Join.Type.CROSS, condition, alias, otherPipeline, otherAlias))
    return this
  }

  fun fullJoin(
    otherPipeline: Pipeline,
    condition: JoinCondition,
    alias: Field = Field.of(this.name),
    otherAlias: Field = Field.of(otherPipeline.name)
  ): Pipeline {
    operations.add(Join(Join.Type.FULL, condition, alias, otherPipeline, otherAlias))
    return this
  }

  fun leftJoin(
    otherPipeline: Pipeline,
    condition: JoinCondition,
    alias: Field = Field.of(this.name),
    otherAlias: Field = Field.of(otherPipeline.name)
  ): Pipeline {
    operations.add(Join(Join.Type.LEFT, condition, alias, otherPipeline, otherAlias))
    return this
  }

  fun rightJoin(
    otherPipeline: Pipeline,
    condition: JoinCondition,
    alias: Field = Field.of(this.name),
    otherAlias: Field = Field.of(otherPipeline.name)
  ): Pipeline {
    operations.add(Join(Join.Type.RIGHT, condition, alias, otherPipeline, otherAlias))
    return this
  }

  fun leftSemiJoin(
    otherPipeline: Pipeline,
    condition: JoinCondition,
    alias: Field = Field.of(this.name),
    otherAlias: Field = Field.of(otherPipeline.name)
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
    otherPipeline: Pipeline,
    condition: JoinCondition,
    alias: Field = Field.of(this.name),
    otherAlias: Field = Field.of(otherPipeline.name)
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
    otherPipeline: Pipeline,
    condition: JoinCondition,
    alias: Field = Field.of(this.name),
    otherAlias: Field = Field.of(otherPipeline.name)
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

  // Sugar
  fun sort(vararg orders: Ordering): Pipeline {
    return this
  }

  fun unnest(field: Field, mode: Unnest.Mode = Unnest.Mode.FULL_REPLACE ): Pipeline {
    operations.add(Unnest(mode, field))
    return this
  }

  fun rawOperation(name: String, params: Map<String, Any>? = null): Pipeline {
    operations.add(RawOperation(name, params))
    return this
  }
}
