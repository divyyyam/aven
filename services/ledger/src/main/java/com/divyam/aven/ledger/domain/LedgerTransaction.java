package com.divyam.aven.ledger.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ledger_transactions")
public class LedgerTransaction {

    @Id
    private UUID id;

    @Column(name = "idempotency_key", nullable = false, unique = true, updatable = false)
    private String idempotencyKey;

    @Column(name = "reversed_transaction_id", updatable = false)
    private UUID reversedTransactionId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LedgerEntry> entries = new ArrayList<>();

    protected LedgerTransaction() {
        // Required by JPA.
    }

    private LedgerTransaction(String idempotencyKey, UUID reversedTransactionId) {
        this.id = UUID.randomUUID();
        this.idempotencyKey = idempotencyKey;
        this.reversedTransactionId = reversedTransactionId;
        this.createdAt = Instant.now();
    }

    public static LedgerTransaction create(String idempotencyKey) {
        return new LedgerTransaction(idempotencyKey, null);
    }

    public static LedgerTransaction reversalOf(String idempotencyKey, UUID originalTransactionId) {
        return new LedgerTransaction(idempotencyKey, originalTransactionId);
    }

    public void addEntry(UUID accountId, java.math.BigDecimal amount, EntryDirection direction) {
        entries.add(new LedgerEntry(this, accountId, amount, direction));
    }

    public UUID getId() { return id; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public UUID getReversedTransactionId() { return reversedTransactionId; }
    public Instant getCreatedAt() { return createdAt; }
    public List<LedgerEntry> getEntries() { return List.copyOf(entries); }
}
