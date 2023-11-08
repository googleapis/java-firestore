package com.google.cloud.firestore;

import static org.mockito.Mockito.doCallRealMethod;

import com.google.api.gax.rpc.BidiStreamObserver;
import com.google.api.gax.rpc.BidiStreamingCallable;
import com.google.firestore.v1.ListenRequest;
import com.google.firestore.v1.ListenResponse;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public final class FirestoreSpy {

  public final FirestoreImpl spy;
  public final ArgumentCaptor<BidiStreamObserver<ListenRequest, ListenResponse>> streamRequestBidiStreamObserverCaptor;

  public FirestoreSpy(Firestore firestore) {
    spy = Mockito.spy((FirestoreImpl) firestore);
    streamRequestBidiStreamObserverCaptor = ArgumentCaptor.forClass(BidiStreamObserver.class);
    doCallRealMethod()
        .when(spy)
        .streamRequest(
          streamRequestBidiStreamObserverCaptor.capture(),
          ArgumentMatchers.<BidiStreamingCallable>any()
        );
  }

}
