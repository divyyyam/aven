package com.divyam.aven.ledger.api;

import com.divyam.aven.ledger.domain.EntryDirection;
import com.divyam.aven.ledger.domain.LedgerEntry;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EntryResponse(
        UUID id,
        UUID transactionId,
        UUID accountId,
        BigDecimal amount,
        EntryDirection direction,
        Instant createdAt) {

    static EntryResponse from(LedgerEntry entry) {
        return new EntryResponse(entry.getId(), entry.getTransactionId(), entry.getAccountId(),
                entry.getAmount(), entry.getDirection(), entry.getCreatedAt());
    }
}
