/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.firestore.snippets;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.firestore.BaseIntegrationTest;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
@SuppressWarnings("checkstyle:abbreviationaswordinname")
public class MultipleRangeInequalitySnippetsIT extends BaseIntegrationTest {

  private static MultipleRangeInequalitySnippets multipleRangeInequalitySnippets;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    multipleRangeInequalitySnippets = new MultipleRangeInequalitySnippets(db);
    multipleRangeInequalitySnippets.prepareExamples();
  }

  @Test
  public void testRangeMultipleInequalityFilter() throws Exception {
    Query query = multipleRangeInequalitySnippets.rangeMultipleInequalityFilter();
    Set<String> expected = newHashSet();
    Set<String> actual = getResultsAsSet(query);
    assertEquals(expected, actual);
  }

  @AfterClass
  public static void tearDown() throws Exception {
    deleteAllDocuments(db);
    multipleRangeInequalitySnippets.close();
  }
}
