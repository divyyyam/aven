package com.divyam.aven.ledger.domain;

public class LedgerValidationException extends RuntimeException {
    public LedgerValidationException(String message) {
        super(message);
    }
}
