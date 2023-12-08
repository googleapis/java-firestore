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

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;

import com.google.cloud.firestore.*;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import org.junit.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.annotation.Nullable;

public class OpenTelemetryOptionsTest{
    private MockedStatic<OpenTelemetryUtil> openTelemetryUtilMock;
    private MockedStatic<EnabledOpenTelemetryUtil> enabledOpenTelemetryUtilMock;

    @Nullable
    private Firestore firestore;

    @Before
    public void setUp() {
        this.openTelemetryUtilMock = Mockito.mockStatic(
                OpenTelemetryUtil.class, Mockito.withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
        this.enabledOpenTelemetryUtilMock = Mockito.mockStatic(
                EnabledOpenTelemetryUtil.class, Mockito.withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
    }

    @After
    public void tearDown() {
        this.openTelemetryUtilMock.close();
        this.enabledOpenTelemetryUtilMock.close();
        if (firestore != null) {
            firestore.shutdown();
            firestore = null;
        }
    }

    @Test
    public void defaultOptionsDisablesTelemetryCollection() {
        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().build();
        firestore = firestoreOptions.getService();
        assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
        assertThat(firestore.getOpenTelemetryUtil() instanceof DisabledOpenTelemetryUtil).isTrue();
        assertThat(firestore.getTraceUtil()).isNotNull();
        assertThat(firestore.getTraceUtil() instanceof DisabledTraceUtil).isTrue();
    }

//    @Test
//    public void canEnableTelemetryCollectionUsingOpenTelemetryOptions() {
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().setOpenTelemetryOptions(
//                FirestoreOpenTelemetryOptions
//                        .newBuilder()
//                        .setTelemetryCollectionEnabled(true)
//                        .build()
//        ).build();
//        firestore = firestoreOptions.getService();
//        assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
//        assertThat(firestore.getOpenTelemetryUtil() instanceof EnabledOpenTelemetryUtil).isTrue();
//        assertThat(firestore.getTraceUtil()).isNotNull();
//        assertThat(firestore.getTraceUtil() instanceof EnabledTraceUtil).isTrue();
//    }
//
//    @Test
//    public void canPassOpenTelemetrySdkToFirestore() {
//        OpenTelemetrySdk myOpenTelemetrySdk = OpenTelemetrySdk.builder().build();
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().setOpenTelemetryOptions(
//                FirestoreOpenTelemetryOptions
//                        .newBuilder()
//                        .setTelemetryCollectionEnabled(true)
//                        .setOpenTelemetrySdk(myOpenTelemetrySdk)
//                        .build()
//        ).build();
//        firestore = firestoreOptions.getService();
//        assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
//        assertThat(firestore.getOpenTelemetryUtil() instanceof EnabledOpenTelemetryUtil).isTrue();
//        EnabledOpenTelemetryUtil enabledOpenTelemetryUtil = (EnabledOpenTelemetryUtil) firestore.getOpenTelemetryUtil();
//        assertThat(enabledOpenTelemetryUtil.getOpenTelemetrySdk()).isEqualTo(myOpenTelemetrySdk);
//    }
//
//    @Test
//    public void telemetryCollectionRemainsDisabledIfOpenTelemetrySdkIsProvided() {
//        OpenTelemetrySdk myOpenTelemetrySdk = OpenTelemetrySdk.builder().build();
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().setOpenTelemetryOptions(
//                FirestoreOpenTelemetryOptions
//                        .newBuilder()
//                        .setTelemetryCollectionEnabled(false)
//                        .setOpenTelemetrySdk(myOpenTelemetrySdk)
//                        .build()
//        ).build();
//        firestore = firestoreOptions.getService();
//        assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
//        assertThat(firestore.getOpenTelemetryUtil() instanceof DisabledOpenTelemetryUtil).isTrue();
//    }
//
//    @Test
//    public void canSetTraceSamplingRate() {
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().setOpenTelemetryOptions(
//                FirestoreOpenTelemetryOptions
//                        .newBuilder()
//                        .setTelemetryCollectionEnabled(true)
//                        .setTraceSamplingRate(0.25)
//                        .build()
//        ).build();
//        firestore = firestoreOptions.getService();
//        assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
//        assertThat(firestore.getOpenTelemetryUtil() instanceof EnabledOpenTelemetryUtil).isTrue();
//        EnabledOpenTelemetryUtil enabledOpenTelemetryUtil = (EnabledOpenTelemetryUtil) firestore.getOpenTelemetryUtil();
//        assertThat(enabledOpenTelemetryUtil.getTraceSamplingRate()).isEqualTo(0.25);
//    }
//
//    @Test
//    public void cannotSetOpenTelemetrySdkAndTraceSamplingRateAtTheSameTime() {
//        OpenTelemetrySdk myOpenTelemetrySdk = OpenTelemetrySdk.builder().build();
//        try {
//            FirestoreOptions.newBuilder().setOpenTelemetryOptions(
//                    FirestoreOpenTelemetryOptions
//                            .newBuilder()
//                            .setTelemetryCollectionEnabled(true)
//                            .setOpenTelemetrySdk(myOpenTelemetrySdk)
//                            .setTraceSamplingRate(0.25)
//                            .build()
//            ).build();
//        } catch (Exception e) {
//            assertThat(e instanceof IllegalArgumentException).isTrue();
//            assertEquals(e.getMessage(), "Cannot set OpenTelemetry trace sampling rate because you have already provided an OpenTelemetrySdk object. Please set the sampling rate directly on the OpenTelemetrySdk object.");
//        }
//
//        try {
//            FirestoreOptions.newBuilder().setOpenTelemetryOptions(
//                    FirestoreOpenTelemetryOptions
//                            .newBuilder()
//                            .setTelemetryCollectionEnabled(true)
//                            .setTraceSamplingRate(0.25)
//                            .setOpenTelemetrySdk(myOpenTelemetrySdk)
//                            .build()
//            ).build();
//        } catch (Exception e) {
//            assertThat(e instanceof IllegalArgumentException).isTrue();
//            assertEquals(e.getMessage(),
//                    "OpenTelemetry trace sampling rate should not be set because you are providing an OpenTelemetrySdk object. Please set the sampling rate directly on the OpenTelemetrySdk object.");
//        }
//    }
//
//    @Test
//    public void canEnableTelemetryCollectionUsingEnvVar() {
//        openTelemetryUtilMock.when(OpenTelemetryUtil::getEnableOpenTelemetryEnvVar).thenReturn("true");
//
//        // Do not enable OpenTelemetry using FirestoreOptions.
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().build();
//        firestore = firestoreOptions.getService();
//
//        // Expect OpenTelemetry to be enabled because of the environment variable.
//        assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
//        assertThat(firestore.getOpenTelemetryUtil() instanceof EnabledOpenTelemetryUtil).isTrue();
//        assertThat(firestore.getTraceUtil()).isNotNull();
//        assertThat(firestore.getTraceUtil() instanceof EnabledTraceUtil).isTrue();
//    }
//
//    @Test
//    public void canEnableTelemetryCollectionUsingEnvVar2() {
//        openTelemetryUtilMock.when(OpenTelemetryUtil::getEnableOpenTelemetryEnvVar).thenReturn("on");
//
//        // Do not enable OpenTelemetry using FirestoreOptions.
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().build();
//        firestore = firestoreOptions.getService();
//
//        // Expect OpenTelemetry to be enabled because of the environment variable.
//        assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
//        assertThat(firestore.getOpenTelemetryUtil() instanceof EnabledOpenTelemetryUtil).isTrue();
//        assertThat(firestore.getTraceUtil()).isNotNull();
//        assertThat(firestore.getTraceUtil() instanceof EnabledTraceUtil).isTrue();
//    }
//
//    @Test
//    public void canDisableTelemetryCollectionUsingEnvVar() {
//        openTelemetryUtilMock.when(OpenTelemetryUtil::getEnableOpenTelemetryEnvVar).thenReturn("false");
//
//        // Enable OpenTelemetry using FirestoreOptions.
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().setOpenTelemetryOptions(
//                FirestoreOpenTelemetryOptions
//                        .newBuilder()
//                        .setTelemetryCollectionEnabled(true)
//                        .build()
//        ).build();
//        firestore = firestoreOptions.getService();
//
//        // Expect OpenTelemetry to be disabled because of the environment variable.
//        assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
//        assertThat(firestore.getOpenTelemetryUtil() instanceof DisabledOpenTelemetryUtil).isTrue();
//        assertThat(firestore.getTraceUtil()).isNotNull();
//        assertThat(firestore.getTraceUtil() instanceof DisabledTraceUtil).isTrue();
//    }
//
//    @Test
//    public void canDisableTelemetryCollectionUsingEnvVar2() {
//        openTelemetryUtilMock.when(OpenTelemetryUtil::getEnableOpenTelemetryEnvVar).thenReturn("off");
//
//        // Enable OpenTelemetry using FirestoreOptions.
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().setOpenTelemetryOptions(
//                FirestoreOpenTelemetryOptions
//                        .newBuilder()
//                        .setTelemetryCollectionEnabled(true)
//                        .build()
//        ).build();
//        firestore = firestoreOptions.getService();
//
//        // Expect OpenTelemetry to be disabled because of the environment variable.
//        assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
//        assertThat(firestore.getOpenTelemetryUtil() instanceof DisabledOpenTelemetryUtil).isTrue();
//        assertThat(firestore.getTraceUtil()).isNotNull();
//        assertThat(firestore.getTraceUtil() instanceof DisabledTraceUtil).isTrue();
//    }
//
//    @Test
//    public void canOverrideTraceSamplingRateUsingEnvVar() {
//        enabledOpenTelemetryUtilMock.when(EnabledOpenTelemetryUtil::getOpenTelemetryTraceSamplingRateEnvVar).thenReturn("0.12");
//
//        // Set the trace sampling rate to 25% in Firestore
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().setOpenTelemetryOptions(
//                FirestoreOpenTelemetryOptions
//                        .newBuilder()
//                        .setTelemetryCollectionEnabled(true)
//                        .setTraceSamplingRate(0.25)
//                        .build()
//        ).build();
//        firestore = firestoreOptions.getService();
//
//        // Expect OpenTelemetry to be disabled because of the environment variable.
//        assertThat(firestore.getOpenTelemetryUtil()).isNotNull();
//        assertThat(firestore.getOpenTelemetryUtil() instanceof EnabledOpenTelemetryUtil).isTrue();
//        EnabledOpenTelemetryUtil enabledOpenTelemetryUtil = (EnabledOpenTelemetryUtil) firestore.getOpenTelemetryUtil();
//        assertThat(enabledOpenTelemetryUtil.getTraceSamplingRate()).isEqualTo(0.12);
//    }
}
