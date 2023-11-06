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

import com.google.api.core.InternalApi;
import com.google.api.gax.tracing.ApiTracer;
import com.google.api.gax.tracing.BaseApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;

import javax.annotation.Nonnull;

/**
 * A {@link com.google.api.gax.tracing.ApiTracerFactory} to build instances of {@link OpenTelemetryTracer}.
 *
 * <p>This class wraps the {@link Tracer} provided by OpenTelemetry in {@code Tracing.getTracer()}. It
 * will be used to create new spans and wrap them in {@link OpenTelemetryTracer}.
 *
 * <p>This class is thread safe.
 */
@InternalApi("For google-cloud-java client use only")
public final class OpenTelemetryTracerFactory extends BaseApiTracerFactory {
    @Nonnull private final Tracer internalTracer;
    @Nonnull private final Attributes spanAttributes;

    /**
     * Instantiates a new instance capturing the {@link io.opentelemetry.api.trace.Tracer} in {@code
     * Tracing.getTracer}.
     */
    public OpenTelemetryTracerFactory() {
        this(Attributes.empty());
    }

    /**
     * Instantiates a new instance capturing the {@link io.opentelemetry.api.trace.Tracer} in {@code
     * Tracing.getTracer}. It will also override the service name of the grpc stub with a custom
     * client name. This is useful disambiguate spans created outer manual written wrappers and around
     * generated gapic spans.
     *
     * @param spanAttributes the attributes to stamp on every span. Should include things like library
     *     version.
     */
    public OpenTelemetryTracerFactory(Attributes spanAttributes) {
        this(GlobalOpenTelemetry.getTracer("instrumentationScopeName"), spanAttributes);
    }

    /**
     * Instantiates a new instance with an explicit {@link io.opentelemetry.api.trace.Tracer}. It will also
     * override the service name of the grpc stub with a custom client name. This is useful
     * disambiguate spans created outer manual written wrappers and around generated gapic spans.
     *
     * @param internalTracer the OpenTelemetry tracer to wrap.
     */
    @InternalApi("Visible for testing")
    OpenTelemetryTracerFactory(Tracer internalTracer, @Nonnull Attributes spanAttributes) {
        this.internalTracer =
                Preconditions.checkNotNull(internalTracer, "internalTracer can't be null");
        this.spanAttributes = spanAttributes;
    }

    /** {@inheritDoc } */
    @Override
    public ApiTracer newTracer(ApiTracer parent, SpanName spanName, OperationType operationType) {
        // Default to the current in context span. This is used for outermost tracers that inherit
        // the caller's parent span.
        Span parentSpan = Span.current();

        // If an outer callable started a span, use it as the parent.
        if (parent instanceof OpenTelemetryTracer) {
            parentSpan = ((OpenTelemetryTracer) parent).getSpan();
        }

        Span span = internalTracer.spanBuilder(spanName.toString()).setParent(Context.current().with(parentSpan)).startSpan();

        span.setAllAttributes(spanAttributes);

        return new OpenTelemetryTracer(internalTracer, span, operationType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OpenTelemetryTracerFactory that = (OpenTelemetryTracerFactory) o;
        return Objects.equal(internalTracer, that.internalTracer)
                && Objects.equal(spanAttributes, that.spanAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(internalTracer, spanAttributes);
    }
}