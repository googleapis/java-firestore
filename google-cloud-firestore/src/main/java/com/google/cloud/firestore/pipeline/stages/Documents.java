package com.google.cloud.firestore.pipeline.stages;

import com.google.api.core.InternalApi;
import com.google.cloud.firestore.DocumentReference;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@InternalApi
public final class Documents implements Stage {

  private static final String name = "documents";
  private List<String> documents;

  @InternalApi
  Documents(List<String> documents) {
    this.documents = documents;
  }

  @InternalApi
  public static Documents of(DocumentReference... documents) {
    return new Documents(
        Arrays.stream(documents).map(doc -> "/" + doc.getPath()).collect(Collectors.toList()));
  }

  @InternalApi
  public List<String> getDocuments() {
    return documents;
  }

  @Override
  public String getName() {
    return name;
  }
}
