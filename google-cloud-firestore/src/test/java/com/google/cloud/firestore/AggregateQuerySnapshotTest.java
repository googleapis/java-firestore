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

import com.google.cloud.Timestamp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AggregateQuerySnapshotTest {

  @Mock
  private Query mockQuery;
  @Mock
  private Query mockQuery2;

  private AggregateQuery sampleAggregateQuery;
  private AggregateQuery sampleAggregateQuery2;
  private Timestamp sampleTimestamp;
  private Timestamp sampleTimestamp2;

  @Before
  public void initializeSampleObjects() {
    sampleAggregateQuery = new AggregateQuery(mockQuery);
    sampleAggregateQuery2 = new AggregateQuery(mockQuery2);
    sampleTimestamp = Timestamp.ofTimeSecondsAndNanos(42, 42);
    sampleTimestamp2 = Timestamp.ofTimeSecondsAndNanos(24, 24);
  }

  @Test
  public void getQueryShouldReturnTheAggregateQuerySpecifiedToTheConstructor() {
    AggregateQuerySnapshot snapshot = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    assertSame(sampleAggregateQuery, snapshot.getQuery());
  }

  @Test
  public void getReadTimeShouldReturnTheTimestampSpecifiedToTheConstructor() {
    AggregateQuerySnapshot snapshot = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    assertSame(sampleTimestamp, snapshot.getReadTime());
  }

  @Test
  public void getCountShouldReturnTheCountSpecifiedToTheConstructor() {
    AggregateQuerySnapshot snapshot = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    assertEquals(Long.valueOf(42), snapshot.getCount());
  }

  @Test
  public void hashCodeShouldReturnSameHashCodeWhenConstructedWithSameObjects() {
    AggregateQuerySnapshot snapshot1 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    AggregateQuerySnapshot snapshot2 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    assertEquals(snapshot1.hashCode(), snapshot2.hashCode());
  }

  @Test
  public void hashCodeShouldReturnDifferentHashCodeWhenConstructedDifferentAggregateQuery() {
    AggregateQuerySnapshot snapshot1 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    AggregateQuerySnapshot snapshot2 = new AggregateQuerySnapshot(sampleAggregateQuery2, sampleTimestamp, 42);
    assertNotEquals(snapshot1.hashCode(), snapshot2.hashCode());
  }

  @Test
  public void hashCodeShouldReturnDifferentHashCodeWhenConstructedDifferentTimestamp() {
    AggregateQuerySnapshot snapshot1 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    AggregateQuerySnapshot snapshot2 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp2, 42);
    assertNotEquals(snapshot1.hashCode(), snapshot2.hashCode());
  }

  @Test
  public void hashCodeShouldReturnDifferentHashCodeWhenConstructedDifferentCount() {
    AggregateQuerySnapshot snapshot1 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    AggregateQuerySnapshot snapshot2 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 24);
    assertNotEquals(snapshot1.hashCode(), snapshot2.hashCode());
  }

  @Test
  public void equalsShouldReturnFalseWhenGivenNull() {
    AggregateQuerySnapshot snapshot = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    assertFalse(snapshot.equals(null));
  }

  @Test
  public void equalsShouldReturnFalseWhenGivenADifferentObject() {
    AggregateQuerySnapshot snapshot = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    assertFalse(snapshot.equals("Not An AggregateQuerySnapshot"));
  }

  @Test
  public void equalsShouldReturnFalseWhenGivenAnAggregateQuerySnapshotWithADifferentQuery() {
    AggregateQuerySnapshot snapshot1 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    AggregateQuerySnapshot snapshot2 = new AggregateQuerySnapshot(sampleAggregateQuery2, sampleTimestamp, 42);
    assertFalse(snapshot1.equals(snapshot2));
  }

  @Test
  public void equalsShouldReturnFalseWhenGivenAnAggregateQuerySnapshotWithADifferentReadTime() {
    AggregateQuerySnapshot snapshot1 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    AggregateQuerySnapshot snapshot2 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp2, 42);
    assertFalse(snapshot1.equals(snapshot2));
  }

  @Test
  public void equalsShouldReturnFalseWhenGivenAnAggregateQuerySnapshotWithADifferentCount() {
    AggregateQuerySnapshot snapshot1 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    AggregateQuerySnapshot snapshot2 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 24);
    assertFalse(snapshot1.equals(snapshot2));
  }

  @Test
  public void equalsShouldReturnTrueWhenGivenTheSameAggregateQuerySnapshotInstance() {
    AggregateQuerySnapshot snapshot = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    assertTrue(snapshot.equals(snapshot));
  }

  @Test
  public void equalsShouldReturnTrueWhenGivenAnAggregateQuerySnapshotConstructedWithTheSameArguments() {
    AggregateQuerySnapshot snapshot1 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    AggregateQuerySnapshot snapshot2 = new AggregateQuerySnapshot(sampleAggregateQuery, sampleTimestamp, 42);
    assertTrue(snapshot1.equals(snapshot2));
  }

}
