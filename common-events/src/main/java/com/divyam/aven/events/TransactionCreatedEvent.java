package com.divyam.aven.events;

import java.util.List;
import java.util.UUID;

public record TransactionCreatedEvent(UUID transactionId, UUID reversalOfTransactionId, List<TransactionEntry> entries) { }
