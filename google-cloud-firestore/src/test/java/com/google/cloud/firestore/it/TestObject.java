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

package com.google.cloud.firestore.it;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import java.util.List;
import java.util.Map;

class TestObject {
  public String outerString;
  public List<String> outerArr;
  public Nested nested;

  public TestObject(String outerString, List<String> outerArr, Nested nested) {
    this.outerString = outerString;
    this.outerArr = outerArr;
    this.nested = nested;
  }

  public static class Nested {
    public InnerNested innerNested;
    public FieldValue innerArr;
    public FieldValue timestamp;

    public Nested(InnerNested innerNested, FieldValue innerArr, FieldValue timestamp) {
      this.innerNested = innerNested;
      this.innerArr = innerArr;
      this.timestamp = timestamp;
    }
  }

  public static class InnerNested {
    public FieldValue innerNestedNum;

    public InnerNested(FieldValue innerNestedNum) {
      this.innerNestedNum = innerNestedNum;
    }
  }

  public static class TestObjectConverterMerge
      implements FirestoreDataConverter<TestObject> {
    @Override
    public Map<String, Object> toFirestore(TestObject testObject) {
      return null;
    }

    @Override
    public TestObject fromFirestore(DocumentSnapshot snapshot) {
      return null;
    }
  }
}
