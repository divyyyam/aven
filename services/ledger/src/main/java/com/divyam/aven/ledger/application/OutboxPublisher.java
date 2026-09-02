package com.divyam.aven.ledger.application;

import com.divyam.aven.events.Topics;
import com.divyam.aven.ledger.domain.OutboxEvent;
import com.divyam.aven.ledger.repository.OutboxEventRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxPublisher {
    private final OutboxEventRepository outbox; private final KafkaTemplate<String, String> kafka;
    public OutboxPublisher(OutboxEventRepository outbox, KafkaTemplate<String, String> kafka) { this.outbox = outbox; this.kafka = kafka; }
    @Scheduled(fixedDelayString = "${aven.outbox.poll-delay-ms:1000}")
    @Transactional
    public void publishPending() {
        List<OutboxEvent> events = outbox.findByPublishedAtIsNullOrderByCreatedAtAsc(PageRequest.of(0, 100));
        for (OutboxEvent event : events) {
            kafka.send(Topics.TRANSACTION_CREATED, event.getAggregateId().toString(), event.getPayloadJson()).join();
            event.markPublished();
        }
    }
}
