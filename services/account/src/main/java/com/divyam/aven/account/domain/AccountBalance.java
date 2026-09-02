package com.divyam.aven.account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "account_balances")
public class AccountBalance {
    @Id
    @Column(name = "account_id")
    private UUID accountId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(name = "last_event_id")
    private UUID lastEventId;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected AccountBalance() {
    }

    public AccountBalance(UUID accountId) {
        this.accountId = accountId;
        this.balance = BigDecimal.ZERO;
        this.updatedAt = Instant.now();
    }

    public void apply(BigDecimal delta, UUID eventId) {
        balance = balance.add(delta);
        lastEventId = eventId;
        updatedAt = Instant.now();
    }

    public void forceBalanceForDemo(BigDecimal value) {
        balance = value;
        updatedAt = Instant.now();
    }

    public UUID getAccountId() { return accountId; }
    public BigDecimal getBalance() { return balance; }
    public UUID getLastEventId() { return lastEventId; }
    public Instant getUpdatedAt() { return updatedAt; }
}
