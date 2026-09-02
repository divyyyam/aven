package com.divyam.aven.settlement.api;

import com.divyam.aven.settlement.domain.SettlementItem;
import com.divyam.aven.settlement.domain.SettlementStatus;
import java.time.Instant;
import java.util.UUID;

public record SettlementItemResponse(
        UUID transactionId,
        UUID batchId,
        SettlementStatus status,
        int retryCount,
        Instant nextAttemptAt,
        String lastError) {
    public static SettlementItemResponse from(SettlementItem item) {
        return new SettlementItemResponse(item.getTransactionId(), item.getBatchId(), item.getStatus(),
                item.getRetryCount(), item.getNextAttemptAt(), item.getLastError());
    }
}
