ALTER TABLE users
    ADD COLUMN chat_id BIGINT,
ADD COLUMN phone_number VARCHAR(50);

UPDATE users
SET chat_id = 987654321, phone_number = '+79991234567'
WHERE telegram_id = '@ivan_petrov';

UPDATE users
SET chat_id = 123456789, phone_number = '+79990001122'
WHERE telegram_id = '@anna_sidorova';