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
import com.google.cloud.firestore.pipeline.expressions.Expression;
import com.google.cloud.firestore.pipeline.expressions.Selectable;
import com.google.firestore.v1.Value;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

@BetaApi
public final class Insert extends Stage {

  private Insert(InternalOptions options) {
    super("insert", options);
  }

  @BetaApi
  public Insert() {
    this(InternalOptions.EMPTY);
  }

  @BetaApi
  public Insert withCollection(CollectionReference target) {
    String path = target.getPath();
    String normalizedPath = path.startsWith("/") ? path : "/" + path;
    return new Insert(
        this.options.with(
            "collection", Value.newBuilder().setReferenceValue(normalizedPath).build()));
  }

  @BetaApi
  public Insert withIdExpression(Expression idExpr) {
    return new Insert(this.options.with("document_id", PipelineUtils.encodeValue(idExpr)));
  }

  @Override
  Iterable<Value> toStageArgs() {
    return java.util.Collections.emptyList();
  }
}
