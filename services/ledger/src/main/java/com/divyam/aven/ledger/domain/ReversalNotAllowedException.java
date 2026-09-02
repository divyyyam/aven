package com.divyam.aven.ledger.domain;

public class ReversalNotAllowedException extends RuntimeException {
    public ReversalNotAllowedException(String message) {
        super(message);
    }
}
