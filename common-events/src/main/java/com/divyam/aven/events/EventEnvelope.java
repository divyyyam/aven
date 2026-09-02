package com.divyam.aven.events;

import java.time.Instant;
import java.util.UUID;

public record EventEnvelope<T>(UUID eventId, UUID aggregateId, Instant occurredAt, String traceId, String type, int version, T payload) {
    public static <T> EventEnvelope<T> of(UUID aggregateId, String traceId, String type, T payload) {
        return new EventEnvelope<>(UUID.randomUUID(), aggregateId, Instant.now(), traceId, type, 1, payload);
    }
}
