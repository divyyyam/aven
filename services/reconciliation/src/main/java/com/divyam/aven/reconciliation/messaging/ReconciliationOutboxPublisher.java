package com.divyam.aven.reconciliation.messaging;

import com.divyam.aven.reconciliation.domain.ReconciliationOutboxEvent;
import com.divyam.aven.reconciliation.repository.ReconciliationOutboxRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ReconciliationOutboxPublisher {
    private final ReconciliationOutboxRepository outbox;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ReconciliationOutboxPublisher(
            ReconciliationOutboxRepository outbox,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.outbox = outbox;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${aven.outbox.poll-delay-ms:1000}")
    @Transactional
    public void publishPending() {
        List<ReconciliationOutboxEvent> events = outbox.findByPublishedAtIsNullOrderByCreatedAt(PageRequest.of(0, 100));
        for (ReconciliationOutboxEvent event : events) {
            kafkaTemplate.send(event.getTopic(), event.getAggregateId().toString(), event.getPayloadJson()).join();
            event.markPublished();
        }
    }
}
