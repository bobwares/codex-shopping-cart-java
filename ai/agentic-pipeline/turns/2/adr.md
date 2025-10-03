# Architecture Decision Record

Model Shopping Cart Aggregate with Explicit Child Entities and SQL Migration

**Turn**: 2

**Status**: Accepted

**Date**: 2025-10-03 - 00:25

**Context**
The Shopping Cart domain schema includes nested arrays for items and discounts along with monetary totals. We needed to persist this aggregate to PostgreSQL, expose CRUD endpoints, and keep schema management aligned with the Liquibase-driven workflow defined in Turn 1.

**Options Considered**
- Represent child collections as JSON columns and manage them application-side.
- Create separate normalized tables for items and discounts with JPA entities mapped via relations.
- Delay schema integration and rely on in-memory persistence until later turns.

**Decision**
Create normalized tables (`shopping_cart_item`, `shopping_cart_discount`) with foreign-key relationships to the cart table and map them to dedicated JPA entities. Maintain a hand-authored SQL migration located under `db/migrations` and reference it from the Liquibase changelog to preserve the project's SQL-first pattern.

**Result**
Added `ShoppingCart`, `ShoppingCartItem`, and `ShoppingCartDiscount` entities with cascading relationships, plus repository/service/controller code to manage CRUD flows. Generated `01_shopping_cart_tables.sql` and wired it into Liquibase for repeatable database setup.

**Consequences**
- Ensures referential integrity and query flexibility at the cost of additional mapping code.
- Liquibase now executes the SQL migration automatically, enabling consistent environments but requiring future schema changes to be reflected in SQL files.
- DTO mapping logic must keep child collections synchronized during updates, which increases service complexity but keeps API payloads intuitive.
