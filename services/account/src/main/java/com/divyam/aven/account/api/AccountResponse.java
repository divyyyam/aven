package com.divyam.aven.account.api;

import com.divyam.aven.account.domain.Account;
import com.divyam.aven.account.domain.AccountStatus;
import java.time.Instant;
import java.util.UUID;

public record AccountResponse(UUID id, String ownerRef, AccountStatus status, Instant createdAt) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getId(), account.getOwnerRef(), account.getStatus(), account.getCreatedAt());
    }
}
