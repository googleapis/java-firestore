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
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.cloud.firestore.telemetry.DisabledMetricsUtil.MetricsContext;
import com.google.cloud.firestore.telemetry.TelemetryConstants.MetricType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class DisabledMetricsUtilTest {
  private DisabledMetricsUtil disabledMetricsUtil;

  @Before
  public void setUp() {
    disabledMetricsUtil = new DisabledMetricsUtil();
  }

  @Test
  public void createMetricsContextShouldReturnNonNullContext() {
    assertThat(disabledMetricsUtil.createMetricsContext("testMethod")).isNotNull();
    assertThat(
            disabledMetricsUtil.createMetricsContext("testMethod")
                instanceof DisabledMetricsUtil.MetricsContext)
        .isTrue();
  }

  @Test
  public void shouldNotAddMetricsTracerFactory() {
    List<ApiTracerFactory> factories = new ArrayList<>();
    disabledMetricsUtil.addMetricsTracerFactory(factories);
    assertThat(factories).isEmpty();
  }

  @Test
  public void metricsContextShouldNotThrow() {
    MetricsContext context = disabledMetricsUtil.createMetricsContext("testMethod");

    // Ensure no exceptions are thrown by no-op methods
    try {
      context.recordLatency(MetricType.END_TO_END_LATENCY);
      context.recordLatency(MetricType.END_TO_END_LATENCY, new Exception("test"));
      context.recordLatency(MetricType.FIRST_RESPONSE_LATENCY);
      context.incrementCounter();

      ApiFuture<String> future = ApiFutures.immediateFuture("test");
      context.recordLatencyAtFuture(MetricType.END_TO_END_LATENCY, future);
    } catch (Exception e) {
      assertThat(e).isNull(); // Fail the test if any exception is thrown
    }
  }
}
