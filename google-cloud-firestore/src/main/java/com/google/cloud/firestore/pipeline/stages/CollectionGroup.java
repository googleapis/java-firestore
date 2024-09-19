/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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