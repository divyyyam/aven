package com.divyam.aven.events;

public final class Topics {
    public static final String TRANSACTION_CREATED = "ledger.transaction-created.v1";
    public static final String TRANSACTION_SETTLED = "settlement.transaction-settled.v1";
    public static final String TRANSACTION_FAILED = "settlement.transaction-failed.v1";
    public static final String SETTLEMENT_DLQ = "settlement.transaction-created.dlq.v1";
    public static final String RECONCILIATION_DRIFT = "reconciliation.drift-detected.v1";
    private Topics() { }
}
