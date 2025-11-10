-- Users
INSERT INTO users (id, telegram_id, created_at, updated_at) VALUES
('11111111-1111-1111-1111-111111111111', '@ivan_petrov', '2024-01-15 10:30:00', '2024-01-15 10:30:00'),
('22222222-2222-2222-2222-222222222222', '@anna_sidorova', '2024-01-16 14:20:00', '2024-01-20 09:15:00'),
('33333333-3333-3333-3333-333333333333', '@sergey_koval', '2024-01-17 16:45:00', '2024-01-25 11:30:00'),
('44444444-4444-4444-4444-444444444444', '@marina_volkova', '2024-01-18 12:00:00', '2024-01-18 12:00:00');

-- Leads
INSERT INTO leads (id, user_id, requirements, status, commission_share, created_at, updated_at) VALUES
('55555555-5555-5555-5555-555555555555', '11111111-1111-1111-1111-111111111111',
 '{"propertyType": "apartment", "minPrice": 12000000, "maxPrice": 15000000, "minArea": 45, "maxArea": 80, "locations": ["Moscow, Center"], "bedrooms": 2}',
 'ACTIVE', 2.50, '2024-01-20 11:00:00', '2024-01-20 11:00:00'),

('66666666-6666-6666-6666-666666666666', '22222222-2222-2222-2222-222222222222',
 '{"propertyType": "house", "minPrice": 30000000, "maxPrice": 35000000, "minArea": 120, "maxArea": 200, "locations": ["Moscow Region"], "bedrooms": 4}',
 'ACTIVE', 3.00, '2024-01-21 14:30:00', '2024-01-22 10:15:00'),

('77777777-7777-7777-7777-777777777777', '33333333-3333-3333-3333-333333333333',
 '{"propertyType": "commercial", "minPrice": 15000000, "maxPrice": 20000000, "minArea": 80, "maxArea": 150, "locations": ["Moscow, Business District"], "bedrooms": null}',
 'ACTIVE', 2.75, '2024-01-19 09:45:00', '2024-01-25 16:20:00'),

('88888888-8888-8888-8888-888888888888', '11111111-1111-1111-1111-111111111111',
 '{"propertyType": "apartment", "minPrice": 7000000, "maxPrice": 8000000, "minArea": 30, "maxArea": 50, "locations": ["Moscow, Any District"], "bedrooms": 1}',
 'ACTIVE', 2.25, '2024-01-23 13:20:00', '2024-01-23 13:20:00');

-- Estates
INSERT INTO estates (id, user_id, attributes, total_commission_rate, commission_share, status, created_at, updated_at) VALUES
('99999999-9999-9999-9999-999999999999', '22222222-2222-2222-2222-222222222222',
 '{"title": "Luxury apartment in Tverskaya", "description": "Spacious 3-room apartment with euro renovation in the city center", "address": "Moscow, Tverskaya st., 15", "price": 18500000, "area": 65, "bedrooms": 3, "photos": []}',
 5.00, 2.50, 'ACTIVE', '2024-01-19 15:30:00', '2024-01-24 12:45:00'),

('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '44444444-4444-4444-4444-444444444444',
 '{"title": "Premium house in Rublevka", "description": "Luxury 5-bedroom house with pool and garden in prestigious area", "address": "Moscow Region, Rublevka", "price": 42000000, "area": 145, "bedrooms": 5, "photos": []}',
 6.00, 3.00, 'ARCHIVE', '2024-01-16 11:20:00', '2024-01-26 14:30:00'),

('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '33333333-3333-3333-3333-333333333333',
 '{"title": "Commercial space on Arbat", "description": "Retail space with separate entrance, ready for business", "address": "Moscow, Arbat st., 25", "price": 35000000, "area": 120, "bedrooms": null, "photos": []}',
 7.00, 3.50, 'ACTIVE', '2024-01-22 16:10:00', '2024-01-22 16:10:00'),

('cccccccc-cccc-cccc-cccc-cccccccccccc', '11111111-1111-1111-1111-111111111111',
 '{"title": "Comfortable apartment on Leninsky prospect", "description": "Cozy 2-room apartment in good condition with balcony", "address": "Moscow, Leninsky prospect, 75", "price": 12500000, "area": 52, "bedrooms": 2, "photos": []}',
 4.50, 2.25, 'ACTIVE', '2024-01-24 09:15:00', '2024-01-25 17:40:00');