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

import com.google.api.core.InternalApi;
import com.google.api.gax.rpc.HeaderProvider;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

/* RequestRoutingHeaderProvider helps set routing-related request headers. */
@InternalApi
public final class RequestRoutingHeaderProvider implements HeaderProvider, Serializable {
  // Session ID for backend affinity
  private static final String FIRESTORE_SESSION_ID = "x-goog-firestore-session-id";
  // Set if the request contains a transaction operation
  private static final String FIRESTORE_TRANSACTION = "x-goog-firestore-transaction";
  // Suggestion for optimized routing, not binding on final routing decision
  private static final String FIRESTORE_ROUTING_SKIP_DFE = "x-goog-firestore-skip-dfe";

  private Map<String, String> headers;

  public RequestRoutingHeaderProvider(
      Boolean skipDfe, Boolean isTransaction, @Nullable String sessionId) {
    headers = new HashMap<>();
    headers.put(FIRESTORE_ROUTING_SKIP_DFE, skipDfe.toString());
    headers.put(FIRESTORE_TRANSACTION, isTransaction.toString());
    headers.put(
        FIRESTORE_SESSION_ID, (sessionId == null ? UUID.randomUUID().toString() : sessionId));
  }

  @Override
  public Map<String, String> getHeaders() {
    return headers;
  }
}
