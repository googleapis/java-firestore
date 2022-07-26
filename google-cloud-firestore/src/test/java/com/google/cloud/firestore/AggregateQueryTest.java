/*
 * Copyright 2022 Google LLC
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

package com.google.cloud.firestore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AggregateQueryTest {

  @Mock
  private Query mockQuery;
  @Mock
  private Query mockQuery2;

  @Test
  public void getQueryShouldReturnTheQuerySpecifiedToTheConstructor() {
    AggregateQuery aggregateQuery = new AggregateQuery(mockQuery);
    assertSame(mockQuery, aggregateQuery.getQuery());
  }

  @Test
  public void hashCodeShouldReturnHashCodeOfUnderlyingQuery() {
    AggregateQuery aggregateQuery = new AggregateQuery(mockQuery);
    assertEquals(mockQuery.hashCode(), aggregateQuery.hashCode());
  }

  @Test
  public void equalsShouldReturnFalseWhenGivenNull() {
    AggregateQuery aggregateQuery = new AggregateQuery(mockQuery);
    assertFalse(aggregateQuery.equals(null));
  }

  @Test
  public void equalsShouldReturnFalseWhenGivenADifferentObject() {
    AggregateQuery aggregateQuery = new AggregateQuery(mockQuery);
    assertFalse(aggregateQuery.equals("Not An AggregateQuery"));
  }

  @Test
  public void equalsShouldReturnFalseWhenGivenAnAggregateQueryWithADifferentQuery() {
    AggregateQuery aggregateQuery1 = new AggregateQuery(mockQuery);
    AggregateQuery aggregateQuery2 = new AggregateQuery(mockQuery2);
    assertFalse(aggregateQuery1.equals(aggregateQuery2));
  }

  @Test
  public void equalsShouldReturnTrueWhenGivenTheSameAggregateQueryInstance() {
    AggregateQuery aggregateQuery = new AggregateQuery(mockQuery);
    assertTrue(aggregateQuery.equals(aggregateQuery));
  }

  @Test
  public void equalsShouldReturnTrueWhenGivenAnAggregateQueryWithTheSameQuery() {
    AggregateQuery aggregateQuery1 = new AggregateQuery(mockQuery);
    AggregateQuery aggregateQuery2 = new AggregateQuery(mockQuery);
    assertTrue(aggregateQuery1.equals(aggregateQuery2));
  }

}
