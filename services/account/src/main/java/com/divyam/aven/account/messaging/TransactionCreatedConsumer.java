package com.divyam.aven.account.messaging;

import com.divyam.aven.account.application.BalanceProjectionService;
import com.divyam.aven.events.EventEnvelope;
import com.divyam.aven.events.Topics;
import com.divyam.aven.events.TransactionCreatedEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionCreatedConsumer {
    private static final TypeReference<EventEnvelope<TransactionCreatedEvent>> EVENT_TYPE = new TypeReference<>() { };

    private final ObjectMapper objectMapper;
    private final BalanceProjectionService projectionService;

    public TransactionCreatedConsumer(ObjectMapper objectMapper, BalanceProjectionService projectionService) {
        this.objectMapper = objectMapper;
        this.projectionService = projectionService;
    }

    @KafkaListener(topics = Topics.TRANSACTION_CREATED, groupId = "account-projection")
    public void consume(String rawEvent) throws Exception {
        EventEnvelope<TransactionCreatedEvent> event = objectMapper.readValue(rawEvent, EVENT_TYPE);
        try (MDC.MDCCloseable ignored = MDC.putCloseable("traceId", event.traceId())) {
            projectionService.apply(event);
        }
    }
}
