DELETE FROM users;

ALTER TABLE users
    ADD COLUMN chat_id BIGINT NOT NULL,
    ADD COLUMN phone VARCHAR(50) NOT NULL;

INSERT INTO users (id, telegram_id, chat_id, phone, created_at, updated_at)
VALUES (
           '11111111-1111-1111-1111-111111111111',
           '@ivan_petrov',
           987654322,
           '79209515796',
           '2024-01-15T10:30:00.000000',
           '2024-01-15T10:30:00.000000'
       );
