/*
 * App: Shopping Cart API
 * Package: db.migrations
 * File: 01_shopping_cart_tables.sql
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: shopping_cart schema, tables, indexes, and summary view
 * Description: Creates normalized shopping cart tables, constraints, indexes, and a summary view for reporting.
 */
BEGIN;

CREATE SCHEMA IF NOT EXISTS shopping_cart;

SET search_path TO shopping_cart, public;

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS shopping_cart (
    shopping_cart_id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    subtotal NUMERIC(12, 2) NOT NULL CHECK (subtotal >= 0),
    tax NUMERIC(12, 2) DEFAULT 0 CHECK (tax >= 0),
    shipping NUMERIC(12, 2) DEFAULT 0 CHECK (shipping >= 0),
    total NUMERIC(12, 2) NOT NULL CHECK (total >= 0),
    currency CHAR(3) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (user_id)
);

CREATE INDEX IF NOT EXISTS idx_shopping_cart_user_id ON shopping_cart (user_id);

CREATE TABLE IF NOT EXISTS shopping_cart_item (
    cart_item_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shopping_cart_id UUID NOT NULL REFERENCES shopping_cart (shopping_cart_id) ON DELETE CASCADE,
    product_id VARCHAR(64) NOT NULL,
    name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(12, 2) NOT NULL CHECK (unit_price >= 0),
    currency CHAR(3) NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL CHECK (total_price >= 0),
    UNIQUE (shopping_cart_id, product_id)
);

CREATE INDEX IF NOT EXISTS idx_cart_item_cart_id ON shopping_cart_item (shopping_cart_id);
CREATE INDEX IF NOT EXISTS idx_cart_item_product_id ON shopping_cart_item (product_id);

CREATE TABLE IF NOT EXISTS shopping_cart_discount (
    discount_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shopping_cart_id UUID NOT NULL REFERENCES shopping_cart (shopping_cart_id) ON DELETE CASCADE,
    code VARCHAR(64) NOT NULL,
    amount NUMERIC(12, 2) NOT NULL CHECK (amount >= 0),
    UNIQUE (shopping_cart_id, code)
);

CREATE INDEX IF NOT EXISTS idx_cart_discount_cart_id ON shopping_cart_discount (shopping_cart_id);
CREATE INDEX IF NOT EXISTS idx_cart_discount_code ON shopping_cart_discount (code);

CREATE OR REPLACE VIEW shopping_cart_summary AS
SELECT
    c.shopping_cart_id,
    c.user_id,
    c.subtotal,
    c.tax,
    c.shipping,
    c.total,
    c.currency,
    c.created_at,
    c.updated_at,
    COUNT(i.cart_item_id) AS item_count,
    COALESCE(SUM(i.total_price), 0) AS item_total,
    COALESCE(SUM(d.amount), 0) AS discount_total
FROM shopping_cart c
LEFT JOIN shopping_cart_item i ON i.shopping_cart_id = c.shopping_cart_id
LEFT JOIN shopping_cart_discount d ON d.shopping_cart_id = c.shopping_cart_id
GROUP BY c.shopping_cart_id;

COMMIT;
