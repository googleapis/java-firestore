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

import static com.github.stefanbirkner.systemlambda.SystemLambda.*;
import static com.google.common.truth.Truth.assertThat;

import com.google.cloud.firestore.FirestoreOptions;
import org.junit.Test;

public class MetricsUtilTest {
  @Test
  public void defaultOptionsUseEnabledMetricsUtil() {
    MetricsUtil util =
        MetricsUtil.getInstance(
            FirestoreOptions.newBuilder()
                .setProjectId("test-project")
                .setDatabaseId("(default)")
                .build());

    assertThat(util instanceof EnabledMetricsUtil).isTrue();
  }

  @Test
  public void testFirestoreWithMetricsEnabled() throws Exception {
    withEnvironmentVariable("FIRESTORE_ENABLE_METRICS", "off")
        .execute(
            () -> {
              MetricsUtil util =
                  MetricsUtil.getInstance(
                      FirestoreOptions.newBuilder()
                          .setProjectId("test-project")
                          .setDatabaseId("(default)")
                          .build());

              assertThat(util instanceof DisabledMetricsUtil).isTrue();
            });
  }
}
