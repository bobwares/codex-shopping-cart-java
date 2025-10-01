# Shopping Cart Database Assets

## Migrations

SQL migrations live in [`db/migrations`](./migrations). Apply them in order:

```bash
psql "$DATABASE_URL" -f db/migrations/01_shopping_cart_tables.sql
```

Liquibase loads the same SQL via `src/main/resources/db/changelog` when the application starts.

## Test Data

Idempotent seed data scripts are stored in [`db/scripts`](./scripts). Execute them after the migrations:

```bash
psql "$DATABASE_URL" -f db/scripts/shopping_cart_test_data.sql
```

Each script inserts 20 shopping carts with representative items and discounts and finishes with a smoke-test count query.
