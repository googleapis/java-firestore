/*
 * Copyright 2023 Google LLC
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

import com.google.api.gax.rpc.BidiStreamObserver;
import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.StreamController;
import java.util.function.Function;
import java.util.logging.Logger;

final class SilenceableBidiStream<RequestT, ResponseT>
    implements BidiStreamObserver<RequestT, ResponseT> {

  private final ClientStream<RequestT> stream;
  private final BidiStreamObserver<RequestT, ResponseT> delegate;
  private boolean silence = false;
  private static final Logger LOGGER = Logger.getLogger(Watch.class.getName());

  SilenceableBidiStream(
      BidiStreamObserver<RequestT, ResponseT> responseObserverT,
      Function<BidiStreamObserver<RequestT, ResponseT>, ClientStream<RequestT>> streamSupplier) {
    this.delegate = responseObserverT;
    stream = streamSupplier.apply(this);
  }

  public boolean isSilenced() {
    return silence;
  }

  public void send(RequestT request) {
    LOGGER.info(stream.toString());
    stream.send(request);
  }

  public void close() {
    LOGGER.info(stream::toString);
    stream.closeSend();
  }

  public void closeAndSilence() {
    LOGGER.info(stream::toString);
    silence = true;
    stream.closeSend();
  }

  @Override
  public void onReady(ClientStream<RequestT> stream) {
    if (silence) {
      LOGGER.info(() -> String.format("Silenced: %s", stream));
    } else {
      delegate.onReady(stream);
    }
  }

  @Override
  public void onStart(StreamController controller) {
    if (silence) {
      LOGGER.info(() -> String.format("Silenced: %s", stream));
    } else {
      delegate.onStart(controller);
    }
  }

  @Override
  public void onResponse(ResponseT response) {
    if (silence) {
      LOGGER.info(() -> String.format("Silenced: %s", stream));
    } else {
      delegate.onResponse(response);
    }
  }

  @Override
  public void onError(Throwable t) {
    if (silence) {
      LOGGER.info(() -> String.format("Silenced: %s", stream));
    } else {
      delegate.onError(t);
    }
  }

  @Override
  public void onComplete() {
    if (silence) {
      LOGGER.info(() -> String.format("Silenced: %s", stream));
    } else {
      delegate.onComplete();
    }
  }
}
