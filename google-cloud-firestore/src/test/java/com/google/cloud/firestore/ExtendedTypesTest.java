/*
 * Copyright 2025 Google LLC
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

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.cloud.firestore.UserDataConverter.MapRepresentation;
import com.google.firestore.v1.MapValue;
import com.google.firestore.v1.Value;
import com.google.protobuf.ByteString;
import com.google.protobuf.NullValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExtendedTypesTest {

  Value encode(Object o) {
    // The field path and encoding options are not used for encoding of extended types,
    // so we'll use empty field path and `ARGUMENT` as encoding option.
    return UserDataConverter.encodeValue(FieldPath.empty(), o, UserDataConverter.ARGUMENT);
  }

  Object decode(Value value) {
    // The RpcContext is not needed for decoding extended types, so we can use null.
    return UserDataConverter.decodeValue(null, value);
  }

  void assertEncodesAndDecodesCorrectly(Value proto, Object object) {
    assertThat(proto).isEqualTo(encode(object));
    assertThat(object).isEqualTo(decode(proto));
    assertThat(decode(encode(object))).isEqualTo(object);
    assertThat(encode(decode(proto))).isEqualTo(proto);
  }

  @Test
  public void minKeyIsSingleton() {
    MinKey minKey1 = FieldValue.minKey();
    MinKey minKey2 = MinKey.instance();
    assertThat(minKey1).isEqualTo(minKey2);
    assertThat(minKey1).isNotEqualTo(FieldValue.maxKey());
  }

  @Test
  public void maxKeyIsSingleton() {
    MaxKey maxKey1 = FieldValue.maxKey();
    MaxKey maxKey2 = MaxKey.instance();
    assertThat(maxKey1).isEqualTo(maxKey2);
    assertThat(maxKey1).isNotEqualTo(FieldValue.minKey());
  }

  @Test
  public void regexFieldsAndEquality() {
    RegexValue regex1 = FieldValue.regex("^foo", "i");
    RegexValue regex2 = new RegexValue("^foo", "i");
    RegexValue regex3 = new RegexValue("^foo", "x");
    RegexValue regex4 = new RegexValue("^bar", "i");

    assertThat(regex1.pattern).isEqualTo("^foo");
    assertThat(regex1.options).isEqualTo("i");
    assertThat(regex1).isEqualTo(regex2);
    assertThat(regex1).isNotEqualTo(regex3);
    assertThat(regex1).isNotEqualTo(regex4);
  }

  @Test
  public void Int32ValueAndEquality() {
    Int32Value i1 = FieldValue.int32(123);
    Int32Value i2 = new Int32Value(123);
    Int32Value i3 = new Int32Value(456);

    assertThat(i1.value).isEqualTo(123);
    assertThat(i1).isEqualTo(i2);
    assertThat(i1).isNotEqualTo(i3);
  }

  @Test
  public void BsonObjectIdValueAndEquality() {
    BsonObjectId oid1 = FieldValue.bsonObjectId("foo");
    BsonObjectId oid2 = new BsonObjectId("foo");
    BsonObjectId oid3 = new BsonObjectId("bar");

    assertThat(oid1.value).isEqualTo("foo");
    assertThat(oid1).isEqualTo(oid2);
    assertThat(oid1).isNotEqualTo(oid3);
  }

  @Test
  public void BsonTimestampValuesAndEquality() {
    BsonTimestamp t1 = FieldValue.bsonTimestamp(123, 456);
    BsonTimestamp t2 = new BsonTimestamp(123, 456);
    BsonTimestamp t3 = new BsonTimestamp(124, 456);
    BsonTimestamp t4 = new BsonTimestamp(123, 457);

    assertThat(t1.seconds).isEqualTo(123);
    assertThat(t1.increment).isEqualTo(456);
    assertThat(t1).isEqualTo(t2);
    assertThat(t1).isNotEqualTo(t3);
    assertThat(t1).isNotEqualTo(t4);
  }

  @Test
  public void BsonBinaryDataValuesAndEquality() {
    BsonBinaryData b1 = FieldValue.bsonBinaryData(127, new byte[] {1, 2, 3});
    BsonBinaryData b2 = BsonBinaryData.fromBytes(127, new byte[] {1, 2, 3});
    BsonBinaryData b3 = BsonBinaryData.fromBytes(1, new byte[] {1, 2, 3});
    BsonBinaryData b4 = BsonBinaryData.fromBytes(127, new byte[] {1, 2, 4});

    assertThat(b1.subtype()).isEqualTo(127);
    assertThat(b1.dataAsBytes()).isEqualTo(new byte[] {1, 2, 3});
    assertThat(b1).isEqualTo(b2);
    assertThat(b1).isNotEqualTo(b3);
    assertThat(b1).isNotEqualTo(b4);
  }

  @Test
  public void BsonBinaryDataConvertsByteToIntAndIntToByteCorrectly() {
    byte[] data = new byte[] {1, 2, 3};
    BsonBinaryData b1 = FieldValue.bsonBinaryData(127, data); // 0x7F - MSB:0
    BsonBinaryData b2 = FieldValue.bsonBinaryData(128, data); // 0x80 - MSB:1
    BsonBinaryData b3 = BsonBinaryData.fromBytes(255, data); // 0xFF - MSB:1

    BsonBinaryData b4 = UserDataConverter.decodeBsonBinary(b1.toProto());
    BsonBinaryData b5 = UserDataConverter.decodeBsonBinary(b2.toProto());
    BsonBinaryData b6 = UserDataConverter.decodeBsonBinary(b3.toProto());

    assertThat(b4.subtype()).isEqualTo(127);
    assertThat(b4.dataAsBytes()).isEqualTo(data);
    assertThat(b4).isEqualTo(b1);

    assertThat(b5.subtype()).isEqualTo(128);
    assertThat(b5.dataAsBytes()).isEqualTo(data);
    assertThat(b5).isEqualTo(b2);

    assertThat(b6.subtype()).isEqualTo(255);
    assertThat(b6.dataAsBytes()).isEqualTo(data);
    assertThat(b6).isEqualTo(b3);
  }

  @Test
  public void BsonBinaryDataConstructorsEncodeToTheSameValue() {
    byte[] bytes = new byte[] {1, 2, 3};
    ByteString byteString = ByteString.copyFromUtf8("\01\02\03");
    BsonBinaryData b1 = BsonBinaryData.fromByteString(127, byteString);
    BsonBinaryData b2 = BsonBinaryData.fromBytes(127, bytes);
    assertThat(b1.toProto()).isEqualTo(b2.toProto());
    assertThat(b1).isEqualTo(b2);

    BsonBinaryData b3 = BsonBinaryData.fromByteString(128, byteString);
    BsonBinaryData b4 = BsonBinaryData.fromBytes(128, bytes);
    assertThat(b3.toProto()).isEqualTo(b4.toProto());
    assertThat(b3).isEqualTo(b4);
  }

  @Test
  public void DetectsBsonTypesCorrectly() {
    MapValue minKeyMapValue = UserDataConverter.encodeMinKey();
    MapValue maxKeyMapValue = UserDataConverter.encodeMaxKey();
    MapValue int32MapValue = UserDataConverter.encodeInt32Value(5);
    MapValue regexMapValue = UserDataConverter.encodeRegexValue("^foo", "i");
    MapValue bsonTimestamp = UserDataConverter.encodeBsonTimestamp(1, 2);
    MapValue bsonObjectId = UserDataConverter.encodeBsonObjectId("foo");
    MapValue bsonBinaryData1 = UserDataConverter.encodeBsonBinaryData(128, ByteString.EMPTY);
    MapValue bsonBinaryData2 =
        UserDataConverter.encodeBsonBinaryData(128, ByteString.fromHex("010203"));

    assertTrue(UserDataConverter.isMinKey(minKeyMapValue));
    assertFalse(UserDataConverter.isMinKey(maxKeyMapValue));
    assertFalse(UserDataConverter.isMinKey(int32MapValue));
    assertFalse(UserDataConverter.isMinKey(regexMapValue));
    assertFalse(UserDataConverter.isMinKey(bsonTimestamp));
    assertFalse(UserDataConverter.isMinKey(bsonObjectId));
    assertFalse(UserDataConverter.isMinKey(bsonBinaryData1));
    assertFalse(UserDataConverter.isMinKey(bsonBinaryData2));

    assertFalse(UserDataConverter.isMaxKey(minKeyMapValue));
    assertTrue(UserDataConverter.isMaxKey(maxKeyMapValue));
    assertFalse(UserDataConverter.isMaxKey(int32MapValue));
    assertFalse(UserDataConverter.isMaxKey(regexMapValue));
    assertFalse(UserDataConverter.isMaxKey(bsonTimestamp));
    assertFalse(UserDataConverter.isMaxKey(bsonObjectId));
    assertFalse(UserDataConverter.isMaxKey(bsonBinaryData1));
    assertFalse(UserDataConverter.isMaxKey(bsonBinaryData2));

    assertFalse(UserDataConverter.isInt32Value(minKeyMapValue));
    assertFalse(UserDataConverter.isInt32Value(maxKeyMapValue));
    assertTrue(UserDataConverter.isInt32Value(int32MapValue));
    assertFalse(UserDataConverter.isInt32Value(regexMapValue));
    assertFalse(UserDataConverter.isInt32Value(bsonTimestamp));
    assertFalse(UserDataConverter.isInt32Value(bsonObjectId));
    assertFalse(UserDataConverter.isInt32Value(bsonBinaryData1));
    assertFalse(UserDataConverter.isInt32Value(bsonBinaryData2));

    assertFalse(UserDataConverter.isRegexValue(minKeyMapValue));
    assertFalse(UserDataConverter.isRegexValue(maxKeyMapValue));
    assertFalse(UserDataConverter.isRegexValue(int32MapValue));
    assertTrue(UserDataConverter.isRegexValue(regexMapValue));
    assertFalse(UserDataConverter.isRegexValue(bsonTimestamp));
    assertFalse(UserDataConverter.isRegexValue(bsonObjectId));
    assertFalse(UserDataConverter.isRegexValue(bsonBinaryData1));
    assertFalse(UserDataConverter.isRegexValue(bsonBinaryData2));

    assertFalse(UserDataConverter.isBsonTimestamp(minKeyMapValue));
    assertFalse(UserDataConverter.isBsonTimestamp(maxKeyMapValue));
    assertFalse(UserDataConverter.isBsonTimestamp(int32MapValue));
    assertFalse(UserDataConverter.isBsonTimestamp(regexMapValue));
    assertTrue(UserDataConverter.isBsonTimestamp(bsonTimestamp));
    assertFalse(UserDataConverter.isBsonTimestamp(bsonObjectId));
    assertFalse(UserDataConverter.isBsonTimestamp(bsonBinaryData1));
    assertFalse(UserDataConverter.isBsonTimestamp(bsonBinaryData2));

    assertFalse(UserDataConverter.isBsonObjectId(minKeyMapValue));
    assertFalse(UserDataConverter.isBsonObjectId(maxKeyMapValue));
    assertFalse(UserDataConverter.isBsonObjectId(int32MapValue));
    assertFalse(UserDataConverter.isBsonObjectId(regexMapValue));
    assertFalse(UserDataConverter.isBsonObjectId(bsonTimestamp));
    assertTrue(UserDataConverter.isBsonObjectId(bsonObjectId));
    assertFalse(UserDataConverter.isBsonObjectId(bsonBinaryData1));
    assertFalse(UserDataConverter.isBsonObjectId(bsonBinaryData2));

    assertFalse(UserDataConverter.isBsonBinaryData(minKeyMapValue));
    assertFalse(UserDataConverter.isBsonBinaryData(maxKeyMapValue));
    assertFalse(UserDataConverter.isBsonBinaryData(int32MapValue));
    assertFalse(UserDataConverter.isBsonBinaryData(regexMapValue));
    assertFalse(UserDataConverter.isBsonBinaryData(bsonTimestamp));
    assertFalse(UserDataConverter.isBsonBinaryData(bsonObjectId));
    assertTrue(UserDataConverter.isBsonBinaryData(bsonBinaryData1));
    assertTrue(UserDataConverter.isBsonBinaryData(bsonBinaryData2));

    assertEquals(
        UserDataConverter.detectMapRepresentation(minKeyMapValue), MapRepresentation.MIN_KEY);
    assertEquals(
        UserDataConverter.detectMapRepresentation(maxKeyMapValue), MapRepresentation.MAX_KEY);
    assertEquals(UserDataConverter.detectMapRepresentation(int32MapValue), MapRepresentation.INT32);
    assertEquals(UserDataConverter.detectMapRepresentation(regexMapValue), MapRepresentation.REGEX);
    assertEquals(
        UserDataConverter.detectMapRepresentation(bsonTimestamp), MapRepresentation.BSON_TIMESTAMP);
    assertEquals(
        UserDataConverter.detectMapRepresentation(bsonObjectId), MapRepresentation.BSON_OBJECT_ID);
    assertEquals(
        UserDataConverter.detectMapRepresentation(bsonBinaryData1),
        MapRepresentation.BSON_BINARY_DATA);
    assertEquals(
        UserDataConverter.detectMapRepresentation(bsonBinaryData2),
        MapRepresentation.BSON_BINARY_DATA);
  }

  @Test
  public void BsonTimestampValidation() {
    // Negative seconds
    IllegalArgumentException error1 = null;
    try {
      BsonTimestamp t1 = FieldValue.bsonTimestamp(-1, 1);
    } catch (IllegalArgumentException e) {
      error1 = e;
    }
    assertThat(error1).isNotNull();
    assertThat(error1.getMessage())
        .isEqualTo("BsonTimestamp 'seconds' must be in the range of a 32-bit unsigned integer.");

    // Larger than 2^32-1 seconds
    IllegalArgumentException error2 = null;
    try {
      BsonTimestamp t1 = FieldValue.bsonTimestamp(4294967296L, 1);
    } catch (IllegalArgumentException e) {
      error2 = e;
    }
    assertThat(error2).isNotNull();
    assertThat(error2.getMessage())
        .isEqualTo("BsonTimestamp 'seconds' must be in the range of a 32-bit unsigned integer.");

    // Negative increment
    IllegalArgumentException error3 = null;
    try {
      BsonTimestamp t1 = FieldValue.bsonTimestamp(1234, -1);
    } catch (IllegalArgumentException e) {
      error3 = e;
    }
    assertThat(error3).isNotNull();
    assertThat(error3.getMessage())
        .isEqualTo("BsonTimestamp 'increment' must be in the range of a 32-bit unsigned integer.");

    // Larger than 2^32-1 increment
    IllegalArgumentException error4 = null;
    try {
      BsonTimestamp t1 = FieldValue.bsonTimestamp(123, 4294967296L);
    } catch (IllegalArgumentException e) {
      error4 = e;
    }
    assertThat(error4).isNotNull();
    assertThat(error4.getMessage())
        .isEqualTo("BsonTimestamp 'increment' must be in the range of a 32-bit unsigned integer.");
  }

  @Test
  public void canEncodeAndDecodeMinKey() {
    MinKey minKey = FieldValue.minKey();
    Value proto =
        Value.newBuilder()
            .setMapValue(
                MapValue.newBuilder()
                    .putFields(
                        "__min__", Value.newBuilder().setNullValue(NullValue.NULL_VALUE).build())
                    .build())
            .build();
    assertEncodesAndDecodesCorrectly(proto, minKey);
  }

  @Test
  public void canEncodeAndDecodeMaxKey() {
    MaxKey maxKey = FieldValue.maxKey();
    Value proto =
        Value.newBuilder()
            .setMapValue(
                MapValue.newBuilder()
                    .putFields(
                        "__max__", Value.newBuilder().setNullValue(NullValue.NULL_VALUE).build())
                    .build())
            .build();
    assertEncodesAndDecodesCorrectly(proto, maxKey);
  }

  @Test
  public void canEncodeAndDecodeRegexValue() {
    RegexValue regexValue = new RegexValue("^foo", "i");
    Value proto =
        Value.newBuilder()
            .setMapValue(
                MapValue.newBuilder()
                    .putFields(
                        "__regex__",
                        Value.newBuilder()
                            .setMapValue(
                                MapValue.newBuilder()
                                    .putFields(
                                        "pattern",
                                        Value.newBuilder().setStringValue("^foo").build())
                                    .putFields(
                                        "options", Value.newBuilder().setStringValue("i").build())
                                    .build())
                            .build())
                    .build())
            .build();
    assertEncodesAndDecodesCorrectly(proto, regexValue);
  }

  @Test
  public void canEncodeAndDecodeInt32Value() {
    Int32Value int32Value = new Int32Value(12345);
    Value proto =
        Value.newBuilder()
            .setMapValue(
                MapValue.newBuilder()
                    .putFields("__int__", Value.newBuilder().setIntegerValue(12345).build())
                    .build())
            .build();
    assertEncodesAndDecodesCorrectly(proto, int32Value);
  }

  @Test
  public void canEncodeAndDecodeBsonObjectId() {
    BsonObjectId bsonObjectId = new BsonObjectId("foo");
    Value proto =
        Value.newBuilder()
            .setMapValue(
                MapValue.newBuilder()
                    .putFields("__oid__", Value.newBuilder().setStringValue("foo").build())
                    .build())
            .build();
    assertEncodesAndDecodesCorrectly(proto, bsonObjectId);
  }

  @Test
  public void canEncodeAndDecodeBsonTimestamp() {
    BsonTimestamp bsonTimestamp = FieldValue.bsonTimestamp(12345, 67);
    Value proto =
        Value.newBuilder()
            .setMapValue(
                MapValue.newBuilder()
                    .putFields(
                        "__request_timestamp__",
                        Value.newBuilder()
                            .setMapValue(
                                MapValue.newBuilder()
                                    .putFields(
                                        "seconds",
                                        Value.newBuilder().setIntegerValue(12345).build())
                                    .putFields(
                                        "increment", Value.newBuilder().setIntegerValue(67).build())
                                    .build())
                            .build())
                    .build())
            .build();
    assertEncodesAndDecodesCorrectly(proto, bsonTimestamp);
  }

  @Test
  public void canEncodeAndDecodeBsonBinaryData() {
    BsonBinaryData bsonBinaryData = FieldValue.bsonBinaryData(127, new byte[] {1, 2, 3});
    Value proto =
        Value.newBuilder()
            .setMapValue(
                MapValue.newBuilder()
                    .putFields(
                        "__binary__",
                        Value.newBuilder()
                            .setBytesValue(ByteString.copyFrom(new byte[] {127, 1, 2, 3}))
                            .build())
                    .build())
            .build();
    assertEncodesAndDecodesCorrectly(proto, bsonBinaryData);
  }
}
