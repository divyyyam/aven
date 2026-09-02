package com.divyam.aven.settlement.messaging;

import com.divyam.aven.settlement.domain.SettlementOutboxEvent;
import com.divyam.aven.settlement.repository.SettlementOutboxRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SettlementOutboxPublisher {
    private final SettlementOutboxRepository outbox;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public SettlementOutboxPublisher(
            SettlementOutboxRepository outbox,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.outbox = outbox;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${aven.outbox.poll-delay-ms:1000}")
    @Transactional
    public void publishPending() {
        List<SettlementOutboxEvent> events = outbox.findByPublishedAtIsNullOrderByCreatedAt(PageRequest.of(0, 100));
        for (SettlementOutboxEvent event : events) {
            kafkaTemplate.send(event.getTopic(), event.getAggregateId().toString(), event.getPayloadJson()).join();
            event.markPublished();
        }
    }
}
