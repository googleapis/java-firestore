/*
 * Copyright 2026 Google LLC
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.cloud.firestore.pipeline.expressions.Field;
import com.google.firestore.v1.Value;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class LiteralsTest {

  @Test
  public void testToStageArgsWithConstant() {
    Map<String, Object> map = new HashMap<>();
    map.put("title", "The Hitchhiker's Guide to the Galaxy");

    Literals literals = new Literals(map);
    Iterable<Value> args = literals.toStageArgs();

    assertTrue(args.iterator().hasNext());
    Value value = args.iterator().next();
    assertTrue(value.hasMapValue());
    assertEquals(
        "The Hitchhiker's Guide to the Galaxy",
        value.getMapValue().getFieldsOrThrow("title").getStringValue());
  }

  @Test
  public void testToStageArgsWithExpression() {
    Map<String, Object> map = new HashMap<>();
    map.put("title", "The Hitchhiker's Guide to the Galaxy");
    map.put(
        "message",
        com.google.cloud.firestore.pipeline.expressions.Expression.concat(
            Field.ofServerPath("user_name"), " is awesome"));

    Literals literals = new Literals(map);
    Iterable<Value> args = literals.toStageArgs();

    assertTrue(args.iterator().hasNext());
    Value value = args.iterator().next();
    assertTrue(value.hasMapValue());
    assertEquals(
        "The Hitchhiker's Guide to the Galaxy",
        value.getMapValue().getFieldsOrThrow("title").getStringValue());

    Value messageValue = value.getMapValue().getFieldsOrThrow("message");
    assertTrue(messageValue.hasFunctionValue());
    com.google.firestore.v1.Function func = messageValue.getFunctionValue();
    assertEquals("concat", func.getName());
    assertEquals(2, func.getArgsCount());

    Value arg0 = func.getArgs(0);
    assertTrue(arg0.hasFieldReferenceValue());
    assertEquals("user_name", arg0.getFieldReferenceValue());

    Value arg1 = func.getArgs(1);
    assertTrue(arg1.hasStringValue());
    assertEquals(" is awesome", arg1.getStringValue());
  }
}
