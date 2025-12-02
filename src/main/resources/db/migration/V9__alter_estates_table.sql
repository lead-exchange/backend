ALTER TABLE estates
ALTER COLUMN total_commission_rate TYPE DECIMAL(10,2),
ALTER COLUMN total_commission_rate DROP NOT NULL;