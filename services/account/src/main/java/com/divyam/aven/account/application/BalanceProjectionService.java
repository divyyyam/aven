package com.divyam.aven.account.application;

import com.divyam.aven.account.domain.AccountBalance;
import com.divyam.aven.account.domain.ProcessedEvent;
import com.divyam.aven.account.repository.AccountBalanceRepository;
import com.divyam.aven.account.repository.ProcessedEventRepository;
import com.divyam.aven.events.EventEnvelope;
import com.divyam.aven.events.TransactionCreatedEvent;
import com.divyam.aven.events.TransactionEntry;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BalanceProjectionService {
    private final AccountBalanceRepository balances;
    private final ProcessedEventRepository processedEvents;

    public BalanceProjectionService(
            AccountBalanceRepository balances,
            ProcessedEventRepository processedEvents) {
        this.balances = balances;
        this.processedEvents = processedEvents;
    }

    @Transactional
    public void apply(EventEnvelope<TransactionCreatedEvent> event) {
        if (processedEvents.existsById(event.eventId())) {
            return;
        }

        for (TransactionEntry entry : event.payload().entries()) {
            AccountBalance balance = balances.findById(entry.accountId())
                    .orElseThrow(() -> new AccountNotFoundException(entry.accountId()));
            balance.apply(signedAmount(entry), event.eventId());
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
