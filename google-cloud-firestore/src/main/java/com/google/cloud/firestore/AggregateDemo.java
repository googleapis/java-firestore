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

import java.util.Objects;

public class AggregateDemo {

  public static void Demo1_CountOfDocumentsInACollection(Firestore db) throws Exception {
    Query query = db.collection("games").document("halo").collection("players");
    AggregateQuerySnapshot snapshot = query.count().get().get();
    assertEqual(snapshot.getCount(), 5_000_000);
  }

  public static void Demo2_LimitNumberOfDocumentsScannedWithLimit(Firestore db) throws Exception {
    // Limit the work / documents scanned by restricting underlying query.
    Query query = db.collection("games").document("halo").collection("players").limit(1000);
    AggregateQuerySnapshot snapshot = query.count().get().get();
    assertEqual(snapshot.getCount(), 1000);
  }

  private static void assertEqual(Long num1, Long num2) {
    if (!Objects.equals(num1, num2)) {
      throw new AssertionError("num1!=num2");
    }
  }

  private static void assertEqual(Long num1, int num2) {
    assertEqual(num1, Long.valueOf(num2));
  }
}
