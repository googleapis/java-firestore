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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.cloud.firestore.telemetry.TelemetryConstants.MetricType;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class BuiltinMetricsProviderTest {
  private OpenTelemetry mockOpenTelemetry;
  private Meter mockMeter;
  private DoubleHistogram mockEndToEndLatency;
  private DoubleHistogram mockFirstResponseLatency;
  private DoubleHistogram mockTransactionLatency;
  private LongCounter mockTransactionAttemptCount;
  private BuiltinMetricsProvider metricsProvider;

  @Before
  public void setUp() {
    // Mock OpenTelemetry components
    mockOpenTelemetry = mock(OpenTelemetry.class);
    MeterProvider mockMeterProvider = mock(MeterProvider.class);
    when(mockOpenTelemetry.getMeterProvider()).thenReturn(mockMeterProvider);
    mockMeter = mock(Meter.class);
    when(mockOpenTelemetry.getMeter(anyString())).thenReturn(mockMeter);
    MeterBuilder mockMeterBuilder = mock(MeterBuilder.class);
    when(mockMeterBuilder.setInstrumentationVersion(anyString())).thenReturn(mockMeterBuilder);
    when(mockMeterBuilder.build()).thenReturn(mockMeter);
    when(mockOpenTelemetry.meterBuilder(anyString())).thenReturn(mockMeterBuilder);

    // Mock Histogram and Counter builders
    DoubleHistogramBuilder mockHistogramBuilder = mock(DoubleHistogramBuilder.class);
    when(mockMeter.histogramBuilder(anyString())).thenReturn(mockHistogramBuilder);
    when(mockHistogramBuilder.setDescription(anyString())).thenReturn(mockHistogramBuilder);
    when(mockHistogramBuilder.setUnit(anyString())).thenReturn(mockHistogramBuilder);
    LongCounterBuilder mockCounterBuilder = mock(LongCounterBuilder.class);
    when(mockMeter.counterBuilder(anyString())).thenReturn(mockCounterBuilder);
    when(mockCounterBuilder.setDescription(anyString())).thenReturn(mockCounterBuilder);
    when(mockCounterBuilder.setUnit(anyString())).thenReturn(mockCounterBuilder);

    mockEndToEndLatency = mock(DoubleHistogram.class);
    mockFirstResponseLatency = mock(DoubleHistogram.class);
    mockTransactionLatency = mock(DoubleHistogram.class);
    mockTransactionAttemptCount = mock(LongCounter.class);

    // Configure mockHistogramBuilder to return the first 3 histogram mocks created in sequence.
    // The SDK configures 4 SDK layer metrics first, and then the RPC metrics gets created in GAX
    // layer
    when(mockHistogramBuilder.build())
        .thenReturn(mockEndToEndLatency)
        .thenReturn(mockFirstResponseLatency)
        .thenReturn(mockTransactionLatency);

    // Mock the counter builder to return the transaction attempt counter
    when(mockCounterBuilder.build()).thenReturn(mockTransactionAttemptCount);

    // Set up BuiltinMetricsProvider instance with mocked OpenTelemetry
    metricsProvider = new BuiltinMetricsProvider(mockOpenTelemetry);
  }

  @Test
  public void SDKLayerMetricsConfiguredSuccessfully() {
    metricsProvider = new BuiltinMetricsProvider(mockOpenTelemetry);
    assertNotNull(metricsProvider.getHistogram(MetricType.END_TO_END_LATENCY));
    assertNotNull(metricsProvider.getHistogram(MetricType.FIRST_RESPONSE_LATENCY));
    assertNotNull(metricsProvider.getHistogram(MetricType.TRANSACTION_LATENCY));
    assertNotNull(metricsProvider.getCounter(MetricType.TRANSACTION_ATTEMPT_COUNT));
  }

  @Test
  public void getHistogramReturnsHistogramsInstrumentCorrectly() {
    DoubleHistogram mockHistogram = metricsProvider.getHistogram(MetricType.END_TO_END_LATENCY);
    assertEquals(mockEndToEndLatency, mockHistogram);

    mockHistogram = metricsProvider.getHistogram(MetricType.FIRST_RESPONSE_LATENCY);
    assertEquals(mockFirstResponseLatency, mockHistogram);

    mockHistogram = metricsProvider.getHistogram(MetricType.TRANSACTION_LATENCY);
    assertEquals(mockTransactionLatency, mockHistogram);
  }

  @Test
  public void getHistogramThrowsOnInvalidMetricType() {
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> metricsProvider.getHistogram(MetricType.TRANSACTION_ATTEMPT_COUNT));
    assertEquals("Unknown latency MetricType: TRANSACTION_ATTEMPT_COUNT", exception.getMessage());
  }

  @Test
  public void getCounterReturnsCounterInstrumentCorrectly() {
    LongCounter mockCounter = metricsProvider.getCounter(MetricType.TRANSACTION_ATTEMPT_COUNT);
    assertEquals(mockTransactionAttemptCount, mockCounter);
  }

  @Test
  public void getCounterThrowsOnInvalidMetricType() {
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> metricsProvider.getCounter(MetricType.END_TO_END_LATENCY));
    assertEquals("Unknown counter MetricType: END_TO_END_LATENCY", exception.getMessage());
  }

  @Test
  public void latencyRecorderTriggersCorrectInstrument() {
    Map<String, String> attributes = new HashMap<>();
    attributes.put("attribute", "value");

    metricsProvider.latencyRecorder(MetricType.END_TO_END_LATENCY, 100.0, attributes);
    verify(mockEndToEndLatency)
        .record(
            eq(100.0),
            argThat(
                arguments -> arguments.get(AttributeKey.stringKey("attribute")).equals("value")));

    metricsProvider.latencyRecorder(MetricType.FIRST_RESPONSE_LATENCY, 50.0, attributes);
    verify(mockFirstResponseLatency)
        .record(
            eq(50.0),
            argThat(
                arguments -> arguments.get(AttributeKey.stringKey("attribute")).equals("value")));

    metricsProvider.latencyRecorder(MetricType.TRANSACTION_LATENCY, 200.0, attributes);
    verify(mockTransactionLatency)
        .record(
            eq(200.0),
            argThat(
                arguments -> arguments.get(AttributeKey.stringKey("attribute")).equals("value")));
  }

  @Test
  public void counterRecorderTriggersCorrectInstrument() {
    Map<String, String> attributes = new HashMap<>();
    attributes.put("key", "value");

    metricsProvider.counterRecorder(MetricType.TRANSACTION_ATTEMPT_COUNT, 5, attributes);
    verify(mockTransactionAttemptCount).add(eq(5L), any(Attributes.class));
  }

  @Test
  public void handlesNoopMetricProviderGracefully() throws Exception {
    BuiltinMetricsProvider provider = new BuiltinMetricsProvider(OpenTelemetry.noop());
    Map<String, String> attributes = new HashMap<>();
    attributes.put("key", "disabledTest");

    try {
      provider.latencyRecorder(MetricType.END_TO_END_LATENCY, 50.0, attributes);
      provider.latencyRecorder(MetricType.FIRST_RESPONSE_LATENCY, 100.0, attributes);
      provider.latencyRecorder(MetricType.TRANSACTION_LATENCY, 150.0, attributes);
      provider.counterRecorder(MetricType.TRANSACTION_ATTEMPT_COUNT, 1, attributes);
    } catch (Exception e) {
      assertThat(e).isNull(); // Fail the test if any exception is thrown
    }
  }
}
