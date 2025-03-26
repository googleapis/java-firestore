package com.google.cloud.firestore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;
import org.junit.Test;

public class RequestRoutingHeaderProviderTest {

  @Test
  public void testHeaderProviderNullSessionId() {
    RequestRoutingHeaderProvider headerProvider =
        new RequestRoutingHeaderProvider(/* skipDfe */ true, /* istTransaction */ false, null);
    assertEquals("true", headerProvider.getHeaders().get("x-goog-firestore-skip-dfe"));
    assertEquals("false", headerProvider.getHeaders().get("x-goog-firestore-transaction"));
    assertNotNull(headerProvider.getHeaders().get("x-goog-firestore-session-id"));
  }

  @Test
  public void testHeaderProviderNonNullSessionId() {
    String sessionId = UUID.randomUUID().toString();
    RequestRoutingHeaderProvider headerProvider =
        new RequestRoutingHeaderProvider(/* skipDfe */ true, /* istTransaction */ true, sessionId);
    assertEquals("true", headerProvider.getHeaders().get("x-goog-firestore-skip-dfe"));
    assertEquals("true", headerProvider.getHeaders().get("x-goog-firestore-transaction"));
    assertEquals(sessionId, headerProvider.getHeaders().get("x-goog-firestore-session-id"));
  }
}
