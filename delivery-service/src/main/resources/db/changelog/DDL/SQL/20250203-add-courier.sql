CREATE TABLE IF NOT EXISTS courier
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    phone      VARCHAR(20),
    country    VARCHAR(20),
    created_at TIMESTAMP DEFAULT now() NOT NULL,
    updated_at TIMESTAMP DEFAULT now() NOT NULL
);

ALTER TABLE orders
    ADD COLUMN courier_id BIGINT REFERENCES courier (id) ON DELETE CASCADE;

ALTER TABLE orders
    ADD COLUMN message_location_id BIGINT;

ALTER TABLE orders
    ADD COLUMN chat_id BIGINT;
