package com.google.cloud.firestore;

import javax.annotation.Nullable;

/**
 * A split point that can be used by in a query as a starting or end point for the query results.
 * The cursors returned by {@link #getStartAt()} and {@link #getEndBefore()} can only be used in a
 * query that matches the constraint of query that produced this partition.
 */
public class QueryPartition {
  private final Query query;
  @Nullable private final Object[] startAt;
  @Nullable private final Object[] endBefore;

  public QueryPartition(Query query, Object[] startAt, @Nullable Object[] endBefore) {
    this.query = query;
    this.startAt = startAt;
    this.endBefore = endBefore;
  }

  /**
   * The cursor that defines the first result for this partition. {@code null} if this is the first
   * partition.
   *
   * @return a cursor value that can be used with {@link Query#startAt(Object...)} or {@code null}
   *     if this is the first partition.
   */
  @Nullable
  public Object[] getStartAt() {
    return startAt;
  }

  /**
   * The cursor that defines the first result after this partition. {@code null} if this is the last
   * partition.
   *
   * @return a cursor value that can be used with {@link Query#endBefore(Object...)} or {@code null}
   *     if this is the last partition.
   */
  @Nullable
  public Object[] getEndBefore() {
    return endBefore;
  }

  /**
   * Returns a query that only returns the documents for this partition.
   *
   * @return a query partitioned by a {@link Query#startAt(Object...)} and {@link
   *     Query#endBefore(Object...)} cursor.
   */
  public Query createQuery() {
    Query baseQuery = query;
    if (startAt != null) {
      baseQuery = baseQuery.startAt(startAt);
    }
    if (endBefore != null) {
      baseQuery = baseQuery.endBefore(endBefore);
    }
    return baseQuery;
  }
}
