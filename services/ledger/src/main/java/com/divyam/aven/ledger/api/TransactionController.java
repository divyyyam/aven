package com.divyam.aven.ledger.api;

import com.divyam.aven.ledger.application.LedgerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Validated
public class TransactionController {

    private final LedgerService ledgerService;

    public TransactionController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> create(
            @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey,
            @RequestHeader(value = "X-Correlation-ID", required = false) String traceId,
            @Valid @RequestBody CreateTransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransactionResponse.from(ledgerService.create(idempotencyKey, traceId(traceId), request)));
    }

    @PostMapping("/transactions/{transactionId}/reverse")
    public ResponseEntity<TransactionResponse> reverse(
            @PathVariable UUID transactionId,
            @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey,
            @RequestHeader(value = "X-Correlation-ID", required = false) String traceId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransactionResponse.from(ledgerService.reverse(transactionId, idempotencyKey, traceId(traceId))));
    }

    @GetMapping("/accounts/{accountId}/entries")
    public Page<EntryResponse> entries(
            @PathVariable UUID accountId,
            Pageable pageable) {
        Pageable stablePage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id")));
        return ledgerService.findEntries(accountId, stablePage).map(EntryResponse::from);
    }

    private String traceId(String suppliedTraceId) {
        return suppliedTraceId == null || suppliedTraceId.isBlank()
                ? UUID.randomUUID().toString()
                : suppliedTraceId;
    }
}
