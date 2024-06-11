package com.google.cloud.firestore;

import com.google.api.core.ApiFuture;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.pipeline.PaginatingPipeline;
import com.google.cloud.firestore.pipeline.expressions.AggregatorTarget;
import com.google.cloud.firestore.pipeline.expressions.Expr;
import com.google.cloud.firestore.pipeline.expressions.Field;
import com.google.cloud.firestore.pipeline.expressions.Fields;
import com.google.cloud.firestore.pipeline.expressions.FilterCondition;
import com.google.cloud.firestore.pipeline.expressions.Ordering;
import com.google.cloud.firestore.pipeline.expressions.Selectable;
import com.google.cloud.firestore.pipeline.stages.AddFields;
import com.google.cloud.firestore.pipeline.stages.Aggregate;
import com.google.cloud.firestore.pipeline.stages.Collection;
import com.google.cloud.firestore.pipeline.stages.CollectionGroup;
import com.google.cloud.firestore.pipeline.stages.Database;
import com.google.cloud.firestore.pipeline.stages.Documents;
import com.google.cloud.firestore.pipeline.stages.FindNearest;
import com.google.cloud.firestore.pipeline.stages.GenericStage;
import com.google.cloud.firestore.pipeline.stages.Limit;
import com.google.cloud.firestore.pipeline.stages.Offset;
import com.google.cloud.firestore.pipeline.stages.Select;
import com.google.cloud.firestore.pipeline.stages.Sort;
import com.google.cloud.firestore.pipeline.stages.Stage;
import com.google.cloud.firestore.pipeline.stages.StageUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.firestore.v1.Document;
import com.google.firestore.v1.ExecutePipelineRequest;
import com.google.firestore.v1.ExecutePipelineResponse;
import com.google.firestore.v1.StructuredPipeline;
import com.google.firestore.v1.Value;
import io.opencensus.trace.AttributeValue;
import io.opencensus.trace.Tracing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The Pipeline class provides a flexible and expressive framework for building complex data
 * transformation and query pipelines for Firestore.
 *
 * <p>A pipeline takes data sources such as Firestore collections, collection groups, or even
 * in-memory data, and applies a series of operations that are chained together, each operation
 * takes the output from the last operation (or the data source) and produces an output for the next
 * operation (or as the final output of the pipeline).
 *
 * <p>NOTE: the chained operations are not a prescription of exactly how Firestore will execute the
 * pipeline, instead Firestore only guarantee the result is the same as if the chained operations
 * are executed in order.
 *
 * <p>Usage Examples:
 *
 * <p>**1. Projecting Specific Fields and Renaming:**
 *
 * <p>```java Pipeline pipeline = Pipeline.fromCollection("users") // Select 'name' and 'email'
 * fields, create 'userAge' which is renamed from field 'age'. .project(Fields.of("name", "email"),
 * Field.of("age").asAlias("userAge")) ```
 *
 * <p>**2. Filtering and Sorting:**
 *
 * <p>```java Pipeline pipeline = Pipeline.fromCollectionGroup("reviews")
 * .filter(Field.of("rating").greaterThan(Expr.Constant.of(3))) // High ratings
 * .sort(Ordering.of("timestamp").descending()); ```
 *
 * <p>**3. Aggregation with Grouping:**
 *
 * <p>```java Pipeline pipeline = Pipeline.fromCollection("orders") .group(Field.of("customerId"))
 * .aggregate(count(Field.of("orderId")).asAlias("orderCount")); ```
 */
public final class Pipeline {
  private final ImmutableList<Stage> stages;
  private final String name;

  private Pipeline(List<Stage> stages, String name) {
    this.stages = ImmutableList.copyOf(stages);
    this.name = name;
  }

  private Pipeline(Collection collection) {
    this(Lists.newArrayList(collection), collection.getPath());
  }

  private Pipeline(CollectionGroup group) {
    this(Lists.newArrayList(group), group.getCollectionId());
  }

  private Pipeline(Database db) {
    this(Lists.newArrayList(db), db.getName());
  }

  private Pipeline(Documents docs) {
    this(Lists.newArrayList(docs), docs.getName());
  }

  public static Pipeline from(CollectionReference source) {
    return new Pipeline(new Collection(source.getPath()));
  }

  public static Pipeline from(com.google.cloud.firestore.CollectionGroup source) {
    return new Pipeline(new CollectionGroup(source.options.getCollectionId()));
  }

  public static Pipeline fromCollection(String collectionName) {
    return new Pipeline(new Collection(collectionName));
  }

  public static Pipeline fromCollectionGroup(String collectionId) {
    Preconditions.checkArgument(
        !collectionId.contains("/"),
        "Invalid collectionId '%s'. Collection IDs must not contain '/'.",
        collectionId);
    return new Pipeline(new CollectionGroup(collectionId));
  }

  public static Pipeline fromDatabase() {
    return new Pipeline(new Database());
  }

  public static Pipeline fromDocuments(DocumentReference... docs) {
    return new Pipeline(Documents.of(docs));
  }

  private Map<String, Expr> projectablesToMap(Selectable... selectables) {
    Map<String, Expr> projMap = new HashMap<>();
    for (Selectable proj : selectables) {
      if (proj instanceof Field) {
        Field fieldProj = (Field) proj;
        projMap.put(fieldProj.getPath().getEncodedPath(), fieldProj);
      } else if (proj instanceof AggregatorTarget) {
        AggregatorTarget aggregatorProj = (AggregatorTarget) proj;
        projMap.put(aggregatorProj.getFieldName(), aggregatorProj.getAccumulator());
      } else if (proj instanceof Fields) {
        Fields fieldsProj = (Fields) proj;
        if (fieldsProj.getFields() != null) {
          fieldsProj.getFields().forEach(f -> projMap.put(f.getPath().getEncodedPath(), f));
        }
      }
    }
    return projMap;
  }

  private Map<String, Expr> fieldNamesToMap(String... fields) {
    Map<String, Expr> projMap = new HashMap<>();
    for (String field : fields) {
      projMap.put(field, Field.of(field));
    }
    return projMap;
  }

  public Pipeline addFields(Selectable... fields) {
    return new Pipeline(
        ImmutableList.<Stage>builder()
            .addAll(stages)
            .add(new AddFields(projectablesToMap(fields)))
            .build(),
        name);
  }

  public Pipeline select(Selectable... projections) {
    return new Pipeline(
        ImmutableList.<Stage>builder()
            .addAll(stages)
            .add(new Select(projectablesToMap(projections)))
            .build(),
        name);
  }

  public Pipeline select(String... fields) {
    return new Pipeline(
        ImmutableList.<Stage>builder()
            .addAll(stages)
            .add(new Select(fieldNamesToMap(fields)))
            .build(),
        name);
  }

  public Pipeline filter(FilterCondition condition) {
    return new Pipeline(
        ImmutableList.<Stage>builder()
            .addAll(stages)
            .add(new com.google.cloud.firestore.pipeline.stages.Filter(condition))
            .build(),
        name);
  }

  public Pipeline offset(int offset) {
    return new Pipeline(
        ImmutableList.<Stage>builder().addAll(stages).add(new Offset(offset)).build(), name);
  }

  public Pipeline limit(int limit) {
    return new Pipeline(
        ImmutableList.<Stage>builder().addAll(stages).add(new Limit(limit)).build(), name);
  }

  public Pipeline aggregate(AggregatorTarget... aggregators) {
    return new Pipeline(
        ImmutableList.<Stage>builder().addAll(stages).add(new Aggregate(aggregators)).build(),
        name);
  }

  public Pipeline findNearest(
      String fieldName, double[] vector, FindNearest.FindNearestOptions options) {
    return findNearest(Field.of(fieldName), vector, options);
  }

  public Pipeline findNearest(
      Field property, double[] vector, FindNearest.FindNearestOptions options) {
    // Implementation for findNearest (add the FindNearest stage if needed)
    return new Pipeline(
        ImmutableList.<Stage>builder()
            .addAll(stages)
            .add(
                new FindNearest(
                    property, vector, options)) // Assuming FindNearest takes these arguments
            .build(),
        name);
  }

  public Pipeline sort(List<Ordering> orders, Sort.Density density, Sort.Truncation truncation) {
    return new Pipeline(
        ImmutableList.<Stage>builder()
            .addAll(stages)
            .add(new Sort(orders, density, truncation))
            .build(),
        name);
  }

  // Sugar
  public Pipeline sort(Ordering... orders) {
    return sort(Arrays.asList(orders), Sort.Density.UNSPECIFIED, Sort.Truncation.UNSPECIFIED);
  }

  public PaginatingPipeline paginate(int pageSize, Ordering... orders) {
    return new PaginatingPipeline(this, pageSize, Arrays.asList(orders));
  }

  public Pipeline genericStage(String name, Map<String, Object> params) {
    // Implementation for genericStage (add the GenericStage if needed)
    return new Pipeline(
        ImmutableList.<Stage>builder()
            .addAll(stages)
            .add(
                new GenericStage(
                    name,
                    Lists.newArrayList(
                        params.values()))) // Assuming GenericStage takes a list of params
            .build(),
        name);
  }

  public ApiFuture<List<PipelineResult>> execute(Firestore db) {
    if (db instanceof FirestoreImpl) {
      FirestoreImpl firestoreImpl = (FirestoreImpl) db;
      Value pipelineValue = toProto();
      ExecutePipelineRequest request =
          ExecutePipelineRequest.newBuilder()
              .setDatabase(firestoreImpl.getResourcePath().getDatabaseName().toString())
              .setStructuredPipeline(
                  StructuredPipeline.newBuilder()
                      .setPipeline(pipelineValue.getPipelineValue())
                      .build())
              .build();

      SettableApiFuture<List<PipelineResult>> futureResult = SettableApiFuture.create();

      pipelineInternalStream( // Assuming you have this method
          firestoreImpl,
          request,
          new PipelineResultObserver() {
            final List<PipelineResult> results = new ArrayList<>();

            @Override
            public void onCompleted() {
              futureResult.set(results);
            }

            @Override
            public void onNext(PipelineResult result) {
              results.add(result);
            }

            @Override
            public void onError(Throwable t) {
              futureResult.setException(t);
            }
          });

      return futureResult;
    } else {
      // Handle unsupported Firestore types
      throw new IllegalArgumentException("Unsupported Firestore type");
    }
  }

  public void execute(Firestore db, ApiStreamObserver<PipelineResult> observer) {
    if (db instanceof FirestoreImpl) {
      FirestoreImpl firestoreImpl = (FirestoreImpl) db;
      Value pipelineValue = toProto();
      ExecutePipelineRequest request =
          ExecutePipelineRequest.newBuilder()
              .setDatabase(firestoreImpl.getResourcePath().getDatabaseName().toString())
              .setStructuredPipeline(
                  StructuredPipeline.newBuilder()
                      .setPipeline(pipelineValue.getPipelineValue())
                      .build())
              .build();

      pipelineInternalStream(
          firestoreImpl,
          request,
          new PipelineResultObserver() {
            @Override
            public void onCompleted() {
              observer.onCompleted();
            }

            @Override
            public void onNext(PipelineResult result) {
              observer.onNext(result);
            }

            @Override
            public void onError(Throwable t) {
              observer.onError(t);
            }
          });
    } else {
      // Handle unsupported Firestore types
      throw new IllegalArgumentException("Unsupported Firestore type");
    }
  }

  public Value toProto() {
    return Value.newBuilder()
        .setPipelineValue(
            com.google.firestore.v1.Pipeline.newBuilder()
                .addAllStages(
                    stages.stream()
                        .map(StageUtils::toStageProto)
                        .collect(Collectors.toList())) // Use the static method
            )
        .build();
  }

  private void pipelineInternalStream(
      FirestoreImpl rpcContext,
      ExecutePipelineRequest request,
      PipelineResultObserver resultObserver) {
    ResponseObserver<ExecutePipelineResponse> observer =
        new ResponseObserver<ExecutePipelineResponse>() {
          Timestamp executionTime = null;
          boolean firstResponse = false;
          int numDocuments = 0;
          boolean hasCompleted = false;

          @Override
          public void onStart(StreamController controller) {
            // No action needed in onStart
          }

          @Override
          public void onResponse(ExecutePipelineResponse response) {
            if (!firstResponse) {
              firstResponse = true;
              Tracing.getTracer()
                  .getCurrentSpan()
                  .addAnnotation(
                      "Firestore.Query: First response"); // Assuming Tracing class exists
            }
            if (response.getResultsCount() > 0) {
              numDocuments += response.getResultsCount();
              if (numDocuments % 100 == 0) {
                Tracing.getTracer()
                    .getCurrentSpan()
                    .addAnnotation("Firestore.Query: Received 100 documents");
              }
              for (Document doc : response.getResultsList()) {
                resultObserver.onNext(
                    PipelineResult.fromDocument(
                        rpcContext, Timestamp.fromProto(response.getExecutionTime()), doc));
              }
            }

            if (executionTime == null) {
              executionTime = Timestamp.fromProto(response.getExecutionTime());
            }
          }

          @Override
          public void onError(Throwable throwable) {
            Tracing.getTracer().getCurrentSpan().addAnnotation("Firestore.Query: Error");
            resultObserver.onError(throwable);
          }

          @Override
          public void onComplete() {
            if (hasCompleted) {
              return;
            }
            hasCompleted = true;

            Tracing.getTracer()
                .getCurrentSpan()
                .addAnnotation(
                    "Firestore.ExecutePipeline: Completed",
                    ImmutableMap.of(
                        "numDocuments", AttributeValue.longAttributeValue((long) numDocuments)));
            resultObserver.onCompleted(executionTime);
          }
        };

    Logger.getLogger("Pipeline").log(Level.WARNING, "Sending request: " + request);

    rpcContext.streamRequest(request, observer, rpcContext.getClient().executePipelineCallable());
  }
}

abstract class PipelineResultObserver implements ApiStreamObserver<PipelineResult> {
  private Timestamp executionTime; // Remove optional since Java doesn't have it

  public void onCompleted(Timestamp executionTime) {
    this.executionTime = executionTime;
    this.onCompleted();
  }

  public Timestamp getExecutionTime() { // Add getter for executionTime
    return executionTime;
  }
}