package com.google.cloud.firestore;

import com.google.firestore.v1.Value;
import java.util.HashMap;
import java.util.Map;

public final class TestUtil {
  public static Map<String, Object> getAggregateSnapshotData(AggregateQuerySnapshot snapshot) {
    Map<String, Object> result = new HashMap<>();
    for (Map.Entry<String, Value> entry : snapshot.getData().entrySet()) {
      if (entry.getValue().hasIntegerValue()) {
        result.put(entry.getKey(), entry.getValue().getIntegerValue());
      } else if (entry.getValue().hasDoubleValue()) {
        result.put(entry.getKey(), entry.getValue().getDoubleValue());
      } else if (entry.getValue().hasNullValue()) {
        result.put(entry.getKey(), null);
      } else {
        throw new IllegalArgumentException("AggregateSnapshot has unrecognized value type");
      }
    }

    return result;
  }
}
