package com.google.cloud.firestore

import com.google.cloud.firestore.pipeline.AddFields
import com.google.cloud.firestore.pipeline.Collection
import com.google.cloud.firestore.pipeline.CollectionGroup
import com.google.cloud.firestore.pipeline.Database
import com.google.cloud.firestore.pipeline.Expr
import com.google.cloud.firestore.pipeline.Expr.AllFields
import com.google.cloud.firestore.pipeline.Expr.Field
import com.google.cloud.firestore.pipeline.Fields
import com.google.cloud.firestore.pipeline.Filter
import com.google.cloud.firestore.pipeline.FindNearest
import com.google.cloud.firestore.pipeline.GenericOperation
import com.google.cloud.firestore.pipeline.Group
import com.google.cloud.firestore.pipeline.Join
import com.google.cloud.firestore.pipeline.Limit
import com.google.cloud.firestore.pipeline.Offset
import com.google.cloud.firestore.pipeline.Operation
import com.google.cloud.firestore.pipeline.Ordering
import com.google.cloud.firestore.pipeline.Project
import com.google.cloud.firestore.pipeline.Projectable
import com.google.cloud.firestore.pipeline.RemoveFields
import com.google.cloud.firestore.pipeline.Sort
import com.google.cloud.firestore.pipeline.UnionWith
import com.google.cloud.firestore.pipeline.Unnest

class GroupingPipeline internal constructor(val p: Pipeline, vararg val by: Projectable) {
  fun aggregate(vararg aggregator: Expr.AggregatorTarget): Pipeline {
    // TODO: this.p.operations.add()
    return this.p
  }
}

class JoiningPipeline internal constructor(
  val left: Pipeline,
  val right: Pipeline,
  val join: Join.Type
) {
  fun on(condition: Expr.Function): Pipeline {
    // TODO: this.p.operations.add()
    return left
  }

  fun on(vararg field: Field): Pipeline {
    // TODO: this.p.operations.add()
    return left
  }

  fun on(field: Fields): Pipeline {
    // TODO: this.p.operations.add()
    return left
  }
}

class PaginatingPipeline internal constructor(
  val p: Pipeline,
  pageSize: Int,
  orders: Array<out Ordering>
) {
  fun firstPage(): Pipeline {
    return this.p
  }

  fun page(n:Int): Pipeline {
    return this.p
  }

  fun startAt(result: PipelineResult): Pipeline {
    return this.p
  }

  fun startAfter(result: PipelineResult): Pipeline {
    return this.p
  }

  fun endAt(result: PipelineResult): Pipeline {
    return this.p
  }

  fun endBefore(result: PipelineResult): Pipeline {
    return this.p
  }
}

class Pipeline {
  internal val operations: MutableList<Operation> = mutableListOf()
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
    fun from(source: CollectionReference): Pipeline {
      return Pipeline(Collection(source.path))
    }

    @JvmStatic
    fun from(source: com.google.cloud.firestore.CollectionGroup): Pipeline {
      return Pipeline(CollectionGroup(source.options.collectionId))
    }

    @JvmStatic
    fun fromCollection(collectionName: String): Pipeline {
      return Pipeline(Collection(collectionName))
    }

    @JvmStatic
    fun fromCollectionGroup(group: String): Pipeline {
      return Pipeline(CollectionGroup(group))
    }

    @JvmStatic
    fun fromDatabase(): Pipeline {
      return Pipeline(Database())
    }
  }

  fun fieldOf(name: String): Field {
    return Field(name, this)
  }

  fun fieldOfAll(): AllFields {
    return AllFields(this)
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

  fun filter(condition: Expr.Function.FilterCondition): Pipeline {
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

  fun group(by: Fields): GroupingPipeline {
    // operations.add(Group(fields, accumulators))
    return GroupingPipeline(this /*TODO*/)
  }

  fun group(by: Projectable): GroupingPipeline {
    // operations.add(Group(fields, accumulators))
    return GroupingPipeline(this /*TODO*/)
  }

  fun aggregate(vararg aggregator: Expr.AggregatorTarget): Pipeline {
    // operations.add(Group())
    // operations.add(aggregator)
    return this
  }

  fun findNearest(
    property: Field,
    vector: DoubleArray,
    options: FindNearest.FindNearestOptions
  ): Pipeline {
    operations.add(FindNearest(property, vector, options))
    return this
  }

  fun innerJoin(
    otherPipeline: Pipeline
  ) = JoiningPipeline(this, otherPipeline, Join.Type.INNER)

  fun crossJoin(otherPipeline: Pipeline): JoiningPipeline =
    JoiningPipeline(this, otherPipeline, Join.Type.CROSS)

  fun fullJoin(otherPipeline: Pipeline): JoiningPipeline =
    JoiningPipeline(this, otherPipeline, Join.Type.FULL)

  fun leftJoin(otherPipeline: Pipeline): JoiningPipeline =
    JoiningPipeline(this, otherPipeline, Join.Type.LEFT)

  fun rightJoin(otherPipeline: Pipeline): JoiningPipeline =
    JoiningPipeline(this, otherPipeline, Join.Type.RIGHT)

  fun leftSemiJoin(otherPipeline: Pipeline): JoiningPipeline =
    JoiningPipeline(this, otherPipeline, Join.Type.LEFT_SEMI)

  fun rightSemiJoin(otherPipeline: Pipeline): JoiningPipeline =
    JoiningPipeline(this, otherPipeline, Join.Type.RIGHT_SEMI)

  fun leftAntiSemiJoin(otherPipeline: Pipeline): JoiningPipeline =
    JoiningPipeline(this, otherPipeline, Join.Type.LEFT_ANTI_SEMI)

  fun rightAntiSemiJoin(otherPipeline: Pipeline): JoiningPipeline =
    JoiningPipeline(this, otherPipeline, Join.Type.RIGHT_ANTI_SEMI)

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

  fun unnest(field: Field, mode: Unnest.Mode = Unnest.Mode.FULL_REPLACE): Pipeline {
    operations.add(Unnest(mode, field))
    return this
  }

  fun paginate(pageSize: Int, vararg orders: Ordering): PaginatingPipeline {
    return PaginatingPipeline(this, pageSize, orders)
  }

  fun rawOperation(name: String, params: Map<String, Any>? = null): Pipeline {
    operations.add(GenericOperation(name, params))
    return this
  }
}

// placeholder for now
class PipelineResult {

}
