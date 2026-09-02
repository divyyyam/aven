package com.divyam.aven.events;
import java.util.UUID;
public record TransactionSettlementFailedEvent(UUID transactionId, int attempts, String reason) { }
