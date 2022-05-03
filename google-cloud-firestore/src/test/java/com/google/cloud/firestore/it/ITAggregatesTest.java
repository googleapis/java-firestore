/*
 * Copyright 2017 Google LLC
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

package com.google.cloud.firestore.it;

import static com.google.cloud.firestore.AggregateField.count;
import static com.google.common.truth.Truth.assertThat;

import com.google.cloud.firestore.AggregateField;
import com.google.cloud.firestore.AggregateQuery;
import com.google.cloud.firestore.AggregateQuerySnapshot;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.common.base.Preconditions;
import com.google.common.truth.Truth;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ITAggregatesTest {

  @Rule public TestName testName = new TestName();

  private Firestore firestore;

  @Before
  public void before() {
    FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().build();
    firestore = firestoreOptions.getService();
  }

  @After
  public void after() throws Exception {
    Preconditions.checkNotNull(
        firestore,
        "Error instantiating Firestore. Check that the service account credentials were properly set.");
    firestore.close();
  }

  @Test
  public void test() throws Exception {
    CollectionReference collection = firestore.collection("colA").document().collection("colB");
    HashMap<String, Object> data = new HashMap<>();
    data.put("key1", "value1");
    collection.document().set(data).get();
    collection.document().set(data).get();

    AggregateQuery query = collection.aggregate(count());
    AggregateQuerySnapshot snapshot = query.get().get();
    assertThat(snapshot.get(count())).isEqualTo(2);
  }

}
