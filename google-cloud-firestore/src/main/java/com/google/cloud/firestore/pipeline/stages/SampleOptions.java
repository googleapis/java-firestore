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

package com.google.cloud.firestore.pipeline.stages;

import static com.google.cloud.firestore.PipelineUtils.encodeValue;

import com.google.common.collect.ImmutableList;
import com.google.firestore.v1.Value;

public class SampleOptions {

  private final Number n;
  private final Mode mode;

  private SampleOptions(Number n, Mode mode) {
    this.n = n;
    this.mode = mode;
  }

  public enum Mode {
    DOCUMENTS(Value.newBuilder().setStringValue("documents").build()),
    PERCENT(Value.newBuilder().setStringValue("percent").build());

    public final Value value;

    Mode(Value value) {
      this.value = value;
    }
  }

  public static SampleOptions percentage(double percentage) {
    return new SampleOptions(percentage, Mode.PERCENT);
  }

  public static SampleOptions docLimit(int documents) {
    return new SampleOptions(documents, Mode.DOCUMENTS);
  }

  Iterable<Value> getProtoArgs() {
    return ImmutableList.of(encodeValue(n), mode.value);
  }
}
