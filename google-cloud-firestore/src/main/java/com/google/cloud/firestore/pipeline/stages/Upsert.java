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

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.PipelineUtils;
import com.google.cloud.firestore.pipeline.expressions.Selectable;
import com.google.firestore.v1.Value;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

@InternalApi
public final class Upsert extends Stage {

  @Nullable private final String path;

  private Upsert(@Nullable String path, InternalOptions options) {
    super("upsert", options);
    this.path = path;
  }

  @BetaApi
  public Upsert() {
    this(null, InternalOptions.EMPTY);
  }

  @BetaApi
  public static Upsert withCollection(CollectionReference target) {
    String path = target.getPath();
    return new Upsert(path.startsWith("/") ? path : "/" + path, InternalOptions.EMPTY);
  }

  @BetaApi
  public Upsert withOptions(UpsertOptions options) {
    return new Upsert(path, this.options.adding(options));
  }

  @BetaApi
  public Upsert withReturns(UpsertReturn returns) {
    return new Upsert(
        path, this.options.with("returns", PipelineUtils.encodeValue(returns.getValue())));
  }

  @BetaApi
  public Upsert withTransformations(Selectable... transformations) {
    return new Upsert(
        path,
        this.options.with(
            "transformations",
            PipelineUtils.encodeValue(PipelineUtils.selectablesToMap(transformations))));
  }

  @Override
  Iterable<Value> toStageArgs() {
    List<Value> args = new ArrayList<>();
    if (path != null) {
      args.add(Value.newBuilder().setReferenceValue(path).build());
    }
    return args;
  }
}
