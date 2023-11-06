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

import com.google.api.core.BetaApi;
import com.google.api.gax.tracing.ApiTracer;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.BaseApiTracer;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.threeten.bp.Duration;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicLong;

@BetaApi("Surface for tracing is not yet stable")
public class OpenTelemetryTracer extends BaseApiTracer {
    private final Tracer tracer;
    private final Span span;
    private final ApiTracerFactory.OperationType operationType;
    private volatile String lastConnectionId;
    private volatile long currentAttemptId;
    private AtomicLong attemptSentMessages = new AtomicLong(0L);
    private long attemptReceivedMessages = 0L;
    private AtomicLong totalSentMessages = new AtomicLong(0L);
    private long totalReceivedMessages = 0L;

    OpenTelemetryTracer(@Nonnull Tracer tracer, @Nonnull Span span, @Nonnull ApiTracerFactory.OperationType operationType) {
        this.tracer = (Tracer) Preconditions.checkNotNull(tracer, "tracer can't be null");
        this.span = (Span)Preconditions.checkNotNull(span, "span can't be null");
        this.operationType = (ApiTracerFactory.OperationType)Preconditions.checkNotNull(operationType, "operationType can't be null");
    }

    Span getSpan() {
        return this.span;
    }

    public ApiTracer.Scope inScope() {
        final io.opentelemetry.context.Scope scope = span.makeCurrent();
        return new ApiTracer.Scope() {
            public void close() {
                scope.close();
            }
        };
    }

    public void operationSucceeded() {
        Attributes attributes = this.baseOperationAttributes().build();
        this.span.setAllAttributes(attributes);
        this.span.end();
    }

    public void operationCancelled() {
        Attributes attributes = this.baseOperationAttributes().build();
        this.span.setAllAttributes(attributes);
        span.setStatus(io.opentelemetry.api.trace.StatusCode.UNSET, "Cancelled by caller");
        this.span.end();
    }

    public void operationFailed(Throwable error) {
        Attributes attributes = this.baseOperationAttributes().build();
        this.span.setAllAttributes(attributes);
        populateError(error);
        span.end();
    }

    public void lroStartFailed(Throwable error) {
        this.populateError(error);
        this.span.addEvent("Operation failed to start");
    }

    public void lroStartSucceeded() {
        this.span.addEvent("Operation started");
    }

    public void connectionSelected(String id) {
        this.lastConnectionId = id;
    }

    public void attemptStarted(int attemptNumber) {
        this.currentAttemptId = (long)attemptNumber;
        this.attemptSentMessages.set(0L);
        this.attemptReceivedMessages = 0L;
    }

    public void attemptStarted(Object request, int attemptNumber) {
        this.attemptStarted(attemptNumber);
    }

    public void attemptSucceeded() {
        Attributes attributes = this.baseAttemptAttributes().build();
        if (this.operationType == ApiTracerFactory.OperationType.LongRunning) {
            this.span.addEvent("Polling completed", attributes);
        } else {
            this.span.addEvent("Attempt succeeded", attributes);
        }

    }

    public void attemptCancelled() {
        Attributes attributes = this.baseAttemptAttributes().build();
        if (this.operationType == ApiTracerFactory.OperationType.LongRunning) {
            this.span.addEvent("Polling was cancelled", attributes);
        } else {
            this.span.addEvent("Attempt cancelled", attributes);
        }

        this.lastConnectionId = null;
    }

    public void attemptFailed(Throwable error, Duration delay) {
        this.populateError(error);
        AttributesBuilder attributes = this.baseAttemptAttributes();
        attributes.put("delay(ms)", delay.toMillis());
        if (this.operationType == ApiTracerFactory.OperationType.LongRunning) {
            this.span.addEvent("Scheduling next poll", attributes.build());
        } else {
            this.span.addEvent("Attempt failed, scheduling next attempt", attributes.build());
        }

        this.lastConnectionId = null;
    }

    public void attemptFailedRetriesExhausted(Throwable error) {
        this.populateError(error);
        Attributes attributes = this.baseAttemptAttributes().build();
        if (this.operationType == ApiTracerFactory.OperationType.LongRunning) {
            this.span.addEvent("Polling attempts exhausted", attributes);
        } else {
            this.span.addEvent("Attempts exhausted", attributes);
        }

        this.lastConnectionId = null;
    }

    public void attemptPermanentFailure(Throwable error) {
        this.populateError(error);
        Attributes attributes = this.baseAttemptAttributes().build();
        if (this.operationType == ApiTracerFactory.OperationType.LongRunning) {
            this.span.addEvent("Polling failed", attributes);
        } else {
            this.span.addEvent("Attempt failed, error not retryable", attributes);
        }

        this.lastConnectionId = null;
    }

    public void responseReceived() {
        ++this.attemptReceivedMessages;
        ++this.totalReceivedMessages;
    }

    public void requestSent() {
        this.attemptSentMessages.incrementAndGet();
        this.totalSentMessages.incrementAndGet();
    }

    public void batchRequestSent(long elementCount, long requestSize) {
        this.span.setAttribute("batch count", elementCount);
        this.span.setAttribute("batch size", requestSize);
    }

    private AttributesBuilder baseOperationAttributes() {
        AttributesBuilder attributes = Attributes.builder();
        attributes.put("attempt count", this.currentAttemptId + 1L);
        long localTotalSentMessages = this.totalSentMessages.get();
        if (localTotalSentMessages > 0L) {
            attributes.put("total request count", localTotalSentMessages);
        }

        if (this.totalReceivedMessages > 0L) {
            attributes.put("total response count", this.totalReceivedMessages);
        }

        return attributes;
    }

    private AttributesBuilder baseAttemptAttributes() {
        AttributesBuilder attributes = Attributes.builder();
        this.populateAttemptNumber(attributes);
        long localAttemptSentMessages = this.attemptSentMessages.get();
        if (localAttemptSentMessages > 0L) {
            attributes.put("attempt request count", localAttemptSentMessages);
        }

        if (this.attemptReceivedMessages > 0L) {
            attributes.put("attempt response count", this.attemptReceivedMessages);
        }

        String localLastConnectionId = this.lastConnectionId;
        if (localLastConnectionId != null) {
            attributes.put("connection", localLastConnectionId);
        }

        return attributes;
    }

    private void populateAttemptNumber(AttributesBuilder attributes) {
        attributes.put("attempt", this.currentAttemptId);
    }

    private void populateError(Throwable error) {
        if (error == null) {
            this.span.setStatus(io.opentelemetry.api.trace.StatusCode.OK);
        } else {
            this.span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, error.getMessage());
            this.span.recordException(error, Attributes.builder()
                        .put("exception.message", error.getMessage())
                        .put("exception.type", error.getClass().getName())
                        .put("exception.stacktrace", Throwables.getStackTraceAsString(error))
                        .build());
        }
    }
}
