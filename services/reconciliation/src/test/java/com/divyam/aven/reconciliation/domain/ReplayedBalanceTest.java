package com.divyam.aven.reconciliation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReplayedBalanceTest {
    @Test
    void replayAccumulatesSignedDeltas() {
        ReplayedBalance balance = new ReplayedBalance(UUID.randomUUID());
        balance.apply(new BigDecimal("40.0000"), UUID.randomUUID());
        balance.apply(new BigDecimal("-12.5000"), UUID.randomUUID());
        assertThat(balance.getComputedBalance()).isEqualByComparingTo("27.5000");
    }
}
