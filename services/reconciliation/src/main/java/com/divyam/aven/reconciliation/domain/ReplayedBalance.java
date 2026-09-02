package com.divyam.aven.reconciliation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "replayed_balances")
public class ReplayedBalance {
    @Id @Column(name = "account_id") private UUID accountId;
    @Column(name = "computed_balance", nullable = false, precision = 19, scale = 4) private BigDecimal computedBalance;
    @Column(name = "last_event_id") private UUID lastEventId;
    @Column(name = "computed_at", nullable = false) private Instant computedAt;

    protected ReplayedBalance() { }
    public ReplayedBalance(UUID accountId) { this.accountId = accountId; this.computedBalance = BigDecimal.ZERO; this.computedAt = Instant.now(); }
    public void apply(BigDecimal delta, UUID eventId) { computedBalance = computedBalance.add(delta); lastEventId = eventId; computedAt = Instant.now(); }
    public UUID getAccountId() { return accountId; }
    public BigDecimal getComputedBalance() { return computedBalance; }
    public UUID getLastEventId() { return lastEventId; }
}
