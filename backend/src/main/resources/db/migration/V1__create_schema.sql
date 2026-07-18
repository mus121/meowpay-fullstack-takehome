CREATE TABLE cats (
    id         UUID           NOT NULL PRIMARY KEY,
    name       VARCHAR(100)   NOT NULL,
    balance    NUMERIC(19, 2) NOT NULL DEFAULT 0 CHECK (balance >= 0),
    created_at TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE TABLE transfers (
    id           UUID           NOT NULL PRIMARY KEY,
    sender_id    UUID           NOT NULL REFERENCES cats (id),
    recipient_id UUID           NOT NULL REFERENCES cats (id),
    amount       NUMERIC(19, 2) NOT NULL CHECK (amount > 0),
    created_at   TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    CONSTRAINT transfers_sender_recipient_different CHECK (sender_id <> recipient_id)
);

CREATE INDEX idx_transfers_sender_id ON transfers (sender_id);
CREATE INDEX idx_transfers_recipient_id ON transfers (recipient_id);
CREATE INDEX idx_transfers_created_at ON transfers (created_at DESC);
