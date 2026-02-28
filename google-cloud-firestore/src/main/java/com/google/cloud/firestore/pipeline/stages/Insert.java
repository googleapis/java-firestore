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
public final class Insert extends Stage {

  @Nullable private final String path;

  private Insert(@Nullable String path, InternalOptions options) {
    super("insert", options);
    this.path = path;
  }

  @BetaApi
  public Insert() {
    this(null, InternalOptions.EMPTY);
  }

  @BetaApi
  public static Insert withCollection(CollectionReference target) {
    String path = target.getPath();
    return new Insert(path.startsWith("/") ? path : "/" + path, InternalOptions.EMPTY);
  }

  @BetaApi
  public Insert withOptions(InsertOptions options) {
    return new Insert(path, this.options.adding(options));
  }

  @BetaApi
  public Insert withReturns(InsertReturn returns) {
    return new Insert(
        path, this.options.with("returns", PipelineUtils.encodeValue(returns.getValue())));
  }

  @BetaApi
  public Insert withTransformations(Selectable... transformations) {
    return new Insert(
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
