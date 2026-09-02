package com.divyam.aven.ledger.domain;

import java.math.BigDecimal;

public enum EntryDirection {
    DEBIT(BigDecimal.ONE),
    CREDIT(BigDecimal.ONE.negate());

    private final BigDecimal sign;

    EntryDirection(BigDecimal sign) {
        this.sign = sign;
    }

    public BigDecimal signedAmount(BigDecimal amount) {
        return amount.multiply(sign);
    }

    public EntryDirection inverse() {
        return this == DEBIT ? CREDIT : DEBIT;
    }
}
