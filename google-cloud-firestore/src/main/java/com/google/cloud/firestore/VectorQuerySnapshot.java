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

package com.google.cloud.firestore;

import com.google.cloud.Timestamp;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * A QuerySnapshot contains the results of a query. It can contain zero or more DocumentSnapshot
 * objects.
 */
public abstract class VectorQuerySnapshot extends GenericQuerySnapshot<VectorQuery> {
  protected VectorQuerySnapshot(
      VectorQuery query,
      Timestamp readTime,
      final List<QueryDocumentSnapshot> documents,
      final DocumentSet documentSet,
      final List<DocumentChange> documentChanges) {
    super(query, readTime, documents, documentSet, documentChanges);
  }

  /** Creates a new QuerySnapshot representing the results of a Query with added documents. */
  public static VectorQuerySnapshot withDocuments(
      final VectorQuery query, Timestamp readTime, final List<QueryDocumentSnapshot> documents) {
    return new VectorQuerySnapshot(query, readTime, documents, null, null) {
      volatile ImmutableList<DocumentChange> documentChanges;

      @Nonnull
      @Override
      public List<QueryDocumentSnapshot> getDocuments() {
        return Collections.unmodifiableList(documents);
      }

      @Nonnull
      @Override
      public List<DocumentChange> getDocumentChanges() {
        if (documentChanges == null) {
          synchronized (documents) {
            if (documentChanges == null) {
              int size = documents.size();
              ImmutableList.Builder<DocumentChange> builder =
                  ImmutableList.builderWithExpectedSize(size);
              for (int i = 0; i < size; ++i) {
                builder.add(new DocumentChange(documents.get(i), DocumentChange.Type.ADDED, -1, i));
              }
              documentChanges = builder.build();
            }
          }
        }
        return documentChanges;
      }

      @Override
      public int size() {
        return documents.size();
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }
        QuerySnapshot that = (QuerySnapshot) o;
        return Objects.equals(query, that.query)
            && Objects.equals(this.size(), that.size())
            && Objects.equals(this.getDocuments(), that.getDocuments());
      }

      @Override
      public int hashCode() {
        return Objects.hash(query, this.getDocuments());
      }
    };
  }
}
