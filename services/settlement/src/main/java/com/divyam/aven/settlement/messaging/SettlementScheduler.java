package com.divyam.aven.settlement.messaging;

import com.divyam.aven.settlement.application.SettlementBatchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SettlementScheduler {
    private final SettlementBatchService batchService;

    public SettlementScheduler(SettlementBatchService batchService) {
        this.batchService = batchService;
    }

    @Scheduled(fixedDelayString = "${aven.settlement.poll-delay-ms:1000}")
    public void processDueItems() {
        batchService.processDueItems();
    }
}
