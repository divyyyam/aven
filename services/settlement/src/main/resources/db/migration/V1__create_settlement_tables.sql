CREATE TABLE settlement_batches (
    id UUID PRIMARY KEY,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    completed_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE settlement_items (
    id UUID PRIMARY KEY,
    batch_id UUID REFERENCES settlement_batches(id),
    transaction_id UUID NOT NULL UNIQUE,
    trace_id VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL,
    retry_count INTEGER NOT NULL DEFAULT 0,
    next_attempt_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_error VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_settlement_items_due ON settlement_items (next_attempt_at)
    WHERE status IN ('PENDING', 'RETRY_PENDING');

CREATE TABLE processed_events (
    event_id UUID PRIMARY KEY,
    consumed_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE settlement_outbox (
    id UUID PRIMARY KEY,
    aggregate_id UUID NOT NULL,
    topic VARCHAR(255) NOT NULL,
    payload_json JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_settlement_outbox_unpublished ON settlement_outbox (created_at)
    WHERE published_at IS NULL;
