/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.firestore;

import static com.google.common.truth.Truth.assertThat;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;

public class ITNativeImageFirestoreSample {

  private ByteArrayOutputStream bout;
  private PrintStream out;
  private Firestore database;

  @Before
  public void setUp() throws Exception {
    FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance();
    database = firestoreOptions.getService();

    NativeImageFirestoreSample.deleteCollection(database);
    NativeImageFirestoreSample.createUserDocument(database);

    bout = new ByteArrayOutputStream();
    out = new PrintStream(bout);
    System.setOut(out);
  }

  @Test
  public void testReadDocument() throws Exception {
    bout.reset();
    NativeImageFirestoreSample.readDocuments(database);
    String output = bout.toString();
    assertThat(output).contains("Document: alovelace | Ada Lovelace born 1815");
  }

  @Test
  public void testRunSampleQueries() throws Exception {
    bout.reset();
    NativeImageFirestoreSample.runSampleQueries(database);
    String output = bout.toString();
    assertThat(output).contains("Number of users born before 1900: 1");
    assertThat(output).contains("Number of users born earlier after 1900: 0");
    assertThat(output).contains("Number of users whose first name is 'Ada': 0");
  }
}
