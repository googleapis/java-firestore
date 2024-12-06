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
import com.google.firestore.v1.Value;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@InternalApi
public final class FunctionUtils {
  @InternalApi
  public static Value exprToValue(Expr expr) {
    return (expr == null ? Constant.NULL : expr).toProto();
  }

  @InternalApi
  static List<Expr> toExprList(Object[] other) {
    return Arrays.stream(other)
        .map(obj -> (obj instanceof Expr) ? (Expr) obj : Constant.of(obj))
        .collect(Collectors.toList());
  }
}
