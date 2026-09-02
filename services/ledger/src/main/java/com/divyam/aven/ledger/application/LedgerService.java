package com.divyam.aven.ledger.application;

import com.divyam.aven.ledger.api.CreateTransactionRequest;
import com.divyam.aven.ledger.api.EntryRequest;
import com.divyam.aven.ledger.domain.LedgerEntry;
import com.divyam.aven.ledger.domain.LedgerTransaction;
import com.divyam.aven.ledger.domain.LedgerValidationException;
import com.divyam.aven.ledger.domain.TransactionNotFoundException;
import com.divyam.aven.ledger.repository.LedgerEntryRepository;
import com.divyam.aven.ledger.repository.LedgerTransactionRepository;
import com.divyam.aven.ledger.repository.OutboxEventRepository;
import com.divyam.aven.events.EventEnvelope;
import com.divyam.aven.events.TransactionCreatedEvent;
import com.divyam.aven.events.TransactionEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LedgerService {

    private final LedgerTransactionRepository transactionRepository;
    private final LedgerEntryRepository entryRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public LedgerService(LedgerTransactionRepository transactionRepository, LedgerEntryRepository entryRepository, OutboxEventRepository outboxEventRepository, ObjectMapper objectMapper) {
        this.transactionRepository = transactionRepository;
        this.entryRepository = entryRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public LedgerTransaction create(String idempotencyKey, CreateTransactionRequest request) {
        return transactionRepository.findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> persistNewTransaction(idempotencyKey, request.entries()));
    }

    @Transactional
    public LedgerTransaction reverse(UUID transactionId, String idempotencyKey) {
        LedgerTransaction existingByKey = transactionRepository.findByIdempotencyKey(idempotencyKey).orElse(null);
        if (existingByKey != null) {
            return existingByKey;
        }

        LedgerTransaction original = transactionRepository.findWithEntriesById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        LedgerTransaction reversal = LedgerTransaction.reversalOf(idempotencyKey, original.getId());
        original.getEntries().forEach(entry -> reversal.addEntry(
                entry.getAccountId(), entry.getAmount(), entry.getDirection().inverse()));
        original.markReversedBy(reversal.getId());
        LedgerTransaction saved = transactionRepository.save(reversal);
        writeTransactionCreatedEvent(saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public Page<LedgerEntry> findEntries(UUID accountId, Pageable pageable) {
        return entryRepository.findByAccountId(accountId, pageable);
    }

    private LedgerTransaction persistNewTransaction(String idempotencyKey, List<EntryRequest> entries) {
        ensureBalanced(entries);
        LedgerTransaction transaction = LedgerTransaction.create(idempotencyKey);
        entries.forEach(entry -> transaction.addEntry(entry.accountId(), entry.amount(), entry.direction()));
        LedgerTransaction saved = transactionRepository.save(transaction);
        writeTransactionCreatedEvent(saved);
        return saved;
    }

    private void writeTransactionCreatedEvent(LedgerTransaction transaction) {
        TransactionCreatedEvent payload = new TransactionCreatedEvent(transaction.getId(), transaction.getReversedTransactionId(), transaction.getEntries().stream().map(entry -> new TransactionEntry(entry.getAccountId(), entry.getAmount(), entry.getDirection().name())).toList());
        try {
            String event = objectMapper.writeValueAsString(EventEnvelope.of(transaction.getId(), "", "TransactionCreated", payload));
            outboxEventRepository.save(new com.divyam.aven.ledger.domain.OutboxEvent(transaction.getId(), "TransactionCreated", event));
        } catch (JsonProcessingException exception) { throw new IllegalStateException("Cannot serialize transaction event", exception); }
    }

    private void ensureBalanced(List<EntryRequest> entries) {
        BigDecimal signedTotal = entries.stream()
                .map(entry -> entry.direction().signedAmount(entry.amount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (signedTotal.compareTo(BigDecimal.ZERO) != 0) {
            throw new LedgerValidationException("Transaction entries must balance: total debits must equal total credits");
        }
    }
}
