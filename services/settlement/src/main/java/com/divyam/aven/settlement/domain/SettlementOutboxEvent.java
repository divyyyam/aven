package com.divyam.aven.settlement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "settlement_outbox")
public class SettlementOutboxEvent {
    @Id private UUID id;
    @Column(name = "aggregate_id", nullable = false) private UUID aggregateId;
    @Column(nullable = false) private String topic;
    @Column(name = "payload_json", nullable = false, columnDefinition = "jsonb") private String payloadJson;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @Column(name = "published_at") private Instant publishedAt;
    protected SettlementOutboxEvent() { }
    public SettlementOutboxEvent(UUID aggregateId, String topic, String payloadJson) { this.id = UUID.randomUUID(); this.aggregateId = aggregateId; this.topic = topic; this.payloadJson = payloadJson; this.createdAt = Instant.now(); }
    public UUID getAggregateId() { return aggregateId; }
    public String getTopic() { return topic; }
    public String getPayloadJson() { return payloadJson; }
    public void markPublished() { publishedAt = Instant.now(); }
}
