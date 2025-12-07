DELETE FROM users;

ALTER TABLE users alter column telegram_id TYPE BIGINT USING telegram_id::BIGINT;

INSERT INTO users (id, telegram_id, chat_id, phone, created_at, updated_at)
VALUES (
           '11111111-1111-1111-1111-111111111111',
           1,
           987654322,
           '79209515796',
           '2024-01-15T10:30:00.000000',
           '2024-01-15T10:30:00.000000'
       );

INSERT INTO users (id, telegram_id, chat_id, phone, created_at, updated_at)
VALUES (
           '22222222-2222-2222-2222-222222222222',
           2,
           987654321,
           '79384669877',
           '2024-01-15T10:30:00.000000',
           '2024-01-15T10:30:00.000000'
       );