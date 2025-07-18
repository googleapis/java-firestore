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

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FirestoreDataConverter;

public class Post {
  public String title;
  public String author;

  public Post(String title, String author) {
    this.title = title;
    this.author = author;
  }

  @Override
  public String toString() {
    return title + ", by " + author;
  }

  public static class PostConverter implements FirestoreDataConverter<Post> {
    @Override
    public java.util.Map<String, Object> toFirestore(Post post) {
      java.util.Map<String, Object> data = new java.util.HashMap<>();
      if (post.title != null) {
        data.put("title", post.title);
      }
      if (post.author != null) {
        data.put("author", post.author);
      }
      return data;
    }

    @Override
    public Post fromFirestore(DocumentSnapshot snapshot) {
      java.util.Map<String, Object> data = snapshot.getData();
      return new Post((String) data.get("title"), (String) data.get("author"));
    }
  }

  public static class PostConverterMerge implements FirestoreDataConverter<Post> {
    @Override
    public java.util.Map<String, Object> toFirestore(Post post) {
      java.util.Map<String, Object> data = new java.util.HashMap<>();
      data.put("title", post.title);
      data.put("author", post.author);
      return data;
    }

    @Override
    public Post fromFirestore(DocumentSnapshot snapshot) {
      java.util.Map<String, Object> data = snapshot.getData();
      return new Post((String) data.get("title"), (String) data.get("author"));
    }
  }
}
