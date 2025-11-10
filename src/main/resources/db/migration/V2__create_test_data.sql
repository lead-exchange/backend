-- Users
INSERT INTO users (id, telegram_id, created_at, updated_at) VALUES
('11111111-1111-1111-1111-111111111111', '@ivan_petrov', '2024-01-15 10:30:00', '2024-01-15 10:30:00'),
('22222222-2222-2222-2222-222222222222', '@anna_sidorova', '2024-01-16 14:20:00', '2024-01-20 09:15:00'),
('33333333-3333-3333-3333-333333333333', '@sergey_koval', '2024-01-17 16:45:00', '2024-01-25 11:30:00'),
('44444444-4444-4444-4444-444444444444', '@marina_volkova', '2024-01-18 12:00:00', '2024-01-18 12:00:00');

-- Leads
INSERT INTO leads (id, user_id, requirements, status, commission_share, created_at, updated_at) VALUES
('55555555-5555-5555-5555-555555555555', '11111111-1111-1111-1111-111111111111',
 '{"property_type": "apartment", "rooms": 2, "min_area": 45, "max_price": 15000000, "location": "Moscow, Center", "amenities": ["parking", "balcony"]}',
 'active', 2.50, '2024-01-20 11:00:00', '2024-01-20 11:00:00'),

('66666666-6666-6666-6666-666666666666', '22222222-2222-2222-2222-222222222222',
 '{"property_type": "house", "rooms": 4, "min_area": 120, "max_price": 35000000, "location": "Moscow Region", "plot_area": 600, "condition": "renovated"}',
 'processing', 3.00, '2024-01-21 14:30:00', '2024-01-22 10:15:00'),

('77777777-7777-7777-7777-777777777777', '33333333-3333-3333-3333-333333333333',
 '{"property_type": "commercial", "purpose": "office", "min_area": 80, "max_price": 20000000, "location": "Moscow, Business District", "floor": "not_first"}',
 'completed', 2.75, '2024-01-19 09:45:00', '2024-01-25 16:20:00'),

('88888888-8888-8888-8888-888888888888', '11111111-1111-1111-1111-111111111111',
 '{"property_type": "apartment", "rooms": 1, "min_area": 30, "max_price": 8000000, "location": "Moscow, Any District", "new_building": true}',
 'active', 2.25, '2024-01-23 13:20:00', '2024-01-23 13:20:00');

-- Estates
INSERT INTO estates (id, user_id, attributes, total_commission_rate, commission_share, status, created_at, updated_at) VALUES
('99999999-9999-9999-9999-999999999999', '22222222-2222-2222-2222-222222222222',
 '{"property_type": "apartment", "rooms": 3, "area": 65.5, "price": 18500000, "address": "Moscow, Tverskaya st., 15", "floor": 7, "total_floors": 12, "condition": "euro_renovation", "amenities": ["parking", "concierge", "security"]}',
 5.00, 2.50, 'active', '2024-01-19 15:30:00', '2024-01-24 12:45:00'),

('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '44444444-4444-4444-4444-444444444444',
 '{"property_type": "house", "rooms": 5, "area": 145.0, "price": 42000000, "address": "Moscow Region, Rublevka", "plot_area": 800, "condition": "luxury", "amenities": ["pool", "sauna", "garage", "garden"]}',
 6.00, 3.00, 'sold', '2024-01-16 11:20:00', '2024-01-26 14:30:00'),

('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '33333333-3333-3333-3333-333333333333',
 '{"property_type": "commercial", "purpose": "retail", "area": 120.0, "price": 35000000, "address": "Moscow, Arbat st., 25", "floor": 1, "separate_entrance": true, "ready_for_business": true}',
 7.00, 3.50, 'active', '2024-01-22 16:10:00', '2024-01-22 16:10:00'),

('cccccccc-cccc-cccc-cccc-cccccccccccc', '11111111-1111-1111-1111-111111111111',
 '{"property_type": "apartment", "rooms": 2, "area": 52.3, "price": 12500000, "address": "Moscow, Leninsky prospect, 75", "floor": 3, "total_floors": 9, "condition": "good", "balcony": true}',
 4.50, 2.25, 'reserved', '2024-01-24 09:15:00', '2024-01-25 17:40:00');