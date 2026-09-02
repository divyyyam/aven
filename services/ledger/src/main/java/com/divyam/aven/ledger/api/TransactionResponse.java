package com.divyam.aven.ledger.api;

import com.divyam.aven.ledger.domain.LedgerTransaction;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID reversedTransactionId,
        UUID reversedByTransactionId,
        Instant createdAt,
        List<EntryResponse> entries) {

    public static TransactionResponse from(LedgerTransaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getReversedTransactionId(),
                transaction.getReversedByTransactionId(),
                transaction.getCreatedAt(),
                transaction.getEntries().stream().map(EntryResponse::from).toList());
    }
}
