package com.divyam.aven.ledger.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false, updatable = false)
    private LedgerTransaction transaction;

    @Column(name = "account_id", nullable = false, updatable = false)
    private UUID accountId;

    @Column(nullable = false, precision = 19, scale = 4, updatable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private EntryDirection direction;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected LedgerEntry() {
        // Required by JPA.
    }

    LedgerEntry(LedgerTransaction transaction, UUID accountId, BigDecimal amount, EntryDirection direction) {
        this.id = UUID.randomUUID();
        this.transaction = transaction;
        this.accountId = accountId;
        this.amount = amount;
        this.direction = direction;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getTransactionId() { return transaction.getId(); }
    public UUID getAccountId() { return accountId; }
    public BigDecimal getAmount() { return amount; }
    public EntryDirection getDirection() { return direction; }
    public Instant getCreatedAt() { return createdAt; }
}
