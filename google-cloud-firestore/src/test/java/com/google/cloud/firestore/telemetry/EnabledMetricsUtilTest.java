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

import static com.google.common.truth.Truth.assertThat;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.grpc.GrpcStatusCode;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.FirestoreOpenTelemetryOptions;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.telemetry.EnabledMetricsUtil.MetricsContext;
import com.google.cloud.firestore.telemetry.TelemetryConstants.MetricType;
import io.grpc.Status;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EnabledMetricsUtilTest {
  @Before
  public void setUp() {
    GlobalOpenTelemetry.resetForTest();
  }

  FirestoreOptions.Builder getBaseOptions() {
    return FirestoreOptions.newBuilder().setProjectId("test-project").setDatabaseId("(default)");
  }

  EnabledMetricsUtil newEnabledMetricsUtil() {
    return new EnabledMetricsUtil(getBaseOptions().build());
  }

  @Test
  public void createsDefaultBuiltinMetricsProviderWithBuiltinOpenTelemetryInstance() {
    FirestoreOptions firestoreOptions = getBaseOptions().build();
    EnabledMetricsUtil metricsUtil = new EnabledMetricsUtil(firestoreOptions);
    BuiltinMetricsProvider metricsProvider = metricsUtil.getDefaultMetricsProvider();
    assertThat(metricsProvider).isNotNull();
    assertThat(metricsProvider.getOpenTelemetry()).isNotNull();
    // The default OpenTelemetry MeterProvider has registered GoogleCloudMonitoringExporter.
    assertThat(metricsProvider.getOpenTelemetry().getMeterProvider())
        .isNotEqualTo(MeterProvider.noop());
  }

  @Test
  public void canDisableBuiltinMetricsProviderWithFirestoreOpenTelemetryOptions() {
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .exportBuiltinMetricsToGoogleCloudMonitoring(false)
                    .build())
            .build();
    EnabledMetricsUtil metricsUtil = new EnabledMetricsUtil(firestoreOptions);
    BuiltinMetricsProvider metricsProvider = metricsUtil.getDefaultMetricsProvider();
    assertThat(metricsProvider).isNotNull();
    assertThat(metricsProvider.getOpenTelemetry()).isNotNull();

    // Metrics collection is "disabled" with OpenTelemetry No-op meter provider instance
    assertThat(metricsProvider.getOpenTelemetry().getMeterProvider())
        .isEqualTo(MeterProvider.noop());
  }

  @Test
  public void usesCustomOpenTelemetryFromOptions() {
    OpenTelemetry openTelemetry = OpenTelemetrySdk.builder().build();
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder().setOpenTelemetry(openTelemetry).build())
            .build();
    EnabledMetricsUtil metricsUtil = new EnabledMetricsUtil(firestoreOptions);

    BuiltinMetricsProvider customMetricsProvider = metricsUtil.getCustomMetricsProvider();
    assertThat(customMetricsProvider).isNotNull();
    assertThat(customMetricsProvider.getOpenTelemetry()).isNotNull();
    assertThat(customMetricsProvider.getOpenTelemetry()).isEqualTo(openTelemetry);
  }

  @Test
  public void usesGlobalOpenTelemetryIfCustomOpenTelemetryInstanceNotProvided() {
    OpenTelemetrySdk.builder().buildAndRegisterGlobal();
    FirestoreOptions firestoreOptions = getBaseOptions().build();
    EnabledMetricsUtil metricsUtil = new EnabledMetricsUtil(firestoreOptions);

    BuiltinMetricsProvider customMetricsProvider = metricsUtil.getCustomMetricsProvider();
    assertThat(customMetricsProvider).isNotNull();
    assertThat(customMetricsProvider.getOpenTelemetry()).isNotNull();
    assertThat(customMetricsProvider.getOpenTelemetry()).isEqualTo(GlobalOpenTelemetry.get());
  }

  @Test
  public void globalOpenTelemetryDefaultsToNoopIfNotRegistered() {
    FirestoreOptions firestoreOptions = getBaseOptions().build();
    EnabledMetricsUtil metricsUtil = new EnabledMetricsUtil(firestoreOptions);

    BuiltinMetricsProvider customMetricsProvider = metricsUtil.getCustomMetricsProvider();
    assertThat(customMetricsProvider).isNotNull();
    assertThat(customMetricsProvider.getOpenTelemetry()).isNotNull();
    assertThat(customMetricsProvider.getOpenTelemetry()).isEqualTo(GlobalOpenTelemetry.get());
    assertThat(customMetricsProvider.getOpenTelemetry().getMeterProvider())
        .isEqualTo(MeterProvider.noop());
  }

  @Test
  public void usesIndependentOpenTelemetryInstanceInDefaultAndCustomMetricsProvider() {
    OpenTelemetry openTelemetry = OpenTelemetrySdk.builder().build();
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder().setOpenTelemetry(openTelemetry).build())
            .build();
    EnabledMetricsUtil metricsUtil = new EnabledMetricsUtil(firestoreOptions);

    BuiltinMetricsProvider defaultMetricsProvider = metricsUtil.getDefaultMetricsProvider();
    BuiltinMetricsProvider customMetricsProvider = metricsUtil.getCustomMetricsProvider();
    assertThat(defaultMetricsProvider).isNotNull();
    assertThat(customMetricsProvider).isNotNull();
    assertThat(defaultMetricsProvider).isNotEqualTo(customMetricsProvider);
    assertThat(defaultMetricsProvider.getOpenTelemetry())
        .isNotEqualTo(customMetricsProvider.getOpenTelemetry());
  }

  @Test
  public void canCreateMetricsContext() {
    MetricsContext context = newEnabledMetricsUtil().createMetricsContext("testMethod");

    assertThat(context).isNotNull();
    assertThat(context instanceof EnabledMetricsUtil.MetricsContext).isTrue();
    assertThat(context.methodName).isEqualTo("testMethod");
  }

  @Test
  public void addsMetricsTracerFactoryForDefaultMetricsProvider() {
    List<ApiTracerFactory> factories = new ArrayList<>();
    EnabledMetricsUtil metricsUtil = newEnabledMetricsUtil();
    metricsUtil.addMetricsTracerFactory(factories);
    // Add tracer factory for default metrics provider only as the custom metrics provider is not
    // enabled.
    assertThat(factories.size()).isEqualTo(1);
  }

  @Test
  public void addsMetricsTracerFactoriesForBothMetricsProvider() {
    List<ApiTracerFactory> factories = new ArrayList<>();

    OpenTelemetry openTelemetry = OpenTelemetrySdk.builder().build();
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder().setOpenTelemetry(openTelemetry).build())
            .build();
    EnabledMetricsUtil metricsUtil = new EnabledMetricsUtil(firestoreOptions);

    metricsUtil.addMetricsTracerFactory(factories);
    // One from each provider
    assertThat(factories.size()).isEqualTo(2);
  }

  @Test
  public void addsMetricsTracerFactoriesIndependentlyForMetricsProviders() {
    List<ApiTracerFactory> factories = new ArrayList<>();

    OpenTelemetry openTelemetry = OpenTelemetrySdk.builder().build();
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setOpenTelemetry(openTelemetry)
                    .exportBuiltinMetricsToGoogleCloudMonitoring(false)
                    .build())
            .build();
    EnabledMetricsUtil metricsUtil = new EnabledMetricsUtil(firestoreOptions);

    metricsUtil.addMetricsTracerFactory(factories);
    // Add tracer factory for custom metrics provider only as the default metrics provider is not
    // enabled.
    assertThat(factories.size()).isEqualTo(1);
  }

  @Test
  public void extractErrorStatusShouldNotThrowOnNonFirestoreException() {
    EnabledMetricsUtil metricsUtil = newEnabledMetricsUtil();

    ApiException apiException =
        new ApiException(
            new IllegalStateException("Mock batchWrite failed in test"),
            GrpcStatusCode.of(Status.Code.INVALID_ARGUMENT),
            false);

    String errorStatus = metricsUtil.extractErrorStatus(apiException);
    assertThat(errorStatus).isEqualTo(StatusCode.Code.UNKNOWN.toString());
  }

  @Test
  public void testExtractErrorStatus_firestoreExceptionWithStatus() {
    EnabledMetricsUtil metricsUtil = newEnabledMetricsUtil();

    FirestoreException firestoreException =
        FirestoreException.forApiException(
            new ApiException(
                new IllegalStateException("Mock batchWrite failed in test"),
                GrpcStatusCode.of(Status.Code.INVALID_ARGUMENT),
                false));

    String errorStatus = metricsUtil.extractErrorStatus(firestoreException);
    // TODO(b/305998085):Change this to correct status code
    assertThat(errorStatus).isEqualTo(StatusCode.Code.UNKNOWN.toString());
  }

  @Test
  public void recordLatencyCalledWhenFutureIsCompletedWithSuccess() {
    EnabledMetricsUtil enabledMetricsUtil = newEnabledMetricsUtil();
    BuiltinMetricsProvider mockDefaultProvider = Mockito.mock(BuiltinMetricsProvider.class);
    BuiltinMetricsProvider mockCustomProvider = Mockito.mock(BuiltinMetricsProvider.class);
    enabledMetricsUtil.setDefaultMetricsProvider(mockDefaultProvider);
    enabledMetricsUtil.setCustomMetricsProvider(mockCustomProvider);

    MetricsContext context = enabledMetricsUtil.createMetricsContext("testMethod");
    ApiFuture<String> future = ApiFutures.immediateFuture("success");

    context.recordLatencyAtFuture(MetricType.END_TO_END_LATENCY, future);
    Mockito.verify(mockDefaultProvider)
        .latencyRecorder(
            Mockito.eq(MetricType.END_TO_END_LATENCY), Mockito.anyDouble(), Mockito.anyMap());
    Mockito.verify(mockCustomProvider)
        .latencyRecorder(
            Mockito.eq(MetricType.END_TO_END_LATENCY), Mockito.anyDouble(), Mockito.anyMap());
  }

  @Test
  public void recordLatencyCalledWhenFutureIsCompletedWithError() {
    EnabledMetricsUtil enabledMetricsUtil = newEnabledMetricsUtil();
    BuiltinMetricsProvider mockDefaultProvider = Mockito.mock(BuiltinMetricsProvider.class);
    BuiltinMetricsProvider mockCustomProvider = Mockito.mock(BuiltinMetricsProvider.class);
    enabledMetricsUtil.setDefaultMetricsProvider(mockDefaultProvider);
    enabledMetricsUtil.setCustomMetricsProvider(mockCustomProvider);

    MetricsContext context = enabledMetricsUtil.createMetricsContext("testMethod");
    FirestoreException firestoreException =
        FirestoreException.forApiException(
            new ApiException(
                new IllegalStateException("Mock batchWrite failed in test"),
                GrpcStatusCode.of(Status.Code.INVALID_ARGUMENT),
                false));
    ApiFuture<String> future = ApiFutures.immediateFailedFuture(firestoreException);
    context.recordLatencyAtFuture(MetricType.END_TO_END_LATENCY, future);
    // TODO(b/305998085):Change this to correct status code
    Mockito.verify(mockDefaultProvider)
        .latencyRecorder(
            Mockito.eq(MetricType.END_TO_END_LATENCY),
            Mockito.anyDouble(),
            Mockito.argThat(
                attributes ->
                    attributes
                        .get(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS.getKey())
                        .equals(Status.Code.UNKNOWN.toString())));
    Mockito.verify(mockCustomProvider)
        .latencyRecorder(
            Mockito.eq(MetricType.END_TO_END_LATENCY),
            Mockito.anyDouble(),
            Mockito.argThat(
                attributes ->
                    attributes
                        .get(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS.getKey())
                        .equals(Status.Code.UNKNOWN.toString())));
  }

  @Test
  public void recordCounterCalledWhenFutureIsCompletedWithSuccess() {
    EnabledMetricsUtil enabledMetricsUtil = newEnabledMetricsUtil();
    BuiltinMetricsProvider mockDefaultProvider = Mockito.mock(BuiltinMetricsProvider.class);
    BuiltinMetricsProvider mockCustomProvider = Mockito.mock(BuiltinMetricsProvider.class);
    enabledMetricsUtil.setDefaultMetricsProvider(mockDefaultProvider);
    enabledMetricsUtil.setCustomMetricsProvider(mockCustomProvider);

    MetricsContext context = enabledMetricsUtil.createMetricsContext("testMethod");
    ApiFuture<String> future = ApiFutures.immediateFuture("success");

    context.recordCounterAtFuture(MetricType.TRANSACTION_ATTEMPT, future);

    Mockito.verify(mockDefaultProvider)
        .counterRecorder(
            Mockito.eq(MetricType.TRANSACTION_ATTEMPT), Mockito.anyLong(), Mockito.anyMap());
    Mockito.verify(mockCustomProvider)
        .counterRecorder(
            Mockito.eq(MetricType.TRANSACTION_ATTEMPT), Mockito.anyLong(), Mockito.anyMap());
  }

  @Test
  public void recordCounterCalledWhenFutureIsCompletedWithError() {
    EnabledMetricsUtil enabledMetricsUtil = newEnabledMetricsUtil();
    BuiltinMetricsProvider mockDefaultProvider = Mockito.mock(BuiltinMetricsProvider.class);
    BuiltinMetricsProvider mockCustomProvider = Mockito.mock(BuiltinMetricsProvider.class);
    enabledMetricsUtil.setDefaultMetricsProvider(mockDefaultProvider);
    enabledMetricsUtil.setCustomMetricsProvider(mockCustomProvider);

    MetricsContext context = enabledMetricsUtil.createMetricsContext("testMethod");
    FirestoreException firestoreException =
        FirestoreException.forApiException(
            new ApiException(
                new IllegalStateException("Mock batchWrite failed in test"),
                GrpcStatusCode.of(Status.Code.INVALID_ARGUMENT),
                false));
    ApiFuture<String> future = ApiFutures.immediateFailedFuture(firestoreException);
    context.recordCounterAtFuture(MetricType.TRANSACTION_ATTEMPT, future);

    // TODO(b/305998085):Change this to correct status code
    Mockito.verify(mockDefaultProvider)
        .counterRecorder(
            Mockito.eq(MetricType.TRANSACTION_ATTEMPT),
            Mockito.anyLong(),
            Mockito.argThat(
                attributes ->
                    attributes
                        .get(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS.getKey())
                        .equals(Status.Code.UNKNOWN.toString())));
    Mockito.verify(mockCustomProvider)
        .counterRecorder(
            Mockito.eq(MetricType.TRANSACTION_ATTEMPT),
            Mockito.anyLong(),
            Mockito.argThat(
                attributes ->
                    attributes
                        .get(TelemetryConstants.METRIC_ATTRIBUTE_KEY_STATUS.getKey())
                        .equals(Status.Code.UNKNOWN.toString())));
  }
}
