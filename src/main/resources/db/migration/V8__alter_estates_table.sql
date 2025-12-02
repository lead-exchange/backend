DELETE FROM estates;

ALTER TABLE estates
    ADD COLUMN external_id BIGINT NOT NULL;
