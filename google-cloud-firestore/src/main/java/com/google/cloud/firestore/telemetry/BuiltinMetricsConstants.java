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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BuiltinMetricsConstants {

  static final String FIRESTORE_METER_NAME = "firestore_java";

  // TODO: change to firestore.googleapis.com
  public static final String METER_NAME = "custom.googleapis.com/internal/client";
  public static final String GAX_METER_NAME = OpenTelemetryMetricsRecorder.GAX_METER_NAME;

  // Metric attribute keys for monitored resource
  static final AttributeKey<String> PROJECT_ID_KEY = AttributeKey.stringKey("project_id");
  // public static final AttributeKey<String> INSTANCE_ID_KEY = AttributeKey.stringKey("instance");

  // Metric attribute keys for labels
  // static final AttributeKey<String> LANGUAGE_KEY = AttributeKey.stringKey("language");
  static final AttributeKey<String> METHOD_NAME_KEY = AttributeKey.stringKey("method_name");
  static final AttributeKey<String> STATUS_KEY = AttributeKey.stringKey("status");
  public static final AttributeKey<String> DATABASE_ID_KEY = AttributeKey.stringKey("database_id");
  static final AttributeKey<String> CLIENT_LIBRARY_KEY = AttributeKey.stringKey("client_library");
  static final AttributeKey<String> LIBRARY_VERSION_KEY = AttributeKey.stringKey("library_version");
  static final AttributeKey<String> CLIENT_UID_KEY = AttributeKey.stringKey("client_uid");

  static final AttributeKey<String> Language_KEY = AttributeKey.stringKey("language");
  public static final AttributeKey<String> SERVICE_NAME = AttributeKey.stringKey("service.name");
  // Metric
  static final String OPERATION_LATENCY_NAME = "operation_latency";
  static final String OPERATION_COUNT_NAME = "operation_count";
  static final String ATTEMPT_LATENCY_NAME = "attempt_latency";
  static final String ATTEMPT_COUNT_NAME = "attempt_count";
  static final String FIRST_RESPONSE_LATENCY_NAME = "first_response_latency";
  static final String END_TO_END_LATENCY_NAME = "end_to_end_latency";
  static final String TRANSACTION_LATENCY_NAME = "transaction_latency";
  static final String TRANSACTION_ATTEMPT_COUNT_NAME = "transaction_attempt_count";

  static final String MILLISECOND_UNIT = "ms";

  public static final String ENABLE_METRICS_ENV_VAR = "FIRESTORE_ENABLE_TRACING";

  public static final Set<AttributeKey> COMMON_ATTRIBUTES =
      ImmutableSet.of(
          PROJECT_ID_KEY,
          DATABASE_ID_KEY,
          CLIENT_UID_KEY,
          CLIENT_LIBRARY_KEY,
          LIBRARY_VERSION_KEY,
          STATUS_KEY);

  public static final Set<String> BUILTIN_METRICS =
      ImmutableSet.of(
              OPERATION_LATENCY_NAME,
              ATTEMPT_LATENCY_NAME,
              OPERATION_COUNT_NAME,
              ATTEMPT_COUNT_NAME,
              FIRST_RESPONSE_LATENCY_NAME,
              END_TO_END_LATENCY_NAME,
              TRANSACTION_LATENCY_NAME,
              TRANSACTION_ATTEMPT_COUNT_NAME)
          .stream()
          .map(m -> METER_NAME + '/' + m)
          .collect(Collectors.toSet());

  static void defineView(
      ImmutableMap.Builder<InstrumentSelector, View> viewMap,
      String id,
      Set<AttributeKey> attributes) {
    InstrumentSelector selector =
        InstrumentSelector.builder().setName(METER_NAME + "/" + id).build();
    Set<String> attributesFilter =
        ImmutableSet.<String>builder()
            .addAll(attributes.stream().map(AttributeKey::getKey).collect(Collectors.toSet()))
            .build();
    View view = View.builder().setAttributeFilter(attributesFilter).build();

    viewMap.put(selector, view);
  }

  public static Map<InstrumentSelector, View> getAllViews() {
    ImmutableMap.Builder<InstrumentSelector, View> views = ImmutableMap.builder();

    defineView(
        views,
        OPERATION_LATENCY_NAME,
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(METHOD_NAME_KEY)
            .build());
    defineView(
        views,
        ATTEMPT_LATENCY_NAME,
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(METHOD_NAME_KEY)
            .build());

    defineView(
        views,
        OPERATION_COUNT_NAME,
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(METHOD_NAME_KEY)
            .build());

    defineView(
        views,
        ATTEMPT_COUNT_NAME,
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(METHOD_NAME_KEY)
            .build());

    defineView(
        views,
        FIRST_RESPONSE_LATENCY_NAME,
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(METHOD_NAME_KEY)
            .build());

    defineView(
        views,
        END_TO_END_LATENCY_NAME,
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(METHOD_NAME_KEY)
            .build());
    return views.build();
  }
}
