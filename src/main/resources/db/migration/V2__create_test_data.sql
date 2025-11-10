INSERT INTO users (id, telegram_id, created_at, updated_at) VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '@ivan_petrov', '2024-01-15 10:30:00', '2024-01-15 10:30:00'),
('b1ffc99-9c0b-4ef8-bb6d-6bb9bd380a12', '@anna_sidorova', '2024-01-16 14:20:00', '2024-01-20 09:15:00'),
('c2eecc99-9c0b-4ef8-bb6d-6bb9bd380a13', '@sergey_koval', '2024-01-17 16:45:00', '2024-01-25 11:30:00'),
('d3eedc99-9c0b-4ef8-bb6d-6bb9bd380a14', '@marina_volkova', '2024-01-18 12:00:00', '2024-01-18 12:00:00');

INSERT INTO leads (id, user_id, requirements, status, commission_share, created_at, updated_at) VALUES
('e4eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
 '{"property_type": "apartment", "rooms": 2, "min_area": 45, "max_price": 15000000, "location": "Moscow, Center", "amenities": ["parking", "balcony"]}',
 'active', 2.50, '2024-01-20 11:00:00', '2024-01-20 11:00:00'),

('f5eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'b1ffc99-9c0b-4ef8-bb6d-6bb9bd380a12',
 '{"property_type": "house", "rooms": 4, "min_area": 120, "max_price": 35000000, "location": "Moscow Region", "plot_area": 600, "condition": "renovated"}',
 'processing', 3.00, '2024-01-21 14:30:00', '2024-01-22 10:15:00'),

('g6eebc99-9c0b-4ef8-bb6d-6bb9bd380a23', 'c2eecc99-9c0b-4ef8-bb6d-6bb9bd380a13',
 '{"property_type": "commercial", "purpose": "office", "min_area": 80, "max_price": 20000000, "location": "Moscow, Business District", "floor": "not_first"}',
 'completed', 2.75, '2024-01-19 09:45:00', '2024-01-25 16:20:00'),

('h7eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
 '{"property_type": "apartment", "rooms": 1, "min_area": 30, "max_price": 8000000, "location": "Moscow, Any District", "new_building": true}',
 'active', 2.25, '2024-01-23 13:20:00', '2024-01-23 13:20:00');

 INSERT INTO estates (id, user_id, attributes, total_commission_rate, commission_share, status, created_at, updated_at) VALUES
 ('i8eebc99-9c0b-4ef8-bb6d-6bb9bd380a31', 'b1ffc99-9c0b-4ef8-bb6d-6bb9bd380a12',
  '{"property_type": "apartment", "rooms": 3, "area": 65.5, "price": 18500000, "address": "Moscow, Tverskaya st., 15", "floor": 7, "total_floors": 12, "condition": "euro_renovation", "amenities": ["parking", "concierge", "security"]}',
  5.00, 2.50, 'active', '2024-01-19 15:30:00', '2024-01-24 12:45:00'),

 ('j9eebc99-9c0b-4ef8-bb6d-6bb9bd380a32', 'd3eedc99-9c0b-4ef8-bb6d-6bb9bd380a14',
  '{"property_type": "house", "rooms": 5, "area": 145.0, "price": 42000000, "address": "Moscow Region, Rublevka", "plot_area": 800, "condition": "luxury", "amenities": ["pool", "sauna", "garage", "garden"]}',
  6.00, 3.00, 'sold', '2024-01-16 11:20:00', '2024-01-26 14:30:00'),

 ('k0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33', 'c2eecc99-9c0b-4ef8-bb6d-6bb9bd380a13',
  '{"property_type": "commercial", "purpose": "retail", "area": 120.0, "price": 35000000, "address": "Moscow, Arbat st., 25", "floor": 1, "separate_entrance": true, "ready_for_business": true}',
  7.00, 3.50, 'active', '2024-01-22 16:10:00', '2024-01-22 16:10:00'),

 ('l1eebc99-9c0b-4ef8-bb6d-6bb9bd380a34', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
  '{"property_type": "apartment", "rooms": 2, "area": 52.3, "price": 12500000, "address": "Moscow, Leninsky prospect, 75", "floor": 3, "total_floors": 9, "condition": "good", "balcony": true}',
  4.50, 2.25, 'reserved', '2024-01-24 09:15:00', '2024-01-25 17:40:00');