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
import com.google.cloud.firestore.pipeline.expressions.Expr;
import com.google.cloud.firestore.pipeline.expressions.Field;
import javax.annotation.Nullable;

public final class FindNearest implements Stage {

  private static final String name = "find_nearest";
  private final Expr property;
  private final double[] vector;
  private final FindNearest.FindNearestOptions options;

  @InternalApi
  public FindNearest(Expr property, double[] vector, FindNearest.FindNearestOptions options) {
    this.property = property;
    this.vector = vector;
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
  public FindNearestOptions getOptions() {
    return options;
  }

  // Nested interfaces and classes
  public interface DistanceMeasure {

    enum Type {
      EUCLIDEAN,
      COSINE,
      DOT_PRODUCT
    }

    static FindNearest.DistanceMeasure euclidean() {
      return new FindNearest.EuclideanDistanceMeasure();
    }

    static FindNearest.DistanceMeasure cosine() {
      return new FindNearest.CosineDistanceMeasure();
    }

    static FindNearest.DistanceMeasure dotProduct() {
      return new FindNearest.DotProductDistanceMeasure();
    }

    static FindNearest.DistanceMeasure generic(String name) {
      return new FindNearest.GenericDistanceMeasure(name);
    }

    @InternalApi
    String toProtoString();
  }

  static class EuclideanDistanceMeasure implements FindNearest.DistanceMeasure {

    @Override
    @InternalApi
    public String toProtoString() {
      return "euclidean";
    }
  }

  static class CosineDistanceMeasure implements FindNearest.DistanceMeasure {

    @Override
    @InternalApi
    public String toProtoString() {
      return "cosine";
    }
  }

  static class DotProductDistanceMeasure implements FindNearest.DistanceMeasure {

    @Override
    @InternalApi
    public String toProtoString() {
      return "dot_product";
    }
  }

  static class GenericDistanceMeasure implements FindNearest.DistanceMeasure {

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

  public static class FindNearestOptions {

    private final Long limit;
    private final FindNearest.DistanceMeasure distanceMeasure;

    @Nullable private final Field distanceField;

    private FindNearestOptions(Long limit, FindNearest.DistanceMeasure distanceMeasure) {
      this.limit = limit;
      this.distanceMeasure = distanceMeasure;
      this.distanceField = null;
    }

    private FindNearestOptions(
        Long limit, FindNearest.DistanceMeasure distanceMeasure, Field field) {
      this.limit = limit;
      this.distanceMeasure = distanceMeasure;
      this.distanceField = field;
    }

    public static FindNearest.FindNearestOptions withLimitAndMeasure(
        long limit, FindNearest.DistanceMeasure distanceMeasure) {
      return new FindNearest.FindNearestOptions(limit, distanceMeasure);
    }

    public FindNearest.FindNearestOptions withDistanceField(String name) {
      return new FindNearest.FindNearestOptions(limit, distanceMeasure, Field.of(name));
    }

    public Long getLimit() {
      return limit;
    }

    public DistanceMeasure getDistanceMeasure() {
      return distanceMeasure;
    }

    public Field getDistanceField() {
      return distanceField;
    }
  }
}
