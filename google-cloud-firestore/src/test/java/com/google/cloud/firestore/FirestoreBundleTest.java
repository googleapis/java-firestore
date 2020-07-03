package com.google.cloud.firestore;

import static com.google.cloud.firestore.LocalFirestoreHelper.COLLECTION_ID;
import static com.google.cloud.firestore.LocalFirestoreHelper.bundleToElementList;
import static com.google.cloud.firestore.LocalFirestoreHelper.limit;
import static com.google.cloud.firestore.LocalFirestoreHelper.query;
import static com.google.cloud.firestore.LocalFirestoreHelper.queryResponse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;

import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.firestore.proto.BundleElement;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FirestoreBundleTest {
  @Before
  public void before() {
  }

  @Test
  public void testBundleToElementArrayWorks() throws Exception {
    String bundleString =
        "20{\"a\":\"string value\"}9{\"b\":123}26{\"c\":{\"d\":\"nested value\"}}";
    List<String> elements = bundleToElementList(ByteBuffer.wrap(bundleString.getBytes(
        StandardCharsets.UTF_8)));
    assertArrayEquals(elements.toArray(), new String[]{"{\"a\":\"string value\"}", "{\"b\":123}",
        "{\"c\":{\"d\":\"nested value\"}}"});
  }
}
