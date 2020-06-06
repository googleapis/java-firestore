/*
 * Copyright 2019 Google LLC
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

package com.google.firestore.admin.v1;

import com.google.api.pathtemplate.PathTemplate;
import com.google.api.resourcenames.ResourceName;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** AUTO-GENERATED DOCUMENTATION AND CLASS */
@javax.annotation.Generated("by GAPIC protoc plugin")
public class CollectionGroupName implements ResourceName {

  private static final PathTemplate PATH_TEMPLATE =
      PathTemplate.createWithoutUrlEncoding(
          "projects/{project}/databases/{database}/collectionGroups/{collection}");

  private volatile Map<String, String> fieldValuesMap;

  private final String project;
  private final String database;
  private final String collection;

  public String getProject() {
    return project;
  }

  public String getDatabase() {
    return database;
  }

  public String getCollection() {
    return collection;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  private CollectionGroupName(Builder builder) {
    project = Preconditions.checkNotNull(builder.getProject());
    database = Preconditions.checkNotNull(builder.getDatabase());
    collection = Preconditions.checkNotNull(builder.getCollection());
  }

  public static CollectionGroupName of(String project, String database, String collection) {
    return newBuilder().setProject(project).setDatabase(database).setCollection(collection).build();
  }

  public static String format(String project, String database, String collection) {
    return newBuilder()
        .setProject(project)
        .setDatabase(database)
        .setCollection(collection)
        .build()
        .toString();
  }

  public static CollectionGroupName parse(String formattedString) {
    if (formattedString.isEmpty()) {
      return null;
    }
    Map<String, String> matchMap =
        PATH_TEMPLATE.validatedMatch(
            formattedString, "CollectionGroupName.parse: formattedString not in valid format");
    return of(matchMap.get("project"), matchMap.get("database"), matchMap.get("collection"));
  }

  public static List<CollectionGroupName> parseList(List<String> formattedStrings) {
    List<CollectionGroupName> list = new ArrayList<>(formattedStrings.size());
    for (String formattedString : formattedStrings) {
      list.add(parse(formattedString));
    }
    return list;
  }

  public static List<String> toStringList(List<CollectionGroupName> values) {
    List<String> list = new ArrayList<String>(values.size());
    for (CollectionGroupName value : values) {
      if (value == null) {
        list.add("");
      } else {
        list.add(value.toString());
      }
    }
    return list;
  }

  public static boolean isParsableFrom(String formattedString) {
    return PATH_TEMPLATE.matches(formattedString);
  }

  public Map<String, String> getFieldValuesMap() {
    if (fieldValuesMap == null) {
      synchronized (this) {
        if (fieldValuesMap == null) {
          ImmutableMap.Builder<String, String> fieldMapBuilder = ImmutableMap.builder();
          fieldMapBuilder.put("project", project);
          fieldMapBuilder.put("database", database);
          fieldMapBuilder.put("collection", collection);
          fieldValuesMap = fieldMapBuilder.build();
        }
      }
    }
    return fieldValuesMap;
  }

  public String getFieldValue(String fieldName) {
    return getFieldValuesMap().get(fieldName);
  }

  @Override
  public String toString() {
    return PATH_TEMPLATE.instantiate(
        "project", project, "database", database, "collection", collection);
  }

  /** Builder for CollectionGroupName. */
  public static class Builder {

    private String project;
    private String database;
    private String collection;

    public String getProject() {
      return project;
    }

    public String getDatabase() {
      return database;
    }

    public String getCollection() {
      return collection;
    }

    public Builder setProject(String project) {
      this.project = project;
      return this;
    }

    public Builder setDatabase(String database) {
      this.database = database;
      return this;
    }

    public Builder setCollection(String collection) {
      this.collection = collection;
      return this;
    }

    private Builder() {}

    private Builder(CollectionGroupName collectionGroupName) {
      project = collectionGroupName.project;
      database = collectionGroupName.database;
      collection = collectionGroupName.collection;
    }

    public CollectionGroupName build() {
      return new CollectionGroupName(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof CollectionGroupName) {
      CollectionGroupName that = (CollectionGroupName) o;
      return (this.project.equals(that.project))
          && (this.database.equals(that.database))
          && (this.collection.equals(that.collection));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= project.hashCode();
    h *= 1000003;
    h ^= database.hashCode();
    h *= 1000003;
    h ^= collection.hashCode();
    return h;
  }
}
