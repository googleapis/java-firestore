package com.google.cloud.firestore;

import com.google.cloud.firestore.spi.v1.FirestoreRpc;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

public class FirestoreOptionsTest {

    @Test
    public void instantiateWithProjectId() {
        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setProjectId("test-project")
                .build();
        FirestoreImpl firestore = new FirestoreImpl(firestoreOptions, mock(FirestoreRpc.class));

        DocumentReference document = firestore.document("coll/doc1");
        assertThat(document.getName()).isEqualTo("projects/test-project/databases/(default)/documents/coll/doc1");
    }

    @Test
    public void instantiateWithProjectIdAndDatabaseId() {
        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setProjectId("test-project")
                .setDatabaseId("test-database")
                .build();
        FirestoreImpl firestore = new FirestoreImpl(firestoreOptions, mock(FirestoreRpc.class));

        DocumentReference document = firestore.document("coll/doc1");
        assertThat(document.getName()).isEqualTo("projects/test-project/databases/test-database/documents/coll/doc1");
    }
}
