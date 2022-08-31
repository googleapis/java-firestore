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

package com.google.cloud.firestore.it;

import static com.google.common.truth.Truth.assertThat;

import com.google.cloud.firestore.AggregateQuery;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.common.base.Preconditions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ITAggregateQueryTest {

  private Firestore firestore;

  @Before
  public void setUpFirestore() {
    // TODO(dconeybe) Remove the hardcoded "host" and "projectId" once COUNT is supported by prod.
    firestore =
        FirestoreOptions.newBuilder()
            .setHost("localhost:8080")
            .setProjectId("my-cool-project")
            .build()
            .getService();
    Preconditions.checkNotNull(
        firestore,
        "Error instantiating Firestore. Check that the service account credentials were properly set.");
  }

  @After
  public void tearDownFirestore() throws Exception {
    if (firestore != null) {
      firestore.close();
      firestore = null;
    }
  }

  @Test
  public void toProtoFromProtoRoundTripShouldProduceEqualAggregateQueryObjects() {
    Query query1 = firestore.collection("abc");
    Query query2 = firestore.collection("def").whereEqualTo("age", 42).limit(5000).orderBy("name");
    AggregateQuery countQuery1 = query1.count();
    AggregateQuery countQuery2 = query2.count();
    AggregateQuery countQuery1Recreated =
        AggregateQuery.fromProto(firestore, countQuery1.toProto());
    AggregateQuery countQuery2Recreated =
        AggregateQuery.fromProto(firestore, countQuery2.toProto());
    assertThat(countQuery1).isNotSameInstanceAs(countQuery1Recreated);
    assertThat(countQuery2).isNotSameInstanceAs(countQuery2Recreated);
    assertThat(countQuery1).isEqualTo(countQuery1Recreated);
    assertThat(countQuery2).isEqualTo(countQuery2Recreated);
    assertThat(countQuery1).isNotEqualTo(countQuery2);
  }
}
