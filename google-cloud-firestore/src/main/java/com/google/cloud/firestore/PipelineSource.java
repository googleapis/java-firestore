package com.google.cloud.firestore;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.stages.Collection;
import com.google.cloud.firestore.pipeline.stages.CollectionGroup;
import com.google.cloud.firestore.pipeline.stages.Database;
import com.google.cloud.firestore.pipeline.stages.Documents;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;

@BetaApi
public class PipelineSource {
  private final Firestore db;

  @InternalApi
  PipelineSource(Firestore db) {
    this.db = db;
  }

  @Nonnull
  @BetaApi
  public Pipeline collection(@Nonnull String path) {
    return new Pipeline(this.db, new Collection(path));
  }

  @Nonnull
  @BetaApi
  public Pipeline collectionGroup(@Nonnull String collectionId) {
    Preconditions.checkArgument(
        !collectionId.contains("/"),
        "Invalid collectionId '%s'. Collection IDs must not contain '/'.",
        collectionId);
    return new Pipeline(this.db, new CollectionGroup(collectionId));
  }

  @Nonnull
  @BetaApi
  public Pipeline database() {
    return new Pipeline(this.db, new Database());
  }

  @Nonnull
  @BetaApi
  public Pipeline documents(DocumentReference... docs) {
    return new Pipeline(this.db, Documents.of(docs));
  }
}
