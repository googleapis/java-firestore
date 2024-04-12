package com.google.cloud.firestore

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutures
import com.google.api.gax.rpc.ApiStreamObserver
import com.google.cloud.firestore.UserDataConverter.EncodingOptions
import com.google.cloud.firestore.pipeline.AggregatorTarget
import com.google.cloud.firestore.pipeline.Collection
import com.google.cloud.firestore.pipeline.CollectionGroup
import com.google.cloud.firestore.pipeline.Expr
import com.google.cloud.firestore.pipeline.ExprAsAlias
import com.google.cloud.firestore.pipeline.Field
import com.google.cloud.firestore.pipeline.Fields
import com.google.cloud.firestore.pipeline.Filter
import com.google.cloud.firestore.pipeline.FindNearest
import com.google.cloud.firestore.pipeline.Function
import com.google.cloud.firestore.pipeline.Project
import com.google.cloud.firestore.pipeline.Projectable
import com.google.cloud.firestore.pipeline.Sort
import com.google.cloud.firestore.pipeline.Sort.Ordering
import com.google.cloud.firestore.pipeline.Stage
import com.google.cloud.firestore.pipeline.ToProto
import com.google.firestore.v1.Value

class PaginatingPipeline internal constructor(
  val p: Pipeline,
  pageSize: Int,
  orders: Array<out Sort.Ordering>
) {
  fun firstPage(): Pipeline {
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
class Pipeline private constructor(private val stages: List<Stage>, private val name: String):
  ToProto {

  private constructor(collection: Collection) : this(listOf(collection), collection.path)

  private constructor(group: CollectionGroup): this(listOf(group), group.path)

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
  }

  fun project(vararg projections: Projectable): Pipeline {
    val projMap = mutableMapOf<String, Expr>()
    for(proj in projections) {
      when (proj){
        is Field -> projMap[proj.field] = proj
        is AggregatorTarget -> projMap[proj.target] = proj.current
        is ExprAsAlias -> projMap[proj.alias] = proj.current
        is Fields -> proj.fs?.forEach { projMap[it.field] = it}
      }
    }
    return Pipeline(stages.plus(Project(projMap)), name)
  }

  fun filter(condition: Function.FilterCondition): Pipeline {
    return Pipeline(stages.plus(Filter(condition)), name)
  }

  fun offset(offset: Int): Pipeline {
    return this
  }

  fun limit(limit: Int): Pipeline {
    return this
  }

  fun aggregate(vararg aggregator: AggregatorTarget): Pipeline {
    // operations.add(Group())
    // operations.add(aggregator)
    return this
  }

  fun findNearest(
    property: Field,
    vector: DoubleArray,
    options: FindNearest.FindNearestOptions
  ): Pipeline {
    return this
  }

  fun sort(
    orders: List<Sort.Ordering>,
    density: Sort.Density = Sort.Density.UNSPECIFIED,
    truncation: Sort.Truncation = Sort.Truncation.UNSPECIFIED
  ): Pipeline {
    return this
  }

  // Sugar
  fun sort(vararg orders: Ordering): Pipeline {
    return this
  }

  fun paginate(pageSize: Int, vararg orders: Ordering): PaginatingPipeline {
    return PaginatingPipeline(this, pageSize, orders)
  }

  fun genericOperation(name: String, params: Map<String, Any>? = null): Pipeline {
    return this
  }

  fun execute(db: Firestore): ApiFuture<Iterator<PipelineResult>> {
    return ApiFutures.immediateFuture(listOf(PipelineResult()).iterator())
  }

  fun execute(db: Firestore, observer: ApiStreamObserver<PipelineResult>): Unit {
  }

  override fun toProto(): Value {
    return Value.newBuilder()
      .setPipelineValue(com.google.firestore.v1.Pipeline.newBuilder()
                          .addAllStages(stages.map { toStageProto(it) })
      )
      .build()
  }
}

internal fun toStageProto(stage: Stage): com.google.firestore.v1.Pipeline.Stage {
  return when (stage) {
    is Project -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName("project")
      .addArgs(UserDataConverter.encodeValue(FieldPath.empty(), stage.projections, UserDataConverter.ARGUMENT))
      .build()
    is Collection -> com.google.firestore.v1.Pipeline.Stage.newBuilder()
      .setName("collection")
      .addArgs(UserDataConverter.encodeValue(FieldPath.empty(),stage.path, UserDataConverter.ARGUMENT))
      .build()
    else -> {
      TODO()
    }
  }
}

