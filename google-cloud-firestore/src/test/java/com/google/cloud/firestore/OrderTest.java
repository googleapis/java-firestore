/*
 * Copyright 2017 Google LLC
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

import static org.junit.Assert.assertEquals;

import com.google.firestore.v1.ArrayValue;
import com.google.firestore.v1.MapValue;
import com.google.firestore.v1.Value;
import com.google.protobuf.ByteString;
import com.google.protobuf.NullValue;
import com.google.protobuf.Timestamp;
import com.google.type.LatLng;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class OrderTest {

  @Test
  public void verifyOrder() {
    List<Value[]> groups = new ArrayList<>();

    groups.add(new Value[] {nullValue()});

    groups.add(new Value[] {minKeyValue(), minKeyValue()});

    groups.add(new Value[] {booleanValue(false)});
    groups.add(new Value[] {booleanValue(true)});

    // numbers
    groups.add(new Value[] {doubleValue(Double.NaN), doubleValue(Double.NaN)});
    groups.add(new Value[] {doubleValue(Double.NEGATIVE_INFINITY)});
    groups.add(new Value[] {doubleValue((double) Long.MIN_VALUE - 100)});
    groups.add(new Value[] {intValue((long) Integer.MIN_VALUE - 1)});
    // 64-bit and 32-bit integers order together numerically, so the same
    // value (-2147483648) as int or long should order equally.
    groups.add(new Value[] {intValue(Integer.MIN_VALUE), int32Value(Integer.MIN_VALUE)});
    groups.add(new Value[] {doubleValue(-1.1)});
    // Integers and Doubles and int32 order together numerically.
    groups.add(new Value[] {intValue(-1), doubleValue(-1.0), int32Value(-1)});
    groups.add(new Value[] {doubleValue(-Double.MIN_VALUE)});
    // zeros all compare the same.
    groups.add(
        new Value[] {
          intValue(0), doubleValue(-0.0), doubleValue(0.0), doubleValue(+0.0), int32Value(0)
        });
    groups.add(new Value[] {doubleValue(Double.MIN_VALUE)});
    groups.add(new Value[] {intValue(1), doubleValue(1.0), int32Value(1)});
    groups.add(new Value[] {doubleValue(1.1)});
    groups.add(new Value[] {int32Value(11)});
    groups.add(new Value[] {int32Value(12)});
    groups.add(new Value[] {intValue(Integer.MAX_VALUE), int32Value(Integer.MAX_VALUE)});
    groups.add(new Value[] {intValue((long) Integer.MAX_VALUE + 1)});
    groups.add(new Value[] {doubleValue(((double) Long.MAX_VALUE) + 100)});
    groups.add(new Value[] {doubleValue(Double.POSITIVE_INFINITY)});

    groups.add(new Value[] {timestampValue(123, 0)});
    groups.add(new Value[] {timestampValue(123, 123)});
    groups.add(new Value[] {timestampValue(345, 0)});

    // BSON Timestamp
    groups.add(new Value[] {bsonTimestampValue(123, 4)});
    groups.add(new Value[] {bsonTimestampValue(123, 5)});
    groups.add(new Value[] {bsonTimestampValue(124, 0)});

    // strings
    groups.add(new Value[] {stringValue("")});
    groups.add(new Value[] {stringValue("\u0000\ud7ff\ue000\uffff")});
    groups.add(new Value[] {stringValue("(╯°□°）╯︵ ┻━┻")});
    groups.add(new Value[] {stringValue("a")});
    groups.add(new Value[] {stringValue("abc def")});
    // latin small letter e + combining acute accent + latin small letter b
    groups.add(new Value[] {stringValue("e\u0301b")});
    groups.add(new Value[] {stringValue("æ")});
    // latin small letter e with acute accent + latin small letter a
    groups.add(new Value[] {stringValue("\u00e9a")});

    // blobs
    groups.add(new Value[] {blobValue(new byte[] {})});
    groups.add(new Value[] {blobValue(new byte[] {0})});
    groups.add(new Value[] {blobValue(new byte[] {0, 1, 2, 3, 4})});
    groups.add(new Value[] {blobValue(new byte[] {0, 1, 2, 4, 3})});
    groups.add(new Value[] {blobValue(new byte[] {127})});

    // BSON Binary Data
    groups.add(new Value[] {bsonBinaryData(5, new byte[] {})});
    groups.add(new Value[] {bsonBinaryData(5, new byte[] {0}), bsonBinaryData(5, new byte[] {0})});
    groups.add(new Value[] {bsonBinaryData(7, new byte[] {0, 1, 2, 3, 4})});
    groups.add(new Value[] {bsonBinaryData(7, new byte[] {0, 1, 2, 4, 3})});

    // resource names
    groups.add(new Value[] {referenceValue("projects/p1/databases/d1/documents/c1/doc1")});
    groups.add(new Value[] {referenceValue("projects/p1/databases/d1/documents/c1/doc2")});
    groups.add(new Value[] {referenceValue("projects/p1/databases/d1/documents/c1/doc2/c2/doc1")});
    groups.add(new Value[] {referenceValue("projects/p1/databases/d1/documents/c1/doc2/c2/doc2")});
    groups.add(new Value[] {referenceValue("projects/p1/databases/d1/documents/c10/doc1")});
    groups.add(new Value[] {referenceValue("projects/p1/databases/d1/documents/c2/doc1")});
    groups.add(new Value[] {referenceValue("projects/p2/databases/d2/documents/c1/doc1")});
    groups.add(new Value[] {referenceValue("projects/p2/databases/d2/documents/c1-/doc1")});
    groups.add(new Value[] {referenceValue("projects/p2/databases/d3/documents/c1-/doc1")});

    // BSON ObjectId
    groups.add(new Value[] {bsonObjectIdValue("foo"), bsonObjectIdValue("foo")});
    groups.add(new Value[] {bsonObjectIdValue("foo\\u0301")});
    groups.add(new Value[] {bsonObjectIdValue("xyz")});
    groups.add(
        new Value[] {bsonObjectIdValue("Ḟoo")}); // with latin capital letter f with dot above

    // geo points
    groups.add(new Value[] {geoPointValue(-90, -180)});
    groups.add(new Value[] {geoPointValue(-90, 0)});
    groups.add(new Value[] {geoPointValue(-90, 180)});
    groups.add(new Value[] {geoPointValue(0, -180)});
    groups.add(new Value[] {geoPointValue(0, 0)});
    groups.add(new Value[] {geoPointValue(0, 180)});
    groups.add(new Value[] {geoPointValue(1, -180)});
    groups.add(new Value[] {geoPointValue(1, 0)});
    groups.add(new Value[] {geoPointValue(1, 180)});
    groups.add(new Value[] {geoPointValue(90, -180)});
    groups.add(new Value[] {geoPointValue(90, 0)});
    groups.add(new Value[] {geoPointValue(90, 180)});

    // Regex
    groups.add(new Value[] {regexValue("a", "bar1"), regexValue("a", "bar1")});
    groups.add(new Value[] {regexValue("foo", "bar1")});
    groups.add(new Value[] {regexValue("foo", "bar2")});
    groups.add(new Value[] {regexValue("go", "bar1")});

    // arrays
    groups.add(new Value[] {arrayValue()});
    groups.add(new Value[] {arrayValue(stringValue("bar"))});
    groups.add(new Value[] {arrayValue(stringValue("foo"))});
    groups.add(new Value[] {arrayValue(stringValue("foo"), intValue(0))});
    groups.add(new Value[] {arrayValue(stringValue("foo"), intValue(1))});
    groups.add(new Value[] {arrayValue(stringValue("foo"), stringValue("0"))});

    // objects
    groups.add(new Value[] {objectValue("bar", intValue(0))});
    groups.add(new Value[] {objectValue("bar", intValue(0), "foo", intValue(1))});
    groups.add(new Value[] {objectValue("bar", intValue(1))});
    groups.add(new Value[] {objectValue("bar", intValue(2))});
    groups.add(new Value[] {objectValue("bar", stringValue("0"))});

    groups.add(new Value[] {maxKeyValue(), maxKeyValue()});

    for (int left = 0; left < groups.size(); left++) {
      for (int right = 0; right < groups.size(); right++) {
        for (int i = 0; i < groups.get(left).length; i++) {
          for (int j = 0; j < groups.get(right).length; j++) {
            assertEquals(
                String.format(
                    "Order does not match for: groups[%d][%d] and groups[%d][%d]",
                    left, i, right, j),
                Integer.compare(left, right),
                Integer.compare(
                    Order.INSTANCE.compare(groups.get(left)[i], groups.get(right)[j]), 0));
          }
        }
      }
    }
  }

  private Value booleanValue(boolean b) {
    return Value.newBuilder().setBooleanValue(b).build();
  }

  private Value doubleValue(double d) {
    return Value.newBuilder().setDoubleValue(d).build();
  }

  private Value intValue(long l) {
    return Value.newBuilder().setIntegerValue(l).build();
  }

  private Value stringValue(String s) {
    return Value.newBuilder().setStringValue(s).build();
  }

  private Value referenceValue(String r) {
    return Value.newBuilder().setReferenceValue(r).build();
  }

  private Value blobValue(byte[] b) {
    return Value.newBuilder().setBytesValue(ByteString.copyFrom(b)).build();
  }

  private Value nullValue() {
    return Value.newBuilder().setNullValue(NullValue.NULL_VALUE).build();
  }

  private Value minKeyValue() {
    return Value.newBuilder().setMapValue(MinKey.instance().toProto()).build();
  }

  private Value maxKeyValue() {
    return Value.newBuilder().setMapValue(MaxKey.instance().toProto()).build();
  }

  private Value regexValue(String pattern, String options) {
    return Value.newBuilder().setMapValue(new RegexValue(pattern, options).toProto()).build();
  }

  private Value int32Value(int value) {
    return Value.newBuilder().setMapValue(new Int32Value(value).toProto()).build();
  }

  private Value bsonObjectIdValue(String oid) {
    return Value.newBuilder().setMapValue(new BsonObjectId(oid).toProto()).build();
  }

  private Value bsonTimestampValue(long seconds, long increment) {
    return Value.newBuilder().setMapValue(new BsonTimestamp(seconds, increment).toProto()).build();
  }

  private Value bsonBinaryData(int subtype, byte[] data) {
    return Value.newBuilder()
        .setMapValue(BsonBinaryData.fromBytes(subtype, data).toProto())
        .build();
  }

  private Value timestampValue(long seconds, int nanos) {
    return Value.newBuilder()
        .setTimestampValue(Timestamp.newBuilder().setSeconds(seconds).setNanos(nanos).build())
        .build();
  }

  private Value geoPointValue(double latitude, double longitude) {
    return Value.newBuilder()
        .setGeoPointValue(LatLng.newBuilder().setLatitude(latitude).setLongitude(longitude).build())
        .build();
  }

  private Value arrayValue(Value... values) {
    return Value.newBuilder()
        .setArrayValue(ArrayValue.newBuilder().addAllValues(Arrays.asList(values)).build())
        .build();
  }

  private Value objectValue(String key, Value value, Object... keysAndValues) {
    MapValue.Builder mapBuilder = MapValue.newBuilder();
    mapBuilder.putFields(key, value);

    for (int i = 0; i < keysAndValues.length; i += 2) {
      mapBuilder.putFields((String) keysAndValues[i], (Value) keysAndValues[i + 1]);
    }

    return Value.newBuilder().setMapValue(mapBuilder.build()).build();
  }
}
