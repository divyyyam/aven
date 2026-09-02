CREATE TABLE replayed_balances (
    account_id UUID PRIMARY KEY,
    computed_balance NUMERIC(19,4) NOT NULL,
    last_event_id UUID,
    computed_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE processed_events (
    event_id UUID PRIMARY KEY,
    consumed_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE reconciliation_drifts (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    expected_balance NUMERIC(19,4) NOT NULL,
    observed_balance NUMERIC(19,4) NOT NULL,
    detected_at TIMESTAMP WITH TIME ZONE NOT NULL,
    resolved_at TIMESTAMP WITH TIME ZONE
);

CREATE UNIQUE INDEX idx_one_active_drift_per_account
    ON reconciliation_drifts (account_id)
    WHERE resolved_at IS NULL;

CREATE TABLE reconciliation_outbox (
    id UUID PRIMARY KEY,
    aggregate_id UUID NOT NULL,
    topic VARCHAR(255) NOT NULL,
    payload_json JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_reconciliation_outbox_unpublished ON reconciliation_outbox (created_at)
    WHERE published_at IS NULL;
