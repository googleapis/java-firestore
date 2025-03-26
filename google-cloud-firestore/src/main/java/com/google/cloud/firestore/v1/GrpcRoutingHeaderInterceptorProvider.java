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
package com.google.cloud.firestore.v1;

import com.google.api.core.InternalApi;
import com.google.api.gax.grpc.GrpcHeaderInterceptor;
import com.google.api.gax.grpc.GrpcInterceptorProvider;
import io.grpc.ClientInterceptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

@InternalApi
public class GrpcRoutingHeaderInterceptorProvider implements GrpcInterceptorProvider {
  private static final String X_GOOG_SKIP_FIRESTORE_DFE = "x-goog-firestore-skip-dfe";
  List<ClientInterceptor> clientInterceptors;

  private GrpcRoutingHeaderInterceptorProvider(
      @Nonnull GrpcHeaderInterceptor grpcRoutingHeaderInterceptor) {
    clientInterceptors = Collections.singletonList(grpcRoutingHeaderInterceptor);
  }

  public static GrpcRoutingHeaderInterceptorProvider defaultGrpcRoutingHeaderInterceptorProvider() {
    Map<String, String> staticHeaders = new HashMap<String, String>();
    staticHeaders.put(X_GOOG_SKIP_FIRESTORE_DFE, "true");
    return new GrpcRoutingHeaderInterceptorProvider(new GrpcHeaderInterceptor(staticHeaders));
  }

  @Override
  public List<ClientInterceptor> getInterceptors() {
    return clientInterceptors;
  }
}
