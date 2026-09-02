package com.divyam.aven.events;

import java.math.BigDecimal;
import java.util.UUID;

public record ReconciliationDriftDetectedEvent(
        UUID accountId,
        BigDecimal expectedBalance,
        BigDecimal observedBalance,
        BigDecimal difference) {
}
