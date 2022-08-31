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

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AggregateQueryTest {

  @Mock private Query mockQuery;
  @Mock private Query mockQuery2;

  @Test
  public void getQueryShouldReturnTheQuerySpecifiedToTheConstructor() {
    AggregateQuery aggregateQuery = new AggregateQuery(mockQuery);
    assertThat(aggregateQuery.getQuery()).isSameInstanceAs(mockQuery);
  }

  @Test
  public void hashCodeShouldReturnHashCodeOfUnderlyingQuery() {
    AggregateQuery aggregateQuery = new AggregateQuery(mockQuery);
    assertThat(aggregateQuery.hashCode()).isEqualTo(mockQuery.hashCode());
  }

  @Test
  public void equalsShouldReturnFalseWhenGivenNull() {
    AggregateQuery aggregateQuery = new AggregateQuery(mockQuery);
    assertThat(aggregateQuery.equals(null)).isFalse();
  }

  @Test
  public void equalsShouldReturnFalseWhenGivenADifferentObject() {
    AggregateQuery aggregateQuery = new AggregateQuery(mockQuery);
    assertThat(aggregateQuery.equals("Not An AggregateQuery")).isFalse();
  }

  @Test
  public void equalsShouldReturnFalseWhenGivenAnAggregateQueryWithADifferentQuery() {
    AggregateQuery aggregateQuery1 = new AggregateQuery(mockQuery);
    AggregateQuery aggregateQuery2 = new AggregateQuery(mockQuery2);
    assertThat(aggregateQuery1.equals(aggregateQuery2)).isFalse();
  }

  @Test
  public void equalsShouldReturnTrueWhenGivenTheSameAggregateQueryInstance() {
    AggregateQuery aggregateQuery = new AggregateQuery(mockQuery);
    assertThat(aggregateQuery.equals(aggregateQuery)).isTrue();
  }

  @Test
  public void equalsShouldReturnTrueWhenGivenAnAggregateQueryWithTheSameQuery() {
    AggregateQuery aggregateQuery1 = new AggregateQuery(mockQuery);
    AggregateQuery aggregateQuery2 = new AggregateQuery(mockQuery);
    assertThat(aggregateQuery1.equals(aggregateQuery2)).isTrue();
  }
}
