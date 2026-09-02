package com.divyam.aven.settlement.application;

import com.divyam.aven.events.EventEnvelope;
import com.divyam.aven.events.TransactionCreatedEvent;
import com.divyam.aven.settlement.domain.ProcessedEvent;
import com.divyam.aven.settlement.domain.SettlementItem;
import com.divyam.aven.settlement.repository.ProcessedEventRepository;
import com.divyam.aven.settlement.repository.SettlementItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettlementIntakeService {
    private final SettlementItemRepository items;
    private final ProcessedEventRepository processedEvents;

    public SettlementIntakeService(SettlementItemRepository items, ProcessedEventRepository processedEvents) {
        this.items = items;
        this.processedEvents = processedEvents;
    }

    @Transactional
    public void accept(EventEnvelope<TransactionCreatedEvent> event) {
        if (processedEvents.existsById(event.eventId())) return;

        items.findByTransactionId(event.payload().transactionId())
                .orElseGet(() -> items.save(new SettlementItem(
                        event.payload().transactionId(), event.traceId())));
        processedEvents.save(new ProcessedEvent(event.eventId()));
    }
}
