ALTER TABLE leads
    ADD COLUMN name VARCHAR(255);

-- 2. Заполняем существующие ряды русскими именами и фамилиями
UPDATE leads SET name = 'Иван Петров'
WHERE id = '55555555-5555-5555-5555-555555555555';

UPDATE leads SET name = 'Мария Соколова'
WHERE id = '66666666-6666-6666-6666-666666666666';

UPDATE leads SET name = 'Алексей Смирнов'
WHERE id = '77777777-7777-7777-7777-777777777777';

UPDATE leads SET name = 'Екатерина Васильева'
WHERE id = '88888888-8888-8888-8888-888888888888';

-- 3. Делаем колонку NOT NULL
ALTER TABLE leads
    ALTER COLUMN name SET NOT NULL;