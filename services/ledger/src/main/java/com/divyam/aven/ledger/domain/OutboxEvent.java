package com.divyam.aven.ledger.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox")
public class OutboxEvent {
    @Id private UUID id;
    @Column(name = "aggregate_id", nullable = false) private UUID aggregateId;
    @Column(name = "event_type", nullable = false) private String eventType;
    @Column(name = "payload_json", nullable = false, columnDefinition = "jsonb") private String payloadJson;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @Column(name = "published_at") private Instant publishedAt;
    protected OutboxEvent() { }
    public OutboxEvent(UUID aggregateId, String eventType, String payloadJson) { this.id = UUID.randomUUID(); this.aggregateId = aggregateId; this.eventType = eventType; this.payloadJson = payloadJson; this.createdAt = Instant.now(); }
    public UUID getId() { return id; }
    public UUID getAggregateId() { return aggregateId; }
    public String getEventType() { return eventType; }
    public String getPayloadJson() { return payloadJson; }
    public boolean isPublished() { return publishedAt != null; }
    public void markPublished() { publishedAt = Instant.now(); }
}
