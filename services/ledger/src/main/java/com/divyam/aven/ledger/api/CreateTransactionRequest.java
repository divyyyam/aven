package com.divyam.aven.ledger.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateTransactionRequest(
        @NotEmpty @Size(min = 2) List<@Valid EntryRequest> entries) {
}
