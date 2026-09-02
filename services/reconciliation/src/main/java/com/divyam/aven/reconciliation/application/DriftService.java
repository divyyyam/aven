package com.divyam.aven.reconciliation.application;

import com.divyam.aven.events.EventEnvelope;
import com.divyam.aven.events.ReconciliationDriftDetectedEvent;
import com.divyam.aven.events.Topics;
import com.divyam.aven.reconciliation.domain.ReconciliationDrift;
import com.divyam.aven.reconciliation.domain.ReconciliationOutboxEvent;
import com.divyam.aven.reconciliation.repository.ReconciliationDriftRepository;
import com.divyam.aven.reconciliation.repository.ReconciliationOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriftService {
    private final ReconciliationDriftRepository drifts;
    private final ReconciliationOutboxRepository outbox;
    private final ObjectMapper objectMapper;

    public DriftService(ReconciliationDriftRepository drifts, ReconciliationOutboxRepository outbox, ObjectMapper objectMapper) {
        this.drifts = drifts;
        this.outbox = outbox;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void compare(UUID accountId, BigDecimal expected, BigDecimal observed) {
        var activeDrift = drifts.findByAccountIdAndResolvedAtIsNull(accountId);
        if (expected.compareTo(observed) == 0) {
            activeDrift.ifPresent(ReconciliationDrift::resolve);
            return;
        }
        if (activeDrift.isPresent()) return;

        ReconciliationDrift drift = drifts.save(new ReconciliationDrift(accountId, expected, observed));
        ReconciliationDriftDetectedEvent payload = new ReconciliationDriftDetectedEvent(
                accountId, expected, observed, expected.subtract(observed));
        try {
            String json = objectMapper.writeValueAsString(EventEnvelope.of(
                    accountId, "reconciliation-" + UUID.randomUUID(), "ReconciliationDriftDetected", payload));
            outbox.save(new ReconciliationOutboxEvent(drift.getId(), Topics.RECONCILIATION_DRIFT, json));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize reconciliation drift event", exception);
        }
    }
}
