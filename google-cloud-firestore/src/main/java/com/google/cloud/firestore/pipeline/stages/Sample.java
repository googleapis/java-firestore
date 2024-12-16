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

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.common.collect.ImmutableList;
import com.google.firestore.v1.Value;
import javax.annotation.Nonnull;

public final class Sample extends Stage {

  private final Number size;
  private final Mode mode;

  public enum Mode {
    DOCUMENTS(encodeValue("documents")),
    PERCENT(encodeValue("percent"));

    public final Value value;

    Mode(Value value) {
      this.value = value;
    }
  }

  @BetaApi
  public static Sample withPercentage(double percentage) {
    return new Sample(percentage, Mode.PERCENT, SampleOptions.DEFAULT);
  }

  @BetaApi
  public static Sample withDocLimit(int documents) {
    return new Sample(documents, Mode.DOCUMENTS, SampleOptions.DEFAULT);
  }

  @BetaApi
  public Sample withOptions(@Nonnull SampleOptions options) {
    return new Sample(size, mode, options);
  }

  @InternalApi
  private Sample(Number size, Mode mode, SampleOptions options) {
    super("sample", options.options);
    this.size = size;
    this.mode = mode;
  }

  @Override
  Iterable<Value> toStageArgs() {
    return ImmutableList.of(encodeValue(size), mode.value);
  }
}
