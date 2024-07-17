package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;

@InternalApi
public final class CollectionGroup implements Stage {

  private static final String name = "collection_group";
  private final String collectionId;

  @InternalApi
  public CollectionGroup(String collectionId) {
    this.collectionId = collectionId;
  }

  @InternalApi
  public String getCollectionId() {
    return collectionId;
  }

  @Override
  public String getName() {
    return name;
  }
}
