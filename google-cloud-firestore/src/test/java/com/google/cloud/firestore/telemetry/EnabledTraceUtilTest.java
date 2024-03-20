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
import io.opentelemetry.sdk.OpenTelemetrySdk;
import org.junit.Before;
import org.junit.Test;

public class EnabledTraceUtilTest {
  @Before
  public void setUp() {
    GlobalOpenTelemetry.resetForTest();
  }

  FirestoreOptions.Builder getBaseOptions() {
    return FirestoreOptions.newBuilder().setProjectId("test-project").setDatabaseId("(default)");
  }

  FirestoreOptions getTracingEnabledOptions() {
    return getBaseOptions()
        .setOpenTelemetryOptions(
            FirestoreOpenTelemetryOptions.newBuilder().setTracingEnabled(true).build())
        .build();
  }

  EnabledTraceUtil newEnabledTraceUtil() {
    return new EnabledTraceUtil(getTracingEnabledOptions());
  }

  @Test
  public void usesOpenTelemetryFromOptions() {
    OpenTelemetrySdk myOpenTelemetrySdk = OpenTelemetrySdk.builder().build();
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setTracingEnabled(true)
                    .setOpenTelemetry(myOpenTelemetrySdk)
                    .build())
            .build();
    EnabledTraceUtil traceUtil = new EnabledTraceUtil(firestoreOptions);
    assertThat(traceUtil.getOpenTelemetry()).isEqualTo(myOpenTelemetrySdk);
  }

  @Test
  public void usesGlobalOpenTelemetryIfOpenTelemetryInstanceNotProvided() {
    OpenTelemetrySdk.builder().buildAndRegisterGlobal();
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder().setTracingEnabled(true).build())
            .build();
    EnabledTraceUtil traceUtil = new EnabledTraceUtil(firestoreOptions);
    assertThat(traceUtil.getOpenTelemetry()).isEqualTo(GlobalOpenTelemetry.get());
  }

  @Test
  public void enabledTraceUtilProvidesChannelConfigurator() {
    assertThat(newEnabledTraceUtil().getChannelConfigurator()).isNotNull();
  }

  @Test
  public void usesEnabledContext() {
    assertThat(newEnabledTraceUtil().currentContext() instanceof EnabledTraceUtil.Context).isTrue();
  }

  @Test
  public void usesEnabledSpan() {
    EnabledTraceUtil traceUtil = newEnabledTraceUtil();
    assertThat(traceUtil.currentSpan() instanceof EnabledTraceUtil.Span).isTrue();
    assertThat(traceUtil.startSpan("foo") instanceof EnabledTraceUtil.Span).isTrue();
    assertThat(
            traceUtil.startSpan("foo", traceUtil.currentContext()) instanceof EnabledTraceUtil.Span)
        .isTrue();
  }

  @Test
  public void usesEnabledScope() {
    EnabledTraceUtil traceUtil = newEnabledTraceUtil();
    assertThat(traceUtil.currentContext().makeCurrent() instanceof EnabledTraceUtil.Scope).isTrue();
    assertThat(traceUtil.currentSpan().makeCurrent() instanceof EnabledTraceUtil.Scope).isTrue();
  }
}
