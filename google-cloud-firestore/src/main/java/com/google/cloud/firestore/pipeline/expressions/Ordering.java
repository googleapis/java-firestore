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

import static com.google.cloud.firestore.PipelineUtils.encodeValue;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.firestore.v1.MapValue;
import com.google.firestore.v1.Value;
import java.util.Locale;

@BetaApi
public final class Ordering {

  private final Expr expr;
  private Ordering.Direction dir = Ordering.Direction.ASCENDING;

  private Ordering(Expr expr, Ordering.Direction dir) {
    this.expr = expr;
    this.dir = dir;
  }

  @BetaApi
  public enum Direction {
    ASCENDING,
    DESCENDING;

    @Override
    public String toString() {
      return name().toLowerCase(Locale.getDefault());
    }
  }

  @InternalApi
  public Value toProto() {
    return Value.newBuilder()
        .setMapValue(
            MapValue.newBuilder()
                .putFields("direction", encodeValue(dir.toString()))
                .putFields("expression", encodeValue(expr))
                .build())
        .build();
  }

  @BetaApi
  public static Ordering ascending(Expr expr) {
    return new Ordering(expr, Direction.ASCENDING);
  }

  @BetaApi
  public static Ordering descending(Expr expr) {
    return new Ordering(expr, Direction.DESCENDING);
  }

  @InternalApi
  public Expr getExpr() {
    return expr;
  }

  @InternalApi
  public Ordering.Direction getDir() {
    return dir;
  }
}
