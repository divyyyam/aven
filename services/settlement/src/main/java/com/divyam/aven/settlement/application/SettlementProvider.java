package com.divyam.aven.settlement.application;

import java.util.UUID;

public interface SettlementProvider {
    void settle(UUID transactionId);
}
