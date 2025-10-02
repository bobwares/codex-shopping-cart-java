-- App: Shopping Cart API
-- Package: resources.db.changelog.sql
-- File: 001-shopping-cart-tables.sql
-- Version: 0.1.0
-- Turns: [1]
-- Author: Codex Agent <agent@local>
-- Date: 2025-10-02T10:47:14Z
-- Description: Mirrors the Shopping Cart relational schema for Liquibase
--              managed migrations.

CREATE SCHEMA IF NOT EXISTS shopping_cart_app;
SET search_path TO shopping_cart_app, public;

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS shopping_cart (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    subtotal NUMERIC(19, 2) NOT NULL CHECK (subtotal >= 0),
    tax NUMERIC(19, 2) DEFAULT 0 CHECK (tax >= 0),
    shipping NUMERIC(19, 2) DEFAULT 0 CHECK (shipping >= 0),
    total NUMERIC(19, 2) NOT NULL CHECK (total >= 0),
    currency CHAR(3) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_shopping_cart_user_id
    ON shopping_cart (user_id);
CREATE INDEX IF NOT EXISTS idx_shopping_cart_currency
    ON shopping_cart (currency);

CREATE TABLE IF NOT EXISTS shopping_cart_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shopping_cart_id UUID NOT NULL REFERENCES shopping_cart (id) ON DELETE CASCADE,
    product_id VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(19, 2) NOT NULL CHECK (unit_price >= 0),
    total_price NUMERIC(19, 2) NOT NULL CHECK (total_price >= 0),
    currency CHAR(3) NOT NULL,
    CONSTRAINT uq_cart_item UNIQUE (shopping_cart_id, product_id)
);

CREATE INDEX IF NOT EXISTS idx_cart_item_cart_id
    ON shopping_cart_item (shopping_cart_id);
CREATE INDEX IF NOT EXISTS idx_cart_item_product_id
    ON shopping_cart_item (product_id);

CREATE TABLE IF NOT EXISTS shopping_cart_discount (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shopping_cart_id UUID NOT NULL REFERENCES shopping_cart (id) ON DELETE CASCADE,
    code VARCHAR(64) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL CHECK (amount >= 0),
    CONSTRAINT uq_cart_discount UNIQUE (shopping_cart_id, code)
);

CREATE INDEX IF NOT EXISTS idx_cart_discount_cart_id
    ON shopping_cart_discount (shopping_cart_id);

CREATE OR REPLACE VIEW v_shopping_cart_summary AS
SELECT
    c.id,
    c.user_id,
    c.subtotal,
    c.tax,
    c.shipping,
    c.total,
    c.currency,
    c.created_at,
    c.updated_at,
    COUNT(i.id) AS item_count,
    COALESCE(SUM(i.total_price), 0) AS items_total,
    COALESCE(SUM(d.amount), 0) AS discounts_total
FROM shopping_cart c
LEFT JOIN shopping_cart_item i ON i.shopping_cart_id = c.id
LEFT JOIN shopping_cart_discount d ON d.shopping_cart_id = c.id
GROUP BY
    c.id,
    c.user_id,
    c.subtotal,
    c.tax,
    c.shipping,
    c.total,
    c.currency,
    c.created_at,
    c.updated_at;
