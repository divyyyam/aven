CREATE TABLE ledger_transactions (
    id UUID PRIMARY KEY,
    idempotency_key VARCHAR(255) NOT NULL UNIQUE,
    reversed_transaction_id UUID REFERENCES ledger_transactions(id),
    reversed_by_transaction_id UUID UNIQUE REFERENCES ledger_transactions(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE ledger_entries (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL REFERENCES ledger_transactions(id),
    account_id UUID NOT NULL,
    amount NUMERIC(19, 4) NOT NULL CHECK (amount > 0),
    direction VARCHAR(10) NOT NULL CHECK (direction IN ('DEBIT', 'CREDIT')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_ledger_entries_account_created_at
    ON ledger_entries (account_id, created_at DESC, id DESC);
