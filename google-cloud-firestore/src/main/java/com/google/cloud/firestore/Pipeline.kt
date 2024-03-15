package com.google.cloud.firestore

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutures
import com.google.api.gax.rpc.ApiStreamObserver
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

/**
 * The Pipeline class provides a flexible and expressive framework for building complex data transformation
 * and query pipelines for Firestore.
 *
 * A pipeline takes data sources such as Firestore collections, collection groups, or even in-memory data, and
 * applies a series of operations that are chained together, each operation takes the output from the last
 * operation (or the data source) and produces an output for the next operation (or as the final output of the pipeline).
 *
 * NOTE: the chained operations are not a prescription of exactly how Firestore will execute the pipeline,
 * instead Firestore only guarantee the result is the same as if the chained operations are executed in order.
 *
 * Usage Examples:
 *
 * **1. Projecting Specific Fields and Renaming:**
 * ```java
 * Pipeline pipeline = Pipeline.fromCollection("users")
 *     // Select 'name' and 'email' fields, create 'userAge' which is renamed from field 'age'.
 *     .project(Fields.of("name", "email"), Field.of("age").asAlias("userAge"))
 * ```
 *
 * **2. Filtering and Sorting:**
 * ```java
 * Pipeline pipeline = Pipeline.fromCollectionGroup("reviews")
 *     .filter(Field.of("rating").greaterThan(Expr.Constant.of(3))) // High ratings
 *     .sort(Ordering.of("timestamp").descending());
 * ```
 *
 * **3. Aggregation with Grouping:**
 * ```java
 * Pipeline pipeline = Pipeline.fromCollection("orders")
 *     .group(Field.of("customerId"))
 *     .aggregate(count(Field.of("orderId")).asAlias("orderCount"));
 * ```
 */
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

    @JvmStatic
    fun fromDocuments(vararg docPath :String): Pipeline {
      return Pipeline(Database())
    }

    @JvmStatic
    fun fromData(vararg doc: Map<String, Map<String, Expr.Constant>>): Pipeline {
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

  fun genericOperation(name: String, params: Map<String, Any>? = null): Pipeline {
    operations.add(GenericOperation(name, params))
    return this
  }

  fun execute(db: Firestore): ApiFuture<Iterator<PipelineResult>> {
    return ApiFutures.immediateFuture(listOf(PipelineResult()).iterator())
  }

  fun execute(db: Firestore, observer: ApiStreamObserver<PipelineResult>): Unit {
  }
}

