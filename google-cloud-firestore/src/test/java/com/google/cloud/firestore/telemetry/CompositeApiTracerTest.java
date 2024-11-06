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
package com.google.cloud.firestore.telemetry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.api.gax.tracing.ApiTracer;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.threeten.bp.Duration;

@RunWith(JUnit4.class)
public class CompositeApiTracerTest {

  private final ApiTracer child1 = mock(ApiTracer.class);
  private final ApiTracer child2 = mock(ApiTracer.class);
  private final CompositeApiTracer compositeApiTracer =
      new CompositeApiTracer(Arrays.asList(child1, child2));

  @Test
  public void inScope_callsInScopeOnChildren() {
    compositeApiTracer.inScope();

    verify(child1, times(1)).inScope();
    verify(child2, times(1)).inScope();
  }

  @Test
  public void inScope_closesChildScopes() {
    ApiTracer.Scope childScope1 = mock(ApiTracer.Scope.class);
    ApiTracer.Scope childScope2 = mock(ApiTracer.Scope.class);
    when(child1.inScope()).thenReturn(childScope1);
    when(child2.inScope()).thenReturn(childScope2);

    compositeApiTracer.inScope().close();

    verify(child1).inScope();
    verify(child2).inScope();
    verify(childScope1).close();
    verify(childScope2).close();
  }

  @Test
  public void operationSucceeded_callsOperationSucceededOnChildren() {
    compositeApiTracer.operationSucceeded();

    verify(child1, times(1)).operationSucceeded();
    verify(child2, times(1)).operationSucceeded();
  }

  @Test
  public void operationCancelled_callsOperationCancelledOnChildren() {
    compositeApiTracer.operationCancelled();

    verify(child1, times(1)).operationCancelled();
    verify(child2, times(1)).operationCancelled();
  }

  @Test
  public void operationFailed_callsOperationFailedOnChildren() {
    Exception error = new Exception("Test error");
    compositeApiTracer.operationFailed(error);

    verify(child1, times(1)).operationFailed(error);
    verify(child2, times(1)).operationFailed(error);
  }

  @Test
  public void connectionSelected_callsConnectionSelectedOnChildren() {
    String connectionId = "connection-id";
    compositeApiTracer.connectionSelected(connectionId);

    verify(child1, times(1)).connectionSelected(connectionId);
    verify(child2, times(1)).connectionSelected(connectionId);
  }

  @Test
  public void attemptStarted_callsAttemptStartedOnChildren() {
    int attemptNumber = 1;
    compositeApiTracer.attemptStarted(attemptNumber);

    verify(child1, times(1)).attemptStarted(null, attemptNumber);
    verify(child2, times(1)).attemptStarted(null, attemptNumber);
  }

  @Test
  public void attemptStartedWithRequest_callsAttemptStartedOnChildren() {
    Object request = new Object();
    int attemptNumber = 1;
    compositeApiTracer.attemptStarted(request, attemptNumber);

    verify(child1, times(1)).attemptStarted(request, attemptNumber);
    verify(child2, times(1)).attemptStarted(request, attemptNumber);
  }

  @Test
  public void attemptSucceeded_callsAttemptSucceededOnChildren() {
    compositeApiTracer.attemptSucceeded();

    verify(child1, times(1)).attemptSucceeded();
    verify(child2, times(1)).attemptSucceeded();
  }

  @Test
  public void attemptCancelled_callsAttemptCancelledOnChildren() {
    compositeApiTracer.attemptCancelled();

    verify(child1, times(1)).attemptCancelled();
    verify(child2, times(1)).attemptCancelled();
  }

  @Test
  public void attemptFailed_callsAttemptFailedOnChildren() {
    Exception error = new Exception("Test error");
    Duration delay = Duration.ofSeconds(1);
    compositeApiTracer.attemptFailed(error, delay);

    verify(child1, times(1)).attemptFailed(error, delay);
    verify(child2, times(1)).attemptFailed(error, delay);
  }

  @Test
  public void attemptFailedDuration_callsAttemptFailedDurationOnChildren() {
    Exception error = new Exception("Test error");
    java.time.Duration delay = java.time.Duration.ofSeconds(1);
    compositeApiTracer.attemptFailedDuration(error, delay);

    verify(child1, times(1)).attemptFailedDuration(error, delay);
    verify(child2, times(1)).attemptFailedDuration(error, delay);
  }

  @Test
  public void attemptFailedRetriesExhausted_callsAttemptFailedRetriesExhaustedOnChildren() {
    Exception error = new Exception("Test error");
    compositeApiTracer.attemptFailedRetriesExhausted(error);

    verify(child1, times(1)).attemptFailedRetriesExhausted(error);
    verify(child2, times(1)).attemptFailedRetriesExhausted(error);
  }

  @Test
  public void attemptPermanentFailure_callsAttemptPermanentFailureOnChildren() {
    Exception error = new Exception("Test error");
    compositeApiTracer.attemptPermanentFailure(error);

    verify(child1, times(1)).attemptPermanentFailure(error);
    verify(child2, times(1)).attemptPermanentFailure(error);
  }

  @Test
  public void lroStartFailed_callsLroStartFailedOnChildren() {
    Exception error = new Exception("Test error");
    compositeApiTracer.lroStartFailed(error);

    verify(child1, times(1)).lroStartFailed(error);
    verify(child2, times(1)).lroStartFailed(error);
  }

  @Test
  public void lroStartSucceeded_callsLroStartSucceededOnChildren() {
    compositeApiTracer.lroStartSucceeded();

    verify(child1, times(1)).lroStartSucceeded();
    verify(child2, times(1)).lroStartSucceeded();
  }

  @Test
  public void responseReceived_callsResponseReceivedOnChildren() {
    compositeApiTracer.responseReceived();

    verify(child1, times(1)).responseReceived();
    verify(child2, times(1)).responseReceived();
  }

  @Test
  public void requestSent_callsRequestSentOnChildren() {
    compositeApiTracer.requestSent();

    verify(child1, times(1)).requestSent();
    verify(child2, times(1)).requestSent();
  }

  @Test
  public void batchRequestSent_callsBatchRequestSentOnChildren() {
    long elementCount = 10;
    long requestSize = 100;
    compositeApiTracer.batchRequestSent(elementCount, requestSize);

    verify(child1, times(1)).batchRequestSent(elementCount, requestSize);
    verify(child2, times(1)).batchRequestSent(elementCount, requestSize);
  }

  @Test
  public void noInteractions_whenNoChildren() {
    CompositeApiTracer emptyTracer = new CompositeApiTracer(Arrays.asList());

    emptyTracer.inScope();
    emptyTracer.operationSucceeded();
    emptyTracer.operationCancelled();
    emptyTracer.operationFailed(new Exception("Test error"));
    emptyTracer.connectionSelected("connection-id");
    emptyTracer.attemptStarted(1);
    emptyTracer.attemptStarted(new Object(), 1);
    emptyTracer.attemptSucceeded();
    emptyTracer.attemptCancelled();
    emptyTracer.attemptFailed(new Exception("Test error"), Duration.ofSeconds(1));
    emptyTracer.attemptFailedDuration(new Exception("Test error"), java.time.Duration.ofSeconds(1));
    emptyTracer.attemptFailedRetriesExhausted(new Exception("Test error"));
    emptyTracer.attemptPermanentFailure(new Exception("Test error"));
    emptyTracer.lroStartFailed(new Exception("Test error"));
    emptyTracer.lroStartSucceeded();
    emptyTracer.responseReceived();
    emptyTracer.requestSent();
    emptyTracer.batchRequestSent(10, 100);

    verifyNoMoreInteractions(child1, child2);
  }
}
