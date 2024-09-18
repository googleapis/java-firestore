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

package com.google.cloud.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.firestore.v1.RunQueryRequest;
import com.google.firestore.v1.StructuredQuery;
import com.google.protobuf.ByteString;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class VectorQuery extends StreamableQuery<VectorQuerySnapshot> {
  static final Comparator<QueryDocumentSnapshot> DOCUMENT_ID_COMPARATOR =
      QueryDocumentSnapshot::compareDocumentId;
  final Query query;
  final FieldPath vectorField;
  final VectorValue queryVector;
  final int limit;
  final DistanceMeasure distanceMeasure;
  final FindNearestOptions options;

  /** Creates a VectorQuery for documents in a single collection */
  VectorQuery(
      Query query,
      FieldPath vectorField,
      VectorValue queryVector,
      int limit,
      DistanceMeasure distanceMeasure,
      FindNearestOptions options) {
    super(query.rpcContext, query.options);

    this.query = query;
    this.options = options;
    this.vectorField = vectorField;
    this.queryVector = queryVector;
    this.limit = limit;
    this.distanceMeasure = distanceMeasure;
  }

  @Override
  public ApiFuture<VectorQuerySnapshot> get() {
    return get(null, null);
  }

  /**
   * Plans and optionally executes this query. Returns an ApiFuture that will be resolved with the
   * planner information, statistics from the query execution (if any), and the query results (if
   * any).
   *
   * @return An ApiFuture that will be resolved with the planner information, statistics from the
   *     query execution (if any), and the query results (if any).
   */
  @Override
  public ApiFuture<ExplainResults<VectorQuerySnapshot>> explain(ExplainOptions options) {
    return super.explain(options);
  }

  /**
   * Returns true if this VectorQuery is equal to the provided object.
   *
   * @param obj The object to compare against.
   * @return Whether this VectorQuery is equal to the provided object.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || !(obj instanceof VectorQuery)) {
      return false;
    }
    VectorQuery otherQuery = (VectorQuery) obj;
    return Objects.equals(query, otherQuery.query)
        && Objects.equals(options, otherQuery.options)
        && Objects.equals(vectorField, otherQuery.vectorField)
        && Objects.equals(queryVector, otherQuery.queryVector)
        && Objects.equals(options, otherQuery.options)
        && limit == otherQuery.limit
        && distanceMeasure == otherQuery.distanceMeasure;
  }

  @Override
  public int hashCode() {
    return Objects.hash(query, options, vectorField, queryVector, limit, distanceMeasure);
  }

  @Override
  protected RunQueryRequest.Builder toRunQueryRequestBuilder(
      @Nullable final ByteString transactionId,
      @Nullable final Timestamp readTime,
      @Nullable ExplainOptions explainOptions) {

    // Builder for the base query
    RunQueryRequest.Builder requestBuilder =
        query.toRunQueryRequestBuilder(transactionId, readTime, explainOptions);

    // Builder for find nearest
    StructuredQuery.FindNearest.Builder findNearestBuilder =
        requestBuilder.getStructuredQueryBuilder().getFindNearestBuilder();
    findNearestBuilder.getQueryVectorBuilder().setMapValue(this.queryVector.toProto());
    findNearestBuilder.getLimitBuilder().setValue(this.limit);
    switch (this.distanceMeasure) {
      case COSINE:
        findNearestBuilder.setDistanceMeasure(StructuredQuery.FindNearest.DistanceMeasure.COSINE);
        break;
      case EUCLIDEAN:
        findNearestBuilder.setDistanceMeasure(
            StructuredQuery.FindNearest.DistanceMeasure.EUCLIDEAN);
        break;
      case DOT_PRODUCT:
        findNearestBuilder.setDistanceMeasure(
            StructuredQuery.FindNearest.DistanceMeasure.DOT_PRODUCT);
        break;
      default:
        findNearestBuilder.setDistanceMeasure(
            StructuredQuery.FindNearest.DistanceMeasure.UNRECOGNIZED);
    }
    findNearestBuilder.getVectorFieldBuilder().setFieldPath(this.vectorField.toString());

    if (this.options != null) {
      if (this.options.getDistanceThreshold() != null) {
        findNearestBuilder
            .getDistanceThresholdBuilder()
            .setValue(this.options.getDistanceThreshold().doubleValue());
      }
      if (this.options.getDistanceResultField() != null) {
        findNearestBuilder.setDistanceResultField(this.options.getDistanceResultField().toString());
      }
    }

    return requestBuilder;
  }

  @Override
  boolean isRetryableWithCursor() {
    return false;
  }

  @Override
  StreamableQuery startAfter(@Nonnull DocumentSnapshot snapshot) {
    throw new RuntimeException("Not implemented");
  }

  @Override
  VectorQuerySnapshot createSnaphot(
      Timestamp readTime, final List<QueryDocumentSnapshot> documents) {
    return VectorQuerySnapshot.withDocuments(this, readTime, documents);
  }

  /**
   * The distance measure to use when comparing vectors in a {@link VectorQuery}.
   *
   * @see com.google.cloud.firestore.Query#findNearest
   */
  public enum DistanceMeasure {
    /**
     * COSINE distance compares vectors based on the angle between them, which allows you to measure
     * similarity that isn't based on the vectors' magnitude. We recommend using DOT_PRODUCT with
     * unit normalized vectors instead of COSINE distance, which is mathematically equivalent with
     * better performance.
     */
    COSINE,
    /** Measures the EUCLIDEAN distance between the vectors. */
    EUCLIDEAN,
    /** Similar to cosine but is affected by the magnitude of the vectors. */
    DOT_PRODUCT
  }
}
