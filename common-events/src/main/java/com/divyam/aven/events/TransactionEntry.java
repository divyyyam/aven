package com.divyam.aven.events;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionEntry(UUID accountId, BigDecimal amount, String direction) { }
