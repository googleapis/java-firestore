package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.pipeline.expressions.Ordering;
import java.util.List;
import java.util.Locale;

@InternalApi
public final class Sort implements Stage {

  private static final String name = "sort";
  private final List<Ordering> orders;
  private final Sort.Density density;
  private final Sort.Truncation truncation;

  @InternalApi
  public Sort(List<Ordering> orders, Sort.Density density, Sort.Truncation truncation) {
    this.orders = orders;
    this.density = density;
    this.truncation = truncation;
  }

  // Getters
  public String getName() {
    return name;
  }

  public List<Ordering> getOrders() {
    return orders;
  }

  public Sort.Density getDensity() {
    if (density != null) {
      return density;
    }
    return Density.UNSPECIFIED;
  }

  public Sort.Truncation getTruncation() {
    if (truncation != null) {
      return truncation;
    }
    return Truncation.UNSPECIFIED;
  }

  public enum Density {
    UNSPECIFIED,
    REQUIRED;

    @Override
    public String toString() {
      return name().toLowerCase(Locale.getDefault());
    }
  }

  public enum Truncation {
    UNSPECIFIED,
    DISABLED;

    @Override
    public String toString() {
      return name().toLowerCase(Locale.getDefault());
    }
  }
}
