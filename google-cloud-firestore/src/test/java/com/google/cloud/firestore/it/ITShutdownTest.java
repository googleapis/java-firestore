package com.google.cloud.firestore.it;

import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.QuerySnapshot;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import javax.annotation.Nullable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ITShutdownTest {
    @Rule
    public final Timeout timeout = new Timeout(5, TimeUnit.SECONDS);

    @Test
    public void closeSuccess_withListenerRemove() throws Exception {

        final CountDownLatch cdl = new CountDownLatch(1);

        Firestore fs = FirestoreOptions.getDefaultInstance().getService();
        ListenerRegistration listener = fs.collection("abcd").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirestoreException error) {
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
        ListenerRegistration listener = fs.collection("abcd").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirestoreException error) {
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
        ListenerRegistration listener = fs.collection("abcd").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirestoreException error) {
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
        ListenerRegistration listener = fs.collection("abcd").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirestoreException error) {
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
        ListenerRegistration listener = fs.collection("abcd").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirestoreException error) {
                cdl.countDown();
            }
        });


        cdl.await();
        fs.close();
        fs.shutdown();
        fs.shutdownNow();
    }
}
