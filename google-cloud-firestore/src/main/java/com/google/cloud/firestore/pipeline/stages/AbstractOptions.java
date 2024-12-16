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

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.pipeline.expressions.Expr;
import com.google.cloud.firestore.pipeline.expressions.Field;
import com.google.cloud.firestore.pipeline.expressions.FunctionUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.firestore.v1.Value;
import java.util.List;

abstract class AbstractOptions<T> {

  protected final InternalOptions options;

  AbstractOptions(InternalOptions options) {
    this.options = options;
  }

  abstract T self(InternalOptions options);

  public T with(String key, String value) {
    return self(options.with(key, encodeValue(value)));
  }

  public T with(String key, boolean value) {
    return self(options.with(key, encodeValue(value)));
  }

  public T with(String key, long value) {
    return self(options.with(key, encodeValue(value)));
  }

  public T with(String key, double value) {
    return self(options.with(key, encodeValue(value)));
  }

  public T with(String key, Field value) {
    return self(options.with(key, value.toProto()));
  }

  protected T with(String key, List<? extends Expr> expressions) {
    return self(options.with(key, Lists.transform(expressions, FunctionUtils::exprToValue)));
  }

  protected T with(String key, AbstractOptions<?> subSection) {
    return self(options.with(key, subSection.options));
  }

  public T withSection(String key, GenericOptions subSection) {
    return with(key, subSection);
  }

  ImmutableMap<String, Value> toMap() {
    return options.options;
  }
}
