package com.divyam.aven.account.api;

import com.divyam.aven.account.domain.AccountBalance;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BalanceResponse(UUID accountId, BigDecimal balance, UUID lastEventId, Instant updatedAt) {
    public static BalanceResponse from(AccountBalance balance) {
        return new BalanceResponse(balance.getAccountId(), balance.getBalance(), balance.getLastEventId(), balance.getUpdatedAt());
    }
}
