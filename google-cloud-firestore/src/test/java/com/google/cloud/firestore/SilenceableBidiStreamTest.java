package com.google.cloud.firestore;

import static com.google.common.truth.Truth.assertWithMessage;

import com.google.api.gax.rpc.BidiStreamObserver;
import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.StreamController;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SilenceableBidiStreamTest {

  @Mock
  BidiStreamObserver<Integer, String> mockObserver;

  @Mock
  ClientStream<Integer> mockClientStream;

  SilenceableBidiStream<Integer, String> sut;

  @Before
  public void before() {
    Consumer captureCall = Mockito.mock(Consumer.class);
    sut = new SilenceableBidiStream<>(mockObserver, o -> {
      captureCall.accept(o);
      return mockClientStream;
    });
    // The lambda should pass itself as parameter
    Mockito.verify(captureCall).accept(sut);
  }

  @Test
  public void byDefault_theStreamWillPassThroughData_onComplete() {
    sut.onComplete();
    Mockito.verify(mockObserver, Mockito.times(1)).onComplete();
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver);
  }

  @Test
  public void byDefault_theStreamWillPassThroughData_onError() {
    Exception e = new Exception("Something bad");
    sut.onError(e);
    Mockito.verify(mockObserver, Mockito.times(1)).onError(e);
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver);
  }

  @Test
  public void byDefault_theStreamWillPassThroughData_onResponse() {
    sut.onResponse("ABC");
    Mockito.verify(mockObserver, Mockito.times(1)).onResponse("ABC");
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver);
  }

  @Test
  public void byDefault_theStreamWillPassThroughData_onStart() {
    StreamController controller = Mockito.mock(StreamController.class);
    sut.onStart(controller);
    Mockito.verify(mockObserver, Mockito.times(1)).onStart(controller);
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver, controller);
  }

  @Test
  public void byDefault_theStreamWillPassThroughData_onReady() {
    ClientStream<Integer> client = Mockito.mock(ClientStream.class);
    sut.onReady(client);
    Mockito.verify(mockObserver, Mockito.times(1)).onReady(client);
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver, client);
  }

  @Test
  public void byDefault_theStreamWillPassThroughData_send() {
    sut.send(7);
    Mockito.verify(mockClientStream, Mockito.times(1)).send(7);
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver);
  }

  @Test
  public void closeMethod() {
    assertWithMessage("Expect isSilenced() to be false by default")
        .that(sut.isSilenced())
        .isFalse();
    sut.close();
    assertWithMessage("Expect isSilenced() to be unaffected after close()")
        .that(sut.isSilenced())
        .isFalse();
    Mockito.verify(mockClientStream, Mockito.times(1)).closeSend();
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver);
  }

  @Test
  public void closeAndSilenceMethod() {
    assertWithMessage("Expect isSilenced() to be false by default")
        .that(sut.isSilenced())
        .isFalse();
    sut.closeAndSilence();
    assertWithMessage("Expect isSilenced() to be true after closeAndSilence()")
        .that(sut.isSilenced())
        .isTrue();
    Mockito.verify(mockClientStream, Mockito.times(1)).closeSend();
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver);
  }

  @Test
  public void afterClose_theStreamWillPassThroughData_onComplete() {
    sut.close();
    sut.onComplete();
    Mockito.verify(mockClientStream, Mockito.times(1)).closeSend();
    Mockito.verify(mockObserver, Mockito.times(1)).onComplete();
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver);
  }

  @Test
  public void afterClose_theStreamWillPassThroughData_onError() {
    Exception e = new Exception("Something bad");
    sut.close();
    sut.onError(e);
    Mockito.verify(mockClientStream, Mockito.times(1)).closeSend();
    Mockito.verify(mockObserver, Mockito.times(1)).onError(e);
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver);
  }

  @Test
  public void afterClose_theStreamWillPassThroughData_onResponse() {
    sut.close();
    sut.onResponse("ABC");
    Mockito.verify(mockClientStream, Mockito.times(1)).closeSend();
    Mockito.verify(mockObserver, Mockito.times(1)).onResponse("ABC");
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver);
  }

  @Test
  public void afterCloseAndSilence_theStreamWillPassThroughData_onComplete() {
    sut.closeAndSilence();
    sut.onComplete();
    Mockito.verify(mockClientStream, Mockito.times(1)).closeSend();
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver);
  }

  @Test
  public void afterCloseAndSilence_theStreamWillPassThroughData_onError() {
    Exception e = new Exception("Something bad");
    sut.closeAndSilence();
    sut.onError(e);
    Mockito.verify(mockClientStream, Mockito.times(1)).closeSend();
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver);
  }

  @Test
  public void afterCloseAndSilence_theStreamWillPassThroughData_onResponse() {
    sut.closeAndSilence();
    sut.onResponse("ABC");
    Mockito.verify(mockClientStream, Mockito.times(1)).closeSend();
    Mockito.verifyNoMoreInteractions(mockClientStream, mockObserver);
  }
}
