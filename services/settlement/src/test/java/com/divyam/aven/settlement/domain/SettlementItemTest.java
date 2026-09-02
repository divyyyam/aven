package com.divyam.aven.settlement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SettlementItemTest {
    @Test
    void failureSchedulesRetryAndRecordsReason() {
        SettlementItem item = new SettlementItem(UUID.randomUUID(), "trace-1");
        item.scheduleRetry("provider unavailable", Duration.ofSeconds(2));

        assertThat(item.getStatus()).isEqualTo(SettlementStatus.RETRY_PENDING);
        assertThat(item.getRetryCount()).isEqualTo(1);
        assertThat(item.getLastError()).isEqualTo("provider unavailable");
    }

    @Test
    void successClearsPreviousFailure() {
        SettlementItem item = new SettlementItem(UUID.randomUUID(), "trace-1");
        item.scheduleRetry("temporary", Duration.ZERO);
        item.markSettled();

        assertThat(item.getStatus()).isEqualTo(SettlementStatus.SETTLED);
        assertThat(item.getLastError()).isNull();
    }
}
