@file:JvmName("Pipelines")
package com.google.cloud.firestore

import com.google.api.core.ApiFuture
import com.google.api.core.SettableApiFuture
import com.google.api.gax.rpc.ApiStreamObserver
import com.google.api.gax.rpc.ResponseObserver
import com.google.api.gax.rpc.StreamController
import com.google.cloud.Timestamp
import com.google.cloud.firestore.pipeline.AddFields
import com.google.cloud.firestore.pipeline.Aggregate
import com.google.cloud.firestore.pipeline.AggregatorTarget
import com.google.cloud.firestore.pipeline.Collection
import com.google.cloud.firestore.pipeline.CollectionGroup
import com.google.cloud.firestore.pipeline.Database
import com.google.cloud.firestore.pipeline.Documents
import com.google.cloud.firestore.pipeline.Expr
import com.google.cloud.firestore.pipeline.Field
import com.google.cloud.firestore.pipeline.Fields
import com.google.cloud.firestore.pipeline.Filter
import com.google.cloud.firestore.pipeline.FindNearest
import com.google.cloud.firestore.pipeline.Function
import com.google.cloud.firestore.pipeline.Group
import com.google.cloud.firestore.pipeline.Join
import com.google.cloud.firestore.pipeline.Limit
import com.google.cloud.firestore.pipeline.Offset
import com.google.cloud.firestore.pipeline.ReplaceMap
import com.google.cloud.firestore.pipeline.Select
import com.google.cloud.firestore.pipeline.Selectable
import com.google.cloud.firestore.pipeline.Sort
import com.google.cloud.firestore.pipeline.Sort.Ordering
import com.google.cloud.firestore.pipeline.Stage
import com.google.cloud.firestore.pipeline.UnionWith
import com.google.cloud.firestore.pipeline.UnnestArray
import com.google.cloud.firestore.pipeline.toStageProto
import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableMap
import com.google.firestore.v1.Cursor
import com.google.firestore.v1.Document
import com.google.firestore.v1.ExecutePipelineRequest
import com.google.firestore.v1.ExecutePipelineResponse
import com.google.firestore.v1.StructuredPipeline
import com.google.firestore.v1.Value
import io.opencensus.trace.AttributeValue
import io.opencensus.trace.Tracing
import java.util.logging.Level
import java.util.logging.Logger

class GroupingPipeline internal constructor(val p: Pipeline, vararg val by: Selectable) {
  fun aggregate(vararg aggregator: AggregatorTarget): Pipeline {
    // TODO: this.p.operations.add()
    return this.p
  }
}

class JoiningPipeline internal constructor(
  private val left: Pipeline,
  private val right: Pipeline,
  private val join: Join.Type,
  private val leftPrefix: String? = null,
  private val rightPrefix: String? = null,
) {
  fun on(condition: Function.FilterCondition): Pipeline {
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

internal fun setStartCursor(pipeline: PaginatingPipeline, cursor: Cursor): PaginatingPipeline {
  return pipeline
}

internal fun setEndCursor(pipeline: PaginatingPipeline, cursor: Cursor): PaginatingPipeline {
  return pipeline
}

class PaginatingPipeline
internal constructor(
  internal val p: Pipeline,
  internal val pageSize: Int,
  internal val orders: List<Ordering>,
  private val offset: Int? = null,
  private val startCursor: Cursor? = null,
  private val endCursor: Cursor? = null,
) {
  fun firstPage(): Pipeline {
    return this.p
  }

  fun lastPage(): Pipeline {
    return this.p
  }

  fun startAt(result: PipelineResult): PaginatingPipeline {
    return this
  }

  fun startAfter(result: PipelineResult): PaginatingPipeline {
    return this
  }

  fun endAt(result: PipelineResult): PaginatingPipeline {
    return this
  }

  fun endBefore(result: PipelineResult): PaginatingPipeline {
    return this
  }

  // Internal as this is only potentially used when converting Query to Pipeline.
  internal fun offset(offset: Int): PaginatingPipeline {
    return this
  }
}

/**
 * The Pipeline class provides a flexible and expressive framework for building complex data
 * transformation and query pipelines for Firestore.
 *
 * A pipeline takes data sources such as Firestore collections, collection groups, or even in-memory
 * data, and applies a series of operations that are chained together, each operation takes the
 * output from the last operation (or the data source) and produces an output for the next operation
 * (or as the final output of the pipeline).
 *
 * NOTE: the chained operations are not a prescription of exactly how Firestore will execute the
 * pipeline, instead Firestore only guarantee the result is the same as if the chained operations
 * are executed in order.
 *
 * Usage Examples:
 *
 * **1. Projecting Specific Fields and Renaming:**
 *
 * ```java
 * Pipeline pipeline = Pipeline.fromCollection("users")
 *     // Select 'name' and 'email' fields, create 'userAge' which is renamed from field 'age'.
 *     .project(Fields.of("name", "email"), Field.of("age").asAlias("userAge"))
 * ```
 *
 * **2. Filtering and Sorting:**
 *
 * ```java
 * Pipeline pipeline = Pipeline.fromCollectionGroup("reviews")
 *     .filter(Field.of("rating").greaterThan(Expr.Constant.of(3))) // High ratings
 *     .sort(Ordering.of("timestamp").descending());
 * ```
 *
 * **3. Aggregation with Grouping:**
 *
 * ```java
 * Pipeline pipeline = Pipeline.fromCollection("orders")
 *     .group(Field.of("customerId"))
 *     .aggregate(count(Field.of("orderId")).asAlias("orderCount"));
 * ```
 */
class Pipeline private constructor(private val stages: List<Stage>, private val name: String) {

  private constructor(collection: Collection) : this(listOf(collection), collection.relativePath)

  private constructor(group: CollectionGroup) : this(listOf(group), group.collectionId)

  private constructor(db: Database) : this(listOf(db), db.name)

  private constructor(docs: Documents) : this(listOf(docs), docs.name)

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
    fun fromCollectionGroup(collectionId: String): Pipeline {
      Preconditions.checkArgument(
        !collectionId.contains("/"),
        "Invalid collectionId '%s'. Collection IDs must not contain '/'.",
        collectionId,
      )
      return Pipeline(CollectionGroup(collectionId))
    }

    @JvmStatic
    fun fromDatabase(): Pipeline {
      return Pipeline(Database())
    }

    @JvmStatic
    fun fromDocuments(vararg docs: DocumentReference): Pipeline {
      return Pipeline(Documents.of(*docs))
    }
  }

  private fun projectablesToMap(vararg selectables: Selectable): Map<String, Expr> {
    val projMap = mutableMapOf<String, Expr>()
    for (proj in selectables) {
      when (proj) {
        is Field -> projMap[proj.path.encodedPath] = proj
        is AggregatorTarget -> projMap[proj.fieldName] = proj.accumulator
        is Fields -> proj.fs?.forEach { projMap[it.path.encodedPath] = it }
      }
    }
    return projMap
  }

  private fun fieldNamesToMap(vararg fields: String): Map<String, Expr> {
    val projMap = mutableMapOf<String, Expr>()
    for (field in fields) {
      projMap[field] = Field.of(field)
    }
    return projMap
  }

  fun addFields(vararg fields: Selectable): Pipeline {
    return Pipeline(stages.plus(AddFields(projectablesToMap(*fields))), name)
  }

  fun select(vararg projections: Selectable): Pipeline {
    return Pipeline(stages.plus(Select(projectablesToMap(*projections))), name)
  }

  fun select(vararg fields: String): Pipeline {
    return Pipeline(stages.plus(Select(fieldNamesToMap(*fields))), name)
  }

  fun <T> filter(condition: T): Pipeline where T : Expr, T : Function.FilterCondition {
    return Pipeline(stages.plus(Filter(condition)), name)
  }

  fun offset(offset: Int): Pipeline {
    return Pipeline(stages.plus(Offset(offset)), name)
  }

  fun limit(limit: Int): Pipeline {
    return Pipeline(stages.plus(Limit(limit)), name)
  }

  fun unionWith(pipeline: Pipeline, distinct: Boolean): Pipeline {
    return Pipeline(stages.plus(UnionWith(pipeline, distinct)), name)
  }

  fun group(fields: Map<Field, Expr>, accumulators: Map<Field, Expr>): Pipeline {
    return Pipeline(stages.plus(Group(fields, accumulators)), name)
  }

  fun group(vararg by: Fields): GroupingPipeline {
    // operations.add(Group(fields, accumulators))
    return GroupingPipeline(this /*TODO*/)
  }

  fun group(vararg by: Selectable): GroupingPipeline {
    // operations.add(Group(fields, accumulators))
    return GroupingPipeline(this /*TODO*/)
  }

  fun aggregate(vararg aggregators: AggregatorTarget): Pipeline {
    return Pipeline(stages.plus(Aggregate(*aggregators)), name)
  }

  fun findNearest(
    property: Field,
    vector: DoubleArray,
    options: FindNearest.FindNearestOptions,
  ): Pipeline {
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

  @JvmOverloads
  fun replaceMap(field: Field, mode: ReplaceMap.Mode = ReplaceMap.Mode.FULL_REPLACE): Pipeline {
    return Pipeline(stages.plus(ReplaceMap(mode, field)), name)
  }

  fun unnestArray(field: Field): Pipeline {
    return Pipeline(stages.plus(UnnestArray(field)), name)
  }

  fun sort(
    orders: List<Ordering>,
    density: Sort.Density = Sort.Density.UNSPECIFIED,
    truncation: Sort.Truncation = Sort.Truncation.UNSPECIFIED,
  ): Pipeline {
    return Pipeline(stages.plus(Sort(orders, density, truncation)), name)
  }

  // Sugar
  fun sort(vararg orders: Ordering): Pipeline {
    return this.sort(orders.toList())
  }

  fun paginate(pageSize: Int, vararg orders: Ordering): PaginatingPipeline {
    return PaginatingPipeline(this, pageSize, orders.toList())
  }

  fun genericStage(name: String, params: Map<String, Any>? = null): Pipeline {
    return this
  }

  fun execute(db: Firestore): ApiFuture<List<PipelineResult>> {
    when (db) {
      is FirestoreImpl -> {
        val pipelineValue = toProto()
        val request =
          ExecutePipelineRequest.newBuilder()
            .setDatabase(db.resourcePath.databaseName.toString())
            .setStructuredPipeline(
              StructuredPipeline.newBuilder().setPipeline(pipelineValue.pipelineValue).build()
            )
            .build()

        val futureResult = SettableApiFuture.create<List<PipelineResult>>()
        pipelineInternalStream(
          db,
          request,
          object : PipelineResultObserver() {
            val results = mutableListOf<PipelineResult>()

            override fun onCompleted() {
              futureResult.set(results)
            }

            override fun onNext(result: PipelineResult?) {
              results.add(result!!)
            }

            override fun onError(t: Throwable?) {
              futureResult.setException(t)
            }
          },
        )

        return futureResult
      }
      else -> {
        TODO()
      }
    }
  }

  fun execute(db: Firestore, observer: ApiStreamObserver<PipelineResult>): Unit {
    when (db) {
      is FirestoreImpl -> {
        val pipelineValue = toProto()
        val request =
          ExecutePipelineRequest.newBuilder()
            .setDatabase(db.resourcePath.databaseName.toString())
            .setStructuredPipeline(
              StructuredPipeline.newBuilder().setPipeline(pipelineValue.pipelineValue).build()
            )
            .build()

        pipelineInternalStream(
          db,
          request,
          object : PipelineResultObserver() {
            override fun onCompleted() {
              observer.onCompleted()
            }

            override fun onNext(result: PipelineResult?) {
              observer.onNext(result)
            }

            override fun onError(t: Throwable?) {
              observer.onError(t)
            }
          },
        )
      }
      else -> {
        TODO()
      }
    }
  }

  internal fun toProto(): Value {
    return Value.newBuilder()
      .setPipelineValue(
        com.google.firestore.v1.Pipeline.newBuilder().addAllStages(stages.map { toStageProto(it) })
      )
      .build()
  }
}

internal fun encodeValue(value: Any?): Value? {
  return UserDataConverter.encodeValue(FieldPath.empty(), value, UserDataConverter.ARGUMENT)
}

private abstract class PipelineResultObserver : ApiStreamObserver<PipelineResult?> {
  var executionTime: Timestamp? = null
    private set

  fun onCompleted(executionTime: Timestamp?) {
    this.executionTime = executionTime
    this.onCompleted()
  }
}

private fun pipelineInternalStream(
  rpcContext: FirestoreImpl,
  request: ExecutePipelineRequest,
  resultObserver: PipelineResultObserver,
) {
  val observer: ResponseObserver<ExecutePipelineResponse> =
    object : ResponseObserver<ExecutePipelineResponse> {
      var executionTime: Timestamp? = null
      var firstResponse: Boolean = false
      var numDocuments: Int = 0

      // The stream's `onComplete()` could be called more than once,
      // this flag makes sure only the first one is actually processed.
      var hasCompleted: Boolean = false

      override fun onStart(streamController: StreamController) {}

      override fun onResponse(response: ExecutePipelineResponse) {
        if (!firstResponse) {
          firstResponse = true
          Tracing.getTracer().currentSpan.addAnnotation("Firestore.Query: First response")
        }
        if (response.resultsCount > 0) {
          numDocuments += response.resultsCount
          if (numDocuments % 100 == 0) {
            Tracing.getTracer().currentSpan.addAnnotation("Firestore.Query: Received 100 documents")
          }
          response.resultsList.forEach { doc: Document ->
            resultObserver.onNext(
              PipelineResult.fromDocument(rpcContext, response.executionTime, doc)
            )
          }
        }

        if (executionTime == null) {
          executionTime = Timestamp.fromProto(response.executionTime)
        }
      }

      override fun onError(throwable: Throwable) {
        Tracing.getTracer().currentSpan.addAnnotation("Firestore.Query: Error")
        resultObserver.onError(throwable)
      }

      override fun onComplete() {
        if (hasCompleted) {
          return
        }
        hasCompleted = true

        Tracing.getTracer()
          .currentSpan
          .addAnnotation(
            "Firestore.Query: Completed",
            ImmutableMap.of(
              "numDocuments",
              AttributeValue.longAttributeValue(numDocuments.toLong()),
            ),
          )
        resultObserver.onCompleted(executionTime)
      }
    }

  Logger.getLogger("Pipeline").log(Level.WARNING, "Sending request: $request")

  rpcContext.streamRequest(request, observer, rpcContext.client.executePipelineCallable())
}
