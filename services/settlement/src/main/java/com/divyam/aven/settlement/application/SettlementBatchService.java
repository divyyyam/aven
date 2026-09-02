package com.divyam.aven.settlement.application;

import com.divyam.aven.events.EventEnvelope;
import com.divyam.aven.events.Topics;
import com.divyam.aven.events.TransactionSettledEvent;
import com.divyam.aven.events.TransactionSettlementFailedEvent;
import com.divyam.aven.settlement.domain.SettlementBatch;
import com.divyam.aven.settlement.domain.SettlementItem;
import com.divyam.aven.settlement.domain.SettlementOutboxEvent;
import com.divyam.aven.settlement.domain.SettlementStatus;
import com.divyam.aven.settlement.repository.SettlementBatchRepository;
import com.divyam.aven.settlement.repository.SettlementItemRepository;
import com.divyam.aven.settlement.repository.SettlementOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettlementBatchService {
    private final SettlementItemRepository items;
    private final SettlementBatchRepository batches;
    private final SettlementOutboxRepository outbox;
    private final SettlementProvider provider;
    private final ObjectMapper objectMapper;
    private final int batchSize;
    private final int maxAttempts;
    private final Duration initialBackoff;

    public SettlementBatchService(
            SettlementItemRepository items,
            SettlementBatchRepository batches,
            SettlementOutboxRepository outbox,
            SettlementProvider provider,
            ObjectMapper objectMapper,
            @Value("${aven.settlement.batch-size:100}") int batchSize,
            @Value("${aven.settlement.max-attempts:3}") int maxAttempts,
            @Value("${aven.settlement.initial-backoff-ms:1000}") long initialBackoffMs) {
        this.items = items;
        this.batches = batches;
        this.outbox = outbox;
        this.provider = provider;
        this.objectMapper = objectMapper;
        this.batchSize = batchSize;
        this.maxAttempts = maxAttempts;
        this.initialBackoff = Duration.ofMillis(initialBackoffMs);
    }

    @Transactional
    public void processDueItems() {
        List<SettlementItem> dueItems = items.findByStatusInAndNextAttemptAtLessThanEqualOrderByNextAttemptAt(
                List.of(SettlementStatus.PENDING, SettlementStatus.RETRY_PENDING),
                Instant.now(), PageRequest.of(0, batchSize));
        if (dueItems.isEmpty()) return;

        SettlementBatch batch = batches.save(new SettlementBatch());
        dueItems.forEach(item -> process(item, batch));
        batch.complete();
    }

    private void process(SettlementItem item, SettlementBatch batch) {
        item.assignTo(batch.getId());
        try {
            provider.settle(item.getTransactionId());
            item.markSettled();
            enqueue(item, Topics.TRANSACTION_SETTLED,
                    "TransactionSettled", new TransactionSettledEvent(item.getTransactionId()));
        } catch (SettlementProviderException exception) {
            if (item.getRetryCount() + 1 >= maxAttempts) {
                item.markFailed(exception.getMessage());
                TransactionSettlementFailedEvent failure = new TransactionSettlementFailedEvent(
                        item.getTransactionId(), item.getRetryCount(), exception.getMessage());
                enqueue(item, Topics.TRANSACTION_FAILED, "TransactionSettlementFailed", failure);
                enqueue(item, Topics.SETTLEMENT_DLQ, "TransactionSettlementFailed", failure);
            } else {
                long multiplier = 1L << item.getRetryCount();
                item.scheduleRetry(exception.getMessage(), initialBackoff.multipliedBy(multiplier));
            }
        }
    }

    private void enqueue(SettlementItem item, String topic, String eventType, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(EventEnvelope.of(
                    item.getTransactionId(), item.getTraceId(), eventType, payload));
            outbox.save(new SettlementOutboxEvent(item.getTransactionId(), topic, json));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize settlement event", exception);
        }
    }
}
