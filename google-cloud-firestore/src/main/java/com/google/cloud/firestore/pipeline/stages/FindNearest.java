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

import static com.google.cloud.firestore.PipelineUtils.encodeValue;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.expressions.Expr;
import com.google.firestore.v1.Pipeline;

@BetaApi
public final class FindNearest extends AbstractStage {

  public interface DistanceMeasure {

    enum Type {
      EUCLIDEAN,
      COSINE,
      DOT_PRODUCT
    }

    static DistanceMeasure euclidean() {
      return new EuclideanDistanceMeasure();
    }

    static DistanceMeasure cosine() {
      return new CosineDistanceMeasure();
    }

    static DistanceMeasure dotProduct() {
      return new DotProductDistanceMeasure();
    }

    static DistanceMeasure generic(String name) {
      return new GenericDistanceMeasure(name);
    }

    @InternalApi
    String toProtoString();
  }

  public static class EuclideanDistanceMeasure implements DistanceMeasure {

    @Override
    @InternalApi
    public String toProtoString() {
      return "euclidean";
    }
  }

  public static class CosineDistanceMeasure implements DistanceMeasure {

    @Override
    @InternalApi
    public String toProtoString() {
      return "cosine";
    }
  }

  public static class DotProductDistanceMeasure implements DistanceMeasure {

    @Override
    @InternalApi
    public String toProtoString() {
      return "dot_product";
    }
  }

  public static class GenericDistanceMeasure implements DistanceMeasure {

    String name;

    public GenericDistanceMeasure(String name) {
      this.name = name;
    }

    @Override
    @InternalApi
    public String toProtoString() {
      return name;
    }
  }

  private static final String name = "find_nearest";
  private final Expr property;
  private final double[] vector;
  private final DistanceMeasure distanceMeasure;
  private final FindNearestOptions options;

  @InternalApi
  public FindNearest(
      Expr property, double[] vector, DistanceMeasure distanceMeasure, FindNearestOptions options) {
    this.property = property;
    this.vector = vector;
    this.distanceMeasure = distanceMeasure;
    this.options = options;
  }

  @Override
  @InternalApi
  public String getName() {
    return name;
  }

  @InternalApi
  public Expr getProperty() {
    return property;
  }

  @InternalApi
  public double[] getVector() {
    return vector;
  }

  @InternalApi
  public DistanceMeasure getDistanceMeasure() {
    return distanceMeasure;
  }

  @InternalApi
  public FindNearestOptions getOptions() {
    return options;
  }

  @Override
  Pipeline.Stage toStageProto() {
    return Pipeline.Stage.newBuilder()
        .setName(name)
        .addArgs(encodeValue(property))
        .addArgs(encodeValue(vector))
        .addArgs(encodeValue(distanceMeasure.toProtoString()))
        .putOptions("limit", encodeValue(options.getLimit()))
        .putOptions("distance_field", encodeValue(options.getDistanceField()))
        .build();
  }
}
