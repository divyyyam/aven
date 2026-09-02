package com.divyam.aven.events;
import java.util.UUID;
public record TransactionSettledEvent(UUID transactionId) { }
