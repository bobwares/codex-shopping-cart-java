# App: Shopping Cart API
# Package: db
# File: README.md
# Version: 0.1.0
# Turns: [1]
# Author: Codex Agent <agent@local>
# Date: 2025-10-02T10:47:14Z
# Exports: Database documentation
# Description: Documents the database migrations and seed data for the
#              Shopping Cart API domain.

## Migrations

The SQL migrations produced for this turn are stored in `db/migrations` and
are designed to be executed with `psql` against a PostgreSQL 16 instance. The
scripts create a dedicated schema named `shopping_cart_app` and include
indexes plus a reporting view for cart summaries.

```bash
psql "$DATABASE_URL" -f db/migrations/01_shopping_cart_tables.sql
```

## Test Data

Sample test data for manual smoke testing lives in `db/scripts`. The script is
idempotent and can be executed multiple times thanks to `ON CONFLICT DO
NOTHING` clauses.

```bash
psql "$DATABASE_URL" -f db/scripts/shopping_cart_test_data.sql
```

After executing the script, verify that at least twenty carts exist:

```sql
SELECT COUNT(*) FROM shopping_cart_app.shopping_cart;
```
