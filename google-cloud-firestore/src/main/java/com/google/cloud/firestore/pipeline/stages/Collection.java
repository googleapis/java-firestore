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
import com.google.firestore.v1.Value;
import java.util.Collections;
import javax.annotation.Nonnull;

@InternalApi
public final class Collection extends Stage {

  @Nonnull private final String path;

  public Collection(@Nonnull String path, CollectionOptions options) {
    super("collection", options.options);
    if (!path.startsWith("/")) {
      this.path = "/" + path;
    } else {
      this.path = path;
    }
  }

  public Collection withOptions(CollectionOptions options) {
    return new Collection(path, options);
  }

  @Override
  Iterable<Value> toStageArgs() {
    return Collections.singleton(Value.newBuilder().setReferenceValue(path).build());
  }
}
