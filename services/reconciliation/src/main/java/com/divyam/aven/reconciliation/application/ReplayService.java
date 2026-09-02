package com.divyam.aven.reconciliation.application;

import com.divyam.aven.events.EventEnvelope;
import com.divyam.aven.events.TransactionCreatedEvent;
import com.divyam.aven.events.TransactionEntry;
import com.divyam.aven.reconciliation.domain.ProcessedEvent;
import com.divyam.aven.reconciliation.domain.ReplayedBalance;
import com.divyam.aven.reconciliation.repository.ProcessedEventRepository;
import com.divyam.aven.reconciliation.repository.ReplayedBalanceRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReplayService {
    private final ReplayedBalanceRepository balances;
    private final ProcessedEventRepository processedEvents;

    public ReplayService(ReplayedBalanceRepository balances, ProcessedEventRepository processedEvents) {
        this.balances = balances;
        this.processedEvents = processedEvents;
    }

    @Transactional
    public void apply(EventEnvelope<TransactionCreatedEvent> event) {
        if (processedEvents.existsById(event.eventId())) return;

        for (TransactionEntry entry : event.payload().entries()) {
            ReplayedBalance balance = balances.findById(entry.accountId())
                    .orElseGet(() -> new ReplayedBalance(entry.accountId()));
            balance.apply(signedAmount(entry), event.eventId());
            balances.save(balance);
        }
        processedEvents.save(new ProcessedEvent(event.eventId()));
    }

    private BigDecimal signedAmount(TransactionEntry entry) {
        return switch (entry.direction()) {
            case "DEBIT" -> entry.amount();
            case "CREDIT" -> entry.amount().negate();
            default -> throw new IllegalArgumentException("Unknown entry direction: " + entry.direction());
        };
    }
}
