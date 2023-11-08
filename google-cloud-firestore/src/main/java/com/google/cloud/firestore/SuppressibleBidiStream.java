package com.google.cloud.firestore;

import com.google.api.gax.rpc.BidiStreamObserver;
import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.StreamController;
import com.google.firestore.v1.ListenRequest;
import java.util.function.Function;
import java.util.logging.Logger;

public class SuppressibleBidiStream<RequestT, ResponseT>
    implements BidiStreamObserver<RequestT, ResponseT> {

  private final ClientStream<ListenRequest> stream;
  private final BidiStreamObserver<RequestT, ResponseT> delegate;
  private boolean silence = false;
  private static final Logger LOGGER = Logger.getLogger(Watch.class.getName());

  SuppressibleBidiStream(
      BidiStreamObserver<RequestT, ResponseT> responseObserverT,
      Function<BidiStreamObserver<RequestT, ResponseT>, ClientStream<ListenRequest>>
          streamSupplier) {
    this.delegate = responseObserverT;
    stream = streamSupplier.apply(this);
  }

  public void send(ListenRequest request) {
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
