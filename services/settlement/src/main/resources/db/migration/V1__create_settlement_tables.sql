CREATE TABLE settlement_batches (id UUID PRIMARY KEY, status VARCHAR(32) NOT NULL, created_at TIMESTAMP WITH TIME ZONE NOT NULL);
CREATE TABLE settlement_items (id UUID PRIMARY KEY, batch_id UUID REFERENCES settlement_batches(id), transaction_id UUID NOT NULL UNIQUE, status VARCHAR(32) NOT NULL, retry_count INTEGER NOT NULL DEFAULT 0, next_attempt_at TIMESTAMP WITH TIME ZONE, created_at TIMESTAMP WITH TIME ZONE NOT NULL);
CREATE TABLE processed_events (event_id UUID PRIMARY KEY, consumed_at TIMESTAMP WITH TIME ZONE NOT NULL);
