-- Seed data for testing

-- Insert sample products
INSERT INTO products (sku, name, price, available) VALUES
    ('PROD001', 'Sample Product 1', 1000.00, true),
    ('PROD002', 'Sample Product 2', 2000.00, true),
    ('PROD003', 'Sample Product 3', 1500.00, true)
ON CONFLICT DO NOTHING;

-- Note: Users will be created via API, but you can manually insert test users if needed
-- Example:
-- INSERT INTO users (username, parent_id, effective_parent_id, is_active) VALUES
--     ('user1', NULL, NULL, true),
--     ('user2', 1, 1, true),
--     ('user3', 2, 2, true)
-- ON CONFLICT DO NOTHING;

