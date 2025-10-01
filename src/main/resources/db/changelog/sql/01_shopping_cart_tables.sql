-- App: Shopping Cart API
-- Package: db.migrations
-- File: 01_shopping_cart_tables.sql
-- Version: 0.1.0
-- Turns: 1
-- Author: Codex Agent
-- Date: 2025-10-01T19:32:32Z
-- Description: Creates the shopping_cart schema, core tables, constraints, indexes, and a reporting view.

BEGIN;

CREATE SCHEMA IF NOT EXISTS shopping_cart;

SET search_path TO shopping_cart, public;

CREATE TABLE IF NOT EXISTS shopping_cart (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    subtotal NUMERIC(12, 2) NOT NULL CHECK (subtotal >= 0),
    tax NUMERIC(12, 2) DEFAULT 0 CHECK (tax >= 0),
    shipping NUMERIC(12, 2) DEFAULT 0 CHECK (shipping >= 0),
    total NUMERIC(12, 2) NOT NULL CHECK (total >= 0),
    currency CHAR(3) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT shopping_cart_user_unique UNIQUE (user_id)
);

CREATE TABLE IF NOT EXISTS shopping_cart_item (
    id BIGSERIAL PRIMARY KEY,
    cart_id UUID NOT NULL REFERENCES shopping_cart(id) ON DELETE CASCADE,
    product_id VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(12, 2) NOT NULL CHECK (unit_price >= 0),
    total_price NUMERIC(12, 2) NOT NULL CHECK (total_price >= 0),
    currency CHAR(3) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT shopping_cart_item_product_unique UNIQUE (cart_id, product_id)
);

CREATE TABLE IF NOT EXISTS shopping_cart_discount (
    id BIGSERIAL PRIMARY KEY,
    cart_id UUID NOT NULL REFERENCES shopping_cart(id) ON DELETE CASCADE,
    code VARCHAR(64) NOT NULL,
    amount NUMERIC(12, 2) NOT NULL CHECK (amount >= 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT shopping_cart_discount_code_unique UNIQUE (cart_id, code)
);

CREATE INDEX IF NOT EXISTS idx_shopping_cart_user_id ON shopping_cart (user_id);
CREATE INDEX IF NOT EXISTS idx_shopping_cart_item_cart_id ON shopping_cart_item (cart_id);
CREATE INDEX IF NOT EXISTS idx_shopping_cart_item_product_id ON shopping_cart_item (product_id);
CREATE INDEX IF NOT EXISTS idx_shopping_cart_discount_cart_id ON shopping_cart_discount (cart_id);

CREATE OR REPLACE VIEW shopping_cart_summary AS
SELECT
    c.id,
    c.user_id,
    c.subtotal,
    COALESCE(SUM(i.total_price), 0) AS items_total,
    COALESCE(SUM(d.amount), 0) AS discounts_total,
    c.tax,
    c.shipping,
    c.total,
    c.currency,
    c.created_at,
    c.updated_at
FROM shopping_cart c
LEFT JOIN shopping_cart_item i ON c.id = i.cart_id
LEFT JOIN shopping_cart_discount d ON c.id = d.cart_id
GROUP BY c.id;

COMMIT;
