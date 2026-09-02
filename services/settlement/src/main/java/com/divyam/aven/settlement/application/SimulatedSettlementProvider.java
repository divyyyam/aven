package com.divyam.aven.settlement.application;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimulatedSettlementProvider implements SettlementProvider {
    private final double failureRate;

    public SimulatedSettlementProvider(@Value("${aven.settlement.failure-rate:0.20}") double failureRate) {
        if (failureRate < 0 || failureRate > 1) {
            throw new IllegalArgumentException("Settlement failure rate must be between 0 and 1");
        }
        this.failureRate = failureRate;
    }

    @Override
    public void settle(UUID transactionId) {
        if (ThreadLocalRandom.current().nextDouble() < failureRate) {
            throw new SettlementProviderException("Simulated settlement provider failure for " + transactionId);
        }
    }
}
