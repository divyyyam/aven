package com.divyam.aven.reconciliation.api;

import com.divyam.aven.reconciliation.domain.ReconciliationDrift;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReconciliationDriftResponse(
        UUID id,
        UUID accountId,
        BigDecimal expectedBalance,
        BigDecimal observedBalance,
        Instant detectedAt,
        Instant resolvedAt) {
    public static ReconciliationDriftResponse from(ReconciliationDrift drift) {
        return new ReconciliationDriftResponse(drift.getId(), drift.getAccountId(),
                drift.getExpectedBalance(), drift.getObservedBalance(), drift.getDetectedAt(), drift.getResolvedAt());
    }
}
