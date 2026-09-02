package com.divyam.aven.reconciliation.application;

import com.divyam.aven.reconciliation.repository.ReplayedBalanceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReconciliationScheduler {
    private final ReplayedBalanceRepository balances;
    private final AccountBalanceClient accountClient;
    private final DriftService driftService;

    public ReconciliationScheduler(ReplayedBalanceRepository balances, AccountBalanceClient accountClient, DriftService driftService) {
        this.balances = balances;
        this.accountClient = accountClient;
        this.driftService = driftService;
    }

    @Scheduled(fixedDelayString = "${aven.reconciliation.interval-ms:5000}")
    public void reconcile() {
        balances.findAll().forEach(balance -> driftService.compare(
                balance.getAccountId(), balance.getComputedBalance(), accountClient.fetch(balance.getAccountId())));
    }
}
