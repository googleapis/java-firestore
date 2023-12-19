/*
 * Copyright 2023 Google LLC
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

import static com.google.cloud.firestore.telemetry.OpenTelemetryUtil.ENABLE_OPEN_TELEMETRY_ENV_VAR_NAME;
import static com.google.common.truth.Truth.assertThat;

import com.google.cloud.firestore.*;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import javax.annotation.Nullable;
import org.junit.*;
import uk.org.webcompere.systemstubs.rules.EnvironmentVariablesRule;

public class OpenTelemetryOptionsTest {
  @Nullable private Firestore firestore;

  @Rule public EnvironmentVariablesRule environmentVariablesRule = new EnvironmentVariablesRule();

  @Before
  public void setUp() {
    GlobalOpenTelemetry.resetForTest();
  }

  @After
  public void tearDown() {
    if (firestore != null) {
      firestore.shutdown();
      firestore = null;
    }
  }

  FirestoreOptions.Builder getBaseOptions() {
    return FirestoreOptions.newBuilder().setProjectId("test-project").setDatabaseId("(default)");
  }

  @Test
  public void defaultOptionsDisablesTelemetryCollection() {
    FirestoreOptions firestoreOptions = getBaseOptions().build();
    firestore = firestoreOptions.getService();
    assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
    assertThat(firestore.getOpenTelemetryUtil() instanceof DisabledOpenTelemetryUtil).isTrue();
    assertThat(firestore.getTraceUtil()).isNotNull();
    assertThat(firestore.getTraceUtil() instanceof DisabledTraceUtil).isTrue();
  }

  @Test
  public void canEnableTelemetryCollectionUsingOpenTelemetryOptions() {
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setTelemetryCollectionEnabled(true)
                    .build())
            .build();
    firestore = firestoreOptions.getService();
    assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
    assertThat(firestore.getOpenTelemetryUtil() instanceof EnabledOpenTelemetryUtil).isTrue();
    assertThat(firestore.getTraceUtil()).isNotNull();
    assertThat(firestore.getTraceUtil() instanceof EnabledTraceUtil).isTrue();
  }

  @Test
  public void usesGlobalOpenTelemetryIfOpenTelemetryNotProvidedInOptions() {
    // Register a global OpenTelemetry SDK.
    OpenTelemetrySdk.builder().buildAndRegisterGlobal();

    // Do _not_ pass it to FirestoreOptions.
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setTelemetryCollectionEnabled(true)
                    .build())
            .build();
    firestore = firestoreOptions.getService();
    assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
    assertThat(firestore.getOpenTelemetryUtil() instanceof EnabledOpenTelemetryUtil).isTrue();
    EnabledOpenTelemetryUtil enabledOpenTelemetryUtil =
        (EnabledOpenTelemetryUtil) firestore.getOpenTelemetryUtil();
    assertThat(enabledOpenTelemetryUtil.getOpenTelemetry()).isEqualTo(GlobalOpenTelemetry.get());
  }

  @Test
  public void canPassOpenTelemetryToFirestore() {
    OpenTelemetrySdk myOpenTelemetrySdk = OpenTelemetrySdk.builder().build();
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setTelemetryCollectionEnabled(true)
                    .setOpenTelemetry(myOpenTelemetrySdk)
                    .build())
            .build();
    firestore = firestoreOptions.getService();
    assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
    assertThat(firestore.getOpenTelemetryUtil() instanceof EnabledOpenTelemetryUtil).isTrue();
    EnabledOpenTelemetryUtil enabledOpenTelemetryUtil =
        (EnabledOpenTelemetryUtil) firestore.getOpenTelemetryUtil();
    assertThat(enabledOpenTelemetryUtil.getOpenTelemetry()).isEqualTo(myOpenTelemetrySdk);
  }

  @Test
  public void telemetryCollectionRemainsDisabledIfOpenTelemetryIsProvided() {
    OpenTelemetrySdk myOpenTelemetrySdk = OpenTelemetrySdk.builder().build();
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setTelemetryCollectionEnabled(false)
                    .setOpenTelemetry(myOpenTelemetrySdk)
                    .build())
            .build();
    firestore = firestoreOptions.getService();
    assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
    assertThat(firestore.getOpenTelemetryUtil() instanceof DisabledOpenTelemetryUtil).isTrue();
  }

  @Test
  public void canEnableTelemetryCollectionUsingEnvVar() {
    environmentVariablesRule.set(ENABLE_OPEN_TELEMETRY_ENV_VAR_NAME, "true");

    // Do not enable OpenTelemetry using FirestoreOptions.
    FirestoreOptions firestoreOptions = getBaseOptions().build();
    firestore = firestoreOptions.getService();

    // Expect OpenTelemetry to be enabled because of the environment variable.
    assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
    assertThat(firestore.getOpenTelemetryUtil() instanceof EnabledOpenTelemetryUtil).isTrue();
    assertThat(firestore.getTraceUtil()).isNotNull();
    assertThat(firestore.getTraceUtil() instanceof EnabledTraceUtil).isTrue();
  }

  @Test
  public void canEnableTelemetryCollectionUsingEnvVar2() {
    environmentVariablesRule.set(ENABLE_OPEN_TELEMETRY_ENV_VAR_NAME, "on");

    // Do not enable OpenTelemetry using FirestoreOptions.
    FirestoreOptions firestoreOptions = getBaseOptions().build();
    firestore = firestoreOptions.getService();

    // Expect OpenTelemetry to be enabled because of the environment variable.
    assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
    assertThat(firestore.getOpenTelemetryUtil() instanceof EnabledOpenTelemetryUtil).isTrue();
    assertThat(firestore.getTraceUtil()).isNotNull();
    assertThat(firestore.getTraceUtil() instanceof EnabledTraceUtil).isTrue();
  }

  @Test
  public void canDisableTelemetryCollectionUsingEnvVar() {
    environmentVariablesRule.set(ENABLE_OPEN_TELEMETRY_ENV_VAR_NAME, "false");

    // Enable OpenTelemetry using FirestoreOptions.
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setTelemetryCollectionEnabled(true)
                    .build())
            .build();
    firestore = firestoreOptions.getService();

    // Expect OpenTelemetry to be disabled because of the environment variable.
    assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
    assertThat(firestore.getOpenTelemetryUtil() instanceof DisabledOpenTelemetryUtil).isTrue();
    assertThat(firestore.getTraceUtil()).isNotNull();
    assertThat(firestore.getTraceUtil() instanceof DisabledTraceUtil).isTrue();
  }

  @Test
  public void canDisableTelemetryCollectionUsingEnvVar2() {
    environmentVariablesRule.set(ENABLE_OPEN_TELEMETRY_ENV_VAR_NAME, "off");

    // Enable OpenTelemetry using FirestoreOptions.
    FirestoreOptions firestoreOptions =
        getBaseOptions()
            .setOpenTelemetryOptions(
                FirestoreOpenTelemetryOptions.newBuilder()
                    .setTelemetryCollectionEnabled(true)
                    .build())
            .build();
    firestore = firestoreOptions.getService();

    // Expect OpenTelemetry to be disabled because of the environment variable.
    assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
    assertThat(firestore.getOpenTelemetryUtil() instanceof DisabledOpenTelemetryUtil).isTrue();
    assertThat(firestore.getTraceUtil()).isNotNull();
    assertThat(firestore.getTraceUtil() instanceof DisabledTraceUtil).isTrue();
  }
}
