package com.google.cloud.firestore.pipeline.stages;

import static com.google.cloud.firestore.PipelineUtils.encodeValue;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.PipelineUtils;
import com.google.cloud.firestore.pipeline.expressions.Ordering;
import com.google.firestore.v1.Value;
import java.util.stream.Collectors;

@InternalApi
public final class StageUtils {
  @InternalApi
  public static com.google.firestore.v1.Pipeline.Stage toStageProto(Stage stage) {

    if (stage instanceof Collection) {
      Collection collectionStage = (Collection) stage;
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(collectionStage.getName())
          .addArgs(Value.newBuilder().setReferenceValue(collectionStage.getPath()).build())
          .build();
    } else if (stage instanceof CollectionGroup) {
      CollectionGroup collectionGroupStage = (CollectionGroup) stage;
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(collectionGroupStage.getName())
          .addArgs(Value.newBuilder().setReferenceValue("").build())
          .addArgs(encodeValue(collectionGroupStage.getCollectionId()))
          .build();
    } else if (stage instanceof Database) {
      Database databaseStage = (Database) stage;
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(databaseStage.getName())
          .build();
    } else if (stage instanceof Documents) {
      Documents documentsStage = (Documents) stage;
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(documentsStage.getName())
          .addAllArgs(
              documentsStage.getDocuments().stream()
                  .map(doc -> Value.newBuilder().setReferenceValue(doc).build())
                  .collect(Collectors.toList()))
          .build();
    } else if (stage instanceof Select) {
      Select selectStage = (Select) stage;
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(selectStage.getName())
          .addArgs(encodeValue(selectStage.getProjections()))
          .build();
    } else if (stage instanceof AddFields) {
      AddFields addFieldsStage = (AddFields) stage;
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(addFieldsStage.getName())
          .addArgs(encodeValue(addFieldsStage.getFields()))
          .build();
    } else if (stage instanceof Filter) {
      Filter filterStage = (Filter) stage; // Use wildcard for generic type
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(filterStage.getName())
          .addArgs(encodeValue(filterStage.getCondition()))
          .build();
    } else if (stage instanceof Sort) {
      Sort sortStage = (Sort) stage;
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(sortStage.getName())
          .addAllArgs(
              sortStage.getOrders().stream().map(Ordering::toProto).collect(Collectors.toList()))
          .putOptions("density", encodeValue(sortStage.getDensity().toString()))
          .putOptions("truncation", encodeValue(sortStage.getTruncation().toString()))
          .build();
    } else if (stage instanceof Offset) {
      Offset offsetStage = (Offset) stage;
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(offsetStage.getName())
          .addArgs(encodeValue(offsetStage.getOffset()))
          .build();
    } else if (stage instanceof Limit) {
      Limit limitStage = (Limit) stage;
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(limitStage.getName())
          .addArgs(encodeValue(limitStage.getLimit()))
          .build();
    } else if (stage instanceof Aggregate) {
      Aggregate aggregateStage = (Aggregate) stage;
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(aggregateStage.getName())
          .addArgs(encodeValue(aggregateStage.getGroups()))
          .addArgs(encodeValue(aggregateStage.getAccumulators()))
          .build();
    } else if (stage instanceof FindNearest) {
      FindNearest findNearestStage = (FindNearest) stage;
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(findNearestStage.getName())
          .addArgs(encodeValue(findNearestStage.getProperty()))
          .addArgs(encodeValue(findNearestStage.getVector()))
          .addArgs(encodeValue(findNearestStage.getOptions().getDistanceMeasure().toProtoString()))
          .putOptions("limit", encodeValue(findNearestStage.getOptions().getLimit()))
          .putOptions(
              "distance_field", encodeValue(findNearestStage.getOptions().getDistanceField()))
          .build();
    } else if (stage instanceof GenericStage) {
      GenericStage genericStage = (GenericStage) stage;
      return com.google.firestore.v1.Pipeline.Stage.newBuilder()
          .setName(genericStage.getName())
          .addAllArgs(
              genericStage.getParams().stream()
                  .map(PipelineUtils::encodeValue)
                  .collect(Collectors.toList()))
          .build();
    } else {
      // Handle the else case appropriately (e.g., throw an exception)
      throw new IllegalArgumentException("Unknown stage type: " + stage.getClass());
    }
  }
}
