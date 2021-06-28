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
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

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
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Range;
import com.google.common.truth.Truth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;

import org.junit.After;
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

  @Before
  public void before() {
    FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().build();
    firestore = firestoreOptions.getService();
    String autoId = LocalFirestoreHelper.autoId();
    String collPath = String.format("java-%s-%s", testName.getMethodName(), autoId);
    randomColl = firestore.collection(collPath);
  }

  @After
  public void after() throws Exception {
    Preconditions.checkNotNull(
        firestore,
        "Error instantiating Firestore. Check that the service account credentials were properly set.");
    firestore.close();
  }

  /**
   *
   *
   * <ol>
   *   <li>Attach a listener to a query with empty results.
   *   <li>Verify the listener receives an empty event.
   * </ol>
   */
  @Test
  public void emptyResults() throws InterruptedException {
    final Query query = randomColl.whereEqualTo("foo", "bar");
    // register the snapshot listener for the query
    QuerySnapshotEventListener listener =
        QuerySnapshotEventListener.builder().setInitialEventCount(1).build();
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCountDownLatch.awaitInitialEvents();
    } finally {
      registration.remove();
    }

    ListenerAssertions listenerAssertions = listener.assertions();
    listenerAssertions.noError();
    listenerAssertions.eventCountIsAnyOf(Range.closed(1, 1));
    listenerAssertions.addedIdsIsAnyOf(emptyList());
    listenerAssertions.modifiedIdsIsAnyOf(emptyList());
    listenerAssertions.removedIdsIsAnyOf(emptyList());
  }

  /**
   *
   *
   * <ol>
   *   <li>Attach a listener to a query with non-empty results.
   *   <li>Verify the listener receives an event including the expected document.
   * </ol>
   */
  @Test
  public void nonEmptyResults() throws Exception {
    // create a document in our collection that will match the query
    setDocument("doc", map("foo", "bar"));

    final Query query = randomColl.whereEqualTo("foo", "bar");
    QuerySnapshotEventListener listener =
        QuerySnapshotEventListener.builder().setInitialEventCount(1).build();
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCountDownLatch.awaitInitialEvents();
    } finally {
      registration.remove();
    }

    ListenerAssertions listenerAssertions = listener.assertions();
    listenerAssertions.noError();
    listenerAssertions.eventCountIsAnyOf(Range.closed(1, 1));
    listenerAssertions.addedIdsIsAnyOf(singletonList("doc"));
    listenerAssertions.modifiedIdsIsAnyOf(emptyList());
    listenerAssertions.removedIdsIsAnyOf(emptyList());
  }

  /**
   *
   *
   * <ol>
   *   <li>Attach a listener to a query with empty results.
   *   <li>Create a new document that matches the query.
   *   <li>Verify newly created document results in an ADDED event.
   * </ol>
   */
  @Test
  public void emptyResults_newDocument_ADDED()
      throws InterruptedException, TimeoutException, ExecutionException {

    final Query query = randomColl.whereEqualTo("foo", "bar");
    QuerySnapshotEventListener listener =
        QuerySnapshotEventListener.builder().setInitialEventCount(1).setAddedEventCount(1).build();
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCountDownLatch.awaitInitialEvents();
      randomColl.document("doc").set(map("foo", "bar")).get(5, TimeUnit.SECONDS);
      listener.eventsCountDownLatch.await(DocumentChange.Type.ADDED);
    } finally {
      registration.remove();
    }

    ListenerAssertions listenerAssertions = listener.assertions();
    listenerAssertions.noError();
    listenerAssertions.eventCountIsAnyOf(Range.closed(2, 2));
    listenerAssertions.addedIdsIsAnyOf(emptyList(), singletonList("doc"));
    listenerAssertions.modifiedIdsIsAnyOf(emptyList());
    listenerAssertions.removedIdsIsAnyOf(emptyList());
  }

  /**
   *
   *
   * <ol>
   *   <li>Attach a listener to a query with empty results.
   *   <li>Modify an existing document so that it matches the query.
   *   <li>Verify newly created document results in an ADDED event.
   * </ol>
   */
  @Test
  public void emptyResults_modifiedDocument_ADDED() throws Exception {
    // create our "existing non-matching document"
    DocumentReference testDoc = setDocument("doc", map("baz", "baz"));

    final Query query = randomColl.whereEqualTo("foo", "bar");
    QuerySnapshotEventListener listener =
        QuerySnapshotEventListener.builder().setInitialEventCount(1).setAddedEventCount(1).build();
    List<ListenerEvent> receivedEvents = listener.receivedEvents;
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCountDownLatch.awaitInitialEvents();
      testDoc.update("foo", "bar").get(5, TimeUnit.SECONDS);
      listener.eventsCountDownLatch.await(DocumentChange.Type.ADDED);
    } finally {
      registration.remove();
    }

    ListenerAssertions listenerAssertions = listener.assertions();
    listenerAssertions.noError();
    listenerAssertions.eventCountIsAnyOf(Range.closed(2, 2));
    listenerAssertions.addedIdsIsAnyOf(emptyList(), singletonList("doc"));
    listenerAssertions.modifiedIdsIsAnyOf(emptyList());
    listenerAssertions.removedIdsIsAnyOf(emptyList());

    ListenerEvent event = receivedEvents.get(receivedEvents.size() - 1);
    //noinspection ConstantConditions guarded by "assertNoError" above
    QueryDocumentSnapshot doc = event.value.getDocumentChanges().get(0).getDocument();
    assertThat(doc.get("foo")).isEqualTo("bar");
    assertThat(doc.get("baz")).isEqualTo("baz");
  }

  /**
   *
   *
   * <ol>
   *   <li>Attach a listener to a query with non-empty results.
   *   <li>Modify an existing document that is part of the results.
   *   <li>Verify modified document results in a MODIFIED event.
   * </ol>
   */
  @Test
  public void nonEmptyResults_modifiedDocument_MODIFIED() throws Exception {
    DocumentReference testDoc = setDocument("doc", map("foo", "bar"));

    final Query query = randomColl.whereEqualTo("foo", "bar");
    // register the snapshot listener for the query
    QuerySnapshotEventListener listener =
        QuerySnapshotEventListener.builder()
            .setInitialEventCount(1)
            .setModifiedEventCount(1)
            .build();
    List<ListenerEvent> receivedEvents = listener.receivedEvents;
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCountDownLatch.awaitInitialEvents();
      testDoc.update("baz", "baz").get(5, TimeUnit.SECONDS);
      listener.eventsCountDownLatch.await(DocumentChange.Type.MODIFIED);
    } finally {
      registration.remove();
    }

    ListenerAssertions listenerAssertions = listener.assertions();
    listenerAssertions.noError();
    listenerAssertions.eventCountIsAnyOf(Range.closed(2, 2));
    listenerAssertions.addedIdsIsAnyOf(emptyList(), singletonList("doc"));
    listenerAssertions.modifiedIdsIsAnyOf(emptyList(), singletonList("doc"));
    listenerAssertions.removedIdsIsAnyOf(emptyList());

    ListenerEvent event = receivedEvents.get(receivedEvents.size() - 1);
    //noinspection ConstantConditions guarded by "assertNoError" above
    QueryDocumentSnapshot doc = event.value.getDocumentChanges().get(0).getDocument();
    assertThat(doc.get("foo")).isEqualTo("bar");
    assertThat(doc.get("baz")).isEqualTo("baz");
  }

  /**
   *
   *
   * <ol>
   *   <li>Attach a listener to a query with non-empty results.
   *   <li>Delete an existing document that is part of the results.
   *   <li>Verify deleted document results in a REMOVED event.
   * </ol>
   */
  @Test
  public void nonEmptyResults_deletedDocument_REMOVED() throws Exception {
    DocumentReference testDoc = setDocument("doc", map("foo", "bar"));

    final Query query = randomColl.whereEqualTo("foo", "bar");
    // register the snapshot listener for the query
    QuerySnapshotEventListener listener =
        QuerySnapshotEventListener.builder()
            .setInitialEventCount(1)
            .setRemovedEventCount(1)
            .build();
    List<ListenerEvent> receivedEvents = listener.receivedEvents;
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCountDownLatch.awaitInitialEvents();
      testDoc.delete().get(5, TimeUnit.SECONDS);
      listener.eventsCountDownLatch.await(DocumentChange.Type.REMOVED);
    } finally {
      registration.remove();
    }

    ListenerAssertions listenerAssertions = listener.assertions();
    listenerAssertions.noError();
    listenerAssertions.eventCountIsAnyOf(Range.closed(2, 2));
    listenerAssertions.addedIdsIsAnyOf(emptyList(), singletonList("doc"));
    listenerAssertions.modifiedIdsIsAnyOf(emptyList());
    listenerAssertions.removedIdsIsAnyOf(emptyList(), singletonList("doc"));

    ListenerEvent event = receivedEvents.get(receivedEvents.size() - 1);
    //noinspection ConstantConditions guarded by "assertNoError" above
    QueryDocumentSnapshot doc = event.value.getDocumentChanges().get(0).getDocument();
    assertThat(doc.get("foo")).isEqualTo("bar");
  }

  /**
   *
   *
   * <ol>
   *   <li>Attach a listener to a query with non-empty results.
   *   <li>Modify an existing document that is part of the results to no longer match the query.
   *   <li>Verify modified document results in a REMOVED event.
   * </ol>
   */
  @Test
  public void nonEmptyResults_modifiedDocument_REMOVED() throws Exception {
    DocumentReference testDoc = setDocument("doc", map("foo", "bar"));

    final Query query = randomColl.whereEqualTo("foo", "bar");
    // register the snapshot listener for the query
    QuerySnapshotEventListener listener =
        QuerySnapshotEventListener.builder()
            .setInitialEventCount(1)
            .setRemovedEventCount(1)
            .build();
    List<ListenerEvent> receivedEvents = listener.receivedEvents;
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCountDownLatch.awaitInitialEvents();
      testDoc.set(map("bar", "foo")).get(5, TimeUnit.SECONDS);
      listener.eventsCountDownLatch.await(DocumentChange.Type.REMOVED);
    } finally {
      registration.remove();
    }

    ListenerAssertions listenerAssertions = listener.assertions();
    listenerAssertions.noError();
    listenerAssertions.eventCountIsAnyOf(Range.closed(2, 2));
    listenerAssertions.addedIdsIsAnyOf(emptyList(), singletonList("doc"));
    listenerAssertions.modifiedIdsIsAnyOf(emptyList());
    listenerAssertions.removedIdsIsAnyOf(emptyList(), singletonList("doc"));

    ListenerEvent event = receivedEvents.get(receivedEvents.size() - 1);
    //noinspection ConstantConditions guarded by "assertNoError" above
    QueryDocumentSnapshot doc = event.value.getDocumentChanges().get(0).getDocument();
    assertThat(doc.get("foo")).isEqualTo("bar");
  }

  /** Verifies that QuerySnapshot for limitToLast() queries are ordered correctly. */
  @Test
  public void limitToLast() throws Exception {
    setDocument("doc1", Collections.singletonMap("counter", 1));
    setDocument("doc2", Collections.singletonMap("counter", 2));
    setDocument("doc3", Collections.singletonMap("counter", 3));

    final Query query = randomColl.orderBy("counter").limitToLast(2);
    QuerySnapshotEventListener listener =
        QuerySnapshotEventListener.builder().setInitialEventCount(1).build();
    ListenerRegistration registration = query.addSnapshotListener(listener);

    try {
      listener.eventsCountDownLatch.awaitInitialEvents();
    } finally {
      registration.remove();
    }

    ListenerAssertions listenerAssertions = listener.assertions();
    listenerAssertions.noError();
    listenerAssertions.addedIdsIsAnyOf(emptyList(), asList("doc2", "doc3"));
  }

  @Test
  public void shutdownNowTerminatesActiveListener() throws Exception {
    Query query = randomColl.whereEqualTo("foo", "bar");
    QuerySnapshotEventListener listener =
        QuerySnapshotEventListener.builder().setExpectError().build();

    query.addSnapshotListener(listener);
    firestore.shutdownNow();

    listener.eventsCountDownLatch.awaitError();

    ListenerAssertions listenerAssertions = listener.assertions();
    listenerAssertions.hasError();
  }

  @Test
  public void shutdownNowPreventsAddingNewListener() throws Exception {
    Query query = randomColl.whereEqualTo("foo", "bar");
    QuerySnapshotEventListener listener =
        QuerySnapshotEventListener.builder().setExpectError().build();

    firestore.shutdownNow();
    query.addSnapshotListener(listener);

    listener.eventsCountDownLatch.awaitError();

    ListenerAssertions listenerAssertions = listener.assertions();
    listenerAssertions.hasError();
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

  private static final class EventsCountDownLatch {
    private final CountDownLatch initialEventsCountDownLatch;
    private final int initialEventCount;
    private final CountDownLatch errorCountDownLatch;
    private final EnumMap<DocumentChange.Type, Integer> eventsCounts;
    private final EnumMap<DocumentChange.Type, CountDownLatch> eventsCountDownLatches;

    EventsCountDownLatch(
        int initialEventCount,
        int addedInitialCount,
        int modifiedInitialCount,
        int removedInitialCount,
        int errorCount) {
      initialEventsCountDownLatch = new CountDownLatch(initialEventCount);
      this.initialEventCount = initialEventCount;
      this.errorCountDownLatch = new CountDownLatch(errorCount);
      eventsCounts = new EnumMap<>(DocumentChange.Type.class);
      eventsCounts.put(DocumentChange.Type.ADDED, addedInitialCount);
      eventsCounts.put(DocumentChange.Type.MODIFIED, modifiedInitialCount);
      eventsCounts.put(DocumentChange.Type.REMOVED, removedInitialCount);
      eventsCountDownLatches = new EnumMap<>(DocumentChange.Type.class);
      eventsCountDownLatches.put(DocumentChange.Type.ADDED, new CountDownLatch(addedInitialCount));
      eventsCountDownLatches.put(
          DocumentChange.Type.MODIFIED, new CountDownLatch(modifiedInitialCount));
      eventsCountDownLatches.put(
          DocumentChange.Type.REMOVED, new CountDownLatch(removedInitialCount));
    }

    void countDown() {
      initialEventsCountDownLatch.countDown();
    }

    void countDown(DocumentChange.Type type) {
      eventsCountDownLatches.get(type).countDown();
    }

    void countError() {
      errorCountDownLatch.countDown();
    }

    void awaitInitialEvents() throws InterruptedException {
      initialEventsCountDownLatch.await(5 * initialEventCount, TimeUnit.SECONDS);
    }

    void awaitError() throws InterruptedException {
      errorCountDownLatch.await(5, TimeUnit.SECONDS);
    }

    void await(DocumentChange.Type type) throws InterruptedException {
      int count = eventsCounts.get(type);
      eventsCountDownLatches.get(type).await(5 * count, TimeUnit.SECONDS);
    }
  }

  static class QuerySnapshotEventListener implements EventListener<QuerySnapshot> {
    final List<ListenerEvent> receivedEvents;
    final EventsCountDownLatch eventsCountDownLatch;

    private QuerySnapshotEventListener(
        int initialCount,
        int addedEventCount,
        int modifiedEventCount,
        int removedEventCount,
        int errorCount) {
      this.receivedEvents = Collections.synchronizedList(new ArrayList<ListenerEvent>());
      this.eventsCountDownLatch =
          new EventsCountDownLatch(
              initialCount, addedEventCount, modifiedEventCount, removedEventCount, errorCount);
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirestoreException error) {
      receivedEvents.add(new ListenerEvent(value, error));
      if (value != null) {
        List<DocumentChange> documentChanges = value.getDocumentChanges();
        for (DocumentChange docChange : documentChanges) {
          eventsCountDownLatch.countDown(docChange.getType());
        }
      }
      if (error != null) {
        eventsCountDownLatch.countError();
      }
      eventsCountDownLatch.countDown();
    }

    ListenerAssertions assertions() {
      return new ListenerAssertions(receivedEvents);
    }

    static Builder builder() {
      return new Builder();
    }

    @SuppressWarnings("SameParameterValue")
    static final class Builder {
      private int initialEventCount = 0;
      private int addedEventCount = 0;
      private int modifiedEventCount = 0;
      private int removedEventCount = 0;
      private int errorCount = 0;

      private Builder() {}

      Builder setInitialEventCount(int initialEventCount) {
        this.initialEventCount = initialEventCount;
        return this;
      }

      Builder setAddedEventCount(int addedEventCount) {
        this.addedEventCount = addedEventCount;
        return this;
      }

      Builder setModifiedEventCount(int modifiedEventCount) {
        this.modifiedEventCount = modifiedEventCount;
        return this;
      }

      Builder setRemovedEventCount(int removedEventCount) {
        this.removedEventCount = removedEventCount;
        return this;
      }

      Builder setExpectError() {
        this.errorCount = 1;
        return this;
      }

      public QuerySnapshotEventListener build() {
        return new QuerySnapshotEventListener(
            initialEventCount, addedEventCount, modifiedEventCount, removedEventCount, errorCount);
      }
    }

    static final class ListenerAssertions {
      private static final MapJoiner MAP_JOINER = Joiner.on(",").withKeyValueSeparator("=");
      private final FluentIterable<ListenerEvent> events;
      private final List<String> addedIds;
      private final List<String> modifiedIds;
      private final List<String> removedIds;

      ListenerAssertions(List<ListenerEvent> receivedEvents) {
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

      private void hasError() {
        final Optional<ListenerEvent> anyError =
            events.firstMatch(
                new Predicate<ListenerEvent>() {
                  @Override
                  public boolean apply(ListenerEvent input) {
                    return input.error != null;
                  }
                });
        assertWithMessage("snapshotListener did not receive an expected error")
            .that(anyError)
            .isPresent();
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

      private static List<String> getIds(
          List<QuerySnapshot> querySnapshots, DocumentChange.Type type) {
        final List<String> documentIds = new ArrayList<>();
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

      void addedIdsIsAnyOf(List<?> s) {
        Truth.assertWithMessage(debugMessage()).that(addedIds).isEqualTo(s);
      }

      void addedIdsIsAnyOf(List<?> s1, List<?> s2) {
        Truth.assertWithMessage(debugMessage()).that(addedIds).isAnyOf(s1, s2);
      }

      void modifiedIdsIsAnyOf(List<?> s) {
        Truth.assertWithMessage(debugMessage()).that(modifiedIds).isEqualTo(s);
      }

      void modifiedIdsIsAnyOf(List<?> s1, List<?> s2) {
        Truth.assertWithMessage(debugMessage()).that(modifiedIds).isAnyOf(s1, s2);
      }

      void removedIdsIsAnyOf(List<?> s) {
        Truth.assertWithMessage(debugMessage()).that(removedIds).isEqualTo(s);
      }

      void removedIdsIsAnyOf(List<?> s1, List<?> s2) {
        Truth.assertWithMessage(debugMessage()).that(removedIds).isAnyOf(s1, s2);
      }

      void eventCountIsAnyOf(Range<Integer> range) {
        Truth.assertWithMessage(debugMessage()).that(events.size()).isIn(range);
      }

      private String debugMessage() {
        final StringBuilder builder = new StringBuilder();
        builder.append("events[\n");
        for (ListenerEvent receivedEvent : events) {
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

      private static void debugMessage(
          StringBuilder builder, QueryDocumentSnapshot queryDocumentSnapshot) {
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

  private DocumentReference setDocument(String documentId, Map<String, ?> fields) throws Exception {
    DocumentReference documentReference = randomColl.document(documentId);
    documentReference.set(fields).get();
    return documentReference;
  }
}
