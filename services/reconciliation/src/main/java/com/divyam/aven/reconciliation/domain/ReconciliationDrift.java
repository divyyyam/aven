package com.divyam.aven.reconciliation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reconciliation_drifts")
public class ReconciliationDrift {
    @Id private UUID id;
    @Column(name = "account_id", nullable = false) private UUID accountId;
    @Column(name = "expected_balance", nullable = false, precision = 19, scale = 4) private BigDecimal expectedBalance;
    @Column(name = "observed_balance", nullable = false, precision = 19, scale = 4) private BigDecimal observedBalance;
    @Column(name = "detected_at", nullable = false) private Instant detectedAt;
    @Column(name = "resolved_at") private Instant resolvedAt;

    protected ReconciliationDrift() { }
    public ReconciliationDrift(UUID accountId, BigDecimal expected, BigDecimal observed) { this.id = UUID.randomUUID(); this.accountId = accountId; this.expectedBalance = expected; this.observedBalance = observed; this.detectedAt = Instant.now(); }
    public void resolve() { resolvedAt = Instant.now(); }
    public UUID getId() { return id; }
    public UUID getAccountId() { return accountId; }
    public BigDecimal getExpectedBalance() { return expectedBalance; }
    public BigDecimal getObservedBalance() { return observedBalance; }
    public Instant getDetectedAt() { return detectedAt; }
    public Instant getResolvedAt() { return resolvedAt; }
}
