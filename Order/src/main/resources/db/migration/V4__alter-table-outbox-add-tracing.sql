ALTER TABLE outbox ADD COLUMN trace_id VARCHAR(100);
ALTER TABLE outbox ADD COLUMN span_id VARCHAR(100);

CREATE INDEX idx_outbox_processed_created ON outbox(processed, created_at);