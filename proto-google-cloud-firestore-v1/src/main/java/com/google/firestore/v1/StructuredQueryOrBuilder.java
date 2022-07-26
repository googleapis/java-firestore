// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/firestore/v1/query.proto

package com.google.firestore.v1;

public interface StructuredQueryOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.firestore.v1.StructuredQuery)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * The projection to return.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery.Projection select = 1;</code>
   * @return Whether the select field is set.
   */
  boolean hasSelect();
  /**
   * <pre>
   * The projection to return.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery.Projection select = 1;</code>
   * @return The select.
   */
  com.google.firestore.v1.StructuredQuery.Projection getSelect();
  /**
   * <pre>
   * The projection to return.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery.Projection select = 1;</code>
   */
  com.google.firestore.v1.StructuredQuery.ProjectionOrBuilder getSelectOrBuilder();

  /**
   * <pre>
   * The collections to query.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.CollectionSelector from = 2;</code>
   */
  java.util.List<com.google.firestore.v1.StructuredQuery.CollectionSelector> 
      getFromList();
  /**
   * <pre>
   * The collections to query.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.CollectionSelector from = 2;</code>
   */
  com.google.firestore.v1.StructuredQuery.CollectionSelector getFrom(int index);
  /**
   * <pre>
   * The collections to query.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.CollectionSelector from = 2;</code>
   */
  int getFromCount();
  /**
   * <pre>
   * The collections to query.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.CollectionSelector from = 2;</code>
   */
  java.util.List<? extends com.google.firestore.v1.StructuredQuery.CollectionSelectorOrBuilder> 
      getFromOrBuilderList();
  /**
   * <pre>
   * The collections to query.
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.CollectionSelector from = 2;</code>
   */
  com.google.firestore.v1.StructuredQuery.CollectionSelectorOrBuilder getFromOrBuilder(
      int index);

  /**
   * <pre>
   * The filter to apply.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery.Filter where = 3;</code>
   * @return Whether the where field is set.
   */
  boolean hasWhere();
  /**
   * <pre>
   * The filter to apply.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery.Filter where = 3;</code>
   * @return The where.
   */
  com.google.firestore.v1.StructuredQuery.Filter getWhere();
  /**
   * <pre>
   * The filter to apply.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery.Filter where = 3;</code>
   */
  com.google.firestore.v1.StructuredQuery.FilterOrBuilder getWhereOrBuilder();

  /**
   * <pre>
   * The order to apply to the query results.
   * Firestore allows callers to provide a full ordering, a partial ordering, or
   * no ordering at all. In all cases, Firestore guarantees a stable ordering
   * through the following rules:
   *  * The `order_by` is required to reference all fields used with an
   *    inequality filter.
   *  * All fields that are required to be in the `order_by` but are not already
   *    present are appended in lexicographical ordering of the field name.
   *  * If an order on `__name__` is not specified, it is appended by default.
   * Fields are appended with the same sort direction as the last order
   * specified, or 'ASCENDING' if no order was specified. For example:
   *  * `ORDER BY a` becomes `ORDER BY a ASC, __name__ ASC`
   *  * `ORDER BY a DESC` becomes `ORDER BY a DESC, __name__ DESC`
   *  * `WHERE a &gt; 1` becomes `WHERE a &gt; 1 ORDER BY a ASC, __name__ ASC`
   *  * `WHERE __name__ &gt; ... AND a &gt; 1` becomes
   *     `WHERE __name__ &gt; ... AND a &gt; 1 ORDER BY a ASC, __name__ ASC`
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.Order order_by = 4;</code>
   */
  java.util.List<com.google.firestore.v1.StructuredQuery.Order> 
      getOrderByList();
  /**
   * <pre>
   * The order to apply to the query results.
   * Firestore allows callers to provide a full ordering, a partial ordering, or
   * no ordering at all. In all cases, Firestore guarantees a stable ordering
   * through the following rules:
   *  * The `order_by` is required to reference all fields used with an
   *    inequality filter.
   *  * All fields that are required to be in the `order_by` but are not already
   *    present are appended in lexicographical ordering of the field name.
   *  * If an order on `__name__` is not specified, it is appended by default.
   * Fields are appended with the same sort direction as the last order
   * specified, or 'ASCENDING' if no order was specified. For example:
   *  * `ORDER BY a` becomes `ORDER BY a ASC, __name__ ASC`
   *  * `ORDER BY a DESC` becomes `ORDER BY a DESC, __name__ DESC`
   *  * `WHERE a &gt; 1` becomes `WHERE a &gt; 1 ORDER BY a ASC, __name__ ASC`
   *  * `WHERE __name__ &gt; ... AND a &gt; 1` becomes
   *     `WHERE __name__ &gt; ... AND a &gt; 1 ORDER BY a ASC, __name__ ASC`
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.Order order_by = 4;</code>
   */
  com.google.firestore.v1.StructuredQuery.Order getOrderBy(int index);
  /**
   * <pre>
   * The order to apply to the query results.
   * Firestore allows callers to provide a full ordering, a partial ordering, or
   * no ordering at all. In all cases, Firestore guarantees a stable ordering
   * through the following rules:
   *  * The `order_by` is required to reference all fields used with an
   *    inequality filter.
   *  * All fields that are required to be in the `order_by` but are not already
   *    present are appended in lexicographical ordering of the field name.
   *  * If an order on `__name__` is not specified, it is appended by default.
   * Fields are appended with the same sort direction as the last order
   * specified, or 'ASCENDING' if no order was specified. For example:
   *  * `ORDER BY a` becomes `ORDER BY a ASC, __name__ ASC`
   *  * `ORDER BY a DESC` becomes `ORDER BY a DESC, __name__ DESC`
   *  * `WHERE a &gt; 1` becomes `WHERE a &gt; 1 ORDER BY a ASC, __name__ ASC`
   *  * `WHERE __name__ &gt; ... AND a &gt; 1` becomes
   *     `WHERE __name__ &gt; ... AND a &gt; 1 ORDER BY a ASC, __name__ ASC`
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.Order order_by = 4;</code>
   */
  int getOrderByCount();
  /**
   * <pre>
   * The order to apply to the query results.
   * Firestore allows callers to provide a full ordering, a partial ordering, or
   * no ordering at all. In all cases, Firestore guarantees a stable ordering
   * through the following rules:
   *  * The `order_by` is required to reference all fields used with an
   *    inequality filter.
   *  * All fields that are required to be in the `order_by` but are not already
   *    present are appended in lexicographical ordering of the field name.
   *  * If an order on `__name__` is not specified, it is appended by default.
   * Fields are appended with the same sort direction as the last order
   * specified, or 'ASCENDING' if no order was specified. For example:
   *  * `ORDER BY a` becomes `ORDER BY a ASC, __name__ ASC`
   *  * `ORDER BY a DESC` becomes `ORDER BY a DESC, __name__ DESC`
   *  * `WHERE a &gt; 1` becomes `WHERE a &gt; 1 ORDER BY a ASC, __name__ ASC`
   *  * `WHERE __name__ &gt; ... AND a &gt; 1` becomes
   *     `WHERE __name__ &gt; ... AND a &gt; 1 ORDER BY a ASC, __name__ ASC`
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.Order order_by = 4;</code>
   */
  java.util.List<? extends com.google.firestore.v1.StructuredQuery.OrderOrBuilder> 
      getOrderByOrBuilderList();
  /**
   * <pre>
   * The order to apply to the query results.
   * Firestore allows callers to provide a full ordering, a partial ordering, or
   * no ordering at all. In all cases, Firestore guarantees a stable ordering
   * through the following rules:
   *  * The `order_by` is required to reference all fields used with an
   *    inequality filter.
   *  * All fields that are required to be in the `order_by` but are not already
   *    present are appended in lexicographical ordering of the field name.
   *  * If an order on `__name__` is not specified, it is appended by default.
   * Fields are appended with the same sort direction as the last order
   * specified, or 'ASCENDING' if no order was specified. For example:
   *  * `ORDER BY a` becomes `ORDER BY a ASC, __name__ ASC`
   *  * `ORDER BY a DESC` becomes `ORDER BY a DESC, __name__ DESC`
   *  * `WHERE a &gt; 1` becomes `WHERE a &gt; 1 ORDER BY a ASC, __name__ ASC`
   *  * `WHERE __name__ &gt; ... AND a &gt; 1` becomes
   *     `WHERE __name__ &gt; ... AND a &gt; 1 ORDER BY a ASC, __name__ ASC`
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.Order order_by = 4;</code>
   */
  com.google.firestore.v1.StructuredQuery.OrderOrBuilder getOrderByOrBuilder(
      int index);

  /**
   * <pre>
   * A starting point for the query results.
   * </pre>
   *
   * <code>.google.firestore.v1.Cursor start_at = 7;</code>
   * @return Whether the startAt field is set.
   */
  boolean hasStartAt();
  /**
   * <pre>
   * A starting point for the query results.
   * </pre>
   *
   * <code>.google.firestore.v1.Cursor start_at = 7;</code>
   * @return The startAt.
   */
  com.google.firestore.v1.Cursor getStartAt();
  /**
   * <pre>
   * A starting point for the query results.
   * </pre>
   *
   * <code>.google.firestore.v1.Cursor start_at = 7;</code>
   */
  com.google.firestore.v1.CursorOrBuilder getStartAtOrBuilder();

  /**
   * <pre>
   * A end point for the query results.
   * </pre>
   *
   * <code>.google.firestore.v1.Cursor end_at = 8;</code>
   * @return Whether the endAt field is set.
   */
  boolean hasEndAt();
  /**
   * <pre>
   * A end point for the query results.
   * </pre>
   *
   * <code>.google.firestore.v1.Cursor end_at = 8;</code>
   * @return The endAt.
   */
  com.google.firestore.v1.Cursor getEndAt();
  /**
   * <pre>
   * A end point for the query results.
   * </pre>
   *
   * <code>.google.firestore.v1.Cursor end_at = 8;</code>
   */
  com.google.firestore.v1.CursorOrBuilder getEndAtOrBuilder();

  /**
   * <pre>
   * The number of results to skip.
   * Applies before limit, but after all other constraints. Must be &gt;= 0 if
   * specified.
   * </pre>
   *
   * <code>int32 offset = 6;</code>
   * @return The offset.
   */
  int getOffset();

  /**
   * <pre>
   * The maximum number of results to return.
   * Applies after all other constraints.
   * Must be &gt;= 0 if specified.
   * </pre>
   *
   * <code>.google.protobuf.Int32Value limit = 5;</code>
   * @return Whether the limit field is set.
   */
  boolean hasLimit();
  /**
   * <pre>
   * The maximum number of results to return.
   * Applies after all other constraints.
   * Must be &gt;= 0 if specified.
   * </pre>
   *
   * <code>.google.protobuf.Int32Value limit = 5;</code>
   * @return The limit.
   */
  com.google.protobuf.Int32Value getLimit();
  /**
   * <pre>
   * The maximum number of results to return.
   * Applies after all other constraints.
   * Must be &gt;= 0 if specified.
   * </pre>
   *
   * <code>.google.protobuf.Int32Value limit = 5;</code>
   */
  com.google.protobuf.Int32ValueOrBuilder getLimitOrBuilder();
}
