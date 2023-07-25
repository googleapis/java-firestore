package com.google.cloud.firestore;

import java.util.Map;
import javax.annotation.Nonnull;

/**
 * An AggregateQueryProfileInfo contains information about planning, execution, and results of an
 * aggregate query.
 */
public class AggregateQueryProfileInfo {
  /** A Map that contains information about the query plan. Contents are subject to change. */
  @Nonnull public final Map<String, Object> plan;

  /**
   * A Map that contains statistics about the execution of the aggregate query. Contents are subject
   * to change.
   */
  @Nonnull public final Map<String, Object> stats;

  /** The snapshot that contains the results of executing the aggregate query. */
  @Nonnull public final AggregateQuerySnapshot snapshot;

  public AggregateQueryProfileInfo(
      @Nonnull Map<String, Object> plan,
      @Nonnull Map<String, Object> stats,
      @Nonnull AggregateQuerySnapshot snapshot) {
    this.plan = plan;
    this.stats = stats;
    this.snapshot = snapshot;
  }
}
