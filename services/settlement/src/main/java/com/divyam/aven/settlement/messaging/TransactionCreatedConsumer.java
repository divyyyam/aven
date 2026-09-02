package com.divyam.aven.settlement.messaging;

import com.divyam.aven.events.EventEnvelope;
import com.divyam.aven.events.Topics;
import com.divyam.aven.events.TransactionCreatedEvent;
import com.divyam.aven.settlement.application.SettlementIntakeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionCreatedConsumer {
    private static final TypeReference<EventEnvelope<TransactionCreatedEvent>> EVENT_TYPE = new TypeReference<>() { };
    private final ObjectMapper objectMapper;
    private final SettlementIntakeService intakeService;

    public TransactionCreatedConsumer(ObjectMapper objectMapper, SettlementIntakeService intakeService) {
        this.objectMapper = objectMapper;
        this.intakeService = intakeService;
    }

    @KafkaListener(topics = Topics.TRANSACTION_CREATED, groupId = "settlement")
    public void consume(String rawEvent) throws Exception {
        EventEnvelope<TransactionCreatedEvent> event = objectMapper.readValue(rawEvent, EVENT_TYPE);
        try (MDC.MDCCloseable ignored = MDC.putCloseable("traceId", event.traceId())) {
            intakeService.accept(event);
        }
    }
}
