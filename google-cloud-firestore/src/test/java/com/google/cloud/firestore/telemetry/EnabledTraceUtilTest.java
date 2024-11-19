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

import com.google.cloud.firestore.FirestoreOpenTelemetryOptions;
import com.google.cloud.firestore.FirestoreOptions;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import java.time.Duration;
import org.junit.Before;
import org.junit.Test;

public class EnabledTraceUtilTest {
  @Before
  public void setUp() {
    GlobalOpenTelemetry.resetForTest();
  }

  EnabledTraceUtil defaultEnabledTraceUtil() {
    return new EnabledTraceUtil(FirestoreOptions.getDefaultInstance());
  }

  FirestoreOptions.Builder getBaseOptions() {
    return FirestoreOptions.newBuilder().setProjectId("test-project").setDatabaseId("(default)");
  }

  @Test
  public void usesOpenTelemetryFromOptions() {
    OpenTelemetrySdk myOpenTelemetrySdk = OpenTelemetrySdk.builder().build();
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setOpenTelemetry(myOpenTelemetrySdk)
                    .build())
            .build();
    EnabledTraceUtil traceUtil = new EnabledTraceUtil(firestoreOptions);
    assertThat(traceUtil.getOpenTelemetry()).isSameInstanceAs(myOpenTelemetrySdk);
  }

  @Test
  public void usesGlobalOpenTelemetryIfOpenTelemetryInstanceNotProvided() {
    OpenTelemetrySdk.builder().buildAndRegisterGlobal();
    EnabledTraceUtil traceUtil = defaultEnabledTraceUtil();
    assertThat(traceUtil.getOpenTelemetry()).isSameInstanceAs(GlobalOpenTelemetry.get());
  }

  @Test
  public void usesOpenTelemetryFromOptionsEvenIfGlobalOpenTelemetryExists() {
    // Register a GlobalOpenTelemetry.
    OpenTelemetrySdk.builder().buildAndRegisterGlobal();

    // Pass in a *different* OpenTelemetry instance to Firestore to use.
    OpenTelemetrySdk myOpenTelemetrySdk = OpenTelemetrySdk.builder().build();
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setOpenTelemetry(myOpenTelemetrySdk)
                    .build())
            .build();
    EnabledTraceUtil traceUtil = new EnabledTraceUtil(firestoreOptions);

    // Assert Firestore uses the custom one, not the global one.
    assertThat(traceUtil.getOpenTelemetry()).isSameInstanceAs(myOpenTelemetrySdk);
    assertThat(traceUtil.getOpenTelemetry()).isNotSameInstanceAs(GlobalOpenTelemetry.get());
  }

  @Test
  public void defaultOptionsDoesNotRegisterGrpcChannelConfigurator() {
    EnabledTraceUtil traceUtil = defaultEnabledTraceUtil();
    assertThat(traceUtil.getOpenTelemetry().getTracerProvider())
        .isSameInstanceAs(TracerProvider.noop());
    assertThat(traceUtil.getChannelConfigurator()).isNull();
  }

  @Test
  public void globalOpenTelemetryRegistersGrpcChannelConfigurator() {
    OpenTelemetrySdk.builder().buildAndRegisterGlobal();
    EnabledTraceUtil traceUtil = defaultEnabledTraceUtil();
    assertThat(traceUtil.getChannelConfigurator()).isNotNull();
  }

  @Test
  public void openTelemetryInstanceRegistersGrpcChannelConfigurator() {
    OpenTelemetrySdk myOpenTelemetrySdk = OpenTelemetrySdk.builder().build();
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setOpenTelemetry(myOpenTelemetrySdk)
                    .build())
            .build();
    EnabledTraceUtil traceUtil = new EnabledTraceUtil(firestoreOptions);
    assertThat(traceUtil.getChannelConfigurator()).isNotNull();
  }

  @Test
  public void usesEnabledContext() {
    assertThat(defaultEnabledTraceUtil().currentContext() instanceof EnabledTraceUtil.Context)
        .isTrue();
  }

  @Test
  public void usesEnabledSpan() {
    EnabledTraceUtil traceUtil = defaultEnabledTraceUtil();
    assertThat(traceUtil.currentSpan() instanceof EnabledTraceUtil.Span).isTrue();
    assertThat(traceUtil.startSpan("foo") instanceof EnabledTraceUtil.Span).isTrue();
    assertThat(
            traceUtil.startSpan("foo", traceUtil.currentContext()) instanceof EnabledTraceUtil.Span)
        .isTrue();
  }

  @Test
  public void usesEnabledScope() {
    EnabledTraceUtil traceUtil = defaultEnabledTraceUtil();
    assertThat(traceUtil.currentContext().makeCurrent() instanceof EnabledTraceUtil.Scope).isTrue();
    assertThat(traceUtil.currentSpan().makeCurrent() instanceof EnabledTraceUtil.Scope).isTrue();
  }

  @Test
  public void durationString() {
    EnabledTraceUtil traceUtil = defaultEnabledTraceUtil();
    Duration duration = Duration.ofSeconds(2, 9);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("2.000000009s");

    duration = Duration.ofSeconds(3, 98);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("3.000000098s");

    duration = Duration.ofSeconds(4, 987);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("4.000000987s");

    duration = Duration.ofSeconds(5, 9876);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("5.000009876s");

    duration = Duration.ofSeconds(6, 98765);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("6.000098765s");

    duration = Duration.ofSeconds(7, 987654);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("7.000987654s");

    duration = Duration.ofSeconds(8, 9876543);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("8.009876543s");

    duration = Duration.ofSeconds(9, 98765432);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("9.098765432s");

    duration = Duration.ofSeconds(10, 987654321);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("10.987654321s");

    duration = Duration.ofSeconds(1, 0);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("1.0s");

    duration = Duration.ofSeconds(1, 1);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("1.000000001s");

    duration = Duration.ofSeconds(1, 10);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("1.00000001s");

    duration = Duration.ofSeconds(1, 100);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("1.0000001s");

    duration = Duration.ofSeconds(1, 1_000);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("1.000001s");

    duration = Duration.ofSeconds(1, 10_000);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("1.00001s");

    duration = Duration.ofSeconds(1, 100_000);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("1.0001s");

    duration = Duration.ofSeconds(1, 1_000_000);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("1.001s");

    duration = Duration.ofSeconds(1, 10_000_000);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("1.01s");

    duration = Duration.ofSeconds(1, 100_000_000);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("1.1s");

    duration = Duration.ofSeconds(1, 100_000_001);
    assertThat(traceUtil.durationStringDuration(duration)).isEqualTo("1.100000001s");
  }
}
