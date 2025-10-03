/*
 * App: Shopping Cart API
 * Package: db.scripts
 * File: shopping_cart_test_data.sql
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: Shopping cart seed data inserts
 * Description: Populates shopping cart tables with 20 sample carts, associated items, and discounts for local testing.
 */
BEGIN;

WITH carts AS (
    SELECT *
    FROM (
        VALUES
            ('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 150.00, 9.00, 5.00, 154.00, 'USD',
             '2025-09-28T10:00:00Z'::timestamptz, '2025-09-28T11:00:00Z'::timestamptz,
             '[{"productId":"SKU-1001","name":"Wireless Mouse","quantity":2,"unitPrice":25.00,"currency":"USD","totalPrice":50.00},
               {"productId":"SKU-2050","name":"Mechanical Keyboard","quantity":1,"unitPrice":75.00,"currency":"USD","totalPrice":75.00}]'::jsonb,
             '[{"code":"SAVE10","amount":10.00}]'::jsonb),
            ('22222222-2222-2222-2222-222222222222', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 89.99, 5.40, 0.00, 95.39, 'USD',
             '2025-09-27T14:12:00Z'::timestamptz, '2025-09-27T14:45:00Z'::timestamptz,
             '[{"productId":"SKU-3003","name":"Bluetooth Speaker","quantity":1,"unitPrice":89.99,"currency":"USD","totalPrice":89.99}]'::jsonb,
             '[]'::jsonb),
            ('33333333-3333-3333-3333-333333333333', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 210.50, 12.63, 8.99, 232.12, 'USD',
             '2025-09-25T09:05:00Z'::timestamptz, '2025-09-25T09:50:00Z'::timestamptz,
             '[{"productId":"SKU-4010","name":"4K Monitor","quantity":1,"unitPrice":210.50,"currency":"USD","totalPrice":210.50}]'::jsonb,
             '[{"code":"FREESHIP","amount":8.99}]'::jsonb),
            ('44444444-4444-4444-4444-444444444444', 'dddddddd-dddd-dddd-dddd-dddddddddddd', 65.00, 3.90, 4.50, 73.40, 'USD',
             '2025-09-22T16:25:00Z'::timestamptz, '2025-09-22T16:59:00Z'::timestamptz,
             '[{"productId":"SKU-5020","name":"USB-C Hub","quantity":1,"unitPrice":45.00,"currency":"USD","totalPrice":45.00},
               {"productId":"SKU-8800","name":"HDMI Cable","quantity":2,"unitPrice":10.00,"currency":"USD","totalPrice":20.00}]'::jsonb,
             '[]'::jsonb),
            ('55555555-5555-5555-5555-555555555555', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 320.00, 19.20, 12.50, 351.70, 'USD',
             '2025-09-20T08:30:00Z'::timestamptz, '2025-09-20T09:02:00Z'::timestamptz,
             '[{"productId":"SKU-9201","name":"Gaming Headset","quantity":2,"unitPrice":80.00,"currency":"USD","totalPrice":160.00},
               {"productId":"SKU-7777","name":"Webcam","quantity":1,"unitPrice":60.00,"currency":"USD","totalPrice":60.00},
               {"productId":"SKU-3050","name":"Desk Lamp","quantity":2,"unitPrice":50.00,"currency":"USD","totalPrice":100.00}]'::jsonb,
             '[{"code":"WELCOME15","amount":15.00}]'::jsonb),
            ('66666666-6666-6666-6666-666666666666', 'ffffffff-ffff-ffff-ffff-ffffffffffff', 49.50, 2.97, 0.00, 52.47, 'USD',
             '2025-09-18T19:15:00Z'::timestamptz, '2025-09-18T19:28:00Z'::timestamptz,
             '[{"productId":"SKU-1100","name":"Wireless Charger","quantity":1,"unitPrice":49.50,"currency":"USD","totalPrice":49.50}]'::jsonb,
             '[]'::jsonb),
            ('77777777-7777-7777-7777-777777777777', '00000000-0000-0000-0000-000000000000', 580.00, 34.80, 20.00, 634.80, 'USD',
             '2025-09-17T12:45:00Z'::timestamptz, '2025-09-17T13:10:00Z'::timestamptz,
             '[{"productId":"SKU-6600","name":"Ultrabook","quantity":1,"unitPrice":580.00,"currency":"USD","totalPrice":580.00}]'::jsonb,
             '[]'::jsonb),
            ('88888888-8888-8888-8888-888888888888', '11111111-2222-3333-4444-555555555555', 130.75, 7.85, 6.25, 144.85, 'USD',
             '2025-09-15T11:22:00Z'::timestamptz, '2025-09-15T11:40:00Z'::timestamptz,
             '[{"productId":"SKU-3210","name":"Smart Speaker","quantity":1,"unitPrice":130.75,"currency":"USD","totalPrice":130.75}]'::jsonb,
             '[{"code":"LOYAL5","amount":5.00}]'::jsonb),
            ('99999999-9999-9999-9999-999999999999', '22222222-3333-4444-5555-666666666666', 42.00, 2.52, 0.00, 44.52, 'USD',
             '2025-09-14T07:40:00Z'::timestamptz, '2025-09-14T08:05:00Z'::timestamptz,
             '[{"productId":"SKU-4500","name":"Portable Battery","quantity":2,"unitPrice":21.00,"currency":"USD","totalPrice":42.00}]'::jsonb,
             '[]'::jsonb),
            ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab', '33333333-4444-5555-6666-777777777777', 275.30, 16.52, 10.00, 301.82, 'USD',
             '2025-09-12T17:05:00Z'::timestamptz, '2025-09-12T17:45:00Z'::timestamptz,
             '[{"productId":"SKU-8701","name":"Graphic Tablet","quantity":1,"unitPrice":275.30,"currency":"USD","totalPrice":275.30}]'::jsonb,
             '[{"code":"GRAPHIC","amount":10.00}]'::jsonb),
            ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbc', '44444444-5555-6666-7777-888888888888', 315.00, 18.90, 12.00, 345.90, 'USD',
             '2025-09-10T15:30:00Z'::timestamptz, '2025-09-10T16:00:00Z'::timestamptz,
             '[{"productId":"SKU-9800","name":"Ergonomic Chair","quantity":1,"unitPrice":315.00,"currency":"USD","totalPrice":315.00}]'::jsonb,
             '[]'::jsonb),
            ('cccccccc-cccc-cccc-cccc-cccccccccccd', '55555555-6666-7777-8888-999999999999', 95.25, 5.72, 3.50, 104.47, 'USD',
             '2025-09-09T10:18:00Z'::timestamptz, '2025-09-09T10:40:00Z'::timestamptz,
             '[{"productId":"SKU-1122","name":"Smart Plug","quantity":3,"unitPrice":31.75,"currency":"USD","totalPrice":95.25}]'::jsonb,
             '[]'::jsonb),
            ('dddddddd-dddd-dddd-dddd-ddddddddddde', '66666666-7777-8888-9999-aaaaaaaaaaaa', 410.00, 24.60, 15.00, 449.60, 'USD',
             '2025-09-08T13:10:00Z'::timestamptz, '2025-09-08T13:32:00Z'::timestamptz,
             '[{"productId":"SKU-5521","name":"Gaming Console","quantity":1,"unitPrice":410.00,"currency":"USD","totalPrice":410.00}]'::jsonb,
             '[{"code":"BUNDLE15","amount":15.00}]'::jsonb),
            ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeef', '77777777-8888-9999-aaaa-bbbbbbbbbbbb', 220.40, 13.22, 7.00, 240.62, 'USD',
             '2025-09-07T18:50:00Z'::timestamptz, '2025-09-07T19:14:00Z'::timestamptz,
             '[{"productId":"SKU-6611","name":"Noise Cancelling Earbuds","quantity":2,"unitPrice":110.20,"currency":"USD","totalPrice":220.40}]'::jsonb,
             '[{"code":"EARFUN","amount":7.00}]'::jsonb),
            ('ffffffff-ffff-ffff-ffff-fffffffffff0', '88888888-9999-aaaa-bbbb-cccccccccccc', 185.75, 11.15, 6.75, 203.65, 'USD',
             '2025-09-05T09:45:00Z'::timestamptz, '2025-09-05T10:05:00Z'::timestamptz,
             '[{"productId":"SKU-7345","name":"Action Camera","quantity":1,"unitPrice":185.75,"currency":"USD","totalPrice":185.75}]'::jsonb,
             '[]'::jsonb),
            ('99999999-aaaa-bbbb-cccc-dddddddddddf', '99999999-aaaa-bbbb-cccc-eeeeeeeeeeee', 72.80, 4.37, 0.00, 77.17, 'USD',
             '2025-09-03T07:00:00Z'::timestamptz, '2025-09-03T07:29:00Z'::timestamptz,
             '[{"productId":"SKU-9090","name":"Fitness Tracker","quantity":1,"unitPrice":72.80,"currency":"USD","totalPrice":72.80}]'::jsonb,
             '[]'::jsonb),
            ('aaaa1111-bbbb-2222-cccc-333333333333', 'aaaa2222-bbbb-3333-cccc-444444444444', 140.00, 8.40, 5.00, 153.40, 'EUR',
             '2025-09-02T12:10:00Z'::timestamptz, '2025-09-02T12:36:00Z'::timestamptz,
             '[{"productId":"SKU-2200","name":"Espresso Machine","quantity":1,"unitPrice":140.00,"currency":"EUR","totalPrice":140.00}]'::jsonb,
             '[{"code":"EURO5","amount":5.00}]'::jsonb),
            ('bbbb2222-cccc-3333-dddd-444444444445', 'bbbb3333-cccc-4444-dddd-555555555555', 98.60, 5.92, 4.20, 108.72, 'EUR',
             '2025-08-31T16:45:00Z'::timestamptz, '2025-08-31T17:05:00Z'::timestamptz,
             '[{"productId":"SKU-3311","name":"Smart Thermostat","quantity":1,"unitPrice":98.60,"currency":"EUR","totalPrice":98.60}]'::jsonb,
             '[]'::jsonb),
            ('cccc3333-dddd-4444-eeee-555555555556', 'cccc4444-dddd-5555-eeee-666666666666', 54.99, 3.30, 0.00, 58.29, 'EUR',
             '2025-08-30T09:20:00Z'::timestamptz, '2025-08-30T09:44:00Z'::timestamptz,
             '[{"productId":"SKU-8765","name":"LED Strip Lights","quantity":3,"unitPrice":18.33,"currency":"EUR","totalPrice":54.99}]'::jsonb,
             '[]'::jsonb),
            ('dddd4444-eeee-5555-ffff-666666666667', 'dddd5555-eeee-6666-ffff-777777777777', 260.00, 15.60, 9.50, 285.10, 'EUR',
             '2025-08-28T20:00:00Z'::timestamptz, '2025-08-28T20:20:00Z'::timestamptz,
             '[{"productId":"SKU-7788","name":"Robot Vacuum","quantity":1,"unitPrice":260.00,"currency":"EUR","totalPrice":260.00}]'::jsonb,
             '[{"code":"CLEAN10","amount":10.00}]'::jsonb),
            ('eeee5555-ffff-6666-0000-777777777778', 'eeee6666-ffff-7777-0000-888888888888', 118.45, 7.11, 3.80, 129.36, 'EUR',
             '2025-08-26T06:55:00Z'::timestamptz, '2025-08-26T07:15:00Z'::timestamptz,
             '[{"productId":"SKU-9900","name":"E-Reader","quantity":1,"unitPrice":118.45,"currency":"EUR","totalPrice":118.45}]'::jsonb,
             '[{"code":"READMORE","amount":3.80}]'::jsonb),
            ('ffff6666-0000-7777-1111-888888888889', 'ffff7777-0000-8888-1111-999999999999', 305.00, 18.30, 11.00, 334.30, 'EUR',
             '2025-08-24T18:10:00Z'::timestamptz, '2025-08-24T18:45:00Z'::timestamptz,
             '[{"productId":"SKU-1234","name":"Smartphone","quantity":1,"unitPrice":305.00,"currency":"EUR","totalPrice":305.00}]'::jsonb,
             '[]'::jsonb)
    ) AS c(cart_id, user_id, subtotal, tax, shipping, total, currency, created_at, updated_at, items_json, discounts_json)
)
INSERT INTO shopping_cart.shopping_cart (shopping_cart_id, user_id, subtotal, tax, shipping, total, currency, created_at, updated_at)
SELECT cart_id, user_id, subtotal, tax, shipping, total, currency, created_at, updated_at
FROM carts
ON CONFLICT (shopping_cart_id) DO NOTHING;

INSERT INTO shopping_cart.shopping_cart_item (cart_item_id, shopping_cart_id, product_id, name, quantity, unit_price, currency, total_price)
SELECT
    COALESCE((item_record->>'cartItemId')::uuid, gen_random_uuid()),
    carts.cart_id,
    item_record->>'productId',
    item_record->>'name',
    (item_record->>'quantity')::integer,
    ROUND((item_record->>'unitPrice')::numeric, 2),
    COALESCE(item_record->>'currency', carts.currency),
    ROUND((item_record->>'totalPrice')::numeric, 2)
FROM carts
CROSS JOIN LATERAL jsonb_to_recordset(carts.items_json) AS item_record(
    "productId" text,
    "name" text,
    "quantity" int,
    "unitPrice" numeric,
    "currency" text,
    "totalPrice" numeric,
    "cartItemId" text
)
ON CONFLICT (shopping_cart_id, product_id) DO NOTHING;

INSERT INTO shopping_cart.shopping_cart_discount (discount_id, shopping_cart_id, code, amount)
SELECT
    COALESCE((discount_record->>'discountId')::uuid, gen_random_uuid()),
    carts.cart_id,
    discount_record->>'code',
    ROUND((discount_record->>'amount')::numeric, 2)
FROM carts
CROSS JOIN LATERAL jsonb_to_recordset(carts.discounts_json) AS discount_record(
    "code" text,
    "amount" numeric,
    "discountId" text
)
WHERE (discount_record->>'code') IS NOT NULL
ON CONFLICT (shopping_cart_id, code) DO NOTHING;

COMMIT;

-- Smoke test
SELECT COUNT(*) AS shopping_cart_count FROM shopping_cart.shopping_cart;
