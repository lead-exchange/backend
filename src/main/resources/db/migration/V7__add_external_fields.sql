ALTER TABLE users
    ADD COLUMN phone VARCHAR(32);

ALTER TABLE estates
    ADD COLUMN external_id VARCHAR(64),
    ALTER COLUMN total_commission_rate DROP NOT NULL,
    ALTER COLUMN commission_share DROP NOT NULL;

DELETE FROM estates;
