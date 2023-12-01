package com.google.cloud.firestore.telemetry;

import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;

import javax.annotation.Nullable;
import java.util.Map;

public interface TraceUtil {
    String ATTRIBUTE_SERVICE_PREFIX = "gcp.firestore.";
    String LIBRARY_NAME = "com.google.cloud.firestore";
    String SPAN_NAME_DOC_REF_CREATE = "DocumentReference.Create";
    String SPAN_NAME_DOC_REF_SET = "DocumentReference.Set";
    String SPAN_NAME_DOC_REF_UPDATE = "DocumentReference.Update";
    String SPAN_NAME_DOC_REF_DELETE = "DocumentReference.Delete";
    String SPAN_NAME_DOC_REF_GET = "DocumentReference.Get";
    String SPAN_NAME_DOC_REF_LIST_COLLECTIONS = "DocumentReference.ListCollections";
    String SPAN_NAME_COL_REF_ADD = "CollectionReference.Add";
    String SPAN_NAME_COL_REF_LIST_DOCUMENTS = "CollectionReference.ListDocuments";
    String SPAN_NAME_QUERY_GET = "Query.Get";
    String SPAN_NAME_AGGREGATION_QUERY_GET = "AggregationQuery.Get";
    String SPAN_NAME_RUN_QUERY = "RunQuery";
    String SPAN_NAME_RUN_AGGREGATION_QUERY = "RunAggregationQuery";
    String SPAN_NAME_BATCH_GET_DOCUMENTS = "BatchGetDocuments";
    String SPAN_NAME_TRANSACTION_RUN = "Transaction.Run";
    String SPAN_NAME_TRANSACTION_BEGIN = "Transaction.Begin";
    String SPAN_NAME_TRANSACTION_GET_QUERY = "Transaction.Get.Query";
    String SPAN_NAME_TRANSACTION_GET_AGGREGATION_QUERY = "Transaction.Get.AggregationQuery";
    String SPAN_NAME_TRANSACTION_GET_DOCUMENT = "Transaction.Get.Document";
    String SPAN_NAME_TRANSACTION_GET_DOCUMENTS = "Transaction.Get.Documents";
    String SPAN_NAME_TRANSACTION_ROLLBACK = "Rollback";
    String SPAN_NAME_BATCH_COMMIT = "Batch.Commit";
    String SPAN_NAME_TRANSACTION_COMMIT = "Transaction.Commit";
    String SPAN_NAME_PARTITION_QUERY = "PartitionQuery";
    String SPAN_NAME_BULK_WRITER_COMMIT = "BulkWriter.Commit";

    interface Span {
        /** Ends this span. */
        void end();

        /** Ends this span in an error. */
        void end(Throwable error);

        /**
         * If an operation ends in the future, its relevant span should end _after_ the future has been
         * completed. This method "appends" the span completion code at the completion of the given
         * future. In order for telemetry info to be recorded, the future returned by this method should
         * be completed.
         */
        <T> void endAtFuture(ApiFuture<T> futureValue);

        /** Adds the given event to this span. */
        Span addEvent(String name);

        /** Adds the given event with the given attributes to this span. */
        Span addEvent(String name, Map<String, Object> attributes);

        /** Adds the given attribute to this span */
        Span setAttribute(String key, int value);

        /** Adds the given attribute to this span */
        Span setAttribute(String key, String value);

        Scope makeCurrent();
    }

    /**
     * Returns a channel configurator for gRPC, or {@code null} if telemetry collection is disabled.
     */
    @Nullable
    ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> getChannelConfigurator();

    /** Starts a new span with the given name, sets it as the current span, and returns it. */
    @Nullable
    Span startSpan(String spanName);

    @Nullable
    Span startSpan(String spanName, Context parent);

    /** Returns the current span. */
    @Nullable
    Span currentSpan();
}
