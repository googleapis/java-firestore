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

import com.google.cloud.firestore.pipeline.expressions.Field;
import java.util.Arrays;

public class CollectionOptions extends AbstractOptions<CollectionOptions> {

  public static final CollectionOptions DEFAULT = new CollectionOptions(InternalOptions.EMPTY);

  CollectionOptions(InternalOptions options) {
    super(options);
  }

  @Override
  CollectionOptions self(InternalOptions options) {
    return new CollectionOptions(options);
  }

  public CollectionOptions withHints(Hints hints) {
    return with("hints", hints);
  }

  public static class Hints extends AbstractOptions<Hints> {

    public static Hints DEFAULT = new Hints(InternalOptions.EMPTY);

    Hints(InternalOptions options) {
      super(options);
    }

    @Override
    Hints self(InternalOptions options) {
      return new Hints(options);
    }

    public Hints withIgnoreIndexFields(Field... fields) {
      return with("ignore_index_fields", Arrays.asList(fields));
    }
  }
}
