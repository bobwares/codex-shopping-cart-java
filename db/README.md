<!--
/**
 * App: Shopping Cart API
 * Package: db.documentation
 * File: README.md
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: Database migration documentation
 * Description: Explains how to execute Shopping Cart domain migrations and validate outcomes locally.
 */
-->
# Database Guide

## Domain Migration

1. Ensure PostgreSQL is running via `docker compose up -d postgres`.
2. Apply migrations with Liquibase:
   ```
   mvn liquibase:update
   ```
3. Verify schema objects:
   ```
   docker compose exec postgres psql -U "$DATABASE_USERNAME" -d "$DATABASE_NAME" -c "\dt shopping_cart.*"
   ```
4. Optionally rerun the migration script manually:
   ```
   docker compose exec postgres psql -U "$DATABASE_USERNAME" -d "$DATABASE_NAME" -f /docker-entrypoint-initdb.d/01_shopping_cart_tables.sql
   ```

### Smoke Test Query
After loading the test data script, confirm row counts:
```
docker compose exec postgres psql -U "$DATABASE_USERNAME" -d "$DATABASE_NAME" -c "SELECT COUNT(*) FROM shopping_cart.shopping_cart;"
```
