/*
 * Copyright 2020 Google LLC
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

import static com.google.cloud.firestore.LocalFirestoreHelper.map;
import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static java.util.Collections.emptySet;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.LocalFirestoreHelper;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.it.ITQueryWatchTest.QuerySnapshotEventListener.ListenerAssertions;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Range;
import com.google.common.truth.Truth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public final class ITQueryWatchTest {

  private static Firestore firestore;

  @Rule public TestName testName = new TestName();

  private CollectionReference randomColl;

  @BeforeClass
  public static void beforeClass() {
    FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().build();
    firestore = firestoreOptions.getService();
  }

  @Before
  public void before() {
    String autoId = LocalFirestoreHelper.autoId();
    String collPath = String.format("java-%s-%s", testName.getMethodName(), autoId);
    randomColl = firestore.collection(collPath);
  }

  @AfterClass
  public static void afterClass() throws Exception {
    firestore.close();
  }

  /*
  Attach a listener to a query with empty results.
  Verify the listener receives an empty event.
   */
  @Test
  public void emptyResults() throws InterruptedException {
    final Query query = randomColl.whereEqualTo("foo", "bar");
    // register the snapshot listener for the query
    QuerySnapshotEventListener listener = new QuerySnapshotEventListener(1);
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCDL.await();
    } finally {
      registration.remove();
    }

    ListenerAssertions la = listener.assertions();
    la.noError();
    la.eventCountIsAnyOf(Range.closed(1, 1));
    la.addedIdsIsAnyOf(emptySet());
    la.modifiedIdsIsAnyOf(emptySet());
    la.removedIdsIsAnyOf(emptySet());
  }

  /*
  Attach a listener to a query with non-empty results.
  Verify the listener receives an event including the expected document.
   */
  @Test
  public void nonEmptyResults() throws InterruptedException, TimeoutException, ExecutionException {
    // create a document in our collection that will match the query
    randomColl.document("doc").set(map("foo", "bar")).get(5, TimeUnit.SECONDS);

    final Query query = randomColl.whereEqualTo("foo", "bar");
    QuerySnapshotEventListener listener = new QuerySnapshotEventListener(1);
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCDL.await();
    } finally {
      registration.remove();
    }

    ListenerAssertions la = listener.assertions();
    la.noError();
    la.eventCountIsAnyOf(Range.closed(1, 1));
    la.addedIdsIsAnyOf(newHashSet("doc"));
    la.modifiedIdsIsAnyOf(emptySet());
    la.removedIdsIsAnyOf(emptySet());
  }

  /*
   Attach a listener to a query with empty results.
   Create a new document that matches the query.
   Verify newly created document results in an ADDED event.
   */
  @Test
  public void emptyResults_newDocument_ADDED()
      throws InterruptedException, TimeoutException, ExecutionException {

    final Query query = randomColl.whereEqualTo("foo", "bar");
    QuerySnapshotEventListener listener = new QuerySnapshotEventListener(1, 1, 0, 0);
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCDL.await();
      randomColl.document("doc").set(map("foo", "bar")).get(5, TimeUnit.SECONDS);
      listener.eventsCDL.await(DocumentChange.Type.ADDED);
    } finally {
      registration.remove();
    }

    ListenerAssertions la = listener.assertions();
    la.noError();
    la.eventCountIsAnyOf(Range.closed(2, 2));
    la.addedIdsIsAnyOf(emptySet(), newHashSet("doc"));
    la.modifiedIdsIsAnyOf(emptySet());
    la.removedIdsIsAnyOf(emptySet());
  }

  /*
   Attach a listener to a query with empty results.
   Modify an existing document so that it matches the query.
   Verify newly created document results in an ADDED event.
   */
  @Test
  public void emptyResults_modifiedDocument_ADDED()
      throws InterruptedException, TimeoutException, ExecutionException {
    // create our "existing non-matching document"
    randomColl.document("doc").set(map("baz", "baz")).get(5, TimeUnit.SECONDS);

    final Query query = randomColl.whereEqualTo("foo", "bar");
    QuerySnapshotEventListener listener = new QuerySnapshotEventListener(1, 1, 0, 0);
    List<ListenerEvent> receivedEvents = listener.receivedEvents;
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCDL.await();
      randomColl.document("doc").update("foo", "bar").get(5, TimeUnit.SECONDS);
      listener.eventsCDL.await(DocumentChange.Type.ADDED);
    } finally {
      registration.remove();
    }

    ListenerAssertions la = listener.assertions();
    la.noError();
    la.eventCountIsAnyOf(Range.closed(2, 2));
    la.addedIdsIsAnyOf(emptySet(), newHashSet("doc"));
    la.modifiedIdsIsAnyOf(emptySet());
    la.removedIdsIsAnyOf(emptySet());

    ListenerEvent event = receivedEvents.get(receivedEvents.size() - 1);
    //noinspection ConstantConditions guarded by "assertNoError" above
    QueryDocumentSnapshot doc = event.value.getDocumentChanges().get(0).getDocument();
    assertThat(doc.get("foo")).isEqualTo("bar");
    assertThat(doc.get("baz")).isEqualTo("baz");
  }

  /*
   Attach a listener to a query with non-empty results.
   Modify an existing document that is part of the results.
   Verify modified document results in a MODIFIED event.
   */
  @Test
  public void nonEmptyResults_modifiedDocument_MODIFIED()
      throws InterruptedException, TimeoutException, ExecutionException {
    DocumentReference testDoc = randomColl.document("doc");
    // create our "existing non-matching document"
    testDoc.set(map("foo", "bar")).get(5, TimeUnit.SECONDS);

    final Query query = randomColl.whereEqualTo("foo", "bar");
    // register the snapshot listener for the query
    QuerySnapshotEventListener listener = new QuerySnapshotEventListener(1, 0, 1, 0);
    List<ListenerEvent> receivedEvents = listener.receivedEvents;
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCDL.await();
      testDoc.update("baz", "baz").get(5, TimeUnit.SECONDS);
      listener.eventsCDL.await(DocumentChange.Type.MODIFIED);
    } finally {
      registration.remove();
    }

    ListenerAssertions la = listener.assertions();
    la.noError();
    la.eventCountIsAnyOf(Range.closed(2, 2));
    la.addedIdsIsAnyOf(emptySet(), newHashSet("doc"));
    la.modifiedIdsIsAnyOf(emptySet(), newHashSet("doc"));
    la.removedIdsIsAnyOf(emptySet());

    ListenerEvent event = receivedEvents.get(receivedEvents.size() - 1);
    //noinspection ConstantConditions guarded by "assertNoError" above
    QueryDocumentSnapshot doc = event.value.getDocumentChanges().get(0).getDocument();
    assertThat(doc.get("foo")).isEqualTo("bar");
    assertThat(doc.get("baz")).isEqualTo("baz");
  }

  /*
   Attach a listener to a query with non-empty results.
   Delete an existing document that is part of the results.
   Verify deleted document results in a REMOVED event.
   */
  @Test
  public void nonEmptyResults_deletedDocument_REMOVED()
      throws InterruptedException, TimeoutException, ExecutionException {
    DocumentReference testDoc = randomColl.document("doc");
    // create our "existing non-matching document"
    testDoc.set(map("foo", "bar")).get(5, TimeUnit.SECONDS);

    final Query query = randomColl.whereEqualTo("foo", "bar");
    // register the snapshot listener for the query
    QuerySnapshotEventListener listener = new QuerySnapshotEventListener(1, 0, 0, 1);
    List<ListenerEvent> receivedEvents = listener.receivedEvents;
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCDL.await();
      testDoc.delete().get(5, TimeUnit.SECONDS);
      listener.eventsCDL.await(DocumentChange.Type.REMOVED);
    } finally {
      registration.remove();
    }

    ListenerAssertions la = listener.assertions();
    la.noError();
    la.eventCountIsAnyOf(Range.closed(2, 2));
    la.addedIdsIsAnyOf(emptySet(), newHashSet("doc"));
    la.modifiedIdsIsAnyOf(emptySet());
    la.removedIdsIsAnyOf(emptySet(), newHashSet("doc"));

    ListenerEvent event = receivedEvents.get(receivedEvents.size() - 1);
    //noinspection ConstantConditions guarded by "assertNoError" above
    QueryDocumentSnapshot doc = event.value.getDocumentChanges().get(0).getDocument();
    assertThat(doc.get("foo")).isEqualTo("bar");
  }

  /*
   Attach a listener to a query with non-empty results.
   Modify an existing document that is part of the results to no longer match the query.
   Verify modified document results in a REMOVED event.
   */
  @Test
  public void nonEmptyResults_modifiedDocument_REMOVED()
      throws InterruptedException, TimeoutException, ExecutionException {
    DocumentReference testDoc = randomColl.document("doc");
    // create our "existing non-matching document"
    testDoc.set(map("foo", "bar")).get(5, TimeUnit.SECONDS);

    final Query query = randomColl.whereEqualTo("foo", "bar");
    // register the snapshot listener for the query
    QuerySnapshotEventListener listener = new QuerySnapshotEventListener(1, 0, 0, 1);
    List<ListenerEvent> receivedEvents = listener.receivedEvents;
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCDL.await();
      testDoc.set(map("bar", "foo")).get(5, TimeUnit.SECONDS);
      listener.eventsCDL.await(DocumentChange.Type.REMOVED);
    } finally {
      registration.remove();
    }

    ListenerAssertions la = listener.assertions();
    la.noError();
    la.eventCountIsAnyOf(Range.closed(2, 2));
    la.addedIdsIsAnyOf(emptySet(), newHashSet("doc"));
    la.modifiedIdsIsAnyOf(emptySet());
    la.removedIdsIsAnyOf(emptySet(), newHashSet("doc"));

    ListenerEvent event = receivedEvents.get(receivedEvents.size() - 1);
    //noinspection ConstantConditions guarded by "assertNoError" above
    QueryDocumentSnapshot doc = event.value.getDocumentChanges().get(0).getDocument();
    assertThat(doc.get("foo")).isEqualTo("bar");
  }

  /**
   * A tuple class used by {@code #queryWatch}. This class represents an event delivered to the
   * registered query listener.
   */
  private static final class ListenerEvent {

    @Nullable private final QuerySnapshot value;
    @Nullable private final FirestoreException error;

    ListenerEvent(@Nullable QuerySnapshot value, @Nullable FirestoreException error) {
      this.value = value;
      this.error = error;
    }
  }

  private static final class EventsCDL {
    private final CountDownLatch cdl;
    private final EnumMap<DocumentChange.Type, CountDownLatch> cdls;

    EventsCDL(int nonChangeInitial, int addedInitial, int modifiedInitial, int removedInitial) {
      cdl = new CountDownLatch(nonChangeInitial);
      cdls = new EnumMap<>(DocumentChange.Type.class);
      cdls.put(DocumentChange.Type.ADDED, new CountDownLatch(addedInitial));
      cdls.put(DocumentChange.Type.MODIFIED, new CountDownLatch(modifiedInitial));
      cdls.put(DocumentChange.Type.REMOVED, new CountDownLatch(removedInitial));
    }

    void countDown() {
      cdl.countDown();
    }

    void countDown(DocumentChange.Type type) {
      cdls.get(type).countDown();
    }

    void await() throws InterruptedException {
      cdl.await(5, TimeUnit.SECONDS);
    }

    void await(DocumentChange.Type type) throws InterruptedException {
      cdls.get(type).await(5, TimeUnit.SECONDS);
    }
  }

  static class QuerySnapshotEventListener implements EventListener<QuerySnapshot> {
    final List<ListenerEvent> receivedEvents;
    final EventsCDL eventsCDL;

    QuerySnapshotEventListener(int nonChangeInitial) {
      this(nonChangeInitial, 0, 0, 0);
    }

    QuerySnapshotEventListener(
        int nonChangeInitial, int addedInitial, int modifiedInitial, int removedInitial) {
      this.receivedEvents = Collections.synchronizedList(new ArrayList<ListenerEvent>());
      this.eventsCDL =
          new EventsCDL(nonChangeInitial, addedInitial, modifiedInitial, removedInitial);
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirestoreException error) {
      receivedEvents.add(new ListenerEvent(value, error));
      if (value != null) {
        List<DocumentChange> documentChanges = value.getDocumentChanges();
        for (DocumentChange dc : documentChanges) {
          eventsCDL.countDown(dc.getType());
        }
      }
      eventsCDL.countDown();
    }

    ListenerAssertions assertions() {
      return new ListenerAssertions(receivedEvents);
    }

    static final class ListenerAssertions {
      private static final MapJoiner MAP_JOINER = Joiner.on(",").withKeyValueSeparator("=");
      final List<ListenerEvent> receivedEvents;
      private final FluentIterable<ListenerEvent> events;
      private final Set<String> addedIds;
      private final Set<String> modifiedIds;
      private final Set<String> removedIds;

      ListenerAssertions(List<ListenerEvent> receivedEvents) {
        this.receivedEvents = receivedEvents;
        events = FluentIterable.from(receivedEvents);
        List<QuerySnapshot> querySnapshots = getQuerySnapshots(events);
        addedIds = getIds(querySnapshots, DocumentChange.Type.ADDED);
        modifiedIds = getIds(querySnapshots, DocumentChange.Type.MODIFIED);
        removedIds = getIds(querySnapshots, DocumentChange.Type.REMOVED);
      }

      private void noError() {
        final Optional<ListenerEvent> anyError =
            events.firstMatch(
                new Predicate<ListenerEvent>() {
                  @Override
                  public boolean apply(ListenerEvent input) {
                    return input.error != null;
                  }
                });
        assertWithMessage("snapshotListener received an error").that(anyError).isAbsent();
      }

      private static List<QuerySnapshot> getQuerySnapshots(FluentIterable<ListenerEvent> events) {
        return events
            .filter(
                new Predicate<ListenerEvent>() {
                  @Override
                  public boolean apply(ListenerEvent input) {
                    return input.value != null;
                  }
                })
            .transform(
                new com.google.common.base.Function<ListenerEvent, QuerySnapshot>() {
                  @Override
                  public QuerySnapshot apply(ListenerEvent input) {
                    return input.value;
                  }
                })
            .toList();
      }

      private static Set<String> getIds(
          List<QuerySnapshot> querySnapshots, DocumentChange.Type type) {
        final Set<String> documentIds = new HashSet<>();
        for (QuerySnapshot querySnapshot : querySnapshots) {
          final List<DocumentChange> changes = querySnapshot.getDocumentChanges();
          for (DocumentChange change : changes) {
            if (change.getType() == type) {
              documentIds.add(change.getDocument().getId());
            }
          }
        }
        return documentIds;
      }

      void addedIdsIsAnyOf(Set<?> s) {
        Truth.assertWithMessage(debugMessage()).that(addedIds).isEqualTo(s);
      }
      void addedIdsIsAnyOf(Set<?> s1, Set<?> s2) {
        Truth.assertWithMessage(debugMessage()).that(addedIds).isAnyOf(s1, s2);
      }

      void modifiedIdsIsAnyOf(Set<?> s) {
        Truth.assertWithMessage(debugMessage()).that(modifiedIds).isEqualTo(s);
      }

      void modifiedIdsIsAnyOf(Set<?> s1, Set<?> s2) {
        Truth.assertWithMessage(debugMessage()).that(modifiedIds).isAnyOf(s1, s2);
      }

      void removedIdsIsAnyOf(Set<?> s) {
        Truth.assertWithMessage(debugMessage()).that(removedIds).isEqualTo(s);
      }

      void removedIdsIsAnyOf(Set<?> s1, Set<?> s2) {
        Truth.assertWithMessage(debugMessage()).that(removedIds).isAnyOf(s1, s2);
      }

      void eventCountIsAnyOf(Range<Integer> range) {
        Truth.assertWithMessage(debugMessage()).that(receivedEvents.size()).isIn(range);
      }

      private String debugMessage() {
        final StringBuilder builder = new StringBuilder();
        builder.append("events[\n");
        for (ListenerEvent receivedEvent : receivedEvents) {
          builder.append("event{");
          builder.append("error=").append(receivedEvent.error);
          builder.append(",");
          builder.append("value=");
          debugMessage(builder, receivedEvent.value);
          builder.append("},\n");
        }
        builder.append("]");
        return builder.toString();
      }

      private static void debugMessage(StringBuilder builder, QuerySnapshot qs) {
        if (qs == null) {
          builder.append("null");
        } else {
          builder.append("{");
          List<QueryDocumentSnapshot> documents = qs.getDocuments();
          builder.append("documents[");
          for (QueryDocumentSnapshot document : documents) {
            debugMessage(builder, document);
          }
          builder.append("],");
          List<DocumentChange> changes = qs.getDocumentChanges();
          builder.append("documentChanges[");
          for (DocumentChange change : changes) {
            debugMessage(builder, change.getDocument());
          }
          builder.append("]");
          builder.append("}");
        }
      }

      private static void debugMessage(StringBuilder builder, QueryDocumentSnapshot queryDocumentSnapshot) {
        if (queryDocumentSnapshot == null) {
          builder.append("null");
        } else {
          builder.append("{");
          builder.append("path=").append(queryDocumentSnapshot.getReference().getPath());
          builder.append(",");
          builder.append("data=");
          debugMessage(builder, queryDocumentSnapshot.getData());
          builder.append("}");
        }
      }

      private static void debugMessage(StringBuilder builder, Map<String, Object> data) {
        builder.append("{");
        MAP_JOINER.appendTo(builder, data);
        builder.append("}");
      }
    }
  }
}
