package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.expressions.Field;

public final class FindNearest implements Stage {

  private static final String name = "find_nearest";
  private final Field property;
  private final double[] vector;
  private final FindNearest.FindNearestOptions options;

  @InternalApi
  public FindNearest(Field property, double[] vector, FindNearest.FindNearestOptions options) {
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
  public Field getProperty() {
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
    private final Field distanceField;

    private FindNearestOptions(
        Long limit, FindNearest.DistanceMeasure distanceMeasure, Field distanceField) {
      this.limit = limit;
      this.distanceMeasure = distanceMeasure;
      this.distanceField = distanceField;
    }

    public static FindNearest.FindNearestOptions newInstance(
        long limit, FindNearest.DistanceMeasure distanceMeasure, Field output) {
      return new FindNearest.FindNearestOptions(limit, distanceMeasure, output);
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
