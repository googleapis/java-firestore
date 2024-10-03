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

import com.google.api.gax.tracing.OpenTelemetryMetricsRecorder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.View;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class BuiltinMetricsConstants {

  public static final String FIRESTORE_RESOURCE_TYPE = "firestore_client_raw";

  // TODO: change to firestore.googleapis.com
  public static final String METER_NAME = "custom.googleapis.com/internal/client";
  public static final String FIRESTORE_METER_NAME = "java_firestore";
  public static final String GAX_METER_NAME = OpenTelemetryMetricsRecorder.GAX_METER_NAME;

  // Monitored resource labels
  static final String RESOURCE_KEY_RESOURCE_CONTAINER = "resource_container";
  static final String RESOURCE_KEY_LOCATION = "location";
  static final String RESOURCE_KEY_DATABASE_ID = "database_id";

  // Metric attribute keys for labels
  public static final AttributeKey<String> METRIC_KEY_METHOD = AttributeKey.stringKey("method");
  public static final AttributeKey<String> METRIC_KEY_STATUS = AttributeKey.stringKey("status");
  public static final AttributeKey<String> METRIC_KEY_LIBRARY_NAME =
      AttributeKey.stringKey("library_name");
  public static final AttributeKey<String> METRIC_KEY_LIBRARY_VERSION =
      AttributeKey.stringKey("library_version");
  public static final AttributeKey<String> METRIC_KEY_CLIENT_UID =
      AttributeKey.stringKey("client_uid");

  // Metric
  public static final String METRIC_NAME_OPERATION_LATENCY = "operation_latency";
  public static final String METRIC_NAME_OPERATION_COUNT = "operation_count";
  public static final String METRIC_NAME_ATTEMPT_LATENCY = "attempt_latency";
  public static final String METRIC_NAME_ATTEMPT_COUNT = "attempt_count";
  public static final String METRIC_NAME_FIRST_RESPONSE_LATENCY = "first_response_latency";
  public static final String METRIC_NAME_END_TO_END_LATENCY = "end_to_end_latency";
  public static final String METRIC_NAME_TRANSACTION_LATENCY = "transaction_latency";
  public static final String METRIC_NAME_TRANSACTION_ATTEMPT_COUNT = "transaction_attempt_count";

  // TODO(metrics): an app should have one client_uid, and it is contant. should this be hold here?
  private static String CLIENT_UID;

  public static final String MILLISECOND_UNIT = "ms";

  public static final String ENABLE_METRICS_ENV_VAR = "FIRESTORE_ENABLE_TRACING";

  static final Set<String> FIRESTORE_RESOURCE_LABELS =
      ImmutableSet.of(
          RESOURCE_KEY_RESOURCE_CONTAINER, RESOURCE_KEY_LOCATION, RESOURCE_KEY_DATABASE_ID);

  public static final Set<String> BUILTIN_METRICS =
      ImmutableSet.of(
              METRIC_NAME_OPERATION_LATENCY,
              METRIC_NAME_ATTEMPT_LATENCY,
              METRIC_NAME_OPERATION_COUNT,
              METRIC_NAME_ATTEMPT_COUNT,
              METRIC_NAME_FIRST_RESPONSE_LATENCY,
              METRIC_NAME_END_TO_END_LATENCY,
              METRIC_NAME_TRANSACTION_LATENCY,
              METRIC_NAME_TRANSACTION_ATTEMPT_COUNT)
          .stream()
          .map(m -> METER_NAME + '/' + m)
          .collect(Collectors.toSet());

  public static void defineView(
      ImmutableMap.Builder<InstrumentSelector, View> viewMap,
      String id,
      String meter,
      Set<AttributeKey> attributes) {
    InstrumentSelector selector =
        InstrumentSelector.builder().setMeterName(meter).setName(METER_NAME + "/" + id).build();
    Set<String> attributesFilter =
        ImmutableSet.<String>builder()
            .addAll(attributes.stream().map(AttributeKey::getKey).collect(Collectors.toSet()))
            .build();
    View view = View.builder().setAttributeFilter(attributesFilter).build();

    viewMap.put(selector, view);
  }

  public static final Set<AttributeKey> COMMON_ATTRIBUTES =
      ImmutableSet.of(
          METRIC_KEY_CLIENT_UID,
          METRIC_KEY_LIBRARY_NAME,
          METRIC_KEY_LIBRARY_VERSION,
          METRIC_KEY_STATUS);

  private static Set<AttributeKey> withAdditionalAttributes(Set<AttributeKey> attributes) {
    return ImmutableSet.<AttributeKey>builder()
        .addAll(COMMON_ATTRIBUTES)
        .addAll(attributes)
        .build();
  }

  public static Map<InstrumentSelector, View> getAllViews() {
    ImmutableMap.Builder<InstrumentSelector, View> views = ImmutableMap.builder();

    // Define views with COMMON_ATTRIBUTES and METHOD_KEY
    defineView(
        views,
        METRIC_NAME_OPERATION_LATENCY,
        GAX_METER_NAME,
        withAdditionalAttributes(ImmutableSet.of(METRIC_KEY_METHOD)));
    defineView(
        views,
        METRIC_NAME_ATTEMPT_LATENCY,
        GAX_METER_NAME,
        withAdditionalAttributes(ImmutableSet.of(METRIC_KEY_METHOD)));
    defineView(
        views,
        METRIC_NAME_OPERATION_COUNT,
        GAX_METER_NAME,
        withAdditionalAttributes(ImmutableSet.of(METRIC_KEY_METHOD)));
    defineView(
        views,
        METRIC_NAME_ATTEMPT_COUNT,
        GAX_METER_NAME,
        withAdditionalAttributes(ImmutableSet.of(METRIC_KEY_METHOD)));
    defineView(
        views,
        METRIC_NAME_FIRST_RESPONSE_LATENCY,
        FIRESTORE_METER_NAME,
        withAdditionalAttributes(ImmutableSet.of(METRIC_KEY_METHOD)));
    defineView(
        views,
        METRIC_NAME_END_TO_END_LATENCY,
        FIRESTORE_METER_NAME,
        withAdditionalAttributes(ImmutableSet.of(METRIC_KEY_METHOD)));

    // Define views with only COMMON_ATTRIBUTES
    defineView(views, METRIC_NAME_TRANSACTION_LATENCY, FIRESTORE_METER_NAME, COMMON_ATTRIBUTES);
    defineView(
        views, METRIC_NAME_TRANSACTION_ATTEMPT_COUNT, FIRESTORE_METER_NAME, COMMON_ATTRIBUTES);

    return views.build();
  }

  public static String getClientUid() {
    if (CLIENT_UID == null) {
      String identifier = UUID.randomUUID().toString();
      String pid = getProcessId();

      try {
        String hostname = InetAddress.getLocalHost().getHostName();
        CLIENT_UID = identifier + "@" + pid + "@" + hostname;
      } catch (UnknownHostException e) {
        CLIENT_UID = identifier + "@" + pid + "@localhost";
      }
    }
    return CLIENT_UID;
  }

  private static String getProcessId() {
    try {
      // Check if Java 9+ and ProcessHandle class is available
      Class<?> processHandleClass = Class.forName("java.lang.ProcessHandle");
      Method currentMethod = processHandleClass.getMethod("current");
      Object processHandleInstance = currentMethod.invoke(null);
      Method pidMethod = processHandleClass.getMethod("pid");
      long pid = (long) pidMethod.invoke(processHandleInstance);
      return Long.toString(pid);
    } catch (Exception e) {
      // Fallback to Java 8 method
      final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
      if (jvmName != null && jvmName.contains("@")) {
        return jvmName.split("@")[0];
      } else {
        return "unknown";
      }
    }
  }
}
