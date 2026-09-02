package com.divyam.aven.settlement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "settlement_items")
public class SettlementItem {
    @Id private UUID id;
    @Column(name = "batch_id") private UUID batchId;
    @Column(name = "transaction_id", nullable = false, unique = true, updatable = false) private UUID transactionId;
    @Column(name = "trace_id", nullable = false, updatable = false) private String traceId;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SettlementStatus status;
    @Column(name = "retry_count", nullable = false) private int retryCount;
    @Column(name = "next_attempt_at", nullable = false) private Instant nextAttemptAt;
    @Column(name = "last_error") private String lastError;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;

    protected SettlementItem() { }

    public SettlementItem(UUID transactionId, String traceId) {
        this.id = UUID.randomUUID();
        this.transactionId = transactionId;
        this.traceId = traceId == null ? "" : traceId;
        this.status = SettlementStatus.PENDING;
        this.nextAttemptAt = Instant.now();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public void assignTo(UUID batchId) { this.batchId = batchId; this.updatedAt = Instant.now(); }
    public void markSettled() { this.status = SettlementStatus.SETTLED; this.lastError = null; this.updatedAt = Instant.now(); }

    public void scheduleRetry(String error, Duration delay) {
        retryCount++;
        status = SettlementStatus.RETRY_PENDING;
        lastError = safeError(error);
        nextAttemptAt = Instant.now().plus(delay);
        updatedAt = Instant.now();
    }

    public void markFailed(String error) {
        retryCount++;
        status = SettlementStatus.FAILED;
        lastError = safeError(error);
        updatedAt = Instant.now();
    }

    private String safeError(String error) {
        String value = error == null ? "Unknown settlement failure" : error;
        return value.substring(0, Math.min(value.length(), 500));
    }

    public UUID getId() { return id; }
    public UUID getBatchId() { return batchId; }
    public UUID getTransactionId() { return transactionId; }
    public String getTraceId() { return traceId; }
    public SettlementStatus getStatus() { return status; }
    public int getRetryCount() { return retryCount; }
    public Instant getNextAttemptAt() { return nextAttemptAt; }
    public String getLastError() { return lastError; }
}
