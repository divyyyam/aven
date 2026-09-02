package com.divyam.aven.ledger.domain;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(java.util.UUID transactionId) {
        super("Transaction not found: " + transactionId);
    }
}
