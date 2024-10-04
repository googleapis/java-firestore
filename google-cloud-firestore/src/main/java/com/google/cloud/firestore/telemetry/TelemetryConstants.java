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

package com.google.cloud.firestore.telemetry;

public interface TelemetryConstants {
  String METHOD_NAME_DOC_REF_CREATE = "DocumentReference.Create";
  String METHOD_NAME_DOC_REF_SET = "DocumentReference.Set";
  String METHOD_NAME_DOC_REF_UPDATE = "DocumentReference.Update";
  String METHOD_NAME_DOC_REF_DELETE = "DocumentReference.Delete";
  String METHOD_NAME_DOC_REF_GET = "DocumentReference.Get";
  String METHOD_NAME_DOC_REF_LIST_COLLECTIONS = "DocumentReference.ListCollections";
  String METHOD_NAME_COL_REF_ADD = "CollectionReference.Add";
  String METHOD_NAME_COL_REF_LIST_DOCUMENTS = "CollectionReference.ListDocuments";
  String METHOD_NAME_QUERY_GET = "Query.Get";
  String METHOD_NAME_AGGREGATION_QUERY_GET = "AggregationQuery.Get";
  String METHOD_NAME_RUN_QUERY = "RunQuery";
  String METHOD_NAME_RUN_QUERY_TRANSACTION = "RunQuery.Transaction";
  String METHOD_NAME_RUN_QUERY_EXPLAIN_QUERY = "RunQuery.ExplainQuery";
  String METHOD_NAME_RUN_QUERY_EXPLAIN_AGGREGATION_QUERY = "RunQuery.ExplainAggregationQuery";
  String METHOD_NAME_RUN_QUERY_QUERY = "RunQuery.Query";
  String METHOD_NAME_RUN_AGGREGATION_QUERY = "RunAggregationQuery";
  String METHOD_NAME_BATCH_GET_DOCUMENTS = "BatchGetDocuments";
  String METHOD_NAME_TRANSACTION_RUN = "Transaction.Run";
  String METHOD_NAME_TRANSACTION_BEGIN = "Transaction.Begin";
  String METHOD_NAME_TRANSACTION_GET_QUERY = "Transaction.Get.Query";
  String METHOD_NAME_TRANSACTION_GET_AGGREGATION_QUERY = "Transaction.Get.AggregationQuery";
  String METHOD_NAME_TRANSACTION_GET_DOCUMENT = "Transaction.Get.Document";
  String METHOD_NAME_TRANSACTION_GET_DOCUMENTS = "Transaction.Get.Documents";
  String METHOD_NAME_TRANSACTION_ROLLBACK = "Transaction.Rollback";
  String METHOD_NAME_BATCH_COMMIT = "Batch.Commit";
  String METHOD_NAME_TRANSACTION_COMMIT = "Transaction.Commit";
  String METHOD_NAME_PARTITION_QUERY = "PartitionQuery";
  String METHOD_NAME_BULK_WRITER_COMMIT = "BulkWriter.Commit";
  String METHOD_NAME_RUN_TRANSACTION = "RunTransaction";
}
