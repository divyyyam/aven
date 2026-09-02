package com.divyam.aven.settlement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "processed_events")
public class ProcessedEvent {
    @Id @Column(name = "event_id") private UUID eventId;
    @Column(name = "consumed_at", nullable = false) private Instant consumedAt;
    protected ProcessedEvent() { }
    public ProcessedEvent(UUID eventId) { this.eventId = eventId; this.consumedAt = Instant.now(); }
}
