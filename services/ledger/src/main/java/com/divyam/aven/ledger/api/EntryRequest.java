package com.divyam.aven.ledger.api;

import com.divyam.aven.ledger.domain.EntryDirection;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record EntryRequest(
        @NotNull UUID accountId,
        @NotNull @DecimalMin(value = "0.0001") BigDecimal amount,
        @NotNull EntryDirection direction) {
}
