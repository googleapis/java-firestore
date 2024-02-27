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

package com.google.cloud.firestore.spi.v1;

import static com.google.common.truth.Truth.assertThat;

import com.google.api.gax.retrying.ExponentialRetryAlgorithm;
import com.google.api.gax.retrying.RetryAlgorithm;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.retrying.ScheduledRetryingExecutor;
import com.google.api.gax.rpc.StatusCode.Code;
import com.google.api.gax.tracing.TracedServerStreamingCallable;
import com.google.api.gax.tracing.TracedUnaryCallable;
import com.google.cloud.firestore.FirestoreOptions;
import java.lang.reflect.Field;
import java.util.Set;
import org.junit.Test;
import org.threeten.bp.Duration;

public class GrpcFirestoreRpcTest {

  // From firestore_grpc_service_config.json
  // This should be considered the source of truth.
  // BIDI streams do not have retry settings applied, even though they exist in service config.

  // Applies to CreateDocument, UpdateDocument and Commit
  private final RetrySettings expectedRetrySettings1 =
      retrySettingsWithTotalTimeout(Duration.ofSeconds(60))
          .setMaxAttempts(5)
          .setInitialRetryDelay(Duration.ofMillis(100))
          .setMaxRetryDelay(Duration.ofSeconds(60))
          .setRetryDelayMultiplier(1.3)
          .build();
  private final Code[] expectedRetryableCodes1 = {Code.RESOURCE_EXHAUSTED, Code.UNAVAILABLE};

  // Applies to BatchGetDocuments, RunQuery, PartitionQuery and RunAggregationQuery
  private final RetrySettings expectedRetrySettings2 =
      retrySettingsWithTotalTimeout(Duration.ofSeconds(300))
          .setMaxAttempts(5)
          .setInitialRetryDelay(Duration.ofMillis(100))
          .setMaxRetryDelay(Duration.ofSeconds(60))
          .setRetryDelayMultiplier(1.3)
          .build();
  private final Code[] expectedRetryableCodes2 = {
    Code.RESOURCE_EXHAUSTED, Code.UNAVAILABLE, Code.INTERNAL, Code.DEADLINE_EXCEEDED
  };

  // Applies to GetDocument, ListDocuments, DeleteDocument, BeginTransaction, Rollback and
  // ListCollectionIds
  private final RetrySettings expectedRetrySettings4 =
      retrySettingsWithTotalTimeout(Duration.ofSeconds(60))
          .setMaxAttempts(5)
          .setInitialRetryDelay(Duration.ofMillis(100))
          .setMaxRetryDelay(Duration.ofSeconds(60))
          .setRetryDelayMultiplier(1.3)
          .build();
  private final Code[] expectedRetryableCodes4 = {
    Code.RESOURCE_EXHAUSTED, Code.UNAVAILABLE, Code.INTERNAL, Code.DEADLINE_EXCEEDED
  };

  // Applies to BatchWrite
  private final RetrySettings expectedRetrySettings5 =
      retrySettingsWithTotalTimeout(Duration.ofSeconds(60))
          .setMaxAttempts(5)
          .setInitialRetryDelay(Duration.ofMillis(100))
          .setMaxRetryDelay(Duration.ofSeconds(60))
          .setRetryDelayMultiplier(1.3)
          .build();
  private final Code[] expectedRetryableCodes5 = {
    Code.RESOURCE_EXHAUSTED, Code.UNAVAILABLE, Code.ABORTED
  };

  @Test
  public void retrySettingsOverride() throws Exception {
    RetrySettings retrySettings = RetrySettings.newBuilder().setMaxAttempts(2).build();
    GrpcFirestoreRpc grpcFirestoreRpc =
        new GrpcFirestoreRpc(FirestoreOptions.newBuilder().setRetrySettings(retrySettings).build());

    CallableRetryData commit = getRetryData(grpcFirestoreRpc.commitCallable());
    assertThat(commit.retrySettings).isEqualTo(retrySettings);
    assertThat(commit.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes1);

    CallableRetryData batchWrite = getRetryData(grpcFirestoreRpc.batchWriteCallable());
    assertThat(batchWrite.retrySettings).isEqualTo(retrySettings);
    assertThat(batchWrite.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes5);

    CallableRetryData batchGetDocuments =
        getRetryData(grpcFirestoreRpc.batchGetDocumentsCallable());
    assertThat(batchGetDocuments.retrySettings).isEqualTo(retrySettings);
    assertThat(batchGetDocuments.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes2);

    CallableRetryData runQuery = getRetryData(grpcFirestoreRpc.runQueryCallable());
    assertThat(runQuery.retrySettings).isEqualTo(retrySettings);
    assertThat(runQuery.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes2);

    CallableRetryData runAggregationQuery =
        getRetryData(grpcFirestoreRpc.runAggregationQueryCallable());
    assertThat(runAggregationQuery.retrySettings).isEqualTo(retrySettings);
    assertThat(runAggregationQuery.retryableCodes)
        .containsExactlyElementsIn(expectedRetryableCodes2);

    CallableRetryData beginTransaction = getRetryData(grpcFirestoreRpc.beginTransactionCallable());
    assertThat(beginTransaction.retrySettings).isEqualTo(retrySettings);
    assertThat(beginTransaction.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes4);

    CallableRetryData rollback = getRetryData(grpcFirestoreRpc.rollbackCallable());
    assertThat(rollback.retrySettings).isEqualTo(retrySettings);
    assertThat(rollback.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes4);

    CallableRetryData listCollectionIdsPaged =
        getRetryData(grpcFirestoreRpc.listCollectionIdsPagedCallable());
    assertThat(listCollectionIdsPaged.retrySettings).isEqualTo(retrySettings);
    assertThat(listCollectionIdsPaged.retryableCodes)
        .containsExactlyElementsIn(expectedRetryableCodes4);

    CallableRetryData partitionQueryPaged =
        getRetryData(grpcFirestoreRpc.partitionQueryPagedCallable());
    assertThat(partitionQueryPaged.retrySettings).isEqualTo(retrySettings);
    assertThat(partitionQueryPaged.retryableCodes)
        .containsExactlyElementsIn(expectedRetryableCodes2);

    CallableRetryData listDocumentsPaged =
        getRetryData(grpcFirestoreRpc.listDocumentsPagedCallable());
    assertThat(listDocumentsPaged.retrySettings).isEqualTo(retrySettings);
    assertThat(listDocumentsPaged.retryableCodes)
        .containsExactlyElementsIn(expectedRetryableCodes4);
  }

  @Test
  public void commitCallableFollowsServiceConfig() throws Exception {
    GrpcFirestoreRpc grpcFirestoreRpc = new GrpcFirestoreRpc(FirestoreOptions.getDefaultInstance());
    CallableRetryData actual = getRetryData(grpcFirestoreRpc.commitCallable());
    assertThat(actual.retrySettings).isEqualTo(expectedRetrySettings1);
    assertThat(actual.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes1);
  }

  @Test
  public void batchWriteCallableFollowsServiceConfigFollowsServiceConfig() throws Exception {
    GrpcFirestoreRpc grpcFirestoreRpc = new GrpcFirestoreRpc(FirestoreOptions.getDefaultInstance());
    CallableRetryData actual = getRetryData(grpcFirestoreRpc.batchWriteCallable());
    assertThat(actual.retrySettings).isEqualTo(expectedRetrySettings5);
    assertThat(actual.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes5);
  }

  @Test
  public void batchGetDocumentsCallableFollowsServiceConfig() throws Exception {
    GrpcFirestoreRpc grpcFirestoreRpc = new GrpcFirestoreRpc(FirestoreOptions.getDefaultInstance());
    CallableRetryData actual = getRetryData(grpcFirestoreRpc.batchGetDocumentsCallable());
    assertThat(actual.retrySettings).isEqualTo(expectedRetrySettings2);
    assertThat(actual.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes2);
  }

  @Test
  public void runQueryCallableFollowsServiceConfig() throws Exception {
    GrpcFirestoreRpc grpcFirestoreRpc = new GrpcFirestoreRpc(FirestoreOptions.getDefaultInstance());
    CallableRetryData actual = getRetryData(grpcFirestoreRpc.runQueryCallable());
    assertThat(actual.retrySettings).isEqualTo(expectedRetrySettings2);
    assertThat(actual.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes2);
  }

  @Test
  public void runAggregationQueryCallableFollowsServiceConfig() throws Exception {
    GrpcFirestoreRpc grpcFirestoreRpc = new GrpcFirestoreRpc(FirestoreOptions.getDefaultInstance());
    CallableRetryData actual = getRetryData(grpcFirestoreRpc.runAggregationQueryCallable());
    assertThat(actual.retrySettings).isEqualTo(expectedRetrySettings2);
    assertThat(actual.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes2);
  }

  @Test
  public void beginTransactionCallableFollowsServiceConfig() throws Exception {
    GrpcFirestoreRpc grpcFirestoreRpc = new GrpcFirestoreRpc(FirestoreOptions.getDefaultInstance());
    CallableRetryData actual = getRetryData(grpcFirestoreRpc.beginTransactionCallable());
    assertThat(actual.retrySettings).isEqualTo(expectedRetrySettings4);
    assertThat(actual.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes4);
  }

  @Test
  public void rollbackCallableFollowsServiceConfig() throws Exception {
    GrpcFirestoreRpc grpcFirestoreRpc = new GrpcFirestoreRpc(FirestoreOptions.getDefaultInstance());
    CallableRetryData actual = getRetryData(grpcFirestoreRpc.rollbackCallable());
    assertThat(actual.retrySettings).isEqualTo(expectedRetrySettings4);
    assertThat(actual.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes4);
  }

  @Test
  public void listCollectionIdsPagedCallableFollowsServiceConfig() throws Exception {
    GrpcFirestoreRpc grpcFirestoreRpc = new GrpcFirestoreRpc(FirestoreOptions.getDefaultInstance());
    CallableRetryData actual = getRetryData(grpcFirestoreRpc.listCollectionIdsPagedCallable());
    assertThat(actual.retrySettings).isEqualTo(expectedRetrySettings4);
    assertThat(actual.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes4);
  }

  @Test
  public void partitionQueryPagedCallableFollowsServiceConfig() throws Exception {
    GrpcFirestoreRpc grpcFirestoreRpc = new GrpcFirestoreRpc(FirestoreOptions.getDefaultInstance());
    CallableRetryData actual = getRetryData(grpcFirestoreRpc.partitionQueryPagedCallable());
    assertThat(actual.retrySettings).isEqualTo(expectedRetrySettings2);
    assertThat(actual.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes2);
  }

  @Test
  public void listDocumentsPagedCallableFollowsServiceConfig() throws Exception {
    GrpcFirestoreRpc grpcFirestoreRpc = new GrpcFirestoreRpc(FirestoreOptions.getDefaultInstance());
    CallableRetryData actual = getRetryData(grpcFirestoreRpc.listDocumentsPagedCallable());
    assertThat(actual.retrySettings).isEqualTo(expectedRetrySettings4);
    assertThat(actual.retryableCodes).containsExactlyElementsIn(expectedRetryableCodes4);
  }

  private static RetrySettings.Builder retrySettingsWithTotalTimeout(Duration totalTimeout) {
    return RetrySettings.newBuilder()
        .setTotalTimeout(totalTimeout)
        .setInitialRpcTimeout(totalTimeout)
        .setMaxRpcTimeout(totalTimeout);
  }

  private static CallableRetryData getRetryData(Object callable) throws Exception {

    if (callable instanceof TracedServerStreamingCallable) {
      Field innerCallable = TracedServerStreamingCallable.class.getDeclaredField("innerCallable");
      innerCallable.setAccessible(true);
      return getRetryData(innerCallable.get(callable));
    }

    if (callable instanceof TracedUnaryCallable) {
      Field innerCallable = TracedUnaryCallable.class.getDeclaredField("innerCallable");
      innerCallable.setAccessible(true);
      return getRetryData(innerCallable.get(callable));
    }

    Class<?> aClass = callable.getClass();
    String aClassName = aClass.getName();
    switch (aClassName) {
      case "com.google.api.gax.rpc.UnaryCallable$1":
      case "com.google.api.gax.rpc.ServerStreamingCallable$1":
        {
          Field innerCallable = aClass.getDeclaredField("this$0");
          innerCallable.setAccessible(true);
          return getRetryData(innerCallable.get(callable));
        }
      case "com.google.api.gax.rpc.RetryingCallable":
        {
          Field executor = aClass.getDeclaredField("executor");
          executor.setAccessible(true);
          Field innerCallable = aClass.getDeclaredField("callable");
          innerCallable.setAccessible(true);

          return new CallableRetryData(
              getRetrySettings(executor.get(callable)),
              getRetryableCodes(innerCallable.get(callable)));
        }
      case "com.google.api.gax.rpc.RetryingServerStreamingCallable":
        {
          Field executor = aClass.getDeclaredField("executor");
          executor.setAccessible(true);
          Field innerCallable = aClass.getDeclaredField("innerCallable");
          innerCallable.setAccessible(true);

          return new CallableRetryData(
              getRetrySettings(executor.get(callable)),
              getRetryableCodes(innerCallable.get(callable)));
        }
      case "com.google.api.gax.rpc.PagedCallable":
        {
          Field innerCallable = aClass.getDeclaredField("callable");
          innerCallable.setAccessible(true);
          return getRetryData(innerCallable.get(callable));
        }
      default:
        throw new Exception("Unexpected class " + aClassName);
    }
  }

  private static Set<Code> getRetryableCodes(Object o)
      throws NoSuchFieldException, IllegalAccessException {
    Class<?> aClass = o.getClass();
    String aClassName = aClass.getName();
    switch (aClassName) {
      case "com.google.api.gax.rpc.ServerStreamingCallable$1":
        {
          Field innerCallable = aClass.getDeclaredField("this$0");
          innerCallable.setAccessible(true);
          return getRetryableCodes(innerCallable.get(o));
        }
      case "com.google.api.gax.rpc.WatchdogServerStreamingCallable":
        {
          Field innerCallable = aClass.getDeclaredField("inner");
          innerCallable.setAccessible(true);
          return getRetryableCodes(innerCallable.get(o));
        }
    }
    Field exceptionFactory = aClass.getDeclaredField("exceptionFactory");
    exceptionFactory.setAccessible(true);
    Object exceptionFactoryObject = exceptionFactory.get(o);
    Field retryableCodes = exceptionFactoryObject.getClass().getDeclaredField("retryableCodes");
    retryableCodes.setAccessible(true);
    return (Set<Code>) retryableCodes.get(exceptionFactoryObject);
  }

  private static RetrySettings getRetrySettings(Object o) throws Exception {
    if (o instanceof ScheduledRetryingExecutor) {
      Field retryAlgorithm = ScheduledRetryingExecutor.class.getDeclaredField("retryAlgorithm");
      retryAlgorithm.setAccessible(true);
      Object retryAlgorithmObject = retryAlgorithm.get(o);

      Field timedAlgorithmWithContext =
          RetryAlgorithm.class.getDeclaredField("timedAlgorithmWithContext");
      timedAlgorithmWithContext.setAccessible(true);
      Object exponentialRetryAlgorithm = timedAlgorithmWithContext.get(retryAlgorithmObject);

      Field globalSettings = ExponentialRetryAlgorithm.class.getDeclaredField("globalSettings");
      globalSettings.setAccessible(true);
      return (RetrySettings) globalSettings.get(exponentialRetryAlgorithm);
    }

    Class<?> aClass = o.getClass();
    String aClassName = aClass.getName();

    switch (aClassName) {
      default:
        throw new Exception("Unexpected class " + aClassName);
    }
  }

  static class CallableRetryData {
    final RetrySettings retrySettings;
    final Set<Code> retryableCodes;

    CallableRetryData(RetrySettings retrySettings, Set<Code> retryableCodes) {
      this.retrySettings = retrySettings;
      this.retryableCodes = retryableCodes;
    }
  }
}
