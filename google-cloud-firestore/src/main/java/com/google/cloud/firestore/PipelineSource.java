package com.google.cloud.firestore;

import com.google.cloud.firestore.pipeline.stages.Collection;
import com.google.cloud.firestore.pipeline.stages.CollectionGroup;
import com.google.cloud.firestore.pipeline.stages.Database;
import com.google.cloud.firestore.pipeline.stages.Documents;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;

public class PipelineSource {

  @Nonnull
  public Pipeline collection(@Nonnull String path) {
    return new Pipeline(new Collection(path));
  }

  @Nonnull
  public Pipeline collectionGroup(@Nonnull String collectionId) {
    Preconditions.checkArgument(
        !collectionId.contains("/"),
        "Invalid collectionId '%s'. Collection IDs must not contain '/'.",
        collectionId);
    return new Pipeline(new CollectionGroup(collectionId));
  }

  @Nonnull
  public Pipeline database() {
    return new Pipeline(new Database());
  }

  @Nonnull
  public Pipeline documents(DocumentReference... docs) {
    return new Pipeline(Documents.of(docs));
  }
}
