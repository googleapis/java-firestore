/*
 * Copyright 2023 Google LLC
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

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.FirestoreSpy;
import com.google.common.base.Preconditions;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public abstract class ITBaseTest {
  private static final Logger logger = Logger.getLogger(ITBaseTest.class.getName());
  protected Firestore firestore;
  protected FirestoreSpy firestoreSpy;
  private FirestoreOptions firestoreOptions;

  @Before
  public void before() throws Exception {
    FirestoreOptions.Builder optionsBuilder = FirestoreOptions.newBuilder();

    String namedDb = System.getProperty("FIRESTORE_NAMED_DATABASE");
    if (namedDb != null) {
      logger.log(Level.INFO, "Integration test using named database " + namedDb);
      optionsBuilder = optionsBuilder.setDatabaseId(namedDb);
    } else {
      logger.log(Level.INFO, "Integration test using default database.");
    }

    firestoreOptions = optionsBuilder.build();
    firestore = firestoreOptions.getService();
  }

  @After
  public void after() throws Exception {
    Preconditions.checkNotNull(
        firestore,
        "Error instantiating Firestore. Check that the service account credentials were properly set.");
    firestore.close();
    firestore = null;
    firestoreOptions = null;
    firestoreSpy = null;
  }

  public FirestoreSpy useFirestoreSpy() {
    if (firestoreSpy == null) {
      firestoreSpy = new FirestoreSpy(firestoreOptions);
      firestore = firestoreSpy.spy;
    }
    return firestoreSpy;
  }
}
