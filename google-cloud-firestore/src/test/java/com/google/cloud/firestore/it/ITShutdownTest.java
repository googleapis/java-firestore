/*
 * Copyright 2021 Google LLC
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

import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class ITShutdownTest {
  @Rule public final Timeout timeout = new Timeout(5, TimeUnit.SECONDS);

  @Test
  public void closeSuccess_withListenerRemove() throws Exception {
    final CountDownLatch cdl = new CountDownLatch(1);

    Firestore fs = FirestoreOptions.getDefaultInstance().getService();
    ListenerRegistration listener =
        fs.collection("abcd")
            .addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                  @Override
                  public void onEvent(
                      @Nullable QuerySnapshot value, @Nullable FirestoreException error) {
                    cdl.countDown();
                  }
                });

    cdl.await();
    listener.remove();
    fs.close();
  }

  @Test
  public void closeSuccess_withoutListenerRemove() throws Exception {
    final CountDownLatch cdl = new CountDownLatch(1);

    Firestore fs = FirestoreOptions.getDefaultInstance().getService();
    ListenerRegistration listener =
        fs.collection("abcd")
            .addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                  @Override
                  public void onEvent(
                      @Nullable QuerySnapshot value, @Nullable FirestoreException error) {
                    cdl.countDown();
                  }
                });

    cdl.await();
    fs.close();
  }

  @Test
  public void shutdownNowSuccess_withoutListenerRemove() throws Exception {
    final CountDownLatch cdl = new CountDownLatch(1);

    Firestore fs = FirestoreOptions.getDefaultInstance().getService();
    ListenerRegistration listener =
        fs.collection("abcd")
            .addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                  @Override
                  public void onEvent(
                      @Nullable QuerySnapshot value, @Nullable FirestoreException error) {
                    cdl.countDown();
                  }
                });

    cdl.await();
    fs.shutdownNow();
  }

  @Test
  public void shutdownSuccess_withoutListenerRemove() throws Exception {
    final CountDownLatch cdl = new CountDownLatch(1);

    Firestore fs = FirestoreOptions.getDefaultInstance().getService();
    ListenerRegistration listener =
        fs.collection("abcd")
            .addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                  @Override
                  public void onEvent(
                      @Nullable QuerySnapshot value, @Nullable FirestoreException error) {
                    cdl.countDown();
                  }
                });

    cdl.await();
    fs.shutdown();
  }

  @Test
  public void closeAndShutdown() throws Exception {
    final CountDownLatch cdl = new CountDownLatch(1);

    Firestore fs = FirestoreOptions.getDefaultInstance().getService();
    ListenerRegistration listener =
        fs.collection("abcd")
            .addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                  @Override
                  public void onEvent(
                      @Nullable QuerySnapshot value, @Nullable FirestoreException error) {
                    cdl.countDown();
                  }
                });

    cdl.await();
    fs.close();
    fs.shutdown();
    fs.shutdownNow();
  }
}
