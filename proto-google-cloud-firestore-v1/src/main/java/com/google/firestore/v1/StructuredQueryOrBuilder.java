/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
   */
  boolean hasSelect();
  /**
   * <pre>
   * The projection to return.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery.Projection select = 1;</code>
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
   */
  boolean hasWhere();
  /**
   * <pre>
   * The filter to apply.
   * </pre>
   *
   * <code>.google.firestore.v1.StructuredQuery.Filter where = 3;</code>
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
   * Firestore guarantees a stable ordering through the following rules:
   *  * Any field required to appear in `order_by`, that is not already
   *    specified in `order_by`, is appended to the order in field name order
   *    by default.
   *  * If an order on `__name__` is not specified, it is appended by default.
   * Fields are appended with the same sort direction as the last order
   * specified, or 'ASCENDING' if no order was specified. For example:
   *  * `SELECT * FROM Foo ORDER BY A` becomes
   *    `SELECT * FROM Foo ORDER BY A, __name__`
   *  * `SELECT * FROM Foo ORDER BY A DESC` becomes
   *    `SELECT * FROM Foo ORDER BY A DESC, __name__ DESC`
   *  * `SELECT * FROM Foo WHERE A &gt; 1` becomes
   *    `SELECT * FROM Foo WHERE A &gt; 1 ORDER BY A, __name__`
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.Order order_by = 4;</code>
   */
  java.util.List<com.google.firestore.v1.StructuredQuery.Order> 
      getOrderByList();
  /**
   * <pre>
   * The order to apply to the query results.
   * Firestore guarantees a stable ordering through the following rules:
   *  * Any field required to appear in `order_by`, that is not already
   *    specified in `order_by`, is appended to the order in field name order
   *    by default.
   *  * If an order on `__name__` is not specified, it is appended by default.
   * Fields are appended with the same sort direction as the last order
   * specified, or 'ASCENDING' if no order was specified. For example:
   *  * `SELECT * FROM Foo ORDER BY A` becomes
   *    `SELECT * FROM Foo ORDER BY A, __name__`
   *  * `SELECT * FROM Foo ORDER BY A DESC` becomes
   *    `SELECT * FROM Foo ORDER BY A DESC, __name__ DESC`
   *  * `SELECT * FROM Foo WHERE A &gt; 1` becomes
   *    `SELECT * FROM Foo WHERE A &gt; 1 ORDER BY A, __name__`
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.Order order_by = 4;</code>
   */
  com.google.firestore.v1.StructuredQuery.Order getOrderBy(int index);
  /**
   * <pre>
   * The order to apply to the query results.
   * Firestore guarantees a stable ordering through the following rules:
   *  * Any field required to appear in `order_by`, that is not already
   *    specified in `order_by`, is appended to the order in field name order
   *    by default.
   *  * If an order on `__name__` is not specified, it is appended by default.
   * Fields are appended with the same sort direction as the last order
   * specified, or 'ASCENDING' if no order was specified. For example:
   *  * `SELECT * FROM Foo ORDER BY A` becomes
   *    `SELECT * FROM Foo ORDER BY A, __name__`
   *  * `SELECT * FROM Foo ORDER BY A DESC` becomes
   *    `SELECT * FROM Foo ORDER BY A DESC, __name__ DESC`
   *  * `SELECT * FROM Foo WHERE A &gt; 1` becomes
   *    `SELECT * FROM Foo WHERE A &gt; 1 ORDER BY A, __name__`
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.Order order_by = 4;</code>
   */
  int getOrderByCount();
  /**
   * <pre>
   * The order to apply to the query results.
   * Firestore guarantees a stable ordering through the following rules:
   *  * Any field required to appear in `order_by`, that is not already
   *    specified in `order_by`, is appended to the order in field name order
   *    by default.
   *  * If an order on `__name__` is not specified, it is appended by default.
   * Fields are appended with the same sort direction as the last order
   * specified, or 'ASCENDING' if no order was specified. For example:
   *  * `SELECT * FROM Foo ORDER BY A` becomes
   *    `SELECT * FROM Foo ORDER BY A, __name__`
   *  * `SELECT * FROM Foo ORDER BY A DESC` becomes
   *    `SELECT * FROM Foo ORDER BY A DESC, __name__ DESC`
   *  * `SELECT * FROM Foo WHERE A &gt; 1` becomes
   *    `SELECT * FROM Foo WHERE A &gt; 1 ORDER BY A, __name__`
   * </pre>
   *
   * <code>repeated .google.firestore.v1.StructuredQuery.Order order_by = 4;</code>
   */
  java.util.List<? extends com.google.firestore.v1.StructuredQuery.OrderOrBuilder> 
      getOrderByOrBuilderList();
  /**
   * <pre>
   * The order to apply to the query results.
   * Firestore guarantees a stable ordering through the following rules:
   *  * Any field required to appear in `order_by`, that is not already
   *    specified in `order_by`, is appended to the order in field name order
   *    by default.
   *  * If an order on `__name__` is not specified, it is appended by default.
   * Fields are appended with the same sort direction as the last order
   * specified, or 'ASCENDING' if no order was specified. For example:
   *  * `SELECT * FROM Foo ORDER BY A` becomes
   *    `SELECT * FROM Foo ORDER BY A, __name__`
   *  * `SELECT * FROM Foo ORDER BY A DESC` becomes
   *    `SELECT * FROM Foo ORDER BY A DESC, __name__ DESC`
   *  * `SELECT * FROM Foo WHERE A &gt; 1` becomes
   *    `SELECT * FROM Foo WHERE A &gt; 1 ORDER BY A, __name__`
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
   */
  boolean hasStartAt();
  /**
   * <pre>
   * A starting point for the query results.
   * </pre>
   *
   * <code>.google.firestore.v1.Cursor start_at = 7;</code>
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
   */
  boolean hasEndAt();
  /**
   * <pre>
   * A end point for the query results.
   * </pre>
   *
   * <code>.google.firestore.v1.Cursor end_at = 8;</code>
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
