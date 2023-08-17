/*
 * Copyright 2017 Google LLC
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

import static com.google.common.collect.Lists.reverse;
import static com.google.firestore.v1.StructuredQuery.FieldFilter.Operator.ARRAY_CONTAINS;
import static com.google.firestore.v1.StructuredQuery.FieldFilter.Operator.ARRAY_CONTAINS_ANY;
import static com.google.firestore.v1.StructuredQuery.FieldFilter.Operator.EQUAL;
import static com.google.firestore.v1.StructuredQuery.FieldFilter.Operator.GREATER_THAN;
import static com.google.firestore.v1.StructuredQuery.FieldFilter.Operator.GREATER_THAN_OR_EQUAL;
import static com.google.firestore.v1.StructuredQuery.FieldFilter.Operator.IN;
import static com.google.firestore.v1.StructuredQuery.FieldFilter.Operator.LESS_THAN;
import static com.google.firestore.v1.StructuredQuery.FieldFilter.Operator.LESS_THAN_OR_EQUAL;
import static com.google.firestore.v1.StructuredQuery.FieldFilter.Operator.NOT_EQUAL;
import static com.google.firestore.v1.StructuredQuery.FieldFilter.Operator.NOT_IN;

import com.google.api.core.ApiFuture;
import com.google.api.core.InternalExtensionOnly;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.StreamController;
import com.google.auto.value.AutoValue;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Query.QueryOptions.Builder;
import com.google.cloud.firestore.v1.FirestoreSettings;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.firestore.bundle.BundledQuery;
import com.google.firestore.v1.Cursor;
import com.google.firestore.v1.Document;
import com.google.firestore.v1.RunQueryRequest;
import com.google.firestore.v1.RunQueryResponse;
import com.google.firestore.v1.StructuredQuery;
import com.google.firestore.v1.StructuredQuery.CollectionSelector;
import com.google.firestore.v1.StructuredQuery.CompositeFilter;
import com.google.firestore.v1.StructuredQuery.FieldFilter.Operator;
import com.google.firestore.v1.StructuredQuery.FieldReference;
import com.google.firestore.v1.StructuredQuery.Filter;
import com.google.firestore.v1.StructuredQuery.Order;
import com.google.firestore.v1.Value;
import com.google.protobuf.ByteString;
import com.google.protobuf.Int32Value;
import io.grpc.Status;
import io.opencensus.trace.AttributeValue;
import io.opencensus.trace.Tracing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.threeten.bp.Duration;

/**
 * A Query which you can read or listen to. You can also construct refined Query objects by adding
 * filters and ordering.
 */
@InternalExtensionOnly
public class Query {

  final FirestoreRpcContext<?> rpcContext;
  final QueryOptions options;

  private static final Logger LOGGER = Logger.getLogger(Query.class.getName());

  /** The direction of a sort. */
  public enum Direction {
    ASCENDING(StructuredQuery.Direction.ASCENDING),
    DESCENDING(StructuredQuery.Direction.DESCENDING);

    private final StructuredQuery.Direction direction;

    Direction(StructuredQuery.Direction direction) {
      this.direction = direction;
    }

    StructuredQuery.Direction getDirection() {
      return direction;
    }
  }

  abstract static class FilterInternal {
    /** Returns a list of all field filters that are contained within this filter */
    abstract List<FieldFilterInternal> getFlattenedFilters();

    /** Returns a list of all filters that are contained within this filter */
    abstract List<FilterInternal> getFilters();

    /** Returns the field of the first filter that's an inequality, or null if none. */
    @Nullable
    abstract FieldReference getFirstInequalityField();

    /** Returns the proto representation of this filter */
    abstract Filter toProto();

    static FilterInternal fromProto(StructuredQuery.Filter filter) {
      if (filter.hasUnaryFilter()) {
        return new UnaryFilterInternal(
            filter.getUnaryFilter().getField(), filter.getUnaryFilter().getOp());
      }

      if (filter.hasFieldFilter()) {
        return new ComparisonFilterInternal(
            filter.getFieldFilter().getField(),
            filter.getFieldFilter().getOp(),
            filter.getFieldFilter().getValue());
      }

      // `filter` must be a composite filter.
      Preconditions.checkArgument(filter.hasCompositeFilter(), "Unknown filter type.");
      CompositeFilter compositeFilter = filter.getCompositeFilter();
      // A composite filter with only 1 sub-filter should be reduced to its sub-filter.
      if (compositeFilter.getFiltersCount() == 1) {
        return FilterInternal.fromProto(compositeFilter.getFiltersList().get(0));
      }
      List<FilterInternal> filters = new ArrayList<>();
      for (StructuredQuery.Filter subfilter : compositeFilter.getFiltersList()) {
        filters.add(FilterInternal.fromProto(subfilter));
      }
      return new CompositeFilterInternal(filters, compositeFilter.getOp());
    }
  }

  static class CompositeFilterInternal extends FilterInternal {
    private final List<FilterInternal> filters;
    private final StructuredQuery.CompositeFilter.Operator operator;

    // Memoized list of all field filters that can be found by traversing the tree of filters
    // contained in this composite filter.
    private List<FieldFilterInternal> memoizedFlattenedFilters;

    public CompositeFilterInternal(
        List<FilterInternal> filters, StructuredQuery.CompositeFilter.Operator operator) {
      this.filters = filters;
      this.operator = operator;
    }

    @Override
    public List<FilterInternal> getFilters() {
      return filters;
    }

    @Nullable
    @Override
    public FieldReference getFirstInequalityField() {
      for (FieldFilterInternal fieldFilter : getFlattenedFilters()) {
        if (fieldFilter.isInequalityFilter()) {
          return fieldFilter.fieldReference;
        }
      }
      return null;
    }

    public boolean isConjunction() {
      return operator == CompositeFilter.Operator.AND;
    }

    @Override
    public List<FieldFilterInternal> getFlattenedFilters() {
      if (memoizedFlattenedFilters != null) {
        return memoizedFlattenedFilters;
      }
      memoizedFlattenedFilters = new ArrayList<>();
      for (FilterInternal subfilter : filters) {
        memoizedFlattenedFilters.addAll(subfilter.getFlattenedFilters());
      }
      return memoizedFlattenedFilters;
    }

    @Override
    Filter toProto() {
      // A composite filter that contains one sub-filter is equivalent to the sub-filter.
      if (filters.size() == 1) {
        return filters.get(0).toProto();
      }

      Filter.Builder protoFilter = Filter.newBuilder();
      StructuredQuery.CompositeFilter.Builder compositeFilter =
          StructuredQuery.CompositeFilter.newBuilder();
      compositeFilter.setOp(operator);
      for (FilterInternal filter : filters) {
        compositeFilter.addFilters(filter.toProto());
      }
      protoFilter.setCompositeFilter(compositeFilter.build());
      return protoFilter.build();
    }
  }

  abstract static class FieldFilterInternal extends FilterInternal {
    protected final FieldReference fieldReference;

    FieldFilterInternal(FieldReference fieldReference) {
      this.fieldReference = fieldReference;
    }

    abstract boolean isInequalityFilter();

    public List<FilterInternal> getFilters() {
      return Collections.singletonList(this);
    }

    @Override
    public List<FieldFilterInternal> getFlattenedFilters() {
      return Collections.singletonList(this);
    }
  }

  private static class UnaryFilterInternal extends FieldFilterInternal {

    private final StructuredQuery.UnaryFilter.Operator operator;

    UnaryFilterInternal(
        FieldReference fieldReference, StructuredQuery.UnaryFilter.Operator operator) {
      super(fieldReference);
      this.operator = operator;
    }

    @Override
    boolean isInequalityFilter() {
      return false;
    }

    @Nullable
    @Override
    public FieldReference getFirstInequalityField() {
      return null;
    }

    Filter toProto() {
      Filter.Builder result = Filter.newBuilder();
      result.getUnaryFilterBuilder().setField(fieldReference).setOp(operator);
      return result.build();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof UnaryFilterInternal)) {
        return false;
      }
      UnaryFilterInternal other = (UnaryFilterInternal) o;
      return Objects.equals(fieldReference, other.fieldReference)
          && Objects.equals(operator, other.operator);
    }
  }

  static class ComparisonFilterInternal extends FieldFilterInternal {
    final StructuredQuery.FieldFilter.Operator operator;
    final Value value;

    ComparisonFilterInternal(
        FieldReference fieldReference, StructuredQuery.FieldFilter.Operator operator, Value value) {
      super(fieldReference);
      this.value = value;
      this.operator = operator;
    }

    @Override
    boolean isInequalityFilter() {
      return operator.equals(GREATER_THAN)
          || operator.equals(GREATER_THAN_OR_EQUAL)
          || operator.equals(LESS_THAN)
          || operator.equals(LESS_THAN_OR_EQUAL);
    }

    @Nullable
    @Override
    public FieldReference getFirstInequalityField() {
      if (isInequalityFilter()) {
        return fieldReference;
      }
      return null;
    }

    Filter toProto() {
      Filter.Builder result = Filter.newBuilder();
      result.getFieldFilterBuilder().setField(fieldReference).setValue(value).setOp(operator);
      return result.build();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof ComparisonFilterInternal)) {
        return false;
      }
      ComparisonFilterInternal other = (ComparisonFilterInternal) o;
      return Objects.equals(fieldReference, other.fieldReference)
          && Objects.equals(operator, other.operator)
          && Objects.equals(value, other.value);
    }
  }

  static final class FieldOrder {
    private final FieldReference fieldReference;
    private final Direction direction;

    FieldOrder(FieldReference fieldReference, Direction direction) {
      this.fieldReference = fieldReference;
      this.direction = direction;
    }

    Order toProto() {
      Order.Builder result = Order.newBuilder();
      result.setField(fieldReference);
      result.setDirection(direction.getDirection());
      return result.build();
    }

    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof FieldOrder)) {
        return false;
      }
      FieldOrder filter = (FieldOrder) o;
      return Objects.equals(toProto(), filter.toProto());
    }
  }

  /** Denotes whether a provided limit is applied to the beginning or the end of the result set. */
  enum LimitType {
    First,
    Last
  }

  /** Options that define a Firestore Query. */
  @AutoValue
  abstract static class QueryOptions {

    abstract ResourcePath getParentPath();

    abstract String getCollectionId();

    abstract boolean getAllDescendants();

    abstract @Nullable Integer getLimit();

    abstract LimitType getLimitType();

    abstract @Nullable Integer getOffset();

    abstract @Nullable Cursor getStartCursor();

    abstract @Nullable Cursor getEndCursor();

    abstract ImmutableList<FilterInternal> getFilters();

    abstract ImmutableList<FieldOrder> getFieldOrders();

    abstract ImmutableList<FieldReference> getFieldProjections();

    // Whether to select all documents under `parentPath`. By default, only
    // collections that match `collectionId` are selected.
    abstract boolean isKindless();

    // Whether to require consistent documents when restarting the query. By
    // default, restarting the query uses the readTime offset of the original
    // query to provide consistent results.
    abstract boolean getRequireConsistency();

    static Builder builder() {
      return new AutoValue_Query_QueryOptions.Builder()
          .setAllDescendants(false)
          .setLimitType(LimitType.First)
          .setFieldOrders(ImmutableList.of())
          .setFilters(ImmutableList.of())
          .setFieldProjections(ImmutableList.of())
          .setKindless(false)
          .setRequireConsistency(true);
    }

    abstract Builder toBuilder();

    @AutoValue.Builder
    abstract static class Builder {
      abstract Builder setParentPath(ResourcePath value);

      abstract Builder setCollectionId(String value);

      abstract Builder setAllDescendants(boolean value);

      abstract Builder setLimit(Integer value);

      abstract Builder setLimitType(LimitType value);

      abstract Builder setOffset(Integer value);

      abstract Builder setStartCursor(@Nullable Cursor value);

      abstract Builder setEndCursor(@Nullable Cursor value);

      abstract Builder setFilters(ImmutableList<FilterInternal> value);

      abstract Builder setFieldOrders(ImmutableList<FieldOrder> value);

      abstract Builder setFieldProjections(ImmutableList<FieldReference> value);

      abstract Builder setKindless(boolean value);

      abstract Builder setRequireConsistency(boolean value);

      abstract QueryOptions build();
    }
  }

  /** Creates a query for documents in a single collection */
  Query(FirestoreRpcContext<?> rpcContext, ResourcePath path) {
    this(
        rpcContext,
        QueryOptions.builder()
            .setParentPath(path.getParent())
            .setCollectionId(path.getId())
            .build());
  }

  protected Query(FirestoreRpcContext<?> rpcContext, QueryOptions queryOptions) {
    this.rpcContext = rpcContext;
    this.options = queryOptions;
  }

  /**
   * Gets the Firestore instance associated with this query.
   *
   * @return The Firestore instance associated with this query.
   */
  @Nonnull
  public Firestore getFirestore() {
    return rpcContext.getFirestore();
  }

  /** Checks whether the provided object is NULL or NaN. */
  private static boolean isUnaryComparison(@Nullable Object value) {
    return value == null || value.equals(Double.NaN) || value.equals(Float.NaN);
  }

  /** Computes the backend ordering semantics for DocumentSnapshot cursors. */
  private ImmutableList<FieldOrder> createImplicitOrderBy() {
    List<FieldOrder> implicitOrders = new ArrayList<>(options.getFieldOrders());

    // If no explicit ordering is specified, use the first inequality to define an implicit order.
    if (implicitOrders.isEmpty()) {
      for (FilterInternal filter : options.getFilters()) {
        FieldReference fieldReference = filter.getFirstInequalityField();
        if (fieldReference != null) {
          implicitOrders.add(new FieldOrder(fieldReference, Direction.ASCENDING));
          break;
        }
      }
    }

    boolean hasDocumentId = false;
    for (FieldOrder fieldOrder : implicitOrders) {
      if (FieldPath.isDocumentId(fieldOrder.fieldReference.getFieldPath())) {
        hasDocumentId = true;
      }
    }

    if (!hasDocumentId) {
      // Add implicit sorting by name, using the last specified direction.
      Direction lastDirection =
          implicitOrders.isEmpty()
              ? Direction.ASCENDING
              : implicitOrders.get(implicitOrders.size() - 1).direction;

      implicitOrders.add(new FieldOrder(FieldPath.documentId().toProto(), lastDirection));
    }

    return ImmutableList.<FieldOrder>builder().addAll(implicitOrders).build();
  }

  private Cursor createCursor(
      ImmutableList<FieldOrder> order, DocumentSnapshot documentSnapshot, boolean before) {
    List<Object> fieldValues = new ArrayList<>();

    for (FieldOrder fieldOrder : order) {
      String path = fieldOrder.fieldReference.getFieldPath();
      if (FieldPath.isDocumentId(path)) {
        fieldValues.add(documentSnapshot.getReference());
      } else {
        FieldPath fieldPath = FieldPath.fromDotSeparatedString(path);
        Preconditions.checkArgument(
            documentSnapshot.contains(fieldPath),
            "Field '%s' is missing in the provided DocumentSnapshot. Please provide a document "
                + "that contains values for all specified orderBy() and where() constraints.");
        fieldValues.add(documentSnapshot.get(fieldPath));
      }
    }

    return createCursor(order, fieldValues.toArray(), before);
  }

  private Cursor createCursor(List<FieldOrder> order, Object[] fieldValues, boolean before) {
    Cursor.Builder result = Cursor.newBuilder();

    Preconditions.checkState(
        fieldValues.length != 0, "At least one cursor value must be specified.");

    Preconditions.checkState(
        fieldValues.length <= order.size(),
        "Too many cursor values specified. The specified values must match the "
            + "orderBy() constraints of the query.");

    Iterator<FieldOrder> fieldOrderIterator = order.iterator();

    for (Object fieldValue : fieldValues) {
      Object sanitizedValue;

      FieldReference fieldReference = fieldOrderIterator.next().fieldReference;

      if (FieldPath.isDocumentId(fieldReference.getFieldPath())) {
        sanitizedValue = convertReference(fieldValue);
      } else {
        sanitizedValue = CustomClassMapper.serialize(fieldValue);
      }

      Value encodedValue = encodeValue(fieldReference, sanitizedValue);

      if (encodedValue == null) {
        throw FirestoreException.forInvalidArgument(
            "Cannot use FieldValue.delete() or FieldValue.serverTimestamp() in a query boundary");
      }
      result.addValues(encodedValue);
    }

    result.setBefore(before);

    return result.build();
  }

  /**
   * Validates that a value used with FieldValue.documentId() is either a string or a
   * DocumentReference that is part of the query`s result set. Throws a validation error or returns
   * a DocumentReference that can directly be used in the Query.
   */
  private Object convertReference(Object fieldValue) {
    ResourcePath basePath =
        options.getAllDescendants()
            ? options.getParentPath()
            : options.getParentPath().append(options.getCollectionId());

    DocumentReference reference;
    if (fieldValue instanceof String) {
      reference = new DocumentReference(rpcContext, basePath.append((String) fieldValue));
    } else if (fieldValue instanceof DocumentReference) {
      reference = (DocumentReference) fieldValue;
    } else {
      throw new IllegalArgumentException(
          String.format(
              "The corresponding value for FieldPath.documentId() must be a String or a "
                  + "DocumentReference, but was: %s.",
              fieldValue.toString()));
    }

    if (!basePath.isPrefixOf(reference.getResourcePath())) {
      throw new IllegalArgumentException(
          String.format(
              "'%s' is not part of the query result set and cannot be used as a query boundary.",
              reference.getPath()));
    }

    if (!options.getAllDescendants() && !reference.getParent().getResourcePath().equals(basePath)) {
      throw new IllegalArgumentException(
          String.format(
              "Only a direct child can be used as a query boundary. Found: '%s'",
              reference.getPath()));
    }

    return reference;
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value should be equal to the specified value.
   *
   * @param field The name of the field to compare.
   * @param value The value for comparison.
   * @return The created Query.
   */
  @Nonnull
  public Query whereEqualTo(@Nonnull String field, @Nullable Object value) {
    return whereEqualTo(FieldPath.fromDotSeparatedString(field), value);
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value should be equal to the specified value.
   *
   * @param fieldPath The path of the field to compare.
   * @param value The value for comparison.
   * @return The created Query.
   */
  @Nonnull
  public Query whereEqualTo(@Nonnull FieldPath fieldPath, @Nullable Object value) {
    return where(new com.google.cloud.firestore.Filter.UnaryFilter(fieldPath, EQUAL, value));
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and its value does not equal the specified value.
   *
   * @param field The name of the field to compare.
   * @param value The value for comparison.
   * @return The created Query.
   */
  @Nonnull
  public Query whereNotEqualTo(@Nonnull String field, @Nullable Object value) {
    return whereNotEqualTo(FieldPath.fromDotSeparatedString(field), value);
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value does not equal the specified value.
   *
   * @param fieldPath The path of the field to compare.
   * @param value The value for comparison.
   * @return The created Query.
   */
  @Nonnull
  public Query whereNotEqualTo(@Nonnull FieldPath fieldPath, @Nullable Object value) {
    return where(new com.google.cloud.firestore.Filter.UnaryFilter(fieldPath, NOT_EQUAL, value));
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value should be less than the specified value.
   *
   * @param field The name of the field to compare.
   * @param value The value for comparison.
   * @return The created Query.
   */
  @Nonnull
  public Query whereLessThan(@Nonnull String field, @Nonnull Object value) {
    return whereLessThan(FieldPath.fromDotSeparatedString(field), value);
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value should be less than the specified value.
   *
   * @param fieldPath The path of the field to compare.
   * @param value The value for comparison.
   * @return The created Query.
   */
  @Nonnull
  public Query whereLessThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
    return where(new com.google.cloud.firestore.Filter.UnaryFilter(fieldPath, LESS_THAN, value));
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value should be less or equal to the specified value.
   *
   * @param field The name of the field to compare.
   * @param value The value for comparison.
   * @return The created Query.
   */
  @Nonnull
  public Query whereLessThanOrEqualTo(@Nonnull String field, @Nonnull Object value) {
    return whereLessThanOrEqualTo(FieldPath.fromDotSeparatedString(field), value);
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value should be less or equal to the specified value.
   *
   * @param fieldPath The path of the field to compare.
   * @param value The value for comparison.
   * @return The created Query.
   */
  @Nonnull
  public Query whereLessThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
    return where(
        new com.google.cloud.firestore.Filter.UnaryFilter(fieldPath, LESS_THAN_OR_EQUAL, value));
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value should be greater than the specified value.
   *
   * @param field The name of the field to compare.
   * @param value The value for comparison.
   * @return The created Query.
   */
  @Nonnull
  public Query whereGreaterThan(@Nonnull String field, @Nonnull Object value) {
    return whereGreaterThan(FieldPath.fromDotSeparatedString(field), value);
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value should be greater than the specified value.
   *
   * @param fieldPath The path of the field to compare.
   * @param value The value for comparison.
   * @return The created Query.
   */
  @Nonnull
  public Query whereGreaterThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
    return where(new com.google.cloud.firestore.Filter.UnaryFilter(fieldPath, GREATER_THAN, value));
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value should be greater than or equal to the specified value.
   *
   * @param field The name of the field to compare.
   * @param value The value for comparison.
   * @return The created Query.
   */
  @Nonnull
  public Query whereGreaterThanOrEqualTo(@Nonnull String field, @Nonnull Object value) {
    return whereGreaterThanOrEqualTo(FieldPath.fromDotSeparatedString(field), value);
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value should be greater than or equal to the specified value.
   *
   * @param fieldPath The path of the field to compare.
   * @param value The value for comparison.
   * @return The created Query.
   */
  @Nonnull
  public Query whereGreaterThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
    return where(
        new com.google.cloud.firestore.Filter.UnaryFilter(fieldPath, GREATER_THAN_OR_EQUAL, value));
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field, the value must be an array, and that the array must contain the provided
   * value.
   *
   * <p>A Query can have only one whereArrayContains() filter and it cannot be combined with
   * whereArrayContainsAny().
   *
   * @param field The name of the field containing an array to search
   * @param value The value that must be contained in the array
   * @return The created Query.
   */
  @Nonnull
  public Query whereArrayContains(@Nonnull String field, @Nonnull Object value) {
    return whereArrayContains(FieldPath.fromDotSeparatedString(field), value);
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field, the value must be an array, and that the array must contain the provided
   * value.
   *
   * <p>A Query can have only one whereArrayContains() filter and it cannot be combined with
   * whereArrayContainsAny().
   *
   * @param fieldPath The path of the field containing an array to search
   * @param value The value that must be contained in the array
   * @return The created Query.
   */
  @Nonnull
  public Query whereArrayContains(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
    return where(
        new com.google.cloud.firestore.Filter.UnaryFilter(fieldPath, ARRAY_CONTAINS, value));
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field, the value must be an array, and that the array must contain at least one value
   * from the provided list.
   *
   * <p>A Query can have only one whereArrayContainsAny() filter and it cannot be combined with
   * whereArrayContains() or whereIn().
   *
   * @param field The name of the field containing an array to search.
   * @param values A list that contains the values to match.
   * @return The created Query.
   */
  @Nonnull
  public Query whereArrayContainsAny(
      @Nonnull String field, @Nonnull List<? extends Object> values) {
    return whereArrayContainsAny(FieldPath.fromDotSeparatedString(field), values);
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field, the value must be an array, and that the array must contain at least one value
   * from the provided list.
   *
   * <p>A Query can have only one whereArrayContainsAny() filter and it cannot be combined with
   * whereArrayContains() or whereIn().
   *
   * @param fieldPath The path of the field containing an array to search.
   * @param values A list that contains the values to match.
   * @return The created Query.
   */
  @Nonnull
  public Query whereArrayContainsAny(
      @Nonnull FieldPath fieldPath, @Nonnull List<? extends Object> values) {
    return where(
        new com.google.cloud.firestore.Filter.UnaryFilter(fieldPath, ARRAY_CONTAINS_ANY, values));
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value must equal one of the values from the provided list.
   *
   * <p>A Query can have only one whereIn() filter, and it cannot be combined with
   * whereArrayContainsAny().
   *
   * @param field The name of the field to search.
   * @param values A list that contains the values to match.
   * @return The created Query.
   */
  @Nonnull
  public Query whereIn(@Nonnull String field, @Nonnull List<? extends Object> values) {
    return whereIn(FieldPath.fromDotSeparatedString(field), values);
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value must equal one of the values from the provided list.
   *
   * <p>A Query can have only one whereIn() filter, and it cannot be combined with
   * whereArrayContainsAny().
   *
   * @param fieldPath The path of the field to search.
   * @param values A list that contains the values to match.
   * @return The created Query.
   */
  @Nonnull
  public Query whereIn(@Nonnull FieldPath fieldPath, @Nonnull List<? extends Object> values) {
    return where(new com.google.cloud.firestore.Filter.UnaryFilter(fieldPath, IN, values));
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value does not equal any of the values from the provided list.
   *
   * <p>A Query can have only one whereNotIn() filter and it cannot be combined with
   * whereArrayContains(), whereArrayContainsAny(), whereIn(), or whereNotEqualTo().
   *
   * @param field The name of the field to search.
   * @param values The list that contains the values to match.
   * @return The created Query.
   */
  @Nonnull
  public Query whereNotIn(@Nonnull String field, @Nonnull List<? extends Object> values) {
    return whereNotIn(FieldPath.fromDotSeparatedString(field), values);
  }

  /**
   * Creates and returns a new Query with the additional filter that documents must contain the
   * specified field and the value does not equal any of the values from the provided list.
   *
   * <p>A Query can have only one whereNotIn() filter, and it cannot be combined with
   * whereArrayContains(), whereArrayContainsAny(), whereIn(), or whereNotEqualTo().
   *
   * @param fieldPath The path of the field to search.
   * @param values The list that contains the values to match.
   * @return The created Query.
   */
  @Nonnull
  public Query whereNotIn(@Nonnull FieldPath fieldPath, @Nonnull List<? extends Object> values) {
    return where(new com.google.cloud.firestore.Filter.UnaryFilter(fieldPath, NOT_IN, values));
  }

  /**
   * Creates and returns a new Query with the additional filter.
   *
   * @param filter The new filter to apply to the existing query.
   * @return The newly created Query.
   */
  public Query where(com.google.cloud.firestore.Filter filter) {
    Preconditions.checkState(
        options.getStartCursor() == null && options.getEndCursor() == null,
        "Cannot call a where() clause after defining a boundary with startAt(), "
            + "startAfter(), endBefore() or endAt().");
    FilterInternal parsedFilter = parseFilter(filter);
    if (parsedFilter.getFilters().isEmpty()) {
      // Return the existing query if not adding any more filters (e.g. an empty composite filter).
      return this;
    }
    Builder newOptions = options.toBuilder();
    newOptions.setFilters(append(options.getFilters(), parsedFilter));
    return new Query(rpcContext, newOptions.build());
  }

  FilterInternal parseFilter(com.google.cloud.firestore.Filter filter) {
    if (filter instanceof com.google.cloud.firestore.Filter.UnaryFilter) {
      return parseFieldFilter((com.google.cloud.firestore.Filter.UnaryFilter) filter);
    }
    return parseCompositeFilter((com.google.cloud.firestore.Filter.CompositeFilter) filter);
  }

  private FieldFilterInternal parseFieldFilter(
      com.google.cloud.firestore.Filter.UnaryFilter fieldFilterData) {
    Object value = fieldFilterData.getValue();
    Operator operator = fieldFilterData.getOperator();
    FieldPath fieldPath = fieldFilterData.getField();

    if (isUnaryComparison(value)) {
      if (operator.equals(EQUAL) || operator.equals(NOT_EQUAL)) {
        StructuredQuery.UnaryFilter.Operator unaryOp =
            operator.equals(EQUAL)
                ? (value == null
                    ? StructuredQuery.UnaryFilter.Operator.IS_NULL
                    : StructuredQuery.UnaryFilter.Operator.IS_NAN)
                : (value == null
                    ? StructuredQuery.UnaryFilter.Operator.IS_NOT_NULL
                    : StructuredQuery.UnaryFilter.Operator.IS_NOT_NAN);
        return new UnaryFilterInternal(fieldPath.toProto(), unaryOp);
      } else {
        throw new IllegalArgumentException(
            String.format(
                "Cannot use '%s' in field comparison. Use an equality filter instead.", value));
      }
    } else {
      if (fieldPath.equals(FieldPath.DOCUMENT_ID)) {
        if (operator.equals(ARRAY_CONTAINS) || operator.equals(ARRAY_CONTAINS_ANY)) {
          throw new IllegalArgumentException(
              String.format(
                  "Invalid query. You cannot perform '%s' queries on FieldPath.documentId().",
                  operator.toString()));
        } else if (operator.equals(IN) || operator.equals(NOT_IN)) {
          if (!(value instanceof List) || ((List<?>) value).isEmpty()) {
            throw new IllegalArgumentException(
                String.format(
                    "Invalid Query. A non-empty array is required for '%s' filters.",
                    operator.toString()));
          }
          List<Object> referenceList = new ArrayList<>();
          for (Object arrayValue : (List<Object>) value) {
            Object convertedValue = this.convertReference(arrayValue);
            referenceList.add(convertedValue);
          }
          value = referenceList;
        } else {
          value = this.convertReference(value);
        }
      }
      return new ComparisonFilterInternal(
          fieldPath.toProto(), operator, encodeValue(fieldPath, value));
    }
  }

  private FilterInternal parseCompositeFilter(
      com.google.cloud.firestore.Filter.CompositeFilter compositeFilterData) {
    List<FilterInternal> parsedFilters = new ArrayList<>();
    for (com.google.cloud.firestore.Filter filter : compositeFilterData.getFilters()) {
      FilterInternal parsedFilter = parseFilter(filter);
      if (!parsedFilter.getFilters().isEmpty()) {
        parsedFilters.add(parsedFilter);
      }
    }

    // For composite filters containing 1 filter, return the only filter.
    // For example: AND(FieldFilter1) == FieldFilter1
    if (parsedFilters.size() == 1) {
      return parsedFilters.get(0);
    }
    return new CompositeFilterInternal(parsedFilters, compositeFilterData.getOperator());
  }

  /**
   * Creates and returns a new Query that's additionally sorted by the specified field.
   *
   * @param field The field to sort by.
   * @return The created Query.
   */
  @Nonnull
  public Query orderBy(@Nonnull String field) {
    return orderBy(FieldPath.fromDotSeparatedString(field), Direction.ASCENDING);
  }

  /**
   * Creates and returns a new Query that's additionally sorted by the specified field.
   *
   * @param fieldPath The field to sort by.
   * @return The created Query.
   */
  @Nonnull
  public Query orderBy(@Nonnull FieldPath fieldPath) {
    return orderBy(fieldPath, Direction.ASCENDING);
  }

  /**
   * Creates and returns a new Query that's additionally sorted by the specified field, optionally
   * in descending order instead of ascending.
   *
   * @param field The field to sort by.
   * @param direction The direction to sort.
   * @return The created Query.
   */
  @Nonnull
  public Query orderBy(@Nonnull String field, @Nonnull Direction direction) {
    return orderBy(FieldPath.fromDotSeparatedString(field), direction);
  }

  /**
   * Creates and returns a new Query that's additionally sorted by the specified field, optionally
   * in descending order instead of ascending.
   *
   * @param fieldPath The field to sort by.
   * @param direction The direction to sort.
   * @return The created Query.
   */
  @Nonnull
  public Query orderBy(@Nonnull FieldPath fieldPath, @Nonnull Direction direction) {
    Preconditions.checkState(
        options.getStartCursor() == null && options.getEndCursor() == null,
        "Cannot specify an orderBy() constraint after calling startAt(), "
            + "startAfter(), endBefore() or endAt().");

    Builder newOptions = options.toBuilder();
    FieldOrder newFieldOrder = new FieldOrder(fieldPath.toProto(), direction);
    newOptions.setFieldOrders(append(options.getFieldOrders(), newFieldOrder));

    return new Query(rpcContext, newOptions.build());
  }

  /**
   * Creates and returns a new Query that only returns the first matching documents.
   *
   * @param limit The maximum number of items to return.
   * @return The created Query.
   */
  @Nonnull
  public Query limit(int limit) {
    return new Query(
        rpcContext, options.toBuilder().setLimit(limit).setLimitType(LimitType.First).build());
  }

  /**
   * Creates and returns a new Query that only returns the last matching documents.
   *
   * <p>You must specify at least one orderBy clause for limitToLast queries. Otherwise, an {@link
   * java.lang.IllegalStateException} is thrown during execution.
   *
   * <p>Results for limitToLast() queries are only available once all documents are received. Hence,
   * limitToLast() queries cannot be streamed via the {@link #stream(ApiStreamObserver)} API.
   *
   * @param limit the maximum number of items to return
   * @return the created Query
   */
  @Nonnull
  public Query limitToLast(int limit) {
    return new Query(
        rpcContext, options.toBuilder().setLimit(limit).setLimitType(LimitType.Last).build());
  }

  /**
   * Creates and returns a new Query that skips the first n results.
   *
   * @param offset The number of items to skip.
   * @return The created Query.
   */
  @Nonnull
  public Query offset(int offset) {
    return new Query(rpcContext, options.toBuilder().setOffset(offset).build());
  }

  /**
   * Creates and returns a new Query that starts at the provided document (inclusive). The starting
   * position is relative to the order of the query. The document must contain all of the fields
   * provided in the orderBy of this query.
   *
   * @param snapshot The snapshot of the document to start at.
   * @return The created Query.
   */
  @Nonnull
  public Query startAt(@Nonnull DocumentSnapshot snapshot) {
    ImmutableList<FieldOrder> fieldOrders = createImplicitOrderBy();
    Cursor cursor = createCursor(fieldOrders, snapshot, true);

    Builder newOptions = options.toBuilder();
    newOptions.setFieldOrders(fieldOrders);
    newOptions.setStartCursor(cursor);
    return new Query(rpcContext, newOptions.build());
  }

  /**
   * Creates and returns a new Query that starts at the provided fields relative to the order of the
   * query. The order of the field values must match the order of the order by clauses of the query.
   *
   * @param fieldValues The field values to start this query at, in order of the query's order by.
   * @return The created Query.
   */
  @Nonnull
  public Query startAt(Object... fieldValues) {
    // TODO(b/296435819): Remove this warning message.
    warningOnSingleDocumentReference(fieldValues);

    ImmutableList<FieldOrder> fieldOrders =
        fieldValues.length == 1 && fieldValues[0] instanceof DocumentReference
            ? createImplicitOrderBy()
            : options.getFieldOrders();
    Cursor cursor = createCursor(fieldOrders, fieldValues, true);

    Builder newOptions = options.toBuilder();
    newOptions.setFieldOrders(fieldOrders);
    newOptions.setStartCursor(cursor);
    return new Query(rpcContext, newOptions.build());
  }

  /**
   * Creates and returns a new Query instance that applies a field mask to the result and returns
   * the specified subset of fields. You can specify a list of field paths to return, or use an
   * empty list to only return the references of matching documents.
   *
   * @param fields The fields to include.
   * @return The created Query.
   */
  @Nonnull
  public Query select(String... fields) {
    FieldPath[] fieldPaths = new FieldPath[fields.length];

    for (int i = 0; i < fields.length; ++i) {
      fieldPaths[i] = FieldPath.fromDotSeparatedString(fields[i]);
    }

    return select(fieldPaths);
  }

  /**
   * Creates and returns a new Query instance that applies a field mask to the result and returns
   * the specified subset of fields. You can specify a list of field paths to return, or use an
   * empty list to only return the references of matching documents.
   *
   * @param fieldPaths The field paths to include.
   * @return The created Query.
   */
  @Nonnull
  public Query select(FieldPath... fieldPaths) {
    ImmutableList.Builder<FieldReference> fieldProjections = ImmutableList.builder();

    if (fieldPaths.length == 0) {
      fieldPaths = new FieldPath[] {FieldPath.DOCUMENT_ID};
    }

    for (FieldPath path : fieldPaths) {
      FieldReference fieldReference =
          FieldReference.newBuilder().setFieldPath(path.getEncodedPath()).build();
      fieldProjections.add(fieldReference);
    }

    Builder newOptions = options.toBuilder().setFieldProjections(fieldProjections.build());
    return new Query(rpcContext, newOptions.build());
  }

  /**
   * Creates and returns a new Query that starts after the provided document (exclusive). The
   * starting position is relative to the order of the query. The document must contain all of the
   * fields provided in the orderBy of this query.
   *
   * @param snapshot The snapshot of the document to start after.
   * @return The created Query.
   */
  @Nonnull
  public Query startAfter(@Nonnull DocumentSnapshot snapshot) {
    ImmutableList<FieldOrder> fieldOrders = createImplicitOrderBy();
    Cursor cursor = createCursor(fieldOrders, snapshot, false);

    Builder newOptions = options.toBuilder();
    newOptions.setFieldOrders(fieldOrders);
    newOptions.setStartCursor(cursor);
    return new Query(rpcContext, newOptions.build());
  }

  /**
   * Creates and returns a new Query that starts after the provided fields relative to the order of
   * the query. The order of the field values must match the order of the order by clauses of the
   * query.
   *
   * @param fieldValues The field values to start this query after, in order of the query's order
   *     by.
   * @return The created Query.
   */
  public Query startAfter(Object... fieldValues) {
    // TODO(b/296435819): Remove this warning message.
    warningOnSingleDocumentReference(fieldValues);

    ImmutableList<FieldOrder> fieldOrders =
        fieldValues.length == 1 && fieldValues[0] instanceof DocumentReference
            ? createImplicitOrderBy()
            : options.getFieldOrders();
    Cursor cursor = createCursor(fieldOrders, fieldValues, false);

    Builder newOptions = options.toBuilder();
    newOptions.setFieldOrders(fieldOrders);
    newOptions.setStartCursor(cursor);
    return new Query(rpcContext, newOptions.build());
  }

  /**
   * Creates and returns a new Query that ends before the provided document (exclusive). The end
   * position is relative to the order of the query. The document must contain all of the fields
   * provided in the orderBy of this query.
   *
   * @param snapshot The snapshot of the document to end before.
   * @return The created Query.
   */
  @Nonnull
  public Query endBefore(@Nonnull DocumentSnapshot snapshot) {
    ImmutableList<FieldOrder> fieldOrders = createImplicitOrderBy();
    Cursor cursor = createCursor(fieldOrders, snapshot, true);

    Builder newOptions = options.toBuilder();
    newOptions.setFieldOrders(fieldOrders);
    newOptions.setEndCursor(cursor);
    return new Query(rpcContext, newOptions.build());
  }

  /**
   * Creates and returns a new Query that ends before the provided fields relative to the order of
   * the query. The order of the field values must match the order of the order by clauses of the
   * query.
   *
   * @param fieldValues The field values to end this query before, in order of the query's order by.
   * @return The created Query.
   */
  @Nonnull
  public Query endBefore(Object... fieldValues) {
    // TODO(b/296435819): Remove this warning message.
    warningOnSingleDocumentReference(fieldValues);

    ImmutableList<FieldOrder> fieldOrders =
        fieldValues.length == 1 && fieldValues[0] instanceof DocumentReference
            ? createImplicitOrderBy()
            : options.getFieldOrders();
    Cursor cursor = createCursor(fieldOrders, fieldValues, true);

    Builder newOptions = options.toBuilder();
    newOptions.setFieldOrders(fieldOrders);
    newOptions.setEndCursor(cursor);
    return new Query(rpcContext, newOptions.build());
  }

  /**
   * Creates and returns a new Query that ends at the provided fields relative to the order of the
   * query. The order of the field values must match the order of the order by clauses of the query.
   *
   * @param fieldValues The field values to end this query at, in order of the query's order by.
   * @return The created Query.
   */
  @Nonnull
  public Query endAt(Object... fieldValues) {
    // TODO(b/296435819): Remove this warning message.
    warningOnSingleDocumentReference(fieldValues);

    ImmutableList<FieldOrder> fieldOrders =
        fieldValues.length == 1 && fieldValues[0] instanceof DocumentReference
            ? createImplicitOrderBy()
            : options.getFieldOrders();
    Cursor cursor = createCursor(fieldOrders, fieldValues, false);

    Builder newOptions = options.toBuilder();
    newOptions.setFieldOrders(fieldOrders);
    newOptions.setEndCursor(cursor);
    return new Query(rpcContext, newOptions.build());
  }

  private void warningOnSingleDocumentReference(Object... fieldValues) {
    if (fieldValues.length == 1 && fieldValues[0] instanceof DocumentReference) {
      LOGGER.warning(
          "Warning: Passing DocumentReference into a cursor without orderBy clause is not an intended "
              + "behavior. Please use DocumentSnapshot or add an explicit orderBy on document key field.");
    }
  }

  /**
   * Creates and returns a new Query that ends at the provided document (inclusive). The end
   * position is relative to the order of the query. The document must contain all of the fields
   * provided in the orderBy of this query.
   *
   * @param snapshot The snapshot of the document to end at.
   * @return The created Query.
   */
  @Nonnull
  public Query endAt(@Nonnull DocumentSnapshot snapshot) {
    ImmutableList<FieldOrder> fieldOrders = createImplicitOrderBy();
    Cursor cursor = createCursor(fieldOrders, snapshot, false);

    Builder newOptions = options.toBuilder();
    newOptions.setFieldOrders(fieldOrders);
    newOptions.setEndCursor(cursor);
    return new Query(rpcContext, newOptions.build());
  }

  /** Build the final Firestore query. */
  StructuredQuery.Builder buildQuery() {
    StructuredQuery.Builder structuredQuery = buildWithoutClientTranslation();
    if (options.getLimitType().equals(LimitType.Last)) {
      structuredQuery.clearOrderBy();
      structuredQuery.clearStartAt();
      structuredQuery.clearEndAt();

      // Apply client translation for limitToLast.
      if (!options.getFieldOrders().isEmpty()) {
        for (FieldOrder order : options.getFieldOrders()) {
          // Flip the orderBy directions since we want the last results
          order =
              new FieldOrder(
                  order.fieldReference,
                  order.direction.equals(Direction.ASCENDING)
                      ? Direction.DESCENDING
                      : Direction.ASCENDING);
          structuredQuery.addOrderBy(order.toProto());
        }
      }

      if (options.getStartCursor() != null) {
        // Swap the cursors to match the flipped query ordering.
        Cursor cursor =
            options
                .getStartCursor()
                .toBuilder()
                .setBefore(!options.getStartCursor().getBefore())
                .build();
        structuredQuery.setEndAt(cursor);
      }

      if (options.getEndCursor() != null) {
        // Swap the cursors to match the flipped query ordering.
        Cursor cursor =
            options
                .getEndCursor()
                .toBuilder()
                .setBefore(!options.getEndCursor().getBefore())
                .build();
        structuredQuery.setStartAt(cursor);
      }
    }

    return structuredQuery;
  }

  /**
   * Builds a {@link BundledQuery} that is able to be saved in a bundle file.
   *
   * <p>This will not perform any limitToLast order flip, as {@link BundledQuery} has first class
   * representation via {@link BundledQuery.LimitType}.
   */
  BundledQuery toBundledQuery() {
    StructuredQuery.Builder structuredQuery = buildWithoutClientTranslation();

    return BundledQuery.newBuilder()
        .setStructuredQuery(structuredQuery)
        .setParent(options.getParentPath().toString())
        .setLimitType(
            options.getLimitType().equals(LimitType.Last)
                ? BundledQuery.LimitType.LAST
                : BundledQuery.LimitType.FIRST)
        .build();
  }

  private StructuredQuery.Builder buildWithoutClientTranslation() {
    StructuredQuery.Builder structuredQuery = StructuredQuery.newBuilder();
    CollectionSelector.Builder collectionSelector = CollectionSelector.newBuilder();

    // Kindless queries select all descendant documents, so we don't add the collectionId field.
    if (!options.isKindless()) {
      collectionSelector.setCollectionId(options.getCollectionId());
    }

    collectionSelector.setAllDescendants(options.getAllDescendants());
    structuredQuery.addFrom(collectionSelector);

    // There's an implicit AND operation between the top-level query filters.
    if (!options.getFilters().isEmpty()) {
      FilterInternal filter =
          new CompositeFilterInternal(options.getFilters(), CompositeFilter.Operator.AND);
      structuredQuery.setWhere(filter.toProto());
    }

    if (!options.getFieldOrders().isEmpty()) {
      for (FieldOrder order : options.getFieldOrders()) {
        structuredQuery.addOrderBy(order.toProto());
      }
    } else if (LimitType.Last.equals(options.getLimitType())) {
      throw new IllegalStateException(
          "limitToLast() queries require specifying at least one orderBy() clause.");
    }

    if (!options.getFieldProjections().isEmpty()) {
      structuredQuery.getSelectBuilder().addAllFields(options.getFieldProjections());
    }

    if (options.getLimit() != null) {
      structuredQuery.setLimit(Int32Value.newBuilder().setValue(options.getLimit()));
    }

    if (options.getOffset() != null) {
      structuredQuery.setOffset(options.getOffset());
    }

    if (options.getStartCursor() != null) {
      structuredQuery.setStartAt(options.getStartCursor());
    }

    if (options.getEndCursor() != null) {
      structuredQuery.setEndAt(options.getEndCursor());
    }

    return structuredQuery;
  }

  /**
   * Executes the query and streams the results as a StreamObserver of DocumentSnapshots.
   *
   * @param responseObserver The observer to be notified when results arrive.
   */
  public void stream(@Nonnull final ApiStreamObserver<DocumentSnapshot> responseObserver) {
    Preconditions.checkState(
        !LimitType.Last.equals(Query.this.options.getLimitType()),
        "Query results for queries that include limitToLast() constraints cannot be streamed. "
            + "Use Query.get() instead.");

    internalStream(
        new QuerySnapshotObserver() {
          boolean hasCompleted = false;

          @Override
          public void onNext(QueryDocumentSnapshot documentSnapshot) {
            responseObserver.onNext(documentSnapshot);
          }

          @Override
          public void onError(Throwable throwable) {
            responseObserver.onError(throwable);
          }

          @Override
          public void onCompleted() {
            if (hasCompleted) return;
            hasCompleted = true;
            responseObserver.onCompleted();
          }
        },
        /* startTimeNanos= */ rpcContext.getClock().nanoTime(),
        /* transactionId= */ null,
        /* readTime= */ null);
  }

  /**
   * Returns the {@link RunQueryRequest} that this Query instance represents. The request contains
   * the serialized form of all Query constraints.
   *
   * <p>Runtime metadata (as required for `limitToLast()` queries) is not serialized and as such,
   * the serialized request will return the results in the original backend order.
   *
   * @return the serialized RunQueryRequest
   */
  public RunQueryRequest toProto() {
    RunQueryRequest.Builder request = RunQueryRequest.newBuilder();
    request.setStructuredQuery(buildQuery()).setParent(options.getParentPath().toString());
    return request.build();
  }

  /**
   * Returns a Query instance that can be used to execute the provided {@link RunQueryRequest}.
   *
   * <p>Only RunQueryRequests that pertain to the same project as the Firestore instance can be
   * deserialized.
   *
   * <p>Runtime metadata (as required for `limitToLast()` queries) is not restored and as such, the
   * results for limitToLast() queries will be returned in the original backend order.
   *
   * @param firestore a Firestore instance to apply the query to
   * @param proto the serialized RunQueryRequest
   * @return a Query instance that can be used to execute the RunQueryRequest
   */
  public static Query fromProto(Firestore firestore, RunQueryRequest proto) {
    Preconditions.checkState(
        FirestoreRpcContext.class.isAssignableFrom(firestore.getClass()),
        "The firestore instance passed to this method must also implement FirestoreRpcContext.");
    return fromProto((FirestoreRpcContext<?>) firestore, proto);
  }

  private static Query fromProto(FirestoreRpcContext<?> rpcContext, RunQueryRequest proto) {
    QueryOptions.Builder queryOptions = QueryOptions.builder();
    StructuredQuery structuredQuery = proto.getStructuredQuery();

    ResourcePath parentPath = ResourcePath.create(proto.getParent());
    if (!rpcContext.getDatabaseName().equals(parentPath.getDatabaseName().toString())) {
      throw new IllegalArgumentException(
          String.format(
              "Cannot deserialize query from different Firestore project (\"%s\" vs \"%s\")",
              rpcContext.getDatabaseName(), parentPath.getDatabaseName()));
    }
    queryOptions.setParentPath(parentPath);

    Preconditions.checkArgument(
        structuredQuery.getFromCount() == 1,
        "Can only deserialize query with exactly one collection selector.");
    queryOptions.setCollectionId(structuredQuery.getFrom(0).getCollectionId());
    queryOptions.setAllDescendants(structuredQuery.getFrom(0).getAllDescendants());

    if (structuredQuery.hasWhere()) {
      FilterInternal filter = FilterInternal.fromProto(structuredQuery.getWhere());

      // There's an implicit AND operation between the top-level query filters.
      if (filter instanceof CompositeFilterInternal
          && ((CompositeFilterInternal) filter).isConjunction()) {
        queryOptions.setFilters(
            new ImmutableList.Builder<FilterInternal>().addAll(filter.getFilters()).build());
      } else {
        queryOptions.setFilters(ImmutableList.of(filter));
      }
    }

    ImmutableList.Builder<FieldOrder> fieldOrders = ImmutableList.builder();
    for (Order order : structuredQuery.getOrderByList()) {
      fieldOrders.add(
          new FieldOrder(order.getField(), Direction.valueOf(order.getDirection().name())));
    }
    queryOptions.setFieldOrders(fieldOrders.build());

    if (structuredQuery.hasLimit()) {
      queryOptions.setLimit(structuredQuery.getLimit().getValue());
    }

    if (structuredQuery.getOffset() != 0) {
      queryOptions.setOffset(structuredQuery.getOffset());
    }

    if (structuredQuery.hasSelect()) {
      queryOptions.setFieldProjections(
          ImmutableList.copyOf(structuredQuery.getSelect().getFieldsList()));
    }

    if (structuredQuery.hasStartAt()) {
      queryOptions.setStartCursor(structuredQuery.getStartAt());
    }

    if (structuredQuery.hasEndAt()) {
      queryOptions.setEndCursor(structuredQuery.getEndAt());
    }

    return new Query(rpcContext, queryOptions.build());
  }

  private Value encodeValue(FieldReference fieldReference, Object value) {
    return encodeValue(FieldPath.fromDotSeparatedString(fieldReference.getFieldPath()), value);
  }

  private Value encodeValue(FieldPath fieldPath, Object value) {
    Object sanitizedObject = CustomClassMapper.serialize(value);
    Value encodedValue =
        UserDataConverter.encodeValue(fieldPath, sanitizedObject, UserDataConverter.ARGUMENT);
    if (encodedValue == null) {
      throw FirestoreException.forInvalidArgument(
          "Cannot use Firestore sentinels in FieldFilter or cursors");
    }
    return encodedValue;
  }

  /** Stream observer that captures DocumentSnapshots as well as the Query read time. */
  private abstract static class QuerySnapshotObserver
      implements ApiStreamObserver<QueryDocumentSnapshot> {

    private Timestamp readTime;

    void onCompleted(Timestamp readTime) {
      this.readTime = readTime;
      this.onCompleted();
    }

    Timestamp getReadTime() {
      return readTime;
    }
  }

  private void internalStream(
      final QuerySnapshotObserver documentObserver,
      final long startTimeNanos,
      @Nullable final ByteString transactionId,
      @Nullable final Timestamp readTime) {
    RunQueryRequest.Builder request = RunQueryRequest.newBuilder();
    request.setStructuredQuery(buildQuery()).setParent(options.getParentPath().toString());

    if (transactionId != null) {
      request.setTransaction(transactionId);
    }
    if (readTime != null) {
      request.setReadTime(readTime.toProto());
    }

    Tracing.getTracer()
        .getCurrentSpan()
        .addAnnotation(
            TraceUtil.SPAN_NAME_RUNQUERY + ": Start",
            ImmutableMap.of(
                "transactional", AttributeValue.booleanAttributeValue(transactionId != null)));

    final AtomicReference<QueryDocumentSnapshot> lastReceivedDocument = new AtomicReference<>();

    ResponseObserver<RunQueryResponse> observer =
        new ResponseObserver<RunQueryResponse>() {
          Timestamp readTime;
          boolean firstResponse;
          int numDocuments;

          @Override
          public void onStart(StreamController streamController) {}

          @Override
          public void onResponse(RunQueryResponse response) {
            if (!firstResponse) {
              firstResponse = true;
              Tracing.getTracer().getCurrentSpan().addAnnotation("Firestore.Query: First response");
            }
            if (response.hasDocument()) {
              numDocuments++;
              if (numDocuments % 100 == 0) {
                Tracing.getTracer()
                    .getCurrentSpan()
                    .addAnnotation("Firestore.Query: Received 100 documents");
              }
              Document document = response.getDocument();
              QueryDocumentSnapshot documentSnapshot =
                  QueryDocumentSnapshot.fromDocument(
                      rpcContext, Timestamp.fromProto(response.getReadTime()), document);
              documentObserver.onNext(documentSnapshot);
              lastReceivedDocument.set(documentSnapshot);
            }

            if (readTime == null) {
              readTime = Timestamp.fromProto(response.getReadTime());
            }

            if (response.getDone()) {
              Tracing.getTracer()
                  .getCurrentSpan()
                  .addAnnotation(
                      "Firestore.Query: Completed",
                      ImmutableMap.of(
                          "numDocuments", AttributeValue.longAttributeValue(numDocuments)));
              documentObserver.onCompleted(readTime);
            }
          }

          @Override
          public void onError(Throwable throwable) {
            QueryDocumentSnapshot cursor = lastReceivedDocument.get();
            if (shouldRetry(cursor, throwable)) {
              Tracing.getTracer()
                  .getCurrentSpan()
                  .addAnnotation("Firestore.Query: Retryable Error");

              Query.this
                  .startAfter(cursor)
                  .internalStream(
                      documentObserver,
                      startTimeNanos,
                      /* transactionId= */ null,
                      options.getRequireConsistency() ? cursor.getReadTime() : null);

            } else {
              Tracing.getTracer().getCurrentSpan().addAnnotation("Firestore.Query: Error");
              documentObserver.onError(throwable);
            }
          }

          @Override
          public void onComplete() {
            Tracing.getTracer()
                .getCurrentSpan()
                .addAnnotation(
                    "Firestore.Query: Completed",
                    ImmutableMap.of(
                        "numDocuments", AttributeValue.longAttributeValue(numDocuments)));
            documentObserver.onCompleted(readTime);
          }

          boolean shouldRetry(DocumentSnapshot lastDocument, Throwable t) {
            if (lastDocument == null) {
              // Only retry if we have received a single result. Retries for RPCs with initial
              // failure are handled by Google Gax, which also implements backoff.
              return false;
            }

            Set<StatusCode.Code> retryableCodes =
                FirestoreSettings.newBuilder().runQuerySettings().getRetryableCodes();
            return shouldRetryQuery(t, transactionId, startTimeNanos, retryableCodes);
          }
        };

    rpcContext.streamRequest(request.build(), observer, rpcContext.getClient().runQueryCallable());
  }

  /**
   * Executes the query and returns the results as QuerySnapshot.
   *
   * @return An ApiFuture that will be resolved with the results of the Query.
   */
  @Nonnull
  public ApiFuture<QuerySnapshot> get() {
    return get(null);
  }

  /**
   * Starts listening to this query.
   *
   * @param listener The event listener that will be called with the snapshots.
   * @return A registration object that can be used to remove the listener.
   */
  @Nonnull
  public ListenerRegistration addSnapshotListener(@Nonnull EventListener<QuerySnapshot> listener) {
    return addSnapshotListener(rpcContext.getClient().getExecutor(), listener);
  }

  /**
   * Starts listening to this query.
   *
   * @param executor The executor to use to call the listener.
   * @param listener The event listener that will be called with the snapshots.
   * @return A registration object that can be used to remove the listener.
   */
  @Nonnull
  public ListenerRegistration addSnapshotListener(
      @Nonnull Executor executor, @Nonnull EventListener<QuerySnapshot> listener) {
    return Watch.forQuery(this).runWatch(executor, listener);
  }

  ApiFuture<QuerySnapshot> get(@Nullable ByteString transactionId) {
    final SettableApiFuture<QuerySnapshot> result = SettableApiFuture.create();

    internalStream(
        new QuerySnapshotObserver() {
          final List<QueryDocumentSnapshot> documentSnapshots = new ArrayList<>();
          // The stream's onCompleted could be called more than once,
          // this flag makes sure only the first one is actually processed.
          boolean hasCompleted = false;

          @Override
          public void onNext(QueryDocumentSnapshot documentSnapshot) {
            documentSnapshots.add(documentSnapshot);
          }

          @Override
          public void onError(Throwable throwable) {
            result.setException(throwable);
          }

          @Override
          public void onCompleted() {
            if (hasCompleted) return;
            hasCompleted = true;

            // The results for limitToLast queries need to be flipped since we reversed the
            // ordering constraints before sending the query to the backend.
            List<QueryDocumentSnapshot> resultView =
                LimitType.Last.equals(Query.this.options.getLimitType())
                    ? reverse(documentSnapshots)
                    : documentSnapshots;
            QuerySnapshot querySnapshot =
                QuerySnapshot.withDocuments(Query.this, this.getReadTime(), resultView);
            result.set(querySnapshot);
          }
        },
        /* startTimeNanos= */ rpcContext.getClock().nanoTime(),
        transactionId,
        /* readTime= */ null);

    return result;
  }

  Comparator<QueryDocumentSnapshot> comparator() {
    return (doc1, doc2) -> {
      // Add implicit sorting by name, using the last specified direction.
      ImmutableList<FieldOrder> fieldOrders = options.getFieldOrders();
      Direction lastDirection =
          fieldOrders.isEmpty()
              ? Direction.ASCENDING
              : fieldOrders.get(fieldOrders.size() - 1).direction;

      List<FieldOrder> orderBys = new ArrayList<>(fieldOrders);
      orderBys.add(new FieldOrder(FieldPath.DOCUMENT_ID.toProto(), lastDirection));

      for (FieldOrder orderBy : orderBys) {
        int comp;

        String path = orderBy.fieldReference.getFieldPath();
        if (FieldPath.isDocumentId(path)) {
          comp =
              doc1.getReference()
                  .getResourcePath()
                  .compareTo(doc2.getReference().getResourcePath());
        } else {
          FieldPath fieldPath = FieldPath.fromDotSeparatedString(path);
          Preconditions.checkState(
              doc1.contains(fieldPath) && doc2.contains(fieldPath),
              "Can only compare fields that exist in the DocumentSnapshot."
                  + " Please include the fields you are ordering on in your select() call.");
          Value v1 = doc1.extractField(fieldPath);
          Value v2 = doc2.extractField(fieldPath);

          comp = com.google.cloud.firestore.Order.INSTANCE.compare(v1, v2);
        }

        if (comp != 0) {
          int direction = orderBy.direction.equals(Direction.ASCENDING) ? 1 : -1;
          return direction * comp;
        }
      }

      return 0;
    };
  }

  /**
   * Helper method to append an element to an existing ImmutableList. Returns the newly created
   * list.
   */
  private <T> ImmutableList<T> append(ImmutableList<T> existingList, T newElement) {
    ImmutableList.Builder<T> builder = ImmutableList.builder();
    builder.addAll(existingList);
    builder.add(newElement);
    return builder.build();
  }

  /** Verifies whether the given exception is retryable based on the RunQuery configuration. */
  private boolean isRetryableError(Throwable throwable, Set<StatusCode.Code> retryableCodes) {
    if (!(throwable instanceof FirestoreException)) {
      return false;
    }
    Status status = ((FirestoreException) throwable).getStatus();
    for (StatusCode.Code code : retryableCodes) {
      if (code.equals(StatusCode.Code.valueOf(status.getCode().name()))) {
        return true;
      }
    }
    return false;
  }

  /** Returns whether a query that failed in the given scenario should be retried. */
  boolean shouldRetryQuery(
      Throwable throwable,
      @Nullable ByteString transactionId,
      long startTimeNanos,
      Set<StatusCode.Code> retryableCodes) {
    if (transactionId != null) {
      // Transactional queries are retried via the transaction runner.
      return false;
    }

    if (!isRetryableError(throwable, retryableCodes)) {
      return false;
    }

    if (rpcContext.getTotalRequestTimeout().isZero()) {
      return true;
    }

    Duration duration = Duration.ofNanos(rpcContext.getClock().nanoTime() - startTimeNanos);
    return duration.compareTo(rpcContext.getTotalRequestTimeout()) < 0;
  }

  /**
   * Returns a query that counts the documents in the result set of this query.
   *
   * <p>The returned query, when executed, counts the documents in the result set of this query
   * <em>without actually downloading the documents</em>.
   *
   * <p>Using the returned query to count the documents is efficient because only the final count,
   * not the documents' data, is downloaded. The returned query can even count the documents if the
   * result set would be prohibitively large to download entirely (e.g. thousands of documents).
   *
   * @return a query that counts the documents in the result set of this query.
   */
  @Nonnull
  public AggregateQuery count() {
    return new AggregateQuery(this);
  }

  /**
   * Returns true if this Query is equal to the provided object.
   *
   * @param obj The object to compare against.
   * @return Whether this Query is equal to the provided object.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || !(obj instanceof Query)) {
      return false;
    }
    Query query = (Query) obj;
    return Objects.equals(rpcContext, query.rpcContext) && Objects.equals(options, query.options);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rpcContext, options);
  }
}
