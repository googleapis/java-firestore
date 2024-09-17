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

package com.google.cloud.firestore;

import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.cloud.Timestamp;
import com.google.firestore.v1.Document;
import com.google.firestore.v1.RunQueryResponse;
import java.util.ArrayList;
import java.util.List;

public abstract class QueryApiStreamObserver implements ApiStreamObserver<RunQueryResponse> {

  protected final List<QueryDocumentSnapshot> documentSnapshots = new ArrayList<>();
  protected Timestamp responseReadTime;
  FirestoreRpcContext rpcContext;

  QueryApiStreamObserver(FirestoreRpcContext rpcContext) {
    this.rpcContext = rpcContext;
  }

  @Override
  public void onNext(RunQueryResponse runQueryResponse) {
    if (runQueryResponse.hasDocument()) {
      Document document = runQueryResponse.getDocument();
      QueryDocumentSnapshot documentSnapshot =
          QueryDocumentSnapshot.fromDocument(
              this.rpcContext, Timestamp.fromProto(runQueryResponse.getReadTime()), document);
      documentSnapshots.add(documentSnapshot);
    }
    if (responseReadTime == null) {
      responseReadTime = Timestamp.fromProto(runQueryResponse.getReadTime());
    }
  }
}
