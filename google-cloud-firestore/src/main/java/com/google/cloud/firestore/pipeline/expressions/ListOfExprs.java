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

package com.google.cloud.firestore.pipeline.expressions;

import com.google.api.core.InternalApi;
import com.google.common.collect.ImmutableList;
import com.google.firestore.v1.ArrayValue;
import com.google.firestore.v1.Value;
import java.util.List;
import java.util.stream.Collectors;

@InternalApi
public final class ListOfExprs extends Expr {
  private final ImmutableList<Expr> conditions;

  @InternalApi
  ListOfExprs(List<Expr> list) {
    this.conditions = ImmutableList.copyOf(list);
  }

  @InternalApi
  public List<Expr> getConditions() {
    return conditions;
  }

  @Override
  Value toProto() {
    Value.Builder builder = Value.newBuilder();
    ArrayValue.Builder arrayBuilder = builder.getArrayValueBuilder();
    for (Expr condition : conditions) {
      arrayBuilder.addValues(condition.toProto());
    }
    return builder.build();
  }
}
