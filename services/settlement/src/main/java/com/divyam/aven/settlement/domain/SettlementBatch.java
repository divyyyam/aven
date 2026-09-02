package com.divyam.aven.settlement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "settlement_batches")
public class SettlementBatch {
    @Id private UUID id;
    @Column(nullable = false) private String status;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @Column(name = "completed_at") private Instant completedAt;

    public SettlementBatch() {
        id = UUID.randomUUID();
        status = "PROCESSING";
        createdAt = Instant.now();
    }

    public void complete() { status = "COMPLETED"; completedAt = Instant.now(); }
    public UUID getId() { return id; }
}
